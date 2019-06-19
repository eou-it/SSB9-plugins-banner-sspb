/*
 * $(...).combobox( options ) makes an input element into an autoselect combobox.
 * based on jQuery UI autocomplete combobox example.
 * set options.combobox.data to the list of dropdown values
 */
(function( $ ) {
    $.widget( "ui.combobox", {
        _create: function() {
            var self = this,
            input = this.input = this.element;
            input.addClass('combobox');
            input.autocomplete( this.options ).attr('style','') //autocomplete adds a style attribute
                .addClass( "ui-widget ui-widget-content ui-corner-left" );

            this.button = $( "<button type='button'>&nbsp;</button>" )
                .attr( "tabIndex", -1 )
                .insertAfter( input )
                .button({
		    icons: {
			primary: "ui-icon-triangle-1-s"
		    }
                    //text: true,
                    //label:'...'
                })
                .removeClass( "ui-corner-all" ).removeClass( "ui-icon-only" )
                .addClass( "combobox" )
                .addClass( "ui-corner-right" )
                .click(function() {
                    // close if already visible
                    if ( input.autocomplete( "widget" ).is( ":visible" ) ) {
                        input.autocomplete( "close" );
                        return;
                    }

                    // copied the following from original, but haven't seen the bug...
                    // needed to work around a bug (likely same cause as #5265)
                    //$( this ).blur();

                    // pass empty string as value to search for, displaying all results
                    input.autocomplete( "search", "" );
                    input.focus();
                });
        },

        destroy: function() {
            this.button.remove();
            $.Widget.prototype.destroy.call( this );
        }
    });


    ////////// create an editable 'combobox' type
    
    // enhance the focus selector to support elements that can't usually be focused
    // hesitant to modify ':focus', but copied from jquery.jeditable.datepicker.js
    // redundant with datepicker, but necessary here if combobox is used in isolation
    jQuery.expr[':'].focus = function( elem ) {
        return elem === document.activeElement && ( elem.type || elem.href );
    };
    
    var onclosedelay = 50,
        onblurdelay = onclosedelay + 100;
    
    /*
     * editable inputType 'combobox' provides an autocomplete with a button
     * to restrict to just the values in the source data, do $(...).editable( submitFunc, {
     *     validate: function(value, settings, original) {
     *          return _.include(settings.combobox.data, value) ? value : false;
     *      }, ... });
     */
    $.editable.addInputType('combobox', {
        element : $.editable.types.text.element,
        submit:function(settings, original){
            // from validated input type jquery.editable.input.types.js
            // Update the hidden value in the form if the visible value validates,
            // otherwise, reset the form to the original value.
            if (settings.validate && !settings.validate($('input',this).val(), settings, original)) {
                original.reset(this); //!! do we need an alert msg?
                return false;
            } else {
                return true;
            }
        },
        plugin: function(settings, original) {
            var form = this,
                input = form.find('input');
            form.data('isOpen', false); // !! where to store this value? copy autocomplete
            form.data('selected', false);
            var orgOnblur = settings.onblur;
            settings.onblur = function() {
                var args = arguments;
                setTimeout( function() {
                    if (!form.data('isOpen')) { // clicked elsewhere
                        log.debug( 'combobox onblur', this, args, orgOnblur);
                        orgOnblur && orgOnblur.apply(original, args); // only support functions for now
                    }
                    // otherwise, lost focus because dropdown opened, so do nothing
                    
                    // the delay is necessary to allow the dropdown to be closed
                    // and the submit to have occurred (in the close callback below).
                }, onblurdelay); 
            }
            var combobox = jQuery.extend( {}, settings.combobox, {
                source:settings.combobox.source || settings.combobox.data,
                minLength: 0,
                delay:0,
                open:function(evt,ui){
                    form.data('isOpen', true);
                    form.data('selected', false);
                    var orgOpen = settings.combobox && settings.combobox.open;
                    orgOpen && orgOpen.apply( this, [evt,ui] );
                },
                select:function(evt,ui){
                    form.data('selected', true);
                },
                close:function(evt,ui){
                    var self = this;
                    var orgClose = settings.combobox && settings.combobox.close;
                    form.data('isOpen', false);
                    if ( form.data('selected') ) {
                        form.data('selected', false);
                        orgClose && orgClose.apply( self, [evt, ui]);
                        evt.stopPropagation(); // prevent an autocomplete click from triggering _fnBlur in the KeyTable
                    } else {
                        setTimeout( function() { // from jquery.jeditable.datepicker.js
                            if ( form.data('isOpen') && // if we submit again when form is closed, it reloads the page.  cool.
                                 !input.is( ':focus' ) ) {
                                // input has NO focus after 150ms which means
                                // dropdown was closed due to click outside of it
                                // so close the input field
                                log.debug( 'close method, click outside, orgClose') ;
                                orgClose && orgClose.apply( self, [evt,ui]);
                            } else {
                                // input still HAS focus after 150ms which means
                                // dropdown was closed due to ENTER in the input field
                                // or because of invalid/unrecognized input.
                                // we'd like to submit for ENTER, but need to keep it open for
                                // invalid. We could call a validate here, or ignore the event
                                // and test elsewhere.
                                log.debug( 'close method, input still has focus, think you selected, would like to submit, but think it may be because of invalid input' );
                                //form.submit();
                            }
                            
                            // the delay is necessary; dropdown must be already
                            // closed for the above :focus checking to work properly;
                            // without a delay the form is submitted in all scenarios, which is wrong
                        }, onclosedelay );
                    }
                }
            });
            input.combobox(combobox);
        }
    });
})( jQuery );
