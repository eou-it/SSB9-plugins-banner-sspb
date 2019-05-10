/*******************************************************************************
 * Copyright 2013-2016 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
package net.hedtech.banner.sspb

class PageModelValidationException extends Exception{
    def pageComponentPath
    int errorCode
    def errorMessage
}
