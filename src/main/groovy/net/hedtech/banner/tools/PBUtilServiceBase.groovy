/******************************************************************************
 *  Copyright 2013-2019 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/

package net.hedtech.banner.tools

import grails.util.Holders
import groovy.util.logging.Log4j
import org.hibernate.HibernateException

import javax.annotation.PostConstruct
import java.text.ParseException

@Log4j
public class PBUtilServiceBase {

    def final static loadOverwriteExisting=0
    def final static loadSkipExisting=1
    def final static loadRenameExisting =2
    def final static loadIfNew=3
    def  pbConfig
    def  locationsValidated
    def jsonExt = ~/.*\.json/



    PBUtilServiceBase(){
        pbConfig= Holders.getConfig().pageBuilder
        locationsValidated = locationsValidated?:configLocations()
    }

    def static testWrite(location) {
        def existsAndWritable = true
        try {
            def f = File.createTempFile('pbTempTestFile', '.tst', new File(location))
            f.text = "x"
            f.delete()
        } catch (IOException e) {
            bootMsg "Configured location $location does not exist or is not writable"
            existsAndWritable = false
        }
        existsAndWritable
    }

    def configLocations() {
        if (locationsValidated) {
            return true
        }
        pbConfig.locations.each { name , location ->
            if (!testWrite(location)){
                //def dir = File.createTempDir(name, "")
                //dir.deleteOnExit()
                def dir = new File(System.getProperty("java.io.tmpdir")+'/pbTemp.'+name)
                if (!dir.exists()) {
                    dir.mkdir()
                }
                dir.deleteOnExit()
                pbConfig.locations[name] = dir.getPath()
                //Need to change to use a logger that works in a static context
                bootMsg "Created temporary directory ${pbConfig.locations[name]} for location $name"
            }
        }
        locationsValidated = true
    }

    static def bootMsg( msg ) {
        def tempMsg = "***** - Admin/Bootstrap: $msg "
        org.apache.commons.logging.LogFactory.getLog(this)
                .info tempMsg
    }

    def nowAsIsoInFileName() {
        new Date().format("yyyy-MM-dd_HH_mm_ss")
    }

    def safeMaxTime = { i,j ->
        if (i==null)
            i=Long.MIN_VALUE
        if (j==null)
            j=Long.MIN_VALUE
        return i<j?j:i
    }

    def saveObject = { o ->
        def savedObject = null
        try {
            savedObject = o.merge(flush:true)
        } catch (HibernateException e ) {
            log.error "Exception in saveObject", e
            return null
        }
        return savedObject
    }

    def json2date = { s ->
        Date date
        try {
            date = s?javax.xml.bind.DatatypeConverter.parseDateTime(s).time:null
        } catch (ParseException e) {
            log.error "Exception in json2date", e
        }
        date
    }

    def renameExisting = { object ->
        if  (object.hasProperty("constantName")) {
            object.constantName += "." + nowAsIsoInFileName() + ".bak"
        } else {
            object.serviceName += "." + nowAsIsoInFileName() + ".bak"
        }
        object.save()
    }

    def loadStreamMode = { stream, mode, object ->
        def doLoad
        def result= null
        switch ( mode )  {
            case loadIfNew:
                //use timeStamp from json object
                doLoad = true
                break
            case loadSkipExisting:
                doLoad = (object == null)
                break
            case loadOverwriteExisting:
                doLoad = true
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
                doLoad = (object == null) || (object.metaClass.respondsTo(object, "isEmptyInstance") && object.isEmptyInstance()) || (file.lastModified() > maxt  )
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
