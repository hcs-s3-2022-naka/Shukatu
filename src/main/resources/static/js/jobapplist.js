function aleatscript(num) {
	if (num == undefined) {

	} else {

	}
}

$(function() {
	$('.js-modal-open').on('click', function() {
		$('.js-modal').fadeIn();
		return false;
	});
	$('.js-modal-close').on('click', function() {
		$('.js-modal').fadeOut();
		return false;
	});
});

jQuery(function($) {
	$.extend($.fn.dataTable.defaults, {
		language: {
			url: "http://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Japanese.json"
		}
	});
	$('#jobappTable').DataTable({
		lengthChange: false,
		searching: false,
		ordering: false,
		pageLength: 7
	});

	$(".toggle-search").click(function() {
		$(".toggle-search-open").slideToggle();
	});

	$(".toggle-list").click(function() {
		$(".toggle-list-open").slideToggle();
	});

	$(".toggle-ai").click(function() {
		var str = document.getElementById('advice');
		if (str.textContent == '表示するには赤いバーをクリック') {
			str.textContent = '閉じるには赤いバーをクリック';
		}else{
			str.textContent = '表示するには赤いバーをクリック';
		}


		$(".toggle-ai-open").slideToggle();
	});
});

function adviceChange() {


}

