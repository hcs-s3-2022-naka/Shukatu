package jp.ac.hcs.ShukatsuPortal.jobapp;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 申請情報を管理するエンティティクラス
 */
@Data
public class JobAppEntity {
	/**
	 * 申請一覧を管理するリスト
	 */
	List<JobAppData> jobAppList = new ArrayList<JobAppData>();

	/**
	 * 申請企業を管理するリスト
	 */
	List<String> jobAppCompanys = new ArrayList<String>();

	/**
	 * とりまとめ書類を管理するリスト
	 */
	List<JobAppDocumentData> jobAppDocumentList  = new ArrayList<JobAppDocumentData>();

}
