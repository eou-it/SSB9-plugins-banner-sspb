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


/* App Module */



if (undefined == myCustomServices)
    var myCustomServices = [];


var appModule = angular.module('BannerOnAngular', myCustomServices);


// below filter is used fur pagination
appModule.filter('startFrom', function() {
        return function(input, start) {
            start = +start; //parse to int
            return input.slice(start);
        }
});


appModule.run( function($templateCache )  {
    console.log("App module.run started");
    $templateCache.put('gridFooter.html',
        "<div ng-show=\"showFooter\" class=\"ngFooterPanel\" ng-class=\"{'ui-widget-content': jqueryUITheme, 'ui-corner-bottom': jqueryUITheme}\" ng-style=\"footerStyle()\">" +
        "    <div class=\"paging-container \" ng-show=\"enablePaging\" >" +
        "        <div class=\"paging-control first {{!cantPageBackward() && 'enabled'||''}}\" ng-click=\"pageToFirst()\"></div>"+
        "        <div class=\"paging-control previous {{!cantPageBackward() && 'enabled'||''}}\" ng-click=\"pageBackward()\"></div>"+
        "        <span class=\"paging-text page\"> {{i18n.pageLabel}}</span>"+
        "        <input class=\"page-number\" min=\"1\" max=\"{{maxPages()}}\" type=\"number\" ng-model=\"pagingOptions.currentPage\" style=\"width: 30px; display: inline;\"/>" +
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
        "    <div class=\"ngFooterTotalItems\" ng-class=\"{'ngNoMultiSelect': !multiSelect}\" style=\"float: {{i18n.styleRight}};\ >" +
        "        <span class=\"ngLabel\">{{i18n.ngTotalItemsLabel}} {{maxRows()}}</span>" +
        "        <span ng-show=\"filterText.length > 0\" class=\"ngLabel\">({{i18n.ngShowingItemsLabel}} {{totalFilteredItemsLength()}})</span>" +
        "    </div>" +
        "    <div style=\"position: absolute; bottom:2px; {{i18n.styleRight}}:2px\"> #gridControlPanel# </div>" +
        "</div>");
});


