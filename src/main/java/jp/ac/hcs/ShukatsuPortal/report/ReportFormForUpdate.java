package jp.ac.hcs.ShukatsuPortal.report;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class ReportFormForUpdate {

	/**
	 * レポートID
	 */
	private int reportId;

	/**
	 * 学科コード 必須入力
	 */
	@NotBlank(message = "{require_check}")
	@Length(min = 1, max = 1, message = "{length_check_depa}")
	@Pattern(regexp = "^[A-Za-z0-9]*$", message = "{length_check_depa}")
	private String department;

	/**
	 * 求人番号
	 */
	private int jobNumber;

	/**
	 * 企業名カナ
	 * 必須
	 */
	@NotBlank(message = "{require_check}")
	@Length(min = 3, max = 3, message="{length_check_kana}")
	@Pattern(regexp = "^[ァ-ヶー]*$", message = "{length_check_kana}")
	private String enterpriseNameKana;

	/**
	 * 企業名
	 * 必須
	 */
	@NotBlank(message = "{require_check}")
	private String enterpriseName;

	/**
	 * 試験申込区分
	 */
	@NotBlank(message = "{require_check}")
	private String testCategory;

	/**
	 * 試験申込区分その他
	 */
	private String testCategoryOther;

	/**
	 * 受験日時
	 */
	@NotBlank(message = "{require_check}")
	private String examinationDate;

	/**
	 * 受験場所名
	 */
	@NotBlank(message = "{require_check}")
	private String examinationLocation;

	/**
	 * 試験会場区分
	 */
	@NotBlank(message = "{require_check}")
	private String locationCategory;

	/**
	 * 試験会場区分その他
	 */
	private String locationCategoryOther;

	/**
	 * 試験段階区分
	 */
	@NotBlank(message = "{require_check}")
	private String stageCategory;

	/**
	 * 試験段階区分その他
	 */
	private String stageCategoryOther;

	/**
	 * 試験内容区分
	 */
	@NotBlank(message = "{require_check}")
	private String contentCategory;

	/**
	 * 試験内容区分その他
	 */
	private String contentCategoryOther;

	/**
	 * 結果通知日時
	 */
	@NotNull(message = "{require_check}")
	private int resultDate;

	/**
	 * 合格のみフラグ
	 */
	private boolean passFlag;

	/**
	 * 結果通知区分
	 */
	@NotBlank(message = "{require_check}")
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
	@NotBlank(message = "{require_check}")
	private String content;

	/**
	 * 備考
	 */
	private String remarks;

}
