package net.hedtech.banner.virtualDomain

class VirtualDomainService {

    // Services to retrieve and save a VirtualDomain

    def saveVirtualDomain(vdServiceName, vdQuery, vdPost, vdPut, vdDelete) {

        println "---------- Save $vdServiceName -------------"
        def updateVD = true
        def success = false
        def error = ""
        def vd = null

        try {
            vd=VirtualDomain.findByServiceName(vdServiceName)
            if (!vd)   {
                vd = new VirtualDomain([serviceName:vdServiceName])
                updateVD = false
            }
            if (vd) {
                vd.codeGet=vdQuery
                vd.codePost=vdPost
                vd.codePut=vdPut
                vd.codeDelete=vdDelete
                vd = vd.save(flush:true, failOnError: true)
                if (vd)
                    success = true
            }
        } catch (Exception ex) {
            error = ex.getMessage()
        }
        return [success:success, updated:updateVD, error:error, id:vd?.id, version:vd?.version]
    }

    def loadVirtualDomain(vdServiceName) {

        println "---------- load $vdServiceName -------------"
        def success = false
        def error = "$vdServiceName not found"
        def vd = null

        try {
            vd=VirtualDomain.findByServiceName(vdServiceName)
            if (vd) {
                success = true
                error = null
            }
        } catch (Exception ex) {
            error = ex.getMessage()
        }
        return [success:success, virtualDomain:vd, error:error]
    }
}
