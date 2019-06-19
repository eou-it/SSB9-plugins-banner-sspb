/* Copyright 2013 Ellucian Company L.P. and its affiliates. */

/* dependent upon Collection enhancements in backbone-custom.js */
;(function () {
    Backbone.PagedCollectionInternal = Backbone.Collection.extend({
        paginate: true,
        parse: function(response, xhr) {
            this.totalCount = response.totalCount

            return response.data;
        },
        fetch: function(options) {
            typeof(options) != 'undefined' || (options = {});

            this.trigger("fetching");

            if (typeof(options.page) == 'number') {
                this.page = options.page;
            }

            var self = this;
            var success = options.success;
            var error = options.error;
            self.loading = true;

            options.success = function(response) {
                self.loading = false;
                self.trigger("fetched");

                var info = self.pageInfo();

                if (info.pages && self.page > info.pages) {
                    self.fetch({ page: 1, success: success });
                }

                if ( _.isFunction( success ) )  { success( response ); }
            };

            options.error = function( originalModel, response, options ) {
                self.loading = false;
                self.trigger("failed");
                if ( _.isFunction( error ) ) { error( originalModel, response, options ); }
            };

            return Backbone.Collection.prototype.fetch.call(this, options);
        }
    });

    var PagedCollection = Backbone.PagedCollection = Backbone.PagedCollectionInternal.extend({});
}).call( this );
