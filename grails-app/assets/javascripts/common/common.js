/*********************************************************************************
 Copyright 2009-2018 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/
//IE fix to support indexOf method on Array objects
Array.prototype.indexOf=[].indexOf||function(a,b,c,r) {
    for(b=this,c=b.length,r=-1;~c;r=b[--c]===a?c:r);
    return r;
}

function encodeHTML(string) {
    if (string == null) return null;
    return string.toString().replace(/&(?!\w+;|#\d+;|#x[\da-f]+;)/gi, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#x27;').replace(/\//g,'&#x2F;');
}

function getUrl(name) {
    var url = $("#endpoint-" + name).attr('data-endpoint');

    if (url === null || url === undefined) {
        log.debug("#endpoint-" + name + " was not found or does not include a data-endpoint.");
    }

    return url;
}


function validateDate( dateString ) {
    var defaultCalendar = $.i18n.prop("default.calendar");
    var result = false;
    try {
        if($.multicalendar.isValidDateFormat(defaultCalendar, dateString)) {
            result = true;
        }
    } catch (e) {
        result = false;
    }

    return result;
}


/**
 * $(ele).screenReaderLabel( text, ariaLive );
 *
 * set the label that will be read by a screen reader for this element.
 *
 * text - label text
 * ariaLive - off (default), polite, assertive - will only be applied
 * when the label is first created. If you need to change the ariaLive
 * setting for a label, remove the label and the link attribute and
 * create a new one.
 * ariaLink - aria-labelledby (default) or aria-describedby
 *
 * Returns the original jQuery object.
 */
$.fn.screenReaderLabel = (function(){
    var counter = 0;
    return function( text, ariaLive, ariaLink) {
        var $el = this,
            ariaLink = ariaLink || 'aria-describedby',
            $label = getLabel($el, ariaLink) || createLabel($el, ariaLive, ariaLink);

        function getLabel($el, ariaLink) {
            var id = $el.attr(ariaLink);
            return id ? $('#' + id) : null;
        }

        function createLabel($el, ariaLive, ariaLink) {
            var id = 'screen-reader-label-' + ++counter;
            var live = ariaLive ? ' aria-live="' + ariaLive + '"' : "";
            var $label = $('<span style="display:none" role="region" id="' + id + '"' + live + '></span>').screenReaderOnly();
            $('body').append( $label );

            $el.attr(ariaLink, id);
            return $label;
        }

        $label.text( text );
        return $el;
    }
})();

/**
 * Hide the element and optionally its descendants, from the screen
 * reader.
 *
 * element - an element, jquery object, or a selector
 * recursive - boolean to hide descendants or not. Use with caution - if the contained
 * elements can receive keyboard focus, it can cause problems on some
 * browser/screen reader combinations. google aria-hidden for more
 * information.
 *
 * Returns a jQuery object of the element for chaining.
 */
$.fn.screenReaderHide = function( recursive ) {
    if ( recursive ) {
        this.attr('aria-hidden', 'true')
    } else {
        this.attr('role', 'presentation');
    }

    return this;
}

/**
 * mark content to only be read by screen reader, not displayed visually
 * Returns jQuery object for element
 */
$.fn.screenReaderOnly = function() {
    return this.addClass( 'screen-reader');
}


window.InactivityTimer = ActivityTimer.extend({
    timeoutHandler: function() {
        var n = new Notification( {
            message: $.i18n.prop("js.notification.inactivityTimer.message"),
            type:"warning",
            promptMessage: $.i18n.prop("js.notification.inactivityTimer.promptMessage")
        });

        CommonContext.removeNotification=n; //// putting notification as a callback to close the notification when keepalive message received from application navigator

        this.logoutAction = function( event ) {
            // Logout the user
            log.debug( "logout due to inactivity" );

            if (event && event.type === "click") {
                window.location = 'logout';
            }
            else {
                window.location = "logout/timeout";
            }
        };

        this.keepAliveAction = function() {
            $.ajax({
                url: $("meta[name='keepAliveURL']").attr( "content" ),
                dataType: "html",
                success: function(data, textStatus, jqXHR) {
                    inactivityTimer.reset();
                    notifications.remove( n );
                }
            });
        };

        _.bindAll( this, "logoutAction", "keepAliveAction" );

        n.addPromptAction( $.i18n.prop("js.notification.inactivityTimer.keepAliveButton"), this.keepAliveAction );
        n.addPromptAction( $.i18n.prop("js.notification.inactivityTimer.logoutButton"), this.logoutAction );

        notifications.addNotification( n );

        // Create another timer that will monitor of the notification is still in the notifications collection.
        // If it is when the timer fires, we are goign to automatically call the logoutAction.

        window.setTimeout( _.bind( function() {
            if (notifications.find( function( notification ) { return notification === n; })) {
                this.logoutAction();
            }
        }, this ),
            60 * 1000 /** 60 seconds */ );

    },
    activityEvents: [ "ajaxStart" ],
    activityTarget: function() { return $(document); }
});


var inactivityTimer = new InactivityTimer({
    delay: (parseInt( $("meta[name='maxInactiveInterval']").attr( "content" ) ) - 90 /** Subtract 90 seconds to give a notification prior session invalidating **/ ) * 1000
});

// Calling Timeout after the notification component is ready and fired the event
$(document).bind('notification-use-ready', function (e) {
    inactivityTimer.start();
    CommonContext.resetInActivityTimer=inactivityTimer; // putting inactivity timer as a callback to reset when keepalive message received from application navigator
});


// #################################################################################################################
// Name: SaveTimer
// Type: JavaScript 'class' used to manage the actions involved with prompting the user to save over an interval.
//       The use of 'this' is a bit much in here, but was necessary to manage the scopes of 'this'.  I suspect
//       this can be simplified, but have validated this works.
// Req:  This requires notification center to be usable.
// #################################################################################################################
window.SaveTimer = ActivityTimer.extend({
    initialize: function( options ) {
        log.debug( "init SaveTimer" );
    },
    timeoutHandler: function() {
        var isDirty = this.get( "isDirty" );
        var saveAction = this.get( "saveAction" );

        if (isDirty()) {
            this.save = function() {
                saveAction();
            };

            var n = new Notification( {
                message: "",
                type:"warning",
                promptMessage: $.i18n.prop("js.notification.savePrompt.message")
            });

            this.doNotSavePromptAction = function() {
                notifications.remove( n );

                if (isDirty()) {
                    // Reset the timer
                    this.reset();
                }
            };

            this.savePromptAction = function() {
                this.save();
                notifications.remove( n );
                this.stop();
            };

            _.bindAll( this, "doNotSavePromptAction", "savePromptAction" );

            n.addPromptAction( $.i18n.prop("js.notification.savePrompt.doNotSaveActionButton"), this.doNotSavePromptAction );
            n.addPromptAction( $.i18n.prop("js.notification.savePrompt.saveActionButton"), this.savePromptAction );

            notifications.addNotification( n );
        }
    },
    activityEvents: [ "mousemove", "keypress" ],
    activityTarget: function() { return $("body"); }
});


function showLoading( target ) {
    var t = $(target);
    var offTop = (undefined === t[0] ? 0 : t[0].offsetTop);
    var offLeft = (undefined === t[0] ? 0 : t[0].offsetLeft);

    var loading = t.append( '<div class="loading loading-pending">' ).find( '.loading' )
        .attr("aria-label", $.i18n.prop("js.net.hedtech.banner.ajax.loading"))
        .attr("aria-live", "assertive").attr("aria-busy","true");

    // $.offset() includes the top nav bar's height, so find position manually
    var pos = {top:offTop + $(window).scrollTop(), left:offLeft };

    loading.css(pos).height(t.outerHeight()).width(t.outerWidth());

    setTimeout(
        function() {
            $(target).find( 'div.loading-pending' ).fadeIn( 200, function() {
                $(this).removeClass( 'loading-pending' );
            });
        }, 750
    );
}

function hideLoading( target ) {
    $(target).find('div.loading').fadeOut( 200, function() { $(this).remove(); } )
}


/**
 * @deprecated to be consolidated with showLoading
 */
function showLoadingPopup( target ) {
    var t = $(target);

    var loading = t.append( '<div class="loading loading-pending">' ).find( '.loading' )
        .attr("aria-label", $.i18n.prop("js.net.hedtech.banner.ajax.loading"))
        .attr("aria-live", "assertive").attr("aria-busy","true");
    var pos = {top: $(window).scrollTop(), left: 0 };
    var height = (t[0] && t[0].scrollHeight > t.outerHeight() ? t[0].scrollHeight : t.outerHeight() );
    loading.css(pos).height(height-2).width(t.outerWidth()-2);

    setTimeout(
        function() {
            $(target).find( 'div.loading-pending' ).fadeIn( 200, function() {
                $(this).removeClass( 'loading-pending' );
            });
        }, 500
    );
}

/**
 * @deprecated use hideLoading
 */
var hideLoadingPopup = hideLoading;

/* Usage:
 $(selector).loading();   // show loading indicator
 $(selector).loading(false); // hide loading indicator
 */
$.fn.loading = function(isLoading) {
    (isLoading||isLoading==undefined) ? showLoading( this ) : hideLoading( this );
    return this;
}

/**
 * @deprecated use $.fn.loading
 */
$.fn.loadingPopup = $.fn.loading;


$(document).ajaxError( function(event, jqXHR, ajaxOptions, thrownError) {
    // This cannot detect all failures to provide an error handler, as
    // ajaxmanager or backbone may be wrapping a missing error handler.
    log.debug( "ajaxError url=" + ajaxOptions.url + " thrownError=" + thrownError
        + " status=" + jqXHR.status + " readyState=" + jqXHR.readyState );
    var handledError = ajaxOptions.error || ajaxOptions.complete;
    if ( thrownError !== 'abort' && !handledError ) {
        hideLoading( document );

        var msg = $.i18n.prop("js.net.hedtech.banner.ajax.error.message", [ thrownError ]);
        if ( thrownError == 'timeout' ) {
            msg = $.i18n.prop("js.net.hedtech.banner.ajax.timeout.message");
        }
        var n = new Notification( {
            message: msg,
            type:"error",
            promptMessage: $.i18n.prop("js.net.hedtech.banner.ajax.reload.prompt")
        });

        n.addPromptAction( $.i18n.prop("js.net.hedtech.banner.ajax.reload.button"),
            function() { window.location.reload() });
        n.addPromptAction( $.i18n.prop("js.net.hedtech.banner.ajax.continue.button"),
            function() { notifications.remove( n ); });

        notifications.addNotification( n );

        log.error( msg, jqXHR, ajaxOptions, thrownError );
    }
});

$(document).ready(function() {
    var dir = $('meta[name=dir]').attr("content");

    if (dir == 'rtl') {
        var selector = $(".ui-layout-east");

        if (selector.length) {
            selector.removeClass("ui-layout-east");
            selector.addClass("ui-layout-west");
        }
    }

    $(window).bind({
        'ajaxStart': function(e, xhr, settings) {
            $('.spinner').show();
        },
        'ajaxStop': function(e, xhr, settings) {
            $('.spinner').fadeOut();
        }
    });


    // All ajax requests will use a prefilter to add a request header of the token to ensure
    // that only one request is sent per token eliminating.
    $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
        var synchronizerToken = $("meta[name='synchronizerToken']").attr('content');
        if (synchronizerToken) {
            jqXHR.setRequestHeader("X-Synchronizer-Token", synchronizerToken);
        }
    });


    $(document).ajaxComplete( function(event, jqXHR, ajaxOptions) {
        // We will inspect each response header.  If a customer header exists where we are requested to show the login page
        // we will inform the user that their session is no longer valid and that they have to log back in.
        if (jqXHR.getResponseHeader( "X-Login-Page" ) === "true") {

            var n = new Notification( {
                message: $.i18n.prop("js.net.hedtech.banner.logout.timeout.message"),
                type:"warning",
                promptMessage: $.i18n.prop("js.net.hedtech.banner.logout.timeout.promptMessage")
            });

            n.addPromptAction( $.i18n.prop("js.net.hedtech.banner.logout.timeout.acknowledgement"), function() { window.location.reload() } );

            notifications.addNotification( n );
        }
    });

    // Initialize Aurora
    if ( !_.isUndefined( window[ "CommonPlatform" ] ) && _.isFunction( CommonPlatform.initialize ) ) {
        CommonPlatform.initialize( {
            standalone : true,
            globalNav : true,
            header : true,
            footer : true,
            showHelp: false,
            hideSSBHeaderComps:  false,
            iframe: (window.location != window.parent.location) ? true : false,
            isAppDirty: function(){},
            langDir: $.i18n.prop( "default.language.direction" ),
            resourceMap : {
                skip_link_text :                        $.i18n.prop( "aurora.skip_link_text" ),
                areas_label_browse_description :        $.i18n.prop( "aurora.areas_label_browse_description" ),
                areas_label_browse_title :              $.i18n.prop( "aurora.areas_label_browse_title" ),
                areas_label_home_title :                $.i18n.prop( "aurora.areas_label_home_title" ),
                areas_label_home_description :          $.i18n.prop( "aurora.areas_label_home_description" ),
                areas_label_branding:                   $.i18n.prop( "aurora.areas_label_branding" ),
                areas_label_tools_title :               $.i18n.prop( "aurora.areas_label_tools_title" ),
                areas_label_tools_description :         $.i18n.prop( "aurora.areas_label_tools_description" ),
                openitems_label_closeSelected :         $.i18n.prop( "aurora.openitems_label_closeSelected" ),
                openitems_label_closeAll :              $.i18n.prop( "aurora.openitems_label_closeAll" ),
                preferences_label :                     $.i18n.prop( "aurora.preferences_label" ),
                userdetails_signin :                    $.i18n.prop( "aurora.userdetails_signin" ),
                userdetails_signout :                   $.i18n.prop( "aurora.userdetails_signout" ),
                userdetails_signin_title :              $.i18n.prop( "aurora.userdetails_signin_title" ),
                userdetails_signin_description :        $.i18n.prop( "aurora.userdetails_signin_description" ),
                userdetails_profile_title :             $.i18n.prop( "aurora.areas_label_profile_title" ),
                userdetails_profile_description :       $.i18n.prop( "aurora.areas_label_profile_description" ),
                guestuserdetails_signin :               $.i18n.prop( "aurora.guestuserdetails_signin" ),
                userdetails_help :                      $.i18n.prop( "aurora.userdetails_help" ),
                notification_title :                    $.i18n.prop( "js.notification.title" )
            },
            handler : function( data ) {
            }
        } );
    }

    if ($.browser.msie && 8 == parseInt($.browser.version)) {
        // make text zoom on ie8 trigger resize event, like other browsers
        var deviceXDPI = screen.deviceXDPI;
        setInterval( function() {
            if ( deviceXDPI != screen.deviceXDPI ) {
                deviceXDPI = screen.deviceXDPI;
                $(window).resize();
            }
        }, 200);
    }

    // Prevent Firefox mis-feature of allowing copy of password masking characters.
    $('body').on('copy', 'input[type=password]', function (event) {
        event.preventDefault();
    });

    addCssClass();
    changeFavicon();
});

$(document).bind('notification-use-ready', function (e) {
    showAipNotification();
});

function showAipNotification(){
    if($("meta[name='hasActiveActionItems']").attr( "content" )=== 'true'){
        showAipPromptNotification($("meta[name='aipUrl']").attr( "content" ));
    }
}

function showAipPromptNotification(url) {
    var n = new Notification({
        message: $.i18n.prop("js.banner.general.aip.notification.prompt.message"),
        type: "warning"
    });

    n.addPromptAction($.i18n.prop("js.banner.general.aip.notification.prompt.view.actionitems"), function () {
        window.location.href = url;
    });
    n.addPromptAction($.i18n.prop("js.banner.general.aip.notification.prompt.dismiss"), function () {
        notifications.remove(n);
    });
    notifications.addNotification( n );
}


function changeFavicon() {
    // get the dynamic favicon link, an existing favicon link, or create a link to use
    var ID = 'dynamic-favicon';
    var link = document.getElementById(ID);
    if ( !link ) {
        // must append the link to the document head before we can use getComputedStyle
        link = document.head.appendChild(document.createElement('link'));

        // look for the favicon URL as the background image of link.favicon
        link.className = 'favicon';
    link.rel = 'shortcut icon';
    link.type = 'image/x-icon';
        link.id = ID;
    }

    // get the assigned .favicon background-image URL from the theme stylesheet
    var backgroundImage = window.getComputedStyle(link).getPropertyValue('background-image');

    // check if we have a url(___), possibly with quotes. IE doesn't put quotes around the URL.
    var urlRegex = /^url\("?(.*)"?\)$/;
    if(urlRegex.test(backgroundImage) && backgroundImage.indexOf("$themefavicon") == -1){
        // remove all old links of the various ways they are specified by UXD
        // includes shortcut icon and apple-touch-icon
        var oldLinks = document.querySelectorAll("link[rel*='icon']");
        Array.prototype.forEach.call( oldLinks, function(element) {
            document.head.removeChild(element);
        })

        // replace with a single updated favicon URL
        link.href = backgroundImage.slice(4, -1).replace(/"/g, "");
        document.head.appendChild(link);
    }
}

function addCssClass() {

    if($.browser.safari) {
        checkAndAddClass("webkit");
    }
    if($.browser.mozilla) {
        checkAndAddClass("gecko");
    }
    if($.browser.msie) {
        checkAndAddClass("ie");
    }
}

function checkAndAddClass(browserName) {
    var bodyTag = $('body');
    var browserWithVersion = browserName + parseInt($.browser.version);
    if(!bodyTag.hasClass(browserName)) {
        bodyTag.addClass(browserName);
    }
    if(!bodyTag.hasClass(browserWithVersion)) {
        bodyTag.addClass(browserWithVersion);
    }
}

function formatTitleAndShortcut(title, shortcut) {
    return $.i18n.prop( "net.hedtech.banner.title.shortcut", [title, shortcut] )
        .replace( /&lt;br&gt;/g, '\n' );
}

key = (function(key) {
    key.modifierCode = {
        SHIFT: 1,
        ALT: 2,
        CTRL: 4
        //META: 8 //windows key or command key (mac)
        //MOD: platform-specific, Ctrl on windows/unix or Command on Mac.  See 'mousetrap' library
    };
    key.modifierStrings = {
        'shift':key.modifierCode.SHIFT,
        'alt':key.modifierCode.ALT,
        'ctrl':key.modifierCode.CTRL
    };

    key.keys = {
        backspace: 0x08,
        tab: 0x09,
        'return': 0x0d,
        escape: 0x1b,
        space: 0x20,
        pageUp: 0x21,
        pageDown: 0x22,
        end: 0x23,
        home: 0x24,
        left: 0x25,
        up: 0x26,
        right: 0x27,
        down: 0x28,
        insert: 0x2d,
        'delete': 0x30,
        f1: 0x70,
        f2: 0x71,
        f3: 0x72,
        f4: 0x73,
        f5: 0x74,
        f6: 0x75,
        f7: 0x76,
        f8: 0x77,
        f9: 0x78,
        f10: 0x79,
        f11: 0x7a,
        f12: 0x7b,
        '`':0xc0,
        '-':0xbd,
        '=':0xbb,
        '[':0xdb,
        ']':0xdd,
        '\\':0xdc,
        ';': 0xba,
        '\'': 0xde,
        ',': 0xbc,
        '.': 0xbe,
        '/': 0xbf
    };

    key.KeyException = function (message) {
        this.message = message;
        this.name = "KeyException";
    }

    key.parseModifiers = function( shortcut ) {
        var modifiers = 0,
            words = shortcut.split('+');
        words.pop();

        _.each( words, function( word ) {
            var value = key.modifierStrings[ word.toLowerCase() ];
            if ( value ) {
                modifiers |= value;
            } else {
                throw new key.KeyException( "Unknown modifier '" + word + "' in shortcut '" + shortcut + "'" );
            }
        });
        return modifiers;
    };


    key.parseKey = function( shortcut ) {
        var word = shortcut.split('+').pop(),
            code = key.keys[word.toLowerCase()];
        if ( !code ) {
            if ( word.length > 1 ) {
                throw new key.KeyException( "Unknown key name '" + word + "' in shortcut '" + shortcut + "'" );
            } else {
                code = word.toUpperCase().charCodeAt(0);
            }
        }
        return code;
    };


    var BoundKey = function( shortcut, handler ) {
        this.modifiers = key.parseModifiers( shortcut );
        this.code = key.parseKey( shortcut );
        this.handler = handler;
    };

    key.boundKeys = [];

    key.eventHandler = function( event ) {
        _.each( key.boundKeys, function( boundKey ) {
            if ( boundKey.code != event.keyCode ) {
                return;
            }
            if ( boundKey.modifiers != modifierKeysPressed(event) ) {
                return;
            }
            boundKey.handler( event );
        });
    };

    function modifierKeysPressed(event){
        return (( event.shiftKey && key.modifierCode.SHIFT ) | ( event.altKey && key.modifierCode.ALT ) | ( event.ctrlKey && key.modifierCode.CTRL ));
    }

    /**
     * bind shortcut & handler pairs.
     * key.bind( 'shift+home', homeFunction, 'alt+m', menuFunction, ... )
     *   or
     * key.bind( ['shift+home', homeFunction, 'alt+m', menuFunction, ...] )
     */
    key.bind = function() {
        var shortcuts = arguments.length > 1 ? arguments : arguments[0];
        var i = 0;
        if ( !key.boundKeys.length ) {
            // register page-level event handler only once
            $(document).on( 'keyup', key.eventHandler );
        }

        for ( ; i < shortcuts.length; i += 2 ) {
            shortcut = shortcuts[ i ];
            handler = shortcuts[ i+1 ];
            key.boundKeys.push( new BoundKey( shortcut, handler ));
        }
    };

    return key;
})(window.key || {});

function getNextTabbableElement( currentElement , container) {
    var tabbableElements = getTabbableElements(container);
    var currentIndex = tabbableElements.index(currentElement);
    var nextTabbableElement = tabbableElements.eq(currentIndex + 1);
    return nextTabbableElement;
}

function getPreviousTabbableElement( currentElement , container) {
    var tabbableElements = getTabbableElements(container);
    var currentIndex = tabbableElements.index(currentElement);
    var previousTabbableElement = tabbableElements.eq(currentIndex - 1);
    return previousTabbableElement;
}

function getTabbableElements(container){
    return  _.isUndefined(container) ?  $(":tabbable") : container.find(":tabbable");
}

var isMobile = function(){
    var isMobile = window.matchMedia("only screen and (min-width: 0px) and (max-width: 768px)");
    if (isMobile.matches) {
        return true;
    }
    return false;
}

var isTablet = function(){
    var isTablet = window.matchMedia("only screen and (min-width: 769px) and (max-width:1024px)");
    if (isTablet.matches) {
        return true;
    }
    return false;
}

var isDesktop = function(){
    var isDesktop = window.matchMedia("only screen and (min-width:1025px)");
    if (isDesktop.matches) {
        return true;
    }
    return false;
}

var isRTLMode = function(){
    var result = false;
    if($.i18n.prop('default.language.direction') == "rtl"){
        return true
    }
    return result;
}

var isLTRMode = function(){
    var result = false;
    if($.i18n.prop('default.language.direction') == "ltr"){
        return true
    }
    return result;
}



var DirtyCheck = {

    addDirtyCheckCallback: function( isDirty ) {
        CommonContext.isAppDirty=isDirty;
    },
    isDirty : function() {
        return CommonContext.isAppDirty();
    }
}

function mepcodeNotificationAddition(sessionMepCode){
    var mepDescription = $('meta[name=ssbMepDesc]').attr("content");
    var n = new Notification( {
        message: $.i18n.prop('js.notification.mep.changed.onLoad', [mepDescription]),
        type:"warning"
    });

    var logoutMepUser = function( event ) {
        window.location = 'logout';
    };

    var ignoreMepChangeAction = function() {
        notifications.remove( n );
    };

    n.addPromptAction( $.i18n.prop("js.notification.mep.buttons.continue"), ignoreMepChangeAction );
    n.addPromptAction( $.i18n.prop("js.notification.mep.buttons.logout"), logoutMepUser );

    notifications.addNotification(n);
}
