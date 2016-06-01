package net.hedtech.banner.tools.i18n

import grails.util.Holders as CH
import org.codehaus.groovy.grails.context.support.PluginAwareResourceBundleMessageSource
import org.codehaus.groovy.grails.context.support.ReloadableResourceBundleMessageSource.PropertiesHolder

import java.text.MessageFormat

// from ssh://git@devgit1/banner/plugins/banner_tools.git
class BannerMessageSource extends PluginAwareResourceBundleMessageSource {

    PageMessageSource pageMessageSource

    public BannerMessageSource() {
        super()
        if (pageMessageSource == null) {
            pageMessageSource = new PageMessageSource()
        }
    }

    public def getRootProperties(baseName) {
        PropertiesHolder propHolder
        Locale locale = new Locale('qq') //A locale that does not exist, we want to retrieve the root props
        def match = pluginBaseNames.find{ it.endsWith("/$baseName") && it.contains("banner-sspb")}
        if (match) {
            def fnames = calculateAllFilenames(match, locale)
            def resource = fnames.find{it.bValue!=null}
            propHolder = getProperties(resource.aValue,resource.bValue)
        }
        propHolder?.properties
    }

    @Override
    protected String resolveCodeWithoutArguments(String code, Locale locale) {

        String msg = pageMessageSource.resolveCodeWithoutArguments(code, locale)
        if(msg == null) {
            return super.resolveCodeWithoutArguments(code, getLocale(locale))    //To change body of overridden methods use File | Settings | File Templates.
        } else {
            return  msg
        }

    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {

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
