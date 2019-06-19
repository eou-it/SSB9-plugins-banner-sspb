/**
 * This is an aggregator of 'manageAjax' instances.
 *
 * We can use one overall manager to help manage multiple queues.
 *
 * Notice that the create function will only create a queue once and always return an instance of a managed queue.
 */
function AjaxManager() {
    this.queues = {};
    this.create = function( name, o ) {
        if (!this.queues[name]) {
            this.queues[name] = $.manageAjax.create( name, o );
        }

        return this.queues[name];
    };

    this.add = function( name, o ) {
        if (!queues[name]) {
            throw new Error( "'" + name + "' has not been created." );
        }

        return this.queues[name].add( o );
    };

    this.destroy = function( name ) {
        if (this.queues[name]) {
            delete this.queues[name];
        }
    };
};