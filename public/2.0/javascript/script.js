$(document).ready(function(){
    
    $('#changes .link').click(function(e) {
        e.preventDefault()
        var link = $(this)
        $('#wholeAnnouncement').slideDown('slow', function() {
            link.hide()
            $(window).trigger('resize')
        })
    })

	var versions = $("#historyMenu li"),
		container = $("#history")[0];
	versions.click(function(){
		$("#historyMenu li.active").removeClass("active");
		var id = $(this).addClass("active").attr("id");
		container.className = id;
	});
	// Animate from 1.X to 2.0 on loaded
	setTimeout(function(){
		versions.eq(3).trigger("click")
	}, 1000);

	var scrolls, paddings, 
		els = $("#logo, #menu a"),
		marker = $("#marker");
	$(window).scroll(function(){
		var scroll = $(this).scrollTop();
		for(var i in scrolls){
			if ( scrolls[i] > scroll-100 ) break;
		}
		marker.css({left: paddings[i]+"px"});
		els.removeClass("active").eq(i).addClass("active");
	});
	
	$(window).resize(function(){
		scrolls = [], paddings = [];
		els.each(function(i){
			paddings.push( $(this).offset().left + $(this).width()/2 + (i==0 ? -16 : 0) );
			var id = $(this).attr("href");
			scrolls.push( $(id).offset().top );
		});
		scrolls[1] = scrolls[1] + 200;
		$(window).trigger("scroll");
	}).trigger("resize");

	els.click(function(e){
		e.preventDefault();
		var id = $(this).attr("href");
		$('html,body').addClass("scrolling").animate({
			scrollTop: $(id).offset().top + "px"
		},1000).removeClass("scrolling");
		return false;
	});

	$("#features .list a").click(function(e){
		e.preventDefault();
	});
	$("#features .list li").click(function(){
		$("#features .active").removeClass("active");
		var id = $(this).addClass("active").find("h2 a").attr("href");
		$(id).addClass("active");
	});

});
