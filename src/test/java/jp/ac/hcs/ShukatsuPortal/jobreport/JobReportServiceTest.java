package jp.ac.hcs.ShukatsuPortal.jobreport;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import jp.ac.hcs.ShukatsuPortal.jobapp.JobAppForm;
import jp.ac.hcs.ShukatsuPortal.jobreport.JobReportEntity;
import jp.ac.hcs.ShukatsuPortal.jobreport.JobReportForm;
import jp.ac.hcs.ShukatsuPortal.jobreport.JobReportFormForRemand;
import jp.ac.hcs.ShukatsuPortal.jobreport.JobReportService;
import lombok.extern.slf4j.Slf4j;

/**
 *	JobReportServiceに関するメソッドの単体テスト
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class JobReportServiceTest {

	@Autowired
	JobReportService jobReportService;

	@Test
	public void testInsert() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobReportForm form = new JobReportForm();
		List<String> appProceed = new ArrayList<String>();
		appProceed.add("株式会社ABC");
		form.setAppId("1");
		form.setRepComment("とても気持ちのいい就活でした");
		form.setAppProceed(appProceed);
		// 2.Do(対象のメソッドを呼び出す)
		int rowNumber = jobReportService.insert(form, form.getAppId());
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(rowNumber, 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testInsert]rowNumber:" + rowNumber);
	}

	@Test
	public void testGetJobReportDetail() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		String appId = "1";
		// 2.Do(対象のメソッドを呼び出す)
		JobReportEntity entity = jobReportService.getJobReportDetail(appId);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(entity);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testGetJobAppCanpanyListAll]jobReportEntity:" + entity);
	}

	@Test
	public void testRemand() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobReportFormForRemand form = new JobReportFormForRemand();
		List<String> appProceed = new ArrayList<String>();
		appProceed.add("株式会社ABC");
		form.setAppId("1");
		form.setIndicate("こんなんで提出しないでください");
		// 2.Do(対象のメソッドを呼び出す)
		int rowNumber = jobReportService.remand(form);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(rowNumber, 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testRemand]rowNumber:" + rowNumber);
	}

	@Test
	public void testApproval() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobAppForm form = new JobAppForm();
		form.setAppId(1);
		// 2.Do(対象のメソッドを呼び出す)
		int rowNumber = jobReportService.approval(form);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(rowNumber, 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testApproval]rowNumber:" + rowNumber);
	}

	@Test
	public void testCancel() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobReportForm form = new JobReportForm();
		form.setAppId("6");
		// 2.Do(対象のメソッドを呼び出す)
		int rowNumber = jobReportService.cancel(form);
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(rowNumber, 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testCancel]rowNumber:" + rowNumber);
	}

	@Test
	public void testUpdateDetail() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobReportForm form = new JobReportForm();
		form.setAppId("1");
		form.setRepComment("");
		form.getAppProceed().add("株式会社ABC");
		// 2.Do(対象のメソッドを呼び出す)
		JobReportEntity entity = jobReportService.updateDetail(form, form.getAppId());
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(entity);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testUpdateDetail]entity:" + entity);
	}

	@Test
	public void testUpdateDetail_会社なし() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobReportForm form = new JobReportForm();
		form.setAppId("1");
		form.setRepComment("");
		// 2.Do(対象のメソッドを呼び出す)
		JobReportEntity entity = jobReportService.updateDetail(form, form.getAppId());
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(entity);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testUpdateDetail_会社なし]entity:" + entity);
	}

	@Test
	public void testUpdateDetail_会社複数_進める1件() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobReportForm form = new JobReportForm();
		form.setAppId("2");
		form.setRepComment("");
		form.getAppProceed().add("株式会社CBA");
		// 2.Do(対象のメソッドを呼び出す)
		JobReportEntity entity = jobReportService.updateDetail(form, form.getAppId());
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotNull(entity);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testUpdateDetail_会社複数_進める1件]entity:" + entity);
	}

	@Test
	public void testUpdateDone() {
		// 1.Ready(データなどの事前準備が必要な場合に記述)
		JobReportForm form = new JobReportForm();
		List<String> appProceed = new ArrayList<String>();
		appProceed.add("株式会社Aaa");
		form.setAppId("7");
		form.setRepComment("悪魔の実の能力を試されました。");
		form.setAppProceed(appProceed);
		// 2.Do(対象のメソッドを呼び出す)
		int rowNumber = jobReportService.updateDone(form, form.getAppId());
		// 3.Assert(結果を比較して、問題ないか確認する)
		assertNotSame(rowNumber, 0);
		// 4.logs(実行結果が問題ないかをログで確認する)
		log.warn("[testUpdateDone]rowNumber:" + rowNumber);
	}

}
