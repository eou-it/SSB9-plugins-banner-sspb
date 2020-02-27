/*******************************************************************************
 * Copyright 2013-2020 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
package net.hedtech.banner.sspb
import grails.util.Environment
import groovy.json.StringEscapeUtils
import groovy.util.logging.Slf4j
import net.hedtech.banner.exceptions.ApplicationException

import javax.script.ScriptContext
import javax.script.SimpleScriptContext
import javax.script.ScriptEngineManager
//import jdk.nashorn.api.scripting.NashornScriptEngineFactory

@Slf4j
class ComponentTemplateEngine {

    static final def resourcePath ="data/componentTemplates"
    static final def jsStartTag = "<%JavaScript"
    static final def jsEndTag = "%>"
    static final def timeout = 10 * 1000 //ms i.e. 10 s
    static def timeLoaded = new Date(0L)
    static def templates
    static def nashornEngine = null
    static def gStringEngine = null

    static def getFile(path) {
        def res
        if (Environment.current == Environment.DEVELOPMENT) { //try avoid cache
            res = ComponentTemplateEngine.class.classLoader.getResource(path).openStream()
        } else {
            res = ComponentTemplateEngine.class.classLoader.getResourceAsStream(path)
        }
        res
    }

    static def loadTemplates() {
        // Reload the templates when not loaded or in development mode and loaded more than timeout ago
        if (!templates || (Environment.current == Environment.DEVELOPMENT && (new Date().getTime() - timeLoaded?.getTime())> timeout )) {
            templates = [:]
            def fileNames = getFile("$resourcePath/htmlTemplates.txt").text
            fileNames.eachLine { fileName ->
                def templateName = fileName.substring(0, fileName.lastIndexOf(".html"))
                def templateText = getFile("$resourcePath/$fileName").text.trim()
                if (templateText.startsWith(jsStartTag)) {
                    templates."$templateName" = getJavaScriptTemplate(templateText)
                } else {
                    templates."$templateName" = getGStringTemplate(templateText)
                }
            }
            timeLoaded = new Date()
            log.debug ("*** Loaded Template at $timeLoaded ***")
        }
    }

    static def getJavaScriptTemplate(text) {
        if (!nashornEngine) {
            // Need nashornEngine with scripting option to replace variables in template
            // nashornEngine = new NashornScriptEngineFactory().getScriptEngine( "-scripting" )
            ScriptEngineManager manager = new ScriptEngineManager()
            nashornEngine = manager.getEngineByExtension("js")
            if (nashornEngine == null) {
                throw new RuntimeException("no JavaScript engine registered")
            }
        }
        def jsEnd = text.indexOf(jsEndTag) //only support simple non nested script
        [ javaScript: text[jsStartTag.length()+1..jsEnd-1], html: text.substring(jsEnd+jsEndTag.length()), engine: nashornEngine]
    }

    static def getGStringTemplate(text) {
        try {
            if (!gStringEngine) {
                gStringEngine = new groovy.text.GStringTemplateEngine()
            }
            return [template: gStringEngine.createTemplate(text)]
        } catch (e){
            throw new RuntimeException("Error in getGStringTemplate for text = $text\n$e")
        }
    }

    static def supports(templateName) {
        loadTemplates() // Make sure the templates are loaded when we need them
        templates[templateName] != null
    }

    static def render(component) {
        loadTemplates() // Make sure the templates are loaded when we need them
        if (component.templateName && templates[component.templateName] ) {
            if (templates[component.templateName].template) {
                try {
                    templates[component.templateName].template.make(component)
                } catch (e) {
                    log.error("Error rendering component ${component.name} using template ${component.templateName} \n$e")
                }
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
            b.put(k,v!=null?v:"") //put empty string rather than null
        }
        try {
            template.engine.eval(js)
        } catch(ScriptException e) {
            log.error "Error Executing JavaScript:\n $js"
            throw new ApplicationException(ComponentTemplateEngine, e)
        }
        //println b // see if we can grab the result from b or if we need to get things back from the context somehow
        return "\n"+b._renderResult.trim()+"\n"
    }
}
