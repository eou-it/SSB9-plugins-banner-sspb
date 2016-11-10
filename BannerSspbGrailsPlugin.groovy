/*******************************************************************************
 Copyright 2013-2016 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.ContextResource
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import grails.util.Environment
import org.codehaus.groovy.grails.commons.GrailsStringUtils
import org.codehaus.groovy.grails.web.context.GrailsConfigUtils
import org.codehaus.groovy.grails.web.pages.GroovyPagesTemplateEngine
import groovy.transform.CompileStatic
import net.hedtech.banner.tools.PBPersistenceListener
import net.hedtech.banner.tools.i18n.BannerMessageSource
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

    String baseDir = "grails-app/i18n"
    String watchedResources = "file:./${baseDir}/**/*.properties".toString()

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }

    // From  I18nGrailsPlugin.groovy 2.5.4
    def doWithSpring = {
        net.hedtech.banner.sspb.PageUtilService.configLocations() //Make sure locations are set up before creating the message bean
        // find i18n resource bundles and resolve basenames
        Set baseNames = []
        def messageResources
        if (application.warDeployed) {
            messageResources = parentCtx?.getResources("**/WEB-INF/${baseDir}/**/*.properties")?.toList()
        }
        else {
            messageResources = plugin.watchedResources
        }
        calculateBaseNamesFromMessageSources(messageResources, baseNames)

        if (Environment.isWarDeployed()) {
            servletContextResourceResolver(ServletContextResourcePatternResolver, ref('servletContext'))
        }

        messageSource(BannerMessageSource) {
            basenames = baseNames.toArray()
            fallbackToSystemLocale = false
            pluginManager = manager
            if (Environment.current.isReloadEnabled() || GrailsConfigUtils.isConfigTrue(application, GroovyPagesTemplateEngine.CONFIG_PROPERTY_GSP_ENABLE_RELOAD)) {
                def cacheSecondsSetting = application?.flatConfig?.get('grails.i18n.cache.seconds')
                cacheSeconds = cacheSecondsSetting == null ? 5 : cacheSecondsSetting as Integer
                def fileCacheSecondsSetting = application?.flatConfig?.get('grails.i18n.filecache.seconds')
                fileCacheSeconds = fileCacheSecondsSetting == null ? 5 : fileCacheSecondsSetting as Integer
            }
            if (Environment.isWarDeployed()) {
                resourceResolver = ref('servletContextResourceResolver')
            }
        }

    }

    // From  I18nGrailsPlugin.groovy 2.5.4
    @CompileStatic
    protected void calculateBaseNamesFromMessageSources(messageResources, Set baseNames) {
        if (messageResources) {
            for (mr in messageResources) {
                Resource resource = (Resource)mr
                String path
                // Extract the file path of the file's parent directory
                // that comes after "grails-app/i18n".
                if (resource instanceof ContextResource) {
                    path = GrailsStringUtils.substringAfter(resource.getPathWithinContext(), baseDir)
                } else if (resource instanceof FileSystemResource) {
                    path = GrailsStringUtils.substringAfter(resource.getPath(), baseDir)
                }
                else if (resource instanceof ClassPathResource) {
                    path = GrailsStringUtils.substringAfter(resource.getPath(), baseDir)
                }

                if (path) {

                    // look for an underscore in the file name (not the full path)
                    String fileName = resource.filename
                    int firstUnderscore = fileName.indexOf('_')

                    if (firstUnderscore > 0) {
                        // grab everything up to but not including
                        // the first underscore in the file name
                        int numberOfCharsToRemove = fileName.length() - firstUnderscore
                        int lastCharacterToRetain = -1 * (numberOfCharsToRemove + 1)
                        fileName = fileName[0..lastCharacterToRetain]
                    } else {
                        // Lop off the extension - the "basenames" property in the
                        // message source cannot have entries with an extension.
                        fileName -= ".properties"
                    }

                    baseNames << "WEB-INF/$baseDir/$fileName".toString()
                }
            }
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
}
