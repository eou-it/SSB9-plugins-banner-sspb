angular.module('xe.common', [])./*
   * Intercept Angular $http requests and trigger the jQuery ajaxStart event
   * to allow the activity timer to recognize the activity.
   */
factory('ajaxStartLoadingInterceptor', function () {
    return {
        request: function (config) {
            $(document).trigger('ajaxStart');
            return config;
        }
    }
}).config(['$httpProvider', function ($httpProvider) {
    $httpProvider.interceptors.push('ajaxStartLoadingInterceptor');
}]);