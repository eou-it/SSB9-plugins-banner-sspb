package net.hedtech.banner.tools

class PBUtilServiceBase {
    def final static propertyDataDir = 'SSPB_DATA_DIR'
    def final static loadOverwriteExisting=0
    def final static loadSkipExisting=1
    def final static loadRenameExisting =2
    def final static loadIfNew=3

    def static pbConfig = grails.util.Holders.getConfig().pageBuilder
    def nowAsIsoInFileName = {
        new Date().format("yyyy-MM-dd_HH_mm_ss")
    }

    def nowAsIsoTime = {
        new Date().format("HH:mm:ss.SSS")
    }

    def bootMsg = { msg ->
        def tempMsg = "***** ${nowAsIsoTime()} - Bootstrap: $msg "
        //tempMsg=tempMsg.padRight(120,"*")
        println tempMsg
    }

    def safeMaxTime = { i,j ->
        if (i==null)
            i=Long.MIN_VALUE
        if (j==null)
            j=Long.MIN_VALUE
        return i<j?j:i
    }

    def saveObject = { o ->
        if (!o.save(flush:true)) {
            o.errors.each {
                println it
            }
        }
    }

    def json2date = { s ->
        Date date
        try {
            date = s?javax.xml.bind.DatatypeConverter.parseDateTime(s).time:null
        } catch (e) {
            println "Exception in json2date: $e"
        }
        date
    }

    def renameExisting = { object ->
        if  (object.hasProperty("constantName"))
            object.constantName += "."+nowAsIsoInFileName()+".bak"
        else
            object.serviceName += "."+nowAsIsoInFileName()+".bak"
        object.save()
    }

    def loadStreamMode = { stream, mode, object ->
        def doLoad
        def result= null
        switch ( mode )  {
            case loadIfNew:
                //cannot obtain file timestamp for a stream
                //do same as for next mode for now
                //use timestamp from json instead?
                //fall through
            case loadSkipExisting:
                doLoad = (object == null)
                break
            case loadOverwriteExisting:
                break
            case loadRenameExisting:
                if (object) {
                    renameExisting(object)
                    object = null // create a new page
                }
                break
        }
        if (doLoad)
            result=stream.text
        result
    }

    def loadFileMode = {file, mode, object ->
        def doLoad = true
        def result= null
        switch ( mode ) {
            case loadIfNew:
                def maxt = safeMaxTime(object?.fileTimestamp?.getTime(),object?.lastUpdated?.getTime())
                doLoad = (object == null) ||  (file.lastModified() > maxt  )
                break
            case loadOverwriteExisting:
                break
            case loadRenameExisting:
                if (object) {
                    renameExisting(object)
                    object = null // create a new page
                }
                break
            case loadSkipExisting:
                doLoad = (object == null)
                break
        }
        if (doLoad)
            result=file.text
        result
    }

}
