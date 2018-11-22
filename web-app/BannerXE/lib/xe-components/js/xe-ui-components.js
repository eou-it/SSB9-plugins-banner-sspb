/*
 * component-library
 * 

 * Version: 9.1 - 2018-10-10
 * License: Copyright 2018 Ellucian Company L.P. and its affiliates.
 */
angular.module("xe-ui-components", ['badge','button','checkbox','dropdown','label','radiobutton','simpleTextbox','statusLabel','switch','textarea','textbox','ui.select','xeUISelect','external-resouces','utils','columnFilter','pagination','search','xebarmodule','dataTableModule','aboutModal','pieChartModule','popupModal','tabnav','timePicker','xe-ui-components-tpls']);
angular.module('xe-ui-components-tpls', ['templates/badge.html', 'templates/button.html', 'templates/checkbox.html', 'templates/dropdown.html', 'templates/label.html', 'templates/radio-button.html', 'templates/simple-textbox.html', 'templates/statusLabel.html', 'templates/switch.html', 'templates/text-area-counter.html', 'templates/text-area.html', 'templates/text-box-char-limit.html', 'templates/text-box-password.html', 'templates/text-box.html', 'templates/column-filter.html', 'templates/pagination.html', 'templates/search.html', 'templates/dataTable.html', 'templates/dialog.html', 'templates/dialog_default.html', 'templates/modal.html', 'templates/tabNav.html', 'templates/tabPanel.html', 'templates/timePicker.html', 'templates/timePicker_rtl.html']);

angular.module("templates/badge.html", []).run(["$templateCache", function ($templateCache) {
  $templateCache.put("templates/badge.html",
    "<span tabindex=\"0\" class=\"xe-badge {{::xeType}}-badge\" aria-label=\"{{::xeLabel}}\">{{xeLabel}}</span>");
}]);

angular.module("templates/button.html", []).run(["$templateCache", function ($templateCache) {
  $templateCache.put("templates/button.html",
    "<button class=\"{{xeType +' '+ xeBtnClass}}\" ng-disabled=\"xeDisabled\" aria-label=\"{{xeAriaLabel}}\" ng-click=\"xeBtnClick()\" ng-bind=\"xeLabel\"></button>");
}]);

angular.module("templates/checkbox.html", []).run(["$templateCache", function ($templateCache) {
  $templateCache.put("templates/checkbox.html",
    "<div class=\"xe-checkbox\" ng-class=\"{disabled: xeDisabled, checked: xeModel}\" ng-click=\"cbClicked($event)\" ng-checked=\"xeModel\" role=\"{{ariaRole ? ariaRole : 'checkbox'}}\" aria-checked=\"{{xeModel}}\" aria-disabled=\"{{xeDisabled}}\" aria-labelledby=\"{{::'ckbox-' + xeId}}\" aria-live=\"assertive\" tabindex=\"0\"><span class=\"checkbox\" role=\"presentaion\"></span><xe-label id=\"{{::'ckbox-' + xeId}}\" xe-value=\"{{::xeLabel}}\" xe-hidden=\"{{!xeLabel || xeLabelHidden}}\"></xe-label></div>");
}]);

angular.module("templates/dropdown.html", []).run(["$templateCache", function ($templateCache) {
  $templateCache.put("templates/dropdown.html",
    "<div class=\"btn-group\"><button type=\"button\" ng-disabled=\"{{disabled}}\" ng-class=\"{disabledDD:disabled}\" data-toggle=\"dropdown\" class=\"btn btn-default dropdown dropdown-toggle\" role=\"listbox\" aria-expanded=\"false\" aria-haspopup=\"true\"><span class=\"placeholder\" ng-show=\"!ngModel\">{{::xeLabel}}</span> <span class=\"placeholder\">{{ dropDownLabel }}</span> <span class=\"glyphicon glyphicon-chevron-down\"></span></button><ul class=\"dropdown-menu\" role=\"listbox\" aria-expanded=\"false\" role=\"listbox\"><li ng-hide=\"!ngModel\" ng-click=\"updateModel(xeLabel)\">{{::xeLabel}}</li><li ng-if=\"!isObject\" role=\"option\" ng-repeat=\"option in xeOptions track by $index\" ng-click=\"updateModel(option)\" ng-class=\"{'selected':option===ngModel}\">{{::option}}</li><li ng-if=\"isObject\" ng-repeat=\"option in xeOptions track by $index\" ng-click=\"updateModel(option)\">{{::option.label}}</li></ul></div>");
}]);

angular.module("templates/label.html", []).run(["$templateCache", function ($templateCache) {
  $templateCache.put("templates/label.html",
    "<label class=\"xe-label\" for=\"{{xeFor}}\" ng-hide=\"{{xeHidden}}\" ng-cloak>{{xeValue}}<span class=\"xe-required\" ng-if=\"xeRequired\" ng-bind=\"' * '\"></span></label>");
}]);

angular.module("templates/radio-button.html", []).run(["$templateCache", function ($templateCache) {
  $templateCache.put("templates/radio-button.html",
    "<div class=\"radio-container\" tabindex=\"{{!xeDisabled ? 0 : ''}}\"><input ng-value=\"ngValue\" ng-model=\"ngModel\" ng-disabled=\"xeDisabled\" ng-class=\"{disabledRadio:xeDisabled}\" ng-click=\"xeOnClick\" type=\"radio\" id=\"{{xeId}}\" name=\"{{xeName}}\"><xe-label xe-value=\"{{xeLabel}}\" xe-for=\"{{xeId}}\" aria-checked=\"{{ngModel===ngValue}}\" role=\"radio\"></xe-label></div>");
}]);

angular.module("templates/simple-textbox.html", []).run(["$templateCache", function ($templateCache) {
  $templateCache.put("templates/simple-textbox.html",
    "<input id=\"{{::inputField}}\" xe-field=\"search\" name=\"{{::inputField}}\" placeholder=\"{{placeHolder}}\" class=\"simple-input-field auto-width font-semibold {{xeClass}}\" ng-model=\"value\" ng-class=\"{readOnly: inputDisabled}\" ng-disabled=\"{{disabled}}\" ng-keyup=\"onChange({data: value, id: inputField, event: $event})\" ng-keydown=\"onKeydown({data: value, id: inputField, event: $event})\" ng-keypress=\"onKeypress({data: value, id: inputField, event: $event})\" ng-paste=\"onKeypress({data: $event.originalEvent.clipboardData.getData('text/plain'), id: inputField, event: $event})\" ng-focus=\"onFocus({event: $event})\" ng-blur=\"onBlur(({event: $event}))\" autocomplete=\"off\">");
}]);

angular.module("templates/statusLabel.html", []).run(["$templateCache", function ($templateCache) {
  $templateCache.put("templates/statusLabel.html",
    "<span class=\"labels {{xeType}}\" aria-label=\"{{::xeLabel}}\">{{::xeLabel}}</span>");
}]);

angular.module("templates/switch.html", []).run(["$templateCache", function ($templateCache) {
  $templateCache.put("templates/switch.html",
    "<input id=\"{{id}}\" ng-disabled=\"disabled\" ng-class=\"{disabledSwitch:disabled}\" ng-model=\"value\" class=\"cmn-toggle cmn-toggle-round\" type=\"checkbox\"><label for=\"{{id}}\"></label>");
}]);

angular.module("templates/text-area-counter.html", []).run(["$templateCache", function ($templateCache) {
  $templateCache.put("templates/text-area-counter.html",
    "<div class=\"textarea-container\"><xe-label xe-value=\"{{xeLabel}}\" xe-for=\"{{xeId}}\" xe-required=\"xeRequired\"></xe-label><div class=\"xe-labeltext-margin\"></div><textarea ng-model=\"ngModel\" class=\"comments-field\" ng-class=\"{readonly:xeReadonly}\" id=\"{{xeId}}\" placeholder=\"{{xePlaceholder}}\" ng-required=\"xeRequired\" aria-multiline=\"true\" ng-readonly=\"xeReadonly\" maxlength=\"{{xeCharCounter}}\" ng-trim=\"false\">\n" +
    "\n" +
    "    </textarea><div id=\"xe-id-characters-count\"><span>{{::'xe.text.chars.left' | xei18n}} : {{ charRemaining }}</span></div></div>");
}]);

angular.module("templates/text-area.html", []).run(["$templateCache", function ($templateCache) {
  $templateCache.put("templates/text-area.html",
    "<div class=\"textarea-container\"><xe-label xe-value=\"{{xeLabel}}\" xe-for=\"{{xeId}}\" xe-required=\"xeRequired\"></xe-label><div class=\"xe-labeltext-margin\"></div><textarea ng-model=\"ngModel\" class=\"comments-field\" ng-class=\"{readonly:xeReadonly}\" id=\"{{xeId}}\" placeholder=\"{{xePlaceholder}}\" ng-required=\"xeRequired\" aria-multiline=\"true\" ng-readonly=\"xeReadonly\">\n" +
    "    </textarea></div>");
}]);

angular.module("templates/text-box-char-limit.html", []).run(["$templateCache", function ($templateCache) {
  $templateCache.put("templates/text-box-char-limit.html",
    "<div class=\"textbox-container\" aria-live=\"assertive\"><xe-label xe-value=\"{{xeLabel}}\" xe-for=\"{{xeId}}\" xe-required=\"xeRequired\"></xe-label><span id=\"xe-id-max-characters\">{{ xeMaxlength }} {{::'xe.text.max.chars' | xei18n}}</span><div class=\"xe-labeltext-margin\"></div><input ng-class=\"{readonly:xeReadonly}\" ng-model=\"ngModel\" ng-form=\"ngForm\" class=\"{{xeType}}-field\" id=\"{{xeId}}\" name=\"{{xeName}}\" placeholder=\"{{xePlaceholder}}\" ng-pattern=\"xePattern\" ng-required=\"xeRequired\" ng-maxlength=\"xeMaxlength\" ng-minlength=\"xeMinlength\" ng-readonly=\"xeReadonly\" aria-labelledby=\"{{xeId + 'label'}}\" aria-describedby=\"xe-id-max-characters\" aria-required=\"{{xeRequired}}\"><br><div class=\"error-messages\" id=\"{{xeId+'label'}}\" ng-messages=\"\" ng-show=\"{{!xeNotification}}\"><div ng-message=\"required\">{{::'textbox.validation.required' | xei18n}}</div><div ng-message=\"maxlength\">{{::'textbox.validation.maxlength' | xei18n}} {{xeMaxlength}}</div></div></div>");
}]);

angular.module("templates/text-box-password.html", []).run(["$templateCache", function ($templateCache) {
  $templateCache.put("templates/text-box-password.html",
    "<div class=\"textbox-container\" aria-live=\"assertive\"><xe-label xe-value=\"{{xeLabel}}\" xe-for=\"{{xeId}}\" xe-required=\"xeRequired\"></xe-label><div class=\"xe-labeltext-margin\"></div><input ng-class=\"{readonly:xeReadonly}\" ng-model=\"ngModel\" ng-form=\"ngForm\" class=\"{{xeType}}-field\" id=\"{{xeId}}\" type=\"password\" name=\"{{xeName}}\" placeholder=\"{{xePlaceholder}}\" ng-pattern=\"xePattern\" ng-required=\"xeRequired\" ng-maxlength=\"xeMaxlength\" ng-minlength=\"xeMinlength\" ng-readonly=\"xeReadonly\" aria-describedby=\"{{xeId + 'label'}}\"><br><div class=\"error-messages\" id=\"{{xeId+'label'}}\" ng-messages=\"\" ng-show=\"{{!xeNotification}}\"><div ng-message=\"required\">{{::'textbox.validation.required' | xei18n}}</div><div ng-message=\"maxlength\">{{::'textbox.validation.maxlength' | xei18n}} {{xeMaxlength}}</div></div></div>");
}]);

angular.module("templates/text-box.html", []).run(["$templateCache", function ($templateCache) {
  $templateCache.put("templates/text-box.html",
    "<div class=\"textbox-container\" aria-live=\"assertive\"><xe-label xe-value=\"{{xeLabel}}\" xe-for=\"{{xeId}}\" xe-required=\"xeRequired\"></xe-label><div class=\"xe-labeltext-margin\"></div><input ng-class=\"{readonly:xeReadonly}\" ng-model=\"ngModel\" ng-form=\"ngForm\" class=\"{{xeType}}-field\" id=\"{{xeId}}\" name=\"{{xeName}}\" placeholder=\"{{xePlaceholder}}\" ng-pattern=\"xePattern\" ng-required=\"xeRequired\" ng-maxlength=\"xeMaxlength\" ng-minlength=\"xeMinlength\" ng-readonly=\"xeReadonly\" aria-describedby=\"{{xeId + 'label'}}\" aria-required=\"{{xeRequired}}\"><br><div class=\"error-messages\" id=\"{{xeId+'label'}}\" ng-messages=\"\" ng-show=\"{{!xeNotification}}\"><div ng-message=\"required\">{{::'textbox.validation.required' | xei18n}}</div><div ng-message=\"maxlength\">{{::'textbox.validation.maxlength' | xei18n}} {{xeMaxlength}}</div></div></div>");
}]);

angular.module("templates/column-filter.html", []).run(["$templateCache", function ($templateCache) {
  $templateCache.put("templates/column-filter.html",
    "<span><div class=\"column-filter-container\" ng-if=\"::!nocolumnFilterMenu\"><button type=\"button\" class=\"column-filter-button\" ng-click=\"bindClickEvent($event)\" aria-haspopup=\"true\" aria-labelledby=\"columnFilter\" xe-field=\"columnFilterMenu\"><span id=\"columnFilter\" class=\"placeholder\" ng-bind=\"'dataTable.columnFilter.label' | xei18n\"></span><div class=\"dropdown-icon\">&nbsp;</div></button><ul class=\"column-setting-menu\" ng-hide=\"hideColumnSettingMenu\" role=\"menu\" aria-labelledby=\"columnFilter\"><li role=\"presentation\"><xe-checkbox xe-label=\"{{::'dataTable.columnFilter.selectAll' | xei18n}}\" xe-model=\"selectAll.visible\" xe-on-click=\"onSelectAll(header, event)\" xe-id=\"0\" data-name=\"all\" aria-role=\"menuitemcheckbox\"></xe-checkbox></li><li ng-repeat=\"heading in header\" ng-class=\"{'disabled': heading.options.disable}\" ng-hide=\"heading.options.columnShowHide === false\" data-name=\"{{heading.name}}\" role=\"presentation\"><xe-checkbox xe-id=\"{{heading.name}}\" xe-value=\"{{$index+1}}\" xe-label=\"{{heading.title}}\" xe-model=\"heading.options.visible\" xe-on-click=\"hideUnhideColumn(heading, event)\" xe-disabled=\"heading.options.disable\" aria-role=\"menuitemcheckbox\"></xe-checkbox></li></ul></div></span>");
}]);

angular.module("templates/pagination.html", []).run(["$templateCache", function ($templateCache) {
  $templateCache.put("templates/pagination.html",
    "<div class=\"tfoot pagination-container visible-lg\" role=\"navigation\" ng-cloak><div id=\"resultsFound\" class=\"results-container\" ng-bind=\"('pagination.record.found' | xei18n) + ': ' + resultsFound\"></div><div class=\"pagination-controls\"><xe-button xe-type=\"secondary\" xe-btn-class=\"first\" xe-aria-label=\"{{::'pagination.first.label' | xei18n}}\" xe-btn-click=\"first()\" xe-disabled=\"firstPrev\" ng-cloak></xe-button><xe-button xe-type=\"secondary\" xe-btn-class=\"previous\" xe-aria-label=\"{{::'pagination.previous.label' | xei18n}}\" xe-btn-click=\"prev()\" xe-disabled=\"firstPrev\" ng-cloak></xe-button><xe-label xe-value=\"{{::'pagination.page.label' | xei18n}}\" role=\"presentation\" aria-hidden=\"true\"></xe-label><span title=\"{{::'pagination.page.shortcut.label' | xei18n}}\" role=\"presentation\"><input id=\"pageInput\" type=\"number\" ng-model=\"onPage\" aria-valuenow=\"{{onPage}}\" aria-valuemax=\"{{numberOfPages}}\" aria-valuemin=\"{{!numberOfPages ? 0 : 1}}\" max=\"{{numberOfPages}}\" min=\"{{!numberOfPages ? 0 : 1}}\" ng-model-options=\"{ debounce: {'default': 200, 'blur': 0} }\" ng-change=\"paggeNumberChange()\" ng-blur=\"focusOut($event)\" aria-label=\"{{::'pagination.page.aria.label' | xei18n}}. {{::'pagination.page.label' | xei18n}} {{onPage}} {{::'pagination.page.of.label' | xei18n}} {{numberOfPages}}\" ng-cloak></span><xe-label xe-value=\"{{::'pagination.page.of.label' | xei18n}} {{numberOfPages}}\" role=\"presentation\"></xe-label><xe-button xe-type=\"secondary\" xe-btn-class=\"next\" xe-aria-label=\"{{::'pagination.next.label' | xei18n}}\" xe-btn-click=\"next()\" xe-disabled=\"nextLast\" ng-cloak></xe-button><xe-button xe-type=\"secondary\" xe-btn-class=\"last\" xe-aria-label=\"{{::'pagination.last.label' | xei18n}}\" xe-btn-click=\"last()\" xe-disabled=\"nextLast\" ng-cloak></xe-button><xe-label id=\"perPage\" xe-value=\"{{::'pagination.per.page.label' | xei18n}}\" role=\"presentation\" aria-hidden=\"true\"></xe-label><select class=\"per-page-select\" aria-labelledby=\"perPage\" ng-model=\"offset\" ng-options=\"pageOffset for pageOffset in ::pageOffsets\" ng-change=\"offsetChanged(true)\" ng-disabled=\"resultsFound === 0\"></select></div></div>");
}]);

angular.module("templates/search.html", []).run(["$templateCache", function ($templateCache) {
  $templateCache.put("templates/search.html",
    "<form name=\"form\" class=\"search-container\"><xe-simple-text-box input-field=\"{{searchConfig.id}}\" xe-class=\"search\" value=\"value\" place-holder=\"{{searchConfig.placeholder}}\" on-keydown=\"searchKeydown(data, id, event)\" on-keypress=\"searchKeypress(data, id, event)\" on-focus=\"onFocus(event)\" on-blur=\"onBlur(event)\" aria-label=\"{{::ariaLabel}}\"></xe-simple-text-box></form>");
}]);

angular.module("templates/dataTable.html", []).run(["$templateCache", function ($templateCache) {
  $templateCache.put("templates/dataTable.html",
    "<div id=\"{{tableId}}\" class=\"table-container\" ng-class=\"{'fixed-height': !!height, 'noToolbar': noCaptionAndToolbar, 'no-data': !resultsFound, 'empty': emptyTableMsg}\" browser-detect role=\"grid\" aria-labelledby=\"gridCaption\" ng-cloak><div class=\"caption\" ng-if=\"::!noCaptionBar\" xe-section=\"{{xeSection + 'CaptionBar'}}\"><table class=\"data-table\" role=\"presentaion\"><caption ng-class=\"{'search-opened': hideContainer}\"><span id=\"gridCaption\" class=\"caption-container font-semibold\" ng-if=\"::!nocaption\" ng-bind=\"::caption\" xe-field=\"caption\"></span><div class=\"toolbar\" ng-if=\"toolbar\"><xe-toolbar></xe-toolbar><xe-column-filter></xe-column-filter><span role=\"search\" title=\"{{'search.title' | xei18n}}\" ng-if=\"::!nosearch\"><xe-search value=\"searchConfig.searchString\" place-holder=\"{{'search.label' | xei18n}}\" on-change=\"fetchSpecial(query)\" on-focus=\"onSearchFocus({event: event})\" on-blur=\"onSearchBlur({event: event})\" search-config=\"searchConfig\" xe-focus loading-data=\"loadingData\"></xe-search></span></div></caption></table></div><div class=\"hr-scrollable-content\"><div class=\"thead\"><table class=\"data-table\" ng-style=\"headerPadding\" role=\"presentaion\"><thead role=\"rowgroup\"><tr role=\"row\"><th class=\"font-semibold width-animate {{::heading.name}}\" ng-repeat=\"heading in header\" ng-class=\"{'sortable': heading.options.sortable, 'ascending': sortArray[heading.name].ascending, 'decending': sortArray[heading.name].decending}\" ng-if=\"heading.options.visible === true\" ng-style=\"{'width': heading.dynamicWidth + 'px'}\" data-name=\"{{::heading.name}}\" ng-click=\"onSort({heading: heading}); sortOnHeading(heading, $index);\" role=\"columnheader\" aria-sort=\"{{sortArray[heading.name].ascending ? ('dataTable.sort.ascending.label' | xei18n) : (sortArray[heading.name].decending ? ('dataTable.sort.descending.label' | xei18n) : 'none')}}\" aria-describedby=\"{{'headingAria' + $index}}\" drag-drop=\"handleDrop\" tabindex=\"0\" xe-field=\"{{::heading.name}}\" xe-heading-injector xe-focus xe-click-grid><div class=\"data\" title=\"{{heading.label}}\"><span ng-show=\"::heading.options.titleVisible !== false\" aria-hidden=\"false\" ng-bind=\"::heading.title\"></span><label id=\"${{'headingAria' + $index}}\" class=\"sr-only\" ng-bind=\"heading.ariaLabel + (heading.options.sortable ? ('dataTable.sortable.label' | xei18n) : '')\"></label></div></th></tr></thead></table></div><div class=\"tbody\" ng-style=\"::{'height': height}\" continuous-scroll=\"nextPage()\" scroll-parent=\"{{::continuousScrollParent}}\" aria-labelledby=\"msg\" tabindex=\"{{(!resultsFound || emptyTableMsg) ? 0 : ''}}\" resize><div id=\"msg\" ng-bind=\"emptyTableMsg? emptyTableMsg : ((!resultsFound && !loadingData) ? noDataMsg : '')\"></div><table class=\"data-table\" ng-class=\"::mobileLayout ? 'mobileLayout' : 'noMobileLayout'\" role=\"presentaion\"><thead role=\"presentaion\" aria-hidden=\"true\"><tr><th class=\"font-semibold {{::heading.name}}\" ng-repeat=\"heading in header\" ng-class=\"{'sortable': heading.options.sortable, 'ascending': sortArray[heading.name].ascending, 'decending': sortArray[heading.name].decending}\" ng-if=\"heading.options.visible === true\" ng-style=\"{'width': heading.dynamicWidth + 'px'}\" data-name=\"{{::heading.name}}\" xe-field=\"{{::heading.name}}\" xe-heading-injector tabindex=\"0\"><div class=\"data\"><span ng-show=\"::heading.options.titleVisible !== false\" ng-bind=\"::heading.title\"></span></div></th></tr></thead><tbody role=\"rowgroup\"><tr ng-repeat=\"row in content\" ng-click=\"onRowClick({data:row,index:$index})\" ng-dblclick=\"onRowDoubleClick({data:row,index:$index})\" xe-row-injector tabindex=\"-1\" role=\"row\"><td class=\"width-animate\" ng-repeat=\"heading in header\" ng-class=\"{'align-right': heading.options.actionOrStatus, 'sortable': heading.options.sortable}\" ng-if=\"heading.options.visible === true\" data-name=\"{{::heading.name}}\" data-title=\"{{::(heading.title && heading.options.titleVisible !== false) ? heading.title + ':' : ''}}\" attain-mobile-layout=\"{{mobileLayout[heading.name]}}\" xe-field=\"{{::heading.name}}\" xe-cell-injector xe-focus xe-click-grid role=\"gridcell\" ng-cloak><div ng-if=\"!isExtendedField(row, heading.name)\"><span ng-bind=\"getObjectValue(row, heading.name)\" tabindex=\"0\"></span></div><div ng-if=\"isExtendedField(row, heading.name)\"><div ng-if=\"isEditable\"><div ng-if=\"dataType === 'DATE'\"><input date-picker=\"\" value=\"{{::extensionValue}}\" class=\"datePicker\" aria-labelledby=\"\"></div><div ng-if=\"dataType === 'VARCHAR2'\"><xe-simple-text-box value=\"::extensionValue\"></xe-simple-text-box></div><div ng-if=\"dataType === 'NUMBER'\"><decimal-input ng-model=\"::extensionValue\" decimals=\"0\"></decimal-input></div></div><div ng-if=\"!isEditable\"><span ng-bind=\"::extensionValue\"></span></div></div></td></tr></tbody></table></div></div><div class=\"tfoot\" ng-transclude></div><xe-pagination model=\"content\" results-found=\"resultsFound\" ng-show=\"showPagination\" xe-grid-pagination-control search-string=\"searchConfig.searchString\"></xe-pagination><div ng-show=\"loadingData\" class=\"load-indicator\"><div class=\"spinner\"><div class=\"bounce1\"></div><div class=\"bounce2\"></div><div class=\"bounce3\"></div></div></div></div>");
}]);

angular.module("templates/dialog.html", []).run(["$templateCache", function ($templateCache) {
  $templateCache.put("templates/dialog.html",
    "<div id=\"xeModalMask\" class=\"xe-modal-mask\" ng-show=\"show\" tabindex=\"-1\" aria-labelledby=\"xeModalLabel\" role=\"dialog\"><div class=\"xe-modal-wrapper\" role=\"document\"><div class=\"xe-modal-container\"><span class=\"xe-modal-close\" ng-click=\"hide()\" tabindex=\"0\" title=\"{{about[api.close]}\"></span><h4 id=\"xeModalLabel\" hidden>{{about[api.title]}}</h4><div class=\"xe-modal-header\" tabindex=\"0\"><h5 class=\"xe-app-title\">{{about[api.name]}}</h5><h6 class=\"xe-app-version\">{{about[api.version]}}</h6></div><hr><div class=\"xe-modal-body\" role=\"list\" tabindex=\"0\"><h6 id=\"xe-modal-gen-sec\" class=\"xe-title\">General</h6><ul role=\"list\" aria-labelledby=\"xe-modal-gen-sec\"><li role=\"listitem\" aria-level=\"{{$index + 1}}\" ng-repeat-start=\"(key, value) in about[api.general]\" ng-repeat-end><strong>{{key}}</strong> : <span>{{value}}</span></li></ul></div><hr><div class=\"xe-modal-body\" role=\"list\" tabindex=\"0\"><h6 id=\"xe-modal-plugin-info\" class=\"xe-title\">Plugin information</h6><ul role=\"list\" aria-labelledby=\"xe-modal-plugin-info\"><li role=\"listitem\" aria-level=\"{{$index + 1}}\" ng-repeat-start=\"(key, value) in about[api.plugin]\" ng-repeat-end><strong>{{key}}</strong> : <span>{{value}}</span></li></ul></div><hr><div class=\"xe-modal-body\" role=\"list\" tabindex=\"0\"><h6 id=\"xe-modal-other-plugin-info\" class=\"xe-title\">Other Plugin information</h6><ul role=\"list\" aria-labelledby=\"xe-modal-other-plugin-info\"><li role=\"listitem\" aria-level=\"{{$index + 1}}\" ng-repeat-start=\"(key, value) in about[api.otherPlugin]\" ng-repeat-end><strong>{{key}}</strong> : <span>{{value}}</span></li></ul></div><div class=\"xe-modal-footer\" tabindex=\"0\">{{about[api.copyright]}}</div></div></div></div>");
}]);

angular.module("templates/dialog_default.html", []).run(["$templateCache", function ($templateCache) {
  $templateCache.put("templates/dialog_default.html",
    "<div id=\"xeModalMask\" class=\"xe-modal-mask\" ng-show=\"show\" tabindex=\"-1\" aria-labelledby=\"xeModalMask\" role=\"region\"><div class=\"xe-modal-wrapper\" role=\"dialog\" title=\"{{about[api.title]}}\"><div id=\"xeModalContainer\" class=\"xe-modal-container\"><span id=\"xeModalClose\" class=\"xe-modal-close\" ng-click=\"hide()\" tabindex=\"0\" role=\"button\" alt=\"{{about[api.close]}}\" title=\"{{about[api.close]}}\"></span><h4 id=\"xeModalLabel\" hidden>{{about[api.title]}}</h4><div id=\"xeModalHeader\" class=\"xe-modal-header\" tabindex=\"0\"><h5 class=\"xe-app-title\">{{about[api.name]}}</h5><h6 class=\"xe-app-version\">{{about[api.version]}}</h6></div><hr><div id=\"xeModalFooter\" class=\"xe-modal-footer\" tabindex=\"0\" role=\"heading\"><span class=\"xe-modal-footer-copyright\">&copy; {{about[api.copyright]}}</span><br><br><span class=\"xe-modal-footer-copyright-info\">{{about[api.copyrightLegalNotice]}}</span></div></div></div></div>");
}]);

angular.module("templates/modal.html", []).run(["$templateCache", function ($templateCache) {
  $templateCache.put("templates/modal.html",
    "<div id=\"xeModalMask\" class=\"xe-popup-mask\" ng-show=\"show\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"xePopTitle\"><div class=\"xe-popup-wrapper\"><div class=\"xe-popup-container\"><div id=\"xeModalHeader\" class=\"xe-popup-header\"><h5 id=\"xePopTitle\" class=\"xe-app-title\" role=\"heading\" data-ng-bind=\"popupTitle\"></h5><span id=\"xePopupClose\" class=\"xe-popup-close\" ng-click=\"hide()\" tabindex=\"0\" role=\"button\" aria-label=\"{{'userpreference.popup.language.close' | xei18n}}\" alt=\"{{'userpreference.popup.language.close' | xei18n}}\" title=\"{{'userpreference.popup.language.close' | xei18n}}\"></span></div><div id=\"xePopupContent\" class=\"xe-pop-content\" ng-transclude=\"popupContent\"></div><div id=\"buttonContainer\" class=\"buttonContainer\" ng-transclude=\"popupButtons\"></div><div tabindex=\"0\" id=\"tabOrdering\"></div></div></div></div>");
}]);

angular.module("templates/tabNav.html", []).run(["$templateCache", function ($templateCache) {
  $templateCache.put("templates/tabNav.html",
    "<div class=\"xe-tab-container\" role=\"presentation\"><ul class=\"xe-tab-nav\" role=\"tablist\"><li ng-repeat=\"tab in tabnav.tabs\" ng-click=\"tabnav.activate(tab)\" ng-class=\"{active: tab.active}\" ng-repeat-complete role=\"tab\" aria-controls=\"{{'xe-tab-panel'+ ($index+1)}}\" aria-selected=\"{{tab.active}}\"><a ui-sref=\"{{ tab.state && tab.state || '#' }}\" href=\"#\" id=\"{{'xe-tab'+ ($index+1)}}\" title=\"{{tab.heading}}\" ng-if=\"tab.state\">{{tab.heading}} <span></span> </a><a href=\"#\" id=\"{{'xe-tab'+ ($index+1)}}\" title=\"{{tab.heading}}\" ng-if=\"!tab.state\">{{tab.heading}} <span></span></a></li></ul><div class=\"xe-tab-content\" role=\"presentation\"><ng-transclude></ng-transclude></div></div>");
}]);

angular.module("templates/tabPanel.html", []).run(["$templateCache", function ($templateCache) {
  $templateCache.put("templates/tabPanel.html",
    "<div class=\"xe-tab-container\" role=\"presentation\"><div id=\"{{ 'xe-tab-panel'+ tabIndex}}\" class=\"xe-tab-panel\" ng-show=\"active\" ng-class=\"{active: active }\" role=\"tabpanel\" aria-labelledby=\"{{'xe-tab'+ tabIndex}}\" content=\"\" aria-hidden=\"{{ !active }}\"></div></div>");
}]);

angular.module("templates/timePicker.html", []).run(["$templateCache", function ($templateCache) {
  $templateCache.put("templates/timePicker.html",
    "<div class=\"xe-time-container\" role=\"presentation\"><div class=\"input-outerContainer\"><div class=\"input-container\"><input class=\"input-field xeTimePickerTextField\" name=\"{{xeName}}\" ng-model=\"displayModel\" placeholder=\"{{xeTimePlaceholder}}\" id=\"{{xeId}}\" title=\"{{'js.input.timepicker.info' | xei18n}}\" aria-label=\"{{'timepicker.textbox.aria.label' | xei18n}} {{'default.time.format' | xei18n}}\"> <button type=\"button\" class=\"inputicon\" tabindex=\"-1\"></button></div><div class=\"timepickerContainer\" ng-show=\"toggleContainer\"><div id=\"article\" class=\"mainContainer\"><div class=\"wrappingDiv hrsContainer\" ng-class=\"timeObject.is24Hrformat ? 'LTRpadding24hrs' : 'LTRpadding12hrs'\"><div class=\"itemContainer previousDiv\"></div><ul role=\"listbox\" class=\"dropdown ulContainer hrsFocus\"><li class=\"hrs_{{$index}}\" role=\"option\" data-ng-repeat=\"hr in timeObject.hours\" value=\"{{hr}}\" data-ng-bind=\"hr\"></li></ul><div class=\"itemContainer nextDiv\"></div></div><div class=\"wrappingDiv minContainer\"><div class=\"itemContainer previousDiv\"></div><ul class=\"dropdown ulContainer minFocus\" role=\"listbox\"><li role=\"option\" class=\"mins_{{$index}}\" tabindex=\"0\" data-ng-repeat=\"mins in timeObject.minutes\" value=\"{{mins}}\" data-ng-bind=\"mins\"></li></ul><div class=\"itemContainer nextDiv\" data-ng-bind=\"timeObject.configstep\"></div></div><div role=\"listbox\" class=\"wrappingDiv meridianContainer\" ng-if=\"!timeObject.is24Hrformat\" aria-label=\"{{'timepicker.am.pm.aria.label' | xei18n}}\"><div class=\"itemContainer\"></div><div role=\"option\" aria-live=\"assertive\" class=\"tickerContainer pushmeBorder meridianFocus\" tabindex=\"0\" ng-bind=\"'default.time.am' | xei18n\"></div><div aria-label=\"{{'default.time.pm' | xei18n}}\" class=\"tickerContainer meridianPm\" ng-bind=\"'default.time.pm' | xei18n\"></div></div></div><footer class=\"timerFooter\"><xe-button class=\"footerButton footercancelButton\" xe-type=\"secondary\" xe-label=\"{{'js.timepicker.cancel' | xei18n}}\" aria-label=\"{{'js.timepicker.cancel' | xei18n}}\"></xe-button><xe-button class=\"footerButton\" xe-type=\"primary\" xe-label=\"{{'js.timepicker.set' | xei18n}}\" aria-label=\"{{'js.timepicker.set' | xei18n}}\"></xe-button><div class=\"extraDiv\" tabindex=\"0\"></div></footer></div></div></div>");
}]);

angular.module("templates/timePicker_rtl.html", []).run(["$templateCache", function ($templateCache) {
  $templateCache.put("templates/timePicker_rtl.html",
    "<div class=\"xe-time-container\" role=\"presentation\"><div class=\"input-outerContainer\"><div class=\"input-container\"><input class=\"input-field xeTimePickerTextField\" name=\"{{xeName}}\" ng-model=\"displayModel\" placeholder=\"{{xeTimePlaceholder}}\" id=\"{{xeId}}\" title=\"{{'js.input.timepicker.info' | xei18n}}\" aria-label=\"{{'timepicker.textbox.aria.label' | xei18n}} {{'default.time.format' | xei18n}}\"> <button type=\"button\" class=\"inputicon\" tabindex=\"-1\"></button></div><div class=\"timepickerContainer\" ng-show=\"toggleContainer\"><div id=\"article\" class=\"mainContainer\"><div role=\"listbox\" class=\"wrappingDiv meridianContainer rtl12hrsPadding\" ng-if=\"!timeObject.is24Hrformat\" aria-label=\"{{'timepicker.am.pm.aria.label' | xei18n}}\"><div id=\"prevampmDiv\" class=\"itemContainer\"></div><div role=\"option\" aria-live=\"assertive\" class=\"tickerContainer pushmeBorder meridianFocus\" tabindex=\"0\" ng-bind=\"'default.time.am' | xei18n\"></div><div aria-label=\"{{'default.time.pm' | xei18n}}\" class=\"tickerContainer meridianPm\" ng-bind=\"'default.time.pm' | xei18n\"></div></div><div class=\"wrappingDiv hrsContainer\" ng-class=\"timeObject.is24Hrformat ? 'rtl24hrsPadding' : 'rtlhrsPadding'\"><div id=\"prevHrDiv\" class=\"itemContainer previousDiv\"></div><ul id=\"nav\" role=\"listbox\" class=\"dropdown ulContainer hrsFocus\"><li class=\"hrs_{{$index}}\" role=\"option\" data-ng-repeat=\"hr in timeObject.hours\" value=\"{{hr}}\" data-ng-bind=\"hr\"></li></ul><div class=\"itemContainer nextDiv\"></div></div><div class=\"wrappingDiv minContainer\"><div class=\"itemContainer previousDiv\"></div><ul class=\"dropdown ulContainer minFocus\"><li class=\"mins_{{$index}}\" tabindex=\"0\" data-ng-repeat=\"mins in timeObject.minutes\" value=\"{{mins}}\" data-ng-bind=\"mins\"></li></ul><div class=\"itemContainer nextDiv\" data-ng-bind=\"timeObject.configstep\"></div></div></div><footer class=\"timerFooter\"><xe-button class=\"footerButton footercancelButton\" xe-type=\"secondary\" xe-label=\"{{'js.timepicker.cancel' | xei18n}}\" aria-label=\"{{'js.timepicker.cancel' | xei18n}}\"></xe-button><xe-button class=\"footerButton\" xe-type=\"primary\" xe-label=\"{{'js.timepicker.set' | xei18n}}\" aria-label=\"{{'js.timepicker.set' | xei18n}}\"></xe-button><div class=\"extraDiv\" tabindex=\"0\"></div></footer></div></div></div>");
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
            templateUrl : 'templates/badge.html'
        };
    });
}());

(function () {
    'use strict';
    angular.module('button', []).directive('xeButton', function () {
        return {
            restrict: 'E',
            scope: {
                xeType : '@',
                xeDisabled : '=',
                xeLabel : '@',
                xeBtnClick : '&',
                xeBtnClass : "@",
                xeAriaLabel : "@"
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
                xeHidden : '@?',
                xeRequired : '=?',
                xeFor: '@'
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
                placeHolder: '@',
                disabled: '=',
                onChange: '&',
                onKeydown: '&',
                onKeypress: '&',
                onFocus : '&',
                onBlur : '&',
                required: '='
            },
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
/*****************************************************
 *  Copyright 2016 Ellucian Company L.P. and its affiliates. *
 *****************************************************/
/*global $*/
(function () {
    'use strict';
    angular.module('textarea', []).directive('xeTextArea', ['$filter', function ($filter) {
        return {
            restrict: 'E',
            scope: {
                ngModel: '=',
                xeOnChange: '&',
                xePlaceholder: '@',
                xeLabel: '=',
                xeId: '=',
                xeRequired: '=',
                xeReadonly: '=',
                xeCharCounter: '@'
            },
            templateUrl: function (element, attrs) {
                if (attrs.xeCharCounter) {return 'templates/text-area-counter.html'; }
                return 'templates/text-area.html';
            },
            controller: function ($scope, $element, $attrs) {
                var CharCounterLiveRegion, remainingCharsMsg;
                //Setting Live Regions for Accessibility
                if ($attrs.xeCharCounter) {
                    CharCounterLiveRegion = $('.textarea-char-accessible');
                    if (CharCounterLiveRegion.length === 0) {
                        CharCounterLiveRegion = $("<span>", {
                            role: "status",
                            "aria-live": "assertive",
                            "aria-atomic": "true",
                            "aria-hidden": "false"
                        })
                            .addClass("textarea-char-accessible screen-reader")
                            .appendTo(document.body);
                    }
                }
                remainingCharsMsg = $filter('xei18n')('xe.text.limit.over', $attrs.xeCharCounter);

                //Accessibility for Text Area Focus Case to read Remaining Characters
                if ($attrs.xeCharCounter) {
                    $element.find('textarea').focus(function () {
                        var ariaTextRemainingChars = $filter('xei18n')('xe.text.chars.remaining.aria', $scope.charRemaining);
                        CharCounterLiveRegion.text(ariaTextRemainingChars);
                    });
                }

                $scope.$watch('ngModel', function () {

                    if ($attrs.xeCharCounter) {

                        if ($scope.charRemaining <= 0) {
                            $scope.charRemaining = 0;

                        } else {
                            $scope.charRemaining = $attrs.xeCharCounter;
                        }

                        if ($scope.ngModel) {
                            var currentChar = $scope.ngModel;
                            $scope.charRemaining = $attrs.xeCharCounter - currentChar.length;
                            $element.find('textarea').on("keyup change", function (e) {
                                if ($(this).val().length === $attrs.xeCharCounter) {
                                    CharCounterLiveRegion.text(remainingCharsMsg);
                                }
                            });
                        }
                    }
                });
            }
        };
    }]);
}());

/*****************************************************
 *  Copyright 2016 Ellucian Company L.P. and its affiliates. *
 *****************************************************/
/*global $*/
(function () {
    'use strict';
    angular.module('textbox', ['ngMessages']).directive('xeTextBox', ['$filter', function ($filter) {
        return {
            restrict: 'E',
            scope: {
                xePlaceholder: '@',
                xeId: '@',
                xeRequired: '=',
                xeType: '@',
                xeReadonly: '=',
                xeValidate: '=',
                xePattern: '@',
                xeErrorMessages: '=',
                xeNotification: '@',
                xeLabel: '@',
                xeName: '@',
                xeMaxlength: '@',
                xeMinlength: '@',
                xeFormName: '@',
                ngModel: '=',
                ngForm: '=',
                xeCharMax: '@'
            },
            require: ['ngModel', '?ngForm'],
            templateUrl: function (element, attrs) {
                if (attrs.xeType === 'password') {
                    return 'templates/text-box-password.html';
                }
                if (attrs.hasOwnProperty('xeCharMax')) {
                    return 'templates/text-box-char-limit.html';
                }
                return 'templates/text-box.html';

            },
            controller: function ($scope, $element, $attrs) {
                var CharCounterTextLiveRegion, remainingCharsTextMsg;
                if ($attrs.hasOwnProperty('xeCharMax')) {
                    CharCounterTextLiveRegion = $('.text-box-char-accessible');
                    remainingCharsTextMsg = $filter('xei18n')('xe.text.limit.over', $attrs.xeMaxlength);
                    if (CharCounterTextLiveRegion.length === 0) {
                        CharCounterTextLiveRegion = $("<span>", {
                            role: "status",
                            "aria-live": "assertive",
                            "aria-atomic": "true",
                            "aria-hidden": "false"
                        })
                            .addClass("text-box-char-accessible screen-reader")
                            .appendTo(document.body);
                    }

                    $element.find('input').on("keyup change", function (e) {
                        if ($(this).val().length >= $attrs.xeMaxlength) {
                            CharCounterTextLiveRegion.text(remainingCharsTextMsg);
                        }
                    });
                }
            },
            compile: function (elem, attrs) {
                var formStr = "ngForm." + attrs.xeName;
                elem.find("div.error-messages").attr("ng-messages", formStr + "." + "$error");
                elem.find("div.error-messages").attr("ng-if", formStr  + "." + attrs.xeName + "." + "$touched");
            }
        };
    }]);
}());

/*!
 * ui-select
 * http://github.com/angular-ui/ui-select
 * Version: 0.16.0 - 2016-03-23T20:51:56.609Z
 * License: MIT
 */


(function () {
    "use strict";
    var KEY = {
        TAB: 9,
        ENTER: 13,
        ESC: 27,
        SPACE: 32,
        LEFT: 37,
        UP: 38,
        RIGHT: 39,
        DOWN: 40,
        SHIFT: 16,
        CTRL: 17,
        ALT: 18,
        PAGE_UP: 33,
        PAGE_DOWN: 34,
        HOME: 36,
        END: 35,
        BACKSPACE: 8,
        DELETE: 46,
        COMMAND: 91,

        MAP: { 91 : "COMMAND", 8 : "BACKSPACE", 9 : "TAB", 13 : "ENTER", 16 : "SHIFT", 17 : "CTRL", 18 : "ALT", 19 : "PAUSEBREAK", 20 : "CAPSLOCK", 27 : "ESC", 32 : "SPACE", 33 : "PAGE_UP", 34 : "PAGE_DOWN", 35 : "END", 36 : "HOME", 37 : "LEFT", 38 : "UP", 39 : "RIGHT", 40 : "DOWN", 43 : "+", 44 : "PRINTSCREEN", 45 : "INSERT", 46 : "DELETE", 48 : "0", 49 : "1", 50 : "2", 51 : "3", 52 : "4", 53 : "5", 54 : "6", 55 : "7", 56 : "8", 57 : "9", 59 : ";", 61 : "=", 65 : "A", 66 : "B", 67 : "C", 68 : "D", 69 : "E", 70 : "F", 71 : "G", 72 : "H", 73 : "I", 74 : "J", 75 : "K", 76 : "L", 77 : "M", 78 : "N", 79 : "O", 80 : "P", 81 : "Q", 82 : "R", 83 : "S", 84 : "T", 85 : "U", 86 : "V", 87 : "W", 88 : "X", 89 : "Y", 90 : "Z", 96 : "0", 97 : "1", 98 : "2", 99 : "3", 100 : "4", 101 : "5", 102 : "6", 103 : "7", 104 : "8", 105 : "9", 106 : "*", 107 : "+", 109 : "-", 110 : ".", 111 : "/", 112 : "F1", 113 : "F2", 114 : "F3", 115 : "F4", 116 : "F5", 117 : "F6", 118 : "F7", 119 : "F8", 120 : "F9", 121 : "F10", 122 : "F11", 123 : "F12", 144 : "NUMLOCK", 145 : "SCROLLLOCK", 186 : ";", 187 : "=", 188 : ",", 189 : "-", 190 : ".", 191 : "/", 192 : "`", 219 : "[", 220 : "\\", 221 : "]", 222 : "'"
        },

        isControl: function (e) {
            var k = e.which;
            switch (k) {
                case KEY.COMMAND:
                case KEY.SHIFT:
                case KEY.CTRL:
                case KEY.ALT:
                    return true;
            }

            if (e.metaKey) {
                return true;
            }

            return false;
        },
        isFunctionKey: function (k) {
            k = k.which ? k.which : k;
            return k >= 112 && k <= 123;
        },
        isVerticalMovement: function (k) {
            return ~[KEY.UP, KEY.DOWN].indexOf(k);
        },
        isHorizontalMovement: function (k) {
            return ~[KEY.LEFT, KEY.RIGHT, KEY.BACKSPACE, KEY.DELETE].indexOf(k);
        },
        toSeparator: function (k) {
            var sep = {ENTER: "\n", TAB: "\t", SPACE: " "}[k];
            if (sep) { return sep; }
            // return undefined for special keys other than enter, tab or space.
            // no way to use them to cut strings.
            return KEY[k] ? undefined : k;
        }
    };

    /**
     * Add querySelectorAll() to jqLite.
     *
     * jqLite find() is limited to lookups by tag name.
     * TODO This will change with future versions of AngularJS, to be removed when this happens
     *
     * See jqLite.find - why not use querySelectorAll? https://github.com/angular/angular.js/issues/3586
     * See feat(jqLite): use querySelectorAll instead of getElementsByTagName in jqLite.find https://github.com/angular/angular.js/pull/3598
     */
    if (angular.element.prototype.querySelectorAll === undefined) {
        angular.element.prototype.querySelectorAll = function (selector) {
            return angular.element(this[0].querySelectorAll(selector));
        };
    }

    /**
     * Add closest() to jqLite.
     */
    if (angular.element.prototype.closest === undefined) {
        angular.element.prototype.closest = function (selector) {
            var elem = this[0];
            var matchesSelector = elem.matches || elem.webkitMatchesSelector || elem.mozMatchesSelector || elem.msMatchesSelector;

            while (elem) {
                if (matchesSelector.bind(elem)(selector)) {
                    return elem;
                } else {
                    elem = elem.parentElement;
                }
            }
            return false;
        };
    }

    var latestId = 0;

    var uis = angular.module('ui.select', [])

        .constant('uiSelectConfig', {
            theme: 'bootstrap',
            searchEnabled: true,
            sortable: false,
            placeholder: '', // Empty by default, like HTML tag <select>
            refreshDelay: 0, // In milliseconds
            closeOnSelect: true,
            skipFocusser: false,
            dropdownPosition: 'auto',
            generateId: function () {
                return latestId = latestId + 1;
            },
            appendToBody: false
        })

        // See Rename minErr and make it accessible from outside https://github.com/angular/angular.js/issues/6913
        .service('uiSelectMinErr', function() {
            var minErr = angular.$$minErr('ui.select');
            return function() {
                var error = minErr.apply(this, arguments);
                var message = error.message.replace(new RegExp('\nhttp://errors.angularjs.org/.*'), '');
                return new Error(message);
            };
        })

        // Recreates old behavior of ng-transclude. Used internally.
        .directive('uisTranscludeAppend', function () {
            return {
                link: function (scope, element, attrs, ctrl, transclude) {
                    transclude(scope, function (clone) {
                        element.append(clone);
                    });
                }
            };
        })

    /**
     * Highlights text that matches $select.search.
     *
     * Taken from AngularUI Bootstrap Typeahead
     * See https://github.com/angular-ui/bootstrap/blob/0.10.0/src/typeahead/typeahead.js#L340
     */
        .filter('highlight', function () {
            function escapeRegexp(queryToEscape) {
                return ("" + queryToEscape).replace(/([.?*+^$[\]\\(){}|-])/g, '\\$1');
            }

            return function (matchItem, query) {
                return query && matchItem ? ("" + matchItem).replace(new RegExp(escapeRegexp(query), 'gi'), '<span class="ui-select-highlight">$&</span>') : matchItem;
            };
        })

    /**
     * A read-only equivalent of jQuery's offset function: http://api.jquery.com/offset/
     *
     * Taken from AngularUI Bootstrap Position:
     * See https://github.com/angular-ui/bootstrap/blob/master/src/position/position.js#L70
     */
        .factory('uisOffset',
        ['$document', '$window',
            function ($document, $window) {

                return function (element) {
                    var boundingClientRect = element[0].getBoundingClientRect();
                    return {
                        width: boundingClientRect.width || element.prop('offsetWidth'),
                        height: boundingClientRect.height || element.prop('offsetHeight'),
                        top: boundingClientRect.top + ($window.pageYOffset || $document[0].documentElement.scrollTop),
                        left: boundingClientRect.left + ($window.pageXOffset || $document[0].documentElement.scrollLeft)
                    };
                };
            }]);

    uis.directive('xeUiSelectChoices',
        ['uiSelectConfig', 'uisRepeatParser', 'uiSelectMinErr', '$compile', '$window', '$timeout',
            function (uiSelectConfig, RepeatParser, uiSelectMinErr, $compile, $window, $timeout) {

                return {
                    restrict: 'EA',
                    require: '^xeUiSelect',
                    replace: true,
                    transclude: true,
                    templateUrl: function (tElement) {
                        // Needed so the uiSelect can detect the transcluded content
                        tElement.addClass('ui-select-choices');

                        // Gets theme attribute from parent (ui-select)
                        var theme = tElement.parent().attr('theme') || uiSelectConfig.theme;
                        return theme + '/choices.tpl.html';
                    },

                    compile: function (tElement, tAttrs) {
                        if (!tAttrs.repeat) {
                            throw uiSelectMinErr('repeat', "Expected 'repeat' expression.");
                        }

                        return function link(scope, element, attrs, $select, transcludeFn) {

                            // var repeat = RepeatParser.parse(attrs.repeat);
                            var groupByExp = attrs.groupBy, groupFilterExp = attrs.groupFilter, groups,
                                choices = element.querySelectorAll('.ui-select-choices-row'), rowsInner = element.querySelectorAll('.ui-select-choices-row-inner');

                            $select.parseRepeatAttr(attrs, groupByExp, groupFilterExp, $select); //Result ready at $select.parserResult

                            $select.disableChoiceExpression = attrs.uiDisableChoice;
                            $select.onHighlightCallback = attrs.onHighlight;

                            $select.dropdownPosition = attrs.position ? attrs.position.toLowerCase() : uiSelectConfig.dropdownPosition;

                            if (groupByExp) {
                                groups = element.querySelectorAll('.ui-select-choices-group');
                                if (groups.length !== 1) {
                                    throw uiSelectMinErr('rows', "Expected 1 .ui-select-choices-group but got '{0}'.", groups.length);
                                }
                                groups.attr('ng-repeat', RepeatParser.getGroupNgRepeatExpression());
                            }

                            if (choices.length !== 1) {
                                throw uiSelectMinErr('rows', "Expected 1 .ui-select-choices-row but got '{0}'.", choices.length);
                            }

                            choices.attr('ng-repeat', $select.parserResult.repeatExpression(groupByExp))
                                .attr('ng-if', '$select.open'); //Prevent unnecessary watches when dropdown is closed
                            if ($window.document.addEventListener) {  //crude way to exclude IE8, specifically, which also cannot capture events
                                choices.attr('ng-mouseenter', '$select.setActiveItem('+ $select.parserResult.itemName +')')
                                    .attr('ng-click', '$select.select(' + $select.parserResult.itemName + ',$select.skipFocusser,$event)');
                            }

                            if (rowsInner.length !== 1) {
                                throw uiSelectMinErr('rows', "Expected 1 .ui-select-choices-row-inner but got '{0}'.", rowsInner.length);
                            }
                            rowsInner.attr('uis-transclude-append', ''); //Adding uisTranscludeAppend directive to row element after choices element has ngRepeat
                            if (!$window.document.addEventListener) {  //crude way to target IE8, specifically, which also cannot capture events - so event bindings must be here
                                rowsInner.attr('ng-mouseenter', '$select.setActiveItem('+$select.parserResult.itemName +')')
                                    .attr('ng-click', '$select.select(' + $select.parserResult.itemName + ',$select.skipFocusser,$event)');
                            }

                            $compile(element, transcludeFn)(scope); //Passing current transcludeFn to be able to append elements correctly from uisTranscludeAppend

                            scope.$watch('$select.search', function (newValue) {
                                if (newValue && !$select.open && $select.multiple) {
                                    $select.activate(false, true);
                                    $select.activeIndex = $select.tagging.isActivated ? -1 : 0;
                                }if (!attrs.minimumInputLength || $select.search.length >= attrs.minimumInputLength) {
                                    $select.refresh(attrs.refresh);
                                    $select.showMinMsg = false;
                                } else {
                                    $select.minimumInputLength = attrs.minimumInputLength;
                                    $select.showMinMsg = true;
                                    $select.items = [];
                                }
                            });
                            attrs.$observe('refreshDelay', function() {
                                // $eval() is needed otherwise we get a string instead of a number
                                var refreshDelay = scope.$eval(attrs.refreshDelay);
                                $select.refreshDelay = refreshDelay !== undefined ? refreshDelay : uiSelectConfig.refreshDelay;
                            });
                            scope.$watch('$select.items', function (newVal, oldVal) {
                                if ($select.items.length == 0) {
                                    $select.showNoResultsMsg = true;
                                } else {
                                    $select.showNoResultsMsg = false;
                                }
                            });
                        };
                    }
                };
            }]);

    /**
     * Contains ui-select "intelligence".
     *
     * The goal is to limit dependency on the DOM whenever possible and
     * put as much logic in the controller (instead of the link functions) as possible so it can be easily tested.
     */
    uis.controller('uiSelectCtrl',
        ['$scope', '$element', '$timeout', '$filter', 'uisRepeatParser', 'uiSelectMinErr', 'uiSelectConfig', '$parse', '$injector', '$window',
            function($scope, $element, $timeout, $filter, RepeatParser, uiSelectMinErr, uiSelectConfig, $parse, $injector, $window ) {

                var ctrl = this;
                var EMPTY_SEARCH = '';

                ctrl.placeholder = uiSelectConfig.placeholder;
                ctrl.searchEnabled = uiSelectConfig.searchEnabled;
                ctrl.sortable = uiSelectConfig.sortable;
                ctrl.refreshDelay = uiSelectConfig.refreshDelay;
                ctrl.paste = uiSelectConfig.paste;

                ctrl.removeSelected = false; //If selected item(s) should be removed from dropdown list
                ctrl.closeOnSelect = true; //Initialized inside uiSelect directive link function
                ctrl.skipFocusser = false; //Set to true to avoid returning focus to ctrl when item is selected
                ctrl.search = EMPTY_SEARCH;

                ctrl.activeIndex = 0; //Dropdown of choices
                ctrl.items = []; //All available choices

                ctrl.open = false;
                ctrl.focus = false;
                ctrl.disabled = false;
                ctrl.selected = undefined;

                ctrl.dropdownPosition = 'auto';

                ctrl.focusser = undefined; //Reference to input element used to handle focus events
                ctrl.resetSearchInput = true;
                ctrl.multiple = undefined; // Initialized inside uiSelect directive link function
                ctrl.disableChoiceExpression = undefined; // Initialized inside uiSelectChoices directive link function
                ctrl.tagging = {isActivated: false, fct: undefined};
                ctrl.taggingTokens = {isActivated: false, tokens: undefined};
                ctrl.lockChoiceExpression = undefined; // Initialized inside uiSelectMatch directive link function
                ctrl.clickTriggeredSelect = false;
                ctrl.$filter = $filter;
                ctrl.minimumInputLength;

                // Use $injector to check for $animate and store a reference to it
                ctrl.$animate = (function () {
                    try {
                        return $injector.get('$animate');
                    } catch (err) {
                        // $animate does not exist
                        return null;
                    }
                })();

                ctrl.searchInput = $element.querySelectorAll('input.ui-select-search');
                if (ctrl.searchInput.length !== 1) {
                    throw uiSelectMinErr('searchInput', "Expected 1 input.ui-select-search but got '{0}'.", ctrl.searchInput.length);
                }

                ctrl.isEmpty = function () {
                    return angular.isUndefined(ctrl.selected) || ctrl.selected === null || ctrl.selected === '' || (ctrl.multiple && ctrl.selected.length === 0);
                };

                function _findIndex (collection, predicate, thisArg){
                    if (collection.findIndex) {
                        return collection.findIndex(predicate, thisArg);
                    } else {
                        var list = Object(collection), length = list.length >>> 0, value, i;
                        for (i = 0; i < length; i += 1) {
                            value = list[i];
                            if (predicate.call(thisArg, value, i, list)) {
                                return i;
                            }
                        }
                        return -1;
                    }
                }

                // Most of the time the user does not want to empty the search input when in typeahead mode
                function _resetSearchInput () {
                    if (ctrl.resetSearchInput || (ctrl.resetSearchInput === undefined && uiSelectConfig.resetSearchInput)) {
                        ctrl.search = EMPTY_SEARCH;
                        //reset activeIndex+-
                        if (ctrl.selected && ctrl.items.length && !ctrl.multiple) {
                            ctrl.activeIndex = _findIndex(ctrl.items, function(item){
                                return angular.equals(this, item);
                            }, ctrl.selected);
                        }
                    }
                }

                function _groupsFilter(groups, groupNames) {
                    var i, j, result = [];
                    for(i = 0; i < groupNames.length; i += 1){
                        for(j = 0; j < groups.length; j += 1){
                            if(groups[j].name == [groupNames[i]]){
                                result.push(groups[j]);
                            }
                        }
                    }
                    return result;
                }

                // When the user clicks on ui-select, displays the dropdown list
                ctrl.activate = function(initSearchValue, avoidReset) {
                    if (!ctrl.disabled && !ctrl.open) {
                        if (!avoidReset) _resetSearchInput();

                        $scope.$broadcast('uis:activate');

                        ctrl.open = true;

                        ctrl.liveRegion = $('.uiselect-hidden-accessible');
                        ctrl.liveChoicestStusRegion = $('.uiselect-choice-status-hidden-accessible');

                        if (ctrl.liveRegion.length == 0) {
                            var regionSpan1 = angular.element('<span></span>');
                            regionSpan1.attr('role', 'status');
                            regionSpan1.attr('aria-live', 'assertive');
                            regionSpan1.attr('class', 'uiselect-hidden-accessible');
                            angular.element(document.body).append(regionSpan1);
                        }

                        if (ctrl.liveChoicestStusRegion.length == 0) {
                            var regionSpan2 = angular.element('<span></span>');
                            regionSpan2.attr('role', 'status');
                            regionSpan2.attr('aria-live', 'assertive');
                            regionSpan2.attr('aria-atomic', 'true');
                            regionSpan2.attr('class', 'uiselect-choice-status-hidden-accessible');
                            angular.element(document.body).append(regionSpan2);
                        }

                        ctrl.activeIndex = ctrl.activeIndex >= ctrl.items.length ? 0 : ctrl.activeIndex;

                        // ensure that the index is set to zero for tagging variants
                        // that where first option is auto-selected
                        if (ctrl.activeIndex === -1 && ctrl.taggingLabel !== false) {
                            ctrl.activeIndex = 0;
                        }

                        var container = $element.querySelectorAll('.ui-select-choices-content');
                        if (ctrl.$animate && ctrl.$animate.on && ctrl.$animate.enabled(container[0])) {
                            ctrl.$animate.on('enter', container[0], function (elem, phase) {
                                if (phase === 'close') {
                                    // Only focus input after the animation has finished
                                    $timeout(function () {
                                        ctrl.focusSearchInput(initSearchValue);
                                    });
                                }
                            });
                        } else {
                            $timeout(function () {
                                ctrl.focusSearchInput(initSearchValue);
                                if (!ctrl.tagging.isActivated && ctrl.items.length > 1) {
                                    var ariaResultFoundMessage;
                                    $('.uiselect-hidden-accessible').text("");
                                    if(!ctrl.searchEnabled){
                                        ariaResultFoundMessage = $filter('xei18n')('uiselect.search.disabled.results');
                                    }else {
                                        ariaResultFoundMessage = $filter('xei18n')('uiselect.search.results', ctrl.items.length);
                                    }
                                    $('.uiselect-hidden-accessible').text(ariaResultFoundMessage);
                                    $('.uiselect-hidden-accessible').innerText = ariaResultFoundMessage;
                                } else if (ctrl.items.length === 0 && ctrl.minimumInputLength != "undefined") {
                                    var ariaMinMessageLength = $filter('xei18n')('uiselect.minimum.input.text', ctrl.minimumInputLength);
                                    $('.uiselect-hidden-accessible').text("");
                                    $('.uiselect-hidden-accessible').text(ariaMinMessageLength);
                                    $('.uiselect-hidden-accessible').innerText = ariaMinMessageLength;
                                }
                            });
                        }
                    } else if (ctrl.open && ctrl.multiple) {
                        ctrl.open = false;
                    }
                };

                ctrl.focusSearchInput = function (initSearchValue) {
                    ctrl.search = initSearchValue || ctrl.search;
                    setTimeout(function() { ctrl.searchInput[0].focus(); }, 10);
                };

                ctrl.findGroupByName = function(name) {
                    return ctrl.groups && ctrl.groups.filter(function(group) {
                            return group.name === name;
                        })[0];
                };

                ctrl.parseRepeatAttr = function(attrs, groupByExp, groupFilterExp, $select) {
                    function updateGroups(items) {
                        var groupFn = $scope.$eval(groupByExp);
                        ctrl.groups = [];
                        angular.forEach(items, function(item) {
                            var groupName = angular.isFunction(groupFn) ? groupFn(item) : item[groupFn];
                            var group = ctrl.findGroupByName(groupName);
                            if(group) {
                                group.items.push(item);
                            }
                            else {
                                ctrl.groups.push({name: groupName, items: [item]});
                            }
                        });
                        if(groupFilterExp){
                            var groupFilterFn = $scope.$eval(groupFilterExp);
                            if( angular.isFunction(groupFilterFn)){
                                ctrl.groups = groupFilterFn(ctrl.groups);
                            } else if(angular.isArray(groupFilterFn)){
                                ctrl.groups = _groupsFilter(ctrl.groups, groupFilterFn);
                            }
                        }
                        ctrl.items = [];
                        ctrl.groups.forEach(function(group) {
                            ctrl.items = ctrl.items.concat(group.items);
                        });
                    }

                    function setPlainItems(items) {
                        ctrl.items = items;
                    }

                    ctrl.setItemsFn = groupByExp ? updateGroups : setPlainItems;

                    ctrl.parserResult = RepeatParser.parse(attrs.repeat);

                    ctrl.isGrouped = !!groupByExp;
                    ctrl.itemProperty = ctrl.parserResult.itemName;

                    //If collection is an Object, convert it to Array

                    var originalSource = ctrl.parserResult.source;

                    //When an object is used as source, we better create an array and use it as 'source'
                    var createArrayFromObject = function(){
                        var origSrc = originalSource($scope);
                        $scope.$uisSource = Object.keys(origSrc).map(function(v){
                            var result = {};
                            result[ctrl.parserResult.keyName] = v;
                            result.value = origSrc[v];
                            return result;
                        });
                    };

                    if (ctrl.parserResult.keyName){ // Check for (key,value) syntax
                        createArrayFromObject();
                        ctrl.parserResult.source = $parse('$uisSource' + ctrl.parserResult.filters);
                        $scope.$watch(originalSource, function(newVal, oldVal){
                            if (newVal !== oldVal) createArrayFromObject();
                        }, true);
                    }

                    ctrl.refreshItems = function (data) {
                        data = data || ctrl.parserResult.source($scope);
                        var selectedItems = ctrl.selected;
                        //TODO should implement for single mode removeSelected
                        if (ctrl.isEmpty() || (angular.isArray(selectedItems) && !selectedItems.length) || !ctrl.removeSelected) {
                            ctrl.setItemsFn(data);
                        } else {
                            if (data !== undefined) {
                                var filteredItems = data.filter(function (i) {
                                    return selectedItems.every(function (selectedItem) {
                                        return !angular.equals(i, selectedItem);
                                    });
                                });
                                ctrl.setItemsFn(filteredItems);
                            }
                        }
                        if (ctrl.items !== null && ctrl.items !== undefined && ctrl.items.length !== undefined && ctrl.items.length > 0) {
                            if(ctrl.search != "") {
                                var ariaSearchResult = $filter('xei18n')('uiselect.search.results', ctrl.items.length);
                                $('.uiselect-hidden-accessible').text("");
                                $('.uiselect-hidden-accessible').text(ariaSearchResult);
                                $('.uiselect-hidden-accessible').innerText = ariaSearchResult;
                            }
                        }else if(ctrl.items !== null && ctrl.items !== undefined && ctrl.items.length !== undefined && ctrl.items.length === 0){
                            var ariaNoSearchResult = $filter('xei18n')('uiselect.no.results.found.text');
                            $('.uiselect-hidden-accessible').text("");
                            $('.uiselect-hidden-accessible').text(ariaNoSearchResult);
                            $('.uiselect-hidden-accessible').innerText = ariaNoSearchResult;
                        }
                        if (ctrl.dropdownPosition === 'auto' || ctrl.dropdownPosition === 'up') {
                            $scope.calculateDropdownPos();
                        }
                    };

                    // See https://github.com/angular/angular.js/blob/v1.2.15/src/ng/directive/ngRepeat.js#L259
                    $scope.$watchCollection(ctrl.parserResult.source, function(items) {
                        if (items === undefined || items === null) {
                            // If the user specifies undefined or null => reset the collection
                            // Special case: items can be undefined if the user did not initialized the collection on the scope
                            // i.e $scope.addresses = [] is missing
                            ctrl.items = [];
                        } else {
                            if (!angular.isArray(items)) {
                                throw uiSelectMinErr('items', "Expected an array but got '{0}'.", items);
                            } else {
                                //Remove already selected items (ex: while searching)
                                //TODO Should add a test
                                if (!attrs.minimumInputLength || $select.search.length >= attrs.minimumInputLength) {
                                    ctrl.refreshItems(items);
                                } else {
                                    ctrl.groups = [];
                                    ctrl.items = [];
                                }
                                ctrl.ngModel.$modelValue = null; //Force scope model value and ngModel value to be out of sync to re-run formatters
                            }
                        }
                    });

                };

                var _refreshDelayPromise;

                /**
                 * Typeahead mode: lets the user refresh the collection using his own function.
                 *
                 * See Expose $select.search for external / remote filtering https://github.com/angular-ui/ui-select/pull/31
                 */
                ctrl.refresh = function(refreshAttr) {
                    if (refreshAttr !== undefined) {

                        // Debounce
                        // See https://github.com/angular-ui/bootstrap/blob/0.10.0/src/typeahead/typeahead.js#L155
                        // FYI AngularStrap typeahead does not have debouncing: https://github.com/mgcrea/angular-strap/blob/v2.0.0-rc.4/src/typeahead/typeahead.js#L177
                        if (_refreshDelayPromise) {
                            $timeout.cancel(_refreshDelayPromise);
                        }
                        _refreshDelayPromise = $timeout(function() {
                            $scope.$eval(refreshAttr);
                        }, ctrl.refreshDelay);
                    }
                };

                ctrl.isActive = function(itemScope) {
                    if ( !ctrl.open ) {
                        return false;
                    }
                    var itemIndex = ctrl.items.indexOf(itemScope[ctrl.itemProperty]);
                    var isActive =  itemIndex == ctrl.activeIndex;

                    if ( !isActive || ( itemIndex < 0 && ctrl.taggingLabel !== false ) ||( itemIndex < 0 && ctrl.taggingLabel === false) ) {
                        return false;
                    }

                    if (isActive && !angular.isUndefined(ctrl.onHighlightCallback)) {
                        itemScope.$eval(ctrl.onHighlightCallback);
                    }

                    return isActive;
                };

                ctrl.isDisabled = function(itemScope) {

                    if (!ctrl.open) return;

                    var itemIndex = ctrl.items.indexOf(itemScope[ctrl.itemProperty]);
                    var isDisabled = false;
                    var item;

                    if (itemIndex >= 0 && !angular.isUndefined(ctrl.disableChoiceExpression)) {
                        item = ctrl.items[itemIndex];
                        isDisabled = !!(itemScope.$eval(ctrl.disableChoiceExpression)); // force the boolean value
                        item._uiSelectChoiceDisabled = isDisabled; // store this for later reference
                    }

                    return isDisabled;
                };


                // When the user selects an item with ENTER or clicks the dropdown
                ctrl.select = function(item, skipFocusser, $event) {
                    if (item === undefined || !item._uiSelectChoiceDisabled) {

                        if ( ! ctrl.items && ! ctrl.search && ! ctrl.tagging.isActivated) return;

                        if (!item || !item._uiSelectChoiceDisabled) {
                            if(ctrl.tagging.isActivated) {
                                // if taggingLabel is disabled, we pull from ctrl.search val
                                if ( ctrl.taggingLabel === false ) {
                                    if ( ctrl.activeIndex < 0 ) {
                                        item = ctrl.tagging.fct !== undefined ? ctrl.tagging.fct(ctrl.search) : ctrl.search;
                                        if (!item || angular.equals( ctrl.items[0], item ) ) {
                                            return;
                                        }
                                    } else {
                                        // keyboard nav happened first, user selected from dropdown
                                        item = ctrl.items[ctrl.activeIndex];
                                    }
                                } else {
                                    // tagging always operates at index zero, taggingLabel === false pushes
                                    // the ctrl.search value without having it injected
                                    if ( ctrl.activeIndex === 0 ) {
                                        // ctrl.tagging pushes items to ctrl.items, so we only have empty val
                                        // for `item` if it is a detected duplicate
                                        if ( item === undefined ) return;

                                        // create new item on the fly if we don't already have one;
                                        // use tagging function if we have one
                                        if ( ctrl.tagging.fct !== undefined && typeof item === 'string' ) {
                                            item = ctrl.tagging.fct(item);
                                            if (!item) return;
                                            // if item type is 'string', apply the tagging label
                                        } else if ( typeof item === 'string' ) {
                                            // trim the trailing space
                                            item = item.replace(ctrl.taggingLabel,'').trim();
                                        }
                                    }
                                }
                                // search ctrl.selected for dupes potentially caused by tagging and return early if found
                                if ( ctrl.selected && angular.isArray(ctrl.selected) && ctrl.selected.filter( function (selection) { return angular.equals(selection, item); }).length > 0 ) {
                                    ctrl.close(skipFocusser);
                                    return;
                                }
                            }
                            ctrl.search = "";
                            var ariaOptionSelected;
                            $('.uiselect-choice-status-hidden-accessible').text("");
                            if(undefined != ctrl.selectedValue){
                                ariaOptionSelected = $filter('xei18n')('uiselect.option.selected',item[ctrl.selectedValue]);
                            }else{
                                ariaOptionSelected = $filter('xei18n')('uiselect.option.selected',item.name);
                            }
                            $('.uiselect-choice-status-hidden-accessible').text(ariaOptionSelected);
                            $('.uiselect-choice-status-hidden-accessible').innerText = ariaOptionSelected;

                            $scope.$broadcast('uis:select', item);

                            var locals = {};
                            locals[ctrl.parserResult.itemName] = item;

                            $timeout(function(){
                                ctrl.onSelectCallback($scope, {
                                    $item: item,
                                    $model: ctrl.parserResult.modelMapper($scope, locals)
                                });
                            });

                            //To set focus on current element When user selects an item with clicks the dropdown
                            ctrl.focusSearchInput("");
                            if (ctrl.closeOnSelect) {
                                ctrl.close(skipFocusser);
                            }
                            if ($event && $event.type === 'click') {
                                ctrl.clickTriggeredSelect = true;
                            }
                        }
                    }
                };

                // Closes the dropdown
                ctrl.close = function(skipFocusser) {
                    if (!ctrl.open) return;
                    if (ctrl.ngModel && ctrl.ngModel.$setTouched) ctrl.ngModel.$setTouched();
                    _resetSearchInput();
                    ctrl.open = false;

                    $scope.$broadcast('uis:close', skipFocusser);

                };

                ctrl.setFocus = function(){
                    if (!ctrl.focus) ctrl.focusInput[0].focus();
                };

                ctrl.clear = function($event) {
                    ctrl.select(undefined);
                    $event.stopPropagation();
                    $timeout(function() {
                        ctrl.focusser[0].focus();
                    }, 0, false);
                };

                // Toggle dropdown
                ctrl.toggle = function(e) {
                    if (ctrl.open) {
                        ctrl.close();
                        e.preventDefault();
                        e.stopPropagation();
                    } else {
                        ctrl.activate();
                    }
                };

                ctrl.toggleMultiDropdown = function (e) {

                };

                ctrl.isLocked = function(itemScope, itemIndex) {
                    var isLocked, item = ctrl.selected[itemIndex];

                    if (item && !angular.isUndefined(ctrl.lockChoiceExpression)) {
                        isLocked = !!(itemScope.$eval(ctrl.lockChoiceExpression)); // force the boolean value
                        item._uiSelectChoiceLocked = isLocked; // store this for later reference
                    }

                    return isLocked;
                };

                var sizeWatch = null;
                ctrl.sizeSearchInput = function() {

                    var input = ctrl.searchInput[0],
                        container = ctrl.searchInput.parent().parent()[0],
                        calculateContainerWidth = function() {
                            // Return the container width only if the search input is visible
                            return container.clientWidth * !!input.offsetParent;
                        },
                        updateIfVisible = function(containerWidth) {
                            if (containerWidth === 0) {
                                return false;
                            }
                            var inputWidth = containerWidth - input.offsetLeft - 10;
                            if (inputWidth < 50) inputWidth = containerWidth;
                            ctrl.searchInput.css('width', inputWidth+'px');
                            return true;
                        };

                    ctrl.searchInput.css('width', '10px');
                    $timeout(function() { //Give tags time to render correctly
                        if (sizeWatch === null && !updateIfVisible(calculateContainerWidth())) {
                            sizeWatch = $scope.$watch(calculateContainerWidth, function(containerWidth) {
                                if (updateIfVisible(containerWidth)) {
                                    sizeWatch();
                                    sizeWatch = null;
                                }
                            });
                        }
                    });
                };

                function _handleDropDownSelection(key) {
                    var processed = true;
                    switch (key) {
                        case KEY.DOWN:
                            if (!ctrl.open && ctrl.multiple) ctrl.activate(false, true); //In case its the search input in 'multiple' mode
                            else if (ctrl.activeIndex < ctrl.items.length - 1) { ctrl.activeIndex++; }
                            break;
                        case KEY.UP:
                            if (!ctrl.open && ctrl.multiple) ctrl.activate(false, true); //In case its the search input in 'multiple' mode
                            else if (ctrl.activeIndex > 0 || (ctrl.search.length === 0 && ctrl.tagging.isActivated && ctrl.activeIndex > -1)) { ctrl.activeIndex--; }
                            break;
                        case KEY.TAB:
                            if (!ctrl.multiple || ctrl.open) ctrl.select(ctrl.items[ctrl.activeIndex], true);
                            break;
                        case KEY.ENTER:
                            if(ctrl.items.length == 1) {
                                ctrl.select(ctrl.items[0], ctrl.skipFocusser); // Make sure at least one dropdown item is highlighted before adding if not in tagging mode
                            }
                            else if(ctrl.open && (ctrl.tagging.isActivated || ctrl.activeIndex >= 0)){
                                ctrl.select(ctrl.items[ctrl.activeIndex], ctrl.skipFocusser); // Make sure at least one dropdown item is highlighted before adding if not in tagging mode
                            }
                            else
                            {
                                ctrl.activate(false, true); //In case its the search input in 'multiple' mode
                            }
                            break;
                        case KEY.ESC:
                            ctrl.close();
                            break;
                        default:
                            processed = false;
                    }
                    return processed;
                }

                // Bind to keyboard shortcuts
                ctrl.searchInput.on('keydown', function (e) {

                    var key = e.which;
                    if (~[KEY.ENTER, KEY.ESC].indexOf(key)) {
                        e.preventDefault();
                        e.stopPropagation();
                    }

                    if (~[KEY.ESC , KEY.TAB].indexOf(key)) {
                        ctrl.close();
                    }

                    // if(~[KEY.ESC,KEY.TAB].indexOf(key)){
                    //   //TODO: SEGURO?
                    //   ctrl.close();
                    // }

                    //When the select component searchEnabled is false shouldn't allow user to search ; Hence restricting the user to search here.
                    if (!~[KEY.ENTER, KEY.ESC].indexOf(key) && !KEY.isVerticalMovement(key) && ctrl.searchEnabled === false) {
                        e.preventDefault();
                        e.stopPropagation();
                        return false;
                    }

                    $scope.$apply(function () {

                        var tagged = false;

                        if (ctrl.items.length > 0 || ctrl.tagging.isActivated) {
                            _handleDropDownSelection(key);
                            if (ctrl.taggingTokens.isActivated) {
                                for (var i = 0; i < ctrl.taggingTokens.tokens.length; i++) {
                                    if (ctrl.taggingTokens.tokens[i] === KEY.MAP[e.keyCode]) {
                                        // make sure there is a new value to push via tagging
                                        if (ctrl.search.length > 0) {
                                            tagged = true;
                                        }
                                    }
                                }
                                if (tagged) {
                                    $timeout(function () {
                                        ctrl.searchInput.triggerHandler('tagged');
                                        var newItem = ctrl.search.replace(KEY.MAP[e.keyCode], '').trim();
                                        if (ctrl.tagging.fct) {
                                            newItem = ctrl.tagging.fct(newItem);
                                        }
                                        if (newItem) ctrl.select(newItem, true);
                                    });
                                }
                            }
                        } else if (!ctrl.open && ctrl.multiple) {
                            _handleDropDownSelection(key);
                        }

                    });

                    if (KEY.isVerticalMovement(key) && ctrl.items.length > 0) {
                        _ensureHighlightVisible();
                    }
                    if ($scope.$select.search.length == 0 && key === KEY.SPACE) {
                        e.preventDefault();
                        e.stopPropagation();
                    }
                    if (key === KEY.ENTER || key === KEY.ESC || key === KEY.UP || KEY === KEY.DOWN) {
                        e.preventDefault();
                        e.stopPropagation();
                        //return false;
                    }

                    if (ctrl.activeIndex === -1 || (ctrl.taggingLabel !== false && !KEY.isVerticalMovement(key) )) {
                        ctrl.activeIndex = 0;
                    }

                });

                ctrl.searchInput.on('paste', function (e) {
                    var data;

                    if (window.clipboardData && window.clipboardData.getData) { // IE
                        data = window.clipboardData.getData('Text');
                    } else {
                        data = (e.originalEvent || e).clipboardData.getData('text/plain');
                    }

                    // Prepend the current input field text to the paste buffer.
                    data = ctrl.search + data;

                    if (data && data.length > 0) {
                        // If tagging try to split by tokens and add items
                        if (ctrl.taggingTokens.isActivated) {
                            var separator = KEY.toSeparator(ctrl.taggingTokens.tokens[0]);
                            var items = data.split(separator || ctrl.taggingTokens.tokens[0]); // split by first token only
                            if (items && items.length > 0) {
                                var oldsearch = ctrl.search;
                                angular.forEach(items, function (item) {
                                    var newItem = ctrl.tagging.fct ? ctrl.tagging.fct(item) : item;
                                    if (newItem) {
                                        ctrl.select(newItem, true);
                                    }
                                });
                                ctrl.search = oldsearch || EMPTY_SEARCH;
                                e.preventDefault();
                                e.stopPropagation();
                            }
                        } else if (ctrl.paste) {
                            ctrl.paste(data);
                            ctrl.search = EMPTY_SEARCH;
                            e.preventDefault();
                            e.stopPropagation();
                        }
                    }
                });

                ctrl.searchInput.on('tagged', function() {
                    $timeout(function() {
                        _resetSearchInput();
                    });
                });

                // See https://github.com/ivaynberg/select2/blob/3.4.6/select2.js#L1431
                function _ensureHighlightVisible() {
                    var container = $element.querySelectorAll('.ui-select-choices-content');
                    var choices = container.querySelectorAll('.ui-select-choices-row');
                    if (choices.length < 1) {
                        throw uiSelectMinErr('choices', "Expected multiple .ui-select-choices-row but got '{0}'.", choices.length);
                    }

                    if (ctrl.activeIndex < 0) {
                        return;
                    }

                    var highlighted = choices[ctrl.activeIndex];
                    $('.uiselect-choice-status-hidden-accessible').text("");
                    var optionHighlighted =$(highlighted).find('div:last').text();
                    var ariaOptionSelected = $filter('xei18n')('uiselect.option.selected', optionHighlighted);
                    $('.uiselect-choice-status-hidden-accessible').text(ariaOptionSelected);
                    $('.uiselect-choice-status-hidden-accessible').attr('aria-selected',true);
                    $('.uiselect-choice-status-hidden-accessible').innerText = ariaOptionSelected;
                    var posY = highlighted.offsetTop + highlighted.clientHeight - container[0].scrollTop;
                    var height = container[0].offsetHeight;

                    if (posY > height) {
                        container[0].scrollTop += posY - height;
                    } else if (posY < highlighted.clientHeight) {
                        if (ctrl.isGrouped && ctrl.activeIndex === 0)
                            container[0].scrollTop = 0; //To make group header visible when going all the way up
                        else
                            container[0].scrollTop -= highlighted.clientHeight - posY;
                    }
                }

                $scope.$on('$destroy', function() {
                    ctrl.searchInput.off('keyup keydown tagged blur paste');
                });

                angular.element($window).bind('resize', function() {
                    ctrl.sizeSearchInput();
                });

            }]);

    uis.directive('xeUiSelect',
        ['$document', 'uiSelectConfig', 'uiSelectMinErr', 'uisOffset', '$compile', '$parse', '$timeout',
            function($document, uiSelectConfig, uiSelectMinErr, uisOffset, $compile, $parse, $timeout) {

                return {
                    restrict: 'EA',
                    templateUrl: function(tElement, tAttrs) {
                        var theme = tAttrs.theme || uiSelectConfig.theme;
                        return theme + (angular.isDefined(tAttrs.multiple) ? '/select-multiple.tpl.html' : '/select.tpl.html');
                    },
                    replace: true,
                    transclude: true,
                    require: ['xeUiSelect', '^ngModel'],
                    scope: true,

                    controller: 'uiSelectCtrl',
                    controllerAs: '$select',
                    compile: function(tElement, tAttrs) {

                        // Allow setting ngClass on uiSelect
                        var match = /{(.*)}\s*{(.*)}/.exec(tAttrs.ngClass);
                        if(match) {
                            var combined = '{'+ match[1] +', '+ match[2] +'}';
                            tAttrs.ngClass = combined;
                            tElement.attr('ng-class', combined);
                        }

                        //Multiple or Single depending if multiple attribute presence
                        if (angular.isDefined(tAttrs.multiple))
                            tElement.append('<ui-select-multiple/>').removeAttr('multiple');
                        else
                            tElement.append('<ui-select-single/>');

                        if (tAttrs.inputId)
                            tElement.querySelectorAll('input.ui-select-search')[0].id = tAttrs.inputId;

                        return function(scope, element, attrs, ctrls, transcludeFn) {

                            var $select = ctrls[0];
                            var ngModel = ctrls[1];
                            $select.selectedValue = attrs.textSelected;
                            $select.generatedId = uiSelectConfig.generateId();
                            $select.baseTitle = attrs.title || 'Select box';
                            $select.focusserId = 'focusser-' + $select.generatedId;

                            if(attrs.searchEnabled == "false"){
                                $select.focusserTitle = $select.baseTitle + ' focus, Single Select Search Unavailable.';

                            }else{
                                $select.focusserTitle = $select.baseTitle + ' focus';
                            }

                            $select.closeOnSelect = function() {
                                if (angular.isDefined(attrs.closeOnSelect)) {
                                    return $parse(attrs.closeOnSelect)();
                                } else {
                                    return uiSelectConfig.closeOnSelect;
                                }
                            }();

                            scope.$watch('skipFocusser', function() {
                                var skipFocusser = scope.$eval(attrs.skipFocusser);
                                $select.skipFocusser = skipFocusser !== undefined ? skipFocusser : uiSelectConfig.skipFocusser;
                            });

                            $select.onSelectCallback = $parse(attrs.onSelect);
                            $select.onRemoveCallback = $parse(attrs.onRemove);

                            //Limit the number of selections allowed
                            $select.limit = (angular.isDefined(attrs.limit)) ? parseInt(attrs.limit, 10) : undefined;

                            //Set reference to ngModel from uiSelectCtrl
                            $select.ngModel = ngModel;

                            $select.choiceGrouped = function(group){
                                return $select.isGrouped && group && group.name;
                            };

                            if(attrs.tabindex){
                                attrs.$observe('tabindex', function(value) {
                                    $select.focusInput.attr('tabindex', value);
                                    element.removeAttr('tabindex');
                                });
                            }

                            scope.$watch('searchEnabled', function() {
                                var searchEnabled = scope.$eval(attrs.searchEnabled);
                                $select.searchEnabled = searchEnabled !== undefined ? searchEnabled : uiSelectConfig.searchEnabled;
                            });

                            scope.$watch('sortable', function() {
                                var sortable = scope.$eval(attrs.sortable);
                                $select.sortable = sortable !== undefined ? sortable : uiSelectConfig.sortable;
                            });

                            attrs.$observe('disabled', function() {
                                // No need to use $eval() (thanks to ng-disabled) since we already get a boolean instead of a string
                                $select.disabled = attrs.disabled !== undefined ? attrs.disabled : false;
                            });

                            attrs.$observe('resetSearchInput', function() {
                                // $eval() is needed otherwise we get a string instead of a boolean
                                var resetSearchInput = scope.$eval(attrs.resetSearchInput);
                                $select.resetSearchInput = resetSearchInput !== undefined ? resetSearchInput : true;
                            });

                            attrs.$observe('paste', function() {
                                $select.paste = scope.$eval(attrs.paste);
                            });

                            attrs.$observe('tagging', function() {
                                if(attrs.tagging !== undefined)
                                {
                                    // $eval() is needed otherwise we get a string instead of a boolean
                                    var taggingEval = scope.$eval(attrs.tagging);
                                    $select.tagging = {isActivated: true, fct: taggingEval !== true ? taggingEval : undefined};
                                }
                                else
                                {
                                    $select.tagging = {isActivated: false, fct: undefined};
                                }
                            });

                            attrs.$observe('taggingLabel', function() {
                                if(attrs.tagging !== undefined )
                                {
                                    // check eval for FALSE, in this case, we disable the labels
                                    // associated with tagging
                                    if ( attrs.taggingLabel === 'false' ) {
                                        $select.taggingLabel = false;
                                    }
                                    else
                                    {
                                        $select.taggingLabel = attrs.taggingLabel !== undefined ? attrs.taggingLabel : '(new)';
                                    }
                                }
                            });

                            attrs.$observe('taggingTokens', function() {
                                if (attrs.tagging !== undefined) {
                                    var tokens = attrs.taggingTokens !== undefined ? attrs.taggingTokens.split('|') : [',','ENTER'];
                                    $select.taggingTokens = {isActivated: true, tokens: tokens };
                                }
                            });

                            //Automatically gets focus when loaded
                            if (angular.isDefined(attrs.autofocus)){
                                $timeout(function(){
                                    $select.setFocus();
                                });
                            }

                            //Gets focus based on scope event name (e.g. focus-on='SomeEventName')
                            if (angular.isDefined(attrs.focusOn)){
                                scope.$on(attrs.focusOn, function() {
                                    $timeout(function(){
                                        $select.setFocus();
                                    });
                                });
                            }

                            function onDocumentClick(e) {
                                if (!$select.open) return; //Skip it if dropdown is close

                                var contains = false;

                                if (window.jQuery) {
                                    // Firefox 3.6 does not support element.contains()
                                    // See Node.contains https://developer.mozilla.org/en-US/docs/Web/API/Node.contains
                                    contains = window.jQuery.contains(element[0], e.target);
                                } else {
                                    contains = element[0].contains(e.target);
                                }

                                if (!contains && !$select.clickTriggeredSelect) {
                                    var skipFocusser;
                                    if (!$select.skipFocusser) {
                                        //Will lose focus only with certain targets
                                        var focusableControls = ['input','button','textarea','select'];
                                        var targetController = angular.element(e.target).controller('xeUiSelect'); //To check if target is other ui-select
                                        skipFocusser = targetController && targetController !== $select; //To check if target is other ui-select
                                        if (!skipFocusser) skipFocusser =  ~focusableControls.indexOf(e.target.tagName.toLowerCase()); //Check if target is input, button or textarea
                                    } else {
                                        skipFocusser = true;
                                    }
                                    $select.close(skipFocusser);
                                    scope.$digest();
                                }
                                $select.clickTriggeredSelect = false;
                            }

                            // See Click everywhere but here event http://stackoverflow.com/questions/12931369
                            $document.on('click', onDocumentClick);

                            scope.$on('$destroy', function() {
                                $document.off('click', onDocumentClick);
                            });

                            // Move transcluded elements to their correct position in main template
                            transcludeFn(scope, function(clone) {
                                // See Transclude in AngularJS http://blog.omkarpatil.com/2012/11/transclude-in-angularjs.html

                                // One day jqLite will be replaced by jQuery and we will be able to write:
                                // var transcludedElement = clone.filter('.my-class')
                                // instead of creating a hackish DOM element:
                                var transcluded = angular.element('<div>').append(clone);

                                var transcludedMatch = transcluded.querySelectorAll('.ui-select-match');
                                transcludedMatch.removeAttr('ui-select-match'); //To avoid loop in case directive as attr
                                transcludedMatch.removeAttr('data-ui-select-match'); // Properly handle HTML5 data-attributes
                                if (transcludedMatch.length !== 1) {
                                    throw uiSelectMinErr('transcluded', "Expected 1 .ui-select-match but got '{0}'.", transcludedMatch.length);
                                }
                                element.querySelectorAll('.ui-select-match').replaceWith(transcludedMatch);

                                var transcludedChoices = transcluded.querySelectorAll('.ui-select-choices');
                                transcludedChoices.removeAttr('ui-select-choices'); //To avoid loop in case directive as attr
                                transcludedChoices.removeAttr('data-ui-select-choices'); // Properly handle HTML5 data-attributes
                                if (transcludedChoices.length !== 1) {
                                    throw uiSelectMinErr('transcluded', "Expected 1 .ui-select-choices but got '{0}'.", transcludedChoices.length);
                                }
                                element.querySelectorAll('.ui-select-choices').replaceWith(transcludedChoices);
                            });

                            // Support for appending the select field to the body when its open
                            var appendToBody = scope.$eval(attrs.appendToBody);
                            if (appendToBody !== undefined ? appendToBody : uiSelectConfig.appendToBody) {
                                scope.$watch('$select.open', function(isOpen) {
                                    if (isOpen) {
                                        positionDropdown();
                                    } else {
                                        resetDropdown();
                                    }
                                });

                                // Move the dropdown back to its original location when the scope is destroyed. Otherwise
                                // it might stick around when the user routes away or the select field is otherwise removed
                                scope.$on('$destroy', function() {
                                    resetDropdown();
                                });
                            }

                            // Hold on to a reference to the .ui-select-container element for appendToBody support
                            var placeholder = null,
                                originalWidth = '';

                            function positionDropdown() {
                                // Remember the absolute position of the element
                                var offset = uisOffset(element);

                                // Clone the element into a placeholder element to take its original place in the DOM
                                placeholder = angular.element('<div class="ui-select-placeholder"></div>');
                                placeholder[0].style.width = offset.width + 'px';
                                placeholder[0].style.height = offset.height + 'px';
                                element.after(placeholder);

                                // Remember the original value of the element width inline style, so it can be restored
                                // when the dropdown is closed
                                originalWidth = element[0].style.width;

                                // Now move the actual dropdown element to the end of the body
                                $document.find('body').append(element);

                                element[0].style.position = 'absolute';
                                element[0].style.left = offset.left + 'px';
                                element[0].style.top = offset.top + 'px';
                                element[0].style.width = offset.width + 'px';
                            }

                            function resetDropdown() {
                                if (placeholder === null) {
                                    // The dropdown has not actually been display yet, so there's nothing to reset
                                    return;
                                }

                                // Move the dropdown element back to its original location in the DOM
                                placeholder.replaceWith(element);
                                placeholder = null;

                                element[0].style.position = '';
                                element[0].style.left = '';
                                element[0].style.top = '';
                                element[0].style.width = originalWidth;

                                // Set focus back on to the moved element
                                $select.setFocus();
                            }

                            // Hold on to a reference to the .ui-select-dropdown element for direction support.
                            var dropdown = null,
                                directionUpClassName = 'direction-up';

                            // Support changing the direction of the dropdown if there isn't enough space to render it.
                            scope.$watch('$select.open', function() {

                                if ($select.dropdownPosition === 'auto' || $select.dropdownPosition === 'up'){
                                    scope.calculateDropdownPos();
                                }

                            });

                            var setDropdownPosUp = function(offset, offsetDropdown){

                                offset = offset || uisOffset(element);
                                offsetDropdown = offsetDropdown || uisOffset(dropdown);

                                dropdown[0].style.position = 'absolute';
                                dropdown[0].style.top = (offsetDropdown.height * -1) + 'px';
                                element.addClass(directionUpClassName);

                            };

                            var setDropdownPosDown = function(offset, offsetDropdown){

                                element.removeClass(directionUpClassName);

                                offset = offset || uisOffset(element);
                                offsetDropdown = offsetDropdown || uisOffset(dropdown);

                                dropdown[0].style.position = '';
                                dropdown[0].style.top = '';

                            };

                            scope.calculateDropdownPos = function(){

                                if ($select.open) {
                                    dropdown = angular.element(element).querySelectorAll('.ui-select-dropdown');
                                    if (dropdown.length === 0) {
                                        return;
                                    }

                                    // Hide the dropdown so there is no flicker until $timeout is done executing.
                                    dropdown[0].style.opacity = 0;

                                    // Delay positioning the dropdown until all choices have been added so its height is correct.
                                    $timeout(function(){

                                        if ($select.dropdownPosition === 'up'){
                                            //Go UP
                                            setDropdownPosUp();

                                        }else{ //AUTO

                                            element.removeClass(directionUpClassName);

                                            var offset = uisOffset(element);
                                            var offsetDropdown = uisOffset(dropdown);

                                            //https://code.google.com/p/chromium/issues/detail?id=342307#c4
                                            var scrollTop = $document[0].documentElement.scrollTop || $document[0].body.scrollTop; //To make it cross browser (blink, webkit, IE, Firefox).

                                            // Determine if the direction of the dropdown needs to be changed.
                                            if (offset.top + offset.height + offsetDropdown.height > scrollTop + $document[0].documentElement.clientHeight) {
                                                //Go UP
                                                setDropdownPosUp(offset, offsetDropdown);
                                            }else{
                                                //Go DOWN
                                                setDropdownPosDown(offset, offsetDropdown);
                                            }

                                        }

                                        // Display the dropdown once it has been positioned.
                                        dropdown[0].style.opacity = 1;
                                    });
                                } else {
                                    if (dropdown === null || dropdown.length === 0) {
                                        return;
                                    }

                                    // Reset the position of the dropdown.
                                    dropdown[0].style.position = '';
                                    dropdown[0].style.top = '';
                                    element.removeClass(directionUpClassName);
                                }
                            };
                        };
                    }
                };
            }]);

    uis.directive('xeUiSelectMatch', ['uiSelectConfig', function(uiSelectConfig) {
        return {
            restrict: 'EA',
            require: '^xeUiSelect',
            replace: true,
            transclude: true,
            templateUrl: function(tElement) {
                // Needed so the uiSelect can detect the transcluded content
                tElement.addClass('ui-select-match');

                // Gets theme attribute from parent (ui-select)
                var theme = tElement.parent().attr('theme') || uiSelectConfig.theme;
                var multi = tElement.parent().attr('multiple');
                return theme + (multi ? '/match-multiple.tpl.html' : '/match.tpl.html');
            },
            link: function(scope, element, attrs, $select) {
                $select.lockChoiceExpression = attrs.uiLockChoice;
                attrs.$observe('placeholder', function(placeholder) {
                    $select.placeholder = placeholder !== undefined ? placeholder : uiSelectConfig.placeholder;
                });

                function setAllowClear(allow) {
                    $select.allowClear = (angular.isDefined(allow)) ? (allow === '') ? true : (allow.toLowerCase() === 'true') : false;
                }

                attrs.$observe('allowClear', setAllowClear);
                setAllowClear(attrs.allowClear);

                if($select.multiple){
                    $select.sizeSearchInput();
                }

            }
        };
    }]);

    uis.directive('uiSelectMultiple', ['uiSelectMinErr','$timeout', function(uiSelectMinErr, $timeout) {
        return {
            restrict: 'EA',
            require: ['^xeUiSelect', '^ngModel'],

            controller: ['$scope','$timeout', function($scope, $timeout){

                var ctrl = this,
                    $select = $scope.$select,
                    ngModel;

                if (angular.isUndefined($select.selected))
                    $select.selected = [];

                //Wait for link fn to inject it
                $scope.$evalAsync(function(){ ngModel = $scope.ngModel; });

                ctrl.activeMatchIndex = -1;

                ctrl.updateModel = function(){
                    ngModel.$setViewValue(Date.now()); //Set timestamp as a unique string to force changes
                    ctrl.refreshComponent();
                };

                ctrl.refreshComponent = function(){
                    //Remove already selected items
                    //e.g. When user clicks on a selection, the selected array changes and
                    //the dropdown should remove that item
                    $select.refreshItems();
                    $select.sizeSearchInput();
                };

                // Remove item from multiple select
                ctrl.removeChoice = function(index){

                    var removedChoice = $select.selected[index];

                    // if the choice is locked, can't remove it
                    if(removedChoice._uiSelectChoiceLocked) return;

                    var locals = {};
                    locals[$select.parserResult.itemName] = removedChoice;

                    $select.selected.splice(index, 1);
                    ctrl.activeMatchIndex = -1;
                    $select.sizeSearchInput();

                    // Give some time for scope propagation.
                    $timeout(function(){
                        $select.onRemoveCallback($scope, {
                            $item: removedChoice,
                            $model: $select.parserResult.modelMapper($scope, locals)
                        });
                    });

                    ctrl.updateModel();
                    $select.setFocus();
                };

                ctrl.getPlaceholder = function(){
                    //Refactor single?
                    if($select.selected && $select.selected.length) return;
                    return $select.placeholder;
                };


            }],
            controllerAs: '$selectMultiple',

            link: function(scope, element, attrs, ctrls) {

                var $select = ctrls[0];
                var ngModel = scope.ngModel = ctrls[1];
                var $selectMultiple = scope.$selectMultiple;

                //$select.selected = raw selected objects (ignoring any property binding)

                $select.multiple = true;
                $select.removeSelected = true;

                //Input that will handle focus
                $select.focusInput = $select.searchInput;

                //Properly check for empty if set to multiple
                ngModel.$isEmpty = function(value) {
                    return !value || value.length === 0;
                };

                //From view --> model
                ngModel.$parsers.unshift(function () {
                    var locals = {},
                        result,
                        resultMultiple = [];
                    for (var j = $select.selected.length - 1; j >= 0; j--) {
                        locals = {};
                        locals[$select.parserResult.itemName] = $select.selected[j];
                        result = $select.parserResult.modelMapper(scope, locals);
                        resultMultiple.unshift(result);
                    }
                    return resultMultiple;
                });

                // From model --> view
                ngModel.$formatters.unshift(function (inputValue) {
                    var data = $select.parserResult.source (scope, { $select : {search:''}}), //Overwrite $search
                        locals = {},
                        result;
                    if (!data) return inputValue;
                    var resultMultiple = [];
                    var checkFnMultiple = function(list, value){
                        if (!list || !list.length) return;
                        for (var p = list.length - 1; p >= 0; p--) {
                            locals[$select.parserResult.itemName] = list[p];
                            result = $select.parserResult.modelMapper(scope, locals);
                            if($select.parserResult.trackByExp){
                                var propsItemNameMatches = /(\w*)\./.exec($select.parserResult.trackByExp);
                                var matches = /\.([^\s]+)/.exec($select.parserResult.trackByExp);
                                if(propsItemNameMatches && propsItemNameMatches.length > 0 && propsItemNameMatches[1] == $select.parserResult.itemName){
                                    if(matches && matches.length>0 && result[matches[1]] == value[matches[1]]){
                                        resultMultiple.unshift(list[p]);
                                        return true;
                                    }
                                }
                            }
                            if (angular.equals(result,value)){
                                resultMultiple.unshift(list[p]);
                                return true;
                            }
                        }
                        return false;
                    };
                    if (!inputValue) return resultMultiple; //If ngModel was undefined
                    for (var k = inputValue.length - 1; k >= 0; k--) {
                        //Check model array of currently selected items
                        if (!checkFnMultiple($select.selected, inputValue[k])){
                            //Check model array of all items available
                            if (!checkFnMultiple(data, inputValue[k])){
                                //If not found on previous lists, just add it directly to resultMultiple
                                resultMultiple.unshift(inputValue[k]);
                            }
                        }
                    }
                    return resultMultiple;
                });

                //Watch for external model changes
                scope.$watchCollection(function(){ return ngModel.$modelValue; }, function(newValue, oldValue) {
                    if (oldValue != newValue){
                        ngModel.$modelValue = null; //Force scope model value and ngModel value to be out of sync to re-run formatters
                        $selectMultiple.refreshComponent();
                    }
                });

                ngModel.$render = function() {
                    // Make sure that model value is array
                    if(!angular.isArray(ngModel.$viewValue)){
                        // Have tolerance for null or undefined values
                        if(angular.isUndefined(ngModel.$viewValue) || ngModel.$viewValue === null){
                            $select.selected = [];
                        } else {
                            throw uiSelectMinErr('multiarr', "Expected model value to be array but got '{0}'", ngModel.$viewValue);
                        }
                    }
                    $select.selected = ngModel.$viewValue;
                    $selectMultiple.refreshComponent();
                    scope.$evalAsync(); //To force $digest
                };

                scope.$on('uis:select', function (event, item) {
                    if($select.selected.length >= $select.limit) {
                        return;
                    }
                    $select.selected.push(item);
                    $selectMultiple.updateModel();
                });

                scope.$on('uis:activate', function () {
                    $selectMultiple.activeMatchIndex = -1;
                });

                scope.$watch('$select.disabled', function(newValue, oldValue) {
                    // As the search input field may now become visible, it may be necessary to recompute its size
                    if (oldValue && !newValue) $select.sizeSearchInput();
                });

                $select.searchInput.on('keydown', function(e) {
                    var key = e.which;
                    scope.$apply(function() {
                        var processed = false;
                        // var tagged = false; //Checkme
                        if(KEY.isHorizontalMovement(key)){
                            processed = _handleMatchSelection(key);
                        }
                        if (processed  && key != KEY.TAB) {
                            //TODO Check si el tab selecciona aun correctamente
                            //Crear test
                            e.preventDefault();
                            e.stopPropagation();
                        }
                    });
                });
                function _getCaretPosition(el) {
                    if(angular.isNumber(el.selectionStart)) return el.selectionStart;
                    // selectionStart is not supported in IE8 and we don't want hacky workarounds so we compromise
                    else return el.value.length;
                }
                // Handles selected options in "multiple" mode
                function _handleMatchSelection(key){
                    var caretPosition = _getCaretPosition($select.searchInput[0]),
                        length = $select.selected.length,
                    // none  = -1,
                        first = 0,
                        last  = length-1,
                        curr  = $selectMultiple.activeMatchIndex,
                        next  = $selectMultiple.activeMatchIndex+1,
                        prev  = $selectMultiple.activeMatchIndex-1,
                        newIndex = curr;

                    if(caretPosition > 0 || ($select.search.length && key == KEY.RIGHT)) return false;

                    $select.close();

                    function getNewActiveMatchIndex(){
                        switch(key){
                            case KEY.LEFT:
                                // Select previous/first item
                                if(~$selectMultiple.activeMatchIndex){
                                    var precedingItem=$select.selected[prev].name;
                                    $('.uiselect-choice-status-hidden-accessible').text(precedingItem);
                                    return prev;
                                }
                                // Select last item
                                else {
                                    var precedingItem=$select.selected[last].name;
                                    $('.uiselect-choice-status-hidden-accessible').text(precedingItem);
                                    return last;
                                }
                                break;
                            case KEY.RIGHT:
                                // Open drop-down
                                if(!~$selectMultiple.activeMatchIndex || curr === last){
                                    $select.activate();
                                    return false;
                                }
                                // Select next/last item
                                else {
                                    var succedingItem = $select.selected[next].name
                                    $('.uiselect-choice-status-hidden-accessible').text(succedingItem);
                                    return next;
                                }
                                break;
                            case KEY.BACKSPACE:
                                // Remove selected item and select previous/first
                                if(~$selectMultiple.activeMatchIndex){
                                    $selectMultiple.removeChoice(curr);
                                    return prev;
                                }
                                // Select last item
                                else return last;
                                break;
                            case KEY.DELETE:
                                // Remove selected item and select next item
                                if(~$selectMultiple.activeMatchIndex){
                                    $selectMultiple.removeChoice($selectMultiple.activeMatchIndex);
                                    return curr;
                                }
                                else return false;
                        }
                    }

                    newIndex = getNewActiveMatchIndex();

                    if(!$select.selected.length || newIndex === false) $selectMultiple.activeMatchIndex = -1;
                    else $selectMultiple.activeMatchIndex = Math.min(last,Math.max(first,newIndex));

                    return true;
                }

                $select.searchInput.on('keyup', function(e) {

                    if ( ! KEY.isVerticalMovement(e.which) ) {
                        scope.$evalAsync( function () {
                            $select.activeIndex = $select.taggingLabel === false ? -1 : 0;
                        });
                    }
                    // Push a "create new" item into array if there is a search string
                    if ( $select.tagging.isActivated && $select.search.length > 0 ) {

                        // return early with these keys
                        if (e.which === KEY.TAB || KEY.isControl(e) || KEY.isFunctionKey(e) || e.which === KEY.ESC || KEY.isVerticalMovement(e.which) ) {
                            return;
                        }
                        // always reset the activeIndex to the first item when tagging
                        $select.activeIndex = $select.taggingLabel === false ? -1 : 0;
                        // taggingLabel === false bypasses all of this
                        if ($select.taggingLabel === false) return;

                        var items = angular.copy( $select.items );
                        var stashArr = angular.copy( $select.items );
                        var newItem;
                        var item;
                        var hasTag = false;
                        var dupeIndex = -1;
                        var tagItems;
                        var tagItem;

                        // case for object tagging via transform `$select.tagging.fct` function
                        if ( $select.tagging.fct !== undefined) {
                            tagItems = $select.$filter('filter')(items,{'isTag': true});
                            if ( tagItems.length > 0 ) {
                                tagItem = tagItems[0];
                            }
                            // remove the first element, if it has the `isTag` prop we generate a new one with each keyup, shaving the previous
                            if ( items.length > 0 && tagItem ) {
                                hasTag = true;
                                items = items.slice(1,items.length);
                                stashArr = stashArr.slice(1,stashArr.length);
                            }
                            newItem = $select.tagging.fct($select.search);
                            // verify the new tag doesn't match the value of a possible selection choice or an already selected item.
                            if (
                                stashArr.some(function (origItem) {
                                    return angular.equals(origItem, $select.tagging.fct($select.search));
                                }) ||
                                $select.selected.some(function (origItem) {
                                    return angular.equals(origItem, newItem);
                                })
                            ) {
                                scope.$evalAsync(function () {
                                    $select.activeIndex = 0;
                                    $select.items = items;
                                });
                                return;
                            }
                            newItem.isTag = true;
                            // handle newItem string and stripping dupes in tagging string context
                        } else {
                            // find any tagging items already in the $select.items array and store them
                            tagItems = $select.$filter('filter')(items,function (item) {
                                return item.match($select.taggingLabel);
                            });
                            if ( tagItems.length > 0 ) {
                                tagItem = tagItems[0];
                            }
                            item = items[0];
                            // remove existing tag item if found (should only ever be one tag item)
                            if ( item !== undefined && items.length > 0 && tagItem ) {
                                hasTag = true;
                                items = items.slice(1,items.length);
                                stashArr = stashArr.slice(1,stashArr.length);
                            }
                            newItem = $select.search+' '+$select.taggingLabel;
                            if ( _findApproxDupe($select.selected, $select.search) > -1 ) {
                                return;
                            }
                            // verify the the tag doesn't match the value of an existing item from
                            // the searched data set or the items already selected
                            if ( _findCaseInsensitiveDupe(stashArr.concat($select.selected)) ) {
                                // if there is a tag from prev iteration, strip it / queue the change
                                // and return early
                                if ( hasTag ) {
                                    items = stashArr;
                                    scope.$evalAsync( function () {
                                        $select.activeIndex = 0;
                                        $select.items = items;
                                    });
                                }
                                return;
                            }
                            if ( _findCaseInsensitiveDupe(stashArr) ) {
                                // if there is a tag from prev iteration, strip it
                                if ( hasTag ) {
                                    $select.items = stashArr.slice(1,stashArr.length);
                                }
                                return;
                            }
                        }
                        if ( hasTag ) dupeIndex = _findApproxDupe($select.selected, newItem);
                        // dupe found, shave the first item
                        if ( dupeIndex > -1 ) {
                            items = items.slice(dupeIndex+1,items.length-1);
                        } else {
                            items = [];
                            items.push(newItem);
                            items = items.concat(stashArr);
                        }
                        scope.$evalAsync( function () {
                            $select.activeIndex = 0;
                            $select.items = items;
                        });
                    }
                });
                function _findCaseInsensitiveDupe(arr) {
                    if ( arr === undefined || $select.search === undefined ) {
                        return false;
                    }
                    var hasDupe = arr.filter( function (origItem) {
                            if ( $select.search.toUpperCase() === undefined || origItem === undefined ) {
                                return false;
                            }
                            return origItem.toUpperCase() === $select.search.toUpperCase();
                        }).length > 0;

                    return hasDupe;
                }
                function _findApproxDupe(haystack, needle) {
                    var dupeIndex = -1;
                    if(angular.isArray(haystack)) {
                        var tempArr = angular.copy(haystack);
                        for (var i = 0; i <tempArr.length; i++) {
                            // handle the simple string version of tagging
                            if ( $select.tagging.fct === undefined ) {
                                // search the array for the match
                                if ( tempArr[i]+' '+$select.taggingLabel === needle ) {
                                    dupeIndex = i;
                                }
                                // handle the object tagging implementation
                            } else {
                                var mockObj = tempArr[i];
                                if (angular.isObject(mockObj)) {
                                    mockObj.isTag = true;
                                }
                                if ( angular.equals(mockObj, needle) ) {
                                    dupeIndex = i;
                                }
                            }
                        }
                    }
                    return dupeIndex;
                }

                $select.searchInput.on('blur', function() {
                    $timeout(function() {
                        $selectMultiple.activeMatchIndex = -1;
                    });
                });

            }
        };
    }]);

    uis.directive('uiSelectSingle', ['$timeout','$compile', function($timeout, $compile) {
        return {
            restrict: 'EA',
            require: ['^xeUiSelect', '^ngModel'],
            link: function(scope, element, attrs, ctrls) {

                var $select = ctrls[0];
                var ngModel = ctrls[1];

                //From view --> model
                ngModel.$parsers.unshift(function (inputValue) {
                    var locals = {},
                        result;
                    locals[$select.parserResult.itemName] = inputValue;
                    result = $select.parserResult.modelMapper(scope, locals);
                    return result;
                });

                //From model --> view
                ngModel.$formatters.unshift(function (inputValue) {
                    var data = $select.parserResult.source (scope, { $select : {search:''}}), //Overwrite $search
                        locals = {},
                        result;
                    if (data){
                        var checkFnSingle = function(d){
                            locals[$select.parserResult.itemName] = d;
                            result = $select.parserResult.modelMapper(scope, locals);
                            return result == inputValue;
                        };
                        //If possible pass same object stored in $select.selected
                        if ($select.selected && checkFnSingle($select.selected)) {
                            return $select.selected;
                        }
                        for (var i = data.length - 1; i >= 0; i--) {
                            if (checkFnSingle(data[i])) return data[i];
                        }
                    }
                    return inputValue;
                });

                //Update viewValue if model change
                scope.$watch('$select.selected', function(newValue) {
                    if (ngModel.$viewValue !== newValue) {
                        ngModel.$setViewValue(newValue);
                    }
                });

                ngModel.$render = function() {
                    $select.selected = ngModel.$viewValue;
                };

                scope.$on('uis:select', function (event, item) {
                    $select.selected = item;
                });

                scope.$on('uis:close', function (event, skipFocusser) {
                    $timeout(function(){
                        $select.focusser.prop('disabled', false);
                        if (!skipFocusser) $select.focusser[0].focus();
                    },0,false);
                });

                scope.$on('uis:activate', function () {
                    focusser.prop('disabled', true); //Will reactivate it on .close()
                });

                //Idea from: https://github.com/ivaynberg/select2/blob/79b5bf6db918d7560bdd959109b7bcfb47edaf43/select2.js#L1954
                var focusser = angular.element("<input ng-disabled='$select.disabled' class='ui-select-focusser ui-select-offscreen' type='text' id='{{ $select.focusserId }}' aria-label='{{ $select.focusserTitle }}' aria-haspopup='true' role='button'/>");

                $compile(focusser)(scope);
                $select.focusser = focusser;

                //Input that will handle focus
                $select.focusInput = focusser;

                element.parent().append(focusser);
                focusser.bind("focus", function(){
                    scope.$evalAsync(function(){
                        $select.focus = true;
                    });
                });

                focusser.bind("blur", function(){
                    scope.$evalAsync(function(){
                        $select.focus = false;
                    });
                });

                focusser.bind("keydown", function (e) {

                    if (e.which === KEY.BACKSPACE) {
                        e.preventDefault();
                        e.stopPropagation();
                        $select.select(undefined);
                        scope.$apply();
                        return;
                    }

                    if (e.which === KEY.TAB || KEY.isControl(e) || KEY.isFunctionKey(e) || e.which === KEY.ESC) {
                        return;
                    }

                    if (e.which == KEY.DOWN || e.which == KEY.UP || e.which == KEY.ENTER || e.which == KEY.SPACE) {
                        e.preventDefault();
                        e.stopPropagation();
                        $select.activate();
                        return false;
                    }

                    scope.$digest();
                });

                focusser.bind("keyup input", function(e){

                    if (e.which === KEY.TAB || KEY.isControl(e) || KEY.isFunctionKey(e) || e.which === KEY.ESC || e.which == KEY.ENTER || e.which === KEY.BACKSPACE) {
                        return;
                    }

                    $select.activate(focusser.val()); //User pressed some regular key, so we pass it to the search input
                    focusser.val('');
                    scope.$digest();

                });


            }
        };
    }]);
// Make multiple matches sortable
    uis.directive('uiSelectSort', ['$timeout', 'uiSelectConfig', 'uiSelectMinErr', function($timeout, uiSelectConfig, uiSelectMinErr) {
        return {
            require: '^xeUiSelect',
            link: function(scope, element, attrs, $select) {
                if (scope[attrs.uiSelectSort] === null) {
                    throw uiSelectMinErr('sort', 'Expected a list to sort');
                }

                var options = angular.extend({
                        axis: 'horizontal'
                    },
                    scope.$eval(attrs.uiSelectSortOptions));

                var axis = options.axis;
                var draggingClassName = 'dragging';
                var droppingClassName = 'dropping';
                var droppingBeforeClassName = 'dropping-before';
                var droppingAfterClassName = 'dropping-after';

                scope.$watch(function(){
                    return $select.sortable;
                }, function(newValue){
                    if (newValue) {
                        element.attr('draggable', true);
                    } else {
                        element.removeAttr('draggable');
                    }
                });

                element.on('dragstart', function(event) {
                    element.addClass(draggingClassName);

                    (event.dataTransfer || event.originalEvent.dataTransfer).setData('text', scope.$index.toString());
                });

                element.on('dragend', function() {
                    element.removeClass(draggingClassName);
                });

                var move = function(from, to) {
                    /*jshint validthis: true */
                    this.splice(to, 0, this.splice(from, 1)[0]);
                };

                var dragOverHandler = function(event) {
                    event.preventDefault();

                    var offset = axis === 'vertical' ? event.offsetY || event.layerY || (event.originalEvent ? event.originalEvent.offsetY : 0) : event.offsetX || event.layerX || (event.originalEvent ? event.originalEvent.offsetX : 0);

                    if (offset < (this[axis === 'vertical' ? 'offsetHeight' : 'offsetWidth'] / 2)) {
                        element.removeClass(droppingAfterClassName);
                        element.addClass(droppingBeforeClassName);

                    } else {
                        element.removeClass(droppingBeforeClassName);
                        element.addClass(droppingAfterClassName);
                    }
                };

                var dropTimeout;

                var dropHandler = function(event) {
                    event.preventDefault();

                    var droppedItemIndex = parseInt((event.dataTransfer || event.originalEvent.dataTransfer).getData('text'), 10);

                    // prevent event firing multiple times in firefox
                    $timeout.cancel(dropTimeout);
                    dropTimeout = $timeout(function() {
                        _dropHandler(droppedItemIndex);
                    }, 20);
                };

                var _dropHandler = function(droppedItemIndex) {
                    var theList = scope.$eval(attrs.uiSelectSort);
                    var itemToMove = theList[droppedItemIndex];
                    var newIndex = null;

                    if (element.hasClass(droppingBeforeClassName)) {
                        if (droppedItemIndex < scope.$index) {
                            newIndex = scope.$index - 1;
                        } else {
                            newIndex = scope.$index;
                        }
                    } else {
                        if (droppedItemIndex < scope.$index) {
                            newIndex = scope.$index;
                        } else {
                            newIndex = scope.$index + 1;
                        }
                    }

                    move.apply(theList, [droppedItemIndex, newIndex]);

                    scope.$apply(function() {
                        scope.$emit('uiSelectSort:change', {
                            array: theList,
                            item: itemToMove,
                            from: droppedItemIndex,
                            to: newIndex
                        });
                    });

                    element.removeClass(droppingClassName);
                    element.removeClass(droppingBeforeClassName);
                    element.removeClass(droppingAfterClassName);

                    element.off('drop', dropHandler);
                };

                element.on('dragenter', function() {
                    if (element.hasClass(draggingClassName)) {
                        return;
                    }

                    element.addClass(droppingClassName);

                    element.on('dragover', dragOverHandler);
                    element.on('drop', dropHandler);
                });

                element.on('dragleave', function(event) {
                    if (event.target != element) {
                        return;
                    }
                    element.removeClass(droppingClassName);
                    element.removeClass(droppingBeforeClassName);
                    element.removeClass(droppingAfterClassName);

                    element.off('dragover', dragOverHandler);
                    element.off('drop', dropHandler);
                });
            }
        };
    }]);

    /**
     * Parses "repeat" attribute.
     *
     * Taken from AngularJS ngRepeat source code
     * See https://github.com/angular/angular.js/blob/v1.2.15/src/ng/directive/ngRepeat.js#L211
     *
     * Original discussion about parsing "repeat" attribute instead of fully relying on ng-repeat:
     * https://github.com/angular-ui/ui-select/commit/5dd63ad#commitcomment-5504697
     */

    uis.service('uisRepeatParser', ['uiSelectMinErr','$parse', function(uiSelectMinErr, $parse) {
        var self = this;

        /**
         * Example:
         * expression = "address in addresses | filter: {street: $select.search} track by $index"
         * itemName = "address",
         * source = "addresses | filter: {street: $select.search}",
         * trackByExp = "$index",
         */
        self.parse = function(expression) {


            var match;
            //var isObjectCollection = /\(\s*([\$\w][\$\w]*)\s*,\s*([\$\w][\$\w]*)\s*\)/.test(expression);
            // If an array is used as collection

            // if (isObjectCollection){
            // 000000000000000000000000000000111111111000000000000000222222222222220033333333333333333333330000444444444444444444000000000000000055555555555000000000000000000000066666666600000000
            match = expression.match(/^\s*(?:([\s\S]+?)\s+as\s+)?(?:([\$\w][\$\w]*)|(?:\(\s*([\$\w][\$\w]*)\s*,\s*([\$\w][\$\w]*)\s*\)))\s+in\s+(\s*[\s\S]+?)?(?:\s+track\s+by\s+([\s\S]+?))?\s*$/);

            // 1 Alias
            // 2 Item
            // 3 Key on (key,value)
            // 4 Value on (key,value)
            // 5 Source expression (including filters)
            // 6 Track by

            if (!match) {
                throw uiSelectMinErr('iexp', "Expected expression in form of '_item_ in _collection_[ track by _id_]' but got '{0}'.",
                    expression);
            }

            var source = match[5],
                filters = '';

            // When using (key,value) ui-select requires filters to be extracted, since the object
            // is converted to an array for $select.items
            // (in which case the filters need to be reapplied)
            if (match[3]) {
                // Remove any enclosing parenthesis
                source = match[5].replace(/(^\()|(\)$)/g, '');
                // match all after | but not after ||
                var filterMatch = match[5].match(/^\s*(?:[\s\S]+?)(?:[^\|]|\|\|)+([\s\S]*)\s*$/);
                if(filterMatch && filterMatch[1].trim()) {
                    filters = filterMatch[1];
                    source = source.replace(filters, '');
                }
            }

            return {
                itemName: match[4] || match[2], // (lhs) Left-hand side,
                keyName: match[3], //for (key, value) syntax
                source: $parse(source),
                filters: filters,
                trackByExp: match[6],
                modelMapper: $parse(match[1] || match[4] || match[2]),
                repeatExpression: function (grouped) {
                    var expression = this.itemName + ' in ' + (grouped ? '$group.items' : '$select.items');
                    if (this.trackByExp) {
                        expression += ' track by ' + this.trackByExp;
                    }
                    return expression;
                }
            };

        };

        self.getGroupNgRepeatExpression = function() {
            return '$group in $select.groups';
        };

    }]);

}());
angular.module("ui.select").run(["$templateCache", function($templateCache) {$templateCache.put("bootstrap/choices.tpl.html","<ul class=\"ui-select-choices ui-select-choices-content ui-select-dropdown dropdown-menu\" role=\"listbox\" ng-show=\"$select.open\"><li class=\"ui-select-choices-group\" id=\"ui-select-choices-{{ $select.generatedId }}\"><div class=\"divider\" ng-show=\"$select.isGrouped && $index > 0\"></div><div ng-show=\"$select.isGrouped\" class=\"ui-select-choices-group-label dropdown-header\" ng-bind=\"$group.name\"></div><div id=\"ui-select-choices-row-{{ $select.generatedId }}-{{$index}}\" class=\"ui-select-choices-row\" ng-class=\"{active: $select.isActive(this), disabled: $select.isDisabled(this)}\" role=\"option\"><a href=\"\" class=\"ui-select-choices-row-inner\"></a></div></li></ul>");
    $templateCache.put("bootstrap/match-multiple.tpl.html","<span class=\"ui-select-match\"><span ng-repeat=\"$item in $select.selected\"><span class=\"ui-select-match-item btn btn-default btn-xs\" tabindex=\"-1\" type=\"button\" ng-disabled=\"$select.disabled\" ng-click=\"$selectMultiple.activeMatchIndex = $index;\" ng-class=\"{\'btn-primary\':$selectMultiple.activeMatchIndex === $index, \'select-locked\':$select.isLocked(this, $index)}\" ui-select-sort=\"$select.selected\"><span class=\"close ui-select-match-close\" ng-hide=\"$select.disabled\" ng-click=\"$selectMultiple.removeChoice($index)\">&nbsp;&times;</span> <span uis-transclude-append=\"\"></span></span></span></span>");
    $templateCache.put("bootstrap/match.tpl.html","<div class=\"ui-select-match\" ng-hide=\"$select.open\" ng-disabled=\"$select.disabled\" ng-class=\"{\'btn-default-focus\':$select.focus}\"><span tabindex=\"-1\" class=\"btn btn-default form-control ui-select-toggle\" aria-label=\"{{ $select.baseTitle }} activate\" ng-disabled=\"$select.disabled\" ng-click=\"$select.activate()\" style=\"outline: 0;\"><span ng-show=\"$select.isEmpty()\" class=\"ui-select-placeholder text-muted\">{{$select.placeholder}}</span> <span ng-hide=\"$select.isEmpty()\" class=\"ui-select-match-text pull-left\" ng-class=\"{\'ui-select-allow-clear\': $select.allowClear && !$select.isEmpty()}\" ng-transclude=\"\"></span> <i class=\"caret pull-right\" ng-click=\"$select.toggle($event)\"></i> <a ng-show=\"$select.allowClear && !$select.isEmpty()\" aria-label=\"{{ $select.baseTitle }} clear\" style=\"margin-right: 10px\" ng-click=\"$select.clear($event)\" class=\"btn btn-xs btn-link pull-right\"><i class=\"glyphicon glyphicon-remove\" aria-hidden=\"true\"></i></a></span></div>");
    $templateCache.put("bootstrap/select-multiple.tpl.html","<div class=\"ui-select-container ui-select-multiple ui-select-bootstrap dropdown form-control\" ng-class=\"{open: $select.open}\"><div><div class=\"ui-select-match\"></div><input type=\"text\" autocomplete=\"off\" autocorrect=\"off\" autocapitalize=\"off\" spellcheck=\"false\" class=\"ui-select-search input-xs\" placeholder=\"{{$selectMultiple.getPlaceholder()}}\" ng-disabled=\"$select.disabled\" ng-hide=\"$select.disabled\" ng-click=\"$select.activate()\" ng-model=\"$select.search\" role=\"application\" aria-label=\"{{ $select.baseTitle }}\" ondrop=\"return false;\"></div><div class=\"ui-select-choices\"></div></div>");
    $templateCache.put("bootstrap/select.tpl.html","<div class=\"ui-select-container ui-select-bootstrap dropdown\" ng-class=\"{open: $select.open}\"><div class=\"ui-select-match\"></div><input type=\"text\" autocomplete=\"off\" tabindex=\"-1\" aria-expanded=\"true\" aria-label=\"{{ $select.baseTitle }}\" aria-owns=\"ui-select-choices-{{ $select.generatedId }}\" aria-activedescendant=\"ui-select-choices-row-{{ $select.generatedId }}-{{ $select.activeIndex }}\" class=\"form-control ui-select-search\" placeholder=\"{{$select.placeholder}}\" ng-model=\"$select.search\" ng-show=\"$select.searchEnabled && $select.open\"><div class=\"ui-select-choices\"></div></div>");

    $templateCache.put("select2/choices.tpl.html",
        "<ul class=\"ui-select-choices ui-select-choices-content select2-results\" >" +
        "<li ng-show=\"$select.showMinMsg\" class=\"select2-no-results\" role=\"listbox\" ng-bind=\"'uiselect.minimum.input.text' | xei18n: $select.minimumInputLength\" aria-live=\"polite\" ></li>" +
        "<li ng-show=\"$select.showNoResultsMsg && !$select.showMinMsg\" role=\"listbox\"  class=\"select2-no-results \"  ng-bind=\"'uiselect.no.results.found.text' | xei18n\" > " +
        "<li class=\"ui-select-choices-group\" ng-class=\"{\'select2-result-with-children\': $select.choiceGrouped($group) }\" role=\"listbox\"  >" +
        "<div ng-show=\"$select.choiceGrouped($group) && !$select.showNoResultsMsg && !$select.showMinMsg\" class=\"ui-select-choices-group-label select2-result-label\" ng-bind=\"$group.name\"></div>" +
        "<ul ng-show=\"!$select.showNoResultsMsg && !$select.showMinMsg\" role=\"listbox\" id=\"ui-select-choices-{{ $select.generatedId }}\" " +
        "ng-class=\"{\'select2-result-sub\': $select.choiceGrouped($group), \'select2-result-single\': !$select.choiceGrouped($group) }\">" +
        "<li role=\"listbox\" id=\"ui-select-choices-row-{{ $select.generatedId }}-{{$index}}\" class=\"ui-select-choices-row\"  " +
        "ng-class=\"{\'select2-highlighted\': $select.isActive(this), \'select2-disabled\': $select.isDisabled(this)}\" >" +
        "<div class=\"select2-result-label ui-select-choices-row-inner\"></div></li></ul></li></ul>");

    $templateCache.put("select2/match-multiple.tpl.html","<span class=\"ui-select-match\"><li class=\"ui-select-match-item select2-search-choice\" ng-repeat=\"$item in $select.selected\" ng-class=\"{\'select2-search-choice-focus\':$selectMultiple.activeMatchIndex === $index, \'select2-locked\':$select.isLocked(this, $index)}\" ui-select-sort=\"$select.selected\"><span uis-transclude-append=\"\"></span> <a class=\"ui-select-match-close select2-search-choice-close\" ng-click=\"$selectMultiple.removeChoice($index)\" tabindex=\"-1\"></a></li></span>");
    $templateCache.put("select2/match.tpl.html","<a class=\"select2-choice ui-select-match\" ng-class=\"{\'select2-default\': $select.isEmpty()}\" ng-click=\"$select.toggle($event)\" aria-label=\"{{ $select.baseTitle }} select\"><span ng-show=\"$select.isEmpty()\" class=\"select2-chosen\">{{$select.placeholder}}</span> <span ng-hide=\"$select.isEmpty()\" class=\"select2-chosen\" ng-transclude=\"\"></span> <abbr ng-if=\"$select.allowClear && !$select.isEmpty()\" class=\"select2-search-choice-close\" ng-click=\"$select.clear($event)\"></abbr> <span class=\"select2-arrow ui-select-toggle\"><b></b></span></a>");

    $templateCache.put("select2/select-multiple.tpl.html",
        "<div class=\"ui-select-container ui-select-multiple select2 select2-container select2-container-multi\" " +
        "ng-class=\"{\'select2-container-active select2-dropdown-open open\': $select.open, \'select2-container-disabled\': $select.disabled}\">" +
        "<ul class=\"select2-choices\" ng-click=\"$select.activate()\"><span class=\"ui-select-match\"></span>" +
        "<li class=\"select2-search-field\">" +
        "<input type=\"text\" autocomplete=\"off\" autocorrect=\"off\" autocapitalize=\"off\" spellcheck=\"false\" role=\"application\" aria-expanded=\"true\" " +
        "aria-owns=\"ui-select-choices-{{ $select.generatedId }}\" " +
        "aria-label=\"{{ $select.baseTitle }}\" aria-activedescendant=\"ui-select-choices-row-{{ $select.generatedId }}-{{ $select.activeIndex }}\" " +
        "class=\"select2-input ui-select-search\" placeholder=\"{{$selectMultiple.getPlaceholder()}}\" ng-disabled=\"$select.disabled\" ng-hide=\"$select.disabled\" " +
        "ng-model=\"$select.search\"  style=\"width: 34px;\" ondrop=\"return false;\"/>" +
        "</li>" +
        "</ul><div class=\"ui-select-dropdown select2-drop select2-with-searchbox select2-drop-active\" ng-class=\"{\'select2-display-none\': !$select.open}\">" +
        "<div class=\"ui-select-choices\"></div></div></div>");

    $templateCache.put("select2/select.tpl.html","<div class=\"ui-select-container select2 select2-container\" " +
        "ng-class=\"{\'select2-container-active select2-dropdown-open open\': $select.open, \'select2-container-disabled\': $select.disabled, \'select2-container-active\': $select.focus, \'select2-allowclear\': $select.allowClear && !$select.isEmpty()}\">" +
        "<div class=\"ui-select-match\"></div><div class=\"ui-select-dropdown select2-drop select2-with-searchbox select2-drop-active\" " +
        "ng-class=\"{\'select2-display-none\': !$select.open}\"><div ng-class=\" {\'ui-select-search-hidden\' : !$select.searchEnabled , \'select2-search\' : $select.searchEnabled , \'search-container\' : $select.searchEnabled }\">" +
        "<input type=\"text\" autocomplete=\"off\" aria-autocomplete=\"list\" autocorrect=\"false\" autocapitalize=\"off\" spellcheck=\"false\" role=\"application\" aria-expanded=\"true\" aria-owns=\"ui-select-choices-{{ $select.generatedId }}\" aria-label=\"{{ $select.baseTitle }}\" aria-activedescendant=\"ui-select-choices-row-{{ $select.generatedId }}-{{ $select.activeIndex }}\" class=\"ui-select-search select2-input\" ng-model=\"$select.search\"></div>" +
        "<div class=\"ui-select-choices\"></div></div></div>");


    $templateCache.put("selectize/choices.tpl.html","<div ng-show=\"$select.open\" class=\"ui-select-choices ui-select-dropdown selectize-dropdown single\"><div class=\"ui-select-choices-content selectize-dropdown-content\"><div class=\"ui-select-choices-group optgroup\" role=\"listbox\"><div ng-show=\"$select.isGrouped\" class=\"ui-select-choices-group-label optgroup-header\" ng-bind=\"$group.name\"></div><div role=\"option\" class=\"ui-select-choices-row\" ng-class=\"{active: $select.isActive(this), disabled: $select.isDisabled(this)}\"><div class=\"option ui-select-choices-row-inner\" data-selectable=\"\"></div></div></div></div></div>");
    $templateCache.put("selectize/match.tpl.html","<div ng-hide=\"($select.open || $select.isEmpty())\" class=\"ui-select-match\" ng-transclude=\"\"></div>");
    $templateCache.put("selectize/select.tpl.html","<div class=\"ui-select-container selectize-control single\" ng-class=\"{\'open\': $select.open}\"><div class=\"selectize-input\" ng-class=\"{\'focus\': $select.open, \'disabled\': $select.disabled, \'selectize-focus\' : $select.focus}\" ng-click=\"$select.open && !$select.searchEnabled ? $select.toggle($event) : $select.activate()\"><div class=\"ui-select-match\"></div><input type=\"text\" autocomplete=\"off\" tabindex=\"-1\" class=\"ui-select-search ui-select-toggle\" ng-click=\"$select.toggle($event)\" placeholder=\"{{$select.placeholder}}\" ng-model=\"$select.search\" ng-hide=\"!$select.searchEnabled || ($select.selected && !$select.open)\" ng-disabled=\"$select.disabled\" aria-label=\"{{ $select.baseTitle }}\"></div><div class=\"ui-select-choices\"></div></div>");}]);
(function () {
    'use strict';
    angular.module('xeUISelect', ['ui.select', 'ngSanitize'])
        .filter('propsFilter', function() {
            return function(items, props) {
                var out = [];

                if (angular.isArray(items)) {
                    items.forEach(function(item) {
                        var itemMatches = false;

                        var keys = Object.keys(props);
                        for (var i = 0; i < keys.length; i++) {
                            var prop = keys[i];
                            var text = props[prop].toLowerCase();
                            if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
                                itemMatches = true;
                                break;
                            }
                        }

                        if (itemMatches) {
                            out.push(item);
                        }
                    });
                } else {
                    // Let the output be the input untouched
                    out = items;
                }

                return out;
            }
        }).directive('reachInfinity', ['$parse', '$timeout', '$q', function($parse, $timeout, $q) {
            function height(elem) {
                elem = elem[0] || elem;
                if (isNaN(elem.offsetHeight)) {
                    return elem.document.documentElement.clientHeight;
                } else {
                    return elem.offsetHeight;
                }
            }

            function offsetTop(elem) {
                if (!elem[0].getBoundingClientRect || elem.css('none')) {
                    return;
                }
                return elem[0].getBoundingClientRect().top + pageYOffset(elem);
            }

            function pageYOffset(elem) {
                elem = elem[0] || elem;
                if (isNaN(window.pageYOffset)) {
                    return elem.document.documentElement.scrollTop;
                } else {
                    return elem.ownerDocument.defaultView.pageYOffset;
                }
            }

            /**
             * Since scroll events can fire at a high rate, the event handler
             * shouldn't execute computationally expensive operations such as DOM modifications.
             * based on https://developer.mozilla.org/en-US/docs/Web/Events/scroll#requestAnimationFrame_.2B_customEvent
             *
             * @param type
             * @param name
             * @param (obj)
             * @returns {Function}
             */
            function throttle(type, name, obj) {
                var running = false;

                obj = obj || window;

                var func = function() {
                    if (running) {
                        return;
                    }

                    running = true;
                    requestAnimationFrame(function() {
                        obj.dispatchEvent(new CustomEvent(name));
                        running = false;
                    });
                };

                obj.addEventListener(type, func);

                return function() {
                    obj.removeEventListener(type, func);
                };
            }

            return {
                link: function(scope, elem, attrs) {
                    var container = elem,
                        scrollDistance = angular.isDefined(attrs.scrollDistance) ? parseInt(attrs.scrollDistance) : 0.3,
                        removeThrottle;

                    function tryToSetupInfinityScroll() {
                        var rows = elem.querySelectorAll('.ui-select-choices-row');

                        if (rows.length === 0) {
                            return false;
                        }

                        var lastChoice = angular.element(rows[rows.length - 1]);

                        container = angular.element(elem.querySelectorAll('.ui-select-choices-content'));

                        var handler = function() {
                            var containerBottom = height(container),
                                containerTopOffset = 0,
                                elementBottom;

                            if (offsetTop(container) !== void 0) {
                                containerTopOffset = offsetTop(container);
                            }

                            elementBottom = offsetTop(lastChoice) - containerTopOffset + height(lastChoice);

                            var remaining = elementBottom - containerBottom,
                                shouldScroll = remaining <= height(container) * (scrollDistance + 1);

                            if (shouldScroll) {
                                $q.when($parse(attrs['reachInfinity'])(scope)).then(function() {
                                    setTimeout(function() {
                                        rows = elem.querySelectorAll('.ui-select-choices-row');
                                        lastChoice = angular.element(rows[rows.length - 1]);
                                    }, 0);
                                });
                            }
                        };

                        removeThrottle = throttle('scroll', 'optimizedScroll', container[0]);
                        container.on('optimizedScroll', handler);

                        scope.$on('$destroy', function() {
                            removeThrottle();
                            container.off('optimizedScroll', handler);
                        });

                        return true;
                    }

                    var unbindWatcher = scope.$watch('$select.open', function(newItems) {
                        if (!newItems) {
                            return;
                        }

                        $timeout(function() {
                            if (tryToSetupInfinityScroll()) {
                                unbindWatcher();
                            }
                        });
                    });
                }
            }
        }]);
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
                        }else{
                         return true;
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
            return function (key, arg1) {
                var value = "";
                if (angular.isDefined(key) && angular.isDefined(jQuery.i18n)) {
                    value = jQuery.i18n.prop(key, [arg1]);
                }

                if (!value || value.indexOf(key) >= 0) {
                    value = $filter('translate')(key, {arg1: arg1});
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
                            $scope.addExtensionColumns($scope.header, data);
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
                            $scope.addExtensionColumns($scope.header, data);
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
            scope.header =  parentController.header;
            scope.addExtensionColumns = parentController.addExtensionColumns;

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
                placeholder: '@',
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
/*****************************************************
 *  © 2018 Ellucian Company L.P. and its affiliates. *
 *****************************************************/
; // safe for iife
(function() {


    angular.module('xebarmodule', [])
        .factory('d3', ['$window', '$log', function ($window, $log) {
            if (!$window.d3) {
                $log.error('D3 must be present');
            }
            return $window.d3;
        }])
        .directive('xeBarModule', ['d3', 'Language','$filter','getlocale', function (d3, Language,$filter,getlocale){

            return {
                restrict: 'E',
                scope: {
                    data: '=xebardata',
                    ngclickfunction: "&",
                    barChartWidth: "@xeChartWidth",
                    barChartHeight: "@xeChartHeight",
                    barWidth: "@xeBarWidth",
                    gapWidth:"@xeGapWidth",
                    xTitleValue: "@xeXaxisTitle",
                    yTitleValue: "@xeYaxisTitle",
                    yAxisFormat:"@xeYaxisFormat"
                },
                template: '<div><div class="xe-bar-tooltip"></div></div>',
                link: function(scope, element) {
                    /* svg variables */
                    var xAxis,yAxis,svg,x,y,svgChild,xSvgChild,ySvgChild,allBars,bar,zoom,chartArea,yTitle;

                    /* Input variables */
                    var chartWidth,chartHeight,barWidth,gapWidth,xTitleValue,yTitleValue,yAxisFormat;

                    /* Chart variables */
                    var divTooltip,gapFactor,numberOfBars,width,height,chartData;



                    var margin = {
                        top: 20,
                        right: 50,
                        bottom: 50,
                        left: 40
                    };
                    var color = d3.scale.ordinal()
                        .range(["#9191E1", "#90C9FF", "#6DD6BC", "#BBE19B", "#F4DB76", "#FFB581", "#D389C6", "#B2B3B7","#DD5C5F"]);

                    /* Bar chart variables */
                    var indexG=-1;

                    /* Multi bar chart variables */
                    var isMultipleBarChart = false,numberOfSG=0,indexSG=0,multipleBarChartMetaData;


                    element.addClass("xe-bar-chart");

                    function detectIE() {
                        ua = window.navigator.userAgent;
                        msie = ua.indexOf('MSIE ');
                        trident = ua.indexOf('Trident/');
                        if (msie > 0 || trident > 0) {
                            return true;
                        }
                        return false;
                    }

                    if(detectIE()){
                        /* Code to support classList in IE */
                        if (!Object.getOwnPropertyDescriptor(Element.prototype,'classList')){
                            if (HTMLElement&&Object.getOwnPropertyDescriptor(HTMLElement.prototype,'classList')){
                                Object.defineProperty(Element.prototype,'classList',Object.getOwnPropertyDescriptor(HTMLElement.prototype,'classList'));
                            }
                        }
                    }
                    function localizedNumber(input){
                        if(Language.isRtl()) {
                            return input;
                        }
                        else {
                            return input.toLocaleString(getlocale.getUserLocale().replace('_', '-'));
                        }
                    }
                    function wrap(text, width) {
                        width = 2* width;
                        text.each(function() {
                            var text = d3.select(this),
                                words = text.text().split(/\s+/).reverse(),
                                word,
                                line = [],
                                lineNumber = 0,
                                lineHeight = 1.1, // ems
                                y = text.attr("y"),
                                dy = parseFloat(text.attr("dy")),
                                tspan = text.text("").append("tspan").attr("x", 0).attr("y", y).attr("dy", dy + "em");
                            while (word = words.pop()) {
                                line.push(word);
                                tspan.text(line.join(" "));
                                if ((tspan.node().getComputedTextLength()>0? tspan.node().getComputedTextLength():(tspan.text().length*5)) > width) {
                                    if(line.length>1)
                                    {
                                        line.pop();
                                        tspan.text(line.join(" "));
                                        line = [word];
                                        tspan = text.append("tspan").attr("x", 0).attr("y", y).attr("dy", ++lineNumber * lineHeight + dy + "em").text(word);
                                    }
                                    else
                                    {
                                        line=[];
                                        tspan = text.append("tspan").attr("x", 0).attr("y", y).attr("dy", ++lineNumber * lineHeight + dy + "em").text("");
                                    }

                                }
                            }
                        });
                    }
                    function initializeVariables(){
                        if(scope.barWidth == undefined) {
                            barWidth = 44;
                        }
                        else {
                            barWidth = +scope.barWidth;
                        }
                        if(scope.gapWidth == undefined) {
                            gapWidth= 55;
                        }
                        else {
                            gapWidth = +scope.gapWidth;
                        }
                        if (scope.barChartWidth == undefined) {
                            chartWidth = 400;
                        }
                        else {
                            chartWidth = +scope.barChartWidth;
                        }
                        if (scope.barChartHeight == undefined) {
                            chartHeight = 450;
                        }
                        else {
                            chartHeight = +scope.barChartHeight;
                        }
                        if(scope.xTitleValue==undefined) {
                            xTitleValue ="";
                        }
                        else {
                            xTitleValue = scope.xTitleValue;
                        }
                        if(scope.yTitleValue==undefined) {
                            yTitleValue = "";
                        }
                        else {
                            yTitleValue = scope.yTitleValue;
                        }
                        if(scope.yAxisFormat==undefined) {
                            yAxisFormat = "d";
                        }
                        else {
                            yAxisFormat = scope.yAxisFormat;
                        }

                        width = +chartWidth - margin.right - margin.left;
                        height = +chartHeight - margin.top - margin.bottom;
                        numberOfBars = width / (barWidth+gapWidth);
                        gapFactor= gapWidth/(barWidth+gapWidth);
                    }
                    function initializeKeys(){
                        scope.dataKeys = [];
                        angular.forEach(scope.data[0], function (value, key) {
                            scope.dataKeys.push(key);
                        });

                        if (scope.dataKeys.length == 1) {
                            isMultipleBarChart = true;
                            scope.dataKeys = [];
                            angular.forEach(scope.data, function (value, key) {
                                angular.forEach(value, function (value1, key1) {
                                    scope.dataKeys.push(key1);
                                });
                            });
                            scope.displayGroupsKey = scope.dataKeys[0];
                            scope.displayValuesKey = scope.dataKeys[1];
                            multipleBarChartMetaData = scope.data[0][scope.displayGroupsKey];
                            chartData = scope.data[1][scope.displayValuesKey];
                            numberOfSG =multipleBarChartMetaData.length;
                            if(Language.isRtl()){
                                multipleBarChartMetaData = multipleBarChartMetaData.reverse();
                            }
                            scope.dataKeys = [];
                            angular.forEach(multipleBarChartMetaData[0], function (value, key) {
                                scope.dataKeys.push(key);
                            });
                            angular.forEach(chartData[0], function (value, key) {
                                scope.dataKeys.push(key);
                            });
                            scope.subLabelKey = scope.dataKeys[0];
                            scope.colorKey = scope.dataKeys[1];
                            scope.labelKey = scope.dataKeys[2];
                            scope.valueKey = scope.dataKeys[3];
                        }
                        else {
                            scope.labelKey = scope.dataKeys[0];
                            scope.valueKey = scope.dataKeys[1];
                            scope.colorKey = scope.dataKeys[2];
                            chartData = scope.data;
                        }

                    }
                    function renderChart() {
                        if(chartData.length > numberOfBars){
                            svg.call(zoom);
                        }

                        x.domain(chartData.map(function (d) {
                            return d[scope.labelKey];
                        }));


                        if (Language.isRtl()) {
                            x.rangeRoundBands([width, ((barWidth+gapWidth) * (numberOfBars - chartData.length))], gapFactor);
                        } else {
                            x.rangeRoundBands([0, ((barWidth+gapWidth) * (chartData.length))], gapFactor);
                        }
                        if (isMultipleBarChart) {
                            y.domain([0, d3.max(chartData, function (d) {
                                return d3.max(d[scope.valueKey]);
                            })]).nice()
                                .range([height, 0]);
                        }
                        else {
                            y.domain([0, d3.max(chartData, function (d) {
                                return d[scope.valueKey];
                            })]).nice()
                                .range([height, 0]);
                        }
                        y.domain([0, d3.max(y.ticks()) + y.ticks()[1]]);
                        svg.on("keydown", function () {
                                switch (d3.event.keyCode) {
                                    case 37:
                                        zoom.translate([zoom.translate()[0] + (barWidth+gapWidth), zoom.translate()[1]]).event(svg);
                                        break;
                                    case 39:
                                        zoom.translate([zoom.translate()[0] - (barWidth+gapWidth), zoom.translate()[1]]).event(svg);
                                        break;
                                }

                            });
                        if (Language.isRtl())
                        {
                            ySvgChild.call(yAxis)
                                .attr("transform", "translate(" + width + " ,0)");
                            ySvgChild.selectAll("text").style("text-anchor", "end");
                        } else {
                            ySvgChild.call(yAxis);
                        }

                        xSvgChild.attr("transform", "translate(0," + height + ")")
                            .call(xAxis)
                            .selectAll(".tick text")
                            .call(wrap, x.rangeBand());




                        allBars.selectAll(".bar").remove();

                        bar = allBars.selectAll(".bar").data(chartData)
                                .enter().append("g");

                        bar.attr("class", "bar")
                            .attr("transform", function (d) {
                                return "translate(" + x(d[scope.labelKey]) + ",0)";
                            });
                        bar.selectAll("rect")
                            .data(function (d) {
                                if (isMultipleBarChart)
                                    if(Language.isRtl())
                                        return d[scope.valueKey].reverse();
                                    else
                                        return d[scope.valueKey];
                                else
                                    return [d];
                            })
                            .enter().append("rect")
                            .attr("width", function (d) {
                                if (isMultipleBarChart)
                                    return barWidth / numberOfSG;
                                else
                                    return barWidth;
                            })
                            .attr("x", function (d) {
                                if (isMultipleBarChart) {
                                    if(indexSG==numberOfSG) indexSG=0;
                                    return (indexSG++ * barWidth / numberOfSG);
                                }
                                else
                                    return 0;
                            })
                            .attr("y", function (d) {
                                if(isMultipleBarChart)
                                    return isNaN(y(d))?0: y(d);
                                else
                                    return isNaN(y(d[scope.valueKey]))?0: y(d[scope.valueKey]);
                            })
                            .attr("height", function (d) {
                                if(isMultipleBarChart)
                                    return isNaN(y(d))?0: (height - y(d));
                                else
                                    return  isNaN(y(d[scope.valueKey]))?0: (height- y(d[scope.valueKey]));

                            })
                            .style("fill", function (d) {
                                if(isMultipleBarChart){
                                    if(indexSG==numberOfSG) indexSG=0;
                                    if(multipleBarChartMetaData[indexSG][scope.colorKey] == undefined || multipleBarChartMetaData[indexSG][scope.colorKey]=='')
                                        return color(indexSG++);
                                    else
                                        return multipleBarChartMetaData[indexSG++][scope.colorKey];
                                }
                                else {
                                    indexG++;
                                    if (d[scope.colorKey] == undefined||d[scope.colorKey]=='') {
                                        return color((indexG%color.range().length));
                                    }
                                    else
                                        return d[scope.colorKey];

                                }

                            })
                            .attr("tabindex", function (d) {
                                if((isMultipleBarChart&& d==0) ||(!isMultipleBarChart&& d[scope.valueKey]==0)) return -1;
                                return 0;
                            })
                            .attr("focusable", function (d) {
                                if((isMultipleBarChart&& d==0) ||(!isMultipleBarChart&& d[scope.valueKey]==0)) return "false";
                                return "true";
                            })
                            .attr("aria-label", function (d) {
                                if (isMultipleBarChart) {
                                    if(indexSG==numberOfSG) indexSG=0;
                                    return multipleBarChartMetaData[indexSG++][scope.subLabelKey] + " " + localizedNumber(d);
                                }
                                else
                                    return d[scope.labelKey] + " " + localizedNumber(d[scope.valueKey]);
                            })
                            .on("mousemove", function (d) {
                                divTooltip[0].style.left = d3.event.layerX + 10 + "px";
                                divTooltip[0].style.top = d3.event.layerY - 25 + "px";
                                divTooltip[0].style.display = "block";
                                if (isMultipleBarChart) {
                                    var tooltip = d3.event.currentTarget.attributes["aria-label"].value;
                                    divTooltip[0].innerHTML = tooltip.substring(0, tooltip.lastIndexOf(' ')) +"<br>"+tooltip.substring(tooltip.lastIndexOf(' ') + 1);
                                }
                                else
                                    divTooltip[0].innerHTML = d[scope.labelKey] + " <br> " + localizedNumber(d[scope.valueKey]);
                            })
                            .on("mouseout", function (d) {
                                divTooltip[0].style.display = "none";
                            })
                            .on('focusin',function() {
                                d3.event.target.classList.add("xe-rect-on");
                            })
                            .on('focusout',function() {
                                d3.event.target.classList.remove("xe-rect-on");
                            });


                        bar.on("click", function (d) {
                            scope.ngclickfunction({
                                barData: d
                            });
                        });

                        bar.on("keydown", function (d) {
                            if(d3.event.keyCode==13) {
                                scope.ngclickfunction({
                                    barData:d
                                });
                            }
                        });

                        bar.selectAll("text")
                            .data(function (d) {
                                if (isMultipleBarChart)
                                    if(Language.isRtl())
                                        return d[scope.valueKey].reverse();
                                    else
                                        return d[scope.valueKey];
                                else
                                    return [d];
                            })
                            .enter()
                            .append("text")
                            .attr("class", "xe-bar-label")
                            .attr("text-anchor", "middle")
                            .attr("x", function (d) {
                                if (isMultipleBarChart) {
                                    if (indexSG == numberOfSG) indexSG = 0;
                                    if(Language.isRtl()){
                                        return ((numberOfSG-indexSG++) * barWidth / numberOfSG) - (barWidth / (2 * numberOfSG));
                                    }
                                    else {
                                        return (indexSG++ * barWidth / numberOfSG) + (barWidth / (2 * numberOfSG));
                                    }
                                }
                                else
                                    return barWidth/2;
                            })
                            .attr("y", function (d) {
                                if(isMultipleBarChart)
                                    return isNaN(y(d))?0: (y(d)- 3);
                                else
                                    return isNaN(y(d[scope.valueKey]))?0: (y(d[scope.valueKey]) -3) ;
                            })
                            .text(function(d){
                                if (isMultipleBarChart) {
                                    return localizedNumber(d);
                                }
                                else
                                    return localizedNumber(d[scope.valueKey]);
                            });
                    }
                    function initializeChart() {
                        divTooltip = element[0].getElementsByClassName("xe-bar-tooltip");
                        x = d3.scale.ordinal();
                        y = d3.scale.linear();
                        xAxis = d3.svg.axis()
                            .scale(x)
                            .orient("bottom")
                            .innerTickSize(0)
                            .outerTickSize(0);

                        zoom = d3.behavior.zoom().on("zoom", scope.zoom)

                        d3.select(element[0]).selectAll("svg").remove();
                        svg = d3.select(element[0])
                            .classed("svg-container", true)
                            .append("svg")
                            .attr("preserveAspectRatio", "xMinYMin meet")
                            .attr("viewBox", "0 "+margin.top+" " + chartWidth + " " + chartHeight)
                            .classed("svg-content-responsive", true);

                        if(detectIE()){
                            svg.attr("width",width)
                                .attr("height",height);
                        }


                        if(chartData.length > numberOfBars) {
                            svg.append("text")
                                .attr("text-anchor", "middle")
                                .attr("class", "xe-bar-title")
                                .attr("tabindex", "0")
                                .attr("focusable","true")
                                .attr("transform", "translate(" + (chartWidth / 2) + "," + (margin.top * 1.5) + ")")
                                .attr("aria-label", $filter('xei18n')('barchart.viewmore.help.label'))
                                .text($filter('xei18n')('barchart.viewmore.help.label'));
                        };

                        /* Set Y axis label if defined */
                        if(yTitleValue!="") {
                            yTitle = svg.append("text")
                                .attr("text-anchor", "middle")
                                .attr("class", "xe-bar-title")
                                .attr("tabindex","0")
                                .attr("focusable","true")
                                .attr("aria-label",yTitleValue)
                                .text(yTitleValue);
                            if (Language.isRtl()) {
                                yTitle.attr("transform", "translate(" + (chartWidth - (margin.right/4)) + "," + (chartHeight / 2) + ")rotate(-90)");
                            }
                            else {
                                yTitle.attr("transform", "translate(" + (margin.left / 3) + "," + (chartHeight / 2) + ")rotate(-90)");
                            }
                        }

                        /* Set X axis label if defined */
                        if(xTitleValue!="") {
                            svg.append("text")
                                .attr("text-anchor", "middle")
                                .attr("class", "xe-bar-title")
                                .attr("tabindex", "0")
                                .attr("focusable","true")
                                .attr("transform", "translate(" + (chartWidth / 2) + "," + (chartHeight + (margin.bottom / 3)) + ")")
                                .attr("aria-label",xTitleValue)
                                .text(xTitleValue);
                        }



                        svgChild = svg.append("g")
                            .attr("transform", "translate(" + margin.left + "," + margin.bottom + ")");

                        yAxis = d3.svg.axis()
                            .scale(y)
                            .orient(Language.isRtl()?"right":"left")
                            .innerTickSize(-width)
                            .tickFormat(d3.format(yAxisFormat));

                        ySvgChild = svgChild.append("g")
                            .attr("class", "y xe-bar-axis");

                        var chartArea = svgChild.append('g')
                            .attr('clip-path', 'url(#chartAreaClip'+scope.$id+')');

                        chartArea.append('clipPath')
                            .attr('id', 'chartAreaClip'+scope.$id)
                            .append('rect')
                            .attr({ width: width, height: height + margin.bottom });

                        xSvgChild = chartArea.append("g")
                            .attr("class", "x xe-bar-axis");
                        allBars = chartArea.append("g")
                            .attr("class", "allBars")

                    }
                    function DisplayChart() {
                        if (scope.data != null && scope.data.length != 0) {
                            element[0].style = "";
                            initializeVariables();
                            initializeKeys();
                            initializeChart();
                            renderChart();
                        }
                        else
                        {
                            element[0].style = "display:none";
                        }
                    }
                    scope.$watch('data', function (newVal, oldVal, scope) {
                        if (newVal!=oldVal) {
                            DisplayChart();
                        }
                    }, true);
                    scope.zoom = function(){

                        if(d3.event.scale==1) {

                            svg.select(".allBars").attr("transform", "translate(" + (d3.event.translate[0] ) +
                                ",0)");
                            svg.select(".x.xe-bar-axis").attr("transform", "translate(" + (d3.event.translate[0]) +
                                "," + (height) + ")").call(xAxis).selectAll(".tick text")
                                .call(wrap, x.rangeBand());
                        }
                    };

                    DisplayChart();
                }
            }

        }])
})();
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
    var editableMode = false;
    var paginationPreviousGridCell;
    var keys = {'left': 37, 'right': 39, 'enter': 13, 'escape': 27, 'tab': 9, 'downArrow': 40, 'upArrow': 38, 'PAGE_UP':33 , 'PAGE_DOWN':34 , 'HOME':36, 'END':35};
    angular.module('dataTableModule', ['utils'])
        .constant('mobileMaxWidth', 768)
        .directive('xeTableGrid', ['$timeout', 'accessibility', '$window', 'mobileMaxWidth', function ($timeout, accessibility, $window, mobileMaxWidth) {
            return {
                restrict: 'E',
                transclude: true,
                replace: true,
                scope: {
                    tableId: '@',
                    caption: '@?',
                    header: '=',
                    endPoint: '=?',
                    fetch: '&',
                    postFetch: '&',
                    content: '=',
                    resultsFound: '=?',
                    toolbar: '=',
                    paginate: '=?',
                    continuousScrolling: '=?',
                    continuousScrollParent: '@?',
                    onRowClick: '&',
                    onRowDoubleClick: '&',
                    noDataMsg: '@?',
                    emptyTableMsg: '=?',
                    searchConfig: '=',
                    paginationConfig: '=',
                    draggableColumnNames: '=?',
                    mobileLayout: '=?',
                    height: '@?',
                    refreshContent: '=?refreshGrid',
                    xeSection: '@?'
                },
            controller : ['$scope', '$filter', '$attrs', "$http", "$sce", "$timeout", function ($scope, $filter, $attrs, $http, $sce, $timeout) {
                    var orderBy = $filter('orderBy'),
                        filter = $filter("filter"),
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
                    // Sharing the header information from grid
                    this.header = $scope.header;

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

                    $scope.handleDrop = function (draggedFrom, draggedTo) {
                        $scope.header = orderColumns($scope.header, draggedFrom, draggedTo);
                    };

                    function applyExtensions(sectionName) {
                        var gridXESection;
                        var gridSectionExtns;

                        if (typeof xe !== 'undefined' && xe.extensionsFound) {
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

                        _.each(sectionExtns.fields, function (extensibleField) {
                            if (extensibleField.exclude) {
                                $scope['no' + extensibleField.name] = true;
                            }
                        });
                    }

                    function setVisibilityForHeaders(sectionExtns, columnHeaders) {
                        var updatedOption, columnToHide;

                        _.each(sectionExtns.fields, function (extensibleField) {
                            columnToHide = _.findWhere(columnHeaders, {name: extensibleField.name});

                            if (angular.isDefined(columnToHide)) {  //Set visibility to false only if the column exists
                                updatedOption = extensibleField.exclude ? {
                                    visible: !extensibleField.exclude,
                                    columnShowHide: !extensibleField.exclude
                                } : {};
                                _.extend(_.findWhere(columnHeaders, {name: extensibleField.name}).options, updatedOption);
                            }
                        });

                        return columnHeaders;
                    }

                    function orderHeaders(sectionExtns, columnHeaders) {
                        _.each(sectionExtns.fields, function (extension) {
                            if (_.has(extension, "nextSibling")) {
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

                            _.each(columnHeaders, function (item, index) {
                                item.position[device] = index + 1;
                            });
                        }

                        return columnHeaders;
                    }
                    /*Extensibility support to add new columns(HRU-6831.) */
                    this.addExtensionColumns = function (columnHeaders, response) {
                        if (response && response.length > 0 && response.result && response.result[0].extensions) {
                            if (!_.find(columnHeaders, function (heading) { return (heading.name).indexOf("extension.") > -1;})) {
                                angular.forEach(response.result[0].extensions, function (extension) {
                                    var displayColumn;
                                    if (typeof xe !== 'undefined' && xe.extensionsFound) {
                                        var gridSectionExtns = _.find(xe.extensions.sections, function (section) {
                                            return section.name == $scope.xeSection;
                                        });
                                        if (gridSectionExtns) {
                                            var gridField = _.find(gridSectionExtns.fields, function (field) {
                                                return field.name == 'extension.' + extension.name;
                                            });
                                        }
                                        if (gridField) {
                                            if (gridField.exclude || typeof gridField.exclude === 'undefined') {
                                                displayColumn = false;
                                            } else {
                                                displayColumn = true;
                                            }
                                        }
                                    }
                                    if(displayColumn) {
                                        var desktopPosition = _.max(columnHeaders, function (columnHeader) {
                                                return columnHeader.position.desktop;
                                            }).position.desktop + 1;
                                        var mobilePosition = _.max(columnHeaders, function (columnHeader) {
                                                return columnHeader.position.mobile;
                                            }).position.mobile + 1;
                                        var column = {
                                            position: {desktop: desktopPosition, mobile: mobilePosition},
                                            name: "extension." + extension.name,
                                            title: extension.prompt,
                                            options: {
                                                visible: true,
                                                sortable: false
                                            },
                                            width: '10%'
                                        };
                                        columnHeaders.push(column);
                                    }
                                });
                            }
                            $scope.populateHeaderWidths(angular.element('#' + $scope.tableId));
                            var maxTableWidth = _.reduce(_.pluck(columnHeaders, 'dynamicWidth'), function (memo, num) {
                                return memo + num;
                            }, 0);
                            angular.element('#' + $scope.tableId).find('.thead, .tbody').width(maxTableWidth);
                        }
                        return columnHeaders;
                    };

                    var defaultOptions = {visible: true, sortable: false};

                    angular.forEach($scope.header, function (value, index) {
                        if (angular.isUndefined(value.width)) {
                            value.width = '';
                        }

                        $scope.$watch(function () {
                            return $scope.header[index].options.visible;
                        }, function (newValue, oldValue) {
                            if (newValue !== oldValue) {
                                $timeout(function () {
                                    $scope.populateHeaderWidths(angular.element('#' + $scope.tableId));
                                }, 0);
                            }
                        });

                        if (angular.isDefined(value.options)) {
                            if (angular.isDefined(value.options.ascending)) {
                                $scope.sortArray[value.name] = {
                                    ascending: value.options.ascending,
                                    decending: !value.options.ascending
                                };
                                previousSortColumn = value.name;
                                _this.ascending = value.options.ascending;
                                _this.sortColumnName = value.name;
                            } else {
                                $scope.sortArray[value.name] = {ascending: false, decending: false};
                            }

                            if (!angular.isDefined(value.options.visible)) {
                                value.options.visible = true;
                            }

                            if (!angular.isDefined(value.options.sortable)) {
                                value.options.sortable = false;
                            }
                        } else {
                            value.options = defaultOptions;
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
                    $scope.populateHeaderWidths = function (table) {
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
                                    invisibleColumnWidths += width;
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
                                        heading.dynamicWidth += availableWidthPerColumn;
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
                                if (!content) {
                                    content = $scope.content;
                                }
                                $scope.content = orderBy(
                                    filter(content, data.query.searchString, false),
                                    _this.sortColumnName,
                                    _this.ascending
                                );
                                $scope.resultsFound = $scope.content.length;
                                _this.loadingDataIndicator(false);
                                _this.addExtensionColumns($scope.header, data);
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
                                        _this.addExtensionColumns($scope.header, data);
                                    })
                                    .error(function (data) {
                                        console.error(data);
                                        $scope.postFetch({response: data, oldResult: $scope.content});
                                        _this.loadingDataIndicator(false);
                                        _this.addExtensionColumns($scope.header, data);
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
                                    _this.addExtensionColumns($scope.header, data);
                                },
                                // error
                                function (data) {
                                    console.error(data);
                                    $scope.postFetch({response: data, oldResult: $scope.content});
                                    _this.loadingDataIndicator(false);
                                    _this.addExtensionColumns($scope.header, data);
                                }
                            );
                        }
                    }

                    loadData();

                    // TODO: This is just a temporary arrangement to change the search string. Ideally two-way data-binding should this job for us.
                    // Right now with nested directive two-way data-biding is not working. Need to revisit this.
                    $scope.fetchSpecial = function (searchString) {

                        if (!$scope.pagination) {
                            var promise = $scope.fetch({
                                query: {
                                    searchString: searchString,
                                    sortColumnName: _this.sortColumnName,
                                    ascending: _this.ascending
                                }
                            });

                            _this.loadingDataIndicator(true);
                            _this.addExtensionColumns($scope.header, data);

                            if (promise) {
                                promise.then(
                                    // success
                                    function (data) {
                                        $scope.postFetch({response: data, oldResult: $scope.content});
                                        $scope.content = data.result;
                                        $scope.resultsFound = $scope.content.length;
                                        _this.loadingDataIndicator(false);
                                        _this.addExtensionColumns($scope.header, data);
                                    },
                                    // error
                                    function (data) {
                                        console.error(data);
                                        $scope.postFetch({response: data, oldResult: $scope.content});
                                        _this.loadingDataIndicator(false);
                                        _this.addExtensionColumns($scope.header, data);
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

                    $scope.onSearchfocus = function (event) {
                        $scope.hideContainer = !$scope.hideContainer;
                    };

                    $scope.onSearchBlur = function (event) {
                        $scope.hideContainer = !$scope.hideContainer;
                        angular.element(event.target).val($scope.searchConfig.searchString);
                    };

                    $scope.sortOnHeading = function (heading, headerIndex) {

                        if (heading.options.sortable) {
                            var columnName = heading.name;

                            if (previousSortColumn == columnName) {
                                $scope.sortArray[columnName] = {
                                    ascending: !$scope.sortArray[columnName].ascending,
                                    decending: !$scope.sortArray[columnName].decending
                                };
                            } else {
                                previousSortColumn = columnName;
                                for (var obj in $scope.sortArray) {
                                    if (obj == columnName) {
                                        $scope.sortArray[obj] = {ascending: true, decending: false};
                                    }
                                    else {
                                        $scope.sortArray[obj] = {ascending: false, decending: false};
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
                    if (typeof object === 'object' && typeof key === 'string'){
                            value = eval('object' + '.' + key);
                        }

                    return value;
                };

                $scope.isExtendedField = function (row, fieldName) {
                   if (fieldName.indexOf("extension.") > -1) {
                       $scope.extensionValue = $scope.getObjectValueForExtendedField(row, fieldName);
                       $scope.isEditable = $scope.isEditableField(fieldName);
                       if($scope.isEditable) {
                           $scope.dataType = $scope.getDataType(row, fieldName);
                       }
                       return true;
                   }
                };

                $scope.isEditableField = function(fieldName) {
                    var isEditable;
                    if (typeof xe !== 'undefined' && xe.extensionsFound) {
                        var gridSectionExtns = _.find(xe.extensions.sections, function (section) {
                            return section.name == $scope.xeSection;
                        });
                        if (gridSectionExtns) {
                            gridSectionExtns.fields.forEach(function (field, key) {
                                if (field.name === fieldName && !field.exclude) {
                                    if (field.editable) {
                                        isEditable = true;
                                    }
                                }
                            });
                        }
                    }
                    return isEditable;
                };

                $scope.getDataType = function(row, fieldName) {
                    var ret;
                    if (typeof xe !== 'undefined' && xe.extensionsFound) {
                        row.extensions.forEach(function (extension) {
                            if ('extension.' + extension.name === fieldName) {
                                ret = extension.datatype;
                            }
                        });
                    }
                    return ret;
                };

                $scope.getObjectValueForExtendedField = function (object, key) {
                    var value;
                    var key_array = key.split('.');
                    var extensionArray = object.extensions;
                    if (extensionArray) {
                        extensionArray.forEach(function (extension) {
                            if (extension.name === key_array[1]) {
                                value = extension.value;
                                if (typeof xe !== 'undefined' && xe.extensionsFound) {
                                    var gridSectionExtns = _.find(xe.extensions.sections, function (section) {
                                        return section.name == $scope.xeSection;
                                    });
                                    if (gridSectionExtns) {
                                        var gridField = _.find(gridSectionExtns.fields, function (field) {
                                           return field.name == key;
                                        });
                                    }
                                }
                            }
                        });
                    }
                    return value;
                }

                $scope.refreshContent = function (refresh) {
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
                templateUrl: function (element, attr) {
                    return 'templates/dataTable.html';
                },
                compile: function compile(tElement, tAttrs) {
                    // Setting opacity of table to 0 till html rendering completes(to avoid displaying UI distortions)
                    tElement.css('opacity', 0);

                    if (tAttrs.paginate === "true" || tAttrs.continuousScrolling === "true") {
                        var paginationObject = tElement.find("xe-pagination");

                        if (angular.isDefined(tAttrs.fetch)) {
                            paginationObject.attr('fetch', 'fetch({query: query})');
                        }

                        if (angular.isDefined(tAttrs.postFetch)) {
                            paginationObject.attr('post-fetch', 'postFetch({response: response, oldResult: oldResult})');
                        }

                        if (angular.isDefined(tAttrs.paginationConfig)) {
                            paginationObject.attr('pagination-config', 'paginationConfig');
                        }

                        if (tAttrs.endPoint) {
                            paginationObject.attr('end-point', 'endPoint');
                        }
                    } else {
                        // Removing pagination if its not set to true, to avoid getting executed even when its not needed.
                        tElement.find("xe-pagination").remove();
                    }

                    return function postLink(scope, element, attrs, controller) {
                        angular.element(".tfoot").remove();

                        $timeout(function () {
                            // Resetting opacity of table after html rendering completes
                            element.css('opacity', 1);

                            scope.populateHeaderWidths(element);

                            accessibility.provideAccessibilityForTable(element, angular.element('#' + scope.continuousScrollParent));

                            scope.nextPage = function () {
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
        .directive('xeCellInjector', [function () {
            return {
                require: '^xeTableGrid',
                restrict: 'A',
                replace: true,
                scope: true,
                /*
                 This block to provide DOM manipulation methods if any.
                 */
                link: function (scope, element, attrs, controllerInstance, $transclude) {
                    var id = attrs.name;
                    element.attr("tabindex", 0);
                    var transclude = scope.transcludes[id];
                    if (transclude) {
                        var scopeRowValue = scope.row[attrs.name];
                        if (angular.isUndefined(scopeRowValue)) {
                            var newScopeObj = transclude.scope.$new();
                            transclude.transclude(newScopeObj, function (transcludeEl, transcludeScope) {
                                transcludeScope.row = scope.row;
                                element.append(transcludeEl);
                            });
                        } else {
                            transclude.transclude(scope, function (clone, scope) {
                                element.html(clone);
                            });
                        }
                    }
                }
            };
        }])
        .directive('xeRowInjector', [function () {
            var previousElement;
            return {
                restrict: 'A',
                replace: true,
                require: '^xeTableGrid',
                scope: true,
                link: function (scope, element, attrs, controllerInstance, $transclude) {
                    element.on("click", function (event) {
                        if (previousElement) {
                            previousElement.removeClass("active-row");
                        }
                        element.addClass("active-row");
                        previousElement = element;
                    });
                }
            };
        }])
        .directive('xeHeadingInjector', [function () {
            return {
                restrict: 'A',
                replace: true,
                require: '^xeTableGrid',
                scope: true,
                link: function (scope, element, attrs, controllerInstance, $transclude) {
                    var id = attrs.name;
                    var transclude = scope.headingTranscludes[id];
                    if (transclude) {
                        var newScopeObj = transclude.scope.$new();
                        transclude.transclude(newScopeObj, function (transcludeEl, transcludeScope) {
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
        .directive('xeCellMarkup', [function () {
            return {
                restrict: 'EA',
                transclude: 'element',
                replace: true,
                scope: true,
                require: "^xeTableGrid",
                link: function (scope, element, attrs, controllerInstance, $transclude) {
                    var directiveTransclude;

                    if (attrs.columnName && controllerInstance.registerTransclude) {
                        directiveTransclude = {
                            id: attrs.columnName,
                            transclude: $transclude,
                            element: element,
                            scope: scope
                        };
                        controllerInstance.registerTransclude(directiveTransclude);
                    } else if (attrs.headingName && controllerInstance.registerHeadingTransclude) {
                        directiveTransclude = {
                            id: attrs.headingName,
                            transclude: $transclude,
                            element: element,
                            scope: scope
                        };
                        controllerInstance.registerHeadingTransclude(directiveTransclude);
                    }

                }
            };
        }])
        .directive('attainMobileLayout', function () {
            // TODO: Check if using number as object name best practice. Its valid according to JS spec.
            var columnClasses = {1: "single-column", 2: "two-columns", 3: "all-columns"};
            return {
                restrict: "A",
                scope: true,
                link: function (scope, element, attrs) {
                    element.addClass(columnClasses[parseInt(attrs.attainMobileLayout)]);
                }
            };
        })
        .directive('droppable', ['$parse', function ($parse) {
            return {

                link: function (scope, element, attr) {

                }
            };
        }
        ])
        .directive('dragDrop', function () {
            return {
                link: function (scope, element, attr) {
                    var enterTarget = null,
                        index = element.index();

                    // Draggable column check
                    if (scope.draggableColumnNames.indexOf(scope.header[index].name) === -1) {
                        return;
                    }
                    element.attr("draggable", true);

                    function dragstart(event) {
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
                            left: dragX,
                            top: dragY
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

                        var fromHeader = event.originalEvent.dataTransfer.getData('text');
                        var toHeader = angular.element(event.target).closest('th').data('name');
                        var dropfn = attr.dragDrop;

                        scope.$apply(function () {
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
                $timeout(function () {
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

        .directive('tabIndex', [function () {
            return {
                link: function (scope, element, attrs) {
                    element.find('a, :input, [tabindex=0]')
                        .each(function (index, elem) {
                            // angular.element(elem).attr('tabindex', -1);
                        });
                }
            };
        }])

        /*
         Directive xeFocus is for keyBoard Navigation of Angular Grid component.
         Important point to note - Binding events to each gridCell and moving the focus to next element.
         JIRA's HRU-6719 and HRU-7041

         editableMode is the flag used to indicate whether the user has entered editable-mode in grid. this is set on press of ENTER and cleared on click of ESCAPE
         focus-ring is the class which provides the border of blue and showcase as the element is in focus.

         Few API of jquery used are $.inArray and $(elem).index(); to get the index of the element.

         On Click of Enter - Select entire row adding class 'active-row' and same class when navigating next row.
         Events handled in this directives are -
         LEFT
         RIGHT
         ENTER
         ESCAPE
         TAB
         SHIFT+TAB
         UP-ARROW
         DOWN-ARROW

         paginationPreviousGridCell is the variable to store the previous element where the focus was and when user clicks shift+tab then it should come back to paginationPreviousGridCell.
         */

        .directive('xeFocus', function () {
            var COLUMNHEADER = 'columnheader';
            var FOCUSRING = 'focus-ring';
            var ACTIVEROW = 'active-row';
            return {
                restrict: 'A',
                link: function ($scope, elem, attrs) {
                    elem.bind('keydown', function (e) {
                        var code = e.keyCode || e.which;
                        if (!editableMode) {
                          if (e.target.id !== 'undefined' && e.target.classList.contains('search')) {
                                switch (code) {
                                    case keys.tab:
                                        if (e.shiftKey) {
                                            clearFocus(e);
                                            return true;
                                        } else {
                                            clearFocus(e);
                                            var gridFirstRow = $(elem).closest('.table-container').find('.tbody tbody tr:first');
                                            var gridFirstRowFirstCell = $(elem).closest('.table-container').find('.tbody tbody tr:first td:first');
                                            $('tr.active-row').removeClass(ACTIVEROW);
                                            $(gridFirstRow).addClass(ACTIVEROW);
                                            $(gridFirstRowFirstCell).focus();
                                            $(gridFirstRowFirstCell).addClass(FOCUSRING);
                                            e.preventDefault();
                                            return false;
                                        }
                                        break;
                                }
                                return true;
                            } else {
                                switch (code) {
                                    case keys.left:
                                        if ($(this).closest("td").is(":first-child")) {
                                            $(elem).addClass(FOCUSRING)
                                        } else if ($(this).closest("th").is(":first-child")) {
                                            $(elem).addClass(FOCUSRING)
                                        } else {
                                            if ($(elem).hasClass(FOCUSRING)) {
                                                $(elem).removeClass(FOCUSRING);
                                            }
                                            $(elem).prev().focus();
                                            $(elem).prev().addClass(FOCUSRING);
                                        }
                                        e.preventDefault();
                                        return false;
                                        break;
                                    case keys.right:
                                        if ($(this).closest("td").is(":last-child")) {
                                            $(elem).addClass(FOCUSRING);

                                        } else if ($(this).closest("th").is(":last-child")) {
                                            $(elem).addClass(FOCUSRING);
                                        }
                                        else {
                                            if ($(elem).hasClass(FOCUSRING)) {
                                                $(elem).removeClass(FOCUSRING);
                                            }
                                            $(elem).next().focus();
                                            $(elem).next().addClass(FOCUSRING);
                                        }
                                        e.preventDefault();
                                        return false;
                                        break;
                                    case keys.enter:
                                        var inputElem = $(elem).find('a, :input');
                                        if (inputElem.length > 0) {
                                            editableMode = true;
                                            $('tr.active-row').removeClass(ACTIVEROW);
                                            $(elem).parent().addClass(ACTIVEROW);
                                            $(elem).addClass(FOCUSRING);
                                            $(inputElem[0]).focus();
                                            return false;
                                        }
                                        break;
                                    case keys.escape:
                                        editableMode = false;
                                        if ($(elem).hasClass(FOCUSRING)) {
                                            $(elem).removeClass(FOCUSRING);
                                        }
                                        if ($(elem).is(':input') || $(elem).is('a')) {
                                            $(elem).parent().focus();
                                            $(elem).parent().addClass(FOCUSRING);
                                        } else {
                                            $(elem).focus();
                                            $(elem).addClass(FOCUSRING);
                                        }
                                        e.preventDefault();
                                        return false;
                                        break;
                                    case keys.tab:
                                        if (e.shiftKey) {
                                            if ($(e.target).is('th') || $(e.target).is('td') ) {
                                                clearFocus(e);
                                                $('tr.active-row').removeClass(ACTIVEROW);
                                                //angular.element("#dataTableSearch").select().focus();
                                                angular.element('#'+$scope.searchConfig.id).select().focus();
                                            }
                                        }
                                        else {
                                            clearFocus(e);
                                            $('tr.active-row').removeClass(ACTIVEROW);
                                            var paginationControlDiv =$(elem).closest('.table-container').children().find('div.pagination-controls');
                                            var moveFocusToFirstElement = $(paginationControlDiv).find('input, button , select').not(':disabled');
                                            $(moveFocusToFirstElement[0]).focus();
                                            paginationPreviousGridCell = e.currentTarget;
                                        }
                                        e.preventDefault();
                                        return false;
                                        break;
                                    case keys.PAGE_UP:
                                        clearFocus(e);
                                        var currentIndex = $(elem).index();
                                        var gridFirstRow = $(elem).closest('.table-container').find('.tbody tbody tr:first');
                                        var selectedCell = $(gridFirstRow).children()[currentIndex];
                                        $(selectedCell).focus();
                                        $(selectedCell).addClass(FOCUSRING);
                                        return false;
                                        break;
                                    case keys.PAGE_DOWN:
                                        clearFocus(e);
                                        var currentIndex = $(elem).index();
                                        var gridLasttRow = $(elem).closest('.table-container').find('.tbody tbody tr:last');
                                        var selectedCell = $(gridLasttRow).children()[currentIndex];
                                        $(selectedCell).focus();
                                        $(selectedCell).addClass(FOCUSRING);
                                        return false;
                                        break;
                                    case keys.HOME:
                                        clearFocus(e);
                                        var firstColumnCell = $(elem).parent().find('td:first');
                                        $(firstColumnCell).focus();
                                        $(firstColumnCell).addClass(FOCUSRING);
                                        return false;
                                        break;
                                    case keys.END:
                                        clearFocus(e);
                                        var lastColumnCell = $(elem).parent().find('td:last');
                                        $(lastColumnCell).focus();
                                        $(lastColumnCell).addClass(FOCUSRING);
                                        return false;
                                        break;
                                    case (keys.downArrow):
                                        if (undefined === e.target.attributes['role'] || COLUMNHEADER !== e.target.attributes['role'].nodeValue) {
                                            var allElements = $(elem).parent().find('td');
                                            if ($(allElements).hasClass(FOCUSRING)) {
                                                $(allElements).removeClass(FOCUSRING);
                                            }
                                            if (allElements.length > 0) {
                                                var colIndex = $(elem).index();
                                                var nextRow = $(elem).parent().next();
                                                if (nextRow.length > 0) {
                                                    $('tr.active-row').removeClass(ACTIVEROW);
                                                    $(nextRow).addClass(ACTIVEROW);
                                                    var nextColums = $(nextRow).find('td');
                                                    if ($(nextColums[colIndex]).is(':input') || elem.is('a')) {
                                                        $(nextColums[colIndex]).parent().focus();
                                                        $(nextColums[colIndex]).parent().addClass(FOCUSRING);

                                                    } else {
                                                        $(nextColums[colIndex]).focus();
                                                        $(nextColums[colIndex]).addClass(FOCUSRING);
                                                    }
                                                } else {
                                                    $(elem).addClass(FOCUSRING);
                                                }
                                            }
                                            e.preventDefault();
                                            return false;
                                        } else {
                                            var allElements = $(elem).parent().find('th');
                                            $('th.focus-ring').removeClass(FOCUSRING);
                                            if (allElements.length > 0) {
                                                $('tr.active-row').removeClass(ACTIVEROW);
                                                var colIndex = $(elem).index();
                                                var gridTableBody = $(elem).closest('.table-container').find('tbody');
                                                var focusGridCell = gridTableBody.find('tr:first td')[colIndex];
                                                $(focusGridCell).parent().addClass(ACTIVEROW);
                                                var newtd = $(focusGridCell).parent().find('td');
                                                $(newtd[colIndex]).addClass(FOCUSRING);
                                                $(focusGridCell).focus();
                                            }
                                            return false;
                                        }
                                        break;
                                    case (keys.upArrow):
                                        var colIndex;
                                        if (elem.closest('tr').index() > 0) {
                                            $('tr.active-row').removeClass(ACTIVEROW);
                                            var allElements = $(elem).parent().find('td');
                                            if ($(allElements).hasClass(FOCUSRING)) {
                                                $(allElements).removeClass(FOCUSRING);
                                            }
                                            if (allElements.length > 0) {
                                                colIndex = $(elem).index();
                                                var prevRow = $(elem).parent().prev();
                                                var prevColums = $(prevRow).find('td');
                                                if ($(prevColums[colIndex]).is(':input') || $(elem).is('a')) {
                                                    $(prevColums[colIndex]).parent().focus();
                                                    $(prevColums[colIndex]).parent().addClass(FOCUSRING);
                                                } else {
                                                    $(prevColums[colIndex]).focus();
                                                    $(prevColums[colIndex]).addClass(FOCUSRING);
                                                }
                                                $(prevRow).addClass(ACTIVEROW);
                                                $(prevRow[colIndex]).removeClass(FOCUSRING);
                                            }
                                            e.preventDefault();
                                            return false;
                                        } else if ($(elem).closest('tr').index() === 0) {
                                            $('tr.active-row').removeClass(ACTIVEROW);
                                            $('td.focus-ring').removeClass(FOCUSRING);
                                            var trPosition = $(elem).closest('tr').index();
                                            var tdPosition = $(elem).closest('td').index();
                                            if (tdPosition !== -1) {
                                                var headArr = $(elem).closest('.table-container').find('thead');
                                                $(headArr[0]).find('tr').find('th')[tdPosition].focus();
                                                var newtd = $(headArr[0]).find('tr').find('th')[tdPosition];
                                                $(newtd).addClass(FOCUSRING);
                                            }
                                            e.preventDefault();
                                            return false;
                                        }
                                        break;
                                }
                            }
                        } else if (editableMode) {
                            switch (code) {
                                case keys.enter:
                                    var inputElem = $(elem).find('a, :input');
                                    if (inputElem.length > 0) {
                                        editableMode = true;
                                        $(elem).addClass(FOCUSRING);
                                        $(inputElem[0]).focus();
                                        return false;
                                    }
                                    break;
                                case keys.escape:
                                    editableMode = false;
                                    if ($(elem).hasClass(FOCUSRING)) {
                                        $(elem).removeClass(FOCUSRING);
                                    }
                                    if ($(elem).is(':input') || $(elem).is('a')) {
                                        $(elem).parent.focus();
                                        $(elem).parent().addClass(FOCUSRING);
                                    } else {
                                        $(elem).focus();
                                        $(elem).addClass(FOCUSRING);
                                    }
                                    e.preventDefault();
                                    return false;
                                    break;
                                case keys.tab:
                                    if (e.shiftKey) {
                                        var closestColumn;
                                        var allActionableElementInRow = $(elem).parent().find('a, :input');
                                        var currentFocussedElement = $(elem).find('a, :input');
                                        var inputElementIndex = $.inArray(currentFocussedElement[currentFocussedElement.length - 1], allActionableElementInRow);
                                        if (inputElementIndex > 0) {
                                            if ($(elem).hasClass(FOCUSRING)) {
                                                $(elem).removeClass(FOCUSRING);
                                            }
                                            var moveFocus = allActionableElementInRow[inputElementIndex - 1];
                                            inputElementIndex--;
                                            closestColumn = $(moveFocus).closest('td');
                                            $(closestColumn).addClass(FOCUSRING);
                                            moveFocus.focus();
                                        } else {
                                            var prevParent = elem.parent().prev();
                                            if (prevParent.length > 0) {
                                                if (elem.hasClass(FOCUSRING)) {
                                                    elem.removeClass(FOCUSRING);
                                                }
                                                var allActionableElementInRow = prevParent.find('a, :input');
                                                if (allActionableElementInRow.length > 0) {
                                                    var moveFocus = allActionableElementInRow[allActionableElementInRow.length - 1];
                                                    closestColumn = $(moveFocus).closest('td');
                                                    $(closestColumn).addClass(FOCUSRING);
                                                    $(moveFocus).focus();
                                                }
                                            }
                                        }

                                        return false;
                                    } else {
                                        var closestColumn;
                                        var allActionableElementInRow = $(elem).parent().find('a, :input');
                                        var currentFocussedElement = $(elem).find('a, :input');
                                        var inputElementIndex = $.inArray(currentFocussedElement[0], allActionableElementInRow);
                                        if (inputElementIndex < allActionableElementInRow.length - 1) {
                                            if ($(elem).hasClass(FOCUSRING)) {
                                                $(elem).removeClass(FOCUSRING);
                                            }
                                            var moveFocus = allActionableElementInRow[inputElementIndex + 1];
                                            closestColumn = $(moveFocus).closest('td');
                                            $(closestColumn).addClass(FOCUSRING);
                                            inputElementIndex++;
                                            $(moveFocus).focus();
                                        } else {
                                            var nextParent = $(elem).parent().next();
                                            if (nextParent.length > 0) {
                                                if ($(elem).hasClass(FOCUSRING)) {
                                                    $(elem).removeClass(FOCUSRING);
                                                }
                                                var allActionableElementInRow = nextParent.find('a, :input');
                                                if (allActionableElementInRow.length > 0) {
                                                    var moveFocus = allActionableElementInRow[0];
                                                    closestColumn = $(moveFocus).closest('td');
                                                    $(closestColumn).addClass(FOCUSRING);
                                                    $(moveFocus).focus();
                                                }
                                            } else {
                                                if ($(elem).hasClass(FOCUSRING)) {
                                                    $(elem).removeClass(FOCUSRING);
                                                }
                                                var allPreviousRows = $(elem).parent().prevAll();
                                                if (allPreviousRows.length > 0) {
                                                    var firstRow = allPreviousRows[allPreviousRows.length - 1];
                                                    var allActionableElements = $(firstRow).find(':input, a');
                                                    if (allActionableElements.length > 0) {
                                                        $(allActionableElements[0]).focus();
                                                    }
                                                    closestColumn = $(allActionableElements[0]).closest('td');
                                                    $(closestColumn).addClass(FOCUSRING);
                                                }
                                            }
                                        }
                                    }
                                    return false;
                                    break;
                                case (keys.downArrow):
                                    //do nothing
                                    return false;
                                    break;
                                case (keys.upArrow):
                                    //do nothing
                                    return false;
                                    break;
                            }
                        }

                    });
                }

            }
        })
        .directive('xeClickGrid', function(){
            return{
                restrict:'A',
                link: function ($scope, elem, attrs) {
                    //Binding Click event for removing focus-ring class.
                    elem.bind('click', function (e) {
                        clearFocus(e);
                        $('tr.active-row').removeClass('active-row');
                        editableMode = false;
                        $(elem).addClass('focus-ring');
                        $(elem).parent().addClass('active-row');
                    });
                }

            }
        })
        .directive('xeGridPaginationControl',function(){
            return {
                restrict: 'A',
                link: function ($scope, elem, attrs) {
                    //Binding Click event for removing focus-ring class.
                    elem.bind('keydown', function (e) {
                        clearFocus(e);
                        var code = e.keyCode || e.which;
                        var paginationControlDiv =$(elem).closest('.table-container').children().find('div.pagination-controls');
                        var moveFocusToFirstElement = $(paginationControlDiv).find('input, button , select').not(':disabled');
                        var inputElementIndex = $.inArray(e.target, moveFocusToFirstElement);
                        if (inputElementIndex === 0) {
                            if (e.shiftKey) {
                                switch (code) {
                                    case keys.tab:
                                        if ($(paginationPreviousGridCell).is(':visible') && $(elem).parent('.table-container').find(paginationPreviousGridCell)) {
                                            $(paginationPreviousGridCell).focus();
                                            $(paginationPreviousGridCell).parent().addClass('active-row');
                                            $(paginationPreviousGridCell).addClass('focus-ring');
                                        } else {
                                            var associatedTable = $(elem).parent('.table-container');
                                            var gridFirstRow = $(associatedTable).find('tbody tr:first');
                                            var gridFirstRowFirstCell = $(associatedTable).find('tbody tr:first td:first');
                                            $(gridFirstRow).addClass('active-row');
                                            $(gridFirstRowFirstCell).focus();
                                            $(gridFirstRowFirstCell).addClass('focus-ring');
                                        }
                                        e.preventDefault();
                                        return false;

                                        break;
                                    case keys.downArrow:
                                        return true;
                                        break;
                                    case keys.upArrow:
                                        return true;
                                        break;
                                }
                            }
                            else {
                                return true;
                            }
                        }
                    });
                }
            }
        })


}());

clearFocus = function (e) {
    if ($('table td.focus-ring').hasClass('focus-ring')) {
        $('table td.focus-ring').removeClass('focus-ring');
    }
    if ($('table th.focus-ring').hasClass('focus-ring')) {
        $('table th.focus-ring').removeClass('focus-ring');
    }
    return true;
};


(function () {
    'use strict';

    // The about Modal consumes aboutService which has business logic. We can share same data to entire application
    // aboutService has two configurable settings. Those are method and url
    // And the about Modal has configurable api object with title, name, version, general category, plugin category, other plugin category and copyright
    // Which is enable to configure your api key name's to this component.

    angular.module('aboutModal', [])
        .provider('aboutService', function () {
            this.method = "GET";
            this.backendUrl = "";
            this.setMethod = function (method) {
                if (method) {
                    this.method = method;
                }
            };
            this.setBackendUrl = function (newUrl) {
                if (newUrl) {
                    this.backendUrl = newUrl;
                }
            };
            this.$get = ['$http', '$templateCache', function ($http, $templateCache) {
                var self = this,
                    service;

                service = {
                    aboutInfo: function () {
                        return $http({method: self.method, url: self.backendUrl, cache: $templateCache});
                    }
                };

                return service;
            }];
        })
        .directive('xeAboutModal', ['aboutService', function (aboutService) {
            return {
                restrict: 'EA',
                scope: {
                    show: '=',
                    api: '='
                },
                replace: true, // Replace with the template below
                transclude: true, // we want to insert custom content inside the directive
                templateUrl: 'templates/dialog_default.html',
                controller: ['$scope', function ($scope) {
                    $scope.hide = function () {
                        $scope.show = false;
                    };
                    aboutService.aboutInfo()
                        .then(function (response) {
                            $scope.status = response.status;
                            $scope.about = response.data;
                        }, function (response) {
                            if (response.status === 500) {
                                aboutService.aboutInfo()
                                    .then(function (response) {
                                        $scope.status = response.status;
                                        $scope.about = response.data;
                                    }, function (response) {
                                        $scope.about = response.data || "Request failed";
                                        $scope.status = response.status;
                                    });
                            } else {
                                $scope.about = response.data || "Request failed";
                                $scope.status = response.status;
                            }
                        });
                }],
                link: function (scope, ele) {
                    ele.on('keydown', function (event) {
                        if (event.keyCode === 27 || (document.activeElement.className === "xe-modal-close" && event.keyCode === 13)) {
                            scope.show = false;
                            scope.$apply();
                        }
                        if (!event.shiftKey && event.keyCode === 9) {
                            if ("xe-modal-footer ng-binding" === document.activeElement.className) {
                                event.preventDefault();
                                angular.element('#xeModalClose').focus();
                            }
                        }
                        if (event.shiftKey && event.keyCode === 9) {
                            if ("xe-modal-footer ng-binding" === document.activeElement.className) {
                                event.preventDefault();
                                angular.element('#xeModalHeader').focus();
                            }
                            if ("xe-modal-mask ng-isolate-scope" === document.activeElement.className) {
                                event.preventDefault();
                                angular.element('#xeModalFooter').focus();
                            }
                            if ("xe-modal-close" === document.activeElement.className) {
                                event.preventDefault();
                                angular.element('#xeModalFooter').focus();
                            }
                        }

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
                    table.append('thead').attr('class', 'xe-pie-table-head');
                    table.append('tbody').attr('class', 'xe-pie-table-body');
                }

                svg.selectAll('g').remove();

                svg.append("text")
                    .attr("text-anchor", "middle")
                    .attr('font-size', '4em')
                    .attr('y', 20)
                   .text(110);

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

                    scope.valueforclick=d;
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

                function toggle(d) {
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
                    text.on("click", function(d) {
                       alert("sdad");
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
                    .innerRadius(radius * 0.25)
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
                     $(".xe-pie-slice").click(function(d){
                           scope.ngclickfunction({
                            names: scope.valueforclick
                        });
                    });
             }

            return {
                restrict: 'E',
                scope: {
                    data: '=xePieData',
                    config: '=xePieConfig',
                    ngclickfunction: "&",
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
/*****************************************************
 *  © 2016-2018 Ellucian Company L.P. and its affiliates. *
 *****************************************************/

; // safe for iife
(function () {

    angular.module('pieChartModule', [])
        .factory('d3', ['$window', '$log', function ($window, $log) {
            if (!$window.d3) {
                $log.error('D3 must be present');
            }
            return $window.d3;
        }])
        .directive('xePieChart', ['d3', '$window', '$log', '$timeout', 'Language','$filter','getlocale', function (d3, $window, $log, $timeout, Language,$filter,getlocale) {

            var uniqueId = 0;

            function localizedNumber(input){
                if(Language.isRtl()) {
                    return input;
                }
                else {
                    return (+input).toLocaleString(getlocale.getUserLocale().replace('_', '-'));
                }
            }

            function draw(tooltip, table, svg, radius, scope, options, element) {

                // var declaration
                var subdata = [], other, subdatatotal, total, chartTotal,
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
                svg.selectAll("text").remove();

                if(options.innerRadiusFactor>0) {
                    chartTotal = d3.sum(scope.data, function (d) {
                        return d.value;
                    });
                    var innerText = svg.append("text")
                        .attr("text-anchor", "middle");
                    innerText.append("tspan")
                        .attr("class","xe-pie-total-value")
                        .attr("y",0).attr("x",0).attr("tabindex","0")
                        .text(localizedNumber(chartTotal));
                    innerText.append("tspan")
                        .attr("class", "xe-pie-total-label")
                        .attr("y", 20).attr("x", 0).attr("tabindex","0")
                        .text(options.pie.totalLabel);
                }

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
                                if (subdatatotal || row[dtValue]) {
                                    row[dtPercentage] = (Math.round(1000 * row[dtValue] / subdatatotal) / 10).toFixed(2);
                                } else {
                                    row[dtPercentage] = '0.00';
                                }

                                return {column: column, value: row[column]};
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
                        tooltip.select('.xe-pie-tooltip-value').html(localizedNumber(d.data.value));
                        if(Language.isRtl())
                            tooltip.select('.xe-pie-tooltip-percent').html( $filter('xei18n')('default.percent')+localizedNumber(d.data.percentage));
                        else
                            tooltip.select('.xe-pie-tooltip-percent').html(localizedNumber(d.data.percentage)+ $filter('xei18n')('default.percent'));
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

                function wrap(text) {
                    var width = 45  ;
                    text.each(function() {
                        var text = d3.select(this),
                            words = text.text().split(/\s+/).reverse(),
                            word,
                            line = [],
                            y = text.attr("y"),
                            dy = parseFloat(text.attr("dy")),
                            tspan = text.text("").append("tspan").attr("x", 0).attr("dy", dy + "em");
                        while (word = words.pop()) {
                            line.push(word);
                            tspan.text(line.join(" "));
                            if ((tspan.node().getComputedTextLength()>0? tspan.node().getComputedTextLength():(tspan.text().length*5)) > width) {
                                if(line.length>1)
                                {
                                    line.pop();
                                    tspan.text(line.join(" "));
                                    line = [word];
                                    tspan = text.append("tspan").attr("x", 0).attr("dy", dy + "em").text(word);
                                }
                                else
                                {
                                    line=[];
                                    tspan = text.append("tspan").attr("x", 0).attr("dy", dy + "em").text("");
                                }

                            }
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

                function passSliceInfo(d,i) {
                    scope.ngclickfunction({
                        pieSliceData: d.data
                    });
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
                        .attr("tabindex","0")
                        .attr("aria-label",function (d){
                            var ariaLabel;
                            if(Language.isRtl())
                                ariaLabel =  $filter('xei18n')('default.percent')+localizedNumber(d.data.percentage);
                            else
                                ariaLabel = localizedNumber(d.data.percentage)+ $filter('xei18n')('default.percent');
                            ariaLabel += " " + d.data.label + " " + d.data.value;
                            return ariaLabel;
                        })
                        .on('click', toggle)
                        .on('click',passSliceInfo)
                        .on('keydown',function (d,i) {
                            if(d3.event.keyCode==13) {
                                passSliceInfo(d,i);
                            }
                        })
                        .on('touchstart', toggle)
                        .on('mouseover', mouseover)
                        .on('touchstart', mouseover)
                        .on('touchstart', touchTarget)
                        .on('mouseout', mouseout)
                        .on('touchend', mouseout)
                        .on('touchend', touchTarget)
                        .on('mousemove', mousemove)
                        .on('touchmove', mousemove)
                        .on('focusin',function() {
                            d3.event.target.classList.add("xe-slice-on");
                        })
                        .on('focusout',function() {
                            d3.event.target.classList.remove("xe-slice-on");
                        });

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
                            if(Language.isRtl())
                                return  $filter('xei18n')('default.percent')+ localizedNumber(d.data.percentage);
                            else
                                return  localizedNumber(d.data.percentage) + $filter('xei18n')('default.percent');
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
                        .style('fill', '#DBDBDD')
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
                            if(Language.isRtl())
                                return  $filter('xei18n')('default.percent') + localizedNumber(d.data.percentage);
                            else
                                return localizedNumber(d.data.percentage) + $filter('xei18n')('default.percent');
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
                    if (subdatatotal || d.value) {
                        d.percentage = (Math.round(1000 * d.value / subdatatotal) / 10).toFixed(2);
                    }
                    else {
                        d.percentage = '0.00';
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
                    .innerRadius(radius* 0.5 * options.innerRadiusFactor)
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
                        '#BA46A6',
                        '#5353d1',
                        '#51ABFF',
                        '#1ABF96',
                        '#95D162',
                        '#F5DB75',
                        '#FFB581'
                    ]);

                subColor = d3.scale.ordinal()
                    .range([
                        '#FFF4EB',
                        '#FFDFC8',
                        '#FFCAA4',
                        '#FFB581',
                        '#FFA15D',
                        '#FF8C3A',
                        '#FF7513',
                        '#FE6A00',
                        '#EB6200',
                        '#C35100'
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
                    config: '=xePieConfig',
                    ngclickfunction: "&"
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
                            innerRadiusFactor:0,
                            svg: {
                                title: '',
                                desc: ''
                            },
                            pie: {
                                otherLabel: 'Other',
                                totalLabel: 'Units'
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
                    height = width ;
                    radius = Math.min(width, height) / 2;

                    s = d3.select(element[0]).classed("svg-container", true).append('svg')
                        .attr({
                            'xmlns': "http://www.w3.org/2000/svg",
                            'xmlns:xlink': "http://www.w3.org/1999/xlink",
                            'version': 1.1,
                            'width': width,
                            'height': height,
                            'viewBox': '0 0 ' + Math.min(width, height) + ' ' + Math.min(width, height),
                            'preserveAspectRatio': 'xMinYMid meet',
                            'role': 'group',
                            'aria-labelledby': 'title' + uniqueId + ' ' + 'desc' + uniqueId
                        }).classed("svg-content-responsive", true);

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

                    // Watch the data attribute of the scope
                    scope.$watch('data', function (newVal, oldVal, scope) {

                        // Update the chart
                        if (newVal!=oldVal) {
                            draw(tooltip, table, svg, radius, scope, options, element);
                        }
                    }, true);

                    draw(tooltip, table, svg, radius, scope, options, element);
                }
            };
        }]);
}());
(function () {
    'use strict';

    angular.module('popupModal', ['xe-ui-components'])
        .directive('xePopupModal', ['$timeout','$compile', function ($timeout, $compile) {
            return {
                restrict: 'EA',
                scope: {
                    show: '=',
                    pageheader: '@',
                    focusbackelement:'@'
                },
                replace: true, // Replace with the template below
                transclude: {
                    popupContent:'?popupContent',
                    popupButtons:'?popupButtons'
                }, // we want to insert custom content inside the directive
                templateUrl: 'templates/modal.html',
                controller: ['$scope', function ($scope) {
                    $scope.hide = function () {
                        $scope.show = false;
                    };
                    $scope.popupTitle = $scope.pageheader;
                    $scope.focusBackElement = $scope.focusbackelement;
                }],

                link: function (scope, ele) {
                    // Keyboard Navigation
                    angular.element('#xeModalMask').attr('tabindex', 0).focus();
                    ele.on('keydown', function (event) {
                        //ESC Key and Enter
                        if (event.keyCode === 27 || (document.activeElement.className === "xe-popup-close" && event.keyCode === 13)) {
                            scope.show = false;
                            scope.$apply();
                            if(null!=scope.focusBackElement && undefined!=scope.focusBackElement) {
                                angular.element("#" + scope.focusBackElement).focus();
                            }
                        }

                    });

                    $timeout(function () {
                        angular.element("#tabOrdering").on('focus', function (event) {
                            angular.element("#xePopupClose").focus();
                        });

                        angular.element("#xePopupClose").on('keydown', function (event) {
                            if (event.shiftKey) {
                                if (event.keyCode === 9) {
                                    if (angular.element("#buttonContainer").find('xe-button').length > 1) {
                                        angular.element("#buttonContainer").find('xe-button:last').closest('xe-button').focus();
                                    } else {
                                        angular.element("#buttonContainer").find('xe-button').focus();
                                    }
                                }
                            } else if (event.keyCode === 9) {
                                return true;
                            }
                            if (event.keyCode === 27 || (document.activeElement.className === "xe-popup-close" && event.keyCode === 13)) {
                                scope.show = false;
                                scope.$apply();
                                if(null!=scope.focusBackElement && undefined!=scope.focusBackElement) {
                                    angular.element("#" + scope.focusBackElement).focus();
                                }
                            }
                            event.preventDefault();
                            event.stopImmediatePropagation();
                        });

                    }, 100);

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
                scope: {
                    xeSection: '@?'
                },
                transclude: true,
                templateUrl: 'templates/tabNav.html',
                controllerAs: 'tabnav',
                controller: ['$compile', '$sce', '$q', '$scope', '$state', function ($compile, $sce, $q, $scope, $state) {
                    var self = this;
                    self.currentActive = null;
                    self.tabs = [];
                    self.extendTab = function(attr, tabScope) {
                        var hideTab;
                        if (typeof xe !== 'undefined' && xe.extensionsFound) {
                            var tabSectionExtns = _.find(xe.extensions.sections, function (section) {
                                return section.name == $scope.xeSection;
                            });
                            if (tabSectionExtns) {
                                tabSectionExtns.fields.forEach(function (field, key) {
                                    if (field.name === tabScope.xeField) {
                                        if (field.label) {
                                            attr.heading = xe.i18n(field.label);
                                        }
                                        if (field.exclude) {
                                            hideTab = true;
                                        }
                                    }
                                });
                            }
                        }
                        if(!hideTab) {
                            self.tabs.push(tabScope);
                        }
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
                    jsLazyLoad: '@',
                    xeField: '@?'
                },
                templateUrl: 'templates/tabPanel.html',
                require: '^xeTabNav',
                link: function (scope, ele, attr, xeTabNavCtrl, $transclude) {
                    xeTabNavCtrl.extendTab(attr, scope);
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
/*jslint browser: true*/
(function () {
    'use strict';

    angular.module('timePicker', ['xe-ui-components', 'ngMessages','dateParser'])
        .directive('xeTimePicker', ['$timeout', '$filter', '$window', '$document', 'Language', 'keyCodes', '$dateParser','dateFilter', function ($timeout, $filter, $window, $document, Language, keyCodes, $dateParser,$timeFilter) {
            return {
                restrict: 'E',
                scope: {
                    displayFormat: '@?',
                    xeConfigstep: '@?',
                    xeId: '@',
                    xeName: '@',
                    xePlaceholder: '@?',
                    modelScope: '=?xeModel',
                    returnFormat: '@?',
                    returnTranslatedMeridian:'@?',
                    xeWidth: '@?'
                },
                replace: true, // Replace with the template below
                templateUrl: Language.isRtl() ? 'templates/timePicker_rtl.html' : 'templates/timePicker.html',
                controller: ['$scope', function ($scope) {
                    $scope.timeObject = {};
                    $scope.toggleContainer = false;
                    $scope.hours = [];
                    $scope.minutes = [];
                    if (!$scope.displayFormat) {
                        $scope.displayFormat = $filter('xei18n')('default.time.format');
                        if ('default.time.watermark.format' != $filter('xei18n')('default.time.watermark.format')) {
                            $scope.xeTimePlaceholder = $filter('xei18n')('default.time.watermark.format');
                        }
                    } else {
                        $scope.xeTimePlaceholder = $scope.xePlaceholder;
                        //$scope.displayModel = $scope.modelScope;
                    }
                    if ($scope.modelScope === undefined || $scope.modelScope === null ){
                        $scope.modelScope="";
                    }
                    $scope.displayModel = $scope.modelScope;
                    $scope.minFocusElem = "";
                    $scope.hrsFocusElem = "";
                    $scope.showError = false;
                    $scope._12hrFormat = 'hh:mm a';
                    $scope._24hrFormat = 'HH:mm';

                    $scope.formatNumberTwoDecimal = function (num) {
                        if (num != "") {
                            num = new Intl.NumberFormat('en-US', {minimumIntegerDigits: 2}).format(num);
                        }
                        return num;
                    };

                    $scope.formatTimeTo12Hours = function (inputValue, returnTranslated) {
                        var formattedValue;
                        if (inputValue) {
                            var meridian;
                            meridian = inputValue.split(" ")[1];
                            if (returnTranslated) {
                                if (meridian === "PM") {
                                    meridian = $scope.meridiansList[1];
                                } else if (meridian === "AM") {
                                    meridian = $scope.meridiansList[0];
                                }
                            } else {
                                if (meridian === $scope.meridiansList[1]) {
                                    meridian = "PM";
                                } else if (meridian === $scope.meridiansList[0]) {
                                    meridian = "AM";
                                }
                            }
                            formattedValue = inputValue.split(" ")[0] + " " + meridian;
                        }
                        return formattedValue;
                    };

                    $scope.formatEnteredTime = function (timeData, inputformat, returnformat) {
                            var formattedDate = $dateParser(timeData, inputformat);
                        return $timeFilter(formattedDate, returnformat);
                    };


                    var userAgent = $window.navigator.userAgent;
                    var browsers = {
                        chrome: /chrome/i,
                        safari: /safari/i,
                        firefox: /firefox/i,
                        ie: /internet explorer/i
                    };

                    $scope.getBrowser = function () {
                        for (var key in browsers) {
                            if (browsers[key].test(userAgent)) {
                                return key;
                            }
                        }
                    };

                    if (!$scope.xeConfigstep) {
                        var num = new Intl.NumberFormat('en-US', {minimumIntegerDigits: 2}).format(1);
                        $scope.xeConfigstep = num;
                    }
                    function timepickerConstructor() {
                        $scope.timeObject.hours = [];
                        $scope.timeObject.minutes = [];
                        $scope.timeObject.configstep;
                        $scope.timeObject.is24Hrformat;
                        $scope.timeObject.returnFormat;
                    }

                    $scope.timeObject = new timepickerConstructor();
                    $scope.timeObject.displayFormat = $scope.displayFormat;
                    $scope.timeObject.returnFormat = $scope.returnFormat;


                    if (null != $scope.timeObject.displayFormat && "undefined" != $scope.timeObject.displayFormat) {
                        var lengthOfFormat = $scope.timeObject.displayFormat.split(" ").length;
                        if (lengthOfFormat === 2) {
                            //12 hours format
                            $scope.maxHrs = 12;
                            $scope.minHr = 1;
                            $scope.timeObject.is24Hrformat = false;
                        } else {
                            //24 hours format
                            $scope.maxHrs = 23;
                            $scope.minHr = 0;
                            $scope.timeObject.is24Hrformat = true;
                        }
                        $scope.maxMinutes = 59;
                        $scope.timeObject.configstep = $scope.xeConfigstep;
                        populateTimeObject();
                    }

                    if (null != $scope.returnFormat && "undefined" != $scope.returnFormat) {
                        var lengthOfFormat = $scope.returnFormat.split(" ").length;
                        if (lengthOfFormat === 2) {
                            //12 hours format
                            $scope.timeObject.returnFormat = 12;
                        } else {
                            //24 hours format
                            $scope.timeObject.returnFormat = 24;
                        }
                    }

                    if (null != $scope.displayModel && "undefined" != $scope.displayModel) {
                        var formatLength = $scope.displayModel.split(" ").length;
                        if (formatLength === 2) {
                            //12 hours format
                            $scope.timeObject.format12 = "Y";
                        } else {
                            $scope.timeObject.format12 = "N";
                            //24 hours format
                        }
                    } else {
                        $scope.timeObject.format12 = null;
                    }

                    if($scope.timeObject.is24Hrformat==false && $scope.timeObject.format12 == 'Y'){
                        $scope.timeObject.format12 = 'Y';
                    }else if($scope.timeObject.is24Hrformat==true && $scope.timeObject.format12 == 'N'){
                        $scope.timeObject.format12 = 'N';
                    }

                    function populateTimeObject() {
                        var defaultHoursConfigStep = 1;
                        $scope.timeObject.hours = creatingTimeData($scope.maxHrs, defaultHoursConfigStep);
                        $scope.timeObject.minutes = creatingTimeData($scope.maxMinutes, $scope.timeObject.configstep);
                    }

                    function creatingTimeData(maxcount, configstep) {
                        var listofItems = [];
                        var maxElement = parseInt(0);
                        configstep = parseInt(configstep);
                        if (maxcount === 12) {
                            maxElement = 1;
                        }
                        if (configstep) {
                            if (configstep > 60 || configstep < 0) {
                                configstep = 1
                            }
                        } else {
                            configstep = 1;
                        }
                        while (maxElement <= maxcount) {
                            var num = new Intl.NumberFormat('en-US', {minimumIntegerDigits: 2}).format(maxElement);
                            listofItems.push(num);
                            maxElement = maxElement + configstep;
                        }
                        return listofItems;
                    }

                    $scope.meridiansList = [$filter('xei18n')('default.time.am'), $filter('xei18n')('default.time.pm')];

                    $scope.commonIncrement = function (currentIndex, currentObj, isHours) {
                        if (!isHours) {
                            if (parseInt(currentIndex) === 0 || parseInt(currentIndex) === -1) {
                                nextCommonIncrement(currentIndex, currentObj, $scope.timeObject.minutes, isHours);
                                setPreviousValues(currentIndex, currentObj);
                            } else if (parseInt(currentIndex) > 0 && parseInt(currentIndex) < $scope.timeObject.minutes.length - 2) {
                                nextCommonIncrement(currentIndex, currentObj, $scope.timeObject.minutes, isHours);
                                setPreviousValues(currentIndex, currentObj)
                            } else if (parseInt(currentIndex) > $scope.timeObject.minutes.length - 2 || parseInt(currentIndex) === $scope.timeObject.minutes.length - 2) {
                                nextCommonIncrement(currentIndex, currentObj, $scope.timeObject.minutes, isHours);
                                var prevElementInDiv = parseInt(currentIndex); //doesn't go below scope.hours[0]
                                if (parseInt(currentIndex) !== $scope.timeObject.minutes.length - 1) {
                                    var prevMinUL = angular.element(currentObj).closest('ul');
                                    var prevContent = angular.element(prevMinUL).find('li')[prevElementInDiv].innerText;
                                    var wrapDiv = angular.element(currentObj).closest('div');
                                    angular.element(wrapDiv).find('div.previousDiv').text("");
                                    angular.element(wrapDiv).find('div.previousDiv').text(prevContent);
                                }

                            }
                        } else {
                            if (parseInt(currentIndex) === 0 || parseInt(currentIndex) === -1) {
                                nextCommonIncrement(currentIndex, currentObj, $scope.timeObject.hours, isHours);
                                setPreviousValues(currentIndex, currentObj);
                            } else if (parseInt(currentIndex) > 0 && parseInt(currentIndex) < $scope.timeObject.hours.length - 2) {
                                nextCommonIncrement(currentIndex, currentObj, $scope.timeObject.hours, isHours);
                                setPreviousValues(currentIndex, currentObj);
                            } else if (parseInt(currentIndex) > $scope.timeObject.hours.length - 2 || parseInt(currentIndex) === $scope.timeObject.hours.length - 2) {
                                nextCommonIncrement(currentIndex, currentObj, $scope.timeObject.hours, isHours);
                                var prevElementInDiv = parseInt(currentIndex); //doesn't go below scope.hours[0]
                                if (parseInt(currentIndex) !== $scope.timeObject.hours.length - 1) {
                                    var prevHRUL = angular.element(currentObj).closest('ul');
                                    var prevContent = angular.element(prevHRUL).find('li')[prevElementInDiv].innerText;
                                    var wrapDiv = angular.element(currentObj).closest('div');
                                    angular.element(wrapDiv).find('div.previousDiv').text("");
                                    angular.element(wrapDiv).find('div.previousDiv').text(prevContent);
                                }
                            }
                        }
                    };

                    function nextCommonIncrement(currentIndex, currentObj, arrayList, isHours) {
                        // nextElementFocus shouldn't exceed 24 or 12 that is length of the scope.hours.
                        var nextElementFocus = parseInt(currentIndex) + 1;
                        if (nextElementFocus < arrayList.length) {
                            var nextHRUL = angular.element(currentObj).closest('ul');
                            angular.element(nextHRUL).find('li').each(function (ind, eachLi) {
                                $(eachLi).attr('tabindex', -1);
                            });
                            angular.element(nextHRUL).find('li').siblings().removeClass('active-timepicker');
                            var setTabToFocus = angular.element(nextHRUL).find('li')[nextElementFocus];
                            if (isHours) {
                                $scope.hrsFocusElem = setTabToFocus;
                            } else {
                                $scope.minFocusElem = setTabToFocus;
                            }
                            angular.element(setTabToFocus).addClass('active-timepicker');
                            angular.element(setTabToFocus).attr('tabindex', 0);
                            angular.element(setTabToFocus).focus();
                        }
                        var nextElementInDiv = parseInt(nextElementFocus) + 1;
                        var wrapDiv = angular.element(currentObj).closest('div');
                        angular.element(wrapDiv).find('div.nextDiv').text("");
                        if (parseInt(currentIndex) > arrayList.length - 2 || parseInt(currentIndex) === arrayList.length - 2) {
                            angular.element(wrapDiv).find('div.nextDiv').text("");
                        } else {
                            var nextHRUL = angular.element(currentObj).closest('ul');
                            angular.element(wrapDiv).find('div.nextDiv').text("");
                            if (angular.element(nextHRUL).find('li')) {
                                var nextContent = angular.element(nextHRUL).find('li')[nextElementInDiv].innerText;
                                angular.element(wrapDiv).find('div.nextDiv').text(nextContent);
                            }
                        }
                    }

                    $scope.commonDecrement = function (currentIndex, currentObj, isHours) {
                        var focusIndex = currentIndex - 1;
                        var wrapDiv = angular.element(currentObj).closest('div');
                        if (parseInt(focusIndex) < 0) {
                            //retain the focus
                        } else {
                            var prevHRUL = angular.element(currentObj).closest('ul');
                            var setTabToFocus = angular.element(prevHRUL).find('li')[focusIndex];
                            if (isHours) {
                                $scope.hrsFocusElem = angular.element(prevHRUL).find('li')[focusIndex];
                            } else {
                                $scope.minFocusElem = angular.element(prevHRUL).find('li')[focusIndex];
                            }
                            angular.element(prevHRUL).find('li').each(function (ind, eachLi) {
                                $(eachLi).attr('tabindex', -1);
                            });
                            angular.element(prevHRUL).find('li').siblings().removeClass('active-timepicker');
                            angular.element(setTabToFocus).addClass('active-timepicker');
                            angular.element(setTabToFocus).attr('tabindex', 0);
                            angular.element(setTabToFocus).focus();
                            angular.element(wrapDiv).find('div.nextDiv').text("");
                            if (isHours) {
                                var incValue = $scope.timeObject.hours[focusIndex + 1];
                            } else {
                                var incValue = $scope.timeObject.minutes[focusIndex + 1];
                            }
                            angular.element(wrapDiv).find('div.nextDiv').text(incValue);
                            if (focusIndex < 1 || focusIndex === 0) {
                                angular.element(wrapDiv).find('div.previousDiv').text("");
                            } else {
                                angular.element(wrapDiv).find('div.previousDiv').text("");
                                if (isHours) {
                                    var decValue = $scope.timeObject.hours[focusIndex - 1];
                                } else {
                                    var decValue = $scope.timeObject.minutes[focusIndex - 1];
                                }
                                angular.element(wrapDiv).find('div.previousDiv').text(decValue);
                            }

                        }
                    };

                    function setPreviousValues(currentIndex, currentObj) {
                        var prevElementInDiv = parseInt(currentIndex); //doesn't go below scope.hours[0]
                        var prevHRUL = angular.element(currentObj).closest('ul');
                        var wrapDiv = angular.element(currentObj).closest('div');
                        if (angular.element(prevHRUL).find('li')[prevElementInDiv] !== undefined) {
                            var prevContent = angular.element(prevHRUL).find('li')[prevElementInDiv].innerText;
                            angular.element(wrapDiv).find('div.previousDiv').text(prevContent);
                        } else {
                            angular.element(wrapDiv).find('div.previousDiv').text("");
                        }
                    };

                    $scope.disableInput = function (ele) {
                        ele.find('input.input-field').attr('disabled', 'disabled');
                        ele.find('input.input-field').attr('readonly', 'readonly');
                        ele.find('input.input-field').attr('tabindex', -1);
                    };

                    $scope.enableInput = function (ele) {
                        ele.find('input.input-field').removeAttr('readonly');
                        ele.find('input.input-field').removeAttr('disabled');
                        ele.find('input.input-field').attr('tabindex', 0);
                    };
                }],

                link: function (scope, ele) {

                    $document.bind('click', function (event) {
                        var isClickedElementChildOfPopup = ele
                                .find(event.target)
                                .length > 0;
                        if (!isClickedElementChildOfPopup) {
                            scope.toggleContainer = false;
                            scope.enableInput(ele);
                            scope.$digest();
                        }
                    });

                    // Keyboard Navigation
                    $timeout(function () {
                        var elem = ele.find('.input-outerContainer');
                        elem.css('width', scope.xeWidth);
                        angular.element(ele.find('div.timepickerContainer')).on('keydown', function (event) {
                            if (event.keyCode === keyCodes.F9) {
                                scope.toggleContainer = false;
                                scope.$digest();
                                scope.enableInput(ele);
                                ele.find('div.input-container input.input-field').focus();
                                removeMinsAndHoursAriaLabel();
                            }
                        });

                        function stopEventsDefault(event) {
                            event.preventDefault();
                            event.stopPropagation();
                            event.cancelBubble = true;
                        };

                        convertTimeOnPageLoad(scope.displayModel);

                        var browser = scope.getBrowser();
                        var eventtype;
                        if (browser == "chrome") {
                            eventtype = "wheel"
                        } else if (browser == "firefox") {
                            eventtype = "DOMMouseScroll"
                        } else {
                            eventtype = "mousewheel"
                        }

                        ele.find('div.extraDiv').on('focus', function () {
                            angular.element(scope.hrsFocusElem).focus();
                            scope.focusPrevElement = ele.find('.hrsFocus');
                            angular.element(scope.focusPrevElement).addClass('active-timepickerFocus');
                        });
                        var timerButton = ele.find('div.input-container .inputicon');

                        angular.element(timerButton).on('click', function (event) {
                            removeallFocus();
                            scope.focusHrsElement = ele.find('.hrsFocus');
                            angular.element(scope.focusHrsElement).addClass('active-timepickerFocus');
                            scope.showError = false;
                            scope.toggleContainer = scope.toggleContainer ? false : true;
                            if (scope.toggleContainer) {
                                scope.disableInput(ele);
                            } else {
                                scope.enableInput(ele);
                            }
                            var enteredContent = ele.find('input.input-field').val();

                            validateEnteredContent(enteredContent);
                            scope.$digest();
                            if (!scope.showError) {
                                if (scope.timeObject.is24Hrformat) {
                                    //24 hours
                                    var hrPreSelected = enteredContent.split(":")[0];
                                    var minPreSelected = enteredContent.split(":")[1];
                                    hrPreSelected = parseInt(hrPreSelected);
                                    minPreSelected = parseInt(minPreSelected);
                                    populateTimePicker(hrPreSelected, minPreSelected, ele);
                                } else {
                                    //12 hours
                                    var hrPreSelected = enteredContent.split(":")[0];
                                    hrPreSelected = parseInt(hrPreSelected) - 1;
                                    var minPreSelected = enteredContent.split(":")[1];
                                    if (minPreSelected !== undefined) {
                                        var minPreSelected1 = minPreSelected.split(" ")[0];
                                        var meridianEntered, nextDivMeridian;
                                        populateTimePicker(hrPreSelected, minPreSelected1, ele);
                                        var meridian = minPreSelected.split(" ")[1];
                                        if (meridian.toLocaleLowerCase() === $filter('xei18n')('default.time.am').toLocaleLowerCase()) {
                                            meridianEntered = scope.meridiansList[0];
                                            nextDivMeridian = scope.meridiansList[1];
                                        } else if (meridian.toLocaleLowerCase() === $filter('xei18n')('default.time.pm').toLocaleLowerCase()) {
                                            meridianEntered = scope.meridiansList[1];
                                            nextDivMeridian = scope.meridiansList[0];
                                        }
                                        ele.find('div.pushmeBorder').text(meridianEntered);
                                        ele.find('div.meridianPm').text(nextDivMeridian);
                                    } else {
                                        populateTimePicker(hrPreSelected, 0, ele, 1);
                                    }
                                }
                            } else {
                                var minutesUL = ele.find('ul:last li')[0];
                                var hourUL = ele.find('ul:first li')[0];
                                scope.commonDecrement(1, minutesUL, false);
                                scope.commonDecrement(1, hourUL, true);
                                ele.find('ul:first li')[0].focus();
                                angular.element(ele).find('input.input-field').val("");
                                scope.displayModel="";
                                scope.modelScope = "";
                                scope.$apply();
                            }
                            stopEventsDefault(event);
                        });

                        //close popup on escape key
                        var excapeKeyContainer = ele.find('div.timepickerContainer');

                        angular.element(excapeKeyContainer).on('keydown', function (event) {
                            if (event.keyCode === keyCodes.ESC) {
                                removeallFocus();
                                scope.enableInput(ele);
                                scope.showError = false;
                                scope.toggleContainer = false;
                                scope.$digest();
                                ele.find('div.input-container input.input-field').focus();
                                stopEventsDefault(event);
                            }
                        });

                        //open timepicker on f9 shortcut press
                        var shortcutElement = ele.find('div.input-container input.input-field');

                        angular.element(shortcutElement).on('focusout', function (event) {
                            var enteredContent = ele.find('input.input-field').val();
                            scope.enableInput(ele);
                            validateEnteredContent(enteredContent);
                            stopEventsDefault(event);
                            if (scope.showError) {
                                ele.find('input.input-field').val("");
                                scope.displayModel = "";
                                scope.modelScope = "";
                            } else {
                                enteredContent = formatTimeEntered(enteredContent);
                                timeFormatConvertor(enteredContent);
                            }
                            scope.$apply();
                        });

                        angular.element(shortcutElement).on('keydown', function (event) {
                            if (event.keyCode === keyCodes.F9) {
                                removeallFocus();
                                var hrsFocusElem = ele.find('.hrsFocus');
                                angular.element(hrsFocusElem).addClass('active-timepickerFocus');
                                scope.showError = false;
                                var enteredContent = angular.element(ele).find('input.input-field').val();
                                scope.toggleContainer = true;
                                validateEnteredContent(enteredContent);
                                scope.$digest();
                                scope.disableInput(ele);
                                removeMinsAndHoursAriaLabel();
                                addHoursAriaLabel();
                                if (!scope.showError) {
                                    var decrementCount;
                                    if (scope.timeObject.is24Hrformat) {
                                        //24 hours
                                        var hrPreSelected = enteredContent.split(":")[0];
                                        var minPreSelected = enteredContent.split(":")[1];
                                        hrPreSelected = parseInt(hrPreSelected);
                                        minPreSelected = parseInt(minPreSelected);
                                        decrementCount = 1;
                                        populateTimePicker(hrPreSelected, minPreSelected, ele, decrementCount);
                                    } else {
                                        //12 hours
                                        var hrPreSelected = enteredContent.split(":")[0];
                                        hrPreSelected = parseInt(hrPreSelected) - 1;
                                        var minPreSelected = enteredContent.split(":")[1];
                                        if (minPreSelected !== undefined) {
                                            decrementCount = 0;
                                            var minPreSelected1 = minPreSelected.split(" ")[0];
                                            var meridianEntered;
                                            var nextDivMeridian = scope.meridiansList[1];
                                            populateTimePicker(hrPreSelected, minPreSelected1, ele, decrementCount);
                                            var meridian = minPreSelected.split(" ")[1];
                                            if (meridian !== undefined) {
                                                if (meridian.toLocaleLowerCase() === $filter('xei18n')('default.time.am').toLocaleLowerCase()) {
                                                    meridianEntered = scope.meridiansList[0];
                                                    nextDivMeridian = scope.meridiansList[1];
                                                } else if (meridian.toLocaleLowerCase() === $filter('xei18n')('default.time.pm').toLocaleLowerCase()) {
                                                    meridianEntered = scope.meridiansList[1];
                                                    nextDivMeridian = scope.meridiansList[0];
                                                }
                                                ele.find('div.pushmeBorder').text(meridianEntered);
                                                ele.find('div.meridianPm').text(nextDivMeridian);
                                            }
                                        } else {
                                            populateTimePicker(hrPreSelected, 0, ele, 1);
                                        }
                                    }
                                } else {
                                    var minutesUL = ele.find('ul:last li')[0];
                                    var hourUL = ele.find('ul:first li')[0];
                                    scope.commonDecrement(1, minutesUL, false);
                                    scope.commonDecrement(1, hourUL, true);
                                    ele.find('ul:first li')[0].focus();
                                    angular.element(ele).find('input.input-field').val("");
                                }
                                stopEventsDefault(event);
                            }
                            if (event.keyCode === keyCodes.TAB) {
                                validateEnteredContent(this.value);
                                enteredContent = formatTimeEntered(this.value);
                                timeFormatConvertor(enteredContent);
                                scope.$apply();
                                return true;
                            }
                        });

                        function populateTimePicker(hrPreSelected, minPreSelected, ele) {
                            var hrcurrent;
                            var mincurrent;
                            var setToFirstElement = parseInt(-1);
                            var isHours = true;
                            if (scope.timeObject.is24Hrformat) {
                                hrcurrent = scope.timeObject.hours[parseInt(hrPreSelected)];
                                var minIndex;
                                scope.timeObject.minutes.map(function (elementInArray, index) {
                                    if (elementInArray == minPreSelected) {
                                        minIndex = index;
                                    }
                                });
                                minPreSelected = minIndex;
                                mincurrent = scope.timeObject.minutes[parseInt(minPreSelected)];
                            } else {
                                hrcurrent = scope.timeObject.hours[parseInt(hrPreSelected) - 1];
                                var minIndex;
                                scope.timeObject.minutes.map(function (elementInArray, index) {
                                    if (elementInArray == minPreSelected) {
                                        minIndex = index;
                                    }
                                });
                                minPreSelected = minIndex;
                                mincurrent = scope.timeObject.minutes[parseInt(minPreSelected) - 1];
                            }
                            var minutesUL = ele.find('ul:last li')[parseInt(minPreSelected)];
                            var hourUL = ele.find('ul:first li')[parseInt(hrPreSelected)];
                            if (mincurrent && hrcurrent) {
                                ele.find('ul:last li')[parseInt(minPreSelected)].focus();
                                scope.commonIncrement(parseInt(minPreSelected) - 1, minutesUL, !isHours);
                                scope.commonIncrement(parseInt(hrPreSelected) - 1, hourUL, isHours);
                                ele.find('ul:first li')[parseInt(hrPreSelected)].focus();
                            } else if (hrPreSelected === 0 && hrcurrent === undefined && mincurrent !== undefined) {
                                ele.find('ul:last li')[parseInt(minPreSelected)].focus();
                                scope.commonIncrement(parseInt(minPreSelected) - 1, minutesUL, !isHours);
                                scope.commonIncrement(setToFirstElement, hourUL, true);
                            } else if (mincurrent === undefined && hrcurrent !== undefined) {
                                minutesUL = ele.find('ul:last li')[0];
                                scope.commonIncrement(setToFirstElement, minutesUL, !isHours);
                                scope.commonIncrement(parseInt(hrPreSelected - 1), hourUL, isHours);
                            } else {
                                minutesUL = ele.find('ul:last li')[0];
                                hourUL = ele.find('ul:first li')[0];
                                scope.commonIncrement(setToFirstElement, minutesUL, !isHours);
                                scope.commonIncrement(setToFirstElement, hourUL, isHours);
                            }
                        };

                        function validateEnteredContent(enteredContent) {
                            scope.showError = false;
                            var isInvalidTime = false;
                            if (null != enteredContent && "" != enteredContent) {
                                isInvalidTime = validateTimeWithFormat(enteredContent);
                            }
                            if (isInvalidTime) {
                                scope.showError = true;
                                scope.$emit('time-picker-error', $filter('xei18n')('timepicker.error.format.validation'), scope.xeId);
                            }
                        }

                        function validateTimeWithFormat(timeData) {
                            var formattedTime = "";
                            var isInvalidTime = false;
                            formattedTime = formatTimeEntered(timeData);
                            if (!formattedTime) {
                                isInvalidTime = true;
                            }
                            return isInvalidTime
                        }

                        function formatTimeEntered(timeData) {
                            var formattedTime;
                            if (scope.timeObject.is24Hrformat) {
                                var timeArray = splitInputData(timeData);
                                var hours = scope.formatNumberTwoDecimal(timeArray[0]);
                                var minutes = scope.formatNumberTwoDecimal(timeArray[1]);
                                var formattedValue = hours + ":" + minutes;
                                formattedTime = scope.formatEnteredTime(formattedValue, scope._24hrFormat, scope.timeObject.displayFormat);
                            } else {
                                if(timeData){
                                    var format12WithMeridian = scope.formatTimeTo12Hours(timeData,false);
                                    formattedTime = scope.formatEnteredTime(format12WithMeridian, scope._12hrFormat, scope._12hrFormat);
                                }

                            }
                            return formattedTime;
                        }

                        function splitInputData(inputData){
                            return inputData.split(":");
                        }

                        var cancelElement = ele.find('footer.timerFooter .footercancelButton');
                        ele.find('footer.timerFooter .footercancelButton').on('click', function (event) {
                            removeallFocus();
                            scope.showError = false;
                            scope.enableInput(ele);
                            scope.toggleContainer = false;
                            scope.$digest();
                            ele.find('div.input-container input.input-field').focus();
                            stopEventsDefault(event);
                        });

                        ele.find('footer.timerFooter .footercancelButton').on('keydown', function (event) {
                            if (event.shiftKey && event.keyCode === keyCodes.TAB) {
                                removeMinsAndHoursAriaLabel();
                                if (scope.timeObject.is24Hrformat) {
                                    addMinutesAriaLabel();
                                    removeallFocus();
                                    var minFocus = ele.find('ul.minFocus');
                                    toggleFocus(minFocus);
                                } else {
                                    removeallFocus();
                                    var meridianFocus = ele.find('div.meridianFocus');
                                    toggleFocus(meridianFocus);
                                }
                            }
                            return true;
                        });

                        ele.find('footer.timerFooter xe-button button.primary').on('keydown, click', function (event) {
                            setTimePickerValue();
                            stopEventsDefault(event);
                        });


                        ele.find('footer.timerFooter xe-button button.primary').on('keydown', function (event) {
                            if ((!event.shiftKey) && event.keyCode === keyCodes.TAB) {
                                removeMinsAndHoursAriaLabel();
                                addHoursAriaLabel();
                            }
                        });

                        function setTimePickerValue() {
                            var parentDiv = angular.element("#" + scope.xeId).closest('div').next();
                            scope.toggleContainer = false;
                            var hrLiSelected = angular.element(parentDiv).find('ul:first li.active-timepicker').text();
                            var minLiSelected = angular.element(parentDiv).find('ul:last li.active-timepicker').text();
                            var meridianValue = angular.element(parentDiv).find('div.pushmeBorder').text();
                            var timeFormatValueSelect = hrLiSelected + ":" + minLiSelected;
                            var v = !scope.timeObject.is24Hrformat ? timeFormatValueSelect + " " + meridianValue : timeFormatValueSelect;
                            timeFormatConvertor(v);
                            scope.enableInput(ele);
                            ele.find('input.input-field').focus();
                            scope.displayModel = v;
                            angular.element(ele.find('input.input-field')).controller('ngModel').$setDirty();
                            scope.$apply();
                        }

                        function convertTimeOnPageLoad(timeOnLoad) {
                            var returnData = scope.formatEnteredTime(timeOnLoad, scope.returnFormat, scope.returnFormat);
                            var displayData = scope.formatEnteredTime(timeOnLoad, scope.returnFormat, scope.displayFormat);
                            if (scope.timeObject.returnFormat == 12) {
                                if (scope.timeObject.is24Hrformat) {
                                    returnData = returnData
                                } else {
                                    returnData = scope.formatTimeTo12Hours(displayData, false);
                                    returnData = scope.formatEnteredTime(returnData, scope._12hrFormat, scope.returnFormat);
                                }
                            }
                            if (!scope.timeObject.is24Hrformat) {
                                displayData = scope.formatTimeTo12Hours(displayData, true);
                            }
                            scope.displayModel = displayData;
                            scope.modelScope = returnData;
                        }

                        function timeFormatConvertor(timeToConvert) {
                            var displayData, returnData;
                            if (scope.timeObject.format12 === 'Y' || scope.timeObject.format12 === 'N') {
                                if (scope.timeObject.is24Hrformat) {
                                    returnData = scope.formatEnteredTime(timeToConvert, scope.displayFormat, scope.returnFormat);
                                } else {
                                    var returnDataInEnglish = scope.formatTimeTo12Hours(timeToConvert, false);
                                    returnData = scope.formatEnteredTime(returnDataInEnglish, scope.displayFormat, scope.returnFormat);
                                }

                            }
                            if (scope.timeObject.format12 === 'Y' || scope.timeObject.format12 === 'N') {
                                if (scope.timeObject.is24Hrformat) {
                                    displayData = scope.formatEnteredTime(timeToConvert, scope.displayFormat, scope.displayFormat);
                                } else {
                                    var displayDataInEnglish = scope.formatTimeTo12Hours(timeToConvert, false);
                                    displayData = scope.formatEnteredTime(displayDataInEnglish, scope.displayFormat, scope.displayFormat);
                                    displayData = scope.formatTimeTo12Hours(displayData, true);
                                }

                            }
                            scope.displayModel = displayData;
                            scope.modelScope = returnData;
                        }

                        function ChangeAMPM(result) {
                            var amMeridian = $filter('xei18n')('default.time.am');
                            var pmMeridian = $filter('xei18n')('default.time.pm');
                            $(result).text(function (i, v) {
                                return v === amMeridian ? pmMeridian : amMeridian
                            });
                            $(result).next().text(function (i, v) {
                                return v === amMeridian ? pmMeridian : amMeridian
                            });
                        }

                        //Meridian Button add on 12 hrs format
                        if (!scope.timeObject.is24Hrformat) {
                            var tickerContainer = ele.find('div.timepickerContainer .mainContainer .tickerContainer')[0].classList[1];
                            var meridianElement = ele.find('div.timepickerContainer .mainContainer .pushmeBorder')[0];
                            var meridianFocus = ele.find('.meridianFocus');
                            $("." + tickerContainer).on(eventtype, function (event) {
                                removeallFocus();
                                toggleFocus(meridianFocus);
                                ChangeAMPM(meridianElement);
                            });
                            $("." + tickerContainer).on('click', function (event) {
                                removeallFocus();
                                toggleFocus(meridianFocus);

                            });
                            $("." + tickerContainer).on('keydown', function (event) {
                                if (event.keyCode === keyCodes.UP || event.keyCode === keyCodes.DOWN) {
                                    ChangeAMPM(meridianElement);
                                }
                                if (event.shiftKey && event.keyCode === keyCodes.TAB) {
                                    removeMinsAndHoursAriaLabel();
                                    addMinutesAriaLabel();
                                }

                                if ((!event.shiftKey) && event.keyCode === keyCodes.TAB) {
                                    removeMinsAndHoursAriaLabel();
                                }
                            });
                            $("." + tickerContainer).on("touchend", function (event) {
                                removeallFocus();
                                toggleFocus(meridianFocus);
                                ChangeAMPM(meridianElement);
                            });
                        }

                        scope.hrsFocusElem = ele.find('ul:first li')[0];
                        scope.minFocusElem = ele.find('ul:last li')[0];
                        var hrsElement = ele.find('ul:first li');
                        var firstElem = hrsElement[0];
                        var wrapHRDiv = angular.element(firstElem).closest('div');
                        var nextHRUL = angular.element(firstElem).closest('ul');
                        var nextContent = angular.element(nextHRUL).find('li')[1].innerText;
                        angular.element(wrapHRDiv).find('div.nextDiv').text(nextContent);
                        angular.element(firstElem).attr('tabindex', 0);


                        $(nextHRUL).on('click', function () {
                            removeallFocus();
                            toggleFocus(nextHRUL);
                        });

                        angular.element(wrapHRDiv).find('div.nextDiv').on(eventtype, function (event) {
                            var isHours = true;
                            var mouseEvt = event.originalEvent;
                            var currentElement = this;
                            var activeLi = $(currentElement.parentElement).find('ul li.active-timepicker');
                            removeallFocus();
                            var setHrsElement = ele.find('.hrsFocus');
                            toggleFocus(setHrsElement);
                            var currentIndex = parseInt(activeLi[0].classList[0].split("_")[1]);
                            scrollFunctionality(mouseEvt, currentIndex, activeLi, isHours);
                            stopEventsDefault(event);
                            return false;
                        });

                        angular.element(wrapHRDiv).find('div.previousDiv').on(eventtype, function (event) {
                            var isHours = true;
                            var mouseEvt = event.originalEvent;
                            var currentElement = this;
                            var activeLi = $(currentElement.parentElement).find('ul li.active-timepicker');
                            removeallFocus();
                            var setHrsElement = ele.find('.hrsFocus');
                            toggleFocus(setHrsElement);
                            var currentIndex = parseInt(activeLi[0].classList[0].split("_")[1]);
                            scrollFunctionality(mouseEvt, currentIndex, activeLi, isHours);
                            stopEventsDefault(event);
                            return false;
                        });

                        function scrollFunctionality(evt, currentIndex, currentObj, isHours) {
                            var delta = Math.max(-1, Math.min(1, (evt.wheelDelta || -evt.detail)));
                            if (delta < 0) {
                                scope.commonIncrement(currentIndex, currentObj, isHours);
                                return false;
                            } else if (delta > 0) {
                                scope.commonDecrement(currentIndex, currentObj, isHours);
                            }
                        }

                        function toggleFocus(setfocus) {
                            angular.element(setfocus).addClass('active-timepickerFocus');
                        }

                        hrsElement.on('keydown', function (event) {
                            var currentIndex = parseInt(this.classList[0].split("_")[1]);
                            var currentElement = this;
                            var isHours = true;
                            scope.focusPrevElement = ele.find('.minFocus');
                            scope.focusCurrentElement = ele.find('.hrsFocus');
                            hrsElement.each(function (ind, eachLi) {
                                $(eachLi).attr('tabindex', -1);
                            });
                            angular.element("." + this.classList[0]).attr('tabindex', 0);
                            if (event.keyCode === keyCodes.DOWN) {
                                scope.commonIncrement(currentIndex, currentElement, isHours);
                                removeMinsAndHoursAriaLabel();
                                stopEventsDefault(event);
                                return false;
                            }
                            if (event.keyCode === keyCodes.UP) {
                                scope.commonDecrement(currentIndex, currentElement, isHours);
                                removeMinsAndHoursAriaLabel();
                                stopEventsDefault(event);
                                return false;
                            }
                            if (event.keyCode === keyCodes.RIGHT) {
                                angular.element(scope.minFocusElem).focus();
                                removeallFocus();
                                toggleFocus(scope.focusPrevElement);
                                removeMinsAndHoursAriaLabel();
                                addMinutesAriaLabel();
                                angular.element(scope.minFocusElem).focus();
                                stopEventsDefault(event);
                                return false;
                            }

                            if (event.keyCode === keyCodes.ENTER) {
                                removeMinsAndHoursAriaLabel();
                                setTimePickerValue();
                                stopEventsDefault(event);
                                return false;
                            }

                            if (!event.shiftKey && event.keyCode === keyCodes.TAB) {
                                angular.element(scope.minFocusElem).focus();
                                removeallFocus();
                                toggleFocus(scope.focusPrevElement);
                                removeMinsAndHoursAriaLabel();
                                addMinutesAriaLabel();
                                angular.element(scope.minFocusElem).focus();
                                stopEventsDefault(event);
                                return false;
                            }

                            if (event.shiftKey && event.keyCode === keyCodes.TAB) {
                                var setButton = ele.find('footer.timerFooter xe-button button.primary');
                                angular.element(setButton).focus();
                                removeallFocus();
                                toggleFocus(setButton);
                                removeMinsAndHoursAriaLabel();
                                stopEventsDefault(event);
                                return false;
                            }
                            return true;
                        });


                        hrsElement.on(eventtype, function (event) {
                            var mouseEvt = event.originalEvent;
                            var _this = $(this).closest('ul');
                            var currentElement = $(_this).find('li.active-timepicker');
                            var currentIndex = parseInt(currentElement[0].classList[0].split("_")[1]);
                            var isHours = true;
                            removeallFocus();
                            var setHrsElement = ele.find('.hrsFocus');
                            toggleFocus(setHrsElement);
                            scrollFunctionality(mouseEvt, currentIndex, currentElement, isHours);
                            stopEventsDefault(event);
                            return false;

                        });

                        var minElements = ele.find('ul:last li');
                        var firstMinElem = minElements[0];
                        var wrapMinDiv = angular.element(firstMinElem).closest('div');
                        var nextMinUL = angular.element(firstMinElem).closest('ul');
                        var nextContent = angular.element(nextMinUL).find('li')[1].innerText;
                        angular.element(firstMinElem).attr('tabindex', 0);
                        angular.element(wrapMinDiv).find('div.nextDiv').text(nextContent);

                        $(nextMinUL).on('click', function () {
                            removeallFocus();
                            toggleFocus(nextMinUL);
                        });

                        angular.element(wrapMinDiv).find('div.nextDiv').on(eventtype, function (event) {
                            var isHours = false;
                            var mouseEvt = event.originalEvent;
                            var activeLi = $(this.parentElement).find('ul li.active-timepicker');
                            removeallFocus();
                            var minFocus = ele.find('.minFocus');
                            toggleFocus(minFocus);
                            var currentIndex = parseInt(activeLi[0].classList[0].split("_")[1]);
                            scrollFunctionality(mouseEvt, currentIndex, activeLi, isHours);
                            stopEventsDefault(event);
                            return false;
                        });

                        angular.element(wrapMinDiv).find('div.previousDiv').on(eventtype, function (event) {
                            var isHours = false;
                            var mouseEvt = event.originalEvent;
                            var activeLi = $(this.parentElement).find('ul li.active-timepicker');
                            removeallFocus();
                            var minFocus = ele.find('.minFocus');
                            toggleFocus(minFocus);
                            var currentIndex = parseInt(activeLi[0].classList[0].split("_")[1]);
                            scrollFunctionality(mouseEvt, currentIndex, activeLi, isHours);
                            stopEventsDefault(event);
                            return false;
                        });


                        addHoursAriaLabel();

                        minElements.on('keydown', function (event) {
                            var currentIndex = parseInt(this.classList[0].split("_")[1]);
                            var currentElement = this;
                            var isHours = false;
                            scope.focusPrevElement = ele.find('.hrsFocus');
                            scope.focusNextElement = ele.find('.meridianFocus');
                            scope.focusCurrentElement = ele.find('.minFocus');
                            minElements.each(function (ind, eachLi) {
                                $(eachLi).attr('tabindex', -1);
                            });
                            angular.element("." + this.classList[0]).attr('tabindex', 0);
                            if (event.keyCode === keyCodes.DOWN) {
                                removeMinsAndHoursAriaLabel();
                                scope.commonIncrement(currentIndex, currentElement, isHours);
                                stopEventsDefault(event);
                                return false;
                            }
                            if (event.keyCode === keyCodes.UP) {
                                removeMinsAndHoursAriaLabel();
                                scope.commonDecrement(currentIndex, currentElement, isHours);
                                stopEventsDefault(event);
                                return false;
                            }
                            if (event.keyCode === keyCodes.LEFT) {
                                removeMinsAndHoursAriaLabel();
                                addHoursAriaLabel();
                                angular.element(scope.hrsFocusElem).attr('tabindex', 0);
                                angular.element(scope.hrsFocusElem).focus();
                                removeallFocus();
                                toggleFocus(scope.focusPrevElement);
                                stopEventsDefault(event);
                                return false;
                            }
                            if (event.keyCode === keyCodes.RIGHT) {
                                removeMinsAndHoursAriaLabel();
                                scope.ampmEle = ele.find('div.pushmeBorder');
                                angular.element(scope.ampmEle).attr('tabindex', 0);
                                angular.element(scope.ampmEle).focus();
                                if (!scope.timeObject.is24Hrformat) {
                                    var meridianFocus = ele.find('div.meridianFocus');
                                    var minFocus = ele.find('.minFocus');
                                    removeallFocus();
                                    toggleFocus(meridianFocus);
                                }
                                stopEventsDefault(event);
                                return false;
                            }

                            if (event.keyCode === keyCodes.ENTER) {
                                removeMinsAndHoursAriaLabel();
                                setTimePickerValue();
                                stopEventsDefault(event);
                                return false;
                            }

                            if ((!event.shiftKey) && event.keyCode === keyCodes.TAB) {
                                var cancelfocus = ele.find('.footercancelButton');
                                var minfocus = ele.find('.minFocus');
                                var meridianFocus = ele.find('.meridianFocus');
                                if (Language.isRtl()) {
                                    angular.element(cancelfocus).addClass('active-timepickerFocus');
                                    angular.element(minfocus).removeClass('active-timepickerFocus');
                                } else {
                                    if (!scope.timeObject.is24Hrformat) {
                                        removeallFocus();
                                        toggleFocus(meridianFocus);

                                    } else {
                                        removeallFocus();
                                    }
                                }
                                angular.element(cancelfocus).removeClass('active-timepickerFocus');
                                removeMinsAndHoursAriaLabel();
                            }
                            if (event.shiftKey && event.keyCode === keyCodes.TAB) {
                                angular.element(scope.hrsFocusElem).attr('tabindex', 0);
                                angular.element(scope.hrsFocusElem).focus();
                                removeallFocus();
                                toggleFocus(scope.focusPrevElement);
                                removeMinsAndHoursAriaLabel();
                                addHoursAriaLabel();
                                angular.element(scope.hrsFocusElem).focus();
                                stopEventsDefault(event);
                                return false;
                            }

                            return true;
                        });

                        var meridianElem = ele.find('div.mainContainer .pushmeBorder');
                        meridianElem.on('keydown', function (event) {
                            scope.focusCurrentElement = ele.find('.meridianFocus');
                            scope.focusPrevElement = ele.find('.minFocus');
                            if (event.keyCode === keyCodes.LEFT) {
                                angular.element(scope.minFocusElem).focus();
                                removeallFocus();
                                toggleFocus(scope.focusPrevElement);
                                removeMinsAndHoursAriaLabel();
                                addMinutesAriaLabel();
                                angular.element(scope.minFocusElem).focus();
                                stopEventsDefault(event);
                                return false;
                            }

                            if (event.keyCode === keyCodes.RIGHT) {
                                return false;
                            }

                            if (event.keyCode === keyCodes.ENTER) {
                                removeMinsAndHoursAriaLabel();
                                setTimePickerValue();
                                stopEventsDefault(event);
                                return false;
                            }
                            if ((!event.shiftKey) && event.keyCode === keyCodes.TAB) {
                                removeallFocus();
                            }
                            if (event.shiftKey && event.keyCode === keyCodes.TAB) {
                                removeallFocus();
                                toggleFocus(scope.focusPrevElement);
                                $('.meridianFocus').removeClass('active-timepickerFocus');
                            }

                        });

                        function removeMinsAndHoursAriaLabel() {
                            angular.element(scope.hrsFocusElem).parent().removeAttr('aria-label');
                            angular.element(scope.minFocusElem).parent().removeAttr('aria-label');
                        }

                        function removeallFocus() {
                            var minFocus = ele.find('.minFocus');
                            var hrsFocus = ele.find('.hrsFocus');
                            var meridianFocus = ele.find('.meridianFocus');
                            var cancelFocus = ele.find('.footercancelButton')
                            angular.element(meridianFocus).removeClass('active-timepickerFocus');
                            angular.element(minFocus).removeClass('active-timepickerFocus');
                            angular.element(hrsFocus).removeClass('active-timepickerFocus');
                            angular.element(cancelFocus).removeClass('active-timepickerFocus');
                        }

                        function addHoursAriaLabel() {
                            angular.element(scope.hrsFocusElem).parent().attr('aria-label', $filter('xei18n')('timepicker.hour.aria.label') + " " + $filter('xei18n')('timepicker.shortcuts.aria.label'));
                        }

                        function addMinutesAriaLabel() {
                            if (!scope.timeObject.is24Hrformat)
                                angular.element(scope.minFocusElem).parent().attr('aria-label', $filter('xei18n')('timepicker.min.aria.label') + " " + $filter('xei18n')('timepicker.shortcuts.aria.label'));
                            else
                                angular.element(scope.minFocusElem).parent().attr('aria-label', $filter('xei18n')('timepicker.min.aria.24hrs.label') + " " + $filter('xei18n')('timepicker.shortcuts.aria.label'));
                        }

                        minElements.on(eventtype, function (event) {
                            var mouseEvt = event.originalEvent;
                            var isHours = false;
                            var _this = $(this).closest('ul');
                            var currentElement = $(_this).find('li.active-timepicker');
                            var currentIndex = parseInt(currentElement[0].classList[0].split("_")[1]);
                            removeallFocus();
                            var minFocus = ele.find('.minFocus');
                            toggleFocus(minFocus);
                            scrollFunctionality(mouseEvt, currentIndex, currentElement, isHours);
                            stopEventsDefault(event);
                            return false;
                        });

                        var start = {};
                        var stop = {};

                        function touchmove(event) {
                            if (!start) {
                                return;
                            }

                            var data = event.originalEvent.touches ?
                                event.originalEvent.touches[0] :
                                event;
                            stop = {
                                time: (new Date).getTime(),
                                coords: [data.pageX, data.pageY]
                            };

                            // prevent scrolling
                            if (Math.abs(start.coords[1] - stop.coords[1]) > 10) {
                                stopEventsDefault(event);
                            }

                        }

                        //Drag Feature in Desktop for Hrs Container
                        var yDown;
                        var yUp;
                        angular.element(wrapHRDiv).on("mousedown", function (e) {
                            yDown = e.pageY;
                        }).on('mouseup', function (e) {
                            removeallFocus();
                            var setHrsElement = ele.find('.hrsFocus');
                            angular.element(setHrsElement).addClass('active-timepickerFocus');
                            var isHours = true;
                            var activeLi = $(this.parentElement).find('.hrsContainer  ul li.active-timepicker');
                            var currentIndex = parseInt(activeLi[0].classList[0].split("_")[1]);
                            yUp = e.pageY;
                            if (yDown < yUp) {
                                scope.commonDecrement(currentIndex, activeLi, isHours);
                            }
                            else if (yDown > yUp) {
                                scope.commonIncrement(currentIndex, activeLi, isHours);
                            }
                            stopEventsDefault(e);
                            return false;
                        });

                        //Drag Feature in Desktop for Mins Container
                        var yMinDown;
                        var yMinUp;
                        angular.element(wrapMinDiv).on("mousedown", function (e) {
                            yMinDown = e.pageY;
                        }).on('mouseup', function (e) {
                            removeallFocus();
                            var setHrsElement = ele.find('.minFocus');
                            angular.element(setHrsElement).addClass('active-timepickerFocus');
                            var isHours = false;
                            var activeLi = $(this.parentElement).find('.minContainer  ul li.active-timepicker');
                            var currentIndex = parseInt(activeLi[0].classList[0].split("_")[1]);
                            yMinUp = e.pageY;
                            if (yMinDown < yMinUp) {
                                scope.commonDecrement(currentIndex, activeLi, isHours);
                            }
                            else if (yMinDown > yMinUp) {
                                scope.commonIncrement(currentIndex, activeLi, isHours);
                            }
                            stopEventsDefault(e);
                            return false;
                        });

                        function touchStart(touchEvent, elementClass) {
                            removeallFocus();
                            var setElement = ele.find('.' + elementClass);
                            angular.element(setElement).addClass('active-timepickerFocus');
                            var touches = touchEvent.originalEvent.touches ? touchEvent.originalEvent.touches[0] : touchEvent;
                            start = {
                                time: (new Date).getTime(),
                                coords: [touches.pageX, touches.pageY],
                                origin: $(touchEvent.target)
                            }
                        }

                        function touchEnd(currentIndex, ulElement, isHours, evt) {
                            if (start && stop) {
                                if (stop.time - start.time < 1000 &&
                                    Math.abs(start.coords[1] - stop.coords[1]) > 30 &&
                                    Math.abs(start.coords[0] - stop.coords[0]) < 75) {
                                    if (start.coords[1] > stop.coords[1]) {
                                        scope.commonIncrement(parseInt(currentIndex), ulElement, isHours);
                                    } else {
                                        scope.commonDecrement(parseInt(currentIndex), ulElement, isHours);
                                    }
                                    start = stop = undefined;
                                    stopEventsDefault(evt);
                                    return false;
                                }
                            }
                        }

                        $(wrapHRDiv).find('div.nextDiv').on({
                            'touchstart': function (event) {
                                var elementClass = "hrsFocus";
                                touchStart(event, elementClass);
                                $(this).on('touchmove', touchmove);
                                event.preventDefault();
                            },
                            'touchend': function (event) {
                                $(this).off('touchmove');
                                var isHours = true;
                                var wrapDiv = this.closest('div');
                                var ulElement = $(wrapDiv).prev();
                                var currentIndex = $(ulElement).find('li.active-timepicker').val();
                                touchEnd(currentIndex, ulElement, isHours, event);
                            }
                        });


                        $(wrapHRDiv).find('div.previousDiv').on({
                            'touchstart': function (event) {
                                var elementClass = "hrsFocus";
                                touchStart(event, elementClass);
                                $(this).on('touchmove', touchmove);
                                event.preventDefault();
                            },
                            'touchend': function (event) {
                                $(this).off('touchmove');
                                var isHours = true;
                                var wrapDiv = this.closest('div');
                                var ulElement = $(wrapDiv).next();
                                var currentIndex = $(ulElement).find('li.active-timepicker').val();
                                touchEnd(currentIndex, ulElement, isHours, event);
                            }
                        });

                        $(hrsElement).on({
                            'touchstart': function (event) {
                                var elementClass = "hrsFocus";
                                touchStart(event, elementClass);
                                $(this).on('touchmove', touchmove);
                                event.preventDefault();
                            },
                            'touchend': function (event) {
                                $(this).off('touchmove');
                                var isHours = true;
                                var currentIndex = (this.classList[0]).split("_")[1];
                                var ulElement = this;
                                touchEnd(currentIndex, ulElement, isHours, event);
                            }
                        });

                        $(minElements).on({
                            'touchstart': function (event) {
                                var elementClass = "minFocus";
                                touchStart(event, elementClass);
                                $(this).on('touchmove', touchmove);
                                stopEventsDefault(event);
                            },
                            'touchend': function (event) {
                                $(this).off('touchmove');
                                var isHours = false;
                                var ulElement = this;
                                var currentIndex = (this.classList[0]).split("_")[1];
                                touchEnd(currentIndex, ulElement, isHours, event);
                            }
                        });

                        $(wrapMinDiv).find('div.previousDiv').on({
                            'touchstart': function (event) {
                                var elementClass = "minFocus";
                                touchStart(event, elementClass);
                                $(this).on('touchmove', touchmove);
                                event.preventDefault();
                            },
                            'touchend': function (event) {
                                $(this).off('touchmove');
                                var isHours = false;
                                var wrapDiv = this.closest('div');
                                var ulElement = $(this.closest('div')).next();
                                var currentIndex = $(ulElement).find('li.active-timepicker')[0].classList[0].split("_")[1];
                                touchEnd(currentIndex, ulElement, isHours, event);
                            }
                        });

                        $(wrapMinDiv).find('div.nextDiv').on({
                            'touchstart': function (event) {
                                var elementClass = "minFocus";
                                touchStart(event, elementClass);
                                $(this).on('touchmove', touchmove);
                                event.preventDefault();
                            },
                            'touchend': function (event) {
                                $(this).off('touchmove');
                                var isHours = false;
                                var wrapDiv = this.closest('div');
                                var ulElement = $(this.closest('div')).prev();
                                var currentIndex = $(ulElement).find('li.active-timepicker')[0].classList[0].split("_")[1];
                                touchEnd(currentIndex, ulElement, isHours, event);
                            }
                        });
                    },10);
                }
            };
        }])
}());
//# sourceMappingURL=xe-ui-components.js.map