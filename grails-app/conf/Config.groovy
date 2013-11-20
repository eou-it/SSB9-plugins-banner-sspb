/*******************************************************************************
 Copyright 2009-2013 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

import net.hedtech.banner.configuration.ApplicationConfigurationUtils as ConfigFinder
import grails.plugins.springsecurity.SecurityConfigType

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

grails.config.locations = [] // leave this initialized to an empty list, and add your locations in the map below.

def locationAdder = ConfigFinder.&addLocation.curry(grails.config.locations)

[ BANNER_APP_CONFIG:        "banner_configuration.groovy",
        TEST_BANNER_UI_SS_CONFIG: "${appName}_configuration.groovy",
].each { envName, defaultFileName -> locationAdder( envName, defaultFileName ) }

grails.config.locations.each {
    println "configuration: " + it
}

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
grails.views.default.codec = "none" // none, html, base64  **** note: Setting this to html will ensure html is escaped, to prevent XSS attack ****
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
grails.plugins.springsecurity.logout.afterLogoutUrl = "/"
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

// set per-environment serverURL stem for creating absolute links
environments {
    production {
        grails.serverURL = "http://localhost:8080/${appName}"
    }
    development {
        grails.resources.debug = true
        grails.serverURL = "http://localhost:8080/${appName}"
    }
    test {
        grails.serverURL = "http://localhost:8080/${appName}"
    }
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
        'pagemodelcomposer'       : ['SELFSERVICE'], // kind of obsolete, but useful for viewing the generated code
        'custompage'              : ['SELFSERVICE'], // renders the page builder pages
        'restfulapi'              : ['SELFSERVICE']
]

grails.plugins.springsecurity.useRequestMapDomainClass = false
grails.plugins.springsecurity.providerNames = ['casBannerAuthenticationProvider', 'selfServiceBannerAuthenticationProvider', 'bannerAuthenticationProvider']
//grails.plugins.springsecurity.rejectIfNoRule = true

grails.plugins.springsecurity.filterChain.chainMap = [
        '/api/**': 'authenticationProcessingFilter,basicAuthenticationFilter,securityContextHolderAwareRequestFilter,anonymousProcessingFilter,basicExceptionTranslationFilter,filterInvocationInterceptor',
        '/**': 'securityContextPersistenceFilter,logoutFilter,authenticationProcessingFilter,securityContextHolderAwareRequestFilter,anonymousProcessingFilter,exceptionTranslationFilter,filterInvocationInterceptor'
]

grails.plugins.springsecurity.securityConfigType = SecurityConfigType.InterceptUrlMap
//TODO: evaluate if it makes sense to use grails.plugins.springsecurity.securityConfigType = "Requestmap"
// This allows dynamic configuration of spring security as we need in page builder (now the security is done 'by hand' in the controller).
//see http://blog.springsource.com/2010/08/11/simplified-spring-security-with-grails/

// ******************************************************************************
//
//                       +++ INTERCEPT-URL MAP +++
//
// ******************************************************************************

grails.plugins.springsecurity.interceptUrlMap = [
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
        '/api/virtualDomains.*/**'    : ['IS_AUTHENTICATED_ANONYMOUSLY','ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M'], // not sure if this is overriding or complementing the filterChain
        '/api/pages/**'               : ['ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M'],
        '/api/csses/**'               : ['ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M'],
        '/internal/pagesecurity/**'     : ['ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M'],
        '/internal/pageexports/**'      : ['ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M'],
        '/internal/virtualdomainexports/**'  : ['ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M'],
        '/internal/cssexports/**'       : ['ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M'],
        '/virtualDomainComposer/**'   : ['ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M'],
        '/visualPageModelComposer/**' : ['ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M','ROLE_SCADETL_BAN_DEFAULT_M'],
        '/cssManager/**'              : ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/cssRender/**'               : ['IS_AUTHENTICATED_ANONYMOUSLY'],
        //'/customPage/**'              : ['IS_AUTHENTICATED_ANONYMOUSLY'],   // Controller should deal with privileges
        // Not sure if next line should be there - it is commented in other SS modules
        //'/**'          : [ 'ROLE_DETERMINED_DYNAMICALLY' ]
        '/**'          : ['IS_AUTHENTICATED_ANONYMOUSLY']

]

// CodeNarc rulesets
codenarc.ruleSetFiles="rulesets/banner.groovy"
codenarc.reportName="target/CodeNarcReport.html"
codenarc.propertiesFile="grails-app/conf/codenarc.properties"
codenarc.extraIncludeDirs=["grails-app/composers"]

grails.validateable.packages=['net.hedtech.banner.student.registration']

// placeholder for real configuration
// base.dir is probably not defined for .war file deployments
//banner.picturesPath=System.getProperty('base.dir') + '/test/images'

// local seeddata files
seedDataTarget = [ ]

markdown = [
        removeHtml: true
]

// ******************************************************************************
//                              CORS Configuration
// ******************************************************************************
// Note: If changing custom header names, remember to reflect them here.
//
cors.url.pattern        = '/api/*'
cors.allow.origin.regex ='.*'
cors.expose.headers     ='content-type,X-hedtech-totalCount,X-hedtech-pageOffset,X-hedtech-pageMaxSize,X-hedtech-message,X-hedtech-Media-Type'


// ******************************************************************************
//             RESTful API Custom Response Header Name Configuration
// ******************************************************************************
// Note: Tests within this test app expect this 'X-hedtech...' naming to be used.
//
restfulApi.header.totalCount  = 'X-hedtech-totalCount'
restfulApi.header.pageOffset  = 'X-hedtech-pageOffset'
restfulApi.header.pageMaxSize = 'X-hedtech-pageMaxSize'
restfulApi.header.message     = 'X-hedtech-message'
restfulApi.header.mediaType   = 'X-hedtech-Media-Type'

// ******************************************************************************
//             RESTful API 'Paging' Query Parameter Name Configuration
// ******************************************************************************
// Note: Tests within this test app expect this 'X-hedtech...' naming to be used.
//
restfulApi.page.max    = 'max'
restfulApi.page.offset = 'offset'

// API path component to construct the REST API URL
sspb.apiPath = 'internal'
// Reveal detailed SQL error messages to users with this role
sspb.debugRoleName = "SELFSERVICE-WTAILORADMIN"

// ******************************************************************************
//                       RESTful API Endpoint Configuration
// ******************************************************************************

restfulApiConfig = {


    jsonDomainMarshallerTemplates {
        template 'jsonDomainAffordance' config {
            additionalFields {map ->
                map['json'].property("_href", "/${map['resourceName']}/${map['resourceId']}" )
            }
        }
    }

    xmlDomainMarshallerTemplates {
        template 'xmlDomainAffordance' config {
            additionalFields {map ->
                def xml = map['xml']
                xml.startNode('_href')
                xml.convertAnother("/${map['resourceName']}/${map['resourceId']}")
                xml.end()
            }
        }
    }

    marshallerGroups {
        group 'json-date-closure' marshallers {
            marshaller {
                instance = new org.codehaus.groovy.grails.web.converters.marshaller.ClosureOjectMarshaller<grails.converters.JSON>(
                    java.util.Date, {return "customized-date:" + it?.format("yyyy-MM-dd'T'HH:mm:ssZ")})
            }
        }
    }

    // This pseudo resource is used when issuing a query using a POST. Such a POST is made
    // against the actual resource being queried, but using a different URL prefix (e.g., qapi)
    // so the request is routed to the 'list' method (versus the normal 'create' method).
    resource 'query-filters' config {
        // TODO: Add support for 'application/x-www-form-urlencoded'
        representation {
            mediaTypes = ["application/json"]
            jsonExtractor {}
        }
    }
	
    // Pagebuilder resources

    resource  'pages' config {
        representation {
            mediaTypes = ["application/json"]
            marshallers {
                marshaller {
                    instance = new net.hedtech.restfulapi.marshallers.json.BasicDomainClassMarshaller(app:grailsApplication)
                    priority = 100
                }
            }
            extractor = new net.hedtech.restfulapi.extractors.json.DefaultJSONExtractor()
        }
        representation {
            mediaTypes = ["application/xml"]
            //jsonAsXml = true
            marshallers {
                marshaller {
                    instance = new net.hedtech.restfulapi.marshallers.xml.BasicDomainClassMarshaller(app:grailsApplication)
                    priority = 200
                }
            }
            extractor = new net.hedtech.restfulapi.extractors.xml.MapExtractor()
        }
    }

    resource 'csses' config {
        representation {
            mediaTypes = ["application/json"]
            marshallers {
                marshaller {
                    instance = new net.hedtech.restfulapi.marshallers.json.BasicDomainClassMarshaller(app:grailsApplication)
                    priority = 100
                }
            }
            extractor = new net.hedtech.restfulapi.extractors.json.DefaultJSONExtractor()
        }
        representation {
            mediaTypes = ["application/xml"]
            //jsonAsXml = true
            marshallers {
                marshaller {
                    instance = new net.hedtech.restfulapi.marshallers.xml.BasicDomainClassMarshaller(app:grailsApplication)
                    priority = 200
                }
            }
            extractor = new net.hedtech.restfulapi.extractors.xml.MapExtractor()
        }
    }
}

//does not to work well with virtual domains yet - updates are not being picked up
cache.headers.enabled = false