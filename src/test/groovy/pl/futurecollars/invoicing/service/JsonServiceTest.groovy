package pl.futurecollars.invoicing.service

import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

class JsonServiceTest extends Specification {

    def "can convert object to json and read it back"() {
        given:
        def jsonService = new JsonService()
        def invoice = TestHelpers.invoice(1)

        when:
        def invoiceAsString = jsonService.objectToString(invoice)

        and:
        def invoiceFromJson = jsonService.stringToObject(invoiceAsString, Invoice)

        then:
        invoice.getId() == invoiceFromJson.getId()
        invoice.getBuyer().getName() == invoiceFromJson.getBuyer().getName()
        invoice.getSeller().getAddress() == invoiceFromJson.getSeller().getAddress()
        invoice.getDate() == invoiceFromJson.getDate()
    }

}