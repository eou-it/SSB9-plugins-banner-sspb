/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.sspb

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

class PageRole {
    static belongsTo = [page: Page]
    @JsonIgnoreProperties("pageRoles")
    Page page
    String roleName
    Boolean allow = true     // assume access is allowed for a role - can change to temporarily disable
    String lastModifiedBy // Transient to work around banner-core issue
    static constraints = {
        roleName nullable: false,  maxSize: 30
        //page unique: true
    }
    static transients = ['lastModifiedBy']
}

