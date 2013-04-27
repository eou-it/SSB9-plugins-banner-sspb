package net.hedtech.banner.virtualDomain

import groovy.sql.Sql
import groovy.sql.SqlWithParams
import static javax.xml.bind.DatatypeConverter.parseDateTime
import java.sql.SQLException


class VirtualDomainSqlService {

   // def sessionFactory

    //allow connecting to 2 different datasources, depending on VirtualDomain.dataSource
    //to make development easy, but maybe also useful
    def dataSource
    def dataSource_sspb

    private def getSql =  { ds ->
        new Sql(ds =="B"?dataSource:dataSource_sspb)
    }

    private def changeParamsGet = {  p ->
        def modifyKeys = []
        p.each { key, value ->
            if (key != "action" && key != "virtualDomain" && key!="controller" )  {
                modifyKeys << key
            }
        }
        modifyKeys.each {
            def value = p[it]
            // unmarshall null from JS - if it is undefined change to null
            // HvT: This leads to issues if user wants to pass string undefined in a parameter.
            //      Is it possible to fix this in AngularJS?
            //      An empty value seems fine. Oracle does not distinguish between null and undefined.
            if (value=="undefined")
                value = null
            p.remove(it)
            // set parameter key name to lower case to workaround a groovy parameter replacement issue
            p.put(it.toLowerCase(), value)
        }
        if (p.id)
            p.id = urlPathDecode(p.id)
    }

    /*
    get will return an array of objects satisfying the parameters
    refactoring to support paging/pagination:
      input max: number of rows to return
      input offset: offset within total number of rows

    */
    def get(vd, params) {
        changeParamsGet(params) // some tweaks and work arounds
        def logmsg="Converted params for get: $params"
        def sql = getSql(vd.dataSource)
        def debugStatement = (params.debug == "true")?" and rownum<6":""
        def errorMessage = ""
        // Add a dummy bind variable to Groovy SQL to workaround an issue related to passing a map
        // to a query without bind variables
        def statement = "select * from (${vd.codeGet}) where (1=1 or :x is null) $debugStatement"
        def countStatement="select count(*) COUNT from (${vd.codeGet}) where (1=1 or :x is null) $debugStatement"

        def metaData = { meta ->
            logmsg += "\nNumber of columns: $meta.columnCount"
        }
        def rows
        def totalCount=-1
        try {
            def max=params.max?params.max.toInteger():-1
            def offset=params.offset?params.offset.toInteger():-1
            if (max>=0 || offset>=0)  {
                // determine the total count
                rows = sql.rows(countStatement,params)
                totalCount = rows[0].COUNT
            }
            if (totalCount == 0 ) { //no need to query again if 0
                rows = []
            }  else {
                rows = sql.rows(statement,params,offset,max,metaData)
                rows = idEncodeRows(rows)
                rows = handleClobRows(rows)
                if (totalCount<=0)
                    totalCount=rows.size
            }

            logmsg += " Fetched: ${rows?.size()} of  $totalCount with offset $offset  "
        } catch(e) {
            logmsg +="\n***ERROR*** ${e.getMessage()}\nStatement: \n $statement"
            errorMessage=logmsg
        } finally {
            sql.close()
        }
        println logmsg
        return [error: errorMessage, rows:rows, totalCount:totalCount]
    }

    def update(vd, params, data) {
        data = prepareData(data, params)
        def sql = getSql(vd.dataSource)
        sql.execute(vd.codePut, data)
        sql.close()
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
                throw new SQLException("Unable to read CLOB: " + e.getMessage())
            }
        }
        else {
            return "";
        }
    }

}