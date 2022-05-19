package jp.ac.hcs.ShukatsuPortal.report;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import jp.ac.hcs.ShukatsuPortal.WebConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class ReportServiceTest {

	@Autowired
	ReportService reportService;

	@SpyBean
	ReportRepository reportRepository;

	@Test
	void GetReportListメソッドの正常系テスト() {
		// 1.Ready
		// 2.Do
		ReportEntity reportEntity = reportService.getReportList();
		// 3.Assert
		assertNotNull(reportEntity);
		// 4.Logs
		log.warn("[GetReportListメソッドの正常系テスト]reportEntity:" + reportEntity.toString());
	}

	@Test
	void GetReportListメソッドの異常系_DataAccessException() {
		// 1.Ready
		// Mock
		doThrow(new DataAccessResourceFailureException("")).when(reportRepository).getReportList();
		// 2.Do
		ReportEntity reportEntity = reportService.getReportList();
		// 3.Assert
		assertNull(reportEntity);
		// 4.Logs
		log.warn("[GetReportListメソッドの異常系_DataAccessException]reportEntity:" + reportEntity);
	}

	@Test
	void UpdateReportメソッドの正常系テスト() {
		// 1.Ready
		ReportFormForUpdate form = new ReportFormForUpdate();
		String user_id = "sample@gmail.com";

		form.setReportId(1);
		form.setEnterpriseName("HCS");
		form.setTestCategory("学校斡旋");
		form.setExaminationDate("2021-07-27T12:00");
		form.setExaminationLocation("HCS");
		form.setLocationCategory("札幌-HCS");
		form.setStageCategory("1次試験");
		form.setContentCategory("適正");
		form.setContent("テスト");

		// 2.Do
		boolean result = reportService.updateReport(form, user_id);
		// 3.Assert
		assertTrue(result);
		// 4.Logs
		log.warn("[UpdateReportメソッドの正常系テスト]result:" + result);
	}

	@Test
	void UpdateReportメソッドの異常系テスト_ParseException() {
		// 1.Ready
		ReportFormForUpdate form = new ReportFormForUpdate();
		String user_id = "sample@gmail.com";

		form.setReportId(1);
		form.setEnterpriseName("HCS");
		form.setTestCategory("学校斡旋");
		form.setExaminationDate("aaa");
		form.setExaminationLocation("HCS");
		form.setLocationCategory("札幌-HCS");
		form.setStageCategory("1次試験");
		form.setContentCategory("適正");
		form.setContent("テスト");

		// 2.Do
		boolean result = reportService.updateReport(form, user_id);
		// 3.Assert
		assertFalse(result);
		// 4.Logs
		log.warn("[UpdateReportメソッドの異常系テスト_ParseException]result:" + result);
	}

	@Test
	void UpdateReportメソッドの異常系テスト_DataAccessException() {
		// 1.Ready
		ReportFormForUpdate form = new ReportFormForUpdate();
		String user_id = "sample@gmail.com";

		form.setReportId(1);
		form.setEnterpriseName("HCS");
		form.setTestCategory("学校斡旋");
		form.setExaminationDate("aaa");
		form.setExaminationLocation("HCS");
		form.setLocationCategory("札幌-HCS");
		form.setStageCategory("1次試験");
		form.setContentCategory("適正");
		form.setContent("テスト");

		doThrow(new DataAccessResourceFailureException("")).when(reportRepository).updateReport(any());

		// 2.Do
		boolean result = reportService.updateReport(form, user_id);
		// 3.Assert
		assertFalse(result);
		// 4.Logs
		log.warn("[UpdateReportメソッドの異常系テスト_DataAccessException]result:" + result);
	}

	@Test
	void GetReportListOneメソッドの正常系テスト() {
		// 1.Ready
		int reportId = 1;
		// 2.Do
		ReportData reportData = reportService.getReportListOne(reportId);
		// 3.Assert
		assertNotNull(reportData);
		// 4.Logs
		log.warn("[GetReportListOneメソッドの正常系テスト]reportdata:" + reportData);
	}

	@Test
	void GetReportListOneメソッドの異常系テスト_DataAccessException() {
		// 1.Ready
		int user_id = 0;
		doThrow(new DataAccessResourceFailureException("")).when(reportRepository).getReportListOne(anyString());
		// 2.Do
		ReportData reportData = reportService.getReportListOne(user_id);
		// 3.Assert
		assertNull(reportData);
		// 4.Logs
		log.warn("[GetReportListOneメソッドの異常系テスト_DataAccessException]reportdata:" + reportData);
	}

	@Test
	void Insertのメソッドの正常系テスト() {
		// 1.Ready
		ReportForm form = new ReportForm();
		String user_id = "sample@gmail.com";

		form.setEnterpriseName("HCS");
		form.setTestCategory("学校斡旋");
		form.setExaminationDate("2021-07-27T12:00");
		form.setExaminationLocation("HCS");
		form.setLocationCategory("札幌-HCS");
		form.setStageCategory("1次試験");
		form.setContentCategory("適正");
		form.setContent("テスト");
		// 2.Do
		boolean result = reportService.insert(form, user_id);
		// 3.Assert
		assertTrue(result);
		// 4.Logs
		log.warn("[Insertメソッドの正常系テスト]result:" + result);
	}

	@Test
	void Insertのメソッドの異常性テスト_ParseException() {
		// 1.Ready
		ReportForm form = new ReportForm();
		String user_id = "sample@gmail.com";

		form.setEnterpriseName("HCS");
		form.setTestCategory("学校斡旋");
		form.setExaminationDate("aaa");
		form.setExaminationLocation("HCS");
		form.setLocationCategory("札幌-HCS");
		form.setStageCategory("1次試験");
		form.setContentCategory("適正");
		form.setContent("テスト");
		// 2.Do
		boolean result = reportService.insert(form, user_id);
		// 3.Assert
		assertFalse(result);
		// 4.Logs
		log.warn("Insertのメソッドの異常性テスト_ParseException]result:" + result);
	}

	@Test
	void Insertのメソッドの異常性テスト_DataAccessException() {
		// 1.Ready
		ReportForm form = new ReportForm();
		String user_id = "sample@gmail.com";

		form.setEnterpriseName("HCS");
		form.setTestCategory("学校斡旋");
		form.setExaminationDate("2021-07-27T12:00");
		form.setExaminationLocation("HCS");
		form.setLocationCategory("札幌-HCS");
		form.setStageCategory("1次試験");
		form.setContentCategory("適正");
		form.setContent("テスト");
		doThrow(new DataAccessResourceFailureException("")).when(reportRepository).insertOne(any());

		// 2.Do
		boolean result = reportService.insert(form, user_id);
		// 3.Assert
		assertFalse(result);
		// 4.Logs
		log.warn("Insertのメソッドの異常性テスト_DataAccessException]result:" + result);
	}

//	@Test
//	void UpdateStatusメソッドの正常系テスト() {
//		// 1.Ready
//		String user_id = "sample@gmail.com";
//		String reportStatus = "承認済み";
//		// 2.Do
//		boolean result = reportService.updateStatus(user_id, reportStatus);
//		// 3.Assert
//		assertTrue(result);
//		// 4.Logs
//		log.warn("[UpdateStatuメソッドの正常系テスト]result:" + result);
//	}

//	@Test
//	void UpdateStatusメソッドの異常系テスト_DataAccessException() {
//		// 1.Ready
//		String user_id = "sample@gmail.com";
//		String reportStatus = "承認済み";
//		doThrow(new DataAccessResourceFailureException("")).when(reportRepository).updateStatus(anyString(), anyString());
//		// 2.Do
//		boolean result = reportService.updateStatus(user_id, reportStatus);
//		// 3.Assert
//		assertFalse(result);
//		// 4.Logs
//		log.warn("[UpdateStatusメソッドの異常系テスト_DataAccessException]result:" + result);
//	}


	@Test
	void GetFileメソッドの正常系テスト() {
		// 1.Ready
		byte[] result = null;
		String fileName = WebConfig.OUTPUT_PATH + WebConfig.FILENAME_REPORT_CSV;
		String year = "2021";
		// 2.Do
		try {
			reportService.reportListCsvOut(year);
			result = reportService.getFile(fileName);
		} catch (IOException e) {
		}
		// 3.Assert
		assertNotNull(result);
		// 4.Logs
		log.warn("[GetFileメソッドの正常系テスト]result:" + result);
	}

	@Test
	void GetReportListStringStringメソッドの正常系テスト_学生氏名() {
		// 1.Ready
		String filtering = "学生氏名";
		String conditions = "中野知歩";
		// 2.Do
		ReportEntity reportEntity = reportService.getReportList(filtering, conditions);
		// 3.Assert
		assertNotNull(reportEntity);
		// 4.Logs
		log.warn("[GetReportListStringStringメソッドの正常系テスト_学生氏名]reportEntity:" + reportEntity);
	}

	@Test
	void GetReportListStringStringメソッドの正常系テスト_企業名() {
		// 1.Ready
		String filtering = "企業名";
		String conditions = "HCS";
		// 2.Do
		ReportEntity reportEntity = reportService.getReportList(filtering, conditions);
		// 3.Assert
		assertNotNull(reportEntity);
		// 4.Logs
		log.warn("[GetReportListStringStringメソッドの正常系テスト_企業名]reportEntity:" + reportEntity);
	}

	@Test
	void GetReportListStringStringメソッドの正常系テスト_状態() {
		// 1.Ready
		String filtering = "状態";
		String conditions = "承認済み";
		// 2.Do
		ReportEntity reportEntity = reportService.getReportList(filtering, conditions);
		// 3.Assert
		assertNotNull(reportEntity);
		// 4.Logs
		log.warn("[GetReportListStringStringメソッドの正常系テスト_状態]reportEntity:" + reportEntity);
	}

	@Test
	void GetReportListStringStringメソッドの異常性テスト_DataAccessException() {
		// 1.Ready
		String filtering = "状態";
		String conditions = "承認済み";
		doThrow(new DataAccessResourceFailureException("")).when(reportRepository).getReportListByStatus(anyString());
		// 2.Do
		ReportEntity reportEntity = reportService.getReportList(filtering, conditions);
		// 3.Assert
		assertNull(reportEntity);
		// 4.Logs
		log.warn("[GetReportListStringStringメソッドの異常性テスト_DataAccessException]reportEntity:" + reportEntity);
	}

}
