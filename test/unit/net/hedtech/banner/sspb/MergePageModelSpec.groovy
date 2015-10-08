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

        // number of subcomponents preserved
        assertEquals extendedPageModel.components[3].components.size(), 5

        // subcomponents reordered
        assertEquals extendedPageModel.components[3].components[0].name, "componentI"
        assertEquals extendedPageModel.components[3].components[1].name, "componentJ"
        assertEquals extendedPageModel.components[3].components[2].name, "componentF"
        assertEquals extendedPageModel.components[3].components[3].name, "componentG"
        assertEquals extendedPageModel.components[3].components[4].name, "componentH"
    }
}
