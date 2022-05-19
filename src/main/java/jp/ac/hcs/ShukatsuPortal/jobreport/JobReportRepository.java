package jp.ac.hcs.ShukatsuPortal.jobreport;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jp.ac.hcs.ShukatsuPortal.jobapp.JobAppForm;

/**
 * 就職活動報告情報のデータを管理する
 */

@Repository
public class JobReportRepository {

	/** SQL 報告1件追加 */
	private static final String SQL_UPDATE_ONE1 = "UPDATE APP SET REP_COMMENT = ?, REP_DATE = ?, APP_STATUS = 5 WHERE APP_ID = ?";

	/** SQL 報告1件の進めるをリセット */
	private static final String SQL_UPDATE_RESET = "UPDATE COMPANY SET APP_PROCEED = FALSE WHERE APP_ID = ?";

	/** SQL 報告1件の会社情報を追加 */
	private static final String SQL_UPDATE_ONE2 = "UPDATE COMPANY SET APP_PROCEED = TRUE WHERE APP_ID = ? AND APP_COMPANY = ?";

	/** SQL 報告詳細を取得 */
	private static final String SQL_REPORT_DETAIL = "SELECT * FROM COMPANY,APP WHERE COMPANY.APP_ID = APP.APP_ID AND COMPANY.APP_ID = ?";

	/** SQL 報告を差戻し */
	private static final String SQL_REPORT_REMAND = "UPDATE APP SET APP_STATUS = 6, INDICATE = ?, UPDATE_DATE = ? WHERE APP_ID = ?";

	/** SQL 報告を承認 */
	private static final String SQL_REPORT_APPROVAL = "UPDATE APP SET APP_STATUS = 7, UPDATE_DATE = ? WHERE APP_ID = ?";

	/** SQL 報告を取消 */
	private static final String SQL_REPORT_CANCEL = "UPDATE APP SET APP_STATUS = 99, UPDATE_DATE = ? WHERE APP_ID = ?";

	/** SQL 報告を変更 */
	private static final String SQL_REPORT_UPDATE = "UPDATE APP SET REP_COMMENT = ?, UPDATE_DATE = ?, APP_STATUS = 5 WHERE APP_ID = ?";

	@Autowired
	private JdbcTemplate jdbc;

	/**
	 * 報告の詳細を取得する
	 *
	 * @param appId 申請ID
	 * @return jobReportEntity
	 */
	public JobReportEntity getJobCanpany(String appId) {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_REPORT_DETAIL, appId);
		JobReportEntity jobReportEntity = mappingSelectCompany(resultList);
		return jobReportEntity;
	}

	/**
	 *Companyテーブルから取得したデータをJobReportEntity形式にマッピングする.
	 *
	 * @param resultList
	 * @return jobReportEntity
	 */
	private JobReportEntity mappingSelectCompany(List<Map<String, Object>> resultList) {
		JobReportEntity entity = new JobReportEntity();

		for (Map<String, Object> map : resultList) {
			JobReportData data = new JobReportData();
			data.setAppId((int) map.get("app_id"));
			data.setAppCompany((String) map.get("app_company"));
			data.setAppProceed((boolean) map.get("app_proceed"));
			entity.getJobReportList().add(data);
			entity.setRepComment((String) map.get("rep_comment"));
			entity.setAppStatus((int) map.get("app_status"));
			entity.setIndicate((String) map.get("indicate"));
		}
		return entity;
	}

	/**
	 * 報告情報を登録する
	 *
	 * @param form  報告登録用フォーム
	 * @param date  現在日時
	 * @param appId 申請ID
	 * @return rowNumber
	 */
	public int insertOne(JobReportForm form, Date date, String appId) throws DataAccessException {
		int rowNumber = jdbc.update(SQL_UPDATE_ONE1, form.getRepComment(), date, appId);
		rowNumber = jdbc.update(SQL_UPDATE_RESET, appId);
		for (int cnt = 0; cnt < form.getAppProceed().size(); cnt++) {
			rowNumber = jdbc.update(SQL_UPDATE_ONE2, appId, form.getAppProceed().get(cnt));
		}

		return rowNumber;
	}

	/**
	 * 報告差戻を登録する
	 *
	 * @param form 差戻用フォーム
	 * @param date 現在日時
	 * @return rowNumber
	 */
	public int remandOne(JobReportFormForRemand form, Date date) {
		int rowNumber = jdbc.update(SQL_REPORT_REMAND, form.getIndicate(), date, form.getAppId());
		return rowNumber;
	}

	/**
	 * 報告承認を登録する
	 *
	 * @param form 承認用フォーム
	 * @param date 現在日時
	 * @return rowNumber
	 */
	public int approvalOne(JobAppForm form, Date date) {
		int rowNumber = jdbc.update(SQL_REPORT_APPROVAL, date, form.getAppId());
		return rowNumber;
	}

	/**
	 * 報告取消を登録する
	 *
	 * @param form 取消用フォーム
	 * @param date 現在日時
	 * @return rowNumber
	 */
	public int cancelOne(JobReportForm form, Date date) {
		int rowNumber = jdbc.update(SQL_REPORT_CANCEL, date, form.getAppId());
		return rowNumber;
	}

	/**
	 * 報告の変更情報を登録する
	 *
	 * @param form  変更用フォーム
	 * @param date  現在日時
	 * @param appId 申請ID
	 * @return rowNumber
	 */
	public int updateOne(JobReportForm form, Date date, String appId) throws DataAccessException {
		int rowNumber = jdbc.update(SQL_REPORT_UPDATE, form.getRepComment(), date, appId);

		rowNumber = jdbc.update(SQL_UPDATE_RESET, appId);
		for (int cnt = 0; cnt < form.getAppProceed().size(); cnt++) {
			rowNumber = jdbc.update(SQL_UPDATE_ONE2, appId, form.getAppProceed().get(cnt));
		}

		return rowNumber;
	}

}
