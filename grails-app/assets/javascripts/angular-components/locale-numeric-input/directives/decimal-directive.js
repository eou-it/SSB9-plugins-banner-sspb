numericApp.directive('decimalInput',['$timeout', '$filter','readonlysvc', '$compile',function($timeout, $filter,readonlysvc,$compile) {
    var withoutDecimal='<input type="number" ng-model="ngModel" class="eds-text-field" ng-show="showNumber" ng-blur="numberBlurred()" ng-change="valNgChange()" only-number/><input value="{{formatted}}" id="{{id}}" ng-focus="textFocused()" class="eds-text-field" ng-click="textFocused()" ng-hide="showNumber" only-number/>';
    var withDecimal='<input type="number" ng-model="ngModel" class="eds-text-field" ng-show="showNumber" ng-blur="numberBlurred()" ng-change="valNgChange()" /><input value="{{formatted}}" id="{{id}}" ng-focus="textFocused()" class="eds-text-field" ng-click="textFocused()" ng-hide="showNumber" />';
    var getTemplate = function(decimalLength){
        var template = '';
        decimalLength = decimalLength||0;
        if(decimalLength==0) {
            template = withoutDecimal;
        } else {
            template = withDecimal;
        }
        return template;
    };
    return {
        restrict: 'E',
        scope:{
            ngModel : "=",
            id : "@",
            integerLength:"=",
            decimalLength:"="
        },
        link: function($scope, $elm, $attrs) {
            $elm.removeAttr('id');
            $elm.html(getTemplate($attrs.decimals));

            $scope.valNgChange = function() {
                $timeout(function() {
                    if ($attrs.ngChange) $scope.$parent.$eval($attrs.ngChange);
                }, 0);
            };

            var liveRegion= $('.number-input-accessible');
            if (liveRegion.length == 0) {
                liveRegion = $("<span>", {
                    role: "status",
                    "aria-live": "assertive",
                    "aria-atomic":"true"
                })
                    .addClass("number-input-accessible screen-reader")
                    .appendTo(document.body);
            }
            $elm.find('input[type=number]').focus(function() {
                var ariaText=$.i18n.prop("numeric.decimal.value");
                var editUnavailableText=$.i18n.prop("numeric.edit.unavailable");
                if ($elm.find('input').attr('readonly')) {
                    ariaText = ariaText+" "+editUnavailableText;
                }
                liveRegion.text(ariaText);
            });
            $compile($elm.contents())($scope);
            $scope.showNumber = false;
            $scope.numberBlurred = function(){
                $scope.showNumber = false;
            };

            $scope.textBlurred = function(){
                $scope.showNumber = true;
            };

            $scope.textFocused = function(){
                $scope.showNumber = true;
                $timeout(function () {
                    $elm.find('input[type=number]').focus().select();
                }, 50)
            };

            $scope.$watch('$scope.showNumber',function(){
                if($scope.showNumber){
                     $timeout(function(){
                        $elm.find('input[type=number]').focus();
                        console.log('focused');
                    }, 50)
                }
            },true);

            $scope.$watch(function() { return $elm.attr('ng-readonly') },function(value){
                if(value !== undefined){
                    readonlysvc.toggle($elm,$elm.attr('ng-readonly'));
                }
            });

            $scope.$watch('ngModel', function(){
                var formatted;
                var dotIndex
                if($scope.ngModel) {
                    dotIndex = $scope.ngModel.toString().indexOf(".")
                    if (dotIndex !== -1) {
                        if ($attrs.integerLength && (dotIndex > parseInt($attrs.integerLength))) {
                            var splittedNumber = $scope.ngModel.toString().split(".");
                            var slicedIntegerPart = splittedNumber[0].toString().slice(0, $attrs.integerLength);
                            $scope.ngModel = parseFloat(slicedIntegerPart.concat("." + splittedNumber[1]));
                        }

                        var numberOfDecimals = $scope.ngModel.toString().length - dotIndex + 1;

                        if ($attrs.decimalLength && (numberOfDecimals > parseInt($attrs.decimalLength))) {
                            var splittedNumber = $scope.ngModel.toString().split(".");
                            var slicedDecimalPart = splittedNumber[1].toString().slice(0, $attrs.decimalLength);
                            $scope.ngModel = parseFloat(splittedNumber[0].toString().concat(".", slicedDecimalPart));
                        }
                    }
                    else {
                        $scope.ngModel = parseFloat($scope.ngModel.toString().slice(0, $attrs.integerLength));
                    }
                }
                formatted = $filter("number")($scope.ngModel, $attrs.decimals);
                $scope.formatted = formatted;

            }, true);
        }
    };
}]);