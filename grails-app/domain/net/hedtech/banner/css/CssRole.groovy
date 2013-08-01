package net.hedtech.banner.css

class CssRole {
    static belongsTo = [css: Css]
    String roleName
    Boolean allow = true     // assume access is allowed for a role - can change to temporarily disable

    static constraints = {
        roleName nullable: false,  maxSize: 30

    }

    static mapping = {
        datasource 'sspb'
        //css roles can be stored in a separate data store
    }
}

