/* ****************************************************************************
Copyright 2013 Ellucian Company L.P. and its affiliates.
******************************************************************************/
package net.hedtech.banner.virtualDomain


class VirtualDomainException extends RuntimeException{


    private int statusCode
    private def errorMessage
    private def errorType

    VirtualDomainException( def msg ) {
        this.statusCode = 500
        this.errorType = "sql"
        this.errorMessage = msg
    }

    public def getHttpStatusCode() {
        return statusCode
    }

    public returnMap = { localize ->
        def map = [:]
        if (errorMessage) {
            map.message = "SQL Error"
        }
        if (errorType) {
            map.errors = [ type: errorType, errorMessage: errorMessage.replace("\n", " ") ]
        }
        if (errorType == "sql") {
            map.headers = ['X-Status-Reason':'SQL Execution Failed']
        }
        map
    }

}
