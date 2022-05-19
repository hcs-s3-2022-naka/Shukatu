package jp.ac.hcs.ShukatsuPortal.main;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import jp.ac.hcs.ShukatsuPortal.user.UserData;
import jp.ac.hcs.ShukatsuPortal.user.UserService;
import lombok.extern.slf4j.Slf4j;

/**
 *  認証に関するイベントを制御する.
 */
@Slf4j
@Component
public class AuthenticationEventListeners {

	/** ロックを行うパスワード誤りの回数 */
	private static final int ERROR_LIMIT = 3;

	@Autowired
	UserService userService;

	@Autowired
	HttpSession session;

	/**
	 * ログイン失敗時イベント
	 * @param event ログイン失敗時
	 */
	@EventListener
	public void loginFailureEventHandler(AuthenticationFailureBadCredentialsEvent event) {

		// 前回のロックメッセージを削除
		session.setAttribute("UserLocked", null);

		String username = (String) event.getAuthentication().getPrincipal();
		//log.info("[ログイン失敗]入力ユーザ名:" + username);

		try {
			UserData userData = userService.selectOne(username);

			// パスワードエラー回数をインクリメント
			userData.setPasswordErrorCount(userData.getPasswordErrorCount() + 1);
			// ロック
			if (userData.getPasswordErrorCount() >= ERROR_LIMIT) {
				userData.setUserStatus("LOCKED");
			}
			userService.updateOne(userData);

			log.info("[ログイン失敗]入力ユーザ:" + username
					+ ", user_status:" + userData.getUserStatus()
					+ ", password_error_count:" + userData.getPasswordErrorCount());

		} catch (IndexOutOfBoundsException e) {
			log.info("[ログイン失敗]入力ユーザ:" + username + ", 存在しないユーザ");
		}
	}
}
