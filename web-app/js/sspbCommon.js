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


// Use function to create a post query function associated with
// a DataSet instance
function CreatePostQuery(instanceIn, userFunction) {
    console.log("Post Query Constructor for DataSet " + instanceIn.modelName);
    this.go = function(it) {
        var instance=instanceIn;
        var uf=userFunction;
        console.log("Executing Post for DataSet="+instance.modelName+" size="+it.length) ;
        instance.currentRecord=instance.data[0];  //set the current record
        instance.setCurrentRecord();
        if (uf) { uf(); }
    };
    return this;
}

// Common function to create a new DataSet
// The DataSet should encapsulate all the model functions query, create, update, delete
// for large datasets, we should have some sort of cursor feature to scroll through the set
//TODO refactor so we don't need to inject this into the controller
function CreateDataSet(params){//nameIn,  resourceURLIn, autoPopulate, paramMap, dataIn ) {
    this.modelName=params.modelName;
    this.data=params.data;
    this.Resource=$resource(params.resourceURL);
    this.queryParams=params.queryParams;
    this.selectValueKey=params.selectValueKey;

    this.currentRecord=null;
    this.selectedRecords=[];
    this.modified = [];
    this.added = [];
    this.deleted = [];

    function init() {
        this.currentRecord=null;
        this.selectedRecords=[];
        this.modified = [];
        this.added = [];
        this.deleted = [];
    }

    var post = new CreatePostQuery(this,params.postQuery) ;  //remember this

    this.load = function() {
        this.init;
        var params =  $scope.$eval(this.queryParams);
        console.log(params) ;
        this.data = this.Resource.query(params, post.go  );
    }

    this.loadAll = function() {
        this.init;
        this.data = this.Resource.query({}, post.go  );
    }

    // some work needed here, if we keep currentRecord inside the
    // dataset, we need to change it if the model changes
    // other issue is with select lists... should assign a value not a record
    this.setCurrentRecord = function ()   {
        var model = $parse(this.modelName);
        //a grid has model.name noop and cannot be assigned a value
        if (model.name != "noop")  {

            console.log("Set current record");
            console.log(this.currentRecord);
            if (this.selectValueKey) {
                model.assign($scope, this.currentRecord[this.selectValueKey]);
            }  else {
                model.assign($scope, this.currentRecord);
            }
        }
    }

    this.setModified = function(item) {
        if (this.modified.indexOf(item) == -1)
            this.modified.push(item);
    }

    this.add = function(item) {
        var newItem = new this.Resource(item);
        //this.added.push(newItem);
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
            item.$save();
        });
        this.modified = [];
        this.deleted.forEach( function(item)  {
            item.$delete({id:item.id});
        });
        this.deleted = [];
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
        return Math.ceil(this.dataSet.data.length/this.pageSize);
    }
}