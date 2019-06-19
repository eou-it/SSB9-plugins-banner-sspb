/*******************************************************************************
 Copyright 2017-2018 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

(function () {
    'use strict';
    angular.module('userPreference', ['xe-ui-components'])
        .config(['$httpProvider', function ($httpProvider) {
            $httpProvider.defaults.headers.common['Cache-Control'] = 'no-cache';
            $httpProvider.defaults.cache = false;
            if (!$httpProvider.defaults.headers.get) {
                $httpProvider.defaults.headers.get = {};
            }
            $httpProvider.defaults.headers.get['If-Modified-Since'] = '0';
        }])
        .controller('PopupCtrl', ['$scope', '$timeout', '$http', function ($scope, $timeout, $http) {
            $scope.modalShown = false;
            $scope.disableButton = true;
            if ($('meta[name=menuBaseURL]').attr("content")) {
                var backendlocale = $('meta[name=menuBaseURL]').attr("content");
            }
            $scope.prevSelected = {};
            $scope.togglepopup = function () {
                notifications.clearNotifications();
                $scope.modalShown = !$scope.modalShown;
                delete $scope.language.selected;
                var liveChoicestStusRegion = $('.uiselect-choice-status-hidden-accessible');
                if (liveChoicestStusRegion.length == 0) {
                    var regionSpan = angular.element('<span></span>');
                    regionSpan.attr('role', 'status');
                    regionSpan.attr('aria-live', 'assertive');
                    regionSpan.attr('aria-atomic', 'true');
                    regionSpan.attr('class', 'uiselect-choice-status-hidden-accessible');
                    angular.element(document.body).append(regionSpan);
                }
                $timeout(function () {
                    angular.element('#xeModalMask').attr('tabindex', 0).focus();
                    angular.element('#preference').find('input')[1].focus();
                    // To Fetch all Locales from DB
                    $http({
                        method: 'GET',
                        url: backendlocale + "/userPreference/locales",
                        data: $scope.language.selected
                    }).then(function onSuccess(response) {
                        var localesFromDB = response.data["locales"];
                        if (undefined != response.data["selectedLocale"]) {
                            $scope.language.selected = response.data["selectedLocale"];
                            $scope.prevSelected = response.data["selectedLocale"];
                            $('.uiselect-choice-status-hidden-accessible').text("");
                            $('.uiselect-choice-status-hidden-accessible').text($scope.prevSelected.description);
                            $('.uiselect-choice-status-hidden-accessible').innerText = $scope.prevSelected.description;
                        }
                        $scope.disableButton = true;
                        $scope.localeList = localesFromDB;
                    }).catch(function onError(data) {
                        // Handle error
                        errorNotification();
                    });
                });
            };

            $scope.closePopup = function () {
                $scope.modalShown = !$scope.modalShown;
            };

            $scope.popupTitle = $.i18n.prop('userpreference.popup.language.heading');
            $scope.localeList = [];

            $scope.$watch("language.selected", function (newVal, oldVal) {
                if (undefined != $scope.prevSelected && undefined != $scope.language.selected && $scope.language.selected.locale === $scope.prevSelected.locale) {
                    $scope.disableButton = true;
                } else if (newVal != oldVal) {
                    $scope.disableButton = false;
                }
            });

            // To pass the selected Locale to backend and set in DB
            $scope.saveLocale = function () {
                if ($scope.disableButton) {
                    return false;
                }
                $scope.closePopup();
                $http({
                    method: 'POST',
                    url: backendlocale + "/userPreference/saveLocale",
                    data: $scope.language.selected,
                    cache: false
                }).then(function onSuccess(response, status) {
                    if (response.data && response.data.status === 'success') {
                        successNotification();
                    } else {
                        errorNotification();
                    }
                }).catch(function onError(data) {
                    // Handle error
                    errorNotification();
                });

            };


            $scope.language = {};

            function successNotification() {
                notifications.clearNotifications();
                var element1 = angular.element("#ariaNotificationCountText");
                var successmessage = $.i18n.prop('userpreference.notification.success.message');
                var successNotification = new Notification({
                    message: successmessage,
                    type: "success",
                    flash: "true",
                    elementToFocus: element1
                });
                notifications.addNotification(successNotification);
            }

            function errorNotification() {
                notifications.clearNotifications();
                var errorMessage = $.i18n.prop('userpreference.notification.failure.message');
                var errorNotification = new Notification({
                    message: errorMessage,
                    type: "error",
                    flash: "true"
                });
                notifications.addNotification(errorNotification);
                angular.element("div.notification-item-message").find('span:first').attr('tabindex', 0);
                angular.element("div.notification-item-message").find('span:first').addClass('notification-flyout-item');
                angular.element("div.notification-item-message").find('span:first').focus();
            }

        }]);
})();