package net.hedtech.banner.css

import net.hedtech.banner.sspb.PageRole

class Css {

    static hasMany = [cssRoles: CssRole]

    String constantName
    String css
    String description
    static constraints = {
        constantName       nullable: false , unique: true, maxSize: 60
        css             nullable: false , maxSize: 1000000, widget: 'textarea'
        description     nullable: false , maxSize: 1000000, widget: 'textarea'
    }
      //uncomment first time if db object is created
    static mapping = {
        datasource 'sspb'
    }


}