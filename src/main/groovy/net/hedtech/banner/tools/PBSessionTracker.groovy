/*******************************************************************************
 Copyright 2020 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.tools

import groovy.util.logging.Slf4j
import org.springframework.security.core.context.SecurityContextHolder

import javax.servlet.http.HttpSessionEvent
import javax.servlet.http.HttpSessionListener
import java.util.concurrent.ConcurrentHashMap

@Slf4j
class PBSessionTracker implements HttpSessionListener {
    public static ConcurrentHashMap<String, Map> cachedMap = new ConcurrentHashMap<String, Map>()


    @Override
    void sessionCreated(HttpSessionEvent event) {
        if (event && event.getSession() && event.getSession().id) {
            log.trace("Page Builder User Session created: " + event.getSession().id)
        }
    }

    @Override
    void sessionDestroyed(HttpSessionEvent event) {
        try {
            if (event && event.getSession() && event.getSession().id) {
                log.debug("Page Builder User Session destroyed: " + event.getSession().id)
            }

            def userIn = SecurityContextHolder?.context?.authentication?.principal
            if (userIn && cachedMap && cachedMap.containsKey(userIn.username)) {
                cachedMap?.remove(userIn.username)
            }
        } catch (Exception ex) {
            log.error("PBSessionTracker Exception : ", ex)
        }
    }

}
