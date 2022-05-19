package jp.ac.hcs.ShukatsuPortal.report;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ReportRepository {

	/** SQL 全件取得（受験報告ID昇順） */
	private static final String SQL_SELECT_ALL = "SELECT * FROM REPORT, USERS WHERE USERS.USER_ID = REPORT.REGISTERED_USER_ID ORDER BY REPORT_ID";

	/** SQL 1件取得（report_id） */
	private static final String SQL_SELECT_ONE = "SELECT * FROM REPORT WHERE REPORT_ID = ?";

	/** SQL 1件取得（user_name） */
	private static final String SQL_SELECT_ONE_BY_NAME = "SELECT * FROM REPORT, USERS WHERE REPORT.REGISTERED_USER_ID = USERS.USER_ID AND USERS.USER_NAME = ? ORDER BY REPORT_ID";

	/** SQL 1件取得（enterpriseName） */
	private static final String SQL_SELECT_ONE_BY_ENTERPRISENAME = "SELECT * FROM REPORT, USERS WHERE USERS.USER_ID = REPORT.REGISTERED_USER_ID AND ENTERPRISE_NAME = ? ORDER BY REPORT_ID";

	/** SQL 1件取得（status） */
	private static final String SQL_SELECT_ONE_BY_STATUS = "SELECT * FROM REPORT, USERS WHERE USERS.USER_ID = REPORT.REGISTERED_USER_ID AND REPORT_STATUS = ? ORDER BY REPORT_ID";

	/** SQL 受験報告状態1件更新 */
	private static final String SQL_REOPOT_STATUS_UPDATE = "UPDATE REPORT SET REPORT_STATUS = ?, teacher_comment = ? WHERE report_id = ?";

	/** SQL 1件追加 */
	private static final String SQL_INSERT_ONE = "INSERT INTO REPORT(DEPARTMENT, JOB_NUMBER, ENTERPRISE_NAME_KANA, ENTERPRISE_NAME, TEST_CATEGORY, TEST_CATEGORY_OTHER, EXAMINATION_DATE, EXAMINATION_LOCATION, LOCATION_CATEGORY, LOCATION_CATEGORY_OTHER, STAGE_CATEGORY, STAGE_CATEGORY_OTHER, CONTENT_CATEGORY, CONTENT_CATEGORY_OTHER, RESULT_DATE, PASS_FLAG, RESULT_CATEGORY, APPROPRIATE_CATEGORY, APPROPRIATE_CATEGORY_OTHER, WRITING_CATEGORY, WRITING_CATEGORY_OTHER, INTERVIEW_CATEGORY, INTERVIEW_CATEGORY_OTHER, GROUP_NUMBER, OFFICER_NUMBER, OFFICER_ROLE, INTERVIEW_TIME, GD_THEMA, CONTENT, REMARKS, REPORT_STATUS, REGISTRATION_DATE, REGISTERED_USER_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	/** SQL 1件更新 */
	private static final String SQL_UPDATE = "UPDATE REPORT SET DEPARTMENT = ?, JOB_NUMBER = ?, ENTERPRISE_NAME_KANA = ?, ENTERPRISE_NAME = ?,TEST_CATEGORY = ?,TEST_CATEGORY_OTHER = ?, EXAMINATION_DATE = ?, EXAMINATION_LOCATION = ?, LOCATION_CATEGORY = ?, LOCATION_CATEGORY_OTHER = ?, STAGE_CATEGORY = ?, STAGE_CATEGORY_OTHER = ?, CONTENT_CATEGORY = ?, CONTENT_CATEGORY_OTHER = ?, RESULT_DATE = ?, PASS_FLAG = ?, RESULT_CATEGORY = ?, APPROPRIATE_CATEGORY = ?, APPROPRIATE_CATEGORY_OTHER = ?, WRITING_CATEGORY = ?, WRITING_CATEGORY_OTHER = ?, INTERVIEW_CATEGORY = ?, INTERVIEW_CATEGORY_OTHER = ?, GROUP_NUMBER = ?, OFFICER_NUMBER = ?, OFFICER_ROLE = ?, INTERVIEW_TIME = ?, GD_THEMA = ?, CONTENT = ?, REMARKS = ?, REPORT_STATUS = ?, UPDATE_DATE = ?, UPDATE_USER_ID = ?  WHERE REPORT_ID = ? ";

	/** SQL 全件取得(指定年度かつ承認済み)*/
	private static final String SQL_SELECT_UPDATE_DATE_REPORT_STATUS = "SELECT * FROM REPORT, USERS WHERE USERS.USER_ID = REPORT.REGISTERED_USER_ID AND ? IN (SELECT YEAR(REPORT.UPDATE_DATE) FROM REPORT) AND REPORT.REPORT_STATUS = '承認済'";

	@Autowired
	private JdbcTemplate jdbc;

	/**
	 * 受験報告を全権取得する
	 * @return reportEntity
	 */
	public ReportEntity getReportList() throws DataAccessException{
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_ALL);
		ReportEntity reportEntity = mappingSelectResult(resultList);
		return reportEntity;
	}

	/**
	 * Reportテーブルから取得したデータをReportEntity形式にマッピングする.
	 * @param resultList Reportテーブルから取得したデータ
	 * @return ReportEntity
	 */
	private ReportEntity mappingSelectResult(List<Map<String, Object>> resultList) {
		ReportEntity entity = new ReportEntity();

		for (Map<String, Object> map : resultList) {
			ReportData data = new ReportData();
			data.setReportId((int) map.get("report_id"));
			data.setDepartment((String) map.get("department"));
			data.setJobNumber((int) map.get("job_number"));
			data.setEnterpriseNameKana((String) map.get("enterprise_name_kana"));
			data.setEnterpriseName((String) map.get("enterprise_name"));
			data.setRemarks((String) map.get("remarks"));
			data.setReportStatus((String) map.get("report_status"));
			data.setRegistrationDate((Date) map.get("registration_date"));
			data.setRegistrationUserId((String) map.get("registered_user_id"));
			data.setUpdateDate((Date) map.get("update_date"));
			data.setUpdateUserId((String) map.get("update_user_id"));
			if (map.get("belong_class") == null) {
				data.setBelongClass("なし");
			} else {
				data.setBelongClass((String) map.get("belong_class"));
			}
			if (map.get("student_number") == null) {
				data.setStudentNumber(0);
			} else {
				data.setStudentNumber((int) map.get("student_number"));
			}
			data.setUserName((String) map.get("user_name"));

			entity.getReportList().add(data);
		}
		return entity;
	}

	/**
	 * Reportテーブルにデータを1件追加する.
	 * @param data 追加するユーザ情報
	 * @return 追加データ数
	 * @throws DataAccessException データアクセスエラー
	 */
	public int insertOne(ReportData data) throws DataAccessException {
		int rowNumber = jdbc.update(SQL_INSERT_ONE, data.getDepartment(), data.getJobNumber(),
				data.getEnterpriseNameKana(), data.getEnterpriseName(), data.getTestCategory(),
				data.getTestCategoryOther(), data.getExaminationDate(), data.getExaminationLocation(),
				data.getLocationCategory(), data.getLocationCategoryOther(), data.getStageCategory(),
				data.getStageCategoryOther(), data.getContentCategory(), data.getContentCategoryOther(),
				data.getResultDate(), data.isPassFlag(), data.getResultCategory(), data.getAppropriateCategory(),
				data.getAppropriateCategoryOther(), data.getWritingCategory(), data.getWritingCategoryOther(),
				data.getInterviewCategory(), data.getInterviewCategoryOther(), data.getGroupNumber(),
				data.getOfficerNumber(), data.getOfficerRole(), data.getInterviewTime(), data.getGdThema(),
				data.getContent(), data.getRemarks(), data.getReportStatus(), data.getRegistrationDate(),
				data.getRegistrationUserId());
		return rowNumber;
	}

	/**
	 * 受験報告詳細に使用。USER,REPORTテーブルから取得したデータをReportEntity形式にマッピングする.
	 * @param resultList
	 * @return
	 */
	private ReportEntity mappingSelectDetail(List<Map<String, Object>> resultList) {
		ReportEntity entity = new ReportEntity();

		for (Map<String, Object> map : resultList) {
			ReportData data = new ReportData();
			if (map.get("belong_class") == null) {
				data.setBelongClass("なし");
			} else {
				data.setBelongClass((String) map.get("belong_class"));
			}
			if (map.get("student_number") == null) {
				data.setStudentNumber(0);
			} else {
				data.setStudentNumber((int) map.get("student_number"));
			}
			data.setUserName((String) map.get("user_name"));
			data.setReportId((int) map.get("report_id"));
			data.setDepartment((String) map.get("department"));
			data.setJobNumber((int) map.get("job_number"));
			data.setEnterpriseNameKana((String) map.get("enterprise_name_kana"));
			data.setEnterpriseName((String) map.get("enterprise_name"));
			data.setTestCategory((String) map.get("test_category"));
			data.setTestCategoryOther((String) map.get("test_category_other"));
			data.setExaminationDate((Date) map.get("examination_date"));
			data.setExaminationLocation((String) map.get("examination_location"));
			data.setLocationCategory((String) map.get("location_category"));
			data.setLocationCategoryOther((String) map.get("location_category_other"));
			data.setStageCategory((String) map.get("stage_category"));
			data.setStageCategoryOther((String) map.get("stage_category_other"));
			data.setContentCategory((String) map.get("content_category"));
			data.setContentCategoryOther((String) map.get("content_category_other"));
			data.setResultDate((int) map.get("result_date"));
			data.setPassFlag((boolean) map.get("pass_flag"));
			data.setResultCategory((String) map.get("result_category"));
			data.setAppropriateCategory((String) map.get("appropriate_category"));
			data.setAppropriateCategoryOther((String) map.get("appropriate_category_other"));
			data.setWritingCategory((String) map.get("writing_category"));
			data.setWritingCategoryOther((String) map.get("writing_category_other"));
			data.setInterviewCategory((String) map.get("interview_category"));
			data.setInterviewCategoryOther((String) map.get("interview_category_other"));
			data.setGroupNumber((int) map.get("group_number"));
			data.setOfficerNumber((int) map.get("officer_number"));
			data.setOfficerRole((String) map.get("officer_role"));
			data.setInterviewTime((int) map.get("interview_time"));
			data.setGdThema((String) map.get("gd_thema"));
			data.setContent((String) map.get("content"));
			data.setRemarks((String) map.get("remarks"));
			data.setReportStatus((String) map.get("report_status"));
			data.setRegistrationDate((Date) map.get("registration_date"));
			data.setRegistrationUserId((String) map.get("registered_user_id"));
			data.setUpdateDate((Date) map.get("update_date"));
			data.setUpdateUserId((String) map.get("update_user_id"));
			data.setTeacherComment((String) map.get("teacher_comment"));

			entity.getReportList().add(data);
		}
		return entity;
	}

	/**
	 * 受験報告の更新を行う
	 * @param update  更新内容
	 * @return 可否を数値で返す
	 * @throws DataAccessException データアクセスエラー
	 */
	public int updateReport(ReportData update) throws DataAccessException{
		int rowNumber = jdbc.update(SQL_UPDATE, update.getDepartment(), update.getJobNumber(),
					update.getEnterpriseNameKana(), update.getEnterpriseName(), update.getTestCategory(),
					update.getTestCategoryOther(), update.getExaminationDate(), update.getExaminationLocation(),
					update.getLocationCategory(), update.getLocationCategoryOther(), update.getStageCategory(),
					update.getStageCategoryOther(), update.getContentCategory(), update.getContentCategoryOther(),
					update.getResultDate(), update.isPassFlag(), update.getResultCategory(),
					update.getAppropriateCategory(), update.getAppropriateCategoryOther(),
					update.getWritingCategory(), update.getWritingCategoryOther(), update.getInterviewCategory(),
					update.getInterviewCategoryOther(), update.getGroupNumber(), update.getOfficerNumber(), update.getOfficerRole(),
					update.getInterviewTime(), update.getGdThema(), update.getContent(), update.getRemarks(),update.getReportStatus(), update.getUpdateDate(),
					update.getUpdateUserId(),update.getReportId());
		return rowNumber;
	}

	/**
	 * Reportテーブルから取得したデータをReportEntity形式にマッピングする.
	 * @param reportId 受験報告ID
	 * @return data
	 * @throws DataAccessException データアクセスエラー
	 */
	public ReportData getReportListOne(String reportId) throws DataAccessException {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_ONE, reportId);
		ReportEntity reportEntity = mappingSelectDetail(resultList);
		// 必ず1件のみのため、最初のReportDataを取り出す
		ReportData data = reportEntity.getReportList().get(0);
		return data;
	}

	/**
	 * 受験報告状態の更新
	 * @param reportId 受験報告ID
	 * @param comment 指摘コメント
	 * @param reportStatus 受験報告状態
	 * @return 更新可否
	 */
	public boolean updateStatus(String reportId, String comment, String reportStatus) throws DataAccessException{
		int rowNumber = jdbc.update(SQL_REOPOT_STATUS_UPDATE, reportStatus,comment ,reportId);
		return rowNumber > 0;
	}

	/**
	 * ReportテーブルからユーザIDをキーにデータを全件取得し、CSVファイルとしてサーバに保存する.
	 * @param year 指定年度
	 * @throws DataAccessException データアクセスエラー
	 */
	public void reportlistCsvOut(String year) throws DataAccessException {

		// CSVファイル出力用設定
		ReportRowCallbackHandler handler = new ReportRowCallbackHandler();

		jdbc.query(SQL_SELECT_UPDATE_DATE_REPORT_STATUS, handler, year);
	}

	/**
	 * 受験報告の作成者で絞り込み検索を行う
	 * @param user_name 検索する作成者の名前
	 * @return reportEntity
	 */
	public ReportEntity getReportListByName(String user_name) {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_ONE_BY_NAME, user_name);
		ReportEntity reportEntity = mappingSelectResult(resultList);
		return reportEntity;
	}

	/**
	 * 企業名で絞り込み検索を行う
	 * @param enterpriseName 検索する企業名
	 * @return reportEntity
	 */
	public ReportEntity getReportListByEnterpriseName(String enterpriseName) {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_ONE_BY_ENTERPRISENAME, enterpriseName);
		ReportEntity reportEntity = mappingSelectResult(resultList);
		return reportEntity;
	}

	/**
	 * 受験報告の状態で絞込検索を行う
	 * @param status 状態
	 * @return reportEntity
	 */
	public ReportEntity getReportListByStatus(String status) {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_ONE_BY_STATUS, status);
		ReportEntity reportEntity = mappingSelectResult(resultList);
		return reportEntity;
	}

}
