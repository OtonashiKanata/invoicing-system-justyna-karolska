package pl.futurecollars.invoicing.db.memory

import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.db.file.FileBasedDatabase
import pl.futurecollars.invoicing.service.IdService


import java.nio.file.Files

class FileBasedDatabaseTest extends DatabaseTest {

    def dbPath

    @Override
    Database getDatabaseInstance() {
        def idPath = File.createTempFile('ids', '.txt').toPath()
        dbPath = File.createTempFile('invoices', '.txt').toPath()

        return new FileBasedDatabase(new File(dbPath as String), dbPath, new IdService(idPath))
    }

    def "FileBasedDatabase save invoices in correct file"() {
        given:
        def db = getDatabaseInstance()

        when:
        db.save(TestHelpers.invoice(7))

        then:
        1 == Files.readAllLines(dbPath).size()

        when:
        db.save(TestHelpers.invoice(8))

        then:
        2 == Files.readAllLines(dbPath).size()
    }

    def "should return exception when can't get invoices from database"() {

        given:
        def db = getDatabaseInstance()
        Files.deleteIfExists(dbPath)

        when:
        db.getAll()

        then:
        RuntimeException exception = thrown(RuntimeException)
        exception.message == "Getting all invoices from database failed"

    }

    def "1"() {

        given:
        def db = getDatabaseInstance()
        Files.deleteIfExists(dbPath)


        when:
        db.update(4, TestHelpers.invoice(4))

        then:
        RuntimeException exception = thrown(RuntimeException)
        exception.message == "Getting all invoices from database failed"

    }

    def "should return exception when can't delete invoices from database"() {

        given:
        def db = getDatabaseInstance()
        Files.deleteIfExists(dbPath)


        when:
        db.delete(2)

        then:
        RuntimeException exception = thrown(RuntimeException)
        exception.message == "Deleting invoice failed"
    }


}