package jp.ac.hcs.ShukatsuPortal.user;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

/**
 * ユーザ情報に関する操作を行う
 */
@Service
public class UserService {
	@Autowired
	UserRepository userRepository;

	/**
	 * ユーザ情報の一覧を取得する
	 *
	 * @return UserEntity
	 */
	public UserEntity getUserlist() {
		UserEntity userEntity;
		try {
			userEntity = userRepository.selectAll();
		} catch (DataAccessException e) {
			e.printStackTrace();
			userEntity = null;
		}
		return userEntity;
	}

	/**
	 * ユーザ情報を１件追加する
	 *
	 * @param form ユーザ情報
	 * @param user_id ユーザID
	 * @return boolean 可否
	 */
	public boolean insert(UserForm form, String user_id) {
		Date date = new Date();
		int rowNumber;
		UserData userData = new UserData();
		userData.setUser_id(form.getUser_id());
		userData.setPassword(form.getPassword());
		userData.setUserName(form.getUserName());
		userData.setUserAuthority(form.getUserAuthority());
		userData.setBelongClass(form.getBelongClass());
		userData.setStudentNumber(form.getStudentNumber());
		userData.setRegiDate(date);
		userData.setRegiUser(user_id);
		try {
			rowNumber = userRepository.insertOne(userData);
		} catch (DataAccessException e) {
			e.printStackTrace();
			rowNumber = 0;
		}
		return rowNumber > 0;
	}

	/**
	 * ユーザの詳細情報を取得（手抜き）
	 *
	 * @param user_id ユーザID
	 * @return UserData
	 */
	public UserFormForUpdate getDetail(String user_id) {
		UserFormForUpdate form = new UserFormForUpdate();
		UserData userData = userRepository.selectOne(user_id);
		form.setUser_id(userData.getUser_id());
		form.setPassword(userData.getPassword());
		form.setUserName(userData.getUserName());
		form.setUserAuthority(userData.getUserAuthority());
		form.setBelongClass(userData.getBelongClass());
		form.setStudentNumber(userData.getStudentNumber());
		form.setUserStatus(userData.getUserStatus());
		return form;
	}

	/**
	 * ユーザ情報を１件削除
	 *
	 * @param user_id ユーザID
	 * @return boolean 可否
	 */
	public boolean delete(String user_id) {
		int rowNumber;
		try {
			rowNumber = userRepository.deleteOne(user_id);
		} catch (DataAccessException e) {
			e.printStackTrace();
			rowNumber = 0;
		}
		return rowNumber > 0;
	}

	/**
	 * ユーザ情報を更新する
	 *
	 * @param update 更新情報
	 * @param user_id ユーザID
	 * @return boolean 可否
	 */
	public boolean update(UserFormForUpdate update, String user_id) {
		Date date = new Date();
		int rowNumber;
		boolean flg = false;
		UserData userData = new UserData();
		userData.setUser_id(update.getUser_id());
		if (update.getPassword() != "") {
			userData.setPassword(update.getPassword());
			flg = true;
		}
		userData.setUserName(update.getUserName());
		userData.setUserAuthority(update.getUserAuthority());
		userData.setBelongClass(update.getBelongClass());
		userData.setStudentNumber(update.getStudentNumber());
		userData.setUserStatus(update.getUserStatus());
		userData.setUpDate(date);
		userData.setUpUser(user_id);
		userData.setPasswordErrorCount(0);

		try {
			if (flg) {
				rowNumber = userRepository.updateOneWithPassword(userData);
			} else {
				rowNumber = userRepository.updateOne(userData);
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			rowNumber = 0;
		}
		return rowNumber > 0;
	}

	/**
	 * ユーザの絞り込み検索を行う
	 *
	 * @param user_name 検索するユーザ名
	 * @return userEntity
	 */
	public UserEntity filteredSearch(String user_name) {
		UserEntity userEntity;
		try {
			// 入力値制御
			if (user_name != "") {
				userEntity = userRepository.filteredSearch(user_name);
				if (userEntity.getUserlist().size() == 0) {
					userEntity = userRepository.selectAll();
					userEntity.setMessage("一致するユーザが見つからなかったため一覧を表示します");
				}
			} else {
				userEntity = userRepository.selectAll();
				userEntity.setMessage("文字が入力されなかったため一覧を表示します");
			}
		} catch (DataAccessException e) {
			e.printStackTrace();
			userEntity = null;
		}
		return userEntity;
	}

	/**
	 * 指定したユーザIDのユーザ情報を取得する.
	 * @param user_id ユーザID
	 * @return UserData
	 */
	public UserData selectOne(String user_id) {
		UserData data = userRepository.selectOne(user_id);
		return data;
	}

	/**
	 * (管理用)ユーザ情報を1件更新する(パスワード更新無).
	 * @param userData 更新するユーザ情報(パスワードは設定しない)
	 * @return 処理結果(成功:true, 失敗:false)
	 */
	public boolean updateOne(UserData userData) {
		int rowNumber = userRepository.updateOne(userData);
		return rowNumber > 0;
	}
}
