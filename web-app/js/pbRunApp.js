/*
Common Javascript functions used by pagebuilder applications
*/

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

//function to avoid undefined
function nvl(val,def){
    if ( (val == undefined) || (val == null ) ) {
        return def;
    }
    return val;
}


/* App Module */

if (undefined == myCustomServices) {
    var myCustomServices = [];
}


var appModule = appModule||angular.module('BannerOnAngular', myCustomServices);

var CustomPageController = CustomPageController||undefined;
if ( CustomPageController) {
    appModule.controller("CustomPageController", CustomPageController);
}

var VisualPageComposerController = VisualPageComposerController||undefined;
if (VisualPageComposerController) {
    appModule.controller("VisualPageComposerController", VisualPageComposerController);
}

var CssManagerController = CssManagerController||undefined;
if (CssManagerController) {
    appModule.controller("CssManagerController", CssManagerController);
}


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


appModule.run( function($templateCache )  {
    console.log("App module.run started" );
    $templateCache.put('gridFooter.html',
        "<div ng-show=\"showFooter\" class=\"ngFooterPanel\" ng-class=\"{'ui-widget-content': jqueryUITheme, 'ui-corner-bottom': jqueryUITheme}\" ng-style=\"footerStyle()\">" +
        "    <div class=\"paging-container \" ng-show=\"enablePaging\" >" +
        "        <div class=\"paging-control first {{!cantPageBackward() && 'enabled'||''}}\" ng-click=\"pageToFirst()\"></div>"+
        "        <div class=\"paging-control previous {{!cantPageBackward() && 'enabled'||''}}\" ng-click=\"pageBackward()\"></div>"+
        "        <span class=\"paging-text page\"> {{i18n.pageLabel}}</span>"+
        "        <input class=\"page-number\" ng-disabled=\"totalServerItems==0\" min=\"1\" max=\"{{maxPages()}}\" type=\"number\" ng-model=\"pagingOptions.currentPage\" style=\"width: 40px; display: inline;\"/>" +
        "        <span class=\"paging-text page-of\"> {{i18n.maxPageLabel}} </span> <span class=\"paginate_total\"> {{maxPages()}}  </span>"+
        "        <div class=\"paging-control next {{!cantPageForward() && 'enabled'||''}}\" ng-click=\"pageForward()\"></div>" +
        "        <div class=\"paging-control last {{!cantPageToLast()  && 'enabled'||''}}\" ng-click=\"pageToLast()\" ></div>"+
        "        <div class=\"divider\"></div>" +
        "        <span class=\"paging-text page-per\"> {{i18n.ngPageSizeLabel}} </span>" +
        "        <div class=\"page-size-select-wrapper\" >" +
        "            <select page-size-select  ng-model=\"pagingOptions.pageSize\"  style=\"width: 100%; \"> "+
        "                <option ng-repeat=\"size in pagingOptions.pageSizes\">{{size}}</option>" +
        "             </select>" +
        "        </div>"+
        "    </div>" +
        "    <div class=\"ngFooterTotalItems\" ng-class=\"{'ngNoMultiSelect': !multiSelect}\" ng-style=\"{float: '{{i18n.styleRight}}'}\" >" +
        "        <span class=\"ngLabel\">{{i18n.ngTotalItemsLabel}} {{maxRows()}}</span>" +
        "        <span ng-show=\"filterText.length > 0\" class=\"ngLabel\">({{i18n.ngShowingItemsLabel}} {{totalFilteredItemsLength()}})</span>" +
        "    </div>" +
        "    <div style=\"position: absolute; bottom:2px;\" ng-style=\"{ {{i18n.styleRight}}:'2px'}\"> #gridControlPanel# </div>" +
        "</div>");
});

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
        }
        scopeIn.log = function(txt) {
            console.log(txt);
        }
        scopeIn.alert = function(txt) {
            alert(txt);
        }

        //function to avoid undefined
        scopeIn.nvl = function (val,def){
            if ( (val == undefined) || (val == null ) ) {
                return def;
            }
            return val;
        }
    };
    return factory;
});

//Factory for resources
appModule.factory('pbResource', function($resource ) {
    function PBResource(resourceURL )  {
        this.resourceURL=resourceURL
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
        }

        //post (create) a new record immediately
        this.post = function (item, params, success, error) {
            if (this.Resource == null) {
                this.Resource = this.getResource();
            }
            var newItem = new this.Resource(item);
            newItem.$save(params, success, error);
        }

        //put (update) a record immediately
        this.put = function (item, params, success, error) {
            if (this.Resource == null) {
                this.Resource = this.getResource();
            }
            var newItem = new this.Resource(item);
            newItem.$update(params, success, error);
        }
    };

    function PBResourceFactory(resourceURL) {
        var result = new PBResource(resourceURL)
        return result;
    };

    return PBResourceFactory;
});


//Factory for data sets
appModule.factory('pbDataSet', function( $cacheFactory, $parse ) {
    // Use function to create a post query function associated with
    // a DataSet instance
    var $scope;
    function CreatePostEventHandlers(instanceIn, userPostQuery, userOnError) {
        console.log("Post Query Constructor for DataSet " + instanceIn.componentId);
        this.go = function(it, response) {
            var instance=instanceIn;
            var uf=userPostQuery;
            console.log("Executing Post Load for DataSet="+instance.componentId+" size="+it.length) ;
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
        this.componentId=params.componentId;
        this.data=params.data;
        if (params.resource) {
            this.cache = $cacheFactory(this.componentId);
            this.Resource=params.resource.getResource(this.cache);
        }

        this.queryParams=params.queryParams;
        this.selectValueKey=params.selectValueKey;
        this.selectInitialValue=params.selectInitialValue;
        this.useGet=nvl(params.useGet,false);
        this.currentRecord=null;
        this.selectedRecords=[];
        this.sortInfo={fields:[], directions:[], columns:[]};
        this.modified = [];
        this.added = [];
        this.deleted = [];
        if (this.data === undefined)  {
            this.data = [];
        }

        this.pageSize=params.pageSize;
        if (this.pageSize>0){
            this.pagingOptions = {  pageSizes: [this.pageSize, this.pageSize*2, this.pageSize*4],
                pageSize: this.pageSize,
                currentPage: null
            };
        }

        this.numberOfPages = function () {
            return this.pageSize === 0? 1 : Math.max(1,Math.ceil(this.totalCount/this.pagingOptions.pageSize));
        }

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
        }

        var post = new CreatePostEventHandlers(this,params.postQuery, params.onError);

        this.get = function() {
            this.init();
            var params;
            eval("params="+this.queryParams+";");
            console.log("Query Parameters:", params) ;
            this.data=[];
            this.data[0] = this.Resource.get(params, post.go, post.error);
        }

        this.load = function(p) {
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
            var params;
            if (!(p && p.all))
                eval("params="+this.queryParams+";");
            else
                params={};
            if (this.pageSize>0) {
                params.offset=(nvl(this.pagingOptions.currentPage,1)-1)*this.pagingOptions.pageSize;
                params.max=this.pagingOptions.pageSize;
            }
            if (this.sortInfo.fields.length>0) {
                params.sortby=[];
                for (var ix = 0;ix< this.sortInfo.fields.length;ix++){
                    params.sortby[ix] = this.sortInfo.fields[ix] +' '+ this.sortInfo.directions[ix] ;
                }
            }
            console.log("Query Parameters:", params) ;
            if (this.useGet)  {
                this.data=[];
                this.data[0] = this.Resource.get(params, post.go, post.error  );
            }
            else {
                this.data = this.Resource.list(params, post.go, post.error  );
            }
        }

        this.loadAll = function() {
            this.load({all:true});
        }

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
                }  else {
                    model.assign($scope, this.currentRecord);
                }
                console.log("Set initial record ");
            }
        }
        this.setCurrentRecord = function ( item )   {
            var model = $parse(this.componentId);
            //a grid has model.name noop and cannot be assigned a value
            if (model.name != "noop")  {
                this.currentRecord = null;
                if (item === undefined )   {
                    ; //do nothing
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
        }

        this.setModified = function(item) {
            if (this.modified.indexOf(item) == -1 && this.added.indexOf(item) == -1)
                this.modified.push(item);
        }

        this.add = function(item) {
            var newItem = new this.Resource(item);
            this.added.push(newItem);
            // add the new item to the beginning of the array so they show up on the top of the table
            this.data.unshift(newItem);
            // TODO - clear the add control content
        }

        //delete selected record(s)
        this.deleteRecords = function(items) {
            if (this.data.remove(items) ) {
                // we got a single record
                if (this.deleted.indexOf(items) == -1)
                    this.deleted.push(items);
                this.selectedRecords.remove(item);
            } else {
                // we got an array of records to delete
                for (ix in items){
                    var item=items[ix];
                    if (this.data.remove(item) )  {
                        if (this.deleted.indexOf(item) == -1)
                            this.deleted.push(item);
                        this.selectedRecords.remove(item);
                    }
                }
            }
        }



        this.save = function() {
            var success = function (response) {
                       if (params.onSaveSuccess) {
                           params.onSaveSuccess(response);
                       }
                   };
            var replaces = false;
            if (params.onSave) {
                replaces = params.onSave();
                if (replaces) {
                    return;
                }
            }

            this.added.forEach( function(item)  {
                item.$save({},success, post.error);
            });
            this.added = [];
            this.modified.forEach( function(item)  {
                item.$update({}, success, post.error);
            });
            this.modified = [];
            this.deleted.forEach( function(item)  {
                item.$delete({id:item.id}, success, post.error);
            });
            this.deleted = [];
            this.cache.removeAll();
        }

        this.dirty = function() {
            return this.added.length + this.modified.length + this.deleted.length>0
        }

        this.onUpdate=params.onUpdate;

        if (params.autoPopulate) {
            this.load();
        }

        return this;
    } ;

    function PBDataSetFactory(scopeIn, params) {
        $scope = scopeIn;
        var result = new PBDataSet(params);
        return result;
    };

    return PBDataSetFactory;
});
