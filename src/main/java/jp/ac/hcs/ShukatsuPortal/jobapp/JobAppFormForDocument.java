package jp.ac.hcs.ShukatsuPortal.jobapp;

import lombok.Data;

/**
 *	フロントーバックで提出書類情報をやり取りする
 */
@Data
public class JobAppFormForDocument {

	/**
	 * 申請ID 主キー
	 */
	private int appId;

	/**
	 * 申請ID とりまとめID
	 */
	private int summaryId;

	/**
	 * 書類ID:履歴書
	 */
	private boolean resume;

	/**
	 * 書類ID:推薦書
	 */
	private boolean recommendation;

	/**
	 * 書類ID:大学（卒業・卒業見込）証明書
	 */
	private boolean universityCrtificate;

	/**
	 * 書類ID:HCS成績証明書
	 */
	private boolean hcsTranscript;

	/**
	 * 書類ID:HCS卒業見込証明書
	 */
	private boolean hcsCrtificate;

	/**
	 * 書類ID:健康診断証明書
	 */
	private boolean healthCheckCertificate;

	/**
	 * 書類ID:出身高校成績証明書
	 */
	private boolean highscoolTranscript;

	/**
	 * 書類ID:大学成績証明書等
	 */
	private boolean universityTranscript;

	/**
	 * 書類ID:個人情報等同意書
	 */
	private boolean personalInfo;

}
