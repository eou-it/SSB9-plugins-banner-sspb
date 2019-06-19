window.ActivityTimer = Backbone.Model.extend({
    saveTimerId: 0,
    initialize: function( options ) {
        if (!options.delay) {
            this.set( {delay: 1000} );
        }
    },
    start: function() {
        var events = this.activityEvents;

        if (events) {
            _.each( events, function ( event ) {
                this.activityTarget().bind( event, _.bind( this.lazyResetter, this ) );
            }, this );
        }

        this.saveTimerId = window.setTimeout( _.bind( this.timeoutHandler, this ), this.get( "delay" ));

        this.trigger( 'timer:start' );
    },
    stop: function( options ) {
        if (this.saveTimerId) {
            window.clearTimeout( this.saveTimerId );
        }

        var events = this.activityEvents;
        if (events) {
            _.each( events, function ( event ) {
                this.activityTarget().unbind( event, _.bind( this.lazyResetter, this ) );
            }, this );
        }

        this.trigger( 'timer:stop' );
    },
    reset: function( options ) {
        this.stop();
        this.start();
        this.trigger( 'timer:reset' );
    },
    lazyResetter: _.throttle( function(e) {
        if (this.saveTimerId) {
            log.debug( "reseting timer '" + this.saveTimerId + "' due to '" + e.type  + "' type." );
            this.reset();
        }
    }, 1500 )
});