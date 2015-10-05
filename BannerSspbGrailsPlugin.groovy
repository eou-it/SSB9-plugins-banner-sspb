import net.hedtech.banner.sspb.PageModelValidator
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.core.io.ContextResource
import org.apache.commons.lang.StringUtils
//import net.hedtech.banner.tools.i18n.PageMessageSource
import net.hedtech.banner.tools.i18n.BannerMessageSource
import grails.util.Environment
import org.codehaus.groovy.grails.web.context.GrailsConfigUtils
import org.codehaus.groovy.grails.web.pages.GroovyPagesTemplateEngine
import net.hedtech.banner.sspb.PageUtilService
import net.hedtech.banner.tools.PBPersistenceListener

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
    def title = "Banner Sspb Plugin" // Headline display name of the plugin
    def author = "Your name"
    def authorEmail = ""
    def description = '''\
Brief summary/description of the plugin.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/banner-sspb"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
//    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
//    def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
//    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
//    def scm = [ url: "http://svn.codehaus.org/grails-plugins/" ]

    String baseDir = "grails-app/i18n"
    String watchedResources = "file:./${baseDir}/**/*.properties".toString()

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }

    def doWithSpring = {
        //  from ssh://git@devgit1/banner/plugins/banner_tools.git   mostly
        Set baseNames = []

        def messageResources
        if (application.warDeployed) {
            messageResources = parentCtx?.getResources("**/WEB-INF/${baseDir}/**/*.properties")?.toList()
        }
        else {
            messageResources = plugin.watchedResources
        }

        if (messageResources) {
            for (resource in messageResources) {
                // Extract the file path of the file's parent directory
                // that comes after "grails-app/i18n".
                String path
                if (resource instanceof ContextResource) {
                    path = StringUtils.substringAfter(resource.pathWithinContext, baseDir)
                }
                else {
                    path = StringUtils.substringAfter(resource.path, baseDir)
                }

                // look for an underscore in the file name (not the full path)
                String fileName = resource.filename
                int firstUnderscore = fileName.indexOf('_')

                if (firstUnderscore > 0) {
                    // grab everyting up to but not including
                    // the first underscore in the file name
                    int numberOfCharsToRemove = fileName.length() - firstUnderscore
                    int lastCharacterToRetain = -1 * (numberOfCharsToRemove + 1)
                    path = path[0..lastCharacterToRetain]
                }
                else {
                    // Lop off the extension - the "basenames" property in the
                    // message source cannot have entries with an extension.
                    path -= ".properties"
                }
                baseNames << "WEB-INF/" + baseDir + path
            }
        }

        LOG.debug "Creating messageSource with basenames: $baseNames"

        messageSource(BannerMessageSource) {
            basenames = baseNames.toArray()
            fallbackToSystemLocale = false
            pluginManager = manager
            if (Environment.current.isReloadEnabled() || GrailsConfigUtils.isConfigTrue(application, GroovyPagesTemplateEngine.CONFIG_PROPERTY_GSP_ENABLE_RELOAD)) {
                def cacheSecondsSetting = application?.flatConfig?.get('grails.i18n.cache.seconds')
                if(cacheSecondsSetting != null) {
                    cacheSeconds = cacheSecondsSetting as Integer
                } else {
                    cacheSeconds = 5
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
