package net.hedtech.banner.sspb

class Page {
    String constantName
    String modelView
    String compiledView
    //String compiledController
    static constraints = {
        constantName nullable: false, unique: true, maxSize: 60
        modelView    widget: 'textarea'  ,  nullable: false ,  maxSize: 1000000
        compiledView widget: 'textarea'  ,  nullable: true  ,  maxSize: 1000000
        //compiledController nullable:  true, widget: 'textarea'
    }
      //uncomment first time if db object is created
    static mapping = {
        modelView type: "clob"
        compiledView type: "clob"
        //compiledController type: "clob"

        datasource 'sspb'
    }


}