'use strict';

/* Directives */
/* added for page builder runtime */
var pbRunModule = angular.module('pbrun.directives', []);

var FLOAT_REGEXP = /^\-?\d+((\.|\,)\d+)?$/;
pbRunModule.directive('pbNumber', function($filter, $locale) {
    //http://stackoverflow.com/questions/15901889/angularjs-formatting-ng-model-before-template-is-rendered-in-custom-directive
    var numberFilter = $filter('number');
    return {
        require: 'ngModel',
        //scope: { ngModel: '=' },
        link: function(scope, elm, attrs, ctrl) {
            //would there ever be more than one formatter? For now make it the first.
            ctrl.$formatters.unshift(function(modelValue) {
                // what you return here will be passed to the text field
                return numberFilter(modelValue);  //Todo: implement mask or number of decimals
            });
            //make this the first in the array of parsers
            ctrl.$parsers.unshift(function(viewValue) {
                var f=viewValue.replace($locale.NUMBER_FORMATS.GROUP_SEP,"");
                //Todo: use gSize (3 mostly, 2 for several Indian locales and lgSize (3 for locales available) for group sep
                f = f.replace($locale.NUMBER_FORMATS.DECIMAL_SEP,".");
                //Todo: check what to do for negative numbers
                if (FLOAT_REGEXP.test(f)) {
                    ctrl.$setValidity('float', true);
                    return parseFloat(f);
                } else {
                    ctrl.$setValidity('float', false);
                    return undefined;
                }
            });
        }
    };
});