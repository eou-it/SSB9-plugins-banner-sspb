class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }


        "/virt/$virtualDomain/$id?/$other1?/$other2?"  (controller:"VirtualDomain" /*, parseRequest:true */) {
            action = [GET: "get", POST: "save", PUT: "update", DELETE: "delete"]
        }

        "/"(view:"/index")
        "500"(view:'/error')

    }
}