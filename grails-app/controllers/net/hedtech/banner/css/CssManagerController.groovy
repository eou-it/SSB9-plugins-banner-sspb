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
        def res
        def buf
        if (mpf.getContentType() == 'text/css' ) {
            buf = mpf.inputStream.getText('utf-8')
            if ( validEncoding(buf) ) {
                def ret = cssService.create([cssName: params.cssName, source: buf, description: params.description], [:])
                res = [received     : true, fileName: filename, fileSize: buf.length(), statusCode: ret.statusCode,
                       statusMessage: ret.statusMessage]
            } else {
                res = [received: false, fileName: filename, statusCode: 1,
                       statusMessage: message(code:"sspb.css.cssManager.upload.invalid.encoding.message")]
            }
        } else {
            res = [received: false, fileName: filename, statusCode: 1,
                   statusMessage: message(code:"sspb.css.cssManager.upload.invalid.file.type.message")]
        }
        header 'X-Frame-Options' ,"SAMEORIGIN"
        render res as JSON
    }

    def validEncoding(data) {
        return !data.contains('\ufffd')
    }
}
