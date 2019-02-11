/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

(function () {
    'use strict';

    angular.module('modalPopup',['xe-ui-components'])
        .controller("nameModalPopupCtrl", ["$scope","$timeout","$http", "$q", "$filter",  function($scope, $timeout,$http, $q, $filter) {
            $scope.nocolumnFilterMenu = true;
            $scope.rtl = "xe-ui-components.min";
            $scope.rtlText = "Switch to RTL";
            $scope.urlTest = getContextPath()+/internalPb/;
            $scope.records = 1;
            $scope.content = {};
            $scope.resultsFound = 0;
            $scope.params = {};
            $scope.serviceNameType = "";
            $scope.nameHeader = "";
            $scope.excludePage =" ";
            $scope.inputTypeFieldID="";
            $scope.isPbPage = "";


            function getContextPath() {
                return window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
            };

            $scope.nameToggleModal = function(dataFetch) {
                if(dataFetch){
                    $scope.getData({excludePage:$scope.excludePage,max:5,offset:0});
                }
                $scope.modalShown = !$scope.modalShown;
                $timeout(function () {
                    angular.element('#nameDataTableSearch').focus();
                },0);

            };

            $scope.draggableColumnNames = [$scope.nameHeader, 'dateCreated', 'lastUpdated'];

            $scope.mobileConfig = {
                term: 2,
                crn: 2,
                subject: 2,
                status: 2
            };

            $scope.paginationConfig = {
                pageLengths : [ 5, 10, 25, 50, 100],
                offset : 5,
                recordsFoundLabel : $.i18n.prop("nameDataTable.column.common.pagination.recordsFoundLabel"),
                pageTitle: "Go To Page (End)",
                pageLabel: $.i18n.prop("nameDataTable.column.common.pagination.pageLabel"),
                pageAriaLabel: "Go To Page. Short cut is End",
                ofLabel: $.i18n.prop("nameDataTable.column.common.pagination.ofLabel"),
                perPageLabel: $.i18n.prop("nameDataTable.column.common.pagination.perPageLabel")
            };

            $scope.searchConfig = {
                id: 'nameDataTableSearch',
                title: 'Search (Alt+Y)',
                ariaLabel: 'Search for any Name',
                delay: 300,
                searchString : '',
                placeholder : 'Search by Name',
                maxlength: 250,
                minimumCharacters : 1
            };

            $scope.getData = function(query) {
                var deferred = $q.defer(),
                    url = "";
                    query.max = '5';
                url = getContextPath()+/internalPb/+$scope.serviceNameType+"/getGridData"
                    + "?getGridData=true&"
                    + "excludePage="+$scope.excludePage+"&"
                    + "searchString=" + (query.searchString ? query.searchString : "")
                    + "&sortColumnName=" + (query.sortColumnName ? query.sortColumnName : "")
                    + "&ascending=" + query.ascending
                    + "&offset=" + (query.offset ? query.offset : "")
                    + "&max=" + (query.max ? query.max : "");

                $http.get(url)
                    .success(function(data) {
                        deferred.resolve(data);
                        $scope.postFetch({response: data, oldResult: $scope.content});
                        $scope.content = data.result;
                        $scope.resultsFound = data.length;
                    })
                    .error(function(data) {
                        deferred.reject(data);
                    });

                return deferred.promise;
            };
            // Data to populate as part of data table header
            $scope.pageColumns = [
                {position: {desktop: 1, mobile: 1}, name: 'constantName', title: $.i18n.prop("nameDataTable.column.page.name.heading"), options: {visible: true, sortable:true}},
                {position: {desktop: 2, mobile: 2}, name: 'dateCreated', title: $.i18n.prop("nameDataTable.column.common.createDate.heading"), options: {visible: true, sortable: true}},
                {position: {desktop: 3, mobile: 3}, name: 'lastUpdated', title: $.i18n.prop("nameDataTable.column.common.modifiedDate.heading"), options: {visible: true, sortable:true}}
            ];

            $scope.virtualDomainColumns = [
                {position: {desktop: 1, mobile: 1}, name: 'serviceName', title: $.i18n.prop("nameDataTable.column.virtualDomain.name.heading"), options: {visible: true, sortable:true}},
                {position: {desktop: 2, mobile: 2}, name: 'dateCreated', title: $.i18n.prop("nameDataTable.column.common.createDate.heading"), options: {visible: true, sortable: true}},
                {position: {desktop: 3, mobile: 3}, name: 'lastUpdated', title: $.i18n.prop("nameDataTable.column.common.modifiedDate.heading"), options: {visible: true, sortable:true}}
            ];

            $scope.cssColumns = [
                {position: {desktop: 1, mobile: 1}, name: 'constantName', title: $.i18n.prop("nameDataTable.column.stylesheet.name.heading"), options: {visible: true, sortable:true}},
                {position: {desktop: 2, mobile: 2}, name: 'dateCreated', title: $.i18n.prop("nameDataTable.column.common.createDate.heading"), options: {visible: true, sortable: true}},
                {position: {desktop: 3, mobile: 3}, name: 'lastUpdated', title: $.i18n.prop("nameDataTable.column.common.modifiedDate.heading"), options: {visible: true, sortable:true}}
            ];

            $scope.toggleRTL = function() {
                if($scope.rtl === "xe-ui-components.min") {
                    $scope.rtl = "xe-ui-components-rtl";
                    $scope.rtlText = "Switch to LTR";
                } else {
                    $scope.rtl = "xe-ui-components.min";
                    $scope.rtlText = "Switch to RTL";
                }
            };

            $scope.refreshData = function() {
                $scope.refreshGrid(true);
            };

            $scope.postFetch = function(response, oldResult) {
               // rows = response.result;
            };

            $scope.isResponseEmpty = function(){
                return $scope.resultsFound == 0;
            }
            $scope.goToPage = function () {
                console.log('my go to page');
                var name = "";
                var value= "";
                $scope.modalShown = !$scope.modalShown;
                var element =  angular.element('tr.active-row');
                var nameIndex = element.index();
                if(nameIndex == -1){
                    console.log('My row selection is not empty !!')
                    nameIndex = 0;
                }

                if(nameIndex != -1 && !$scope.isResponseEmpty()) {
                    console.log('My row selection is not empty, so pick top one !!')
                    console.log($scope.content[nameIndex]);
                    console.log($scope.content[nameIndex][$scope.nameHeader])
                    name = $scope.content[nameIndex][$scope.nameHeader]
                    value = name;
                }
                console.log(name)
                element.removeClass('active-row');

                if($scope.inputTypeFieldID == 'constantName'){
                    $("#pageRoleId").val($scope.content[nameIndex]['id'])
                }

                if($scope.isPbPage== 'true'){
                    value =$scope.content[nameIndex]['id']
                    $scope.inputTypeFieldID = 'pbid-'+$scope.inputTypeFieldID;
                }

                if($scope.inputTypeFieldID == 'constantName' && $scope.content[nameIndex]['extendsPage']) {
                    $("#extendsPage  option:selected").remove();
                    $("#extendsPage").append("<option label='"+$scope.content[nameIndex]['extendsPage']+"' selected='selected' value="+$scope.content[nameIndex]['extendsPage']+">"+$scope.content[nameIndex]['extendsPage']+"</option>");

                    var input = angular.element(document.getElementById('extendsPage'))
                    input.trigger('change');
                }

                $("#"+$scope.inputTypeFieldID+" option:selected").remove();
                $("#"+$scope.inputTypeFieldID).append("<option label='"+name+"' selected='selected' value="+value+">"+name+"</option>");

                var input = angular.element(document.getElementById($scope.inputTypeFieldID))
                input.trigger('change');
            }


        }]);
})();