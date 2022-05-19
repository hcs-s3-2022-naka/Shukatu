package jp.ac.hcs.ShukatsuPortal.user;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

/**
 * ユーザ情報のデータを管理する.
 * - Userテーブル
 */
@Repository
public class UserRepository {

	/** SQL 全件取得（ユーザID昇順） */
	private static final String SQL_SELECT_ALL = "SELECT * FROM USERS ORDER BY USER_ID";

	/** SQL 1件取得(ID) */
	private static final String SQL_SELECT_ONE = "SELECT * FROM USERS WHERE USER_ID = ?";

	/** SQL 1件取得(名前) */
	private static final String SQL_SELECT_NAME = "SELECT * FROM USERS WHERE USER_NAME = ?";

	/** SQL 1件追加 */
	private static final String SQL_INSERT_ONE = "INSERT INTO USERS(USER_ID, ENCRYPTED_PASSWORD, USER_NAME, USER_AUTHORITY, BELONG_CLASS, STUDENT_NUMBER, REGISTERED_DATE, REGISTERED_USER_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

	/** SQL 1件更新 管理者 パスワード更新有 */
	private static final String SQL_UPDATE_ONE_WITH_PASSWORD = "UPDATE USERS SET ENCRYPTED_PASSWORD = ?, USER_NAME = ?, USER_AUTHORITY = ?, BELONG_CLASS = ?, STUDENT_NUMBER = ?, USER_STATUS = ?, UPDATE_DATE = ?, UPDATE_USER_ID = ?, PASSWORD_ERROR_COUNT = ? WHERE USER_ID = ?";

	/** SQL 1件更新 管理者 パスワード更新無 */
	private static final String SQL_UPDATE_ONE = "UPDATE USERS SET USER_NAME = ?, USER_AUTHORITY = ?, BELONG_CLASS = ?, STUDENT_NUMBER = ?, USER_STATUS = ?, UPDATE_DATE = ?, UPDATE_USER_ID = ?, PASSWORD_ERROR_COUNT = ? WHERE USER_ID = ?";

	/** SQL 1件更新 一般ユーザ パスワード更新有 */
	private static final String SQL_UPDATE_GENERAL_WITH_PASSWORD = "UPDATE USERS SET ENCRYPTED_PASSWORD = ?, USER_NAME = ?, DARK_MODE = ? WHERE USER_ID = ?";

	/** SQL 1件更新 一般ユーザ パスワード更新無 */
	private static final String SQL_UPDATE_GENERAL = "UPDATE USERS SET USER_NAME = ?, DARK_MODE = ? WHERE USER_ID = ?";

	/** SQL 1件削除 */
	private static final String SQL_DELETE_ONE = "DELETE FROM USERS WHERE USER_ID = ?";

	/** SQL 1件取得（クラス番号→名前）*/
	private static final String SQL_SELECT_USERNAME = "SELECT USER_NAME FROM USERS WHERE BELONG_CLASS = ? AND STUDENT_NUMBER = ?";

	@Autowired
	private JdbcTemplate jdbc;

	@Autowired
	PasswordEncoder passwordEncoder;

	/**
	 * Userテーブルから全データを取得.
	 * @return UserEntity
	 * @throws DataAccessException データアクセスエラー
	 */
	public UserEntity selectAll() throws DataAccessException {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_ALL);
		UserEntity userEntity = mappingSelectResult(resultList);
		return userEntity;
	}

	/**
	 * UserテーブルからユーザIDをキーにデータを1件を取得.
	 * @param user_id 検索するユーザID
	 * @return UserData
	 * @throws DataAccessException データアクセスエラー
	 */
	public UserData selectOne(String user_id) throws DataAccessException {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_ONE, user_id);
		UserEntity entity = mappingSelectResult(resultList);
		// 必ず1件のみのため、最初のUserDataを取り出す
		UserData data = entity.getUserlist().get(0);
		return data;
	}

	/**
	 * Userテーブルから取得したデータをUserEntity形式にマッピングする.
	 * @param resultList Userテーブルから取得したデータ
	 * @return UserEntity
	 */
	private UserEntity mappingSelectResult(List<Map<String, Object>> resultList) {
		UserEntity entity = new UserEntity();

		for (Map<String, Object> map : resultList) {
			UserData data = new UserData();
			data.setUser_id((String) map.get("user_id"));
			data.setUserName((String) map.get("user_name"));
			data.setUserAuthority((String) map.get("user_authority"));
			data.setDarkMode((boolean) map.get("dark_mode"));
			data.setUserStatus((String) map.get("user_status"));
			data.setPasswordErrorCount((int)map.get("password_error_count"));
			data.setBelongClass((String) map.get("belong_class"));
			if (data.getUserAuthority().equals("STUDENT")) {
				data.setStudentNumber((int) map.get("student_number"));
			}

			entity.getUserlist().add(data);
		}
		return entity;
	}

	/**
	 * Userテーブルから取得したデータをUserEntity形式にマッピングする.
	 * @param resultList Userテーブルから取得したデータ
	 * @return UserEntity
	 */
	private UserData mappingSelectResult2(List<Map<String, Object>> resultList) {
		UserData data = new UserData();
		for (Map<String, Object> map : resultList) {
			data.setUserName((String) map.get("user_name"));
		}
		return data;
	}

	/**
	 * Userテーブルにデータを1件追加する.
	 * @param data 追加するユーザ情報
	 * @return 追加データ数
	 * @throws DataAccessException データアクセスエラー
	 */
	public int insertOne(UserData data) throws DataAccessException {
		int rowNumber = jdbc.update(SQL_INSERT_ONE,
				data.getUser_id(),
				passwordEncoder.encode(data.getPassword()),
				data.getUserName(),
				data.getUserAuthority(),
				data.getBelongClass(),
				data.getStudentNumber(),
				data.getRegiDate(),
				data.getRegiUser());
		return rowNumber;
	}

	/**
	 * (管理用)Userテーブルのデータを1件更新する(パスワード更新有).
	 * @param data 更新するユーザ情報
	 * @return 更新データ数
	 * @throws DataAccessException データアクセスエラー
	 */
	public int updateOneWithPassword(UserData data) throws DataAccessException {
		int rowNumber = jdbc.update(SQL_UPDATE_ONE_WITH_PASSWORD,
				passwordEncoder.encode(data.getPassword()),
				data.getUserName(),
				data.getUserAuthority(),
				data.getBelongClass(),
				data.getStudentNumber(),
				data.getUserStatus(),
				data.getUpDate(),
				data.getUpUser(),
				data.getPasswordErrorCount(),
				data.getUser_id());
		return rowNumber;
	}

	/**
	 * (管理用)Userテーブルのデータを1件更新する(パスワード更新無).
	 * @param data 更新するユーザ情報
	 * @return 更新データ数
	 * @throws DataAccessException データアクセスエラー
	 */
	public int updateOne(UserData data) throws DataAccessException {
		int rowNumber = jdbc.update(SQL_UPDATE_ONE,
				data.getUserName(),
				data.getUserAuthority(),
				data.getBelongClass(),
				data.getStudentNumber(),
				data.getUserStatus(),
				data.getUpDate(),
				data.getUpUser(),
				data.getPasswordErrorCount(),
				data.getUser_id());
		return rowNumber;
	}

	/**
	 * (一般用)Userテーブルのデータを1件更新する(パスワード更新有).
	 * @param data 更新するユーザ情報
	 * @return 更新データ数
	 * @throws DataAccessException データアクセスエラー
	 */
	public int updateGeneralWithPassword(UserData data) throws DataAccessException {
		int rowNumber = jdbc.update(SQL_UPDATE_GENERAL_WITH_PASSWORD,
				passwordEncoder.encode(data.getPassword()),
				data.getUserName(),
				data.isDarkMode(),
				data.getUser_id());
		return rowNumber;
	}

	/**
	 * (一般用)Userテーブルのデータを1件更新する(パスワード更新無).
	 * @param userData 更新するユーザ情報
	 * @return 更新データ数
	 * @throws DataAccessException データアクセスエラー
	 */
	public int updateGeneral(UserData userData) throws DataAccessException {
		int rowNumber = jdbc.update(SQL_UPDATE_GENERAL,
				userData.getUserName(),
				userData.isDarkMode(),
				userData.getUser_id());
		return rowNumber;
	}

	/**
	 * Userテーブルのデータを1件削除する.
	 * @param user_id 削除するユーザID
	 * @return 削除データ数
	 * @throws DataAccessException データアクセスエラー
	 */
	public int deleteOne(String user_id) throws DataAccessException {
		int rowNumber = jdbc.update(SQL_DELETE_ONE, user_id);
		return rowNumber;
	}

	/**
	 * Userテーブルからユーザ名で検索する
	 * @param user_name 検索するユーザ名
	 * @return UserEntity
	 */
	public UserEntity filteredSearch(String user_name) throws DataAccessException {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_NAME, user_name);
		UserEntity entity = mappingSelectResult(resultList);
		return entity;
	}

	/**
	 * Userテーブルからクラス番号で名前を取得
	 * @param inputClass クラス
	 * @param inputNumber 出席番号
	 * @return userData
	 */

	public UserData selectUserName(String inputClass, int inputNumber) {
		List<Map<String, Object>> resultList = jdbc.queryForList(SQL_SELECT_USERNAME, inputClass, inputNumber);
		UserData data = mappingSelectResult2(resultList);
		return data;
	}

}