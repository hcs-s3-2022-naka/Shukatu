package jp.ac.hcs.ShukatsuPortal.jobapp;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import jp.ac.hcs.ShukatsuPortal.WebConfig;
import jp.ac.hcs.ShukatsuPortal.user.UserData;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;


/**
 *	JobAppServiceに関するメソッドの単体テスト
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class JobAppServiceTest {

	@Autowired
	JobAppService jobAppService;

	//テスト用テンプレート
	//	@Test
	//	public void methodName() {
	//		// 1.Ready(データなどの事前準備が必要な場合に記述)
	//
	//		// 2.Do(対象のメソッドを呼び出す)
	//
	//		// 3.Assert(結果を比較して、問題ないか確認する)
	//
	//		// 4.logs(実行結果が問題ないかをログで確認する)
	//
	//	}

	@Test
	public void testGetJobAppListStudent() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String name = "sample@gmail.com";
		// 2.Do(対象のメソッドを呼び出す)
		JobAppEntity jobAppEntity = jobAppService.getJobAppListStudent(name);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(jobAppEntity.getJobAppList().size(), 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testGetJobAppListStudent]jobAppEntity:" + jobAppEntity.toString());
	}

	@Test
	public void testGetJobAppListTeacher() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		// 2.Do(対象のメソッドを呼び出す)
		JobAppEntity jobAppEntity = jobAppService.getJobAppListTeacher();
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(jobAppEntity.getJobAppList().size(), 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testGetJobAppListTeacher]jobAppEntity:" + jobAppEntity.toString());
	}

	@Test
	public void testGetJobAppDetail() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "1";
		// 2.Do(対象のメソッドを呼び出す)
		JobAppData jobAppData = jobAppService.getJobAppDetail(appId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(jobAppData);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testGetJobAppDetail]jobAppData:" + jobAppData.toString());

	}

	@Test
	public void testGetJobAppCompany() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "1";
		// 2.Do(対象のメソッドを呼び出す)
		JobAppEntity jobAppEntity = jobAppService.getJobAppCompany(appId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(jobAppEntity);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testGetJobAppCompany]jobAppEntity:" + jobAppEntity.toString());
	}

	@Test
	public void testInsertJobApp_教師_DB名前あり() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobAppForm form = new JobAppForm();
		List<String> appCompanys = new ArrayList<String>();
		form.setApplyClass("S3A1");
		form.setApplyNumber(24);
		form.setAppStartDate("2021-04-01");
		form.setAppStartTime("16:50");
		form.setAppFinishDate("");
		form.setAppFinishTime("");
		form.setAppAddress("HCS");
		form.setAppContents(1);
		appCompanys.add("HCS");
		appCompanys.add("SCC");
		form.setAppCompany(appCompanys);
		form.setAppStartAbsence("2021-04-01");
		form.setAppFinishAbsence("2021-04-01");
		form.setAppLeaveDate("2021-04-01");
		form.setAppLateDate("2021-04-01");
		form.setAppLeaveTime("17:00");
		form.setAppLateTime("17:00");
		form.setDeadline("2021-04-01");
		form.setSummaryFlag(true);
		form.setDocumentFlag(true);
		form.setResume(true);
		form.setRecommendation(true);
		form.setUniversityCrtificate(true);
		form.setHcsTranscript(true);
		form.setHcsCrtificate(true);
		form.setHealthCheckCertificate(true);
		form.setHighscoolTranscript(true);
		form.setUniversityTranscript(true);
		form.setPersonalInfo(true);
		String userId = "master@gmail.com";
		String role = "TEACHER";
		// 2.Do(対象のメソッドを呼び出す)
		int rowNumber = jobAppService.insertJobApp(form, userId, role);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(rowNumber, 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testInsertJobApp_教師_DB名前あり]rowNumber:" + rowNumber);
	}

	@Test
	public void testInsertJobApp_教師_DB名前なし() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobAppForm form = new JobAppForm();
		List<String> appCompanys = new ArrayList<String>();
		form.setApplyClass("S3A1");
		form.setApplyNumber(40);
		form.setAppStartDate("2021-04-01");
		form.setAppStartTime("16:50");
		form.setAppFinishDate("");
		form.setAppFinishTime("");
		form.setAppAddress("HCS");
		form.setAppContents(1);
		appCompanys.add("HCS");
		appCompanys.add("SCC");
		form.setAppCompany(appCompanys);
		form.setAppStartAbsence("2021-04-01");
		form.setAppFinishAbsence("2021-04-01");
		form.setAppLeaveDate("2021-04-01");
		form.setAppLateDate("2021-04-01");
		form.setAppLeaveTime("17:00");
		form.setAppLateTime("17:00");
		form.setDeadline("2021-04-01");
		form.setSummaryFlag(true);
		form.setDocumentFlag(true);
		form.setResume(true);
		form.setRecommendation(true);
		form.setUniversityCrtificate(true);
		form.setHcsTranscript(true);
		form.setHcsCrtificate(true);
		form.setHealthCheckCertificate(true);
		form.setHighscoolTranscript(true);
		form.setUniversityTranscript(true);
		form.setPersonalInfo(true);
		String userId = "master@gmail.com";
		String role = "TEACHER";
		// 2.Do(対象のメソッドを呼び出す)
		int rowNumber = jobAppService.insertJobApp(form, userId, role);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(rowNumber, 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testInsertJobApp_教師_DB名前なし]rowNumber:" + rowNumber);
	}

	@Test
	public void testInsertJobApp_学生() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobAppForm form = new JobAppForm();
		List<String> appCompanys = new ArrayList<String>();
		form.setApplyClass("S3A1");
		form.setApplyNumber(24);
		form.setAppStartDate("2021-04-01");
		form.setAppStartTime("16:50");
		form.setAppFinishDate("");
		form.setAppFinishTime("");
		form.setAppAddress("HCS");
		form.setAppContents(1);
		appCompanys.add("HCS");
		appCompanys.add("SCC");
		form.setAppCompany(appCompanys);
		form.setAppStartAbsence("2021-04-01");
		form.setAppFinishAbsence("2021-04-01");
		form.setAppLeaveDate("2021-04-01");
		form.setAppLateDate("2021-04-01");
		form.setAppLeaveTime("17:00");
		form.setAppLateTime("17:00");
		form.setDeadline("2021-04-01");
		form.setSummaryFlag(true);
		form.setDocumentFlag(true);
		form.setResume(true);
		form.setRecommendation(true);
		form.setUniversityCrtificate(true);
		form.setHcsTranscript(true);
		form.setHcsCrtificate(true);
		form.setHealthCheckCertificate(true);
		form.setHighscoolTranscript(true);
		form.setUniversityTranscript(true);
		form.setPersonalInfo(true);
		String userId = "sample@gmail.com";
		String role = "STUDENT";
		// 2.Do(対象のメソッドを呼び出す)
		int rowNumber = jobAppService.insertJobApp(form, userId, role);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(rowNumber, 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testInsertJobApp_学生]rowNumber:" + rowNumber);
	}

	@Test
	public void testInsertJobApp_書類なし() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobAppForm form = new JobAppForm();
		List<String> appCompanys = new ArrayList<String>();
		form.setApplyClass("S3A1");
		form.setApplyNumber(24);
		form.setAppStartDate("2021-04-01");
		form.setAppStartTime("16:50");
		form.setAppFinishDate("");
		form.setAppFinishTime("");
		form.setAppAddress("HCS");
		form.setAppContents(1);
		appCompanys.add("HCS");
		appCompanys.add("SCC");
		form.setAppCompany(appCompanys);
		form.setAppStartAbsence("2021-04-01");
		form.setAppFinishAbsence("2021-04-01");
		form.setAppLeaveDate("2021-04-01");
		form.setAppLateDate("2021-04-01");
		form.setAppLeaveTime("17:00");
		form.setAppLateTime("17:00");
		form.setDeadline("2021-04-01");
		form.setSummaryFlag(false);
		form.setDocumentFlag(false);
		form.setResume(false);
		form.setRecommendation(false);
		form.setUniversityCrtificate(false);
		form.setHcsTranscript(false);
		form.setHcsCrtificate(false);
		form.setHealthCheckCertificate(false);
		form.setHighscoolTranscript(false);
		form.setUniversityTranscript(false);
		form.setPersonalInfo(false);
		String userId = "sample@gmail.com";
		String role = "STUDENT";
		// 2.Do(対象のメソッドを呼び出す)
		int rowNumber = jobAppService.insertJobApp(form, userId, role);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(rowNumber, 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testInsertJobApp_書類なし]rowNumber:" + rowNumber);
	}

	@Test
	public void testInsertJobApp_日付間違い() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobAppForm form = new JobAppForm();
		List<String> appCompanys = new ArrayList<String>();
		form.setApplyClass("S3A1");
		form.setApplyNumber(24);
		form.setAppStartDate("2021-04-01");
		form.setAppStartTime("16:50");
		form.setAppFinishDate("2021-03-01");
		form.setAppFinishTime("16:50");
		form.setAppAddress("HCS");
		form.setAppContents(1);
		appCompanys.add("HCS");
		appCompanys.add("SCC");
		form.setAppCompany(appCompanys);
		form.setAppStartAbsence("2021-04-01");
		form.setAppFinishAbsence("2021-04-01");
		form.setAppLeaveDate("2021-04-01");
		form.setAppLateDate("2021-04-01");
		form.setAppLeaveTime("17:00");
		form.setAppLateTime("17:00");
		form.setDeadline("2021-04-01");
		form.setSummaryFlag(true);
		form.setDocumentFlag(true);
		form.setResume(true);
		form.setRecommendation(true);
		form.setUniversityCrtificate(true);
		form.setHcsTranscript(true);
		form.setHcsCrtificate(true);
		form.setHealthCheckCertificate(true);
		form.setHighscoolTranscript(true);
		form.setUniversityTranscript(true);
		form.setPersonalInfo(true);
		String userId = "sample@gmail.com";
		String role = "STUDENT";
		// 2.Do(対象のメソッドを呼び出す)
		int rowNumber = jobAppService.insertJobApp(form, userId, role);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(rowNumber, 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testInsertJobApp]rowNumber:" + rowNumber);
	}


	@Test
	public void testNullReturn() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String str = "";
		// 2.Do(対象のメソッドを呼び出す)
		str = jobAppService.nullReturn(str);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNull(str);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testNullReturn]str:" + str);
	}

	@Test
	public void testAppListCsvOut() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		List<String> appIds = new ArrayList<String>();
		appIds.add("1");
		appIds.add("2");
		// 2.Do(対象のメソッドを呼び出す)
		jobAppService.appListCsvOut(appIds);
		// 3.Assert(結果を比較して、問題ないか確認する)
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testAppListCsvOut]");
	}

	@Test
	public void testGetFile() throws IOException {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String fileName = WebConfig.OUTPUT_PATH + WebConfig.FILENAME_JOBAPPLIST_CSV;
		// 2.Do(対象のメソッドを呼び出す)
		byte[] bytes = jobAppService.getFile(fileName);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(bytes);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testGetFile]bytes:" + bytes.toString());
	}

	@Test
	public void testSearchJobAppStudent() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobAppFormForSearch search = new JobAppFormForSearch();
		String name = "sample@gmail.com";
		search.setAppCompany("株式会社ABC");
		search.setAppStartDate("2021-03-01");
		search.setAppStatus(1);
		search.setAppFinishDate("2021-03-01");
		search.setButtonValue(1);
		// 2.Do(対象のメソッドを呼び出す)
		JobAppEntity jobAppEntity = jobAppService.searchJobAppStudent(search, name);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(jobAppEntity.getJobAppList().size(), 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testSearchJobAppStudent]jobAppEntity:" + jobAppEntity.toString());
	}

	@Test
	public void testSearchJobAppTeacher() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobAppFormForSearch search = new JobAppFormForSearch();
		search.setApplyClass("S3A1");
		search.setApplyNumber(24);
		search.setApplyName("中野知歩");
		search.setAppCompany("株式会社ABC");
		search.setAppStartDate("2021-03-01");
		search.setAppStatus(1);
		search.setAppFinishDate("2021-03-01");
		search.setButtonValue(1);
		// 2.Do(対象のメソッドを呼び出す)
		JobAppEntity jobAppEntity = jobAppService.searchJobAppTeacher(search);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(jobAppEntity.getJobAppList().size(), 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testSearchJobAppTeacher]jobAppEntity:" + jobAppEntity.toString());
	}

	@Test
	public void testSearchSummaryStudent() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobAppFormForSearch search = new JobAppFormForSearch();
		String name = "sample@gmail.com";
		search.setAppCompany("株式会社SCC");
		search.setAppStartDate("2021-03-03");
		search.setAppStatus(5);
		search.setAppFinishDate("2021-03-03");
		// 2.Do(対象のメソッドを呼び出す)
		JobAppEntity jobAppEntity = jobAppService.searchSummaryStudent(search, name);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(jobAppEntity.getJobAppList().size(), 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testSearchSummaryStudent]jobAppEntity:" + jobAppEntity.toString());
	}

	@Test
	public void testSearchSummaryTeacher() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobAppFormForSearch search = new JobAppFormForSearch();
		search.setApplyClass("S3A1");
		search.setApplyNumber(24);
		search.setApplyName("中野知歩");
		search.setAppCompany("株式会社SCC");
		search.setAppStartDate("2021-03-03");
		search.setAppStatus(5);
		search.setAppFinishDate("2021-03-03");
		// 2.Do(対象のメソッドを呼び出す)
		JobAppEntity jobAppEntity = jobAppService.searchSummaryTeacher(search);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(jobAppEntity.getJobAppList().size(), 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testSearchSummaryTeacher]jobAppEntity:" + jobAppEntity.toString());
	}

	@Test
	public void testSelectUser() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "sample@gmail.com";
		// 2.Do(対象のメソッドを呼び出す)
		UserData userData = jobAppService.selectUser(userId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(userData);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testSelectUser]userData:" + userData);
	}

	@Test
	public void testSelectUserName() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String inputClass = "S3A1";
		int inputNumber = 24;
		// 2.Do(対象のメソッドを呼び出す)
		String name = jobAppService.selectUserName(inputClass, inputNumber);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(name);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testSelectUserName]name:" + name);
	}

	@Test
	public void testUpdateApprovalFlag() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "1";
		// 2.Do(対象のメソッドを呼び出す)
		int result = jobAppService.updateApprovalFlag(appId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(result, 0);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testUpdateApprovalFlag]result:" + result);
	}

	@Test
	public void testUpdateApproval() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "1";
		// 2.Do(対象のメソッドを呼び出す)
		int result = jobAppService.updateApproval(appId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(result, 0);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[updateApproval]result:" + result);
	}

	@Test
	public void testUpdateRemand() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobAppFormForRemand form = new JobAppFormForRemand();
		form.setAppId("1");
		form.setIndicate("aaaaa");
		// 2.Do(対象のメソッドを呼び出す)
		int result = jobAppService.updateRemand(form);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(result, 0);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[updateRemand]result:" + result);
	}

	@Test
	public void testUpdateJobApp_ユーザなし() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "6";
		List<String> appCompanys = new ArrayList<String>();
		JobAppFormForUpdate form = new JobAppFormForUpdate();
		form.setApplyClass("S3A3");
		form.setApplyNumber(24);
		form.setAppStartDate("2021-04-01");
		form.setAppStartTime("16:50");
		form.setAppFinishDate("");
		form.setAppFinishTime("");
		form.setAppAddress("HCS");
		form.setAppContents(1);
		appCompanys.add("HCS");
		appCompanys.add("SCC");
		form.setAppCompany(appCompanys);
		form.setAppStartAbsence("2021-04-01");
		form.setAppFinishAbsence("2021-04-01");
		form.setAppLeaveDate("2021-04-01");
		form.setAppLateDate("2021-04-01");
		form.setAppLeaveTime("17:00");
		form.setAppLateTime("17:00");
		form.setDeadline("2021-04-01");
		form.setSummaryFlag(true);
		form.setDocumentFlag(true);
		form.setResume(true);
		form.setRecommendation(true);
		form.setUniversityCrtificate(true);
		form.setHcsTranscript(true);
		form.setHcsCrtificate(true);
		form.setHealthCheckCertificate(true);
		form.setHighscoolTranscript(true);
		form.setUniversityTranscript(true);
		form.setPersonalInfo(true);
		// 2.Do(対象のメソッドを呼び出す)
		int rowNumber = jobAppService.updateJobApp(appId, form);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(rowNumber, 0);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testUpdateJobApp_ユーザなし]rowNumber:" + rowNumber);
	}


	@Test
	public void testUpdateJobApp_書類あり() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "6";
		List<String> appCompanys = new ArrayList<String>();
		JobAppFormForUpdate form = new JobAppFormForUpdate();
		form.setApplyClass("S3A1");
		form.setApplyNumber(24);
		form.setAppStartDate("2021-04-01");
		form.setAppStartTime("16:50");
		form.setAppFinishDate("");
		form.setAppFinishTime("");
		form.setAppAddress("HCS");
		form.setAppContents(1);
		appCompanys.add("HCS");
		appCompanys.add("SCC");
		form.setAppCompany(appCompanys);
		form.setAppStartAbsence("2021-04-01");
		form.setAppFinishAbsence("2021-04-01");
		form.setAppLeaveDate("2021-04-01");
		form.setAppLateDate("2021-04-01");
		form.setAppLeaveTime("17:00");
		form.setAppLateTime("17:00");
		form.setDeadline("2021-04-01");
		form.setSummaryFlag(true);
		form.setDocumentFlag(true);
		form.setResume(true);
		form.setRecommendation(true);
		form.setUniversityCrtificate(true);
		form.setHcsTranscript(true);
		form.setHcsCrtificate(true);
		form.setHealthCheckCertificate(true);
		form.setHighscoolTranscript(true);
		form.setUniversityTranscript(true);
		form.setPersonalInfo(true);
		// 2.Do(対象のメソッドを呼び出す)
		int rowNumber = jobAppService.updateJobApp(appId, form);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(rowNumber, 0);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testUpdateJobApp_書類あり]rowNumber:" + rowNumber);
	}

	@Test
	public void testUpdateJobApp_書類なし() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "6";
		List<String> appCompanys = new ArrayList<String>();
		JobAppFormForUpdate form = new JobAppFormForUpdate();
		form.setApplyClass("S3A1");
		form.setApplyNumber(24);
		form.setAppStartDate("2021-04-01");
		form.setAppStartTime("16:50");
		form.setAppFinishDate("");
		form.setAppFinishTime("");
		form.setAppAddress("HCS");
		form.setAppContents(1);
		appCompanys.add("HCS");
		appCompanys.add("SCC");
		form.setAppCompany(appCompanys);
		form.setAppStartAbsence("2021-04-01");
		form.setAppFinishAbsence("2021-04-01");
		form.setAppLeaveDate("2021-04-01");
		form.setAppLateDate("2021-04-01");
		form.setAppLeaveTime("17:00");
		form.setAppLateTime("17:00");
		form.setDeadline("2021-04-01");
		form.setSummaryFlag(false);
		form.setDocumentFlag(false);
		form.setResume(false);
		form.setRecommendation(false);
		form.setUniversityCrtificate(false);
		form.setHcsTranscript(false);
		form.setHcsCrtificate(false);
		form.setHealthCheckCertificate(false);
		form.setHighscoolTranscript(false);
		form.setUniversityTranscript(false);
		form.setPersonalInfo(false);
		// 2.Do(対象のメソッドを呼び出す)
		int rowNumber = jobAppService.updateJobApp(appId, form);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(rowNumber, 0);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testUpdateJobApp_書類なし]rowNumber:" + rowNumber);
	}

	@Test
	public void testUpdateJobApp_日付間違い() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "6";
		List<String> appCompanys = new ArrayList<String>();
		JobAppFormForUpdate form = new JobAppFormForUpdate();
		form.setApplyClass("S3A1");
		form.setApplyNumber(24);
		form.setAppStartDate("2021-04-01");
		form.setAppStartTime("16:50");
		form.setAppFinishDate("2021-03-01");
		form.setAppFinishTime("16:50");
		form.setAppAddress("HCS");
		form.setAppContents(1);
		appCompanys.add("HCS");
		appCompanys.add("SCC");
		form.setAppCompany(appCompanys);
		form.setAppStartAbsence("2021-04-01");
		form.setAppFinishAbsence("2021-04-01");
		form.setAppLeaveDate("2021-04-01");
		form.setAppLateDate("2021-04-01");
		form.setAppLeaveTime("17:00");
		form.setAppLateTime("17:00");
		form.setDeadline("2021-04-01");
		form.setSummaryFlag(true);
		form.setDocumentFlag(true);
		form.setResume(true);
		form.setRecommendation(true);
		form.setUniversityCrtificate(true);
		form.setHcsTranscript(true);
		form.setHcsCrtificate(true);
		form.setHealthCheckCertificate(true);
		form.setHighscoolTranscript(true);
		form.setUniversityTranscript(true);
		form.setPersonalInfo(true);
		// 2.Do(対象のメソッドを呼び出す)
		int rowNumber = jobAppService.updateJobApp(appId, form);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(rowNumber, 0);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testUpdateJobApp_日付間違い]rowNumber:" + rowNumber);
	}

	@Test
	public void testGetjobAppDocument() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "1";
		// 2.Do(対象のメソッドを呼び出す)
		JobAppData jobAppDocument = jobAppService.getjobAppDocument(appId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(jobAppDocument);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[getjobAppDocument]jobAppDocument:" + jobAppDocument);
	}

	@Test
	public void testGetJobAppDocumentList() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "1";
		JobAppEntity entity = new JobAppEntity();
		// 2.Do(対象のメソッドを呼び出す)
		JobAppEntity entity2 = jobAppService.getJobAppDocumentList(appId, entity);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertSame(entity2.getJobAppList().size(), 0);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testGetJobAppDocumentList]entity2:" + entity2);
	}

	@Test
	public void testCancel() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "1";
		// 2.Do(対象のメソッドを呼び出す)
		int rowNumber = jobAppService.cancel(appId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(rowNumber, 0);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testCancel]rowNumber:" + rowNumber);
	}

	@Test
	public void testChangeJobApp() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobAppData data = new JobAppData();
		// 2.Do(対象のメソッドを呼び出す)
		JobAppFormForUpdate change = jobAppService.changeJobApp(data);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(change);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testChangeJobApp]change:" + change);
	}

	@Test
	public void testGetJobAppDetailChange() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "1";
		// 2.Do(対象のメソッドを呼び出す)
		JobAppForm jobAppForm = jobAppService.getJobAppDetailChange(appId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(jobAppForm);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testGetJobAppDetailChange]jobAppForm:" + jobAppForm);
	}

	@Test
	public void testAutoSetFinish_18時以降() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String startTimeS = "18";
		String startMinuteS = "00";
		JobAppForm form = new JobAppForm();
		// 2.Do(対象のメソッドを呼び出す)
		JobAppForm jobAppForm = jobAppService.autoSetFinish(startTimeS, startMinuteS, form);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(jobAppForm);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testAutoSetFinish_18時以降]jobAppForm:" + jobAppForm);
	}

	@Test
	public void testAutoSetFinish_0時以上4時未満_分が0分() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String startTimeS = "00";
		String startMinuteS = "00";
		JobAppForm form = new JobAppForm();
		// 2.Do(対象のメソッドを呼び出す)
		JobAppForm jobAppForm = jobAppService.autoSetFinish(startTimeS, startMinuteS, form);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(jobAppForm);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testAutoSetFinish_0時以上4時未満_分が0分]jobAppForm:" + jobAppForm);
	}

	@Test
	public void testAutoSetFinish_0時以上4時未満_分が1桁分() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String startTimeS = "00";
		String startMinuteS = "01";
		JobAppForm form = new JobAppForm();
		// 2.Do(対象のメソッドを呼び出す)
		JobAppForm jobAppForm = jobAppService.autoSetFinish(startTimeS, startMinuteS, form);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(jobAppForm);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testAutoSetFinish_0時以上4時未満_分が1桁分]jobAppForm:" + jobAppForm);
	}

	@Test
	public void testAutoSetFinish_0時以上4時未満_分が2桁分() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String startTimeS = "00";
		String startMinuteS = "10";
		JobAppForm form = new JobAppForm();
		// 2.Do(対象のメソッドを呼び出す)
		JobAppForm jobAppForm = jobAppService.autoSetFinish(startTimeS, startMinuteS, form);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(jobAppForm);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testAutoSetFinish_0時以上4時未満_分が2桁分]jobAppForm:" + jobAppForm);
	}

	@Test
	public void testAutoSetFinish_4時以上_分が0分() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String startTimeS = "04";
		String startMinuteS = "00";
		JobAppForm form = new JobAppForm();
		// 2.Do(対象のメソッドを呼び出す)
		JobAppForm jobAppForm = jobAppService.autoSetFinish(startTimeS, startMinuteS, form);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(jobAppForm);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testAutoSetFinish_4時以上_分が0分]jobAppForm:" + jobAppForm);
	}

	@Test
	public void testAutoSetFinish_4時以上_分が1桁分() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String startTimeS = "04";
		String startMinuteS = "01";
		JobAppForm form = new JobAppForm();
		// 2.Do(対象のメソッドを呼び出す)
		JobAppForm jobAppForm = jobAppService.autoSetFinish(startTimeS, startMinuteS, form);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(jobAppForm);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testAutoSetFinish_4時以上_分が1桁分]jobAppForm:" + jobAppForm);
	}

	@Test
	public void testAutoSetFinish_4時以上_分が2桁分() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String startTimeS = "04";
		String startMinuteS = "10";
		JobAppForm form = new JobAppForm();
		// 2.Do(対象のメソッドを呼び出す)
		JobAppForm jobAppForm = jobAppService.autoSetFinish(startTimeS, startMinuteS, form);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(jobAppForm);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testAutoSetFinish_4時以上_分が2桁分]jobAppForm:" + jobAppForm);
	}

	@Test
	public void testAutoSetFinish_不正入力負の数() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String startTimeS = "-1";
		String startMinuteS = "10";
		JobAppForm form = new JobAppForm();
		// 2.Do(対象のメソッドを呼び出す)
		JobAppForm jobAppForm = jobAppService.autoSetFinish(startTimeS, startMinuteS, form);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(jobAppForm);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testAutoSetFinish_不正入力負の数]jobAppForm:" + jobAppForm);
	}

	@Test
	public void testAutoSetFinish_不正入力24時以降() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String startTimeS = "24";
		String startMinuteS = "10";
		JobAppForm form = new JobAppForm();
		// 2.Do(対象のメソッドを呼び出す)
		JobAppForm jobAppForm = jobAppService.autoSetFinish(startTimeS, startMinuteS, form);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(jobAppForm);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testAutoSetFinish_不正入力24時以降]jobAppForm:" + jobAppForm);
	}

	@Test
	public void testAutoSetFinishSecond_18時以降() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String startTimeS = "18";
		String startMinuteS = "00";
		JobAppFormForUpdate form = new JobAppFormForUpdate();
		// 2.Do(対象のメソッドを呼び出す)
		JobAppFormForUpdate jobAppFormForUpdate = jobAppService.autoSetFinishSecond(startTimeS, startMinuteS, form);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(jobAppFormForUpdate);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testAutoSetFinishSecond_18時以降]jobAppFormForUpdate:" + jobAppFormForUpdate);
	}

	@Test
	public void testAutoSetFinishSecond_0時以上4時未満_分が0分() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String startTimeS = "00";
		String startMinuteS = "00";
		JobAppFormForUpdate form = new JobAppFormForUpdate();
		// 2.Do(対象のメソッドを呼び出す)
		JobAppFormForUpdate jobAppFormForUpdate = jobAppService.autoSetFinishSecond(startTimeS, startMinuteS, form);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(jobAppFormForUpdate);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testAutoSetFinishSecond_0時以上4時未満_分が0分]jobAppFormForUpdate:" + jobAppFormForUpdate);
	}

	@Test
	public void testAutoSetFinishSecond_0時以上4時未満_分が1桁分() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String startTimeS = "00";
		String startMinuteS = "01";
		JobAppFormForUpdate form = new JobAppFormForUpdate();
		// 2.Do(対象のメソッドを呼び出す)
		JobAppFormForUpdate jobAppFormForUpdate = jobAppService.autoSetFinishSecond(startTimeS, startMinuteS, form);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(jobAppFormForUpdate);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testAutoSetFinishSecond_0時以上4時未満_分が1桁分]jobAppFormForUpdate:" + jobAppFormForUpdate);
	}

	@Test
	public void testAutoSetFinishSecond_0時以上4時未満_分が2桁分() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String startTimeS = "00";
		String startMinuteS = "10";
		JobAppFormForUpdate form = new JobAppFormForUpdate();
		// 2.Do(対象のメソッドを呼び出す)
		JobAppFormForUpdate jobAppFormForUpdate = jobAppService.autoSetFinishSecond(startTimeS, startMinuteS, form);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(jobAppFormForUpdate);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testAutoSetFinishSecond_0時以上4時未満_分が2桁分]jobAppFormForUpdate:" + jobAppFormForUpdate);
	}

	@Test
	public void testAutoSetFinishSecond_4時以上_分が0分() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String startTimeS = "04";
		String startMinuteS = "00";
		JobAppFormForUpdate form = new JobAppFormForUpdate();
		// 2.Do(対象のメソッドを呼び出す)
		JobAppFormForUpdate jobAppFormForUpdate = jobAppService.autoSetFinishSecond(startTimeS, startMinuteS, form);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(jobAppFormForUpdate);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testAutoSetFinishSecond_4時以上_分が0分]jobAppFormForUpdate:" + jobAppFormForUpdate);
	}

	@Test
	public void testAutoSetFinishSecond_4時以上_分が1桁分() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String startTimeS = "04";
		String startMinuteS = "01";
		JobAppFormForUpdate form = new JobAppFormForUpdate();
		// 2.Do(対象のメソッドを呼び出す)
		JobAppFormForUpdate jobAppFormForUpdate = jobAppService.autoSetFinishSecond(startTimeS, startMinuteS, form);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(jobAppFormForUpdate);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testAutoSetFinishSecond_4時以上_分が1桁分]jobAppFormForUpdate:" + jobAppFormForUpdate);
	}

	@Test
	public void testAutoSetFinishSecond_4時以上_分が2桁分() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String startTimeS = "04";
		String startMinuteS = "10";
		JobAppFormForUpdate form = new JobAppFormForUpdate();
		// 2.Do(対象のメソッドを呼び出す)
		JobAppFormForUpdate jobAppFormForUpdate = jobAppService.autoSetFinishSecond(startTimeS, startMinuteS, form);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(jobAppFormForUpdate);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testAutoSetFinishSecond_4時以上_分が2桁分]jobAppFormForUpdate:" + jobAppFormForUpdate);
	}

	@Test
	public void testAutoSetFinishSecond_不正入力負の数() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String startTimeS = "-1";
		String startMinuteS = "10";
		JobAppFormForUpdate form = new JobAppFormForUpdate();
		// 2.Do(対象のメソッドを呼び出す)
		JobAppFormForUpdate jobAppFormForUpdate = jobAppService.autoSetFinishSecond(startTimeS, startMinuteS, form);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(jobAppFormForUpdate);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testAutoSetFinishSecond_不正入力負の数]jobAppFormForUpdate:" + jobAppFormForUpdate);
	}

	@Test
	public void testAutoSetFinishSecond_不正入力24時以降() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String startTimeS = "24";
		String startMinuteS = "10";
		JobAppFormForUpdate form = new JobAppFormForUpdate();
		// 2.Do(対象のメソッドを呼び出す)
		JobAppFormForUpdate jobAppFormForUpdate = jobAppService.autoSetFinishSecond(startTimeS, startMinuteS, form);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(jobAppFormForUpdate);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testAutoSetFinishSecond_不正入力24時以降]jobAppFormForUpdate:" + jobAppFormForUpdate);
	}

	@Test
	public void testAppNotificationT() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "master@gmail.com";
		String notificateId = "1";
		// 2.Do(対象のメソッドを呼び出す)
		List<String> appIdList = jobAppService.appNotificationT(userId, notificateId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(appIdList);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testAppNotificationT]appIdList:" + appIdList);
	}

	@Test
	public void testReportNotificationT() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "master@gmail.com";
		String notificateId = "2";
		// 2.Do(対象のメソッドを呼び出す)
		List<String> appIdList = jobAppService.reportNotificationT(userId, notificateId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(appIdList);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testReportNotificationT]appIdList:" + appIdList);
	}

	@Test
	public void testEventNotificationT() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "master@gmail.com";
		// 2.Do(対象のメソッドを呼び出す)
		List<String> appIdList = jobAppService.eventNotificationT(userId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(appIdList);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testEventNotificationT]appIdList:" + appIdList);
	}

	@Test
	public void testCourseNotificationT() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "master@gmail.com";
		String notificateId = "4";
		// 2.Do(対象のメソッドを呼び出す)
		List<String> appIdList = jobAppService.courseNotificationT(userId, notificateId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(appIdList);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testCourseNotificationT]appIdList:" + appIdList);
	}

	@Test
	public void testRemandAppNotificationS() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "sample@gmail.com";
		String notificateId = "5";
		// 2.Do(対象のメソッドを呼び出す)
		List<String> appIdList = jobAppService.remandAppNotificationS(userId, notificateId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(appIdList);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testRemandAppNotificationS]appIdList:" + appIdList);
	}

	@Test
	public void testApprovalAppNotificationS() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "sample@gmail.com";
		String notificateId = "6";
		// 2.Do(対象のメソッドを呼び出す)
		List<String> appIdList = jobAppService.approvalAppNotificationS(userId, notificateId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(appIdList);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testApprovalAppNotificationS]appIdList:" + appIdList);
	}

	@Test
	public void testRemandReportNotificationS() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "sample@gmail.com";
		String notificateId = "7";
		// 2.Do(対象のメソッドを呼び出す)
		List<String> appIdList = jobAppService.remandReportNotificationS(userId, notificateId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(appIdList);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testRemandReportNotificationS]appIdList:" + appIdList);
	}

	@Test
	public void testApprovalReportNotificationS() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "sample@gmail.com";
		String notificateId = "8";
		// 2.Do(対象のメソッドを呼び出す)
		List<String> appIdList = jobAppService.approvalReportNotificationS(userId, notificateId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(appIdList);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testApprovalReportNotificationS]appIdList:" + appIdList);
	}

	@Test
	public void testEventIncompNotificationS() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "sample@gmail.com";
		String notificateId = "9";
		// 2.Do(対象のメソッドを呼び出す)
		List<String> appIdList = jobAppService.eventIncompNotificationS(userId, notificateId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(appIdList);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testEventIncompNotificationS]appIdList:" + appIdList);
	}

	@Test
	public void testNotSubmittedNotificationS() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "sample@gmail.com";
		String notificateId = "10";
		// 2.Do(対象のメソッドを呼び出す)
		List<String> appIdList = jobAppService.notSubmittedNotificationS(userId, notificateId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(appIdList);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testNotSubmittedNotificationS]appIdList:" + appIdList);
	}

	@Test
	public void testEventNotificationS() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "sample@gmail.com";
		// 2.Do(対象のメソッドを呼び出す)
		List<String> appIdList = jobAppService.eventNotificationS(userId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(appIdList);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testEventNotificationS]appIdList:" + appIdList);
	}

	@Test
	public void testDocumentNotificationS() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "sample@gmail.com";
		String notificateId = "12";
		// 2.Do(対象のメソッドを呼び出す)
		List<String> appIdList = jobAppService.documentNotificationS(userId, notificateId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(appIdList);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testDocumentNotificationS]appIdList:" + appIdList);
	}

	@Test
	public void testDocumentLimitNotificationS() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "sample@gmail.com";
		String notificateId = "13";
		// 2.Do(対象のメソッドを呼び出す)
		List<String> appIdList = jobAppService.documentLimitNotificationS(userId, notificateId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(appIdList);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testDocumentLimitNotificationS]appIdList:" + appIdList);
	}

	@Test
	public void testJobAppDocument() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobAppFormForDocument form = new JobAppFormForDocument();
		// 2.Do(対象のメソッドを呼び出す)
		int result = jobAppService.jobAppDocument(form);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertSame(result, 0);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testJobAppDocument]result:" + result);
	}

	@Test
	public void testUpdateSummary() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "6";
		// 2.Do(対象のメソッドを呼び出す)
		int result = jobAppService.updateSummary(appId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(result, 0);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testUpdateSummary]result:" + result);
	}

	@Test
	public void testJobAppNotificationList() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		List<String> appIdList = new ArrayList<String>();
		appIdList.add("1");

		// 2.Do(対象のメソッドを呼び出す)
		JobAppEntity entity = jobAppService.JobAppNotificationList(appIdList);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(entity);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testJobAppNotificationList]entity:" + entity);
	}

	@Test
	public void testJobAppPdfFalse() throws JRException {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId="4";
		boolean flag = true;
		jobAppService.jobAppPdf(appId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertTrue(flag);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testJobAppPdfFalse]flag:" + flag);
	}

	@Test
	public void testJobAppPdfTrue() throws JRException {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId="6";
		boolean flag = true;
		jobAppService.jobAppPdf(appId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertTrue(flag);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testJobAppPdfTrue]flag:" + flag);
	}


	@Test
	public void testUpdateNotifiPushDate() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "sample@gmail.com";
		String listNum = "1";
		// 2.Do(対象のメソッドを呼び出す)
		int rowNumber = jobAppService.updateNotifiPushDate(userId, listNum);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(rowNumber, 0);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testUpdateNotifiPushDate]rowNumber:" + rowNumber);
	}

	@Test
	public void testGetPdfFile() throws IOException {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String fileName = "target/summary.pdf";
		// 2.Do(対象のメソッドを呼び出す)
		byte[] bytes = jobAppService.getPdfFile(fileName);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(bytes, 0);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testGetPdfFile]bytes:" + bytes);
	}

	@Test
	public void testErrorCheckAi()  {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String sentence = "こんばんｈ";
		// 2.Do(対象のメソッドを呼び出す)
		ResEntity entity = jobAppService.errorCheckAi(sentence);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(entity);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testErrorCheckAi]entity:" + entity);
	}

	@Test
	public void testErrorCheckAiNull()  {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String sentence = null;
		// 2.Do(対象のメソッドを呼び出す)
		ResEntity entity = jobAppService.errorCheckAi(sentence);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(entity);
		//4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testErrorCheckAiNull]entity:" + entity);
	}


}
