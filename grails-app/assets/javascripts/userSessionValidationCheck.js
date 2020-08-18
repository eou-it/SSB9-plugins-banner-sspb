/******************************************************************************
 *  Copyright 2020 Ellucian Company L.P. and its affiliates.                  *
 ******************************************************************************/

document.addEventListener( 'visibilitychange' , function() {
    if (!document.hidden && CommonContext.user !== null) {
        var contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/",2))
        $.get(contextPath+'/customPage/userSessionValidationCheck', {}, function(data) {
            if(data === 'false'){
                var ele =  document.getElementById('signOut');
                if(ele) {
                    if($('meta[name=logoutEndpoint]').attr("content") === "") {
                        $('meta[name=logoutEndpoint]').attr("content",contextPath+'/logout')
                    }
                    ele.click();
                }
            }
        });
    }
}, false );
