package jp.ac.hcs.ShukatsuPortal.jobapp;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jp.ac.hcs.ShukatsuPortal.WebConfig;
import jp.ac.hcs.ShukatsuPortal.user.UserData;
import jp.ac.hcs.ShukatsuPortal.user.UserService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;

/**
 * 就職活動申請に関する機能・画面を制御する
 */
@Slf4j
@Controller
public class JobAppController {
	@Autowired
	JobAppService jobAppService;

	@Autowired
	UserService userService;

	@Autowired
	HttpSession session;

	/**
	 * 就職活動申請の一覧を表示する
	 *
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return 就職活動申請一覧画面
	 */
	@GetMapping("/jobapp/jobapplist")
	public String getJobAppList(Principal principal, Model model) {
		// 権限取得
		String role = ((Authentication) principal).getAuthorities().toString().replace("[", "").replace("]", "");
		// user_id取得
		String userId = principal.getName();
		JobAppFormForSearch search = new JobAppFormForSearch();
		JobAppEntity jobAppEntity;

		try {
			// 通知リストを取得
			JobAppNotification(principal, model);

			// 権限が学生の場合
			if (role.equals("STUDENT")) {
				// 申請一覧取得
				jobAppEntity = jobAppService.getJobAppListStudent(userId);
				// 申請一覧取得
				jobAppEntity = jobAppService.getJobAppListStudent(userId);
				model.addAttribute("searchForm", search);
				session.setAttribute("jobapp", jobAppEntity);
				model.addAttribute("jobAppEntity", jobAppEntity);

				return "jobapp/jobapplist_s";

				// 権限が担任の場合
			} else if (role.equals("TEACHER")) {
				// 申請一覧取得
				jobAppEntity = jobAppService.getJobAppListTeacher();
				model.addAttribute("searchForm", search);
				session.setAttribute("jobapp", jobAppEntity);
				model.addAttribute("jobAppEntity", jobAppEntity);

				return "jobapp/jobapplist_t";

			} else {
				// 権限が学生でも担任でもない場合
				return "index";

			}

		} catch (DataAccessException e) {
			// エラー時メイン画面に推移
			return "index";

		}
	}

	/**
	 * 就職活動申請の検索条件をクリアする
	 *
	 * @param principal ログイン情報
	 * @param model	変数情報
	 * @return 就職活動申請一覧画面
	 */
	@GetMapping("/jobapp/clear")
	public String getJobAppListClear(Principal principal, Model model) {
		// 権限取得
		String role = ((Authentication) principal).getAuthorities().toString().replace("[", "").replace("]", "");
		// user_id取得
		String name = principal.getName();
		JobAppFormForSearch search = new JobAppFormForSearch();
		JobAppEntity jobAppEntity;
		// 通知リストを取得
		JobAppNotification(principal, model);

		try {
			// 権限が学生の場合
			if (role.equals("STUDENT")) {
				// 申請一覧取得
				jobAppEntity = jobAppService.getJobAppListStudent(name);
				model.addAttribute("searchForm", search);
				session.setAttribute("jobapp", jobAppEntity);
				model.addAttribute("jobAppEntity", jobAppEntity);

				return "jobapp/jobapplist_s";

				// 権限が担任の場合
			} else if (role.equals("TEACHER")) {
				// 申請一覧取得
				jobAppEntity = jobAppService.getJobAppListTeacher();
				model.addAttribute("searchForm", search);
				session.setAttribute("jobapp", jobAppEntity);
				model.addAttribute("jobAppEntity", jobAppEntity);

				return "jobapp/jobapplist_t";

			} else {
				// 権限が学生でも担任でもない場合
				return "index";

			}

		} catch (DataAccessException e) {
			// エラー時メイン画面に推移
			return "index";

		}
	}

	/**
	 * 一覧検索を行う
	 *
	 * @param search    検索用申請フォーム
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @param bindingResult データバインド実施結果
	 * @return 就職活動申請一覧画面
	 */
	@PostMapping("/jobapp/search")
	public String searchApp(@ModelAttribute @Validated JobAppFormForSearch search, BindingResult bindingResult,
			Principal principal, Model model) {
		// 権限取得
		String role = ((Authentication) principal).getAuthorities().toString().replace("[", "").replace("]", "");
		// user_id取得
		String userId = principal.getName();
		JobAppEntity jobAppEntity = null;

		try {
			// 検索ボタン押下
			if (search.getButtonValue() == 1) {
				// 権限が学生の場合
				if (role.equals("STUDENT")) {
					// 申請検索
					jobAppEntity = jobAppService.searchJobAppStudent(search, userId);
					// 権限が担任の場合
				} else if (role.equals("TEACHER")) {
					// 申請検索
					jobAppEntity = jobAppService.searchJobAppTeacher(search);
				}
				// とりまとめボタン押下
			} else if (search.getButtonValue() == 2) {
				// 権限が学生の場合
				if (role.equals("STUDENT")) {
					// とりまとめ検索
					jobAppEntity = jobAppService.searchSummaryStudent(search, userId);
					// 権限が担任の場合
				} else if (role.equals("TEACHER")) {
					// とりまとめ検索
					jobAppEntity = jobAppService.searchSummaryTeacher(search);
				}
			}
			// 検索結果が0件の場合
			if (jobAppEntity.getJobAppList().size() < 1) {
				model.addAttribute("errorMessage", "検索条件に該当する申請は0件でした");
			}
		} catch (DataAccessException e) {
			// エラー時メイン画面に推移
			return "index";

		}
		// 通知リストを取得
		JobAppNotification(principal, model);
		model.addAttribute("searchForm", search);
		model.addAttribute("jobAppEntity", jobAppEntity);
		session.setAttribute("jobapp", jobAppEntity);
		// 権限が学生の場合
		if (role.equals("STUDENT")) {

			return "jobapp/jobapplist_s";

			// 権限が担任の場合
		} else if (role.equals("TEACHER")) {

			return "jobapp/jobapplist_t";

			// 権限が学生でも担任でもない場合
		} else {

			return "index";
		}
	}

	/**
	 * 新規申請作成を行う
	 *
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @param jobAppForm 新規登録用フォーム
	 * @return 申請新規作成画面
	 */
	@GetMapping("/jobapp/jobappinsert/")
	public String JobAppInsert(Principal principal, Model model, JobAppForm jobAppForm) {
		// 作成されるユーザ情報の取得
		UserData userData = jobAppService.selectUser(principal.getName());
		model.addAttribute("userData", userData);
		model.addAttribute("jobAppForm", jobAppForm);

		return "jobapp/jobappinsert";

	}

	/**
	 * 申請詳細の表示を行う
	 *
	 * @param appId 申請ID
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return 申請詳細画面
	 */
	@GetMapping("/jobapp/jobappdetail/{appId}")
	public String JobAppDetail(@PathVariable("appId") String appId, Principal principal, Model model) {
		// 権限取得
		String role = ((Authentication) principal).getAuthorities().toString().replace("[", "").replace("]", "");
		// 詳細取得
		JobAppData data = jobAppService.getJobAppDetail(appId);
		// 申請企業取得
		JobAppEntity entity = jobAppService.getJobAppCompany(appId);
		model.addAttribute("jobAppData", data);
		model.addAttribute("jobAppEntity", entity);
		// 権限が学生の場合
		if (role.equals("STUDENT")) {

			return "jobapp/jobappdetail_s";

			// 権限が担任の場合
		} else if (role.equals("TEACHER")) {
			//誤字脱字チェック
			ResEntity resEntity = jobAppService.errorCheckAi(data.getAppMemo());
			model.addAttribute("resEntity", resEntity);
			return "jobapp/jobappdetail_t";

			// 権限が学生でも担任でもない場合
		} else {

			return "index";

		}
	}

	/**
	 * 申請新規作成情報のDB登録
	 *
	 * @param jobAppForm 新規登録用フォーム
	 * @param principal  ログイン情報
	 * @param model 変数情報
	 * @param bindingResult データバインド実施結果
	 * @return 申請一覧画面
	 */
	@PostMapping("/jobapp/jobappinsert/")
	public String JobAppInsertDb(@ModelAttribute @Validated JobAppForm jobAppForm, BindingResult bindingResult,
			Principal principal, Model model) {
		// 必須入力チェック
		if (bindingResult.hasErrors()) {
			model.addAttribute("message", "入力項目に誤りがあります。または必須項目が未入力です。");
			// 申請新規作成
			return JobAppInsert(principal, model, jobAppForm);
		}

		String role = ((Authentication) principal).getAuthorities().toString().replace("[", "").replace("]", "");
		// 会社名必須入力チェック
		if (jobAppForm.getAppCompany().get(0).equals("")) {
			model.addAttribute("errorMessage", "会社名は必須入力です");
			// 申請新規作成
			return JobAppInsert(principal, model, jobAppForm);

		}

		//欠席チェック
		if (jobAppForm.isAbsenceFlag()) {
			if (jobAppForm.getAppStartAbsence().length() == 0 | jobAppForm.getAppFinishAbsence().length() == 0) {
				model.addAttribute("errorMessage", "欠席・遅刻・早退のいずれかが未入力です");
				// 申請新規作成
				return JobAppInsert(principal, model, jobAppForm);
			}
		} else if (!jobAppForm.isAbsenceFlag()) {
			if (jobAppForm.getAppStartAbsence().length() != 0 | jobAppForm.getAppFinishAbsence().length() != 0) {
				model.addAttribute("errorMessage", "欠席・遅刻・早退チェックが入っていません");
				// 申請新規作成
				return JobAppInsert(principal, model, jobAppForm);
			}
		}
		//遅刻チェック
		if (jobAppForm.isLeaveFlag()) {
			if (jobAppForm.getAppLeaveDate().length() == 0 | jobAppForm.getAppLeaveTime().length() == 0) {
				model.addAttribute("errorMessage", "欠席・遅刻・早退のいずれかが未入力です");
				// 申請新規作成
				return JobAppInsert(principal, model, jobAppForm);
			}
		} else if (!jobAppForm.isLeaveFlag()) {
			if (jobAppForm.getAppLeaveDate().length() != 0 | jobAppForm.getAppLeaveTime().length() != 0) {
				model.addAttribute("errorMessage", "欠席・遅刻・早退チェックが入っていません");
				// 申請新規作成
				return JobAppInsert(principal, model, jobAppForm);
			}
		}
		//早退チェック
		if (jobAppForm.isLateFlag()) {
			if (jobAppForm.getAppLateDate().length() == 0 | jobAppForm.getAppLateTime().length() == 0) {
				model.addAttribute("errorMessage", "欠席・遅刻・早退のいずれかが未入力です");
				// 申請新規作成
				return JobAppInsert(principal, model, jobAppForm);
			}
		} else if (!jobAppForm.isLateFlag()) {
			if (jobAppForm.getAppLateDate().length() != 0 | jobAppForm.getAppLateTime().length() != 0) {
				model.addAttribute("errorMessage", "欠席・遅刻・早退チェックが入っていません");
				// 申請新規作成
				return JobAppInsert(principal, model, jobAppForm);
			}
		}

		//会社のnull,重複チェック
		for (int i = 0; i < jobAppForm.getAppCompany().size() - 1; i++) {
			for (int j = i + 1; j < jobAppForm.getAppCompany().size(); j++) {
				//NULL
				if (jobAppForm.getAppCompany().get(j).equals("")) {
					model.addAttribute("errorMessage", "会社名が不正です。");
					// 申請新規作成
					return JobAppInsert(principal, model, jobAppForm);
				}
				//重複
				if (jobAppForm.getAppCompany().get(i).equals(jobAppForm.getAppCompany().get(j))) {
					model.addAttribute("errorMessage", "会社名が不正です。");
					// 申請新規作成
					return JobAppInsert(principal, model, jobAppForm);
				}
			}
		}

		//合企のときに会社が複数追加できない
		if (jobAppForm.getAppContents() != 1 & jobAppForm.getAppCompany().size() > 1) {
			model.addAttribute("errorMessage", "会社名の複数追加は合企のみです。");
			// 申請新規作成
			return JobAppInsert(principal, model, jobAppForm);
		}

		// DB登録処理
		int rowNumber = jobAppService.insertJobApp(jobAppForm, principal.getName(), role);
		// 申請新規作成が成功した場合
		if (rowNumber > 0) {
			model.addAttribute("message", "登録に成功しました");
			// 申請新規作成が失敗した場合
		} else if (rowNumber == 0) {
			model.addAttribute("errorMessage", "登録に失敗しました");
			// 入力されたクラス番号がDBに存在しなかったとき
		} else if (rowNumber == -1) {
			model.addAttribute("errorMessage", "存在しないユーザを登録しようとしています");
			// 申請新規作成に推移
			return JobAppInsert(principal, model, jobAppForm);
			// 日時の入力チェックがエラーの場合
		} else if (rowNumber == -2) {
			model.addAttribute("errorMessage", "終了日時が不正です");
			// 申請新規作成に推移
			return JobAppInsert(principal, model, jobAppForm);
		}
		return getJobAppList(principal, model);

	}

	/**
	 * 検索した条件の一覧をCSVファイルとしてダウンロードさせる
	 *
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return 申請一覧のCSVファイル
	 */
	@PostMapping("/jobapp/jobappcsv/")
	public ResponseEntity<byte[]> getReportCsv(Principal principal, Model model) {
		// 現在日時取得
		Date date = new Date();
		// ファイル名に使用するタイムスタンプを設定
		WebConfig.timestamp = date.toString().replace(" ", "").replace(":", "");
		// セッションから一覧に表示されている申請を取得
		JobAppEntity jobAppEntity = (JobAppEntity) session.getAttribute("jobapp");
		List<String> appIds = new ArrayList<String>();
		// ファイル名生成
		final String OUTPUT_FULLPATH = WebConfig.OUTPUT_PATH + WebConfig.timestamp + WebConfig.FILENAME_JOBAPPLIST_CSV;
		// ログ出力
		log.info("[" + principal.getName() + "]CSVファイル作成:" + OUTPUT_FULLPATH);

		// 一覧に表示されている申請の申請IDをリスト化
		for (JobAppData data : jobAppEntity.getJobAppList()) {
			appIds.add(Integer.toString(data.getAppId()));
		}
		// CSVファイルをサーバ上に作成
		jobAppService.appListCsvOut(appIds);

		// CSVファイルをサーバから読み込み
		byte[] bytes = null;
		try {
			// サーバーに保存されているファイルを取得して、byte配列に変換する
			bytes = jobAppService.getFile(OUTPUT_FULLPATH);
			// ログ出力
			log.info("[" + principal.getName() + "]CSVファイル読み込み成功:" + OUTPUT_FULLPATH);
		} catch (IOException e) {
			// ログ出力
			log.warn("[" + principal.getName() + "]CSVファイル読み込み失敗:" + OUTPUT_FULLPATH);
			e.printStackTrace();
		}

		// CSVファイルのダウンロード用ヘッダー情報設定
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "text/csv; charset=UTF-8");
		header.setContentDispositionFormData("filename", WebConfig.FILENAME_JOBAPPLIST_CSV);

		// CSVファイルを端末へ送信
		return new ResponseEntity<byte[]>(bytes, header, HttpStatus.OK);

	}

	/**
	 * 申請の差戻しを行う
	 *
	 * @param form 差戻用フォーム
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @param bindingResult データバインド実施結果
	 * @return 申請一覧画面
	 */
	@PostMapping("/jobapp/jobappremand/")
	public String JobAppRemand(@ModelAttribute @Validated JobAppFormForRemand form, BindingResult bindingResult,
			Principal principal, Model model) {
		// 入力チェック
		if (bindingResult.hasErrors()) {
			model.addAttribute("checkMessage", "担任コメントが未入力です");
			// エラー時申請詳細画面に推移
			return JobAppDetail(form.getAppId(), principal, model);

		}
		// 差戻し登録処理
		int result = jobAppService.updateRemand(form);
		// 差戻しの登録が成功した場合
		if (result == 1) {
			model.addAttribute("message", "差戻しの登録に成功しました");
			// 差戻の登録に失敗した場合
		} else {
			model.addAttribute("errorMessage", "登録に失敗しました");
		}

		return getJobAppList(principal, model);

	}

	/**
	 * コース担当に連絡済みの申請をDBに登録する 申請状態を申請完了にする
	 *
	 * @param appId 申請ID
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return 申請一覧画面
	 */
	@GetMapping("/jobapp/jopappcharge/{appId}")
	public String JobAppCharge(@PathVariable("appId") String appId, Principal principal, Model model) {
		// 申請のコース担当連絡登録と申請状態を申請完了にする
		int result = jobAppService.updateApprovalFlag(appId);
		// DB登録に成功した場合
		if (result == 1) {
			model.addAttribute("message", "申請完了の登録に成功しました");
			// DB登録に失敗した場合
		} else {
			model.addAttribute("errorMessage", "登録に失敗しました");
		}

		return getJobAppList(principal, model);

	}

	/**
	 * 申請状態を申請待から申請承認済に変更する
	 *
	 * @param appId 申請ID
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return 申請一覧画面
	 */
	@GetMapping("/jobapp/jopappapproval/{appId}")
	public String JobAppApproval(@PathVariable("appId") String appId, Principal principal, Model model) {
		// 申請状態を申請待から申請承認済に変更
		int result = jobAppService.updateApproval(appId);
		// DB登録に成功した場合
		if (result == 1) {
			model.addAttribute("message", "申請承認済の登録に成功しました");
			// DB登録に失敗した場合
		} else {
			model.addAttribute("errorMessage", "登録に失敗しました");
		}

		return getJobAppList(principal, model);

	}

	/**
	 * 申請詳細の変更画面を表示する
	 *
	 * @param appId 申請ID
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @param jobAppUpdateForm 変更用フォーム
	 * @return 申請内容変更画面
	 */
	@GetMapping("/jobapp/jopappchange/{appId}")
	public String getJobAppChange(@PathVariable("appId") String appId, Principal principal, Model model,
			JobAppFormForUpdate jobAppUpdateForm) {
		// デフォルトデータの取得
		JobAppData jobAppData = jobAppService.getJobAppDetail(appId);
		// dataをformに変換（チェックのため）
		jobAppUpdateForm = jobAppService.changeJobApp(jobAppData);
		// 会社情報の取得
		JobAppEntity jobAppEntity = jobAppService.getJobAppCompany(appId);
		// 書類情報の取得
		JobAppData jobAppDocument = jobAppService.getjobAppDocument(appId);

		model.addAttribute("jobAppUpdateForm", jobAppUpdateForm);
		model.addAttribute("jobAppEntity", jobAppEntity);
		model.addAttribute("jobAppDocument", jobAppDocument);
		return "jobapp/jopappchange";
	}

	/**
	 * 申請の変更内容を登録する
	 *
	 * @param appId 申請ID
	 * @param jobAppUpdateForm 申請更新内容フォーム
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @param bindingResult データバインド実施結果
	 * @return 申請一覧画面
	 * @throws ParseException 型変換エラー
	 */
	@PostMapping("/jobapp/jobappupdate/{appId}")
	public String JobAppUpdateDb(@ModelAttribute @PathVariable("appId") String appId,
			@Validated JobAppFormForUpdate jobAppUpdateForm, BindingResult bindingResult, Principal principal,
			Model model) throws ParseException {

		// 必須入力チェック
		if (bindingResult.hasErrors()) {

			model.addAttribute("message", "入力項目に誤りがあります。または必須項目が未入力です。");
			// 申請新規作成
			return getJobAppChange(appId, principal, model, jobAppUpdateForm);
		}

		// 会社名必須入力チェック
		if (jobAppUpdateForm.getAppCompany().get(0).equals("")) {
			model.addAttribute("errorMessage", "会社名は必須入力です。");
			// 申請新規作成
			return getJobAppChange(appId, principal, model, jobAppUpdateForm);

		}

		//欠席チェック
		if (jobAppUpdateForm.isAbsenceFlag()) {
			if (jobAppUpdateForm.getAppStartAbsence().length() == 0
					| jobAppUpdateForm.getAppFinishAbsence().length() == 0) {
				model.addAttribute("errorMessage", "欠席・遅刻・早退のいずれかが未入力です。");
				// 申請新規作成
				return getJobAppChange(appId, principal, model, jobAppUpdateForm);
			}
		} else if (!jobAppUpdateForm.isAbsenceFlag()) {
			if (jobAppUpdateForm.getAppStartAbsence().length() != 0
					| jobAppUpdateForm.getAppFinishAbsence().length() != 0) {
				model.addAttribute("errorMessage", "欠席・遅刻・早退チェックが入っていません。");
				// 申請新規作成
				return getJobAppChange(appId, principal, model, jobAppUpdateForm);
			}
		}
		//遅刻チェック
		if (jobAppUpdateForm.isLeaveFlag()) {
			if (jobAppUpdateForm.getAppLeaveDate().length() == 0 | jobAppUpdateForm.getAppLeaveTime().length() == 0) {
				model.addAttribute("errorMessage", "欠席・遅刻・早退のいずれかが未入力です。");
				// 申請新規作成
				return getJobAppChange(appId, principal, model, jobAppUpdateForm);
			}
		} else if (!jobAppUpdateForm.isLeaveFlag()) {
			if (jobAppUpdateForm.getAppLeaveDate().length() != 0 | jobAppUpdateForm.getAppLeaveTime().length() != 0) {
				model.addAttribute("errorMessage", "欠席・遅刻・早退チェックが入っていません。");
				// 申請新規作成
				return getJobAppChange(appId, principal, model, jobAppUpdateForm);
			}
		}
		//早退チェック
		if (jobAppUpdateForm.isLateFlag()) {
			if (jobAppUpdateForm.getAppLateDate().length() == 0 | jobAppUpdateForm.getAppLateTime().length() == 0) {
				model.addAttribute("errorMessage", "欠席・遅刻・早退のいずれかが未入力です。");
				// 申請新規作成
				return getJobAppChange(appId, principal, model, jobAppUpdateForm);
			}
		} else if (!jobAppUpdateForm.isLateFlag()) {
			if (jobAppUpdateForm.getAppLateDate().length() != 0 | jobAppUpdateForm.getAppLateTime().length() != 0) {
				model.addAttribute("errorMessage", "欠席・遅刻・早退チェックが入っていません。");
				// 申請新規作成
				return getJobAppChange(appId, principal, model, jobAppUpdateForm);
			}
		}

		//会社のnull,重複チェック
		for (int i = 0; i < jobAppUpdateForm.getAppCompany().size() - 1; i++) {
			for (int j = i + 1; j < jobAppUpdateForm.getAppCompany().size(); j++) {
				//NULL
				if (jobAppUpdateForm.getAppCompany().get(j).equals("")) {
					model.addAttribute("errorMessage", "会社名が不正です。");
					// 申請新規作成
					return getJobAppChange(appId, principal, model, jobAppUpdateForm);
				}
				//重複
				if (jobAppUpdateForm.getAppCompany().get(i).equals(jobAppUpdateForm.getAppCompany().get(j))) {
					model.addAttribute("errorMessage", "会社名が不正です。");
					// 申請新規作成
					return getJobAppChange(appId, principal, model, jobAppUpdateForm);
				}
			}
		}

		// DB登録処理
		int rowNumber = jobAppService.updateJobApp(appId, jobAppUpdateForm);
		// DB登録に成功した場合
		if (rowNumber > 0) {
			model.addAttribute("message", "登録に成功しました");
			// DB登録に失敗した場合
		} else if (rowNumber == 0) {
			model.addAttribute("errorMessage", "登録に失敗しました");
			// 登録しようとしたユーザが存在しない場合
		} else if (rowNumber == -1) {
			model.addAttribute("errorMessage", "存在しないユーザを登録しようとしています");
			// 申請内容変更画面
			return getJobAppChange(appId, principal, model, jobAppUpdateForm);
			// 登録しようとした終了日時が正しくない場合
		} else if (rowNumber == -2) {
			model.addAttribute("errorMessage", "終了日時が不正です");
			// 申請内容変更画面
			return getJobAppChange(appId, principal, model, jobAppUpdateForm);

		}

		return getJobAppList(principal, model);

	}

	/**
	 * 提出書類の確認を行う
	 *
	 * @param appId 申請ID
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return 提出書類確認画面
	 */
	@GetMapping("/jobapp/jobappdocument/{appId}")
	public String getJobAppDocument(@PathVariable("appId") String appId, Principal principal, Model model) {
		JobAppData data = jobAppService.getJobAppDetail(appId);
		JobAppEntity entity = jobAppService.getJobAppCompany(appId);
		entity = jobAppService.getJobAppDocumentList(appId, entity);
		model.addAttribute("jobAppData", data);
		model.addAttribute("jobAppEntity", entity);

		return "/jobapp/jobappdocument";
	}

	/**
	 * 必要書類を更新する
	 *
	 * @param form 書類変更フォーム
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return 提出確認画面
	 */
	@PostMapping("/jobapp/jobappdocument")
	public String jobAppDocument(@ModelAttribute JobAppFormForDocument form, Principal principal, Model model) {
		Integer app = form.getAppId();
		int rowNumber = jobAppService.jobAppDocument(form);
		// 書類更新に成功した場合
		if (rowNumber > 0) {
			model.addAttribute("message", "更新しました");

			return getJobAppDocument(app.toString(), principal, model);
			// 書類更新に失敗した場合
		} else {
			model.addAttribute("errorMessage", "更新する書類を選択してください");

			return getJobAppDocument(app.toString(), principal, model);

		}
	}

	/**
	 * 申請の取消処理
	 *
	 * @param appId 申請ID
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return 申請一覧画面
	 */
	@GetMapping("/jobapp/jobappcancel/{appId}")
	public String JobAppCancel(@PathVariable("appId") String appId, Principal principal, Model model) {
		int rowNumber = jobAppService.cancel(appId);
		// 取消に成功した場合
		if (rowNumber > 0) {
			model.addAttribute("message", "取消に成功しました");
			// 取消に失敗した場合
		} else {
			model.addAttribute("errorMessage", "取消に失敗しました");
		}

		return getJobAppList(principal, model);

	}

	/**
	 * とりまとめ名簿登録済機能
	 *
	 * @param appId 申請ID
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return 申請報告一覧画面
	 */

	@GetMapping("/jobapp/jobappupdatesummary/{appId}")
	public String updateSummary(@PathVariable("appId") String appId, Principal principal, Model model) {
		// 登録処理を行う
		int rowNumber = jobAppService.updateSummary(appId);

		if (rowNumber > 0) {
			// 更新が成功したとき
			model.addAttribute("message", "1件のとりまとめをとりまとめ名簿登録済にしました");

		} else {
			// 更新が失敗したとき
			model.addAttribute("errorMessage", "変更に失敗しました");
		}

		return getJobAppList(principal, model);

	}

	/**
	 * 通知リスト取得機能
	 *
	 * @param principal ログイン情報
	 * @param model 変数情報
	 */
	public void JobAppNotification(Principal principal, Model model) {
		int cnt = 0;
		String role = ((Authentication) principal).getAuthorities().toString().replace("[", "").replace("]", "");
		if (role.equals("STUDENT")) {
			for (int listCnt = 1; listCnt < 9; listCnt++) {
				if (listCnt == 1) {
					// 申請の差戻し通知リスト
					String notificateId = Integer.valueOf(listCnt).toString();
					List<String> remandAppList = jobAppService.remandAppNotificationS(principal.getName(),
							notificateId);
					cnt = cnt + remandAppList.size();
					session.setAttribute("remandAppList", remandAppList);
					model.addAttribute("remandAppList", remandAppList);
				} else if (listCnt == 2) {
					// 申請の承認通知リスト
					String notificateId = Integer.valueOf(listCnt).toString();
					List<String> approvalAppList = jobAppService.approvalAppNotificationS(principal.getName(),
							notificateId);
					cnt = cnt + approvalAppList.size();
					session.setAttribute("approvalAppList", approvalAppList);
					model.addAttribute("approvalAppList", approvalAppList);
				} else if (listCnt == 3) {
					// 報告の差戻し通知リスト
					String notificateId = Integer.valueOf(listCnt).toString();
					List<String> remandReportList = jobAppService.remandReportNotificationS(principal.getName(),
							notificateId);
					cnt = cnt + remandReportList.size();
					session.setAttribute("remandReportList", remandReportList);
					model.addAttribute("remandReportList", remandReportList);
				} else if (listCnt == 4) {
					// 報告の承認通知リスト
					String notificateId = Integer.valueOf(listCnt).toString();
					List<String> approvalReportList = jobAppService.approvalReportNotificationS(principal.getName(),
							notificateId);
					cnt = cnt + approvalReportList.size();
					session.setAttribute("approvalReportList", approvalReportList);
					model.addAttribute("approvalReportList", approvalReportList);
				} else if (listCnt == 5) {
					// 直近にイベントがある申請未完了の通知リスト
					String notificateId = Integer.valueOf(listCnt).toString();
					List<String> eventIncompList = jobAppService.eventIncompNotificationS(principal.getName(),
							notificateId);
					cnt = cnt + eventIncompList.size();
					session.setAttribute("eventIncompList", eventIncompList);
					model.addAttribute("eventIncompList", eventIncompList);
				} else if (listCnt == 6) {
					// イベントがの翌朝になっても報告未提出の通知リスト
					String notificateId = Integer.valueOf(listCnt).toString();
					List<String> notSubmittedList = jobAppService.notSubmittedNotificationS(principal.getName(),
							notificateId);
					cnt = cnt + notSubmittedList.size();
					session.setAttribute("notSubmittedList", notSubmittedList);
					model.addAttribute("notSubmittedList", notSubmittedList);
				} else if (listCnt == 7) {
					// 直近にイベントがある申請の通知リスト
					List<String> eventList = jobAppService.eventNotificationS(principal.getName());
					cnt = cnt + eventList.size();
					session.setAttribute("eventList", eventList);
					model.addAttribute("eventList", eventList);
				} else if (listCnt == 8) {
					// とりまとめ書類提出のある申請の通知リスト
					String notificateId = Integer.valueOf(listCnt).toString();
					List<String> documentList = jobAppService.documentNotificationS(principal.getName(), notificateId);
					cnt = cnt + documentList.size();
					session.setAttribute("documentList", documentList);
					model.addAttribute("documentList", documentList);
				} else {
					// 書類提出締め切り直前で未提出の書類のある申請の通知リスト
					String notificateId = Integer.valueOf(listCnt).toString();
					List<String> documentLimitList = jobAppService.documentLimitNotificationS(principal.getName(),
							notificateId);
					cnt = cnt + documentLimitList.size();
					session.setAttribute("documentLimitList", documentLimitList);
					model.addAttribute("documentLimitList", documentLimitList);
				}
			}
			model.addAttribute("appListCnt", cnt);
		} else if (role.equals("TEACHER")) {
			for (int listCnt = 10; listCnt < 13; listCnt++) {
				if (listCnt == 10) {
					// 申請の承認待ち通知リスト
					String notificateId = Integer.valueOf(listCnt).toString();
					List<String> appList = jobAppService.appNotificationT(principal.getName(), notificateId);
					cnt = cnt + appList.size();
					session.setAttribute("appList", appList);
					model.addAttribute("appList", appList);
				} else if (listCnt == 11) {
					// 報告の承認待ち通知リスト
					String notificateId = Integer.valueOf(listCnt).toString();
					List<String> reportList = jobAppService.reportNotificationT(principal.getName(), notificateId);
					cnt = cnt + reportList.size();
					session.setAttribute("reportList", reportList);
					model.addAttribute("reportList", reportList);
				} else if (listCnt == 12) {
					// 今日活動の申請完了通知リスト
					List<String> eventList = jobAppService.eventNotificationT(principal.getName());
					cnt = cnt + eventList.size();
					session.setAttribute("eventList", eventList);
					model.addAttribute("eventList", eventList);
				} else {
					// コース担当への連絡完了通知リスト
					String notificateId = Integer.valueOf(listCnt).toString();
					List<String> courseList = jobAppService.courseNotificationT(principal.getName(), notificateId);
					cnt = cnt + courseList.size();
					session.setAttribute("courseList", courseList);
					model.addAttribute("courseList", courseList);

				}
			}
			model.addAttribute("appListCnt", cnt);
		}
	}

	/**
	 * 通知のリストの一覧変換処理
	 *
	 * @param listNum リスト番号
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return 申請報告一覧画面
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/jobapp/jobappNotification/{listNum}")
	public String JobAppNotificationList(@PathVariable("listNum") String listNum, Principal principal, Model model) {
		String role = ((Authentication) principal).getAuthorities().toString().replace("[", "").replace("]", "");
		JobAppFormForSearch search = new JobAppFormForSearch();
		JobAppEntity jobAppEntity = null;

		// 通知リスト押下時間の更新
		jobAppService.updateNotifiPushDate(principal.getName(), listNum);

		if (role.equals("STUDENT")) {
			if (listNum.equals("1")) {
				List<String> remandAppList = (List<String>) session.getAttribute("remandAppList");
				jobAppEntity = jobAppService.JobAppNotificationList(remandAppList);
			} else if (listNum.equals("2")) {
				List<String> approvalAppList = (List<String>) session.getAttribute("approvalAppList");
				jobAppEntity = jobAppService.JobAppNotificationList(approvalAppList);
			} else if (listNum.equals("3")) {
				List<String> remandReportList = (List<String>) session.getAttribute("remandReportList");
				jobAppEntity = jobAppService.JobAppNotificationList(remandReportList);
			} else if (listNum.equals("4")) {
				List<String> approvalReportList = (List<String>) session.getAttribute("approvalReportList");
				jobAppEntity = jobAppService.JobAppNotificationList(approvalReportList);
			} else if (listNum.equals("5")) {
				List<String> eventIncompList = (List<String>) session.getAttribute("eventIncompList");
				jobAppEntity = jobAppService.JobAppNotificationList(eventIncompList);
			} else if (listNum.equals("6")) {
				List<String> notSubmittedList = (List<String>) session.getAttribute("notSubmittedList");
				jobAppEntity = jobAppService.JobAppNotificationList(notSubmittedList);
			} else if (listNum.equals("7")) {
				List<String> eventList = (List<String>) session.getAttribute("eventList");
				jobAppEntity = jobAppService.JobAppNotificationList(eventList);
			} else if (listNum.equals("8")) {
				List<String> documentList = (List<String>) session.getAttribute("documentList");
				jobAppEntity = jobAppService.JobAppNotificationList(documentList);
			} else if (listNum.equals("9")) {
				List<String> documentLimitList = (List<String>) session.getAttribute("documentLimitList");
				jobAppEntity = jobAppService.JobAppNotificationList(documentLimitList);
			}
			// 通知リストを取得
			JobAppNotification(principal, model);

			model.addAttribute("searchForm", search);
			session.setAttribute("jobapp", jobAppEntity);
			model.addAttribute("jobAppEntity", jobAppEntity);
			return "jobapp/jobapplist_s";
		} else if (role.equals("TEACHER")) {
			if (listNum.equals("10")) {
				List<String> appList = (List<String>) session.getAttribute("appList");
				jobAppEntity = jobAppService.JobAppNotificationList(appList);
			} else if (listNum.equals("11")) {
				List<String> reportList = (List<String>) session.getAttribute("reportList");
				jobAppEntity = jobAppService.JobAppNotificationList(reportList);
			} else if (listNum.equals("12")) {
				List<String> eventList = (List<String>) session.getAttribute("eventList");
				jobAppEntity = jobAppService.JobAppNotificationList(eventList);
			} else if (listNum.equals("13")) {
				List<String> courseList = (List<String>) session.getAttribute("courseList");
				jobAppEntity = jobAppService.JobAppNotificationList(courseList);
			}
			// 通知リストを取得
			JobAppNotification(principal, model);
			model.addAttribute("searchForm", search);
			session.setAttribute("jobapp", jobAppEntity);
			model.addAttribute("jobAppEntity", jobAppEntity);
			return "jobapp/jobapplist_t";

		}
		return "index";
	}

	/**
	 * 受験申込書をPDFで出力する
	 *
	 * @param appId 申請ID
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return 受験申込書のPDFファイル
	 */
	@GetMapping("/jobapp/jobapppdf/{appId}")
	public ResponseEntity<byte[]> JobAppPdf(@PathVariable("appId") String appId, Principal principal, Model model) {

		// PDFファイルをサーバから読み込み
		byte[] bytes = null;
		try {
			jobAppService.jobAppPdf(appId);
			// サーバーに保存されているファイルを取得して、byte配列に変換する
			bytes = jobAppService.getPdfFile("target/summary.pdf");
			// ログ出力
			log.info("[" + principal.getName() + "]PDFファイル読み込み成功:" + "target/summary.pdf");
		} catch (IOException e) {
			// ログ出力
			log.warn("[" + principal.getName() + "]PDFファイル読み込み失敗:" + "target/summary.pdf");
			e.printStackTrace();
		} catch (JRException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		// PDFファイルのダウンロード用ヘッダー情報設定
		HttpHeaders header = new HttpHeaders();
		header.setContentDispositionFormData("filename", "summary.pdf");

		// PDFファイルを端末へ送信
		return new ResponseEntity<byte[]>(bytes, header, HttpStatus.OK);
	}
}
