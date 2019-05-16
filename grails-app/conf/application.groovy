/*******************************************************************************
 Copyright 2017-2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

import grails.util.Holders as CH

// ******************************************************************************
//
//                       +++ EXTERNALIZED CONFIGURATION +++
//
// ******************************************************************************
//
// Config locations should be added to the map used below. They will be loaded based upon this search order:
// 1. Load the configuration file if its location was specified on the command line using -DmyEnvName=myConfigLocation
// 2. Load the configuration file if it exists within the user's .grails directory (i.e., convenient for developers)
// 3. Load the configuration file if its location was specified as a system environment variable
//
// Map [ environment variable or -D command line argument name : file path ]

//Added for integration tests to run in plugin level
grails.config.locations = [
        BANNER_APP_CONFIG:        "banner_configuration.groovy",
        BANNER_EXTENSIBILITY_APP_CONFIG:   "BannerExtensibility_configuration.groovy",
        WEB_APP_EXTENSIBILITY_CONFIG: "WebAppExtensibilityConfig.class"
]

// ******************************************************************************
//
//                       +++ BUILD NUMBER SEQUENCE UUID +++
//
// ******************************************************************************
//
// A UUID corresponding to this project, which is used by the build number generator.
// Since the build number generator web service provides build number sequences to
// multiple projects, and each project uses a unique UUID to identify which number
// sequence it is using.
//
// This number should NOT be changed.
// FYI: When a new UUID is needed (e.g., for a new project), use this URI:
//      http://maldevl2.sungardhe.com:8080/BuildNumberServer/newUUID
//
// DO NOT EDIT THIS UUID UNLESS YOU ARE AUTHORIZED TO DO SO AND KNOW WHAT YOU ARE DOING
//
//build.number.uuid = "" // specific UUID for FGE solution
//build.number.base.url="http://m039200.sungardhe.com:8080/BuildNumberServer/buildNumber?method=getNextBuildNumber&uuid="

grails.project.groupId = "net.hedtech" // used when deploying to a maven repo

grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [
        html: ['text/html', 'application/xhtml+xml'],
        xml: ['text/xml', 'application/xml', 'application/vnd.sungardhe.student.v0.01+xml'],
        text: 'text/plain',
        js: 'text/javascript',
        rss: 'application/rss+xml',
        atom: 'application/atom+xml',
        css: 'text/css',
        csv: 'text/csv',
        all: '*/*',
        json: ['application/json', 'text/json'],
        form: 'application/x-www-form-urlencoded',
        multipartForm: 'multipart/form-data',
        jpg: 'image/jpeg',
        png: 'image/png',
        gif: 'image/gif',
        bmp: 'image/bmp',
]

// The default codec used to encode data with ${}
//TODO: change default codec back to html and specify codec for javascript in visualComposer.gsp
grails.views.default.codec = "html" // none, html, base64  **** note: Setting this to html will ensure html is escaped, to prevent XSS attack ****
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
grails.plugin.springsecurity.logout.afterLogoutUrl = "/"
grails.converters.domain.include.version = true
//grails.converters.json.date = "default"

grails.converters.json.pretty.print = true
grails.converters.json.default.deep = true

// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = false

// enable GSP preprocessing: replace head -> g:captureHead, title -> g:captureTitle, meta -> g:captureMeta, body -> g:captureBody
grails.views.gsp.sitemesh.preprocess = true

grails.resources.mappers.yuicssminify.includes = ['**/*.css']
grails.resources.mappers.yuijsminify.includes  = ['**/*.js']
grails.resources.mappers.yuicssminify.excludes = ['**/*.min.css']
grails.resources.mappers.yuijsminify.excludes  = ['**/*.min.js']

markdown = [
        removeHtml: true
]

dataSource {
    configClass = GrailsAnnotationConfiguration.class
    dialect = "org.hibernate.dialect.Oracle10gDialect"
    loggingSql = false
}

hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'org.hibernate.cache.SingletonEhCacheRegionFactory' // Hibernate 3
//  cache.region.factory_class = 'org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory' // Hibernate 4
    singleSession = true // configure OSIV singleSession mode
    flush.mode = 'manual' // OSIV session flush mode outside of transactional context
//  cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
    hbm2ddl.auto = null
    show_sql = false
    // naming_strategy = "org.hibernate.cfg.ImprovedNamingStrategy"
    dialect = "org.hibernate.dialect.Oracle10gDialect"

    config.location = [
            "classpath:hibernate-banner-sspb.cfg.xml",
            "classpath:hibernate-banner-general-utility.cfg.xml"
    ]

}

// ******************************************************************************
//
//                       +++ DATA ORIGIN CONFIGURATION +++
//
// ******************************************************************************
// This field is a Banner standard, along with 'lastModifiedBy' and lastModified.
// These properties are populated automatically before an entity is inserted or updated
// within the database. The lastModifiedBy uses the username of the logged in user,
// the lastModified uses the current timestamp, and the dataOrigin uses the value
// specified here:
dataOrigin = "Banner"

// ******************************************************************************
//
//                       +++ FORM-CONTROLLER MAP +++
//
// ******************************************************************************
// This map relates controllers to the Banner forms that it replaces.  This map
// supports 1:1 and 1:M (where a controller supports the functionality of more than
// one Banner form.  This map is critical, as it is used by the security framework to
// set appropriate Banner security role(s) on a database connection. For example, if a
// logged in user navigates to the 'medicalInformation' controller, when a database
// connection is attained and the user has the necessary role, the role is enabled
// for that user and Banner object.
formControllerMap = [
        // SELFSERVICE should be first 'Form Name' - probably self service and other forms is not expected
        'virtualdomain'           : ['SELFSERVICE'], // kind of obsolete - should go through restfulapi
        'virtualdomaincomposer'   : ['SELFSERVICE'],
        'cssmanager'              : ['SELFSERVICE'],
        'visualpagemodelcomposer' : ['SELFSERVICE'],
        'custompage'              : ['SELFSERVICE'], // renders the page builder pages
        'restfulapi'              : ['SELFSERVICE']
]

grails.plugin.springsecurity.useRequestMapDomainClass = false
//grails.plugin.springsecurity.providerNames = ['casBannerAuthenticationProvider', 'selfServiceBannerAuthenticationProvider', 'bannerAuthenticationProvider']
//grails.plugin.springsecurity.rejectIfNoRule = true

//grails.plugin.springsecurity.filterChain.chainMap = [
//        '/api/**': 'authenticationProcessingFilter,basicAuthenticationFilter,securityContextHolderAwareRequestFilter,anonymousProcessingFilter,basicExceptionTranslationFilter,filterInvocationInterceptor',
//        '/**': 'securityContextPersistenceFilter,logoutFilter,authenticationProcessingFilter,securityContextHolderAwareRequestFilter,anonymousProcessingFilter,exceptionTranslationFilter,filterInvocationInterceptor'
//]
// User RequestMap to only EXTZ App, since other app might need SS config changes.
    String appId = 'EXTZ'
    if(appId && "EXTZ".equals(appId)){
        grails.plugin.springsecurity.securityConfigType = grails.plugin.springsecurity.SecurityConfigType.Requestmap //SecurityConfigType.Requestmap
        grails.plugin.springsecurity.requestMap.className = 'net.hedtech.banner.sspb.Requestmap'
    }else{
        grails.plugin.springsecurity.securityConfigType = grails.plugin.springsecurity.SecurityConfigType.InterceptUrlMap
    }
//TODO: evaluate if it makes sense to use grails.plugin.springsecurity.securityConfigType = "Requestmap"

// This allows dynamic configuration of spring security as we need in page builder (now the security is done 'by hand' in the controller).
//see http://blog.springsource.com/2010/08/11/simplified-spring-security-with-grails/

// ******************************************************************************
//
//                       +++ INTERCEPT-URL MAP +++
//
// ******************************************************************************

grails.plugin.springsecurity.interceptUrlMap = [
        '/'            : ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/develop'     : ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/login/**'    : ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/ssb/menu/**'     : ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/index**'     : ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/logout/**'   : ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/js/**'       : ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/css/**'      : ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/images/**'   : ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/plugins/**'  : ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/errors/**'   : ['IS_AUTHENTICATED_ANONYMOUSLY'],
        //Page Builder specific
        '/api/virtualDomains.*/**'    : ['IS_AUTHENTICATED_ANONYMOUSLY','ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M'],
        '/api/pages/**'               : ['ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M'],
        '/api/csses/**'               : ['ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M'],
        '/internalPb/pagesecurity/**'     : ['ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M'],
        '/internalPb/pageexports/**'      : ['ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M'],
        '/internalPb/virtualdomainexports/**'  : ['ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M'],
        '/internalPb/cssexports/**'       : ['ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M'],
        '/virtualDomainComposer/**'   : ['ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M'],
        '/visualPageModelComposer/**' : ['ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M','ROLE_SCADETL_BAN_DEFAULT_M'],
        '/cssManager/**'              : ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/cssRender/**'               : ['IS_AUTHENTICATED_ANONYMOUSLY'],
        //'/customPage/**'              : ['IS_AUTHENTICATED_ANONYMOUSLY'],   // Controller should deal with privileges
        // Not sure if next line should be there - it is commented in other SS modules
        //'/**'          : [ 'ROLE_DETERMINED_DYNAMICALLY' ]
        '/**'          : ['IS_AUTHENTICATED_ANONYMOUSLY']

]

grails.validateable.packages=['net.hedtech.banner.student.registration']

// set per-environment serverURL stem for creating absolute links
environments {
    production {
        grails.serverURL = "http://www.changeme.com"
    }
    development {
        grails.serverURL = "http://localhost:8080/${appName}"
    }
    test {
        grails.serverURL = "http://localhost:8080/${appName}"
    }

}