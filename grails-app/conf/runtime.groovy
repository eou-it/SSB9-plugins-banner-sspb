/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

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
        [pattern:'/'       ,   access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/develop',   access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/login/**'    , access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/ssb/menu/**',   access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/index**',   access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/logout/**'   , access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/js/**'  ,   access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/css/**' ,   access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/images/**'   , access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/plugins/**'  , access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/errors/**'   , access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        //Page Builder specific
        [pattern:'/api/virtualDomains.*/**'    , access:['IS_AUTHENTICATED_ANONYMOUSLY','ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M']],
        [pattern:'/api/pages/**'          ,   access:['ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M']],
        [pattern:'/api/csses/**'          ,   access:['ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M']],
        [pattern:'/internalPb/pagesecurity/**',   access:['ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M']],
        [pattern:'/internalPb/pageexports/**' ,   access:['ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M']],
        [pattern:'/internalPb/virtualdomainexports/**'  , access:['ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M']],
        [pattern:'/internalPb/cssexports/**'  ,   access:['ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M']],
        [pattern:'/virtualDomainComposer/**'   , access:['ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M']],
        [pattern:'/visualPageModelComposer/**' , access:['ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M','ROLE_SCADETL_BAN_DEFAULT_M']],
        [pattern:'/cssManager/**'         ,   access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/cssRender/**'          ,   access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/**'     ,   access:['IS_AUTHENTICATED_ANONYMOUSLY']]

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
sspb.apiPath = 'internalPb'

// ******************************************************************************
//                       RESTful API Endpoint Configuration
// ******************************************************************************

restfulApiConfig = {
    // Pagebuilder resources

    // generic resource for virtual domains

    anyResource {
        serviceName = 'virtualDomainResourceService'
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

    resource 'pagesecurity' config {
        serviceName= 'pageSecurityService'
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
    }


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

    resource 'pageexports' config {
        serviceName= 'pageExportService'
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
    }
    resource 'virtualdomainexports' config {
        serviceName= 'virtualDomainExportService'
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
    }
    resource 'cssexports' config {
        serviceName= 'cssExportService'
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

    // 2 demo resources
    resource 'todos' config {
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

    resource 'projects'  config {
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