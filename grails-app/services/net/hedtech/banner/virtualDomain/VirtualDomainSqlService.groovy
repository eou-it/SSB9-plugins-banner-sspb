package net.hedtech.banner.virtualDomain

import groovy.sql.Sql
import groovy.sql.SqlWithParams
import static javax.xml.bind.DatatypeConverter.parseDateTime
import java.sql.SQLException
import org.codehaus.groovy.grails.plugins.web.taglib.ValidationTagLib
import net.hedtech.banner.sspb.PBUser


class VirtualDomainSqlService {

    def static localizer = { mapToLocalize ->
        new ValidationTagLib().message( mapToLocalize )
    }

   def sessionFactory  //injected by Spring

    //allow connecting to 2 different datasources, depending on VirtualDomain.dataSource
    //to make development easy, but maybe also useful
    //def dataSource
    def dataSource_sspb

    private def getSql =  { ds ->
        def result

        if ( true || ds == "B") {
            result = new Sql(sessionFactory.getCurrentSession().connection())
        } else {
            result = new Sql(dataSource_sspb)
        }
        result
    }

    // copy params rather than changing params itself
    //changing param pluralizedResourceName made the restful api fail
    private def getNormalized = {  p ->
        Map result = [:]
        p.each { key, value ->
            def k=key
            def v=value
            if ( !["action","virtualDomain","controller","pluralizedResourceName"].contains(key) )  {
                if (key=="id")
                    v = urlPathDecode(value)
                else
                    k = k.toLowerCase()
                // unmarshall null from JS - if it is undefined change to null
                // HvT: This leads to issues if user wants to pass string undefined in a parameter.
                //      Is it possible to fix this in AngularJS?
                //      An empty value seems fine. Oracle does not distinguish between null and undefined.
                if (v == "undefined")
                    v = null
                result.put(k,v)
            }
        }
        result
    }

    private def addUser = { params ->
        def user = net.hedtech.banner.sspb.PBUser.get()
        user.each { k,v ->
            params.put("user_" + k, v)
        }
    }
    /* Check the user roles against the virtual domain roles
     */
    private def userAccessRights = { vd, userRoles ->
        def result=[get: false, put: false, post: false, delete: false ]
        vd.virtualDomainRoles.each{
            println 'vdRole ' + it
        }
        userRoles.each{
            //objectName is like SELFSERVICE-ALUMNI
            //role is BAN_DEFAULT_M
            //strip SELFSERVICE- this can be handled by spring security
            def r = it.objectName.substring(it.objectName.indexOf("-")+1)
            vd.virtualDomainRoles.findAll{vr -> vr.roleName==r}.each {
                result.get |= it.allowGet
                result.put |= it.allowPut
                result.post |= it.allowPost
                result.delete |= it.allowDelete
            }
        }
        result
    }


    /*
    get will return an array of objects satisfying the parameters
    refactoring to support paging/pagination:
      input max: number of rows to return
      input offset: offset within total number of rows

    */
    def get(vd, params) {
        def parameters = getNormalized(params) // some tweaks and work arounds
        addUser(parameters)

        def logmsg=localizer(code:"sspb.virtualdomain.sqlservice.param", args:[parameters])
        def userRights = userAccessRights(vd, parameters.user_roles)

        def sql = getSql(vd.dataSource)
        def debugStatement = (parameters.debug == "true")?" and rownum<6":""
        def errorMessage = ""
        // Add a dummy bind variable to Groovy SQL to workaround an issue related to passing a map
        // to a query without bind variables
        def statement = "select * from (${vd.codeGet}) where (1=1 or :x is null) $debugStatement"
        //maybe remove metaData - what value?
        def metaData = { meta ->
            logmsg += localizer(code:"sspb.virtualdomain.sqlservice.numbercolumns", args:[meta.columnCount])
        }
        def rows
        try {
            def max=parameters.max?parameters.max.toInteger():-1
            def offset=parameters.offset?parameters.offset.toInteger():-1
            rows = sql.rows(statement,parameters,offset,max,metaData)
            rows = idEncodeRows(rows)
            rows = handleClobRows(rows)
            logmsg += " "+localizer(code:"sspb.virtualdomain.sqlservice.numberrows", args:[rows?.size(),offset])

        } catch(e) {
            logmsg += localizer(code:"sspb.virtualdomain.sqlservice.error.message", args:[e.getMessage(),statement])

            errorMessage=logmsg
        } finally {
            sql.close()
        }
        println logmsg
        return [error: errorMessage, rows:rows, totalCount: rows?.size()]
    }

    def count(vd, params) {
        def parameters = getNormalized(params) // some tweaks and work arounds
        def logmsg=localizer(code:"sspb.virtualdomain.sqlservice.param", args:[parameters])
        def sql = getSql(vd.dataSource)
        def debugStatement = (parameters.debug == "true")?" and rownum<6":""
        def errorMessage = ""
        // Add a dummy bind variable to Groovy SQL to workaround an issue related to passing a map
        // to a query without bind variables
        def statement="select count(*) COUNT from (${vd.codeGet}) where (1=1 or :x is null) $debugStatement"
        def rows
        def totalCount=-1
        try {
            def max=parameters.max?parameters.max.toInteger():-1
            def offset=parameters.offset?parameters.offset.toInteger():-1
            // determine the total count
            rows = sql.rows(statement,parameters)
            totalCount = rows[0].COUNT
        } catch(e) {
            logmsg += localizer(code:"sspb.virtualdomain.sqlservice.error.message", args:[e.getMessage(),statement])
            errorMessage=logmsg
        } finally {
            sql.close()
        }
        println logmsg
        return [error: errorMessage, totalCount:totalCount]
    }

    def update(vd, params, data) {
        data = prepareData(data, params)
        def sql = getSql(vd.dataSource)
        sql.execute(vd.codePut, data)
        sql.close()
        //data
    }

    def create(vd, params, data) {
        data = prepareData(data, params)
        def sql = getSql(vd.dataSource)
        sql.execute(vd.codePost, data)
        sql.close()
    }

    def delete(vd, params) {
        def sql = getSql(vd.dataSource)
        params.id = urlPathDecode(params.id)
        sql.execute(vd.codeDelete, params)
        sql.close()
    }

    //Support methods and closures

    private  def prepareData( Map d, Map p) {
        for (it in d) {
            // convert 1981-02-20T05:00:00+0000 to 1981-02-20T05:00:00Z
            if ( (it.value instanceof String) && it.value.find('\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\+\\d{4}') )  {
                //likely a JSON date 1980-12-16T23:00:00Z
                def convertDateString = it.value.replaceAll('(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2})\\+\\d{4}', '$1Z')
                try {
                    def p1 = javax.xml.bind.DatatypeConverter.parseDateTime(convertDateString)
                    it.value=new java.sql.Date(p1.getTime().time)
                } catch (e) {
                    //do nothing it is not a date so it should be ok
                }
            }  else if (  (it.value instanceof String)  && it.value.endsWith('Z')) {
                //not really needed to use a regular expression - as parseDateTime should raise exception
                try {
                    def date = javax.xml.bind.DatatypeConverter.parseDateTime(it.value)
                    it.value=new java.sql.Date(date.getTime().time)
                } catch (e) {
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


    // For supporting urls like .../virt/stvdept/$id where the literal value of $id would contain a /  (example ROWID)
    // Use Hex encoding example is following conversion of concatenation of two ROWID's
    // AAAO/0AAHAAABpFAAA:AAAO/2AAHAAAREwAAA -> 4141414f2f304141484141414270464141413a4141414f2f32414148414141524577414141

    private def urlPathEncode = {
         it.bytes.encodeAsHex()   // Hex encoding doesn't use problematic characters like / in Base64
    }

    private def urlPathDecode = {
        if (it)
            new String( it.decodeHex())
        else
            ""
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

    private def handleClobRows = {
        def foundClob=false
        for ( row in it )  {
            for (col in row ) {
                if (col.value.getClass().toString().endsWith("CLOB"))  {
                    String s=getStringValue(col.value)
                    col.value=s
                    foundClob=true
                }
            }
            if (!foundClob)
                return it // no need to traverse the whole array if first row doesn't have a CLOB
        }
        return it
    }

    // For CLOB support. Based on code found on forum.springsource.org
    private String getStringValue(oracle.sql.CLOB c) {
        if (c != null) {
            BufferedReader reader = new BufferedReader(c.getCharacterStream())
            try {
                return reader.getText()
            } catch (IOException e) {
                throw new SQLException(localizer(code:"sspb.virtualdomain.sqlservice.clob.error.message", args:[e.getMessage()]))
            }
        }
        else {
            return "";
        }
    }

}