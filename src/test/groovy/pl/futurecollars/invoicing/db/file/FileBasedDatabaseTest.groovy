package pl.futurecollars.invoicing.db.memory

import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.db.file.FileBasedDatabase
import pl.futurecollars.invoicing.db.file.FilesService
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.IdService
import pl.futurecollars.invoicing.service.JsonService
import java.nio.file.NoSuchFileException
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path

class FileBasedDatabaseTest extends Specification {

    Path idServiceTestingPath = Path.of("src/test/groovy/pl/futurecollars/invoicing/db/file/testingId.txt")
    Path FileBasedDatabaseTestingPath = Path.of("src/test/groovy/pl/futurecollars/invoicing/db/file/testingInvoices.json")
    FilesService filesService = new FilesService()
    JsonService jsonService = new JsonService()
    IdService idService = new IdService(idServiceTestingPath, filesService)
    Database FileBasedDatabase = new FileBasedDatabase(FileBasedDatabaseTestingPath, idService, filesService, jsonService)
    Database wrongPathDatabase = new FileBasedDatabase(Path.of("wrongPath"), idService, filesService, jsonService)
    Invoice invoice = TestHelpers.invoice(1)
    Invoice updatedInvoice = TestHelpers.updatedInvoice(2)

    def cleanup() {
        Files.write(FileBasedDatabaseTestingPath, [])
        Files.write(idServiceTestingPath, [])
    }

    def "should save invoice"() {
        when:
        def result = FileBasedDatabase.save(invoice)

        then:
        result == 1
    }

    def "get all returns empty collection if there were no invoices"() {
        expect:
        FileBasedDatabase.getAll().isEmpty()

    }

    def "should throw exception with message 'Failed to save invoice'"() {
        when:
        wrongPathDatabase.save(invoice)

        then:
        def exception = thrown(RuntimeException)
        exception.message == "Saving invoice to database failed"
        exception.cause.class == NoSuchFileException
        exception.cause.message == "wrongPath"
    }

    def "should get invoice by id"() {
        when:
        FileBasedDatabase.save(invoice)
        def result = FileBasedDatabase.getById(1)

        then:
        result.isPresent()
        result.toString().contains("Optional[Invoice(id=1, date=2022-09-03, buyer=Company(taxIdentificationNumber=1111111111, address=u200 Industrial Ave, 1 Long Beach, CA 90803, name=Stark Industries 1 Sp. z o.o), seller=Company(taxIdentificationNumber=1111111111, address=u200 Industrial Ave, 1 Long Beach, CA 90803, name=Stark Industries 1 Sp. z o.o), entries=[InvoiceEntry(description=Building Ironman 1, price=1000, vatValue=80.0, vatRate=Vat.VAT_8(rate=8))])]")
    }

    def "should throw exception with message 'Failed to get invoice with id: 1"() {
        when:
        wrongPathDatabase.getById(1)

        then:
        def exception = thrown(RuntimeException)
        exception.message == "Failed to get invoice with id: 1"
        exception.cause.class == NoSuchFileException
        exception.cause.message == "wrongPath"
    }

    def "should update invoice"() {
        given:
        FileBasedDatabase.save(invoice)

        when:
        FileBasedDatabase.update(1, updatedInvoice)
        def result = FileBasedDatabase.getById(1)

        then:
        result.isPresent()
        result.toString().contains("id=1")
        result.toString().contains("Optional[Invoice(id=1, date=2022-09-03, buyer=Company(taxIdentificationNumber=2222222222, address=u200 Industrial Ave, 2 Long Beach, CA 90803, name=Stark Industries 2 Sp. z o.o), seller=Company(taxIdentificationNumber=2222222222, address=u200 Industrial Ave, 2 Long Beach, CA 90803, name=Stark Industries 2 Sp. z o.o), entries=[InvoiceEntry(description=Building Ironman 2, price=2000, vatValue=160.0, vatRate=Vat.VAT_8(rate=8))])]")
    }

    def "should throw exception with message 'Invoice with id: 34 could not be found'"() {
        when:
        FileBasedDatabase.update(34, updatedInvoice)

        then:
        def exception = thrown(RuntimeException)
        exception.message == "Invoice with id: 34 could not be found"
    }

    def "should throw exception with message 'Updating invoice failed for id: 1'"() {
        when:
        wrongPathDatabase.update(1, updatedInvoice)

        then:
        def exception = thrown(RuntimeException)
        exception.message == "Failed to get invoice with id: 1"
    }

    def "should delete invoice"() {
        given:
        FileBasedDatabase.save(invoice)
        FileBasedDatabase.save(updatedInvoice)

        when:
        FileBasedDatabase.delete(2)

        then:
        FileBasedDatabase.getById(2) == Optional.empty()
    }

    def "should throw exception with message 'Deleting invoice failed'"() {
        when:
        wrongPathDatabase.delete(1)

        then:
        def exception = thrown(RuntimeException)
        exception.message == "Failed to get invoice with id: 1"
    }
}