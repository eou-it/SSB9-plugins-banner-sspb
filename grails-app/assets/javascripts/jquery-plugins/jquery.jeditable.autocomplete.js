$(document).ready(function() {
    $.editable.addInputType('autocomplete', {
        element : $.editable.types.text.element,
        plugin : function(settings, original) {
            $('input', this).autocomplete({
                source: settings.autocomplete.data, minLength: 0,
                selectFirst: true,
                autoSelect: true
            });
        }
    });
});