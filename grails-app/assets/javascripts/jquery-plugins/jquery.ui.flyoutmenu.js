$('div.navigation-flyout').stop().animate({'marginRight':'-160px'},1000);

$('div.navigation-flyout').hover(
    function () {
        $(this).stop().animate({'marginRight':'-2px'},200);
    },
    function () {
        $(this).stop().animate({'marginRight':'-160px'},200);
    }
);