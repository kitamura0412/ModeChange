package com.k.modechange.common;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * SPモードメールを送信するクラス
 * 
 * @author USER
 *
 */
public class SendMail {

    /**
     * @param activity 呼び出し元のActivity
     * @param sFilePath ファイルパス
     * @param mailto 送信先
     * @param subject タイトル
     * @param text 本文
     */
    public void send(Activity activity, String sFilePath, String mailto, String subject, String text) {

	Intent intent = new Intent();
	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	intent.setAction(Intent.ACTION_SENDTO);
	intent.setData(Uri.parse("mailto:" + mailto));
	intent.putExtra(Intent.EXTRA_SUBJECT, subject);
	intent.putExtra(Intent.EXTRA_TEXT, text);
	activity.startActivity(intent);
    }

}
