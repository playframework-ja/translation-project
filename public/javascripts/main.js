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
    });

    // Code snippet tabs
    $("body.documentation article dl").has("dd > pre").each(function() {
        var dl = $(this);
        dl.addClass("tabbed");
        dl.find("dt").each(function(i) {
            var dt = $(this);
            dt.html("<a href=\"#tab" + i + "\">" + dt.text() + "</a>");
        });
        dl.find("dd").hide();
        var current = dl.find("dt:first").addClass("current");
        var currentContent = current.next("dd").show();
        dl.css("height", current.height() + currentContent.height());
    });
    $("body.documentation article dl.tabbed dt a").click(function(e){
        e.preventDefault();
        var current = $(this).parent("dt");
        var dl = current.parent("dl");
        dl.find(".current").removeClass("current").next("dd").hide();
        current.addClass("current");
        var currentContent = current.next("dd").show();
        dl.css("height", current.height() + currentContent.height());
    });

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

    // Scope on download page
    $("body.download").each(function(el){
        // Show guides on download
        var download = $(".latest"),
            getStarted = $(".get-started"),
            getStartedBack = $(".back", getStarted);

        // Trigger after a click and its tracking event have finished
        download.on('trackedClick', function(e) {
            getStarted.show();
        });
        getStartedBack.click(function(e) {
            getStarted.hide();
        });

        // Older releases
        var versions = $("#alternatives .version");

        versions.each(function(i, el) {
            var list = $("tr", el).slice(3).hide();
            $(".show-all-versions", el).click(function() {
                list.show();
                $(this).hide();
            }).toggle(!!list.length);
        });
    })

    // Downloads page tracking
    function trackDownload(selector, name) {
        $(selector).click(function() {
            var el = $(this)
            var version = el.data("version");
            if (version) {
                var label = name + "-" + version;
            } else {
                var label = name;
            }
            function triggerTrackedClick() {
                el.trigger('trackedClick');
            }
            if (window._gaq) {
                _gaq.push(
                    ["_set", "hitCallback", triggerTrackedClick],
                    ["_trackEvent", "download", "click", label],
                    ["_set", "hitCallback", null]
                );
            } else {
                // Trigger tracked click immediately if analytics not loaded
                triggerTrackedClick();
            }
            // Stop download for now because it would prevent tracking
            return false;
        });
        // Resume normal click behavior after tracking has happened.
        $(selector).on('trackedClick', function() {
            location.href = $(this).attr('href');
        })
    }
    trackDownload(".downloadActivatorLink", "activator");
    trackDownload(".downloadStandaloneLink", "standalone");
    trackDownload(".downloadDevelopmentLink", "development");
    trackDownload(".downloadPreviousLink", "previous");
})

var _gaq = _gaq || [];
_gaq.push(['_setAccount', 'UA-37989507-1']);
_gaq.push(['_trackPageview']);

(function() {
  var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
  ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
  var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
})();