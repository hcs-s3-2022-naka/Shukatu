package jp.ac.hcs.ShukatsuPortal.jobapp;

import lombok.Data;

/**
 * とりまとめ書類の提出情報 各項目のデータ仕様も合わせて管理する
 **/
@Data
public class JobAppDocumentData {

	/**
	 * とりまとめID
	 */
	private int summaryId;

	/**
	 * 書類ID
	 */
	private int documentId;

	/**
	 * 提出済フラグ
	 */
	private boolean status;
}
