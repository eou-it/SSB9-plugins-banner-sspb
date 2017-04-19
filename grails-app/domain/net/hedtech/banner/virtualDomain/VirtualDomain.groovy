/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
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
    Date fileTimestamp

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
    }
}
