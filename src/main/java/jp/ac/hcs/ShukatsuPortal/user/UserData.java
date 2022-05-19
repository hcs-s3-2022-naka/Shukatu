package jp.ac.hcs.ShukatsuPortal.user;

import java.util.Date;

import lombok.Data;

/**
 * 1件分のユーザ情報 各項目のデータ仕様も合わせて管理する
 */
@Data
public class UserData {

	 /**
	  * ユーザID（メールアドレス）
	  * 主キー、必須入力、メールアドレス形式
	  */
	 private String user_id;

	 /**
	  * パスワード
	  * 必須入力、長さ4から100桁まで、半角英数字のみ
	  */
	 private String password;

	 /**
	  * ユーザ名
	  * 必須入力
	  */
	 private String userName;

	 /**
	  * ユーザー権限
	  * 講師："TEACHER"
	  * 学生："STUDENT"
	  * 事務："STAFF"
	  * 必須入力
	  */
	 private String userAuthority;

	 /**
	  * 所属クラス
	  *
	  */
	 private String belongClass;

	 /**
	  * 出席番号
	  */
	 private int studentNumber;

	 /**
	  * ダークモードフラグ
	  * ON：true
	  * OFF：false
	  * ユーザ作成時はfalse固定
	  */
	 private boolean darkMode;

	 /**
	  * アカウント状態
	  * 有効:VALID
	  * ロック中:LOCKED
	  * 無効:INVALID
	  */
	 private String userStatus;

	 /**
	  * 登録日時
	  */
	 private Date regiDate;

	 /**
	  * 登録者のユーザID
	  */
	 private String regiUser;

	 /**
	  * 更新日時
	  */
	 private Date upDate;

	 /**
	  * 更新者のユーザID
	  */
	 private String upUser;

	 /**
	 * パスワードエラー回数
	 * ユーザ作成時は0固定
	 * ユーザ状態を有効にした際に0に戻す
	 */
	private int passwordErrorCount;
}
