package jp.ac.hcs.ShukatsuPortal.jobapp;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 *1件分の申請情報 各項目のデータ仕様も合わせて管理する
 */

@Data
public class JobAppData {

	/**
	 * 申請ID 主キー
	 */
	private int appId;

	/**
	 * 申請状態
	 */
	private int appStatus;

	/**
	 * とりまとめフラグ
	 */
	private boolean summaryFlag;

	/**
	 * 書類提出フラグ
	 */
	private boolean documentFlag;

	/**
	 * コース担任報告フラグ
	 */
	private boolean approvalFlag;

	/**
	 * 活動開始日付
	 */
	private String appStartDate;

	/**
	 * 活動開始時刻
	 */
	private String appStartTime;

	/**
	 * 活動終了日付
	 */
	private String appFinishDate;

	/**
	 * 活動終了時刻
	 */
	private String appFinishTime;

	/**
	 * 場所
	 */
	private String appAddress;

	/**
	 * 内容区分
	 */
	private int appContents;

	/**
	 * 会社名
	 */
	private List<String> appCompany;

	/**
	 * 内容その他
	 */
	private String appContentOther;

	/**
	 * 欠席フラグ
	 */
	private boolean absenceFlag;

	/**
	 * 欠席開始日付
	 */
	private String appStartAbsence;

	/**
	 * 欠席終了日付
	 */
	private String appFinishAbsence;

	/**
	 * 早退フラグ
	 */
	private boolean leaveFlag;

	/**
	 * 早退日付
	 */
	private String appLeaveDate;

	/**
	 * 早退時刻
	 */
	private String appLeaveTime;

	/**
	 * 遅刻フラグ
	 */
	private boolean lateFlag;

	/**
	 * 遅刻日付
	 */
	private String appLateDate;

	/**
	 * 遅刻時刻
	 */
	private String appLateTime;

	/**
	 * メモ
	 */
	private String appMemo;

	/**
	 * 申請者クラス
	 */
	private String applyClass;

	/**
	 * 申請者番号
	 */
	private int applyNumber;

	/**
	 * 申請者氏名
	 */
	private String applyName;

	/**
	 * 申請日時
	 */
	private Date appDate;

	/**
	 * 更新日時
	 */
	private Date updateDate;

	/**
	 * 報告コメント
	 */
	private String repComment;

	/**
	 * 報告日時
	 */
	private Date repDate;

	/**
	 * 指摘コメント
	 */
	private String indicate;

	/**
	 * 書類ID:履歴書
	 */
	private boolean resume;

	/**
	 * 書類ID:推薦書
	 */
	private boolean recommendation;

	/**
	 * 書類ID:大学（卒業・卒業見込）証明書
	 */
	private boolean universityCrtificate;

	/**
	 * 書類ID:HCS成績証明書
	 */
	private boolean hcsTranscript;

	/**
	 * 書類ID:HCS卒業見込証明書
	 */
	private boolean hcsCrtificate;

	/**
	 * 書類ID:健康診断証明書
	 */
	private boolean healthCheckCertificate;

	/**
	 * 書類ID:出身高校成績証明書
	 */
	private boolean highscoolTranscript;

	/**
	 * 書類ID:大学成績証明書
	 */
	private boolean universityTranscript;

	/**
	 * 書類ID:個人情報等同意書
	 */
	private boolean personalInfo;

	/**
	 * 書類提出期限
	 */
	private String deadline;

	/**
	 * 書類提出済フラグ
	 */
	private boolean status;

	/**
	 * とりまとめ名簿登録済フラグ
	 */
	private boolean registerFlag;
}
