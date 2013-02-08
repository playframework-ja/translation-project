;(function(){

    var scroll = window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame || window.msRequestAnimationFrame || window.oRequestAnimationFrame || function(callback){ window.setTimeout(callback, 1000/60) }
      , has3d = document.body.style.transform
      , lastPosition = -1
      , wHeight = window.innerHeight

    function Penfeld(set, selector){
        var self = this
        this.set = $(set)
        this.matrix = []

        function loop(){
            // Avoid calculations if not needed
            if (lastPosition == window.pageYOffset) {
                scroll(loop)
                return false
            } else lastPosition = window.pageYOffset

            var i = 0, o = {}, delta = 0;
            for (; i<self.matrix.length; i++){
                o = self.matrix[i]
                // Is it visible right now?
                if (lastPosition >= o.start && lastPosition <= o.stop){
                    delta = (lastPosition - o.start) * 100 / (o.stop - o.start)
                    self.matrix[i].func(o.el, delta, o)
                }
            }
            scroll(loop)
        }

        // Default pannel behavior
        function Scroll(el, func){
            this.el = el
            this.func = func
            this.refresh = function(){
                this.height = el.height()
                this.top = el.offset().top
                this.start = this.top - wHeight
                this.stop = this.top + this.height
                return this
            }
            this.refresh()
        }

        this.refresh = function(){
            wHeight = window.innerHeight
            $.each(self.matrix, function(i,el){
                el.refresh()
            })
            lastPosition = -1
        }

        this.scroll = function(selector, func){
            self.matrix.push( new Scroll($(this.set).find(selector), func) )
            return this
        }

        $(window).resize(this.refresh)

        // Let's go
        this.start = function(){
            loop()
        }

    }

    $.fn.penfeld = function(selector){
        return new Penfeld(this)
    }

})()

$(function(){

    var pfx = "",
        form = document.createElement('div'),
        props = ['perspective', 'WebkitPerspective',
                'MozPerspective', 'OPerspective', 'msPerspective'],
        i = 0,
    support = false;
    while (props[i]) {
        if (props[i] in form.style) {
            support = true;
            pfx = props[i].replace(/Perspective/i,'');
            pfx = pfx.toLowerCase();
            break;
        }
        i++;
    } 

    var els = {
        developer: {
            terminal: $(".terminal"),
            editor: $(".editor"),
            browser: $(".browser")
        },
        scale: {
            instance1: $("div.instance1"),
            instance2: $("div.instance2"),
            instance3: $("div.instance3"),
            instance4: $("div.instance4"),
            trigger1: $("span.instance1"),
            trigger2: $("span.instance2"),
            trigger3: $("span.instance3"),
            trigger4: $("span.instance4")
        },
        modern: $("#modern")
    }

    vendor = pfx ? pfx + "Transform" : "transform"
    // Usage
    $("#content").penfeld()
        .scroll(".developer", function(el, delta, infos){
            els.developer.terminal.css(vendor, "translate3d(0,0,-"+(delta/2)+"px)")

            if (delta < 20){
                els.developer.terminal.css("background-position", "0 0")
                els.developer.editor.css("background-position", "0 0")
                els.developer.browser.css("background-position", "0 0")
            } else if(delta < 50){
                els.developer.terminal.css("background-position", "-401px 0")
                els.developer.editor.css("background-position", "0 0")
                els.developer.browser.css("background-position", "0 0")
            } else if(delta < 70){
                els.developer.terminal.css("background-position", "-401px 0")
                els.developer.editor.css("background-position", "-401px 0")
                els.developer.browser.css("background-position", "0 0")
            } else {
                els.developer.terminal.css("background-position", "-401px 0")
                els.developer.editor.css("background-position", "-401px 0")
                els.developer.browser.css("background-position", "-802px 0")
            }

            if (delta < 50){
                els.developer.editor.css(vendor, "translate3d(0,"+(250-delta*4)+"px,"+(25-delta/2)+"px) rotateX(-"+(50-delta)+"deg)")
                els.developer.editor.css("opacity", (delta*2)/100)
            } else {
                els.developer.editor.css(vendor, "translate3d(0,50px,"+(25-delta/2)+"px) rotateX(0)")
                els.developer.editor.css("opacity", 1)
            }

            if (delta < 20){
                els.developer.browser.css(vendor, "translate3d(0,"+(300-(delta-20)*4)+"px,"+(50-delta/2)+"px) rotateX(-20deg)")
                els.developer.browser.css("opacity", 0)
            } else if (delta < 70) {
                els.developer.browser.css(vendor, "translate3d(0,"+(300-(delta-20)*4)+"px,"+(50-delta/2)+"px) rotateX(-"+(50-(delta-20))+"deg)")
                els.developer.browser.css("opacity", ((delta-20)*2)/100)
            } else {
                els.developer.browser.css(vendor, "translate3d(0,100px,"+(50-delta/2)+"px) rotateX(0)")
                els.developer.browser.css("opacity", 1)
            }
        })
        .scroll(".scale", function(el, delta, infos){
            // console.log(delta, infos);

            if(delta < 20){
                els.scale.instance1.css("height", (100+delta*2+50)+"px")
                els.scale.instance2.css("height", (0)+"px")
                els.scale.instance3.css("height", (0)+"px")
                els.scale.instance4.css("height", (0)+"px")

                els.scale.trigger1.css("opacity", 1)
                els.scale.trigger2.css("opacity", 0)
                els.scale.trigger3.css("opacity", 0)
                els.scale.trigger4.css("opacity", 0)
            } else if(delta < 40){
                els.scale.instance1.css("height", (180-delta*2+50)+"px")
                els.scale.instance2.css("height", ((delta-20)*4+50)+"px")
                els.scale.instance3.css("height", (0)+"px")
                els.scale.instance4.css("height", (0)+"px")

                els.scale.trigger1.css("opacity", 1)
                els.scale.trigger2.css("opacity", 1)
                els.scale.trigger3.css("opacity", 0)
                els.scale.trigger4.css("opacity", 0)
            } else if(delta < 60){
                els.scale.instance1.css("height", (60+delta+50)+"px")
                els.scale.instance2.css("height", (40+delta+50)+"px")
                els.scale.instance3.css("height", (delta+50)+"px")
                els.scale.instance4.css("height", (0)+"px")

                els.scale.trigger1.css("opacity", 1)
                els.scale.trigger2.css("opacity", 1)
                els.scale.trigger3.css("opacity", 1)
                els.scale.trigger4.css("opacity", 0)
            } else {
                els.scale.instance1.css("height", (180-delta+50)+"px")
                els.scale.instance2.css("height", (100-delta/3+50)+"px")
                els.scale.instance3.css("height", (delta+50)+"px")
                els.scale.instance4.css("height", ((delta-60+50)*2)+"px")

                els.scale.trigger1.css("opacity", 1)
                els.scale.trigger2.css("opacity", 1)
                els.scale.trigger3.css("opacity", 1)
                els.scale.trigger4.css("opacity", 1)
            }
        })
        .scroll(".modern", function(el, delta, infos){
            els.modern.css("background-position", "20px "+(delta*1.6-10)+"%")
        })
        .start();

})


