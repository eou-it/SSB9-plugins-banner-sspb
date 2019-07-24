/*******************************************************************************
 * Copyright 2013-2016 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
package net.hedtech.banner.sspb

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification
import spock.lang.Unroll

//Need next mixin for json converter
class PageSpec extends Specification implements DomainUnitTest<Page> {

    String baseModelView

    String extensionMergedModelView1

    String extensionMergedModelView2

    Map extensionMergedModelMap1

    Map extensionMergedModelMap2

    Page base

    Page extension1

    Page extension2


    @Unroll
    def "Test Extension 1"() {
        given:
        // do round trip diff + merge
        def mergedModelMap = extension1.getMergedModelMap()
        def diff = Page.modelToMap(extension1.modelView)
        expect:
        // validate merged models are equal (limited test) and delta has 3 entries with key A,B and I
        mergedModelMap.components.size() == extensionMergedModelMap1.components.size()
        mergedModelMap.components[0].label == "Text 1 label modified"
        mergedModelMap.components[0].validation.size() == 2
        mergedModelMap.components[2].name == "I"
        diff.deltas.size() == 4
        diff.deltas['A'] && diff.deltas['I'] && diff.deltas['B']

        mergedModelMap.name.equals("xeTest.e1")
    }


    @Unroll
    def "Test Extension 1 with Baseline upgrade swap B and C components in base"() {
        given:
        //Swap the components
        def baseModel = Page.modelToMap(base.modelView)
        def temp = baseModel.components[1]
        baseModel.components[1] = baseModel.components[2]
        baseModel.components[2] = temp
        //Update the page model
        base.modelView = Page.modelToString(baseModel)
        def mergedModelMap = extension1.getMergedModelMap()

        expect:
        // validate inserted component exists (and is after B)
        mergedModelMap.components.size() == extensionMergedModelMap1.components.size() &&
                mergedModelMap.components[0].label == "Text 1 label modified" &&
                mergedModelMap.components[0].validation.size() == 2 &&
                mergedModelMap.components[2].name == "B" &&
                mergedModelMap.components[3].name == "I"
    }


    @Unroll
    def "Test Extension 1 with Baseline upgrade remove previous sibling component B in base"() {
        given:
        //Remove component B
        def baseModel = Page.modelToMap(base.modelView)
        baseModel.components = baseModel.components.minus(baseModel.components[1])

        base.modelView = Page.modelToString(baseModel)
        def mergedModelMap = extension1.getMergedModelMap()

        expect:
        // validate inserted component I exists (and is before C)
        mergedModelMap.components.size() == 5 &&
                mergedModelMap.components[1].name == "I" &&
                mergedModelMap.components[2].name == "C"

    }

    @Unroll
    def "Test Extension 1 with Baseline upgrade removing both siblings of new component"() {
        given:
        //Remove component B
        def baseModel = Page.modelToMap(base.modelView)
        baseModel.components = baseModel.components.minus(baseModel.components[1]) // remove B
        baseModel.components = baseModel.components.minus(baseModel.components[1]) // remove C
        base.modelView = Page.modelToString(baseModel)
        def mergedModelMap = extension1.getMergedModelMap()

        expect:
        mergedModelMap.components.size() == 4
    }

    @Unroll
    def "Test Extension 1 with Baseline upgrade add maxlength to validation for component A"() {
        given:
        def baseModel = Page.modelToMap(base.modelView)
        baseModel.components[0].validation.maxlength = 3

        base.modelView = Page.modelToString(baseModel)
        def mergedModelMap = extension1.getMergedModelMap()

        expect:
        // validate maxlength
        mergedModelMap.components[0].validation.size() == 3 &&
                mergedModelMap.components[0].validation.maxlength == 3
    }


    @Unroll
    def "Test Extension 1 with Baseline upgrade remove max from validation for component A"() {
        given:
        def baseModel = Page.modelToMap(base.modelView)
        baseModel.components[0].validation.remove('max')

        base.modelView = Page.modelToString(baseModel)
        def mergedModelMap = extension1.getMergedModelMap()

        expect:
        // validate validation
        mergedModelMap.components[0].validation.size() == 1 &&
                mergedModelMap.components[0].validation.min == 10

    }


    @Unroll
    def "Test Extension 1 with Baseline upgrade supported code change in component A"() {
        given:
        def baseModel = Page.modelToMap(base.modelView)
        baseModel.components[0].onUpdate = "${ baseModel.components[0].onUpdate }\nconsole.log('added last line');"
        base.modelView = Page.modelToString(baseModel)
        def mergedModelMap = extension1.getMergedModelMap()
        expect:
        // validate code has the baseline change and the extension change
        mergedModelMap.components[0].onUpdate.contains("added last line") &&
                mergedModelMap.components[0].onUpdate.contains("hallo")
    }


    @Unroll
    def "Test Extension 1 with Baseline upgrade not supported code change in component A"() {
        given:
        def baseModel = Page.modelToMap(base.modelView)
        baseModel.components[0].onUpdate = "console.log('added first line');\n${ baseModel.components[0].onUpdate }\nconsole.log('added last line');"
        base.modelView = Page.modelToString(baseModel)
        def mergedModelMap = extension1.getMergedModelMap()
        expect:
        // validate code has the baseline changes and not the extension change
        mergedModelMap.components[0].onUpdate.contains("added first line") &&
                mergedModelMap.components[0].onUpdate.contains("added last line") &&
                !mergedModelMap.components[0].onUpdate.contains("hallo")
    }

    @Unroll
    def "Test Extension 2"() {
        given:
        def baseModel = Page.modelToMap(base.modelView)

        base.modelView = Page.modelToString(baseModel)
        def mergedModelMap = extension2.getMergedModelMap()
        expect:
        // validate size matches
        mergedModelMap.components.size() == extensionMergedModelMap2.components.size()
    }

    @Unroll
    def "Test Extension 2 with Baseline upgrade swap order A, B"() {
        given:
        def baseModel = Page.modelToMap(base.modelView)

        def temp = baseModel.components[0]
        baseModel.components[0] = baseModel.components[1]
        baseModel.components[1] = temp

        base.modelView = Page.modelToString(baseModel)

        def mergedModelMap = extension2.getMergedModelMap()
        expect:
        // validate size matches
        mergedModelMap.components.size() == extensionMergedModelMap2.components.size()
    }

    @Unroll
    def "Test Extension 2 with Baseline upgrade swap order A, C"() {
        given:
        def baseModel = Page.modelToMap(base.modelView)

        def temp = baseModel.components[0]
        baseModel.components[0] = baseModel.components[2]
        baseModel.components[2] = temp

        base.modelView = Page.modelToString(baseModel)

        def mergedModelMap = extension2.getMergedModelMap()
        expect:
        // validate size matches
        mergedModelMap.components.size() == extensionMergedModelMap2.components.size()
    }

    @Unroll
    def "Test Equals"() {
        given:
        expect:
        // validate equals method
        base.equals(baseModelView)
        base.equals(Page.modelToMap(base.modelView))
        extension1.equals(extensionMergedModelView1)
        extension2.equals(extensionMergedModelView2)
        !extension1.equals(extensionMergedModelView2)
        !base.equals(extensionMergedModelView1)
    }

    def setup() {
        setupModels()
        setupPages()
        extensionMergedModelMap1 = Page.modelToMap(extensionMergedModelView1)
        extensionMergedModelMap2 = Page.modelToMap(extensionMergedModelView2)
    }


    def setupPages() {
        base = new Page(constantName: "xeTest", modelView: baseModelView, extendsPage: null)
        extension1 = new Page([constantName: "xeTest.e1", extendsPage: base])
        extension1.modelView = extension1.diffModelViewText(extensionMergedModelView1)
        extension2 = new Page([constantName: "xeTest.e2", extendsPage: base])
        extension2.modelView = extension2.diffModelViewText(extensionMergedModelView2)
    }


    def setupModels() {
        baseModelView = """
                {
                      "type": "page",
                      "name": "xeTest",
                      "title": "",
                      "scriptingLanguage": "JavaScript",
                      "components": [
                            {
                                  "name": "A",
                                  "type": "text",
                                  "model": "",
                                  "label": "Text 1 label",
                                  "placeholder": "Text placeholder",
                                  "style": "",
                                  "parameters": {},
                                  "validation": {
                                        "max": 100
                                  },
                                  "readonly": false,
                                  "required": false,
                                  "loadInitially": true,
                                  "onUpdate": "//line1 comment\nconsole.log('ON UPDATE A value changed to: ' + \$A);\nalert('hello');\n//last line comment"
                            },
                            {
                                  "name": "B",
                                  "type": "text",
                                  "parameters": {},
                                  "validation": {},
                                  "readonly": false,
                                  "required": false,
                                  "loadInitially": true
                            },
                            {
                                  "name": "C",
                                  "type": "grid",
                                  "model": "C",
                                  "parameters": {},
                                  "loadInitially": true,
                                  "components": [
                                        {
                                              "name": "C_child_1",
                                              "type": "text",
                                              "parameters": {},
                                              "validation": {},
                                              "readonly": false,
                                              "required": false,
                                              "loadInitially": true
                                        }
                                  ],
                                  "allowNew": false,
                                  "allowModify": false,
                                  "allowDelete": false,
                                  "allowReload": false,
                                  "pageSize": 5
                            },
                            {
                                  "name": "D",
                                  "type": "text",
                                  "parameters": {},
                                  "validation": {},
                                  "readonly": false,
                                  "required": false,
                                  "loadInitially": true
                            },
                            {
                                  "name": "E",
                                  "type": "text"
                            }
                      ],
                      "style": ""
                }"""
        // Insert a new element I after B and change PageName for extension
        extensionMergedModelView1 = """
                {
                      "type": "page",
                      "name": "xeTest.e1",
                      "title": "",
                      "scriptingLanguage": "JavaScript",
                      "components": [
                            {
                                  "name": "A",
                                  "type": "text",
                                  "model": "",
                                  "label": "Text 1 label modified",
                                  "placeholder": "Text placeholder",
                                  "style": "",
                                  "parameters": {},
                                  "validation": {
                                        "max": 100,
                                        "min": 10
                                  },
                                  "readonly": false,
                                  "required": false,
                                  "loadInitially": true,
                                  "onUpdate": "//line1 comment\nconsole.log('ON UPDATE A value changed to: ' + \$A);\nalert('hallo');\n//last line comment"
                            },
                            {
                                  "name": "B",
                                  "type": "text",
                                  "parameters": {},
                                  "validation": {},
                                  "readonly": false,
                                  "required": false,
                                  "loadInitially": true
                            },
                            {
                                  "name": "I",
                                  "type": "text",
                                  "parameters": {},
                                  "validation": {},
                                  "readonly": false,
                                  "required": false,
                                  "loadInitially": true
                            },
                            {
                                  "name": "C",
                                  "type": "grid",
                                  "model": "C",
                                  "parameters": {},
                                  "loadInitially": true,
                                  "components": [
                                        {
                                              "name": "C_child_1",
                                              "type": "text",
                                              "parameters": {},
                                              "validation": {},
                                              "readonly": false,
                                              "required": false,
                                              "loadInitially": true
                                        }
                                  ],
                                  "allowNew": false,
                                  "allowModify": false,
                                  "allowDelete": false,
                                  "allowReload": false,
                                  "pageSize": 5
                            },
                            {
                                  "name": "D",
                                  "type": "text",
                                  "parameters": {},
                                  "validation": {},
                                  "readonly": false,
                                  "required": false,
                                  "loadInitially": true
                            },
                            {
                                  "name": "E",
                                  "type": "text"
                            }
                      ],
                      "style": ""
                }
                """

        // Swap C,D
        extensionMergedModelView2 = """
                 {
                       "type": "page",
                       "name": "xeTest",
                       "title": "",
                       "scriptingLanguage": "JavaScript",
                       "components": [
                             {
                                   "name": "A",
                                   "type": "text"
                             },
                             {
                                   "name": "B",
                                   "type": "text"
                             },
                             {
                                   "name": "D",
                                   "type": "text"
                             },
                             {
                                   "name": "C",
                                   "type": "grid",
                                   "components": [
                                         {
                                               "name": "C_child_1",
                                               "type": "text",
                                         }
                                   ]
                             },
                             {
                                   "name": "E",
                                   "type": "text"
                             }
                       ],
                       "style": ""
                 }
                 """

        println "initialized models"
    }

}
