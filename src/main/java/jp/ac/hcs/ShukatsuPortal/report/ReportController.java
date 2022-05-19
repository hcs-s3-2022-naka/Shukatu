package jp.ac.hcs.ShukatsuPortal.report;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.ac.hcs.ShukatsuPortal.WebConfig;
import jp.ac.hcs.ShukatsuPortal.user.UserService;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
/**
 * 受験報告に関する機能・画面を制御する.
 */
public class ReportController {
	@Autowired
	ReportService reportService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	HttpSession session;

	/**
	 * 受験報告の一覧を表示する
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return 受験報告一覧画面
	 */
	@GetMapping("/report/reportlist")
	public String getReportList(Principal principal,Model model) {
		ReportEntity reportEntity = reportService.getReportList();
		model.addAttribute("reportEntity", reportEntity);
		return "report/reportlist";
	}

	/**
	 * 選択された学生の受験報告詳細を表示
	 * @param reportId 受験報告ID
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return 受験報告詳細画面
	 */
	@GetMapping("/report/reportdetail/{reportId}")
	public String ReportDetail(@PathVariable("reportId") int reportId, Principal principal, Model model) {
		ReportData reportData = reportService.getReportListOne(reportId);
		model.addAttribute("reportData", reportData);
		return "report/reportdetail";
	}

	/**
	 * 受験報告登録画面を表示する
	 * @param form 登録時の入力チェック用ReportForm
	 * @param model 変数情報
	 * @return 受験報告登録画面
	 */
	@GetMapping("/report/reportinsert")
	public String getReportInsert(ReportForm form, Model model) {
		return "report/reportinsert";
	}

	/**
	 * 1件分の受験報告情報をデータベースに登録する
	 * @param form 登録する受験報告情報（パスワードは平文）
	 * @param bindingResult データバインド実施結果
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return 受験報告一覧画面
	 */
	@PostMapping("/report/reportinsert")
	public String getReportInsert(@ModelAttribute @Validated ReportForm form, BindingResult bindingResult, Principal principal, Model model) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("message","入力項目に誤りがあります。または必須項目が未入力です。");
			return getReportInsert(form, model);
		}
		boolean result = reportService.insert(form, principal.getName());
		if(result) {
			model.addAttribute("message", "登録に成功しました");
		}else {
			model.addAttribute("errorMessage", "登録に失敗しました");
		}
		return getReportList(principal, model);
	}

	/**
	 * 受験報告情報の内容変更をする
	 * @param update 更新情報のフォーム
	 * @param bindingResult データバインド実施結果
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return 受験報告一覧画面
	 */
	@PostMapping("/report/reportupdate")
	public String updateReport(@ModelAttribute @Validated ReportFormForUpdate update, BindingResult bindingResult, Principal principal,Model model) {
		if(bindingResult.hasErrors()) {
			return "report/reportupdate";
		}
		boolean result = reportService.updateReport(update, principal.getName());
		if(result) {
			model.addAttribute("message", "選択した受験報告の情報を更新しました");
		}else {
			model.addAttribute("errorMessage", "更新に失敗しました");
		}
		return getReportList(principal, model);
	}

	/**
	 * 一覧画面にて絞り込み検索を行う
	 * @param filtering 選択した条件
	 * @param conditions 絞り込む内容
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return 受験報告一覧画面
	 */
	@PostMapping("/report/search")
	public String serch(@ModelAttribute("filtering") String filtering ,@ModelAttribute("conditions") String conditions,Principal principal,Model model) {
		ReportEntity reportEntity;
		//入力制御
		if(filtering.equals("null") || conditions.equals("")) {
			reportEntity = reportService.getReportList();
		}else {
			reportEntity = reportService.getReportList(filtering, conditions);
			//入力された内容と一致するものがなかった場合の処理
			if(reportEntity.getReportList().size() == 0) {
				reportEntity = reportService.getReportList();
			}
		}
		model.addAttribute("reportEntity", reportEntity);
		return "report/reportlist";
	}

	/**
	 * 受験詳細の更新
	 * @param userId ユーザID
	 * @param model 変数情報
	 * @return 受験報告更新画面
	 */
	@GetMapping("/report/reportupdate/{userId}")
	public String update(@PathVariable("userId") String userId, Model model) {
		ReportFormForUpdate reportFormForUpdate = reportService.updateReportListOne(userId);
		model.addAttribute("reportFormForUpdate", reportFormForUpdate);
		return "report/reportupdate";
	}


	/**
	 * 受験報告状態の更新
	 * @param reportStatus 受験報告状態
	 * @param comment 差戻コメント
	 * @param reportId 受験報告ID
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return 受験報告一覧画面
	 */
	@PostMapping("/report/reportstatus")
	public String updateStatus(@RequestParam("reportStatus") String reportStatus, @RequestParam("teacherComment") String comment, @RequestParam("reportId") String reportId, Principal principal,Model model) {
		boolean result = reportService.updateStatus(reportId, comment, reportStatus);
		if(result) {
			model.addAttribute("message", "受験報告状態を更新しました");
		}
		return getReportList(principal,model);
	}

	/**
	 * 指定した年度の受験報告の一覧をCSVファイルとしてダウンロードさせる.
	 * @param year 指定年度
	 * @param principal ログイン情報
	 * @param model 変数情報
	 * @return 受験報告のCSVファイル
	 */
	@PostMapping("/report/csv")
	public ResponseEntity<byte[]> getReportCsv(@RequestParam("year") String year,Principal principal, Model model) {

		final String OUTPUT_FULLPATH = WebConfig.OUTPUT_PATH + WebConfig.FILENAME_REPORT_CSV;

		log.info("[" + principal.getName() + "]CSVファイル作成:" + OUTPUT_FULLPATH);

		// CSVファイルをサーバ上に作成
		reportService.reportListCsvOut(year);

		// CSVファイルをサーバから読み込み
		byte[] bytes = null;
		try {
			bytes = reportService.getFile(OUTPUT_FULLPATH);
			log.info("[" + principal.getName() + "]CSVファイル読み込み成功:" + OUTPUT_FULLPATH);
		} catch (IOException e) {
			log.warn("[" + principal.getName() + "]CSVファイル読み込み失敗:" + OUTPUT_FULLPATH);
			e.printStackTrace();
		}

		// CSVファイルのダウンロード用ヘッダー情報設定
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "text/csv; charset=UTF-8");
		header.setContentDispositionFormData("filename", WebConfig.FILENAME_REPORT_CSV);

		// CSVファイルを端末へ送信
		return new ResponseEntity<byte[]>(bytes, header, HttpStatus.OK);
	}
}