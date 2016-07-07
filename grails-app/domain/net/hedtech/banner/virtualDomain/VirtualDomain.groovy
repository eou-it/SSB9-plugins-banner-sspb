/*******************************************************************************
 * Copyright 2013-2016 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
package net.hedtech.banner.virtualDomain

class VirtualDomain {

    static hasMany = [virtualDomainRoles: VirtualDomainRole]

    String serviceName
    String typeOfCode="S"  //SQL
    String dataSource="B"  // for now B=Banner, S=SSPB
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
        dataSource  nullable: false, inList: ["B","S"]
        codeGet     widget: 'textarea', nullable: false, maxSize: 1000000
        codePost    widget: 'textarea', nullable: true,  maxSize: 1000000
        codePut     widget: 'textarea', nullable: true,  maxSize: 1000000
        codeDelete  widget: 'textarea', nullable: true,  maxSize: 1000000
        //dateCreated     nullable:true
        //lastUpdated     nullabel:true
        fileTimestamp   nullable:true
    }

    //uncomment first time if db object is created
    static mapping = {
        //autoTimestamp true
        datasource 'sspb' //virtual domains can be stored in a separate data store
        //codeGet type: "clob"
        //codePost type: "clob"
        //codePut type: "clob"
        //codeDelete type: "clob"

    }

}
