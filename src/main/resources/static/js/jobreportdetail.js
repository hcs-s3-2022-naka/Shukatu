function requireCheck() {

	var flag = 0;


	// 設定開始（必須にする項目を設定してください）
	if (document.remand.indicate.value == "") { // 「コメント」の入力をチェック

		flag = 1;

	}

	// 設定終了


	if (flag) {

		var newElement = document.createElement("p"); // p要素作成
		var newContent = document.createTextNode("必須入力です"); // テキストノードを作成
		newElement.appendChild(newContent); // p要素にテキストノードを追加
		newElement.setAttribute("id", "requirecheck"); // p要素にidを設定

		// 親要素（div）への参照を取得
		var remand = document.getElementById("remand");

		// 子要素１への参照を取得
		var indicate = document.getElementById("indicate");

		// 追加
		remand.insertBefore(newElement, indicate.nextSibling);
		alert(2)

		return false; // 送信を中止

	}
	else {
		alert(1)
		return true; // 送信を実行

	}

}
jQuery(function($) {
	$(".toggle-detail").click(function() {
		$(".toggle-detail-open").slideToggle();
	});

	$(".toggle-comment").click(function() {
		$(".toggle-comment-open").slideToggle();
	});
});