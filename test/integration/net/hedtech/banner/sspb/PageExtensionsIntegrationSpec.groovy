package net.hedtech.banner.sspb

import grails.test.spock.IntegrationSpec

class PageExtensionsIntegrationSpec extends IntegrationSpec {

    def pageService

    def setup() {
    }

    def cleanup() {
    }

    void "Test multiple extensions are correctly applied in turn to a base page"() {
        when:
        // create and save base page
        String basePageModelView = new File("test/testData/extendedPageModel/test1BasePageModel.json").text
        Page basePage = new Page( constantName: "basePage", modelView: basePageModelView)
        basePage.save(flush:true,failOnError: true)

        // create and save extension 1
        String extension1ModelView = new File("test/testData/extendedPageModel/test1Extension1Model.json").text
        Page extension1 = new Page( constantName: "extension1", modelView: extension1ModelView, extendsPage: basePage)
        extension1.save(flush:true,failOnError: true)

        // create and save extension 2
        String extension2ModelView = new File("test/testData/extendedPageModel/test1Extension2Model.json").text
        Page extension2 = new Page( constantName: "extension2", modelView: extension2ModelView, extendsPage: extension1)
        extension2.save(flush:true,failOnError: true)

        // create and save extension 3
        String extension3ModelView = new File("test/testData/extendedPageModel/test1Extension3Model.json").text
        Page extension3 = new Page( constantName: "extension3", modelView: extension3ModelView, extendsPage: extension2)
        extension3.save(flush:true,failOnError: true)

        Map extendedPage = pageService.constructExtendedPage("extension3")


        then:

        // original attribute has been extended twice
        extendedPage.attr1 == "attribute 1 extended twice"

        // new attribute added by each extension
        extendedPage.attr2 == "attribute 2 added by extension 1"
        extendedPage.attr3 == "attribute 3 added by extension 2"

        // new component added by each extension and existing components deleted
        extendedPage.components.size() == 3
        extendedPage.components[0].name == "componentA"
        extendedPage.components[1].name == "componentB"
        extendedPage.components[2].name == "componentC"

        // subcomponents added
        extendedPage.components[0].components.size() == 3

        // subcomponents reordered
        extendedPage.components[0].components[0].name == "componentH"
        extendedPage.components[0].components[1].name == "componentG"
        extendedPage.components[0].components[2].name == "componentF"

        extension3.delete(flush:true,failOnError: true)
        extension2.delete(flush:true,failOnError: true)
        extension1.delete(flush:true,failOnError: true)
        basePage.delete(flush:true,failOnError: true)
    }
}
