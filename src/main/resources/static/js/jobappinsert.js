var i = 0;
var maxrows = 20;
function hage() {
	i++;
	// Tbody への参照を取得します
	var mybody = document.getElementById("histTablebody");

	// セルを生成します
	// タグ名が TR である要素を生成します
	mycurrent_row = document.createElement("TR");
	mycurrent_row.setAttribute("id", "histrow" + i);

	// -------
	// タグ名が TH である要素を生成します
	mycurrent_cell = document.createElement("TH");
	mycurrent_cell.setAttribute("id", "num" + i);
	// テキストノードを生成します。
	currenttext = document.createTextNode("会社名");
	// 生成したテキストノードを TD セルへと付加します
	mycurrent_cell.appendChild(currenttext);
	// その TD セルを TR 行へと付加します
	mycurrent_row.appendChild(mycurrent_cell);
	// -------
	// タグ名が TD である要素を生成します
	mycurrent_cell = document.createElement("TD");
	// フォームノードを生成します
	mycurrent_form = document.createElement("INPUT");
	mycurrent_form.setAttribute("type", "text");
	mycurrent_form.setAttribute("name", "appCompany[" + i + "]");
	mycurrent_form.setAttribute("value", "");
	mycurrent_form.setAttribute("id", "ymd");
	mycurrent_form.setAttribute("class", "form-control mb-2");
	mycurrent_form.setAttribute("placeholder", "受験企業を入力してください。");
	mycurrent_form.setAttribute("th:field", "*{appCompany[" + i + "]}");
	// 生成したフォームノードを TD セルへと付加します
	mycurrent_cell.appendChild(mycurrent_form);
	// その TD セルを TR 行へと付加します
	mycurrent_row.appendChild(mycurrent_cell);

	// -------
	// タグ名が TD である要素を生成します
	mycurrent_cell = document.createElement("TD");
	// フォームノードを生成します
	mycurrent_form = document.createElement("INPUT");
	mycurrent_form.setAttribute("type", "button");
	mycurrent_form.setAttribute("name", "appCompany[" + i + "]");
	mycurrent_form.setAttribute("value", "削     除");
	mycurrent_form.setAttribute("id", "delrow");
	mycurrent_form.setAttribute("class", "btn btn-dark  btn-block");
	mycurrent_form.setAttribute("onClick", "hige();");
	// 生成したフォームノードを TD セルへと付加します
	mycurrent_cell.appendChild(mycurrent_form);
	// その TD セルを TR 行へと付加します
	mycurrent_row.appendChild(mycurrent_cell);

	// その TR 行を TBODY へと付加します
	mybody.appendChild(mycurrent_row);

	// 削除ボタンを有効にする
	document.hogehoge.delrow.disabled = false;
	// テーブルの数（行の数）がmaxrows以上の場合は
	// 追加ボタンを無効にする
	if (i >= maxrows) {
		document.hogehoge.addrow.disabled = true;
	}
}
var hige = function() {
	var mytable = document.getElementById("histTablebody");
	var removeTable = document.getElementById("histrow" + i);
	mytable.removeChild(removeTable);
	i--;
	// テーブルの数（行の数）が0の場合は
	// 削除ボタンを無効にする
	if (i == 1) {
		document.hogehoge.delrow.disabled = true;
	}
	// 追加ボタンを有効にする
	document.hogehoge.addrow.disabled = false;
}

function changeDisabled() {
	if (document.Form1["appContents"][3].checked) { // 「その他」のラジオボタンをクリックしたとき
		document.Form1["appContentOther"].disabled = false; // 「その他」のラジオボタンの横のテキスト入力欄を有効化
	} else { // 「その他」のラジオボタン以外をクリックしたとき
		document.Form1["appContentOther"].disabled = true; // 「その他」のラジオボタンの横のテキスト入力欄を無効化
	}
}

window.onload = changeDisabled; // ページを表示したとき、changeDisabled() を呼び出す