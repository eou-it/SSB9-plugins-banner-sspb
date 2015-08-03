grails.servlet.version = "2.5"
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

//grails.plugin.location.'banner_ui_ss'="../banner_ui_ss.git"
grails.plugin.location.'banner-core'="../banner_core.git"
//grails.plugin.location.'banner-codenarc'="../banner_codenarc.git"
//grails.plugin.location.'sghe-aurora'="../sghe_aurora.git"
//grails.plugin.location.'spring-security-cas'="../spring_security_cas.git"
//grails.plugin.location.'i18n_core'="../i18n_core.git"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility
    repositories {
        if (System.properties['PROXY_SERVER_NAME']) {
            mavenRepo "${System.properties['PROXY_SERVER_NAME']}"
        }
        mavenRepo "http://repo.grails.org/grails/repo"
        grailsCentral()
        mavenCentral()
        mavenRepo "https://code.lds.org/nexus/content/groups/main-repo"
        mavenRepo "http://repository.jboss.org/maven2/"
    }
    
    dependencies {
///*
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        // runtime 'mysql:mysql-connector-java:5.1.21'
        test "org.spockframework:spock-grails-support:0.7-groovy-2.0"
/*
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
*/
    }

    plugins {
        //compile ":resources:1.1.6"
        compile ":spring-security-core:2.0-RC5"
        compile ":webxml:1.4.1"
        compile ':cache-headers:1.1.7'
        compile ":hibernate:3.6.10.19"
        compile ":tomcat:7.0.55.2"
        compile ":functional-test:2.0.0"
        //compile ':codenarc:0.21'
        //compile ':csv:0.3.1'
        //compile ':feeds:1.5'

        //compile ':selenium:0.8'
        //compile ':selenium-rc:1.0.2'
        //compile  ":inflector:0.2"
        compile ':restful-api:1.0.0'
    
        build(
              ":release:3.1.1",
              ":rest-client-builder:2.1.1") {
            export = false
        }

        test(":spock:0.7") {
            exclude "spock-grails-support"
        }
//        test ':code-coverage:2.0.3-3'
    }
}
