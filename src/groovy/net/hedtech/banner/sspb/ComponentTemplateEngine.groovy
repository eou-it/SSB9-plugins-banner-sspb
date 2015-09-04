package net.hedtech.banner.sspb

class ComponentTemplateEngine extends java.util.HashMap {

    static def templates = loadTemplates()

    static def loadTemplates() {
        def result = [:]
        def classLoader = ComponentTemplateEngine.class.classLoader
        def fileNames = classLoader.getResourceAsStream("data/componentTemplates/htmlTemplates.txt").text
        def engine = new groovy.text.StreamingTemplateEngine()
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
        if (component.templateName && templates[component.templateName] ) {
            templates[component.templateName].make(component)
        }
    }


}
