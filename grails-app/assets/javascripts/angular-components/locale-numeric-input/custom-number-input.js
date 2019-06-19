var numericApp = angular.module('numericApp', ['xe-ui-components']);

numericApp.controller("numericController", function($scope){
    console.log('here in controller');
    $scope.currency1 = 3434343;
})

//angular.module("xe-ui-components").value("RESOURCE_PATH","/PlatformSandboxApp/plugins/banner-ui-ss-2.10.5/angular-components/")
