Backbone.emulateHTTP = true;

Backbone.sync = function(method, model, options) {
    // Map from CRUD to HTTP for our default `Backbone.sync` implementation.
    var methodMap = {
        'create': 'POST',
        'update': 'PUT',
        'delete': 'DELETE',
        'read'  : 'GET'
    };

    // Throw an error when a URL is needed, and none is supplied.
    var urlError = function() {
        throw new Error('A "url" property or function must be specified');
    };

    var getUrl = function(object) {
        if (!(object && object.url)) return null;
        return _.isFunction(object.url) ? object.url() : object.url;
    };

    var type = methodMap[method];

    // Default JSON-request options.
    var params = _.extend({
        type:         type,
        dataType:     'json',
        processData:  false
    }, options);

    // Ensure that we have a URL.
    if (!params.url) {
        params.url = getUrl(model) || urlError();
    }

    // This code is an 'enhancement' to Backbone where we will append query parameters on reads if a fetchCriteria
    // object and/or the pagingCriteria object exists.
    if (method === "read" && model) {
        var queryParams = [];

        if (model.fetchCriteria) {
            var p = _.map(model.fetchCriteria( options ), function(value, key) { return key + "=" + value; });
            _.each(p, function(it) { queryParams.push(it); });
        }

        if (model.paginate && model.pagingCriteria) {
            var p = _.map(model.pagingCriteria( options ), function(value, key) { return key + "=" + value; });
            _.each(p, function(it) { queryParams.push(it); });
        }

        params.url = params.url + (params.url.indexOf("?") > 0 ? "&" : "?") + queryParams.join("&");
    }

    // Ensure that we have the appropriate request data.
    if (!params.data && model && (method == 'create' || method == 'update')) {
        params.contentType = 'application/json';
        params.data = JSON.stringify(model.toJSON());
    }

    // For older servers, emulate JSON by encoding the request into an HTML-form.
    if (Backbone.emulateJSON) {
        params.contentType = 'application/x-www-form-urlencoded';
        params.processData = true;
        params.data = params.data ? {model : params.data} : {};
    }

    // For older servers, emulate HTTP by mimicking the HTTP method with `_method`
    // And an `X-HTTP-Method-Override` header.
    if (Backbone.emulateHTTP) {
        if (type === 'PUT' || type === 'DELETE') {
            if (Backbone.emulateJSON) params.data._method = type;
            params.type = 'POST';
            params.beforeSend = function(xhr) {
                xhr.setRequestHeader('X-HTTP-Method-Override', type);
            };
        }
    }

    // Clear out all messages prior to syncing.
    if (model) {
        model.each(function(m) {
            if (m.has( "messages" )) {
                m.unset( "messages" );
            }
        });
    }

    // Make the request.

    if (model && model.ajaxCallback) {
        // Use the ajaxCallback
        return model.ajaxCallback( params );
    }
    else {
        return $.ajax(params);
    }
};

_.extend(Backbone.Model.prototype, {
    isDirty: function() {
        return this._dirty;
    },
    resetDirty: function() {
        this._dirty = false;
    },
    makeDirty: function() {
        this._dirty = true;
    },
    _dirty: false
});

// TODO This was done to support Backbone.js 1.0's change to non-reset fetch().
// We need this behavior, currently, to support our Model.isDirty() functionality.

Backbone.Collection.prototype.fetch = _.wrap( Backbone.Collection.prototype.fetch, function( original, options ) {
    options = options || { };
    options.reset = options.reset || true;

    original.call( this, options );
});

// Extend Backbone.Collection to include 'save' which will inspect all dirty models and attempt to save them.
_.extend(Backbone.Collection.prototype, {
    cid: undefined,
    getCid: function () {
        if ( _.isUndefined( this.cid ) )
            this.cid = _.uniqueId( 'c' );
        return this.cid;
    },
    save: function(options) {
        options || (options = {});

        // Prior to talking to the service we are going to clear all messages and let the response tell us what
        // errors still exist.
        this.each( function( model ) {
            if (model.has( "messages" )) {
                model.unset( "messages", {silent: true} );
            }
        });

        if (this.batch) {
            var collection = this;

            var success = function( batch, textStatus, jqXHR ) {
                // Non-fatal server errors return a HTTP 200 with { success: false, ... }, not a HTTP 500
                if ( _.isBoolean( batch.success ) && !batch.success ) {
                    if ( _.isFunction( options.error ) ) options.error.call( this, collection, batch, jqXHR );
                    return;
                }

                // The model that comes down is the batch that was sent up.
                // Loop through the model and update the collection.
                _.each((batch.data.create || []).concat(batch.data.update || []), function(updatedModel) {
                    var model = collection.get(updatedModel.id);
                    model.set(model.parse(updatedModel), options);

                    if (model.get( "messages")[0].type == "success" ) {
                        //model.set( { messages: [{ message:"save successful", type:"success"}] } );
                        model.resetDirty();
                    }
                });

                _.each(batch.data.destroy, function(modelToDelete) {
                    var model = _.select(this.deletedModels, function(deletedModel) {
                        return deletedModel.id == modelToDelete.id;
                    });

                    if (model) {
                        collection.remove(model, options);
                    }
                });

                if (options.success) options.success( collection, batch, jqXHR );
            };

            // This is the HTTP 500 response error handler. we pass through the success callback to get single access to the
            // error callback which is also invoked for HTTP 200 { success: false } responses.
            var error = function ( jqXHR, textStatus, errorThrown ) {
                var response = JSON.parse( jqXHR.responseText );

                success.call( this, response, textStatus, jqXHR );
            };

            var batchModel = createBatchModel(this);

            var getBatchUrl = function(object) {
                if (!(object && object.url)) return null;
                var url = _.isFunction(object.url) ? object.url() : object.url;

                return url + "/batch";
            };


            // Setup the options to send to sync.  Note that the url is using the var getUrl from Backbone.
            saveCollectionOptions = {
                success: success,
                error: error,
                data: JSON.stringify(batchModel),
                url: getBatchUrl(this),
                contentType: 'application/json'
            };

            (this.sync || Backbone.sync)("create", null, saveCollectionOptions);
        }
        else {
            $.each(this.models, function(index, model) {
                if (model.isDirty()) {
                    model.save(null, {
                        success: function(model, response) {
                            model.resetDirty();

                            if (options.success) options.success(model);
                        },
                        error: function(model, response) {
                            var responseJson = $.parseJSON(response.responseText);
                            model.set({errors: responseJson.errors});

                            if (options.error) options.error(model);
                        }
                    });
                }
            });

            // TODO:  Do we need specific destory based success and error handlers?
            // TODO:  Determine what model is returned for a destroyed model.  All we send up is the id.
            if (this.containsMarkedAsDeleted()) {
                $.each(this.deletedModels, function(index, model) {
                    model.destroy({
                        success: function(model, response) {

                            // Remove from deletedModels
                            this.deletedModels = _.reject(this.deletedModels, function(markedAsDeletedModel) {
                                return markedAsDeletedModel.id === model.id;
                            });

                            if (options.success) options.success(model);
                        },
                        error: function(model, response) {
                            var responseJson = $.parseJSON(response.responseText);
                            model.set({errors: responseJson.errors});

                            if (options.error) options.error(model);
                        }
                    });
                });
            }
        }

        return this;
    },
    markAsDeleted: function(model) {
        this.remove(model, {silent:true});

        if (this.deletedModels === null) {
            deletedModels = [];
        }

        this.deletedModels.push(model);
    },
    deletedModels: null,
    ajaxManager: null,
    containsMarkedAsDeleted: function() {
        return (this.deletedModels && this.deletedModels.length);
    },
    batch : false,
    isDirty: function() {
        return _.any(this.models, function(model) {
            return model.isDirty()
        }) || this.deletedModels;
    },
    paginate:      false,
    page:          1,
    pageMaxSize:   50,
    pageOffset:    0,
    sortColumn:    null,
    sortColumnIdx: null,
    sortDirection: "asc",
    totalCount:    0,
    pagingCriteria: function() {
        var self = this,
            offset = self.pageMaxSize * (self.page - 1);

        if ( !self.paginate ) {
            return { };
        }

        if ( offset > self.totalCount ) {
            self.page = Math.ceil( self.totalCount / self.pageMaxSize );
        }

        return {
            pageOffset:    self.pageMaxSize * (self.page - 1),
            pageMaxSize:   self.pageMaxSize,
            sortColumn:    self.sortColumn,
            sortDirection: self.sortDirection
        };
    },
    pageInfo: function() {
        var self = this;
        var ceil = Math.ceil(self.totalCount / self.pageMaxSize);
        var pageRanges = [];

        for (var x = 1; x <= ceil; x++) {
            pageRanges.push(self.pageMaxSize * x);
        }

        var info = {
            totalCount:  self.totalCount,
            pageMaxSize: self.pageMaxSize,
            pages:       ceil,
            page:        self.page,
            prev:        self.page > 1    ? self.page - 1 : false,
            next:        self.page < ceil ? self.page + 1 : false,
            pageRanges: pageRanges
        };

        var max = Math.min(self.totalCount, self.page * self.pageMaxSize);

        if (self.totalCount == self.pages * self.pageMaxSize) {
            max = self.totalCount;
        }

        info.range = [(self.page - 1) * self.pageMaxSize + 1, max];
        return info;
    },
    firstPage: function() {
        if (this.page != 1) {
            this.page = 1;

            return this.fetch();
        }
        return false;
    },
    lastPage: function() {
        var info = this.pageInfo();

        if (this.page != info.pages) {
            this.page = info.pages;

            return this.fetch();
        }
        return false;
    },
    nextPage: function() {
        if (!this.pageInfo().next) {
            return false;
        }
        this.page = this.page + 1;
        return this.fetch();
    },
    previousPage: function() {
        if (!this.pageInfo().prev) {
            return false;
        }
        this.page = this.page - 1;
        return this.fetch();
    },
    goToPage: function(page) {
        if ( typeof(page) != 'number' || isNaN( page )) {
            return false;
        }

        var info = this.pageInfo();

        if (info.page == page) {
            return false;
        }

        if (page > info.pages || page < 1) {
            return false;
        }

        this.page = page;
        return this.fetch();
    },
    setPageSize: function(size) {
        if (typeof(size) == 'number') {
            this.pageMaxSize = size;
            return this.fetch();
        }
        return false;
    }
});

function createBatchModel(model) {
    if (model.isDirty()) {
        var changedModels = _.select(model.models, function(model) {
            return model.isDirty();
        });

        var createModels = _.select(changedModels, function(model) {
            return model.isNew();
        });

        var updateModels = _.select(changedModels, function(model) {
            return !model.isNew();
        });

        var deletedIdsList = [];
        if (model.deletedModels) {
            deletedIdsList = _.pluck(model.deletedModels, id);
        }

        return { create: createModels || [], update: updateModels || [], destroy: deletedIdsList };
    }
    else {
        return { create: [], update: [], destroy: [] };
    }
}

function createFetchOptions( collection, options ) {

    var getUrl = function(object) {
        if (!(object && object.url)) return null;
        return _.isFunction(object.url) ? object.url() : object.url;
    };

    var returnUrl = getUrl( collection );

    if (options.params) {
        // Loop through the params and add query parameters to the url
        _.each(options.params,
            function(value, key) {
                if (returnUrl.indexOf("?") > 0) {
                    returnUrl = returnUrl + "&";
                }
                else {
                    returnUrl = returnUrl + "?";
                }

                returnUrl = returnUrl + key + "=" + value;
            }
        );

        options.url = returnUrl;
    }

    return options;
}

// Add validation logic when models change.  We do not use the 'validate' function on the model due to some lack of control when it is called.
// For example, validate is called on the model when the model is fetched/added to the collection.  This means we will validate as we prepare the model
// which has the unforeseen consquence that the model we were validating does not exist in the collection yet.  We want to avoid that.
function registerModelValidation( collection, validationFunction ) {
    collection.bind( "change", function( model ) {
        var valid = validationFunction( model, model.changedAttributes());

        if (_.isUndefined( valid )) {
            valid = true;
        }
        else {
            // Something was returned which means that the model did not validate.
            valid = false;
        }

        if (!valid) {
            log.debug( "model did not validate on change", model, model.changedAttributes());
        }
    });

    collection.bind( "reset", function( models ) {
        models.each( function( m ) {

            var valid = validationFunction( m, m.attributes);

            if (_.isUndefined( valid )) {
                valid = true;
            }
            else {
                // Something was returned which means that the model did not validate.
                valid = false;
            }

            if (!valid) {
                log.debug( "model did not validate on reset", m, m.attributes);
            }
        });
    });
}
