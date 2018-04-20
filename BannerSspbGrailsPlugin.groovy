/*******************************************************************************
 Copyright 2013-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

import grails.util.Holders
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import net.hedtech.banner.i18n.ExternalMessageSource
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.web.access.intercept.RequestmapFilterInvocationDefinition

import net.hedtech.banner.tools.PBPersistenceListener

import net.hedtech.banner.sspb.PageUtilService
import org.springframework.web.context.support.ServletContextResourcePatternResolver

class BannerSspbGrailsPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.2 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def title = "Banner Self Service Page Builder Plugin" // Headline display name of the plugin
    def author = "ellucian"
    def authorEmail = ""
    def description = '''This plugin adds the PageBuilder features to an application.'''

    // URL to the plugin's documentation
    def documentation = ""

    //String baseDir = "grails-app/i18n"
    //String watchedResources = "file:./${baseDir}/**/*.properties".toString()

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }
    String securityConfigType = SpringSecurityUtils.securityConfigType
    // From  I18nGrailsPlugin.groovy 2.5.4
    def doWithSpring = {
        //Register the ExternalMessageSource in the initialization of the application context.
        String appId = Holders.grailsApplication.metadata['app.appId']
        if ("EXTZ".equals(appId) && securityConfigType == 'Requestmap') {
            objectDefinitionSource(RequestmapFilterInvocationDefinition)
        }
    }



    def doWithDynamicMethods = { applicationContext ->
        //add a message method to the following classes
        def classes = application.serviceClasses
        classes += net.hedtech.banner.sspb.PageModelErrors
        classes.each {
            // Note: weblogic throws an error if we try to inject the method if it is already present
            if (!it.metaClass.methods.find { m -> m.name.matches("message") }) {
                def name = it.name // needed as this 'it' is not visible within the below closure...
                try {
                    it.metaClass.static.message = { mapToLocalize ->
                        //def applicationContext = ServletContextHolder.getServletContext().getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT);
                        MessageSource messageSource = applicationContext.getBean("messageSource")
                        try {
                            messageSource.getMessage(mapToLocalize.code, (Object[]) mapToLocalize.args, LocaleContextHolder.locale)
                        }
                        catch (e) {
                            println e
                        }
                    }
                    //println "added message to service class $name"
                }
                catch (e) {} // rare case where we'll bury it...
            }
        }
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
        application.mainContext.eventTriggeringInterceptor.datastores.each { k, datastore ->
            applicationContext.addApplicationListener new PBPersistenceListener(datastore)
            //println "Added PersistenceListener to $datastore"
        }
        //Set up the externalMessageSource for Page Builder
        def externalMessageSource = new ExternalMessageSource(
                PageUtilService.bundleLocation, PageUtilService.bundleName,
                "Banner configuration pageBuilder.locations.bundles default: \$temp or \$tmp")
        applicationContext.getBean("messageSource")?.setExternalMessageSource(externalMessageSource)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    def onShutdown = { event ->
        // TODO Implement code that is executed when the application shuts down (optional)
    }

    def loadAfter = ['banner-general-utility']
}
