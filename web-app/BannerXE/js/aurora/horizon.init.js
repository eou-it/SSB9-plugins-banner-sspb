/*
 * ******************************************************************************
 *  ? 2011 SunGard Higher Education.  All Rights Reserved.
 *
 *  CONFIDENTIAL BUSINESS INFORMATION
 *
 *  THIS PROGRAM IS PROPRIETARY INFORMATION OF SUNGARD HIGHER EDUCATION
 *  AND IS NOT TO BE COPIED, REPRODUCED, LENT, OR DISPOSED OF,
 *  NOR USED FOR ANY PURPOSE OTHER THAN THAT WHICH IT IS SPECIFICALLY PROVIDED
 *  WITHOUT THE WRITTEN PERMISSION OF THE SAID COMPANY
 *  ******************************************************************************
 */

function prepareBuffer() {
    var objNew = document.createElement( 'p' );
    var objHidden = document.createElement( 'input' );

    objHidden.setAttribute( 'type', 'hidden' );
    objHidden.setAttribute( 'value', '1' );
    objHidden.setAttribute( 'id', 'virtualbufferupdate' );
    objHidden.setAttribute( 'name', 'virtualbufferupdate' );

    objNew.appendChild( objHidden );
    document.body.appendChild( objNew );
}

function updateBuffer() {
    var objHidden = document.getElementById( 'virtualbufferupdate' );

    if ( objHidden ) {
        if ( objHidden.getAttribute( 'value' ) == '1' )
            objHidden.setAttribute( 'value', '0' );
        else
            objHidden.setAttribute( 'value', '1' );
    }
}
/**
 * @class Blocker class when activated blocks user interaction on the screen
 */
var Blocker = {

    div : $( "<div id='blocker' />" ),

	block : function() {
        if ( $( '#blocker' ).length == 0 ) {
            $( 'body' ).prepend( Blocker.div );
		}
        if ( jQuery.browser.msie ) {
            Blocker.div.addClass( 'on' );
		} else {
			// Blocker.div.animate({ opacity: 0.1, height: '100%', width: '100%'
			// }, 100);
	}

	// change cursor to 'wait' till data is retrieved
		document.body.style.cursor = 'wait';
		// provide a 15 sec window for the blocker, if the application doesn't call
		// unblock, force it.
        setTimeout( Blocker.unblock, 15000 )
	},

	unblock : function() {
        if ( $( '#blocker' ).length > 0 ) {
			Blocker.div.animate( {
				opacity : 0,
				height : '100%',
				width : '100%'
            }, 100 );
			Blocker.div.remove();
			// change cursor to 'wait' till data is retrieved
			document.body.style.cursor = 'default';
		}
	}
};

function redrawPage() {
	// set block-container height
    var bottomAnchor = $( '.action-bar' );

    if ( bottomAnchor.length == 0 ) {
        bottomAnchor = $( '#outerFooter' );
	}

	// fix: set tabbox width (possible bug in tabbox control - resizes when
	// increasing the window size but not when
	// decreasing)
    if ( $( '.tab-box' ).length > 0 ) {
        $( '.tab-box' ).css( 'width', $( 'body' ).width() );
        $( '.tab-box-tabs .z-tabs-header > ul' ).css( 'width', 'auto' );
	}

    // fix: remove 'table-layout:fixed' style setting from block-header to make it resolution free
    $( '.block-header table' ).each( function( e ) {
        if ( $( this ).css( 'table-layout' ) == 'fixed' ) {
            $( this ).css( 'table-layout', 'auto' );
        }
    } )

    // redraw breadcrumbs based on new width
    BreadCrumb.redraw();
}

initialize = function() {
    // if page not initialized, stop
/*
    if ( $( '.zk-content' ).length == 0 ) {
        intervalId = setTimeout( 'initialize()', 200 );
        return;
    }
*/
    if ( typeof intervalId != "undefined" )
        clearTimeout( intervalId );

    CommonPlatform.initialize( {
        standalone : true,
        globalNav : true,
        header : true,
        footer : true,
        handler : function( data ) {
        }
    } );

    //BannerUi.initialize();

    var openItemsSection = $( '#openedItemsCanvas' );
    if ( openItemsSection.length > 0 ) {
        openItemsSection.append( $( '.gotoPanel .zk-gotopanel' ) );
    } else {
        $( '.gotoPanel .zk-gotopanel' ).hide();
        // hide notification center
        $( ".notification-center-normal" ).hide();
    }
    // move content into the content section of the common-platform
    $( '#content' ).append( $( '.zk-content' ).children(":first-child").children() );

    var toolsSection = $('#toolsCanvas');
    if(toolsSection.length > 0) {
        toolsSection.append($('.zk-toolspanel'));
    } else {
        $('.zk-toolspanel').hide();
        // hide notification center
        $(".notification-center-normal").hide();
    }

    //adjustBlockContainer();

    // set explicit height for the block-container to enable scroll
    redrawPage();
    $( window ).bind( 'resize', function( e ) {
        redrawPage();
    } );

    Blocker.unblock();

    $(document).bind('keydown', 'alt+g', function(){
        if(isModal()) return false;
        $('#openedArrow').click();
        return false;
    });

    $('#openedArrow').bind('keydown', 'down', function(){
        $('#openedArrow').click();
    });

    $(document).bind('keydown', 'ctrl+shift+q', function(){
        if(isModal()) return false;
        $('.signOutText').click();
        return false;
    });

    $(document).bind('keydown', 'alt+F1', function(){
        if(isModal()) return false;
        $('.helpText').click();
        return false;
    });

    $(document).bind('keydown', 'shift+home', function(){
        if(isModal()) return false;
        $('#homeButton').click();
        return false;
    });

    $(document).bind('keydown', 'alt+m', function(){
        if(isModal()) return false;
        $('#browseButton').click();
        $('#browseButtonState').removeClass("over");
        $('#browseMenu').removeClass("over");
    });


    $('#menuArrow').bind('keydown', 'down' ,function(e) {
        $('#browseButton').click();
        $('#browseButtonState').removeClass("over");
        $('#browseMenu').removeClass("over");
    });

    $(document).bind('keydown', 'alt+l', function(){
        if(isModal()) return false;
        $('#toolsArrow').click();
           return false;
    });

   $('#toolsArrow').bind('keydown', 'down', function(){
           $('#toolsArrow').click();
   });

    //Fix for enabling click events on tr, td in Firefox.
    if($.browser.mozilla)
    {
        HTMLElement.prototype.click = function() {
        var evt = this.ownerDocument.createEvent('MouseEvents');
        evt.initMouseEvent('click', true, true, this.ownerDocument.defaultView, 1, 0, 0, 0, 0, false, false, false, false, 0, null);
        this.dispatchEvent(evt);
        }
    }
    //Todo:this below adding ARIA roles code needs to be moved inside respective components
    $('.block-container').attr('role','main');
    $('#header').attr('role','banner');
    $('#footerApplicationBar').attr('role','contentinfo');
    $('.zk-gotopanel').attr('role','search');
//    $('.zk-gotopanel .c-section').attr('role','navigation');

/*
     EventDispatcher.addEventListener(Localization.events.localeChange,
        function() {
            zk.appName = ResourceManager.getString('app_name');
     });
*/

      /*var textOverLay = $('<div id="textAreaPopup" class="simple_overlay"><textarea id="textAreaPopupTextbox" class="resizable"/></div>');
     $(document.body).append( textOverLay );*/
     // adds an effect called "myEffect" to the overlay
  /*  jq.tools.overlay.addEffect(
       "positionEffect",
       function(position, done) {
          var pos   = this.getTrigger().offset();
          if (getDirection(document.body) == "rtl"){
              if (exceedsViewPortRTL(this.getTrigger())) {
                  this.getOverlay().css('left', pos.left);
              } else {
                  this.getOverlay().css('left', ( pos.left - 400 + this.getTrigger().width()));
              }
          } else {
              if(exceedsViewPort(this.getTrigger())){
                  this.getOverlay().css('left',( pos.left - 400 + this.getTrigger().width()));
              }else{
                  this.getOverlay().css('left', pos.left);
              }
          }
          this.getOverlay().css('top', pos.top);
          this.getOverlay().css('position', 'absolute');
          // this.getOverlay().css('width', this.getTrigger().css('width'));
          this.getOverlay().css('width', '400px');
          this.getOverlay().show();
          done.call();
       },
       // close function
       function(done) {
           this.getOverlay().hide();
           done.call();
       }
    );
    $('#textAreaPopupTextbox').blur(function() {
        var id =  $('#textAreaPopupTextbox').attr("callingElement");
        var overlay = jq('#'+id).overlay();
        if(overlay.isOpened())
            overlay.close();
    });

    jq('#textAreaPopupTextbox').keydown(function(e){
			if(e.keyCode == 120){
                var id =  $('#textAreaPopupTextbox').attr("callingElement");
				var api =  jq('#'+id).overlay();
                api.close();
                jq('#'+id).focus();
			}
    });*/

}

function getDirection(element){
    var result = null;
    if (element){
        if (window.getComputedStyle){
            result = window.getComputedStyle(element,null).direction;
        } else if (element.currentStyle){
            result = element.currentStyle.direction;
        }
    }

    return result;
}


function exceedsViewPortRTL(trigger){
    var left = trigger.offset().left - 400 + trigger.width();
    if(left < 20 ){
        return true;
    }
    return false;
}

function exceedsViewPort(trigger){
    var left = trigger.offset().left + 400;
    var w = window.innerWidth || document.documentElement.clientWidth || document.getElementsByTagName('body')[0].clientWidth;
    if(left > (w-20) ){
        return true;
    }
    return false;
}


function isModal(){
    if($('.z-modal-mask:visible').length) return true;
    else return false;
}

function adjustBlockContainer() {
    // set the layout of the WelcomeMessage on page load
    var welcomeMessage = zk.Widget.$( jq( '@welcomemessage' ) );
    if ( welcomeMessage != null )
        welcomeMessage.fixLayout( jq( '@keyblock' ) );
}

function fixErrorWindow() {
    $( '.z-window-modal-header' ).each( function( e ) {
        if ( $( this ).text().toLowerCase().indexOf( 'zk' ) > -1 ) {
            var children = $( this ).children();
            $( this ).text( '' );
            $( this ).append( children );
		}
    } )
}


$( document ).ready( function() {
    initialize ();
    mainWindowCloseHandler();
} );

function mainWindowCloseHandler () {
    if ( $.browser.mozilla  ) {
        onBeforeUnloadHandler ();
    } else if ( $.browser.msie  ) {
        onBeforeUnloadHandler ();
    } else if ( $.browser.safari  ) {
        onBeforeUnloadHandler ();
    }  else {
        onBeforeUnloadHandler ();
    }
};

function onBeforeUnloadHandler () {
    window.onbeforeunload = function () {
        if (window.name != "") { // if blank, in this case, it could be a new window/tab.
            removeCookie("BrowserInstanceID");
        }
    }
};
