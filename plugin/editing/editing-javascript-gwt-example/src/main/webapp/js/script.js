// load when the document is ready.
$(document).ready(function() {

	var $container = $('#masonry');

	$container.isotope({
		itemSelector : '.masonry_item'
	});

	// quick search regex
	var qsRegex;

	// use value of search field to filter
	var $search = $('#search').keyup(function() {

		$container.isotope({
			itemSelector: '.masonry_item',
			filter: function() {
				return qsRegex ? $(this).text().match( qsRegex ) : true;
			}
		});

		qsRegex = new RegExp( $search.val(), 'gi' );
		$container.isotope();

	});

	// Filter isotope entries.
	$('.filter li').click(function(){

		$container.isotope({
			itemSelector: '.masonry_item',
			filter: "*"
		});

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

});