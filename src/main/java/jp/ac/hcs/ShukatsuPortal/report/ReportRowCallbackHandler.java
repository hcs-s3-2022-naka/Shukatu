package jp.ac.hcs.ShukatsuPortal.report;

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
public class ReportRowCallbackHandler implements RowCallbackHandler {

	@Override
	public void processRow(ResultSet rs) throws SQLException {

		try {

			File directory = new File(WebConfig.OUTPUT_PATH);
			if (!directory.exists()) {
				directory.mkdir();
			}

			File file = new File(WebConfig.OUTPUT_PATH + WebConfig.FILENAME_REPORT_CSV);
			FileWriter fw = new FileWriter(file.getAbsoluteFile());

			BufferedWriter bw = new BufferedWriter(fw);
			do {
				// Reportテーブルのデータ構造
				String str = rs.getInt("report_id") + "," + rs.getString("belong_class") + ","
						+ rs.getInt("student_number") + "," + rs.getString("user_name") + ","
						+ rs.getString("department") + "," + rs.getString("job_number") + ","
						+ rs.getString("enterprise_name_kana") + "," + rs.getString("enterprise_name") + ","
						+ rs.getString("test_category") + "," + rs.getString("test_category_other") + ","
						+ rs.getString("examination_date") + "," + rs.getString("examination_location") + ","
						+ rs.getString("location_category") + "," + rs.getString("location_category_other") + ","
						+ rs.getString("stage_category") + "," + rs.getString("stage_category_other") + ","
						+ rs.getString("content_category") + "," + rs.getString("content_category_other") + ","
						+ rs.getInt("result_date") + "," + rs.getBoolean("pass_flag") + ","
						+ rs.getString("result_category") + "," + rs.getString("appropriate_category") + ","
						+ rs.getString("appropriate_category_other") + "," + rs.getString("writing_category") + ","
						+ rs.getString("writing_category_other") + "," + rs.getString("interview_category") + ","
						+ rs.getString("interview_category_other") + "," + rs.getInt("group_number") + ","
						+ rs.getInt("officer_number") + "," + rs.getString("officer_role") + ","
						+ rs.getInt("interview_time") + "," + rs.getString("gd_thema") + ","
						+ rs.getString("content") + "," + rs.getString("remarks") + ","
						+ rs.getString("report_status") + "," + rs.getDate("registration_date") + ","
						+ rs.getString("registered_user_id") + "," + rs.getDate("update_date") + ","
						+ rs.getString("update_user_id") + "," + rs.getString("teacher_comment");
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
