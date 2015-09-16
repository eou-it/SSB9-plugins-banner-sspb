package net.hedtech.banner.sspb
import grails.util.Environment
import groovy.json.StringEscapeUtils
import jdk.nashorn.api.scripting.NashornScriptEngineFactory

import javax.script.Bindings
import javax.script.ScriptContext
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import javax.script.SimpleScriptContext

class ComponentTemplateEngine extends java.util.HashMap {

    static final def jsStartTag = "<%JavaScript"
    static final def jsEndTag = "%>"
    static def templates = loadTemplates()
    static def timeLoaded = new Date()
    static def timeout = 60 * 1000 //ms i.e. 1 minute
    static def nashornEngine = null



    static def loadTemplates() {
        def result = templates?templates:[:]
        def classLoader = ComponentTemplateEngine.class.classLoader
        def fileNames = classLoader.getResourceAsStream("data/componentTemplates/htmlTemplates.txt").text
        def engine = new groovy.text.GStringTemplateEngine()
        fileNames.eachLine { fileName ->
            def templateName = fileName.substring(0, fileName.lastIndexOf(".html"))
            def templateText = classLoader.getResourceAsStream("data/componentTemplates/$fileName").text.trim()
            if (templateText.startsWith(jsStartTag)) {
                if (!nashornEngine) {
                    createNashornEngine()
                }
                result."$templateName" = splitJSTemplate(templateText)
                result."$templateName".engine = nashornEngine

            } else {
                result."$templateName" = [template: engine.createTemplate(templateText)]
            }
        }
        result
    }

    static def createNashornEngine() {
        NashornScriptEngineFactory factory = new NashornScriptEngineFactory()
        nashornEngine = factory.getScriptEngine( "-scripting" )
    }

    static def splitJSTemplate (text) {
        def jsEnd = text.indexOf(jsEndTag) //only support simple non nested script
        [ javaScript: text[jsStartTag.length()+1..jsEnd-1], html: text.substring(jsEnd+jsEndTag.length()) ]
    }


    static def supports(templateName) {
        templates[templateName] != null
    }

    static def render(component) {
        // Reload the templates when not loaded or in development mode and loaded more than timeout ago
        if (!templates || (Environment.current == Environment.DEVELOPMENT && (new Date().getTime() - timeLoaded?.getTime())> timeout )) {
            templates = loadTemplates()
            timeLoaded = new Date()
            println ("Loaded Template at " +timeLoaded)
        }
        if (component.templateName && templates[component.templateName] ) {
            if (templates[component.templateName].template) {
                templates[component.templateName].template.make(component)
            } else {
                executeJavaScriptEngine(templates[component.templateName], component)
            }
        }
    }

    static def executeJavaScriptEngine(template, component) {
        def html = StringEscapeUtils.escapeJavaScript(template.html) //single line template string
        def js = """|${template.javaScript}
                    |var _renderResult = "$html";
                    |""".stripMargin()
        //println "JavaScript to execute:\n$js"
        ScriptContext scriptContext = new SimpleScriptContext()
        //Copy default context to script context
        scriptContext.setBindings(template.engine.getContext().getBindings(ScriptContext.ENGINE_SCOPE),ScriptContext.GLOBAL_SCOPE)
        def b = scriptContext.getBindings(ScriptContext.GLOBAL_SCOPE)
        // bind the component properties to the global scope of the script for simple reference
        component.each { k, v ->
            b.put(k,v)
        }
        template.engine.eval(js)
        //println b // see if we can grab the result from b or if we need to get things back from the context somehow

        return b._renderResult
    }
}
