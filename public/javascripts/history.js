$(document).ready(function(){

	var versions = $("#historyMenu li"),
		container = $("#history")[0];
	versions.click(function(){
		$("#historyMenu li.active").removeClass("active");
		var id = $(this).addClass("active").attr("id");
		container.className = id;
	});


});
