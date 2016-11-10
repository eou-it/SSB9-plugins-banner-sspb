/*******************************************************************************
 Copyright 2013-2016 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.tools.i18n

import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.sspb.PageUtilService
import grails.util.Holders as CH

import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.web.context.request.RequestContextHolder

import java.text.MessageFormat
// from ssh://git@devgit1/banner/plugins/banner_tools.git
class PageMessageSource extends ReloadableResourceBundleMessageSource {

    String bundleLocation

    static final def globalPropertiesName = "pageGlobal"
    def pageResources = [] // will go into basenames in superclass, since that cannot be accessed, we keep a copy

    @Override
    protected String resolveCodeWithoutArguments(String code, Locale locale) {
        return super.resolveCodeWithoutArguments(code, getLocale(locale))
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        return super.resolveCode(code, getLocale(locale))
    }

    private getLocale(locale) {
        if(CH.config.bannerLocaleVariant instanceof String) {
            Locale loc = new Locale(locale.language, locale.country, CH.config.bannerLocaleVariant)
            return loc
        }
        return locale
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        def rloader = new DefaultResourceLoader() {
            public Resource getResource(String location) {
                def path=bundleLocation
                if ( ! (path.endsWith("/") || path.endsWith("\\")) )
                    path+="/"
                new FileSystemResource(new File(path + location))
            }
        }
        super.setResourceLoader(rloader);
    }

    /* constructor + methods added for page builder */
    public PageMessageSource() {
        super()
        logger.debug "Initialize PageResources"
        try {
            bundleLocation=PageUtilService.getBundleLocation()
            logger.debug "External Bundle location : $bundleLocation (Banner configuration pageBuilder.locations.bundles default: \$temp or \$tmp)"
            //Get the basenames from the external root properties files (assume filename has no underscore)
            new File(bundleLocation).eachFileMatch(~/[^_]*.properties/) {   file ->
                String fileName = file.name
                fileName-= ".properties"  // remove the extension
                pageResources.add(fileName)
                logger.debug "added page resource ${file.name}"
            }
            /*
            if (!pageResources.contains(globalPropertiesName))
                pageResources.add(globalPropertiesName)
            */

            this.setResourceLoader() // make sure that the super  class uses the resource loader above
            setBasenames( (String []) pageResources)

        } catch (FileNotFoundException ex) {
            logger.error "Unable to load external resources from configured location. Not found: ${ex.getMessage()}"
        }
    }

    public void addPageResource(String baseName)  {
        try {
            if (baseName && !pageResources.contains(baseName)) {
                pageResources.add(baseName)
            }

            setBasenames((String []) pageResources)
           logger.debug "Added to page resources: " + baseName
        } catch (ApplicationException ex) {
            logger.error "Exception while adding page resource $baseName in PageMessageSource:\n${ex.getMessage()}"
            throw new ApplicationException(PageMessageSource, ex)
        }
    }


}
