/******************************************************************************
 *  Copyright 2013-2016 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/

package net.hedtech.banner.tools.i18n

import groovy.util.logging.Log4j
@Log4j
class LocaleResource {
    static def ga = grails.util.Holders.grailsApplication
    static def g = ga.mainContext.getBean('org.grails.plugins.web.taglib.ApplicationTagLib')
    static def existsCache = [:] //Using a map as a simple cache - should be small

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
            log.debug "Resource not found: $resource"
        }
        null //No locale specific file available
    }

    // Method to determine if a resource exists.
    // Using a cache as the find method may be slow due to the wild card search.
    static private def exists(uri) {
        if ( existsCache[uri] != null ) {
            log.debug "Found in cache: $uri"
            return existsCache[uri]
        }
        log.debug "Finding resource $uri"
        def start = uri.indexOf('/plugins')>=0?uri.indexOf('/plugins')+1:uri.lastIndexOf('/')+1
        def end = uri.indexOf('?') >= 0 ? uri.indexOf('?') : uri.length()
        def pattern = "**/" + uri.substring(start, end)
        log.debug "Search pattern: $pattern"
        def resources = ga.mainContext.getResources(pattern).toList()
        resources.each { resource ->
            log.debug "Found matching resource: ${resource.path}"
        }
        existsCache[uri] = resources?.size()>0
    }

}