package jp.ac.hcs.ShukatsuPortal.jobapp;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 *	フロントーバックで差戻時の入力情報をやり取りする
 **/

@Data
public class JobAppFormForRemand {
	/**
	 * 申請ID
	 */
	private String appId;

	/**
	 * 指摘コメント 必須入力
	 */
	@NotBlank(message = "{require_check}")
	private String indicate;
}
