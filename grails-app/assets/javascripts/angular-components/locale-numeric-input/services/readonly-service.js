numericApp.service('readonlysvc', function() {

    this.toggle = function(ele,value) {
        var numberInput = $(ele).find('input[type=number]');
        var textInput = $(ele).find('input[type!=number]');
        if(value === 'true'){
            numberInput.attr('readonly','readonly');
            $(numberInput).addClass('readonly');
            $(textInput).addClass('readonly');
            $(numberInput).on('click');
        }
        else{
            numberInput.removeAttr('readonly');
            $(numberInput).removeClass('readonly');
            $(textInput).removeClass('readonly');
            $(numberInput).off('click');

        }
    }
});
