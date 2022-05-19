package jp.ac.hcs.ShukatsuPortal.report;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 受験報告情報を管理するエンティティクラス
 */
@Data
public class ReportEntity {

	/**
	 * 受験報告を管理するリスト
	 */
	List<ReportData> reportList = new ArrayList<ReportData>();
}
