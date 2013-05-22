class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }

        //TODO - remove TODO before release
        "/rest/todo/$id?/$other?" (controller:"TodoRest" /*, parseRequest:true */) {
            action = [GET: "getTodo", POST: "postTodo", PUT: "putTodo", DELETE: "deleteTodo"]
        }

        "/virt/$virtualDomain/$id?/$other1?/$other2?"  (controller:"VirtualDomain" /*, parseRequest:true */) {
            action = [GET: "get", POST: "save", PUT: "update", DELETE: "delete"]
        }

        /////// restful api config
        // for now (until the restful api supports regular expressions)
        //
        "/api/$pluralizedResourceName.$virtualDomain/$id"(controller:'restfulApi') {
            action = [GET: "show", PUT: "update",
                    DELETE: "delete"]
            parseRequest = false
            constraints {
                // to constrain the id to numeric, uncomment the following:
                // id matches: /\d+/
            }
        }
        "/api/$pluralizedResourceName.$virtualDomain/"(controller:'restfulApi') {
            action = [GET: "list", POST: "create"]
            parseRequest = false
        }


        //////// restful api config


        "/"(view:"/index")
        "500"(view:'/error')

    }
}