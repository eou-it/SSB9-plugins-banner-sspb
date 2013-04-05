package net.hedtech.banner.virtualDomain

import groovy.sql.Sql
import static javax.xml.bind.DatatypeConverter.parseDateTime
import java.sql.SQLException
import grails.converters.*

class VirtualDomainService  {

    def sessionFactory
 /*
    VirtualDomainService() {
        println "VirtualDomainService.populate"
        populate()
    }
   */
    def get(vd, params) {
        // set parameter key name to lower case to workaround a groovy parameter replacement issue
        def modifyKeys = []
        params.each { key, value ->
            if (key != "action" && key != "virtualDomain" && key!="controller" )  {
                modifyKeys << key
            }
        }
        modifyKeys.each {
            def value = params[it]
            // unmarshall null from JS - if it is undefined change to null
            if (value=="undefined")
                value = null
            params.remove(it)
            params.put(it.toLowerCase(), value)
        }

        println ("params converted to $params")


        def sql = new Sql(sessionFactory.getCurrentSession().connection())
        def rows
        // scan for variable placeholder in SQL to determine if an parameter map should be passed to Groovy SQL
        // to workaround an issue related to passing a map to a query that takes no variable replacement
        if (vd.codeGet.find('\\:\\w+'))
            rows = sql.rows(vd.codeGet, params)
        else
            rows = sql.rows(vd.codeGet)
        sql.close()

        rows = idEncodeRows(rows)
        rows = handleClobRows(rows)


        return rows
    }

    def update(vd, params, data) {
        data = prepareData(data, params)

        def sql = new Sql(sessionFactory.getCurrentSession().connection())
        sql.execute(vd.codePost, data)
        sql.close()
    }

    def delete(vd, params) {
        def sql = new Sql(sessionFactory.getCurrentSession().connection())
        sql.execute(vd.codeDelete, params)
        sql.close()
    }

    private def prepareData( Map d, Map p) {
        for (it in d) {
            // convert 1981-02-20T05:00:00+0000 to 1981-02-20T05:00:00Z
            if ( (it.value instanceof String) && it.value.find('\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\+\\d{4}') )  {
                //likely a JSON date 1980-12-16T23:00:00Z
                def convertDateString = it.value.replaceAll('(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2})\\+\\d{4}', '$1Z')
                try {
                    println "-- datetime is $it.value"
                    def p1 = javax.xml.bind.DatatypeConverter.parseDateTime(convertDateString)
                    println "-- parseDateTime = $p1"
                    println "-- time = ${p1.getTime().time}"
                    it.value=new java.sql.Date(p1.getTime().time)
                } catch (e) {
                    println "failed date conversion $it.value"
                    //do nothing it is not a date so it should be ok
                }
            }
            if (it.value.getClass().toString().endsWith("JSONObject\$Null") ) {
                it.value=""
            }
        }
        d << p  //append parameters (should we add some prefix to parameter names to distinguish from data?)
        d.id = urlPathDecode(d.id)
        return d
    }

    /*
    // For CLOB support. Based on code found on forum.springsource.org
    private String getStringValue(oracle.sql.CLOB c) {
        if (c != null) {
            BufferedReader reader = new BufferedReader(c.getCharacterStream())
            try {
                return reader.getText()
            } catch (IOException e) {
                throw new SQLException("Unable to read CLOB: " + e.getMessage())
            }
        }
        else {
            return "";
        }
    }
    */
    private def handleClobRows = {
        /*
        for ( row in it )  {
            for (col in row ) {
                if (col.value.getClass().toString().endsWith("CLOB"))  {
                   String s=getStringValue(col.value)
                    col.value=s
                }
            }
        }
        return it
        */
    }

    private def urlPathEncode = {
         it.bytes.encodeAsHex()   // Hex encoding doesn't use problematic characters like / in Base64
    }
    private def urlPathDecode = {
        new String( it.decodeHex())
    }
    private def idEncodeRows = {
        for ( row in it ) {
            if (row.containsKey("id")) {
                String idString = row.id
                idString = urlPathEncode(idString)
                println "${row.id} -> $idString"
                row.id=idString
            }  else {
                return it  // no need to traverse the whole array if first row doesn't have an id property
            }
        }
        return it
    }


    def saveVirtualDomain(vdName, vdQuery, vdSave, vdDelete) {

        //def vdName="department"
        println "---------- Save $vdName -------------"
        def updateVD = true
        def success = false
        def error = ""
        def vd = null

        try {
            vd=VirtualDomain.findByName(vdName)
            if (!vd)   {
                vd = new VirtualDomain([name:vdName])
                updateVD = false
            }
            if (vd) {
                vd.codeGet=vdQuery
                vd.codePost=vdSave
                vd.codePut=vdSave
                vd.codeDelete=vdDelete
                vd = vd.save(flush:true, failOnError: true)
                if (vd)
                    success = true
                //println vd
            }
        } catch (Exception ex) {
            error = ex.getMessage()
        }

        return [success:success, updated:updateVD, error:error, id:vd?.id, version:vd?.version]

    }

        def loadVirtualDomain(vdName) {

        //def vdName="department"
        println "---------- load $vdName -------------"
        def success = false
        def error = "$vdName not found"
        def vd = null

        try {
            vd=VirtualDomain.findByName(vdName)
            if (vd)
                success = true
        } catch (Exception ex) {
            error = ex.getMessage()
        }

        return [success:success, vd:vd, error:error]

    }


}
