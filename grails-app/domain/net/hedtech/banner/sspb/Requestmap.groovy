/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.sspb

class Requestmap {

    String url
    String configAttribute

    static constraints = {
        url blank: false, unique: true
        configAttribute blank: false, maxSize: 4000
    }
}
