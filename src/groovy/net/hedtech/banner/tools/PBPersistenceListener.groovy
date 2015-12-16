package net.hedtech.banner.tools

import groovy.util.logging.Log4j
import org.grails.datastore.mapping.core.Datastore
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEvent
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEventListener
import org.grails.datastore.mapping.engine.event.EventType
import org.hibernate.PropertyValueException
import org.springframework.context.ApplicationEvent

@Log4j
class PBPersistenceListener extends AbstractPersistenceEventListener {
    public PBPersistenceListener(final Datastore datastore) {
        super(datastore)
    }

    @Override
    protected void onPersistenceEvent(final AbstractPersistenceEvent event) {
        switch (event.eventType) {
            case EventType.PreInsert: preInsert(event.entityObject)        ;break
            //case EventType.PostInsert: println "POST INSERT ${event.entityObject}"      ;break
            case EventType.PreUpdate: preUpdate(event.entityObject)         ;break
            //case EventType.PostUpdate: println "POST UPDATE ${event.entityObject}"      ;break
            //case EventType.PreDelete: println "PRE DELETE ${event.entityObject}"        ;break
            //case EventType.PostDelete: println "POST DELETE ${event.entityObject}"      ;break
            //case EventType.PreLoad: println "PRE LOAD ${event.entityObject}"            ;break
            //case EventType.PostLoad: println "POST LOAD ${event.entityObject}"          ;break
        }
    }

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        switch (eventType) {
            case EventType.PreInsert:
            case EventType.PreUpdate:
                return true
                break
            default:
                return false
        }
    }

    //seems Grails updating lastUpdated automatically is broken (or fragile)... do it here
    def preUpdate( entity) {
        if (entity.hasProperty( 'lastUpdated' )) {
            try {
                entity.lastUpdated = new Date()
            }
            catch (PropertyValueException e) {
                log.error "error adding last updated", e
            }
        }
    }
    def preInsert( entity) {
        if (entity.hasProperty( 'dateCreated' )) {
            try {
                entity.dateCreated = new Date()
            }
            catch (PropertyValueException e) {
                log.error "error adding date created", e
            }
        }
    }
}
