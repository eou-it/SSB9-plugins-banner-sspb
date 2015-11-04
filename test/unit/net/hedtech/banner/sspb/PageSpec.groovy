/*******************************************************************************
 * Copyright 2013-2016 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
package net.hedtech.banner.sspb

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.web.ControllerUnitTestMixin
import spock.lang.*

//Need next mixin for json converter
@TestMixin(ControllerUnitTestMixin)
@TestFor(Page)
class PageSpec extends Specification {

    String baseModelView

    String extensionMergedModelView

    Map extensionMergedModelMap

    Page base

    Page extension


    @Unroll
    def "Test Diff and Merge"() {
        given:
        // do round trip diff + merge
        def mergedModelMap = extension.getMergedModelMap()
        def diff = Page.modelToMap(extension.modelView)
        expect:
        // validate merged models are equal (limited test) and delta has 3 entries with key A,B and I
        mergedModelMap.components.size() == extensionMergedModelMap.components.size() &&
                mergedModelMap.components[0].label == "Text 1 label modified" &&
                mergedModelMap.components[0].validation.size() == 2 &&
                mergedModelMap.components[2].name == "I" &&
                diff.deltas.size() == 3 && diff.deltas['A'] && diff.deltas['I'] && diff.deltas['B']
    }


    @Unroll
    def "Test Baseline upgrade swap B and C components in base"() {
        given:
        //Swap the components
        def baseModel = Page.modelToMap(base.modelView)
        def temp = baseModel.components[1]
        baseModel.components[1] = baseModel.components[2]
        baseModel.components[2] = temp
        //Update the page model
        base.modelView = Page.modelToString(baseModel)
        def mergedModelMap = extension.getMergedModelMap()

        expect:
        // validate inserted component exists (and is after B)
        mergedModelMap.components.size() == extensionMergedModelMap.components.size() &&
                mergedModelMap.components[0].label == "Text 1 label modified" &&
                mergedModelMap.components[0].validation.size() == 2 &&
                mergedModelMap.components[2].name == "B" &&
                mergedModelMap.components[3].name == "I"
    }


    @Unroll
    def "Test Baseline upgrade remove previous sibling component B in base"() {
        given:
        //Remove component B
        def baseModel = Page.modelToMap(base.modelView)
        baseModel.components = baseModel.components.minus(baseModel.components[1])

        base.modelView = Page.modelToString(baseModel)
        def mergedModelMap = extension.getMergedModelMap()

        expect:
        // validate inserted component I exists (and is before C)
        mergedModelMap.components.size() == 4 &&
                mergedModelMap.components[1].name == "I" &&
                mergedModelMap.components[2].name == "C"

    }


    @Unroll
    def "Test Baseline upgrade add maxlength to validation for component A"() {
        given:
        def baseModel = Page.modelToMap(base.modelView)
        baseModel.components[0].validation.maxlength = 3

        base.modelView = Page.modelToString(baseModel)
        def mergedModelMap = extension.getMergedModelMap()

        expect:
        // validate maxlength
        mergedModelMap.components[0].validation.size() == 3 &&
                mergedModelMap.components[0].validation.maxlength == 3

    }


    @Unroll
    def "Test Baseline upgrade remove max from validation for component A"() {
        given:
        def baseModel = Page.modelToMap(base.modelView)
        baseModel.components[0].validation.remove('max')

        base.modelView = Page.modelToString(baseModel)
        def mergedModelMap = extension.getMergedModelMap()

        expect:
        // validate validation
        mergedModelMap.components[0].validation.size() == 1 &&
                mergedModelMap.components[0].validation.min == 10

    }


    @Unroll
    def "Test Baseline upgrade supported code change in component A"() {
        given:
        def baseModel = Page.modelToMap(base.modelView)
        baseModel.components[0].onUpdate = "${ baseModel.components[0].onUpdate }\nconsole.log('added last line');"
        base.modelView = Page.modelToString(baseModel)
        def mergedModelMap = extension.getMergedModelMap()
        expect:
        // validate code has the baseline change and the extension change
        mergedModelMap.components[0].onUpdate.contains("added last line") &&
                mergedModelMap.components[0].onUpdate.contains("hallo")
    }


    @Unroll
    def "Test Baseline upgrade not supported code change in component A"() {
        given:
        def baseModel = Page.modelToMap(base.modelView)
        baseModel.components[0].onUpdate = "console.log('added first line');\n${ baseModel.components[0].onUpdate }\nconsole.log('added last line');"
        base.modelView = Page.modelToString(baseModel)
        def mergedModelMap = extension.getMergedModelMap()
        expect:
        // validate code has the baseline changes and not the extension change
        mergedModelMap.components[0].onUpdate.contains("added first line") &&
                mergedModelMap.components[0].onUpdate.contains("added last line") &&
                !mergedModelMap.components[0].onUpdate.contains("hallo")
    }


    def setup() {
        setupModels()
        setupPages()
        extensionMergedModelMap = Page.modelToMap(extensionMergedModelView)
    }


    def setupPages() {
        base = new Page(constantName: "xeTest", modelView: baseModelView, extendsPage: null)
        extension = new Page([constantName: "xeTest.e1", extendsPage: base])
        extension.modelView = extension.diffModelViewText(extensionMergedModelView)
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
                            }
                      ],
                      "style": ""
                }"""
        // Insert a new element I after B
        extensionMergedModelView = """
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
                            }
                      ],
                      "style": ""
                }
                """

        println "initialized models"
    }

}
