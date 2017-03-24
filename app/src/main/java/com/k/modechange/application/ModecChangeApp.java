package com.k.modechange.application;

import android.app.Application;
import elephant.jp.net.database.sqlite.SQLiteManagement;

/**
 * 共通Activityクラス
 * 
 * @author k.kitamura
 * @date 2014/05/05
 * 
 */
// public class BaseActivity extends Activity {
public class ModecChangeApp extends Application {

	/**
	 * アプリケーション開始
	 * */
	@Override
	public void onCreate() {

		// テーブルの新規作成
		SQLiteManagement SQLiteManagement = new SQLiteManagement(
				getApplicationContext());
	}

	/**
	 * アプリケーション終了
	 * */
	@Override
	public void onTerminate() {
	}
}
