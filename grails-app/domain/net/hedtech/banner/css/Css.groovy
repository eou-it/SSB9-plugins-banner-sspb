package net.hedtech.banner.css

class Css {

    String constantName
    String css
    String description

    Date dateCreated
    Date lastUpdated
    Date fileTimestamp

    static constraints = {
        constantName    nullable: false , unique: true, maxSize: 60
        css             nullable: false , maxSize: 1000000, widget: 'textarea'
        description     nullable: false , maxSize: 255
        //dateCreated     nullable:true
        //lastUpdated     nullable:true
        fileTimestamp   nullable:true
    }
      //uncomment first time if db object is created
    static mapping = {
        datasource 'sspb'
        //autoTimestamp true
    }


}