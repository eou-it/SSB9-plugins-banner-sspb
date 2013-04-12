package net.hedtech.banner.sspb

class Page {
    String constantName
    String modelView
    String compiledView
    String compiledController
    static constraints = {
        constantName       nullable: false , unique: true, maxSize: 60
        modelView          nullable: false , maxSize: 1000000, widget: 'textarea'
        compiledView       nullable: true  , maxSize: 1000000, widget: 'textarea'
        compiledController nullable: true  , maxSize: 1000000, widget: 'textarea'
    }
      //uncomment first time if db object is created
    static mapping = {
        //modelView type: "clob"
        //compiledView type: "clob"
        //compiledController type: "clob"

        datasource 'sspb'
    }


}