package jp.ac.hcs.ShukatsuPortal.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

/**
 * フロントーバックでユーザ情報をやり取りする
 * 各項目のデータ仕様はUserEntityを参照とする
 */
@Data
public class UserForm {
	
	/*ユーザID（メールアドレス）*/
	@NotBlank(message = "{require_check}")
	@Email(message = "{email_check}")
	private String user_id;
	
	/*パスワード*/
	@NotBlank(message = "{require_check}")
	@Length(min = 4, max = 100, message="{length_check}")
	@Pattern(regexp = "[a-zA-Z0-9]*")
	private String password;
	
	/*ユーザ名*/
	@NotBlank(message = "{require_check}")
	private String userName;
	
	/*ダークモードフラグ*/
	@NotNull
	private boolean darkMode;
	
	/*ユーザ権限*/
	@NotBlank(message = "{require_check}")
	private String userAuthority;
	
	/*所属クラス*/
	@Length(max = 4, message="{length_check}")
	private String belongClass;
	
	/*学籍番号*/
	private int studentNumber;
	
	/*ユーザ状態*/
	private String userStatus;
}
