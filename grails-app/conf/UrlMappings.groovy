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

        "/"(view:"/index")
        "500"(view:'/error')

    }
}