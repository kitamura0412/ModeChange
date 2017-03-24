package com.k.modechange.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.k.modechange.R;
import com.k.modechange.common.CheckLogic;
import com.k.modechange.common.CommonUtil;
import com.k.modechange.common.Const;
import com.k.modechange.common.OutputLog;
import com.k.modechange.common.UtilAdapter;
import com.k.modechange.dto.DeliveryDto;

import java.util.ArrayList;
import java.util.HashMap;

import elephant.jp.net.database.sqlite.IssueSQL;

/**
 * 登録情報一覧のActivity
 *
 * @author k.kitamura
 * @date 2014/05/05
 */
public class RegInfoListActivity extends BaseActivity implements OnClickListener {

    /**
     * ログ出力クラス
     */
    private OutputLog log = new OutputLog();

    /**
     * 『登録』
     */
    private LinearLayout linearlayoutReg;
    /**
     * 『設定』
     */
    private LinearLayout linearlayoutSetup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        log.logD("開始 : onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.reginfolist);

        // 画面項目を設定する
        setInit();

        // 入力チェック
        inputCheck();

        log.logD("終了 : onCreate");
    }

    @Override
    public void onStart() {
        log.logD("開始 : onStart");
        super.onStart();


    }


    /**
     * 別画面から戻るボタン押下時
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        log.logD("onActivityResult");
        if (requestCode == Const.ForResult.FORRESULT_LOCATION_SETTINGS) {
            // 位置情報設定画面からの戻り
            inputCheck();
        }
    }

    /**
     * 画面項目を設定する
     */
    private void setInit() {

        // 画面名の設定
        setScreen(R.string.text_textView_screenName_main);
        LinearLayout linearLayoutUninputtedInfo = (LinearLayout) findViewById(R.id.id_linearLayout_uninputtedInfo);
        ImageView imageviewInfo = (ImageView) findViewById(R.id.id_imageview_info);

        LinearLayout linearLayoutRegList = (LinearLayout) findViewById(R.id.id_linearLayout_regList);
        ListView listViewRegList = (ListView) findViewById(R.id.id_listView_regList);

        this.linearlayoutReg = (LinearLayout) findViewById(R.id.id_linearlayout_reg);
        this.linearlayoutReg.setOnClickListener(this);
        this.linearlayoutSetup = (LinearLayout) findViewById(R.id.id_linearlayout_setup);
        this.linearlayoutSetup.setOnClickListener(this);

        // 広告表示
        AdView adView = (AdView) this.findViewById(R.id.adView);
        adView.loadAd(new AdRequest.Builder().build());

        IssueSQL is = new IssueSQL(this);
        ArrayList<HashMap<String, String>> resultList = is.selectSQL(Const.SqlKey.SELECT001);

        if (resultList.size() <= 0) {
            // 登録件数が0件の場合

            // 設定ボタンを非表示
            linearlayoutSetup.setVisibility(View.GONE);

            // 一覧を半分に表示
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 4);
            linearLayoutRegList.setLayoutParams(params);

            // 説明画面を表示
            linearLayoutUninputtedInfo.setVisibility(View.VISIBLE);
            linearLayoutUninputtedInfo.setLayoutParams(params);

            // 登録を促す説明の表示
            imageviewInfo.setImageDrawable(getResources().getDrawable(R.drawable.info_reg));
            imageviewInfo.setLayoutParams(params);

            TextView textViewRegInfo = (TextView) findViewById(R.id.id_textView_regInfo);
            textViewRegInfo.setVisibility(View.GONE);

        } else if (!Const.InfoFlg.ON.equals(CommonUtil.getApData(this, Const.ApdataID.ID_100101))) {
            // 説明画面への誘導フラグが1以外の場合

            // 一覧を設定
            UtilAdapter UAd = new UtilAdapter(this, CommonUtil.changeCharSequenceList(resultList), startOlCl);
            listViewRegList.setAdapter(UAd);

            // 一覧を半分に表示
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 4);
            linearLayoutRegList.setLayoutParams(params);

            // 説明画面を表示
            linearLayoutUninputtedInfo.setVisibility(View.VISIBLE);
            linearLayoutUninputtedInfo.setLayoutParams(params);

            // 説明画面への誘導する説明の表示
            imageviewInfo.setImageDrawable(getResources().getDrawable(R.drawable.info_rulesetup));
            imageviewInfo.setLayoutParams(params);

        } else {

            // 一覧を設定
            UtilAdapter UAd = new UtilAdapter(this, CommonUtil.changeCharSequenceList(resultList), startOlCl);
            listViewRegList.setAdapter(UAd);

            // 説明画面を非表示
            linearLayoutUninputtedInfo.setVisibility(View.GONE);

        }
    }

    /**
     * 入力チェック
     */
    private void inputCheck() {

        if (!CheckLogic.checkGooglePlayDeveloperService(this)) {
            // GooglePlay開発者サービスがインストールされていない場合

            String title = getResources().getString(R.string.alertdialog_title_002);
            String message = getResources().getString(R.string.alertdialog_message_002);
            String button001 = getResources().getString(R.string.alertdialog_button_003);
            String button002 = getResources().getString(R.string.alertdialog_button_004);

            AlertDialog alertDialog = CommonUtil.alertDialog(this, title, message, true, button001, button002,
                    startOcl1, null);

            // ダイアログのレイアウトを適用
            alertDialog.show();
        } else if (!CheckLogic.checkLocationSettion(this)) {
            // 本体の設定「位置情報」が無効の場合

            String title = getResources().getString(R.string.alertdialog_title_002);
            String message = getResources().getString(R.string.alertdialog_message_003);
            String button001 = getResources().getString(R.string.alertdialog_button_005);
            String button002 = getResources().getString(R.string.alertdialog_button_004);

            AlertDialog alertDialog = CommonUtil.alertDialog(this, title, message, true, button001, button002,
                    startOcl2, null);

            // ダイアログを表示
            alertDialog.show();
        } else if (CommonUtil.getHyoka(getApplicationContext())) {
            // マナーモードチェンジャーの評価画面に遷移
            CommonUtil.getApData(this, Const.ApdataID.ID_100101);
            String title = getResources().getString(R.string.alertdialog_title_003);
            String message = getResources().getString(R.string.alertdialog_message_004);
            String button001 = getResources().getString(R.string.alertdialog_button_006);
            String button002 = getResources().getString(R.string.alertdialog_button_007);
            AlertDialog alertDialog = CommonUtil.alertDialog(this, title, message, true, button001, button002,
                    startOcl31, startOcl32);

            // ダイアログを表示
            alertDialog.show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == this.linearlayoutReg) {
            // 『登録』 押下処理
            log.logD("『登録』押下");
            Intent intent = new Intent(thisActivity, MapSearchActivity.class);
            startActivity(intent);

        } else if (v == this.linearlayoutSetup) {
            // 『ルール設定』 押下処理
            log.logD("『設定』押下");
            Intent intent = new Intent(thisActivity, SetupActivity.class);
            startActivity(intent);
        }
    }

    /**
     * GPS確認ダイアログ『>Google Play開発者サービスをインストールする』
     */
    DialogInterface.OnClickListener startOcl1 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse(Const.GooglePlayDeveloperService.URL)),
                    Const.ForResult.FORRESULT_LOCATION_SETTINGS);
        }
    };

    /**
     * 位置情報設定画面に遷移
     */
    DialogInterface.OnClickListener startOcl2 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // GPS設定画面へ遷移
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                    Const.ForResult.FORRESULT_LOCATION_SETTINGS);
        }
    };


    /**
     * マナーモードチェンジャーの評価画面に遷移（今すぐ評価）
     */
    DialogInterface.OnClickListener startOcl31 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse(Const.ModeChange.URL)),
                    Const.ForResult.FORRESULT_LOCATION_SETTINGS);
            CommonUtil.setApData(getApplicationContext(), Const.Hyoka.COMPLETE, Const.ApdataID.ID_100701);
        }
    };
    /**
     * マナーモードチェンジャーの評価画面に遷移（閉じる）
     */
    DialogInterface.OnClickListener startOcl32 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            CommonUtil.setApData(getApplicationContext(), Const.Hyoka.LONG, Const.ApdataID.ID_100701);
        }
    };


    /**
     * タップした時のイベント
     */
    OnClickListener startOlCl = new OnClickListener() {
        public void onClick(View convertView) {
            log.logD("画面遷移");

            TextView textviewViewRegNo = (TextView) convertView.findViewById(R.id.id_textview_viewRegNo);
            TextView textviewViewRegName = (TextView) convertView.findViewById(R.id.id_textview_viewRegName);
            TextView textviewViewAddress = (TextView) convertView.findViewById(R.id.id_textview_viewAddress);
            TextView textviewViewLatitude = (TextView) convertView.findViewById(R.id.id_textview_viewLatitude);
            TextView textviewViewLongitude = (TextView) convertView.findViewById(R.id.id_textview_viewLongitude);
            TextView textviewViewMannersMode = (TextView) convertView.findViewById(R.id.id_textview_viewMannersMode);
            TextView textviewViewChangeFlg = (TextView) convertView.findViewById(R.id.id_textview_viewChangeFlg);
            TextView textviewViewWiFi = (TextView) convertView.findViewById(R.id.id_textview_viewWiFi);
            TextView time1FormKey = (TextView) convertView.findViewById(R.id.id_spinnerTime1_form);
            TextView time1ToKey = (TextView) convertView.findViewById(R.id.id_spinnerTime1_to);
            TextView time2FormKey = (TextView) convertView.findViewById(R.id.id_spinnerTime2_form);
            TextView time2ToKey = (TextView) convertView.findViewById(R.id.id_spinnerTime2_to);

            DeliveryDto deliveryDto = new DeliveryDto();
            deliveryDto.no = (String) textviewViewRegNo.getText();
            deliveryDto.regName = (String) textviewViewRegName.getText();
            deliveryDto.address = (String) textviewViewAddress.getText();
            deliveryDto.latitude = Double.parseDouble((String) textviewViewLatitude.getText());
            deliveryDto.longitude = Double.parseDouble((String) textviewViewLongitude.getText());
            deliveryDto.status = (String) textviewViewMannersMode.getTag();
            deliveryDto.changeFlg = (String) textviewViewChangeFlg.getTag();
            deliveryDto.wifi = (String) textviewViewWiFi.getTag();
            deliveryDto.time1FormKey = (String) time1FormKey.getTag();
            deliveryDto.time1ToKey = (String) time1ToKey.getTag();
            deliveryDto.time2FormKey = (String) time2FormKey.getTag();
            deliveryDto.time2ToKey = (String) time2ToKey.getTag();

            // 画面遷移
            Intent intent = new Intent(thisActivity, RegActivity.class);
            intent.putExtra(DeliveryDto.NAME_DELIVERYDTO, deliveryDto);
            startActivity(intent);
        }

    };

}
