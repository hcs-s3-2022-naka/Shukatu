package jp.ac.hcs.ShukatsuPortal.jobapp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

/**
 *	JobAppRepositoryに関するメソッドの単体テスト
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class JobAppRepositoryTest {

	@Autowired
	JobAppRepository jobAppRepository;

	@Test
	public void testGetJobAppListStudent() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String name = "sample@gmail.com";
		// 2.Do(対象のメソッドを呼び出す)
		JobAppEntity jobAppEntity = jobAppRepository.getJobAppListStudent(name);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(jobAppEntity.getJobAppList().size(), 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testGetJobAppListStudent]jobAppEntity:" + jobAppEntity.toString());
	}

	@Test
	public void testGetJobAppListTeacher() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		// 2.Do(対象のメソッドを呼び出す)
		JobAppEntity jobAppEntity = jobAppRepository.getJobAppListTeacher();
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(jobAppEntity.getJobAppList().size(), 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testGetJobAppListStudent]jobAppEntity:" + jobAppEntity.toString());
	}

	@Test
	public void testGetJobAppDetail() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "1";
		// 2.Do(対象のメソッドを呼び出す)
		JobAppData jobAppData = jobAppRepository.getJobAppDetail(appId);
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
		JobAppEntity jobAppEntity = jobAppRepository.getJobAppCompany(appId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(jobAppEntity);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testGetJobAppCompany]jobAppEntity:" + jobAppEntity.toString());
	}

	@Test
	public void testSearchJobAppListStudent() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobAppFormForSearch search = new JobAppFormForSearch();
		String name = "sample@gmail.com";
		search.setAppCompany("株式会社ABC");
		search.setAppStartDate("2021-03-01");
		search.setAppStatus(1);
		search.setAppFinishDate("2021-03-01");
		// 2.Do(対象のメソッドを呼び出す)
		JobAppEntity jobAppEntity = jobAppRepository.searchJobAppListStudent(search,name);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertSame(jobAppEntity.getJobAppList().size(), 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testSearchJobAppListStudent]jobAppEntity:" + jobAppEntity.toString());
	}

	@Test
	public void testSearchJobAppListStudentの検索条件なしテスト() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobAppFormForSearch search = new JobAppFormForSearch();
		String name = "sample@gmail.com";
		search.setAppCompany("");
		search.setAppStartDate("");
		search.setAppStatus(0);
		search.setAppFinishDate("");
		// 2.Do(対象のメソッドを呼び出す)
		JobAppEntity jobAppEntity = jobAppRepository.searchJobAppListStudent(search,name);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(jobAppEntity.getJobAppList().size(), 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testSearchJobAppListStudent]jobAppEntity:" + jobAppEntity.toString());
	}

	@Test
	public void testSearchJobAppListTeacher() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobAppFormForSearch search = new JobAppFormForSearch();
		search.setApplyClass("S3A1");
		search.setApplyNumber(24);
		search.setApplyName("中野知歩");
		search.setAppCompany("株式会社ABC");
		search.setAppStartDate("2021-03-01");
		search.setAppStatus(1);
		search.setAppFinishDate("2021-03-01");
		// 2.Do(対象のメソッドを呼び出す)
		JobAppEntity jobAppEntity = jobAppRepository.searchJobAppListTeacher(search);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertSame(jobAppEntity.getJobAppList().size(), 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testSearchJobAppListTeacher]jobAppEntity:" + jobAppEntity.toString());
	}

	@Test
	public void testSearchJobAppListTeacherの検索条件なしテスト() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobAppFormForSearch search = new JobAppFormForSearch();
		search.setApplyClass("");
		search.setApplyNumber(0);
		search.setApplyName("");
		search.setAppCompany("");
		search.setAppStartDate("");
		search.setAppStatus(0);
		search.setAppFinishDate("");
		// 2.Do(対象のメソッドを呼び出す)
		JobAppEntity jobAppEntity = jobAppRepository.searchJobAppListTeacher(search);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(jobAppEntity.getJobAppList().size(), 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testSearchJobAppListTeacher]jobAppEntity:" + jobAppEntity.toString());
	}

	@Test
	public void testInsertJobApp() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		Date date = new Date();
		List<String> companyList = new ArrayList<String>();
		companyList.add("株式会社VVV");
		JobAppData jobAppData = new JobAppData();
		jobAppData.setSummaryFlag(false);
		jobAppData.setDocumentFlag(false);
		jobAppData.setAppStartDate("2021-01-01");
		jobAppData.setAppStartTime("00:00");
		jobAppData.setAppFinishDate("2021-01-01");
		jobAppData.setAppFinishTime("00:00");
		jobAppData.setAppAddress("自宅");
		jobAppData.setAppContents(1);
		jobAppData.setAppContentOther(null);
		jobAppData.setAbsenceFlag(false);
		jobAppData.setAppStartAbsence(null);
		jobAppData.setAppFinishAbsence(null);
		jobAppData.setLeaveFlag(false);
		jobAppData.setAppLeaveDate(null);
		jobAppData.setAppLeaveTime(null);
		jobAppData.setLateFlag(false);
		jobAppData.setAppLateDate(null);
		jobAppData.setAppLateTime(null);
		jobAppData.setAppMemo(null);
		jobAppData.setApplyClass("S3A1");
		jobAppData.setApplyNumber(24);
		jobAppData.setApplyName("中野知歩");
		jobAppData.setAppDate(date);
		jobAppData.setAppCompany(companyList);
		// 2.Do(対象のメソッドを呼び出す)
		int result = jobAppRepository.insertJobApp(jobAppData);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotEquals(result, 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testInsertJobApp]result:" + result);
	}

	@Test
	public void testApplistCsvOut() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "1";
		boolean result = true;
		// 2.Do(対象のメソッドを呼び出す)
		jobAppRepository.applistCsvOut(appId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertTrue(result);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testApplistCsvOut]result:" + result);
	}

	@Test
	public void testInsertJobAppの分岐網羅1() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		Date date = new Date();
		List<String> companyList = new ArrayList<String>();
		companyList.add("株式会社XXX");
		JobAppData jobAppData = new JobAppData();
		jobAppData.setSummaryFlag(true);
		jobAppData.setDocumentFlag(false);
		jobAppData.setAppStartDate("2021-01-01");
		jobAppData.setAppStartTime("00:00");
		jobAppData.setAppFinishDate("2021-01-01");
		jobAppData.setAppFinishTime("00:00");
		jobAppData.setAppAddress("自宅");
		jobAppData.setAppContents(1);
		jobAppData.setAppContentOther(null);
		jobAppData.setAbsenceFlag(false);
		jobAppData.setAppStartAbsence(null);
		jobAppData.setAppFinishAbsence(null);
		jobAppData.setLeaveFlag(false);
		jobAppData.setAppLeaveDate(null);
		jobAppData.setAppLeaveTime(null);
		jobAppData.setLateFlag(false);
		jobAppData.setAppLateDate(null);
		jobAppData.setAppLateTime(null);
		jobAppData.setAppMemo(null);
		jobAppData.setApplyClass("S3A1");
		jobAppData.setApplyNumber(24);
		jobAppData.setApplyName("中野知歩");
		jobAppData.setAppDate(date);
		jobAppData.setAppCompany(companyList);
		// 2.Do(対象のメソッドを呼び出す)
		int result = jobAppRepository.insertJobApp(jobAppData);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotEquals(result, 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testInsertJobApp]result:" + result);
	}

	@Test
	public void testInsertJobAppの分岐網羅2() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		Date date = new Date();
		List<String> companyList = new ArrayList<String>();
		companyList.add("株式会社YYY");
		JobAppData jobAppData = new JobAppData();
		jobAppData.setSummaryFlag(true);
		jobAppData.setDocumentFlag(true);
		jobAppData.setAppStartDate("2021-01-01");
		jobAppData.setAppStartTime("00:00");
		jobAppData.setAppFinishDate("2021-01-01");
		jobAppData.setAppFinishTime("00:00");
		jobAppData.setAppAddress("自宅");
		jobAppData.setAppContents(1);
		jobAppData.setAppContentOther(null);
		jobAppData.setAbsenceFlag(false);
		jobAppData.setAppStartAbsence(null);
		jobAppData.setAppFinishAbsence(null);
		jobAppData.setLeaveFlag(false);
		jobAppData.setAppLeaveDate(null);
		jobAppData.setAppLeaveTime(null);
		jobAppData.setLateFlag(false);
		jobAppData.setAppLateDate(null);
		jobAppData.setAppLateTime(null);
		jobAppData.setAppMemo(null);
		jobAppData.setApplyClass("S3A1");
		jobAppData.setApplyNumber(24);
		jobAppData.setApplyName("中野知歩");
		jobAppData.setAppDate(date);
		jobAppData.setAppCompany(companyList);
		jobAppData.setResume(false);
		jobAppData.setRecommendation(false);
		jobAppData.setUniversityCrtificate(false);
		jobAppData.setHcsTranscript(false);
		jobAppData.setHcsCrtificate(false);
		jobAppData.setHealthCheckCertificate(false);
		jobAppData.setHighscoolTranscript(false);
		jobAppData.setUniversityTranscript(false);
		jobAppData.setPersonalInfo(false);
		// 2.Do(対象のメソッドを呼び出す)
		int result = jobAppRepository.insertJobApp(jobAppData);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotEquals(result, 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testInsertJobApp]result:" + result);
	}

	@Test
	public void testInsertJobAppの分岐網羅3() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		Date date = new Date();
		List<String> companyList = new ArrayList<String>();
		companyList.add("株式会社ZZZ");
		JobAppData jobAppData = new JobAppData();
		jobAppData.setSummaryFlag(true);
		jobAppData.setDocumentFlag(true);
		jobAppData.setAppStartDate("2021-01-01");
		jobAppData.setAppStartTime("00:00");
		jobAppData.setAppFinishDate("2021-01-01");
		jobAppData.setAppFinishTime("00:00");
		jobAppData.setAppAddress("自宅");
		jobAppData.setAppContents(1);
		jobAppData.setAppContentOther(null);
		jobAppData.setAbsenceFlag(false);
		jobAppData.setAppStartAbsence(null);
		jobAppData.setAppFinishAbsence(null);
		jobAppData.setLeaveFlag(false);
		jobAppData.setAppLeaveDate(null);
		jobAppData.setAppLeaveTime(null);
		jobAppData.setLateFlag(false);
		jobAppData.setAppLateDate(null);
		jobAppData.setAppLateTime(null);
		jobAppData.setAppMemo(null);
		jobAppData.setApplyClass("S3A1");
		jobAppData.setApplyNumber(24);
		jobAppData.setApplyName("中野知歩");
		jobAppData.setAppDate(date);
		jobAppData.setAppCompany(companyList);
		jobAppData.setResume(true);
		jobAppData.setRecommendation(true);
		jobAppData.setUniversityCrtificate(true);
		jobAppData.setHcsTranscript(true);
		jobAppData.setHcsCrtificate(true);
		jobAppData.setHealthCheckCertificate(true);
		jobAppData.setHighscoolTranscript(true);
		jobAppData.setUniversityTranscript(true);
		jobAppData.setPersonalInfo(true);
		jobAppData.setDeadline("2021-01-01");
		// 2.Do(対象のメソッドを呼び出す)
		int result = jobAppRepository.insertJobApp(jobAppData);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotEquals(result, 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testInsertJobApp]result:" + result);
	}


	@Test
	public void testSearchSumaryStudent() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobAppFormForSearch search = new JobAppFormForSearch();
		String name = "sample@gmail.com";
		search.setAppCompany("株式会社SCC");
		search.setAppStartDate("2021-03-03");
		search.setAppStatus(5);
		search.setAppFinishDate("2021-03-03");
		// 2.Do(対象のメソッドを呼び出す)
		JobAppEntity jobAppEntity = jobAppRepository.searchSummaryStudent(search,name);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(jobAppEntity.getJobAppList().size(), 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testSearchSummaryStudent]jobAppEntity:" + jobAppEntity.toString());
	}

	@Test
	public void testSearchSumaryStudentの検索条件なしテスト() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobAppFormForSearch search = new JobAppFormForSearch();
		String name = "sample@gmail.com";
		search.setAppCompany("");
		search.setAppStartDate("");
		search.setAppStatus(0);
		search.setAppFinishDate("");
		// 2.Do(対象のメソッドを呼び出す)
		JobAppEntity jobAppEntity = jobAppRepository.searchSummaryStudent(search,name);
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
		JobAppEntity jobAppEntity = jobAppRepository.searchSummaryTeacher(search);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(jobAppEntity.getJobAppList().size(), 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testSearchSummaryTeacher]jobAppEntity:" + jobAppEntity.toString());
	}

	@Test
	public void testSearchSummaryTeacherの検索条件なしテスト() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobAppFormForSearch search = new JobAppFormForSearch();
		search.setApplyClass("");
		search.setApplyNumber(0);
		search.setApplyName("");
		search.setAppCompany("");
		search.setAppStartDate("");
		search.setAppStatus(0);
		search.setAppFinishDate("");
		// 2.Do(対象のメソッドを呼び出す)
		JobAppEntity jobAppEntity = jobAppRepository.searchSummaryTeacher(search);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(jobAppEntity.getJobAppList().size(), 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testSearchSummaryTeacher]jobAppEntity:" + jobAppEntity.toString());
	}

	@Test
	public void testUpdateApprovalFlag() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "1";
		Date date = new Date();
		// 2.Do(対象のメソッドを呼び出す)
		int result = jobAppRepository.updateApprovalFlag(appId, date);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotEquals(result, 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testUpdateApprovalFlag]result:" + result);
	}

	@Test
	public void testUpdateApproval() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "1";
		Date date = new Date();
		// 2.Do(対象のメソッドを呼び出す)
		int result = jobAppRepository.updateApproval(appId, date);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotEquals(result, 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testUpdateApproval]result:" + result);
	}

	@Test
	public void testUpdateRemand() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "1";
		Date date = new Date();
		String indicate = "";
		// 2.Do(対象のメソッドを呼び出す)
		int result = jobAppRepository.updateRemand(appId, date, indicate);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotEquals(result, 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testUpdateRemand]result:" + result);
	}

	@Test
	public void testUpdateJobApp() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "1";
		Date date = new Date();
		List<String> companyList = new ArrayList<String>();
		companyList.add("株式会社ABC");
		JobAppData jobAppData = new JobAppData();
		jobAppData.setSummaryFlag(false);
		jobAppData.setDocumentFlag(false);
		jobAppData.setAppStartDate("2021-01-01");
		jobAppData.setAppStartTime("00:00");
		jobAppData.setAppFinishDate("2021-01-01");
		jobAppData.setAppFinishTime("00:00");
		jobAppData.setAppAddress("自宅");
		jobAppData.setAppContents(1);
		jobAppData.setAppContentOther(null);
		jobAppData.setAbsenceFlag(false);
		jobAppData.setAppStartAbsence(null);
		jobAppData.setAppFinishAbsence(null);
		jobAppData.setLeaveFlag(false);
		jobAppData.setAppLeaveDate(null);
		jobAppData.setAppLeaveTime(null);
		jobAppData.setLateFlag(false);
		jobAppData.setAppLateDate(null);
		jobAppData.setAppLateTime(null);
		jobAppData.setAppMemo(null);
		jobAppData.setApplyClass("S3A1");
		jobAppData.setApplyNumber(24);
		jobAppData.setApplyName("中野知歩");
		jobAppData.setAppDate(date);
		jobAppData.setAppCompany(companyList);
		// 2.Do(対象のメソッドを呼び出す)
		int result = jobAppRepository.updateJobApp(appId, jobAppData, date);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotEquals(result, 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testUpdateJobApp]result:" + result);
	}

	@Test
	public void testUpdateJobAppの分岐網羅1() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "1";
		Date date = new Date();
		List<String> companyList = new ArrayList<String>();
		companyList.add("株式会社ABC");
		JobAppData jobAppData = new JobAppData();
		jobAppData.setSummaryFlag(true);
		jobAppData.setDocumentFlag(false);
		jobAppData.setAppStartDate("2021-01-01");
		jobAppData.setAppStartTime("00:00");
		jobAppData.setAppFinishDate("2021-01-01");
		jobAppData.setAppFinishTime("00:00");
		jobAppData.setAppAddress("自宅");
		jobAppData.setAppContents(1);
		jobAppData.setAppContentOther(null);
		jobAppData.setAbsenceFlag(false);
		jobAppData.setAppStartAbsence(null);
		jobAppData.setAppFinishAbsence(null);
		jobAppData.setLeaveFlag(false);
		jobAppData.setAppLeaveDate(null);
		jobAppData.setAppLeaveTime(null);
		jobAppData.setLateFlag(false);
		jobAppData.setAppLateDate(null);
		jobAppData.setAppLateTime(null);
		jobAppData.setAppMemo(null);
		jobAppData.setApplyClass("S3A1");
		jobAppData.setApplyNumber(24);
		jobAppData.setApplyName("中野知歩");
		jobAppData.setAppDate(date);
		jobAppData.setAppCompany(companyList);
		// 2.Do(対象のメソッドを呼び出す)
		int result = jobAppRepository.updateJobApp(appId, jobAppData, date);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotEquals(result, 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testUpdateJobApp]result:" + result);
	}

	@Test
	public void testUpdateJobAppの分岐網羅2() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "3";
		Date date = new Date();
		List<String> companyList = new ArrayList<String>();
		companyList.add("株式会社ABC");
		JobAppData jobAppData = new JobAppData();
		jobAppData.setSummaryFlag(true);
		jobAppData.setDocumentFlag(true);
		jobAppData.setAppStartDate("2021-01-01");
		jobAppData.setAppStartTime("00:00");
		jobAppData.setAppFinishDate("2021-01-01");
		jobAppData.setAppFinishTime("00:00");
		jobAppData.setAppAddress("自宅");
		jobAppData.setAppContents(1);
		jobAppData.setAppContentOther(null);
		jobAppData.setAbsenceFlag(false);
		jobAppData.setAppStartAbsence(null);
		jobAppData.setAppFinishAbsence(null);
		jobAppData.setLeaveFlag(false);
		jobAppData.setAppLeaveDate(null);
		jobAppData.setAppLeaveTime(null);
		jobAppData.setLateFlag(false);
		jobAppData.setAppLateDate(null);
		jobAppData.setAppLateTime(null);
		jobAppData.setAppMemo(null);
		jobAppData.setApplyClass("S3A1");
		jobAppData.setApplyNumber(24);
		jobAppData.setApplyName("中野知歩");
		jobAppData.setAppDate(date);
		jobAppData.setAppCompany(companyList);
		jobAppData.setResume(true);
		jobAppData.setRecommendation(true);
		jobAppData.setUniversityCrtificate(true);
		jobAppData.setHcsTranscript(true);
		jobAppData.setHcsCrtificate(true);
		jobAppData.setHealthCheckCertificate(true);
		jobAppData.setHighscoolTranscript(true);
		jobAppData.setUniversityTranscript(true);
		jobAppData.setPersonalInfo(true);
		jobAppData.setDeadline("2021-01-01");
		// 2.Do(対象のメソッドを呼び出す)
		int result = jobAppRepository.updateJobApp(appId, jobAppData, date);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotEquals(result, 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testUpdateJobApp]result:" + result);
	}

	@Test
	public void testUpdateJobAppの分岐網羅3() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "3";
		Date date = new Date();
		List<String> companyList = new ArrayList<String>();
		companyList.add("株式会社ABC");
		JobAppData jobAppData = new JobAppData();
		jobAppData.setSummaryFlag(true);
		jobAppData.setDocumentFlag(true);
		jobAppData.setAppStartDate("2021-01-01");
		jobAppData.setAppStartTime("00:00");
		jobAppData.setAppFinishDate("2021-01-01");
		jobAppData.setAppFinishTime("00:00");
		jobAppData.setAppAddress("自宅");
		jobAppData.setAppContents(1);
		jobAppData.setAppContentOther(null);
		jobAppData.setAbsenceFlag(false);
		jobAppData.setAppStartAbsence(null);
		jobAppData.setAppFinishAbsence(null);
		jobAppData.setLeaveFlag(false);
		jobAppData.setAppLeaveDate(null);
		jobAppData.setAppLeaveTime(null);
		jobAppData.setLateFlag(false);
		jobAppData.setAppLateDate(null);
		jobAppData.setAppLateTime(null);
		jobAppData.setAppMemo(null);
		jobAppData.setApplyClass("S3A1");
		jobAppData.setApplyNumber(24);
		jobAppData.setApplyName("中野知歩");
		jobAppData.setAppDate(date);
		jobAppData.setAppCompany(companyList);
		jobAppData.setResume(false);
		jobAppData.setRecommendation(false);
		jobAppData.setUniversityCrtificate(false);
		jobAppData.setHcsTranscript(false);
		jobAppData.setHcsCrtificate(false);
		jobAppData.setHealthCheckCertificate(false);
		jobAppData.setHighscoolTranscript(false);
		jobAppData.setUniversityTranscript(false);
		jobAppData.setPersonalInfo(false);
		jobAppData.setDeadline("2021-01-01");
		// 2.Do(対象のメソッドを呼び出す)
		int result = jobAppRepository.updateJobApp(appId, jobAppData, date);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotEquals(result, 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testUpdateJobApp]result:" + result);
	}

	@Test
	public void testSelectDocument() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "9";
		// 2.Do(対象のメソッドを呼び出す)
		JobAppData jobAppData = jobAppRepository.selectDocument(appId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(jobAppData);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testSelectDocument]jobAppData:" + jobAppData.toString());
	}

	@Test
	public void testGetJobAppDocument() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "5";
		JobAppEntity jobAppEntity = new JobAppEntity();
		// 2.Do(対象のメソッドを呼び出す)
		JobAppEntity resultEntity = jobAppRepository.getJobAppDocumentList(appId, jobAppEntity);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(resultEntity);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testGetJobAppDocument]resultEntity:" + resultEntity);
	}

	@Test
	public void testCancelOne() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "1";
		Date date = new Date();
		// 2.Do(対象のメソッドを呼び出す)
		int result = jobAppRepository.cancelOne(appId, date);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotEquals(result, 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testCancelOne]result:" + result);
	}

	@Test
	public void testUpdateNotifiPushDate() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "sample@gmail.com";
		String notificateId = "1";
		// 2.Do(対象のメソッドを呼び出す)
		int result = jobAppRepository.updateNotifiPushDate(userId, notificateId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotEquals(result, 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testUpdateNotifiPushDate]result:" + result);
	}

	@Test
	public void testAppNotificationT() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "master@gmail.com";
		String notificateId = "10";
		// 2.Do(対象のメソッドを呼び出す)
		List<String> resultList = jobAppRepository.appNotificationT(userId, notificateId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(resultList);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testAppNotificationT]resultList:" + resultList.toString());
	}

	@Test
	public void testReportNotificationT() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "master@gmail.com";
		String notificateId = "11";
		// 2.Do(対象のメソッドを呼び出す)
		List<String> resultList = jobAppRepository.reportNotificationT(userId, notificateId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(resultList);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testReportNotificationT]resultList:" + resultList.toString());
	}

	@Test
	public void testEventNotificationT() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "master@gmail.com";
		// 2.Do(対象のメソッドを呼び出す)
		List<String> resultList = jobAppRepository.eventNotificationT(userId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(resultList);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testEventNotificationT]resultList:" + resultList.toString());
	}

	@Test
	public void testCourseNotificationT() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "master@gmail.com";
		String notificateId = "13";
		// 2.Do(対象のメソッドを呼び出す)
		List<String> resultList = jobAppRepository.courseNotificationT(userId, notificateId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(resultList);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testCourseNotificationT]resultList:" + resultList.toString());
	}

	@Test
	public void testRemandAppNotificationS() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "sample@gmail.com";
		String notificateId = "1";
		// 2.Do(対象のメソッドを呼び出す)
		List<String> resultList = jobAppRepository.remandAppNotificationS(userId, notificateId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(resultList);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testRemandAppNotificationS]resultList:" + resultList.toString());
	}

	@Test
	public void testApprovalAppNotificationS() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "sample@gmail.com";
		String notificateId = "2";
		// 2.Do(対象のメソッドを呼び出す)
		List<String> resultList = jobAppRepository.approvalAppNotificationS(userId, notificateId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(resultList);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testApprovalAppNotificationS]resultList:" + resultList.toString());
	}

	@Test
	public void testRemandReportNotificationS() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "sample@gmail.com";
		String notificateId = "3";
		// 2.Do(対象のメソッドを呼び出す)
		List<String> resultList = jobAppRepository.remandReportNotificationS(userId, notificateId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(resultList);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testRemandReportNotificationS]resultList:" + resultList.toString());
	}

	@Test
	public void testApprovalReportNotificationS() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "sample@gmail.com";
		String notificateId = "4";
		// 2.Do(対象のメソッドを呼び出す)
		List<String> resultList = jobAppRepository.approvalReportNotificationS(userId, notificateId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(resultList);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testApprovalReportNotificationS]resultList:" + resultList.toString());
	}

	@Test
	public void testEventIncompNotificationS() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "sample@gmail.com";
		String notificateId = "5";
		// 2.Do(対象のメソッドを呼び出す)
		List<String> resultList = jobAppRepository.eventIncompNotificationS(userId, notificateId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(resultList);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testEventIncompNotificationS]resultList:" + resultList.toString());
	}

	@Test
	public void testNotSubmittedNotificationS() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "sample@gmail.com";
		String notificateId = "6";
		// 2.Do(対象のメソッドを呼び出す)
		List<String> resultList = jobAppRepository.notSubmittedNotificationS(userId, notificateId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(resultList);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testNotSubmittedNotificationS]resultList:" + resultList.toString());
	}

	@Test
	public void testEventNotificationS() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "sample@gmail.com";
		// 2.Do(対象のメソッドを呼び出す)
		List<String> resultList = jobAppRepository.eventNotificationS(userId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(resultList);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testEventNotificationS]resultList:" + resultList.toString());
	}

	@Test
	public void testDocumentNotificationS() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "sample@gmail.com";
		String notificateId = "8";
		// 2.Do(対象のメソッドを呼び出す)
		List<String> resultList = jobAppRepository.documentNotificationS(userId, notificateId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(resultList);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testDocumentNotificationS]resultList:" + resultList.toString());
	}

	@Test
	public void testDocumentLimitNotificationS() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String userId = "sample@gmail.com";
		String notificateId = "9";
		// 2.Do(対象のメソッドを呼び出す)
		List<String> resultList = jobAppRepository.documentLimitNotificationS(userId, notificateId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(resultList);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testDocumentLimitNotificationS]resultList:" + resultList.toString());
	}

	@Test
	public void testJobAppDocumentUpdate() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobAppFormForDocument jobAppFormForDocument = new JobAppFormForDocument();
		jobAppFormForDocument.setResume(false);
		jobAppFormForDocument.setRecommendation(false);
		jobAppFormForDocument.setUniversityCrtificate(false);
		jobAppFormForDocument.setHcsTranscript(false);
		jobAppFormForDocument.setHcsCrtificate(false);
		jobAppFormForDocument.setHealthCheckCertificate(false);
		jobAppFormForDocument.setHighscoolTranscript(false);
		jobAppFormForDocument.setUniversityTranscript(false);
		jobAppFormForDocument.setPersonalInfo(false);
		// 2.Do(対象のメソッドを呼び出す)
		int result = jobAppRepository.jobAppDocumentUpdate(jobAppFormForDocument);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertEquals(result, 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testJobAppDocumentUpdate]result:" + result);
	}

	@Test
	public void testJobAppDocumentUpdateの分岐網羅() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobAppFormForDocument jobAppFormForDocument = new JobAppFormForDocument();
		jobAppFormForDocument.setResume(true);
		jobAppFormForDocument.setRecommendation(true);
		jobAppFormForDocument.setUniversityCrtificate(true);
		jobAppFormForDocument.setHcsTranscript(true);
		jobAppFormForDocument.setHcsCrtificate(true);
		jobAppFormForDocument.setHealthCheckCertificate(true);
		jobAppFormForDocument.setHighscoolTranscript(true);
		jobAppFormForDocument.setUniversityTranscript(true);
		jobAppFormForDocument.setPersonalInfo(true);
		jobAppFormForDocument.setSummaryId(3);
		// 2.Do(対象のメソッドを呼び出す)
		int result = jobAppRepository.jobAppDocumentUpdate(jobAppFormForDocument);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertEquals(result, 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testJobAppDocumentUpdate]result:" + result);
	}

	@Test
	public void testUpdateSummary() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "1";
		// 2.Do(対象のメソッドを呼び出す)
		int result = jobAppRepository.updateSummary(appId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertEquals(result, 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testUpdateSummary]result:" + result);
	}

	@Test
	public void testJobAppNotificationList() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "1";
		// 2.Do(対象のメソッドを呼び出す)
		JobAppEntity jobAppEntity = jobAppRepository.JobAppNotificationList(appId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(jobAppEntity);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testJobAppNotificationList]jobAppEntity:" + jobAppEntity.toString());
	}
}
