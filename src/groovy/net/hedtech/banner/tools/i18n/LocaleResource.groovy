/******************************************************************************
 *  Copyright 2013-2016 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/

package net.hedtech.banner.tools.i18n

import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.methods.HttpGet

class LocaleResource {
    static def ga = grails.util.Holders.grailsApplication
    static def g = ga.mainContext.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib')
    static def existsCache = [:]


    // Method to derive a list of suffices for locale specific files. 'en-US' results in ['en-US', 'en']
    static private def getSuffices(locale){
        def result = [locale]
        if (locale.contains('-')) {
            result << locale.split('-')[0]
        } else if (locale.contains('_')) {
            result << locale.split('_')[0]
        }
        result
    }

    // Method to support importing locale specific file while checking that it exists
    static def importExisting(args) {
        def suffices = getSuffices(args.locale)

        for ( suffix in suffices ) {
            def resource = g.resource(plugin: args.plugin, dir: args.dir, file: args.file.replace("{locale}",suffix))
            if (exists(resource)) {
                return args.html.replace("{resource}", resource)
            }
        }
        null //No locale specific file available
    }

    // Method to determine if a resource exists
    // Now an http get is used to test if the url is available (cached for performance reasons)
    // Using getResourceAsStream to determine the existence of a resource seems problematic for war deployed apps
    // It should be possible, but it doesn't seem straightforward
    static def exists(uri) {
        if (existsCache[uri] != null ) {
            return existsCache[uri]
        }
        def client = new DefaultHttpClient();
        def root = g.createLink(uri: '/', absolute: true)
        root = root.substring(0, root.indexOf(g.createLink(uri: '/')))
        def httpGet = new HttpGet(root + uri);
        def httpGetResponse = client.execute(httpGet)
        existsCache[uri] = (httpGetResponse.statusLine.statusCode != 404)
    }

}