package net.hedtech.banner.sspb

import grails.test.mixin.TestFor
import grails.test.spock.IntegrationSpec

@TestFor(PageService)
class PageExtendedIntegrationSpec extends IntegrationSpec {

    def setup() {
    }

    def cleanup() {
    }

    def "test1"() {
        when:
        service.constructExtendedPage("badminton")
        then:
        1 == 1
    }
}
