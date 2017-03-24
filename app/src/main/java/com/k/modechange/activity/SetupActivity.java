package com.k.modechange.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.k.modechange.R;
import com.k.modechange.common.CommonUtil;
import com.k.modechange.common.Const;
import com.k.modechange.common.OutputLog;
import com.k.modechange.common.ServiceManagement;

/**
 * 登録のMainActivity
 * 
 * @author k.kitamura
 * @date 2014/05/05
 * 
 */
public class SetupActivity extends BaseActivity implements OnClickListener {

	// クラス変数定義
	/** ログ出力クラス */
	private OutputLog log = new OutputLog();
	/** 『デフォルトのマナーモード』 */
	private RadioGroup radioGroupSetUpDefault;
	/** 『通常マナー』 */
	private RadioButton radioButtonpSetUpVibrate;
	/** 『サイレントマナー』 */
	private RadioButton radioButtonpSetUpSilent;
	/** 『OFF』 */
	private RadioButton radioButtonpSetUpNoemal;

	/** 『Wi-Fi』ラジオグループ */
	private RadioGroup idRadioGroupWifiMode;
	/** Wi-Fiアイテム『ON』 */
	private RadioButton idRadioButtonWifiOn;
	/** Wi-Fiアイテム『OFF』 */
	private RadioButton idRadioButtonWifiOff;

	/** 『監視間隔』ラジオボタン */
	private RadioGroup radioGroupSetUpInterval;
	/** 『有効』 */
	private RadioButton radioButtonSetUpIntervalON;
	/** 『監視間隔』テキスト */
	private EditText editTexSetUpInterval;
	/** 『更新』 */
	private Button buttonSetUp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		log.logD("開始 : onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup);

		// 画面項目の設定
		setInit();

		// キーボードを非表示
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		log.logD("終了 : onCreate");
	}

	@Override
	public void onClick(View v) {

		// ダブルクリック抑止
		this.buttonSetUp.setEnabled(false);

		if (v == this.buttonSetUp) {

			// 『更新』ボタン押下時

			log.logD("『更新』押下");
			if (this.editTexSetUpInterval == null || this.editTexSetUpInterval.getText() == null
					|| "".equals(this.editTexSetUpInterval.getText().toString())) {
				log.logD("監視間隔が入力されていない");
				Toast.makeText(this, "監視間隔を入力してください。", Toast.LENGTH_LONG).show();

			} else if (Integer.parseInt(this.editTexSetUpInterval.getText().toString()) < 10) {
				log.logD("監視間隔が10分以上で入力されている");
				Toast.makeText(this, "監視間隔は10分以上で設定してください。", Toast.LENGTH_LONG).show();

			} else {
				log.logD("『更新』入力内容取得");

				// デフォルトのマナーモード取得
				String sMannersMode = "0";

				if (this.radioGroupSetUpDefault.getCheckedRadioButtonId() == this.radioButtonpSetUpVibrate.getId()) {
					// 『通常マナー』
					sMannersMode = Const.RingerModeCD.RINGER_MODE_VIBRATE;
				} else if (this.radioGroupSetUpDefault.getCheckedRadioButtonId() == this.radioButtonpSetUpSilent
						.getId()) {
					// 『サイレントマナー』
					sMannersMode = Const.RingerModeCD.RINGER_MODE_SILENT;
				} else if (this.radioGroupSetUpDefault.getCheckedRadioButtonId() == this.radioButtonpSetUpNoemal
						.getId()) {
					// 『OFF』
					sMannersMode = Const.RingerModeCD.RINGER_MODE_NORMAL;

				} else {
					// 『何もしない』
					sMannersMode = Const.RingerModeCD.RINGER_MODE_NONE;
				}

				// デフォルトのWiFiモード取得
				String sWiFiMode = "0";

				if (this.idRadioGroupWifiMode.getCheckedRadioButtonId() == this.idRadioButtonWifiOn.getId()) {
					// 『通常マナー』
					sWiFiMode = Const.WiFiFlg.ON;
				} else if (this.idRadioGroupWifiMode.getCheckedRadioButtonId() == this.idRadioButtonWifiOff.getId()) {
					// 『サイレントマナー』
					sWiFiMode = Const.WiFiFlg.OFF;
				} else {
					// 『何もしない』
					sWiFiMode = Const.WiFiFlg.RINGER_MODE_NONE;
				}

				// アプリの機能有効/無効の取得
				String sSurveillanceFlg = "0";
				if (this.radioGroupSetUpInterval.getCheckedRadioButtonId() == this.radioButtonSetUpIntervalON.getId()) {
					// 『有効』
					sSurveillanceFlg = Const.SurveillanceFlg.ON;
					this.editTexSetUpInterval.getText();

				} else {
					sSurveillanceFlg = Const.SurveillanceFlg.OFF;
				}

				log.logD("『更新』 「デフォルトのモード」の更新");

				// 「デフォルトのモード」の更新
				boolean result = CommonUtil.setApData(this, sMannersMode, Const.ApdataID.ID_100401);
				if (result) {
					result = CommonUtil.setApData(this, sWiFiMode, Const.ApdataID.ID_100402);
				}
				if (result) {
					// 「デフォルトのモード」の更新が成功した場合
					log.logD("『更新』 「アプリケーション機能有効/無効」の更新");
					// 「アプリケーション機能有効/無効」の更新
					result = CommonUtil.setApData(this, sSurveillanceFlg, Const.ApdataID.ID_100501);
				}

				if (result
						&& this.radioGroupSetUpInterval.getCheckedRadioButtonId() == this.radioButtonSetUpIntervalON
								.getId()) {
					// 「アプリケーション機能有効/無効」の更新が成功した場合かつ、監視をONにした場合
					log.logD("『更新』「監視間隔（分）」の更新");
					// 「監視間隔（分）」の更新
					result = CommonUtil.setApData(this, this.editTexSetUpInterval.getText().toString(),
							Const.ApdataID.ID_100601);
				}

				if (result) {
					// 全ての更新に成功した場合
					log.logD("全ての更新に成功");
					Toast.makeText(this, "更新しました", Toast.LENGTH_LONG).show();

				} else {
					log.logD("何れかで失敗");
					// 何れかで失敗している場合
					Toast.makeText(this, "更新に失敗しました", Toast.LENGTH_LONG).show();
				}

				if (Const.SurveillanceFlg.ON.equals(sSurveillanceFlg)) {
					// 監視をONにした場合
					// サービス開始
					ServiceManagement.startService(this);
				} else {
					// 監視をOFFにした場合
					// 何もしない
					ServiceManagement.endService(this);
				}

				// 説明画面への誘導を二度と表示しない
				CommonUtil.setApData(this, Const.InfoFlg.ON, Const.ApdataID.ID_100101);

				// 画面遷移
				Intent intent = new Intent(thisActivity, RegInfoListActivity.class);
				// 次の画面の戻るボタンでActivityを全て破棄して終了する。
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		}
		// ダブルクリック抑止解除
		this.buttonSetUp.setEnabled(true);
	}

	/**
	 * 画面項目の設定
	 */
	private void setInit() {
		log.logD("setInit");
		// 画面名の設定
		setScreen(R.string.text_textView_screenName_setup);

		this.radioGroupSetUpDefault = (RadioGroup) findViewById(R.id.id_radioGroup_setUpDefault);
		this.radioButtonpSetUpVibrate = (RadioButton) findViewById(R.id.id_radioButtonp_setUpVibrate);
		this.radioButtonpSetUpSilent = (RadioButton) findViewById(R.id.id_radioButtonp_setUpSilent);
		this.radioButtonpSetUpNoemal = (RadioButton) findViewById(R.id.id_radioButtonp_setUpNoemal);
		
		this.idRadioGroupWifiMode = (RadioGroup) findViewById(R.id.id_radioGroup_setUpDefaultWiFi);
		this.idRadioButtonWifiOn = (RadioButton) findViewById(R.id.id_radioButtonp_setUpWiFiOn);
		this.idRadioButtonWifiOff = (RadioButton) findViewById(R.id.id_radioButtonp_setUpWiFiOff);

		this.radioGroupSetUpInterval = (RadioGroup) findViewById(R.id.id_radioGroup_setUpInterval);
		this.radioButtonSetUpIntervalON = (RadioButton) findViewById(R.id.id_radioButton_setUpIntervalON);

		this.editTexSetUpInterval = (EditText) findViewById(R.id.id_editText_setUpInterval);

		this.buttonSetUp = (Button) findViewById(R.id.id_button_setUp);
		this.buttonSetUp.setOnClickListener(this);

		String defaultMode = CommonUtil.getApData(this, Const.ApdataID.ID_100401);
		String defaultWifi = CommonUtil.getApData(this, Const.ApdataID.ID_100402);
		String interval = CommonUtil.getApData(this, Const.ApdataID.ID_100501);

		// 広告表示
		AdView adView = (AdView) this.findViewById(R.id.adView);
		adView.loadAd(new AdRequest.Builder().build());

		log.logD("初期設定 マナーモード");
		// 『デフォルトのマナーモード』ラジオボタンを設定する
		if (Const.RingerModeCD.RINGER_MODE_VIBRATE.equals(defaultMode)) {
			// 通常マナー
			this.radioGroupSetUpDefault.check(R.id.id_radioButtonp_setUpVibrate);

		} else if (Const.RingerModeCD.RINGER_MODE_SILENT.equals(defaultMode)) {
			// サイレントマナー
			this.radioGroupSetUpDefault.check(R.id.id_radioButtonp_setUpSilent);

		} else if (Const.RingerModeCD.RINGER_MODE_NORMAL.equals(defaultMode)) {
			// OFF
			this.radioGroupSetUpDefault.check(R.id.id_radioButtonp_setUpNoemal);

		} else if (Const.RingerModeCD.RINGER_MODE_NONE.equals(defaultMode)) {
			// 何もしない
			this.radioGroupSetUpDefault.check(R.id.id_radioButtonp_setUpNone);
		}
		
		log.logD("初期設定 wifi");
		// 『デフォルトのwifiド』ラジオボタンを設定する
		if (Const.WiFiFlg.ON.equals(defaultWifi)) {
			// 通常マナー
			this.idRadioGroupWifiMode.check(R.id.id_radioButtonp_setUpWiFiOn);

		} else if (Const.WiFiFlg.OFF.equals(defaultWifi)) {
			// サイレントマナー
			this.idRadioGroupWifiMode.check(R.id.id_radioButtonp_setUpWiFiOff);

		} else if (Const.WiFiFlg.RINGER_MODE_NONE.equals(defaultWifi)) {
			// 何もしない
			this.idRadioGroupWifiMode.check(R.id.id_radioButtonp_setUpWiFiNone);
		}

		this.editTexSetUpInterval.setText(CommonUtil.getApData(this, Const.ApdataID.ID_100601));

		log.logD("初期設定 監視間隔");
		// 『監視間隔』ラジオボタンを設定する
		if (Const.SurveillanceFlg.ON.equals(interval)) {
			// 監視ON（アプリ有効）
			this.radioGroupSetUpInterval.check(R.id.id_radioButton_setUpIntervalON);

		} else {
			// 監視ON（アプリ無効）
			this.radioGroupSetUpInterval.check(R.id.id_radioButton_setUpIntervalOFF);
		}
	}
}
