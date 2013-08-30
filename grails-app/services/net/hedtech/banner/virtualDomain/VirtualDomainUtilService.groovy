package net.hedtech.banner.virtualDomain

import grails.converters.JSON

/**
 * Created with IntelliJ IDEA.
 * User: hvthor
 * Date: 26/08/13
 * Time: 17:59
 * To change this template use File | Settings | File Templates.
 */
class VirtualDomainUtilService {
    def final static loadOverwriteExisting = 0
    def final static loadSkipExisting = 1
    def final static loadRenameIfExisting = 2
    def final static loadIfNew = 3

    void importInitially(mode = loadSkipExisting) {
        def fileNames = VirtualDomainUtilService.class.classLoader.getResourceAsStream("data/install/virtualDomains.txt").text
        fileNames.eachLine { fileName ->
            def serviceName = fileName.substring(0, fileName.lastIndexOf(".json"))
            def model = VirtualDomainUtilService.class.classLoader.getResourceAsStream("data/install/$fileName").text
            saveVirtualDomain(serviceName, model, mode)
        }
    }

    void saveVirtualDomain(serviceName, model, mode = loadRenameIfExisting) {
        def msg
        def existingVD = VirtualDomain.findByServiceName(serviceName)
        if (existingVD && mode == loadSkipExisting)
            return
        else if (existingVD && mode == loadRenameIfExisting) {
            serviceName += ".imp.dup"
            def oldDup = VirtualDomain.findByServiceName(serviceName)
            if (oldDup) //if we have already saved a duplicate, get rid of it.
                oldDup.delete(flush: true)
            msg=message(code: "sspb.virtualdomainutil.import.duplicate.vd.done.message", args: [serviceName])
        } else if (existingVD && mode == loadOverwriteExisting) {
            existingVD.delete(flush: true)
            msg=message(code:"sspb.virtualdomainutil.import.vd.done.message", args:[serviceName])
        } else {
            msg=message(code:"sspb.virtualdomainutil.import.vd.done.message", args:[serviceName])
        }

        JSON.use("deep")
        def json = JSON.parse(model)
        def vd = new VirtualDomain(serviceName:serviceName)

        //default marshaling fails on nested roles so have to do 'manually'
        vd.properties[ 'typeOfCode', 'dataSource', 'codeGet', 'codePost', 'codePut', 'codeDelete'] = json
        json.virtualDomainRoles.each {
            vd.addToVirtualDomainRoles(new VirtualDomainRole(it))
        }
        vd = vd.save(flush: true)
        println msg
    }

}

