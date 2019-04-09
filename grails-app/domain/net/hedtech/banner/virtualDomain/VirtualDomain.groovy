/*******************************************************************************
 Copyright 2017-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.virtualDomain

class VirtualDomain {

    static hasMany = [virtualDomainRoles: VirtualDomainRole]

    String serviceName
    String typeOfCode="S"  //SQL
    String codeGet
    String codePost
    String codePut
    String codeDelete

    Date dateCreated
    Date lastUpdated
    String lastModifiedBy // Transient to work around banner-core issue
    Date fileTimestamp
    String owner
    String allowAllInd
    String tag

    static constraints = {
        serviceName nullable: false, unique: true, maxSize: 60
        typeOfCode  nullable: false, inList: ["S","G"]
        codeGet     widget: 'textarea', nullable: false, maxSize: 1000000
        codePost    widget: 'textarea', nullable: true,  maxSize: 1000000
        codePut     widget: 'textarea', nullable: true,  maxSize: 1000000
        codeDelete  widget: 'textarea', nullable: true,  maxSize: 1000000
        //dateCreated     nullable:true
        //lastUpdated     nullabel:true
        fileTimestamp   nullable:true
        owner      nullable:true
        allowAllInd   nullable:true
        tag        nullable:true
    }
    static transients = ['lastModifiedBy']

    static mapping = {
        owner column: "VIRTUAL_DOMAIN_OWNER"
        allowAllInd column: "VIRTUAL_DOMAIN_ALLOW_ALL_IND"
        tag column: "VIRTUAL_DOMAIN_TAG"
    }
}
