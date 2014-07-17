$(document).ready(function() {

	// LOAD ISOTOPE PLUGIN
	$('#searchable-container').isotope({
		itemSelector : '.items',
		masonry: {
			containerStyle: null
		}
	});

	// SHOW EXAMPLES ON TAG CLICKED.
	$(".nav.nav-pills li").on("click",function(){

		$(".nav.nav-pills li").removeClass("active");
		$(this).addClass("active");

		var filterVal = $(this).text().toLowerCase();

		if(filterVal == 'show all tags') {

			$('#searchable-container .items').fadeIn('slow').removeClass('hidden');

		} else {

			$('#searchable-container .items').filter(function() {
				if(!$(this).hasClass(filterVal)) {
					$(this).addClass('hidden');

				} else {
					$(this).removeClass('hidden');
				}

			});

		}

		$('#searchable-container').isotope({
			itemSelector : '.items',
			masonry: {
				containerStyle: null
			}
		});

		return false;
	});

	// OPEN UP EXAMPLE WHEN CLICKED
	$("#searchable-container .items").on("click",function(){

		var exampleLink = $(this).find("a");
		exampleLink.attr("target", "_blank");
		window.open(exampleLink.attr("href"));

		return false;

	});

	// SEARCH FOR EXAMPLES.
	$(function() {
		$('#input-search').on('keyup', function() {

			var rex = new RegExp($(this).val(), 'i');
			$('#searchable-container .items').hide();
			$('#searchable-container .items').filter(function() {
				return rex.test($(this).text());
			}).show();

			$('#searchable-container').isotope({
				itemSelector : '.items',
				masonry: {
					containerStyle: null
				}
			});

		});
	});

});