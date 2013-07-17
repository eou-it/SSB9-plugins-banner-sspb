package net.hedtech.banner.sspb

class PageRole {
    static belongsTo = [page: Page]
    String roleName
    Boolean allow = true     // assume access is allowed for a role - can change to temporarily disable

    static constraints = {
        roleName nullable: false,  maxSize: 30

    }

    static mapping = {
        datasource 'sspb'
        //page roles can be stored in a separate data store
    }
}

