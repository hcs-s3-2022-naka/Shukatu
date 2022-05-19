package jp.ac.hcs.ShukatsuPortal.jobreport;

import lombok.Data;

/**
 * 報告の中の1社分の情報 各項目のデータ仕様も合わせて管理する
 */


@Data
public class JobReportData {

	/**
	 * 申請ID
	 * 主キー
	 */
	private int appId;

	/**
	 * 会社名
	 */
	private String appCompany;

	/**
	 * 進むか
	 */
	private boolean appProceed;

}
