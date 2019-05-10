/*******************************************************************************
 * Copyright 2013-2016 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

package net.hedtech.banner.sspb

import grails.converters.JSON
import grails.test.spock.IntegrationSpec

class PageUtilServiceIntegrationSpec extends IntegrationSpec {

    def pageUtilService
    def workPath = "target/testData/model"
    def sourcePath = "test/testData/model"

    def setup() {
        // Setup logic here
        new File(workPath).mkdirs()
        new File(workPath).eachFileMatch(~/.*.json.*/) { file ->
            file.delete()
        }
        new AntBuilder().copy(todir: workPath) {
            fileset(dir: sourcePath)
        }
    }


    def cleanup() {
    }


    void "Integration test import"() {

        when:
        println "Before import check that test pages do not exist"

        then:
        Page.findByConstantName("testBasePage1") == null
        Page.findByConstantName("testBasePage2") == null
        Page.findByConstantName("testExtPage1") == null
        Page.findByConstantName("testExtPage2") == null

        when:
        pageUtilService.importAllFromDir(workPath)

        then: "test extended pages created"
        Page.findByConstantName("testBasePage1") != null
        Page.findByConstantName("testBasePage2") != null
        Page.findByConstantName("testExtPage1") != null
        Page.findByConstantName("testExtPage2") != null
        Page.findByConstantName("testExtPage1").extendsPage == Page.findByConstantName("testBasePage1")
        //Same as previous but with a reverse sorting by name - hopefully resulting in at least one test with deferred loading
        Page.findByConstantName("testAExtPage1").extendsPage == Page.findByConstantName("testZBasePage1")
        Page.findByConstantName("testExtPage2").extendsPage == Page.findByConstantName("testBasePage2")

        Page.findByConstantName("testBasePage1").pageRoles?.size() == 1
        Page.findByConstantName("testZBasePage1").pageRoles?.size() == 1
        Page.findByConstantName("testExtPage1").pageRoles?.size() == 1
        Page.findByConstantName("testAExtPage1").pageRoles?.size() == 1

        println "import test complete"
    }


    void "Integration test export"() {
        when:
        pageUtilService.importAllFromDir(workPath)
        new File(workPath).eachFileMatch(~/.*.json.*/) { file ->
            file.delete()
        }

        pageUtilService.exportToFile( "testBasePage%", workPath, true)

        then: "test base pages exported"
        new File(workPath, "testBasePage1.json").exists()
        new File(workPath, "testBasePage2.json").exists()
        println "All test base pages successfully exported"

        when:
        pageUtilService.exportToFile( "test%ExtPage%", workPath, true)

        then: "test extended pages exported"
        new File(workPath, "testExtPage1.json").exists()
        new File(workPath, "testExtPage2.json").exists()
        println "All test extended pages successfully exported"

        when:
        File testBasePage1 = new File(workPath, "testBasePage1.json")
        def baseJson
        JSON.use("deep") {
            baseJson = JSON.parse(testBasePage1.text)
        }

        then: "test non extended file details exported"
        baseJson.constantName == "testBasePage1"
        baseJson.has("extendsPage")
        !baseJson.extendsPage  // extendsPage is null
        println "test base page 1 correctly exported"

        when:
        File textExt1File = new File(workPath, "testExtPage1.json")
        def ext1Json
        JSON.use("deep") {
            ext1Json = JSON.parse(textExt1File.text)
        }

        then: "test extended file details exported"
        ext1Json.constantName == "testExtPage1"
        ext1Json.has("extendsPage")
        ext1Json.extendsPage != null
        ext1Json.extendsPage.constantName == "testBasePage1"
        println "test extended page 1 correctly exported"

        when:
        File testBasePage2 = new File(workPath, "testBasePage2.json")
        def base2Json
        JSON.use("deep") {
            base2Json = JSON.parse(testBasePage2.text)
        }

        then: "test non extended file 2 details exported"
        base2Json.constantName == "testBasePage2"
        base2Json.has("extendsPage")
        !base2Json.extendsPage  // extendsPage is null
        println "test base page 2 correctly exported"

        when:
        File textExt2File = new File(workPath, "testExtPage2.json")
        def ext2Json
        JSON.use("deep") {
            ext2Json = JSON.parse(textExt2File.text)
        }

        then: "test extended file 2 details exported"
        ext2Json.constantName == "testExtPage2"
        ext2Json.has("extendsPage")
        ext2Json.extendsPage != null
        ext2Json.extendsPage.constantName == "testBasePage2"
        println "test extended page 2 correctly exported"
    }

    void "Integration test compileAll"() {
        when:
        def errors = pageUtilService.compileAll('PageModel%')

        then:
        errors.empty
    }
}
