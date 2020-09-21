/*******************************************************************************
 Copyright 2013-2018 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

'use strict';

/* Directives */
var pagebuilderModule = angular.module('pagebuilder.directives', []);

/* a directive to allow display and editing of a JavaScript array that contains map of the same name/value pairs */
pagebuilderModule.directive('pbArrayofmap', function() {
    return {
        restrict:'E',
        //transclude: true,
        scope:{label:'@', array:'=', pbParent:'=', pbAttrname:'=', pbChange:'&'},
        templateUrl: '../assets/angular/pbArrayOfMap.html',
        controller: ['$scope', '$element', '$attrs', '$transclude',
            function($scope, $element, $attrs, $transclude) {
                // assign an empty array to the attribute if the map is undefined
                if ($scope.array == undefined) {
                    $scope.array = [];
                    $scope.pbParent[$scope.pbAttrname]=$scope.array;
                }

                $scope.getKeys = function () {
                    // retrieve the key names from the first array member
                    // if array is empty or the first member has less than 2 attributes set the keys as undefined
                    $scope.hasKeys = false;
                    $scope.keys=[];
                    if ($scope.array != undefined && $scope.array.length > 0) {
                        var i = 0;
                        for (var pName in $scope.array[0]) {
                            $scope.keys[i] = pName;
                            i++;
                        }
                        $scope.hasKeys = true;
                    }
                };

                $scope.getKeys();
                //console.log("keys = " + $scope.keys);

                $scope.add = function(value1, value2) {
                    var newObj = {};
                    newObj[$scope.keys[0]] = value1;
                    newObj[$scope.keys[1]] = value2;
                    $scope.array.push(newObj);
                };
                // IE 8 rename
                $scope.deleteEntry = function(index) {
                    $scope.array.splice(index, 1);

                };
                $scope.insert = function(index) {
                    $scope.array.splice(index, 0, {});
                };

                $scope.newKey=undefined;
                $scope.newValue=undefined;

                // modal dialog functions
                $scope.openArrayOfMapEditModal = function (array) {
                    $scope.arrayOfMapEditShouldBeOpen = true;
                };

                $scope.closeArrayOfMapEditModal = function () {
                    $scope.arrayOfMapEditShouldBeOpen = false;
                    // cause ng-change function passed to the directive to be applied
                    $scope.pbChange();
                    //$scope.handlePageTreeChange();
                };

                $scope.cancelArrayOfMapEditModal = function() {
                    $scope.arrayOfMapEditShouldBeOpen = false;
                };

                $scope.arrayOfMapEditModalOpts = {
                    backdropFade: true,
                    dialogFade:true
                };

            }],
        replace:true
    }
});


/* a directive to allow display and editing of a JavaScript map */
pagebuilderModule.directive('pbMap', function() {
    return {
        restrict:'E',
        //transclude: true,
        scope:{label:'@', map:'=', pbParent:'=', pbAttrname:'=', pbChange:'&'},
        templateUrl: '../assets/angular/pbMap.html',
        controller: ['$scope', '$element', '$attrs', '$transclude',
            function($scope, $element, $attrs, $transclude) {
                // assign an empty map to the attribute if the map is undefined

                if ($scope.map == undefined) {
                    $scope.map = {};
                    $scope.pbParent[$scope.pbAttrname]=$scope.map;
                }

                $scope.getType = function(obj) {
                    return typeof obj;
                };
                $scope.buildIndex = function() {
                    $scope.index = [];
                    for (var pName in $scope.map) {
                        if ($scope.map.hasOwnProperty(pName)) {
                            $scope.index.push(pName);
                        }
                    }
                };

                $scope.add = function(key, value) {
                    if (key == undefined || $scope.map[key] != undefined || value==undefined || value=='')
                        return;
                    $scope.map[key] = value;
                    $scope.buildIndex();
                    $scope.newType = undefined;
                    $scope.newValue = undefined;
                    $scope.newKey = undefined;
                };
                // IE 8 - rename
                $scope.deleteEntry = function(key) {
                    delete $scope.map[key];
                    $scope.buildIndex();
                };

                $scope.buildIndex();
                $scope.newKey=undefined;
                $scope.newValue=undefined;

                // modal dialog functions
                $scope.openMapEditModal = function (map) {
                    $scope.mapEditShouldBeOpen = true;
                };

                $scope.closeMapEditModal = function () {
                    $scope.mapEditShouldBeOpen = false;
                    // cause ng-change function passed to the directive to be applied
                    $scope.pbChange();
                    //$scope.handlePageTreeChange();
                };

                $scope.cancelMapEditModal = function() {
                    $scope.mapEditShouldBeOpen = false;
                };

                $scope.mapEditModalOpts = {
                    backdropFade: true,
                    dialogFade:true
                };

            }],
        replace:true
    }

});

/* a directive to allow display and editing of large amount of text */
pagebuilderModule.directive('pbTextarea', function() {
    return {
        restrict:'E',
        //transclude: true,
        scope:{label:'@', value:'=', pbParent:'=', pbAttrname:'=', pbChange:'&'},
        templateUrl:  '../assets/angular/pbTextarea.html',
        controller: ['$scope', '$element', '$attrs', '$transclude',
            function($scope, $element, $attrs, $transclude) {
                // assign an empty map to the attribute if the map is undefined
               /*
                if ($scope.value == undefined) {
                    $scope.value = '';
                    $scope.pbParent[$scope.pbAttrname]=$scope.value;
                }*/
                //console.log("Init scope, pbParent = " + $scope.pbParent[$scope.pbAttrname]);

                $scope.processInput = function() {
                    console.log("pbAttrname = " + $scope.pbAttrname);
                    console.log("before value = " + $scope.value) ;
                    console.log("before pbParent = " + $scope.pbParent);
                    $scope.pbParent[$scope.pbAttrname]=$scope.value;
                    console.log("after pbParent = " + $scope.pbParent);

                    $scope.pbChange();
                    //console.log("value = " + $scope.value) ;
                };

                // modal dialog functions
                $scope.openTextareaModal = function () {
                    $scope.textareaShouldBeOpen = true;
                };

                $scope.closeTextareaModal = function () {
                    $scope.textareaShouldBeOpen = false;
                    // cause ng-change function passed to the directive to be applied
                    $scope.pbChange();
                    //$scope.handlePageTreeChange();
                };

                $scope.cancelTextareaModal = function() {
                    $scope.textareaShouldBeOpen = false;
                };

                $scope.textareaModalOpts = {
                    backdropFade: true,
                    dialogFade:true
                };

            }],
        replace:true
    }

});

/* a directive to allow editing of text from a list */
pagebuilderModule.directive('pbCombo', function() {
    return {
        restrict:'E',
        //transclude: true,
        scope:{loadSourceLabel:'@', editValueLabel:'@', selectLabel:'@', value:'=', sourceList:"=", pbParent:'=', pbAttrname:'=', pbChange:'&', pbLoadsourcelist:'&' },
        template: "<span>" +
            "<select ng-show='showSelect' ng-model='value'  ng-options='val for val in sourceList' ng-change='processInput()'></select>" +
            "<button ng-show='showSelect' class='btn btn-xs' ng-click='loadSourceList()'>{{loadSourceLabel}}</button>" +
            "<button ng-show='showSelect' class='btn btn-xs' ng-click='showSelect=false'>{{editValueLabel}}</button>" +
            "<input ng-show='!showSelect' type='text' ng-model='value' ng-change='processInput()'/>" +
            "<button ng-show='!showSelect' class='btn btn-xs' ng-click='showSelect=true'>{{selectLabel}}</button>" +
            "</span>",
        controller: ['$scope', '$element', '$attrs', '$transclude',
            function($scope, $element, $attrs, $transclude) {
                $scope.showSelect = false;

                // since value is a string (not an object) we need to copy the value back to the parent scope
                $scope.processInput = function() {
                    console.log("pbAttrname = " + $scope.pbAttrname);
                    console.log("before value = " + $scope.value) ;
                    console.log("before pbParent = " + $scope.pbParent);
                    $scope.pbParent[$scope.pbAttrname]=$scope.value;
                    console.log("after pbParent = " + $scope.pbParent);

                    $scope.pbChange();
                    //console.log("value = " + $scope.value) ;
                };

                $scope.loadSourceList = function() {
                    //$scope.pbLoadsourcelist();
                    // TODO passing loadVdList as a parameter does not work, hardwire the function call
                    $scope.$parent.$parent.loadVdList();
                };
            }],
        replace:true
    }

});


/* a directive to allow uploading of a file to an URL in a modal dialog */
pagebuilderModule.directive('pbUpload', function() {
    return {
        restrict:'E',
        scope:{label:'@', status:'=', pbChange:'&'},
        templateUrl: '../assets/angular/pbUpload.html',
        controller: ['$scope', '$element', '$attrs', '$transclude',
            function($scope, $element, $attrs, $transclude) {
                $scope.complete = function(content, completed) {
                    $scope.uploadResponse = content;

                    // call parent handler
                    if (completed)
                        $scope.pbChange();
                };

                // modal dialog functions
                $scope.openUploadModal = function () {
                    $scope.uploadShouldBeOpen = true;
                    $("#pbid-upload-stylesheet").blur(function () {
                        $("#pbid-cssName-upload").focus();
                    });

                };

                $scope.closeUploadModal = function () {
                    $scope.uploadShouldBeOpen = false;
                    $("#pbid-upload-stylesheet").focus();
                };

                $scope.cancelUploadModal = function() {
                    $scope.uploadShouldBeOpen = false;
                };

                $scope.uploadModalOpts = {
                    backdropFade: true,
                    dialogFade:true
                };

                $scope.i18n = {
                    "sspb.css.cssManager.upload.label"  : $.i18n.prop("sspb.css.cssManager.upload.label"),
                    "sspb.css.cssManager.cssName.label" : $.i18n.prop("sspb.css.cssManager.cssName.label"),
                    "sspb.page.visualbuilder.name.invalid.pattern.message"  : $.i18n.prop("sspb.page.visualbuilder.name.invalid.pattern.message"),
                    "sspb.page.visualbuilder.name.required.message" :   $.i18n.prop("sspb.page.visualbuilder.name.required.message"),
                    "sspb.css.cssManager.description.label" :  $.i18n.prop("sspb.css.cssManager.description.label"),
                    "sspb.css.cssManager.upload.file.label"  : $.i18n.prop("sspb.css.cssManager.upload.file.label"),
                    "sspb.css.cssManager.upload.server.response.label"  : $.i18n.prop("sspb.css.cssManager.upload.server.response.label"),
                    "pb.template.upload.ok.label":   $.i18n.prop("pb.template.upload.ok.label")

                };

            }],
        replace:true
    }

});




