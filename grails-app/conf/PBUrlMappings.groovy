class PBUrlMappings {

    static mappings = {
        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }

        "/api/$pluralizedResourceName/$id"(controller:'restfulApi') {
            action = [GET: "show", PUT: "update",
                    DELETE: "delete"]
            parseRequest = false
            constraints {
                // to constrain the id to numeric, uncomment the following:
                // id matches: /\d+/
            }
        }
        "/api/$pluralizedResourceName"(controller:'restfulApi') {
            action = [GET: "list", POST: "create"]
            parseRequest = false
        }

        //same mapping as for /api except it will have different spring security
        "/internal/$pluralizedResourceName/$id"(controller:'restfulApi') {
            action = [GET: "show", PUT: "update",
                    DELETE: "delete"]
            parseRequest = false
            constraints {
                // to constrain the id to numeric, uncomment the following:
                // id matches: /\d+/
            }
        }
        "/internal/$pluralizedResourceName"(controller:'restfulApi') {
            action = [GET: "list", POST: "create"]
            parseRequest = false
        }

        //used for display records in virtual domain composer - Todo: make sure it can't be used to bypass security
        "/virt/$virtualDomain/$id?/$other1?/$other2?"  (controller:"VirtualDomain" /*, parseRequest:true */) {
            action = [GET: "get", POST: "save", PUT: "update", DELETE: "delete"]
        }

        "/"(view:"/index")
        "500"(view:'/error')

    }
}