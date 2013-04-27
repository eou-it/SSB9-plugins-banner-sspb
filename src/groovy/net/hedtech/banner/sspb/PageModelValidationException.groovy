package net.hedtech.banner.sspb

/**
 * Created with IntelliJ IDEA.
 * User: jzhong
 * Date: 4/23/13
 * Time: 2:07 PM
 * To change this template use File | Settings | File Templates.
 */
class PageModelValidationException extends Exception{
    def pageComponentPath
    int errorCode
    def errorMessage
}
