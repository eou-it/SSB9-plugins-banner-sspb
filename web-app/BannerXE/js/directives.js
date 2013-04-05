'use strict';

/* Directives */
var directivesModule = angular.module('popup.directives', []);
directivesModule.directive('ngPopup', function (PopupService) {
    return {
        restrict:'A',
        link:function postLink(scope, element, attrs) {
            var ngPopupUrl = attrs['ngPopupUrl'];
            // Could have custom or boostrap modal options here
            var popupOptions = {};
            element.bind("click", function () {
                PopupService.load(ngPopupUrl, scope, popupOptions);
            });
        }
    };
});

directivesModule.directive('ngConfirm', function (PopupService) {
    return {
        restrict:'E',
        link:function postLink(scope, element, attrs) {
            // Could have custom or boostrap modal options here
            var popupOptions = {};
            element.bind("click", function () {
                PopupService.confirm(attrs["title"], attrs["actionText"],
                    attrs["actionButtonText"], attrs["actionFunction"],
                    attrs["cancelButtonText"], attrs["cancelFunction"],
                    scope, popupOptions);
            });
        }
    };

});

directivesModule.directive('ngAlert', function (PopupService) {
    return {
        restrict:'E',
        link:function postLink(scope, element, attrs) {
            // Could have custom or boostrap modal options here
            var popupOptions = {};
            element.bind("click", function () {
                PopupService.alert(attrs["title"], attrs["text"],
                    attrs["buttonText"], attrs["alertFunction"],
                    scope, popupOptions);
            });
        }
    };

});


var banner9AdminDirectives = angular.module('banner9Admin.directives', ['popup.directives', 'popup.service', 'extensibility.services']);

banner9AdminDirectives.directive('applicationpage',function () {
    return {
        restrict:'E',
        transclude:'true',
        scope:{},
        template:'<div class="applicationpage">' +
            '<div ng-transclude></div>' +
            '<div class="statusbar">' +
            '<div class="status-block-buttons">' +
            '<button class="primary-button">Save</button>' +
            '</div>' +
            '</div>' +
            '</div>',
        replace:true
    };
}).directive('blockcontainer',function () {
        return {
            restrict:'E',
            transclude:true,
            scope:{show:'@show'},
            template:'<div class="blockcontainer" ng-show="show" ng-transclude>' +
                '</div>',
            replace:true
        };
    }).directive('blocktoolbar',function () {
        return {
            restrict:'E',
            transclude:true,
            scope:{insertable:'=insertable', deletable:'=deletable', copyable:'=copyable', searchable:'=searchable'},
            template:'<div>' +
                '<div ng-show = "searchable" class="toolbar-filter" >Filter</div>' +
                '<div ng-show = "copyable" class="toolbar-copy" >Copy</div>' +
                '<div ng-show = "deletable" class="toolbar-delete" >Delete</div>' +
                '<div ng-show = "insertable"  class="toolbar-insert" >Insert</div>' +
                '</div>',
            replace:true,
            link:function (scope, element, attrs) {
                $(element).find(".toolbar-insert").on("click", function () {
                    scope.$emit("onInsert", {});
                });
            }
        };
    }).directive('block',function ($compile) {
        return {
            restrict:'E',
            transclude:true,
            scope:{title:'@title', insertable:'=insertable', deletable:'=deletable', copyable:'=copyable', searchable:'=searchable', collapsible:'=collapsible'},
            template:'<div class="block" >' +
                '<div style="background:-moz-linear-gradient(center top , #F1F2F4 0%, #E5E8EC 100%) repeat scroll 0 0 transparent; border-bottom: 2px solid darkgray;">' +
                '<table style="width:100%;"><tr><td style="width:0px;">' +
                '<span class="block-open" ng-show="collapsible"></span>' +
                '</td><td>' +
                '<label class="blocklabel">{{title}}</label>' +
                '</td><td style="float:right;">' +
                '<blocktoolbar insertable = "insertable" deletable  = "deletable"  copyable = "copyable" searchable  = "searchable"></blocktoolbar>' +
                '</td></tr>' +
                '</table>' +
                '</div>' +
                '<div class="blockContent"  ng-transclude>' +
                '</div>' +
                '</div>',
            link:function (scope, element, attrs) {
                // Title element
                var blockExpColapseBtn = angular.element(element.children()[0]).find("span"),
                // Opened / closed state
                    opened = true;
                // Clicking on title should open/close the zippy
                blockExpColapseBtn.bind('click', toggle);

                // Toggle the closed/opened state
                var height = $(element.children()[1]).height();

                function toggle() {
                    opened = !opened;
                    if (!opened) {
                        $(element.children()[1]).hide().slideDown(500);
                        //$(element.children()[1]).fadeOut('slow');// .slideDown(500);

                    } else {
                        $(element.children()[1]).show().slideUp(500);

                    }
                    blockExpColapseBtn.removeClass(opened ? 'block-open' : 'block-close');
                    blockExpColapseBtn.addClass(opened ? 'block-close' : 'block-open');
                }

                // initialize the zippy
                toggle();
            },
            replace:true

        };
    }).
    /*
    directive('formitem',function () {
        return {
            restrict:'E',
            transclude:true,
           // scope:{label:'@label', float:'@float'},
            template:'<div class="formitem{{float}}">' +
                '<div class="formitemlabel">' +
                '<span><label class="formitemlabel">{{label}}</label></span>' +
                '</div>' +
                '<div class="formitemComponent">' +
                '<div class="forrmitemwrap" ng-transclude>' +
                '</div>' +
                '</div>' +
                '</div>',
            link:function (scope, element, attrs) {
                $(element).find("input").on("focus", function () {
                    $(element).find(".formitemComponent").addClass("formitemComponent-focus");
                });
                $(element).find("input").on("blur", function () {
                    $(element).find(".formitemComponent").removeClass("formitemComponent-focus");
                });

            },
            replace:true
        };
    }).
    directive('form',function () {
        return {
            restrict:'E',
            transclude:true,
            //scope:{},
            template:'<div style="padding-bottom:2px;" >' +
                '<div class="form" ng-transclude>' +
                '</div>' +
                '</div>',
            replace:true
        };
    }). */
    directive('column',function () {
        return {
            restrict:'E',
            transclude:true,
            scope:{},
            template:'<div style="width:50%;float: left;" ng-transclude>' +
                '</div>',
            replace:true
        };
    }).
    /*
    directive('textbox',function () {
        return {
            restrict:'E',
            transclude:true,
            //scope:{value:'=value', width:'@width'},
            template:'<input class="textbox" type="text" ng-model="value" style="width : {{width}};"/>',
            replace:true,
            link:function (scope, element, attrs) {
                $(element).on("change", function () {
                    scope.$emit("dirty", {});
                });
                scope.$watch(attrs.value, function () {
                    //alert("value changed");
                });
            }

        };
    }).
    directive('checkbox',function () {
        return {
            restrict:'E',
            transclude:true,
            //scope:{label:'@', value:'=value'},
            template:'<div>' +
                '<input type="checkbox" style="float:left;" ng-model="value"/>' +
                '<label style="float:left;padding-left: 5px;">{{label}}</label>' +
                '</div>',
            replace:true
        };
    }).
    directive('radio',function () {
        return {
            restrict:'E',
            transclude:true,
            scope:{label:'@label'},
            template:'<div>' +
                '<input type="radio" style="float:left;" name="test"/>' +
                '<label style="float:left;padding-left: 5px;padding-right: 5px;">{{label}}</label>' +
                '</div>',
            replace:true
        };
    }).  */
    directive('lookup',function ($compile, $http) {
        return {
            restrict:'E',
            transclude:true,
            scope:{label:'@label', clazzname:'@clazzname', value:'=value', methodname:'@methodname',
                additionalfilter:'@additionalfilter', columnmetadata:'=columnmetadata',
                valuecolumn:'@valuecolumn', adddata:"=adddata"},
            template:'<div>' +
                '<input type="text" class="lookup" style="text-transform: uppercase;" ng-model="value"/>' +
                '<i class="lookup-btn"></i>' +
                '</div>',
            link:function (scope, element, attrs) {
                if (attrs.additionalfilter != undefined) {
                    scope[attrs.additionalfilter] = "=" + attrs.additionalfilter;
                }

                scope.lookupdata = null;
                var lookupBtn = angular.element(element.children()[1])

                // Clicking on title should open/close the zippy
                lookupBtn.bind('click', openLookup);
                var modalElement = $('<div class="modal hide"></div>');

                function openLookup() {
                    var lookuptable = '<table id="lookuptable">' +
                        '</table>';

                    var header = '<div class="modal-header"><span class="modal-title">' + attrs.label + '</span></div>';
                    var body = lookuptable;
                    var footer = '<div class="modal-footer">';
                    footer += '<button class="btn" id="cancelBtn">Cancel</button><button class="btn" id="okBtn">Ok</button></div>';

                    modalElement.html(header + body + footer);
                    $compile(modalElement)(scope);
                    var lookupTab = angular.element(modalElement).find("table");

                    var url = "http://localhost:8080/BannerServices/lookup?clazzName=" + attrs.clazzname;
                    if (attrs.methodname != undefined) {
                        url += "&methodName=" + attrs.methodname;
                    }
                    if (attrs.additionalfilter != undefined) {
                        url += "&additionalFilters=" + attrs.additionalfilter;
                        url += "&" + attrs.additionalfilter + "=" + scope["adddata"];
                    }
                    var columnMetadata
                    if (scope.columnmetadata == undefined) {
                        columnMetadata = [
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
                    } else {
                        columnMetadata = scope.columnmetadata;
                    }
                    var table = $(lookupTab).dataTable({
                        "bProcessing":true,
                        "bServerSide":true,
                        "bPaginate":true,
                        "bLengthChange":false,
                        "bFilter":true,
                        "iDisplayLength":10,
                        "sPaginationType":"two_button",
                        "sAjaxSource":url,
                        "fnServerData":function (sUrl, aoData, fnCallback, oSettings) {
                            oSettings.jqXHR = $.ajax({
                                "url":sUrl,
                                "data":aoData,
                                "success":fnCallback,
                                "dataType":"jsonp",
                                "cache":false
                            });
                        },
                        "aoColumns":columnMetadata
                    });


                    $(lookupTab).find("tbody").delegate("tr", "click", function (event) {
                        $(table.fnSettings().aoData).each(function () {
                            $(this.nTr).removeClass('selected');
                        });
                        $(event.target.parentNode).addClass('selected');
                    });
                    $("#lookupTab tbody").click(function (event) {
                        $(oTable.fnSettings().aoData).each(function () {
                            $(this.nTr).removeClass('selected');
                        });
                        $(event.target.parentNode).addClass('selected');
                    });

                    modalElement.modal();
                    scope.$apply();


                    var fnGetSelected = function (oTableLocal) {
                        var aReturn = new Array();
                        var aTrs = oTableLocal.fnGetNodes();

                        for (var i = 0; i < aTrs.length; i++) {
                            if ($(aTrs[i]).hasClass('selected')) {
                                aReturn.push(aTrs[i]);
                            }
                        }
                        return aReturn;
                    }

                    $("#okBtn").click(function () {
                        var aData = table.fnGetData(fnGetSelected(table)[0]);
                        scope.value = aData[scope.valuecolumn || 'code'];
                        scope.$apply();
                        modalElement.modal('hide');
                        $(element).find("input").focus();
                        modalElement.detach();
                    });
                    $("#cancelBtn").click(function () {
                        modalElement.modal('hide');
                        $(element).find("input").focus();
                        modalElement.detach();
                    });

                }
            },

            replace:true
        };
    }).
    directive('keyblock',function () {
        return {
            restrict:'E',
            transclude:true,
            scope:{btnlabel:'=btnlabel'},
            template:'<div style="float:left;width:100%;margin-bottom: 5px;">' +
                '<div ng-transclude>' +
                '</div>' +
                '<div class="btnContainer">' +
                '<button class="keyblock-btn" >{{btnlabel}}</button>' +
                '</div>' +
                '</div>',
            link:function (scope, element, attrs) {

                var keyblockBtn = angular.element(element.children()[1]).find("button")

                // Clicking on title should open/close the zippy
                keyblockBtn.bind('click', toggle);

                function toggle() {
                    scope.$emit("onNotificationMessage", {message:'', type:'normal'});
                    scope.$emit("onKeyblockNext", {});

                }


            },

            replace:true
        };
    }).
    directive('notificationcenter',function () {
        return {
            restrict:'E',
            transclude:true,
            scope:{message:'=message', theme:'=theme' },
            template:'<div class="notificationcenter">' +
                '<div class="notificationcenter-counter">NOTIFICATION CENTER:</div>' +
                '<div class="notificationcenter-message {{theme}}">' +
                '<span>{{message}}</span>' +
                '</div>' +
                '</div>',
            link:function (scope, element, attrs) {

            },

            replace:true
        };
    }).
    directive('tabs',function () {
        return {
            restrict:'E',
            transclude:true,
            scope:{},
            controller:function ($scope, $element) {
                var panes = $scope.panes = [];

                $scope.select = function (pane) {
                    angular.forEach(panes, function (pane) {
                        pane.selected = false;
                    });
                    pane.selected = true;
                }

                this.addPane = function (pane) {
                    if (panes.length == 0) $scope.select(pane);
                    panes.push(pane);
                }
            },
            template:'<div class="tabbable">' +
                '<ul class="nav nav-tabs">' +
                '<li ng-repeat="pane in panes" ng-class="{active:pane.selected}">' +
                '<a href="" ng-click="select(pane)">{{pane.title}}</a>' +
                '</li>' +
                '</ul>' +
                '<div class="tab-content" ng-transclude></div>' +
                '</div>',
            replace:true
        };
    }).
    directive('pane',function () {
        return {
            require:'^tabs',
            restrict:'E',
            transclude:true,
            scope:{ title:'@' },
            link:function (scope, element, attrs, tabsCtrl) {
                tabsCtrl.addPane(scope);
            },
            template:'<div class="tab-pane" ng-class="{active: selected}" ng-transclude>' +
                '</div>',
            replace:true
        };
    }).     /*
    directive('listbox',function ($http) {
        return {
            restrict:'E',
            transclude:true,
            scope:{ title:'@' },
            link:function (scope, element, attrs) {

                $(element).dataTable({
                    "bProcessing":true,
                    "bServerSide":true,
                    "bPaginate":true,
                    "bLengthChange":false,
                    "bFilter":true,
                    "iDisplayLength":10,
                    "sPaginationType":"two_button",
                    "sAjaxSource":"http://localhost:8080/banner_global_app/lookup?clazzName=net.hedtech.banner.general.system.Subject&max=500&offset=0",
                    "fnServerData":function (sUrl, aoData, fnCallback, oSettings) {
                        oSettings.jqXHR = $.ajax({
                            "url":sUrl,
                            "data":aoData,
                            "success":fnCallback,
                            "dataType":"jsonp",
                            "cache":false
                        });
                    },
                    "aoColumns":[
                        {"mDataProp":"code", "sTitle":"Code", "bSortable":"true"},
                        {"mDataProp":"description", "sTitle":"Description", "bSortable":"true"},
                        {"mDataProp":"lastModifiedBy", "sTitle":"Activity Date", "bSortable":"true"}
                    ]
                });
            },
            template:'<table id="example">' +
                '<thead><tr><th>Code</th><th>Description</th><th>Activity Date</th></tr></thead><tbody></tbody>' +
                '</table>',
            replace:true
        };
    }).           */
    directive('grid',function () {
        return {
            restrict:'E',
            transclude:true,
            link:function (scope, element, attrs) {
                var metadata = scope.$eval(attrs['metadata']);
                var gridId = metadata.gridId;
                var columnsMetadata = [];
                var i = 0;
                $(metadata.columnIds).each(function () {
                    columnsMetadata.push({"mDataProp":this, "sTitle":metadata.columnHeaders[i], "bSortable":"true"});
                    i++;
                })
                //alert (scope.$eval(attrs['mydata']));
                var table = $(element).dataTable({
                    "bStateSave":true,
                    "iCookieDuration":2419200, /* 1 month */
                    "bInfo":false,
                    "bDestroy":true,
                    "bPaginate":true,
                    "bLengthChange":false,
                    "bFilter":false,
                    "aoColumns":columnsMetadata,
                    sPaginationType:"full_numbers"
                });

                // watch for any changes to our data, rebuild the DataTable
                scope.$watch(attrs.aaData, function (value) {
                    table.fnClearTable();
                    table.fnAddData(scope.$eval(attrs.aaData));
                });


                $(table).find("tbody").delegate("tr", "click", function () {
                    var iPos = table.fnGetPosition(this);
                    $(this).addClass("selected").siblings().removeClass("selected");
                    if (iPos != null) {
                        //couple of example on what can be done with the clicked row...
                        var aData = table.fnGetData(iPos);//get data of the clicked row
                        var iId = aData[1];//get column data of the row
                    }
                });

                $(table.selector + ' tbody tr').live('click', function (event) {
                });
            },
            template:'<table class="display" width="100%">' +
                '</table>',
            replace:true
        };
    }).directive('servicemodule',function () {
        return {
            restrict:'E',
            transclude:true,
            scope:{title:'@title', description:'@description', imageicon:'@imageicon', populartasks:'=populartasks' },
            template:'<div class="servicemodule">' +
                '<div class="groupbox" >' +
                '<div class="groupbox-header">' +
                '<table style="width:100%;"><tr><td style="width:0px;">' +
                '<span class="groupbox-open"></span>' +
                '</td><td>' +
                '<label class="groupboxlabel">{{title}}</label>' +
                '</td><td style="float:right;">' +
                '</td></tr>' +
                '</table>' +
                '</div>' +
                '<div class="groupboxcontent">' +

                '<pre style="border:none;background: transparent;height:50px;">' +
                '<img src="{{imageicon}}" style="float:left;" ng-show="imageicon"/>' +
                '{{description}}' +
                '</pre>' +
                '<div style="padding-left:30px;" ng-show="populartasks">' +
                '<h4 padding>Popular Tasks</h4>' +
                '<div style="width:100%;" id="populartaskcontainer" >' +
                '<div style="float:left;width:30%;" ng-repeat="task in populartasks"><a href="#/{{task.pageid}}">{{task.label}}</a></div>' +
                '<div style="float:left;height: 10px;width:100%;">&nbsp;</div>' +
                '<div style="float:left;width:30%;">&nbsp;</div>' +
                '<div style="float:left;width:30%;">&nbsp;</div>' +
                '<div style="float:left;width:30%;margin-bottom: 10px;"><a href="">View All...</a></div>' +
                '</div>' +
                '</div>' +

                '</div>' +
                '</div>' +
                '</div>',
            link:function (scope, element, attrs) {

                // Title element
                var blockExpColapseBtn = angular.element(element.children()[0]).find("span"),
                // Opened / closed state
                    opened = true;
                // Clicking on title should open/close the zippy
                blockExpColapseBtn.bind('click', toggle);

                // Toggle the closed/opened state
                var height = $(element.children()[1]).height();

                function toggle() {
                    opened = !opened;
                    if (!opened) {
                        $($(element.children()[0]).children()[1]).hide().slideDown(500);

                    } else {
                        $($(element.children()[0]).children()[1]).show().slideUp(500);
                    }
                    blockExpColapseBtn.removeClass(opened ? 'groupbox-open' : 'groupbox-close');
                    blockExpColapseBtn.addClass(opened ? 'groupbox-close' : 'groupbox-open');
                }

                toggle();
            },
            replace:true

        };
    }).directive('servicemodulecontainer',function () {
        return {
            restrict:'E',
            transclude:true,
            scope:{modules:'=modules'},
            template:'<div class="servicemodulecontainer" ng-transclude>' +
                '<servicemodule ng-repeat="module in modules" title="{{module.title}}" description="{{module.description}}" imageicon="{{module.imageicon}}"  populartasks="module.populartasks"></servicemodule>' +
                '</div>',
            link:function (scope, element, attrs) {

            },
            replace:true

        };
    }).directive('borderlayout',function () {
        return {
            restrict:'E',
            transclude:true,

            template:'<div>' +
                '<DIV class="layout-container">' +
                '<DIV class="ui-layout-center">Center' +
                '<P><A href="http://layout.jquery-dev.net/demos.html">Go to the Demos page</A></P>' +
                '<P>* Pane-resizing is disabled because ui.draggable.js is not linked</P>' +
                '<P>* Pane-animation is disabled because ui.effects.js is not linked</P>' +
                '</DIV>' +
                '<DIV class="ui-layout-north">North</DIV>' +
                '<DIV class="ui-layout-south">South</DIV>' +
                '<DIV class="ui-layout-east">East</DIV>' +
                '<DIV class="ui-layout-west">West</DIV>' +
                '</DIV>' +
                '</div>',
            link:function (scope, element, attrs) {
                //var borderlayout = $(element).layout({
                //  west__size:	325
                //	,east__size: 325

                //});
                var layout = $(element).find(".layout-container").layout({ applyDefaultStyles:true });
            },
            replace:true

        };
    }).directive('select2',function () {
        return {
            restrict:'A',
            transclude:true,

            link:function (scope, element, attrs) {
                //var layout = $(element).select2();
                $(element).select2({
                    data:[
                        {id:0, text:'story'},
                        {id:1, text:'bug'},
                        {id:2, text:'task'}
                    ],
                    ajax:{ // instead of writing the function to execute the request we use Select2's convenient helper
                        url:"http://localhost:8080/BannerServices/ssb/classSearch/getTerms",
                        dataType:'jsonp',
                        data:function (term, page) {
                            return {
                                sSearch:term, // search term
                                iDisplayLength:100,
                                apikey:"ju6z9mjyajq2djue3gbvv26t" // please do not use so this example keeps working
                            };
                        },
                        results:function (data, page) { // parse the results into the format expected by Select2.
                            // since we are using custom formatting functions we do not need to alter remote JSON data
                            return {results:data};
                        }
                    },
                    formatResult:function (obj) {
                        console.log(obj.label);
                        return '<span>' + obj.label + '</span>';

                    }
                });
            },
            replace:true

        };
    }).directive('sectionheader',function () {
        return {
            restrict:'E',
            transclude:true,
            scope:{title:"@title"},
            template:'<div style="background:-moz-linear-gradient(center top , #F1F2F4 0%, #E5E8EC 100%) repeat scroll 0 0 transparent; border-bottom: 4px solid #A1A1A1;">' +
                '<label class="blocklabel">{{title}}</label>' +
                '</div>',
            link:function (scope, element, attrs) {
            },
            replace:true

        };
    }).directive('servicelauncher',function () {
        return {
            restrict:'E',
            transclude:true,
            scope:{link:"@link", href:"@href", imageicon:"@imageicon", description:"@description"},
            template:'<div style="padding:10px;border-bottom:2px dotted;margin: 10px;">' +
                '<table>' +
                '<tr>' +
                '<td>' +
                '<img src="{{imageicon}}" style="float:left;"/>' +
                '</td>' +
                '<td>' +
                '<div style="float:left;">' +
                '<a style="font-size: 16px;font-weight: bold;text-decoration: underline;" href="{{href}}">{{link}}</a>' +
                '<br/>' +
                '<span>' +
                '{{description}}' +
                '</span>' +
                '</div>' +
                '</td>' +
                '</tr>' +
                '</table>' +
                '</div>',
            link:function (scope, element, attrs) {
            },
            replace:true

        };
    }).directive('dynamicpage',function ($compile) {
        return {
            restrict:'A',
            transclude:true,
            link:function (scope, element, attrs) {
                var metadata = scope[attrs['metadata']];
                var comp = $('<servicemodule title="Testing" description="Sample description"></servicemodule>');
                var markup = '';
                var markup2 = '';
                var process = function (metadata, elem, transclude) {
                    $.each(metadata, function (k, v) {
                        var attribs = '';
                        if (v.metadata != undefined && v.metadata.attributes != undefined) {
                            $.each(v.metadata.attributes, function (key, val) {
                                attribs += ' ' + key + '="' + val + '" ';
                            });
                        }
                        var innerText = '';
                        if (v.metadata != undefined && v.metadata.text != undefined) {
                            innerText = v.metadata.text;
                        }
                        var compDef = '<' + v.component + attribs + '>' + innerText + '</' + v.component + '>'
                        markup += '<' + v.component + attribs + '>';
                        markup2 = '</' + v.component + '>' + markup2;
                        var cmp = $(compDef);
                        var newCmp = $compile(cmp)(scope);
                        if (transclude != undefined) {
                            $(elem).find(transclude).append(newCmp);
                        } else {
                            elem.append(newCmp);
                        }

                        if (v.metadata != undefined && v.metadata.children != undefined) {
                            process(v.metadata.children, newCmp, v.metadata.transclude);
                        }

                    })
                };
                process(metadata, element, undefined);
            }
        };
    }).directive('draggable',function () {
        return {
            restrict:'A',
            transclude:true,

            link:function (scope, element, attrs) {
                $(element).draggable({
                    revert:true,
                    start:function (event, ui) {
                        $(element).draggable('option', 'revertDuration', 200);
                        $(element).addClass('jqui-dnd-item-dragging');
                        scope.$apply();
                    },
                    stop:function () {
                        $(element).removeClass('jqui-dnd-item-dragging');
                        $(element).removeClass('jqui-dnd-item-over');
                        $(element).removeData('jqui-dnd-item-token');
                        scope.$apply();
                    }
                });
                $(element).html(attrs.compname);
            },
            replace:true

        };
    }).directive('droppable',function ($compile) {
        return {
            restrict:'A',
            transclude:true,

            link:function (scope, element, attrs) {

                var compFactory = function (comptag, type) {
                    if (type == 'json') {
                        if (comptag == 'block') {
                            return {"component":"block",
                                "metadata":{"attributes":{"title":"Sample Block" }, "transclude":".blockContent" } }; //<block title="Sample Block" childcontainer =".blockContent"></block>';
                        } else if (comptag == "textbox") {
                            return {"component":"formitem",
                                "metadata":{"attributes":{"label":"Sample Text Field" }, "transclude":".forrmitemwrap", "children":[
                                    {
                                        "component":"textbox", "metadata":{"attributes":{}}
                                    }
                                ]  } };
                        } else if (comptag == "lookup") {
                            return {"component":"formitem",
                                "metadata":{"attributes":{"label":"Sample Lookup Field" }, "transclude":".forrmitemwrap", "children":[
                                    {
                                        "component":"lookup", "metadata":{"attributes":{}}
                                    }
                                ]  } };
                        } else if (comptag == "checkbox") {
                            return {"component":"formitem",
                                "metadata":{"attributes":{"label":"Sample Check Field" }, "transclude":".forrmitemwrap", "children":[
                                    {
                                        "component":"checkbox", "metadata":{"attributes":{}}
                                    }
                                ]  } };
                        } else if (comptag == "radio") {
                            return {"component":"formitem",
                                "metadata":{"attributes":{"label":"Sample Radio Field" }, "transclude":".forrmitemwrap", "children":[
                                    {
                                        "component":"radio", "metadata":{"attributes":{}}
                                    }
                                ]  } };
                        } else if (comptag == "button") {
                            return {"component":"formitem",
                                "metadata":{ "attributes":{"label":"" }, "transclude":".forrmitemwrap", "children":[
                                    {
                                        "component":"button", "metadata":{"text":"My Button", "attributes":{"value":"My Button"}}
                                    }
                                ]  } };
                        } else if (comptag == "servicemodule") {
                            return {"component":"servicemodule",
                                "metadata":{"attributes":{"title":"Sample Service Module", "description":"Sample Description", "imageicon":"img/img1.png", "populartasks":"pagedata.pagebuilder.populartasks"  },
                                    "children":[]  } };
                        } else if (comptag == "servicemodulecontainer") {
                            return {"component":"servicemodulecontainer",
                                "metadata":{"attributes":{ }, "transclude":".servicemodulecontainer" } };
                        } else if (comptag == "sectionheader") {
                            return {"component":"sectionheader",
                                "metadata":{"attributes":{"title":"Sample Section Header"},
                                    "children":[]  } };
                        } else if (comptag == "h1") {
                            return {"component":"h1",
                                "metadata":{"text":"Sample H1 header", "attributes":{},
                                    "children":[]  } };
                        } else if (comptag == "h2") {
                            return {"component":"h2",
                                "metadata":{"text":"Sample H2 header", "attributes":{},
                                    "children":[]  } };
                        } else if (comptag == "h3") {
                            return {"component":"h3",
                                "metadata":{"text":"Sample H3 header", "attributes":{},
                                    "children":[]  } };
                        } else if (comptag == "servicelauncher") {
                            return {"component":"servicelauncher",
                                "metadata":{"attributes":{href:"", "link":"Sample Service Launcher", "description":"Sample Service launch Description", "imageicon":"img/img1.png" },
                                    "children":[]  } };
                        }

                    } else {
                        var id = $.guid;
                        if (comptag == 'block') {
                            return '<block title="Sample Block" childcontainer =".blockContent" id=' + id + '></block>';
                        } else if (comptag == "textbox") {
                            return '<formitem label="Sample Text Field" id=' + id + '><textbox></textbox></formitem>';
                        } else if (comptag == "lookup") {
                            return '<formitem label="Sample Lookup Field" ><lookup></lookup></formitem>';
                        } else if (comptag == "checkbox") {
                            return '<formitem label="Sample Check box Field" ><checkbox></checkbox></formitem>';
                        } else if (comptag == "radio") {
                            return '<formitem label="Sample Radio Field" ><radio></radio></formitem>';
                        } else if (comptag == "button") {
                            return '<formitem label="" ><button>My Button</button></formitem>';
                        } else if (comptag == "servicemodule") {
                            return '<servicemodule title="Sample Service Module" description="Sample Description" imageicon="img/img1.png" populartasks="pagedata.pagebuilder.populartasks" ></servicemodule>';
                        } else if (comptag == "servicemodulecontainer") {
                            return '<servicemodulecontainer childcontainer =".servicemodulecontainer"></servicemodulecontainer>';
                        } else if (comptag == "sectionheader") {
                            return '<sectionheader title="Sample Section Header" ></sectionheader>';
                        } else if (comptag == "h1") {
                            return '<h1>Sample H1 header</h1>';
                        } else if (comptag == "h2") {
                            return '<h2 id=' + id + '>Sample H2 header</h2>';
                        } else if (comptag == "h3") {
                            return '<h3>Sample H3 header</h3>';
                        } else if (comptag == "servicelauncher") {
                            return '<servicelauncher link="Sample Service Launcher" href="#" description="Sample Service launch Description" imageicon="img/img1.png" ></servicelauncher>';
                        }


                    }

                }

                var droppable = $(element).droppable({greedy:true,
                    activate:function (event, ui) {
                        $(element).addClass('jqui-dnd-target-active');
                        scope.$apply();
                    },
                    deactivate:function () {
                        $(element).removeClass('jqui-dnd-target-active');
                        $(element).removeClass('jqui-dnd-target-disable');
                        $(element).removeClass('jqui-dnd-target-over');
                    },
                    over:function (event, ui) {
                        //alert ("Over");
                        if ($(element).hasClass('jqui-dnd-target-active')) {
                            $(element).addClass('jqui-dnd-target-over');
                            ui.draggable.addClass('jqui-dnd-item-over');
                        }
                        var tree = $(element).parent().find("#comptree")
                        console.log("Tree in root : " + tree);
                        if (tree != undefined) {
                            $(tree).jstree("select_node", "#rootnode");
                            $(tree).jstree("open_node", "#rootnode", function () {
                            }, false);
                            console.log($(tree).jstree("get_json", "#rootnode"));
                        }
                    },
                    out:function (event, ui) {
                        $(element).removeClass('jqui-dnd-target-over');
                        ui.draggable.removeClass('jqui-dnd-item-over');
                    },
                    drop:function (event, ui) {
                        dropcomplete(this, ui);
                    }
                });

                var dropcomplete = function (node, ui) {
                    var comptag = ui.draggable.attr('comptag')
                    var block = compFactory(comptag, "markup");//  '<' + comptag + '></' + comptag + '>';

                    scope.$apply();
                    var newCmp = $compile($(block))(scope);
                    newCmp.id = $.guid;
                    var jsondata = compFactory(comptag, "json");
                    jsondata.id = newCmp.id;
                    scope.pagedata.pagebuilder.pagemetadata.push(jsondata);

                    if (ui.draggable.attr('childcontainer') != undefined && $(newCmp) != $(element)) {
                        $(newCmp).droppable({greedy:true,
                            activate:function (event, ui) {
                                $(newCmp).addClass('jqui-dnd-target-active');
                                scope.$apply();
                            },
                            deactivate:function () {
                                $(newCmp).removeClass('jqui-dnd-target-active');
                                $(newCmp).removeClass('jqui-dnd-target-disable');
                                $(newCmp).removeClass('jqui-dnd-target-over');
                            },
                            over:function (event, ui) {
                                console.log("Comp Id : " + newCmp.id);
                                if ($(newCmp).hasClass('jqui-dnd-target-active')) {
                                    $(newCmp).addClass('jqui-dnd-target-over');
                                    ui.draggable.addClass('jqui-dnd-item-over');
                                }

                                var tree = $(element).parent().find("#comptree")
                                console.log(tree);
                                if (tree != undefined) {
                                    var treecompid = "#tree" + newCmp.id;
                                    console.log("Tree compid : " + treecompid);
                                    $(tree).jstree("select_node", treecompid, true);
                                    //$(tree).jstree("open_node", treecompid, true);
                                    $(tree).jstree("open_node", treecompid, function () {
                                    }, false);
                                }
                            },
                            drop:function (event, ui) {
                                dropcomplete(this, ui);
                            }
                        });
                    }
                    if ($(node).attr('childcontainer') != undefined) {
                        $(node).find($(node).attr('childcontainer')).append(newCmp);
                    } else {
                        $(node).append(newCmp);
                    }

                    scope.$apply();
                    scope.$broadcast("addcomponent", {"comptag":ui.draggable.attr('comptag'), "compref":newCmp});

                };
                if (scope.pagedata.pagebuilder != undefined && scope.pagedata.pagebuilder.pagemetadata != undefined) {
                    scope.metadata = scope.pagedata.pagebuilder.pagemetadata;
                    var comp = '<div  metadata="metadata" dynamicpage style="bottom: 30px;left: 0;overflow: auto;position: absolute;right: 0;top: 108px;"></div>';
                    var compiledComp = $compile($(comp))(scope);
                    $.each($(compiledComp).children(), function (key, val) {
                        $(element).append($(val));
                    });
                }

            },
            replace:true

        };
    }).directive('tree',function () {
        return {
            restrict:'A',
            transclude:true,

            link:function (scope, element, attrs) {
                //scope.treedata = [];

                scope.treedata = [
                    {
                        "data":{"title":"Page", "attr":{"id":"rootnode"}},
                        "metadata":{ id:23 },
                        "children":[  ]
                    }
                ];

                var tree = $(element).jstree({
                    "json_data":{
                        "data":scope.treedata
                    },
                    "ui":{
                        "select_multiple_modifier":"on"
                    },
                    "plugins":[ "themes", "json_data", "ui", "crrm", "contextmenu" ],
                    "deleteItem":{
                        "label":"Delete component",
                        "action":function (obj) {
                            alert(obj);
                        }
                    },
                    "contextmenu":{
                        items:{ // Could be a function that should return an object like this one
                            "create":false,
                            "rename":{
                                "_class":"myClass",
                                "separator_before":false,
                                "separator_after":false,
                                "label":"Rename Node",
                                "action":function (obj) {
                                    //Do some action here or pass the object to another function
                                    //ex: myFunc(obj);
                                    $(obj).find("a:first").text("My new node label.");
                                }
                            },
                            "remove":{
                                "_class":"myClass",
                                "separator_before":false,
                                "separator_after":false,
                                "label":"Delete Component",
                                "action":function (obj) {
                                    //Do some action here or pass the object to another function
                                    //ex: myFunc(obj);
                                    //$(obj).find("a:first").text("My new node label.");
                                    //alert (obj);
                                    var compref = jQuery.data(obj[0], "compref");
                                    $(element).jstree("remove", obj);
                                    if (compref != undefined) {
                                        $(compref).detach();
                                    }
                                }
                            },
                            "ccp":false
                        }
                    }
                });

                tree.bind("select_node.jstree", function (e, data) {
                    var myTreeClicked = tree.find(".jstree-clicked");
                    for (var i = 0; i < myTreeClicked.length; i++) {
                        if (myTreeClicked[i] != data.rslt.obj.find(".jstree-clicked")[0]) {
                            $.jstree._reference(myTreeClicked[i]).deselect_node(myTreeClicked[i]);
                        }
                    }
                    var compref = jQuery.data(data.rslt.obj[0], "compref");
                    if (compref != undefined) {

                        //alert(jQuery.data(data.rslt.obj[0], "compref"));

                        $(compref).addClass("jqui-dnd-target-active");
                    }
                    var comptag = jQuery.data(data.rslt.obj[0], "comptag");
                    if (comptag != undefined) {
                        //console.log (comptag);
                        //console.log (scope.comppropertymap[comptag]);
                        //scope.propertymap = scope.comppropertymap[comptag];
                        //scope.$apply();
                        //$.find()
                        var filtered = scope.pagedata.pagebuilder.pagemetadata.filter(function (elem, index, array) {
                            return (elem.id == compref.id);
                        });
                        console.log("Filtered : " + filtered[0].metadata.attributes);
                        scope.propertymap = filtered[0].metadata.attributes;
                        scope.$apply();
                    }


                });
                tree.bind("deselect_node.jstree", function (e, data) {
                    var compref = jQuery.data(data.rslt.obj[0], "compref");
                    if (compref != undefined) {
                        $(compref).removeClass("jqui-dnd-target-active");
                    }

                });

                tree.bind("create.jstree", function (e, data) {
                    alert(data);
                });

                tree.bind("create.jstree", function (e, data) {
                    alert("Created " + data);
                });
                scope.$on("addcomponent", function (event, args) {
                    scope.treedata.push({
                        "data":"A node",
                        "metadata":{ id:23 },
                        "children":[ "Child 1", "A Child 2" ]
                    });
                    var treecompid = "tree" + args.compref.id;
                    var creatednode = $(element).jstree("create_node", null, "last", { "data":{"title":args.comptag, "attr":{"id":treecompid}}, "metadata":{ "compref":args.compref, "comptag":args.comptag}  },
                        function () {
                        }, true);
                    console.log($.guid);
                    if (scope.comptreemapping == undefined) {
                        scope.comptreemapping = {};
                        //scope.comptreemapping.
                    }
                });

                setTimeout(function () {
                    $(element).jstree("select_node", "#rootnode");
                }, 200);
            },
            replace:true
        };
    }).directive('pillbox', function () {
        return {
            restrict:'E',
            transclude:true,
            scope:{sunday:'@sunday', monday:'@monday', tuesday:'@tuesday', wednesday:'@wednesday', thursday:'@thursday', friday:'@friday', saturday:'@saturday'},
            template:'<div style="float:left;padding-left: 10px;padding-bottom: 2px;">' +
                '<div class="pillbox-{{saturday}}">S</div>' +
                '<div class="pillbox-{{friday}}">F</div>' +
                '<div class="pillbox-{{thursday}}">T</div>' +
                '<div class="pillbox-{{wednesday}}">W</div>' +
                '<div class="pillbox-{{tuesday}}">T</div>' +
                '<div class="pillbox-{{monday}}">M</div>' +
                '<div class="pillbox-{{sunday}}">S</div>' +
                '</div>',
            replace:true,
            link:function (scope, element, attrs) {

            }
        };
    })
