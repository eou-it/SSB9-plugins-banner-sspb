/******************************************************************************
 *  Copyright 2013-2019 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/

package net.hedtech.banner.tools.i18n

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.util.GrailsWebMockUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.context.request.RequestContextHolder
import spock.lang.Ignore
import spock.lang.Specification

@Integration
@Rollback
class LocaleResourceSpec extends Specification {

    @Autowired
    WebApplicationContext ctx

    def setup() {
        GrailsWebMockUtil.bindMockWebRequest(ctx)
    }

    def cleanup() {
        RequestContextHolder.resetRequestAttributes()
    }

    @Ignore
    void "Integration test importExisting for datepicker resources"() {
        expect:
        LocaleResource.importExisting(plugin:'banner-sspb', dir: 'BannerXE/lib/jquery/i18n',
                file: 'jquery.ui.datepicker-{locale}.js',
                locale: locale, html: '<script src="{resource}" ></script>' )?.length() == len
        where:
        locale  | len
        'ar'    | 77
        'en-AU' | 80
        'en-GB' | 80
        'es'    | 77
        'fr'    | 77
        'fr-CA' | 80
        'pt'    | 77
        'en-US' | null
    }

    @Ignore
    void "Integration test importExisting for angularjs resources"() {
        expect:
        LocaleResource.importExisting(plugin:'', dir: 'BannerXE/lib/angular/i18n',
                file: 'angular-locale_{locale}.js',
                locale: locale.toLowerCase(), html: '<script src="{resource}" ></script>' )?.length() == len
        where:
        locale  | len
        'ar'    | 72
        'en'    | 72
        'en-AU' | 75
        'en-GB' | 75
        'en-US' | 75
        'es'    | 72
        'fr'    | 72
        'fr-CA' | 75
        'pt'    | 72
        'pt-BR' | 75
        'xx'    | null
    }

}
