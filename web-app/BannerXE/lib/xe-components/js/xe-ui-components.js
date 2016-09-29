/*
 * component-library
 *

 * Version: 0.0.1 - 2016-08-10
 * License: ISC
 */
angular.module("xe-ui-components", ['badge','button','checkbox','dropdown','label','radiobutton','simpleTextbox','statusLabel','switch','textarea','textbox','external-resouces','utils','columnFilter','pagination','search','dataTableModule','pieChartModule','tabnav','xe-ui-components-tpls']);
angular.module('xe-ui-components-tpls', ['templates/badge.html', 'templates/button.html', 'templates/checkbox.html', 'templates/dropdown.html', 'templates/label.html', 'templates/radio-button.html', 'templates/simple-textbox.html', 'templates/statusLabel.html', 'templates/switch.html', 'templates/text-area.html', 'templates/text-box.html', 'templates/column-filter.html', 'templates/pagination.html', 'templates/search.html', 'templates/dataTable.html', 'templates/tabNav.html', 'templates/tabPanel.html']);

angular.module("templates/badge.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/badge.html",
    "<span tabindex=\"0\" class=\"xe-badge {{::xeType}}-badge\" aria-label=\"{{::xeLabel}}\">{{xeLabel}}</span>");
}]);

angular.module("templates/button.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/button.html",
    "<button class=\"{{xeType +' '+ xeBtnClass}}\" ng-disabled=\"xeDisabled\" ng-click=\"xeBtnClick()\" ng-bind=\"xeLabel\"></button>");
}]);

angular.module("templates/checkbox.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/checkbox.html",
    "<div class=\"xe-checkbox\" ng-class=\"{disabled: xeDisabled, checked: xeModel}\" ng-click=\"cbClicked($event)\" ng-checked=\"xeModel\" role=\"{{ariaRole ? ariaRole : 'checkbox'}}\" aria-checked=\"{{xeModel}}\" aria-disabled=\"{{xeDisabled}}\" aria-labelledby=\"{{::'ckbox-' + xeId}}\" aria-live=\"assertive\" tabindex=\"0\"><span class=\"checkbox\" role=\"presentaion\"></span><xe-label id=\"{{::'ckbox-' + xeId}}\" xe-value=\"{{::xeLabel}}\" xe-hidden=\"{{!xeLabel || xeLabelHidden}}\"></xe-label></div>");
}]);

angular.module("templates/dropdown.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/dropdown.html",
    "<div class=\"btn-group\"><button type=\"button\" ng-disabled=\"{{disabled}}\" ng-class=\"{disabledDD:disabled}\" data-toggle=\"dropdown\" class=\"btn btn-default dropdown dropdown-toggle\" role=\"listbox\" aria-expanded=\"false\" aria-haspopup=\"true\"><span class=\"placeholder\" ng-show=\"!ngModel\">{{::xeLabel}}</span> <span class=\"placeholder\">{{ dropDownLabel }}</span> <span class=\"glyphicon glyphicon-chevron-down\"></span></button><ul class=\"dropdown-menu\" role=\"listbox\" aria-expanded=\"false\" role=\"listbox\"><li ng-hide=\"!ngModel\" ng-click=\"updateModel(xeLabel)\">{{::xeLabel}}</li><li ng-if=\"!isObject\" role=\"option\" ng-repeat=\"option in xeOptions track by $index\" ng-click=\"updateModel(option)\" ng-class=\"{'selected':option===ngModel}\">{{::option}}</li><li ng-if=\"isObject\" ng-repeat=\"option in xeOptions track by $index\" ng-click=\"updateModel(option)\">{{::option.label}}</li></ul></div>");
}]);

angular.module("templates/label.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/label.html",
    "<label class=\"xe-label\" ng-hide=\"{{xeHidden}}\" ng-bind=\"xeValue\"><span class=\"xe-required\" ng-if=\"xeRequired\" ng-bind=\"'*'\"></span></label>");
}]);

angular.module("templates/radio-button.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/radio-button.html",
    "<div class=\"radio-container\" tabindex=\"{{!xeDisabled ? 0 : ''}}\"><input ng-value=\"ngValue\" ng-model=\"ngModel\" ng-disabled=\"xeDisabled\" ng-class=\"{disabledRadio:xeDisabled}\" ng-click=\"xeOnClick\" type=\"radio\" id=\"{{xeId}}\" name=\"{{xeName}}\"><xe-label xe-value=\"{{xeLabel}}\" xe-for=\"{{xeId}}\" aria-checked=\"{{ngModel===ngValue}}\" role=\"radio\"></xe-label></div>");
}]);

angular.module("templates/simple-textbox.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/simple-textbox.html",
    "<input id=\"{{::inputField}}\" xe-field=\"search\" name=\"{{::inputField}}\" placeholder=\"{{placeHolder}}\" class=\"simple-input-field font-semibold {{xeClass}}\" ng-model=\"value\" ng-class=\"{readOnly: inputDisabled}\" ng-disabled=\"{{disabled}}\" ng-keyup=\"onChange({data: value, id: inputField, event: $event})\" ng-keydown=\"onKeydown({data: value, id: inputField, event: $event})\" ng-keypress=\"onKeypress({data: value, id: inputField, event: $event})\" ng-paste=\"onKeypress({data: $event.originalEvent.clipboardData.getData('text/plain'), id: inputField, event: $event})\" ng-focus=\"onFocus({event: $event})\" ng-blur=\"onBlur(({event: $event}))\" autocomplete=\"off\">");
}]);

angular.module("templates/statusLabel.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/statusLabel.html",
    "<span class=\"labels {{xeType}}\" aria-label=\"{{::xeLabel}}\">{{::xeLabel}}</span>");
}]);

angular.module("templates/switch.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/switch.html",
    "<input id=\"{{id}}\" ng-disabled=\"disabled\" ng-class=\"{disabledSwitch:disabled}\" ng-model=\"value\" class=\"cmn-toggle cmn-toggle-round\" type=\"checkbox\"><label for=\"{{id}}\"></label>");
}]);

angular.module("templates/text-area.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/text-area.html",
    "<div class=\"textarea-container\"><xe-label xe-value=\"{{xeLabel}}\" xe-for=\"{{xeId}}\" xe-required=\"{{xeRequired}}\"></xe-label><div class=\"xe-labeltext-margin\"></div><textarea ng-model=\"ngModel\" class=\"comments-field\" id=\"{{xeId}}\" placeholder=\"{{xePlaceholder}}\" ng-required=\"xeRequired\" aria-multiline=\"”true”\" ng-readonly=\"xeReadonly\">	\n" +
    "    </textarea></div>");
}]);

angular.module("templates/text-box.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/text-box.html",
    "<div class=\"textbox-container\" xe-translate><xe-label xe-value=\"{{xeLabel}}\" xe-for=\"{{xeId}}\" xe-required=\"{{xeRequired}}\"></xe-label><div class=\"xe-labeltext-margin\"></div><input ng-class=\"{readonly:xeReadonly}\" ng-model=\"ngModel\" ng-form=\"ngForm\" class=\"{{xeType}}-field\" id=\"{{xeId}}\" name=\"{{xeName}}\" placeholder=\"{{xePlaceholder}}\" ng-pattern=\"xePattern\" ng-required=\"xeRequired\" ng-maxlength=\"xeMaxlength\" ng-minlength=\"xeMinlength\" ng-readonly=\"xeReadonly\"><br><div class=\"error-messages\" ng-messages=\"\"><div ng-message=\"required\">This field is required</div><div ng-message=\"maxlength\">Maximum length is 10 charector</div></div></div>");
}]);

angular.module("templates/column-filter.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/column-filter.html",
    "<span><div class=\"column-filter-container\" ng-if=\"::!nocolumnFilterMenu\"><button type=\"button\" class=\"column-filter-button\" ng-click=\"bindClickEvent($event)\" aria-haspopup=\"true\" aria-labelledby=\"columnFilter\" xe-field=\"columnFilterMenu\"><span id=\"columnFilter\" class=\"placeholder\" ng-bind=\"'dataTable.columnFilter.label' | xei18n\"></span><div class=\"dropdown-icon\">&nbsp;</div></button><ul class=\"column-setting-menu\" ng-hide=\"hideColumnSettingMenu\" role=\"menu\" aria-labelledby=\"columnFilter\"><li role=\"presentation\"><xe-checkbox xe-label=\"{{::'dataTable.columnFilter.selectAll' | xei18n}}\" xe-model=\"selectAll.visible\" xe-on-click=\"onSelectAll(header, event)\" xe-id=\"0\" data-name=\"all\" aria-role=\"menuitemcheckbox\"></xe-checkbox></li><li ng-repeat=\"heading in header\" ng-class=\"{'disabled': heading.options.disable}\" ng-hide=\"heading.options.columnShowHide === false\" data-name=\"{{heading.name}}\" role=\"presentation\"><xe-checkbox xe-id=\"{{heading.name}}\" xe-value=\"{{$index+1}}\" xe-label=\"{{heading.title}}\" xe-model=\"heading.options.visible\" xe-on-click=\"hideUnhideColumn(heading, event)\" xe-disabled=\"heading.options.disable\" aria-role=\"menuitemcheckbox\"></xe-checkbox></li></ul></div></span>");
}]);

angular.module("templates/pagination.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/pagination.html",
    "<div class=\"tfoot pagination-container visible-lg\" role=\"navigation\" ng-cloak><div id=\"resultsFound\" class=\"results-container\" ng-bind=\"('pagination.record.found' | xei18n) + ': ' + resultsFound\"></div><div class=\"pagination-controls\"><xe-button xe-type=\"secondary\" xe-btn-class=\"first\" aria-label=\"{{::'pagination.first.label' | xei18n}}\" xe-btn-click=\"first()\" xe-disabled=\"firstPrev\" ng-cloak></xe-button><xe-button xe-type=\"secondary\" xe-btn-class=\"previous\" aria-label=\"{{::'pagination.previous.label' | xei18n}}\" xe-btn-click=\"prev()\" xe-disabled=\"firstPrev\" ng-cloak></xe-button><xe-label xe-value=\"{{::'pagination.page.label' | xei18n}}\" role=\"presentation\" aria-hidden=\"true\"></xe-label><span title=\"{{::'pagination.page.shortcut.label' | xei18n}}\" role=\"presentation\"><input id=\"pageInput\" type=\"number\" ng-model=\"onPage\" aria-valuenow=\"{{onPage}}\" aria-valuemax=\"{{numberOfPages}}\" aria-valuemin=\"{{!numberOfPages ? 0 : 1}}\" max=\"{{numberOfPages}}\" min=\"{{!numberOfPages ? 0 : 1}}\" ng-model-options=\"{ debounce: {'default': 200, 'blur': 0} }\" ng-change=\"paggeNumberChange()\" ng-blur=\"focusOut($event)\" aria-label=\"{{::'pagination.page.aria.label' | xei18n}}. {{::'pagination.page.label' | xei18n}} {{onPage}} {{::'pagination.page.of.label' | xei18n}} {{numberOfPages}}\" ng-cloak></span><xe-label xe-value=\"{{::'pagination.page.of.label' | xei18n}} {{numberOfPages}}\" role=\"presentation\"></xe-label><xe-button xe-type=\"secondary\" xe-btn-class=\"next\" aria-label=\"{{::'pagination.next.label' | xei18n}}\" xe-btn-click=\"next()\" xe-disabled=\"nextLast\" ng-cloak></xe-button><xe-button xe-type=\"secondary\" xe-btn-class=\"last\" aria-label=\"{{::'pagination.last.label' | xei18n}}\" xe-btn-click=\"last()\" xe-disabled=\"nextLast\" ng-cloak></xe-button><xe-label id=\"perPage\" xe-value=\"{{::'pagination.per.page.label' | xei18n}}\" role=\"presentation\" aria-hidden=\"true\"></xe-label><select class=\"per-page-select\" aria-labelledby=\"perPage\" ng-model=\"offset\" ng-options=\"pageOffset for pageOffset in ::pageOffsets\" ng-change=\"offsetChanged(true)\" ng-disabled=\"resultsFound === 0\"></select></div></div>");
}]);

angular.module("templates/search.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/search.html",
    "<form name=\"form\" class=\"search-container\"><xe-simple-text-box input-field=\"{{searchConfig.id}}\" xe-class=\"search\" value=\"value\" place-holder=\"placeHolder\" disabled on-keydown=\"searchKeydown(data, id, event)\" on-keypress=\"searchKeypress(data, id, event)\" on-focus=\"onFocus(event)\" on-blur=\"onBlur(event)\" aria-label=\"{{::ariaLabel}}\"></xe-simple-text-box></form>");
}]);

angular.module("templates/dataTable.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/dataTable.html",
    "<div id=\"{{tableId}}\" class=\"table-container\" ng-class=\"{'fixed-height': !!height, 'noToolbar': noCaptionAndToolbar, 'no-data': !resultsFound, 'empty': emptyTableMsg}\" browser-detect role=\"grid\" aria-labelledby=\"gridCaption\" ng-cloak><div class=\"caption\" ng-if=\"::!noCaptionBar\" xe-section=\"{{xeSection + 'CaptionBar'}}\"><table class=\"data-table\" role=\"presentaion\"><caption ng-class=\"{'search-opened': hideContainer}\"><span id=\"gridCaption\" class=\"caption-container font-semibold\" ng-if=\"::!nocaption\" ng-bind=\"::caption\" xe-field=\"caption\"></span><div class=\"toolbar\" ng-if=\"toolbar\"><xe-toolbar></xe-toolbar><xe-column-filter></xe-column-filter><span role=\"search\" title=\"{{'search.shortcut.label' | xei18n}}\" ng-if=\"::!nosearch\"><xe-search value=\"searchConfig.searchString\" place-holder=\"{{'search.label' | xei18n}}\" on-change=\"fetchSpecial(query)\" on-focus=\"onSearchFocus({event: event})\" on-blur=\"onSearchBlur({event: event})\" search-config=\"searchConfig\" loading-data=\"loadingData\"></xe-search></span></div></caption></table></div><div class=\"hr-scrollable-content\"><div class=\"thead\"><table class=\"data-table\" ng-style=\"headerPadding\" role=\"presentaion\"><thead role=\"rowgroup\"><tr role=\"row\"><th class=\"font-semibold width-animate {{::heading.name}}\" ng-repeat=\"heading in header\" ng-class=\"{'sortable': heading.options.sortable, 'ascending': sortArray[heading.name].ascending, 'decending': sortArray[heading.name].decending}\" ng-if=\"heading.options.visible === true\" ng-style=\"{'width': heading.dynamicWidth + 'px'}\" data-name=\"{{::heading.name}}\" ng-click=\"onSort({heading: heading}); sortOnHeading(heading, $index);\" role=\"columnheader\" aria-sort=\"{{sortArray[heading.name].ascending ? ('dataTable.sort.ascending.label' | xei18n) : (sortArray[heading.name].decending ? ('dataTable.sort.descending.label' | xei18n) : 'none')}}\" aria-describedby=\"{{'headingAria' + $index}}\" drag-drop=\"handleDrop\" tabindex=\"0\" xe-field=\"{{::heading.name}}\" xe-heading-injector><div class=\"data\" title=\"{{heading.label}}\"><span ng-show=\"::heading.options.titleVisible !== false\" aria-hidden=\"false\" ng-bind=\"::heading.title\"></span><label id=\"${{'headingAria' + $index}}\" class=\"sr-only\" ng-bind=\"heading.ariaLabel + (heading.options.sortable ? ('dataTable.sortable.label' | xei18n) : '')\"></label></div></th></tr></thead></table></div><div class=\"tbody\" ng-style=\"::{'height': height}\" continuous-scroll=\"nextPage()\" scroll-parent=\"{{::continuousScrollParent}}\" aria-labelledby=\"msg\" tabindex=\"{{(!resultsFound || emptyTableMsg) ? 0 : ''}}\" resize><div id=\"msg\" ng-bind=\"emptyTableMsg? emptyTableMsg : ((!resultsFound && !loadingData) ? noDataMsg : '')\"></div><table class=\"data-table\" ng-class=\"::mobileLayout ? 'mobileLayout' : 'noMobileLayout'\" role=\"presentaion\"><thead role=\"presentaion\" aria-hidden=\"true\"><tr><th class=\"font-semibold {{::heading.name}}\" ng-repeat=\"heading in header\" ng-class=\"{'sortable': heading.options.sortable, 'ascending': sortArray[heading.name].ascending, 'decending': sortArray[heading.name].decending}\" ng-if=\"heading.options.visible === true\" ng-style=\"{'width': heading.dynamicWidth + 'px'}\" data-name=\"{{::heading.name}}\" xe-field=\"{{::heading.name}}\" xe-heading-injector tab-index><div class=\"data\"><span ng-show=\"::heading.options.titleVisible !== false\" ng-bind=\"::heading.title\"></span></div></th></tr></thead><tbody role=\"rowgroup\"><tr ng-repeat=\"row in content\" ng-click=\"onRowClick({data:row,index:$index})\" ng-dblclick=\"onRowDoubleClick({data:row,index:$index})\" xe-row-injector tabindex=\"-1\" role=\"row\"><td class=\"width-animate\" ng-repeat=\"heading in header\" ng-class=\"{'align-right': heading.options.actionOrStatus, 'sortable': heading.options.sortable}\" ng-if=\"heading.options.visible === true\" data-name=\"{{::heading.name}}\" data-title=\"{{::(heading.title && heading.options.titleVisible !== false) ? heading.title + ':' : ''}}\" attain-mobile-layout=\"{{mobileLayout[heading.name]}}\" xe-field=\"{{::heading.name}}\" xe-cell-injector tab-index role=\"gridcell\" ng-cloak>{{getObjectValue(row, heading.name)}}</td></tr></tbody></table></div></div><div class=\"tfoot\" ng-transclude></div><xe-pagination model=\"content\" results-found=\"resultsFound\" ng-show=\"showPagination\" search-string=\"searchConfig.searchString\"></xe-pagination><div ng-show=\"loadingData\" class=\"load-indicator\"><div class=\"spinner\"><div class=\"bounce1\"></div><div class=\"bounce2\"></div><div class=\"bounce3\"></div></div></div></div>");
}]);

angular.module("templates/tabNav.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/tabNav.html",
    "<div class=\"xe-tab-container\" role=\"presentation\"><ul class=\"xe-tab-nav\" role=\"tablist\"><li ng-repeat=\"tab in tabnav.tabs\" ng-click=\"tabnav.activate(tab)\" ng-class=\"{active: tab.active}\" ng-repeat-complete role=\"tab\" aria-controls=\"{{'xe-tab-panel'+ ($index+1)}}\" aria-selected=\"{{tab.active}}\"><a ui-sref=\"{{ tab.state && tab.state || '#' }}\" href=\"#\" id=\"{{'xe-tab'+ ($index+1)}}\" title=\"{{tab.heading}}\" ng-if=\"tab.state\">{{tab.heading}} <span></span></a> <a href=\"#\" id=\"{{'xe-tab'+ ($index+1)}}\" title=\"{{tab.heading}}\" ng-if=\"!tab.state\">{{tab.heading}} <span></span></a></li></ul><div class=\"xe-tab-content\" role=\"presentation\"><ng-transclude></ng-transclude></div></div>");
}]);

angular.module("templates/tabPanel.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/tabPanel.html",
    "<div class=\"xe-tab-container\" role=\"presentation\"><div id=\"{{ 'xe-tab-panel'+ tabIndex}}\" class=\"xe-tab-panel\" ng-show=\"active\" ng-class=\"{active: active }\" role=\"tabpanel\" aria-labelledby=\"{{'xe-tab'+ tabIndex}}\" content=\"\" aria-hidden=\"{{ !active }}\"></div></div>");
}]);

(function () {
    'use strict';
    angular.module('badge', []).directive('xeBadge', function () {
        return {
            restrict : 'E',
            scope : {
                xeLabel : '@',
                xeType : '@'
            },
            replace : true,
            templateUrl : 'templates/badge.html'
        };
    });
}());
(function () {
    'use strict';
    angular.module('button', []).directive('xeButton', function () {
        return {
            restrict: 'E',
            replace: true,
            scope: {
                xeType : '@',
                xeDisabled : '=',
                xeLabel : '@',
                xeBtnClick : '&',
                xeBtnClass : "@"
            },
            templateUrl: 'templates/button.html'
        };
    });
}());
(function () {
    'use strict';
    angular.module('checkbox', ['label']).directive('xeCheckbox', ['keyCodes', '$timeout', function (keyCodes, $timeout) {
        return {
            scope : {
                xeId : '@',
                xeValue : '@',
                xeLabel : '@',
                xeLabelHidden : '@',
                xeModel : '=',
                xeOnClick : '&',
                xeDisabled : '=',
                ariaRole : '@'
            },
            restrict : 'E',
            replace : true,
            templateUrl : 'templates/checkbox.html',
            link : function (scope, element, attrs) {
                scope.cbClicked = function (event) {
                    if (scope.xeDisabled) { return; }

                    scope.xeModel = !scope.xeModel;
                    $timeout(function () {
                        scope.xeOnClick({checked: scope.xeModel, event: event});
                    });
                };

                element.on('keydown', function (event) {
                    if (event.keyCode === keyCodes.SPACEBAR || event.keyCode === keyCodes.ENTER) {
                        event.preventDefault();
                        event.stopPropagation();
                        scope.cbClicked(event);
                        scope.$apply();
                    }
                });
            }
        };
    }]);
}());
(function () {
    'use strict';
    angular.module('dropdown', []).directive('xeDropdown', function () {
        return {
            restrict : 'E',
            scope : {
                xeOptions : '=',
                xeLabel : '=',
                disabled : '=',
                ngModel : '='  // Store selected item.
            },
            require : "ngModel",
            controller : ['$scope', function ($scope) {
                $scope.isObject = angular.isObject($scope.xeOptions[0]);
                $scope.dropDownLabel = "";
                $scope.updateModel = function (value) {
                    if ($scope.xeLabel !== value) {
                        if (angular.isObject(value)) {
                            $scope.ngModel = value;
                            $scope.dropDownLabel = value.label;
                        } else {
                            $scope.ngModel = value;
                            $scope.dropDownLabel = value;
                        }
                    } else {
                        $scope.ngModel = null;
                        $scope.dropDownLabel = null;
                    }
                };
            }],
            link: function (scope) {
                if (angular.isDefined(scope.ngModel)) {
                    scope.updateModel(scope.ngModel);
                }
            },
            replace : true,
            templateUrl : 'templates/dropdown.html'
        };
    });
}());
(function () {
    'use strict';
    angular.module('label', []).directive('xeLabel', function () {
        return {
            restrict : 'E',
            scope : {
                xeValue : '@',
                xeHidden : '@',
                xeRequired : '@'
            },
            replace : true,
            templateUrl : 'templates/label.html'
        };
    });
}());
(function () {
    'use strict';
    angular.module('radiobutton', []).directive('xeRadioButton', ['$window', function ($window) {
        return {
            scope : {
                xeLabel : '@',
                ngValue : '=',
                ngModel : '=',
                xeOnClick : '&',
                xeName : '@',
                xeDisabled : '=',
                xeId : '@'
            },
            restrict : 'E',
            replace : true,
            require : 'ngModel',
            templateUrl : 'templates/radio-button.html',
            link : function (scope, element) {
                element.on('keydown', function (event) {
                    if (event.keyCode === 32 || event.keyCode === 13) {
                        event.preventDefault();
                        scope.ngModel = scope.ngValue;
                        scope.$apply();
                    }
                });
            }
        };
    }]);
}());
(function () {
    'use strict';
    angular.module('simpleTextbox', []).directive('xeSimpleTextBox', function () {
        return {
            restrict: 'E',
            scope: {
                inputField: '@',
                xeClass: '@',
                value: '=',
                placeHolder: '=',
                disabled: '=',
                onChange: '&',
                onKeydown: '&',
                onKeypress: '&',
                onFocus : '&',
                onBlur : '&',
                required: '='
            },
            replace: true,
            templateUrl: 'templates/simple-textbox.html'
        };
    });
}());
(function () {
    'use strict';
    angular.module('statusLabel', []).directive('xeStatusLabel', function () {
        return {
            restrict : 'E',
            scope : {
                xeLabel : '@',
                xeType : '@'
            },
            replace : true,
            templateUrl : 'templates/statusLabel.html'
        };
    });
}());
(function () {
    'use strict';
    angular.module('switch', []).directive('xeSwitch', function () {
        return {
            scope : {
                disabled : '=',
                label : '=',
                value : '=',
                id : '='
            },
            templateUrl: 'templates/switch.html'
        };
    });
}());
(function () {
    'use strict';
    angular.module('textarea', []).directive('xeTextArea', function () {
        return {
            restrict: 'E',
            scope : {
                ngModel : '=',
                xeOnChange : '&',
                xePlaceholder : '@',
                xeLabel : '=',
                xeId : '=',
                xeRequired : '=',
                xeReadonly : '='
            },
            replace : true,
            templateUrl : 'templates/text-area.html'
        };
    });
}());
(function () {
    'use strict';
    angular.module('textbox', ['ngMessages']).directive('xeTextBox', function () {
        return {
            restrict : 'E',
            scope : {
                xePlaceholder : '@',
                xeId : '@',
                xeRequired : '=',
                xeType : '@',
                xeReadonly : '=',
                xeValidate : '=',
                xePattern : '@',
                xeErrorMessages : '=',
                xeLabel : '@',
                xeName : '@',
                xeMaxlength : '@',
                xeMinlength : '@',
                xeFormName : '@',
                ngModel : '=',
                ngForm : '='
            },
            replace: true,
            require : ['ngModel', '?ngForm'],
            templateUrl : 'templates/text-box.html',
            compile : function (elem, attrs) {
                var formStr = "ngForm." + attrs.xeName;
                elem.find("div.error-messages").attr("ng-messages", formStr + "." + "$error");
                elem.find("div.error-messages").attr("ng-if", formStr  + "." + attrs.xeName + "." + "$touched");
            }
        };
    });
}());
angular.module("external-resouces", ['pascalprecht.translate', 'ngSanitize']);
(function () {
    'use strict';
    angular.module("xe-ui-components")
        .constant("keyCodes", {
            A: 65,
            B: 66,
            C: 67,
            D: 68,
            E: 69,
            F: 70,
            G: 71,
            H: 72,
            I: 73,
            J: 74,
            K: 75,
            L: 76,
            M: 77,
            N: 78,
            O: 79,
            P: 80,
            Q: 81,
            R: 82,
            S: 83,
            T: 84,
            U: 85,
            V: 86,
            W: 87,
            X: 88,
            Y: 89,
            Z: 90,
            ZERO: 48,
            ONE: 49,
            TWO: 50,
            THREE: 51,
            FOUR: 52,
            FIVE: 53,
            SIX: 54,
            SEVEN: 55,
            EIGHT: 56,
            NINE: 57,
            NUMPAD_0: 96,
            NUMPAD_1: 97,
            NUMPAD_2: 98,
            NUMPAD_3: 99,
            NUMPAD_4: 100,
            NUMPAD_5: 101,
            NUMPAD_6: 102,
            NUMPAD_7: 103,
            NUMPAD_8: 104,
            NUMPAD_9: 105,
            NUMPAD_MULTIPLY: 106,
            NUMPAD_ADD: 107,
            NUMPAD_ENTER: 108,
            NUMPAD_SUBTRACT: 109,
            NUMPAD_DECIMAL: 110,
            NUMPAD_DIVIDE: 111,
            F1: 112,
            F2: 113,
            F3: 114,
            F4: 115,
            F5: 116,
            F6: 117,
            F7: 118,
            F8: 119,
            F9: 120,
            F10: 121,
            F11: 122,
            F12: 123,
            F13: 124,
            F14: 125,
            F15: 126,
            COLON: 186,
            EQUALS: 187,
            UNDERSCORE: 189,
            QUESTION_MARK: 191,
            TILDE: 192,
            OPEN_BRACKET: 219,
            BACKWARD_SLASH: 220,
            CLOSED_BRACKET: 221,
            QUOTES: 222,
            BACKSPACE: 8,
            TAB: 9,
            CLEAR: 12,
            ENTER: 13,
            RETURN: 13,
            SHIFT: 16,
            CONTROL: 17,
            ALT: 18,
            CAPS_LOCK: 20,
            ESC: 27,
            SPACEBAR: 32,
            SPACE: 32,
            PAGE_UP: 33,
            PAGE_DOWN: 34,
            END: 35,
            HOME: 36,
            LEFT: 37,
            UP: 38,
            RIGHT: 39,
            DOWN: 40,
            INSERT: 45,
            DELETE: 46,
            HELP: 47,
            NUM_LOCK: 144
        })
        .run(['$rootScope', '$window', 'getlocale', 'Language', '$translate', function ($rootScope, $window, getlocale, Language, $translate) {
            $rootScope.isMAC = ($window.navigator.userAgent.indexOf("Mac") !== -1);
            $rootScope.isRtl = Language.isRtl();
            $translate.use(getlocale.getUserLocale());
        }]);
}());

(function () {
    'use strict';
    angular.module('utils', ['ngResource'])
        .directive('numbersOnly', function () { // TODO: Move this to common utility file
            return {
                require: 'ngModel',
                link: function (scope, element, attrs, modelCtrl) {
                    modelCtrl.$parsers.push(function (inputValue) {
                        // It is necessary for when using ng-required on your input.
                        // In such cases, when a letter is typed first, this parser will be called
                        // again, and the 2nd time, the value will be undefined
                        if (inputValue === undefined) {
                            return '';
                        }
                        var transformedInput = inputValue.replace(/[^0-9]/g, '');
                        if (transformedInput !== inputValue) {
                            modelCtrl.$setViewValue(transformedInput);
                            modelCtrl.$render();
                        }
                        return transformedInput;
                    });
                }
            };
        })
        .directive("browserDetect", function () { // TODO: Move this to common utility file
            return {
                link: function (scope, element) {
                    var browser = angular || {},
                        ua = navigator.userAgent;
                    browser.ISFF = ua.indexOf('Firefox') !== -1;
                    browser.ISOPERA = ua.indexOf('Opera') !== -1;
                    browser.ISCHROME = ua.indexOf('Chrome') !== -1;
                    browser.ISSAFARI = ua.indexOf('Safari') !== -1 && !browser.ISCHROME;
                    browser.ISWEBKIT = ua.indexOf('WebKit') !== -1;

                    browser.ISIE = ua.indexOf('Trident') > 0 || navigator.userAgent.indexOf('MSIE') > 0;
                    browser.ISIE9 = ua.indexOf('MSIE 9') > 0;
                    browser.ISIE10 = ua.indexOf('MSIE 10') > 0;

                    browser.ISIE11UP = ua.indexOf('MSIE') === -1 && ua.indexOf('Trident') > 0;
                    browser.ISIE10UP = browser.ISIE10 || browser.ISIE11UP;
                    browser.ISIE9UP = browser.ISIE9 || browser.ISIE10UP;

                    if (browser.ISIE9) {
                        element.addClass("ie ie9");
                    } else if (browser.ISCHROME) {
                        element.addClass("modern chrome");
                    } else {
                        element.addClass("modern");
                    }
                }
            };
        })
        .directive('continuousScroll', ['$window', function ($window) {
            return {
                restrict : "A",
                scope : {
                    continuousScroll : "&",
                    scrollParent: '@'
                },
                link : function (scope, element) {
                    var scrollableElement, threshold = 500;

                    if (scope.scrollParent === 'body') {
                        scrollableElement = angular.element($window);
                    } else if (scope.scrollParent !== '') {
                        scrollableElement = angular.element('#' + scope.scrollParent);
                    } else {
                        scrollableElement = element;
                    }

                    /**
                     * The function is called when scrollbar position is close enough to the bottom
                     */
                    var onBottomTooClose = function() {
                        scope.continuousScroll();
                    };

                    var checkIfBottomTooClose = _.throttle(function(elm) {
                        var scrollHeight = (scope.scrollParent === 'body') ? angular.element(document).height() : elm[0].scrollHeight;

                        // Check if we're too close to the bottom
                        if((scrollHeight - elm.scrollTop() - elm.height()) <= threshold) {
                            onBottomTooClose();
                        }
                    }, 500);

                    /**
                     * maintain scroll event
                     */
                    scrollableElement.bind("scroll", function () {
                        checkIfBottomTooClose(scrollableElement);
                    });

                    /**
                    * Destructor
                    */
                    scope.$on("$destroy", function() {
                        // Remove jquery events manually because angular doesn't know about them
                        scrollableElement.off('scroll');
                    });
                }
            };
        }])
        .directive("xeKeypress", ['keyCodes', function (keyCodes) {
            var keyCodeMatch = function (keyPress, codes) {
                var keys = codes.split(","),
                    index;

                for (index = 0; index < keys.length; index = index + 1) {
                    if (keyPress === keyCodes[keys[index]]) {
                        return true;
                    }
                }
                return false;
            };

            return {
                restrict : 'A',
                link : function (scope, element, attrs) {
                    element.bind("keypress", function (event) {
                        var keyCode = event.which || event.keyCode;

                        if (keyCodeMatch(keyCode, attrs.codes)) {
                            scope.$apply(function () {
                                scope.$eval(attrs.xeKeypress, {$event: event});
                            });
                        }
                    });
                }
            };
        }])

        /* Factory Methods */
        .factory("accessibility", ['$rootScope', 'keyCodes', '$document', function ($rootScope, keyCodes, $document) {
            var accessibility = {
                provideAccessibilityForTable: provideAccessibilityForTable
            }, globalKeydownHandler = function (event) {
                var targetToClick = checkKeyTarget(event, event.data.table.find('[global-key][shortcut-key]'));

                if (targetToClick && targetToClick.length) {
                    scrollToTarget(targetToClick, event.data.parent);
                    targetToClick.select().focus().click();
                    event.preventDefault();
                }
            }, scrollToTarget = function (element, scrollableParent) {
                if (!scrollableParent.prop('nodeName')) {
                    scrollableParent = angular.element('#content');
                }

                if (scrollableParent.length && element) {
                    scrollableParent.animate({
                        scrollTop: element.offset().top - scrollableParent.offset().top + scrollableParent.scrollTop()
                    });
                }
            };

            function provideAccessibilityForTable(component, scrollableParent) {
                applyAccessibilityForTable(component);
                applyKeyboardNavForTable(component, scrollableParent);
            }

            function applyAccessibilityForTable(table) {
                // Setting tabindex of "Show/Hide Column" dropdown items
                table.find('.column-setting-menu').find('.xe-checkbox')
                        .each(function(index, element) {
                            setTabindex(element, -1);
                        });

                // Setting tabindex of all actionable elements inside table header and footer
                // except pageInput of pagination controls, so that it can be traversed with normal tab order
                table.find('thead, .tfoot').find('a, :input').not('#pageInput')
                        .each(function(index, element) {
                            setTabindex(element, -1);
                        });

                /* Tabindex of table rows and all actionable elements present inside table body are set through
                    tab-index directive in dataTable.html as they can be generated dynamically. */

                // Shortcut key bindings
                var searchShortcutKey;
                if ($rootScope.isMAC) {
                    searchShortcutKey = 'CTRL+META+Y';
                } else {
                    searchShortcutKey = 'ALT+Y';
                }

                table.find('input.search:first').attr('shortcut-key', searchShortcutKey).attr('global-key', true);
                table.find('.thead th:not(":hidden"):first').attr('shortcut-key', 'HOME');
                table.find('.tfoot input#pageInput').attr('shortcut-key', 'END');
            }

            function applyKeyboardNavForTable(table, scrollableParent) {
                var tempTargetIndex; // Variable to remember previous column position,
                                     // used while traversing across columns inside rows

                $document.off('keydown', globalKeydownHandler).on('keydown', {"table": table, "parent": scrollableParent}, globalKeydownHandler);

                table.on('keydown', function (event) {
                    var targetToFocus,        // Target to focus itself
                        targetToFocusChildren,// Target to focus itself/children based on the presence of actionable children
                        targetToClick,        // Target to be clicked
                        element = angular.element(event.target),
                        elementIndex,
                        isFromColumnFilter = !!element.closest('.column-filter-container').length,
                        isFromSearch = element.is('input.search'),
                        isFromHeader = !!element.closest('.thead').length,
                        isFromBody = !!element.closest('.tbody').length,
                        isEmptyBody = table.find('.tbody tbody').is(':empty'),
                        isFromFooter = !!element.closest('.tfoot').length;

                    if (element.is("[xe-keypress]")) {
                        return;
                    }

                    switch (event.which) {
                    case keyCodes.ENTER:
                        if (isFromSearch) {
                            targetToFocusChildren = table.find('.tbody tbody tr:first-child');
                            unfocusTableBody(table);
                        } else if (isFromBody || isFromHeader) {
                            targetToClick = element;
                        }
                        break;
                    case keyCodes.SPACEBAR:
                        if (isFromHeader) {
                            targetToClick = element;
                        }
                        break;
                    case keyCodes.TAB:
                        if (isFromBody) {
                            unfocusTableBody(table);
                        }

                        if (isFromColumnFilter && table.find('.column-setting-menu').is(':visible')) {
                            element.closest('.column-filter-container').trigger('close');
                        } else if (event.shiftKey) {
                            if (isFromHeader) {
                                targetToFocus = table.find('input.search:first');
                            } else if (isFromBody && !element.closest('.slick-dots').length) {
                                targetToFocusChildren = table.find('.thead th:not(":hidden"):first');
                            } else if (isFromFooter && !isEmptyBody) {
                                targetToFocusChildren = table.find('.thead th:not(":hidden"):first');
                            }
                        } else if (isFromSearch) {
                            targetToFocusChildren = table.find('.thead th:not(":hidden"):first');
                        } else if (isFromHeader) {
                            if (isEmptyBody) {
                                targetToFocus = table.find('.tbody[tabindex=0]');
                            } else {
                                targetToFocus = table.find('.tfoot input#pageInput');
                                scrollToTarget(angular.element('#pageInput'), scrollableParent);
                            }
                        } else if (isFromBody) {
                            scrollToTarget(angular.element('#pageInput'), scrollableParent);
                        }
                        break;
                    case keyCodes.ESC:
                        if (isFromColumnFilter && table.find('.column-setting-menu').is(':visible')) {
                            element.closest('.column-filter-container').trigger('close');
                        } else if (isFromBody) {
                            tempTargetIndex = undefined;
                            targetToFocus = element.closest('tr');
                        }
                        break;
                    case keyCodes.PAGE_UP:
                        if (isFromBody) {
                            targetToFocus = table.find('.tbody tbody tr:first');
                        }
                        break;
                    case keyCodes.PAGE_DOWN:
                        if (isFromBody) {
                            targetToFocus = table.find('.tbody tbody tr:last');
                        }
                        break;
                    case keyCodes.UP:
                        if (isFromColumnFilter) {
                            targetToFocus = prevActionable(element, 'li').find('.xe-checkbox');
                            event.preventDefault();
                        } else if (isFromHeader) {
                            event.preventDefault();
                        } else if (isFromBody) {
                            if (element.is(':not(tr)')) {
                                elementIndex = element.closest('td').index();
                                tempTargetIndex = elementIndex;
                            } else {
                                elementIndex = tempTargetIndex;
                            }

                            targetToFocusChildren = prev(element.closest('tr'));
                            unfocusTableBody(table);

                            if (targetToFocusChildren && !targetToFocusChildren.length) {
                                elementIndex = undefined;
                                tempTargetIndex = undefined;
                                targetToFocusChildren = table.find('.thead th:not(":hidden"):first');
                            }
                        }
                        break;
                    case keyCodes.DOWN:
                        if (isFromColumnFilter) {
                            targetToFocus = nextActionable(element, 'li').find('.xe-checkbox');
                            event.preventDefault();
                        } else if (isFromHeader) {
                            targetToFocusChildren = table.find('.tbody tbody tr:first');
                        } else if (isFromBody) {
                            if (element.is(':not(tr)')) {
                                elementIndex = element.closest('td').index();
                                tempTargetIndex = elementIndex;
                            } else {
                                elementIndex = tempTargetIndex;
                            }

                            targetToFocusChildren = next(element.closest('tr'));
                            event.preventDefault();
                        }

                        if (targetToFocusChildren && targetToFocusChildren.length) {
                            unfocusTableBody(table);
                        }
                        break;
                    case keyCodes.LEFT:
                        if (isFromHeader) {
                            targetToFocusChildren = prevActionable(element, 'th');
                        } else if (isFromBody) {
                            if (element.is('tr')) {
                                targetToFocusChildren = getActionableSiblings(element.find('td').not(':hidden').first(), 'td').first();
                            } else if (element) {
                                targetToFocusChildren = prevActionable(element, 'td');
                            }
                        } else if (isFromFooter) {
                            targetToFocus = prevActionable(element, 'span', '.pagination-controls');
                            // Disabling dropdown value change with left arrow key
                            event.preventDefault();
                        }
                        break;
                    case keyCodes.RIGHT:
                        if (isFromHeader) {
                            targetToFocusChildren = nextActionable(element, 'th');
                        } else if (isFromBody) {
                            if (element.is('tr')) {
                                targetToFocusChildren = getActionableSiblings(element.find('td').not(':hidden').first(), 'td').last();
                            } else if (element) {
                                targetToFocusChildren = nextActionable(element, 'td');
                            }
                        } else if (isFromFooter) {
                            targetToFocus = nextActionable(element, 'span', '.pagination-controls');
                            // Disabling dropdown value change with right arrow key
                            event.preventDefault();
                        }
                        break;
                    default:
                        targetToFocusChildren = checkKeyTarget(event, table.find(':not([global-key])[shortcut-key]'));
                        scrollToTarget(targetToFocusChildren, scrollableParent);
                    }

                    if (targetToFocusChildren && targetToFocusChildren.length) {
                        focusElement(targetToFocusChildren, elementIndex);
                        event.preventDefault();
                    } else if (targetToFocus && targetToFocus.length) {
                        targetToFocus.select().focus();
                        event.preventDefault();
                    } else if (targetToClick && targetToClick.length) {
                        if (targetToClick.is('a')) {
                            targetToClick = targetToClick.get(0);
                            targetToClick.click();
                        } else {
                            targetToClick.select().focus().click();
                        }
                        event.preventDefault();
                    }
                });
            }

            function getFirstActionableItem (element) {
                return element.find("a, :input, [tabindex=0], .xe-checkbox").first().not(':hidden, .disabled, :disabled, [readonly]');
            }

            function focusElement (element, targetIndex) {
                var actionableElement;

                element.select().focus();

                if (element.is('tr')) {
                    element.addClass('active-row');
                }

                if (targetIndex) {
                    actionableElement = getFirstActionableItem(element.children().eq(targetIndex));
                } else {
                    actionableElement = getFirstActionableItem(element);
                }

                if(actionableElement.length) {
                    focusActionableElement(actionableElement);
                }
            }

            function focusActionableElement (element) {
                element.select().focus()
                    .on('focusout', function(event) {
                        var element = angular.element(event.target);

                        element.closest('th').removeClass('focus-ring');
                        element.closest('td').removeClass('active focus-ring');
                        element.off('focusout');
                    });
                element.closest('tr th').addClass('focus-ring');
                element.closest('tr td').addClass('active focus-ring');
            }

            function prev (element, repeat) {
                var prevElement = element.prev();
                if (!prevElement.get(0) && repeat) {
                    prevElement = element.siblings(':last');
                }

                return prevElement;
            }

            function next (element, repeat) {
                var nextElement = element.next();
                if (!nextElement.get(0) &&  repeat) {
                    nextElement = element.siblings(':first');
                }

                return nextElement;
            }

            function prevActionable (element, parentTag, rootTag, repeat) {
                if (parentTag && !rootTag) {
                    element = element.closest(parentTag);
                }

                var siblings = getActionableSiblings(element, parentTag, rootTag),
                    prevActionableElement = angular.element(siblings[siblings.index(element) - 1]);

                if (!prevActionableElement.length && repeat) {
                    prevActionableElement = siblings.eq(-1);
                }

                return prevActionableElement;
            }

            function nextActionable (element, parentTag, rootTag, repeat) {
                if (parentTag && !rootTag) {
                    element = element.closest(parentTag);
                }

                var siblings = getActionableSiblings(element, parentTag, rootTag),
                    nextActionableElement = angular.element(siblings[siblings.index(element) + 1]);

                if (!nextActionableElement.length && repeat) {
                    nextActionableElement = siblings.eq(0);
                }

                return nextActionableElement;
            }

            function getActionableSiblings (element, parentTag, rootTag) {
                var siblings;

                if (parentTag && element.is('th')) {
                    siblings = element.siblings('.sortable').add(element);
                    siblings = siblings.add(element.siblings(':not(.sortable)').has("a:not(:hidden), :input:enabled:not([readonly]), [tabindex=0]").not(':hidden, .disabled'));
                } else if (parentTag && rootTag) {
                    siblings = element.closest(rootTag).find("a, :input:enabled:not([readonly]), [tabindex=0]").not(':hidden, .disabled');
                } else if (parentTag) {
                    siblings = element.siblings().add(element).has("a:not(:hidden), :input:enabled:not([readonly]), [tabindex=0], .xe-checkbox:not(.disabled)").not(':hidden, .disabled');
                } else {
                    siblings = element.siblings("a, :input:enabled:not([readonly]), [tabindex=0]").not(':hidden, .disabled').add(element);
                }

                return siblings;
            }

            function unfocusTableBody (table) {
                table.find('.active-row').removeClass('active-row');
                table.find('.active').removeClass('active');
            }

            function checkKeyTarget (event, targetElements) {
                if (angular.isUndefined(targetElements) || !targetElements.length) { return; }

                var target;

                for (var i = 0; i < targetElements.length; i++) {
                    var comboKeys = ['ctrl', 'alt', 'shift', 'meta'],
                        keyStrokes = targetElements.eq(i).attr('shortcut-key').split('+');

                    if (keyStrokes.length === 1) {
                        if (keyCodes[keyStrokes[0]] === event.which) {
                            target = targetElements.eq(i);
                            break;
                        }
                    } else if (keyStrokes.length === 2 && comboKeys.indexOf(keyStrokes[0].toLowerCase()) !== -1) {
                        if (event[keyStrokes[0].toLowerCase() + 'Key'] && keyCodes[keyStrokes[1]] === event.which) {
                            target = targetElements.eq(i);
                            break;
                        }
                    } else if (keyStrokes.length === 3 && comboKeys.indexOf(keyStrokes[0].toLowerCase()) !== -1 && comboKeys.indexOf(keyStrokes[1].toLowerCase()) !== -1) {
                        if (event[keyStrokes[0].toLowerCase() + 'Key'] && event[keyStrokes[1].toLowerCase() + 'Key'] && keyCodes[keyStrokes[2]] === event.which) {
                            target = targetElements.eq(i);
                            break;
                        }
                    }
                }

                return target;
            }

            function setTabindex (element, tabindex, replace) {
                element = angular.element(element);

                if (!element.attr('tabindex') || replace) {
                    element.attr('tabindex', tabindex);
                }
            }

            return accessibility;
        }])
        .factory("getlocale", [ '$translate', function ($translate) {
            return {
                getUserLocale : function () {
                    var locale = jQuery('meta[name=userLocale]').attr("content");
                    if (!locale) {
                        locale = $translate.use();
                    }
                    return locale;
                }
            };
        }])
        .factory("Language", function($translate) {
            var isRtl = function() {
                var dir = jQuery('meta[name=dir]').attr("content");

                if (!dir) {
                    //add the languages you support here. ar stands for arabic
                    var rtlLanguages = ['ar'];

                    var languageKey = $translate.proposedLanguage() || $translate.use();
                    for (var i = 0; i < rtlLanguages.length; i += 1) {
                        // You may need to change this logic depending on your supported languages (possible languageKey values)
                        // This code will match both "ar", "ar-XXX" locales. It won't match any other languages as we only support en, es, ar.
                        if (languageKey.indexOf(rtlLanguages[i]) > -1)
                            return true;
                    }
                    return false;
                } else if (dir === 'rtl') {
                    return true;
                }
                return false;
            };

            return {
                isRtl: isRtl
            };
        })
        .filter('xei18n', ['$filter', function ($filter) {
            return function (key) {
                var value = "";
                if (angular.isDefined(key) && angular.isDefined(jQuery.i18n)) {
                    value = jQuery.i18n.prop(key);
                }

                if (!value || value.indexOf(key) >= 0) {
                    value = $filter('translate')(key);
                }
                return value;
            };
        }]);
}());
//TODO : Presently it works for table grid componets. Need to work on this component to make more generic.
(function () {
    'use strict';
    angular.module('columnFilter', [])
        .directive('xeColumnFilter', ['$document', '$timeout', function ($document, $timeout) {
            return {
                restrict : 'E',
                scope : true,
                replace : true,
                templateUrl : 'templates/column-filter.html',
                controller : ['$scope', function ($scope) {
                    $scope.selectAll = {visible: true};
                    $scope.hideColumnSettingMenu = true;

                    $scope.hideUnhideColumn = function (heading, e) {
                        if (!heading.options.visible) {
                            $scope.selectAll.visible = false;
                        } else {
                            var invisibleColumnCount = $scope.header.filter(function (heading) {
                                    return (heading.options.visible === false && heading.options.columnShowHide !== false);
                                }).length;

                            if (!invisibleColumnCount) {
                                $scope.selectAll.visible = true;
                            }
                        }
                    };

                    $scope.onSelectAll = function (header, e) {
                        $scope.selectAll.visible = true;

                        angular.forEach(header, function (heading) {
                            if (heading.options.visible === false && heading.options.columnShowHide !== false) {
                                heading.options.visible = true;
                            }
                        });
                    };
                }],
                link: function (scope, element, attrs) {
                    var postMenuClose = function () {
                        $document.off('click');

                        $timeout(function () {
                            element.find('button').attr('tabindex', 0);
                        }, 10);
                    };

                    element.on('close', function (event, actualTarget) {
                        var isClickedFromPopup = element.find(actualTarget).length > 0;
                        if (isClickedFromPopup) {
                            return;
                        }

                        scope.hideColumnSettingMenu = true;
                        postMenuClose();

                        if (angular.isUndefined(actualTarget) || angular.element(actualTarget).is(':not(:focus)')) {
                            element.find('.column-filter-button').focus();
                        }

                        scope.$apply();
                    });

                    scope.bindClickEvent  = function (event) {
                        scope.hideColumnSettingMenu = !scope.hideColumnSettingMenu;

                        // On open dropdown menu
                        if (!scope.hideColumnSettingMenu) {
                            element.find('button').attr('tabindex', -1);

                            $document.on('click', function (event) {
                                element.trigger('close', event.target);
                            });

                            // Focusing 1st item on open of dropdown menu
                            $timeout(function () {
                                element.find('.xe-checkbox:first').focus();
                            }, 10);
                        } else { // On close dropdown menu
                            postMenuClose();
                        }
                    };
                }
            };
        }]);
}());
angular.module('pagination', [])
.directive('xePagination', ["$http", "$q", function($http, $q) {
    var fetch = function(query) {
        var deferred = $q.defer();

        url = query.endPoint + "?"
            + "searchString=" + (query.searchString ? query.searchString : "")
            + "&sortColumnName=" + (query.sortColumnName ? query.sortColumnName : "")
            + "&ascending=" + query.ascending
            + "&offset=" + (query.offset ? query.offset : "")
            + "&max=" + (query.max ? query.max : "");

        $http.get(url)
            .success(function(data) {
                deferred.resolve(data);
            })
            .error(function(data) {
                deferred.reject(data);
            });

        return deferred.promise;
    };
    var reassignRange = function(pageNumber, offset) {
        var pageEnd = offset * pageNumber;
        return {
            max: pageEnd,
            offset: pageEnd === 0 ? 0 :(pageEnd - offset)
        };
    };

    return {
        restrict: 'EA',
        replace: true,
        require: "?^xeTableGrid",
        scope: {
            model: "=",
            endPoint: "=?",
            paginationConfig: "=?",
            resultsFound: "=",
            searchString: "=",
            fetch: "&?",
            postFetch: "&"
        },
        templateUrl: "templates/pagination.html",
        controller: ['$scope', '$attrs', "$timeout", function($scope, $attrs, $timeout) {
            var oldPageValue = 1;

            $scope.firstPrev = false;
            $scope.nextLast = false;
            $scope.onPage = 1;

            if (angular.isObject($scope.paginationConfig) && !$scope.paginationConfig.pageLengths) {
                $scope.pageOffsets = [10, 20, 50, 100];
            } else {
                $scope.pageOffsets = $scope.paginationConfig.pageLengths;
            }

            if (angular.isObject($scope.paginationConfig) && $scope.paginationConfig.offset) {
                $scope.offset = $scope.paginationConfig.offset;
            } else {
                $scope.offset = $scope.pageOffsets[0];
            }

            if ($scope.pageOffsets.indexOf($scope.offset) < 0) {
                $scope.pageOffsets.push($scope.offset);
                $scope.pageOffsets.sort(function(a, b){ return a-b; });
            }

            $scope.offsetChanged = function(doFetch) {
                calculateNumberOfPages();
                disableButtons($scope.onPage, $scope.numberOfPages);
                if (doFetch) {
                    $scope.fetchData($scope.onPage, $scope.offset);
                }
            };

            $scope.first = function() {
                if ($scope.firstPrev) {
                    return;
                }

                setPageValue(1);

                $scope.fetchData($scope.onPage, $scope.offset);
                disableButtons($scope.onPage, $scope.numberOfPages);
                focusPageInput();
            };

            $scope.prev = function(append) {
                if ($scope.firstPrev) {
                    return;
                }

                var onPage = parseInt($scope.onPage);
                onPage--;
                setPageValue(onPage);

                $scope.fetchData($scope.onPage, $scope.offset, append);
                disableButtons($scope.onPage, $scope.numberOfPages);
                if ($scope.firstPrev) {
                    focusPageInput();
                }
            };

            $scope.next = function(append) {
                if ($scope.nextLast) {
                    return;
                }

                var onPage = parseInt($scope.onPage);
                onPage++;
                setPageValue(onPage);

                $scope.fetchData($scope.onPage, $scope.offset, append);
                disableButtons($scope.onPage, $scope.numberOfPages);
                if ($scope.nextLast) {
                    focusPageInput();
                }
            };

            $scope.last = function() {
                if ($scope.nextLast) {
                    return;
                }

                setPageValue($scope.numberOfPages);

                $scope.fetchData($scope.onPage, $scope.offset);
                disableButtons($scope.onPage, $scope.numberOfPages);
                focusPageInput();
            };

            $scope.paggeNumberChange = function() {
                if ($scope.onPage) {
                    focusPageInput();
                }

                if (($scope.onPage !== null) && (oldPageValue != $scope.onPage)) {
                    if (angular.isUndefined($scope.onPage) || ($scope.onPage <= 0) || ($scope.onPage > $scope.numberOfPages)){
                        $scope.onPage = oldPageValue;
                    } else {
                        setPageValue($scope.onPage);
                        $scope.fetchData($scope.onPage, $scope.offset);
                        disableButtons($scope.onPage, $scope.numberOfPages);
                    }
                }
            };

            $scope.focusOut = function(event) {
                angular.element(event.target).val(oldPageValue);
            };

            $scope.$watch("resultsFound", function(newValue, oldValue) {
                $timeout(function() {
                    if (newValue === 0) {
                        setPageValue(0);
                    } else if ($scope.onPage === 0) {
                        setPageValue(1);
                    }

                    calculateNumberOfPages();
                    disableButtons($scope.onPage, $scope.numberOfPages);
                });
            });

            // Private functions
            var setPageValue = function(onPage) {
                $scope.onPage = onPage;
                oldPageValue = onPage;
            };

            // Private functions
            var focusPageInput = function() {
                $timeout(function() {
                    angular.element('#pageInput').select().focus();
                }, 50);
            };

            var calculateNumberOfPages = function() {
                $scope.numberOfPages = Math.ceil($scope.resultsFound / $scope.offset);
                $scope.numberOfPages = $scope.numberOfPages < 1 ? 0 : $scope.numberOfPages;

                if ($scope.onPage > $scope.numberOfPages) {
                    setPageValue($scope.numberOfPages);
                }
            };

            var disableButtons = function(pageNumber, numberOfPages) {
                pageNumber = parseInt(pageNumber);
                numberOfPages = parseInt(numberOfPages);
                var reminder = numberOfPages / pageNumber;

                if (numberOfPages === 1) { // Only one page
                    $scope.firstPrev = true;
                    $scope.nextLast = true;
                } else if(reminder === 1) { // On last page
                    $scope.nextLast = true;
                    $scope.firstPrev = false;
                } else if(reminder === numberOfPages) { // On first page
                    $scope.firstPrev = true;
                    $scope.nextLast = false;
                } else if(pageNumber <= 0 || (pageNumber > numberOfPages)) { // Out of range
                    $scope.firstPrev = true;
                    $scope.nextLast = true;
                } else { // Between first and last page
                    $scope.nextLast = false;
                    $scope.firstPrev = false;
                }
            };

            /*
                boolean append variable is used to check if we need append to the result set or not.
                This is because on tablet we will not show the pagination but it components can still use pagination
                code to make the continuous scroll happen.
            */
            $scope.fetchData = function(onPage, offset, append) {
                if (!angular.isNumber(onPage)) {
                    onPage = parseInt(onPage);
                }

                setPageValue(onPage);

                var range = reassignRange(onPage, offset),
                    query = {
                        searchString: $scope.searchString,
                        sortColumnName: $scope.sortColumnName,
                        ascending: $scope.ascending,
                        offset: range.offset,
                        max: range.max,
                        endPoint: $scope.endPoint,
                        onPage: onPage,
                        pageSize:offset
                    };

                // Show Load indicator
                $scope.loading(true);

                if (angular.isDefined($attrs.fetch)) {
                    // Call clients fetch method
                    $scope.fetch({query: query}).then(
                        /* Success */
                        function(data) {
                            $scope.postFetch({response: data, oldResult: $scope.model});
                            $scope.model = append ? $scope.model.concat(data.result) : data.result;
                            $scope.resultsFound = data.length;

                            $scope.loading(false);
                        },
                        /* Error */
                        function(data) {
                            if (data) console.error(data);
                            $scope.postFetch({response: data, oldResult: $scope.model});
                            $scope.loading(false);
                        }
                    );
                } else {
                    fetch(query).then(
                        /* Success */
                        function(data) {
                            $scope.postFetch({response: data, oldResult: $scope.model});
                            $scope.model = append ? $scope.model.concat(data.result) : data.result;
                            $scope.resultsFound = data.length;

                            $scope.loading(false);
                        },
                        /* Error */
                        function(data) {
                            if (data) console.error(data);
                            $scope.postFetch({response: data, oldResult: $scope.model});
                            $scope.loading(false);
                        }
                    );
                }
            };

            $scope.offsetChanged(false);
        }],
        link: function(scope, elem, attributes, parentController) {
            // Assigning values from parentCOntroller to be used later in paginations controller.
            scope.loading = parentController.loadingDataIndicator;
            scope.emptyTableMsg = parentController.emptyTableMsg;
            scope.sortColumnName = parentController.sortColumnName;
            scope.ascending = parentController.ascending;

            if(!scope.emptyTableMsg) {
                scope.fetchData(1, scope.offset);
            }

            // If continuous scrolling is true then we can to hide paginations across devices and desktop.
            if (parentController.hidePaginationIfContinuousScroll) {
                parentController.hidePaginationIfContinuousScroll();
            }

            // Injecting next(), previous() and sort() function to parent controller so that it can invoke them later as per the need.
            // For example for continuous scrolling.
            parentController.next = function(append) {
                scope.next(append);
            };

            parentController.previous = function(append) {
                scope.prev(append);
            };

            parentController.fetchData = function(onPage, offset) {
                onPage = angular.isDefined(onPage) ? onPage : scope.onPage;
                offset = angular.isDefined(offset) ? offset : scope.offset;

                scope.fetchData(onPage, offset);
            };

            parentController.sort = function(sortColumnName, order) {
                scope.sortColumnName = sortColumnName;
                scope.ascending = order;
                scope.fetchData(scope.onPage, scope.offset);
            };
        }
    };
}]);
(function () {
    'use strict';
    angular.module('search', []).directive('xeSearch', ['keyCodes', '$filter', function (keyCodes, $filter) {
        return {
            restrict: 'E',
            scope: {
                value: '=',
                placeHolder: '@',
                onChange: '&',
                onFocus : '&',
                onBlur : '&',
                onKeydown : '&',
                searchConfig: '=',
                loadingData: '='
            },
            replace: true,
            templateUrl: 'templates/search.html',
            link: function (scope, element, attrs) {
                var minCharactersToStartSearch = 0,
                    maxlength = Infinity,
                    onDataChange,
                    transformedInput;

                if (angular.isObject(scope.searchConfig)) {
                    if (angular.isDefined(scope.searchConfig.minimumCharacters)) {
                        minCharactersToStartSearch = scope.searchConfig.minimumCharacters;
                    }
                    if (angular.isDefined(scope.searchConfig.maxlength)) {
                        maxlength = scope.searchConfig.maxlength;
                    }
                    // Blank search string if not specified initially
                    if (angular.isUndefined(scope.searchConfig.searchString)) {
                        scope.searchConfig.searchString = '';
                    }
                    // Default element id if not specified
                    if (angular.isUndefined(scope.searchConfig.id)) {
                        scope.searchConfig.id = 'search';
                    }
                    if (minCharactersToStartSearch >= maxlength) {
                        console.error('Wrong searchConfig: maxlength value should be more than the minimumCharacters value inside searchConfig to enable searching.');
                    }
                    scope.ariaLabel = $filter('xei18n')('search.aria.label');
                    if (angular.isDefined(scope.searchConfig.ariaLabel)) {
                        scope.ariaLabel = scope.ariaLabel + ' ' + scope.searchConfig.ariaLabel;
                    }
                }

                // Debouncing search call as per provided value or default is 0ms(immediate)
                /*jslint nomen: true*/
                onDataChange = _.debounce(function (data) {
                    if (angular.isUndefined(data)) { return; }

                    scope.onChange({query: data});
                }, scope.searchConfig.delay || 0);
                /*jslint nomen: false*/

                scope.$watch("searchConfig.searchString", function (newValue, oldValue) {
                    if (angular.isDefined(newValue)) {
                        var searchString = newValue.toString();

                        if (searchString.length > maxlength) {
                            searchString = searchString.substring(0, maxlength);
                            scope.searchConfig.searchString = searchString;
                        }

                        if (angular.isDefined(oldValue)) {
                            oldValue = oldValue.toString();
                        } else {
                            oldValue = '';
                        }

                        if (searchString !== oldValue) {
                            if (searchString.length > maxlength) {
                                searchString = searchString.substring(0, maxlength);
                                scope.searchConfig.searchString = searchString;
                            }

                            if (searchString.length >= minCharactersToStartSearch) {
                                onDataChange(searchString);
                            } else if ((searchString.length < oldValue.length) && (oldValue.length >= minCharactersToStartSearch) && (searchString.length < minCharactersToStartSearch)) {
                                onDataChange('');
                            }
                        }
                    }
                });

                scope.searchKeypress = function (data, id, e) {
                    if (!maxlength) {
                        e.preventDefault();
                    } else if (angular.isDefined(data) && data.length >= maxlength) {
                        e.preventDefault();
                        transformedInput = data.substring(0, maxlength);
                        scope.form[id].$setViewValue(transformedInput);
                        scope.form[id].$render();
                    }
                };

                scope.searchKeydown = function (data, id, e) {
                    if (e && e.which === keyCodes.ESC) {
                        e.preventDefault();
                        scope.form[id].$rollbackViewValue();
                        scope.form[id].$render();
                        scope.value = '';
                    } else if (e && e.which === keyCodes.ENTER) {
                        if (scope.loadingData || !scope.searchConfig.searchString.length || scope.searchConfig.searchString.length < minCharactersToStartSearch) {
                            e.preventDefault();
                            e.stopPropagation();
                        }
                    }

                    scope.onKeydown({$event: e});
                };
            }
        };
    }]);
}());
/**
    DataTable Module is used to render data in table format.

    HTML Markup(Syntax) :
    ------------------------
    <xe-table-grid
        tableId="dataTable"
        caption="Table Caption"
        header="headings"
        end-point="urlTest"
        fetch="fetchData(query)"
        post-fetch="postFetch(response, oldResult)"
        content="rows"
        results-found="records"
        toolbar="true"
        paginate="true"
        continuous-scrolling="false"
        continuous-scroll-parent="body"
        on-row-click="onClick(data,index)"
        on-row-double-click="onDoubleClick(data,index)"
        no-data-msg="No Results Found"
        empty-table-msg="emptyTableMsg"
        search-config="searchConfig"
        pagination-config="paginationConfig"
        draggable-column-names="draggableColumnNames"
        mobile-layout="mobileConfig"
        height="416px"
        refresh-grid="refreshGrid"
        >

        <xe-cell-markup heading-name="tick">
            <input type="checkbox" ng-click="someMethod()" value="all"/>
        </xe-cell-markup>

        <xe-cell-markup column-name="tick">
            <input type="checkbox"/>
        </xe-cell-markup>
    </xe-table-grid>

    Input :
    ----------
    Basically It requires two inputs
        1. Column headings
        2. Column Content / URL Endpoint / Fetch Method

    These should be in following format.

        $scope.headings = [
            {
                position: {desktop: 1, mobile: 1},   // Refer #1 under "Features Available:" below
                name: 'tick',                        // Json key to map with column data
                title: '',                           // Column heading name to display
                label: 'First Column Header (Home)', // Onhover tooltip text, used to show short cut keys if available
                ariaLabel: 'Short cut is Home.',     // Aria text for column header
                options: {                           // Refer #2 under "Features Available:" below
                    visible: true,
                    columnShowHide: false
                },
                width: '100px'
            },
            {position: {desktop: 2, mobile: 2}, name: 'rollNo', title: 'Roll No.', width: '23%', options: {visible: false, isSortable: false}},
            {position: {desktop: 3, mobile: 3}, name: 'studentName', title: 'Studnet Name', width: '23%', options: {visible: true, isSortable: true}},
            {position: {desktop: 4, mobile: 5}, name: 'subject', title: 'Subject', width: '23%', options: {visible: true, isSortable: true}},
            {position: {desktop: 5, mobile: 4}, name: 'marks', title: 'Marks', width: '23%', options: {visible: true, isSortable: true}}
        ];

        $scope.content = [
            {rollNo: 6, studentName: 'Vaikunt Naik', subject: 'Subject1', marks: 45},
            {rollNo: 2, studentName: 'Venuglopal Kathavate', subject: 'Subject2', marks: 50},
            {rollNo: 3, studentName: 'Ram', subject: 'Subject3', marks: 74},
            {rollNo: 4, studentName: 'Nethaji', subject: 'Subject5', marks: 85}
            {rollNo: 1, studentName: 'Mohan Venkatesh', subject: 'Subject5', marks: 65}
        ];

        $scope.urlTest = '/app/components/data';

        $scope.fetchData = function(query) {
            var deferred = $q.defer();
            var url = '/app/components/data' +
                        '?searchString=' + (query.searchString || '') +
                        '&sortColumnName=' + (query.sortColumnName || '') +
                        '&ascending=' + query.ascending +
                        '&offset=' + (query.offset || '') +
                        '&max=' + (query.max || '');

            $http.get(url)
                .success(function(data) {
                    deferred.resolve(data);
                })
                .error(function(data) {
                    deferred.reject(data);
                });

            return deferred.promise;
        };


    Output :
    -----------
    Using given arrays this directive will render the data in table format by enabling/disabling specified configurations for
    each individual columns and headings.


    Features Available :
    -----------------------
    1. Extensibility:
        - Hiding different fields under header/caption bar
            EX: {
                    "sections": [
                        {
                            "name": "dataTable",
                            "fields": [
                                {
                                    "name": "term",
                                    "exclude": true
                                }
                            ]
                        },{
                            "name": "dataTableCaptionBar",
                            "exclude": false,
                            "fields": [
                                {
                                    "name": "caption",
                                    "exclude": true
                                },
                                {
                                    "name": "columnFilterMenu",
                                    "exclude": true
                                },
                                {
                                    "name": "search",
                                    "exclude": true
                                }
                            ]
                        }
                    ]
                }
        - Reordering different columns
            EX: {
                    "sections": [
                        {
                            "name": "dataTable",
                            "fields": [
                                {
                                    "name": "term",
                                    "nextSibling": "subject"
                                }
                            ]
                        }
                    ]
                }
        - replacing columns attributes
            // TO-DO
    2. position: This configuration for each heading in $scope.headings array, orders the headings in the specified positions
        ex: position : {
                desktop: 1, // displays in 1st position for desktop & in 2nd position for mobile
                mobile: 2
            }

    3. options: This configuration for each heading in $scope.headings array, controls the different column behaviours
        ex: options : {
                visible: true,      // If "true" then display the column
                                    // If "false"/not specified then hide it

                titleVisible: false,// If "true"/not specified then display the column header name
                                    // If "false" then hide it

                isSortable: false,  // If "true" then it will provide sortable feature for the specified column
                                    // If "false"/not specified it does not display any sortable controls

                ascending: true,    // If "true" for a column then initially this column will be in ascending order,
                                    // If "false" then descending,
                                    // If not specified then no initial sorting will be applied to that column

                disable: false,     // If "true" then this column name will be disabled in the "Show/Hide Columns" settings menu and user can't check/uncheck this column to show/hide.
                                    // If "false"/not specified by default column name will be enabled in settings menu

                columnShowHide: true// If "true" then this column name will be removed from the "Show/Hide Columns" settings menu,
                                    // If "false"/not specified by default column name will be displayed in the "Show/Hide Columns" settings menu
            }

    4. Adding custom HTML elements
        As column heading: using attrabute heading-name="column name"
        ex: <xe-cell-markup heading-name="tick">
                <input type="checkbox" ng-click="someMethod()" value="all"/>
            </xe-cell-markup>

        As column Data: using attrabute column-name="column name"
        ex: <xe-cell-markup column-name="tick">
                <input type="checkbox"/>
            </xe-cell-markup>

    5. Exposing grid data array and no of records
        ex: content="rows" results-found="records"
        use these two attributes to get an reference to the currently displaying grid data set and the total no. of records available.

    6. Post fetch handler method(fetch callback method)
        ex: post-fetch="postFetch(response, oldResult)"
        use this attribute to do some extra processing just after grid data populates.

    7. How to make grid variable height(not fixed height tbody)??
        Ans: don't specify the height attribute of grid and
            specify the attribute "continuous-scroll-parent" with value as the id of scrollable parent html element / 'body' if the scroll is present on document/page wise,
            so that the continuous scroll will work in tab and mobiles.
            ex: continuous-scroll-parent="content" // #content has overflow: auto
            ex: continuous-scroll-parent="body" // body has overflow: auto

    8. No data found on search & empty table msg display
        ex: no-data-msg="No Results Found"
            empty-table-msg="emptyTableMsg"

        set no-data-msg attribute to display a custom message(ex. "no results found") inside grid on empty search scenario.
        In the above scenario "grid caption", "show/hide column", "search field", "column headers" & "custom message" will be visible, only "grid rows" will be hidden.

        set empty-table-msg attribute to display a custom message(ex. "You don't have access to view the grid") inside grid to handle not authorized scenarios.
        In the above scenario whole grid template will be hidden and a message will be shown instead.

    9. Search configuration to control search behaviour
        ex: $scope.searchConfig = {
                id: 'dataTableSearch',  // A unique id for search input element

                ariaLabel: 'Search for any course or section',
                                        // Additional aria text for search field, This is optional

                delay: 300,             // Debouncing frequent search calls to server when user types fast
                                        // defaults to: 0(in ms)

                searchString : 201410,  // provided search string to filter grid on initial load itself
                                        // defaults to: ''

                maxlength: 200,         // Specifying maximum length for user input
                                        // defaults to: infinite(∞)

                minimumCharacters : 2   // Limiting no of characters to start search
                                        // defaults to: 1
            };

    10. Pagination configuration to control pagination behaviour
        ex: $scope.paginationConfig = {
                pageLengths : [ 5, 10, 25], // Page offsets

                offset : 7,                 // Page offset to set on initial grid load
                                            // this value will be added to pageLengths [] if not present
            };

    11. Enabling drag and drop for columns
        ex: draggable-column-names="draggableColumnNames"
            $scope.draggableColumnNames = ['tick', 'term', 'crn', 'subject', 'status'];
        bind an array to draggable-column-names attribute for which drap&drop will be enabled.

    12. Mobile layout configuration [1: "single-column", 2: "two-columns", 3: "all-columns"]
        ex: mobile-layout="mobileConfig"
            $scope.mobileConfig = {
                term: 2,
                crn: 2,
                subject: 3,
                status: 3
            };

    13. Method exposed to refresh grid data without recreating/rerendering html
        ex: refresh-grid="refreshGrid"
            $scope.refreshGrid('Table Caption');
        call this method with "caption name" as parameter, to refresh corresponding grid data

*/

(function () {

'use strict';
angular.module('dataTableModule', ['utils'])
    .constant('mobileMaxWidth', 768)
    .directive('xeTableGrid', ['$timeout', 'accessibility', '$window', 'mobileMaxWidth', function ($timeout, accessibility, $window, mobileMaxWidth) {
        return {
            restrict : 'E',
            transclude : true,
            replace : true,
            scope: {
                tableId : '@',
                caption : '@?',
                header : '=',
                endPoint : '=?',
                fetch : '&',
                postFetch : '&',
                content : '=',
                resultsFound : '=?',
                toolbar : '=',
                paginate : '=?',
                continuousScrolling : '=?',
                continuousScrollParent : '@?',
                onRowClick  : '&',
                onRowDoubleClick : '&',
                noDataMsg : '@?',
                emptyTableMsg : '=?',
                searchConfig : '=',
                paginationConfig : '=',
                draggableColumnNames : '=?',
                mobileLayout : '=?',
                height : '@?',
                refreshContent: '=?refreshGrid',
                xeSection: '@?'
            },
            controller : ['$scope', '$filter', '$attrs', "$http",  function ($scope, $filter, $attrs, $http) {
                var orderBy = $filter('orderBy'),
                    filter  = $filter("filter"),
                    _this = this,
                    content,
                    previousSortColumn,
                    device;
                $scope.hideColumnSettingMenu = true;
                $scope.transcludes = {};
                $scope.headingTranscludes = {};
                $scope.hideContainer = false;
                $scope.sortArray = [];
                $scope.pagination = $scope.paginate;
                $scope.showPagination = true;

                if (!$scope.tableId) {
                    console.error("Provide a unique id for table");
                    return;
                }

                if (!$scope.pagination) {
                    $scope.pagination = $scope.continuousScrolling;
                }

                if (!$scope.toolbar && !$scope.caption) {
                    $scope.noCaptionBar = true;
                }

                if ($window.innerWidth > mobileMaxWidth) {
                    $scope.header = $filter('orderBy')($scope.header, 'position.desktop', false);
                    device = 'desktop';
                } else {
                    $scope.header = $filter('orderBy')($scope.header, 'position.mobile', false);
                    device = 'mobile';
                }

                /*
                * Applying extensibility after $scope level initializations and the initial header ordering
                */
                applyExtensions('captionBar');
                applyExtensions('header');

                if (angular.isObject($scope.searchConfig) && angular.isUndefined($scope.searchConfig.searchString)) {
                    $scope.searchConfig.searchString = '';
                }

                /*
                    START: Shared properties and methods across directives
                */
                // Used in pagination directive
                // If emptyTableMsg is set no need to fetch the data for grid
                this.emptyTableMsg = $scope.emptyTableMsg;

                // Used in pagination directive
                // Method to show/hide the spinner while fetching data
                this.loadingDataIndicator = function (loading) {
                    $scope.loadingData = loading;
                };

                // Used in pagination directive
                // If continuous scrolling is true then hide pagination on tablets and mobile.
                this.hidePaginationIfContinuousScroll = function () {
                    $scope.showPagination = !$scope.continuousScrolling;
                };
                /*
                    END: Shared properties and methods across directives
                */

                // If Pagination is false, then all the data will be loaded at once and no need to hit the server for sorting.
                // Sorting will be done on model data.
                $scope.onSort = function (params) {
                    _this.sortColumnName = params.heading.name;
                    _this.ascending = !_this.ascending;
                    // TODO: Too many ifs. Revisit this.
                    if (!$scope.pagination) {
                        if (params.heading.options.sortable) {
                            if (!angular.isDefined($attrs.fetch)) {
                                // Model sort
                                $scope.content = orderBy($scope.content, _this.sortColumnName, !_this.ascending);
                            } else {
                                // Server side sort
                                _this.loadingDataIndicator(true);
                                $scope.fetch({
                                    query: {
                                        searchString: $scope.searchConfig.searchString,
                                        sortColumnName: _this.sortColumnName,
                                        ascending: _this.ascending
                                    }
                                }) // success
                                    .then(
                                        function (data) {
                                            $scope.postFetch({response: data, oldResult: $scope.content});
                                            $scope.content = data.result;
                                            _this.loadingDataIndicator(false);
                                        },// error
                                        function (data) {
                                            console.error(data);
                                            $scope.postFetch({response: data, oldResult: $scope.content});
                                            _this.loadingDataIndicator(false);
                                        }
                                    );
                            }
                        }
                    } else {
                        if (params.heading.options.sortable) {
                            _this.sort(_this.sortColumnName, _this.ascending);
                        }
                    }
                };

                $scope.handleDrop = function(draggedFrom, draggedTo) {
                    $scope.header = orderColumns($scope.header, draggedFrom, draggedTo);
                };

                function applyExtensions(sectionName) {
                    var gridXESection;
                    var gridSectionExtns;

                    if (typeof xe !== 'undefined' && xe.extensionsFound ) {
                        switch (sectionName) {
                            case 'captionBar':
                                gridXESection = $scope.xeSection + 'CaptionBar';

                                if (gridXESection) {
                                    gridSectionExtns = _.find(xe.extensions.sections, function (section) {
                                        return section.name == gridXESection;
                                    });

                                    if (gridSectionExtns) {
                                        setVisibilityForCaptionBar(gridSectionExtns);
                                    }
                                }
                                break;
                            case 'header':
                                gridXESection = $scope.xeSection;

                                if (gridXESection) {
                                    gridSectionExtns = _.find(xe.extensions.sections, function (section) {
                                        return section.name == gridXESection;
                                    });

                                    if (gridSectionExtns) {
                                        $scope.header = setVisibilityForHeaders(gridSectionExtns, $scope.header);
                                        $scope.header = orderHeaders(gridSectionExtns, $scope.header);
                                    }
                                }
                        }
                    }
                }

                function setVisibilityForCaptionBar(sectionExtns) {
                    if (sectionExtns.exclude) {
                        $scope.noCaptionBar = true;
                        return;
                    }

                    _.each( sectionExtns.fields, function (extensibleField) {
                        if (extensibleField.exclude) {
                            $scope['no' + extensibleField.name] = true;
                        }
                    });
                }

                function setVisibilityForHeaders(sectionExtns, columnHeaders) {
                    var updatedOption, columnToHide;

                    _.each( sectionExtns.fields, function (extensibleField) {
                        columnToHide  = _.findWhere(columnHeaders, { name: extensibleField.name } );

                        if(angular.isDefined(columnToHide)) {  //Set visibility to false only if the column exists
                            updatedOption = extensibleField.exclude ? { visible: !extensibleField.exclude, columnShowHide: !extensibleField.exclude } : {};
                            _.extend( _.findWhere(columnHeaders, { name: extensibleField.name } ).options, updatedOption );
                        }
                    });

                    return columnHeaders;
                }

                function orderHeaders(sectionExtns, columnHeaders) {
                    _.each( sectionExtns.fields, function (extension) {
                        if ( _.has(extension, "nextSibling") ) {
                            var current_field_idx = _.indexOf(_.pluck(columnHeaders, 'name'), extension.name);
                            var nextSibling_idx = _.indexOf(_.pluck(columnHeaders, 'name'), extension.nextSibling);

                            if (_.isNull(extension.nextSibling) && current_field_idx >= 0) {
                                // A “nextSibling” of null indicates that the element should be placed as the last element of its siblings
                                // {"name": "field2", "nextSibling": null}
                                var lastSibling = _.last(columnHeaders).name;

                                // Reusing same method used for drag-drop column functionality
                                columnHeaders = orderColumns(columnHeaders, extension.name, lastSibling);
                            } else if (current_field_idx >= 0 && nextSibling_idx >= 0) {
                                var prev_of_nextSibling = ((current_field_idx < nextSibling_idx) && columnHeaders[nextSibling_idx - 1]) ? columnHeaders[nextSibling_idx - 1].name : columnHeaders[nextSibling_idx].name;

                                // Reusing same method used for drag-drop column functionality
                                columnHeaders = orderColumns(columnHeaders, extension.name, prev_of_nextSibling);
                            }
                        }
                    });

                    return columnHeaders;
                }

                function orderColumns(columnHeaders, draggedFrom, draggedTo) {
                    var srcIdx = _.indexOf(_.pluck(columnHeaders, 'name'), draggedFrom),
                        destIdx = _.indexOf(_.pluck(columnHeaders, 'name'), draggedTo);

                    if (srcIdx >= 0 && destIdx >= 0) {
                        var element = columnHeaders[srcIdx];
                        columnHeaders.splice(srcIdx, 1);
                        columnHeaders.splice(destIdx, 0, element);

                        _.each(columnHeaders, function(item, index) {
                            item.position[device] = index + 1;
                        });
                    }

                    return columnHeaders;
                }


                var defaultOptions = {visible: true, sortable: false};

                angular.forEach($scope.header,function(value, index){
                    if (angular.isUndefined(value.width)) {
                        value.width = '';
                    }

                    $scope.$watch(function () {
                        return $scope.header[index].options.visible;
                    }, function (newValue, oldValue) {
                        if (newValue !== oldValue) {
                            $timeout(function() {
                                $scope.populateHeaderWidths(angular.element('#' + $scope.tableId));
                            }, 0);
                        }
                    });

                    if (angular.isDefined(value.options)) {
                        if (angular.isDefined(value.options.ascending)) {
                            $scope.sortArray[value.name] = {ascending : value.options.ascending , decending : !value.options.ascending};
                            previousSortColumn = value.name;
                            _this.ascending = value.options.ascending;
                            _this.sortColumnName = value.name;
                        } else {
                            $scope.sortArray[value.name] = {ascending : false , decending : false};
                        }

                        if (!angular.isDefined(value.options.visible)) {
                            value.options.visible = true;
                        }

                        if (!angular.isDefined(value.options.sortable)) {
                            value.options.sortable = false;
                        }
                    } else {
                        value.options =  defaultOptions;
                    }
                });

                // As endsWith is not supported by IE and opera using userDefined funtion
                function endsWith(str, suffix) {
                    return str.indexOf(suffix, str.length - suffix.length) !== -1;
                }

                function calculateWidth(width, parentWidth, headerFontSize) {
                    if (endsWith(width, '%')) {
                        width = Math.floor(((parentWidth * width.substr(0, width.indexOf('%'))) / 100));
                    } else if (endsWith(width, 'em')) {
                        width = Math.floor((headerFontSize * width.substr(0, width.indexOf('em'))));
                    } else if (endsWith(width, 'px')) {
                        width = Math.floor(width.substr(0, width.indexOf('px')));
                    } else {
                        width = 0;
                    }
                    return width;
                }

                // Populating the header widths based on it's visibility
                $scope.populateHeaderWidths = function(table) {
                    table.find('.thead, .tbody').width('');

                    var dynamicWidthColumnCount = 0,
                        undefinedWidthCount = 0,
                        headerWidth = table.find('.tbody thead').width(),
                        headerFontSize = parseFloat(table.find('.tbody thead').css('font-size')) || 16,
                        availableWidth = headerWidth,
                        invisibleColumnWidths = 0,
                        scrollableContainerWidth = table.find('.hr-scrollable-content')[0] ? table.find('.hr-scrollable-content')[0].scrollWidth : 0,
                        scrollableContentWidth = table.find('.tbody')[0] ? table.find('.tbody').width() : 0;

                    if (scrollableContentWidth < scrollableContainerWidth) {
                        // Setting width to display overflowed contents incase of overflow-x
                        // This is needed because when overflow-y is enabled, as per browser standard it hides overflowed x-content
                        table.find('.thead, .tbody').width(scrollableContainerWidth - 1);
                    }

                    // Calculating 'px' value of the column widths specified in header configuration
                    angular.forEach($scope.header, function (heading) {
                        var width = heading.width;

                        if ($window.innerWidth < mobileMaxWidth) {
                            heading.dynamicWidth = '';
                            availableWidth = 0;
                        } else if (heading.options.visible) {
                            width = calculateWidth(width, headerWidth, headerFontSize);

                            if (width) {
                                heading.dynamicWidth = width;
                                availableWidth = availableWidth - heading.dynamicWidth;
                            } else {
                                undefinedWidthCount++;
                            }

                            if (endsWith(heading.width, '%')) {
                                dynamicWidthColumnCount++;
                            }
                        } else {
                            width = calculateWidth(width, headerWidth, headerFontSize);

                            if (width) {
                                invisibleColumnWidths+=width;
                            }
                        }
                    });

                    if (undefinedWidthCount || invisibleColumnWidths) {
                        var undefinedWidth = Math.floor(availableWidth / undefinedWidthCount),
                            availableWidthPerColumn = Math.floor(invisibleColumnWidths / dynamicWidthColumnCount);

                        angular.forEach($scope.header, function (heading) {
                            if (heading.options.visible) {
                                if (undefinedWidth && heading.width.trim() === '') {
                                    heading.dynamicWidth = undefinedWidth;
                                } else if (endsWith(heading.width, '%')) {
                                    heading.dynamicWidth+=availableWidthPerColumn;
                                }
                            }
                        });
                    }
                };

                // This block loads the data for data table if its not provided by the user.
                // Also checks whether application specific search is available or not. if not available calls directive search method.
                // This mainly works on model data.
                function loadData() {
                    if (!$attrs.fetch && !$scope.emptyTableMsg) {
                        $scope.fetch = function (data) {
                            if (!content) { content = $scope.content; }
                            $scope.content = orderBy(
                                filter(content, data.query.searchString, false),
                                _this.sortColumnName,
                                _this.ascending
                            );
                            $scope.resultsFound = $scope.content.length;
                            _this.loadingDataIndicator(false);
                        };

                        if (!$scope.pagination && !$attrs.endPoint) {
                            console.error("Provide either end-point or fetch attribute");
                        } else if (!$scope.pagination) {
                            _this.loadingDataIndicator(true);
                            $http.get($scope.endPoint + "?searchString=" + $scope.searchConfig.searchString + "&sortColumnName=" + (_this.sortColumnName || "") + "&ascending=" + (_this.ascending || ""))
                                .success(function (data) {
                                    $scope.postFetch({response: data, oldResult: $scope.content});
                                    $scope.content = data.result;
                                    $scope.resultsFound = $scope.content.length;
                                    _this.loadingDataIndicator(false);
                                })
                                .error(function (data) {
                                    console.error(data);
                                    $scope.postFetch({response: data, oldResult: $scope.content});
                                    _this.loadingDataIndicator(false);
                                });
                        }
                    } else if (!$scope.pagination && !$scope.emptyTableMsg) {
                        _this.loadingDataIndicator(true);
                        $scope.fetch({
                            query: {
                                searchString: $scope.searchConfig.searchString,
                                sortColumnName: _this.sortColumnName,
                                ascending: _this.ascending
                            }
                        }).then(
                            // success
                            function (data) {
                                $scope.postFetch({response: data, oldResult: $scope.content});
                                $scope.content = data.result;
                                $scope.resultsFound = $scope.content.length;
                                _this.loadingDataIndicator(false);
                            },
    	                    // error
    	                    function(data) {
    	                        console.error(data);
                                $scope.postFetch({response: data, oldResult: $scope.content});
    	                        _this.loadingDataIndicator(false);
    	                    }
                   	    );
				    }
                }

                loadData();

                // TODO: This is just a temporary arrangement to change the search string. Ideally two-way data-binding should this job for us.
				// Right now with nested directive two-way data-biding is not working. Need to revisit this.
				$scope.fetchSpecial = function(searchString) {

					if (!$scope.pagination) {
						var promise = $scope.fetch({
							query: {
								searchString: searchString,
								sortColumnName: _this.sortColumnName,
								ascending: _this.ascending
							}
						});

						_this.loadingDataIndicator(true);

						if (promise) {
							promise.then(
								// success
								function(data) {
                                    $scope.postFetch({response: data, oldResult: $scope.content});
		                        	$scope.content = data.result;
                                    $scope.resultsFound = $scope.content.length;
		                        	_this.loadingDataIndicator(false);
			                    },
			                    // error
			                    function(data) {
			                        console.error(data);
                                    $scope.postFetch({response: data, oldResult: $scope.content});
			                        _this.loadingDataIndicator(false);
			                    }
		                   	);
						}
					} else {
                        _this.fetchData(1);
                    }
				};
				// END TODO: May be use Factory pattern

				// Method to store html objects added during data table declaration
				this.registerTransclude = function (directiveTransclude) {
          			var id = directiveTransclude.id;
         			 $scope.transcludes[id] = directiveTransclude;
        		};

                // Method to store heading html objects added during data table declaration
                this.registerHeadingTransclude = function (directiveTransclude) {
                    var id = directiveTransclude.id;
                     $scope.headingTranscludes[id] = directiveTransclude;
                };

                $scope.onSearchFocus = function (event) {
                    $scope.hideContainer= !$scope.hideContainer;
                };

                $scope.onSearchBlur = function (event) {
                    $scope.hideContainer= !$scope.hideContainer;
                    angular.element(event.target).val($scope.searchConfig.searchString);
                };

                $scope.sortOnHeading = function(heading, headerIndex) {

                	if(heading.options.sortable) {
	                	var columnName = heading.name;

	                    if(previousSortColumn == columnName) {
	                        $scope.sortArray[columnName] = {
	                        	ascending : !$scope.sortArray[columnName].ascending,
	                        	decending : !$scope.sortArray[columnName].decending
	                        };
	                    } else {
	                        previousSortColumn = columnName;
	                        for(var obj in $scope.sortArray) {
	                            if(obj == columnName)  {
	                                $scope.sortArray[obj] = {ascending : true , decending : false};
	                            }
	                            else{
	                                $scope.sortArray[obj] = {ascending : false , decending : false};
	                            }
	                        }
	                    }
	                }
                };

                if (document.doctype && navigator.appVersion.indexOf("MSIE 9") > -1) {
                    document.addEventListener('selectstart', function (e) {
                        for (var el = e.target; el; el = el.parentNode) {
                            if (el.attributes && el.attributes.draggable) {
                                e.preventDefault();
                                e.stopImmediatePropagation();
                                el.dragDrop();
                                return false;
                            }
                        }
                    });
                }

                $scope.getObjectValue = function getter(object, key) {
                    var value;

                    if (typeof object === 'object' && typeof key === 'string') {
                        value = eval('object' + '.' + key);
                    }

                    return value;
                };

                $scope.refreshContent = function(refresh) {
                    if (refresh) {
                        if (!$attrs.fetch || !$scope.pagination) {
                            loadData();
                        } else if (_this.fetchData) {
                            _this.fetchData(1);
                        }
                        $scope.populateHeaderWidths(angular.element('#' + $scope.tableId));
                    }
                };
			}],
			templateUrl: function(element, attr){
				return 'templates/dataTable.html';
			},
            compile: function compile(tElement, tAttrs) {
                // Setting opacity of table to 0 till html rendering completes(to avoid displaying UI distortions)
                tElement.css('opacity', 0);

                if (tAttrs.paginate === "true" || tAttrs.continuousScrolling === "true") {
     		 		var paginationObject = tElement.find("xe-pagination");

     		 		if(angular.isDefined(tAttrs.fetch)) {
                        paginationObject.attr('fetch', 'fetch({query: query})');
                    }

                    if(angular.isDefined(tAttrs.postFetch)) {
                        paginationObject.attr('post-fetch', 'postFetch({response: response, oldResult: oldResult})');
                    }

                    if(angular.isDefined(tAttrs.paginationConfig)) {
                        paginationObject.attr('pagination-config', 'paginationConfig');
                    }

                    if(tAttrs.endPoint){
                        paginationObject.attr('end-point', 'endPoint');
                    }
     		 	}  else {
     		 		// Removing pagination if its not set to true, to avoid getting executed even when its not needed.
     		 		tElement.find("xe-pagination").remove();
     		 	}

                return function postLink(scope, element, attrs, controller) {
                    angular.element(".tfoot").remove();

                    $timeout(function() {
                        // Resetting opacity of table after html rendering completes
                        element.css('opacity', 1);

                        scope.populateHeaderWidths(element);

                        accessibility.provideAccessibilityForTable(element, angular.element('#' + scope.continuousScrollParent));

                        scope.nextPage = function() {
                        	if (scope.pagination &&
                                element.find(".pagination-container").is(':hidden') &&
                                controller.next) {
                        		controller.next(true);
                        	}
                        };
                    });
             	};
            }
        };
	}])
	.directive('xeCellInjector',[function() {
		return {
			require : '^xeTableGrid',
			restrict :'A',
 			replace:true,
 			scope:true,
			/*
				This block to provide DOM manipulation methods if any.
			*/
			link :function(scope, element, attrs, controllerInstance,$transclude){
				var id = attrs.name;
				var transclude = scope.transcludes[id];
				if(transclude){
					var  scopeRowValue = scope.row[attrs.name];
                    if (angular.isUndefined(scopeRowValue)) {
                        var newScopeObj = transclude.scope.$new();
                        transclude.transclude(newScopeObj, function(transcludeEl, transcludeScope) {
                            transcludeScope.row = scope.row;
                            element.append(transcludeEl);
                        });
                    } else {
                        transclude.transclude(scope, function(clone, scope) {
                            element.html(clone);
                        });
                    }
				}
			}
		};
	}])
	.directive('xeRowInjector',[function() {
        var previousElement;
		return {
			restrict :'A',
 			replace:true,
 			require : '^xeTableGrid',
 			scope:true,
 			link :function(scope, element, attrs, controllerInstance,$transclude) {
                element.on("click",function(event){
                    if(previousElement){
                       previousElement.removeClass("active-row");
                    }
                    element.addClass("active-row");
                    previousElement = element;
                });
 			}
 	  };
    }])
    .directive('xeHeadingInjector',[function() {
        return {
            restrict :'A',
            replace:true,
            require : '^xeTableGrid',
            scope:true,
            link :function(scope, element, attrs, controllerInstance, $transclude) {
                var id = attrs.name;
                var transclude = scope.headingTranscludes[id];
                if(transclude){
                    var newScopeObj = transclude.scope.$new();
                    transclude.transclude(newScopeObj,function(transcludeEl, transcludeScope){
                        if (element.find('.data').length) {
                            element.find('.data').append(transcludeEl);
                        } else {
                            element.append(transcludeEl);
                        }
                    });
                }
            }
      };
    }])
    .directive('xeCellMarkup',[function() {
		return {
			restrict :'EA',
			transclude:'element',
			replace:true,
			scope:true,
			require:"^xeTableGrid",
			link:function(scope, element, attrs, controllerInstance,$transclude) {
                var directiveTransclude;

                if (attrs.columnName && controllerInstance.registerTransclude) {
                    directiveTransclude = {
                        id: attrs.columnName,
                        transclude: $transclude,
                        element: element,
                        scope : scope
                    };
                    controllerInstance.registerTransclude(directiveTransclude);
                } else if (attrs.headingName && controllerInstance.registerHeadingTransclude) {
                    directiveTransclude = {
                        id: attrs.headingName,
                        transclude: $transclude,
                        element: element,
                        scope : scope
                    };
                    controllerInstance.registerHeadingTransclude(directiveTransclude);
                }

			}
		};
	}])
    .directive('attainMobileLayout', function() {
    	// TODO: Check if using number as object name best practice. Its valid according to JS spec.
    	var columnClasses = {1: "single-column", 2: "two-columns", 3: "all-columns"};
    	return {
    		restrict: "A",
    		scope: true,
    		link: function(scope, element, attrs) {
    			element.addClass(columnClasses[parseInt(attrs.attainMobileLayout)]);
    		}
    	};
    })
    .directive('droppable', ['$parse', function($parse) {
            return {

                link: function(scope, element, attr) {

                }
            };
        }
    ])
    .directive('dragDrop', function() {
        return {
            link: function(scope, element, attr) {
                var enterTarget = null,
                    index = element.index();

                // Draggable column check
                if (scope.draggableColumnNames.indexOf(scope.header[index].name) === -1) {
                    return;
                }
                element.attr("draggable", true);

                function  dragstart(event) {
                    angular.element(event.target).addClass('dragged');
                    index = angular.element(event.target).closest('th').index() + 1;
                    element.closest('.table-container').find("td:nth-child(" + index + ")").addClass('dragged');

                    var sendData = angular.element(event.target).data('name');
                    event.originalEvent.dataTransfer.setData('text', sendData);
                }

                function dragend(event) {
                    angular.element(event.target).removeClass('dragged');
                    index = angular.element(event.target).closest('th').index() + 1;
                    element.closest('.table-container').find("td:nth-child(" + index + ")").removeClass('dragged');
                    angular.element(".drag-enter").removeClass('drag-enter');
                    angular.element('#dragtable').hide();
                }

                function dragenter(event) {
                    angular.element(event.target).closest('th').addClass('drag-enter');
                    index = angular.element(event.target).closest('th').index() + 1;
                    element.closest('.table-container').find("td:nth-child(" + index + ")").addClass('drag-enter');
                    enterTarget = event.target;
                    event.preventDefault();
                }

                function dragleave(event) {
                    if (enterTarget == event.target) {
                        angular.element(event.target).closest('th').removeClass('drag-enter');
                        index = angular.element(event.target).closest('th').index() + 1;
                        element.closest('.table-container').find("td:nth-child(" + index + ")").removeClass('drag-enter');
                    }
                }

                function onDragOver(event) {
                    processEvent(event);

                    event = event || window.event;
                    var dragX = event.originalEvent.pageX, dragY = event.originalEvent.pageY - 170;

                    angular.element('#dragtable').show();

                    angular.element('#dragtable').css({
                        left:  dragX,
                        top:   dragY
                    });
                    event.originalEvent.dataTransfer.dropEffect = 'move';

                    if (event.preventDefault) {
                        event.preventDefault();
                    }
                    return false;
                }

                function onDrop(event) {
                    angular.element('#dragtable').hide();
                    processEvent(event);

                    var fromHeader =  event.originalEvent.dataTransfer.getData('text');
                    var toHeader = angular.element(event.target).closest('th').data('name');
                    var dropfn = attr.dragDrop;

                    scope.$apply(function() {
                        scope[dropfn](fromHeader, toHeader);
                    });
                }

                function processEvent(e) {
                    if (e.preventDefault) {
                        e.preventDefault();
                    }
                    if (e.stopPropagation) {
                        e.stopPropagation();
                    }
                }

                element.bind("dragover", onDragOver);
                element.bind("drop", onDrop);
                element.bind("dragstart", dragstart);
                element.bind("dragend", dragend);
                element.bind("dragenter", dragenter);
                element.bind("dragleave", dragleave);
            }
        };
    })

    /*
        DataTable resize handlers
    */
    .directive('resize', ['$timeout', '$window', '$filter', 'Language', 'mobileMaxWidth', function ($timeout, $window, $filter, Language, mobileMaxWidth) {
        return function ($scope, element, attr) {
            $timeout(function() {
                // Watch to resize headers & populate their widths based on visibility on window/table resize
                $scope.$watch(
                    function () {
                        return element.closest('.table-container').width();
                    },
                    function (newValue, oldValue) {
                        if (newValue !== oldValue) {
                            if ($window.innerWidth > mobileMaxWidth) {
                                $scope.header = $filter('orderBy')($scope.header, 'position.desktop', false);

                                // Adjust dataTable header widths on window resize
                                $scope.populateHeaderWidths(element.closest('.table-container'));
                                adjustHeader();
                            } else {
                                $scope.header = $filter('orderBy')($scope.header, 'position.mobile', false);
                            }
                        }
                    }
                );

                // Watch to resize headers on resolve of each search result
                $scope.$watch(
                    function () {
                        return element[0].scrollHeight;
                    },
                    function (newValue, oldValue) {
                        if ($window.innerWidth > mobileMaxWidth) {
                            adjustHeader();
                        }
                    }
                );
            });

            function adjustHeader() {
                var adjustHeader = (element[0].scrollHeight > element[0].clientHeight) && (element[0].clientWidth !== element[0].offsetWidth);
                if (adjustHeader) {
                    var headerPadding = (element.width() - element[0].scrollWidth) + 'px';
                    $scope.headerPadding = Language.isRtl() ? {'padding-left': headerPadding} : {'padding-right': headerPadding};
                } else {
                    $scope.headerPadding = {};
                }
            }

            // Debouncing window resize trigger within every 500ms
            var w = angular.element($window);
            var applyScope = _.debounce(function () {
                    $scope.$apply();
                }, 500);

            w.bind('resize', applyScope);

        };
    }])
    .directive('tabIndex', [function(){
        return {
            link: function(scope, element, attrs) {
                element.find('a, :input, [tabindex=0]')
                    .each(function(index, elem) {
                        angular.element(elem).attr('tabindex', -1);
                    });
            }
        };
    }]);
}());

/*****************************************************
 *  © 2016 Ellucian Company L.P. and its affiliates. *
 *****************************************************/

; // safe for iife
(function () {

    'use strict';

    angular.module('pieChartModule', [])
        .factory('d3', ['$window', '$log', function ($window, $log) {
            if (!$window.d3) {
                $log.error('D3 must be present');
            }
            return $window.d3;
        }])
        .directive('xePieChart', ['d3', '$window', '$log', '$timeout', 'Language', function (d3, $window, $log, $timeout, Language) {

            var uniqueId = 0;

            function draw(tooltip, table, svg, radius, scope, options, element) {

                // var declaration
                var subdata = [], other, subdatatotal, total,
                    subPieStartAngle, subPieEndAngle,
                    pie, subPie,
                    arc, lineArc, lineOuterArc, subArc, lineSubArc, lineSubOuterArc,
                    color, subColor,
                    slice, text, polyline, bgSubSlice, subslice, subtext, subpolyline, ua, msie, trident, edge;

                // remove the old data
                if (table) {
                    table.select('thead').remove();
                    table.select('tbody').remove();
                    // append new element
                    table.append('thead').attr('class', 'xe-pie-table-head');
                    table.append('tbody').attr('class', 'xe-pie-table-body');
                }

                svg.selectAll('g').remove();

                svg.append("g").attr({
                    "role": "presentation",
                    "class": "xe-pie-slices"
                });
                svg.append("g").attr({
                    "role": "listitem",
                    "class": "xe-pie-labels",
                    'id': "main-group-" + uniqueId,
                    'aria-live': "polite",
                    'aria-relevant': "additions removals"
                });
                svg.append("g").attr({
                    "role": "presentation",
                    "class": "xe-pie-lines"
                });

                // render when if needed
                if (scope.data.length > 7) {
                    svg.append('g').attr({
                        'role': 'presentation',
                        'class': 'xe-pie-bg-sub-slice'
                    });
                    svg.append("g").attr({
                        'role': 'presentation',
                        "class": "xe-pie-sub-slices"
                    });
                    svg.append("g").attr({
                        'role': 'listitem',
                        "class": "xe-pie-sub-labels",
                        'id': "other-group-" + uniqueId,
                        'aria-live': "polite",
                        'aria-relevant': "additions removals"
                    });
                    svg.append("g").attr({
                        'role': 'presentation',
                        "class": "xe-pie-sub-lines"
                    });
                }

                function tabulate(data, columns) {
                    // var declaration
                    var thead, tbody, rows, dtLabel = columns[0], dtValue = columns[1], dtPercentage = columns[2];

                    thead = table.select('thead.xe-pie-table-head');
                    tbody = table.select('tbody.xe-pie-table-body');

                    // append the header row
                    thead.append("tr")
                        .selectAll("th")
                        .data(columns)
                        .enter()
                        .append("th")
                        .text(function (column) {
                            return column;
                        });

                    // create a row for each object in the data
                    rows = tbody.selectAll("tr")
                        .data(data)
                        .enter()
                        .append("tr");

                    // create a cell in each row for each column
                    rows.selectAll("td")
                        .data(function (row) {
                            return columns.map(function (column) {
                                row[dtLabel] = row.label;
                                row[dtValue] = row.value;
                                if (!Language.isRtl()) {
                                    if (subdatatotal || row[dtValue]) {
                                        row[dtPercentage] = (Math.round(1000 * row[dtValue] / subdatatotal) / 10).toFixed(2) + '%';
                                    } else {
                                        row[dtPercentage] = '0.00%';
                                    }
                                }
                                if (Language.isRtl()) {
                                    if (subdatatotal || row[dtValue]) {
                                        row[dtPercentage] = '%' + (Math.round(1000 * row[dtValue] / subdatatotal) / 10).toFixed(2);
                                    } else {
                                        row[dtPercentage] = '%0.00';
                                    }
                                }
                                return { column: column, value: row[column] };
                            });
                        })
                        .enter()
                        .append("td")
                        .html(function (d) {
                            return d.value;
                        });

                    return table;
                }
                function detectIE() {
                    ua = window.navigator.userAgent;
                    msie = ua.indexOf('MSIE ');
                    trident = ua.indexOf('Trident/');
                    if (msie > 0 || trident > 0) {
                        return true;
                    }
                    return false;
                }
                function detectEdge() {
                    edge = ua.indexOf('Edge/');
                    if (edge > 0) {
                        return true;
                    }
                    return false;
                }
                function mouseover(d) {
                    if (tooltip && d3.select(this).style("opacity") !== '0') {
                        tooltip.select('.xe-pie-tooltip-label').html(d.data.label);
                        tooltip.select('.xe-pie-tooltip-value').html(d.data.value);
                        tooltip.select('.xe-pie-tooltip-percent').html(d.data.percentage);
                        tooltip.style('display', 'block');
                    }
                }

                function mouseout(d) {
                    if (tooltip) {
                        tooltip.style('display', 'none');
                    }
                }

                function mousemove(d) {
                    if (tooltip) {
                        var te = angular.element(element).find('.xe-pie-tooltip')[0],
                            width = te.clientWidth,
                            height = te.clientHeight;
                        if (Language.isRtl() && detectIE()) {
                            tooltip.style('top', (d3.event.layerY - height - 10) + 'px').style('left', ((d3.event.layerX - width + 10) / 2) + 'px');
                        } else {
                            tooltip.style('top', (d3.event.layerY - height - 10) + 'px').style('left', (d3.event.layerX - width / 2) + 'px');
                        }
                    }
                }

                function key(d) {
                    return d.data.label;
                }

                function midAngle(d) {
                    return d.startAngle + (d.endAngle - d.startAngle) / 2;
                }

                function wrap(textWrap) {
                    textWrap.each(function () {
                        var t = d3.select(this),
                            si = 0,
                            ei = 10,
                            w = t.text(),
                            l = w.substring(si, ei),
                            y = t.attr("y"),
                            dy = parseFloat(textWrap.attr("dy")),
                            tspan = t.text(null).append("tspan").attr("x", 0).attr("y", y).attr("dy", dy + "em");

                        while (l) {
                            if (si === 0) {
                                tspan.text(l.trim());
                            } else {
                                tspan = t.append("tspan").attr("x", 0).attr("y", y).attr("dy", dy + "em").text(l.trim());
                            }
                            si += 10;
                            ei += 10;
                            l = w.substring(si, ei);
                        }
                    });
                }

                function relaxSub() {
                    // var declaration
                    var again, labelElements;

                    again = false;

                    if (subtext) {

                        subtext.each(function (d1) {

                            var that = this,
                                a = this.getBoundingClientRect();

                            subtext.each(function (d2) {

                                if (this !== that) {
                                    var b = this.getBoundingClientRect(), dy, tt, to;
                                    if ((Math.abs(a.left - b.left) * 2 < (a.width + b.width)) && (Math.abs(a.top - b.top) * 2 < (a.height + b.height))) {
                                        // overlap, move labels
                                        dy = (Math.max(0, a.bottom - b.top) + Math.min(0, a.top - b.bottom)) * 0.01;
                                        tt = d3.transform(d3.select(this).attr("transform"));
                                        to = d3.transform(d3.select(that).attr("transform"));
                                        again = true;

                                        to.translate = [radius * 0.65 * (midAngle(d1) < Math.PI ? 1 : -1), to.translate[1] + dy];
                                        tt.translate = [radius * 0.65 * (midAngle(d2) < Math.PI ? 1 : -1), tt.translate[1] - dy];
                                        d3.select(this).attr("transform", "translate(" + tt.translate + ")");
                                        d3.select(that).attr("transform", "translate(" + to.translate + ")");
                                    }
                                }
                            });
                        });

                        if (again) {
                            labelElements = subtext[0];
                            if (subpolyline) {
                                subpolyline.attr('points', function (d, i) {
                                    var labelForLine = d3.select(labelElements[i]),
                                        t = d3.transform(labelForLine.attr("transform")),
                                        tx = t.translate[0],
                                        ty = t.translate[1];
                                    return [lineSubArc.centroid(d), lineSubOuterArc.centroid(d), tx, ty];
                                });
                            }
                            $timeout(relaxSub);
                        }
                    }
                }

                function toggle(d, i) {
                    d3.event.preventDefault();
                    d3.event.stopPropagation();
                    if (i === 6 && subdata.length > 7) {
                        slice.style('opacity', function (d, i) {
                            if (i === 6) {
                                return 1.0;
                            }
                            return 0.1; // 10% of opacity
                        });
                        text.style('display', 'none').attr('aria-hidden', true);
                        polyline.style('display', 'none').attr('aria-hidden', true);
                        if (bgSubSlice || subslice || subtext || subpolyline) {
                            bgSubSlice.style('display', 'block').attr('aria-hidden', false);
                            subslice.style('display', 'block').attr('aria-hidden', false);
                            subtext.style('display', 'block').attr('aria-hidden', false);
                            subpolyline.style('display', 'block').attr('aria-hidden', false);
                            subtext.selectAll('.xe-pie-light-text').call(wrap);
                            relaxSub();
                        }
                    } else {
                        if (bgSubSlice || subslice || subtext || subpolyline) {
                            bgSubSlice.style('display', 'none').attr('aria-hidden', true);
                            subslice.style('display', 'none').attr('aria-hidden', true);
                            subtext.style('display', 'none').attr('aria-hidden', true);
                            subpolyline.style('display', 'none').attr('aria-hidden', true);
                        }
                        slice.style('opacity', 1.0);
                        text.style('display', 'block').attr('aria-hidden', false);
                        polyline.style('display', 'block').attr('aria-hidden', false);
                    }
                }

                function touchTarget() {
                    d3.select(this).classed("xe-pie-touch", !d3.select(this).classed("xe-pie-touch"));
                }

                function relax() {
                    // var declaration
                    var again, labelElements;

                    again = false;

                    if (text) {

                        text.selectAll("text").each(function (d1) {

                            var that = this,
                                a = this.getBoundingClientRect();

                            text.selectAll("text").each(function (d2, i) {

                                if (this !== that) {
                                    var b = this.getBoundingClientRect(), dy, tt, to;
                                    if ((Math.abs(a.left - b.left) * 2 < (a.width + b.width)) && (Math.abs(a.top - b.top) * 2 < (a.height + b.height))) {
                                        // overlap, move labels
                                        // dx = (Math.max(0, a.left - b.right) + Math.min(0, a.right - b.left)) * 0.01;
                                        dy = (Math.max(0, a.bottom - b.top) + Math.min(0, a.top - b.bottom)) * 0.01;
                                        tt = d3.transform(d3.select(this).attr("transform"));
                                        to = d3.transform(d3.select(that).attr("transform"));
                                        again = true;

                                        to.translate = [radius * 0.55 * (midAngle(d1) < Math.PI ? 1 : -1), to.translate[1] + dy];
                                        tt.translate = [radius * 0.55 * (midAngle(d2) < Math.PI ? 1 : -1), tt.translate[1] - dy];

                                        d3.select(this).attr("transform", "translate(" + tt.translate + ")");
                                        d3.select(that).attr("transform", "translate(" + to.translate + ")");
                                    }
                                }
                            });
                        });

                        if (again) {
                            labelElements = text[0];
                            if (polyline) {
                                polyline.attr('points', function (d, i) {
                                    var labelForLine = d3.select(labelElements[i]).select('text'),
                                        t = d3.transform(labelForLine.attr("transform")),
                                        tx = t.translate[0],
                                        ty = t.translate[1];
                                    return [lineArc.centroid(d), lineOuterArc.centroid(d), tx, ty];
                                });
                            }
                            $timeout(relax);
                        }
                    }
                }

                function change(data) {

                    /* ------- PIE SLICES -------*/
                    slice = svg.select(".xe-pie-slices").selectAll("path.xe-pie-slice")
                        .data(pie(data), key);

                    slice.enter()
                        .insert("path")
                        .style("fill", function (d, i) {
                            if (i === 6) {
                                subPieStartAngle = d.startAngle;
                                subPieEndAngle = d.endAngle;
                            }
                            return color(i);
                        })
                        .attr("class", "xe-pie-slice")
                        .on('click', toggle)
                        .on('touchstart', toggle)
                        .on('mouseover', mouseover)
                        .on('touchstart', mouseover)
                        .on('touchstart', touchTarget)
                        .on('mouseout', mouseout)
                        .on('touchend', mouseout)
                        .on('touchend', touchTarget)
                        .on('mousemove', mousemove)
                        .on('touchmove', mousemove);

                    slice.transition().duration(1000)
                        .attrTween("d", function (d) {
                            this.d3current = this.d3current || d;
                            var interpolate = d3.interpolate(this.d3current, d);
                            this.d3current = interpolate(0);
                            return function (t) {
                                return arc(interpolate(t));
                            };
                        });

                    slice.exit()
                        .remove();

                    /* ------- TEXT LABELS -------*/

                    text = svg.select(".xe-pie-labels").selectAll("text")
                        .data(pie(data), key);

                    text.enter()
                        .append('a')
                        .attr({
                            'xlink:href': function (d, i) {
                                if (d.data.label === options.pie.otherLabel) {
                                    return '#other-group-' + uniqueId;
                                }
                                return;
                            },
                            'tabindex': function (d, i) {
                                if (d.data.label === options.pie.otherLabel) {
                                    return 0;
                                }
                                return;
                            },
                            'aria-controls': function (d, i) {
                                if (d.data.label === options.pie.otherLabel) {
                                    return 'other-group-' + uniqueId;
                                }
                                return;
                            },
                            'aria-label': function (d, i) {
                                if (d.data.label === options.pie.otherLabel) {
                                    return options.pie.group.otherAriaLabel;
                                }
                                return;
                            }
                        })
                        .on('click', toggle)
                        .on('touchstart', toggle)
                        .append("text")
                        .attr({
                            'class': function (d, i) {
                                if (d.data.label === options.pie.otherLabel) {
                                    return 'xe-pie-large-text xe-pie-other-text';
                                }
                                return 'xe-pie-large-text';
                            }
                        })
                        .on('click', toggle)
                        .on('touchstart', toggle)
                        .on('mouseover', mouseover)
                        .on('touchstart', mouseover)
                        .on('mouseout', mouseout)
                        .on('touchend', mouseout)
                        .on('mousemove', mousemove)
                        .on('touchmove', mousemove);

                    text.selectAll("text").append('tspan')
                        .attr('x', '0')
                        .attr('dy', function (d) {
                            if ((d.startAngle + d.endAngle) / 2 > Math.PI / 2 && (d.startAngle + d.endAngle) / 2 < Math.PI * 1.5) {
                                return '1em';
                            }
                            return '-1em';
                        })
                        .attr('class', 'xe-pie-bold-text')
                        .text(function (d) {
                            return d.data.percentage;
                        });

                    text.selectAll("text").append('tspan')
                        .attr('x', '0')
                        .attr('dy', '1em')
                        .attr('class', 'xe-pie-light-text')
                        .text(function (d) {
                            return d.data.label;
                        });

                    text.selectAll('text').transition().duration(1000)
                        .attrTween("transform", function (d) {
                            this.d3current = this.d3current || d;
                            var interpolate = d3.interpolate(this.d3current, d);
                            this.d3current = interpolate(0);
                            return function (t) {
                                var d2 = interpolate(t),
                                    pos = lineOuterArc.centroid(d2);
                                // pos[0] = radius * 0.6 * (midAngle(d2) < Math.PI ? 1 : -1);
                                return "translate(" + pos + ")";
                            };
                        })
                        .attrTween("text-anchor", function (d) {
                            this.d3current = this.d3current || d;
                            var interpolate = d3.interpolate(this.d3current, d);
                            this.d3current = interpolate(0);
                            return function (t) {
                                var d2 = interpolate(t);
                                if (!Language.isRtl()) {
                                    return midAngle(d2) < Math.PI ? "start" : "end";
                                }
                                if (detectIE() || detectEdge()) {
                                    return midAngle(d2) < Math.PI ? "start" : "end";
                                }
                                return midAngle(d2) < Math.PI ? "end" : "start";
                            };
                        });

                    text.exit()
                        .remove();

                    text.selectAll('.xe-pie-light-text').call(wrap);

                    /* ------- SLICE TO TEXT POLYLINES -------*/

                    polyline = svg.select(".xe-pie-lines").selectAll("polyline")
                        .data(pie(data), key);

                    polyline.enter()
                        .append("polyline")
                        .style('stroke', function (d, i) {
                            return color(i);
                        });

                    polyline.transition().duration(1000)
                        .attrTween("points", function (d) {
                            this.d3current = this.d3current || d;
                            var interpolate = d3.interpolate(this.d3current, d);
                            this.d3current = interpolate(0);
                            return function (t) {
                                var d2 = interpolate(t),
                                    pos = lineOuterArc.centroid(d2);
                                // pos[0] = radius * 0.5 * (midAngle(d2) < Math.PI ? 1 : -1);
                                return [lineArc.centroid(d2), lineOuterArc.centroid(d2), pos];
                            };
                        });

                    polyline.exit()
                        .remove();

                    relax();
                }

                function changeSub(other) {

                    /* ------- SUB PIE SLICES -------*/
                    bgSubSlice = svg.select(".xe-pie-bg-sub-slice")
                        .append('a')
                        .attr({
                            'xlink:href': function (d, i) {
                                return '#main-group-' + uniqueId;
                            },
                            'tabindex': function (d, i) {
                                return 0;
                            },
                            'aria-controls': function (d, i) {
                                return 'main-group-' + uniqueId;
                            },
                            'aria-label': options.pie.group.mainAriaLabel
                        })
                        .on('click', toggle)
                        .on('touchstart', toggle)
                        .append('path')
                        .attr('aria-hidden', true)
                        .style('display', 'none')
                        .datum({
                            startAngle: 0,
                            endAngle: 2 * Math.PI
                        })
                        .style('fill', '#e3e3e3')
                        .attr('d', subArc)
                        .on('click', toggle)
                        .on('touchstart', toggle);

                    subslice = svg.select(".xe-pie-sub-slices").selectAll("path.xe-pie-sub-slice")
                        .data(subPie(other), key);

                    subslice.enter()
                        .insert("path")
                        .attr('aria-hidden', true)
                        .style('display', 'none')
                        .style("fill", function (d, i) {
                            return subColor(i);
                        })
                        .attr("class", "xe-pie-sub-slice")
                        .on('mouseover', mouseover)
                        .on('touchstart', mouseover)
                        .on('mouseout', mouseout)
                        .on('touchend', mouseout)
                        .on('mousemove', mousemove)
                        .on('touchmove', mousemove);

                    subslice.transition().duration(1000)
                        .attrTween("d", function (d) {
                            this.d3current = this.d3current || d;
                            var interpolate = d3.interpolate(this.d3current, d);
                            this.d3current = interpolate(0);
                            return function (t) {
                                return subArc(interpolate(t));
                            };
                        });

                    subslice.exit()
                        .remove();

                    /* ------- SUB TEXT LABELS -------*/

                    subtext = svg.select(".xe-pie-sub-labels").selectAll("text")
                        .data(subPie(other), key);

                    subtext.enter()
                        .append("text")
                        .attr('class', function (d, i) {
                            if (other.length > 7) {
                                return 'xe-pie-small-text';
                            }
                            return 'xe-pie-large-text';
                        })
                        .attr('aria-hidden', true)
                        .style('display', 'none')
                        .on('mouseover', mouseover)
                        .on('touchstart', mouseover)
                        .on('mouseout', mouseout)
                        .on('touchend', mouseout)
                        .on('mousemove', mousemove)
                        .on('touchmove', mousemove);

                    subtext.append('tspan')
                        .attr('x', '0')
                        .attr('dy', '.35em')
                        .attr('class', 'xe-pie-bold-text')
                        .text(function (d) {
                            return d.data.percentage;
                        });

                    subtext.append('tspan')
                        .attr('x', '0')
                        .attr('dy', '1em')
                        .attr('class', 'xe-pie-light-text')
                        .text(function (d) {
                            return d.data.label;
                        });

                    subtext.transition().duration(1000)
                        .attrTween("transform", function (d) {
                            this.d3current = this.d3current || d;
                            var interpolate = d3.interpolate(this.d3current, d);
                            this.d3current = interpolate(0);
                            return function (t) {
                                var d2 = interpolate(t),
                                    pos = lineSubOuterArc.centroid(d2);
                                pos[0] = radius * 0.65 * (midAngle(d2) < Math.PI ? 1 : -1);
                                return "translate(" + pos + ")";
                            };
                        })
                        .attrTween("text-anchor", function (d) {
                            this.d3current = this.d3current || d;
                            var interpolate = d3.interpolate(this.d3current, d);
                            this.d3current = interpolate(0);
                            return function (t) {
                                var d2 = interpolate(t);
                                if (!Language.isRtl()) {
                                    return midAngle(d2) < Math.PI ? "start" : "end";
                                }
                                return midAngle(d2) < Math.PI ? "end" : "start";
                            };
                        });

                    subtext.exit()
                        .remove();

                    /* ------- SUB SLICE TO TEXT POLYLINES -------*/

                    subpolyline = svg.select(".xe-pie-sub-lines").selectAll("polyline")
                        .data(subPie(other), key);

                    subpolyline.enter()
                        .append("polyline")
                        .attr('aria-hidden', true)
                        .style('display', 'none')
                        .style('stroke', function (d, i) {
                            return subColor(i);
                        });

                    subpolyline.transition().duration(1000)
                        .attrTween("points", function (d) {
                            this.d3current = this.d3current || d;
                            var interpolate = d3.interpolate(this.d3current, d);
                            this.d3current = interpolate(0);
                            return function (t) {
                                var d2 = interpolate(t),
                                    pos = lineSubOuterArc.centroid(d2);
                                pos[0] = radius * 0.6 * (midAngle(d2) < Math.PI ? 1 : -1);
                                return [lineSubArc.centroid(d2), lineSubOuterArc.centroid(d2), pos];
                            };
                        });

                    subpolyline.exit()
                        .remove();

                }


                total = 0;

                // Polyfill for Number.isFinite

                Number.isFinite = Number.isFinite || function (value) {
                    return typeof value === "number" && isFinite(value);
                };

                subdata = angular.copy(scope['data']);

                angular.forEach(subdata, function (d, key) {
                    d.value = Number.isFinite(d.value) ? Math.abs(d.value) : 0;
                });

                subdata.sort(function (a, b) {
                    return d3.descending(a.value, b.value);
                });

                subdatatotal = d3.sum(subdata, function (d) {
                    return d.value;
                });

                // render the table
                if (table) {
                    tabulate(subdata, [options.table.label, options.table.value, options.table.percentage]);
                }

                angular.forEach(subdata, function (value, key) {
                    if (key < 6 || subdata.length < 8) {
                        value.origin = "main";
                    } else {
                        value.origin = "sub";
                        total += +value.value;
                    }
                });

                if (subdata.length > 7) {
                    subdata.splice(7, 0, { label: options.pie.otherLabel, value: total, origin: 'main' });
                }

                subdata.forEach(function (d) {
                    if (!Language.isRtl()) {
                        if (subdatatotal || d.value) {
                            d.percentage = (Math.round(1000 * d.value / subdatatotal) / 10).toFixed(2) + '%';
                        } else {
                            d.percentage = '0.00%';
                        }
                    }
                    if (Language.isRtl()) {
                        if (subdatatotal || d.value) {
                            d.percentage = '%' + (Math.round(1000 * d.value / subdatatotal) / 10).toFixed(2);
                        } else {
                            d.percentage = '%0.00';
                        }
                    }
                });

                other = d3.nest()
                    .key(function (d) {
                        return d.origin;
                    })
                    .entries(subdata);

                pie = d3.layout.pie()
                    .sort(function (a, b) {
                        if (!Language.isRtl()) {
                            return d3.descending(a.value, b.value);
                        }
                        if (Language.isRtl()) {
                            return d3.ascending(a.value, b.value);
                        }
                        return d3.descending(a.value, b.value);
                    })
                    .value(function (d) {
                        return d.value;
                    });

                subPie = d3.layout.pie()
                    .value(function (d) {
                        return d.value;
                    })
                    .startAngle(function (d) {
                        return subPieStartAngle;
                    })
                    .endAngle(function (d) {
                        return subPieEndAngle;
                    });

                arc = d3.svg.arc()
                    .innerRadius(0)
                    .outerRadius(radius * 0.5);

                lineArc = d3.svg.arc()
                    .innerRadius(radius * 0.5)
                    .outerRadius(radius * 0.5);

                lineOuterArc = d3.svg.arc()
                    .innerRadius(radius * 0.5)
                    .outerRadius(radius * 0.6);

                subArc = d3.svg.arc()
                    .innerRadius(radius * 0.5)
                    .outerRadius(radius * 0.6);

                lineSubArc = d3.svg.arc()
                    .innerRadius(radius * 0.6)
                    .outerRadius(radius * 0.6);

                lineSubOuterArc = d3.svg.arc()
                    .innerRadius(radius * 0.6)
                    .outerRadius(radius * 0.7);

                color = d3.scale.ordinal()
                    .range([
                        '#783084',
                        '#810c33',
                        '#8073ce',
                        '#c28041',
                        '#008241',
                        '#f39fba',
                        '#6bafa6'
                    ]);

                subColor = d3.scale.ordinal()
                    .range([
                        '#eef6f5',
                        '#ddedeb',
                        '#bcdbd7',
                        '#9ac9c3',
                        '#67aea5',
                        '#56a59b',
                        '#4d948b',
                        '#45847d',
                        '#3c746d',
                        '#33635d',
                        '#2b524d',
                        '#23423e',
                        '#1a312e',
                        '#11201f',
                        '#07100f'
                    ]);

                if (subdata.length > 7) {
                    change(other[0].values);
                    changeSub(other[1].values);
                } else {
                    change(other[0].values);
                }
            }

            return {
                restrict: 'E',
                scope: {
                    data: '=xePieData',
                    config: '=xePieConfig'
                },
                link: function (scope, element, attrs) {

                    uniqueId += 1;

                    element.addClass("xe-pie-chart");

                    var w = element[0].clientWidth,
                        margin = 10,
                        width,
                        height,
                        radius,
                        defaultOptions = {
                            tooltip: false,
                            hiddenTable: false,
                            svg: {
                                title: '',
                                desc: ''
                            },
                            pie: {
                                otherLabel: 'Other'
                            },
                            table: {
                                ariaLabel: '',
                                label: 'label',
                                value: 'value',
                                percentage: 'percentage'
                            }
                        },
                        options = angular.merge({}, defaultOptions, scope.config),
                        table,
                        s,
                        svg,
                        tooltip;

                    if (options.hiddenTable) {
                        table = d3.select(element[0]).append("table")
                            .attr({
                                'aria-label': options.table.ariaLabel,
                                'class': 'xe-pie-table-hidden'
                            });
                    }

                    width = w - margin;
                    height = width + width / 2;
                    radius = Math.min(width, height) / 2;

                    s = d3.select(element[0]).append('svg')
                        .attr({
                            'xmlns': "http://www.w3.org/2000/svg",
                            'xmlns:xlink': "http://www.w3.org/1999/xlink",
                            'version': 1.1,
                            'width': width + 'px',
                            'height': height + 'px',
                            'viewBox': '0 0 ' + Math.min(width, height) + ' ' + Math.min(width, height),
                            'preserveAspectRatio': 'xMinYMid meet',
                            'role': 'group',
                            'aria-labelledby': 'title' + uniqueId + ' ' + 'desc' + uniqueId
                        });

                    s.append('title').attr('id', 'title' + uniqueId).text(options.svg.title);
                    s.append('desc').attr('id', 'desc' + uniqueId).text(options.svg.desc);

                    svg = s.append('g')
                        .attr({
                            'role': 'list',
                            'aria-label': options.pie.ariaLabel,
                            'class': 'xe-pie-svg',
                            'transform': 'translate(' + radius + ', ' + radius + ')' // For better readability used 12'o clock as starting point for pie chart
                        });

                    if (options.tooltip) {
                        tooltip = d3.select(element[0])
                            .append('div')
                            .attr('class', 'xe-pie-tooltip');

                        tooltip.append('p').attr('class', 'xe-pie-tooltip-label');
                        tooltip.append('p').attr('class', 'xe-pie-tooltip-value');
                        tooltip.append('p').attr('class', 'xe-pie-tooltip-percent xe-pie-bold-text');
                    }

                    // on window resize, re-render d3
                    window.onresize = function () {
                        return scope.$apply();
                    };
                    scope.$watch(function () {
                        return angular.element(window)[0].innerWidth;
                    }, function () {
                        w = element[0].clientWidth;
                        width = w - margin;
                        height = width + width / 2;
                        radius = Math.min(width, height) / 2;
                        s.attr({
                            'width': width + 'px',
                            'height': height + 'px',
                            'viewBox': '0 0 ' + Math.min(width, height) + ' ' + Math.min(width, height)
                        });
                        svg.attr({
                            'transform': 'translate(' + radius + ', ' + radius + ')'
                        });
                        draw(tooltip, table, svg, radius, scope, options, element);
                    });

                    // Watch the data attribute of the scope
                    scope.$watch('data', function (newVal, oldVal, scope) {

                        // Update the chart
                        if (newVal) {
                            draw(tooltip, table, svg, radius, scope, options, element);
                        }
                    }, true);
                }
            };
        }]);
}());
(function () {
    'use strict';
    angular
        .module('tabnav', [])
        .directive('ngRepeatComplete', ['$timeout', function ($timeout) {
            return {
                restrict: 'A',
                link: function (scope, ele, attr) {
                    scope.keyboardNav = function () {
                        var tabItems = angular.element(ele[0].parentElement),
                            currentItemPos = parseInt(tabItems.find('li.active a').attr('id').split("tab")[1], 10),
                            firstItem = tabItems[0].firstElementChild,
                            lastItem = tabItems[0].lastElementChild,
                            currentItem = tabItems[0].querySelector('li.active'),
                            item,
                            switchTab = (function () {
                                return function (dir) {
                                    if ((currentItem === lastItem) && dir) {
                                        currentItemPos = scope.tabnav.tabs.length;
                                    } else if ((currentItem === firstItem) && dir) {
                                        currentItemPos = 1;
                                    }
                                    if ((currentItemPos === scope.tabnav.tabs.length) && (dir === 'right')) {
                                        currentItem = firstItem;
                                        item = angular.element(currentItem.querySelector('a'));
                                        currentItemPos = 1;
                                        item.focus();
                                    } else if ((currentItemPos === 1) && (dir === 'left')) {
                                        currentItem = lastItem;
                                        item = angular.element(currentItem.querySelector('a'));
                                        currentItemPos = scope.tabnav.tabs.length;
                                        item.focus();
                                    } else if (dir === 'right') {
                                        currentItem = currentItem.nextElementSibling;
                                        item = angular.element(currentItem.querySelector('a'));
                                        currentItemPos = parseInt(item.attr('id').split("tab")[1], 10);
                                        item.focus();
                                    } else if (dir === 'left') {
                                        currentItem = currentItem.previousElementSibling;
                                        item = angular.element(currentItem.querySelector('a'));
                                        currentItemPos = parseInt(item.attr('id').split("tab")[1], 10);
                                        item.focus();
                                    } else {
                                        scope.activeElement = scope.activeElement || document.activeElement.parentElement;
                                        currentItem = scope.activeElement;
                                        currentItemPos = parseInt(angular.element(currentItem).find('a').attr('id').split("tab")[1], 10);
                                        scope.activeElement = null;
                                    }
                                    scope.$apply(function () {
                                        currentItem.focus();
                                        scope.tabnav.activate(scope.tabnav.tabs[currentItemPos - 1]);
                                    });
                                };
                            }());
                        scope.keydownEventHandler = function (event) {
                            var keyCode = event.keyCode || event.which || event.originalEvent.keyCode;
                            if (keyCode === 39 || keyCode === 40) { // down arrow or right arrow
                                switchTab('right');
                                return false;
                            }
                            if (keyCode === 37 || keyCode === 38) { // top arrow or left arrow
                                switchTab('left');
                                return false;
                            }
                            return true;
                        };
                        scope.keyupEventHandler = function (event) {
                            var keyCode = event.keyCode || event.which || event.originalEvent.keyCode,
                                enterPressed = false;
                            if (keyCode === 13 && !enterPressed) {
                                enterPressed = true;
                                switchTab();
                                return false;
                            }
                        };
                        tabItems.on('keydown', scope.keydownEventHandler);
                        tabItems.on('keyup', scope.keyupEventHandler);
                    };
                    if (scope.$last === true) {
                        $timeout(scope.keyboardNav);
                    }
                }
            };
        }])
        .directive('xeTabNav', function () {
            return {
                restrict: 'EA',
                scope: {},
                transclude: true,
                templateUrl: 'templates/tabNav.html',
                controllerAs: 'tabnav',
                controller: ['$compile', '$sce', '$q', '$scope', '$state', function ($compile, $sce, $q, $scope, $state) {
                    var self = this;
                    self.currentActive = null;
                    self.tabs = [];
                    self.addTab = function addTab(tabScope) {
                        self.tabs.push(tabScope);
                    };
                    self.loadDynamicContent = function (userMethod, selectedTab) {
                        if (Object.prototype.toString.call(userMethod) === '[object Function]' && !selectedTab.hasTranscludedContent && !selectedTab.dynamicContent) {
                            userMethod().then(function (data) {
                                selectedTab.dynamicContent = data;
                                selectedTab.dynamic(data);
                            });
                        }
                    };
                    self.activate = function (selectedTab) {
                        self.currentActive.active = false;
                        selectedTab.active = true;
                        self.currentActive = selectedTab;
                        self.loadDynamicContent(selectedTab.loadDataOnClick, selectedTab);
                        if (selectedTab.state) {
                            $state.go(selectedTab.state);
                        }
                    };
                }]
            };
        })
        .directive('xeTabPanel', ['$compile', function ($compile) {
            return {
                restrict: 'EA',
                transclude: true,
                scope: {
                    heading: '@',
                    state: '@',
                    loadDataOnClick: '&',
                    jsLazyLoad: '@'
                },
                templateUrl: 'templates/tabPanel.html',
                require: '^xeTabNav',
                link: function (scope, ele, attr, xeTabNavCtrl, $transclude) {
                    xeTabNavCtrl.addTab(scope);
                    scope.element = ele;
                    scope.tabIndex = xeTabNavCtrl.tabs.indexOf(scope) + 1;
                    scope.lazyLoadJs = function (activeTab) {
                        var script = document.createElement('script');
                        script.src = activeTab.jsLazyLoad;
                        document.head.appendChild(script);
                    };
                    $transclude(scope, function (clone, scope) {
                        var elementTo;
                        if (clone.text().trim().length) {
                            scope.hasTranscludedContent = true;
                            elementTo = angular.element(ele[0].querySelector('[content]'));
                            elementTo.append(clone);
                        }
                    });
                    if (attr.hasOwnProperty('active')) {
                        scope.active = true;
                        xeTabNavCtrl.currentActive = scope;
                    } else if (ele.is(':last-child') && !xeTabNavCtrl.currentActive) {
                        xeTabNavCtrl.tabs[0].active = true;
                        xeTabNavCtrl.currentActive = xeTabNavCtrl.tabs[0];
                        xeTabNavCtrl.currentActive.element.attr('active', '');
                    }
                    if (xeTabNavCtrl.currentActive) {
                        xeTabNavCtrl.loadDynamicContent(xeTabNavCtrl.currentActive.loadDataOnClick, xeTabNavCtrl.currentActive);
                    }
                    scope.dynamic = function (data) {
                        var htmlTemplate, content, elementTo;
                        htmlTemplate = angular.element(data);
                        content = $compile(htmlTemplate)(scope);
                        elementTo = angular.element(ele[0].querySelector('[content]'));
                        elementTo.append(content);
                        if (scope.jsLazyLoad && scope.active) {
                            scope.lazyLoadJs(scope);
                        }
                    };
                }
            };
        }]);
}());
//# sourceMappingURL=xe-ui-components.js.map
