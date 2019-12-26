/******************************************************************************
 *  Copyright 2013-2019 Ellucian Company L.P. and its affiliates.             *
 ******************************************************************************/

/*
Common Javascript functions used by pagebuilder applications
*/

// notification centre functionality to be invoked by pages created in page builder
alert = function(message, params ){ //message,promptMessage,type,flash,prompts) {
    //give default values
    var noteSpec = {
        message: message,
        type: params&&params.type?params.type:"success",
        promptMessage: params&&params.promptMessage?params.promptMessage:"",
        flash: params&&params.flash?params.flash:false
    };
    var prompts = params&&params.prompts?params.prompts:[{label: $.i18n.prop("sspb.custom.page.default.affirm"), action:function(){}}];

    var note = new Notification(noteSpec);
    if (prompts && !noteSpec.flash) {
        prompts.forEach( function(prompt) {
            note.addPromptAction( prompt.label, function() {
                prompt.action();
                notifications.remove( note );
            });
        })
    }
    notifications.addNotification(note);
};

//Temporary fix for framework issue. ToDo remove when not needed any more.
$(function() {
    _.defer( ContentManager.setContentPosition );
});

//remove by value for arrays. Return if value was remove
Array.prototype.remove=function(value){
    var i =  this.indexOf(value);
    if (i==-1)
        return false;
    else
        this.splice(i,1);
    return true;
};

Array.prototype.removeAll=function(){
    this.splice(0,this.length);
};

// IE 8 missing Array methods
if (!('forEach' in Array.prototype)) {
    Array.prototype.forEach= function(action, that /*opt*/) {
        for (var i= 0, n= this.length; i<n; i++)
            if (i in this)
                action.call(that, this[i], i, this);
    };
}

//Polyfill startsWith
if (!String.prototype.startsWith) {
    String.prototype.startsWith = function(searchString, position){
        position = position || 0;
        return this.substr(position, searchString.length) === searchString;
    };
}

//function to avoid undefined
function nvl(val,def){
    if ( (val == undefined) || (val == null ) ) {
        return def;
    }
    return val;
}

function getControllerScopeById(id) {
    var rootScope = angular.element(document.querySelector('[ng-app]')).injector().get('$rootScope');
    var scope = rootScope.$$childHead;
    //traverse children of root scope to find the id matching _controllerId
    for (; scope && scope._controllerId !== id; scope = scope.$$nextSibling){}
    return scope;
}


/* App Module */

if (undefined == myCustomServices) {
    var myCustomServices = [];
}

myCustomServices.push('modalPopup')
//noinspection JSUnusedAssignment
var appModule = appModule||angular.module('BannerOnAngular', myCustomServices);

/* disable debug: */
appModule.config(['$compileProvider', function ($compileProvider) {
    $compileProvider.debugInfoEnabled(false);
}]);

if (pageControllers) {
    if (window["CustomPageController"]) { // backwards compatibel with alpha release
        pageControllers["CustomPageController"] = window["CustomPageController"];
    }
    for (var pc in pageControllers) {
        if (pageControllers.hasOwnProperty(pc)) {
            appModule.controller(pc, pageControllers[pc]);
        }
    }
}
appModule.controller('homePageUrlCtr', ['$scope', '$window', '$http', function($scope, $window, $http) {
    $window.onload = function() {
        var url = $('#homeURL').val();
        $('#branding').attr('href', url)
    };
}]);
// below filter is used for pagination
appModule.filter('startFrom', function() {
    return function(input, start) {
        start = +start; //parse to int
        return input.slice(start);
    }
});

appModule.filter('to_trusted', ['$sce', function($sce){
    return function(text) {
        return $sce.trustAsHtml(text);
    };
}]);


appModule.run(['$templateCache', function($templateCache )  {
    console.log("App module.run started" );
    $templateCache.put('gridFooter.html',
        "<div ng-show=\"showFooter\" class=\"ngFooterPanel\" ng-class=\"{'ui-widget-content': jqueryUITheme, 'ui-corner-bottom': jqueryUITheme}\" ng-style=\"footerStyle()\">" +
        "    <div id=\"paging-container-#gridName#\" class=\"paging-container \" ng-show=\"enablePaging\" >" +
        "        <div class=\"paging-control first {{!cantPageBackward() && 'enabled'||''}}\" ng-click=\"pageToFirst()\"></div>"+
        "        <div class=\"paging-control previous {{!cantPageBackward() && 'enabled'||''}}\" ng-click=\"pageBackward()\"></div>"+
        "        <span class=\"paging-text page\"> {{i18n.pageLabel}}</span>"+
        "        <input class=\"page-number\" ng-disabled=\"totalServerItems==0\" min=\"1\" max=\"{{maxPages()}}\" type=\"number\" ng-model=\"pagingOptions.currentPage\" style=\"width: 40px; display: inline;\"/>" +
        "        <span class=\"paging-text page-of\"> {{i18n.maxPageLabel}} </span> <span class=\"paging-text total-pages\"> {{maxPages()}}  </span>"+
        "        <div class=\"paging-control next {{!cantPageForward() && 'enabled'||''}}\" ng-click=\"pageForward()\"></div>" +
        "        <div class=\"paging-control last {{!cantPageToLast()  && 'enabled'||''}}\" ng-click=\"pageToLast()\" ></div>"+
        "        <div class=\"divider\"></div>" +
        "        <span class=\"paging-text page-per\"> {{i18n.ngPageSizeLabel}} </span>" +
        "        <div class=\"page-size-select-wrapper\" >" +
        "            <select page-size-select  ng-model=\"pagingOptions.pageSize\" ng-options=\"s as s for s in pagingOptions.pageSizes\" style=\"width: 100%; \"> "+
        "             </select>" +
        "        </div>"+
        "    </div>" +
        "    <div class=\"ngFooterTotalItems\" ng-class=\"{'ngNoMultiSelect': !multiSelect}\" ng-style=\"{float: '{{i18n.styleRight}}'}\" >" +
        "        <span class=\"ngLabel\">{{i18n.ngTotalItemsLabel}} {{maxRows()}}</span>" +
        "        <span ng-show=\"filterText.length > 0\" class=\"ngLabel\">({{i18n.ngShowingItemsLabel}} {{totalFilteredItemsLength()}})</span>" +
        "    </div>" +
        "    <div style=\"position: absolute; bottom:2px;\" ng-style=\"{ {{i18n.styleRight}}:'2px'}\"> #gridControlPanel# </div>" +
        "</div>");

}]);

//Add some functions to the scope
appModule.factory('pbAddCommon', function() {
    function factory(scopeIn) {
        scopeIn.setDefault = function(parent,model,def)   {
            var val;
            if (parent) {
                val=parent[model];
                if ( (val === undefined) || (val === null ) ) {
                    parent[model]=def;
                }
                console.log("***setDefault - parent="+parent +" model="+model);
            } else {
                console.log ("***setDefault - unhandled case. parent="+parent +" model="+model);
            }
        };
        scopeIn.log = function(txt) {
            console.log(txt);
        };
        scopeIn.alert = function(txt) {
            alert(txt);
        };

        //function to avoid undefined
        scopeIn.nvl = function (val,def){
            if ( (val == undefined) || (val == null ) ) {
                return def;
            }
            return val;
        };
    }
    return factory;
});

//Factory for resources
appModule.factory('pbResource', ['$resource', function($resource ) {
    function PBResource(resourceName )  {
        //Expecting a resource name exposed at resourceBase+resourceName
        //For backwards compatibility, replace the location used in the alpha release with resourceBase
        this.resourceURL=resourceName.startsWith("$$contextRoot/")?
            resourceName.replace("$$contextRoot/",rootWebApp):
            resourceName.startsWith("/")?
                resourceName.replace(rootWebApp+'internal/', resourceBase):resourceBase+resourceName;
        this.Resource=null;

        //get a new resource from the factory
        this.getResource = function(cache) {
            return $resource(this.resourceURL+'/:id',
                {id:'@id'}, //parameters
                {//custom methods
                    update: {method:'PUT', params: {id:'@id'}},
                    list: {method:'GET',cache: cache, isArray:true}
                }
            );
        };

        //post (create) a new record immediately
        this.post = function (item, params, success, error) {
            if (this.Resource == null) {
                this.Resource = this.getResource();
            }
            var newItem = new this.Resource(item);
            newItem.$save(params, success, error);
        };

        //put (update) a record immediately
        this.put = function (item, params, success, error) {
            if (this.Resource == null) {
                this.Resource = this.getResource();
            }
            var newItem = new this.Resource(item);
            newItem.$update(params, success, error);
        }
    }

    function PBResourceFactory(resourceURL) {
        return new PBResource(resourceURL);
    }

    return PBResourceFactory;
}]);


//Factory for data sets
appModule.factory('pbDataSet', ['$cacheFactory', '$parse', function( $cacheFactory, $parse ) {
    // Use function to create a post query function associated with
    // a DataSet instance
    console.log("========After Page load =========")
    eval("var $scope"+";");
    function CreatePostEventHandlers(instanceIn, userPostQuery, userOnError) {
        console.log("Post Query Constructor for DataSet " + instanceIn.componentId);
        this.go = function(it, response) {
            var instance=instanceIn;
            var uf=userPostQuery;
            var size = Array.isArray(it)?it.length:1;
            console.log("Executing Post Load for DataSet="+instance.componentId+" size="+size);
            if(instance.added.length > 0){
                instance.tempAdded = JSON.parse(JSON.stringify( instance.added ));
                instance.added.removeAll();
            }
            instance.tempAdded.forEach(function(item) {
                instance.add(item);
            }, instance);
            if(instance.tempAdded.length > 0){
                $scope.changed=false;
            }
            instance.tempAdded.removeAll();
            instance.currentRecord=instance.data[0];  //set the current record
            instance.setInitialRecord();
            instance.totalCount=parseInt(response("X-hedtech-totalCount")) ;
            if (instance.pagingOptions) {
                if (instance.pagingOptions.currentPage>instance.numberOfPages() ) {
                    //causes requery
                    instance.pagingOptions.currentPage=instance.numberOfPages();
                } else if (!instance.pagingOptions.currentPage) {
                    instance.pagingOptions.currentPage=1;
                    console.log('Set currentPage to ',instance.pagingOptions.currentPage);
                }
            }
            if (uf) { uf(it, response); }
        };
        this.error = function (response ) {
            var uf=userOnError;
            if (uf) {uf(response);}
            //alert("Error: "+response.data.errors[0].errorMessage);
        };
        return this;
    }

    // Common function to create a new DataSet
    // The DataSet should encapsulate all the model functions query, create, update, delete
    function PBDataSet(params)  {

        this.setResource = function(resource){
            if (resource) {
                if (!this.cache) {
                    this.cache = $cacheFactory(this.componentId);
                }
                this.Resource = resource.getResource(this.cache);
            }
        };
        this.componentId=params.componentId;
        this.data=params.data;

        this.setResource(params.resource);
        this.queryParams=params.queryParams;
        var onSaveSuccess;
        if(params.onSaveSuccess) {
            onSaveSuccess = params.onSaveSuccess
        }
        var onSave;
        if(params.onSave) {
            onSave = params.onSave
        }
        this.selectValueKey=params.selectValueKey;
        this.selectInitialValue=params.selectInitialValue;
        this.currentRecord=null;
        this.selectedRecords=[];
        this.sortInfo={fields:[], directions:[], columns:[]};
        this.modified = [];
        this.added = [];
        this.deleted = [];
        this.tempAdded = [];
       // this.tempModified = [];
        // this.tempDeleted = [];
        if (this.data === undefined)  {
            this.data = [];
        }
        this.pageSize=params.pageSize;
        if (this.pageSize>0){
            this.pagingOptions = {  pageSizes: [this.pageSize, this.pageSize*2, this.pageSize*4],
                pageSize: this.pageSize, //.toString(), //With number, the select shows empty initially
                currentPage: null
            };
        }
        this.numberOfPages = function () {
            return this.pageSize === 0? 1 : Math.max(1,Math.ceil(this.totalCount/this.pagingOptions.pageSize));
        };

        $scope.iqueryParams =[];



        this.init = function() {
            this.currentRecord=null;
            this.selectedRecords.removeAll();
            this.modified.removeAll();
            this.added.removeAll();
            this.deleted.removeAll();
            this.totalCount=null;
            if (this.pageSize>0) {
                this.pagingOptions.currentPage = 1;
            }
        };

        var post = new CreatePostEventHandlers(this,params.postQuery, params.onError);

        this.get = function() {
            this.init();
            eval("var params="+this.queryParams+";");
            /*fix for minification issue , params will be replaced with variable b after minification*/
            eval("typeof b !=='undefined'") ? eval("b = params"):null;
            console.log("Query Parameters:", params) ;
            this.data=[];
            this.data[0] = this.Resource.get(params, post.go, post.error);
        };

        this.confirmPageActionMain =  function(success,cancelAction) {
            var msg = $.i18n.prop("sspb.page.visualbuilder.loadpage.unsaved.changes.message");
            var note = {type: 'warning', message: msg};
            note.message = note.message.replace(/\n/g, "<br />");
            note.flash = false;
            var n = new Notification( note );

            n.addPromptAction( $.i18n.prop("sspb.page.visualbuilder.page.cancel.message"), function() {
                notifications.remove( n );
                if (cancelAction) {
                    cancelAction();
                }
            });
            $scope.parent = this;
            n.addPromptAction( $.i18n.prop("sspb.page.visualbuilder.page.continue.message"), function() {
                notifications.remove( n );
                success();

            });

            notifications.addNotification( n );
        };

        this.load = function(p,confirmed) {
            var iload = confirmed || !$scope.changed;
            if (!iload) {
                $scope.iqueryParams.push(this);
                this.confirmPageActionMain(function(){
                    $scope.changed = false;
                    $scope.iqueryParams[0].load(p,true);
                    $scope.iqueryParams=[];
                });
            }
            if (iload) {
                if (p && p.clearCache)
                    this.cache.removeAll();
                if (p && p.paging) {
                    if (this.pagingOptions.currentPage && this.pagingOptions.pageSize) {
                        this.currentRecord = null;
                        this.selectedRecords.removeAll();
                    } else {
                        return; //abort load, watch fired for undefined currentPage or pageSize
                    }
                } else {
                    this.init();
                }
                eval("var params;");
                /* Fixing issue for minification , assigning params to variable a*/
                if (!(p && p.all)) {
                    params = eval("params=" + this.queryParams + ";");
                    eval("typeof b !=='undefined'") ? eval("b = params") : null;
                } else {
                    params = {};
                }
                if (this.pageSize > 0) {
                    params.offset = (nvl(this.pagingOptions.currentPage, 1) - 1) * this.pagingOptions.pageSize;
                    params.max = this.pagingOptions.pageSize;
                }
                if (this.sortInfo.fields.length > 0) {
                    params.sortby = [];
                    for (var ix = 0; ix < this.sortInfo.fields.length; ix++) {
                        params.sortby[ix] = this.sortInfo.fields[ix] + ' ' + this.sortInfo.directions[ix];
                    }
                }
                var parameter = {};
                Object.keys(params).forEach(function (key) {
                    var bkey = Base64.encode(getRandomArbitrary(0, 99));
                    var bval = Base64.encode(getRandomArbitrary(0, 99));
                    parameter[bkey + Base64.encode(key)] = (params[key] != null && params[key] != undefined) ? bval + Base64.encode(params[key]) : bval + params[key];
                });
                parameter["encoded"] = true;
                params = parameter;
                console.log("Query Parameters:", params);
                //If an id parameter exists use get
                var res = (params.id === undefined) ? this.Resource.list(params, post.go, post.error) : this.Resource.get(params, post.go, post.error);
                this.data = (Array.isArray(res)) ? res : [res];
            }

        };

        this.loadAll = function() {
            this.load({all:true});
        };

        this.setInitialRecord = function () {
            var model = $parse(this.componentId);
            //a grid has model.name noop and cannot be assigned a value
            if (model.name != "noop")  {
                if (this.selectValueKey) {  //we have a select
                    var iVal=this.selectInitialValue;
                    //it doesn't seem always desired to pick the current record of a select
                    //if (iVal == null || iVal == undefined){
                    //    iVal = this.currentRecord[this.selectValueKey];
                    //}
                    model.assign($scope, iVal);
                    this.setCurrentRecord(iVal); //Adding this experimentally for xe-dropdown
                }  else {
                    model.assign($scope, this.currentRecord);
                }
                console.log("Set initial record ");
            }
        };
        this.setCurrentRecord = function ( item )   {
            var model = $parse(this.componentId);
            //a grid has model.name noop and cannot be assigned a value
            if (model.name != "noop")  {
                if (this.currentRecord === item) {
                    return; // This has already fired
                }
                this.currentRecord = null;
                if (item === undefined )   {
                    //do nothing
                } else {
                    if ( (typeof(item) == "string" || typeof(item) == "number" || item === null) && this.selectValueKey ) {
                        // assume item is a selected string and we are in the DataSet for a select item
                        // Do we have to do a linear search like done below?
                        var len = this.data.length, found=false;
                        for (var i = 0; i < len && !found; i++ ){
                            if (item == this.data[i][this.selectValueKey]) {
                                found=true;
                                this.currentRecord=this.data[i];
                            }
                        }
                    } else {
                        //assume item is of the right type
                        this.currentRecord=item;
                        }
                }
                if (this.selectValueKey) {  //we have a select -- Next assignment may not be needed as item is already the model
                    if (this.currentRecord && this.currentRecord.hasOwnProperty(this.selectValueKey))
                        model.assign($scope, this.currentRecord[this.selectValueKey]);
                }  else {
                    model.assign($scope, this.currentRecord);
                }
                console.log("Set current record:", this.currentRecord);
            }
        };

        this.setModified = function(item) {
            if (this.modified.indexOf(item) == -1 && this.added.indexOf(item) == -1) {
                this.modified.push(item);
                $scope.changed = true;
            }
        };

        this.add = function(item) {
            var newItem = new this.Resource(item);
            this.added.push(newItem);
            // add the new item to the beginning of the array so they show up on the top of the table
            this.data.unshift(newItem);
            $scope.changed = true;
            // TODO - clear the add control content
        };

        //delete selected record(s)
        this.deleteRecords = function(items) {
            $scope.changed = true;
            if (this.data.remove(items) ) {
                // we got a single record
                if (this.deleted.indexOf(items) == -1) {
                    this.deleted.push(items);
                }
                if (this.selectedRecords) {
                    this.selectedRecords.remove(items);
                }
            } else {
                // we got an array of records to delete
                items.forEach(function(item) {
                    if (this.data.remove(item) )  {
                        if (this.deleted.indexOf(item) == -1) {
                            this.deleted.push(item);
                        }
                        this.selectedRecords.remove(item);
                    }
                }, this);
            }
        };

        // Create Base64 Object
        var Base64={_keyStr:"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",
            encode:function(e){var t="";var n,r,i,s,o,u,a;var f=0;e=Base64._utf8_encode(e);while(f<e.length)
            {n=e.charCodeAt(f++);r=e.charCodeAt(f++);i=e.charCodeAt(f++);s=n>>2;o=(n&3)<<4|r>>4;u=(r&15)<<2|i>>6;a=i&63;
                if(isNaN(r)){u=a=64}else if(isNaN(i)){a=64}t=t+this._keyStr.charAt(s)+this._keyStr.charAt(o)
                +this._keyStr.charAt(u)+this._keyStr.charAt(a)}return t},decode:function(e){var t="";var n,r,i;
                var s,o,u,a;var f=0;e=e.toString().replace(/[^A-Za-z0-9+/=]/g,"");while(f<e.length)
                {s=this._keyStr.indexOf(e.charAt(f++));o=this._keyStr.indexOf(e.charAt(f++));
                    u=this._keyStr.indexOf(e.charAt(f++));
                    a=this._keyStr.indexOf(e.charAt(f++));n=s<<2|o>>4;r=(o&15)<<4|u>>2;i=(u&3)<<6|a;t=t+String.fromCharCode(n);
                    if(u!=64){t=t+String.fromCharCode(r)}if(a!=64){t=t+String.fromCharCode(i)}}t=Base64._utf8_decode(t);
                return t},_utf8_encode:function(e){e=e.toString().replace(/\r\n/g,"n");var t="";for(var n=0;n<e.length;n++)
            {var r=e.charCodeAt(n);if(r<128){t+=String.fromCharCode(r)}else if(r>127&&r<2048)
            {t+=String.fromCharCode(r>>6|192);t+=String.fromCharCode(r&63|128)}else
            {t+=String.fromCharCode(r>>12|224);t+=String.fromCharCode(r>>6&63|128);
                t+=String.fromCharCode(r&63|128)}}return t},_utf8_decode:function(e)
            {var t="";var n=0;var r=c1=c2=0;while(n<e.length){r=e.charCodeAt(n);if(r<128){t+=String.fromCharCode(r);n++}
            else if(r>191&&r<224){c2=e.charCodeAt(n+1);t+=String.fromCharCode((r&31)<<6|c2&63);n+=2}else
            {c2=e.charCodeAt(n+1);c3=e.charCodeAt(n+2);t+=String.fromCharCode((r&15)<<12|(c2&63)<<6|c3&63);n+=3}}
                return t}}

        function getRandomArbitrary(min, max) {
            return Math.round(Math.random() * (max - min) + min);
        }

        this.save = function() {

            function successHandler(action) {
                return function (response) {
                    if(!params.onSaveSuccess && onSaveSuccess){params.onSaveSuccess=onSaveSuccess}
                    if (params.onSaveSuccess) {
                        params.onSaveSuccess(response, action);
                    }
                };
            }

            var replaces = false;
            if(!params.onSave && onSave) {params.onSave=onSave}
            if (params.onSave) {
                replaces = params.onSave();
                if (replaces) {
                    return;
                }
            }
            $scope.changed=false;
            var addedCount = JSON.parse(JSON.stringify( this.added )).length;
            var currentObject = this;
            this.added.forEach( function(item)  {
                addedCount--;
                item.$save({},successHandler('C'), post.error).then(function (response) {
                    currentObject.added.remove(response);
                    if(addedCount === 0){
                        currentObject.tempAdded = JSON.parse(JSON.stringify( currentObject.added ));
                        currentObject.load();
                        currentObject.added.removeAll();
                    }
                });
            });
          //  this.added = [];
            this.modified.forEach( function(item)  {
                if(item.id) {
                    item.$update({}, successHandler('U'), post.error);
                }else {
                    //item.$save({},successHandler('C'), post.error);
                    console.error("item cannot update without id" + item);
                }
            });
            this.modified = [];
            var deleteCount = JSON.parse(JSON.stringify( this.deleted )).length;;
            this.deleted.forEach( function(item)  {
                deleteCount--;
                if(item.id) {
                    item.$delete({id: item.id}, successHandler('D'), post.error).then(function (response) {
                        currentObject.deleted.remove(response);
                        if (deleteCount===0) {
                            currentObject.load();
                        }
                    });
                }else {
                    console.error("item cannot delete without id" + item);
                }
            });
            this.deleted = [];
            this.cache.removeAll();
        };

        this.dirty = function() {
            return this.added.length + this.modified.length + this.deleted.length>0;
        };

        this.onUpdate=params.onUpdate;

        if (params.autoPopulate) {
            this.load();
        }

        if (!params.resource && params.data) {
            this.setInitialRecord();
        }

        return this;
    }

    function PBDataSetFactory(scopeIn, params) {
        $scope = scopeIn;
        /* Fixing issue during minification setting the scope value in minfication, c will
        * hold value of scope*/
        eval("typeof c !=='undefined'") ? eval("$scope = c"):null;
        return new PBDataSet(params);
    }

    return PBDataSetFactory;
}]);

function initlizePopUp(params){
    try {
        if (angular.module("modalPopup") && angular.module("xe-ui-components")) {
            var popupContainerDiv = document.getElementById('popupContainerDiv');
            if (null != popupContainerDiv && undefined != popupContainerDiv) {
                dialogPopUp(params);
            }
        }
    } catch (e) {
        console.log(e)
        document.getElementById('popupContainerDiv').style.display = 'none';
    }
};

function dialogPopUp(params) {
    var dataFetch = false;
    var dialogDiv = document.getElementById('popupContainerDiv');
    dialogDiv.setAttribute("ng-app","modalPopup");
    dialogDiv.setAttribute("ng-controller","nameModalPopupCtrl");

    var titleHeader = '';
    var no_result_found = $.i18n.prop("nameDataTable.column.common.nodata");
    var go_button_label = $.i18n.prop("nameDataTable.column.common.button.label");
    var columnRefName = '';
    var nameHeader = '';
    var searchConfig = '';
    if(params.serviceNameType == 'virtualdomains'){
        searchConfig = 'virtualDomainSearchConfig';
        nameHeader = 'serviceName';
        titleHeader = $.i18n.prop("nameDataTable.popup.virtualDomain.pageheader");
        columnRefName = 'virtualDomainColumns';
    }else if(params.serviceNameType == 'pages'){
        searchConfig = 'pageSearchConfig';
        nameHeader = 'constantName';
        titleHeader = $.i18n.prop("nameDataTable.column.page.name.heading");
        columnRefName = 'pageColumns';
    }else if(params.serviceNameType == 'csses'){
        searchConfig = 'cssSearchConfig';
        nameHeader = 'constantName';
        titleHeader = $.i18n.prop("nameDataTable.popup.stylesheet.pageheader");
        columnRefName = 'cssColumns';
    }
    params.excludePage = params.excludePage ? params.excludePage : "";
    dialogDiv.setAttribute("ng-init","nameHeader='"+nameHeader+"';serviceNameType='"+params.serviceNameType+"';excludePage='"+ params.excludePage+"'");

    var scope = angular.element(document.getElementById('popupContainerDiv')).scope();
    if(!scope){
        dialogDiv.innerHTML =
            '<xe-popup-modal show="modalShown" focusbackelement="" ' +
            'pageheader="'+titleHeader+'" class="custom-popup-landpage dataGridModalPopup" > '+
            '<popup-content>' +
            '<div id="namePopupGrid" class="demo-container"> \n' +
            '    <xe-table-grid table-id="nameDataTable" \n'+
            '                   header="'+columnRefName+'"  \n'+
            '                   end-point="urlTest" \n' +
            '                   fetch="getData(query)" on-row-click="onRowClick(data,index)"\n' +
            '                   post-fetch="postFetch(response, oldResult)" \n' +
            '                   content="content"  results-found="resultsFound" toolbar="true"\n' +
            '                   paginate="true" \n' +
            '                   continuous-scrolling="false" \n' +
            '                   on-row-double-click="onDoubleClick(data,index)" \n' +
            '                   no-data-msg="'+no_result_found+'"\n' +
            '                   empty-table-msg="emptyTableMsg" \n' +
            '                   search-config="'+searchConfig+'" \n' +
            '                   pagination-config="paginationConfig"\n' +
            '                   draggable-column-names="draggableColumnNames" \n' +
            '                   mobile-layout="mobileConfig"\n' +
            '                   height="12em" \n' +
            '                   refresh-grid="refreshGrid" >\n' +
            '    </xe-table-grid>\n' +
            '</div> ' +
            '</popup-content>  <popup-buttons>\n' +
            '            <xe-button ng-click="goToPage()" id="goToPageButton" xe-disabled="isResponseEmpty()" xe-type="primary" xe-label="'+go_button_label+'" ></xe-button>\n' +
            '        </popup-buttons>' +
            '</xe-popup-modal>';
        angular.element(document.getElementById('popupContainerDiv')).ready(function() {
            angular.bootstrap(document.getElementById('popupContainerDiv'), ['modalPopup']);
        });
        angular.element(document.getElementsByClassName('column-filter-container ng-scope')).remove();
        scope = angular.element(document.getElementById('popupContainerDiv')).scope();
    }else{
        $("th.constantName").removeClass("focus-ring ascending decending");
        $("th.dateCreated").removeClass("focus-ring ascending decending");
        $("th.lastUpdated").removeClass("focus-ring ascending decending");
        $("th.serviceName").removeClass("focus-ring ascending decending");
        angular.element(document.getElementsByClassName('secondary first')).click();
        var perPageEle = angular.element(document.getElementsByClassName('per-page-select'));
        if($($(perPageEle)[0]).attr("value") != 'number:5'){
            $($(perPageEle)[0]).val("number:5");
            perPageEle.trigger('change');
        }
        dataFetch = true;
    }
    scope.$apply(function(){
        scope.excludePage = params.excludePage;
        scope.inputTypeFieldID = params.id;
        scope.isPbPage = params.isPbPage;
        scope.nameToggleModal(dataFetch);
    });

};

function updateLocalStorage(name,id) {
    window.localStorage['pageName'] = name;
    window.localStorage['pageId'] = id;
}

appModule.directive('pbPopupDataGrid', ['$parse', function($parse)  {
    return {
        require: '?ngModel',
        restrict: 'C',
        scope: false,
        controller: ['$scope', function ($scope) {
            $scope.loadPopup = function (options) {
                initlizePopUp(options);
            };
        }],

        link : function (scope,element,attrs) {
            onLoadEventData();
            scope.onClickData = function (event) {
                //scope.pageName = null;
                scope.options = $parse(attrs.pbPopupDataGrid)() || {};
                if(scope.options.id == 'extendsPage') {
                    scope.options.excludePage = scope.pageName;
                }
                scope.loadPopup(scope.options)
                event.preventDefault();
                event.stopPropagation();
            }

            scope.changeData = function(event){
                if(scope.options.isPbPage== 'true'){
                    var selectedValue = document.getElementById(attrs.id).value;
                    var selectedText = $("#"+attrs.id+" option:selected").text();
                    pbPagesChangeEvent(scope.options.id,selectedText,selectedValue);
                }
                event.preventDefault();
                event.stopPropagation();
            }

            function pbPagesChangeEvent(id,name,value) {
                if(id == 'selectVirtualDomain'){
                    scope.selectVirtualDomainDS = {};
                    scope.selectVirtualDomainDS.data = [{'VID':value,'SERVICE_NAME':name}]
                    scope.selectVirtualDomain = value;
                    scope.CONSTANT_NAME = name;
                    scope.selectVirtualDomain_onUpdate();
                }
                if(id == 'selectPage'){
                    scope.selectPageDS = {};
                    scope.selectPageDS.data = [{'PID':value,'CONSTANT_NAME':name}];
                    scope.selectPage = value;
                    scope.CONSTANT_NAME = name;
                    scope.selectPage_onUpdate();
                }

            }
            function onLoadEventData(){
                var pageName = window.localStorage['pageName'];
                var pageId = window.localStorage['pageId'];
                var pbDataOptions = $parse(attrs.pbPopupDataGrid)() || {};

                if(pageName && pbDataOptions.isPbPage== 'true'){
                    pbPagesChangeEvent(pbDataOptions.id,pageName,pageId)
                }

                if(pageName && pbDataOptions.isPbPage != 'true' && pbDataOptions.id == 'vdServiceName'){
                    $("#"+pbDataOptions.id+" option:selected").remove();
                    $("#"+pbDataOptions.id).append("<option label='"+pageName+"' selected='selected' value="+pageName+">"+pageName+"</option>");
                    $("#LoadVDForm").submit();
                }

                if(pageName && pbDataOptions.isPbPage != 'true' && pbDataOptions.id == 'constantName'){
                    scope.pageName = pageName;
                    scope.getPageSource();
                }

                updateLocalStorage("","");
            }

            element.on('enter',scope.onClickData);
            element.on('keyup', scope.onClickData);
            element.on('keydown', scope.onClickData);
            element.on('change', scope.changeData);
            element.on('click', scope.onClickData);
            element.on('mousedown',scope.onClickData);
        }
    };
}]);

