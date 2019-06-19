/*********************************************************************************
 Copyright 2011-2018 Ellucian Company L.P. and its affiliates.
 **********************************************************************************/

$(document).ready(function() {

    window.Notification = Backbone.Model.extend({
        save: function() {
            throw new Error("A notification object cannot be persisted.  Save Not Supported");
        },
        addPromptAction: function(label, action) {
            var prompts = this.get( "prompts" );

            if (!prompts) {
                prompts = [];
                this.set( {prompts: prompts} );
            }

            prompts.push({label: label, action: action});
        },
        hasPrompts: function() {
            var prompts = this.get( "prompts" );

            return prompts && prompts.length > 0;
        },
        isEqual: function( that ) {

            var comparisonAttributes = [ "message", "type", "attribute", "ignoreForGroupBy" ]
            var returnValue = true;

            _.each( comparisonAttributes, function(attribute) {
                if (returnValue && (!_.isEqual(this.get( attribute ),that.get( attribute )))) {
                    returnValue = false;
                }
            }, this );

            return returnValue;
        },
        prompts: []
    });

    window.Notifications = Backbone.Collection.extend({
        initialize: function () {
            this.bind('add', this.addComponentErrorStyle, this);
            this.bind('remove', this.removeComponentErrorStyle, this);
        },
        model: Notification,
        addNotification: function( notification ) {
            var foundNotification = this.find( function(n) {
                if(notification.get("type")=="error")
                    return false;
                else
                    return notification.isEqual(n);
            });

            if (foundNotification) {
                window.notificationCenter.openNotificationFlyout();
                this.remove( foundNotification );
                this.add( notification );
                if (notification.get( "flash" )) {
                    var removeNotification = function(event) {
                        if(event.type =="keydown"){
                            if(!(event.altKey || event.keyCode == 9 || event.shiftKey  ||  (event.altKey && event.keyCode ==78))){
                                this.remove( notification );
                            }
                        }
                        else
                            this.remove( notification );
                    };

                    removeNotification = _.bind( removeNotification, this );
                    $('body').on('mousedown keydown', removeNotification);
                }
                return foundNotification;
            }
            else {
                this.add( notification );
                // If the notification gets orphaned remove it from the collection.
                if (notification.has( "model" ) && notification.get( "model" )) {
                    var model = notification.get( "model" );

                    notification.get("flash") || model.collection.bind( "reset", function(models) {
                        if (_.isUndefined( model.collection )) {
                            this.remove( notification );
                        }
                    }, this );
                }

                // If the notification is a flash, we are going to set to remove it automagically.
                if (notification.get( "flash" )) {
                    var removeNotification = function(event) {
                        if(event.type =="keydown"){
                            if(!(event.altKey || event.keyCode == 9 || event.shiftKey  ||  (event.altKey && event.keyCode ==78)) ){
                                this.remove( notification );
                            }
                        }
                        else
                            this.remove( notification );
                    };

                    removeNotification = _.bind( removeNotification, this );
                    $('body').on('mousedown keydown', removeNotification);

                }
                return notification;
            }
        },
        fetch: function() {
            throw new Error("not supported");
        },
        clearNotifications: function( model, attributes ) {
            if (model) {
                // Remove all notifications that are bound to the model
                var notificationsToRemove = this.filter( function( n ) {
                    if (n.get( "flash" )) {
                        return false; // allow flash notifications to persist until they time out or are explicitly removed
                    }

                    var notificationModel = n.get( "model" );
                    if (notificationModel && notificationModel.id === model.id) {
                        if (attributes) {
                            if (n.has( "attribute" )) {
                                // We are going to only select the the notifications that have the same model and attribute
                                var keys = _.keys( attributes );

                                var found = _.find( keys, function(k) {
                                    return k === n.get( "attribute" );
                                });

                                // If we got to this point we do not want to mark this notification for removal because none of the attributes are in the notification.
                                return found;
                            }
                            else {
                                // This specific notification does not have an attribute so we will not include it since the request to clear notifications was by model and attributes.
                                return false;
                            }
                        }
                        else {
                            // We are not inspecting attributes and will return this notification.
                            return true;
                        }
                    }
                });

                if (notificationsToRemove) {
                    _.each( notificationsToRemove, function( n ) {
                        this.remove( n );
                    }, this );
                }
            }
            else {
                _.each(this.models,function(model){
                    notifications.removeComponentErrorStyle(model);
                });
                // Remove all models
                this.remove(this.models);
            }
        },
        addNotificationsFromModel: function(model) {
            if (model) {
                function evaluateModel(model) {
                    this.clearNotifications( model );

                    if (model.has && model.has("messages")) {
                        _.each(model.get("messages"), function(message) {

                            var notification = new Notification( {message: message.message, type: message.type, model: model, attribute: message.field } );

                            if (message.type === "success") {
                                notification.set( {
                                    flash: true,
                                    message: $.i18n.prop("js.notification.success"),
                                    ignoreForGroupBy: ["model"]
                                } );
                            }

                            this.addNotification( notification );

                            model.bind( "change:messages", function( m ) {
                                // Reset the notifications that are associated with the model that has new or updated messages
                                var associatedNotifications = _.select( notifications.models, function(n) {
                                    return n.get( "model" ) === m;
                                });

                                _.each( associatedNotifications, function( n ) {
                                    n.get( "flash" ) || notifications.remove( n );
                                });
                            }, this );
                        }, this);
                    } else {
                        var notification = new Notification({message: $.i18n.prop("js.notification.NoChangesToSave"), type: "warning", model: model, attribute: "" });
                        notification.set({
                            flash: true,
                            ignoreForGroupBy: ["model"]
                        });
                        this.addNotification(notification);
                    }
                }

                evaluateModel = _.bind(evaluateModel, this);

                if (!model.models) {
                    evaluateModel(model);
                }
                else if (model.models.length === 0) {
                    evaluateModel(model.models);
                }
                else {
                    _.each(model.models, function(m) {
                        evaluateModel(m);
                    });
                }
            }
        },
        hasErrors: function() {
            return this.find( function(model) { return model.get( "type" ) === "error" } );
        },
        hasFlash: function() {
            return this.find( function(model) { return model.get( "type" ) === "success" } );
        },
        hasPrompt: function() {
            return this.find( function(model) { return model.hasPrompts() } );
        },
        addComponentErrorStyle: function( notification ) {
            var errorComponent = this.getNotificationComponent(notification);
            if(!_.isUndefined(errorComponent)){
                $(errorComponent).addClass("component-error");
            }
        },
        removeComponentErrorStyle: function( notification ) {
            var errorComponent = this.getNotificationComponent(notification);
            if(!_.isUndefined(errorComponent)){
                $(errorComponent).removeClass("component-error");
            }
        },
        getNotificationComponent: function( notification ){
            var notificationType = notification.get("type");
            if (notificationType == "error") {
                var errorComponent = notification.attributes.component;
            }
            return errorComponent;
        }
    });

    window.notifications = new Notifications;

    window.NotificationView = Backbone.View.extend({
        tagName: "li",
        className: "notification-item",
        initialize: function() {
            _.bindAll(this, "render", "removeNotification" );
            this.model.collection.bind("remove", this.removeNotification);
        },
        render: function() {
            //  Evaluate the notification type to determine what css class to append to the notification.
            var notificationType = this.model.get("type");
            var view = this;
            var notificationMessage = '' + this.model.get("message" );
            var messageContainer = $("<span></span>");
            var component = this.model.get("component");
            var flashContainer = $('.aria-flashnotification-screen-reader');
            if (flashContainer.length === 0) {
                var ariaRegion1 = $('<span></span>');
                ariaRegion1.attr('role', 'status');
                ariaRegion1.attr('aria-live', 'assertive');
                ariaRegion1.attr('class', 'aria-flashnotification-screen-reader');
                $(document.body).append(ariaRegion1);
            }

            if(notificationType=="error" && component){
                messageContainer = $("<a tabindex='0' role='link' class='notification-flyout-item'></a>");
                messageContainer.addClass('notification-message');
                messageContainer.on('click', function(){
                    view.navigateToErrorComponent(view.model);
                });
                messageContainer.on('keydown', function(e) {
                    if (e.keyCode == $.ui.keyCode.ENTER || e.which == $.ui.keyCode.ENTER) {
                        view.navigateToErrorComponent(view.model);
                        e.preventDefault();
                    }
                });
            }

            var messageDiv = $("<div></div>").addClass( "notification-item-message vertical-align" ).html(messageContainer.append( notificationMessage ) );

            // Manage the prompts if available
            var promptsDiv;
            if (this.model.hasPrompts()) {

                $(this.el).addClass( "notification-center-message-with-prompts" );

                var promptMessage = this.model.get( "promptMessage" );
                if (promptMessage) {
                    messageContainer.append( promptMessage );
                }

                promptsDiv = $( "<div></div>" ).addClass( "notification-item-prompts" );

                _.each(this.model.get( "prompts" ), function(prompt) {
                    var b = $("<button></button>").html( prompt.label )
                        .on('click', function(){
                            notificationCenter.closeNotificationFlyout();
                            prompt.action();
                        });
                    b.addClass('notification-flyout-item');
                    b.addClass('secondary');
                    promptsDiv.append( b );
                }, this );
            }

            $(this.el).append("<span class='notification-icon'></span>");
            $(this.el).append( messageDiv );


            if (promptsDiv) {
                $(this.el).append( promptsDiv );
            }
            var notificationStyleClass = {error:"notification-center-message-error",warning:"notification-center-message-warning",success:"notification-center-message-success"};
            $(this.el).addClass( notificationStyleClass[notificationType] );

            var screenReaderText = {error: $.i18n.prop("screenreader.notification.error"),warning:$.i18n.prop("screenreader.notification.warning"),success:$.i18n.prop("screenreader.notification.flash")};
            var ariaLabelledbyText = screenReaderText[notificationType] ;

            if(notificationType == "error"){
                messageContainer.screenReaderLabel( ariaLabelledbyText + " " + notificationMessage, "off", "aria-labelledby");
            }
            else if(notificationType == "warning"){
                var actionButton = promptsDiv.find('button:first');
                actionButton.removeClass('secondary');
                actionButton.addClass('primary');
                actionButton.screenReaderLabel( ariaLabelledbyText + " " + notificationMessage+ " " + actionButton.text(), "off", "aria-labelledby");
            }
            else{
                var ariaNotificationItemTextElement = "<b class='offscreen' id='ariaNotificationCountText'> "+ ariaLabelledbyText +"</b>";
                $(messageContainer).addClass('notification-flyout-item')
                    .attr('tabindex','0')
                    .prepend(' '+ariaNotificationItemTextElement);
            }

            return this;
        },
        removeNotification: function(notification) {
            if (this.model === notification) {
                $(this.el).fadeOut( 1000 ).remove();
            }
        },
        navigateToErrorComponent: function(model) {
            if($('body .notification-center-shim').length == 0) {
                var component = model.attributes.component;
                if (component) {
                    if (model.attributes.componentType == "select2" && !component.hasClass('select2-focusser')) {
                        component = component.find('.select2-focusser');
                    }
                    window.notificationCenter.closeNotificationFlyout();
                    window.componentToFocusOnFlyoutClose = null;
                    component.focus();
                }
            }
        }
    });


    // The notification center will instantiate a notificationCenterAnchor and a notificationCenterFlyout.

    // The notification center will be responsible for capturing events that come from the child components
    // and act as a controller.  E.g. is that the notificationCenter will listen for a click event on the
    // notificationCenterAnchor and instruct the flyout to either open or close.


    window.NotificationCenterAnchor = Backbone.View.extend({
        initialize: function() {
            $(this.el).addClass( "notification-center-anchor" ).addClass( "notification-center-anchor-hidden");
            var notificationCountDiv = $('<div class="notification-center-count vertical-align"><span/></div>' );
            $(this.el).screenReaderLabel( $.i18n.prop("js.notification.description") );
            $(this.el).append( notificationCountDiv );
            _.bindAll(this, "render", "isDisplayed", "display", "hide");

            this.model.bind("add", this.render);
            this.model.bind("remove", this.render);
            this.model.bind("reset", this.render);

            this.render();
        },
        render: function() {
            var notifications = this.model.models;

            if (notifications.length > 0) {
                $(this.el).removeClass( "notification-center-anchor-hidden");
                $("#notification-center").removeClass("notification-center-hidden").addClass("notification-center-displayed");
            }
            else {
                $(this.el).addClass( "notification-center-anchor-hidden");
                $("#notification-center").removeClass("notification-center-displayed").addClass("notification-center-hidden");
            }

            var ariaCountLabel = "<b class='offscreen'>"+$.i18n.prop("js.notification.label")+"</b>";
            $(".notification-center-count span", this.el).html(notifications.length).append(ariaCountLabel);
            return this;
        },
        isDisplayed: function() {
            return $(this.el).hasClass( "notification-center-anchor-displayed" );
        },
        display: function() {
            $(this.el).addClass( "notification-center-anchor-displayed" ).removeClass( "notification-center-anchor-hidden" );
            return this;
        },
        hide: function() {
            $(this.el).addClass( "notification-center-anchor-hidden" ).removeClass( "notification-center-anchor-displayed" );
            return this;
        }
    });


    window.NotificationCenterFlyout = Backbone.View.extend({
        events: {
            "keydown .notification-flyout-item:first":"focusLastMessageItem",
            "keydown .notification-flyout-item:last":"focusFirstMessageItem"
        },

        navigateToErrorComponent: function () {
            if($('body .notification-center-shim').length == 0) {
                this.navigateToErrorComponent(this.model);
            }
        },

        initialize: function() {
            $(this.el).addClass( "notification-center-flyout" ).addClass( "notification-center-flyout-hidden" );

            _.bindAll(this, "render", "isDisplayed", "display", "hide","addAriaDescription","addNotificationToFlyout");
            this.model.bind("add", this.addNotificationToFlyout);
        },
        addNotificationToFlyout: function(notification) {
            var view = new NotificationView( {model:notification} );
            var type = notification.get("type");
            var notificationCollection = this.model.models;
            //In case of flash messages aria description regarding shortcuts is not required
            if (!(notificationCollection.length == 1 && notificationCollection[0].get("flash"))) {
                this.addAriaDescription(notificationCollection.length);
            }

            if(type == "success"){
                $(".notification-center-count span").removeAttr('role');
            }
            else{
                $(".notification-center-count span").attr('role','alert');
            }

            var listItem = view.render().el;
            if(type == "warning"){
                $("ul.prompt-container", this.el).prepend( listItem);
            }
            else if(type== "error"){
                $("ul.error-container", this.el).prepend( listItem);
            }
            else{
                $("ul.flash-container", this.el).prepend( listItem);
                window.elementToFocusOnFlash = notification.get("elementToFocus") || document.activeElement;
            }
            return this;
        },
        isDisplayed: function() {
            return $(this.el).hasClass( "notification-center-flyout-displayed" );
        },
        addAriaDescription: function(count) {
            if ($(this.el).children('#notificationCenterAriaInfo').length == 0 ) {
                //First time notification info.
                var notificationCenterAriaInfo = "<p tabindex='-1' class='offscreen' role='alert'  id='notificationCenterAriaInfo'>";
                notificationCenterAriaInfo +=$.i18n.prop("js.notification.countlabel",[count]);
                notificationCenterAriaInfo += $.i18n.prop("js.notification.help");
                notificationCenterAriaInfo +="</p>";
                $(this.el).prepend(notificationCenterAriaInfo);
                $(this.el).attr('aria-describedby','notificationCenterAriaInfo');
            } else {
                //Further time notification info.
                $(this.el).children('#notificationCenterCountAriaInfo').remove();
            }

        },
        display: function() {
            $(this.el).addClass( "notification-center-flyout-displayed" ).removeClass( "notification-center-flyout-hidden" );


            return this;
        },
        hide: function() {
            $(this.el).addClass( "notification-center-flyout-hidden" ).removeClass( "notification-center-flyout-displayed" );
            return this;
        },
        focusLastMessageItem: function(e){
            if (e.keyCode == $.ui.keyCode.TAB && e.shiftKey) {
                $('.notification-flyout-item:last').focus();
                e.preventDefault();
            }
        },
        focusFirstMessageItem: function(e){
            if (e.keyCode == $.ui.keyCode.TAB && !e.shiftKey) {
                $('.notification-flyout-item:first').focus();
                e.preventDefault();
            }
        }
    });


    window.NotificationCenter = Backbone.View.extend({
        events: {
            "click .notification-center-anchor":"toggle",
            "keydown .notification-center-anchor":"toggleIfEnterPressed"

        },
        initialize: function() {
            var self  = this;
            $(this.el).addClass("notification-center");
            $(this.el).append( '<a tabindex="0" class="notification-center-anchor"></a>' );
            $(this.el).append( '<div class="notification-center-flyout" tabindex="0"><ul class="flash-container" role="alert" aria-live="assertive"/><ul class="prompt-container" /><ul class="error-container"/></div>' );


            this.notificationCenterAnchor = new NotificationCenterAnchor({el: $(".notification-center-anchor", this.el), model: this.model });
            this.notificationCenterFlyout = new NotificationCenterFlyout({el: $(".notification-center-flyout", this.el), model: this.model, parent: this.el });

            _.bindAll(this, 'render', 'addNotification', 'removeNotification', 'toggle','pressEscToClose','closeNotificationFlyout','closeNotificationFlyoutAndSetFocus','configureNotificationOverlay','checkAndCloseFlyout');
            this.model.bind("add", this.addNotification);
            this.model.bind("remove", this.removeNotification);

            document.addEventListener('mousedown',this.setComponentToFocusOnFlyoutClose,true);
            document.addEventListener('keydown',this.setComponentToFocusOnFlyoutClose,true);
            $.event.trigger('notification-use-ready');
        },
        render: function() {
            return this;
        },
        addNotification: function(notification) {
            this.openNotificationFlyout();
            this.configNotificationShim();
            return this;
        },
        removeNotification: function(notification) {
            if (this.model.length == 0) {
                this.closeNotificationFlyout();
            }

            this.configNotificationShim();

            return this;
        },
        toggle: function() {
            if (this.notificationCenterFlyout.isDisplayed()) {
                this.closeNotificationFlyoutAndSetFocus();
            }
            else {
                if(this.model.length > 0){
                    this.openNotificationFlyout();
                }
                else{
                    this.notificationCenterAnchor.$el.focus();
                }
            }
            return this;
        },

        toggleIfEnterPressed: function(e) {
            if (e.keyCode == $.ui.keyCode.ENTER || e.which == $.ui.keyCode.ENTER) {
                this.toggle ();
            }
        },

        openNotificationFlyout: function () {
            var promptElementToFocus = $('.prompt-container .notification-flyout-item:first');
            var errorElementToFocus = $('.error-container .notification-flyout-item:first');
            var flashElementContent = $('.flash-container .notification-flyout-item:first');
            if(window.componentToFocusOnFlyoutClose == null){
                window.componentToFocusOnFlyoutClose = $(document.activeElement);
            }
            this.notificationCenterAnchor.display();
            this.notificationCenterFlyout.display();
            this.configureNotificationOverlay();
            $('.notification-center-flyout')[0].addEventListener('keydown', this.pressEscToClose , true );

            if(flashElementContent.length > 0){
                $(".aria-flashnotification-screen-reader").text('');
                $(".aria-flashnotification-screen-reader").text(flashElementContent.text());
            }


            if(_.isUndefined(notifications.hasFlash())){
                $('.notification-flyout-item:first').focus();
            }
            else{
                if(promptElementToFocus.length) {
                    promptElementToFocus.focus();
                }
                else if(errorElementToFocus.length){
                    errorElementToFocus.focus();
                }
                else{
                    $(elementToFocusOnFlash).focus();
                }
            }
        },
        closeNotificationFlyout: function () {
            this.notificationCenterFlyout.hide();
            var flashElementContent = $('.flash-container .notification-flyout-item:first');
            if(flashElementContent.length > 0) {
                $(".aria-flashnotification-screen-reader").text('');
            }
            $('.notification-center-flyout')[0].removeEventListener('keydown',  this.pressEscToClose, true );
        },
        closeNotificationFlyoutAndSetFocus: function(){
            this.closeNotificationFlyout();
            this.focusComponentOnFlyoutClose();
            window.componentToFocusOnFlyoutClose = null;
        },
        focusComponentOnFlyoutClose: function(){
            if($('body.notification-center-shim').length == 0) {
                window.componentToFocusOnFlyoutClose.focus();
            }
            else{
                this.notificationCenterAnchor.$el.focus();
            }
        },
        pressEscToClose: function(e) {
            if(e.keyCode == $.ui.keyCode.ESCAPE){
                if ($(".notification-center-shim").length >= 0) {
                    this.closeNotificationFlyoutAndSetFocus();
                    e.stopImmediatePropagation();
                }
            }
        },
        configNotificationShim: function() {
            // Check to see if any prompts exist.  If there is a prompt, the user must be forced to address the prompt prior
            // to moving on.
            var target = this.options.shimTarget;

            if (_.isUndefined(target)) {
                target = $("body");
            }

            if (this.model.find( function( n ) { return n.hasPrompts(); } )) {
                if ($(".notification-center-shim", target).length === 0) {
                    target.append( '<div class="notification-center-shim"></div>' );
                }
            }
            else {
                $(".notification-center-shim", target).remove();
            }
        },
        configureNotificationOverlay: function(){
            var closeFlyout = function(event) {
                if($(event.target).closest('.notification-center').length == 0 && $('.notification-center-shim').length == 0) {
                    notificationCenter.closeNotificationFlyout();
                    $('body').off('mousedown', closeFlyout);
                }
            };


        },
        checkAndCloseFlyout: function(event){
            if($(event.target).closest('.notification-center').length == 0){
                this.closeNotificationFlyoutAndSetFocus();
            }
        },
        setComponentToFocusOnFlyoutClose: function(e) {
            if(e.type == "keydown"){
                if ($(e.target).closest('.notification-center').length == 0) {
                    if (e.keyCode == $.ui.keyCode.TAB && e.shiftKey) {
                        window.componentToFocusOnFlyoutClose = getPreviousTabbableElement(e.target);
                    }
                    else if (e.keyCode == $.ui.keyCode.TAB && !e.shiftKey) {
                        window.componentToFocusOnFlyoutClose = getNextTabbableElement(e.target);
                    }
                }
            }
            else{
                window.componentToFocusOnFlyoutClose = this.notificationCenterAnchor;
            }

        }
    });

    if ( !_.isUndefined( window[ "EventDispatcher" ] ) ) {
        EventDispatcher.addEventListener( Application.events.initialized, function() {
            var nc = $("<div><div id='notification-center'></div></div>" );
            //TODO: HRU:5803 cleanup
            //ControlBar.append( nc );
            window.notificationCenter = new NotificationCenter({
                el: $("#notification-center"),
                model: notifications
            });
        });
    }
});
