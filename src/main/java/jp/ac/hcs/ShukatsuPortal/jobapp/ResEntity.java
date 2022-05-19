package jp.ac.hcs.ShukatsuPortal.jobapp;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 誤字チェックの戻り値
 * 各項目のデータ仕様については、APIの仕様を参照する
 * -https://a3rt.recruit.co.jp/product/proofreadingAPI/
 */

@Data
public class ResEntity {

	/*
	 * 結果ID
	 */
	private String resultId;

	/*
	 *結果ステータス
	 */
	private int status;

	/*
	 * メッセージ
	 */
	private String message;

	/*
	 * チェック対象文
	 */
	private String inputSentence;


	/*
	 * 正規化した文
	 */
	private String normalizedSentence;

	/*
	 * チェック後の文。指摘箇所を<<>>で示す。
	 */
	private String checkedSentence;

	/*
	 * 指定内容を格納した配列
	 */
	private List<ResponseData> alerts = new ArrayList<ResponseData>();
}
