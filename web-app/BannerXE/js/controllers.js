'use strict';

/* Controllers */

/**  Controller used in mainPage.html
 * @param $scope
 * @param $route
 * @param $routeParams
 * @param $location
 * @constructor
 */
function MainPageCtrl ($scope, $route, $routeParams, $location) {

}

/**
 * Controller used by banner.html. The main page of the App. This is where the Ng-App is located
 * @param $scope
 * @param $route
 * @param $routeParams
 * @param $location
 * @param $compile
 * @param $http
 * @param componentFactory
 * @constructor
 */
function MainCtrl($scope, $route, $routeParams, $location, $compile, $http, componentFactory) {

    $scope.$route = $route;
    $scope.$location = $location;
    $scope.$routeParams = $routeParams;

    /* Model used by Notification center for message and theme */
    $scope.message = 'First Message';
    $scope.theme = 'notification-center-normal';

    /* Routing function. Called to navigate to a page. This basically puts the partial page snippet to the Ng-view */
    $scope.setRoute = function (route) {
        $location.path(route);
    };

    /**
     * Event handler for Notification center messages. The child controllers, or components emit's
     * onNotificationMessage which is trapped by Main Controller here.
     */
    $scope.$on("onNotificationMessage", function (event, args) {
        /* Get the page Id from the location. ITs part of the URL */
        var pageId = $location.$$path.substring(1);

        /* Check for PageData in the current scope and initialize it */
        if( event.currentScope.pagedata[pageId] == undefined) {
            event.currentScope.pagedata[pageId] = {};
        }
        /* The message and theme for each page is kept in the pagedata for that page.
           This is required to show messages for specific pages.
         */
        event.currentScope.pagedata[pageId].message = args.message;
        event.currentScope.pagedata[pageId].theme = "notification-center-" + args.type;

        /* Set the current message and theme to the scope. This is the model used by Notification center to render
            the message and apply theme to the message on notification center.
         */
        event.currentScope.message = args.message;
        event.currentScope.theme = "notification-center-" + args.type;
    })

    /* The Global Model for the keyblock. It is used to store keyblock values across pages with key / value pair
        for each field on the keyblock. This can also be a page specific metadata. Right now its kept as global.
     */
    $scope.keyblock = {"subject":"ACCT"};

    /* The page states metadata is to store the state information of the page. Ideal candidates would be
        like, is the page in keyblock Start over mode, or Keyblock Go mode.
        Is the page displaying welcome message right now?. How many notification messages do this page has?.
        Right now it is just kept as a flag for each page. This is used to flip the welcome message and block container.
        When the user presses Go in keyblock for a page, this flag will set / reset.
        This can also be moved to pagedata
     */
    $scope.pagestates = {"basicCourseInformation" : false, "courseDetailInformation" : false};

    /* Button label for Keyblock. This needs to be refactored.. it should go inside each page bucket.. ideally
        should be part of pagestate or page metadata.
     */
    $scope.btnlabel = 'Go';

    /**
     * This is a global array which stores all the information about the page.
     * Each object in this array will have the key as the page ID. Any model information used inside the page
     * will go into this. Every block domain model (JSON) would be pushed to this array.
     * Storing this information at the Main Controller level will help to restore the data for the page when
     * switching the routes. This is the way we keep the page data in sync and not loose the unsaved data
     * when switching between pages.
     * @type {Array}
     */
    $scope.pagedata = [];

    /** This is just a flag to enable the extensibility framework. The checkbox on the header sets this model
     *  This is just used for demo purpose.
     */
    $scope.extensibilityenabled = false;

    /**
     * Angular fires this event $viewContentLoaded when ever user moves from one page to another.
     * Basically when the router switches and after the page content is loaded from the template.
     * This would be ideal place to deal with Extensibility. This is the right place to manipulate the DOM for the page
     * since the page Dom is completed available.
     */
    $scope.$on("$viewContentLoaded", function (event){

        /** Extract the page Id from the URL.
         * Initialize the page data, if coming for the first time.
         * @type {String}
         */
        var pageId = $location.$$path.substring(1);
        if(event.currentScope.pagedata[pageId] == undefined) {
            event.currentScope.pagedata[pageId] = {};
        }

        /** Grab the page specific message for notification center and push it to the current
         * model.
         */
        event.currentScope.message = event.currentScope.pagedata[pageId].message
        event.currentScope.theme = event.currentScope.pagedata[pageId].theme

        /* Check for extensibility and inject the DOM */
        if(event.currentScope.extensibilityenabled != undefined && event.currentScope.extensibilityenabled == true) {
            /** Get the page metadata from the extensibility folder of the App.
             * This should be a REST call, passing the page Id as the parameter.
             * Right now the metadata is stored under extensibility folder as a JSON text file.
             */
            $http.get('extensibility/' + pageId + '.JSON').success(function(data) {

                /* Store the metadata to the page data (The global page model ) */
                $scope.pagedata[pageId].extendedmetada = data;
                if($scope.pagedata[pageId] != undefined && $scope.pagedata[pageId].extendedmetada != undefined ) {
                    var metadata = $scope.pagedata[pageId].extendedmetada;

                    /* For each block process the metadata */
                    $(metadata.blocks).each(function() {
                        var blockId = this.blockId;
                        $(this.fields).each(function() {
                            var field = $("#" + this.fieldId);
                            /** Check whether this field exists. If exists, then check for visible
                             *  attribute. The metadata would be to turn off the field.
                             */
                            if($("#" + this.fieldId)[0]) {
                                if(this.visible == "false") {
                                    $("#" + this.fieldId).detach();
                                }
                            } else { /* If doesn't exists, then we are going to inject this field */
                                /* Use the Component Factory to get the component definition. Refer services.js file */
                                var comp = componentFactory.getComponent(this,pageId, blockId );

                                /* Let compile it in Angular way */
                                var component = $compile($(comp))($scope);

                                /* Now lets attach it to the DOM using the sibling */
                                var sibling = this.sibling
                                $(component).insertAfter($("#" + sibling));
                            }
                        });
                    });
                }
            });
        }

    });
}

/**
 * Controller used by myregistration.html page.
 * @param $scope
 * @param $http
 * @constructor
 */
function MyRegistrationPageCtrl($scope, $http) {

    /**
     * Scope variables definition. These are the models used in my registration page
     */
    $scope.myflag = true;
    $scope.instructorcolumns = [
        {"mDataProp":"bannerId", "sTitle":"ID", "bSortable":"true"},
        {"mDataProp":"firstName", "sTitle":"First Name", "bSortable":"true"},
        {"mDataProp":"lastName", "sTitle":"Last Name", "bSortable":"true"},
        {"mDataProp":"middleName", "sTitle":"Middle Name", "bSortable":"true"}
    ];
    $scope.coursenumbercolumns = [
        {"mDataProp":"subject", "sTitle":"Subject", "bSortable":"true"},
        {"mDataProp":"courseNumber", "sTitle":"Course Number", "bSortable":"true"},
        {"mDataProp":"termEffective", "sTitle":"Term Effective", "bSortable":"true"},
        {"mDataProp":"title", "sTitle":"Title", "bSortable":"true"}
    ];
    $scope.schedules = [{"key" : "0700", "label" : "07:00 AM"},
        {"key" : "0800", "label" : "08:00 AM"},
        {"key" : "0900", "label" : "09:00 AM"},
        {"key" : "1000", "label" : "10:00 AM"},
        {"key" : "1100", "label" : "11:00 AM"},
        {"key" : "1200", "label" : "12:00 AM"},
        {"key" : "1300", "label" : "01:00 PM"},
        {"key" : "1400", "label" : "02:00 PM"}];
    $scope.selectedCourses=[];

    /* Functions used in the page for event handling */

    /**
     * handler for search function. Triggered when clicked on Search button on the page.
     */
    $scope.handleSearch = function() {

        /* Toggle the my flag which will flip the search screen with the result screen. MVVM is great.!!.*/
        $scope.myflag = !$scope.myflag;

        /**
         * Now lets load a static JSON file for the search result. We need to make the REST full call here
         * to get the search results.
         */
        $http.get('searchresult.JSON').success(function(data) {
           $scope.sections = data.data;
        });

    };

    /* Create a generic function. I love reusability */
    var genericDayTimeDataset = function (time, day, cellvalue, cellcourse, cellstyleclass) {
        time[day] = {};
        time[day].value = cellvalue;
        time[day].course = cellcourse;

        time[day].class = cellstyleclass;

    };

    /**
     * Handler for adding a course to the list. Called when clicked on the Add button for each
     * course on the search result page.
     * @param section
     */
    $scope.addCourse = function(section) {

        /* push the course to the selected course model */
        $scope.selectedCourses.push(section);

        /** Now we need to push the course to the calendar component on the bottom left side of the page.
         * "schedules" is the model name which stores this information. Each item in the schedules model
         * represents a row in the schedule grid. Its one row for each hour of the day.
         * For each hour there is data for each day, which makes it a 2 dimentional array.
         */
        $.each(section.meetingsFaculty, function(key, val) {

            $.each($scope.schedules, function(key1, val1) {
                if(val1.key == val.meetingTime.beginTime) {
                    if(val.meetingTime.sunday) {
                        genericDayTimeDataset(val1, 'sunday', section.subject + " " + section.courseNumber, section, "schedule-pending");
                    }
                    if(val.meetingTime.monday) {
                        genericDayTimeDataset(val1, 'monday', section.subject + " " + section.courseNumber, section, "schedule-pending");
                    }
                    if(val.meetingTime.tuesday) {
                        genericDayTimeDataset(val1, 'tuesday', section.subject + " " + section.courseNumber, section, "schedule-pending");
                    }
                    if(val.meetingTime.wednesday) {
                        genericDayTimeDataset(val1, 'wednesday', section.subject + " " + section.courseNumber, section, "schedule-pending");
                    }
                    if(val.meetingTime.thursday) {
                        genericDayTimeDataset(val1, 'thursday', section.subject + " " + section.courseNumber, section, "schedule-pending");
                    }
                    if(val.meetingTime.friday) {
                        genericDayTimeDataset(val1, 'friday', section.subject + " " + section.courseNumber, section, "schedule-pending");
                    }
                    if(val.meetingTime.saturdayy) {
                        genericDayTimeDataset(val1, 'saturdayy', section.subject + " " + section.courseNumber, section, "schedule-pending");
                    }
                }
            });
        });
    };

    /**
     * Handler for removing a course from the selected list. Called when clicked on remove button from the courses in summary tab.
     * @param section
     */
    $scope.remove = function(section) {

        /* Get the index of the course */
        var index = $.inArray(section, $scope.selectedCourses);
        if(index != -1) {

            /* Remove teh course from the selected course */
            $scope.selectedCourses.splice(index, 1);

            $.each($scope.schedules, function(k, v) {
                if(v.sunday != undefined && v.sunday.course == section)  {
                   genericDayTimeDataset(v, 'sunday', null, null, "");
                }
                if(v.monday != undefined && v.monday.course == section)  {
                    genericDayTimeDataset(v, 'monday', null, null, "");
                }

                if(v.tuesday != undefined && v.tuesday.course == section)  {
                    genericDayTimeDataset(v, 'tuesday', null, null, "");
                }

                if(v.wednesday != undefined && v.wednesday.course == section)  {
                    genericDayTimeDataset(v, 'wednesday', null, null, "");
                }

                if(v.thursday != undefined && v.thursday.course == section)  {
                    genericDayTimeDataset(v, 'thursday', null, null, "");
                }

                if(v.friday != undefined && v.friday.course == section)  {
                    genericDayTimeDataset(v, 'friday', null, null, "");
                }

                if(v.saturday != undefined && v.saturday.course == section)  {
                    genericDayTimeDataset(v, 'saturday', null, null, "");
                }
            });
        }
    };
}

/**
 * Controller used in selfServiceLanding.html page
 * @param $scope
 * @param $http
 * @constructor
 */
function SelfServiceLandingPageCtrl($scope, $http) {

    /* The metadata for rendering the widgets in the landing page */
    $scope.pagedata.selfservicelandingpage = {"1personalinformation" :
                                                {"populartasks" : [{"label" : "Change Your Pin", "pageid" : "SCACRSE"},
                                                                   {"label" : "View Emergency Contact", "pageid" : "emercontact" },
                                                    {"label" : "Update Addresses", "pageid" : "test"},
                                                    {"label" : "Change Security Question", "pageid" : "test"},
                                                    {"label" : "Update E-mail Addresses", "pageid" : "test"},
                                                    {"label" : "Directory Profile", "pageid" : "test"}],
                                                    "title" : "Personal Information",
                                                    "description" : "Sample personal information",
                                                    "imageicon" : "img/img1.png"
                                                },
                                                "2advancementofficers" : {
                                                    "populartasks" : [{"label" : "Featured Link one", "pageid" : "dummy"},
                                                        {"label" : "Featured Link two", "pageid" : "dummy"},
                                                        {"label" : "Featured Link three", "pageid" : "dummy"},
                                                        {"label" : "Featured Link four", "pageid" : "dummy"},
                                                        {"label" : "Featured Link five", "pageid" : "dummy"},
                                                        {"label" : "Featured Link six", "pageid" : "dummy"}],
                                                    "title" : "Accounting Officers",
                                                    "description" : "Descriptio about accounting officers",
                                                    "imageicon" : "img/officer.png"
                                                },
                                                "3student" : {
                                                    "populartasks" : [{"label" : "Basic Course Information", "pageid" : "SCACRSE"},
                                                        {"label" : "Registration", "pageid" : "registration"},
                                                        {"label" : "View Final Grades", "pageid" : "dummy"},
                                                        {"label" : "Course Detail Information", "pageid" : "SCADETL"},
                                                        {"label" : "Events", "pageid" : "dummy"},
                                                        {"label" : "Account Holds", "pageid" : "dummy"}],
                                                    "title" : "Student",
                                                    "description" : "These are the services that are available for Stuents",
                                                    "imageicon" : "img/student.png"
                                                },
                                                "4facultyservices" : {
                                                    "populartasks" : [{"label" : "Enter Grades", "pageid" : "dummy"},
                                                        {"label" : "Registration", "pageid" : "dummy"},
                                                        {"label" : "View Final Grades", "pageid" : "dummy"},
                                                        {"label" : "Lookup Classes", "pageid" : "dummy"},
                                                        {"label" : "Events", "pageid" : "dummy"},
                                                        {"label" : "Account Holds", "pageid" : "dummy"}],
                                                    "title" : "Faculty Services",
                                                    "description" : "Sample factulty services.",
                                                    "imageicon" : "img/facultyservices.png"
                                                }};
}


/**
 * Controller used in PageBuilder.html
 * @param $scope
 * @param $http
 * @constructor
 */
function PageBuilderCtrl($scope, $http) {

    /**
     * function to reset the pagebuilder metadata
     */
    $scope.resetModel = function() {
        if($scope.pagedata.pagebuilder != undefined && $scope.pagedata.pagebuilder.pagemetadata != undefined) {
            $scope.pagedata.pagebuilder.pagemetadata = [];
        }
    };

    /**
     * Function to save the page metadata to the dynamicpages list on the global pagedata
     */
    $scope.savePage = function() {
        var name=prompt("Please enter a Page Id","");

        if (name!=null) {
            if($scope.pagedata.dynamicpages == undefined) {
                $scope.pagedata.dynamicpages = {};
            }
            $scope.pagedata.dynamicpages[name] = $scope.pagedata.pagebuilder.pagemetadata;
        }
    };
    $scope.propertymap = [];

    if($scope.pagedata.pagebuilder == undefined) {
        $scope.pagedata.pagebuilder= {};
        $scope.pagedata.pagebuilder.pagemetadata = [];
        $scope.pagedata.pagebuilder.populartasks = [{"label" : "Link", "pageid" : "SCACRSE"},
                                                                {"label" : "Sample Link 1", "pageid" : "registration"},
                                                                {"label" : "Sample Link 2", "pageid" : "dummy"},
                                                                {"label" : "Sample Link 3", "pageid" : "SCADETL"},
                                                                {"label" : "Sample Link 4", "pageid" : "dummy"},
                                                                {"label" : "Sample Link 5", "pageid" : "dummy"}];
    }
}

/**
 * Controller used in dynamicpage.html. This is a page to demonstrate the runtime engine for rendering
 * a metadata driven pages build using page builder.
 * @param $scope
 * @param $http
 * @param $routeParams
 * @constructor
 */
function DynamicPageCtrl($scope, $http, $routeParams) {
    /**
     * Check where the page id coming from the request exists in the dynamic page metadata.
     * If exists then set this as the current pagemetadata which is used as the page model for rendering
     */
    if( $scope.pagedata.dynamicpages != undefined && $scope.pagedata.dynamicpages[$routeParams.pageId] != undefined) {

        $scope.pagemetadata = $scope.pagedata.dynamicpages[$routeParams.pageId];

    }else {

        /**
         * If page doesn't exist, then push a metadata to show page not found...
         */
        $scope.pagemetadata = [{"component" :  "h3",
                                            "metadata" : {"text" : "OOPS...!!! Page Not Found.!!!", "attributes" : {},
                                                "children" : []  } }];
    }
}


/* Admin Page Controllers */
/**
 * Controller used in basicCourseInformation.html page
 * @param $scope
 * @param $http
 * @param $templateCache
 * @constructor
 */

function BasicCourseInformationPageCtrl($scope, $http, $templateCache) {

    $scope.coursenumbercolumns = [
        {"mDataProp":"subject", "sTitle":"subject", "bSortable":"true"},
        {"mDataProp":"courseNumber", "sTitle":"courseNumber", "bSortable":"true"},
        {"mDataProp":"termEffective", "sTitle":"termEffective", "bSortable":"true"},
        {"mDataProp":"title", "sTitle":"title", "bSortable":"true"}
    ];
    $scope.subjectcolumns = [
        {"mDataProp":"code", "sTitle":"Code", "bSortable":"true"},
        {"mDataProp":"description", "sTitle":"Description", "bSortable":"true"},
        {"mDataProp":"lastModifiedBy", "sTitle":"Last Modified By", "bSortable":"true"},
        {"mDataProp":"lastModified", "sTitle":"Activity Date", "bSortable":"true", sType:'date',
            "fnRender":function (oObj) {
                var javascriptDate = new Date(oObj.aData.lastModified);
                javascriptDate = javascriptDate.getDate() + "/" + javascriptDate.getMonth() + "/" + javascriptDate.getFullYear();
                return "<div class= date>" + javascriptDate + "<div>";
            }
        }
    ];

    /**
     * Event handler for keyblock Go / Startover click
     */
    $scope.$on("onKeyblockNext", function (event) {
        /* toggle the page state to flip the welcome message with the blocks */
        event.currentScope.pagestates.basicCourseInformation = !event.currentScope.pagestates.basicCourseInformation;

        if (event.currentScope.pagestates.basicCourseInformation) {
            event.currentScope.btnlabel = 'Start Over';
            /**
             * Broad cast onLoad event to all the controllers listening.
             */
            event.currentScope.$broadcast("onLoad", []);
        } else {
            event.currentScope.btnlabel = 'Go';
        }
        try {
            event.currentScope.$apply();
        } catch (e) {
            //$exceptionHandler(e);
        }
    });

    /* Function to raise a message to the notification center */
    $scope.raiseMessage = function (message, type) {
        $scope.$emit("onNotificationMessage", {message:message, type:type});
    };

    $scope.$on("dirty", function (event) {
        //alert ("Dirty received " + $scope.course.title);

    });
}

/**
 * Controller used in Course Detail Block on basic Course Information page.
 * @param $scope
 * @param $http
 * @param $templateCache
 * @constructor
 */
function CourseGeneralInformationController($scope, $http, $templateCache) {
    /**
     * Listener for onLoad. This makes a REST full call to fetch data for this block
     */
    $scope.$on("onLoad", function () {

        var url = "http://localhost:8080/BannerServices/lookup?callback=JSON_CALLBACK&serviceName=courseGeneralInformationService&methodName=fetchBySubjectCourseNumberAndTermEffective";

        /* Lets push all the keyblock values to the URL. This would be required by the service to fetch data*/
        $.each($scope.keyblock, function (k, v) {
            url += "&" + k + "=" + v;
        });

        /* Now lets Make the REST full call. Its JSONP since Cross domain */
        $http({method:"JSONP", url:url, cache:$templateCache}).
            success(function (data, status, headers, config) {

                /* Initialize the page global page metada */
                if($scope.pagedata.basicCourseInformation == undefined) {
                    $scope.pagedata.basicCourseInformation = {};
                }

                /* Store the JSON response to the page metadata for the block*/
                $scope.pagedata.basicCourseInformation.courseGeneralInformation = data;

            }).
            error(function (data, status, headers, config) {

                /* On service error, raise an error message to notification center */
                $scope.$emit("onNotificationMessage", {message:"Internal error occured in service call.", type:"error"});
            });
    });

}

/**
 * Controller used in Course Level Block on basic Course Information page.
 * @param $scope
 * @param $http
 * @param $templateCache
 * @constructor
 */
function CourseLevelController($scope, $http, $templateCache) {
    /**
     * Listener for onLoad. This makes a REST full call to fetch data for this block
     */
    $scope.$on("onLoad", function () {
        var url = "http://localhost:8080/BannerServices/lookup?callback=JSON_CALLBACK&serviceName=courseLevelService&methodName=fetchBySubjectCourseNumberAndTermEffective";

        /* Lets push all the keyblock values to the URL. This would be required by the service to fetch data*/
        $.each($scope.keyblock, function (k, v) {
            url += "&" + k + "=" + v;
        });

        /* Now lets Make the REST full call. Its JSONP since Cross domain */
        $http({method:"JSONP", url:url, cache:$templateCache}).
            success(function (data, status, headers, config) {

                /* Initialize the page global page metada */
                if($scope.pagedata.basicCourseInformation == undefined) {
                    $scope.pagedata.basicCourseInformation = {};
                }

                /* Store the JSON response to the page metadata for the block*/
                $scope.pagedata.basicCourseInformation.courseLevels = data;
            }).
            error(function (data, status, headers, config) {

                /* On service error, raise an error message to notification center */
                $scope.$emit("onNotificationMessage", {message:"Internal error occured in service call.", type:"error"});
            });
    });
}

/**
 * Controller used in Course Grading Mode Block on basic Course Information page.
 * @param $scope
 * @param $http
 * @param $templateCache
 * @constructor
 */
function CourseGradingModeController($scope, $http, $templateCache) {

    /**
     * Listener for onLoad. This makes a REST full call to fetch data for this block
     */
    $scope.$on("onLoad", function () {
        var url = "http://localhost:8080/BannerServices/lookup?callback=JSON_CALLBACK&serviceName=courseGradingModeService&methodName=fetchBySubjectCourseNumberAndTermEffective";

        /* Lets push all the keyblock values to the URL. This would be required by the service to fetch data*/
        $.each($scope.keyblock, function (k, v) {
            url += "&" + k + "=" + v;
        });

        /* Now lets Make the REST full call. Its JSONP since Cross domain */
        $http({method:"JSONP", url:url, cache:$templateCache}).
            success(function (data, status, headers, config) {

                /* Initialize the page global page metada */
                if($scope.pagedata.basicCourseInformation == undefined) {
                    $scope.pagedata.basicCourseInformation = {};
                }

                /* Store the JSON response to the page metadata for the block*/
                $scope.pagedata.basicCourseInformation.courseGradingModes = data;
            }).
            error(function (data, status, headers, config) {

                /* On service error, raise an error message to notification center */
                $scope.$emit("onNotificationMessage", {message:"Internal error occured in service call.", type:"error"});
            });
    });
}


/**
 * Controller used in courseDetailInformation.html page
 * @param $scope
 * @param $http
 * @param $templateCache
 * @constructor
 */
function CourseDetailInformationPageCtrl($scope, $http, $templateCache) {

    $scope.coursenumbercolumns = [
        {"mDataProp":"subject", "sTitle":"subject", "bSortable":"true"},
        {"mDataProp":"courseNumber", "sTitle":"courseNumber", "bSortable":"true"},
        {"mDataProp":"termEffective", "sTitle":"termEffective", "bSortable":"true"},
        {"mDataProp":"title", "sTitle":"title", "bSortable":"true"}
    ];
    $scope.subjectcolumns = [
        {"mDataProp":"code", "sTitle":"Code", "bSortable":"true"},
        {"mDataProp":"description", "sTitle":"Description", "bSortable":"true"},
        {"mDataProp":"lastModifiedBy", "sTitle":"Last Modified By", "bSortable":"true"},
        {"mDataProp":"lastModified", "sTitle":"Activity Date", "bSortable":"true", sType:'date',
            "fnRender":function (oObj) {
                var javascriptDate = new Date(oObj.aData.lastModified);
                javascriptDate = javascriptDate.getDate() + "/" + javascriptDate.getMonth() + "/" + javascriptDate.getFullYear();
                return "<div class= date>" + javascriptDate + "<div>";
            }
        }
    ];


    $scope.$on("onKeyblockNext", function (event) {
        event.currentScope.pagestates.courseDetailInformation = !event.currentScope.pagestates.courseDetailInformation;
        if (event.currentScope.pagestates.courseDetailInformation) {
            event.currentScope.btnlabel = 'Start Over';
        } else {
            event.currentScope.btnlabel = 'Go';
        }
        //event.currentScope.fetch();
        //alert (event.currentScope.keyblock.subject + " " + event.currentScope.keyblock.course + " " + event.currentScope.keyblock.term);
        try {
            event.currentScope.$apply();
        } catch (e) {
            //$exceptionHandler(e);
        }

        event.currentScope.$broadcast("onLoad", []);

    });

    $scope.raiseMessage = function (message, type) {
        $scope.$emit("onNotificationMessage", {message:message, type:type});
    };
}