package jp.ac.hcs.ShukatsuPortal.user;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * ユーザ情報をまとめて管理するためのエンティティクラス
 */
@Data
public class UserEntity {
	
	/*ユーザ情報のリスト*/
	private List<UserData> userlist = new ArrayList<UserData>();
	
	/*メッセージ*/
	private String message ;
}
