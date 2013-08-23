package net.hedtech.banner.sspb

/**
 * Created with IntelliJ IDEA.
 * User: hvthor
 * Date: 19/08/13
 * Time: 13:14
 * To change this template use File | Settings | File Templates.
 */
class Requestmap {

    String url
    String configAttribute

    static mapping = {
        cache true
        datasource 'sspb'
    }

    static constraints = {
        url blank: false, unique: true
        configAttribute blank: false, maxSize: 4000
    }
}
