/*******************************************************************************
 * Copyright 2013-2016 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
package net.hedtech.banner.sspb

class Requestmap {

    String url
    String configAttribute

    static mapping = {
        cache true
        datasource 'sspb'
    }

    static constraints = {
        url blank: false, unique: true
        configAttribute blank: false, maxSize: 4000
    }
}
