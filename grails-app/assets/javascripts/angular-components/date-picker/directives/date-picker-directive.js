angular.module('datePickerApp', [])
    .directive('datePicker',function(){
        return{
        restrict: "A",
        scope:{
            date:"=",
            showOn: "@",
            onSelect:"&",
            onClose:"&"
        },

        link:function($scope, $ele) {

            $ele.multiCalendarPicker({
                showOn: $scope.showOn ? $scope.showOn : 'button',
                onSelect:$scope.onSelect(),
                onClose:$scope.onClose()
            })

        }}
    });


var hideCalender=function(){

    if($('#checkId').is(":checked")){

        $('#multiCalendar1').hide()
        $('#multiCalendar2').show()
    }
    else{
        $('#multiCalendar2').hide()
        $('#multiCalendar1').show()
    }
}






