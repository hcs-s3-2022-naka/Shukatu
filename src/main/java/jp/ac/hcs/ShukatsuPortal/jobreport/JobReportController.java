package jp.ac.hcs.ShukatsuPortal.jobreport;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jp.ac.hcs.ShukatsuPortal.jobapp.JobAppController;
import jp.ac.hcs.ShukatsuPortal.jobapp.JobAppForm;
import jp.ac.hcs.ShukatsuPortal.jobapp.JobAppService;
import jp.ac.hcs.ShukatsuPortal.jobapp.ResEntity;

/**
 * 就職活動報告に関する機能・画面を制御する.
 */
@Controller
public class JobReportController {

	@Autowired
	JobReportService jobReportService;
	@Autowired
	JobAppService jobAppService;
	@Autowired
	JobAppController jobAppController;

	/**
	 * 報告新規登録画面を表示する
	 *
	 * @param appId     申請ID
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return 報告新規登録画面
	 */
	@GetMapping("/jobreport/jobreportinsert/{appId}")
	public String getJobReportInsert(@PathVariable("appId") String appId, Principal principal, Model model) {
		// 権限取得
		String role = ((Authentication) principal).getAuthorities().toString().replace("[", "").replace("]", "");
		// 会社取得
		JobReportEntity entity = jobReportService.getJobReportDetail(appId);
		model.addAttribute("appId", appId);
		model.addAttribute("jobReportEntity", entity);

		// 権限が学生か担任の場合
		if (role.equals("STUDENT") || role.equals("TEACHER")) {
			return "jobreport/jobreportinsert";
			// その他権限の場合はメイン画面へ
		} else {
			return "index";
		}
	}

	/**
	 * 報告新規登録画面を表示する(エラー後）
	 *
	 * @param form フォーム
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return 報告新規登録画面
	 */
	public String getJobReportInsertAfter(JobReportForm form, Principal principal, Model model) {
		// 権限取得
		String role = ((Authentication) principal).getAuthorities().toString().replace("[", "").replace("]", "");
		// 申請の詳細を取得
		JobReportEntity entity = jobReportService.updateDetail(form, form.getAppId());
		model.addAttribute("appId", form.getAppId());
		model.addAttribute("jobReportEntity", entity);

		// 権限が学生か担任の場合
		if (role.equals("STUDENT") || role.equals("TEACHER")) {
			return "jobreport/jobreportinsert";
			// その他の権限の場合はメイン画面へ
		} else {
			return "index";
		}
	}

	/**
	 * 1件分の報告情報をデータベースに登録する
	 *
	 * @param form 登録する申請報告情報
	 * @param bindingResult データバインド実施結果
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return 申請一覧画面
	 */
	@PostMapping("/jobreport/jobreportinsert/")
	public String JobReportInsert(@ModelAttribute @Validated JobReportForm form, BindingResult bindingResult,
			Principal principal, Model model) {

		// データバインドでエラーがあった場合
		if (bindingResult.hasErrors()) {
			model.addAttribute("checkMessage", "報告コメントが未入力です");

			return getJobReportInsertAfter(form, principal, model);

		}
		// 追加処理
		int rowNumber = jobReportService.insert(form, form.getAppId());
		//DB登録に成功した場合
		if (rowNumber > 0) {
			model.addAttribute("message", "登録に成功しました");
		//DB登録に失敗した場合
		} else {
			model.addAttribute("errorMessage", "登録に失敗しました");
		}

		return jobAppController.getJobAppList(principal, model);

	}

	/**
	 * 報告詳細の表示を行う
	 *
	 * @param appId 申請ID
	 * @param form 差戻用フォーム
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return 報告詳細画面
	 */
	@GetMapping("/jobreport/jobreportdetail/{appId}")
	public String JobReportDetail(@PathVariable("appId") String appId, JobReportFormForRemand form, Principal principal,
			Model model) {
		// 権限取得
		String role = ((Authentication) principal).getAuthorities().toString().replace("[", "").replace("]", "");
		// 報告詳細の取得
		JobReportEntity entity = jobReportService.getJobReportDetail(appId);
		model.addAttribute("appId", appId);
		model.addAttribute("jobReportEntity", entity);

		// 学生の場合
		if (role.equals("STUDENT")) {
			return "jobreport/jobreportdetail_s";
			// 担任の場合
		} else if (role.equals("TEACHER")) {
			ResEntity resEntity = jobAppService.errorCheckAi(entity.getRepComment());
			model.addAttribute("resEntity", resEntity);
			return "jobreport/jobreportdetail_t";
			// それ以外の場合はメイン画面へ
		} else {
			return "index";
		}
	}

	/**
	 * 報告の差戻しを行う
	 *
	 * @param form 差戻用フォーム
	 * @param principal ログイン情報
	 * @param bindingResult データバインド実施結果
	 * @param model 変数情報
	 * @return 申請報告一覧画面
	 */
	@PostMapping("/jobreport/jobreportremand/")
	public String JobReportRemand(@ModelAttribute @Validated JobReportFormForRemand form, BindingResult bindingResult,
			Principal principal, Model model) {

		// データバインドでエラーがあった場合
		if (bindingResult.hasErrors()) {
			model.addAttribute("checkMessage", "担任コメントが未入力です");
			return JobReportDetail(form.getAppId(), form, principal, model);
		}
		// 差戻処理
		int rowNumber = jobReportService.remand(form);
		if (rowNumber > 0) {
			model.addAttribute("message", "登録に成功しました");
		} else {
			model.addAttribute("errorMessage", "登録に失敗しました");
		}

		return jobAppController.getJobAppList(principal, model);
	}

	/**
	 * 報告の承認を行う
	 *
	 * @param form 申請フォーム
	 * @param bindingResult データバインド実施結果
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return 申請報告一覧画面
	 */
	@PostMapping("/jobreport/jobreportapproval/")
	public String JobReportApproval(@ModelAttribute @Validated JobAppForm form, BindingResult bindingResult,
			Principal principal, Model model) {
		int rowNumber = jobReportService.approval(form);
		if (rowNumber > 0) {
			model.addAttribute("message", "登録に成功しました");
		} else {
			model.addAttribute("errorMessage", "登録に失敗しました");
		}
		return jobAppController.getJobAppList(principal, model);
	}

	/**
	 * 報告の取消を行う
	 *
	 * @param form 報告フォーム
	 * @param principal ログイン情報
	 * @param bindingResult データバインド実施結果
	 * @param model 変数情報
	 * @return 申請報告一覧画面
	 */
	@PostMapping("/jobreport/jobreportcancel/")
	public String JobReportCancel(@ModelAttribute @Validated JobReportForm form, BindingResult bindingResult,
			Principal principal, Model model) {

		// 取消処理
		int rowNumber = jobReportService.cancel(form);

		if (rowNumber > 0) {
			model.addAttribute("message", "登録に成功しました");
		} else {
			model.addAttribute("errorMessage", "登録に失敗しました");
		}
		return jobAppController.getJobAppList(principal, model);
	}

	/**
	 * 報告変更登録画面を表示する
	 *
	 * @param appId 申請ID
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return 報告詳細変更画面
	 */
	@GetMapping("/jobreport/jobreportupdate/{appId}")
	public String getJobReportUpdate(@PathVariable("appId") String appId, Principal principal, Model model) {
		// 権限取得
		String role = ((Authentication) principal).getAuthorities().toString().replace("[", "").replace("]", "");
		// 報告詳細取得
		JobReportEntity entity = jobReportService.getJobReportDetail(appId);
		model.addAttribute("appId", appId);
		model.addAttribute("jobReportEntity", entity);

		// 権限が学生か担任の場合
		if (role.equals("STUDENT") || role.equals("TEACHER")) {
			return "jobreport/jobreportupdate";
			// それ以外の場合はメイン画面へ
		} else {
			return "index";
		}
	}

	/**
	 * 報告変更登録画面を表示する（変更エラー後）
	 *
	 * @param appId 申請ID
	 * @param form 報告用フォーム
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return 報告詳細変更画面
	 */
	public String getJobReportUpdateDetail(String appId, JobReportForm form, Principal principal, Model model) {
		// 権限取得
		String role = ((Authentication) principal).getAuthorities().toString().replace("[", "").replace("]", "");
		// 変更時詳細を取得
		JobReportEntity entity = jobReportService.updateDetail(form, form.getAppId());
		model.addAttribute("appId", form.getAppId());
		model.addAttribute("jobReportEntity", entity);

		// 権限が学生か担任の場合
		if (role.equals("STUDENT") || role.equals("TEACHER")) {
			return "jobreport/jobreportupdate";
			// それ以外の場合はメイン画面へ
		} else {
			return "index";
		}
	}

	/**
	 * 報告の内容変更をする
	 *
	 * @param form 更新情報のフォーム
	 * @param bindingResult データバインド実施結果
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return 受験報告一覧画面
	 */
	@PostMapping("/jobreport/jobreportupdate/")
	public String JobReportUpdate(@ModelAttribute @Validated JobReportForm form, BindingResult bindingResult,
			Principal principal, Model model) {
		// データバインドでエラーがあった場合
		if (bindingResult.hasErrors()) {
			model.addAttribute("checkMessage", "報告コメントが未入力です");
			return getJobReportUpdateDetail(form.getAppId(), form, principal, model);
		}

		try {
			// 更新処理
			int rowNumber = jobReportService.updateDone(form, form.getAppId());
			if (rowNumber > 0) {
				model.addAttribute("message", "登録に成功しました");
			} else {
				model.addAttribute("errorMessage", "登録に失敗しました");
			}
		}catch (DataAccessException e) {
			model.addAttribute("errorMessage", "登録に失敗しました");
		}
		return jobAppController.getJobAppList(principal, model);
	}

}
