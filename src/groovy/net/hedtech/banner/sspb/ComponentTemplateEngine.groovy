package net.hedtech.banner.sspb
import grails.util.Environment

class ComponentTemplateEngine extends java.util.HashMap {

    static def templates = loadTemplates()
    static def timeLoaded = new Date()
    static def timeout = 30 * 1000 //ms i.e. 1 minute

    static def loadTemplates() {
        def result = [:]
        def classLoader = ComponentTemplateEngine.class.classLoader
        def fileNames = classLoader.getResourceAsStream("data/componentTemplates/htmlTemplates.txt").text
        def engine = new groovy.text.GStringTemplateEngine()
        fileNames.eachLine { fileName ->
            def templateName = fileName.substring(0, fileName.lastIndexOf(".html"))
            def stream = classLoader.getResourceAsStream("data/componentTemplates/$fileName")
            result."$templateName" = engine.createTemplate(stream.text)
        }
        result
    }

    static def supports(templateName) {
        templates[templateName] != null
    }

    static def render(component) {
        // Reload the templates when in development mode and loaded more than timeout ago
        if (Environment.current == Environment.DEVELOPMENT && (new Date().getTime() - timeLoaded.getTime())> timeout ) {
            templates = loadTemplates()
            timeLoaded = new Date()
            println ("Loaded Template at " +timeLoaded)
        }
        if (component.templateName && templates[component.templateName] ) {
            templates[component.templateName].make(component)
        }
    }


}
