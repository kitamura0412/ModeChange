package com.k.modechange.activity;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.k.modechange.R;
import com.k.modechange.common.CommonUtil;
import com.k.modechange.common.Const;
import com.k.modechange.common.OutputLog;
import com.k.modechange.dto.DeliveryDto;
import com.k.modechange.util.spinner.KeyValuePair;
import com.k.modechange.util.spinner.SpinnerUtil;

import elephant.jp.net.database.sqlite.IssueSQL;

/**
 * 登録のMainActivity
 *
 * @author k.kitamura
 * @date 2014/05/05
 */
public class RegActivity extends BaseActivity implements OnClickListener, AdapterView.OnItemSelectedListener {

    // クラス変数定義
    /**
     * ログ出力クラス
     */
    private OutputLog log = new OutputLog();

    /**
     * 画面間で引き継ぐデータを保持したクラス
     */
    private DeliveryDto deliveryDto;
    /**
     * 『NO』
     */
    private TextView textviewRegNo;
    /**
     * 『登録名』
     */
    private TextView editTextRegName;
    /**
     * 『住所』
     */
    private TextView editTextRegAddress;
    /**
     * 『緯度』
     */
    private TextView editTextRegLatitude;
    /**
     * 『経度』
     */
    private TextView editTextRegLongitude;
    /**
     * 『マナーモード』
     */
    private RadioGroup radioGroupMannersMode;
    /**
     * ラジオボタンアイテム『通常マナー』
     */
    private RadioButton radioButtonMannersModeManners;
    /**
     * ラジオボタンアイテム『サイレントマナー』
     */
    private RadioButton radioButtonMannersModeSilence;

    /**
     * 『Wi-Fi』ラジオグループ
     */
    private RadioGroup idRadioGroupWifiMode;
    /**
     * Wi-Fiアイテム『ON』
     */
    private RadioButton idRadioButtonWifiOn;
    /**
     * Wi-Fiアイテム『OFF』
     */
    private RadioButton idRadioButtonWifiOff;

    /**
     * 『登録』
     */
    private Button buttonReg;
    /**
     * 『削除』
     */
    private Button buttonDel;
    /**
     * 『位置情報編集』
     */
    private Button buttonUpd;

    /**
     * 時間１From
     */
    private Spinner idSpinnerTime1From;
    /**
     * 時間１To
     */
    private Spinner idSpinnerTime1To;
    /**
     * 時間２From
     */
    private Spinner idSpinnerTime2From;
    /**
     * 時間２To
     */
    private Spinner idSpinnerTime2To;


    private ArrayAdapter<KeyValuePair> timeSpinnerFrom;
    private ArrayAdapter<KeyValuePair> timeSpinnerTo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        log.logD("開始 : onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg);

        // 画面項目の設定
        setInit();

        // キーボードを非表示
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        log.logD("終了 : onCreate");
    }

    /**
     * 画面項目の設定
     */
    private void setInit() {
        log.logD("setInit : 開始");
        // 画面名の設定
        this.deliveryDto = getDeliveryDto();
        setScreen(R.string.text_textView_screenName_reg);
        // 画面項目を初期化
        this.editTextRegName = (EditText) findViewById(R.id.id_editText_regName);
        this.editTextRegAddress = (TextView) findViewById(R.id.id_editText_regAddress);
        this.editTextRegLatitude = (TextView) findViewById(R.id.id_editText_regLatitude);
        this.editTextRegLongitude = (TextView) findViewById(R.id.id_editText_regLongitude);
        this.radioGroupMannersMode = (RadioGroup) findViewById(R.id.id_radioGroup_mannersMode);
        this.radioButtonMannersModeManners = (RadioButton) findViewById(R.id.id_radioButton_mannersModeManners);
        this.radioButtonMannersModeSilence = (RadioButton) findViewById(R.id.id_radioButton_mannersModeSilence);

        this.idRadioGroupWifiMode = (RadioGroup) findViewById(R.id.id_radioGroup_wifiMode);
        this.idRadioButtonWifiOn = (RadioButton) findViewById(R.id.id_radioButton_wifiOn);
        this.idRadioButtonWifiOff = (RadioButton) findViewById(R.id.id_radioButton_wifiOff);

        this.buttonReg = (Button) findViewById(R.id.id_button_reg);
        this.buttonReg.setOnClickListener(this);
        this.buttonDel = (Button) findViewById(R.id.id_button_del);
        this.buttonDel.setOnClickListener(this);
        this.buttonUpd = (Button) findViewById(R.id.id_button_upd);
        this.buttonUpd.setOnClickListener(this);


        this.idSpinnerTime1From = (Spinner) findViewById(R.id.id_spinnerTime1_from);
        this.idSpinnerTime1To = (Spinner) findViewById(R.id.id_spinnerTime1_to);
        this.idSpinnerTime2From = (Spinner) findViewById(R.id.id_spinnerTime2_from);
        this.idSpinnerTime2To = (Spinner) findViewById(R.id.id_spinnerTime2_to);

        // スピナーデータの設定
        this.timeSpinnerFrom = SpinnerUtil.getSpinnerData(this, R.array.timeSpinnerFrom);
        this.timeSpinnerTo = SpinnerUtil.getSpinnerData(this, R.array.timeSpinnerTo);
        this.idSpinnerTime1From.setAdapter(timeSpinnerFrom);
        this.idSpinnerTime1From.setOnItemSelectedListener(this);
        this.idSpinnerTime1To.setAdapter(timeSpinnerTo);
        this.idSpinnerTime1To.setOnItemSelectedListener(this);
        this.idSpinnerTime2From.setAdapter(timeSpinnerFrom);
        this.idSpinnerTime2From.setOnItemSelectedListener(this);
        this.idSpinnerTime2To.setAdapter(timeSpinnerTo);
        this.idSpinnerTime2To.setOnItemSelectedListener(this);
        if (this.deliveryDto != null) {
            // 画面項目を引き継ぐ

            this.editTextRegAddress.setText(this.deliveryDto.address);
            this.editTextRegLatitude.setText(Double.toString(this.deliveryDto.latitude));
            this.editTextRegLongitude.setText(Double.toString(this.deliveryDto.longitude));

            if (this.deliveryDto.no == null) {
                // 登録の場合
                log.logD("setInit : 登録の場合");
                this.buttonReg.setText((String) getText(R.string.text_textview_reg));
                this.editTextRegName.setText(CommonUtil.getRegName(new IssueSQL(this)));

                // 更新ボタンを非表示
                this.buttonDel.setVisibility(View.GONE);
            } else {
                super.screenName.setText(R.string.text_textView_screenName_edit);
                // 更新の場合
                log.logD("setInit : 更新の場合");
                this.buttonReg.setText((String) getText(R.string.text_textview_upd));
                this.textviewRegNo = (TextView) findViewById(R.id.id_textview_regNo);
                this.textviewRegNo.setText(this.deliveryDto.no);
                this.editTextRegName.setText(this.deliveryDto.regName);

                this.editTextRegAddress.setText(this.deliveryDto.address);
                this.editTextRegLatitude.setText(Double.toString(this.deliveryDto.latitude));
                this.editTextRegLongitude.setText(Double.toString(this.deliveryDto.longitude));

                if (AudioManager.RINGER_MODE_VIBRATE == Integer.parseInt(this.deliveryDto.status)) {
                    // 通常マナー
                    log.logD("setInit : status : 通常マナー");
                    this.radioGroupMannersMode.check(R.id.id_radioButton_mannersModeManners);
                } else if (AudioManager.RINGER_MODE_SILENT == Integer.parseInt(this.deliveryDto.status)) {
                    // サイレントマナー
                    log.logD("setInit : status : サイレントマナー");
                    this.radioGroupMannersMode.check(R.id.id_radioButton_mannersModeSilence);
                } else {
                    // OFF
                    log.logD("setInit : status : OFF");
                    this.radioGroupMannersMode.check(R.id.id_radioButton_mannersModeOff);
                }


                if (Const.WiFiFlg.ON.equals(this.deliveryDto.wifi)) {
                    // ON
                    log.logD("setInit : wifi : ON");
                    this.idRadioGroupWifiMode.check(R.id.id_radioButton_wifiOn);
                } else {
                    // OFF
                    log.logD("setInit : wifi : OFF");
                    this.idRadioGroupWifiMode.check(R.id.id_radioButton_wifiOff);
                }

                // 時間の設定
                this.textviewRegNo.setText(this.deliveryDto.no);

                this.idSpinnerTime1From.setSelection(SpinnerUtil.keyToPosition(this.timeSpinnerFrom, this.deliveryDto.time1FormKey));
                this.idSpinnerTime1To.setSelection(SpinnerUtil.keyToPosition(this.timeSpinnerTo, this.deliveryDto.time1ToKey));
                this.idSpinnerTime2From.setSelection(SpinnerUtil.keyToPosition(this.timeSpinnerFrom, this.deliveryDto.time2FormKey));
                this.idSpinnerTime2To.setSelection(SpinnerUtil.keyToPosition(this.timeSpinnerTo, this.deliveryDto.time2ToKey));
            }
        }
    }

    @Override
    public void onClick(View v) {
        // ダブルクリック抑止
        this.buttonReg.setEnabled(false);

        if (v == this.buttonReg) {

            // 『登録』/『更新』ボタン押下時

            // 設定時間の取得
            String spinnerTime1FromKey = SpinnerUtil.getKey(this.idSpinnerTime1From);
            String spinnerTime1ToKey = SpinnerUtil.getKey(this.idSpinnerTime1To);
            String spinnerTime2FromKey = SpinnerUtil.getKey(this.idSpinnerTime2From);
            String spinnerTime2ToKey = SpinnerUtil.getKey(this.idSpinnerTime2To);

            log.logD("『登録』/『更新』押下");
            if (this.editTextRegName == null || this.editTextRegName.getText() == null
                    || "".equals(this.editTextRegName.getText().toString())) {
                log.logD("登録名が入力されていない");
                // TODO 登録名の入力
//            } else if (checkTime(spinnerTime1FromKey, spinnerTime1ToKey, spinnerTime2FromKey, spinnerTime2ToKey)) {


            } else {
                log.logD("『登録』/『更新』");

                // 登録NO取得
                String no;
                if (this.deliveryDto.no == null) {
                    // 登録の場合
                    no = CommonUtil.getNextReginfo(new IssueSQL(this));
                } else {
                    // 更新の場合
                    no = this.deliveryDto.no;
                }

                // マナーモード取得
                int iMannersMode = getRadioGroupMannersMode(this.radioGroupMannersMode.getCheckedRadioButtonId());
                // wifi取得
                String sWifiMode = getRadioGroupWifiMode(this.idRadioGroupWifiMode.getCheckedRadioButtonId());

                // 登録/更新日時取得
                String dt = CommonUtil.getDate(Const.DateFormat.DATEFORMAT_DT);

                log.logD("登録NO : " + no);
                log.logD("登録名 : " + this.editTextRegName.getText());
                log.logD("住  所 : " + this.editTextRegAddress.getText());
                log.logD("緯  度 : " + this.editTextRegLatitude.getText());
                log.logD("経  度 : " + this.editTextRegLongitude.getText());
                log.logD("時間１From : " + spinnerTime1FromKey);
                log.logD("時間１To   : " + spinnerTime1ToKey);
                log.logD("時間２From : " + spinnerTime2FromKey);
                log.logD("時間２To   : " + spinnerTime2ToKey);
                log.logD("登録日 : " + dt);

                IssueSQL is = new IssueSQL(this);
                // SQL発行
                if (this.deliveryDto.no == null) {
                    // 登録の場合
                    log.logD("onClick :SQL発行 登録の場合");
                    if (is.insertUpdateDeleteSQL(Const.SqlKey.INSERT001, no, this.editTextRegName.getText().toString(),
                            this.editTextRegAddress.getText().toString(),
                            this.editTextRegLatitude.getText().toString(),
                            this.editTextRegLongitude.getText().toString(),
                            Integer.toString(iMannersMode),
                            sWifiMode,
                            Const.SurveillanceFlg.ON,
                            spinnerTime1FromKey,
                            spinnerTime1ToKey,
                            spinnerTime2FromKey,
                            spinnerTime2ToKey,
                            dt)) {
                        // 登録成功
                        log.logD("onClick :SQL発行 登録の場合 成功");
                        Toast.makeText(this, "登録しました", Toast.LENGTH_LONG).show();
                    } else {
                        // 登録失敗
                        log.logD("onClick :SQL発行 登録の場合 失敗");
                        Toast.makeText(this, "登録に失敗しました", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // 更新の場合
                    log.logD("onClick :SQL発行 更新の場合");
                    if (is.insertUpdateDeleteSQL(Const.SqlKey.UPDATE001, this.editTextRegName.getText().toString(),
                            this.editTextRegAddress.getText().toString(),
                            this.editTextRegLatitude.getText().toString(),
                            this.editTextRegLongitude.getText().toString(),
                            Integer.toString(iMannersMode), sWifiMode,
                            Const.SurveillanceFlg.ON,
                            spinnerTime1FromKey,
                            spinnerTime1ToKey,
                            spinnerTime2FromKey,
                            spinnerTime2ToKey,
                            dt, no)) {
                        // 更新成功
                        log.logD("onClick :SQL発行 更新の場合 成功");
                        Toast.makeText(this, "更新しました", Toast.LENGTH_LONG).show();
                    } else {
                        // 更新失敗
                        log.logD("onClick :SQL発行 更新の場合 失敗");
                        Toast.makeText(this, "更新に失敗しました", Toast.LENGTH_LONG).show();
                    }
                }

                // 画面遷移
                log.logD("onClick :画面遷移");
                Intent intent = new Intent(thisActivity, RegInfoListActivity.class);
                // 次の画面の戻るボタンでActivityを全て破棄して終了する。
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        } else if (v == this.buttonDel) {
            log.logD("『削除』");

            IssueSQL is = new IssueSQL(this);
            // SQL発行
            if (is.insertUpdateDeleteSQL(Const.SqlKey.DELETE001, this.deliveryDto.no)) {
                // 更新成功
                log.logD("onClick :SQL発行 更新の場合 成功");
                Toast.makeText(this, "削除しました", Toast.LENGTH_LONG).show();
            } else {
                log.logD("onClick :SQL発行 更新の場合 失敗");
                Toast.makeText(this, "削除に失敗しました", Toast.LENGTH_LONG).show();
            }

            // 画面遷移
            log.logD("onClick :画面遷移");
            // 次の画面の戻るボタンでActivityを全て破棄して終了する。
            Intent intent = new Intent(thisActivity, RegInfoListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } else if (v == this.buttonUpd) {
            log.logD("『位置情報編集』");
            // 画面遷移
            log.logD("onClick :画面遷移");
            this.deliveryDto.regName = this.editTextRegName.getText().toString();
            this.deliveryDto.status = Integer.toString(getRadioGroupMannersMode(this.radioGroupMannersMode
                    .getCheckedRadioButtonId()));
            Intent intent = new Intent(thisActivity, MapSearchActivity.class);
            intent.putExtra(DeliveryDto.NAME_DELIVERYDTO, this.deliveryDto);
            startActivity(intent);

        }

        // ダブルクリック抑止解除
        this.buttonReg.setEnabled(true);

    }

    /**
     * マナーモードラジオボタンの状態を取得する
     *
     * @param id
     * @return マナーモードの状態
     */
    private int getRadioGroupMannersMode(int id) {

        int iMannersMode = 0;
        if (id == this.radioButtonMannersModeManners.getId()) {
            // 『通常マナー』
            log.logD("onClick : 通常マナー");
            iMannersMode = AudioManager.RINGER_MODE_VIBRATE;
        } else if (id == this.radioButtonMannersModeSilence.getId()) {
            // 『サイレントマナー』
            log.logD("onClick : サイレントマナー");
            iMannersMode = AudioManager.RINGER_MODE_SILENT;

        } else {
            // 『OFF』
            log.logD("onClick : OFF");
            iMannersMode = AudioManager.RINGER_MODE_NORMAL;
        }
        return iMannersMode;
    }

    /**
     * WiFiラジオボタンの状態を取得する
     *
     * @param id
     * @return WiFiの状態
     */
    private String getRadioGroupWifiMode(int id) {

        String sWiFiMode = "0";
        if (id == this.idRadioButtonWifiOn.getId()) {
            // 『ON』
            log.logD("onClick : WiFi ON");
            sWiFiMode = Const.WiFiFlg.ON;

        } else {
            // 『OFF』
            log.logD("onClick : WiFi OFF");
            sWiFiMode = Const.WiFiFlg.OFF;
        }
        return sWiFiMode;
    }

//    private boolean checkTime(String spinnerTime1FromKey, String spinnerTime1ToKey, String spinnerTime2FromKey, String spinnerTime2ToKey) {
//        int intTime1FromKey = Integer.parseInt(spinnerTime1FromKey);
//        int intTime1ToKey = Integer.parseInt(spinnerTime1ToKey);
//        int intTime2FromKey = Integer.parseInt(spinnerTime2FromKey);
//        int intTime2ToKey = Integer.parseInt(spinnerTime2ToKey);
//
//
//        return true;
//    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int intTime1FromKey = Integer.parseInt(SpinnerUtil.getKey(this.idSpinnerTime1From));
        int intTime1ToKey = Integer.parseInt(SpinnerUtil.getKey(this.idSpinnerTime1To));
        int intTime2FromKey = Integer.parseInt(SpinnerUtil.getKey(this.idSpinnerTime2From));
        int intTime2ToKey = Integer.parseInt(SpinnerUtil.getKey(this.idSpinnerTime2To));

        if (adapterView == this.idSpinnerTime1From) {
//            log.logD("idSpinnerTime1From");
            timeSpinnerChange(intTime1FromKey, intTime1ToKey,intTime1FromKey, intTime1ToKey, this.idSpinnerTime1To, this.timeSpinnerFrom);
        } else if (adapterView == this.idSpinnerTime1To) {
//            log.logD("idSpinnerTime1To");
            timeSpinnerChange(intTime1ToKey, intTime1FromKey,intTime1FromKey, intTime1ToKey, this.idSpinnerTime1From, this.timeSpinnerTo);
        } else if (adapterView == this.idSpinnerTime2From) {
//            log.logD("idSpinnerTime2From");
            timeSpinnerChange(intTime2FromKey, intTime2ToKey,intTime2FromKey, intTime2ToKey, this.idSpinnerTime2To, this.timeSpinnerFrom);
        } else if (adapterView == this.idSpinnerTime2To) {
//            log.logD("idSpinnerTime2To");
            timeSpinnerChange(intTime2ToKey, intTime2FromKey,intTime2FromKey, intTime2ToKey, this.idSpinnerTime2From, this.timeSpinnerTo);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void timeSpinnerChange(int mainKey, int subKey, int fromKey, int toKey, Spinner subSpinner, ArrayAdapter<KeyValuePair> mainAdapter) {
        if (mainKey == 99) {
            subSpinner.setSelection(SpinnerUtil.keyToPosition(mainAdapter, "99"));
        } else if (fromKey >= toKey || subKey == 99) {
            subSpinner.setSelection(SpinnerUtil.keyToPosition(mainAdapter, Integer.toString(mainKey)));
        }
    }

}
