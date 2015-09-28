// Support Hibernate annotations
import org.codehaus.groovy.grails.orm.hibernate.cfg.GrailsAnnotationConfiguration
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
    /*
    config.location = [
            "classpath:hibernate-banner-core.cfg.xml"
    ]
    */
}

// environment specific settings
environments {
    development {

        //Banner database (default)
        dataSource {
        }

        //Database with sspb
        dataSource_sspb {
            /*   ---> moved to banner_configuration.groovy
            */
        }

    }
    test {
        //Banner database (default)
        dataSource {
        }
        //Database with sspb
        dataSource_sspb {
        }
    }
    production {
        dataSource {
        }
        //Database with sspb
        dataSource_sspb {
        }
    }
}
