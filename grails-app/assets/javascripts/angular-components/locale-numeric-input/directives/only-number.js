numericApp.directive('onlyNumber', function() {
    return function(scope, element, attrs) {

        var keyCode = [8,9,37,39,48,49,50,51,52,53,54,55,56,57,96,97,98,99,100,101,102,103,104,105,109,189,110,46];
        element.bind("keydown", function(event) {
            if( event.ctrlKey || event.metaKey){
                if(event.keyCode=="65" || event.keyCode=="86" || event.keyCode=="67"){
                    return;
                }
            }   else if($.inArray(event.which,keyCode) == -1) {
                scope.$apply(function(){
                    scope.$eval(attrs.onlyNum);
                    event.preventDefault();
                });
                event.preventDefault();
            }

        })
    };
})
