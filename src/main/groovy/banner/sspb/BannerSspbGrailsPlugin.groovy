/******************************************************************************
 *  Copyright 2013-2019 Ellucian Company L.P. and its affiliates.                   *
 ******************************************************************************/
package banner.sspb

import grails.plugins.*

import grails.util.Holders
import net.hedtech.banner.sspb.PageUtilService
import net.hedtech.banner.tools.PBUtilServiceBase
import org.grails.datastore.mapping.core.Datastore
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import net.hedtech.banner.i18n.ExternalMessageSource
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.web.access.intercept.RequestmapFilterInvocationDefinition

import net.hedtech.banner.tools.PBPersistenceListener

import org.springframework.web.context.support.ServletContextResourcePatternResolver

class BannerSspbGrailsPlugin extends Plugin {

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.3.2 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def title = "Banner Sspb" // Headline display name of the plugin
    def author = "Your name"
    def authorEmail = ""
    def description = '''\
Brief summary/description of the plugin.
'''
    def profiles = ['web']

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

    Closure doWithSpring() { {->
            // TODO Implement runtime spring config (optional)
			/*String appId = Holders?.grailsApplication?.metadata?.get('app.appId')
			if (appId && "EXTZ".equals(appId) && securityConfigType == 'Requestmap') {
				objectDefinitionSource(RequestmapFilterInvocationDefinition)
			}*/
        }
    }

    void doWithDynamicMethods() {
        //add a message method to the following classes
        def classes = grailsApplication.serviceClasses
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

    void doWithApplicationContext() {
        // TODO Implement post initialization spring config (optional)

        // TODO Implement post initialization spring config (optional)
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        configurableApplicationContext.getBeansOfType(Datastore).each { k, datastore ->
            applicationContext.addApplicationListener new PBPersistenceListener(datastore)
            //println "Added PersistenceListener to $datastore"
        }

        def pbConfig= Holders.getConfig().pageBuilder
        def pageUtilService=new PageUtilService()
        def pbBundleLocation = pageUtilService.bundleLocation
        //Set up the externalMessageSource for Page Builder
        def externalMessageSource = new ExternalMessageSource(
                pbBundleLocation, "pageBuilder",
                "Banner configuration pageBuilder.locations.bundles default: \$temp or \$tmp")
        applicationContext.getBean("messageSource")?.setExternalMessageSource(externalMessageSource)
    }

    void onChange(Map<String, Object> event) {
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    void onConfigChange(Map<String, Object> event) {
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    void onShutdown(Map<String, Object> event) {
        // TODO Implement code that is executed when the application shuts down (optional)
    }

}
