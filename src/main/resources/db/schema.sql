/* ユーザマスタ */
CREATE TABLE IF NOT EXISTS users(

	/*ユーザID*/
	user_id VARCHAR(254) PRIMARY KEY,

	/*暗号化されたパスワード*/
	encrypted_password VARCHAR(100) NOT NULL,

	/*氏名*/
	user_name VARCHAR(16) NOT NULL,

	/*ユーザ権限*/
	user_authority VARCHAR(7) NOT NULL,

	/*所属クラス*/
	belong_class CHAR(4) DEFAULT NULL,

	/*出席番号*/
	student_number INT DEFAULT NULL,

	/*ダークモード*/
	dark_mode BOOLEAN DEFAULT FALSE NOT NULL,

	/*ユーザ状態*/
	user_status VARCHAR(7) DEFAULT 'VALID' NOT NULL,

	/*登録日時*/
	registered_date TIMESTAMP(3) NOT NULL,

	/*登録者のユーザID*/
	registered_user_id VARCHAR(254),

	/*更新日時*/
	update_date TIMESTAMP(3) DEFAULT NULL,

	/*更新者のユーザID*/
	update_user_id VARCHAR(254) DEFAULT NULL,

	/*エラー回数*/
	password_error_count int DEFAULT 0
);

/*就職活動申請マスタ*/
CREATE TABLE IF NOT EXISTS app(

	/*申請ID*/
	app_id INT PRIMARY KEY auto_increment,

	/*申請状態*/
	app_status INT(2) NOT NULL DEFAULT 01,

	/*とりまとめフラグ*/
	summary_flag BOOLEAN NOT NULL DEFAULT FALSE,

	/*書類提出フラグ*/
	document_flag BOOLEAN NOT NULL DEFAULT FALSE,

	/*コース担当報告フラグ*/
	approval_flag BOOLEAN NOT NULL DEFAULT FALSE,

	/*活動開始日付*/
	app_start_date DATE NOT NULL,

	/*活動開始時刻*/
	app_start_time TIME NOT NULL,

	/*活動終了日付*/
	app_finish_date DATE DEFAULT NULL,

	/*活動終了時刻*/
	app_finish_time TIME DEFAULT NULL,

	/*場所*/
	app_address VARCHAR(100) NOT NULL,

	/*内容区分*/
	app_contents INT(2) NOT NULL,

	/*内容その他*/
	app_content_other VARCHAR(50) DEFAULT NULL,

	/*欠席フラグ*/
	absence_flag BOOLEAN NOT NULL DEFAULT FALSE,

	/*欠席開始日付*/
	app_start_absence DATE DEFAULT NULL,

	/*欠席終了日付*/
	app_finish_absence DATE DEFAULT NULL,

	/*早退フラグ*/
	leave_flag BOOLEAN NOT NULL DEFAULT FALSE,

	/*早退日付*/
	app_leave_date DATE DEFAULT NULL,

	/*早退時刻*/
	app_leave_time TIME DEFAULT NULL,

	/*遅刻フラグ*/
	late_flag BOOLEAN NOT NULL DEFAULT FALSE,

	/*遅刻日付*/
	app_late_date DATE DEFAULT NULL,

	/*遅刻時刻*/
	app_late_time TIME DEFAULT NULL,

	/*メモ*/
	app_memo VARCHAR(65534) DEFAULT NULL,

	/*申請者クラス*/
	apply_class VARCHAR(4) NOT NULL,

	/*申請者番号*/
	apply_number INT(2) NOT NULL,

	/*申請者氏名*/
	apply_name VARCHAR(16) NOT NULL,

	/*申請日時*/
	app_date TIMESTAMP(3) NOT NULL,

	/*更新日時*/
	update_date TIMESTAMP(3) DEFAULT NULL,

	/*報告コメント*/
	rep_comment VARCHAR(65534) DEFAULT NULL,

	/*報告日時*/
	rep_date TIMESTAMP(3) DEFAULT NULL,

	/*指摘コメント*/
	indicate VARCHAR(65534) DEFAULT NULL
);

/*申請企業トランザクション*/
CREATE TABLE IF NOT EXISTS company(

	/*申請ID*/
	app_id INT ,

	/*会社名*/
	app_company VARCHAR(254),

	/*進むかフラグ*/
	app_proceed BOOLEAN DEFAULT FALSE NOT NULL,

	FOREIGN KEY(app_id) REFERENCES app(app_id),

	PRIMARY KEY(app_id,app_company)
);

/*とりまとめマスタ*/
CREATE TABLE IF NOT EXISTS summary(

	/*とりまとめID*/
	summary_id int PRIMARY KEY auto_increment,

	/*申請ID*/
	app_id int ,

	/*名簿登録フラグ*/
	register_flag BOOLEAN NOT NULL DEFAULT FALSE,

	/*担任コメント*/
	teacher_comment VARCHAR(65534) DEFAULT NULL,

	/*学生コメント*/
	student_comment VARCHAR(65534) DEFAULT NULL,

	FOREIGN KEY(app_id) REFERENCES app(app_id)
);

/*書類*/
CREATE TABLE IF NOT EXISTS document(

	/*書類ID*/
	document_id int PRIMARY KEY auto_increment,

	/*書類名*/
	document_name VARCHAR(50) NOT NULL
);

/*とりまとめ書類*/
CREATE TABLE IF NOT EXISTS certificate(

	/*とりまとめID*/
	summary_id int ,

	/*書類ID*/
	document_id int,

	/*期限*/
	deadline DATE NOT NULL,

	/*提出済みフラグ*/
	status BOOLEAN NOT NULL DEFAULT FALSE,

	FOREIGN KEY(summary_id) REFERENCES summary(summary_id),

	FOREIGN KEY(document_id) REFERENCES document(document_id),

	PRIMARY KEY(summary_id,document_id)
);

/*通知*/
CREATE TABLE IF NOT EXISTS notificate(

	/*通知ID*/
	notificate_id int(2) PRIMARY KEY auto_increment,

	/*通知名*/
	notificate_name VARCHAR(50) NOT NULL
);

/*ユーザ通知*/
CREATE TABLE IF NOT EXISTS listpush(

	/*通知ID*/
	notificate_id int(2),

	/*ユーザID*/
	user_id VARCHAR(254),

	/*通知押下日時*/
	notificate_date TIMESTAMP(3) DEFAULT NULL,

	FOREIGN KEY(notificate_id) REFERENCES notificate(notificate_id),
	FOREIGN KEY(user_id) REFERENCES users(user_id),

	PRIMARY KEY(notificate_id,user_id)
);


/* 受験報告マスタ */
CREATE TABLE IF NOT EXISTS report(

	/*受験報告ID*/
	report_id INT PRIMARY KEY auto_increment,

	/*学科コード*/
	department VARCHAR(1) NOT NULL,

	/*求人番号*/
	job_number INT DEFAULT 0,

	/*企業名カナ*/
	enterprise_name_kana VARCHAR(50) NOT NULL,

	/*企業名*/
	enterprise_name VARCHAR(50) NOT NULL,

	/*試験申込区分*/
	test_category VARCHAR(100) NOT NULL,

	/*試験申込区分その他*/
	test_category_other VARCHAR(100) DEFAULT NULL,

	/*受験日時*/
	examination_date TIMESTAMP NOT NULL,

	/*受験場所名*/
	examination_location VARCHAR(100) NOT NULL,

	/*試験会場区分*/
	location_category VARCHAR(100) NOT NULL,

	/*試験会場区分その他*/
	location_category_other VARCHAR(100) DEFAULT NULL,

	/*試験段階区分*/
	stage_category VARCHAR(100) NOT NULL,

	/*試験段階区分その他*/
	stage_category_other VARCHAR(100) DEFAULT NULL,

	/*試験内容区分*/
	content_category VARCHAR(100) NOT NULL,

	/*試験内容区分その他*/
	content_category_other VARCHAR(100) DEFAULT NULL,

	/*結果通知日時*/
	result_date INT NOT NULL,

	/*合格のみフラグ*/
	pass_flag BOOLEAN DEFAULT false ,

	/*結果通知区分*/
	result_category VARCHAR(100) NOT NULL,

	/*適正試験区分*/
	appropriate_category VARCHAR(100) DEFAULT NULL,

	/*適正試験区分その他*/
	appropriate_category_other VARCHAR(100) DEFAULT NULL,

	/*筆記作文区分*/
	writing_category VARCHAR(100) DEFAULT NULL,

	/*筆記作文区分その他*/
	writing_category_other VARCHAR(100) DEFAULT NULL,

	/*面接概要区分*/
	interview_category VARCHAR(100) DEFAULT NULL,

	/*面接概要区分その他*/
	interview_category_other VARCHAR(100) DEFAULT NULL,

	/*集団面接人数*/
	group_number INT DEFAULT 0,

	/*面接官数*/
	officer_number INT DEFAULT 0,

	/*面接官役職*/
	officer_role VARCHAR(100) DEFAULT NULL,

	/*面接時間*/
	interview_time INT DEFAULT 0,

	/*GDテーマ*/
	gd_thema VARCHAR(100) DEFAULT NULL,

	/*出題内容*/
	content VARCHAR(254) NOT NULL,

	/*備考*/
	remarks VARCHAR(100) DEFAULT NULL,

	/*受験報告状態*/
	report_status VARCHAR(50) NOT NULL,

	/*登録日時*/
	registration_date TIMESTAMP(3) NOT NULL,

	/*登録者のユーザID*/
	registered_user_id VARCHAR(254) NOT NULL,

	/*更新日時*/
	update_date TIMESTAMP(3) DEFAULT NULL,

	/*更新者のユーザID*/
	update_user_id VARCHAR(254) DEFAULT NULL,

	/**担任指摘コメント*/
	teacher_comment VARCHAR(254) DEFAULT NULL
);
