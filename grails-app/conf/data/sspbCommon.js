

$scope.setDefault = function(parent,model,def)   {
    var val;
    if (parent) {
       val=parent[model];
        if ( (val === undefined) || (val === null ) ) {
            parent[model]=def;
        }
    } else {
        console.log ("setDefault - unhandled case. parent="+parent +" model="+model);
    }
}


// Use function to create a post query function associated with
// a DataSet instance
function CreatePostQuery(instanceIn, userFunction) {
    console.log("Post Query Constructor for DataSet " + instanceIn.componentId);
    this.go = function(it, response) {
        var instance=instanceIn;
        var uf=userFunction;
        console.log("Executing Post for DataSet="+instance.componentId+" size="+it.length) ;
        instance.currentRecord=instance.data[0];  //set the current record
        instance.setInitialRecord();
        instance.totalCount=parseInt(response("X-hedtech-totalCount")) ;
        if (instance.pagingOptions && instance.pagingOptions.currentPage>instance.numberOfPages() ) {
            //causes requery
            instance.pagingOptions.currentPage=instance.numberOfPages();
        }

        if (uf) { uf(); }
    };
    return this;
}

// Common function to create a new DataSet
// The DataSet should encapsulate all the model functions query, create, update, delete
// for large datasets, we should have some sort of cursor feature to scroll through the set
//TODO refactor so we don't need to inject this into the controller
function CreateDataSet(params){
    this.componentId=params.componentId;
    this.data=params.data;
    if (params.resourceURL) {
        this.cache = $cacheFactory(this.componentId);
    }
    this.Resource=$resource(params.resourceURL+'/:id',
                           {id:'@id'}, //parameters
                           {//custom methods
                            update: {method:'PUT', params: {id:'@id'}},
                            list: {method:'GET',cache: this.cache, isArray:true}
                           }
                         );
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
            currentPage:1
        };
    }

    this.numberOfPages = function () {
        return Math.max(1,Math.ceil(this.totalCount/this.pagingOptions.pageSize));
    }

    this.init = function() {
        this.currentRecord=null;
        this.selectedRecords.removeAll();
        this.modified.removeAll();
        this.added.removeAll();
        this.deleted.removeAll();
        this.totalCount=null;
        if (this.pageSize>0)
            this.pagingOptions.currentPage=1;
    }

    var post = new CreatePostQuery(this,params.postQuery);

    this.get = function() {
        this.init();
        var params;
        eval("params="+this.queryParams+";");
        console.log("Query Parameters:") ;
        console.log( params);
        this.data=[];
        this.data[0] = this.Resource.get(params, post.go);
    }

    this.load = function(p) {
        if (p && p.clearCache)
            this.cache.removeAll();
        if (p && p.paging) {
            this.currentRecord=null;
            this.selectedRecords.removeAll();
        } else {
            this.init();
        }
        var params;
        if (!(p && p.all))
            eval("params="+this.queryParams+";");
        else
            params={};
        if (this.pageSize>0) {
            params.offset=(this.pagingOptions.currentPage-1)*this.pagingOptions.pageSize;
            params.max=this.pagingOptions.pageSize;
        }
        if (this.sortInfo.fields.length>0) {
            params.sortby=[];
            for (var ix = 0;ix< this.sortInfo.fields.length;ix++){
                params.sortby[ix] = this.sortInfo.fields[ix] +' '+ this.sortInfo.directions[ix] ;
            }
        }
        console.log("Query Parameters:") ;
        console.log( params);
        if (this.useGet)  {
            this.data=[];
            this.data[0] = this.Resource.get(params, post.go  );
        }
        else {
            this.data = this.Resource.list(params, post.go  );
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
            if (item )   {
                if (typeof(item) == "string" && this.selectValueKey ) {
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
            console.log(this.currentRecord);
            if (this.selectValueKey) {  //we have a select
                model.assign($scope, this.currentRecord[this.selectValueKey]);
            }  else {
                model.assign($scope, this.currentRecord);
            }
            console.log("Set current record "+ this.currentRecord);
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

    /*
    delete selected record(s)
     */
    this.delete = function(items) {
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
        this.added.forEach( function(item)  {
            item.$save();
        });
        this.added = [];
        this.modified.forEach( function(item)  {
            //item.$save();
            item.$update(); // should we add id param here or not
        });
        this.modified = [];
        this.deleted.forEach( function(item)  {
            item.$delete({id:item.id});
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
}