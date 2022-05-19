
jQuery(function($) {
	$.extend($.fn.dataTable.defaults, {
		language: {
			url: "http://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Japanese.json"
		}
	});
	$(".toggle-detail").click(function() {
		$(".toggle-detail-open").slideToggle();
	});

	$(".toggle-comment").click(function() {
		$(".toggle-comment-open").slideToggle();
	});
});

