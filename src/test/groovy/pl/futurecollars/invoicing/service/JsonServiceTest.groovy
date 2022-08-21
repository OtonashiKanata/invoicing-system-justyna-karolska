package pl.futurecollars.invoicing.service

import com.fasterxml.jackson.databind.ObjectMapper
import pl.futurecollars.invoicing.TestHelpers
import spock.lang.Specification

class JsonServiceTest extends Specification {

    def "can convert object to string and string to object"() {
        given:
        def jsonService = new JsonService()
        def invoice = TestHelpers.invoice(6)

        when:
        def invoiceAsString = jsonService.objectToString(invoice)

        and:
        def invoiceFromString = jsonService.stringToObject(invoiceAsString)

        then:
        invoice == invoiceFromString
    }

    def "return exception when serializing wrong json string"() {
        given:
        def jsonService = new JsonService()
        ObjectMapper objectMapper = new ObjectMapper()
        def invoice = TestHelpers.invoice(3)
        def jsonString = objectMapper.writeValueAsString(invoice)
        jsonString = jsonString.replace(',', '.')

        when:
        jsonService.stringToObject(jsonString)

        then:
        RuntimeException exception = thrown(RuntimeException)
        exception.message == "Serialization from string to object failed"
    }


}
