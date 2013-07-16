$(function(){

    // Right click on the logo
    $("#logo").on("contextmenu",function(e){
        e.preventDefault()
        e.stopPropagation()
        $("#getLogo")
            .fadeIn('fast')
            .click(function(){
                $(this).fadeOut("fast")
            })
        return false
    });

    // Scope on documentation pages
    $("body.documentation").each(function(el){
        // Versions' dropdown
        $(".dropdown", el).on("click", "dt", function(e){
            $(e.delegateTarget).toggleClass("open")
        })

        // Extend UI
        $("#extend").hover(function(e){
            $("#content").addClass("plus")
            $("body").addClass("animate")
        },function(e){
            $("#content").removeClass("plus")
        }).click(function(e){
            $("#content").removeClass("plus")
            $("body").toggleClass("flex")
            if (window.localStorage) localStorage['flex'] = $("body").hasClass("flex")
        })
    })

    // Scope on modules page
    $("body.modules").each(function(el){
        var list = $("#list > li")
          , versions = $("input[name=version]")
          , filters = {
                keyword: ""
              , version: "v2"
            }

        function doFilter (){
            console.log("kw", filters.keywords)
            if (!filters.keywords) {
                list.show()
            } else {
                for (i in filters.keywords){
                    list.each(function(){
                        if ($(this).text().toLowerCase().search(filters.keywords[i].toLowerCase())>=0) {
                            $(this).show()
                        } else {
                            $(this).hide()
                        }
                    })
                }
            }
            list.each(function(){
                if ( !!filters.version && $(this).find("."+filters.version).length == 0 ) {
                    $(this).hide()
                }
            })

        }

        $("#module-search").keyup(function(e){
            filters.keywords = !this.value ? false : this.value.split(" ")
            doFilter()
        })

        $(versions).change(function(){
            filters.version = this.value
            doFilter()
        })
        doFilter()

        list.click(function(e){
            $(this).addClass("open")
                .siblings(".open").removeClass("open")
        })

    })

})

var _gaq = _gaq || [];
_gaq.push(['_setAccount', 'UA-37989507-1']);
_gaq.push(['_trackPageview']);

(function() {
  var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
  ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
  var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
})();