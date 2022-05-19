package jp.ac.hcs.ShukatsuPortal.jobreport;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * フロントーバックで報告情報をやり取りする 各項目のデータ仕様はJobReportEntityを参照とする
 */
@Data
public class JobReportForm {

	/**
	 * 申請ID
	 */
	private String appId;

	/**
	 * 報告コメント 必須入力
	 */
	@NotBlank(message = "{require_check}")
	private String repComment;

	/**
	 * 進める
	 */
	private List<String> appProceed = new ArrayList<String>();

}
