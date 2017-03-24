package com.k.modechange.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.k.modechange.R;
import com.k.modechange.dto.DeliveryDto;

import elephant.jp.net.activity.ElephantActivity;
import elephant.jp.net.common.util.log.OutputLog;

/**
 * 共通Activityクラス
 * 
 * @author k.kitamura
 * @date 2014/05/05
 * 
 */
public class BaseActivity extends ElephantActivity {
	/** ログ出力クラス */
	private static OutputLog log = new OutputLog("BaseActivity");
	/** 呼び出し元のActivity */
	Activity thisActivity;
	/** 画面名表示テキスト */
	TextView screenName;
	/** 画面間で受け渡すintent */
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// タイトルバー非表示
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		thisActivity = this;

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * DeliveryDtoを前の画面から引き継ぐ
	 * 
	 * @return 前の画面から引き継いだDeliveryDto
	 * */
	public DeliveryDto getDeliveryDto() {
		// 画面項目を保持するクラスの初期化
		DeliveryDto deliveryDto = (DeliveryDto) getIntent()
				.getSerializableExtra(DeliveryDto.NAME_DELIVERYDTO);
		if (deliveryDto == null) {
			deliveryDto = new DeliveryDto();
		}
		return deliveryDto;
	}

	/**
	 * 画面設定
	 * 
	 * @param screenNameId
	 *            レイアウトID
	 * */
	public void setScreen(int screenNameId) {
		// TODO 画面名
		this.screenName = (TextView) findViewById(R.id.id_textView_screenName);

		this.screenName.setText((String) getText(screenNameId));
	}

	/**
	 * @param contentResolver
	 *            ContentResolver
	 * @return 結果
	 */
	public boolean checkGps(ContentResolver contentResolver) {
		// 現在有効な情報プロバイダ名を取得する
		String providers = android.provider.Settings.Secure.getString(
				contentResolver,
				android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

		if (providers.indexOf("gps", 0) == -1) {
			// プロバイダ名に"gps"が含まれていない場合
			return false;

		}
		// GPSが有効の場合
		return true;
	}

}
