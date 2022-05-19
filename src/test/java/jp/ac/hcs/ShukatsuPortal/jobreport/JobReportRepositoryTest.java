package jp.ac.hcs.ShukatsuPortal.jobreport;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import jp.ac.hcs.ShukatsuPortal.jobapp.JobAppForm;
import jp.ac.hcs.ShukatsuPortal.jobreport.JobReportEntity;
import jp.ac.hcs.ShukatsuPortal.jobreport.JobReportForm;
import jp.ac.hcs.ShukatsuPortal.jobreport.JobReportFormForRemand;
import jp.ac.hcs.ShukatsuPortal.jobreport.JobReportRepository;
import lombok.extern.slf4j.Slf4j;

/**
 *	JobReportRepositoryに関するメソッドの単体テスト
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class JobReportRepositoryTest {

	@Autowired
	JobReportRepository jobReportRepository;

	@Test
	public void testInsertOne() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobReportForm form = new JobReportForm();
		form.setAppId("1");
		form.setRepComment("よい面接でした。");
		form.getAppProceed().add("株式会社ABC");
		Date date = new Date();
		String appId = "1";
		// 2.Do(対象のメソッドを呼び出す)
		int result = jobReportRepository.insertOne(form, date, appId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(result);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testInsertOne]result:" + result);
	}

	@Test
	public void testGetJobCanpany() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "1";
		// 2.Do(対象のメソッドを呼び出す)
		JobReportEntity entity = jobReportRepository.getJobCanpany(appId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(entity);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testJobReportEntity]entity:" + entity);
	}

	@Test
	public void testRemandOne() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobReportFormForRemand form = new JobReportFormForRemand();
		form.setAppId("1");
		form.setIndicate("なめた報告しとんちゃうぞ");
		Date date = new Date();
		// 2.Do(対象のメソッドを呼び出す)
		int result = jobReportRepository.remandOne(form, date);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(result);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testRemandOne]result:" + result);
	}

	@Test
	public void testApprovalOne() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobAppForm form = new JobAppForm();
		form.setAppId(1);
		Date date = new Date();
		// 2.Do(対象のメソッドを呼び出す)
		int result = jobReportRepository.approvalOne(form, date);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(result);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testApprovalOne]result:" + result);
	}

	@Test
	public void testCancelOne() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobReportForm form = new JobReportForm();
		form.setAppId("1");
		Date date = new Date();
		// 2.Do(対象のメソッドを呼び出す)
		int result = jobReportRepository.cancelOne(form, date);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(result);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testCancelOne]result:" + result);
	}

	@Test
	public void testUpdateOne() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobReportForm form = new JobReportForm();
		form.setAppId("1");
		form.setRepComment("とてもよい面接でした。");
		form.getAppProceed().add("株式会社ABC");
		Date date = new Date();
		String appId = "1";
		// 2.Do(対象のメソッドを呼び出す)
		int result = jobReportRepository.updateOne(form, date, appId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(result);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testUpdateOne]result:" + result);
	}

}
