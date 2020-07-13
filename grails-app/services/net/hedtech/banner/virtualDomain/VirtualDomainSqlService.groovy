/*******************************************************************************
 Copyright 2018-2020 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.virtualDomain

import grails.gorm.transactions.Transactional
import grails.io.IOUtils
import groovy.json.JsonSlurper
import groovy.sql.Sql
import net.hedtech.banner.sspb.PBUser
import net.hedtech.banner.sspb.Page
import net.hedtech.restfulapi.AccessDeniedException
import oracle.sql.BLOB
import sun.misc.BASE64Encoder

import java.sql.Blob
import java.sql.SQLException

@Transactional
class VirtualDomainSqlService {

    def sessionFactory    //injected by Spring
    def grailsApplication //ditto

    private def getSql () {
        def result = new Sql(sessionFactory.getCurrentSession().connection())
        result
    }

    // copy params rather than changing params itself
    //changing param pluralizedResourceName made the restful api fail
    private def getNormalized (params) {
        Map result = [:]
        params.each { key, value ->
            def k=key
            def v=value
            if ( !["action","virtualDomain","controller","pluralizedResourceName"].contains(key) && isValidName(key) )  {
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
            catch (RuntimeException e) {
                log.error "Exception adding user:", e
            }
        }
    }

    private def addParams(params){
        def paramData = params.item
        def parser = new JsonSlurper()
        def json = parser.parseText(paramData)
        json.each{k,v ->
            try {
                params.put(k, v)
            }
            catch (RuntimeException e) {
                log.error "Exception adding params:", e
            }
        }
    }
    /* Check the user roles against the virtual domain roles
     */
   // @Memoized
    private def userAccessRights (vd, userRoles) {
        def result=[get: false, put: false, post: false, delete: false, debug: false ]
        String debugRoles = grailsApplication.config.pageBuilder.debugRoles?:""
        log.debug "Determining access right for ${vd.serviceName}"
        //        def adminRolesObjects = userRoles.findAll {!it.objectName.startsWith('SELFSERVICE')}
//        adminRolesObjects.each {
//            grailsApplication.config.formControllerMap.put(vd.serviceName, it.objectName)
//        }
        for (it in userRoles) {
            //objectName is like SELFSERVICE-ALUMNI
            //role is BAN_DEFAULT_M
            //strip SELFSERVICE-
            def adminDBRole = it.roleName
            def r
            def i = it.objectName.indexOf("-")
            r = i>-1?it.objectName.substring(i+1):'ADMIN-'+it.objectName
            if (r) {
                vd.virtualDomainRoles.findAll { vr -> vr.roleName == r }.each {
                    if(adminDBRole == "BAN_DEFAULT_Q") {
                        result.get |= it.allowGet
                    } else {
                        result.get |= it.allowGet
                        result.put |= it.allowPut
                        result.post |= it.allowPost
                        result.delete |= it.allowDelete
                    }
                }
                result.debug |= debugRoles.indexOf("ROLE_${it.objectName}_${it.roleName}") > -1
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
            if (orderBy instanceof String && orderBy?.contains(',')) {
                orderByList = orderBy.split(',')
            } else if (orderBy instanceof String) {
                orderByList << orderBy
            } else {
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
                s=(String) s.tr(" ',.<>?;:|+=!/&*(){}[]`~@#\$%\"^-\n\t\r", " ")
                if (s) {
                    def tokens=s.split() //split sortby on whitespace
                    if ( !(tokens.size() == 2 && isValidName(tokens[0],30) && ['asc','desc'].contains(tokens[1])) ) {
                        throw new RuntimeException(message(code:"sspb.virtualdomain.sqlservice.sortby.message", args:[]))
                    }
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

    private def cleanSqlExceptionMessage(e, debug) {
        def userMessageAnnotation = "@USERMESSAGE:"
        def oraErrorString = "ORA-"
        def errorMessage = e.getLocalizedMessage()
        if (!debug) {
            if (errorMessage.contains(userMessageAnnotation)) {
                errorMessage = errorMessage.substring(errorMessage.indexOf(userMessageAnnotation) + userMessageAnnotation.length())
                def oraPos = errorMessage.indexOf(oraErrorString)
                if (oraPos>=0) {
                    errorMessage=errorMessage.substring(0,oraPos)
                }
            } else {
                errorMessage = ""
            }
        }
        errorMessage
    }

    /*
    get will return an array of objects satisfying the parameters
    refactoring to support paging/pagination:
      input max: number of rows to return
      input offset: offset within total number of rows
    refactoring pagination to restrict in the sql layer

    */
    @Transactional(readOnly = true)
    def get(vd, params) {
        def parameters = getNormalized(params) // some tweaks and work arounds
        addUser(parameters)
        def logmsg=message(code:"sspb.virtualdomain.sqlservice.param", args:[vd.serviceName,parameters])
        def privs=userAccessRights(vd, parameters.parm_user_authorities)
        if (!privs.get) {
            throw new AccessDeniedException("user.not.authorized.get",["${parameters.parm_user_loginName} "]);
            //throw(new org.springframework.security.access.AccessDeniedException("Deny access for ${parameters.parm_user_loginName}"))
        }
        def sql = getSql()
        def errorMessage = ""
        def regEx = /[^a-z0-9A-Z_'",#@:<>.()%!&*]+;*$/
        def statement = vd.codeGet?.replaceAll(regEx,"")
        //maybe remove metaData - what value?
        def metaData = { meta ->
            logmsg += message(code:"sspb.virtualdomain.sqlservice.numbercolumns", args:[meta.columnCount])
        }
        def rows
        if (parameters.sortby) {
            statement=replaceOrderBy(statement, parameters.sortby)
        }
        try {
            //make sure paging params are valid numbers (and avoid sql injection and errors in oracle)
            parameters.max=parameters.max?parameters.max.toInteger():parameters.debug?5.toInteger():10000.toInteger()
            parameters.offset = parameters.offset?parameters.offset.toLong():0.toLong()

            //modify query for sql pagination (should be fast if an index exists on columns in order by clause)
            statement="""select /*+first_rows(${parameters.max})*/ *
                         from (select rownum row_number, a.*
                               from ($statement) a)
                               where row_number  between :offset+1 and :offset+:max
                       """
            if(vd.serviceName == 'pbadmWebTailorRoles') {
                if(parameters.page_id) {
                    def pageInstance = Page.findById(parameters.page_id)
                    def pageModel = pageInstance.getMergedModelMap()
                    if (pageModel.objectName) {
                        parameters.put("object_name", pageModel.objectName)
                    }
                }
            }
            rows = sql.rows(statement,parameters,metaData)
            rows = idEncodeRows(rows)
            rows = handleClobBlobTimestampRows(rows)
            logmsg += " "+message(code:"sspb.virtualdomain.sqlservice.numberrows", args:[rows?.size(),parameters.offset])

        } catch(SQLException e) {
            logmsg += message(code:"sspb.virtualdomain.sqlservice.error.message", args:[e.getMessage(),statement])
            def oracleErrorMsg = message(code:"sspb.virtualdomain.sqlservice.error.message", args:[e.getMessage(),statement])
            errorMessage = privs.debug ? oracleErrorMsg : cleanSqlExceptionMessage(e,privs.debug) ?: "Unable to get resources."
        } catch(NumberFormatException e) {
            logmsg += e.getLocalizedMessage()
            errorMessage=message(code:"sspb.virtualdomain.sqlservice.paging.message", args:[])
        }
        finally {
            //sql.close()
        }
        log.debug logmsg

        return [error: errorMessage, rows:rows, totalCount: rows?.size(), debug: parameters.debug]
    }

    @Transactional(readOnly = true)
    def count(vd, params) {
        def parameters = getNormalized(params) // some tweaks and work arounds
        addUser(parameters)
        def logmsg=message(code:"sspb.virtualdomain.sqlservice.param.count", args:[vd.serviceName,parameters])
        def privs=userAccessRights(vd, parameters.parm_user_authorities)
        if (!privs.get) {
            throw new AccessDeniedException("user.not.authorized.get",["${parameters.parm_user_loginName} "])
            //throw(new org.springframework.security.access.AccessDeniedException("Deny access for ${parameters.parm_user_loginName}"))
        }
        def sql = getSql()
        def errorMessage = ""
        // Add a dummy bind variable to Groovy SQL to workaround an issue related to passing a map
        // to a query without bind variables
        def regEx = /[^a-z0-9A-Z_'",#@:<>.()%!&*]+;*$/
        def statement="select count(*) COUNT from (${vd.codeGet?.replaceAll(regEx,"")}) where (1=1 or :x is null)"
        def rows
        def totalCount=-1
        try {
            // determine the total count
            rows = sql.rows(statement,parameters)
            totalCount = rows[0].COUNT
        } catch(SQLException e) {
            logmsg += message(code:"sspb.virtualdomain.sqlservice.error.message", args:[e.getMessage(),statement])
            errorMessage=privs.debug?logmsg : cleanSqlExceptionMessage(e,privs.debug) ?: "Unable to get resource count."
        } finally {
            //sql.close()
        }
        log.debug logmsg
        return [error: errorMessage, totalCount:totalCount.longValue(),debug: parameters.debug]
    }

    def update(vd, params, data) {
        def parameters = params
        addUser(parameters)
        data = prepareData(data, parameters)
        def privs=userAccessRights(vd, parameters.parm_user_authorities)
        if (!privs.put) {
            throw new AccessDeniedException("user.not.authorized.update",["${parameters.parm_user_loginName} "])
            //throw(new org.springframework.security.access.AccessDeniedException("Deny access for ${parameters.parm_user_loginName}"))
        }
        def sql
        try {
            sql = getSql()
            sql.execute(vd.codePut, data)
        }
        catch(SQLException e) {
            log.error "Exception in update statement", e
            throw new VirtualDomainException( cleanSqlExceptionMessage(e,privs.debug) ?: "Unable to update resource.")
        }
        finally {
            //sql?.close()
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
        if(data.ID){
            data.id = data.ID
        }
        data = prepareData(data, parameters)
        def privs=userAccessRights(vd, parameters.parm_user_authorities)
        if (!privs.post){
            throw new AccessDeniedException("user.not.authorized.create",["${parameters.parm_user_loginName} "])
            //throw(new org.springframework.security.access.AccessDeniedException("Deny access for ${parameters.parm_user_loginName}"))
        }
        def sql
        try {
            sql = getSql()
            sql.execute(vd.codePost, data)
        }
        catch(SQLException e) {
            log.error "exception in create statement: $e"
            throw new VirtualDomainException( cleanSqlExceptionMessage(e,privs.debug) ?: "Unable to create resource.")
        }
        finally {
            //sql?.close()
        }
        return null //should return created object from db if changed in sql
    }

    def delete(vd, params) {
        def parameters = params
        addUser(parameters)
        def privs=userAccessRights(vd, parameters.parm_user_authorities)
        if (!privs.delete){
            throw new AccessDeniedException("user.not.authorized.delete",["${parameters.parm_user_loginName} "])
            //throw(new org.springframework.security.access.AccessDeniedException("Deny access for ${parameters.parm_user_loginName}"))
        }
        parameters.id = urlPathDecode(parameters.id)
        def sql
        try {
            sql = getSql()
            sql.execute(vd.codeDelete, params)
        }
        catch(SQLException e) {
            log.error "exception in delete statement: $e"
            throw new VirtualDomainException( cleanSqlExceptionMessage(e,privs.debug) ?: "Unable to delete resource.")
        }
        finally {
            //sql?.close()
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
                } catch (IllegalArgumentException e) {
                    //do nothing it is not a date so it should be ok
                    log.error "Exception in Date conversion:", e
                }
            } else if ( (it.value instanceof String) && it.value.find('\\d{4}-\\d{2}-\\d{2}T') && it.value.endsWith('Z')) {
                // if a date is returned from datepicker it appends "Z" at the end
                try {
                    // date string is 1981-06-10T04:00:00.000Z, adjusted for GMT by browser
                    def date = javax.xml.bind.DatatypeConverter.parseDateTime(it.value)
                    it.value=new java.sql.Timestamp(date.getTime().time)
                    log.debug "java.sql.Timestamp value: ${it.value}  Milliseconds past 1970 ${it.value.getTime()} date milli: ${date.getTimeInMillis()}"
                } catch (IllegalArgumentException e) {
                    //do nothing it is not a date so it should be ok
                    log.error "Exception in Date conversion:", e
                }
            } else if (it.value.getClass().toString().endsWith("JSONObject\$Null") ) {
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

    private def handleClobBlobTimestampRows(rows) {
        def foundClob=false
        for ( row in rows )  {
            for (col in row ) {
                if (col.value.getClass().getName().endsWith("CLOB")) {
                    if (col.value instanceof java.sql.Clob) {
                        String s = getStringValue(col.value)
                        col.value = s
                        foundClob = true
                    }
                }else if(col.value.getClass().getName().endsWith("BLOB")){
                    if (col.value instanceof java.sql.Blob) {
                        byte[] returnBytes
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
                        Blob blob = col.value
                        InputStream inputStream = blob.getBinaryStream()
                        int inByte;
                        while ((inByte = inputStream.read()) != -1)
                        {
                            byteArrayOutputStream.write(inByte)
                        }
                        returnBytes = byteArrayOutputStream.toByteArray()
                        col.value = Base64.getEncoder().encodeToString(returnBytes)
                        foundBlob = true
                    }
                }else if (col.value instanceof oracle.sql.TIMESTAMP) {
                    col.value = col.value.dateValue()
                    foundClob = true
                }
            }
            if (!foundClob)
                return rows // no need to traverse the whole array if first row doesn't have a CLOB
        }
        return rows
    }

    // For CLOB support. Based on code found on forum.springsource.org
    private String getStringValue(java.sql.Clob c) {
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

    private def isValidName(name, maxLength = 0) {
        def valid = name ==~ /[a-zA-Z]+[a-zA-Z0-9_]*/
        if (maxLength) {
            valid &= name.size()<=30
        }
        valid
    }

}
