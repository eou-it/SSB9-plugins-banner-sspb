class PBUrlMappings {

    static mappings = {

        "/" {
            controller = "customPage"
            action = "page"
            id = "pbadm.homepage"
        }

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

        "500"(view:'/error')

    }
}
