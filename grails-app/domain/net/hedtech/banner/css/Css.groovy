/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.css

class Css {

    String constantName
    String css
    String description

    Date dateCreated
    Date lastUpdated
    Date fileTimestamp

    String lastModifiedBy // Transient to work around banner-core issue

    static constraints = {
        constantName    nullable: false , unique: true, maxSize: 60
        css             nullable: false , maxSize: 1000000, widget: 'textarea'
        description     nullable: false , maxSize: 255
        //dateCreated     nullable:true
        //lastUpdated     nullable:true
        fileTimestamp   nullable:true
    }

    static transients = ['lastModifiedBy']
}