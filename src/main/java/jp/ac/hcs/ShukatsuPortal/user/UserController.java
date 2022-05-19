package jp.ac.hcs.ShukatsuPortal.user;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ユーザに関する機能・画面を制御する.
 */

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	/**
	 * ユーザの一覧画面を検索し表示する
	 *
	 * @param model 変数情報
	 * @return ユーザ一覧画面
	 */
	@GetMapping("/user/list")
	public String getUserList(Model model) {
		UserEntity userEntity = userService.getUserlist();
		model.addAttribute("userEntity", userEntity);
		return "user/list";
	}

	/**
	 * ユーザ登録画面（管理者用）を表示する
	 *
	 * @param form  登録時の入力チェック用UserForm
	 * @param model 変数情報
	 * @return ユーザ登録画面（管理者用）
	 */
	@GetMapping("/user/insert")
	public String getUserInsert(UserForm form, Model model) {
		return "user/insert";
	}

	/**
	 * 1件分のユーザ情報をデータベースに登録する
	 *
	 * @param form 登録するユーザ情報（パスワードは平文）
	 * @param bindingResult データバインド実施結果
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return ユーザ一覧画面
	 */
	@PostMapping("/user/insert")
	public String getUserInsert(@ModelAttribute @Validated UserForm form, BindingResult bindingResult,
			Principal principal, Model model) {
		if (bindingResult.hasErrors()) {
			return getUserInsert(form, model);
		}
		boolean result = userService.insert(form, principal.getName());
		if (result) {
			model.addAttribute("message", "登録に成功しました");
		} else {
			model.addAttribute("errorMessage", "登録に失敗しました");
		}
		return getUserList(model);
	}

	/**
	 * ユーザ情報の詳細画面を表示する
	 *
	 * @param user_id ユーザID
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return ユーザ情報詳細画面
	 */
	@GetMapping("/user/detail/{id}")
	public String getUserDetail(@PathVariable("id") String user_id, Principal principal, Model model) {
		UserFormForUpdate userFormForUpdate = userService.getDetail(user_id);
		model.addAttribute("userFormForUpdate", userFormForUpdate);
		return "user/detail";
	}

	/**
	 * 選択したユーザ情報を削除する
	 *
	 * @param user_id ユーザID
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return ユーザ一覧画面
	 */
	@PostMapping("/user/delete")
	public String deleteUser(@RequestParam("user_id") String user_id, Principal principal, Model model) {
		boolean result = userService.delete(user_id);
		if (result) {
			model.addAttribute("message", "選択したユーザは正常に削除されました");
		}
		return getUserList(model);
	}

	/**
	 * ユーザ情報の更新をする
	 *
	 * @param update 更新情報のフォーム
	 * @param principal ログイン情報
	 * @param bindingResult データバインド実施結果
	 * @param model 変数情報
	 * @return ユーザ一覧画面
	 */
	@PostMapping("/user/update")
	public String update(@ModelAttribute @Validated UserFormForUpdate update, BindingResult bindingResult,
			Principal principal, Model model) {
		if (bindingResult.hasErrors()) {
			return "user/detail";
		}
		boolean result = userService.update(update, principal.getName());
		if (result) {
			model.addAttribute("message", "選択したユーザの情報を更新しました");
		} else {
			model.addAttribute("errorMessage", "更新に失敗しました");
		}
		return getUserList(model);
	}

	/**
	 * ユーザを名前検索する
	 *
	 * @param userName 検索したいユーザ名(完全一致)
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return ユーザ一覧画面
	 */
	@PostMapping("/user/search")
	public String search(@RequestParam("user_name") String userName, Principal principal, Model model) {
		UserEntity userEntity;
		userEntity = userService.filteredSearch(userName);
		model.addAttribute("userEntity", userEntity);
		return "user/list";
	}
}
