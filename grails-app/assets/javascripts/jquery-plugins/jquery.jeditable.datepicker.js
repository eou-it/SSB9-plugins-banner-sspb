/*
 * Datepicker for Jeditable
 *
 * Copyright (c) 2011 Piotr 'Qertoip' Włodarek
 *
 * Licensed under the MIT license:
 *   http://www.opensource.org/licenses/mit-license.php
 *
 * Depends on jQuery UI Datepicker
 *
 * Project home:
 *   http://github.com/qertoip/jeditable-datepicker
 *
 */

// add :focus selector
jQuery.expr[':'].focus = function( elem ) {
  return elem === document.activeElement && ( elem.type || elem.href );
};

$.datepicker._doKeyDown = _.wrap( $.datepicker._doKeyDown, function(func, event) {
    if ( !this._pressedKeys && event.keyCode == 13 ) {
        // if ENTER is the first keypress in the open datepicker, just close it
        $.datepicker._hideDatepicker();
    } else {
        this._pressedKeys = true;
        return func( event );
    }
});

var _datepickerConfig = {

    /* create input element */
    element: function( settings, original ) {
      var form = $( this ),
          input = $( '<input />' );
          btn = $( "<button type='button'>&nbsp;</button>" );
      input.attr( 'autocomplete','off' );
      form.append( input );
      form.append( btn );
      return input;
    },

    /* attach jquery.ui.datepicker to the input element */
    plugin: function( settings, original ) {
      var form = this,
          input = form.find( "input" );

      // Don't cancel inline editing onblur to allow clicking datepicker
      // this is the jeditable settings, not the datepicker options
      settings.onblur = 'nothing';
      var datepickerSettings = settings.datepicker || {};


      var datepicker = jQuery.extend( {}, datepickerSettings, {
        onSelect: function() {
          // clicking specific day in the calendar should
          // submit the form and close the input field
          form.submit();
          var handler = datepickerSettings.onSelect;
          return handler && handler.apply( this, arguments );
        },

        onClose: function() {
          setTimeout( function() {
            if ( !input.is( ':focus' ) ) {
              // input has NO focus after 150ms which means
              // calendar was closed due to click outside of it
              // so let's close the input field without saving
              original.reset( form );
            } else {
              // input still HAS focus after 150ms which means
              // calendar was closed due to Enter in the input field
              // so lets submit the form and close the input field
              form.submit();
            }
            var handler = datepickerSettings.onClose;
            return handler && handler.apply( this, arguments );

            // the delay is necessary; calendar must be already
            // closed for the above :focus checking to work properly;
            // without a delay the form is submitted in all scenarios, which is wrong
          }, 150 );
        }
      });

      input.datepicker(datepicker);
    }
}
$.editable.addInputType( 'datepicker', _datepickerConfig ); // note that this is usually hidden by i18n_core jquery.jeditable.multi.datepicker.js

$.editable.addInputType( 'datepicker.jquery', _datepickerConfig );
