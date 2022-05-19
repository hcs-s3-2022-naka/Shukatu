package jp.ac.hcs.ShukatsuPortal.jobreport;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import jp.ac.hcs.ShukatsuPortal.jobapp.JobAppForm;

/**
 * 就職活動報告に関する操作を行う
 */
@Service
public class JobReportService {
	@Autowired
	JobReportRepository jobReportRepository;

	/**
	 * 報告を１件追加する
	 *
	 * @param form  報告情報
	 * @param appId 申請ID
	 * @return rowNumber 追加行数
	 */
	public int insert(JobReportForm form, String appId) {
		int rowNumber;
		Date date = new Date();
		rowNumber = jobReportRepository.insertOne(form, date, appId);
		return rowNumber;
	}

	/**
	 * 選択された報告の詳細表示
	 *
	 * @param appId 申請ID
	 * @return jobReportEntity
	 */
	public JobReportEntity getJobReportDetail(String appId) {
		JobReportEntity jobReportEntity = jobReportRepository.getJobCanpany(appId);
		return jobReportEntity;
	}

	/**
	 * 報告の差戻処理
	 *
	 * @param form 差戻用フォーム
	 * @return rowNumber 更新行数
	 */
	public int remand(JobReportFormForRemand form) {
		int rowNumber;
		Date date = new Date();
		rowNumber = jobReportRepository.remandOne(form, date);
		return rowNumber;
	}

	/**
	 * 報告の承認処理
	 *
	 * @param form 承認用フォーム
	 * @return rowNumber 更新行数
	 */
	public int approval(JobAppForm form) {
		int rowNumber;
		Date date = new Date();
		rowNumber = jobReportRepository.approvalOne(form, date);
		return rowNumber;
	}

	/**
	 * 報告の取消処理
	 *
	 * @param form 取消用フォーム
	 * @return rowNumber 更新行数
	 */
	public int cancel(JobReportForm form) {
		int rowNumber;
		Date date = new Date();
		rowNumber = jobReportRepository.cancelOne(form, date);
		return rowNumber;
	}

	/**
	 * 変更エラー後の入力情報を取得する
	 *
	 * @param form  報告変更用フォーム
	 * @param appId 申請ID
	 * @return JobReportEntity
	 */
	public JobReportEntity updateDetail(JobReportForm form, String appId) {
		JobReportEntity entity;
		// 会社を取得する
		entity = jobReportRepository.getJobCanpany(appId);
		// 会社のサイズ分ループ
		for (int cntEntity = 0; cntEntity < entity.jobReportList.size(); cntEntity++) {
			if (form.getAppProceed().size() == 0) {
				entity.jobReportList.get(cntEntity).setAppProceed(false);
			} else {
				// 進めるを押した数分ループ
				for (int cntForm = 0; cntForm < form.getAppProceed().size(); cntForm++) {
					if (form.getAppProceed().get(cntForm)
							.equals(entity.getJobReportList().get(cntEntity).getAppCompany())) {
						entity.jobReportList.get(cntEntity).setAppProceed(true);
						break;
					} else {
						entity.jobReportList.get(cntEntity).setAppProceed(false);
					}
				}
			}

		}

		entity.setRepComment(form.getRepComment());
		return entity;
	}

	/**
	 * 報告を変更する
	 *
	 * @param form  報告変更用フォーム
	 * @param appId 申請ID
	 * @return rowNumber更新行数
	 */
	public int updateDone(JobReportForm form, String appId) throws DataAccessException{
		int rowNumber;
		Date date = new Date();
		rowNumber = jobReportRepository.updateOne(form, date, appId);

		return rowNumber;
	}
}
