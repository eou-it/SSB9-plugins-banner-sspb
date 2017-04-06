/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.sspb

class PageRole {
    static belongsTo = [page: Page]
    String roleName
    Boolean allow = true     // assume access is allowed for a role - can change to temporarily disable

    static constraints = {
        roleName nullable: false,  maxSize: 30, unique : 'page'
    }
}

