/**
 * Created with IntelliJ IDEA.
 * User: hvthor
 * Date: 18-4-13
 * Time: 17:51
 * To change this template use File | Settings | File Templates.
 */

//utility functions
//build a sub list from elements of "sourceList" as identified by indices in "indexList"
//Return - the sub list

function buildList(sourceList, indexList) {
    var newList = new Array();
    for (var i = 0; i < indexList.length; i++) {
        newList.push(sourceList[indexList[i]]);
    }
    return newList;
}

/*
 build an expression by replacing tokens on the "statement" with values passed in the "paramList"
 usage - buildExpression("select * from table where col1=? and col2=?", "?", ["'val1'", "'val2'"])
 Return - new statement with tokens replaced. e.g. "select * from table where col1='val1' and col2='val2'"

 TODO - escape ? if it is in the statement e.g.  in the subsitute variables
 */
function buildExpression(statement, token, paramList) {
    for (var i=0; i < paramList.length; i++)
        statement = statement.replace(token, paramList[i]);
    return statement;
}


//function to avoid undefined
function nvl(val,def){
    if ( (val == undefined) || (val == null ) ) {
        return def;
    }
    return val;
}

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
        instance.totalCount=response("X-hedtech-totalCount") ;
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
    this.Resource=$resource(params.resourceURL+'/:id',
                           {id:'@id'}, //parameters
                           {//custom methods
                            update: {method:'PUT', params: {id:'@id'}}
                          }
                         );
    this.queryParams=params.queryParams;
    this.selectValueKey=params.selectValueKey;
    this.selectInitialValue=params.selectInitialValue;
    this.useGet=nvl(params.useGet,false);
    this.currentRecord=null;
    this.selectedRecords=[];
    this.modified = [];
    this.added = [];
    this.deleted = [];
	if (this.data === undefined)  {
		this.data = [];
    }

    function init() {
        this.currentRecord=null;
        this.selectedRecords=[];
        this.modified = [];
        this.added = [];
        this.deleted = [];
        this.totalCount=null;
    }

    var post = new CreatePostQuery(this,params.postQuery) ;  //remember this

    this.get = function() {
        this.init;
        var params;
        eval("params="+this.queryParams+";");
        console.log("Query Parameters:") ;
        console.log( params);
        this.data=[];
        this.data[0] = this.Resource.get(params, post.go  );
    }

    this.load = function() {
        this.init;
        var params;
        eval("params="+this.queryParams+";");
        console.log("Query Parameters:") ;
        console.log( params);
        if (this.useGet)  {
            this.data=[];
            this.data[0] = this.Resource.get(params, post.go  );
        }
        else
            this.data = this.Resource.query(params, post.go  );
    }

    this.loadAll = function() {
        this.init;
        this.data = this.Resource.query({}, post.go  );
    }

    this.setInitialRecord = function () {
        var model = $parse(this.componentId);
        //a grid has model.name noop and cannot be assigned a value
        if (model.name != "noop")  {
            if (this.selectValueKey) {  //we have a select
                var iVal=this.selectInitialValue;
                if (iVal == null || iVal == undefined){
                    iVal = this.currentRecord[this.selectValueKey];
                }
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

    this.delete = function(item) {
        if (this.deleted.indexOf(item) == -1)
            this.deleted.push(item);
        this.data.splice(this.data.indexOf(item),1);
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
            //item.$delete({id:item.id});
            item.$delete({id:item.id});
        });
        this.deleted = [];
    }

    this.dirty = function() {
        return this.added.length + this.modified.length + this.deleted.length>0
    }

    if (params.autoPopulate) {
        this.load();
    }

    return this;
}

function CreateUICtrl(params) {
    this.dataSet=params.dataSet;
    this.onUpdate=params.onUpdate;
    this.pageSize=params.pageSize;
    this.currentPage=0;
    this.numberOfPages = function () {
        //return Math.ceil(this.dataSet.data.length/this.pageSize);
        return Math.ceil(this.dataSet.totalCount/this.pageSize);
    }
}

function CreateDropDownTemplateGrid(params) {
    var temp ="<select ng-class=\"'colt' + $index\" ng-model=\"row.entity[col.field]\" " +
        "ng-options=\"i.STVTERM_CODE for i in termSelectDS.data\"" +
        "placeholder=\"-- Select One --\"></select>";

}