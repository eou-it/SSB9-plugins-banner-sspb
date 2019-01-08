/******************************************************************************
 *  Copyright 2013-2018 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/
package net.hedtech.banner.sspb

import grails.converters.JSON
import grails.test.spock.IntegrationSpec
import grails.validation.ValidationException
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
        Map basePageMap = [pageName   : "stu.base",
                           source     : '''{
                                     "type": "page",
                                     "name": "student",
                                     "title": "Student Base",
                                     "scriptingLanguage": "JavaScript",
                                     "components": null
                                     }''',
                           extendsPage: extendsPage]
        def params = ['constantName': 'stu.base']

        when: "base page create"
        def result = pageService.create(basePageMap, [:])
        Page basePage = result?.page

        then: "able to create page that is not an extension"
        result.statusCode == 0
        basePage?.id != null
        basePage?.constantName == "stu.base"
        basePage?.extendsPage == null
        println "Created page ${basePage?.constantName}"

        when: "extended page create"
        Map extendedPageMap = [pageName   : "ext.1",
                               source     : '''{
                                     "type": "page",
                                     "name": "student",
                                     "title": "Student Extended",
                                     "scriptingLanguage": "JavaScript",
                                     "components":
                                      [{
                                         "name": "t1",
                                         "type": "text",
                                         "parameters": {},
                                         "validation": {},
                                         "readonly": false,
                                         "required": false,
                                         "loadInitially": true
                                      },
                                      {
                                         "name": "t2",
                                         "type": "text",
                                         "parameters": {},
                                         "validation": {},
                                         "readonly": false,
                                         "required": false,
                                         "loadInitially": true
                                      }
                                      ]
                                     }''',
                               extendsPage: new JSONObject(new JSON(basePage).toString())]
        result = pageService.create(extendedPageMap, [:])
        Page extendedPage = result?.page

        then: "able to create page that is an extension"
        result.statusCode == 0
        extendedPage?.id != null
        extendedPage?.constantName == "ext.1"
        extendedPage?.extendsPage == basePage
        println "Created page ${extendedPage?.constantName}"

        when: "page deletion"

        Map extendedPageMap1 = [pageName   : "ext.1", id : "ext.1",
                               source     : '''{
                                     "type": "page",
                                     "name": "student",
                                     "title": "Student Extended",
                                     "scriptingLanguage": "JavaScript"
                                     
                                     }'''
                               ]
        result = pageService.delete([:], extendedPageMap1)

        then: "able to delete page"
        result == null


        when: "list the page"

        def res = pageService.list(params)
        then:
        res.size() > 0

        when: "throws Exception"
        params << ['forceValidationError': 'y']
        pageService.list(params)
        then:
        final ValidationException exception = thrown()
        exception != null

        when: "count"
        res = pageService.count(params)
        then:
        res > 0

        when: "count show"
        res = pageService.show(params)
        then:
        noExceptionThrown()

        when: "count greater"
        params << ['constantName': null]
        res = pageService.count(params)
        then:
        res > 0

        when: "get page"
        String constantName = "pbadm.PageRoles"
        res = pageService.get(constantName)
        then:
        res.constantName == "pbadm.PageRoles"


    }

}
