package jp.ac.hcs.ShukatsuPortal.jobapp;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.ac.hcs.ShukatsuPortal.user.UserData;
import jp.ac.hcs.ShukatsuPortal.user.UserRepository;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 * 就職活動申請に関する操作を行う
 */
@Service
public class JobAppService {

	@Autowired
	JobAppRepository jobAppRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	RestTemplate restTemplate;

	/** 誤字脱字チェックAPI リクエストURL */
	private static final String URL = "https://api.a3rt.recruit.co.jp/proofreading/v2/typo?apikey={apiKey}&sentence={sentence}&sensitivity=high";
	private static final String apiKey = "DZZyEFBrawXMVG2aQY9svPzUdGUX8EIV";

	/**
	 * 就職活動申請の一覧を取得する(学生)
	 *
	 * @param userId ログイン中のユーザのユーザID
	 * @return jobAppEntity
	 * @throws DataAccessException データベースアクセスエラー
	 */
	public JobAppEntity getJobAppListStudent(String userId) throws DataAccessException {
		JobAppEntity jobAppEntity;
		// 学生の場合はクラス番号と一致させるためnameを送る
		jobAppEntity = jobAppRepository.getJobAppListStudent(userId);
		return jobAppEntity;
	}

	/**
	 * 就職活動申請の一覧を取得する(担任)
	 *
	 * @return jobAppEntity
	 * @throws DataAccessException データベースアクセスエラー
	 */
	public JobAppEntity getJobAppListTeacher() throws DataAccessException {
		JobAppEntity jobAppEntity;
		// 担任の場合はそのまま一覧を取得する。
		jobAppEntity = jobAppRepository.getJobAppListTeacher();
		return jobAppEntity;
	}

	/**
	 * 就職活動申請の詳細を取得する（申請内容）
	 *
	 * @param appId 申請ID
	 * @return jobAppData
	 */
	public JobAppData getJobAppDetail(String appId) {
		// 選択された申請の詳細を取得する
		JobAppData data = jobAppRepository.getJobAppDetail(appId);
		return data;
	}

	/**
	 * 就職活動申請の詳細を取得する（会社名）
	 *
	 * @param appId 申請ID
	 * @return jobAppEntity
	 */
	public JobAppEntity getJobAppCompany(String appId) {
		// 選択された申請に紐づく会社を取得する
		JobAppEntity entity = jobAppRepository.getJobAppCompany(appId);
		return entity;
	}

	/**
	 * 申請新規作成のDB登録を行う
	 *
	 * @param form 申請のフォーム
	 * @param userId ユーザID
	 * @param role ログインユーザの権限
	 * @return rowNumber
	 */
	public int insertJobApp(JobAppForm form, String userId, String role) {
		Date date = new Date();
		// 学生の場合：ログインIDで登録する名前を取得
		if (role.equals("STUDENT")) {
			//ユーザ名の取得
			form.setApplyName(userRepository.selectOne(userId).getUserName());
			// 担任の場合：入力されたクラス番号で名前を取得
		} else {
			String appName = selectUserName(form.getApplyClass(), form.getApplyNumber());
			// 入力されたクラス番号がDBに存在したとき
			if (appName != null) {
				form.setApplyName(appName);
				// 入力されたクラス番号がDBに存在しなかったとき
			} else {
				int notExsistUser = -1;
				return notExsistUser;
			}
		}

		// 終了時刻が未入力の際、自動セットする。
		if (form.getAppFinishDate().length() == 0 | form.getAppFinishTime().length() == 0) {
			form = autoSetFinish(form.getAppStartTime().substring(0, 2), form.getAppStartTime().substring(3, 5), form);

		}

		// 日時の入力チェック
		int check = StartFinishCheck(form.getAppStartDate(), form.getAppStartTime(), form.getAppFinishDate(),
				form.getAppFinishTime());
		if (check == -2) {
			return check;
		}

		JobAppData jobAppData = new JobAppData();
		// とりまとめ書類のデータセット
		if (form.isResume()) {
			jobAppData.setResume(form.isResume());
		}
		if (form.isRecommendation()) {
			jobAppData.setRecommendation(form.isRecommendation());
		}
		if (form.isUniversityCrtificate()) {
			jobAppData.setUniversityCrtificate(form.isUniversityCrtificate());
		}
		if (form.isHcsTranscript()) {
			jobAppData.setHcsTranscript(form.isHcsTranscript());
		}
		if (form.isHcsCrtificate()) {
			jobAppData.setHcsCrtificate(form.isHcsCrtificate());
		}
		if (form.isHealthCheckCertificate()) {
			jobAppData.setHealthCheckCertificate(form.isHealthCheckCertificate());
		}
		if (form.isHighscoolTranscript()) {
			jobAppData.setHighscoolTranscript(form.isHighscoolTranscript());
		}
		if (form.isUniversityTranscript()) {
			jobAppData.setUniversityTranscript(form.isUniversityTranscript());
		}
		if (form.isPersonalInfo()) {
			jobAppData.setPersonalInfo(form.isPersonalInfo());
		}

		jobAppData.setSummaryFlag(form.isSummaryFlag());
		jobAppData.setDocumentFlag(form.isDocumentFlag());
		jobAppData.setAppAddress(form.getAppAddress());
		jobAppData.setAppContents(form.getAppContents());
		jobAppData.setAppContentOther(form.getAppContentOther());
		jobAppData.setAbsenceFlag(form.isAbsenceFlag());
		jobAppData.setLeaveFlag(form.isLeaveFlag());
		jobAppData.setLateFlag(form.isLateFlag());
		jobAppData.setAppMemo(form.getAppMemo());
		jobAppData.setApplyClass(form.getApplyClass());
		jobAppData.setApplyNumber(form.getApplyNumber());
		jobAppData.setApplyName(form.getApplyName());
		jobAppData.setAppStartAbsence(form.getAppStartAbsence().replace("/", "-"));
		jobAppData.setAppFinishAbsence(form.getAppFinishAbsence().replace("/", "-"));
		jobAppData.setAppStartDate(form.getAppStartDate().replace("/", "-"));
		jobAppData.setAppFinishDate(form.getAppFinishDate().replace("/", "-"));
		jobAppData.setAppLeaveDate(form.getAppLeaveDate().replace("/", "-"));
		jobAppData.setAppLateDate(form.getAppLateDate().replace("/", "-"));
		jobAppData.setDeadline(form.getDeadline().replace("/", "-"));
		jobAppData.setAppDate(date);
		jobAppData.setAppStartTime(form.getAppStartTime());
		jobAppData.setAppFinishTime(form.getAppFinishTime());
		jobAppData.setAppLeaveTime(form.getAppLeaveTime());
		jobAppData.setAppLateTime(form.getAppLateTime());
		jobAppData.setAppCompany(form.getAppCompany());

		jobAppData.setAppStartAbsence(nullReturn(form.getAppStartAbsence()));
		jobAppData.setAppFinishAbsence(nullReturn(form.getAppFinishAbsence()));
		jobAppData.setAppStartDate(nullReturn(form.getAppStartDate()));
		jobAppData.setAppFinishDate(nullReturn(form.getAppFinishDate()));
		jobAppData.setAppLeaveDate(nullReturn(form.getAppLeaveDate()));
		jobAppData.setAppLateDate((nullReturn(form.getAppLateDate())));
		jobAppData.setAppStartTime(nullReturn(form.getAppStartTime()));
		jobAppData.setAppFinishTime(nullReturn(form.getAppFinishTime()));
		jobAppData.setAppLeaveTime(nullReturn(form.getAppLeaveTime()));
		jobAppData.setAppLateTime(nullReturn(form.getAppLateTime()));
		jobAppData.setDeadline(nullReturn(form.getDeadline()));

		int rowNumber = jobAppRepository.insertJobApp(jobAppData);
		return rowNumber;
	}

	/**
	 * 終了日時が未入力の際に自動セットするメソッド
	 *
	 * @param startTimeS 開始日
	 * @param startMinuteS 開始時間
	 * @param form 申請情報
	 * @return JobAppForm
	 */
	public JobAppForm autoSetFinish(String startTimeS, String startMinuteS, JobAppForm form) {
		int startTime = Integer.parseInt(startTimeS);
		int startMinute = Integer.parseInt(startMinuteS);
		int endTime = 0;
		int endMinute = 0;
		// 不正入力：開始時刻が24時以降
		if (startTime >= 24) {
			// 終了時刻を固定値にしてリターンする
			form.setAppFinishTime("99:99");
			form.setAppFinishDate(form.getAppStartDate());
			// 18時以降開始の申請
		}
		if (startTime >= 18) {
			// 終了時刻を23時59分にする
			endTime = 23;
			endMinute = 59;
			String autoTimeMinute = Integer.toString(endTime) + ":" + Integer.toString(endMinute);
			form.setAppFinishTime(autoTimeMinute);
			form.setAppFinishDate(form.getAppStartDate());
			// 4時以降開始の申請
		} else if (startTime >= 4) {
			// 開始時刻から6時間後を終了時刻とする
			endTime = startTime + 6;
			// 分が０分のとき
			if (startMinute == 0) {
				// 自動計算した時分をSTRING変換
				String autoTimeMinute = Integer.toString(endTime) + ":00";
				form.setAppFinishTime(autoTimeMinute);
				form.setAppFinishDate(form.getAppStartDate());
				// 分が1桁のとき
			} else if (startMinute < 10) {
				// 自動計算した時分をSTRING変換
				String autoTimeMinute = Integer.toString(endTime) + ":0" + Integer.toString(startMinute);
				form.setAppFinishTime(autoTimeMinute);
				form.setAppFinishDate(form.getAppStartDate());

			} else {
				endMinute = startMinute;
				// 自動計算した時分をSTRING変換
				String autoTimeMinute = Integer.toString(endTime) + ":" + Integer.toString(startMinute);
				form.setAppFinishTime(autoTimeMinute);
				form.setAppFinishDate(form.getAppStartDate());
			}
			// 0時以降開始の申請
		} else if (startTime >= 0) {
			// 開始時刻から6時間後を終了時刻とする
			endTime = startTime + 6;
			// 分が０分のとき
			if (startMinute == 0) {
				// 自動計算した時分をSTRING変換
				String autoTimeMinute = "0" + Integer.toString(endTime) + ":00";
				form.setAppFinishTime(autoTimeMinute);
				form.setAppFinishDate(form.getAppStartDate());
				// 分が1桁のとき
			} else if (startMinute < 10) {
				// 自動計算した時分をSTRING変換
				String autoTimeMinute = "0" + Integer.toString(endTime) + ":0" + Integer.toString(startMinute);
				form.setAppFinishTime(autoTimeMinute);
				form.setAppFinishDate(form.getAppStartDate());

			} else {
				endMinute = startMinute;
				// 自動計算した時分をSTRING変換
				String autoTimeMinute = "0" + Integer.toString(endTime) + ":" + Integer.toString(startMinute);
				form.setAppFinishTime(autoTimeMinute);
				form.setAppFinishDate(form.getAppStartDate());
			}
			// 不正入力：開始時刻が負の数のとき
		} else {
			// 終了時刻を固定値にしてリターンする
			form.setAppFinishTime("99:99");
			form.setAppFinishDate(form.getAppStartDate());
		}

		return form;
	}

	/**
	 * 就職活動申請の検索を行う(学生)
	 *
	 * @param search 検索用の申請フォーム
	 * @param userId ユーザID
	 * @return jobAppEntity
	 * @throws DataAccessException データベースアクセスエラー
	 */
	public JobAppEntity searchJobAppStudent(JobAppFormForSearch search, String userId) throws DataAccessException {
		JobAppEntity jobAppEntity;
		jobAppEntity = jobAppRepository.searchJobAppListStudent(search, userId);
		return jobAppEntity;
	}

	/**
	 * 就職活動申請の検索を行う(担任)
	 *
	 * @param search 検索用の申請フォーム
	 * @return jobAppEntity
	 * @throws DataAccessException データベースアクセスエラー
	 */
	public JobAppEntity searchJobAppTeacher(JobAppFormForSearch search) throws DataAccessException {
		JobAppEntity jobAppEntity;
		jobAppEntity = jobAppRepository.searchJobAppListTeacher(search);
		return jobAppEntity;
	}

	/**
	 * Stringの0文字の文字列をnullにして返す
	 *
	 * @param str 対象の文字列
	 * @return str
	 */
	public String nullReturn(String str) {
		if (str.length() == 0) {
			str = null;
		}
		return str;
	}

	/**
	 * ユーザ情報を取得するメソッド
	 * @param userId ユーザID
	 * @return userData
	 */
	public UserData selectUser(String userId) {
		UserData userData = userRepository.selectOne(userId);
		return userData;
	}

	/**
	 * クラス番号から名前を取得するメソッド
	 *
	 * @param inputClass  入力クラス
	 * @param inputNumber 入力番号
	 * @return name
	 */
	public String selectUserName(String inputClass, int inputNumber) {
		UserData userData = userRepository.selectUserName(inputClass, inputNumber);
		String name = userData.getUserName();
		return name;
	}

	/**
	 * 申請報告リストをCSVファイルとしてサーバに保存する.
	 *
	 * @param appIds 申請IDのリスト
	 * @throws DataAccessException データベースアクセスエラー
	 */
	public void appListCsvOut(List<String> appIds) throws DataAccessException {
		for (String appId : appIds) {
			jobAppRepository.applistCsvOut(appId);
		}
	}

	/**
	 * サーバーに保存されているファイルを取得して、byte配列に変換する
	 *
	 * @param fileName ファイル名
	 * @return ファイルのbyte配列
	 * @throws IOException ファイル取得エラー
	 */
	public byte[] getFile(String fileName) throws IOException {
		FileSystem fs = FileSystems.getDefault();
		Path p = fs.getPath(fileName);
		byte[] bytes = Files.readAllBytes(p);
		return bytes;
	}

	/**
	 * とりまとめの検索を行う(学生)
	 *
	 * @param userid ログイン中のユーザID
	 * @param search 検索用の申請フォーム
	 * @return jobAppEntity
	 * @throws DataAccessException データベースアクセスエラー
	 */
	public JobAppEntity searchSummaryStudent(JobAppFormForSearch search, String userid) throws DataAccessException {
		JobAppEntity jobAppEntity;
		jobAppEntity = jobAppRepository.searchSummaryStudent(search, userid);
		return jobAppEntity;
	}

	/**
	 * とりまとめの検索を行う(担任)
	 *
	 * @param search 検索用の申請フォーム
	 * @return jobAppEntity
	 * @throws DataAccessException データベースアクセスエラー
	 */
	public JobAppEntity searchSummaryTeacher(JobAppFormForSearch search) throws DataAccessException {
		JobAppEntity jobAppEntity;
		jobAppEntity = jobAppRepository.searchSummaryTeacher(search);
		return jobAppEntity;
	}

	/**
	 * 申請新規作成の開始日時終了日時チェックを行う
	 *
	 * @param startDate 開始日
	 * @param startTime 開始時刻
	 * @param endDate   終了日
	 * @param endTime   終了時刻
	 * @return notDate
	 */
	public int StartFinishCheck(String startDate, String startTime, String endDate, String endTime) {
		// 返し値
		int notDate = 1;
		// ハイフンやコロンなどを除去し数字のみの文字列に変換する
		String start = startDate.substring(0, 4) + startDate.substring(5, 7) + startDate.substring(8, 10)
				+ startTime.substring(0, 2) + startTime.substring(3, 5);
		String end = endDate.substring(0, 4) + endDate.substring(5, 7) + endDate.substring(8, 10)
				+ endTime.substring(0, 2) + endTime.substring(3, 5);
		// ロング型に変換
		long startInt = Long.parseLong(start);
		long endInt = Long.parseLong(end);
		// 開始日時が終了日時以降である場合
		if (startInt >= endInt) {
			notDate = -2;
		}

		return notDate;

	}

	/**
	 * 申請のコース担当連絡登録と申請状態を申請完了にする
	 *
	 * @param appId 申請ID
	 * @return result
	 */
	public int updateApprovalFlag(String appId) {
		Date date = new Date();
		int result = jobAppRepository.updateApprovalFlag(appId, date);
		return result;
	}

	/**
	 * 申請状態を申請待から申請承認済に変更する
	 *
	 * @param appId 申請ID
	 * @return result
	 */
	public int updateApproval(String appId) {
		Date date = new Date();
		int result = jobAppRepository.updateApproval(appId, date);
		return result;
	}

	/**
	 * 申請の差戻しを行う
	 *
	 * @param form 差戻用フォーム
	 * @return result
	 */
	public int updateRemand(JobAppFormForRemand form) {
		Date date = new Date();
		int result = jobAppRepository.updateRemand(form.getAppId(), date, form.getIndicate());
		return result;
	}

	/**
	 * 申請更新のDB登録を行う
	 *
	 * @param appId 申請ID
	 * @param form 申請更新フォーム
	 * @return rowNumber
	 */
	public int updateJobApp(String appId, JobAppFormForUpdate form) {

		String appName = selectUserName(form.getApplyClass(), form.getApplyNumber());
		// 入力されたクラス番号がDBに存在したとき
		if (appName != null) {
			form.setApplyName(appName);
			// 入力されたクラス番号がDBに存在しなかったとき
		} else {
			int notExsistUser = -1;
			return notExsistUser;
		}

		// 終了時刻が未入力の際、自動セットする。
		if (form.getAppFinishDate().length() == 0 | form.getAppFinishTime().length() == 0)

		{
			form = autoSetFinishSecond(form.getAppStartTime().substring(0, 2), form.getAppStartTime().substring(3, 5),
					form);
		}

		// 日時の入力チェック
		int check = StartFinishCheck(form.getAppStartDate(), form.getAppStartTime(), form.getAppFinishDate(),
				form.getAppFinishTime());
		if (check == -2) {
			return check;
		}

		JobAppData jobAppData = new JobAppData();
		// とりまとめ書類のデータセット
		if (form.isResume()) {
			jobAppData.setResume(form.isResume());
		}
		if (form.isRecommendation()) {
			jobAppData.setRecommendation(form.isRecommendation());
		}
		if (form.isUniversityCrtificate()) {
			jobAppData.setUniversityCrtificate(form.isUniversityCrtificate());
		}
		if (form.isHcsTranscript()) {
			jobAppData.setHcsTranscript(form.isHcsTranscript());
		}
		if (form.isHcsCrtificate()) {
			jobAppData.setHcsCrtificate(form.isHcsCrtificate());
		}
		if (form.isHealthCheckCertificate()) {
			jobAppData.setHealthCheckCertificate(form.isHealthCheckCertificate());
		}
		if (form.isHighscoolTranscript()) {
			jobAppData.setHighscoolTranscript(form.isHighscoolTranscript());
		}
		if (form.isUniversityTranscript()) {
			jobAppData.setUniversityTranscript(form.isUniversityTranscript());
		}
		if (form.isPersonalInfo()) {
			jobAppData.setPersonalInfo(form.isPersonalInfo());
		}
		Date date = new Date();
		jobAppData.setSummaryFlag(form.isSummaryFlag());
		jobAppData.setDocumentFlag(form.isDocumentFlag());
		jobAppData.setAppAddress(form.getAppAddress());
		jobAppData.setAppContents(form.getAppContents());
		jobAppData.setAppContentOther(form.getAppContentOther());
		jobAppData.setAbsenceFlag(form.isAbsenceFlag());
		jobAppData.setLeaveFlag(form.isLeaveFlag());
		jobAppData.setLateFlag(form.isLateFlag());
		jobAppData.setAppMemo(form.getAppMemo());
		jobAppData.setApplyClass(form.getApplyClass());
		jobAppData.setApplyNumber(form.getApplyNumber());
		jobAppData.setApplyName(form.getApplyName());
		jobAppData.setAppStartAbsence(form.getAppStartAbsence().replace("/", "-"));
		jobAppData.setAppFinishAbsence(form.getAppFinishAbsence().replace("/", "-"));
		jobAppData.setAppStartDate(form.getAppStartDate().replace("/", "-"));
		jobAppData.setAppFinishDate(form.getAppFinishDate().replace("/", "-"));
		jobAppData.setAppLeaveDate(form.getAppLeaveDate().replace("/", "-"));
		jobAppData.setAppLateDate(form.getAppLateDate().replace("/", "-"));
		jobAppData.setAppStartTime(form.getAppStartTime());
		jobAppData.setAppFinishTime(form.getAppFinishTime());
		jobAppData.setAppLeaveTime(form.getAppLeaveTime());
		jobAppData.setAppLateTime(form.getAppLateTime());
		jobAppData.setAppCompany(form.getAppCompany());
		jobAppData.setDeadline(form.getDeadline().replace("/", "-"));
		jobAppData.setDeadline(nullReturn(form.getDeadline()));
		jobAppData.setAppStartAbsence(nullReturn(form.getAppStartAbsence()));
		jobAppData.setAppFinishAbsence(nullReturn(form.getAppFinishAbsence()));
		jobAppData.setAppStartDate(nullReturn(form.getAppStartDate()));
		jobAppData.setAppFinishDate(nullReturn(form.getAppFinishDate()));
		jobAppData.setAppLeaveDate(nullReturn(form.getAppLeaveDate()));
		jobAppData.setAppLateDate((nullReturn(form.getAppLateDate())));
		jobAppData.setAppStartTime(nullReturn(form.getAppStartTime()));
		jobAppData.setAppFinishTime(nullReturn(form.getAppFinishTime()));
		jobAppData.setAppLeaveTime(nullReturn(form.getAppLeaveTime()));
		jobAppData.setAppLateTime(nullReturn(form.getAppLateTime()));

		// DB登録処理
		int rowNumber = jobAppRepository.updateJobApp(appId, jobAppData, date);

		return rowNumber;

	}

	/**
	 * 終了日時の自動入力を行うメソッド
	 *
	 * @param startTimeS 開始日
	 * @param startMinuteS 開始時間
	 * @param form 申請情報
	 * @return JobAppFormForUpdate
	 */
	public JobAppFormForUpdate autoSetFinishSecond(String startTimeS, String startMinuteS, JobAppFormForUpdate form) {
		int startTime = Integer.parseInt(startTimeS);
		int startMinute = Integer.parseInt(startMinuteS);
		int endTime = 0;
		int endMinute = 0;
		// 不正入力：開始時刻が24時以降
		if (startTime >= 24) {
			// 終了時刻を固定値にしてリターンする
			form.setAppFinishTime("99:99");
			form.setAppFinishDate(form.getAppStartDate());
			// 18時以降開始の申請
		}
		if (startTime >= 18) {
			// 終了時刻を23時59分にする
			endTime = 23;
			endMinute = 59;
			String autoTimeMinute = Integer.toString(endTime) + ":" + Integer.toString(endMinute);
			form.setAppFinishTime(autoTimeMinute);
			form.setAppFinishDate(form.getAppStartDate());
			// 4時以降開始の申請
		} else if (startTime >= 4) {
			// 開始時刻から6時間後を終了時刻とする
			endTime = startTime + 6;
			// 分が０分のとき
			if (startMinute == 0) {
				// 自動計算した時分をSTRING変換
				String autoTimeMinute = Integer.toString(endTime) + ":00";
				form.setAppFinishTime(autoTimeMinute);
				form.setAppFinishDate(form.getAppStartDate());
				// 分が1桁のとき
			} else if (startMinute < 10) {
				// 自動計算した時分をSTRING変換
				String autoTimeMinute = Integer.toString(endTime) + ":0" + Integer.toString(startMinute);
				form.setAppFinishTime(autoTimeMinute);
				form.setAppFinishDate(form.getAppStartDate());

			} else {
				endMinute = startMinute;
				// 自動計算した時分をSTRING変換
				String autoTimeMinute = Integer.toString(endTime) + ":" + Integer.toString(startMinute);
				form.setAppFinishTime(autoTimeMinute);
				form.setAppFinishDate(form.getAppStartDate());
			}
			// 0時以降開始の申請
		} else if (startTime >= 0) {
			// 開始時刻から6時間後を終了時刻とする
			endTime = startTime + 6;
			// 分が０分のとき
			if (startMinute == 0) {
				// 自動計算した時分をSTRING変換
				String autoTimeMinute = "0" + Integer.toString(endTime) + ":00";
				form.setAppFinishTime(autoTimeMinute);
				form.setAppFinishDate(form.getAppStartDate());
				// 分が1桁のとき
			} else if (startMinute < 10) {
				// 自動計算した時分をSTRING変換
				String autoTimeMinute = "0" + Integer.toString(endTime) + ":0" + Integer.toString(startMinute);
				form.setAppFinishTime(autoTimeMinute);
				form.setAppFinishDate(form.getAppStartDate());

			} else {
				endMinute = startMinute;
				// 自動計算した時分をSTRING変換
				String autoTimeMinute = "0" + Integer.toString(endTime) + ":" + Integer.toString(startMinute);
				form.setAppFinishTime(autoTimeMinute);
				form.setAppFinishDate(form.getAppStartDate());
			}
			// 不正入力：開始時刻が負の数のとき
		} else {
			// 終了時刻を固定値にしてリターンする
			form.setAppFinishTime("99:99");
			form.setAppFinishDate(form.getAppStartDate());
		}
		return form;
	}

	/**
	 * 申請詳細変更の書類情報を取得する
	 *
	 * @param appId 申請ID
	 * @return jobAppDocument
	 */
	public JobAppData getjobAppDocument(String appId) {
		JobAppData jobAppDocument = jobAppRepository.selectDocument(appId);
		return jobAppDocument;
	}

	/**
	 * 就職活動申請の詳細を取得する（申請内容変更）
	 *
	 * @param appId 申請ID
	 * @return jobAppForm
	 */
	public JobAppForm getJobAppDetailChange(String appId) {
		// 選択された申請の詳細を取得する
		JobAppData data = jobAppRepository.getJobAppDetail(appId);
		JobAppForm jobAppForm = new JobAppForm();

		jobAppForm.setSummaryFlag(data.isSummaryFlag());
		jobAppForm.setDocumentFlag(data.isDocumentFlag());
		jobAppForm.setAppAddress(data.getAppAddress());
		jobAppForm.setAppContents(data.getAppContents());
		jobAppForm.setAppContentOther(data.getAppContentOther());
		jobAppForm.setAbsenceFlag(data.isAbsenceFlag());
		jobAppForm.setLeaveFlag(data.isLeaveFlag());
		jobAppForm.setLateFlag(data.isLateFlag());
		jobAppForm.setAppMemo(data.getAppMemo());
		jobAppForm.setApplyClass(data.getApplyClass());
		jobAppForm.setApplyNumber(data.getApplyNumber());
		jobAppForm.setApplyName(data.getApplyName());
		jobAppForm.setAppStartAbsence(data.getAppStartAbsence());
		jobAppForm.setAppFinishAbsence(data.getAppFinishAbsence());
		jobAppForm.setAppStartDate(data.getAppStartDate());
		jobAppForm.setAppFinishDate(data.getAppFinishDate());
		jobAppForm.setAppLeaveDate(data.getAppLeaveDate());
		jobAppForm.setAppLateDate(data.getAppLateDate());
		jobAppForm.setAppStartTime(data.getAppStartTime());
		jobAppForm.setAppFinishTime(data.getAppFinishTime());
		jobAppForm.setAppLeaveTime(data.getAppLeaveTime());
		jobAppForm.setAppLateTime(data.getAppLateTime());
		jobAppForm.setAppCompany(data.getAppCompany());
		jobAppForm.setAppStartAbsence(data.getAppStartAbsence());
		jobAppForm.setAppFinishAbsence(data.getAppFinishAbsence());
		jobAppForm.setAppStartDate(data.getAppStartDate());
		jobAppForm.setAppFinishDate(data.getAppFinishDate());
		jobAppForm.setAppLeaveDate(data.getAppLeaveDate());
		jobAppForm.setAppLateDate(data.getAppLateDate());
		jobAppForm.setAppStartTime(data.getAppStartTime());
		jobAppForm.setAppFinishTime(data.getAppFinishTime());
		jobAppForm.setAppLeaveTime(data.getAppLeaveTime());
		jobAppForm.setAppLateTime(data.getAppLateTime());

		return jobAppForm;

	}

	/**
	 * とりまとめ書類の一覧を取得する
	 *
	 * @param appId 申請ID
	 * @param entity 受験企業の一覧入りエンティティ
	 * @return jobAppEntity
	 */
	public JobAppEntity getJobAppDocumentList(String appId, JobAppEntity entity) {
		entity = jobAppRepository.getJobAppDocumentList(appId, entity);
		return entity;
	}

	/**
	 * 申請の取消処理を行う
	 *
	 * @param appId 申請ID
	 * @return boolean
	 */
	public int cancel(String appId) {
		int rowNumber;
		Date date = new Date();
		rowNumber = jobAppRepository.cancelOne(appId, date);
		return rowNumber;
	}

	/**
	 * 通知押下時間を更新する
	 *
	 * @param userId ユーザID
	 * @param notificateId 通知ID
	 * @return rowNumber 更新行数
	 */
	public int updateNotifiPushDate(String userId, String notificateId) {
		int rowNumber = jobAppRepository.updateNotifiPushDate(userId, notificateId);
		return rowNumber;
	}

	/**
	 * 提出された申請の申請IDリストを取得する
	 *
	 * @param userId ユーザID
	 * @param notificateId 通知ID
	 * @return appIdList
	 */
	public List<String> appNotificationT(String userId, String notificateId) {
		List<String> appIdList = jobAppRepository.appNotificationT(userId, notificateId);
		return appIdList;
	}

	/**
	 * 提出された報告の申請IDリストを取得する
	 *
	 * @param userId ユーザID
	 * @param notificateId 通知ID
	 * @return reportList
	 */
	public List<String> reportNotificationT(String userId, String notificateId) {
		List<String> reportList = jobAppRepository.reportNotificationT(userId, notificateId);
		return reportList;
	}

	/**
	 * 今日活動がある申請完了の申請IDリストを取得する
	 *
	 * @param userId ユーザID
	 * @return eventList
	 */
	public List<String> eventNotificationT(String userId) {
		List<String> eventList = jobAppRepository.eventNotificationT(userId);
		return eventList;
	}

	/**
	 * コース担当への連絡完了の申請IDリストを取得する
	 *
	 * @param userId ユーザID
	 * @param notificateId 通知ID
	 * @return courseList
	 */
	public List<String> courseNotificationT(String userId, String notificateId) {
		List<String> courseList = jobAppRepository.courseNotificationT(userId, notificateId);
		return courseList;
	}

	/**
	 * 差し戻しされた申請の申請IDリストを取得する
	 *
	 * @param userId ユーザID
	 * @param notificateId 通知ID
	 * @return remandAppList
	 */
	public List<String> remandAppNotificationS(String userId, String notificateId) {
		List<String> remandAppList = jobAppRepository.remandAppNotificationS(userId, notificateId);
		return remandAppList;
	}

	/**
	 * 承認された申請の申請IDリストを取得する
	 *
	 * @param userId ユーザID
	 * @param notificateId 通知ID
	 * @return approvalAppList
	 */
	public List<String> approvalAppNotificationS(String userId, String notificateId) {
		List<String> approvalAppList = jobAppRepository.approvalAppNotificationS(userId, notificateId);
		return approvalAppList;
	}

	/**
	 * 差し戻しされた報告の申請IDリストを取得する
	 *
	 * @param userId ユーザID
	 * @param notificateId 通知ID
	 * @return remandReportList
	 */
	public List<String> remandReportNotificationS(String userId, String notificateId) {
		List<String> remandReportList = jobAppRepository.remandReportNotificationS(userId, notificateId);
		return remandReportList;
	}

	/**
	 * 承認された報告の申請IDリストを取得する
	 *
	 * @param userId ユーザID
	 * @param notificateId 通知ID
	 * @return approvalReportList
	 */
	public List<String> approvalReportNotificationS(String userId, String notificateId) {
		List<String> approvalReportList = jobAppRepository.approvalReportNotificationS(userId, notificateId);
		return approvalReportList;
	}

	/**
	 * 直近にイベントがある申請未完了の申請IDリストを取得する
	 *
	 * @param userId ユーザID
	 * @param notificateId 通知ID
	 * @return eventIncompList
	 */
	public List<String> eventIncompNotificationS(String userId, String notificateId) {
		List<String> eventIncompList = jobAppRepository.eventIncompNotificationS(userId, notificateId);
		return eventIncompList;
	}

	/**
	 * イベントが完了しているが報告を作成していない申請IDリストの取得する
	 *
	 * @param userId ユーザID
	 * @param notificateId 通知ID
	 * @return notSubmittedList
	 */
	public List<String> notSubmittedNotificationS(String userId, String notificateId) {
		List<String> notSubmittedList = jobAppRepository.notSubmittedNotificationS(userId, notificateId);
		return notSubmittedList;
	}

	/**
	 * 直近にイベントがある申請の申請IDリストを取得する
	 *
	 * @param userId ユーザID
	 * @return eventList
	 */
	public List<String> eventNotificationS(String userId) {
		List<String> eventList = jobAppRepository.eventNotificationS(userId);
		return eventList;
	}

	/**
	 * とりまとめ書類提出のある申請IDリストを取得する
	 *
	 * @param userId ユーザID
	 * @param notificateId 通知ID
	 * @return documentList
	 */
	public List<String> documentNotificationS(String userId, String notificateId) {
		List<String> documentList = jobAppRepository.documentNotificationS(userId, notificateId);
		return documentList;
	}

	/**
	 * 締め切り前で未提出書類のある申請IDリストを取得する
	 *
	 * @param userId ユーザID
	 * @param notificateId 通知ID
	 * @return documentLimitList
	 */
	public List<String> documentLimitNotificationS(String userId, String notificateId) {
		List<String> documentLimitList = jobAppRepository.documentLimitNotificationS(userId, notificateId);
		return documentLimitList;
	}

	/**
	 * 必要書類を更新する
	 *
	 * @param form 書類更新用フォーム
	 * @return rowNumber
	 */
	public int jobAppDocument(JobAppFormForDocument form) {
		int rowNumber = jobAppRepository.jobAppDocumentUpdate(form);
		return rowNumber;

	}

	/**
	 * 申請詳細変更でJobAppDataからJobAppUpdateFormに変換する
	 *
	 * @param data 申請データ
	 * @return change
	 */
	public JobAppFormForUpdate changeJobApp(JobAppData data) {
		JobAppFormForUpdate change = new JobAppFormForUpdate();
		change.setAppId(data.getAppId());
		change.setAppStatus(data.getAppStatus());
		change.setSummaryFlag(data.isSummaryFlag());
		change.setDocumentFlag(data.isDocumentFlag());
		change.setApprovalFlag(data.isApprovalFlag());
		change.setAppStartDate(data.getAppStartDate());
		change.setAppStartTime(data.getAppStartTime());
		change.setAppFinishDate(data.getAppFinishDate());
		change.setAppFinishTime(data.getAppFinishTime());
		change.setAppAddress(data.getAppAddress());
		change.setAppContents(data.getAppContents());
		change.setAppContentOther(data.getAppContentOther());
		change.setAbsenceFlag(data.isAbsenceFlag());
		change.setAppStartAbsence(data.getAppStartAbsence());
		change.setAppFinishAbsence(data.getAppFinishAbsence());
		change.setLeaveFlag(data.isLeaveFlag());
		change.setAppLeaveDate(data.getAppLeaveDate());
		change.setAppLeaveTime(data.getAppLeaveTime());
		change.setLateFlag(data.isLateFlag());
		change.setAppLateDate(data.getAppLateDate());
		change.setAppLateTime(data.getAppLateTime());
		change.setAppMemo(data.getAppMemo());
		change.setApplyClass(data.getApplyClass());
		change.setApplyNumber(data.getApplyNumber());
		change.setApplyName(data.getApplyName());
		change.setIndicate(data.getIndicate());
		return change;
	}

	/**
	 * とりまとめ名簿に登録したことをシステムに登録する
	 *
	 * @param appId 申請ID
	 * @return rowNumber 更新行数
	 */
	public int updateSummary(String appId) {
		int rowNumber = jobAppRepository.updateSummary(appId);
		return rowNumber;
	}

	/**
	 * 通知リストを申請一覧に変換する
	 *
	 * @param appIdList 変換する申請IDリスト
	 * @return entity 変換された申請一覧
	 */
	public JobAppEntity JobAppNotificationList(List<String> appIdList) {
		JobAppEntity entity = new JobAppEntity();
		JobAppEntity entity2 = new JobAppEntity();
		JobAppData data = new JobAppData();
		for (String appId : appIdList) {
			entity2 = jobAppRepository.JobAppNotificationList(appId);
			data.setAppId(entity2.getJobAppList().get(0).getAppId());
			data.setAppStatus(entity2.getJobAppList().get(0).getAppStatus());
			data.setSummaryFlag(entity2.getJobAppList().get(0).isSummaryFlag());
			data.setAppStartDate(entity2.getJobAppList().get(0).getAppStartDate());
			data.setAppFinishDate(entity2.getJobAppList().get(0).getAppFinishDate());
			data.setApplyClass(entity2.getJobAppList().get(0).getApplyClass());
			data.setApplyNumber(entity2.getJobAppList().get(0).getApplyNumber());
			data.setApplyName(entity2.getJobAppList().get(0).getApplyName());
			entity.getJobAppList().add(data);
		}
		return entity;
	}

	/**
	 * PDFを出力する
	 * @param appId 申請ID
	 * @throws JRException JasperReportsのエラー
	 */

	public void jobAppPdf(String appId) throws JRException {
		Map<String, Object> parameters = getParams(appId);
		File jrxmlFile = new File("target/pdf.jrxml");
		JasperReport report;
		report = JasperCompileManager.compileReport(jrxmlFile.getAbsolutePath());
		JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
		JasperExportManager.exportReportToPdfFile(jasperPrint, "target/summary.pdf");

	}

	/**
	 * PDF出力の際に使用するパラメータを取得する
	 * @param appId 申請ID
	 * @return params
	 */
	public Map<String, Object> getParams(String appId) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");

		Map<String, Object> params = new HashMap<>();
		JobAppData data = jobAppRepository.getJobAppDetail(appId);
		JobAppEntity entity = jobAppRepository.getJobAppCompany(appId);
		entity = jobAppRepository.getJobAppDocumentList(appId, entity);
		JobAppData document = jobAppRepository.selectDocument(appId);

		// データセット処理
		params.put("class", data.getApplyClass());
		params.put("number", data.getApplyNumber());
		params.put("name", data.getApplyName());
		params.put("appDate", sd.format(data.getAppDate()));
		params.put("appCompany", entity.getJobAppCompanys().get(0));
		if (document.isResume()) {
			params.put("rflag", "〇");
		} else {
			params.put("rflag", "");
		}
		if (document.isUniversityTranscript()) {
			params.put("dsflag", "〇");
		} else {
			params.put("dsflag", "");
		}
		if (document.isUniversityCrtificate()) {
			params.put("dsmflag", "〇");
		} else {
			params.put("dsmflag", "");
		}
		if (document.isHcsTranscript()) {
			params.put("hsflag", "〇");
		} else {
			params.put("hsflag", "");
		}
		if (document.isHcsCrtificate()) {
			params.put("hmflag", "〇");
		} else {
			params.put("hmflag", "");
		}
		if (document.isHealthCheckCertificate()) {
			params.put("kflag", "〇");
		} else {
			params.put("kflag", "");
		}
		if (document.isHighscoolTranscript()) {
			params.put("skflag", "〇");
		} else {
			params.put("skflag", "");
		}
		if (document.isRecommendation()) {
			params.put("ssflag", "〇");
		} else {
			params.put("ssflag", "");
		}
		if (document.isPersonalInfo()) {
			params.put("kdflag", "〇");
		} else {
			params.put("kdflag", "");
		}
		return params;
	}

	/**
	 * サーバーに保存されているファイルを取得して、byte配列に変換する
	 *
	 * @param fileName ファイル名
	 * @return ファイルのbyte配列
	 * @throws IOException ファイル取得エラー
	 */
	public byte[] getPdfFile(String fileName) throws IOException {
		FileSystem fs = FileSystems.getDefault();
		Path p = fs.getPath(fileName);
		byte[] bytes = Files.readAllBytes(p);
		return bytes;
	}

	/**
	 * 入力された文字の誤字脱字チェックをする。
	 *
	 * @param sentence 入力文字
	 * @return ResponseEntity
	 */
	public ResEntity errorCheckAi(String sentence) {
		if (sentence != null) {
			sentence = sentence.replaceAll("[\r\n]", " ");
		}
		// APIへアクセスして、結果を取得
		String json = restTemplate.getForObject(URL, String.class, apiKey, sentence);

		// エンティティクラスを生成
		ResEntity responseEntity = new ResEntity();

		// jsonクラスへの変換失敗のため、例外処理
		try {
			// 変換クラスを生成し、文字列からjsonクラスへ変換する（例外の可能性あり）
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(json);

			//パラメータの抽出
			responseEntity.setResultId(node.get("resultID").asText());
			responseEntity.setStatus(node.get("status").asInt());
			responseEntity.setMessage(node.get("message").asText());
			responseEntity.setCheckedSentence(node.get("checkedSentence").asText());

			//alertパラメータの抽出(配列分取得する)
			//見つからない場合
			if (node.get("alerts") == null) {
				;
				//見つかった場合
			} else {
				for (JsonNode alert : node.get("alerts")) {

					// データクラスの生成(alert1件分)
					ResponseData data = new ResponseData();
					data.setPos(alert.get("pos").asInt() + 1);
					data.setWord(alert.get("word").asText());
					data.setScore(String.format("%.1f", alert.get("score").asDouble() * 100));
					var arrays = alert.get("suggestions");
					List<String> lists = StreamSupport.stream(arrays.spliterator(), false)
							.map(
									e -> {
										return e.asText(); // Stringに変換
									})
							.collect(Collectors.toList());
					data.setSuggestions(lists);

					// 可変長配列の末尾に追加
					responseEntity.getAlerts().add(data);

				}
			}
			//			}
		} catch (Exception e) {
		}
		return responseEntity;

	}

}