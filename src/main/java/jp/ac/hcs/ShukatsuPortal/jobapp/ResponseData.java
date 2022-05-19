package jp.ac.hcs.ShukatsuPortal.jobapp;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 誤字1文字に関する情報を管理する
 */

@Data
public class ResponseData {

	/*
	 * 指摘箇所
	 */
	private int pos;

	/*
	 * 指摘文字
	 */
	private String word;

	/*
	 * 指摘した単語の疑わしさを示す指標
	 */
	private String score;

	/*
	 * 修正文字候補
	 */
	private List<String> suggestions = new ArrayList<String>();
}
