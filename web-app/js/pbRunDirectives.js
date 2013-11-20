'use strict';

/* Directives */
/* added for page builder runtime */
var pbRunModule = angular.module('pbrun.directives', []);


//Original Number directive - TODO: remove this directive once the new directive below proves OK
pbRunModule.directive('pbNumber0', function($filter, $locale) {
    //var FLOAT_REGEXP = /^\-?\d+((\.|\,)\d+)?$/;
    var FLOAT_REGEXP = /^\-?\d+((\.)\d*)?$/;
    //http://stackoverflow.com/questions/15901889/angularjs-formatting-ng-model-before-template-is-rendered-in-custom-directive
    var numberFilter = $filter('number');
    return {
        require: 'ngModel',
        //scope: { ngModel: '=' },
        link: function(scope, elm, attrs, ctrl) {
            //would there ever be more than one formatter? For now make it the first.
            ctrl.$formatters.unshift(function(modelValue) {
                // what you return here will be passed to the text field
                if (modelValue)
                    return numberFilter(modelValue,attrs.fractionDigits);  //Todo: implement mask or number of decimals
                return ''
            });

            //make this the first in the array of parsers
            ctrl.$parsers.unshift(function(viewValue) {

                if (viewValue==="") {
                    ctrl.$setValidity('float', true);
                    return ""
                }
                var GROUP_REGEXP = new RegExp("\\"+$locale.NUMBER_FORMATS.GROUP_SEP,"g");
                var f=viewValue.replace(GROUP_REGEXP,"");
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

        } //link
    };
});

//Better number directive
//With minor modifications from:
//http://www.anicehumble.com/2013/07/seamless-numeric-localization-with-angularjs.html
pbRunModule.directive('pbNumber', function($filter, $locale) {
    return {
        require: 'ngModel',
        //scope: { ngModel: '=' },
        link: function(scope, element, attr, ngModel) {
            var decN = scope.$eval(attr.fractionDigits); // this is the fraction-digits attribute
            // http://stackoverflow.com/questions/10454518/javascript-how-to-retrieve-the-number-of-decimals-of-a-string-number
            function theDecimalPlaces(num) {
                var match = (''+num).match(/(?:\.(\d+))?(?:[eE]([+-]?\d+))?$/);
                if (!match) { return 0; }
                return Math.max(
                    0,
                    // Number of digits right of decimal point.
                    (match[1] ? match[1].length : 0)
                        // Adjust for scientific notation.
                        - (match[2] ? +match[2] : 0));
            }

            function fromUser(text) {
                var GROUP_REGEXP = new RegExp("\\"+$locale.NUMBER_FORMATS.GROUP_SEP,"g");
                var x = text.replace(GROUP_REGEXP, '');
                x = x.replace($locale.NUMBER_FORMATS.DECIMAL_SEP, '.');
                x=Number(x);
                if ( isNaN(x) ) {
                    ngModel.$setValidity('float', false);
                    x=undefined;
                }  else {
                    ngModel.$setValidity('float', true);
                }
                if (x>Number(attr.max) )  {
                    ngModel.$setValidity('max', false);
                    x=undefined
                } else {
                    ngModel.$setValidity('max', true);
                }
                if (x<Number(attr.min) ) {
                    ngModel.$setValidity('min', false);
                    x=undefined
                } else {
                    ngModel.$setValidity('min', true);
                }
                return x;
                // return a model-centric value from user input
            }

            function toUser(n) {
                return $filter('number')(n, decN); // locale-aware formatting
            }

            ngModel.$parsers.unshift(fromUser);
            ngModel.$formatters.unshift(toUser);

            element.bind('blur', function() {
                var v=ngModel.$modelValue;
                if (!isNaN(v)) {
                    element.val(toUser(ngModel.$modelValue));
                }
            });
            /*
            element.bind('focus', function() {
                var n = ngModel.$modelValue;
                var formattedN = $filter('number')(n, theDecimalPlaces(n));
                element.val(formattedN);
            });
            */
        } // link
    }; // return
}); // module