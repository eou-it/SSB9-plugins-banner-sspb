dataSource {
    pooled = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'org.hibernate.cache.SingletonEhCacheRegionFactory' // Hibernate 3
    //cache.region.factory_class = 'org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory' // Hibernate 4
    singleSession = true // configure OSIV singleSession mode
    flush.mode = 'manual' // OSIV session flush mode outside of transactional context
    //show_sql = true
}
// environment specific settings
environments {
    development {

        //Banner database (default)
        dataSource {
            pooled = true
            driverClassName = "oracle.jdbc.OracleDriver"
            dialect = "org.hibernate.dialect.Oracle10gDialect"
            username = "baninst1"
            password = "u_pick_it"
            url = "jdbc:oracle:thin:@localhost:1521:ban83"
            dbCreate = "none" //"validate"
            //loggingSql = true
            //logSql =true

        }

        //Database with sspb
        dataSource_sspb {
            pooled = true
            driverClassName = "oracle.jdbc.OracleDriver"
            dialect = "org.hibernate.dialect.Oracle10gDialect"
            username = "sspbmgr"
            password = "u_pick_it"
            url = "jdbc:oracle:thin:@localhost:1521:ban83"
            //url = "jdbc:oracle:thin:@149.24.229.150:1521:orcl"
            dbCreate =  "none" //"validate" "update"
            //loggingSql = true
        }

    }
    test {
        //Banner database (default)
        dataSource {
            pooled = true
            driverClassName = "oracle.jdbc.OracleDriver"
            dialect = "org.hibernate.dialect.Oracle10gDialect"
            username = "baninst1"
            password = "u_pick_it"
            url = "jdbc:oracle:thin:@localhost:1521:ban83"
            dbCreate = "none" //"validate"
        }

        //Database with sspb
        dataSource_sspb {
            pooled = true
            driverClassName = "oracle.jdbc.OracleDriver"
            dialect = "org.hibernate.dialect.Oracle10gDialect"
            username = "sspbmgr"
            password = "u_pick_it"
            url = "jdbc:oracle:thin:@localhost:1521:ban83"
            //url = "jdbc:oracle:thin:@149.24.229.150:1521:orcl"
            dbCreate =  "none" //"validate" "update"
            transactional = false
        }
    }
    production {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:prodDb;MVCC=TRUE"
            pooled = true
            properties {
                maxActive = -1
                minEvictableIdleTimeMillis=1800000
                timeBetweenEvictionRunsMillis=1800000
                numTestsPerEvictionRun=3
                testOnBorrow=true
                testWhileIdle=true
                testOnReturn=true
                validationQuery="SELECT 1"
            }
        }
    }
}
