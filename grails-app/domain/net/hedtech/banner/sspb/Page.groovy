package net.hedtech.banner.sspb

class Page {

    static hasMany = [pageRoles: PageRole]

    String constantName
    String modelView
    String compiledView
    String compiledController

    Date dateCreated
    Date lastUpdated
    Date fileTimestamp

    static constraints = {
        constantName       nullable: false , unique: true, maxSize: 60
        modelView          nullable: false , maxSize: 1000000, widget: 'textarea'
        compiledView       nullable: true  , maxSize: 1000000, widget: 'textarea'
        compiledController nullable: true  , maxSize: 1000000, widget: 'textarea'
        //dateCreated     nullable:true
        //lastUpdated     nullable:true
        fileTimestamp   nullable:true
    }

    static mapping = {
        autoTimestamp true
        datasource 'sspb'
        //uncomment first time if db object is created
        //modelView type: "clob"
        //compiledView type: "clob"
        //compiledController type: "clob"
    }
}