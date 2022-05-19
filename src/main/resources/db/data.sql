/*ユーザサンプルデータ*/
INSERT INTO "PUBLIC"."USERS" VALUES
('t-ukeire@hcs.ac.jp', '$2a$10$xRTXvpMWly0oGiu65WZlm.3YL95LGVV2ASFjDhe6WF4.Qji1huIPa', STRINGDECODE('\u53d7\u5165\u5148\u751f'), 'TEACHER', 'S3A1', 0, FALSE, 'VALID', TIMESTAMP '2020-05-21 12:12:12.123', NULL, NULL, NULL, 0),
('s-ukeire@hcs.ac.jp', '$2a$10$ZT6IPN5yCSh1fTs6MjoGBuYDVQKwZKRvSq5xK4rVCKxcn64Xs0SG.', STRINGDECODE('\u53d7\u5165\u592a\u90ce'), 'STUDENT', 'S3A1', 1, FALSE, 'VALID', TIMESTAMP '2021-11-30 11:13:34.589', 't-ukeire@hcs.ac.jp', NULL, NULL, 0);

/*書類データ*/
INSERT INTO document(document_name)
VALUES('履歴書');
INSERT INTO document(document_name)
VALUES('推薦書');
INSERT INTO document(document_name)
VALUES('大学(卒業 卒業見込)証明書');
INSERT INTO document(document_name)
VALUES('HCS成績証明書');
INSERT INTO document(document_name)
VALUES('HCS卒業見込証明書');
INSERT INTO document(document_name)
VALUES('健康診断証明書');
INSERT INTO document(document_name)
VALUES('出身高校成績証明書');
INSERT INTO document(document_name)
VALUES('大学成績証明書');
INSERT INTO document(document_name)
VALUES('個人情報等同意書');

/*通知サンプルデータ*/
INSERT INTO notificate(notificate_name)
VALUES('申請差戻通知');
INSERT INTO notificate(notificate_name)
VALUES('申請承認通知');
INSERT INTO notificate(notificate_name)
VALUES('報告差戻通知');
INSERT INTO notificate(notificate_name)
VALUES('報告承認通知');
INSERT INTO notificate(notificate_name)
VALUES('申請未完了通知');
INSERT INTO notificate(notificate_name)
VALUES('報告未提出通知');
INSERT INTO notificate(notificate_name)
VALUES('イベント通知');
INSERT INTO notificate(notificate_name)
VALUES('とりまとめ書類提出有通知');
INSERT INTO notificate(notificate_name)
VALUES('書類締切直前未提出通知');
INSERT INTO notificate(notificate_name)
VALUES('申請承認待通知');
INSERT INTO notificate(notificate_name)
VALUES('報告承認待通知');
INSERT INTO notificate(notificate_name)
VALUES('開催直前イベント通知');
INSERT INTO notificate(notificate_name)
VALUES('コース担当連絡完了通知');

/*ユーザ通知サンプルデータ*/
INSERT INTO listpush(notificate_id,user_id,notificate_date)
VALUES('1','s-ukeire@hcs.ac.jp','2019-05-21 12:12:12.123');
INSERT INTO listpush(notificate_id,user_id,notificate_date)
VALUES('2','s-ukeire@hcs.ac.jp','2019-05-21 12:12:12.123');
INSERT INTO listpush(notificate_id,user_id,notificate_date)
VALUES('3','s-ukeire@hcs.ac.jp','2019-05-21 12:12:12.123');
INSERT INTO listpush(notificate_id,user_id,notificate_date)
VALUES('4','s-ukeire@hcs.ac.jp','2019-05-21 12:12:12.123');
INSERT INTO listpush(notificate_id,user_id,notificate_date)
VALUES('5','s-ukeire@hcs.ac.jp','2019-05-21 12:12:12.123');
INSERT INTO listpush(notificate_id,user_id,notificate_date)
VALUES('6','s-ukeire@hcs.ac.jp','2019-05-21 12:12:12.123');
INSERT INTO listpush(notificate_id,user_id,notificate_date)
VALUES('7','s-ukeire@hcs.ac.jp','2019-05-21 12:12:12.123');
INSERT INTO listpush(notificate_id,user_id,notificate_date)
VALUES('8','s-ukeire@hcs.ac.jp','2019-05-21 12:12:12.123');
INSERT INTO listpush(notificate_id,user_id,notificate_date)
VALUES('9','s-ukeire@hcs.ac.jp','2019-05-21 12:12:12.123');
INSERT INTO listpush(notificate_id,user_id,notificate_date)
VALUES('10','t-ukeire@hcs.ac.jp','2019-05-21 12:12:12.123');
INSERT INTO listpush(notificate_id,user_id,notificate_date)
VALUES('11','t-ukeire@hcs.ac.jp','2019-05-21 12:12:12.123');
INSERT INTO listpush(notificate_id,user_id,notificate_date)
VALUES('12','t-ukeire@hcs.ac.jp','2019-05-21 12:12:12.123');
INSERT INTO listpush(notificate_id,user_id,notificate_date)
VALUES('13','t-ukeire@hcs.ac.jp','2019-05-21 12:12:12.123');
