'use strict';

/* Directives */
var pagebuilderModule = angular.module('pagebuilder.directives', []);

pagebuilderModule.directive('pbMap', function() {
    return {
        restrict:'E',
        transclude: true,
        scope:{label:'@', map:'=', pbChange:'&'},
        templateUrl:'/banner-sspb/template/ngMap.html',
        controller: ['$scope', '$element', '$attrs', '$transclude',
            function($scope, $element, $attrs, $transclude) {
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

