/******************************************************************************
 *  Copyright 2013-2020 Ellucian Company L.P. and its affiliates.             *
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
    if(noteSpec.type=='error'){
        noteSpec.id = params&&params.id?params.id:'content';
        noteSpec.component = params&&params.component?params.component:'PbComponent';
    }
    var prompts = params&&params.prompts?params.prompts:[{label: $.i18n.prop("sspb.custom.page.default.affirm"), action:function(){}}];

    if (params && params.elementToFocus) {
        noteSpec.elementToFocus = params.elementToFocus;
    }
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

function clickEvent(element){
    var keycode = (event.keyCode ? event.keyCode : event.which);
    if(!element.getAttribute("ng-true-value")) {
        var isChecked = element.getAttribute("aria-checked") == 'true';
        element.setAttribute("aria-checked", !isChecked);
    }
    if(keycode == 32 || keycode==13){
        element.click();
        event.preventDefault();
    }
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
        if(url && url.indexOf('applicationNavigator')!=-1){
            $('#branding').attr('target','_top')
        }
        $('#branding').attr('href', url);

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
        "<div class=\"ngFooterPanel  pagination-container\" ng-class=\"{'ui-widget-content': jqueryUITheme, 'ui-corner-bottom': jqueryUITheme}\" ng-style=\"getFooterStyles()\" style='font-size: 0.65em'>" +
        "    <div id=\"paging-container-#gridName#\" class=\"pagination-controls align-left\" >" +
        "           {{grid.appScope.#gridName#DS.enableDisablePagination()}}"+
        /*"        <div class=\"paging-control first {{!cantPageBackward() && 'enabled'||''}}\" ng-click=\"pageToFirst()\"></div>"+
        "        <div class=\"paging-control previous {{!cantPageBackward() && 'enabled'||''}}\" ng-click=\"pageBackward()\"></div>"+*/
        "        <xe-button xe-type=\"secondary\" xe-btn-class=\"first\" xe-aria-label=\"{{::'pagination.first.label' | xei18n}}\" xe-btn-click=\"grid.appScope.#gridName#DS.pageToFirst()\" xe-disabled=\"grid.appScope.#gridName#DS.firstPrev\" tabindex='0' ng-cloak></xe-button>\n" +
        "        <xe-button xe-type=\"secondary\" xe-btn-class=\"previous\" xe-aria-label=\"{{::'pagination.previous.label' | xei18n}}\" xe-btn-click=\"grid.appScope.#gridName#DS.pageBackward()\" xe-disabled=\"grid.appScope.#gridName#DS.firstPrev\" tabindex='0' ng-cloak></xe-button>\n"+
        "        <span class=\"paging-text page\" id=\"pbid-Grid-Page\"> {{grid.appScope.geti18n('pageLabel')}}</span>"+
        "        <input class=\"page-number\" ng-disabled=\"totalServerItems==0\" min=\"1\" max=\"{{grid.appScope.#gridName#DS.maxPages()}}\" type=\"number\" ng-model=\"grid.appScope.#gridName#DS.pagingOptions.currentPage\" tabindex='0' style=\"width: 50px; display: inline; height: 3.5em;\" aria-labelledby=\"pbid-Grid-Page\"/>" +
        "        <span class=\"paging-text page-of\"> {{grid.appScope.geti18n('maxPageLabel')}} </span> <span class=\"paging-text total-pages\"> {{grid.appScope.#gridName#DS.maxPages()}}  </span>"+
      /*  "        <div class=\"paging-control next {{!cantPageForward() && 'enabled'||''}}\" ng-click=\"pageForward()\"></div>" +
        "        <div class=\"paging-control last {{!cantPageToLast()  && 'enabled'||''}}\" ng-click=\"pageToLast()\" ></div>"+*/
        "        <xe-button xe-type=\"secondary\" xe-btn-class=\"next\" xe-aria-label=\"{{::'pagination.next.label' | xei18n}}\" xe-btn-click=\"grid.appScope.#gridName#DS.pageForward()\"  xe-disabled=\"grid.appScope.#gridName#DS.nextLast\" tabindex='0' ng-cloak></xe-button>\n" +
        "        <xe-button xe-type=\"secondary\" xe-btn-class=\"last\" xe-aria-label=\"{{::'pagination.last.label' | xei18n}}\" xe-btn-click=\"grid.appScope.#gridName#DS.pageToLast()\"  xe-disabled=\"grid.appScope.#gridName#DS.nextLast\" tabindex='0' ng-cloak></xe-button>\n"+
        "        <div class=\"divider dispInline\"></div>" +
        "        <span class=\"paging-text page-per\" id=\"pbid-Grid-RecordsPerPage\"> {{grid.appScope.geti18n('ngPageSizeLabel')}} </span>" +
        "        <div class=\"page-size-select-wrapper dispInline\" >" +
        "            <select page-size-select  class=\"per-page-select\" ng-model=\"grid.appScope.#gridName#DS.pagingOptions.pageSize\" ng-options=\"s as s for s in grid.appScope.#gridName#DS.pagingOptions.pageSizes\" tabindex='0' aria-labelledby=\"pbid-Grid-RecordsPerPage\"> "+
        "             </select>" +
        "        </div>"+
        "       <span class=\"ngLabel\">{{grid.appScope.geti18n('ngTotalItemsLabel')}} {{grid.appScope.#gridName#DS.maxRows()}}</span>" +
        "       <span ng-show=\"filterText.length > 0\" class=\"ngLabel\">({{grid.appScope.geti18n('ngShowingItemsLabel')}} {{grid.appScope.#gridName#DS.totalFilteredItemsLength()}})</span>" +
        "    </div>" +
        "    <div class='customPaginationRightCenter' > #gridControlPanel# </div>" +
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
            if (instance) {
                if(instance.added.length > 0){
                    instance.tempAdded = JSON.parse(JSON.stringify( instance.added ));
                    instance.added.removeAll();
                }
                instance.tempAdded.forEach(function(item) {
                    instance.add(item);
                }, instance);
                instance.tempAdded.removeAll();
            }

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
            if (!iload  && this.dirty()) {
                var currentInstance = this;
                this.confirmPageActionMain(function(){
                    currentInstance.load(p,true);
                    $scope.changed = false;
                    currentInstance.init();
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
        this.gridKeyPress= function(event, item)
        {
            var focus = "moveFocus";
            var rowIndex = item.row.entity.ROW_NUMBER?item.row.entity.ROW_NUMBER-1:0;
            var pageSize = this.pagingOptions.pageSize?this.pagingOptions.pageSize:5;
            rowIndex=rowIndex%pageSize;
            var t = event.target;
            var clickedClass = $(t).attr("class");
            var keyCode = event.keyCode ? event.keyCode : event.which;
            if([37,38,39,40,9,27,13].includes(keyCode)) {
                var prevElement = $(t);
                var gridID = item.grid.element[0].id;
                var attr = $("#"+gridID+" "+ ".moveFocus0").first().attr('firstCell');
                if(!attr) {
                    $("#" + gridID + " " + ".moveFocus0").not(":eq(0)").removeAttr('tabindex');
                    $("#" + gridID + " " + ".moveFocus0").first().attr('firstCell', true);
                }
                if (clickedClass.includes(focus)) {
                    switch (keyCode) {
                        case 39:
                            if (item.colRenderIndex !== undefined && item.colRenderIndex >= 0)
                                focus += item.colRenderIndex + 1;
                            this.setFocus($("#"+gridID+" "+ "."+focus).eq(rowIndex), prevElement);
                            break;
                        case 37:
                            if (item.colRenderIndex !== undefined && item.colRenderIndex >= 0)
                                focus += item.colRenderIndex - 1
                            this.setFocus($("#"+gridID+" "+ "."+focus).eq(rowIndex), prevElement);
                            break;
                        case 38:
                            if (item.colRenderIndex !== undefined && item.colRenderIndex >= 0)
                                focus += item.colRenderIndex
                            rowIndex > 0 ? rowIndex-- : rowIndex;
                            this.setFocus($("#"+gridID+" "+ "."+focus).eq(rowIndex),prevElement);
                            break;
                        case 40:
                            rowIndex++;
                            if (item.colRenderIndex !== undefined && item.colRenderIndex >= 0)
                                focus += item.colRenderIndex
                            this.setFocus($("#"+gridID+" "+ "."+focus).eq(rowIndex), prevElement);
                            break;
                        case 13:
                            this.setFocus($(t).children().first(), prevElement);
                            if(!$(t).children().first().is('select')){
                                $scope.$$postDigest(function () {
                                    $(t).children().first().click();
                                })
                            }
                            break;
                        case 9:
                            $('.ngFooterPanel').focus();
                            break;
                        default:
                            return;
                    }
                } else {
                    switch (keyCode) {
                        case 27:
                            this.setFocus($(t).parent(), prevElement);
                            break;
                        case 13:
                            this.setFocus($(t), prevElement);
                            break;
                        default:
                            return;
                    }
                }
            }
            return;
        }
        this.setFocus=function(ele, prevElement){
            if($(ele).length>0){
                var attr = $(prevElement).attr('firstCell');
                if (!attr) {
                    $(prevElement).attr('tabindex',-1);
                }
                $scope.$$postDigest(function () {
                    $(ele).attr('tabindex', 0);
                    $(ele).focus();
                })
            }
        }
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
            if($scope.gridApi) {
                items = $scope.gridApi.selection.getSelectedRows();
            }
            $scope.changed = true;
            if (this.data.remove(items) ) {
                // we got a single record
                if (this.deleted.indexOf(items) == -1) {
                    this.deleted.push(items);
                }
                if (this.selectedRecords) {
                    this.selectedRecords.remove(items);
                }
                if(this.added){
                    this.added.remove(items);
                }
            } else {
                // we got an array of records to delete
                items.forEach(function(item) {
                    if (this.data.remove(item) )  {
                        if (this.deleted.indexOf(item) == -1) {
                            this.deleted.push(item);
                        }
                        this.selectedRecords.remove(item);
                        this.added.remove(item);
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
                    $scope.changed = false;
                    return;
                }
            }
            $scope.changed=false;
            var addedCount = JSON.parse(JSON.stringify( this.added )).length;
            var currentObject = this;
            this.added.forEach( function(item)  {
                item.$save({},successHandler('C')).then(function (response) {
                    currentObject.added.remove(response);
                    addedCount--;
                    if(addedCount === 0){
                        currentObject.tempAdded = JSON.parse(JSON.stringify( currentObject.added ));
                        currentObject.load();
                        currentObject.added.removeAll();
                    }
                }).catch(function (errorResponse) {
                    addedCount--;
                    post.error(errorResponse);
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
            var deletedCount = JSON.parse(JSON.stringify( this.deleted )).length;
            this.deleted.forEach( function(item)  {
                if(item.id) {
                    item.$delete({id: item.id}, successHandler('D')).then(function (response) {
                        console.log("Item has been deleted successfully "+ response.id)
                        deletedCount--;
                        if(deletedCount === 0 && currentObject.added.length === 0){
                            currentObject.load();
                        }
                    }).catch(function (errorResponse) {
                        deletedCount--;
                        post.error(errorResponse);
                        if(addedCount === 0 && currentObject.added.length === 0){
                            currentObject.load();
                        }
                    });
                }else {
                    deletedCount--;
                    console.error("item cannot delete without id" + item);
                }
            });

            this.deleted = [];
            this.cache.removeAll();
        };

        this.openDropDown = function(item, event) {
            var id = "#"+item;
            var keycode = (event.keyCode ? event.keyCode : event.which);
            if(keycode == '13'){
                $(id).show();
            }
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

        this.maxRows = function () {
            var tot = this.totalCount?this.totalCount:0
            var ret = Math.max(tot, this.data?this.data.length:0);
            return ret;
        };
        this.multiSelect = false;// ($scope.gridApi.config.enableRowSelection && $scope.gridApi.config.multiSelect);
        this.selectedItemCount = $scope.gridApi?$scope.gridApi.selectedItemCount:0;
        this.maxPages = function () {
            return Math.ceil(this.maxRows() / this.pagingOptions.pageSize);
        };

        this.pageForward = function() {
            var page = this.pagingOptions.currentPage;
            if (this.totalServerItems > 0) {
                this.pagingOptions.currentPage = Math.min(page + 1, this.maxPages());
            } else {
                this.pagingOptions.currentPage++;
            }
            /*this.enableDisablePagination();*/
        };

        this.pageBackward = function() {
            var page = this.pagingOptions.currentPage;
            this.pagingOptions.currentPage = Math.max(page - 1, 1);
            /*this.enableDisablePagination();*/
        };

        this.pageToFirst = function() {
            this.pagingOptions.currentPage = 1;
            /*this.enableDisablePagination();*/
        };

        this.pageToLast = function() {
            var maxPages = this.maxPages();
            this.pagingOptions.currentPage = maxPages;
            /*this.enableDisablePagination();*/
        };

        this.cantPageForward = function() {
            var curPage = this.pagingOptions.currentPage;
            var maxPages = this.maxPages();
            if (this.totalCount > 0) {
                this.nextLast = curPage >= maxPages;
            } else {
                this.nextLast = this.data?this.data.length < 1:true;
            }
            return this.nextLast;

        };
        this.cantPageToLast = function() {
            if (this.totalCount > 0) {
                return this.cantPageForward();
            } else {
                this.nextLast=true;
                return true;
            }

        };
        this.cantPageBackward = function() {
            var curPage = this.pagingOptions.currentPage;
            this.firstPrev=(curPage? curPage<= 1:true);
            return this.firstPrev;
        };

        this.enableDisablePagination = function(){
            this.cantPageForward();
            this.cantPageBackward();
            this.cantPageToLast();
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
            '<xe-popup-modal show="modalShown" focusbackelement="'+params.id+'" ' +
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
                if (scope.options.id == 'extendsPage') {
                    scope.options.excludePage = scope.pageName;
                }
                var keycode = (event.keyCode ? event.keyCode : event.which);
                if (event.type == 'click' || event.type == 'enter' || event.type == 'mousedown')
                    scope.loadPopup(scope.options);
                else {
                    if (keycode == '13' && event.type != 'keyup')
                        scope.loadPopup(scope.options);
                    else if (event.type == 'keydown') {
                        document.getElementById(attrs.id).blur();
                    }
                }
                if (keycode != '9') {
                    event.preventDefault();
                    event.stopPropagation();
                }

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
          //  element.on('keyup', scope.onClickData);
            element.on('keydown', scope.onClickData);
            element.on('change', scope.changeData);
            element.on('click', scope.onClickData);
            element.on('mousedown',scope.onClickData);
        }
    };
}]);

$(document).ready(function () {
    $('table').on('keydown',function (e) {
        e = e || window.event;
        var firstCell = $(this).children('tbody').find('tr:first').find('td:first');
        if($(e.target).attr("role")!=="columnheader" && !$(e.target).attr('firstCell')){
            $(e.target).attr('tabindex','-1');
        }
        if(!$(firstCell).attr('firstCell')){
            $(firstCell).attr('firstCell', true);
            $(firstCell).attr('tabindex','0')
        }
        var keyCode = e.keyCode ? e.keyCode : e.which;
        var start = e.target;
        switch(keyCode) {
            case 38:
                var idx = start.cellIndex;
                var nextrow = start.parentElement.previousElementSibling;
                if (nextrow && idx != null) {
                    var sibling = nextrow.cells[idx];
                    $(sibling).attr('tabindex', '0');
                    sibling.focus();
                }
                break;
            case 40:
                var idx = start.cellIndex;
                var nextrow = start.parentElement.nextElementSibling;
                if (nextrow && idx != null) {
                    var sibling = nextrow.cells[idx];
                    $(sibling).attr('tabindex', '0');
                    sibling.focus();
                }
                break;
            case 37:
                var sibling = start.previousElementSibling;
                $(sibling).attr('tabindex', '0');
                sibling ? sibling.focus() : '';
                break;
            case 39:
                var sibling = start.nextElementSibling;
                $(sibling).attr('tabindex', '0');
                sibling ? sibling.focus() : '';
                break;
            case 13:
                var editablElement = $(start).find('input, select, span, a').first();
                editablElement.attr('tabindex','0').click();
                editablElement.focus();
                editablElement.bind("keydown",function (e) {
                    var keyinternalCode =  e.keyCode ? e.keyCode : e.which;
                    if(keyinternalCode==27){
                        $(this).attr('tabindex','-1');
                        $(this).closest('td').focus();
                    }
                })
                break;
            case 27:
                var current = e.target
                $(current).attr('tabindex','-1');
                $(current).closest('td').attr('tabindex',0);
                $(current).closest('td').focus();
                break;
            default:
                return;
        }
    });
})
