package jp.ac.hcs.ShukatsuPortal.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

/**
 * アップデート用にパスワード、ダークモード、権限のチェックを外したUserForm
 * そのほかの内容はUserFormに準じる
 */
@Data
public class UserFormForUpdate {

	/*ユーザID（メールアドレス）*/
	@NotBlank(message = "{require_check}")
	@Email(message = "{email_check}")
	private String user_id;

	/*パスワード*/
	private String password;

	/*ユーザ名*/
	@NotBlank(message = "{require_check}")
	private String userName;


	/*権限*/
	private String userAuthority;

	/*所属クラス*/
	@Length(max = 4, message="{length_check}")
	private String belongClass;

	/*学籍番号*/
	private int studentNumber;

	/*ユーザ状態*/
	private String userStatus;
}
