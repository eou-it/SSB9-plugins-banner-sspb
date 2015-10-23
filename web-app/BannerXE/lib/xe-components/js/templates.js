angular.module('xe-ui-components-tpls', ['templates/label.html', 'templates/badge.html', 'templates/button.html', 'templates/checkbox.html', 'templates/dropdown.html', 'templates/schedule.html', 'templates/column-filter.html', 'templates/dataTable.html', 'templates/pagination.html', 'templates/search.html', 'templates/pillbox.html', 'templates/radio-button.html', 'templates/simple-textbox.html', 'templates/statusLabel.html', 'templates/switch.html', 'templates/text-area.html', 'templates/text-box.html']);

angular.module("templates/label.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/label.html",
    "<label class=\"xe-label\" for=\"{{::xeFor}}\" ng-hide=\"xeHidden\">{{::xeValue}} <span class=\"xe-required\" ng-if=\"xeRequired\"> * </span></label>");
}]);

angular.module("templates/badge.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/badge.html",
    "<span tabindex=\"0\" class=\"xe-badge {{::xeType}}-badge\" aria-label=\"{{::xeLabel}}\">{{xeLabel}}</span>");
}]);

angular.module("templates/button.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/button.html",
    "<button class=\"{{xeType +' '+ btnClass}}\" ng-disabled=\"disabled\" ng-click= \"btnClick()\">{{label}}</button>");
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

angular.module("templates/schedule.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/schedule.html",
    "<div class=\"schedule-container\">\n" +
    "    <ul>\n" +
    "    <li class=\"schedule-{{weekDay !='false'}}\" ng-repeat=\"weekDay in scheduleData track by $index\"> \n" +
    "        {{week[$index]}}\n" +
    "    </li>\n" +
    "    </ul>\n" +
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
    "			<xe-checkbox xe-label=\"Select All\" ng-model=\"selectAll\" xe-on-click=\"onSelectAll({data:selectAll})\" id=\"'0'\">      </xe-checkbox>\n" +
    "        </li>\n" +
    "        <li ng-repeat=\"heading in header\" ng-class=\"{'disable-container':heading.options.disable}\">\n" +
    "            <xe-checkbox xe-label=\"{{heading.title}}\" ng-model=\"heading.options.visible\" xe-on-click=\"hideUnhideColumn(heading)\" xe-id=\"{{$index+1}}\" xe-disabled=\"heading.options.disable\"></xe-checkbox>\n" +
    "        </li>\n" +
    "    </ul>\n" +
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
    "                        <xe-column-filter ng-class=\"{'hide-container':hideContainer}\" header=\"header\" label=\"{{::'dataTable.columnFilter.label' | xei18n:labels}}\" on-select-all=\"displayAll(data)\" ></xe-column-filter>\n" +
    "                        <xe-search  on-focus=\"onSearchFocus()\" on-blur=\"onSearchFocus()\" value=\"searchFilter\"  place-holder=\"{{'search.label' | xei18n:labels}}\" \n" +
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

angular.module("templates/pagination.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/pagination.html",
    "<div xe-translate class=\"pagination-container visible-md visible-lg\" role=\"navigation\" \n" +
    "aria-label=\"{{::'pagination.aria.result.found.label' | xei18n:labels}} {{resultsFound}}\" tabindex=\"0\" > <!-- TODO: Need to use aria-described by here. its not working now. -->\n" +
    "    <div class=\"results-container\" id=\"resultsFound\">{{::'pagination.result.found.label' | xei18n:labels}} {{resultsFound}}</div>\n" +
    "    <div class=\"pagination-controls\">\n" +
    "        <xe-button xe-type=\"secondary\" btn-class=\"first\" label=\"{{::'pagination.first.label' | xei18n:labels}}\" btn-click=\"first()\" disabled=\"firstPrev\"></xe-button>\n" +
    "        <xe-button xe-type=\"secondary\" btn-class=\"previous\" label=\"{{::'pagination.previous.label' | xei18n:labels}}\" btn-click=\"prev()\" disabled=\"firstPrev\"></xe-button>\n" +
    "\n" +
    "        <div class=\"page-of\">\n" +
    "            <label>{{::'pagination.page.label' | xei18n:labels}} </label>\n" +
    "            <input numbers-only ng-model=\"onPage\" ng-change=\"paggeNumberChange()\" ng-keypress=\"keypress($event)\" aria-label=\"{{::'pagination.page.label' | xei18n:labels}} {{onPage}} {{::'pagination.of.label' | xei18n:labels}}  {{numberOfPages}}\">\n" +
    "            <label class=\"\">{{::'pagination.of.label' | xei18n:labels}} {{numberOfPages}}</label>\n" +
    "        </div>\n" +
    "\n" +
    "        <xe-button xe-type=\"secondary\" btn-class=\"next\" label=\"{{::'pagination.next.label' | xei18n:labels}}\" btn-click=\"next()\" disabled=\"nextLast\"></xe-button>\n" +
    "        <xe-button xe-type=\"secondary\" btn-class=\"last\" label=\"{{::'pagination.last.label' | xei18n:labels}}\" btn-click=\"last()\" disabled=\"nextLast\"></xe-button>\n" +
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

angular.module("templates/pillbox.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("templates/pillbox.html",
    "<div class=\"schedule-container\">\n" +
    "    <ul>\n" +
    "    <li class=\"schedule-{{::weekDay !='false'}}\" ng-repeat=\"weekDay in scheduleData track by $index\"> \n" +
    "        {{::week[$index]}}\n" +
    "    </li>\n" +
    "    </ul>\n" +
    "</div>");
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
    "<div class=\"textbox-container\">\n" +
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
