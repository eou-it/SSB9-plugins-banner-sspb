package net.hedtech.banner.virtualDomain

import grails.converters.JSON

class VirtualDomainUtilService extends net.hedtech.banner.tools.PBUtilServiceBase {

    //Used in integration test
    void exportAllToFile(String path) {
        exportToFile( "%", path, true)
    }

    //Export one or more virtual domains to the configured directory
    void exportToFile(String serviceName, String path=pbConfig.locations.virtualDomain, Boolean skipDuplicates=false ) {
        VirtualDomain.findAllByServiceNameLike(serviceName).each { vd ->
            if (skipDuplicates && vd.serviceName.endsWith(".bak"))
                println message(code:"sspb.virtualdomain.export.skipDuplicate.message", args:[vd.serviceName])
            else {
                def file = new File("$path/${vd.serviceName}.json")
                JSON.use("deep")
                def vdStripped = new VirtualDomain()
                //nullify data that is derivable or not applicable in other environment
                vdStripped.properties[ 'serviceName', 'typeOfCode', 'dataSource', 'codeGet', 'codePost', 'codePut', 'codeDelete'] = vd.properties
                  vd.virtualDomainRoles.each { role ->
                    vdStripped.addToVirtualDomainRoles(new VirtualDomainRole( roleName:role.roleName))
                }
                def json =  new JSON(vdStripped)
                def jsonString = json.toString(true)
                println message(code:"sspb.virtualdomain.export.done.message", args:[vd.serviceName])
                file.text = jsonString
            }
        }
    }

    static Date getTimestamp(String vdName, String path=pbConfig.locations.virtualDomain ) {
        def file = new File( "$path/${vdName}.json")
        Date result
        if (file.exists())
            result =  new Date(file.lastModified() )
        result
    }

    void importInitially(mode = loadSkipExisting) {
        def fileNames = VirtualDomainUtilService.class.classLoader.getResourceAsStream("data/install/virtualDomains.txt").text
        def count=0
        bootMsg "Checking/loading system required virtual domains."
        fileNames.eachLine { fileName ->
            def serviceName = fileName.substring(0, fileName.lastIndexOf(".json"))
            def model = VirtualDomainUtilService.class.classLoader.getResourceAsStream("data/install/$fileName").text
            count+=saveVirtualDomain(serviceName, model, mode)
        }
        bootMsg "Finished checking/loading system required virtual domains. Virtual domains loaded: $count"
        importAllNewFromDir()
    }

    int saveVirtualDomain(serviceName, model, mode = loadRenameExisting) {
        def existingVD = VirtualDomain.findByServiceName(serviceName)
        def msg
        if (existingVD && mode == loadSkipExisting)
            return 0
        else if (existingVD && mode == loadRenameExisting) {
            existingVD.serviceName += "."+nowAsIsoInFileName()+".bak"
            existingVD.save(flush: true)
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
        return 1
    }

    //Import/Install Utility
    void importAllNewFromDir(String path=pbConfig.locations.virtualDomain, mode=loadIfNew) {
        bootMsg "Importing updated or new virtual domains from $path."
        def count=0
        new File(path).eachFileMatch(~/.*.json/) {   file ->
            count+=saveVirtualDomainNew(file, mode)
        }
        bootMsg "Finished importing updated or new virtual domains from $path. Virtual domains loaded: $count"
    }

    int saveVirtualDomainNew(File file, mode) {
        def vdName = file.name.substring(0, file.name.lastIndexOf(".json"))
        def doLoad = true
        def vd = VirtualDomain.findByServiceName(vdName)
        def result=0
        switch (mode) {
            case loadIfNew:
                def ft = vd?.fileTimestamp?.getTime() ? vd.fileTimestamp?.getTime() : 0
                doLoad = (vd == null) || (file.lastModified() > Math.max(ft, vd.lastUpdated.getTime()))
                break
            case loadOverwriteExisting:
                break
            case loadRenameExisting:
                if (vd) {
                    vd.serviceName += "." + nowAsIsoInFileName() + ".bak"
                    vd.save(flush: true)
                    vd = null // create a virtual domain
                }
                break
            case loadSkipExisting:
                doLoad = (vd == null)
                break
        }
        if (doLoad) {
            if (!vd) { vd = new VirtualDomain(serviceName: vdName) }
            def jsonString = file.getText()
            JSON.use("deep")
            def json = JSON.parse(jsonString)
            //default marshaling fails on nested roles so have to do 'manually'
            vd.properties[ 'typeOfCode', 'dataSource', 'codeGet', 'codePost', 'codePut', 'codeDelete'] = json
            if (!json.virtualDomainRoles.equals(null)) {  //have to use equals for JSONObject as it is not really null
                json.virtualDomainRoles.each { newRole ->
                    if ( newRole.roleName && !vd.virtualDomainRoles.find{ it.roleName == newRole.roleName } ) {
                        vd.addToVirtualDomainRoles(new VirtualDomainRole(newRole))
                    }
                }
            }
            vd.fileTimestamp = new Date(file.lastModified())
            if (!vd.save(flush: true))
                vd.errors.each {
                    println it
                }
            file.renameTo(file.getCanonicalPath() + '.' + nowAsIsoInFileName() + ".imp")
            result ++
        }
        result
    }
}