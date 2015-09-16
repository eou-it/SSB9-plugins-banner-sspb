package net.hedtech.banner.sspb


import spock.lang.Specification

/**
 * Created by hvthor on 31-8-2015.
 */

class ComponentTemplateEngineTest extends Specification {


    void "test initialization"() {
        given:

        expect:
        ComponentTemplateEngine.templates["tab"]
        ComponentTemplateEngine.templates["tabset"]
    }

    void "test render tab"() {
        given:
        def component = [templateName: "tab", label: "testLabel", content: "Tab content"]
        def result = ComponentTemplateEngine.render(component)

        expect:
        result.toString().lastIndexOf(component.label)>0
        result.toString().lastIndexOf(component.content)>0
        println result

    }

    void "test supports"() {
        given:

        expect:
        ComponentTemplateEngine.supports("tab") == true
        ComponentTemplateEngine.supports("abcdefg") == false
        ComponentTemplateEngine.supports("") == false
    }

    void "test detail display"(){
        given:
        def detail = new PageComponent(type:PageComponent.COMP_TYPE_DETAIL,name: "detail1")
        def display = new PageComponent(type:PageComponent.COMP_TYPE_DISPLAY,name: "display1", model: "someField", label:"MyLabel")
        display.parent = detail
        display.root = detail
        detail.components = [ display]
        detail.root = detail
        def html = detail.compileComponent("")
        println html

        expect:
        html != null
    }

    void "test render component"(){
        given:
        def component=[name: "name1", model: "comp.model", content:"Passed in from test", templateName: "component"]
        def html = ComponentTemplateEngine.render(component)
        expect:
        html.indexOf("Passed in from test")>0
        html.indexOf("name1")>0
        html.indexOf("comp.model")>0

    }

}
