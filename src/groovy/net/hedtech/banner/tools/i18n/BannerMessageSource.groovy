package net.hedtech.banner.tools.i18n

import grails.util.Holders as CH
import org.codehaus.groovy.grails.context.support.PluginAwareResourceBundleMessageSource
import org.springframework.web.context.request.RequestContextHolder

import java.text.MessageFormat

// from ssh://git@devgit1/banner/plugins/banner_tools.git
class BannerMessageSource extends PluginAwareResourceBundleMessageSource {

    PageMessageSource pageMessageSource

    public BannerMessageSource() {
        super()
        if (pageMessageSource == null) {
            pageMessageSource = new PageMessageSource()
            //pageMessageSource.clearCache()
        }
    }

    @Override
    protected String resolveCodeWithoutArguments(String code, Locale locale) {


        if( RequestContextHolder.getRequestAttributes()?.getSession()?.maintenanceMode &&
                !code.startsWith("default")) {
            return code
        }

        String msg = pageMessageSource.resolveCodeWithoutArguments(code, locale)
        if(msg == null) {
            return super.resolveCodeWithoutArguments(code, getLocale(locale))    //To change body of overridden methods use File | Settings | File Templates.
        } else {
            return  msg
        }

    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {

        if( RequestContextHolder.getRequestAttributes()?.getSession()?.maintenanceMode &&
                !code.startsWith("default")) {
            return new MessageFormat(code, locale)
        }

        MessageFormat mf = pageMessageSource.resolveCode(code, locale)
        if(mf == null) {
            return super.resolveCode(code, getLocale(locale))    //To change body of overridden methods use File | Settings | File Templates.
        } else {
            return  mf
        }

    }

    private getLocale(locale) {
        if(CH.config.bannerLocaleVariant instanceof String) {
            Locale loc = new Locale(locale.language, locale.country, CH.config.bannerLocaleVariant)
            return loc
        }
        return locale
    }
}
