package net.hedtech.banner.sspb

import grails.test.spock.IntegrationSpec
import grails.converters.JSON
import org.codehaus.groovy.grails.web.json.JSONObject

class PageServiceIntegrationSpec extends IntegrationSpec {

    def pageService

    def setup() {
    }

    def cleanup() {
    }

    void "Integration test create page and extension"() {
        given:
        org.codehaus.groovy.grails.web.json.JSONObject extendsPage = null
        Map basePageMap = [pageName: "stu.base",
                           source: '''{
                                     "type": "page",
                                     "name": "StudentBase",
                                     "title": "Student Base",
                                     "scriptingLanguage": "JavaScript",
                                     "components": null
                                     }''',
                           extendsPage:extendsPage]

        when: "base page create"
        def result = pageService.create(basePageMap,[:])
        Page basePage = result?.page

        then: "able to create page that is not an extension"
        result.statusCode == 0
        basePage?.id != null
        basePage?.constantName == "stu.base"
        basePage?.extendsPage == null
        println "Created page ${basePage?.constantName}"

        when: "extended page create"
        Map extendedPageMap = [pageName: "ext.1",
                               source: '''{
                                     "type": "page",
                                     "name": "StudentBase",
                                     "title": "Student Extended",
                                     "scriptingLanguage": "JavaScript",
                                     "components": null
                                     }''',
                               extendsPage: new JSONObject(new JSON(basePage).toString())]
        result = pageService.create(extendedPageMap,[:])
        Page extendedPage = result?.page

        then: "able to create page that is an extension"
        result.statusCode == 0
        extendedPage?.id != null
        extendedPage?.constantName == "ext.1"
        extendedPage?.extendsPage == basePage
        println "Created page ${extendedPage?.constantName}"

    }

}
