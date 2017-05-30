/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.virtualDomain

class VirtualDomainRole {
    static belongsTo = [virtualDomain: VirtualDomain]
    String roleName
    Boolean allowGet  = true     // assume at least a get is allowed for a role - otherwise we wouldn't exist
    Boolean allowPut  = false
    Boolean allowPost = false
    Boolean allowDelete = false

    String lastModifiedBy // Transient to work around banner-core issue

    static constraints = {
        roleName nullable: false,  maxSize: 30
    }
    static transients = ['lastModifiedBy']
}
