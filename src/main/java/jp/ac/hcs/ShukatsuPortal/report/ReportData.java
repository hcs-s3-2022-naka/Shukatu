	package jp.ac.hcs.ShukatsuPortal.report;

import java.util.Date;

import lombok.Data;

@Data
public class ReportData {

	/**
	 * 受験報告ID
	 * 主キー
	 */
	private int reportId;

	/**
	 * 学科コード
	 * 必須入力
	 */
	private String department;

	/**
	 * 求人番号
	 */
	private int jobNumber;

	/**
	 * 企業名カナ
	 */
	private String enterpriseNameKana;

	/**
	 * 企業名
	 */
	private String enterpriseName;

	/**
	 * 試験申込区分
	 */
	private String testCategory;

	/**
	 * 試験申込区分その他
	 */
	private String testCategoryOther;

	/**
	 * 受験日時
	 */
	private Date examinationDate;

	/**
	 * 受験場所名
	 */
	private String examinationLocation;

	/**
	 * 試験会場区分
	 */
	private String locationCategory;

	/**
	 * 試験会場区分その他
	 */
	private String locationCategoryOther;

	/**
	 * 試験段階区分
	 */
	private String stageCategory;

	/**
	 * 試験段階区分その他
	 */
	private String stageCategoryOther;

	/**
	 * 試験内容区分
	 */
	private String contentCategory;

	/**
	 * 試験内容区分その他
	 */
	private String contentCategoryOther;

	/**
	 * 結果通知日時
	 */
	private int resultDate;

	/**
	 * 合格のみフラグ
	 */
	private boolean passFlag;

	/**
	 * 結果通知区分
	 */
	private String resultCategory;

	/**
	 * 適性試験区分
	 */
	private String appropriateCategory;

	/**
	 * 適性試験区分その他
	 */
	private String appropriateCategoryOther;

	/**
	 * 筆記作文区分
	 */
	private String writingCategory;

	/**
	 * 筆記作文区分その他
	 */
	private String writingCategoryOther;

	/**
	 * 面接概要区分
	 */
	private String interviewCategory;

	/**
	 * 面接概要区分その他
	 */
	private String interviewCategoryOther;

	/**
	 * 集団面接人数
	 */
	private int groupNumber;

	/**
	 * 面接官人数
	 */
	private int officerNumber;

	/**
	 * 面接官役職
	 */
	private String officerRole;

	/**
	 * 面接時間
	 */
	private int interviewTime;

	/**
	 * GDテーマ
	 */
	private String gdThema;

	/**
	 * 出題内容
	 */
	private String content;

	/**
	 * 備考
	 */
	private String remarks;

	/**
	 * 受験報告状態
	 */
	private String reportStatus;

	/**
	 * 登録日時
	 */
	private Date registrationDate;

	/**
	 * 登録者のユーザID
	 */
	private String registrationUserId;

	/**
	 * 更新日時
	 */
	private Date updateDate;

	/**
	 * 更新者のユーザID
	 */
	private String updateUserId;

	/**
	 * 担任指摘コメント
	 */
	private String teacherComment;

	/**
	  * ユーザID（メールアドレス）
	  * 主キー、必須入力、メールアドレス形式
	  */
	 private String user_id;


	 /**
	  * ユーザ名
	  * 必須入力
	  */
	 private String userName;

	 /**
	  * 所属クラス
	  *
	  */
	 private String belongClass;

	 /**
	  * 出席番号
	  */
	 private int studentNumber;

}
