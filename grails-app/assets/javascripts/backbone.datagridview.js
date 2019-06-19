
(function ($, _) {
    /**
     * @el jQuery object the element to associate with this view
     * @collection Backbone.Collection the collection to associate with this view
     * @editableColumns array list of columns to make editable
     * @settings object any custom grid settings
     */
    Backbone.DataTablesViewInternal = Backbone.View.extend({
        styles: {
            hover:              "row_hover",
            selected:           "row_selected",
            sortIconNorthSouth: "ui-icon-carat-2-n-s",
            sortIconNorth:      "ui-icon-triangle-1-n",
            sortIconSouth:      "ui-icon-triangle-1-s"
        },
        table: undefined,
        defaultPageLengths: [50, 100, 250, 500],
        initialize: function () {
            _.bindAll(this, 'render', 'reload', 'success', 'error', 'generateDataIdentifier', 'determinePageLengths', 'getSelectedRows', 'getColumnSelector', 'notificationAdded', 'notificationRemoved', 'createRowCallback', 'updateData', 'preparePagingSelect', 'createDataTablesJSON', 'replaceDataTablesSortListeners', 'sortTable');

            if (typeof(this.collection) != 'undefined') {
                this.collection.bind( "reset", this.render );
                this.collection.bind( "change", function(model) {
                    model.makeDirty();
                });
            }

            if (typeof(notifications) != 'undefined' && typeof(notifications.bind) == 'function') {
                notifications.bind('add',    this.notificationAdded );
                notifications.bind('remove', this.notificationRemoved );
            }

            if ( !_.isUndefined( this.options.defaultPageLengths ) && _.isArray( this.options.defaultPageLengths ) )
                this.defaultPageLengths = this.options.defaultPageLengths;
        },
        render: function() {
            var view = this;

            if (_.isFunction(this.options.beforeRender))
                this.options.beforeRender.call(this);

            if (!_.isNull(this.table) && !_.isUndefined(this.table)) {
                this.table.fnDestroy();
                this.$el.data('placeholder',''); // prevents an undefined error on IE.
            }

            if (_.isNull(this.collection.sortColumn)) {
                var column = _.first(this.options.aoColumnDefs);

                this.collection.sortColumn = column.mDataProp
            }

            if (_.isNull(this.collection.sortColumnIdx)) {
                for (var x = 0; x < this.options.aoColumnDefs.length; x++) {
                    if (this.options.aoColumnDefs[x].mDataProp == this.collection.sortColumn) {
                        this.collection.sortColumnIdx = x;
                        break;
                    }
                }
            }

            var blacklist = ['el', 'collection', 'editableColumns'];

            var settings = _.pick(this.options, _.without(_.keys(this.options), blacklist));

            settings = _.defaults(settings, {
                target:               $(this.el),
                bLengthChange:        this.collection.paginate,
                bPaginate:            this.collection.paginate,
                aaSorting:            [ [this.collection.sortColumnIdx, this.collection.sortDirection] ],
                aoBackboneCollection: this.collection,
                oBackboneView:        this,
                editableColumns:      this.options.editableColumns || [],
                iDisplayLength:       this.collection.pageInfo().pageMaxSize
            });

            this.table = this.createDataTable( settings );

            // set sort icon
            var columnDef = _.find( this.options.aoColumnDefs, function ( it ) { return it.mDataProp == view.collection.sortColumn; });
            this.$el.find( "th .DataTables_sort_icon" ).removeClass( this.styles.sortIconNorth + " " + this.styles.sortIconSouth ).addClass( this.styles.sortIconNorthSouth );
            this.$el.find( "th." + columnDef.sClass + " .DataTables_sort_icon" ).removeClass( view.styles.sortIconNorthSouth ).addClass( view.collection.sortDirection == "asc" ? view.styles.sortIconSouth : view.styles.sortIconNorth );

            if ( _.isFunction(this.options.afterRender ) )
                this.options.afterRender.call( this );
        },
        success: function(model, resp) {
            if (typeof(this.options.success) == "function")
                this.options.success.call(this, model, resp);
        },
        error: function(model, resp) {
            if (typeof(this.options.error) == "function")
                this.options.error.call(this, model, resp);
        },
        save: function() {
            if (typeof(this.collection) != 'undefined' && this.collection.isDirty()) {
                this.collection.save({ success: this.success, error: this.error });
            }
        },
        reload: function() {
            this.table.fnClearTable(0);
            this.table.fnAddData( this.createDataTablesJSON() );
        },
        generateDataIdentifier: function() {
            return $(this.el).attr("id") || "dataTable";
        },
        determinePageLengths: function ( settings ) {
            var size = this.collection.pageInfo().pageMaxSize;

            if (!_.find(this.defaultPageLengths, function (it) { return it == size })) {
                var arr = this.defaultPageLengths.concat(size);
                this.defaultPageLengths = _.sortBy(arr, function (it) { return it; });
            }

            return this.defaultPageLengths;
        },
        updateData: function (property, value, settings, el) {

            var data = this.table.fnGetData(el.parentNode);

            if (value === "")
                value = null;

            var model = this.collection.get(data.id);

            if (model) {
                var map = { };
                map[property] = value;

                if (model.get(property) != value)
                    model.set(map);
            }

            return value;
        },
        createRowCallback: function ( settings ) {
            var view = this;
            var editableColumns = settings.editableColumns || [];
            var fnRowCallback   = settings.fnRowCallback;

            var getEditableTypeDef = function ( column, row ) {
                return _.extend( {}, column, {
                    onblur: function(val, settings) {
                        $('form', this).submit();
                    },
                    onedit: function() {
                        $(row).addClass( view.styles.selected ).siblings().removeClass( view.styles.selected );
                    },
                    placeholder: ""
                });
            };

            return function (row, data, displayIndex, displayIndexFull) {
                var tableInstance = this;

                editableColumns = editableColumns || [ ];

                _.each(editableColumns, function (column) {
                    var elements = $('td:nth-child(' + tableInstance.getColumnDivIndexByProperty( column.name ) + ')', row);

                    _.each(elements, function (el) {
                        var aPos  = tableInstance.fnGetPosition( el );
                        var aData = tableInstance.fnGetData( aPos[0] );

                        $(el).editable(
                            function (value, settings) {
                                _.defer(function() {
    //                                getKeyTable().block = false;
                                });
                                return encodeHTML(view.updateData(column.name, value, settings, this));
                            },
                            getEditableTypeDef( column, row )
                        );
                    });
                });

                if ( _.isFunction( fnRowCallback ) ) {
                    fnRowCallback.call( view, row, data, displayIndex, displayIndexFull );
                }

                return row;
            };
        },
        createDataTablesJSON: function () {
            var json = this.collection.toJSON(),
                prefix = this.generateDataIdentifier();

            _.each( json, function( o ) {
                o[ "DT_RowId" ]    = prefix + "-" + o.id;
                o[ "DT_RowClass" ] = prefix + "-row";
            });

            return json;
        },
        hiddenColumns: [ ],
        createDataTable: function ( settings ) {
            var view = this;

            // set up settings
            var defaults = {
                aaData:          this.createDataTablesJSON(),
                bJQueryUI:       true,
                bAutoWidth:      false,
                bInfo:           false,
                bLengthChange:   false,
                bPaginate:       false,
                sPaginationType: "selfServiceDefaultPagination",
                iDisplayLength:  50,
                aLengthMenu:     this.determinePageLengths(),
                sDom:            'Rrt<"bottom ui-widget-header"p<"bottom-divider">l<"dataTables_info"><"clear">',
                oColVis: {
                   iOverlayFade:  100,
                   buttonText:    "&nbsp;",
                   sAlign:        "right",
                   fnStateChange: function ( idx, visible ) {
                       var prop = view.$el.fnSettings().aoColumns[ idx ].mDataProp;

                       if ( visible )
                           view.hiddenColumns = _.without( view.hiddenColumns, prop );
                       else
                           view.hiddenColumns.push( prop );
                   }
                },
                oLanguage: {
                    sLengthMenu:   $.i18n.prop('js.dataTable.sLengthMenu'),
                    sZeroRecords:  $.i18n.prop('js.dataTable.sZeroRecords'),
                    sInfo:         $.i18n.prop('js.dataTable.sInfo'),
                    sInfoEmpty:    $.i18n.prop('js.dataTable.sInfoEmpty'),
                    sInfoFiltered: $.i18n.prop('js.dataTable.sInfoFiltered'),
                    sEmptyTable:   $.i18n.prop('js.dataTable.sEmptyTable')
                },
                fnDrawCallback: function ( oSettings ) {
                    // Position the ColVis button
//                    var nColVis = $( 'div.ColVis', oSettings.nTableWrapper )[0];
//                    nColVis.style.width  = oSettings.oScroll.iBarWidth+"px";
//                    nColVis.style.top    = ( $( 'div.dataTables_scroll', oSettings.nTableWrapper ).position().top ) + "px";
//                    nColVis.style.height = ( $( 'div.dataTables_scrollHead table', oSettings.nTableWrapper ).height() ) + "px";
                }
            };

            settings = $.extend( defaults, settings );

            settings.fnRowCallback = this.createRowCallback( settings );

            _.each( settings.aoColumnDefs, function ( it ) {
                if ( _.indexOf( view.hiddenColumns, it.mDataProp ) != -1 )
                    it.bVisible = false;
                else
                    it.bVisible = true;
            });

            // TODO:  Determine why this can't be done via a CSS class as opposed to manually setting the element.style.
            var table = this.$el.dataTable( settings ).width( "100%" );

            $( table.selector + ' tbody tr').live('click',     function(event) { $(this).addClass( view.styles.selected ).siblings().removeClass( view.styles.selected ); } );
            $( table.selector + ' tbody tr').live('mouseover', function(event) { $(this).addClass( view.styles.hover );    } );
            $( table.selector + ' tbody tr').live('mouseout',  function(event) { $(this).removeClass( view.styles.hover ); } );

            listenForRemovingFocusOnEditableCell();

            this.preparePagingSelect();
            this.replaceDataTablesSortListeners();

            return table;
        },
        preparePagingSelect: function () {
            var collection = this.collection,
                el = this.$el.closest(".dataTables_wrapper").find(".bottom .dataTables_length select"),
                dirtyCheckDefaultsForPagingSelect = {
                save: function( options ) {
                    var callback = options.callback;

                    // This probably can be improved by using a 'promise' object from jQuery.  Will investigate at a later date.
            //        saveGrades( {
            //            success: function() {
            //                if ( !isDirty() ) { callback(); }
            //            }
            //        });
                    collection.fetch()
                },
                no: function( options ) {
                    collection.fetch()
                },
                isDirty: function() {
                    return collection.isDirty();
                }
            };

            el.change(function(e) {
                var size = el.find("option:selected").val();
                collection.setPageSize(parseInt(size, 10));
            });

            // A select statement will changed its selected value even if the 'onChange' event returns false.  If
            // the record is dirty, and the user cancels the request to save we want to have the initial value of the
            // select available to reset it to what it was prior to them changing the paging length.
            el.focus(function(e) {
                $.data( this, 'initial', this.value );
            });


            el.dirtyCheck( _.defaults( {
                    eventType: "change",
                    cancelCallback: function() {
                        var target = $( selectorString );
                        if (target.data().initial) {
                            target.val( target.data().initial );
                        }
                    }
                }, dirtyCheckDefaultsForPagingSelect )
            );
        },
        replaceDataTablesSortListeners: function () {
            var view = this;

            _.each(this.$el.fnSettings().aoColumns, function (c) {
                $(c.nTh).unbind("click.DT click").bind("click", function () {
                    view.sortTable( this )
                });
            });
        },
        sortTable: function (el) {

            if (typeof(this.collection) == 'undefined') {
                log.debug("sortTable: collection not defined");
                return false;
            }

            var view       = this,
                self       = el,
                settings   = this.table.fnSettings(),
                column     = "",
                direction  = "asc",
                obj        = _.find( settings.aoColumns, function(it) { return it.nTh == self; } );

            if (obj) {
                column = obj.mDataProp;

                if (settings.aaSorting.length == 1)
                    direction = settings.aaSorting[0][2] == 0 ? "desc" : "asc";

            var innerSortTable = function () {
                view.collection.sortColumn    = column;
                view.collection.sortDirection = direction;
                view.collection.sortColumnIdx = $(self).index();
                view.collection.fetch();
            };

            if (this.collection.sortColumn    != column
             || this.collection.sortDirection != direction) {

                    if (this.collection.isDirty()) {
                        setTimeout( function() {
                            var n = new Notification( {message: $.i18n.prop("js.notification.dirtyCheck.message"), type:"warning", promptMessage: $.i18n.prop("js.notification.dirtyCheck.promptMessage")} );

                            n.addPromptAction( $.i18n.prop("js.notification.dirtyCheck.cancelActionButton"), function() {
                                notifications.remove( n );
                            });

                            n.addPromptAction( $.i18n.prop("js.notification.dirtyCheck.doNotSaveActionButton"), function() {
                                notifications.remove( n );
                                innerSortTable();
                            });

                            n.addPromptAction( $.i18n.prop("js.notification.dirtyCheck.saveActionButton"), function() {
        //                        saveGrades({
        //                            success: function() {
        //                                if ( !isDirty() ) {
        //                                    innerSortTable();
        //                                }
        //                            }
        //                        });

                                notifications.remove( n );
                            });

                            notifications.addNotification( n );
                        }, 200);
                    } else {
                        innerSortTable();
                    }
                 }
            }
        },
        getSelectedRows: function() {
            var view = this;
            return _.filter(this.table.fnGetNodes(), function(it) {
                return $(it).hasClass( view.styles.selected );
            });
        },
        getColumnSelector: function( propertyName ) {
            var idx = this.table.getColumnDivIndexByProperty( propertyName );

            if (!_.isUndefined(idx))
                return this.$el.selector + ' tbody tr td:nth-child(' + idx + ')';

            return false;
        },
        notificationAdded: function( notification ) {
            var model = this.collection.find( function( m ) {
                if (notification.get( "model" )) {
                    return m.get( "id" ) === notification.get( "model" ).id;
                }
            });

            if (model) {
                var id       = model.get("id"),
                    types    = { success:'notification-success', warning:'notification-warning' },
                    clz      = types[notification.get( "type" )] || "notification-error",
                    selector = "#" + this.generateDataIdentifier() + "-" + id;


                $(selector).stop(true,true).addClass(clz);
            }
        },
        notificationRemoved: function( notification ) {
            var model = this.collection.find( function( m ) {
                return m === notification.get( "model" );
            });

            if (model) {
                var id       = model.get("id"),
                    types    = { success:'notification-success', warning:'notification-warning' },
                    clz      = types[notification.get( "type" )] || "notification-error",
                    selector = "#" + this.generateDataIdentifier() + "-" + id;


                $(selector).removeClass(clz, 1000);
            }
        }
    });

    var DataGridView = Backbone.DataGridView = Backbone.DataTablesViewInternal.extend({ });

}).call(this, $, _);
