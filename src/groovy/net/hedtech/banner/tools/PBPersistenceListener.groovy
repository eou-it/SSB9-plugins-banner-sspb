package net.hedtech.banner.tools

import org.grails.datastore.mapping.core.Datastore
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEvent
import org.grails.datastore.mapping.engine.event.AbstractPersistenceEventListener
import org.grails.datastore.mapping.engine.event.EventType
import org.springframework.context.ApplicationEvent

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
        return true
    }

    //seems Grails updating lastUpdated automatically is broken (or fragile)... do it here
    def preUpdate( entity) {
        if (entity.hasProperty( 'lastUpdated' )) {
            entity.lastUpdated = new Date()
        }
    }
    def preInsert( entity) {
        if (entity.hasProperty( 'dateCreated' )) {
            entity.dateCreated = new Date()
        }
    }
}
