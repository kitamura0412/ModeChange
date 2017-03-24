package com.k.modechange.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;

/**
 * assetsフォルダを管理するクラス
 * 
 * @author k.kitamura
 * @date 2011.05.11 新規作成
 * 
 */
public class AssetsManagement {

	/** ファイルストリーム */
	private InputStream stream;
	/** プロパティーファイル */
	private Properties properties;

	/**
	 * コンストラクタ
	 * 
	 * 対象のファイルを読み込む
	 * 
	 * @param context Context
	 * @param fName 管理するファイル
	 *            ファイルストリームの取得に失敗／プロパティーファイルのロードに失敗
	 */
	public AssetsManagement(Context context, String fName) {

		// プロパティーの生成
		this.properties = new Properties();

		// 対象のファイルをオープンして、ファイルストリームを生成する
		try {
			this.stream = context.getAssets().open(fName);

		} catch (IOException e) {
			// 対象ファイルなし
			e.printStackTrace();
		}

		// プロパティーファイルをロードする
		try {
			this.properties.load(this.stream);
		} catch (IOException e) {
			// プロパティーファイルロード失敗
			e.printStackTrace();
		}

	}

	/**
	 * プロパティーファイルからkeyに紐付くvakueを取得する
	 * 
	 * @param key プロパティーファイルから取得するvakueのkey
	 * @return
	 *         プロパティーファイルから取得したvalue。keyが存在しない場合はnullが返る
	 * */
	public String getProperty(String key) {

		// プロパティーファイルからkeyに紐付くvakueを取得する
		return this.properties.getProperty(key);
	}

	/**
	 * クローズ処理
	 * */
	public void closeAssets() {

		// ファイルストリームを解放する
		try {
			this.stream.close();
		} catch (IOException e) {
			// DBクローズ失敗
			e.printStackTrace();
		}
	}

}
