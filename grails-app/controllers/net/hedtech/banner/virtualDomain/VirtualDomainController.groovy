package net.hedtech.banner.virtualDomain

class VirtualDomainController {
    //inject the service:
    def virtualDomainSqlService
    def virtualDomainService

    private def invalidResource = { e ->
        render(status: 404, text: e)
    }

    private def serverError = { e ->
        render(status:  500, text: e)
    }

    def get = {
        def vd = virtualDomainService.loadVirtualDomain(params.virtualDomain)
        if (vd.error) {
            invalidResource
            return
        }
        def result = virtualDomainSqlService.get(vd.virtualDomain, params)
        if (result.error == "") {
            render groovy.json.JsonOutput.toJson(result.rows)  //json date  ends with +0000
            //render rows.encodeAsJSON() //json date ends with Z
        } else {
            if (params.debug == "true") //should also check user has SSPB privileges
                render result.error.replace("\n", "<br/>")
            else
                serverError
        }
    }

    def save = {
        println "Params for post/save/create:" + params
        def vd = virtualDomainService.loadVirtualDomain(params.virtualDomain)
        if (vd.error) {
            invalidResource
            return
        }
        def data =  request.JSON
        virtualDomainSqlService.create(vd.virtualDomain,params,data)
        render ""
        //Should really be querying the record again so any database trigger changes get reflected in client
        //render data.encodeAsJSON()
    }

    def update = {
        println "Params for put/update:" + params
        def vd = virtualDomainService.loadVirtualDomain(params.virtualDomain)
        if (vd.error) {
            invalidResource
            return
        }
        def data =  request.JSON
        virtualDomainSqlService.update(vd.virtualDomain,params,data)
        render ""
        //Should really be querying the record again so any database trigger changes get reflected in client
        //render data.encodeAsJSON()
    }

    def delete = {
        println "Params for DELETE:" + params
        def vd = virtualDomainService.loadVirtualDomain(params.virtualDomain)
        if (vd.error) {
            invalidResource
            return
        }
        virtualDomainSqlService.delete(vd.virtualDomain,params)
        render ""
    }

}
