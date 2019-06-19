//noinspection JSHint
/*******************************************************************************
 Copyright 2018 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/

/* global xe */
/* global angular */
( function() {

    'use strict';

    xe.ng = xe.ng || {};

    xe.ng.applyExtensions = function( restriction ) {
        return ["$timeout", function ($timeout) {
            return {
                restrict: restriction,
                compile: function () {
                    return {
                        post: function (scope, element) {
                            $timeout(function () {
                                if (xe.enableExtensions()) {
                                    xe.extend(element);
                                }
                            });
                        }
                    };
                }
            };
        }];
    };

    angular.module('extensibility', [])
        .directive( 'body', xe.ng.applyExtensions('E') )
        .directive( 'xeDynamic', xe.ng.applyExtensions('A'));
})();

