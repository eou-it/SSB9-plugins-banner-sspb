package net.hedtech.banner.css

class CssRenderController {
    static defaultAction = "loadCss"
    static rtlReplace = [[from:"\$start"        , to:"right"],
                         [from:"\$end"          , to:"left"],
                         [from:"/*ltr-start*/"  , to:"/*"],
                         [from:"/*ltr-end*/"    , to:"*/"],
                         [from:"/*rtl-start*//*", to:""],
                         [from:"*//*rtl-end*/"  , to:""]
                        ]
    static ltrReplace = [[from:"\$start", to:"left"], [from:"\$end", to:"right"]]

    def cssService


    // loading a stylesheet by name, expecting ?name=<name>
    def loadCss = {
        println "in loadCss. Params = $params"
        // find the CSS by name
        def ret = cssService.show([id:params.name])

        def css = ret?.css?ret.css : ''
        //println "DIRECTION: "+ message(code:"default.language.direction")

        if (message(code:"default.language.direction")=="rtl") {
            //process as documented
            rtlReplace.each {pattern ->
                css=css.replace( pattern.from, pattern.to)
            }
        } else {
            ltrReplace.each {pattern ->
                css=css.replace( pattern.from, pattern.to)
            }
        }


        // TODO set Last-Modified, Cache-Control, Expires
        //response.setContentType('text/css')
        render (text: css, contentType: "text/css", encoding: "UTF-8")
        println "rendered css"
    }
}
