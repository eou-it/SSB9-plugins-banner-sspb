/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.sspb

class Requestmap {

    String url
    String configAttribute
    String lastModifiedBy // Transient to work around banner-core issue
    static constraints = {
        url blank: false, unique: true
        configAttribute blank: false, maxSize: 4000
    }
    static transients = ['lastModifiedBy']
}
