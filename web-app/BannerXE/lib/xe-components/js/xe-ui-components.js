/*
 * component-library
 * 

 * Version: 0.0.1 - 2015-11-30
 * License: ISC
 */
angular.module("xe-ui-components", ['badge','button','checkbox','dropdown','label','radiobutton','simpleTextbox','statusLabel','switch','textarea','textbox','external-resouces','utils','columnFilter','pagination','search','dataTableModule','xe-ui-components-tpls']);
angular.module('xe-ui-components-tpls', ['templates/badge.html', 'templates/button.html', 'templates/checkbox.html', 'templates/dropdown.html', 'templates/label.html', 'templates/radio-button.html', 'templates/simple-textbox.html', 'templates/statusLabel.html', 'templates/switch.html', 'templates/text-area.html', 'templates/text-box.html', 'templates/column-filter.html', 'templates/pagination.html', 'templates/search.html', 'templates/dataTable.html']);

angular.module("templates/badge.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/badge.html",
    "<span tabindex=\"0\" class=\"xe-badge {{::xeType}}-badge\" aria-label=\"{{::xeLabel}}\">{{xeLabel}}</span>");
}]);

angular.module("templates/button.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/button.html",
    "<button class=\"{{xeType +' '+ xeBtnClass}}\" ng-disabled=\"xeDisabled\" ng-click= \"xeBtnClick()\">{{xeLabel}}</button>");
}]);

angular.module("templates/checkbox.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/checkbox.html",
    "<div class=\"checkbox-container\" tabindex=\"{{!xeDisabled ? 0 : ''}}\"> \n" +
    "    <input ng-model=\"ngModel\" class=\"checkbox\" ng-class= \"{cbdisabled:xeDisabled}\" ng-click=\"xeOnClick({data:ngModel})\" type=\"checkbox\" id=\"{{xeId}}\" ng-disabled=\"xeDisabled\"/></input>\n" +
    "    <xe-label xe-value=\"{{xeLabel}}\" role=\"checkbox\" xe-for=\"{{xeId}}\" aria-checked=\"{{ngModel}}\">\n" +
    "</div>");
}]);

angular.module("templates/dropdown.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/dropdown.html",
    "<div class=\"btn-group\">\n" +
    "  	<button type=\"button\" ng-disabled= {{disabled}} ng-class= \"{disabledDD:disabled}\" data-toggle=\"dropdown\" class=\"btn btn-default dropdown dropdown-toggle\" role=\"listbox\" aria-expanded=\"false\" aria-haspopup=\"true\">\n" +
    "  		<span class=\"placeholder\" ng-show=\"!ngModel\">{{::xeLabel}}</span>\n" +
    "  		<span class=\"placeholder\">{{ dropDownLabel }}</span>\n" +
    "  		<span class=\"glyphicon glyphicon-chevron-down\"></span></button>\n" +
    "\n" +
    "  		<ul class=\"dropdown-menu\" role=\"listbox\" aria-expanded=\"false\" role=\"listbox\">\n" +
    "            <li ng-hide=\"!ngModel\" ng-click=\"updateModel(xeLabel)\">{{::xeLabel}}</li>\n" +
    "  		    <li  ng-if=\"!isObject\" role=\"option\" ng-repeat=\"option in xeOptions track by $index\" ng-click=\"updateModel(option)\" ng-class=\"{'selected':option===ngModel}\">{{::option}}</li>\n" +
    "            <li ng-if=\"isObject\" ng-repeat=\"option in xeOptions track by $index\" ng-click=\"updateModel(option)\">{{::option.label}}</li>\n" +
    "  		</ul>\n" +
    " </div>");
}]);

angular.module("templates/label.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/label.html",
    "<label class=\"xe-label\" for=\"{{::xeFor}}\" ng-hide=\"xeHidden\">{{::xeValue}} <span class=\"xe-required\" ng-if=\"xeRequired\"> * </span></label>");
}]);

angular.module("templates/radio-button.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/radio-button.html",
    "<div class=\"radio-container\" tabindex=\"{{!xeDisabled ? 0 : ''}}\">\n" +
    "    <input ng-value=\"ngValue\" ng-model=\"ngModel\" ng-disabled=\"xeDisabled\" ng-class=\"{disabledRadio:xeDisabled}\" ng-click=\"xeOnClick\" type=\"radio\" id=\"{{xeId}}\" name=\"{{xeName}}\"/>\n" +
    "    <xe-label xe-value=\"{{xeLabel}}\" xe-for=\"{{xeId}}\" aria-checked=\"{{ngModel===ngValue}}\"  role=\"radio\"  >\n" +
    "</div>");
}]);

angular.module("templates/simple-textbox.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/simple-textbox.html",
    "<input ng-disabled=disabled ng-class= \"{readOnly:inputDisabled}\" ng-model=\"value\" class=\"simple-input-field font-semibold\" id=\"inputField\" type=\"text\" name=\"{{inputField}}\" placeholder= {{placeHolder}} ng-keyup=\"onChange({data:value})\" ng-focus=\"onFocus()\" ng-blur=\"onBlur()\"></input>   \n" +
    "");
}]);

angular.module("templates/statusLabel.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/statusLabel.html",
    "<span class=\"labels {{xeType}}\" aria-label=\"{{::xeLabel}}\" tabindex=\"0\">{{::xeLabel}}</span>");
}]);

angular.module("templates/switch.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/switch.html",
    "<!-- <label class=\"label\" for=\"cmn-toggle-1\">{{switchLabel}}</label> -->\n" +
    "<input id=\"{{id}}\" ng-disabled=\"disabled\" ng-class=\"{disabledSwitch:disabled}\" ng-model=\"value\" class=\"cmn-toggle cmn-toggle-round\" type=\"checkbox\">\n" +
    "<label for=\"{{id}}\"></label>");
}]);

angular.module("templates/text-area.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/text-area.html",
    "<div class=\"textarea-container\">\n" +
    "    <xe-label xe-value=\"{{xeLabel}}\" xe-for=\"{{xeId}}\" xe-required=\"{{xeRequired}}\"></xe-label>\n" +
    "    <div class=\"xe-labeltext-margin\"></div>\n" +
    "    <textarea  \n" +
    "          ng-model=\"ngModel\"\n" +
    "          class=\"comments-field\" \n" +
    "          id=\"{{xeId}}\" \n" +
    "          placeholder= \"{{xePlaceholder}}\"\n" +
    "          ng-required=\"xeRequired\" \n" +
    "          aria-multiline=”true”\n" +
    "          ng-readonly = \"xeReadonly\">	\n" +
    "    </textarea>\n" +
    "</div>");
}]);

angular.module("templates/text-box.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/text-box.html",
    "<div class=\"textbox-container\" xe-translate>\n" +
    "    <xe-label xe-value=\"{{xeLabel}}\" xe-for=\"{{xeId}}\" xe-required=\"{{xeRequired}}\"></xe-label>\n" +
    "    <div class=\"xe-labeltext-margin\"></div>\n" +
    "	<input \n" +
    "           ng-class= \"{readonly:xeReadonly}\" \n" +
    "           ng-model=\"ngModel\"\n" +
    "           ng-form= \"ngForm\"\n" +
    "           class=\"{{xeType}}-field\" \n" +
    "           id=\"{{xeId}}\" \n" +
    "           type= \"text\"\n" +
    "           name= \"{{xeName}}\" \n" +
    "           placeholder= {{xePlaceholder}} \n" +
    "           ng-pattern=\"xePattern\" \n" +
    "           ng-required=\"xeRequired\"\n" +
    "           ng-maxlength = \"xeMaxlength\"\n" +
    "           ng-minlength = \"xeMinlength\"\n" +
    "           ng-readonly = \"xeReadonly\"\n" +
    "           >\n" +
    "	</input>\n" +
    "    <br>\n" +
    "    <div class=\"error-messages\" ng-messages=\"\">\n" +
    "        <div ng-message=\"required\">This field is required</div>\n" +
    "        <div ng-message=\"maxlength\">Maximum length is 10 charector</div>\n" +
    "    </div>\n" +
    "</div>");
}]);

angular.module("templates/column-filter.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/column-filter.html",
    "<div class=\"column-filter-container\">\n" +
    "    <button type=\"button\" class=\"column-filter-button\" ng-click=\"bindClickEvent($event)\">\n" +
    "        <span class=\"placeholder\">{{label}}</span>\n" +
    "        <div class=\"dropdown-icon\">&nbsp;</div>\n" +
    "    </button>\n" +
    "    <ul class=\"column-setting-menu\" ng-hide=\"hideColumnSettingMenu\">\n" +
    "        <li>\n" +
    "			<xe-checkbox xe-label=\"Select All\" ng-model=\"selectAll\" xe-on-click=\"displayAll(selectAll)\" xe-id=\"'0'\">      </xe-checkbox>\n" +
    "        </li>\n" +
    "        <li ng-repeat=\"heading in header\" ng-class=\"{'disable-container':heading.options.disable}\">\n" +
    "            <xe-checkbox xe-label=\"{{heading.title}}\" ng-model=\"heading.options.visible\" xe-on-click=\"hideUnhideColumn(heading)\" xe-id=\"{{$index+1}}\" xe-disabled=\"heading.options.disable\"></xe-checkbox>\n" +
    "        </li>\n" +
    "    </ul>\n" +
    "</div>");
}]);

angular.module("templates/pagination.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/pagination.html",
    "<div xe-translate class=\"pagination-container visible-md visible-lg\" role=\"navigation\" \n" +
    "aria-label=\"{{::'pagination.aria.result.found.label' | xei18n:labels}} {{resultsFound}}\" tabindex=\"0\" > <!-- TODO: Need to use aria-described by here. its not working now. -->\n" +
    "    <div class=\"results-container\" id=\"resultsFound\">{{::'pagination.result.found.label' | xei18n:labels}} {{resultsFound}}</div>\n" +
    "    <div class=\"pagination-controls\">\n" +
    "        <xe-button xe-type=\"secondary\" xe-btn-class=\"first\" xe-label=\"{{::'pagination.first.label' | xei18n:labels}}\" xe-btn-click=\"first()\" xe-disabled=\"firstPrev\"></xe-button>\n" +
    "        <xe-button xe-type=\"secondary\" xe-btn-class=\"previous\" xe-label=\"{{::'pagination.previous.label' | xei18n:labels}}\" xe-btn-click=\"prev()\" xe-disabled=\"firstPrev\"></xe-button>\n" +
    "\n" +
    "        <div class=\"page-of\">\n" +
    "            <label>{{::'pagination.page.label' | xei18n:labels}} </label>\n" +
    "            <input numbers-only ng-model=\"onPage\" ng-change=\"paggeNumberChange()\" ng-keypress=\"keypress($event)\" aria-label=\"{{::'pagination.page.label' | xei18n:labels}} {{onPage}} {{::'pagination.of.label' | xei18n:labels}}  {{numberOfPages}}\">\n" +
    "            <label class=\"\">{{::'pagination.of.label' | xei18n:labels}} {{numberOfPages}}</label>\n" +
    "        </div>\n" +
    "\n" +
    "        <xe-button xe-type=\"secondary\" xe-btn-class=\"next\" xe-label=\"{{::'pagination.next.label' | xei18n:labels}}\" xe-btn-click=\"next()\" xe-disabled=\"nextLast\"></xe-button>\n" +
    "        <xe-button xe-type=\"secondary\" xe-btn-class=\"last\" xe-label=\"{{::'pagination.last.label' | xei18n:labels}}\" xe-btn-click=\"last()\" xe-disabled=\"nextLast\"></xe-button>\n" +
    "\n" +
    "        <div class=\"per-page\">\n" +
    "            <label id=\"perPage\">{{::'pagination.perPage.label' | xei18n:labels}} : </label>\n" +
    "            <!-- Using a wraper to use CSS triangle, select alone cannot take :before etc pseudo classes -->\n" +
    "             <div class=\"per-page-select\" ng-class=\"{'dis': pageOffsetOptions}\">\n" +
    "                <select ng-model=\"offset\" ng-options=\"pageOffset for pageOffset in ::pageOffsets\" \n" +
    "                    ng-change=\"offsetChanged(true)\" ng-disabled=\"pageOffsetOptions\" aria-label=\"{{::'pagination.perPage.label' | xei18n:labels}} \">\n" +
    "                </select>\n" +
    "            </div>  \n" +
    "<!--            <xe-dropdown value=\"pageOffsets\" default=\"default\" ng-model=\"offset\" ng-change=\"offsetChanged(true)\"></xe-dropdown>-->\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>\n" +
    "");
}]);

angular.module("templates/search.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/search.html",
    "<div class=\"search-container\">\n" +
    "    <xe-simple-text-box value=value on-change=\"onDataChange()\" place-holder=placeHolder disabled=\"false\" on-focus=\"onFocus()\" on-blur=\"onBlur()\"></xe-simple-text-box>\n" +
    "</div>");
}]);

angular.module("templates/dataTable.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/dataTable.html",
    "<div class=\"table-container\" ng-class=\"{'fixedHeader': fixedHeader, 'noToolbar': noCaptionAndToolbar}\" xe-translate browser-detect>\n" +
    "    <!-- This div block is used to display table information  : Start  table-hover-->\n" +
    "    <section class=\"table-main\">\n" +
    "        <!-- Since height is configurable we need to use in-line style -->\n" +
    "        <div class=\"table-sub\" ng-style=\"{'height': height}\" ng-class=\"{'noHeight': !height}\" \n" +
    "            continuous-scroll=\"nextPage()\">            \n" +
    "            <table id=\"dataTable\" class=\"data-table\" tabindex=\"0\" ng-class=\"{'mobileLayout': mobileLayout, 'noMobileLayout': !mobileLayout}\">\n" +
    "                <caption ng-if=\"!noCaptionAndToolbar\"  >\n" +
    "                    <span class=\"caption-container font-semibold\" ng-class=\"{'hide-container':hideContainer}\">{{::caption}}</span> \n" +
    "                    <div ng-if=\"toolbar\" class=\"toolbar\">\n" +
    "                        <xe-column-filter ng-class=\"{'hide-container':hideContainer}\" header=\"header\" label=\"{{::'dataTable.columnFilter.label' | translate}}\" on-select-all=\"displayAll(data)\" ></xe-column-filter>\n" +
    "                        <xe-search  on-focus=\"onSearchFocus()\" on-blur=\"onSearchFocus()\" value=\"searchFilter\"  place-holder=\"{{'search.label' | translate}}\" \n" +
    "                        on-change=\"fetchSpecial({query: searchFilter})\"></xe-search></span>\n" +
    "                    </div>\n" +
    "                </caption>\n" +
    "\n" +
    "                <thead>\n" +
    "                    <tr>\n" +
    "                        <th ng-repeat=\"heading in header\" ng-if=\"header[$index].options.visible ===true\" \n" +
    "                            ng-class=\"{'pointer':heading.options.sortable,'sort-ascending':sortArray[heading.name].ascending,'sort-decending':sortArray[heading.name].decending}\"\n" +
    "                            class=\"font-semibold\" \n" +
    "                            ng-click=\"onSort({heading: heading, reverse:sortDirection=!sortDirection}); sortOnHeading(heading, $index);\"\n" +
    "                            codes=\"RETURN,SPACE\" \n" +
    "                            xe-keypress=\"onSort({heading: heading, reverse:sortDirection=!sortDirection}); sortOnHeading(heading, $index); focusOnOtherSortIcon($event)\"> \n" +
    "                            {{::heading.title}}\n" +
    "                            <div class=\"inline-block duplicate-header\">\n" +
    "                                <label ng-if=\"fixedHeader\" aria-hidden=\"true\" ng-class=\"{'pointer':heading.options.sortable}\">{{::heading.title}}</label>                              \n" +
    "                                <!-- This div block is used to manage sort feature for each individual columns depending on the settings : Start  -->\n" +
    "                                <div ng-if=\"::heading.options.sortable\" class=\"sort-icons-container\">\n" +
    "                                    <div class=\"sort-icon decending hideSortIcon\"                                        \n" +
    "                                        tabindex=\"0\" \n" +
    "                                        aria-label=\"{{::'dataTable.sort.ascending.label' | xei18n:labels}}\" role=\"button\"></div>\n" +
    "                                    <div class=\"sort-icon ascending hideSortIcon\"\n" +
    "                                        tabindex=\"0\" \n" +
    "                                        aria-label=\"{{::'dataTable.sort.decending.label' | xei18n:labels}}\" role=\"button\"></div>\n" +
    "                                </div>\n" +
    "                                <!-- This div block is used to manage sort feature for each individual columns depending on the settings : End  -->\n" +
    "                            </div>                            \n" +
    "                        </th>\n" +
    "                    </tr>\n" +
    "                </thead>\n" +
    "\n" +
    "                <tbody>\n" +
    "                    <!-- This block is used to display table content  : Start  -->\n" +
    "                    <tr ng-repeat=\"row in content\" xe-row-injector ng-click=\"onRowClick({data:row,index:$index})\" ng-dblclick=\"onRowDoubleClick({data:row,index:$index})\"> \n" +
    "                        <td ng-repeat=\"heading in header\" ng-if=\"heading.options.visible ===true \"\n" +
    "                            data-title=\"{{::heading.title}}\" data-name=\"{{::heading.name}}\" xe-cell-injector\n" +
    "                            attain-mobile-layout=\"{{mobileLayout[heading.name]}}\"\n" +
    "                            ng-class=\"{'align-right': heading.options.actionOrStatus}\">  \n" +
    "                            {{heading.name.indexOf(\".\") > 0 ? getNestedObjectValue(row,heading.name) :  row[heading.name]}}                            \n" +
    "                        </td>\n" +
    "                    </tr>\n" +
    "                    <!-- This block is used to display table content  : End  -->\n" +
    "                </tbody>\n" +
    "            </table>\n" +
    "        </div>\n" +
    "    </section>\n" +
    "    <!-- This div block is used to display table information  : End  -->\n" +
    "    <span id=\"transclude\" ng-transclude></span>\n" +
    "    <xe-pagination \n" +
    "        model=\"content\" \n" +
    "        results-found=\"resultsFound\" \n" +
    "        ng-show=\"showPagination\"\n" +
    "        search-string=\"searchString\">\n" +
    "    </xe-pagination>\n" +
    "    <div ng-show=\"loadingData\" class=\"load-indicator\">\n" +
    "        <div class=\"spinner\">\n" +
    "          <div class=\"bounce1\"></div>\n" +
    "          <div class=\"bounce2\"></div>\n" +
    "          <div class=\"bounce3\"></div>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>");
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
    angular.module('checkbox', ['label']).directive('xeCheckbox', ['$document', function ($document) {
        return {
            scope : {
                xeLabel : '@',
                ngModel : '=',
                xeOnClick : '&',
                xeId    : '@',
                xeDisabled : '=',
                ngForm  : '='
            },
            restrict : 'E',
            replace : true,
            templateUrl : 'templates/checkbox.html',
            link : function (scope, element, attrs, controller, transcludeFn) {
                element.on('keydown', function (event) {
                    if (event.keyCode === 32 || event.keyCode === 13) {
                        event.preventDefault();
                        scope.ngModel = !scope.ngModel;
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
    'use strict'
    angular.module('label', []).directive('xeLabel', function() {
        return {
            restrict : 'E',
            scope : {
            xeValue : '@',
            xeFor : '@',
            xeHidden : '@',
            xeRequired : '@'
        },
        replace : true,
        templateUrl : 'templates/label.html'
        }   
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
angular.module('simpleTextbox', []).directive ('xeSimpleTextBox', function() {
	return {
		restrict:'E',

		scope:{
			value:'=',
			placeHolder: '=',
			required: '=',
			disabled: '=',
			label: '=',
			onChange: '&',
            onFocus : '&',
            onBlur : '&'
		},
		replace: true,
		templateUrl:'templates/simple-textbox.html',
		link: function(scope,elem,attributes) {
		}
	}

});
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
angular.module("external-resouces", ['pascalprecht.translate']);
(function () {
    'use strict';
    angular.module("xe-ui-components")
        .value("RESOURCE_PATH", "/dest")
        .value("LOCALE_API", {api: null})
        .value("LABELS", {data: {}})
        .run(['getlocale', '$translate',  function (getlocale, $translate) {
            $translate.use(getlocale.getUserLocale());
        }]);
}());
(function () {
    'use strict';
    var en = {
        "search.label" : "Search",
        "dataTable.columnFilter.label" : "Show/Hide Column",
        "dataTable.loadingData.label" : "Loading...",
        "dataTable.caption.label"  : "Table Caption--en",
        "dataTable.sort.decending.label" : "Sort Decending",
        "dataTable.sort.ascending.label": "Sort Ascending",
        "pagination.aria.result.found.label" : "Pagination Results found",
        "pagination.result.found.label" : "Results found",
        "pagination.first.label" : "First",
        "pagination.previous.label" : "Previous",
        "pagination.last.label" : "Last",
        "pagination.next.label" : "Next",
        "pagination.of.label" : "of",
        "pagination.page.label" : "Page",
        "pagination.perPage.label" : "Per Page",
        "dataTable.no.record.found" : "No records found"
    };
    
    var ar_SA = {
        "search.label" : "Search-AR",
        "dataTable.loadingData.label" : "Loading...AR",
        "dataTable.caption.label"  : "Table Caption--AR",
        "dataTable.sort.decending.label" : "Sort Decending--AR",
        "dataTable.sort.ascending.label": "Sort Ascending",
        "pagination.aria.result.found.label" : "Pagination Results found",
        "pagination.result.found.label" : "Results found",
        "pagination.first.label" : "First",
        "pagination.previous.label" : "Previous",
        "pagination.last.label" : "Last",
        "pagination.next.label" : "Next",
        "pagination.of.label" : "of",
        "pagination.page.label" : "Page-AR",
        "pagination.perPage.label" : "Per Page",
        "dataTable.no.record.found" : "No records found"
    };
    
    angular.module("xe-ui-components")
        .config(['$translateProvider', function ($translateProvider) {
            $translateProvider.translations('en', en);
            $translateProvider.translations('ar_SA', ar_SA);
            
            $translateProvider.determinePreferredLanguage();
            $translateProvider.fallbackLanguage('en');
            $translateProvider.useSanitizeValueStrategy('escape');
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
        .directive('continuousScroll', function () {
            return {
                restrict : "A",
                scope : {
                    continuousScroll : "&"
                },
                link : function (scope, element) {
                    var visibleHeight = element.height(),
                        threshold = 0;

                    element.bind("scroll", function () {
                        var scrollableHeight = element.prop('scrollHeight'),
                            hiddenContentHeight = scrollableHeight - visibleHeight;

                        if (threshold === 0) {
                            threshold += scrollableHeight + 10;
                        }

                        if (hiddenContentHeight - element.scrollTop() <= threshold) {
                            // Scroll is almost at the bottom. Loading more rows
                            scope.continuousScroll();
                        }
                    });
                }
            };
        })
        .directive("xeKeypress", function () {
        // TODO: Move this to global scope
            var Keys = {
                LEFT: 37, // Left arrow
                UP: 38, // Up arrow
                RIGHT: 39, // Right arrow
                DOWN: 40, // Down arrow
                RETURN: 13, // Return Key
                ENTER: 13, // Return Key
                ESC: 27, // Esc key
                TAB: 9, // Tab key
                SPACE: 32, // Space key
                keyCodeMatch: function (keyPress, codes) {
                    var keys = codes.split(","),
                        index;

                    for (index = 0; index < keys.length; index = index + 1) {
                        if (keyPress === this[keys[index]]) {
                            return true;
                        }
                    }
                    return false;
                }
            };

            return {
                restrict : 'A',
                link : function (scope, element, attrs) {
                    element.bind("keypress", function (event) {
                        var keyCode = event.which || event.keyCode;

                        if (Keys.keyCodeMatch(keyCode, attrs.codes)) {
                            scope.$apply(function () {
                                scope.$eval(attrs.xeKeypress, {$event: event});
                            });
                        }
                    });
                }
            };
        })

        /* Factory Methods */
        .factory("accessibility", ["$window", function ($window) {
            return $window.accessibilityMixin;
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
        .filter('xei18n', [ '$translate', '$filter', function ($translate, $filter) {
            return function (key, labels) {
                return $filter('translate')(key);
            };
        }]);

    //// TODO: Check if this can be moved to NG service.
    //var fetch = function(pageNumber, offset, url) {
    //var deferred = $q.defer()
    //,range = reassignRange(pageNumber, offset)
    //,url = url + range.start + "/" + range.end;
    //
    //$http.get(url)
    //.success(function(data) {
    //deferred.resolve(data);
    //})
    //.error(function(data) {
    //deferred.reject(data);
    //});
    //
    //return deferred.promise;
    //};
    //
    //var reassignRange = function(pageNumber, offset) {
    //var pageEnd = offset * pageNumber;
    //return {
    //end: pageEnd,
    //start: pageEnd - offset
    //}
}());
//TODO : Presently it works for table grid componets. Need to work on this component to make more generic.
(function () {
    'use strict';
    angular.module('columnFilter', [])
        .directive('xeColumnFilter', ['$document', function ($document) {
            return {
                restrict : 'E',
                scope : {
                    header : '=',
                    label : '@'
                },
                replace : true,
                templateUrl : 'templates/column-filter.html',
                controller : ['$scope', function ($scope) {
                    $scope.selectAll = true;
                    $scope.hideColumnSettingMenu = true;
                    $scope.hideUnhideColumn = function (heading) {
                        heading.options.visible = heading.options.visible ? true : false;
                    };
                    $scope.displayAll = function (data) {
                        angular.forEach($scope.header, function (val, index) {
                            if (!val.options.disable) {
                                val.options.visible = data;
                            }
                        });
                    };
                }],
                link : function (scope, elem, attrs, controllerInstance, $transclude) {
                    var hideDropdown = function (event) {
                        var isClickedElementChildOfPopup = elem
                                                        .find(event.target)
                                                        .length > 0;
                        if (isClickedElementChildOfPopup) {
                            return;
                        }
                        scope.hideColumnSettingMenu = true;
                        $document.off('click', hideDropdown);
                        scope.$apply();
                    };
                    scope.bindClickEvent  = function ($event) {
                        scope.hideColumnSettingMenu = !scope.hideColumnSettingMenu;
                        // Bind to the document click event.
                        $document.on('click', hideDropdown);
                    };
                }
            };
        }]);
}());
angular.module('pagination', [])
.directive('xePagination', ["$http", "$q", function($http, $q) {		
	// TODO: Check if this can be moved to NG service.
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
			offset: pageEnd - offset
		}
	};

	return {
		restrict: 'EA',		
		replace: true,
		require: "?^xeTableGrid",
		scope: {
			model: "=",			
			endPoint: "=?",
			pageOffsets: "=?",
			resultsFound: "=",
			searchString: "=",
			fetch: "&?"
		},
		templateUrl: "templates/pagination.html",
		controller: ['$scope', '$attrs', "$timeout", function($scope, $attrs, $timeout) {
			// TODO: move the dev errors to separate module so that it can be injected in other modules
			var devErorrMessages = "",
				pageLoaded = false;

			if (!$scope.model) {				
				devErorrMessages += "model attribute is required\n";
			}			

			if (!$scope.resultsFound) {				
				devErorrMessages += "resultsFound attribute is required\n";
			}

			if (!angular.isDefined($attrs.fetch) && !$scope.endPoint) {
				devErorrMessages += "Either fetch or endPoint attribute is required\n";	
			}

			if (devErorrMessages !== "") {
				console.error(devErorrMessages);
				//return;
			}

			$scope.firstPrev = $scope.nextLast = false;

			if (!$scope.pageOffsets) {
				$scope.pageOffsets = [10, 20, 50, 100];
			}

			$scope.offset = $scope.pageOffsets[0];
			$scope.onPage = 1;			
			
			$scope.offsetChanged = function(doFetch) {								
				calculateNumberOfPages();

				if (doFetch) {
					$scope.fetchData($scope.onPage, $scope.offset);
				}
			};		

			$scope.first = function() {
				if ($scope.firstPrev) {
					return;
				}

				$scope.onPage = 1;
				
				$scope.fetchData($scope.onPage, $scope.offset);
				disableButtons($scope.onPage, $scope.numberOfPages);
			};

			$scope.prev = function(append) {
				if ($scope.firstPrev) {
					return;
				}

				$scope.onPage = parseInt($scope.onPage);
				$scope.onPage -= 1;
				
				$scope.fetchData($scope.onPage, $scope.offset, append);
				disableButtons($scope.onPage, $scope.numberOfPages);
			};

			$scope.next = function(append) {	
				if ($scope.nextLast) {
					return;
				}	

				$scope.onPage = parseInt($scope.onPage);
				$scope.onPage += 1;
				
				$scope.fetchData($scope.onPage, $scope.offset, append);
				disableButtons($scope.onPage, $scope.numberOfPages);
			};

			$scope.last = function() {
				if ($scope.nextLast) {
					return;
				}

				$scope.onPage = $scope.numberOfPages;
				
				$scope.fetchData($scope.onPage, $scope.offset);
				disableButtons($scope.onPage, $scope.numberOfPages);
			};

			$scope.keypress = function($event) {
				if($event.keyCode === 13) {
					$scope.onPage = Math.round($scope.onPage);

					if ($scope.onPage <= 0) {
						$scope.onPage = 1;
					} else if ($scope.onPage > $scope.numberOfPages) {
						$scope.onPage = $scope.numberOfPages;
					}

					$scope.fetchData($scope.onPage, $scope.offset);
				}				
			}

			$scope.paggeNumberChange = function() {
				var empty = $scope.onPage === "";

				$scope.pageOffsetOptions = empty;
				
				if (empty) { 
					$scope.nextLast = empty;
					$scope.firstPrev = empty;
				}
			}
			
			// Wathers
			$scope.$watch("searchString", function() {
				$scope.onPage = 1;
				$scope.fetchData($scope.onPage, $scope.offset);								
			});

			$scope.$watch("resultsFound", function() {				
				$timeout(function() {
					resetPagination($scope.resultsFound);
				});				
			});

			// Private functions
			var resetPagination = function(length) {
				if (length == 0) {
					$scope.onPage = "";
					$scope.paggeNumberChange();
				} else {
					$scope.paggeNumberChange();
				}

				calculateNumberOfPages();
				disableButtons($scope.onPage, $scope.numberOfPages);
			}

			var calculateNumberOfPages = function() {
				$scope.numberOfPages = Math.ceil($scope.resultsFound / $scope.offset);
				$scope.numberOfPages = $scope.numberOfPages < 1 ? 1 : $scope.numberOfPages;				

				if ($scope.onPage > $scope.numberOfPages) {
					$scope.onPage = $scope.numberOfPages;				
				}
			}

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
				} else if(reminder == numberOfPages) { // On first page
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
							$scope.model = append ? $scope.model.concat(data.result) : data.result;
							$scope.resultsFound = 0;
							$scope.resultsFound = data.length;
							
							// TODO: disableButtons method is not required here. but buttons are not getting disabled. Revisit after.
							disableButtons($scope.onPage, $scope.numberOfPages);
							$scope.loading(false);
						},
						/* Error */
						function(data) {
							if (data) console.error(data);
							$scope.loading(false);
						}
					);					
				} else {					
					fetch(query).then(
						/* Success */
						function(data) {
							$scope.model = append ? $scope.model.concat(data.result) : data.result;
							$scope.resultsFound = data.length;

							resetPagination(data.length);
							$scope.loading(false);
						},
						/* Error */
						function(data) {
							if (data) console.error(data);
							$scope.loading(false);
						}
					);
				}				
			};
			
			$scope.offsetChanged(false);			
		}],
		link: function(scope, elem, attributes, parentController) {
			// Assigning loadingDataIndicator() from parentCOntroller to be used later in paginations controller.
			scope.loading = parentController.loadingDataIndicator;

			// If continuous scrolling is true then we can to hide paginations across devices and desktop.
			if (parentController.hidePaginationIfContinuousScroll) {
				parentController.hidePaginationIfContinuousScroll();
			}

			// Injecting next(), previous() and sort() function to parent controller so that it can invoke them later as per the need.
			// For example for continuous scrolling.
			parentController.next = function(append) {
				scope.next(append);
			}

			parentController.previous = function(append) {
				scope.prev(append);
			}

			parentController.sort = function(sortColumnName, reverse) {
				scope.sortColumnName = sortColumnName;
				scope.ascending = reverse;
				scope.onPage = 1;			
				scope.fetchData(1, scope.offset);
			}			
		}
	}
}]);
angular.module('search', []).directive ('xeSearch', function() {
	return {
		restrict:'E',
		scope:{
			value:'=',
			placeHolder: '@',
			onChange: '&',
            onFocus : '&',
            onBlur : '&'
		},
		replace: true,
		templateUrl: 'templates/search.html',
        link :function(scope, elem, attrs, controllerInstance,$transclude){	
           scope.onDataChange = function() {
               scope.onChange({data:scope.value});
           }
        }
	}
});
/**
    DataTable Module is used to render data in table format. 
    Input   : Basically It requires two arrays 1. Column headings and 2. Column Data . These should be in following format. 

            $scope.headings = [

                {position: 2, name: 'rollNo', title: 'Roll No.', options: {visible: false, isSortable: false}},
                {position: 1, name: 'studentName', title: 'Studnet Name', options: {ß: true, isSortable: true}},
                {position: 3, name: 'subject', title: 'Subject', options: {visible: true, isSortable: true}},
                {position: 4, name: 'marks', title: 'Marks', options: {visible: true, isSortable: true}}
            ];


            $scope.content = [
                {rollNo: 6, studentName: 'Vaikunt Naik', subject: 'Subject1', marks: 45},
                {rollNo: 2, studentName: 'Venuglopal Kathavate', subject: 'Subject2', marks: 50},
                {rollNo: 3, studentName: 'Ram', subject: 'Subject3', marks: 74},
                {rollNo: 4, studentName: 'Nethaji', subject: 'Subject5', marks: 85}
                {rollNo: 1, studentName: 'Mohan Venkatesh', subject: 'Subject5', marks: 65}


            ];

    Output : Using given arrays this directive will render the data in table format by enabling/disabling specified configurations for 
             each individual columns.


    Features.
    -----------
    This configurations considers values specified the options object for each column.

    1. options.isSortable : if value of options.isSortable is "true" then it will provide sortable feature for the specified column else it does not display any sortable controls.
    2. options.visible : if value of options.visible is "true" then display the column else hide it. This hidden column can enabled/disable in column settings menu.
    TODOs : 
        1. Adding custom HTML elements
        2. Drag and Drop    */

(function () {
    'use strict';
    angular.module('dataTableModule', ['utils'])
        .directive('xeTableGrid', ["$timeout", '$window', 'accessibility', function ($timeout, $window, accessibility) {
            return {
                restrict : 'E',
                transclude : true,
                replace : true,
                //Following variables are part of local scope to directive. 
                scope: {
                    header : '=',
                    content : '=',
                    onSort : '&',
                    onRowClick  : '&',
                    toolbar : "=",
                    paginate : "=?",
                    endPoint : "=?",
                    resultsFound : "=?",
                    fetch : "&",
                    pageOffsets : "=?",
                    height : "@?",
                    mobileLayout : "=?",
                    continuousScrolling : "=?",
                    caption : "@?",
                    onRowDoubleClick : "&"
                },
                controller : [ '$rootScope', '$scope', '$filter', '$attrs', '$transclude', "$http",  function ($rootScope, $scope, $filter, $attrs, $transclude, $http) {
                    var orderBy = $filter('orderBy'),
                        filter  = $filter("filter"),
                        _this = this,
                        content,
                        previousSortColumn;
                    $scope.header = orderBy($scope.header, 'position', false);
                    $scope.hideColumnSettingMenu = true;
                    $scope.transcludes = {};
                    $scope.hideContainer = false;
                    $scope.searchString = "";
                    $scope.searchFilter = "";
                    $scope.sortDirection = true;
                    $scope.sortArray = [];
                    $scope.pagination = $scope.paginate;
                    $scope.showPagination = true;
                    if (!$scope.pagination) {
                        $scope.pagination = $scope.continuousScrolling;
                    }
                    if ($scope.pagination) {
                        if (!$scope.height) {
                            $scope.height = "300px";
                        }
                        $scope.fixedHeader = true;
                    } else if ($scope.height) {
                        $scope.fixedHeader = true;
                    }
                    if (!$scope.toolbar && !$scope.caption) {
                        $scope.noCaptionAndToolbar = true;
                    }
                    // Controller methods used when 'require'd the parameter 'loading' is set to true its loading data
                    this.loadingDataIndicator = function (loading) {
                        $scope.loadingData = loading;
                    };
                    // If continuous scrolling is true then we need to hide pagination across devices and desktops.
                    // Normally pagination is hidden on tablets and mobile.
                    this.hidePaginationIfContinuousScroll = function () {
                        $scope.showPagination = !$scope.continuousScrolling;
                    };
                    // TODO: May be use Factory pattern
                    // If Pagination is false, then all the data will be loaded at once and no need to hit the server for sorting.
                    // Sorting will be done model data.
                    $scope.onSort = function (params) {
                        $scope.sortColumnName = params.heading.name;
                        $scope.ascending = !params.reverse;
                        // TODO: Too many ifs. Revisit this.
                        if (!$scope.pagination) {
                            if (params.heading.options.sortable) {
                                if (!angular.isDefined($attrs.fetch)) {
                                    // Model sort
                                    $scope.content = orderBy($scope.content, $scope.sortColumnName, $scope.ascending);
                                } else {
                                    // Server side sort
                                    _this.loadingDataIndicator(true);
                                    $scope.fetch({
                                        query: {
                                            searchString: $scope.searchString,
                                            sortColumnName: $scope.sortColumnName,
                                            ascending: $scope.ascending
                                        }
                                    }) // success
                                        .then(
                                            function (data) {
                                                $scope.content = data.result;
                                                _this.loadingDataIndicator(false);
                                            },// error
                                            function (data) {
                                                console.error(data);
                                                _this.loadingDataIndicator(false);
                                            }
                                        );
                                }
                            }
                        } else {
                            if (params.heading.options.sortable) {
                                _this.sort($scope.sortColumnName, $scope.ascending);
                            }
                        }
                    };
                    // This block loads the data for data table if its not provided by the user.
                    // Also checks whether application specific search is available or not. if not available calls directive search method.
                    // This mainly works on model data.
                    if (!$attrs.fetch) {
                        $scope.fetch = function (data) {
                            if (!content) { content = $scope.content; }
                            $scope.content = orderBy(
                                filter(content, data.query.searchString, false),
                                $scope.sortColumnName,
                                $scope.ascending
                            );
                            $scope.resultsFound = $scope.content.length;
                            _this.loadingDataIndicator(false);
                        };

                        if (!$scope.pagination && !$attrs.endPoint) {
                            console.error("Provide either end-point or fetch attribute");
                        } else if (!$scope.pagination) {
                            this.loadingDataIndicator(true);
                            $http.get($scope.endPoint + "?searchString=")
                                .success(function (data) {
                                    $scope.content = data.result;
                                    _this.loadingDataIndicator(false);
                                })
                                .error(function (data) {
                                    console.error(data);
                                    _this.loadingDataIndicator(false);
                                });
                        }
                    } else if (!$scope.pagination) {
                        this.loadingDataIndicator(true);
                        $scope.fetch({query: {searchString: ""}})
                                .then(// success
                                    function (data) {
                                    $scope.content = data.result;
                                    _this.loadingDataIndicator(false);
	                       },
	                    // error
	                    function(data) {
	                        console.error(data);
	                        _this.loadingDataIndicator(false);
	                    }
                   	);
				}
                
                // TODO: This is just a temporary arrangement to change the search string. Ideally two-way data-binding should this job for us.
				// Right now with nested directive two-way data-biding is not working. Need to revisit this.
				$scope.fetchSpecial = function(data) {					
					$scope.searchString = data.query;					

					if (!$scope.pagination) {
						var promise = $scope.fetch({
							query: {
								searchString: data.query,
								sortColumnName: $scope.sortColumnName,
								ascending: $scope.ascending
							}
						});

						_this.loadingDataIndicator(true);

						if (promise) {
							promise.then(
								// success
								function(data) {		                    	
		                        	$scope.content = data.result;
		                        	_this.loadingDataIndicator(false);
			                    },
			                    // error
			                    function(data) {
			                        console.error(data);
			                        _this.loadingDataIndicator(false);
			                    }
		                   	);
						}
					}
				}
				// END TODO: May be use Factory pattern

				//Method to store html objects added during data table declaration
				this.registerTransclude = function (directiveTransclude) {
          			var id = directiveTransclude.id;
         			 $scope.transcludes[id] = directiveTransclude;
        		}   
                
//                $scope.displayAll = function(data){
//                    angular.forEach($scope.header,function(val,index){
//                        if(!val.options.disable)
//                            val.options.visible=data
//                    });
//                }
                
                $scope.onSearchFocus = function(){
                    $scope.hideContainer= !$scope.hideContainer;
                }
                
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
	                        for(obj in $scope.sortArray) {
	                            if(obj == columnName)  {
	                                $scope.sortArray[obj] = {ascending : true , decending : false};
	                            }
	                            else{
	                                $scope.sortArray[obj] = {ascending : false , decending : false};
	                            }
	                        }
	                    }
	                }	                
                }                

                var defaultOptions = {visible: true, sortable: false};

                angular.forEach($scope.header,function(value,index){
                    if(angular.isDefined(value.options)) {
                        
                        if(angular.isDefined(value.options.ascending)) {
                            $scope.sortArray[value.name] = {ascending : value.options.ascending , decending : !value.options.ascending};
                            previousSortColumn = value.name;
                            $scope.ascending = !value.options.ascending;
                            $scope.sortColumnName = value.name;
                            $scope.sortDirection = !value.options.ascending;
                        }
                        else{
                            $scope.sortArray[value.name] = {ascending : false , decending : false};
                        }
                        if(!angular.isDefined(value.options.visible)) {
                            value.options['visible']=true;
                        }
                       if(!angular.isDefined(value.options.sortable)) {
                            value.options['sortable']=false;
                        }
                    }
                    else {
                  		value.options =  defaultOptions;
                    }
                });
                
                $scope.getNestedObjectValue = function getter(row,param) {
                    var object = row;
                    var paramArray = param.split(".");
                    for(var i=0; i< paramArray.length; i++) {
                        if(!object) {return null};
                        object = object[paramArray[i]];
                    }
                        return object;
                    }
			}],			
			/*
				Place ot provide external HTML template to populate data.
			*/
			templateUrl: function(element, attr){
				return 'templates/dataTable.html';
			},
            compile: function compile(element, attrs) {          	
     		 	if (attrs.paginate === "true" || attrs.continuousScrolling === "true") {     		 		 		
     		 		var paginationObject = element.find("xe-pagination");

     		 		if(angular.isDefined(attrs.fetch)) {
                        paginationObject.attr('fetch', 'fetch({query: query})');
                    }

                    if(angular.isDefined(attrs.pageOffsets)) {
                        paginationObject.attr('page-Offsets', 'pageOffsets');
                    }

                    if(attrs.endPoint){
                        paginationObject.attr('end-point', 'endPoint');
                    }                    
     		 	}  else {
     		 		// Removing pagination if its not set to true to avoid getting executed even when not its needed.
     		 		element.find("xe-pagination").remove();     		 		
     		 	}           
            
                return {
                    pre: function preLink(scope, element, attrs, controller, transcludeFn) {

                    },
                    post: function postLink(scope, element, attributes, controller, transcludeFn) {
                        angular.element("#transclude").remove();                            

                        var previousTarget;
                        scope.getTarget = function(event) {
                            if(previousTarget){
                                angular.element(previousTarget).toggleClass('hideSortIcon');
                            }
                            previousTarget = event.target;
                            angular.element(event.target).addClass('hideSortIcon');
                        }

                        // TODO: This is temporary fix to bring focus back to sort headers. Try to avoid timeouts.
                        scope.focusOnOtherSortIcon = function(event) {
							var target = angular.element(event.target)[0];
							
							setTimeout(function() {
								if (target.nextElementSibling) target.nextElementSibling.focus();
								if (target.previousElementSibling) target.previousElementSibling.focus();
							}, 300);
                        }
                        
                        // TODO: Use better approach than this $timeout
                        $timeout(function() {
                            accessibility.provideAccessibilityFor(element.find("#dataTable"));

                            scope.nextPage = function() { 
                            	// TODO: find a better way to check the visibility in angular way. 
                            	// Make sure we do not depend on jQuery.
                            	if (scope.pagination 
                            		&& element.find(".pagination-container").css("display") === "none"
                            		&& controller.next) {
                            		
                            		//element.find(".pagination-container .pagination-controls").scope().next(true);                            		
                            		controller.next(true);
                            	}                            		
                            }
                        });                    
                    }
             	}
           }
		};
	}])
	.directive('xeCellInjector',['$rootScope',function($rootScope) {
		return {
			require : '^xeTableGrid',
			restrict :'A',
 			replace:true,
 			scope:true,
			/*
				This block to provide DOM manipulation methods if any. 
			*/
			link :function(scope, elem, attrs, controllerInstance,$transclude){	
				var id = attrs.name;
				var transclude = scope.transcludes[id];
				if(transclude){
					var  scopeRowValue = scope.row[attrs.name];
					if(!scopeRowValue){
						var rootScopeObj = $rootScope.$new();
						rootScopeObj.row=scope.row;
						transclude.transclude(rootScopeObj,function(clone,scope){
							elem.html(clone);

						});
					}
					else{
						//scope[attrs.name]=scope.row[attrs.name];
							transclude.transclude(scope,function(clone,scope){
							elem.html(clone);
						})
					}
					
				}
			}
		}
	}]).
	directive('xeRowInjector',['$rootScope',function($rootScope) {
        var previousElement;
		return {
			restrict :'A',
 			replace:true,
 			require : '^xeTableGrid',
 			scope:true,
 			link :function(scope, elem, attrs, controllerInstance,$transclude) {	
                elem.on("click",function(event){
                    if(previousElement){
                       previousElement.removeClass("active-row");
                    }
                    elem.addClass("active-row");
                    previousElement = elem;
                });
 			}
 	  }
 }])
.directive('xeCellMarkup',['$rootScope',function($rootScope) {
		return {
			restrict :'EA',
			transclude:'element',
			replace:true,
			scope:true,
			priority: 100,
			require:"^xeTableGrid",
			link:function(scope, element, attrs, controllerInstance,$transclude) {
				var directiveTransclude = {
          				id: attrs.columnName,
          				transclude: $transclude,
          				element: element
        			};
        		if(controllerInstance.registerTransclude){
					controllerInstance.registerTransclude(directiveTransclude);
				}
			}
		}
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
	}
});
}());

// ------------------------------------------------------------------------------------------
/*
	This is the mixin created to work with components keyboard navigation and accessibility.
	Right now it is depedent on jQuery. In future may start using native HTML5.
*/
// ------------------------------------------------------------------------------------------

// TODO: keyboardConfig is suppose to be added in server side and should be brought in here as dependent file.
var keyboardConfig = {
	keys: {		
		LEFT: 37, // Left arrow
		UP: 38, // Up arrow
		RIGHT: 39, // Right arrow
		DOWN: 40, // Down arrow
		RETURN: 13, // Return Key
		ESC: 27, // Esc key
		TAB: 9 // Tab key
	}
};

window.accessibilityMixin = (function($, config) {
    return {
		keys: config.keys,
		provideAccessibilityFor: function(component) {
			component = $(component);

			switch(component.prop("tagName").toLowerCase()) {
				case "table":
					this.applyAccessibilityForTable(component);
					this.applyKeyboardNavForTable(component);
					break;
			}
		},
		applyAccessibilityForTable: function(table) {
			if(!table.attr("tabindex")) {				
				table.attr("tabindex", "0");
			}
			
			$("thead th", table).each(function(index, cell) {
				cell = $(cell);

				if (!cell.attr("tabindex")) {
					cell.attr("tabindex", "-1");
				}

				if (!cell.attr("scope")) {
					cell.attr("scope", "col");
				}
			});

			$("tbody th", table).each(function(index, cell) {
				cell = $(cell);

				if (!cell.attr("tabindex")) {
					cell.attr("tabindex", "-1");
				}

				if (!cell.attr("scope")) {
					cell.attr("scope", "row");
				}
			});

			$("tbody td", table).each(function(index, cell) {
				cell = $(cell);

				if (!cell.attr("tabindex")) {
					cell.attr("tabindex", (index === 0 ? "0" : "-1"));
				}
			});
		},
        applyKeyboardNavForTable: function(table) {
        	var _this = this,
        		returnPressed = false,
        		escPressed = true,
        		tableNavigated = false;

        	$("tbody td", table).on("focus", function(e) {
        		_this.checkAndFocus(table);
        	});        	

        	/*table.on("click", "th, td", function(e) {
				table.attr("tabindex", "0");

        		$(".active", table).removeClass("active");
				$("tr.active-row", table).removeClass("active-row");

				$(this).addClass("active");
				$(this).parents("tr").addClass("active-row");
        	});*/        	

			table.on("keydown", function(e) {
				var input = e.target.tagName.toLowerCase() === "input";

				// With two-way data-binding frameworks sometimes table would not have been rendered.
				// In such cases we need to rerun the accessibility.
				if ($("tbody td[tabindex='-1']", table).length === 0) {
					$("tbody td", table).on("focus", function(e) {
		        		_this.checkAndFocus(table);
		        	}); 

					_this.applyAccessibilityForTable(table);
				}

				if (e.shiftKey) {
					return;
				}

				// TODO: Try using jquery's 'on' and 'off' event handlers instead of this extra logic.
				if (input && (e.keyCode === _this.keys.RETURN)) {
					returnPressed = true;
        			escPressed = false;
					return;
				} else if (input && (e.keyCode === _this.keys.ESC)) {
					returnPressed = false;
        			escPressed = true;
        			return;
				}				

				if (escPressed && (e.keyCode !== _this.keys.TAB)) {	
					var active = $(".active", table),
						activeRow = $("tr.active-row", table);
						// cellType = active.is("th") ? "th" : "td",
						// bodyType = activeRow.parents("thead").length === 0 ? "tbody" : "thead";

					active.attr("tabindex", "-1");

					if (e.keyCode == _this.keys.LEFT) {	// left
						active.removeClass("active");
						active = ((_this.prev(active, "td").length === 0) 
							? active.parents("tr").find("td").last("td") 
							: _this.prev(active, "td"))
							.addClass("active");

						_this.focus(active);
					}

					if (e.keyCode == _this.keys.RIGHT) { 	// right
						active.removeClass("active");
						active = ((_this.next(active, "td").length === 0) 
							? active.parents("tr").find("td").first("td") 
							: _this.next(active, "td"))
							.addClass("active");

						_this.focus(active);
					}

					if (e.keyCode == _this.keys.UP) {	// up		
						active.removeClass("active");
						active = ((activeRow.removeClass("active-row").prev("tr").length === 0)
							? activeRow.parents("tbody").find("tr").last("tr")
							: activeRow.prev("tr"))
							.addClass("active-row")
							.find("td")
							.eq(active[0].cellIndex)
							.addClass("active");

						_this.focus(active);
					}
					
					if (e.keyCode == _this.keys.DOWN) { 	// down
						active.removeClass("active");
						active = ((activeRow.removeClass("active-row").next("tr").length === 0)
							? activeRow.parents("tbody").find("tr").first("tr") 
							: activeRow.next("tr"))
							.addClass("active-row")
							.find("td")
							.eq(active[0].cellIndex)
							.addClass("active");

						_this.focus(active);
					}																		
				} else if ((e.keyCode == _this.keys.TAB) && !tableNavigated) {
					$("thead th:first").focus();
					tableNavigated = true;
				}									
			});

			table.on("keyup", function(e) {
				// TODO: TAB navigation support for editable tables. It does also support navigation for web-components
				// if (e.keyCode == _this.keys.TAB && !e.shiftKey) {
				// 	var active = $("tbody td.active", table),
				// 		activeRow = $("tbody tr.active-row", table),
				// 		activeElement = document.activeElement;

				// 	if (activeElement.shadowRoot) {
				// 		// From outside of web-component, web-component itself becomes activeElement, 
				// 		// where as the activeElement should have been input element. This is because of shadow DOM.
				// 		// So we need to rely on shadowRoot to access shadow DOM.
				// 		activeElement = $(activeElement.shadowRoot.activeElement);
				// 	} else {
				// 		activeElement = $(document.activeElement);
				// 	}

				// 	if (activeElement 
				// 		&& activeElement.is("input") 
				// 		&& activeElement.parents("table")) {

				// 		active.removeClass("active");
				// 		activeRow.removeClass("active-row");

				// 		activeElement.parents("td").addClass("active");
				// 		activeElement.parents("tr").addClass("active-row");
				// 	}
				// }				
			});
        },        
        next: function(element, tag) {        	
        	var nextElement = element[0] ? element[0].cellIndex + 1 : "";

        	return element.parents("tr")
        		.find(tag)
        		.eq(nextElement);
        },
        prev: function(element, tag) {
        	var prevElement = element[0] ? element[0].cellIndex - 1 : "";
        	
        	return element.parents("tr")
        		.find(tag)
        		.eq(prevElement);
        },
        checkAndFocus: function(table) {
        	var active = $("tbody td.active", table),
				activeRow = $("tbody tr.active-row", table);				

			if (activeRow.length === 0) {
				activeRow = $("tbody tr:first", table).addClass("active-row");
			}

			if (active.length === 0) {
				active = $("tbody tr:first td:first", table).addClass("active");					
				this.focus(active);					
			}
        },
        focus: function(element) {
        	var input = element.find("input");

        	element.attr("tabindex", "0");
        	(input.length === 0)
				? element.focus()
				: input.select().focus();
        }
    };    
}) (jQuery, keyboardConfig);