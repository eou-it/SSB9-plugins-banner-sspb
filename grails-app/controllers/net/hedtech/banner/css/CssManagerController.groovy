/*******************************************************************************
 * Copyright 2013-2016 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
package net.hedtech.banner.css

import org.springframework.web.multipart.MultipartFile
import grails.converters.JSON

class CssManagerController {
    static defaultAction = "loadCssManagerPage"

    def cssService

    def loadCssManagerPage = {
        render (view:"cssManager")
    }

    // TODO use REST API
    def uploadCss = {
        log.trace "in uploadCss. Params = $params"

        MultipartFile mpf = request.getFile('file')
        def filename = mpf.originalFilename.toLowerCase()
        def buf = mpf.inputStream.getText()

        //println "file name = $filename"
        //println "file content = $buf"
        // save CSS
        def ret = cssService.create([cssName: params.cssName, source: buf, description: params.description], [:])

        def res = [received: true, fileName: filename, fileSize: buf.length(), statusCode: ret.statusCode, statusMessage: ret.statusMessage]
        header 'X-Frame-Options' ,"SAMEORIGIN"
        render res as JSON
    }


}
