/******************************************************************************
 *  Copyright 2013-2016 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/

package net.hedtech.banner.tools.i18n

class LocaleResource {
    static def ga = grails.util.Holders.grailsApplication
    static def g = ga.mainContext.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib')

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
            def root =g.createLink(uri: '/')
            def fname = resource
            def end = fname.indexOf("?")
            end = end==-1?fname.length():end
            fname = fname.substring(fname.indexOf(root)+root.length()-1, end)
            if ( ga.parentContext.getResource(fname).exists() ) {
                return args.html.replace("{resource}", resource)
            }
        }
        null //No locale specific file available
    }

}