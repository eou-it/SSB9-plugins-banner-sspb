/**
 * dirtyCheck is a jQuery function that is used to add dirty checking prior to accepting an event.
 *
 * We will loop through all elements that are selected.
 *
 * We will store the already registered event listeners/handlers in an array on the element.
 *
 * We will then register our own event handler.
 *
 * When the event is fired, the dirtyCheck event handler will be fired.  It'll call the 'isDirty' function
 * sent in the options.  If the event is dirty it'll create a notification to prompt the user to save, not save
 * or cancel the entire operation.
 *
 * If isDirty returned true and the user requested to save the function will first call the 'save' function passed
 * in the options.  The save function requires an options map to be accepted and specifically should be looking for a
 * success callback function to call.  Once the save completes, the save function calls the success callback.  At this
 * point the function will bind the existing handlers and trigger the event.  See section below about ignoring internal
 * dirtyCheck handler.
 *
 * If isDirty returned true and the user said they did not want to save, the function will immediatelly call the
 * existing handlers.  See section below about ignoring internal dirtyCheck handler.
 *
 * If isDirty returned true and the user said they wanted to cancel the function does nothing.  The function return
 * false and is done.
 *
 * For the internal dirtyCheck handler the function needs to know when it is executing the existing handlers and make
 * sure it does not re-excute itself when it triggers the handlers.
 *
 * If the dirty check should be ignored for those special cases (i.e. automatically logging someone out) you can set the
 * $(selector).data( 'ignoreDirtyCheckOneTime', true ) to bypass dirty checking one time.
 *
 * This function was derived from the jQuery confirm plug-in.
 */
jQuery.fn.dirtyCheck = function(options) {
    options = jQuery.extend({
        isDirty: function() {
            return false;
        },
        no: options.callback,
        eventType: "click",
        notifications: notifications
    }, options);

    var type = options.eventType;

    return this.each(function() {
        var target = this;
        var $target = jQuery(target);

        if ( type == "beforeunload" || type == "unload" ) {
            $target.bind( "beforeunload", function ( e ) {
                if ( options.isDirty() == true )
                    return $.i18n.prop( "notification.dirtyCheck.beforeUnload.promptMessage" );

                return undefined;
            });

            return; // beforeunload is handled differently then all other events b/c of browser behavior
        }

        var saveHandlers = function() {
            var events = jQuery( target ).data( 'events' );
            if (!events && target.href) {
                // No handlers but we have href

                // We'll add a click event to provide the same expected functionality as having a 'href' on a target
                $target.bind( "click", function() {
                    document.location = target.href;
                });
                events = jQuery.data(target, 'events');
            } else if (!events) {
                // There are no handlers to save.
                return;
            }

            if (!target._handlers) {
                target._handlers = new Array();
            }

            if (events[type]) {
                for (var i = 0; i < events[type].length; i++) {
                    target._handlers.push(events[type][i]);
                }
            }
        };

        var executeExistingHandlers = _.bind( function() {
            // Rebind the saved handlers.
            if (!_.isUndefined(target._handlers)) {

                // We'll remove the dirtyCheck handler in preparation to execute the existing handlers
                $target.unbind( type, handler );

                // Rebind the stored handlers
                jQuery.each(target._handlers, function(indexInArray, valueOfElement) {
                    $target.bind( type, valueOfElement.handler );
                });

                // Fire the event
                $target.trigger( type );

                // Unbind everything we just setup in the binds before
                $target.unbind( type );

                // And setup the default dirty check handler.
                $target.bind( type, handler );
            }
        }, this );


        var handler = function() {
            setTimeout( function() { // let blur handlers proceed before checking dirty
                if (!jQuery.data( target, 'ignoreDirtyCheckOneTime' ) && options.isDirty()) {
                    var n = new Notification( {message: $.i18n.prop("js.notification.dirtyCheck.message"), type:"warning", promptMessage: $.i18n.prop("js.notification.dirtyCheck.promptMessage")} );

                    n.addPromptAction( $.i18n.prop("js.notification.dirtyCheck.cancelActionButton"), function() {
                        options.notifications.remove( n );

                        if (options.cancelCallback) {
                            options.cancelCallback();
                        }
                    });

                    n.addPromptAction( $.i18n.prop("js.notification.dirtyCheck.doNotSaveActionButton"), function() {
                        options.notifications.remove( n );
                        options.no( { callback:executeExistingHandlers });
                    });

                    n.addPromptAction( $.i18n.prop("js.notification.dirtyCheck.saveActionButton"), function() {
                        options.save( {
                            callback: function() {
                                executeExistingHandlers();
                            }
                        });

                        options.notifications.remove( n );
                    });

                    options.notifications.addNotification( n );
                }
                else {
                    executeExistingHandlers();
                }

                if (jQuery.data( target, 'ignoreDirtyCheckOneTime' )) {
                    delete jQuery.data().ignoreDirtyCheckOneTime;
                }
            }, 200);

            // We'll always return false and let the handling of the notification and existing handlers do their thing.
            return false;
        };

        saveHandlers();

        // Remove the type.
        $target.unbind(type);

        $target.bind( type, handler );
    });
}
