/*******************************************************************************
 Copyright 2020 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.tools

import groovy.util.logging.Slf4j
import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.context.WebApplicationContext

import javax.servlet.http.HttpSessionEvent
import javax.servlet.http.HttpSessionListener
import java.util.concurrent.ConcurrentHashMap

@Slf4j
class PBSessionTracker implements HttpSessionListener, ApplicationContextAware  {
    public static final ConcurrentHashMap<String,Map> cachedMap = new ConcurrentHashMap<String,Map>()


    @Override
    void sessionCreated(HttpSessionEvent se) {
        log.trace("Page Builder User Session created: " + se.session.id)
    }

    @Override
    void sessionDestroyed(HttpSessionEvent se) {
        log.trace("Page Builder User Session destroyed: " + se.session.id)
        def userIn = SecurityContextHolder?.context?.authentication?.principal
        if(cachedMap.containsKey(userIn?.username)){
            cachedMap.remove(userIn?.username)
        }

    }

    @Override
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        def servletContext = ((WebApplicationContext) applicationContext).getServletContext()
        servletContext.addListener(this);
    }
}
