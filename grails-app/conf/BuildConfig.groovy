/*******************************************************************************
 * Copyright 2013-2018 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
grails.servlet.version = "2.5"
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.plugin.location.'banner-core'="../banner_core.git"
grails.plugin.location.'banner-restful-api-support'="../banner-restful-api-support.git"
grails.plugin.location.'banner_general_utility' = "../banner_general_utility.git"
grails.plugin.location.'i18n_core'="../i18n_core.git"

grails.project.dependency.resolver="maven"
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
        test "org.grails:grails-datastore-test-support:1.0.2-grails-2.4"
        compile 'com.googlecode.java-diff-utils:diffutils:1.3.0'
    }

    plugins {
        compile ":spring-security-core:2.0-RC5"
        compile ':cache-headers:1.1.7'
        runtime ":hibernate:3.6.10.19"
        //runtime ":hibernate4:4.3.8.1"
        runtime ":database-migration:1.4.0"
        //build  ":tomcat:8.0.22"
        //compile  ":tomcat:8.0.22" //this avoids prompt for do you want to upgrade/downgrade
    }
}
