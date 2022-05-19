package jp.ac.hcs.ShukatsuPortal.jobapp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowCallbackHandler;

import jp.ac.hcs.ShukatsuPortal.WebConfig;

/**
 * SQLで取得した結果をCSVファイルとしてサーバに保存する.
 */
public class JobAppRowCallbackHandler implements RowCallbackHandler {

	@Override
	public void processRow(ResultSet rs) throws SQLException {
		String appStatus = null;
		String appContents = null;
		try {

			File directory = new File(WebConfig.OUTPUT_PATH);
			if (!directory.exists()) {
				directory.mkdir();
			}
			File file = new File(WebConfig.OUTPUT_PATH +  WebConfig.timestamp + WebConfig.FILENAME_JOBAPPLIST_CSV);
			FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);

			BufferedWriter bw = new BufferedWriter(fw);
			do {
				if(rs.getInt("app_status") == 1) {
					appStatus = "申請承認待";
				}else if(rs.getInt("app_status") == 2){
					appStatus = "申請作成中";
				}else if(rs.getInt("app_status") == 3){
					appStatus = "申請承認済";
				}else if(rs.getInt("app_status") == 4){
					appStatus = "申請完了";
				}else if(rs.getInt("app_status") == 5){
					appStatus = "報告承認待";
				}else if(rs.getInt("app_status") == 6){
					appStatus = "報告作成中";
				}else if(rs.getInt("app_status") == 7){
					appStatus = "報告完了";
				}else if(rs.getInt("app_status") == 99){
					appStatus = "取消完了";
				}
				if(rs.getInt("app_contents") == 1) {
					appContents = "合同企業説明会";
				} else if(rs.getInt("app_contents") == 2){
					appContents = "単独企業説明会";
				} else if(rs.getInt("app_contents") == 2){
					appContents = "試験";
				} else if(rs.getInt("app_contents") == 99){
					appContents = "その他";
				}
				// APPテーブルのデータ構造
				String str =
						"申請ID：" + rs.getInt("app_id") + ",　"
						+"申請状態：" + appStatus + ",　"
						+ "とりまとめフラグ：" + rs.getBoolean("document_flag") + ",　"
						+ "書類提出フラグ：" + rs.getBoolean("approval_flag") + ",　"
						+ "コース担当報告フラグ：" + rs.getBoolean("approval_flag") + "　,"
						+ "活動開始日付：" + rs.getDate("app_start_date") + ",　"
						+ "活動開始時刻：" + rs.getTime("app_start_time") + ",　"
						+ "活動終了日付：" + rs.getDate("app_finish_date") + ",　"
						+ "活動終了時刻：" + rs.getTime("app_finish_time") + ",　"
						+ "場所" + rs.getString("app_address") + ",　"
						+ "内容区分：" + appContents + ",　"
						+ "その他内容：" + rs.getString("app_content_other") + ",　"
						+ "欠席フラグ：" + rs.getBoolean("absence_flag") + ",　"
						+ "欠席開始日付：" + rs.getDate("app_start_absence") + ",　"
						+ "欠席終了日付：" + rs.getDate("app_finish_absence") + ",　"
						+ "早退フラグ：" + rs.getBoolean("leave_flag") + ",　"
						+ "早退日付："  + rs.getDate("app_leave_date") + ",　"
						+ "早退時刻：" + rs.getTime("app_leave_time") + ",　"
						+ "遅刻フラグ：" + rs.getBoolean("late_flag") + ",　"
						+ "遅刻日付：" + rs.getDate("app_late_date") + ",　"
						+ "遅刻時刻：" + rs.getTime("app_late_time") + ",　"
						+ "メモ：" + rs.getString("app_memo") + ",　"
						+ "申請者クラス：" + rs.getString("apply_class") + ",　"
						+ "申請者番号：" + rs.getInt("apply_number") + ",　"
						+ "申請者氏名：" + rs.getString("apply_name") + ",　"
						+ "申請日時：" + rs.getDate("app_date") + ",　"
						+ "更新日時：" + rs.getDate("update_date") + ",　"
						+ "報告コメント：" + rs.getString("rep_comment") + ",　"
						+ "報告日時：" + rs.getDate("rep_date") + ",　"
						+ "指摘コメント：" + rs.getString("indicate");
				str = str.replace("null", "なし").replace("true", "オン").replace("false", "オフ");
				bw.write(str);
				bw.newLine();
			} while (rs.next());
			bw.flush();
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
			throw new SQLException(e);
		}

	}

}
