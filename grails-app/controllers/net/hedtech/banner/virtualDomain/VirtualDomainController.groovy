package net.hedtech.banner.virtualDomain



class VirtualDomainController  {
    //inject the service:
    def virtualDomainSqlService
    def virtualDomainService

    private def invalidResource = { e ->
        render(status: 404, text: e)
    }

    private def serverError = { e ->
        render(status:  500, text: e)
    }

/*
    response headers:
    * X-hedtech-totalCount.  Returned with list responses and contains the total count of objects.
    * X-hedtech-pageOffset.  Returned with list responses.
    * X-hedtech-pageMaxSize. Returned with list responses.
    * X-hedtech-Media-Type.  Returned with all (sucess) responses, and contains the exact type of the response.
    * X-hedtech-message.  May optionally be returned with any response.  Contains a localized message for the response.
    * X-Status-Reason.  Optionally returned with a 400 response to provide additional information on why the request could not be understood.
*/
    def get = {
        def vd = virtualDomainService.loadVirtualDomain(params.virtualDomain)
        if (vd.error) {
            invalidResource
            return
        }
        def result = virtualDomainSqlService.get(vd.virtualDomain, params)




        if (result.error == "") {
            response.addHeader('X-hedtech-totalCount', result.totalCount.toString()) //need to get total count
            response.addHeader('X-hedtech-pageOffset', params.offset ? params?.offset : 0.toString())
            response.addHeader('X-hedtech-pageMaxSize', params.max ? params?.max : result.rows.size().toString())
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
