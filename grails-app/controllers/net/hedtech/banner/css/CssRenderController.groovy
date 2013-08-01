package net.hedtech.banner.css

class CssRenderController {
    static defaultAction = "loadCss"
    def cssService


    // loading a stylesheet by name, expecting ?name=<name>
    def loadCss = {
        println "in loadCss. Params = $params"
        // find the CSS by name
        def ret = cssService.show([id:params.name])

        def css = ret?.css?ret.css : ''

        // TODO set Last-Modified, Cache-Control, Expires
        response.setContentType('text/css')
        render css
    }
}
