package net.hedtech.banner.tools.i18n

import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.web.context.request.RequestContextHolder

import java.text.MessageFormat
// from ssh://git@devgit1/banner/plugins/banner_tools.git
class ExtendedMessageSource extends ReloadableResourceBundleMessageSource {

    String extensibleBundleLocation
    def allBundles

    @Override
    protected String resolveCodeWithoutArguments(String code, Locale locale) {


        if( RequestContextHolder.getRequestAttributes()?.getSession()?."i18n-enable" &&
                !code.startsWith("default")) {
            return code
        }

        return super.resolveCodeWithoutArguments(code, getLocale(locale))    //To change body of overridden methods use File | Settings | File Templates.

    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {

        if( RequestContextHolder.getRequestAttributes()?.getSession()?."i18n-enable" &&
                !code.startsWith("default")) {
            return new MessageFormat(code, locale)
        }

        return super.resolveCode(code, getLocale(locale))    //To change body of overridden methods use File | Settings | File Templates.

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
                def path=extensibleBundleLocation
                if ( ! (path.endsWith("/") || path.endsWith("\\")) )
                    path+="/"
                new FileSystemResource(new File(path + location))
            }
        }
        super.setResourceLoader(rloader);
    }


}
