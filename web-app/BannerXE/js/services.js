'use strict';

var extensibilityServiceModule = angular.module('extensibility.services',[]);
extensibilityServiceModule.factory('componentFactory', function() {

    var componentFactoryService = {};

    componentFactoryService.getComponent = function(metadata, pageId, blockId) {
        var comp = '<formitem label="' + metadata.fieldlabel + '" id="' + metadata.fieldId + '">';
        var binding = 'pagedata.' + pageId + '.' + blockId + '.' + metadata.fieldId;
        switch(metadata.type) {
            case  'string' :
                 comp += '<textbox value = "' + binding + '" />';
                            break;
            case 'lookup' :
                comp += '<lookup value = "' + binding + '" />';
                            break;
            case 'servicemodule' :
                comp = '<servicemodule title="' + metadata.widgetattributes.title +'" description ="' +  metadata.widgetattributes.description + '" imageicon="' + metadata.widgetattributes.imageicon + '" populartasks = "' + metadata.widgetattributes.populartasks+ '" />';
                        return comp;
                        break;
            case 'servicelauncher' :
                comp = '<servicelauncher href="' + metadata.widgetattributes.href + '" link="' + metadata.widgetattributes.link +'" description ="' +  metadata.widgetattributes.description + '" imageicon="' + metadata.widgetattributes.imageicon + '"  />';
                        return comp;
                        break;

        }
        comp += '</formitem>';
        return comp;
    };
    return componentFactoryService;
});

var servicesModule = angular.module('popup.service', []);
servicesModule.factory('PopupService', function ($http, $compile)
{
    // Got the idea for this from a post I found. Tried to not have to make this
    // object but couldn't think of a way to get around this
    var popupService = {};

    // Get the popup
    popupService.getPopup = function(create)
    {
        if (!popupService.popupElement && create)
        {
            popupService.popupElement = $( '<div class="modal hide"></div>' );
            popupService.popupElement.appendTo( 'BODY' );
        }

        return popupService.popupElement;
    }

    popupService.compileAndRunPopup = function (popup, scope, options) {
        $compile(popup)(scope);
        popup.modal(options);
    }

    // Is it ok to have the html here? should all this go in the directives? Is there another way
    // get the html out of here?
    popupService.alert = function(title, text, buttonText, alertFunction, scope, options) {
        text = (text) ? text : "Alert";
        buttonText = (buttonText) ? buttonText : "Ok";
        var alertHTML = "";
        if (title)
        {
            alertHTML += "<div class=\"modal-header\"><h1>"+title+"</h1></div>";
        }
        scope.title = "Sample modal title";
        alertHTML += "<div class=\"modal-body\">"+
                        "<form>" +
                            "<formitem label='{{title}}' float=\"-left\">" +
                            "<formitem label='title'><textbox/></formitem>" +
                            "<formitem label=\"" + title + "\"><textbox/></formitem>" +
                            "<formitem label=\"" + title + "\"><textbox/></formitem>" +
                        "</form>" +
                    "</div>"
                    + "<div class=\"modal-footer\">";
        if (alertFunction)
        {
            alertHTML += "<button class=\"btn\" ng-click=\""+alertFunction+"\">"+buttonText+"</button>";
        }
        else
        {
            alertHTML += "<button class=\"btn\">"+buttonText+"</button>";
        }
        alertHTML += "</div>";
        var popup = popupService.getPopup(true);
        popup.html(alertHTML);
        if (!alertFunction)
        {
            popup.find(".btn").click(function () {
                popupService.close();
            });
        }
        //alert ($compile("<formitem label=\"Title\"></formitem>"));
        popupService.compileAndRunPopup(popup, scope, options);
        scope.$apply();
    }

    // Is it ok to have the html here? should all this go in the directives? Is there another way
    // get the html out of here?
    popupService.confirm = function(title, actionText, actionButtonText, actionFunction, cancelButtonText, cancelFunction, scope, options) {
        actionText = (actionText) ? actionText : "Are you sure?";
        actionButtonText = (actionButtonText) ? actionButtonText : "Ok";
        cancelButtonText = (cancelButtonText) ? cancelButtonText : "Cancel";

        var popup = popupService.getPopup(true);
        var confirmHTML = "";
        if (title)
        {
            confirmHTML += "<div class=\"modal-header\"><h1>"+title+"</h1></div>";
        }
        confirmHTML += "<div class=\"modal-body\">"+actionText+"</div>"
                    +    "<div class=\"modal-footer\">";
        if (actionFunction)
        {
            confirmHTML += "<button class=\"btn btn-primary\" ng-click=\""+actionFunction+"\">"+actionButtonText+"</button>";
        }
        else
        {
            confirmHTML += "<button class=\"btn btn-primary\">"+actionButtonText+"</button>";
        }
        if (cancelFunction)
        {
            confirmHTML += "<button class=\"btn btn-cancel\" ng-click=\""+cancelFunction+"\">"+cancelButtonText+"</button>";
        }
        else
        {
            confirmHTML += "<button class=\"btn btn-cancel\">"+cancelButtonText+"</button>";
        }
        confirmHTML += "</div>";
        popup.html(confirmHTML);
        if (!actionFunction)
        {
            popup.find(".btn-primary").click(function () {
                popupService.close();
            });
        }
        if (!cancelFunction)
        {
            popup.find(".btn-cancel").click(function () {
                popupService.close();
            });
        }
        popupService.compileAndRunPopup(popup, scope, options);

    }

    // Loads the popup
    popupService.load = function(url, scope, options)
    {
        var htmlPage = '<div class="modal-header"><h1>Header</h1></div><div class="modal-body">Body</div><div class="modal-footer"><button class="btn btn-primary" ng-click="doIt()">Do it</button><button class="btn btn-cancel" ng-click="cancel()">Cancel</button></div>';

        $http.get(url).success(function (data) {

            var popup = popupService.getPopup(true);
            // Tried getting this to work with the echo and a post, with no luck, but this gives you the idea
            // popup.html(data);
            popup.html(htmlPage);
            popupService.compileAndRunPopup(popup, scope, options);
        });
    }


    popupService.close = function()
    {
        var popup = popupService.getPopup()
        if (popup)
        {
            popup.modal('hide');
        }
    }

    return popupService;

});




