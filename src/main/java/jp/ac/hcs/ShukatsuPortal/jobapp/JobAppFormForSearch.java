package jp.ac.hcs.ShukatsuPortal.jobapp;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

/**
 * フロントーバックで検索機能実行時の検索情報をやり取りする
 */
@Data
public class JobAppFormForSearch {

	/**
	 * クラス
	 */
	private String applyClass;

	/**
	 * 番号
	 */
	private int applyNumber;

	/**
	 * 氏名
	 */
	@Length(min = 1, max = 20)
	private String applyName;

	/**
	 * 企業名
	 */
	@Length(min = 1, max = 30)
	private String appCompany;

	/**
	 * 開始年月日
	 */
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private String appStartDate;

	/**
	 * 終了年月日
	 */
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private String appFinishDate;

	/**
	 * 申請状態
	 */
	private int appStatus;

	/**
	 * とりまとめと検索のどちらのボタンが押されたかの数値
	 */
	@NotNull
	private int buttonValue;

}
