package jp.ac.hcs.ShukatsuPortal.jobapp;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * フロントーバックで申請情報更新時の更新情報をやり取りする
 */
@Data
public class JobAppFormForUpdate {

	/**
	 *申請ID
	 *主キー
	 */
	private int appId;

	/**
	 *申請状態
	 */
	private int appStatus;

	/**
	 *とりまとめフラグ
	 */
	private boolean summaryFlag;

	/**
	 *書類提出フラグ
	 */
	private boolean documentFlag;

	/**
	 *コース担任報告フラグ
	 */
	private boolean approvalFlag;

	/**
	 *活動開始日付 必須入力
	 */
	@NotBlank(message = "{require_check}")
	private String appStartDate;

	/**
	 *活動開始時刻 必須入力
	 */
	@NotBlank(message = "{require_check}")
	private String appStartTime;

	/**
	 *活動終了日付
	 */
	private String appFinishDate;

	/**
	 *活動終了時刻
	 */
	private String appFinishTime;

	/**
	 *場所 必須入力
	 */
	@NotBlank(message = "{require_check}")
	private String appAddress;

	/**
	 *内容区分 必須入力
	 */
	@NotNull(message = "{require_check}")
	private int appContents;

	/**
	 *内容その他
	 */
	private String appContentOther;

	/**
	 *欠席フラグ
	 */
	private boolean absenceFlag;

	/**
	 *欠席開始日付
	 */
	private String appStartAbsence;

	/**
	 *欠席終了日付
	 */
	private String appFinishAbsence;

	/**
	 *早退フラグ
	 */
	private boolean leaveFlag;

	/**
	 *早退日付
	 */
	private String appLeaveDate;

	/**
	 *早退時刻
	 */
	private String appLeaveTime;

	/**
	 *遅刻フラグ
	 */
	private boolean lateFlag;

	/**
	 *遅刻日付
	 */
	private String appLateDate;

	/**
	 *遅刻時刻
	 */
	private String appLateTime;

	/**
	 *メモ
	 */
	private String appMemo;

	/**
	 *申請者クラス 必須入力
	 */
	@NotBlank(message = "{require_check}")
	private String applyClass;

	/**
	 *申請者番号 必須入力
	 */
	@NotNull(message = "{require_check}")
	private int applyNumber;

	/**
	 *申請者氏名
	 */
	private String applyName;

	/**
	 *申請日時
	 */
	private String appDate;

	/**
	 * 更新日時
	 */
	private String updateDate;

	/**
	 *報告コメント
	 */
	private String repComment;

	/**
	 *報告日時
	 */
	private String repDate;

	/**
	 *指摘コメント
	 */
	private String indicate;

	/**
	 *会社名 必須入力
	 */
	private List<String> appCompany;

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
	 * 書類ID:大学成績証明書等
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
}