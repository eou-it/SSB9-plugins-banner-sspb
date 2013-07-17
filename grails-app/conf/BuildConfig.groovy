grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.plugin.location.'restful-api'="../restful-api.git"
grails.plugin.location.'banner_core'="../banner_core.git"
grails.plugin.location.'banner_codenarc'="../banner_codenarc.git"
grails.plugin.location.'i18n_core'="../i18n_core.git"
grails.plugin.location.'spring-security-cas' = "../spring_security_cas.git"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility
    repositories {
        inherits true // Whether to inherit repository definitions from plugins

        grailsPlugins()
        grailsHome()
		grailsCentral()
        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        mavenLocal()
        mavenCentral()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.21'
        test "org.spockframework:spock-grails-support:0.7-groovy-2.0"
		
		// HvT: resolve compilation issue with Banner-core
        // taken from grails-spring-security-cas / grails-app / conf / BuildConfig.groovy
        compile('org.springframework.security:spring-security-cas-client:3.0.7.RELEASE') {
            excludes 'spring-security-core', 'spring-security-web', 'servlet-api',
                    'spring-tx', 'spring-test', 'cas-client-core', 'ehcache',
                    'junit', 'mockito-core', 'jmock-junit4'
        }
        compile('org.jasig.cas.client:cas-client-core:3.1.12') {
            excludes 'xmlsec', 'opensaml', 'spring-beans', 'spring-test', 'spring-core',
                    'spring-context', 'log4j', 'junit', 'commons-logging', 'servlet-api'
        }
    }

    plugins {
        //compile ":resources:1.1.6"
        compile ":spring-security-core:1.2.7.3"
        compile ":webxml:1.4.1"


        runtime ":hibernate:$grailsVersion"
        //runtime ":jquery:1.8.2"
        //runtime ":jquery-ui:1.8.24"
        //compile ":angularjs-resources:1.0.2"
        //runtime ":resources:1.1.6"
        
		
		build(":tomcat:$grailsVersion",
              ":release:2.2.0",
              ":rest-client-builder:1.0.3") {
            export = false
        }

		compile ":tomcat:$grailsVersion"
        
		compile (":inflector:0.2"
		        ,":cache-headers:1.1.5"
				//,':cache:1.0.0'
				)

        // Fix HvT
        compile 'org.grails.plugins:codenarc:0.18.1'
        // Fix  HvT
        compile ":functional-test:2.0.RC1"


        test(":spock:0.7") {
            exclude "spock-grails-support"
        }
        test ':code-coverage:1.2.5'
    }
}
