import pl.futurecollars.invoicing.App
import spock.lang.Specification

class AppTest extends Specification {
    def "dummy test to cover main"() {
        setup:
        App app = new App()

        and:
        app.main()

    }
}