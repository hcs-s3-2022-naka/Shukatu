package jp.ac.hcs.ShukatsuPortal.report;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class ReportRepositoryTest {

	@SpyBean
	ReportRepository reportRepository;

	@Test
	void getReportListメソッドの正常系テスト() {
		// 1.Ready
		// 2.Do
		ReportEntity result = reportRepository.getReportList();
		// 3.Assert
		assertNotNull(result);
		// 4.Logs
		log.warn("[getReportListメソッドの正常系テスト]reportEntity:" + result.toString());
	}

	@Test
	void insertOneメソッドの正常系テスト() {
		// 1.Ready
		Date date = new Date();
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		ReportData reportData = new ReportData();
		reportData.setEnterpriseName("HCS");
		reportData.setTestCategory("学校斡旋");
		try {
			reportData.setExaminationDate(sdFormat.parse("2021-05-21 12:12"));
		} catch (ParseException e1) {
		}
		reportData.setExaminationLocation("HCS");
		reportData.setLocationCategory("札幌-HCS");
		reportData.setStageCategory("3次試験");
		reportData.setContentCategory("集団面接");
		reportData.setGroupNumber(1);
		reportData.setOfficerNumber(1);
		reportData.setInterviewTime(1);
		reportData.setContent("面接で活きの良いおっさんが出てきた");
		reportData.setReportStatus("承認前");
		reportData.setRegistrationDate(date);
		reportData.setRegistrationUserId("sample@gmail.com");
		// 2.Do
		int result = reportRepository.insertOne(reportData);
		// 3.Assert
		assertEquals(1, result);
		// 4.Logs
		log.warn("[nsertOneメソッドの正常系テスト]result:" + result);
	}

	@Test
	void UpdateReportメソッドの正常系テスト() {
		// 1.Ready
		Date date = new Date();
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		ReportData reportData = new ReportData();
		reportData.setEnterpriseName("SCC");
		reportData.setTestCategory("学校斡旋");
		try {
			reportData.setExaminationDate(sdFormat.parse("2021-05-21 12:12"));
		} catch (ParseException e1) {
		}
		reportData.setExaminationLocation("HCS");
		reportData.setLocationCategory("札幌-HCS");
		reportData.setStageCategory("1次試験");
		reportData.setContentCategory("個人面接");
		reportData.setGroupNumber(1);
		reportData.setOfficerNumber(1);
		reportData.setInterviewTime(1);
		reportData.setContent("ドラ息子が担当だった");
		reportData.setReportStatus("承認前");
		reportData.setUpdateDate(date);
		reportData.setUpdateUserId("sample@gmail.com");
		reportData.setReportId(1);
		// 2.Do
		int result = reportRepository.updateReport(reportData);
		// 3.Assert
		assertEquals(1, result);
		// 4.Logs
		log.warn("[UpdateReportメソッドの正常系テスト]result:" + result);
	}

	@Test
	void getReportListOneメソッドの正常系テスト() {
		// 1.Ready
		String userId = "sample@gmail.com";
		// 2.Do
		ReportData result = reportRepository.getReportListOne(userId);
		// 3.Assert
		assertNotNull(result);
		// 4.Logs
		log.warn("[getReportListOneメソッドの正常系テスト]result:" + result.toString());
	}

//	@Test
//	void updateStatusメソッドの正常系テスト() {
//		// 1.Ready
//		String userId = "sample@gmail.com";
//		String reportStatus = "承認済";
//		// 2.Do
//		boolean result = reportRepository.updateStatus(userId, reportStatus);
//		// 3.Assert
//		assertTrue(result);
//		// 4.Logs
//		log.warn("[updateStatusメソッドの正常系テスト]result:" + result);
//	}

	@Test
	void reportlistCsvOutメソッドの正常系テスト() {
		// 1.Ready
		String year = "2021";
		// 2.Do
		reportRepository.reportlistCsvOut(year);
		// 3.Assert
		// 4.Logs
	}

	@Test
	void getReportListByNameメソッドの正常系テスト() {
		// 1.Ready
		String user_name = "神";
		// 2.Do
		ReportEntity result = reportRepository.getReportListByName(user_name);
		// 3.Assert
		assertNotNull(result);
		// 4.Logs
		log.warn("[getReportListByNameメソッドの正常系テスト]result:" + result.toString());
	}

	@Test
	void getReportListByEnterpriseNameメソッドの正常系テスト() {
		// 1.Ready
		String enterpriseName = "HCS";
		// 2.Do
		ReportEntity result = reportRepository.getReportListByEnterpriseName(enterpriseName);
		// 3.Assert
		assertNotNull(result);
		// 4.Logs
		log.warn("[getReportListByEnterpriseNameメソッドの正常系テスト]result:" + result.toString());
	}

	@Test
	void getReportListByStatusメソッドの正常系テスト() {
		// 1.Ready
		String status = "承認前";
		// 2.Do
		ReportEntity result = reportRepository.getReportListByStatus(status);
		// 3.Assert
		assertNotNull(result);
		// 4.Logs
		log.warn("[getReportListByStatusメソッドの正常系テスト]result:" + result.toString());
	}

}
