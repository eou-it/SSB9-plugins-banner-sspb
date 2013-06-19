'use strict';

/* Directives */
var pagebuilderModule = angular.module('pagebuilder.directives', []);

/* a directive to allow display and editing of a JavaScript array that contains map of the same name/value pairs */
pagebuilderModule.directive('pbArrayofmap', function() {
    return {
        restrict:'E',
        transclude: true,
        scope:{label:'@', array:'=', pbParent:'@', pbAttrname:'@', pbChange:'&'},
        templateUrl: templatesLocation + '/pbArrayOfMap.html',
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
                }

                $scope.getKeys();
                //console.log("keys = " + $scope.keys);

                $scope.add = function(value1, value2) {
                    var newObj = {};
                    newObj[$scope.keys[0]] = value1;
                    newObj[$scope.keys[1]] = value2;
                    $scope.array.push(newObj);
                };

                $scope.delete = function(index) {
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
                }

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
        transclude: true,
        scope:{label:'@', map:'=', pbParent:'@', pbAttrname:'@', pbChange:'&'},
        templateUrl: templatesLocation + '/pbMap.html',
        controller: ['$scope', '$element', '$attrs', '$transclude',
            function($scope, $element, $attrs, $transclude) {
                // assign an empty map to the attribute if the map is undefined

                if ($scope.map == undefined) {
                    $scope.map = {};
                    $scope.pbParent[$scope.pbAttrname]=$scope.map;
                }

                $scope.getType = function(obj) {
                    return typeof obj;
                }
                $scope.buildIndex = function() {
                    $scope.index = [];
                    for (var pName in $scope.map)
                        $scope.index.push(pName);

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

                $scope.delete = function(key) {
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
                }

                $scope.mapEditModalOpts = {
                    backdropFade: true,
                    dialogFade:true
                };

            }],
        replace:true
    }

});


