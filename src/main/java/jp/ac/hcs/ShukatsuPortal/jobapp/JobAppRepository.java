package jp.ac.hcs.ShukatsuPortal.jobapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 就職活動申請情報のデータを管理する
 */
@Repository
public class JobAppRepository {

	/** SQL 全件取得（学生） */
	private static final String SQL_SELECT_STUDENT = "SELECT * FROM APP JOIN USERS ON USERS.BELONG_CLASS = APP.APPLY_CLASS AND USERS.STUDENT_NUMBER = APP.APPLY_NUMBER AND USERS.USER_ID = ? AND APP.APP_STATUS != 99 ORDER BY APP_START_DATE, APP_START_TIME";

	/** SQL 全件取得（担任） */
	private static final String SQL_SELECT_TEACHER = "SELECT * FROM APP A LEFT OUTER JOIN SUMMARY S ON A.APP_ID = S.APP_ID WHERE A.APP_STATUS NOT IN (7, 99) ORDER BY A.APP_START_DATE, A.APP_START_TIME";

	/** SQL 詳細取得 */
	private static final String SQL_DETAIL = "SELECT * FROM APP WHERE APP_ID = ?";

	/** SQL 会社名取得 */
	private static final String SQL_COMPANY = "SELECT * FROM COMPANY WHERE APP_ID = ?";

	/** SQL 申請1件追加 */
	private static final String SQL_INSERT_ONE = "INSERT INTO APP(SUMMARY_FLAG,DOCUMENT_FLAG, APP_START_DATE, APP_START_TIME,APP_FINISH_DATE, APP_FINISH_TIME,APP_ADDRESS,APP_CONTENTS,APP_CONTENT_OTHER,ABSENCE_FLAG,APP_START_ABSENCE,APP_FINISH_ABSENCE,LEAVE_FLAG,APP_LEAVE_DATE,APP_LEAVE_TIME,LATE_FLAG,APP_LATE_DATE,APP_LATE_TIME,APP_MEMO,APPLY_CLASS,APPLY_NUMBER,APPLY_NAME,APP_DATE)VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	/** SQL 申請会社追加 */
	private static final String SQL_INSERT_COMPANY = "INSERT INTO COMPANY(APP_ID,APP_COMPANY) VALUES((SELECT MAX(APP_ID) FROM APP),?);";

	/** SQL 申請報告リスト */
	private static final String SQL_REPORT_LIST = "SELECT * FROM APP WHERE APP_ID = ?";

	/** SQL コース担当連絡と申請状態を申請完了に更新 */
	private static final String SQL_UPDATE_APPROVAL_FLAG = "UPDATE APP SET APP_STATUS = 4, APPROVAL_FLAG = TRUE,INDICATE = NULL,UPDATE_DATE = ? WHERE APP_ID = ?";

	/** SQL 申請状態を申請承認済に変更 */
	private static final String SQL_UPDATE_APPROVAL = "UPDATE APP SET APP_STATUS = 3,UPDATE_DATE = ? WHERE APP_ID = ?";

	/** SQL 申請の差戻し */
	private static final String SQL_UPDATE_REMAND = "UPDATE APP SET APP_STATUS = 2,UPDATE_DATE = ?,INDICATE = ? WHERE APP_ID = ?";

	/** SQL 申請内容変更 */
	private static final String SQL_UPDATE_JOBAPP = "UPDATE APP SET APP_STATUS = 1 ,SUMMARY_FLAG = ?,DOCUMENT_FLAG= ?, APP_START_DATE= ?, APP_START_TIME= ?,APP_FINISH_DATE= ?, APP_FINISH_TIME= ?,APP_ADDRESS= ?,APP_CONTENTS= ?,APP_CONTENT_OTHER= ?,ABSENCE_FLAG= ?,APP_START_ABSENCE= ?,APP_FINISH_ABSENCE= ?,LEAVE_FLAG= ?,APP_LEAVE_DATE= ?,APP_LEAVE_TIME= ?,LATE_FLAG= ?,APP_LATE_DATE= ?,APP_LATE_TIME= ?,APP_MEMO= ?,APPLY_CLASS= ?,APPLY_NUMBER= ?,APP_DATE= ? WHERE APP_ID = ?";

	/** SQL 申請とりまとめマスタ登録 */
	private static final String SQL_INSERT_SUMMARY = "INSERT INTO SUMMARY (APP_ID)  VALUES (SELECT MAX(APP_ID) FROM APP)";

	/** SQL 申請とりまとめ書類登録 */
	private static final String SQL_INSERT_CERTIFICATE = "INSERT INTO CERTIFICATE (SUMMARY_ID,DOCUMENT_ID,DEADLINE) VALUES ((SELECT MAX(SUMMARY_ID) FROM SUMMARY),?,?)";

	/** SQL 申請内容変更 書類情報 */
	private static final String SQL_SELECT_DOCUMENT = "SELECT * FROM CERTIFICATE WHERE SUMMARY_ID = (SELECT SUMMARY_ID FROM SUMMARY WHERE APP_ID = ?)";

	/** SQL 報告を取消 */
	private static final String SQL_REPORT_CANCEL = "UPDATE APP SET APP_STATUS = 99, UPDATE_DATE = ? WHERE APP_ID = ?";

	/** SQL 通知押下時間更新 */
	private static final String SQL_UPDATE_NOTIFICATE_DATE = "UPDATE LISTPUSH SET NOTIFICATE_DATE = CURRENT_TIMESTAMP WHERE NOTIFICATE_ID = ? AND USER_ID = ?";

	/** SQL 通知押下時間以降の申請全件取得 (担任:申請承認待ち） */
	private static final String SQL_SELECT_UPDATE_APP_LIST = "SELECT * FROM APP A, USERS U, NOTIFICATE N, LISTPUSH L WHERE A.APP_DATE BETWEEN L.NOTIFICATE_DATE AND CURRENT_TIMESTAMP AND U.BELONG_CLASS = A.APPLY_CLASS AND A.APP_STATUS = 1 AND U.USER_ID = L.USER_ID AND N.NOTIFICATE_ID = L.NOTIFICATE_ID AND L.USER_ID = ? AND L.NOTIFICATE_ID = ?";

	/** SQL 通知押下時間以降の申請全件取得 (担任:報告承認待ち） */
	private static final String SQL_SELECT_UPDATE_REPORT_LIST = "SELECT * FROM APP A, USERS U, NOTIFICATE N, LISTPUSH L WHERE A.REP_DATE BETWEEN L.NOTIFICATE_DATE AND CURRENT_TIMESTAMP AND U.BELONG_CLASS = A.APPLY_CLASS AND A.APP_STATUS = 5 AND U.USER_ID = L.USER_ID AND N.NOTIFICATE_ID = L.NOTIFICATE_ID AND L.USER_ID = ? AND L.NOTIFICATE_ID = ?";

	/** SQL 通知押下時間以降の申請全件取得 (担任:今日活動の申請完了の申請） */
	private static final String SQL_SELECT_COMP_EVENT_LIST = "SELECT * FROM APP A, USERS U WHERE A.APP_START_DATE = CAST(CURRENT_TIMESTAMP as date) AND U.BELONG_CLASS = A.APPLY_CLASS AND U.USER_ID = ?";

	/** SQL 通知押下時間以降の申請全件取得 (担任:コース担当への連絡完了） */
	private static final String SQL_SELECT_COURSE_LIST = "SELECT * FROM APP A, USERS U, NOTIFICATE N, LISTPUSH L WHERE A.UPDATE_DATE BETWEEN L.NOTIFICATE_DATE AND CURRENT_TIMESTAMP AND U.BELONG_CLASS = A.APPLY_CLASS AND A.APP_STATUS = 4 AND U.USER_ID = L.USER_ID AND N.NOTIFICATE_ID = L.NOTIFICATE_ID AND L.USER_ID = ? AND L.NOTIFICATE_ID = ?";

	/** SQL 通知押下時間以降の申請全件取得 (学生:差戻しされた申請） */
	private static final String SQL_SELECT_REMAND_APP_LIST = "SELECT * FROM APP A, USERS U, NOTIFICATE N, LISTPUSH L WHERE A.UPDATE_DATE BETWEEN L.NOTIFICATE_DATE AND CURRENT_TIMESTAMP AND U.BELONG_CLASS = A.APPLY_CLASS AND U.STUDENT_NUMBER = A.APPLY_NUMBER AND A.APP_STATUS = 2 AND U.USER_ID = L.USER_ID AND N.NOTIFICATE_ID = L.NOTIFICATE_ID AND L.USER_ID = ? AND L.NOTIFICATE_ID = ?";

	/** SQL 通知押下時間以降の申請全件取得 (学生:承認された申請） */
	private static final String SQL_SELECT_APPROVAL_APP_LIST = "SELECT * FROM APP A, USERS U, NOTIFICATE N, LISTPUSH L WHERE A.UPDATE_DATE BETWEEN L.NOTIFICATE_DATE AND CURRENT_TIMESTAMP AND U.BELONG_CLASS = A.APPLY_CLASS AND U.STUDENT_NUMBER = A.APPLY_NUMBER AND A.APP_STATUS = 3 AND U.USER_ID = L.USER_ID AND N.NOTIFICATE_ID = L.NOTIFICATE_ID AND L.USER_ID = ? AND L.NOTIFICATE_ID = ?";

	/** SQL 通知押下時間以降の申請全件取得 (学生:差戻しされた報告） */
	private static final String SQL_SELECT_REMAND_REPORT_LIST = "SELECT * FROM APP A, USERS U, NOTIFICATE N, LISTPUSH L WHERE A.UPDATE_DATE BETWEEN L.NOTIFICATE_DATE AND CURRENT_TIMESTAMP AND U.BELONG_CLASS = A.APPLY_CLASS AND U.STUDENT_NUMBER = A.APPLY_NUMBER AND A.APP_STATUS = 6 AND U.USER_ID = L.USER_ID AND N.NOTIFICATE_ID = L.NOTIFICATE_ID AND L.USER_ID = ? AND L.NOTIFICATE_ID = ?";

	/** SQL 通知押下時間以降の申請全件取得 (学生:承認された報告） */
	private static final String SQL_SELECT_APPROVAL_REPORT_LIST = "SELECT * FROM APP A, USERS U, NOTIFICATE N, LISTPUSH L WHERE A.UPDATE_DATE BETWEEN L.NOTIFICATE_DATE AND CURRENT_TIMESTAMP AND U.BELONG_CLASS = A.APPLY_CLASS AND U.STUDENT_NUMBER = A.APPLY_NUMBER AND A.APP_STATUS = 7 AND U.USER_ID = L.USER_ID AND N.NOTIFICATE_ID = L.NOTIFICATE_ID AND L.USER_ID = ? AND L.NOTIFICATE_ID = ?";

	/** SQL 通知押下時間以降の申請全件取得 (学生:直近にイベントのある未完了の申請） */
	private static final String SQL_SELECT_EVENT_INCOMP_LIST = "SELECT * FROM APP A, USERS U, NOTIFICATE N, LISTPUSH L WHERE CAST(CURRENT_TIMESTAMP as date) BETWEEN CAST(dateadd(day,-3,A.APP_START_DATE) as date) AND APP_START_DATE AND A.APP_DATE BETWEEN L.NOTIFICATE_DATE AND CURRENT_TIMESTAMP AND U.BELONG_CLASS = A.APPLY_CLASS AND U.STUDENT_NUMBER = A.APPLY_NUMBER AND A.APP_STATUS <= 3 AND U.USER_ID = L.USER_ID AND N.NOTIFICATE_ID = L.NOTIFICATE_ID AND L.USER_ID = ? AND L.NOTIFICATE_ID = ?";

	/** SQL 通知押下時間以降の申請全件取得 (学生:イベント翌日に報告未作成の申請） */
	private static final String SQL_SELECT_NOTSUBMITTED_LIST = "SELECT * FROM APP A, USERS U, NOTIFICATE N, LISTPUSH L WHERE CAST(CURRENT_TIMESTAMP as date) = CAST(dateadd(day,1,A.APP_START_DATE) as date) AND A.UPDATE_DATE BETWEEN L.NOTIFICATE_DATE AND CURRENT_TIMESTAMP AND U.BELONG_CLASS = A.APPLY_CLASS AND U.STUDENT_NUMBER = A.APPLY_NUMBER AND A.APP_STATUS = 4 AND U.USER_ID = L.USER_ID AND N.NOTIFICATE_ID = L.NOTIFICATE_ID AND L.USER_ID = ? AND L.NOTIFICATE_ID = ?";

	/** SQL 通知押下時間以降の申請全件取得 (学生:直近にイベントのある申請） */
	private static final String SQL_SELECT_EVENT_LIST = "SELECT * FROM APP A, USERS U WHERE CAST(CURRENT_TIMESTAMP as date) BETWEEN CAST(dateadd(day,-1,A.APP_START_DATE) as date) AND A.APP_START_DATE AND U.BELONG_CLASS = A.APPLY_CLASS AND U.STUDENT_NUMBER = A.APPLY_NUMBER AND U.USER_ID = ?";

	/** SQL 通知押下時間以降の申請全件取得 (学生:とりまとめ書類提出のある申請） */
	private static final String SQL_SELECT_DOCUMENT_LIST = "SELECT * FROM APP A, USERS U, NOTIFICATE N, LISTPUSH L WHERE A.APP_DATE BETWEEN L.NOTIFICATE_DATE AND CURRENT_TIMESTAMP AND U.BELONG_CLASS = A.APPLY_CLASS AND U.STUDENT_NUMBER = A.APPLY_NUMBER AND A.APP_STATUS = 1 AND A.DOCUMENT_FLAG = TRUE AND U.USER_ID = L.USER_ID AND N.NOTIFICATE_ID = L.NOTIFICATE_ID AND L.USER_ID = ? AND L.NOTIFICATE_ID = ?";

	/** SQL 通知押下時間以降の申請全件取得 (学生:書類提出締め切り直前で未提出の書類のある申請） */
	private static final String SQL_SELECT_DOCUMENT_LIMIT_LIST = "SELECT * FROM APP A, USERS U ,SUMMARY S, CERTIFICATE C, NOTIFICATE N, LISTPUSH L WHERE CAST(CURRENT_TIMESTAMP as date) BETWEEN CAST(dateadd(day,-1,C.DEADLINE) as date) AND DEADLINE AND A.APP_DATE BETWEEN L.NOTIFICATE_DATE AND CURRENT_TIMESTAMP AND U.BELONG_CLASS = A.APPLY_CLASS AND U.STUDENT_NUMBER = A.APPLY_NUMBER AND DOCUMENT_FLAG = TRUE AND C.STATUS = FALSE AND U.USER_ID = L.USER_ID AND N.NOTIFICATE_ID = L.NOTIFICATE_ID AND L.USER_ID = ? AND L.NOTIFICATE_ID = ? AND A.APP_ID = S.APP_ID AND S.SUMMARY_ID = C.SUMMARY_ID";

	/** SQL とりまとめ書類更新 */
	private static final String SQL_UPDATE_DOCUMENT = "UPDATE CERTIFICATE SET STATUS = TRUE WHERE SUMMARY_ID = ? AND DOCUMENT_ID = ?";

	/** SQL 申請会社変更 */
	private static final String SQL_UPDATE_COMPANY = "INSERT INTO COMPANY(APP_ID,APP_COMPANY) VALUES(?,?)";

	/** SQL 申請会社削除 */
	private static final String SQL_DELETE_COMPANY = "DELETE FROM COMPANY WHERE APP_ID = ?";

	/** SQLとりまとめ書類削除 */
	private static final String SQL_DELETE_CERTIFICATE = "DELETE FROM CERTIFICATE WHERE SUMMARY_ID = (SELECT SUMMARY_ID FROM SUMMARY WHERE APP_ID = ?)";

	/** SQL 申請とりまとめ書類変更 */
	private static final String SQL_UPDATE_CERTIFICATE = "INSERT INTO CERTIFICATE (SUMMARY_ID,DOCUMENT_ID,DEADLINE) VALUES ((SELECT SUMMARY_ID FROM SUMMARY WHERE APP_ID = ?),?,?)";

	/** SQL とりまとめ名簿登録済フラグ変更 */
	private static final String SQL_UPDATE_SUMMARY = "UPDATE SUMMARY SET REGISTER_FLAG = TRUE WHERE SUMMARY_ID = (SELECT S.SUMMARY_ID FROM APP A, SUMMARY S WHERE A.APP_ID = S.APP_ID AND A.APP_ID = ?)";

	/** SQL 通知リストから一覧情報取得 */
	private static final String SQL_NOTIFICATIONLIST = "SELECT * FROM APP WHERE APP_ID = ?";

	@Autowired
	private JdbcTemplate jdbc;

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	/**
	 * 就職活動申請を全権取得する(学生)
	 *
	 * @param userId ユーザID
	 * @return jobAppEntity
	 */
	public JobAppEntity getJobAppListStudent(String userId) throws DataAccessException {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_STUDENT, userId);
		JobAppEntity jobAppEntity = mappingSelectResultList(resultList);
		return jobAppEntity;
	}

	/**
	 * 就職活動申請を全権取得する(担任)
	 *
	 * @return jobAppEntity
	 */
	public JobAppEntity getJobAppListTeacher() throws DataAccessException {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_TEACHER);
		JobAppEntity jobAppEntity = mappingSelectResultList(resultList);
		return jobAppEntity;
	}

	/**
	 * 選択された申請の詳細を取得する
	 *
	 * @param appId 申請ID
	 * @return jobAppData
	 */
	public JobAppData getJobAppDetail(String appId) throws DataAccessException {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_DETAIL, appId);
		JobAppEntity jobAppEntity = mappingSelectResultDetail(resultList);
		// 必ず1件のみのため、最初のJobAppDataを取り出す
		JobAppData data = jobAppEntity.getJobAppList().get(0);
		return data;
	}

	/**
	 * 選択された申請の企業を取得する。
	 *
	 * @param appId 申請ID
	 * @return jobAppEntity
	 */
	public JobAppEntity getJobAppCompany(String appId) throws DataAccessException {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_COMPANY, appId);
		JobAppEntity jobAppEntity = mappingSelectResultCompany(resultList);
		return jobAppEntity;
	}

	/**
	 * (一覧用）Appテーブルから取得したデータをJobAppEntity形式にマッピングする.
	 *
	 * @param resultList Appテーブルから取得したデータ
	 * @return JobAppEntity
	 */
	private JobAppEntity mappingSelectResultList(List<Map<String, Object>> resultList) {
		JobAppEntity entity = new JobAppEntity();

		for (Map<String, Object> map : resultList) {
			JobAppData data = new JobAppData();
			data.setAppId((int) map.get("app_id"));
			data.setAppStatus((int) map.get("app_status"));
			data.setSummaryFlag((boolean) map.get("summary_flag"));
			data.setAppStartDate(formatDate(map.get("app_start_date")));
			data.setAppFinishDate(formatDate(map.get("app_finish_date")));
			data.setApplyClass((String) map.get("apply_class"));
			data.setApplyNumber((int) map.get("apply_number"));
			data.setApplyName((String) map.get("apply_name"));
			// DB内のデータがとりまとめに登録されていない状態ならfalse
			if (map.get("register_flag") == null) {
				data.setRegisterFlag(false);
			} else {
				data.setRegisterFlag((boolean) map.get("register_flag"));
			}
			entity.getJobAppList().add(data);
		}

		return entity;

	}

	/**
	 * (詳細用）Appテーブルから取得したデータをJobAppEntity形式にマッピングする.
	 *
	 * @param resultList Appテーブルから取得したデータ
	 * @return JobAppEntity
	 */
	private JobAppEntity mappingSelectResultDetail(List<Map<String, Object>> resultList) {
		JobAppEntity entity = new JobAppEntity();

		for (Map<String, Object> map : resultList) {
			JobAppData data = new JobAppData();
			data.setAppId((int) map.get("app_id"));
			data.setAppStatus((int) map.get("app_status"));
			data.setSummaryFlag((boolean) map.get("summary_flag"));
			data.setDocumentFlag((boolean) map.get("document_flag"));
			data.setAppStartDate(formatDate(map.get("app_start_date")));
			data.setAppStartTime(formatTime(map.get("app_start_time")));
			data.setAppFinishDate(formatDate(map.get("app_finish_date")));
			data.setAppFinishTime(formatTime(map.get("app_finish_time")));
			data.setApplyClass((String) map.get("apply_class"));
			data.setApplyNumber((int) map.get("apply_number"));
			data.setAppAddress((String) map.get("app_address"));
			data.setApplyName((String) map.get("apply_name"));
			data.setAppContents((int) map.get("app_contents"));
			data.setAbsenceFlag((boolean) map.get("absence_flag"));
			data.setAppStartAbsence(formatDate(map.get("app_start_absence")));
			data.setAppFinishAbsence(formatDate(map.get("app_finish_absence")));
			data.setLeaveFlag((boolean) map.get("leave_flag"));
			data.setAppLeaveDate(formatDate(map.get("app_leave_date")));
			data.setAppLeaveTime(formatTime(map.get("app_leave_time")));
			data.setLateFlag((boolean) map.get("late_flag"));
			data.setAppLateDate(formatDate(map.get("app_late_date")));
			data.setAppLateTime(formatTime(map.get("app_late_time")));
			data.setAppDate((Date) map.get("app_date"));
			data.setAppMemo((String) map.get("app_memo"));
			data.setIndicate((String) map.get("indicate"));
			entity.getJobAppList().add(data);
		}

		return entity;

	}

	/**
	 * 受験企業テーブルから取得したデータをJobAppEntity形式にマッピングする
	 *
	 * @param resultList companyテーブルから取得したデータ
	 * @return JobAppEntity
	 */
	private JobAppEntity mappingSelectResultCompany(List<Map<String, Object>> resultList) {
		JobAppEntity entity = new JobAppEntity();

		for (Map<String, Object> map : resultList) {
			entity.getJobAppCompanys().add((String) map.get("app_company"));
		}

		return entity;

	}

	/**
	 * 検索条件からリストを作成する(学生)
	 *
	 * @param search 検索内容フォーム
	 * @param userId ユーザID
	 * @return jobAppEntity
	 */
	public JobAppEntity searchJobAppListStudent(JobAppFormForSearch search, String userId) throws DataAccessException {
		// SQL 検索ベースコード
		String SQL_SEARCH = "SELECT * FROM APP WHERE (APP.APPLY_CLASS, APP.APPLY_NUMBER) = (SELECT USERS.BELONG_CLASS, USERS.STUDENT_NUMBER FROM USERS WHERE USERS.USER_ID = :userid ) ";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("userid", userId);

		String appCompany = search.getAppCompany();
		String appStartDate = search.getAppStartDate();
		String appFinishDate = search.getAppFinishDate();
		int appStatus = search.getAppStatus();

		// 会社名が入力された場合
		if (appCompany != "") {
			appCompany = "%" + appCompany + "%";
			SQL_SEARCH += "AND APP.APP_ID IN (SELECT COMPANY.APP_ID FROM COMPANY WHERE COMPANY.APP_COMPANY LIKE :appCompany ) ";
			parameters.put("appCompany", appCompany);
		}
		// 開始日が入力された場合
		if (appStartDate != "") {
			SQL_SEARCH += "AND APP.APP_START_DATE = :appStartDate ";
			parameters.put("appStartDate", appStartDate);
		}
		// 終了日が入力された場合
		if (appFinishDate != "") {
			SQL_SEARCH += "AND APP.APP_FINISH_DATE = :appFinishDate ";
			parameters.put("appFinishDate", appFinishDate);
		}
		// 申請状態が選択された場合
		if (appStatus != 0) {
			SQL_SEARCH += "AND APP.APP_STATUS = :appStatus ";
			parameters.put("appStatus", appStatus);
		}
		SQL_SEARCH += "ORDER BY APP.APP_START_DATE, APP.APP_START_TIME";
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(SQL_SEARCH, parameters);
		JobAppEntity jobAppEntity = mappingSelectResultList(resultList);

		return jobAppEntity;

	}

	/**
	 * 検索条件からリストを作成する(担任)
	 *
	 * @param search 検索内容フォーム
	 * @return jobAppEntity
	 */
	public JobAppEntity searchJobAppListTeacher(JobAppFormForSearch search) throws DataAccessException {
		// SQL 検索ベースコード
		String SQL_SEARCH = "SELECT * FROM APP WHERE TRUE = TRUE ";
		Map<String, Object> parameters = new HashMap<String, Object>();

		String applyClass = search.getApplyClass();
		int applyNumber = search.getApplyNumber();
		String applyName = search.getApplyName();
		String appCompany = search.getAppCompany();
		String appStartDate = search.getAppStartDate();
		String appFinishDate = search.getAppFinishDate();
		int appStatus = search.getAppStatus();

		// クラスが選択された場合
		if (applyClass != "") {
			SQL_SEARCH += "AND APP.APPLY_CLASS = :applyClass ";
			parameters.put("applyClass", applyClass);
		}
		// 番号が選択された場合
		if (applyNumber != 0) {
			SQL_SEARCH += "AND APP.APPLY_NUMBER = :applyNumber ";
			parameters.put("applyNumber", applyNumber);
		}
		// 名前が入力された場合
		if (applyName != "") {
			applyName = "%" + applyName + "%";
			SQL_SEARCH += "AND APP.APPLY_NAME LIKE :applyName ";
			parameters.put("applyName", applyName);
		}
		// 会社名が入力された場合
		if (appCompany != "") {
			appCompany = "%" + appCompany + "%";
			SQL_SEARCH += "AND APP.APP_ID IN (SELECT COMPANY.APP_ID FROM COMPANY WHERE COMPANY.APP_COMPANY LIKE :appCompany ) ";
			parameters.put("appCompany", appCompany);
		}
		// 開始日が入力された場合
		if (appStartDate != "") {
			SQL_SEARCH += "AND APP.APP_START_DATE = :appStartDate ";
			parameters.put("appStartDate", appStartDate);
		}
		// 終了日が入力された場合
		if (appFinishDate != "") {
			SQL_SEARCH += "AND APP.APP_FINISH_DATE = :appFinishDate ";
			parameters.put("appFinishDate", appFinishDate);
		}
		// 申請状態が選択された場合
		if (appStatus != 0) {
			SQL_SEARCH += "AND APP.APP_STATUS = :appStatus ";
			parameters.put("appStatus", appStatus);
		}

		SQL_SEARCH += "ORDER BY APP.APP_START_DATE, APP.APP_START_TIME";
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(SQL_SEARCH, parameters);
		JobAppEntity jobAppEntity = mappingSelectResultList(resultList);

		return jobAppEntity;

	}

	/**
	 * 申請を新規作成登録する
	 *
	 * @param jobAppData 登録する申請データ
	 * @return rowNumber
	 * @throws DataAccessException データベースアクセスエラー
	 */
	public int insertJobApp(JobAppData jobAppData) throws DataAccessException {
		int rowNumber = -1;
		// 申請テーブルに登録
		rowNumber = jdbc.update(SQL_INSERT_ONE, jobAppData.isSummaryFlag(), jobAppData.isDocumentFlag(),
				jobAppData.getAppStartDate(), jobAppData.getAppStartTime(), jobAppData.getAppFinishDate(),
				jobAppData.getAppFinishTime(), jobAppData.getAppAddress(), jobAppData.getAppContents(),
				jobAppData.getAppContentOther(), jobAppData.isAbsenceFlag(), jobAppData.getAppStartAbsence(),
				jobAppData.getAppFinishAbsence(), jobAppData.isLeaveFlag(), jobAppData.getAppLeaveDate(),
				jobAppData.getAppLeaveTime(), jobAppData.isLateFlag(), jobAppData.getAppLateDate(),
				jobAppData.getAppLateTime(), jobAppData.getAppMemo(), jobAppData.getApplyClass(),
				jobAppData.getApplyNumber(), jobAppData.getApplyName(), jobAppData.getAppDate());

		// 会社テーブルに登録
		for (int i = 0; i < jobAppData.getAppCompany().size(); i++) {
			rowNumber = jdbc.update(SQL_INSERT_COMPANY, jobAppData.getAppCompany().get(i));
		}

		// とりまとめフラグON
		if (jobAppData.isSummaryFlag()) {
			// とりまとめマスタテーブルに登録
			rowNumber = jdbc.update(SQL_INSERT_SUMMARY);
			// 書類提出フラグON
			if (jobAppData.isDocumentFlag()) {
				List<Integer> documentId = new ArrayList<Integer>();
				// 履歴書
				if (jobAppData.isResume()) {
					documentId.add(1);
				}
				// 推薦書
				if (jobAppData.isRecommendation()) {
					documentId.add(2);
				}
				// 大学証明書
				if (jobAppData.isUniversityCrtificate()) {
					documentId.add(3);
				}
				// HCS成績証明書
				if (jobAppData.isHcsTranscript()) {
					documentId.add(4);
				}
				// HCS卒業見込証明書
				if (jobAppData.isHcsCrtificate()) {
					documentId.add(5);
				}
				// 健康診断証明書
				if (jobAppData.isHealthCheckCertificate()) {
					documentId.add(6);
				}
				// 出身高校成績証明書
				if (jobAppData.isHighscoolTranscript()) {
					documentId.add(7);
				}
				// 大学成績証明書
				if (jobAppData.isUniversityTranscript()) {
					documentId.add(8);
				}
				// 個人情報等同意書
				if (jobAppData.isPersonalInfo()) {
					documentId.add(9);
				}

				for (int i = 0; i < documentId.size(); i++) {
					// 取りまとめ書類テーブルに登録
					rowNumber = jdbc.update(SQL_INSERT_CERTIFICATE, documentId.get(i), jobAppData.getDeadline());
				}
			}
		}

		return rowNumber;

	}

	/**
	 * AppテーブルからappIdをキーにデータを全件取得し、CSVファイルとしてサーバに保存する.
	 *
	 * @param appId 申請ID
	 * @throws DataAccessException データベースアクセスエラー
	 */
	public void applistCsvOut(String appId) throws DataAccessException {
		// CSVファイル出力用設定
		JobAppRowCallbackHandler handler = new JobAppRowCallbackHandler();
		jdbc.query(SQL_REPORT_LIST, handler, appId);
	}

	/**
	 * SQLから取得した日付をString型に変換する
	 *
	 * @param ob SQLから取得したデータ
	 * @return str 文字に変換した日付
	 */
	public String formatDate(Object ob) {
		SimpleDateFormat dtF = new SimpleDateFormat("yyyy-MM-dd");
		if (ob == null) {
			return null;
		} else {
			String str = dtF.format(ob);
			return str;
		}

	}

	/**
	 * SQLから取得した時刻をString型に変換する
	 *
	 * @param ob SQLから取得したデータ
	 * @return str 文字に変換した時刻
	 */
	public String formatTime(Object ob) {
		SimpleDateFormat dtT = new SimpleDateFormat("HH:mm");
		if (ob == null) {
			return null;
		} else {
			String str = dtT.format(ob);
			return str;
		}
	}


	/**
	 * とりまとめ検索を行う(学生)
	 *
	 * @param search 検索内容フォーム
	 * @param userId ユーザID
	 * @return JobAppEntity
	 */
	public JobAppEntity searchSummaryStudent(JobAppFormForSearch search, String userId) throws DataAccessException {
		// SQL 検索ベースコード
		String SQL_SEARCH = "SELECT * FROM APP WHERE (APP.APPLY_CLASS, APP.APPLY_NUMBER) = (SELECT USERS.BELONG_CLASS, USERS.STUDENT_NUMBER FROM USERS WHERE USERS.USER_ID = :userid ) AND APP.SUMMARY_FLAG = TRUE ";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("userid", userId);

		String appCompany = search.getAppCompany();
		String appStartDate = search.getAppStartDate();
		String appFinishDate = search.getAppFinishDate();
		int appStatus = search.getAppStatus();

		// 会社名が入力された場合
		if (appCompany != "") {
			appCompany = "%" + appCompany + "%";
			SQL_SEARCH += "AND APP.APP_ID IN (SELECT COMPANY.APP_ID FROM COMPANY WHERE COMPANY.APP_COMPANY LIKE :appCompany) ";
			parameters.put("appCompany", appCompany);
		}
		// 開始日が入力された場合
		if (appStartDate != "") {
			SQL_SEARCH += "AND APP.APP_START_DATE = :appStartDate ";
			parameters.put("appStartDate", appStartDate);
		}
		// 終了日が入力された場合
		if (appFinishDate != "") {
			SQL_SEARCH += "AND APP.APP_FINISH_DATE = :appFinishDate ";
			parameters.put("appFinishDate", appFinishDate);
		}
		// 申請状態が選択された場合
		if (appStatus != 0) {
			SQL_SEARCH += "AND APP.APP_STATUS = :appStatus ";
			parameters.put("appStatus", appStatus);
		}

		SQL_SEARCH += "ORDER BY APP.APP_START_DATE, APP_START_TIME";
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(SQL_SEARCH, parameters);
		JobAppEntity jobAppEntity = mappingSelectResultList(resultList);

		return jobAppEntity;
	}

	/**
	 * とりまとめ検索を行う(担任)
	 *
	 * @param search 検索内容フォーム
	 * @return JobAppEntity
	 */
	public JobAppEntity searchSummaryTeacher(JobAppFormForSearch search) throws DataAccessException {
		// SQL 検索ベースコード
		String SQL_SEARCH = "SELECT * FROM APP WHERE APP.SUMMARY_FLAG = TRUE ";
		Map<String, Object> parameters = new HashMap<String, Object>();

		String applyClass = search.getApplyClass();
		int applyNumber = search.getApplyNumber();
		String applyName = search.getApplyName();
		String appCompany = search.getAppCompany();
		String appStartDate = search.getAppStartDate();
		String appFinishDate = search.getAppFinishDate();
		int appStatus = search.getAppStatus();

		// クラスが選択されていた場合
		if (applyClass != "") {
			SQL_SEARCH += "AND APP.APPLY_CLASS = :applyClass ";
			parameters.put("applyClass", applyClass);
		}
		// 番号が選択されていた場合
		if (applyNumber != 0) {
			SQL_SEARCH += "AND APP.APPLY_NUMBER = :applyNumber ";
			parameters.put("applyNumber", applyNumber);
		}
		// 氏名が入力された場合
		if (applyName != "") {
			applyName = "%" + applyName + "%";
			SQL_SEARCH += "AND APP.APPLY_NAME LIKE :applyName ";
			parameters.put("applyName", applyName);
		}
		// 会社名が入力された場合
		if (appCompany != "") {
			appCompany = "%" + appCompany + "%";
			SQL_SEARCH += "AND APP.APP_ID IN (SELECT COMPANY.APP_ID FROM COMPANY WHERE COMPANY.APP_COMPANY LIKE :appCompany) ";
			parameters.put("appCompany", appCompany);
		}
		// 開始日が入力された場合
		if (appStartDate != "") {
			SQL_SEARCH += "AND APP.APP_START_DATE = :appStartDate ";
			parameters.put("appStartDate", appStartDate);
		}
		// 終了日が入力された場合
		if (appFinishDate != "") {
			SQL_SEARCH += "AND APP.APP_FINISH_DATE = :appFinishDate ";
			parameters.put("appFinishDate", appFinishDate);
		}
		// 申請状態が選択された場合
		if (appStatus != 0) {
			SQL_SEARCH += "AND APP.APP_STATUS = :appStatus ";
			parameters.put("appStatus", appStatus);
		}

		SQL_SEARCH += "ORDER BY APP.APP_START_DATE, APP_START_TIME";
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(SQL_SEARCH, parameters);
		JobAppEntity jobAppEntity = mappingSelectResultList(resultList);

		return jobAppEntity;
	}

	/**
	 * 申請のコース担当連絡登録と申請状態を申請完了にする
	 *
	 * @param appId 申請ID
	 * @param date  現在日時
	 * @return result
	 */
	public int updateApprovalFlag(String appId, Date date) {
		int result = jdbc.update(SQL_UPDATE_APPROVAL_FLAG, date, appId);

		return result;
	}

	/**
	 * 申請状態を承認済みにする
	 *
	 * @param appId 申請ID
	 * @param date  現在日時
	 * @return result
	 */
	public int updateApproval(String appId, Date date) {
		int result = jdbc.update(SQL_UPDATE_APPROVAL, date, appId);

		return result;
	}

	/**
	 * 申請状態を差戻にし、指摘コメントを更新する
	 *
	 * @param appId 申請ID
	 * @param date  現在日時
	 * @param indicate 指摘コメント
	 * @return result
	 */
	public int updateRemand(String appId, Date date, String indicate) {
		int result = jdbc.update(SQL_UPDATE_REMAND, date, indicate, appId);

		return result;
	}

	/**
	 * 申請を更新データで更新する
	 *
	 * @param appId      申請ID
	 * @param jobAppData 申請更新データ
	 * @param date       現在日時
	 * @return rowNumber
	 */
	public int updateJobApp(String appId, JobAppData jobAppData, Date date) {
		int rowNumber = jdbc.update(SQL_UPDATE_JOBAPP, jobAppData.isSummaryFlag(), jobAppData.isDocumentFlag(),
				jobAppData.getAppStartDate(), jobAppData.getAppStartTime(), jobAppData.getAppFinishDate(),
				jobAppData.getAppFinishTime(), jobAppData.getAppAddress(), jobAppData.getAppContents(),
				jobAppData.getAppContentOther(), jobAppData.isAbsenceFlag(), jobAppData.getAppStartAbsence(),
				jobAppData.getAppFinishAbsence(), jobAppData.isLeaveFlag(), jobAppData.getAppLeaveDate(),
				jobAppData.getAppLeaveTime(), jobAppData.isLateFlag(), jobAppData.getAppLateDate(),
				jobAppData.getAppLateTime(), jobAppData.getAppMemo(), jobAppData.getApplyClass(),
				jobAppData.getApplyNumber(), date, appId);

		// 変更前会社の削除
		rowNumber = jdbc.update(SQL_DELETE_COMPANY, appId);
		// 変更後会社の追加
		for (String updateCompany : jobAppData.getAppCompany()) {
			rowNumber = jdbc.update(SQL_UPDATE_COMPANY, appId, updateCompany);
		}

		// とりまとめフラグON
		if (jobAppData.isSummaryFlag()) {
			// 書類提出フラグON
			if (jobAppData.isDocumentFlag()) {
				// 書類削除
				rowNumber = jdbc.update(SQL_DELETE_CERTIFICATE, appId);
				List<Integer> documentId = new ArrayList<Integer>();
				// 履歴書
				if (jobAppData.isResume()) {
					documentId.add(1);
				}
				// 推薦書
				if (jobAppData.isRecommendation()) {
					documentId.add(2);
				}
				// 大学証明書
				if (jobAppData.isUniversityCrtificate()) {
					documentId.add(3);
				}
				// HCS成績証明書
				if (jobAppData.isHcsTranscript()) {
					documentId.add(4);
				}
				// HCS卒業見込証明書
				if (jobAppData.isHcsCrtificate()) {
					documentId.add(5);
				}
				// 健康診断証明書
				if (jobAppData.isHealthCheckCertificate()) {
					documentId.add(6);
				}
				// 出身高校成績証明書
				if (jobAppData.isHighscoolTranscript()) {
					documentId.add(7);
				}
				// 大学成績証明書
				if (jobAppData.isUniversityTranscript()) {
					documentId.add(8);
				}
				// 個人情報等同意書
				if (jobAppData.isPersonalInfo()) {
					documentId.add(9);
				}
				for (int i = 0; i < documentId.size(); i++) {
					// 取りまとめ書類テーブルに登録
					rowNumber = jdbc.update(SQL_UPDATE_CERTIFICATE, appId, documentId.get(i), jobAppData.getDeadline());
				}
			}
		}

		return rowNumber;
	}

	/**
	 * 申請詳細変更の書類情報を取得する
	 *
	 * @param appId 申請ID
	 * @return jobAppDocument
	 */
	public JobAppData selectDocument(String appId) {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_DOCUMENT, appId);
		JobAppData jobAppDocument = mappingSelectDocument(resultList);
		return jobAppDocument;
	}

	/**
	 * とりまとめ書類テーブルから取得したデータをJobAppData形式にマッピングする
	 *
	 * @param resultList SQLの結果
	 * @return data
	 */
	private JobAppData mappingSelectDocument(List<Map<String, Object>> resultList) {
		JobAppData data = new JobAppData();
		for (Map<String, Object> map : resultList) {
			data.setDeadline(formatDate(map.get("deadline")));
			// 履歴書
			if ((int) map.get("document_id") == 1) {
				data.setResume(true);
				// 推薦書
			} else if ((int) map.get("document_id") == 2) {
				data.setRecommendation(true);
				// 大学証明書
			} else if ((int) map.get("document_id") == 3) {
				data.setUniversityCrtificate(true);
				// HCS成績証明書
			} else if ((int) map.get("document_id") == 4) {
				data.setHcsTranscript(true);
				// HCS卒業見込証明書
			} else if ((int) map.get("document_id") == 5) {
				data.setHcsCrtificate(true);
				// 健康診断証明書
			} else if ((int) map.get("document_id") == 6) {
				data.setHealthCheckCertificate(true);
				// 出身高校成績証明書
			} else if ((int) map.get("document_id") == 7) {
				data.setHighscoolTranscript(true);
				// 大学成績証明書
			} else if ((int) map.get("document_id") == 8) {
				data.setUniversityTranscript(true);
				// 個人情報等同意書
			} else if ((int) map.get("document_id") == 9) {
				data.setPersonalInfo(true);
			}
		}

		return data;

	}

	/**
	 * とりまとめ書類の一覧を取得する
	 * @param appId  申請ID
	 * @param entity 受験企業のリスト入りのエンティティ
	 * @return entity
	 */
	public JobAppEntity getJobAppDocumentList(String appId, JobAppEntity entity) {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_DOCUMENT, appId);
		entity = mappingSelectCertificate(resultList, entity);
		return entity;
	}

	/**
	 * とりまとめ書類テーブルから取得したデータをJobAppEntity形式にマッピングする
	 * @param resultList SQLの結果
	 * @param entity
	 * @return entity
	 */
	private JobAppEntity mappingSelectCertificate(List<Map<String, Object>> resultList, JobAppEntity entity) {
		for (Map<String, Object> map : resultList) {
			JobAppDocumentData data = new JobAppDocumentData();
			data.setSummaryId((int) map.get("summary_id"));
			data.setDocumentId((int) map.get("document_id"));
			data.setStatus((boolean) map.get("status"));
			entity.getJobAppDocumentList().add(data);
		}
		return entity;
	}

	/**
	 * 申請状態を取消済にする
	 *
	 * @param appId 申請ID
	 * @param date  現在日時
	 * @return rowNumber
	 */
	public int cancelOne(String appId, Date date) {
		int rowNumber = jdbc.update(SQL_REPORT_CANCEL, date, appId);
		return rowNumber;
	}

	/**
	 * 通知押下時間を更新する
	 *
	 * @param userId       ユーザID
	 * @param notificateId 通知ID
	 * @return rowNumber
	 */
	public int updateNotifiPushDate(String userId, String notificateId) {
		int rowNumber = jdbc.update(SQL_UPDATE_NOTIFICATE_DATE, notificateId, userId);
		return rowNumber;
	}

	/**
	 * 通知押下日時以降の提出された申請を取得する
	 *
	 * @param userId       ユーザID
	 * @param notificateId 通知ID
	 * @return appIdList
	 */
	public List<String> appNotificationT(String userId, String notificateId) {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_UPDATE_APP_LIST, userId, notificateId);
		List<String> appIdList = mappingSelectAppList(resultList);
		return appIdList;
	}

	/**
	 * 通知押下日時以降の提出された報告を取得する
	 *
	 * @param userId       ユーザID
	 * @param notificateId 通知ID
	 * @return repotList
	 */
	public List<String> reportNotificationT(String userId, String notificateId) {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_UPDATE_REPORT_LIST, userId, notificateId);
		List<String> reportList = mappingSelectAppList(resultList);
		return reportList;
	}

	/**
	 * 通知押下日時以降の活動日当日の申請を取得する
	 *
	 * @param userId ユーザID
	 * @return eventList
	 */
	public List<String> eventNotificationT(String userId) {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_COMP_EVENT_LIST, userId);
		List<String> eventList = mappingSelectAppList(resultList);
		return eventList;
	}

	/**
	 * 通知押下日時以降の申請完了された申請を取得する
	 *
	 * @param userId ユーザID
	 * @param notificateId 通知ID
	 * @return courseList
	 */
	public List<String> courseNotificationT(String userId, String notificateId) {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_COURSE_LIST, userId, notificateId);
		List<String> courseList = mappingSelectAppList(resultList);
		return courseList;
	}

	/**
	 * 通知押下日時以降の差戻された申請を取得する
	 *
	 * @param userId       ユーザID
	 * @param notificateId 通知ID
	 * @return remandAppList
	 */
	public List<String> remandAppNotificationS(String userId, String notificateId) {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_REMAND_APP_LIST, userId, notificateId);
		List<String> remandAppList = mappingSelectAppList(resultList);
		return remandAppList;
	}

	/**
	 * 通知押下日時以降の承認された申請を取得する
	 *
	 * @param userId       ユーザID
	 * @param notificateId 通知ID
	 * @return approvalAppList
	 */
	public List<String> approvalAppNotificationS(String userId, String notificateId) {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_APPROVAL_APP_LIST, userId, notificateId);
		List<String> approvalAppList = mappingSelectAppList(resultList);
		return approvalAppList;
	}

	/**
	 * 通知押下日時以降の差戻された報告を取得する
	 *
	 * @param userId       ユーザID
	 * @param notificateId 通知ID
	 * @return remandReportList
	 */
	public List<String> remandReportNotificationS(String userId, String notificateId) {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_REMAND_REPORT_LIST, userId, notificateId);
		List<String> remandReportList = mappingSelectAppList(resultList);
		return remandReportList;
	}

	/**
	 * 通知押下日時以降の承認された報告を取得する
	 *
	 * @param userId       ユーザID
	 * @param notificateId 通知ID
	 * @return approvalReportList
	 */
	public List<String> approvalReportNotificationS(String userId, String notificateId) {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_APPROVAL_REPORT_LIST, userId, notificateId);
		List<String> approvalReportList = mappingSelectAppList(resultList);
		return approvalReportList;
	}

	/**
	 * 通知押下日時以降の活動日が近い申請未完了の申請を取得する
	 *
	 * @param userId       ユーザID
	 * @param notificateId 通知ID
	 * @return eventIncompList
	 */
	public List<String> eventIncompNotificationS(String userId, String notificateId) {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_EVENT_INCOMP_LIST, userId, notificateId);
		List<String> eventIncompList = mappingSelectAppList(resultList);
		return eventIncompList;
	}

	/**
	 * 通知押下日時以降の活動日翌日に報告が作成されていない申請を取得する
	 *
	 * @param userId       ユーザID
	 * @param notificateId 通知ID
	 * @return notSubmittedList
	 */
	public List<String> notSubmittedNotificationS(String userId, String notificateId) {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_NOTSUBMITTED_LIST, userId, notificateId);
		List<String> notSubmittedList = mappingSelectAppList(resultList);
		return notSubmittedList;
	}

	/**
	 * 通知押下日時以降の活動日直前の申請を取得する
	 *
	 * @param userId ユーザID
	 * @return eventList
	 */
	public List<String> eventNotificationS(String userId) {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_EVENT_LIST, userId);
		List<String> eventList = mappingSelectAppList(resultList);
		return eventList;
	}

	/**
	 * 通知押下日時以降のとりまとめ書類提出がある申請を取得する
	 *
	 * @param userId       ユーザID
	 * @param notificateId 通知ID
	 * @return documentList
	 */
	public List<String> documentNotificationS(String userId, String notificateId) {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_DOCUMENT_LIST, userId, notificateId);
		List<String> documentList = mappingSelectAppList(resultList);
		return documentList;
	}

	/**
	 * 通知押下日時以降の書類提出締め切りの近い申請を取得する
	 *
	 * @param userId       ユーザID
	 * @param notificateId 通知ID
	 * @return documentLimitList
	 */
	public List<String> documentLimitNotificationS(String userId, String notificateId) {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_DOCUMENT_LIMIT_LIST, userId, notificateId);
		List<String> documentLimitList = mappingSelectAppList(resultList);
		return documentLimitList;
	}

	/**
	 * 通知件数と申請リストに使用。APPテーブルから取得したデータをReportEntity形式にマッピングする.
	 *
	 * @param resultList SQLの結果
	 * @return appIdList
	 */
	private List<String> mappingSelectAppList(List<Map<String, Object>> resultList) {
		List<String> appIdList = new ArrayList<String>();
		JobAppData data = new JobAppData();

		for (Map<String, Object> map : resultList) {
			data.setAppId((int) map.get("app_id"));
			String appId = String.valueOf(data.getAppId());
			appIdList.add(appId);
		}
		return appIdList;
	}

	/**
	 * 必要書類を更新する
	 *
	 * @param form 書類更新フォーム
	 * @return rowNumber
	 */
	public int jobAppDocumentUpdate(JobAppFormForDocument form) {
		int rowNumber = 0;

		List<Integer> documentId = new ArrayList<Integer>();
		// 履歴書
		if (form.isResume()) {
			documentId.add(1);
		}
		// 推薦書
		if (form.isRecommendation()) {
			documentId.add(2);
		}
		// 大学証明書
		if (form.isUniversityCrtificate()) {
			documentId.add(3);
		}
		// HCS成績証明書
		if (form.isHcsTranscript()) {
			documentId.add(4);
		}
		// HCS卒業見込証明書
		if (form.isHcsCrtificate()) {
			documentId.add(5);
		}
		// 健康診断証明書
		if (form.isHealthCheckCertificate()) {
			documentId.add(6);
		}
		// 出身高校成績証明書
		if (form.isHighscoolTranscript()) {
			documentId.add(7);
		}
		// 大学成績証明書
		if (form.isUniversityTranscript()) {
			documentId.add(8);
		}
		// 個人情報等同意書
		if (form.isPersonalInfo()) {
			documentId.add(9);
		}
		for (int i = 0; i < documentId.size(); i++) {
			// 取りまとめ書類テーブルに登録
			rowNumber = jdbc.update(SQL_UPDATE_DOCUMENT, form.getSummaryId(), documentId.get(i));
		}

		return rowNumber;
	}

	/**
	 * とりまとめ名簿登録済にする
	 *
	 * @param appId 申請ID
	 * @return rowNumber 更新行数
	 */
	public int updateSummary(String appId) {
		// 更新処理
		int rowNumber = jdbc.update(SQL_UPDATE_SUMMARY, appId);
		return rowNumber;
	}

	/**
	 * 通知一件の申請情報を全件取得する
	 *
	 * @param appId 申請ID
	 * @return jobAppEntity
	 */
	public JobAppEntity JobAppNotificationList(String appId) {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_NOTIFICATIONLIST, appId);
		JobAppEntity jobAppEntity = mappingSelectResultList(resultList);
		return jobAppEntity;

	}
}
