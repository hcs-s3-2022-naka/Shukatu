package jp.ac.hcs.ShukatsuPortal.jobreport;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 申請の報告情報を管理するエンティティクラス
 */
@Data
public class JobReportEntity {

	/**
	 * 申請の報告情報を管理するリスト
	 */
	List<JobReportData> jobReportList = new ArrayList<JobReportData>();

	/**
	  * 報告コメント
	  * 必須入力
	  */
	private String repComment;

	/**
	 * 申請状態
	 */
	private int appStatus;

	/**
	 * 指摘コメント
	 */
	private String indicate;

}
