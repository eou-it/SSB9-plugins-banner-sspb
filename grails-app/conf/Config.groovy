// configuration for plugin testing - will not be included in the plugin zip

// load data source definition from external configuration file. File folder is specified in Java property SSPB_CONFIG_DIR
// the file name is banner-sspb-config.properties
def extConfig = System.getProperties().get('SSPB_CONFIG_DIR');

if (extConfig) {
    println "Using configuration file $extConfig/${appName}-config.properties"
    grails.config.locations = [ "file:${extConfig}/${appName}-config.properties"]
} else
    println "SSPB_CONFIG_DIR environment variable not defined. Using default settings."

log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'
    warn   'org.mortbay.log'
    debug  'net.hedtech.banner'
}

restfulApiConfig = {
    resource {
        name = 'virtualDomains'    // common service to handle all virtual domain
        /*
        representation {
            mediaType = "text/plain"
            // AngularJS delete doesn't include a body and mediaType will default to text/plain (?)
            //can we omit marshallers and extractors - there is nothing to extract or marchal here
        }
        media type must be xml or json
        */
        representation {
            mediaType = "application/json"
            addMarshaller {
                marshaller = new net.hedtech.restfulapi.marshallers.json.BasicDomainClassMarshaller(app:grailsApplication)
                priority = 100
            }
            extractor = new net.hedtech.restfulapi.extractors.json.DefaultJSONExtractor()
        }
        representation {
            mediaType = "application/xml"
            jsonAsXml = true
            addMarshaller {
                marshaller = new net.hedtech.restfulapi.marshallers.xml.JSONObjectMarshaller()
                priority = 200
            }
            extractor = new net.hedtech.restfulapi.extractors.xml.JSONObjectExtractor()
        }
    }
}

//seems not to work well with virtual domains yet - updates are not being picked up
cache.headers.enabled = false