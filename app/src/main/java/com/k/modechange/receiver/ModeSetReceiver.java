package com.k.modechange.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.k.modechange.R;
import com.k.modechange.activity.RegInfoListActivity;
import com.k.modechange.common.CheckLogic;
import com.k.modechange.common.CommonUtil;
import com.k.modechange.common.Const;
import com.k.modechange.common.OutputLog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import elephant.jp.net.database.sqlite.IssueSQL;

/**
 * 定期処理
 *
 * @author kitamura
 */
public class ModeSetReceiver extends BroadcastReceiver
        implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    /**
     * ログ出力クラス
     */
    private OutputLog log = new OutputLog();

    /**
     * 呼び出し元のContext
     */
    private Context context;

    /**
     * 位置情報を取得するコネクション
     */
    private LocationClient locationClient;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        log.logF("アラーム受信");

        if (!CheckLogic.checkLocationSettion(context)) {
            // タスクバー
            Intent notificationIntent = new Intent(context, RegInfoListActivity.class);
            // 次の画面の戻るボタンでActivityを全て破棄して終了する。
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
            NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
            notification.setContentTitle(context.getResources().getString(R.string.app_name));
            notification.setContentText(context.getResources().getString(R.string.message_004));
            notification.setContentIntent(pendingIntent);
            notification.setSmallIcon(R.drawable.ic_launcher);
            notification.setAutoCancel(true);
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(1000, notification.build());

        } else {
            // 位置情報初期化
            setLocation();
        }
    }


    @Override
    public void onConnected(Bundle arg0) {
        log.logD("onConnected");

        Location location = locationClient.getLastLocation();

        if (location != null) {
            locationActive(location);
        } else {
            log.logD("位置情報が無効");
        }

        // APIから切断
        locationClient.disconnect();
    }

    public void locationActive(Location location) {
        // 位置情報を取得した場合
        // TODO 最後の位置情報が存在しない場合を検討
        log.logD("onLocationChanged : 現在地取得 : 緯度:" + location.getLatitude() + ", 経度:" + location.getLongitude());
        // 二点間の距離を算出する

        double aLatitude = location.getLatitude();
        double aLongitude = location.getLongitude();
        String hour = Integer.toString(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));

        CommonUtil.getCriteria();

        IssueSQL is = new IssueSQL(this.context);

        ArrayList<HashMap<String, String>> resultList = is.selectSQL(Const.SqlKey.SELECT004,
                Double.toString(aLatitude), Double.toString(aLongitude), hour, hour,
                Double.toString(aLatitude), Double.toString(aLongitude), hour, hour,
                Double.toString(aLatitude), Double.toString(aLongitude));
        for (HashMap<String, String> map : resultList) {
            log.logD("NO : " + map.get("RI3.NO") + " : " + map.get("NO"));
        }

        // マナーモード更新フラグ
        boolean updFlg = false;
        double bLatitude;
        double bLongitude;


        for (HashMap<String, String> map : resultList) {
            bLatitude = Double.parseDouble(map.get(Const.ColumnKeyRegInfo.LATITUDE));
            bLongitude = Double.parseDouble(map.get(Const.ColumnKeyRegInfo.LONGITUDE));

            float[] result = new float[1];
            Location.distanceBetween(aLatitude, aLongitude, bLatitude, bLongitude, result);
            log.logF("二点間の距離を取得 : A拠点:" + aLatitude + ", " + aLongitude + ", B拠点:" + bLatitude + ", " + bLongitude);
            float distance = result[0];

            log.logF("差:" + distance / 1000 + "km , " + ", 登録名:" + map.get(Const.ColumnKeyRegInfo.REG_NAME) + ", 住所:"
                    + map.get(Const.ColumnKeyRegInfo.ADDRESS));

            if (distance < 1000f) {
                // 二点間の距離が1000m以内だったら
                log.logF("登録リストのマナーモード");
                log.logF("STATUS : " + map.get(Const.ColumnKeyRegInfo.STATUS));
                log.logF("WIFI   : " + map.get(Const.ColumnKeyRegInfo.WIFI));

                // マナーモードを変更する
                updAudidService(Integer.parseInt(map.get(Const.ColumnKeyRegInfo.STATUS)));


                // WiFiを変更する
                String sWifiMode = map.get(Const.ColumnKeyRegInfo.WIFI);
                if (!Const.WiFiFlg.RINGER_MODE_NONE.equals(sWifiMode)) {
                    updAudidWifiService(Const.WiFiFlg.ON.equals(sWifiMode));
                }

                updFlg = true;
                break;

            } else {
                break;
            }

        }

        if (!updFlg) {
            // 現在地が登録した範囲から遠かった場合

            // 変更するマナーモードを取得する
            String sMode = CommonUtil.getApData(this.context, Const.ApdataID.ID_100401);
            String sWifiMode = CommonUtil.getApData(this.context, Const.ApdataID.ID_100402);

            log.logF("STATUS : " + sMode);
            log.logF("WIFI   : " + sWifiMode);

            if (!Const.RingerModeCD.RINGER_MODE_NONE.equals(sMode)) {
                // 設定が『何もしない』以外の場合
                log.logF("デフォルトのマナーモード");

                int mode = Integer.parseInt(sMode);

                // マナーモードを変更する
                updAudidService(mode);

                // ログ
                getModeLog(mode);

            }

            // WiFiを変更する
            if (!Const.WiFiFlg.RINGER_MODE_NONE.equals(sWifiMode)) {
                updAudidWifiService(Const.WiFiFlg.ON.equals(sWifiMode));
            }
        }
    }

    /**
     * ログ出力用メソッド
     *
     * @param mode
     */
    private void getModeLog(int mode) {
        // ログ出力部
        if (AudioManager.RINGER_MODE_VIBRATE == mode) {
            // 『通常マナー』
            log.logF("マナーモードを「通常マナー」に変更しました");

        } else if (AudioManager.RINGER_MODE_SILENT == mode) {
            // 『サイレントマナー』
            log.logF("マナーモードを「サイレントマナー」に変更しました");
        } else {
            // 『OFF』
            log.logF("マナーモードを「OFF」に変更しました");
        }
    }

    /**
     * マナーモードを変更する
     *
     * @param mode 変更するモード
     */
    private void updAudidService(int mode) {

        AudioManager audioManager = (AudioManager) this.context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(mode);

    }

    /**
     * マナーモードを変更する
     *
     * @param mode 変更するモード
     */
    private void updAudidWifiService(boolean mode) {

        WifiManager wifiManager = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(mode);

    }

    /**
     * 位置情報初期化
     */
    private void setLocation() {
        log.logD("setLocation");
        // 今回追加
        locationClient = new LocationClient(this.context, this, this);
        // APIに接続
        locationClient.connect();

    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        log.logD("onConnectionFailed : 接続失敗");
    }

    @Override
    public void onDisconnected() {
        log.logD("onDisconnected : 切断");
    }

}
