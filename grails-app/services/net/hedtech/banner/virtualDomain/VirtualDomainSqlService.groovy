/*******************************************************************************
 * Copyright 2013-2016 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
package net.hedtech.banner.virtualDomain

import groovy.sql.Sql
import groovy.transform.*
import groovy.util.logging.Log4j
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.sspb.PBUser

import java.sql.SQLException
import java.text.ParseException

@Log4j
class VirtualDomainSqlService {

    def sessionFactory    //injected by Spring
    def grailsApplication //ditto

    //allow connecting to 2 different datasources, depending on VirtualDomain.dataSource
    //to make development easy, but maybe also useful
    //def dataSource
    def dataSource_sspb

    private def getSql (vdDatasource) {
        def result

        if ( vdDatasource == "B") {
            result = new Sql(sessionFactory.getCurrentSession().connection())
        } else {
            result = new Sql(dataSource_sspb)
        }
        result
    }

    // copy params rather than changing params itself
    //changing param pluralizedResourceName made the restful api fail
    private def getNormalized (params) {
        Map result = [:]
        params.each { key, value ->
            def k=key
            def v=value
            if ( !["action","virtualDomain","controller","pluralizedResourceName"].contains(key) )  {
                if (key=="id") {
                    if (params.url_encoding != 'plain') {
                        v = urlPathDecode(value)
                    }
                } else {
                    k = k.toLowerCase()
                }
                // unmarshall null from JS - if it is undefined change to null
                // HvT: This leads to issues if user wants to pass string undefined in a parameter.
                //      Is it possible to fix this in AngularJS?
                //      An empty value seems fine. Oracle does not distinguish between null and undefined.
                if (v == "undefined")
                    v = null
                result.put(k,v)
            }
        }
        result.put('parm_cfg_admin_roles',grails.util.Holders.config.pageBuilder.adminRoles?:"")
        result
    }

    private def addUser (params) {
        def user = PBUser.get()
        user.each { k,v ->
            try {
                params.put("parm_user_" + k, v)
            }
            catch (ApplicationException e) {
                log.error "Exception adding user:", e
            }
        }
    }

    /* Check the user roles against the virtual domain roles
     */
    @Memoized
    private def userAccessRights (vd, userRoles) {
        def result=[get: false, put: false, post: false, delete: false, debug: false ]
        String debugRoles = grailsApplication.config.pageBuilder.debugRoles?grailsApplication.config.pageBuilder.debugRoles:""
        log.debug "Determining access right for ${vd.serviceName}"
        for (it in userRoles) {
            //objectName is like SELFSERVICE-ALUMNI
            //role is BAN_DEFAULT_M
            //strip SELFSERVICE-

            def r
            def i = it.objectName.indexOf("-")
            r = i>-1?it.objectName.substring(i+1):'ADMIN-'+it.objectName
            if (r) {
                vd.virtualDomainRoles.findAll { vr -> vr.roleName == r }.each {
                    result.get |= it.allowGet
                    result.put |= it.allowPut
                    result.post |= it.allowPost
                    result.delete |= it.allowDelete
                }
                result.debug |= debugRoles.indexOf(it.objectName) > -1
            }
        }
        result
    }

    /*
    Replace the default order by clause with an order by clause provided as parameters (or append clause to query)
    This closure may fail to do the right thing if an inner query contains an order by and the outer query does not
    */
    private def replaceOrderBy (statement, orderBy) {
        def result=statement
        if (orderBy && statement) {
            def orderByList=[]
            if (orderBy instanceof String)
                orderByList << orderBy
            else {
                orderByList = orderBy
            }
            def ob="order by"
            //assume order by and ) is not in comments
            def regex=/(?i:order+[ \t\n\r]by)/
            //normalize order by statement
            String norm = statement.replaceAll(regex,ob)
            //find last occurrence of order by
            def lastOB=norm.lastIndexOf(ob)
            result=(lastOB>-1)?norm.substring(0,lastOB + ob.length()):norm +" "+ ob
            //see if there is a bracket after order by
            def ket=(lastOB>-1)?norm.indexOf(")",lastOB+ob.length()):-1
            def index=0
            for (s in orderByList) {
                //replace operator and special characters to avoid sql injection
                s=(String) s.tr(" ',.<>?;:|+=!/&*(){}[]`~@#\$%\"^-", " ")
                if (s) {
                    def tokens=s.split() //split sortby on whitespace
                    if (tokens[0].toLowerCase()==tokens[0]) {
                        //if sortby column is lowercase, Oracle wants a case sensitive column
                        //so add double quotes around column
                        s="\""+tokens[0]+"\" "+tokens[1]
                    }
                    result +=  ((index>0)?",":" ") + s
                    index++
                }
            }
            if (ket>=0) {  //append part after right bracket
                result+=norm.substring(ket)
            }
        }
        result
    }

    /*
    get will return an array of objects satisfying the parameters
    refactoring to support paging/pagination:
      input max: number of rows to return
      input offset: offset within total number of rows
    refactoring pagination to restrict in the sql layer

    */
    def get(vd, params) {
        def parameters = getNormalized(params) // some tweaks and work arounds
        addUser(parameters)
        def logmsg=message(code:"sspb.virtualdomain.sqlservice.param", args:[vd.serviceName,parameters])
        def privs=userAccessRights(vd, parameters.parm_user_authorities)
        if (!privs.get) {
            throw(new org.springframework.security.access.AccessDeniedException("Deny access for ${parameters.parm_user_loginName}"))
        }
        def sql = getSql(vd.dataSource)
        def errorMessage = ""
        def statement = vd.codeGet
        //maybe remove metaData - what value?
        def metaData = { meta ->
            logmsg += message(code:"sspb.virtualdomain.sqlservice.numbercolumns", args:[meta.columnCount])
        }
        def rows
        try {
            //make sure paging params are valid numbers (and avoid sql injection and errors in oracle)
            parameters.max=parameters.max?parameters.max.toInteger():parameters.debug?5.toInteger():10000.toInteger()
            parameters.offset = parameters.offset?parameters.offset.toLong():0.toLong()
            if (parameters.sortby) {
                statement=replaceOrderBy(statement, parameters.sortby)
            }
            //modify query for sql pagination (should be fast if an index exists on columns in order by clause)
            statement="""select /*+first_rows(${parameters.max})*/ *
                         from (select rownum row_number, a.*
                               from ($statement) a
                               where rownum <= :offset+:max
                         )
                         where row_number >= :offset+1 """
            rows = sql.rows(statement,parameters,metaData)
            rows = idEncodeRows(rows)
            rows = handleClobRows(rows)
            logmsg += " "+message(code:"sspb.virtualdomain.sqlservice.numberrows", args:[rows?.size(),parameters.offset])

        } catch(e) {
            logmsg += message(code:"sspb.virtualdomain.sqlservice.error.message", args:[e.getMessage(),statement])
            errorMessage=privs.debug?logmsg :"Unable to get resources."
        } finally {
            sql.close()
        }
        log.debug logmsg

        return [error: errorMessage, rows:rows, totalCount: rows?.size()]
    }

    def count(vd, params) {
        def parameters = getNormalized(params) // some tweaks and work arounds
        addUser(parameters)
        def logmsg=message(code:"sspb.virtualdomain.sqlservice.param.count", args:[vd.serviceName,parameters])
        def privs=userAccessRights(vd, parameters.parm_user_authorities)
        if (!privs.get) {
            throw(new org.springframework.security.access.AccessDeniedException("Deny access for ${parameters.parm_user_loginName}"))
        }
        def sql = getSql(vd.dataSource)
        def errorMessage = ""
        // Add a dummy bind variable to Groovy SQL to workaround an issue related to passing a map
        // to a query without bind variables
        def statement="select count(*) COUNT from (${vd.codeGet}) where (1=1 or :x is null)"
        def rows
        def totalCount=-1
        try {
            // determine the total count
            rows = sql.rows(statement,parameters)
            totalCount = rows[0].COUNT
        } catch(e) {
            logmsg += message(code:"sspb.virtualdomain.sqlservice.error.message", args:[e.getMessage(),statement])
            errorMessage=privs.debug?logmsg : "Unable to get resource count."
        } finally {
            sql.close()
        }
        log.debug logmsg
        return [error: errorMessage, totalCount:totalCount.longValue()]
    }

    def update(vd, params, data) {
        def parameters = params
        addUser(parameters)
        data = prepareData(data, parameters)
        def privs=userAccessRights(vd, parameters.parm_user_authorities)
        if (!privs.put) {
            throw(new org.springframework.security.access.AccessDeniedException("Deny access for ${parameters.parm_user_loginName}"))
        }
        def sql
        try {
            sql = getSql(vd.dataSource)
            sql.execute(vd.codePut, data)
        }
        catch(ApplicationException e) {
            log.error "Exception in update statement", e
            throw new VirtualDomainException( privs.debug?e.getLocalizedMessage():"Unable to update resource.")
        }
        finally {
            sql?.close()
        }
        data = data.findAll { it ->
            !it.key.startsWith('parm_')
        }
        if (parameters.id) {
            data.id = parameters.id
        } else {
            data.id = urlPathEncode(data.id)
        }
        return data //should return updated object from db
    }

    def create(vd, params, data) {
        def parameters = params
        addUser(parameters)
        data = prepareData(data, parameters)
        def privs=userAccessRights(vd, parameters.parm_user_authorities)
        if (!privs.post){
            throw(new org.springframework.security.access.AccessDeniedException("Deny access for ${parameters.parm_user_loginName}"))
        }
        def sql
        try {
            sql = getSql(vd.dataSource)
            sql.execute(vd.codePost, data)
        }
        catch(SQLException e) {
            log.error "exception in create statement: $e"
            throw new VirtualDomainException( privs.debug?e.getLocalizedMessage():"Unable to create resource.")
        }
        finally {
            sql?.close()
        }
        return null //should return created object from db if changed in sql
    }

    def delete(vd, params) {
        def parameters = params
        addUser(parameters)
        def privs=userAccessRights(vd, parameters.parm_user_authorities)
        if (!privs.delete){
            throw(new org.springframework.security.access.AccessDeniedException("Deny access for ${parameters.parm_user_loginName}"))
        }
        parameters.id = urlPathDecode(parameters.id)
        def sql
        try {
            sql = getSql(vd.dataSource)
            sql.execute(vd.codeDelete, params)
        }
        catch(SQLException e) {
            log.error "exception in delete statement: $e"
            throw new VirtualDomainException( privs.debug?e.getLocalizedMessage():"Unable to delete resource.")
        }
        finally {
            sql?.close()
        }
    }

    //Support methods and closures

    private  def prepareData( Map d, Map p) {
        for (it in d) {
            // convert 1981-02-20T05:00:00+/-0000 to 1981-02-20T05:00:00+-00:00
            if ( (it.value instanceof String) && it.value.find('\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[+-]\\d{4}') )  {
                //likely a JSON date 1980-12-16T23:00:00Z
                // convert RFC 822 timezone (1981-02-20T05:00:00+/-0000) to XML date timezone  (1981-02-20T05:00:00+-00:00)
                // RFC 822 time is returned if the date retrieved from the server was not modified by the date picker
                def convertDateString = it.value.replaceAll('(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2})([+-]\\d{2})(\\d{2})', '$1$2:$3')
                try {
                    def p1 = javax.xml.bind.DatatypeConverter.parseDateTime(convertDateString)
                    it.value=new java.sql.Timestamp(p1.getTime().time)
                } catch (ParseException e) {
                    //do nothing it is not a date so it should be ok
                    log.error "Exception in parsing date", e
                }
            }  else if (  (it.value instanceof String)  && it.value.endsWith('Z')) {
                //not really needed to use a regular expression - as parseDateTime should raise exception
                // if a date is returned from datepicker it appends "Z" at the end
                try {
                    // date string is 1981-06-10T04:00:00.000Z, adjusted for GMT by browser
                    // HvT: remove this.
                        // convert to 1981-06-10T04:00:00+00:00
                        // date/time stored on server is assumed to be GMT
                        // it.value = it.value.substring(0, 19)
                        // it.value += "+00:00"
                    def date = javax.xml.bind.DatatypeConverter.parseDateTime(it.value)
                    it.value=new java.sql.Timestamp(date.getTime().time)
                    log.debug "java.sql.Timestamp value: ${it.value}  Milliseconds past 1970 ${it.value.getTime()} date milli: ${date.getTimeInMillis()}"
                } catch (ApplicationException e) {
                    //do nothing it is not a date so it should be ok
                    log.error "Exception in Date conversion:", e
                }
            }
            if (it.value.getClass().toString().endsWith("JSONObject\$Null") ) {
                it.value=""
            }
        }
        p.each { k,v ->
            if (k.startsWith('parm_user_')) {
                d[k] = v
            } else  if (k.equals('id')) {
                d['parm_'+k] = v
            } else if (k.equals('pluralizedResourceName')) {
                d['parm_resource'] = v
            }
        }
        d.id = urlPathDecode(d.id)
        return d
    }


    // For supporting urls like .../virt/stvdept/$id where the literal value of $id would contain a /  (example ROWID)
    // Use Hex encoding example is following conversion of concatenation of two ROWID's
    // AAAO/0AAHAAABpFAAA:AAAO/2AAHAAAREwAAA -> 4141414f2f304141484141414270464141413a4141414f2f32414148414141524577414141

    private def urlPathEncode  (id) {
         id.bytes.encodeAsHex()   // Hex encoding doesn't use problematic characters like / in Base64
    }

    private def urlPathDecode (id) {
        if (id) {
            new String(id.decodeHex())
        } else {
            ""
        }
    }

    private def idEncodeRows(rows) {
        for ( row in rows ) {
            if (row.containsKey("id")) {
                String idString = row.id
                idString = urlPathEncode(idString)
                log.debug "${row.id} -> $idString"
                row.id=idString
            }  else {
                return rows  // no need to traverse the whole array if first row doesn't have an id property
            }
        }
        return rows
    }

    private def handleClobRows(rows) {
        def foundClob=false
        for ( row in rows )  {
            for (col in row ) {
                if (col.value.getClass().toString().endsWith("CLOB"))  {
                    String s=getStringValue(col.value)
                    col.value=s
                    foundClob=true
                }
            }
            if (!foundClob)
                return rows // no need to traverse the whole array if first row doesn't have a CLOB
        }
        return rows
    }

    // For CLOB support. Based on code found on forum.springsource.org
    private String getStringValue(oracle.sql.CLOB c) {
        if (c != null) {
            BufferedReader reader = new BufferedReader(c.getCharacterStream())
            try {
                return reader.getText()
            } catch (IOException e) {
                throw new SQLException(message(code:"sspb.virtualdomain.sqlservice.clob.error.message", args:[e.getMessage()]))
            }
        }
        else {
            return "";
        }
    }

}
