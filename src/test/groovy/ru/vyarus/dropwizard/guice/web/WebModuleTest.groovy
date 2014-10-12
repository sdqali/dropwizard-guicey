package ru.vyarus.dropwizard.guice.web

import io.dropwizard.testing.junit.DropwizardAppRule
import org.junit.Rule
import ru.vyarus.dropwizard.guice.AbstractTest
import ru.vyarus.dropwizard.guice.support.TestConfiguration
import ru.vyarus.dropwizard.guice.support.web.ServletsApplication

/**
 * @author Vyacheslav Rusakov 
 * @since 12.10.2014
 */
class WebModuleTest extends AbstractTest {

    @Rule
    DropwizardAppRule<TestConfiguration> RULE =
            new DropwizardAppRule<TestConfiguration>(ServletsApplication.class, 'src/test/resources/ru/vyarus/dropwizard/guice/config.yml');


    def "Check web bindings"() {

        when: "calling filter"
        def res = new URL("http://localhost:8080/dummyFilter").getText()
        then: "filter active"
        res == 'Sample filter'

        when: "calling servlet"
        res = new URL("http://localhost:8080/dummyServlet").getText()
        then: "servlet active"
        res == 'Sample servlet'
    }
}