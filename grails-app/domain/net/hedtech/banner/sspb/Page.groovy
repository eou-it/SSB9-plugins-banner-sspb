package net.hedtech.banner.sspb

/**
 * Created with IntelliJ IDEA.
 * User: jzhong
 * Date: 4/4/13
 * Time: 10:41 AM
 * To change this template use File | Settings | File Templates.
 */
class Page {
    String name
    String modelView
    String compiledView
    String compiledController
    String controller
    String action
    static constraints = {
        name nullable: false, unique: true, maxSize: 30
        modelView maxSize: 65000, widget: 'textarea'
        compiledView maxSize: 65000, widget: 'textarea'
        compiledController maxSize: 65000, widget: 'textarea'
    }
}
