package net.hedtech.banner.sspb

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.*

@TestMixin(GrailsUnitTestMixin)
@TestFor(PageService)
class MergePageModelSpec extends Specification{

    def setup() {
    }

    def cleanup() {
    }

    @Unroll
    def "Test Top level component is excluded from page"() {

        when:
        Map testData = service.loadTestData("test/testData/mergePageModel/test1Page.json",
                                            "test/testData/mergePageModel/test1Extensions.json")
        Map extendedPageModel = service.extendPageModel(testData.pageModelJSON, testData.pageExtensionsJSON)

        then:
        assertEquals extendedPageModel.name, "testPage"
        assertEquals extendedPageModel.components.size(), 0  // top level component deleted
    }

    @Unroll
    def "Test subcomponent is excluded from component"() {

        when:
        Map testData = service.loadTestData("test/testData/mergePageModel/test2Page.json",
                                            "test/testData/mergePageModel/test2Extensions.json")
        Map extendedPageModel = service.extendPageModel(testData.pageModelJSON, testData.pageExtensionsJSON)

        then:
        assertEquals extendedPageModel.name, "testPage"

        // top level component preserved
        assertEquals extendedPageModel.components.size(), 1
        assertEquals extendedPageModel.components[0].name, "componentWithSubcomponents"

        // subcomponent excluded
        assertEquals extendedPageModel.components[0].components.size(), 0
    }

    @Unroll
    def "Test component attributes are extended"() {

        when:
        Map testData = service.loadTestData("test/testData/mergePageModel/test3Page.json",
                                            "test/testData/mergePageModel/test3Extensions.json")
        Map extendedPageModel = service.extendPageModel(testData.pageModelJSON, testData.pageExtensionsJSON)

        then:
        // component preserved
        assertEquals extendedPageModel.components.size(), 1
        assertEquals extendedPageModel.components[0].name, "componentWithAttributes"

        // attributes extended
        assertEquals extendedPageModel.components[0].size(), 5                     // original number of attributes preserved
        assertFalse extendedPageModel.components[0].containsKey("attr1")           // attr1 excluded
        assertEquals extendedPageModel.components[0].attr2, "extended attribute 2" // attr2 extended
        assertEquals extendedPageModel.components[0].attr3, "new attribute 3"      // attr3 added

        // parameters extended
        assertEquals extendedPageModel.components[0].parameters.size(), 2                       // original number of parameters preserved
        assertFalse extendedPageModel.components[0].parameters.containsKey("param1")            // param1 excluded
        assertEquals extendedPageModel.components[0].parameters.param2, "extended parameter 2"  // param2 extended
        assertEquals extendedPageModel.components[0].parameters.param3, "new parameter 3"       // param3 added

        // validations extended
        assertEquals extendedPageModel.components[0].validation.size(), 2                      // original number of validations preserved
        assertFalse extendedPageModel.components[0].validation.containsKey("val1")             // val1 excluded
        assertEquals extendedPageModel.components[0].validation.val2, "extended validation 2"  // val2 extended
        assertEquals extendedPageModel.components[0].validation.val3, "new validation 3"       // val3 added
    }

    @Unroll
    def "Test component and subcomponents are added to parent component that does not yet have any subcomponents"() {

        when:
        Map testData = service.loadTestData("test/testData/mergePageModel/test4Page.json",
                                            "test/testData/mergePageModel/test4Extensions.json")
        Map extendedPageModel = service.extendPageModel(testData.pageModelJSON, testData.pageExtensionsJSON)

        then:
        assertEquals extendedPageModel.name, "testPage"

        // top level component preserved
        assertEquals extendedPageModel.components.size(), 1
        assertEquals extendedPageModel.components[0].name, "componentWithoutSubcomponents"

        // subcomponent added
        assertEquals extendedPageModel.components[0].components.size(), 1
        assertEquals extendedPageModel.components[0].components[0].name, "subcomponent"

        // attributes added
        assertEquals extendedPageModel.components[0].components[0].attr1, "attribute 1"
        assertEquals extendedPageModel.components[0].components[0].parameters.param1, "parameter 1"
        assertEquals extendedPageModel.components[0].components[0].validation.val1, "validation 1"

        // extraneous attributes not present
        assertFalse extendedPageModel.components[0].components[0].containsKey("parent")
        assertFalse extendedPageModel.components[0].components[0].containsKey("newParent")
        assertFalse extendedPageModel.components[0].components[0].containsKey("nextSibling")

        // subcomponents of subcomponent added
        assertEquals extendedPageModel.components[0].components[0].components[0].name, "subSubcomponent1"
        assertEquals extendedPageModel.components[0].components[0].components[0].attr1, "attribute 1"
        assertEquals extendedPageModel.components[0].components[0].components[0].parameters.param1, "parameter 1"
        assertEquals extendedPageModel.components[0].components[0].components[0].validation.val1, "validation 1"
        assertEquals extendedPageModel.components[0].components[0].components[1].name, "subSubcomponent2"
        assertEquals extendedPageModel.components[0].components[0].components[1].attr1, "attribute 1"
        assertEquals extendedPageModel.components[0].components[0].components[1].parameters.param1, "parameter 1"
        assertEquals extendedPageModel.components[0].components[0].components[1].validation.val1, "validation 1"
    }

    @Unroll
    def "Test component and subcomponents are added to parent component that already has subcomponents"() {

        when:
        Map testData = service.loadTestData("test/testData/mergePageModel/test5Page.json",
                                            "test/testData/mergePageModel/test5Extensions.json")
        Map extendedPageModel = service.extendPageModel(testData.pageModelJSON, testData.pageExtensionsJSON)

        then:
        assertEquals extendedPageModel.name, "testPage"

        // top level component preserved
        assertEquals extendedPageModel.components.size(), 1
        assertEquals extendedPageModel.components[0].name, "componentWithSubcomponents"

        // subcomponent added
        assertEquals extendedPageModel.components[0].components.size(), 2
        assertEquals extendedPageModel.components[0].components[0].name, "subcomponent1"
        assertEquals extendedPageModel.components[0].components[1].name, "subcomponent2"

        // attributes added
        assertEquals extendedPageModel.components[0].components[1].attr1, "attribute 1"
        assertEquals extendedPageModel.components[0].components[1].parameters.param1, "parameter 1"
        assertEquals extendedPageModel.components[0].components[1].validation.val1, "validation 1"

        // extraneous attributes not present
        assertFalse extendedPageModel.components[0].components[1].containsKey("parent")
        assertFalse extendedPageModel.components[0].components[1].containsKey("newParent")
        assertFalse extendedPageModel.components[0].components[1].containsKey("nextSibling")

        // subcomponents of subcomponent added
        assertEquals extendedPageModel.components[0].components[1].components[0].name, "subSubcomponent1"
        assertEquals extendedPageModel.components[0].components[1].components[0].attr1, "attribute 1"
        assertEquals extendedPageModel.components[0].components[1].components[0].parameters.param1, "parameter 1"
        assertEquals extendedPageModel.components[0].components[1].components[0].validation.val1, "validation 1"
        assertEquals extendedPageModel.components[0].components[1].components[1].name, "subSubcomponent2"
        assertEquals extendedPageModel.components[0].components[1].components[1].attr1, "attribute 1"
        assertEquals extendedPageModel.components[0].components[1].components[1].parameters.param1, "parameter 1"
        assertEquals extendedPageModel.components[0].components[1].components[1].validation.val1, "validation 1"
    }

    @Unroll
    def "Test component is moved to different parent"() {

        when:
        Map testData = service.loadTestData("test/testData/mergePageModel/test6Page.json",
                                            "test/testData/mergePageModel/test6Extensions.json")
        Map extendedPageModel = service.extendPageModel(testData.pageModelJSON, testData.pageExtensionsJSON)

        then:
        assertEquals extendedPageModel.name, "testPage"

        // subcomponent excluded from old parent
        assertEquals extendedPageModel.components[0].components.size(), 0

        // subcomponent added to new parent
        assertEquals extendedPageModel.components[1].components.size(), 1
        assertEquals extendedPageModel.components[1].components[0].name, "subcomponent"

        // extraneous attributes not present
        assertFalse extendedPageModel.components[1].components[0].containsKey("parent")
        assertFalse extendedPageModel.components[1].components[0].containsKey("newParent")
        assertFalse extendedPageModel.components[1].components[0].containsKey("nextSibling")
    }

    @Unroll
    def "Test reorder top level components with variant 1 of extensions"() {

        when:
        Map testData = service.loadTestData("test/testData/mergePageModel/test7Page.json",
                                            "test/testData/mergePageModel/test7Extensions.json")
        Map extendedPageModel = service.extendPageModel(testData.pageModelJSON, testData.pageExtensionsJSON)

        then:
        assertEquals extendedPageModel.name, "testPage"

        // number of components preserved
        assertEquals extendedPageModel.components.size(), 5

        // components reordered
        assertEquals extendedPageModel.components[0].name, "componentD"
        assertEquals extendedPageModel.components[1].name, "componentE"
        assertEquals extendedPageModel.components[2].name, "componentA"
        assertEquals extendedPageModel.components[3].name, "componentB"
        assertEquals extendedPageModel.components[4].name, "componentC"

        // extraneous attributes not present
        assertFalse extendedPageModel.components[0].containsKey("parent")
        assertFalse extendedPageModel.components[0].containsKey("newParent")
        assertFalse extendedPageModel.components[0].containsKey("nextSibling")
        assertFalse extendedPageModel.components[1].containsKey("parent")
        assertFalse extendedPageModel.components[1].containsKey("newParent")
        assertFalse extendedPageModel.components[1].containsKey("nextSibling")
        assertFalse extendedPageModel.components[2].containsKey("parent")
        assertFalse extendedPageModel.components[2].containsKey("newParent")
        assertFalse extendedPageModel.components[2].containsKey("nextSibling")
        assertFalse extendedPageModel.components[3].containsKey("parent")
        assertFalse extendedPageModel.components[3].containsKey("newParent")
        assertFalse extendedPageModel.components[3].containsKey("nextSibling")
        assertFalse extendedPageModel.components[4].containsKey("parent")
        assertFalse extendedPageModel.components[4].containsKey("newParent")
        assertFalse extendedPageModel.components[4].containsKey("nextSibling")
    }

    @Unroll
    def "Test reorder top level components with variant 2 of extensions"() {

        when:
        Map testData = service.loadTestData("test/testData/mergePageModel/test8Page.json",
                                            "test/testData/mergePageModel/test8Extensions.json")
        Map extendedPageModel = service.extendPageModel(testData.pageModelJSON, testData.pageExtensionsJSON)

        then:
        assertEquals extendedPageModel.name, "testPage"

        // number of components preserved
        assertEquals extendedPageModel.components.size(), 5

        // components reordered
        assertEquals extendedPageModel.components[0].name, "componentD"
        assertEquals extendedPageModel.components[1].name, "componentE"
        assertEquals extendedPageModel.components[2].name, "componentA"
        assertEquals extendedPageModel.components[3].name, "componentB"
        assertEquals extendedPageModel.components[4].name, "componentC"

        // extraneous attributes not present
        assertFalse extendedPageModel.components[0].containsKey("parent")
        assertFalse extendedPageModel.components[0].containsKey("newParent")
        assertFalse extendedPageModel.components[0].containsKey("nextSibling")
        assertFalse extendedPageModel.components[1].containsKey("parent")
        assertFalse extendedPageModel.components[1].containsKey("newParent")
        assertFalse extendedPageModel.components[1].containsKey("nextSibling")
        assertFalse extendedPageModel.components[2].containsKey("parent")
        assertFalse extendedPageModel.components[2].containsKey("newParent")
        assertFalse extendedPageModel.components[2].containsKey("nextSibling")
        assertFalse extendedPageModel.components[3].containsKey("parent")
        assertFalse extendedPageModel.components[3].containsKey("newParent")
        assertFalse extendedPageModel.components[3].containsKey("nextSibling")
        assertFalse extendedPageModel.components[4].containsKey("parent")
        assertFalse extendedPageModel.components[4].containsKey("newParent")
        assertFalse extendedPageModel.components[4].containsKey("nextSibling")
    }

    @Unroll
    def "Test reverse order of components"() {

        when:
        Map testData = service.loadTestData("test/testData/mergePageModel/test9Page.json",
                                            "test/testData/mergePageModel/test9Extensions.json")
        Map extendedPageModel = service.extendPageModel(testData.pageModelJSON, testData.pageExtensionsJSON)

        then:
        assertEquals extendedPageModel.name, "testPage"

        // number of components preserved
        assertEquals extendedPageModel.components.size(), 5

        // components reordered
        assertEquals extendedPageModel.components[0].name, "componentE"
        assertEquals extendedPageModel.components[1].name, "componentD"
        assertEquals extendedPageModel.components[2].name, "componentC"
        assertEquals extendedPageModel.components[3].name, "componentB"
        assertEquals extendedPageModel.components[4].name, "componentA"

        // extraneous attributes not present
        assertFalse extendedPageModel.components[0].containsKey("parent")
        assertFalse extendedPageModel.components[0].containsKey("newParent")
        assertFalse extendedPageModel.components[0].containsKey("nextSibling")
        assertFalse extendedPageModel.components[1].containsKey("parent")
        assertFalse extendedPageModel.components[1].containsKey("newParent")
        assertFalse extendedPageModel.components[1].containsKey("nextSibling")
        assertFalse extendedPageModel.components[2].containsKey("parent")
        assertFalse extendedPageModel.components[2].containsKey("newParent")
        assertFalse extendedPageModel.components[2].containsKey("nextSibling")
        assertFalse extendedPageModel.components[3].containsKey("parent")
        assertFalse extendedPageModel.components[3].containsKey("newParent")
        assertFalse extendedPageModel.components[3].containsKey("nextSibling")
        assertFalse extendedPageModel.components[4].containsKey("parent")
        assertFalse extendedPageModel.components[4].containsKey("newParent")
        assertFalse extendedPageModel.components[4].containsKey("nextSibling")
    }

    @Unroll
    def "Test reorder top level components and subcomponents"() {

        when:
        Map testData = service.loadTestData("test/testData/mergePageModel/test10Page.json",
                                            "test/testData/mergePageModel/test10Extensions.json")
        Map extendedPageModel = service.extendPageModel(testData.pageModelJSON, testData.pageExtensionsJSON)

        then:
        assertEquals extendedPageModel.name, "testPage"

        // number of top level components preserved
        assertEquals extendedPageModel.components.size(), 5

        // components reordered
        assertEquals extendedPageModel.components[0].name, "componentD"
        assertEquals extendedPageModel.components[1].name, "componentE"
        assertEquals extendedPageModel.components[2].name, "componentA"
        assertEquals extendedPageModel.components[3].name, "componentB"
        assertEquals extendedPageModel.components[4].name, "componentC"

        // extraneous attributes not present
        assertFalse extendedPageModel.components[0].containsKey("parent")
        assertFalse extendedPageModel.components[0].containsKey("newParent")
        assertFalse extendedPageModel.components[0].containsKey("nextSibling")
        assertFalse extendedPageModel.components[1].containsKey("parent")
        assertFalse extendedPageModel.components[1].containsKey("newParent")
        assertFalse extendedPageModel.components[1].containsKey("nextSibling")
        assertFalse extendedPageModel.components[2].containsKey("parent")
        assertFalse extendedPageModel.components[2].containsKey("newParent")
        assertFalse extendedPageModel.components[2].containsKey("nextSibling")
        assertFalse extendedPageModel.components[3].containsKey("parent")
        assertFalse extendedPageModel.components[3].containsKey("newParent")
        assertFalse extendedPageModel.components[3].containsKey("nextSibling")
        assertFalse extendedPageModel.components[4].containsKey("parent")
        assertFalse extendedPageModel.components[4].containsKey("newParent")
        assertFalse extendedPageModel.components[4].containsKey("nextSibling")

        // number of subcomponents preserved
        assertEquals extendedPageModel.components[3].components.size(), 5

        // subcomponents reordered
        assertEquals extendedPageModel.components[3].components[0].name, "componentI"
        assertEquals extendedPageModel.components[3].components[1].name, "componentJ"
        assertEquals extendedPageModel.components[3].components[2].name, "componentF"
        assertEquals extendedPageModel.components[3].components[3].name, "componentG"
        assertEquals extendedPageModel.components[3].components[4].name, "componentH"

        // extraneous attributes not present
        assertFalse extendedPageModel.components[3].components[0].containsKey("parent")
        assertFalse extendedPageModel.components[3].components[0].containsKey("newParent")
        assertFalse extendedPageModel.components[3].components[0].containsKey("nextSibling")
        assertFalse extendedPageModel.components[3].components[1].containsKey("parent")
        assertFalse extendedPageModel.components[3].components[1].containsKey("newParent")
        assertFalse extendedPageModel.components[3].components[1].containsKey("nextSibling")
        assertFalse extendedPageModel.components[3].components[2].containsKey("parent")
        assertFalse extendedPageModel.components[3].components[2].containsKey("newParent")
        assertFalse extendedPageModel.components[3].components[2].containsKey("nextSibling")
        assertFalse extendedPageModel.components[3].components[3].containsKey("parent")
        assertFalse extendedPageModel.components[3].components[3].containsKey("newParent")
        assertFalse extendedPageModel.components[3].components[3].containsKey("nextSibling")
        assertFalse extendedPageModel.components[3].components[4].containsKey("parent")
        assertFalse extendedPageModel.components[3].components[4].containsKey("newParent")
        assertFalse extendedPageModel.components[3].components[4].containsKey("nextSibling")
    }

    @Unroll
    def "Test reorder top level components while adding new component"() {

        when:
        Map testData = service.loadTestData("test/testData/mergePageModel/test11Page.json",
                                            "test/testData/mergePageModel/test11Extensions.json")
        Map extendedPageModel = service.extendPageModel(testData.pageModelJSON, testData.pageExtensionsJSON)

        then:
        assertEquals extendedPageModel.name, "testPage"

        // number of components increased by 1
        assertEquals extendedPageModel.components.size(), 6

        // components reordered
        assertEquals extendedPageModel.components[0].name, "componentD"
        assertEquals extendedPageModel.components[1].name, "componentE"
        assertEquals extendedPageModel.components[2].name, "componentF"
        assertEquals extendedPageModel.components[3].name, "componentA"
        assertEquals extendedPageModel.components[4].name, "componentB"
        assertEquals extendedPageModel.components[5].name, "componentC"

        // extraneous attributes not present
        assertFalse extendedPageModel.components[0].containsKey("parent")
        assertFalse extendedPageModel.components[0].containsKey("newParent")
        assertFalse extendedPageModel.components[0].containsKey("nextSibling")
        assertFalse extendedPageModel.components[1].containsKey("parent")
        assertFalse extendedPageModel.components[1].containsKey("newParent")
        assertFalse extendedPageModel.components[1].containsKey("nextSibling")
        assertFalse extendedPageModel.components[2].containsKey("parent")
        assertFalse extendedPageModel.components[2].containsKey("newParent")
        assertFalse extendedPageModel.components[2].containsKey("nextSibling")
        assertFalse extendedPageModel.components[3].containsKey("parent")
        assertFalse extendedPageModel.components[3].containsKey("newParent")
        assertFalse extendedPageModel.components[3].containsKey("nextSibling")
        assertFalse extendedPageModel.components[4].containsKey("parent")
        assertFalse extendedPageModel.components[4].containsKey("newParent")
        assertFalse extendedPageModel.components[4].containsKey("nextSibling")
        assertFalse extendedPageModel.components[5].containsKey("parent")
        assertFalse extendedPageModel.components[5].containsKey("newParent")
        assertFalse extendedPageModel.components[5].containsKey("nextSibling")
    }

    @Unroll
    def "Test reorder top level components and subcomponents while adding new component"() {

        when:
        Map testData = service.loadTestData("test/testData/mergePageModel/test12Page.json",
                                            "test/testData/mergePageModel/test12Extensions.json")
        Map extendedPageModel = service.extendPageModel(testData.pageModelJSON, testData.pageExtensionsJSON)

        then:
        assertEquals extendedPageModel.name, "testPage"

        // number of top level components increased by 1
        assertEquals extendedPageModel.components.size(), 6

        // components reordered
        assertEquals extendedPageModel.components[0].name, "componentD"
        assertEquals extendedPageModel.components[1].name, "componentE"
        assertEquals extendedPageModel.components[2].name, "componentK"
        assertEquals extendedPageModel.components[3].name, "componentA"
        assertEquals extendedPageModel.components[4].name, "componentB"
        assertEquals extendedPageModel.components[5].name, "componentC"

        // extraneous attributes not present
        assertFalse extendedPageModel.components[0].containsKey("parent")
        assertFalse extendedPageModel.components[0].containsKey("newParent")
        assertFalse extendedPageModel.components[0].containsKey("nextSibling")
        assertFalse extendedPageModel.components[1].containsKey("parent")
        assertFalse extendedPageModel.components[1].containsKey("newParent")
        assertFalse extendedPageModel.components[1].containsKey("nextSibling")
        assertFalse extendedPageModel.components[2].containsKey("parent")
        assertFalse extendedPageModel.components[2].containsKey("newParent")
        assertFalse extendedPageModel.components[2].containsKey("nextSibling")
        assertFalse extendedPageModel.components[3].containsKey("parent")
        assertFalse extendedPageModel.components[3].containsKey("newParent")
        assertFalse extendedPageModel.components[3].containsKey("nextSibling")
        assertFalse extendedPageModel.components[4].containsKey("parent")
        assertFalse extendedPageModel.components[4].containsKey("newParent")
        assertFalse extendedPageModel.components[4].containsKey("nextSibling")
        assertFalse extendedPageModel.components[5].containsKey("parent")
        assertFalse extendedPageModel.components[5].containsKey("newParent")
        assertFalse extendedPageModel.components[5].containsKey("nextSibling")

        // number of subcomponents increased by 1
        assertEquals extendedPageModel.components[4].components.size(), 6

        // subcomponents reordered
        assertEquals extendedPageModel.components[4].components[0].name, "componentI"
        assertEquals extendedPageModel.components[4].components[1].name, "componentJ"
        assertEquals extendedPageModel.components[4].components[2].name, "componentL"
        assertEquals extendedPageModel.components[4].components[3].name, "componentF"
        assertEquals extendedPageModel.components[4].components[4].name, "componentG"
        assertEquals extendedPageModel.components[4].components[5].name, "componentH"

        // extraneous attributes not present
        assertFalse extendedPageModel.components[4].components[0].containsKey("parent")
        assertFalse extendedPageModel.components[4].components[0].containsKey("newParent")
        assertFalse extendedPageModel.components[4].components[0].containsKey("nextSibling")
        assertFalse extendedPageModel.components[4].components[1].containsKey("parent")
        assertFalse extendedPageModel.components[4].components[1].containsKey("newParent")
        assertFalse extendedPageModel.components[4].components[1].containsKey("nextSibling")
        assertFalse extendedPageModel.components[4].components[2].containsKey("parent")
        assertFalse extendedPageModel.components[4].components[2].containsKey("newParent")
        assertFalse extendedPageModel.components[4].components[2].containsKey("nextSibling")
        assertFalse extendedPageModel.components[4].components[3].containsKey("parent")
        assertFalse extendedPageModel.components[4].components[3].containsKey("newParent")
        assertFalse extendedPageModel.components[4].components[3].containsKey("nextSibling")
        assertFalse extendedPageModel.components[4].components[4].containsKey("parent")
        assertFalse extendedPageModel.components[4].components[4].containsKey("newParent")
        assertFalse extendedPageModel.components[4].components[4].containsKey("nextSibling")
        assertFalse extendedPageModel.components[4].components[5].containsKey("parent")
        assertFalse extendedPageModel.components[4].components[5].containsKey("newParent")
        assertFalse extendedPageModel.components[4].components[5].containsKey("nextSibling")
    }

    @Unroll
    def "Test reverse order of components while adding new components to start and end"() {

        when:
        Map testData = service.loadTestData("test/testData/mergePageModel/test13Page.json",
                                            "test/testData/mergePageModel/test13Extensions.json")
        Map extendedPageModel = service.extendPageModel(testData.pageModelJSON, testData.pageExtensionsJSON)

        then:
        assertEquals extendedPageModel.name, "testPage"

        // number of components increased by 2
        assertEquals extendedPageModel.components.size(), 7

        // components reordered
        assertEquals extendedPageModel.components[0].name, "componentF"
        assertEquals extendedPageModel.components[1].name, "componentE"
        assertEquals extendedPageModel.components[2].name, "componentD"
        assertEquals extendedPageModel.components[3].name, "componentC"
        assertEquals extendedPageModel.components[4].name, "componentB"
        assertEquals extendedPageModel.components[5].name, "componentA"
        assertEquals extendedPageModel.components[6].name, "componentG"

        // extraneous attributes not present
        assertFalse extendedPageModel.components[0].containsKey("parent")
        assertFalse extendedPageModel.components[0].containsKey("newParent")
        assertFalse extendedPageModel.components[0].containsKey("nextSibling")
        assertFalse extendedPageModel.components[1].containsKey("parent")
        assertFalse extendedPageModel.components[1].containsKey("newParent")
        assertFalse extendedPageModel.components[1].containsKey("nextSibling")
        assertFalse extendedPageModel.components[2].containsKey("parent")
        assertFalse extendedPageModel.components[2].containsKey("newParent")
        assertFalse extendedPageModel.components[2].containsKey("nextSibling")
        assertFalse extendedPageModel.components[3].containsKey("parent")
        assertFalse extendedPageModel.components[3].containsKey("newParent")
        assertFalse extendedPageModel.components[3].containsKey("nextSibling")
        assertFalse extendedPageModel.components[4].containsKey("parent")
        assertFalse extendedPageModel.components[4].containsKey("newParent")
        assertFalse extendedPageModel.components[4].containsKey("nextSibling")
        assertFalse extendedPageModel.components[5].containsKey("parent")
        assertFalse extendedPageModel.components[5].containsKey("newParent")
        assertFalse extendedPageModel.components[5].containsKey("nextSibling")
        assertFalse extendedPageModel.components[6].containsKey("parent")
        assertFalse extendedPageModel.components[6].containsKey("newParent")
        assertFalse extendedPageModel.components[6].containsKey("nextSibling")
    }

    @Unroll
    def "Test reorder top level components while excluding a component"() {

        when:
        Map testData = service.loadTestData("test/testData/mergePageModel/test14Page.json",
                                            "test/testData/mergePageModel/test14Extensions.json")
        Map extendedPageModel = service.extendPageModel(testData.pageModelJSON, testData.pageExtensionsJSON)

        then:
        assertEquals extendedPageModel.name, "testPage"

        // number of components decreased by 1
        assertEquals extendedPageModel.components.size(), 5

        // components reordered
        assertEquals extendedPageModel.components[0].name, "componentD"
        assertEquals extendedPageModel.components[1].name, "componentE"
        assertEquals extendedPageModel.components[2].name, "componentA"
        assertEquals extendedPageModel.components[3].name, "componentB"
        assertEquals extendedPageModel.components[4].name, "componentC"

        // extraneous attributes not present
        assertFalse extendedPageModel.components[0].containsKey("parent")
        assertFalse extendedPageModel.components[0].containsKey("newParent")
        assertFalse extendedPageModel.components[0].containsKey("nextSibling")
        assertFalse extendedPageModel.components[1].containsKey("parent")
        assertFalse extendedPageModel.components[1].containsKey("newParent")
        assertFalse extendedPageModel.components[1].containsKey("nextSibling")
        assertFalse extendedPageModel.components[2].containsKey("parent")
        assertFalse extendedPageModel.components[2].containsKey("newParent")
        assertFalse extendedPageModel.components[2].containsKey("nextSibling")
        assertFalse extendedPageModel.components[3].containsKey("parent")
        assertFalse extendedPageModel.components[3].containsKey("newParent")
        assertFalse extendedPageModel.components[3].containsKey("nextSibling")
        assertFalse extendedPageModel.components[4].containsKey("parent")
        assertFalse extendedPageModel.components[4].containsKey("newParent")
        assertFalse extendedPageModel.components[4].containsKey("nextSibling")
    }

    @Unroll
    def "Test add multiple components and subcomponents"() {

        when:
        Map testData = service.loadTestData("test/testData/mergePageModel/test15Page.json",
                                            "test/testData/mergePageModel/test15Extensions.json")
        Map extendedPageModel = service.extendPageModel(testData.pageModelJSON, testData.pageExtensionsJSON)

        then:
        assertEquals extendedPageModel.name, "testPage"

        // number of components increased by 4
        assertEquals extendedPageModel.components.size(), 7

        // new components added correctly
        assertEquals extendedPageModel.components[0].name, "componentG"
        assertEquals extendedPageModel.components[1].name, "componentH"
        assertEquals extendedPageModel.components[2].name, "componentA"
        assertEquals extendedPageModel.components[3].name, "componentB"
        assertEquals extendedPageModel.components[4].name, "componentC"
        assertEquals extendedPageModel.components[5].name, "componentM"
        assertEquals extendedPageModel.components[6].name, "componentN"

        // extraneous attributes not present
        assertFalse extendedPageModel.components[0].containsKey("parent")
        assertFalse extendedPageModel.components[0].containsKey("newParent")
        assertFalse extendedPageModel.components[0].containsKey("nextSibling")
        assertFalse extendedPageModel.components[1].containsKey("parent")
        assertFalse extendedPageModel.components[1].containsKey("newParent")
        assertFalse extendedPageModel.components[1].containsKey("nextSibling")
        assertFalse extendedPageModel.components[2].containsKey("parent")
        assertFalse extendedPageModel.components[2].containsKey("newParent")
        assertFalse extendedPageModel.components[2].containsKey("nextSibling")
        assertFalse extendedPageModel.components[3].containsKey("parent")
        assertFalse extendedPageModel.components[3].containsKey("newParent")
        assertFalse extendedPageModel.components[3].containsKey("nextSibling")
        assertFalse extendedPageModel.components[4].containsKey("parent")
        assertFalse extendedPageModel.components[4].containsKey("newParent")
        assertFalse extendedPageModel.components[4].containsKey("nextSibling")
        assertFalse extendedPageModel.components[5].containsKey("parent")
        assertFalse extendedPageModel.components[5].containsKey("newParent")
        assertFalse extendedPageModel.components[5].containsKey("nextSibling")
        assertFalse extendedPageModel.components[6].containsKey("parent")
        assertFalse extendedPageModel.components[6].containsKey("newParent")
        assertFalse extendedPageModel.components[6].containsKey("nextSibling")

        // new subcomponents added correctly
        assertEquals extendedPageModel.components[3].components[0].name, "componentI"
        assertEquals extendedPageModel.components[3].components[1].name, "componentJ"
        assertEquals extendedPageModel.components[3].components[2].name, "componentD"
        assertEquals extendedPageModel.components[3].components[3].name, "componentE"
        assertEquals extendedPageModel.components[3].components[4].name, "componentF"
        assertEquals extendedPageModel.components[3].components[5].name, "componentK"
        assertEquals extendedPageModel.components[3].components[6].name, "componentL"

        // extraneous attributes not present
        assertFalse extendedPageModel.components[3].components[0].containsKey("parent")
        assertFalse extendedPageModel.components[3].components[0].containsKey("newParent")
        assertFalse extendedPageModel.components[3].components[0].containsKey("nextSibling")
        assertFalse extendedPageModel.components[3].components[1].containsKey("parent")
        assertFalse extendedPageModel.components[3].components[1].containsKey("newParent")
        assertFalse extendedPageModel.components[3].components[1].containsKey("nextSibling")
        assertFalse extendedPageModel.components[3].components[2].containsKey("parent")
        assertFalse extendedPageModel.components[3].components[2].containsKey("newParent")
        assertFalse extendedPageModel.components[3].components[2].containsKey("nextSibling")
        assertFalse extendedPageModel.components[3].components[3].containsKey("parent")
        assertFalse extendedPageModel.components[3].components[3].containsKey("newParent")
        assertFalse extendedPageModel.components[3].components[3].containsKey("nextSibling")
        assertFalse extendedPageModel.components[3].components[4].containsKey("parent")
        assertFalse extendedPageModel.components[3].components[4].containsKey("newParent")
        assertFalse extendedPageModel.components[3].components[4].containsKey("nextSibling")
        assertFalse extendedPageModel.components[3].components[5].containsKey("parent")
        assertFalse extendedPageModel.components[3].components[5].containsKey("newParent")
        assertFalse extendedPageModel.components[3].components[5].containsKey("nextSibling")
        assertFalse extendedPageModel.components[3].components[6].containsKey("parent")
        assertFalse extendedPageModel.components[3].components[6].containsKey("newParent")
        assertFalse extendedPageModel.components[3].components[6].containsKey("nextSibling")
    }
}
