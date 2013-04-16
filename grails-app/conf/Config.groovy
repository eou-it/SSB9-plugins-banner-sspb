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
