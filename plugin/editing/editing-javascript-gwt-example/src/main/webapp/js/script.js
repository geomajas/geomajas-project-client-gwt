function initialise() {

	// Set an effect for the isotope (http://api.jqueryui.com/easings/)
	$easingType= 'easeInOutQuart';

	// Isotope init
	var $container = $('#masonry');
	$container.imagesLoaded( function(){

		$container.isotope({
			itemSelector : '.masonry_item'
		});

	});

	// Filter isotope entries.
	$('.filter li').click(function(){

		$('.filter li').removeClass('active');
		$(this).addClass('active');

		var selector = $(this).find('a').attr('data-option-value');
		$container.isotope({ filter: selector });

		return(false);

	});

	// Open the example on click
	$('.entry').each(function() {

		$(this).click(function() {

			var href = $(this).find('a').attr('href');
			window.open(href, '_blank');

		});

	});

}

// load when the document is ready.
$(document).ready(function() {

	initialise();

});
