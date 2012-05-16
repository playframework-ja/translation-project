$(function(){

    // Homemade clickout plugin
    $.fn.clickout = function( target, callback ){
        return this.each(function(){
            var _button = $(this),
                _pan = $(target);
            if (document.addEventListener){
                document.addEventListener('click', function(e){
                    if (_button == e.target){
                        e.preventDefault();
                        e.stopPropagation();
                    }
                    if (_pan[0] != e.target && _pan.has(e.target).length == 0){
                        document.removeEventListener('click', arguments.callee, true);
                        callback();
                    }
                }, true);
            }
        });
    }

    // Download pan
    $("header a.download").click(function(e){
        e.preventDefault();
        var _button = $(this),
            _pan = $("#download");
        _pan.slideDown('fast');
        _button.clickout(_pan,function(){
            _pan.slideUp('fast');
        })
    });

    // Logos pan
    // Idea stollen from wordpress.com
    $("#logo").on("contextmenu",function(e){
        e.preventDefault();
        e.stopPropagation();
        var _button = $(this),
            _pan = $("#getLogo");
        _pan.slideDown('fast');
        // Fix firefox
        setTimeout(function(){
            _button.clickout(_pan,function(){
                _pan.slideUp('fast');
            });
        }, 100);
        return false;
    });

    // Scroll the side nav
    var scroll = (function(){
        // Detect css transform
        var prefixes = 'transform WebkitTransform MozTransform OTransform msTransform'.split(' '),
            cssT = "",
            support = 0;
        while( support !== true ){
          cssT = prefixes[support];
          support = document.createElement('div').style[prefixes[support++]] != undefined || support;
        }
        if (!support || !$("aside").length || document.ontouchstart === null ) return false;

        var el      = $("aside"),
            top     = el.offset().top,
            height  = el.height(),
            wHeight = $(window).height(),
            memo    = 0,
            // In a perfect world, we'd caculate this after image loading
            max     = $(document).height() - $("footer").height() - 200 + wHeight - height - 30;

        $(document).scroll(function(e){
            if (window.scrollY + 120 > top){
                el.addClass('stick');
                if (window.scrollY + wHeight > max){
                    el.css(cssT, "translate(0,"+( max - window.scrollY - wHeight ) +"px)");
                } else {
                    el[0].scrollTop += window.scrollY - memo;
                    el.css(cssT, "translate(0,0)");
                    memo = window.scrollY;
                }
            } else {
                el.removeClass('stick');
                el.css(cssT, "translate(0,0)");
            }
        }).trigger("scroll")
        $(window).resize(function(){
            wHeight = $(window).height();
            max     = $(document).height() - $("footer").height() - 200 + wHeight - height - 30;
        })
        $("#content").css("min-height", el.height() + 100 + "px")
    })
    //setTimeout(scroll, 1000)
    
    // Tweets
    
    var parseURL = function(s) {
    	return s.replace(/[A-Za-z]+:\/\/[A-Za-z0-9-_]+\.[A-Za-z0-9-_:%&~\?\/.=]+/g, function(url) {
    		return url.link(url);
    	});
    };
    
    var parseUsername = function(s) {
    	return s.replace(/[@]+[A-Za-z0-9-_]+/g, function(u) {
    		var username = u.replace("@","")
    		return u.link("http://twitter.com/"+username);
    	});
    }
    
    var parseHashtag = function(s) {
    	return s.replace(/[#]+[A-Za-z0-9-_]+/g, function(t) {
    		var tag = t.replace("#","%23")
    		return t.link("http://search.twitter.com/search?q="+tag);
    	});
    };
    
    var makeTwitterLinks = function(s) {
        return parseUsername(parseHashtag(parseURL(s)))
    }
    
    
    $('.tweet p').each(function() {
        $(this).html(makeTwitterLinks($(this).text()))
    })

})
 
