package net.hedtech.banner.sspb


class CustomPageController {

    static defaultAction = "page"
    def groovyPagesTemplateEngine

    def page = {
        println "params = " + params
        println "actionName = " + actionName

        def pageDefaultID = 0

        def pageId = params.id?params.id:pageDefaultID
        render  (view:"page", model:[id: pageId])
    }


    private def renderGsp(String templateString, String pageName) {
        def t = groovyPagesTemplateEngine.createTemplate(templateString, "${pageName}.gsp")
        def writer = new StringWriter()
        t.make().writeTo(writer)
        return writer.toString()
    }


    def getHTML(Long id)  {

        println "In getHTML: id = $id"
        render renderGsp(new File("target/compiledPage/page${id}.html").getText(), "Page$id")
    }


}