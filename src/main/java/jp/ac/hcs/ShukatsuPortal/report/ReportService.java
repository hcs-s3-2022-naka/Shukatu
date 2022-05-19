package jp.ac.hcs.ShukatsuPortal.report;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

/**
 * 受験報告に関する操作を行う
 */
@Service
public class ReportService {
	@Autowired
	ReportRepository reportRepository;

	/**
	 * 受験報告の一覧を取得する
	 * @return reportEntity
	 */
	public ReportEntity getReportList() {
		ReportEntity reportEntity;
		try {
			reportEntity = reportRepository.getReportList();
		}catch (DataAccessException e){
			reportEntity = null;
		}
		return reportEntity;
	}

	/**
	 * 受験報告の変更を行う
	 * @param update 更新内容
	 * @param user_id ユーザID
	 * @return result
	 */
	public boolean updateReport(ReportFormForUpdate update, String user_id) {
		Date date = new Date();
		//日付を形式変換
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		String edate = update.getExaminationDate().replace("T", " ");
		int rowNumber;
		ReportData reportData = new ReportData();
		reportData.setDepartment(update.getDepartment());
		reportData.setJobNumber(update.getJobNumber());
		reportData.setEnterpriseNameKana(update.getEnterpriseNameKana());
		reportData.setEnterpriseName(update.getEnterpriseName());
		reportData.setTestCategory(update.getTestCategory());
		reportData.setTestCategoryOther(update.getTestCategoryOther());
		try {
			reportData.setExaminationDate(sdFormat.parse(edate));
		} catch (ParseException e1) {
		}
		reportData.setExaminationLocation(update.getExaminationLocation());
		reportData.setLocationCategory(update.getLocationCategory());
		reportData.setLocationCategoryOther(update.getLocationCategoryOther());
		reportData.setStageCategory(update.getStageCategory());
		reportData.setStageCategoryOther(update.getStageCategoryOther());
		reportData.setContentCategory(update.getContentCategory());
		reportData.setContentCategoryOther(update.getContentCategoryOther());
		reportData.setResultDate(update.getResultDate());
		reportData.setPassFlag(update.isPassFlag());
		reportData.setResultCategory(update.getResultCategory());
		reportData.setAppropriateCategory(update.getAppropriateCategory());
		reportData.setAppropriateCategoryOther(update.getAppropriateCategoryOther());
		reportData.setWritingCategory(update.getWritingCategory());
		reportData.setWritingCategoryOther(update.getWritingCategoryOther());
		reportData.setInterviewCategory(update.getInterviewCategory());
		reportData.setInterviewCategoryOther(update.getInterviewCategoryOther());
		reportData.setGroupNumber(update.getGroupNumber());
		reportData.setOfficerNumber(update.getOfficerNumber());
		reportData.setOfficerRole(update.getOfficerRole());
		reportData.setInterviewTime(update.getInterviewTime());
		reportData.setGdThema(update.getGdThema());
		reportData.setContent(update.getContent());
		reportData.setRemarks(update.getRemarks());
		reportData.setReportStatus("承認前");
		reportData.setUpdateDate(date);
		reportData.setUpdateUserId(user_id);
		reportData.setReportId(update.getReportId());
		try {
		 rowNumber = reportRepository.updateReport(reportData);
		}catch (DataAccessException e) {
			rowNumber = 0;
		}
		return rowNumber > 0;
	}

	/**
	 * 選択された学生の受験報告詳細を表示
	 * @param iReportId 受験報告ID
	 * @return reportData
	 */
	public ReportData getReportListOne(int iReportId) {
		String reportId = Integer.valueOf(iReportId).toString();
		ReportData data;
		try {
			data = reportRepository.getReportListOne(reportId);
		}catch (DataAccessException e){
			data = null;
		}
		return data;
	}

	/**
	 * 受験報告情報を１件追加する
	 * @param form 受験報告内容
	 * @param user_id ユーザID
	 * @return boolean 可否
	 */
	public boolean insert(ReportForm form, String user_id) {
		Date date = new Date();
		//日付を形式変換
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		String edate = form.getExaminationDate().replace("T", " ");
		int rowNumber;
		ReportData reportData = new ReportData();
		reportData.setDepartment(form.getDepartment());
		reportData.setJobNumber(form.getJobNumber());
		reportData.setEnterpriseNameKana(form.getEnterpriseNameKana());
		reportData.setEnterpriseName(form.getEnterpriseName());
		reportData.setTestCategory(form.getTestCategory());
		reportData.setTestCategoryOther(form.getTestCategoryOther());
		try {
			reportData.setExaminationDate(sdFormat.parse(edate));
		} catch (ParseException e1) {
			return false;
		}
		reportData.setExaminationLocation(form.getExaminationLocation());
		reportData.setLocationCategory(form.getLocationCategory());
		reportData.setLocationCategoryOther(form.getLocationCategoryOther());
		reportData.setStageCategory(form.getStageCategory());
		reportData.setStageCategoryOther(form.getStageCategoryOther());
		reportData.setContentCategory(form.getContentCategory());
		reportData.setContentCategoryOther(form.getContentCategoryOther());
		reportData.setResultDate(form.getResultDate());
		reportData.setPassFlag(form.isPassFlag());
		reportData.setResultCategory(form.getResultCategory());
		reportData.setAppropriateCategory(form.getAppropriateCategory());
		reportData.setAppropriateCategoryOther(form.getAppropriateCategoryOther());
		reportData.setWritingCategory(form.getWritingCategory());
		reportData.setWritingCategoryOther(form.getWritingCategoryOther());
		reportData.setInterviewCategory(form.getInterviewCategory());
		reportData.setInterviewCategoryOther(form.getInterviewCategoryOther());
		reportData.setGroupNumber(form.getGroupNumber());
		reportData.setOfficerNumber(form.getOfficerNumber());
		reportData.setOfficerRole(form.getOfficerRole());
		reportData.setInterviewTime(form.getInterviewTime());
		reportData.setGdThema(form.getGdThema());
		reportData.setContent(form.getContent());
		reportData.setRemarks(form.getRemarks());
		reportData.setReportStatus("承認前");
		reportData.setRegistrationDate(date);
		reportData.setRegistrationUserId(user_id);
		try {
		 rowNumber = reportRepository.insertOne(reportData);
		}catch (DataAccessException e) {
			rowNumber = 0;
		}
		return rowNumber > 0;
	}

	/**
	 * 受験報告状態の更新
	 * @param userId ユーザID
	 * @param comment 指摘コメント
	 * @param reportStatus 受験報告状態
	 * @return result
	 */
	public boolean updateStatus(String userId, String comment, String reportStatus) {
		boolean result;
		try {
			result = reportRepository.updateStatus(userId, comment, reportStatus);
		}catch (DataAccessException e){
			result = false;
		}
		return result;
	}

	/**
	 * 受験報告をCSVファイルとしてサーバに保存する.
	 * @param year 指定年度
	 * @throws DataAccessException データアクセスエラー
	 */
	public void reportListCsvOut(String year) throws DataAccessException {
		reportRepository.reportlistCsvOut(year);
	}

	/**
	 * サーバーに保存されているファイルを取得して、byte配列に変換する.
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
	 * 絞込検索を行う
	 * @param filtering 選択した条件
	 * @param conditions 条件内容
	 * @return reportEntity
	 */
	public ReportEntity getReportList(String filtering, String conditions) {
		ReportEntity reportEntity = new ReportEntity();
		try {
			//入力された内容に応じてリポジトリを呼ぶ
			switch(filtering){
				case "学生氏名":
					reportEntity = reportRepository.getReportListByName(conditions);
					break;
				case "企業名":
					reportEntity = reportRepository.getReportListByEnterpriseName(conditions);
					break;
				case "状態":
					reportEntity = reportRepository.getReportListByStatus(conditions);
					break;
			}
		}catch (DataAccessException e){
			reportEntity = null;
		}
		return reportEntity;
	}

	/**
	 * 選択された学生の受験報告詳細(更新用)
	 * @param userId 指定したユーザのID
	 * @return form データをセットし直したフォーム
	 */
	public ReportFormForUpdate updateReportListOne(String userId) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		ReportFormForUpdate form = new ReportFormForUpdate();
		ReportData reportData = reportRepository.getReportListOne(userId);
		form.setReportId(reportData.getReportId());
		form.setDepartment(reportData.getDepartment());
		form.setJobNumber(reportData.getJobNumber());
		form.setEnterpriseNameKana(reportData.getEnterpriseNameKana());
		form.setEnterpriseName(reportData.getEnterpriseName());
		form.setTestCategory(reportData.getTestCategory());
		form.setTestCategoryOther(reportData.getTestCategory());
		form.setExaminationDate(dateFormat.format(reportData.getExaminationDate()));
		form.setExaminationLocation(reportData.getExaminationLocation());
		form.setLocationCategory(reportData.getLocationCategory());
		form.setLocationCategoryOther(reportData.getLocationCategoryOther());
		form.setStageCategory(reportData.getStageCategory());
		form.setStageCategoryOther(reportData.getStageCategoryOther());
		form.setContentCategory(reportData.getContentCategory());
		form.setContentCategoryOther(reportData.getContentCategoryOther());
		form.setResultDate(reportData.getResultDate());
		form.setPassFlag(reportData.isPassFlag());
		form.setResultCategory(reportData.getResultCategory());
		form.setAppropriateCategory(reportData.getAppropriateCategory());
		form.setAppropriateCategoryOther(reportData.getAppropriateCategoryOther());
		form.setWritingCategory(reportData.getStageCategory());
		form.setWritingCategoryOther(reportData.getWritingCategoryOther());
		form.setInterviewCategory(reportData.getInterviewCategory());
		form.setInterviewCategoryOther(reportData.getInterviewCategoryOther());
		form.setGroupNumber(reportData.getGroupNumber());
		form.setOfficerNumber(reportData.getOfficerNumber());
		form.setOfficerRole(reportData.getOfficerRole());
		form.setInterviewTime(reportData.getInterviewTime());
		form.setGdThema(reportData.getGdThema());
		form.setContent(reportData.getContent());
		form.setRemarks(reportData.getRemarks());
		return form;
	}
}
