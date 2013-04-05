/* App Module */

if (undefined == myCustomServices)
    var myCustomServices = [];

myCustomServices.push('banner9Admin.directives');


var appModule = angular.module('BannerOnAngular', myCustomServices, function ($routeProvider, $locationProvider) {
    $routeProvider.when('/basicCourseInformation', {
        templateUrl:"/StudentRegistrationSsb/custom/XE/pages/basicCourseInformation.html"
    });
    $routeProvider.when('/courseDetailInformation', {
        templateUrl:"/StudentRegistrationSsb/custom/XE/pages/courseDetailInformation.html"
    });
    $routeProvider.when('mainpage', {
            templateUrl:"/StudentRegistrationSsb/custom/XE/pages/mainPage.html"
    });
    $routeProvider.when('/selfservicelanding', {
            templateUrl:"/StudentRegistrationSsb/custom/XE/pages/selfserviceLanding.html"
    });
    $routeProvider.when('/registration', {
            templateUrl:"/StudentRegistrationSsb/custom/XE/pages/registration.html"
    });
    $routeProvider.when('/termselect', {
            templateUrl:"/StudentRegistrationSsb/custom/XE/pages/termselect.html"
    });
    $routeProvider.when('/myregistration', {
            templateUrl:"/StudentRegistrationSsb/custom/XE/pages/myregistration.html"
    });
    $routeProvider.when('/dynamicpage/:pageId', {
                templateUrl:"/StudentRegistrationSsb/custom/XE/pages/dynamicpage.html"
        });
    $routeProvider.when('/pagebuilder', {
                templateUrl:"/StudentRegistrationSsb/custom/XE/pages/pagebuilder.html"
        });
    $routeProvider.otherwise(
        {redirectTo: 'mainpage'}
    );
});

// below filter is used fur pagination
appModule.filter('startFrom', function() {
        return function(input, start) {
            start = +start; //parse to int
            return input.slice(start);
        }
});


