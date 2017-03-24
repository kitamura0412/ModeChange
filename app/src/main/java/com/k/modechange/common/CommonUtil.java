package com.k.modechange.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Criteria;
import android.media.AudioManager;
import android.view.Display;
import android.view.WindowManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import elephant.jp.net.database.sqlite.IssueSQL;

/**
 * 共通メソッドクラス
 *
 * @author k.kitamura
 * @date 2014/05/05
 */
public class CommonUtil {

    /**
     * ログ出力クラス
     */
    private static OutputLog log = new OutputLog();

    /**
     * 【アプリケーションデータ】の更新
     *
     * @param context 呼び出し元のContext
     * @param data    更新年月日時分秒ミリ秒
     * @param id      更新ID
     * @return 結果
     */
    public static boolean setApData(Context context, String data, String id) {
        log.logD("setApData id : " + id + ", data : " + data);
        IssueSQL is = new IssueSQL(context);
        return is.insertUpdateDeleteSQL(Const.SqlKey.UPDATE002, data, getDate(Const.DateFormat.DATEFORMAT_DT), id);

    }

    /**
     * 【アプリケーションデータ】の検索
     *
     * @param context 呼び出し元のContext
     * @param id      検索するid
     * @return 結果
     */
    public static String getApData(Context context, String id) {
        log.logD("getApData 開始 id : " + id);
        IssueSQL is = new IssueSQL(context);
        ArrayList<HashMap<String, String>> resultList = is.selectSQL(Const.SqlKey.SELECT003, id);
        log.logD("getApData 終了 id : " + id);
        return resultList.get(0).get(Const.ColumnKeyApdata.DATA);

    }

    /**
     * 評価条件の取得
     */
    public static boolean getHyoka(Context context) {
        IssueSQL is = new IssueSQL(context);
        ArrayList<HashMap<String, String>> resultList = is.selectSQL(Const.SqlKey.SELECT003, Const.ApdataID.ID_100701);
        String hyokaDate = resultList.get(0).get(Const.ColumnKeyApdata.DATA);

        if (Const.Hyoka.COMPLETE.equals(hyokaDate)) {
            return false;
        }

        if (hyokaDate == null || "".equals(hyokaDate)) {
            // 未登録の場合は登録する。
            setApData(context, Const.Hyoka.SHORT, Const.ApdataID.ID_100701);
        } else {
            // 既に登録されていた場合
            Date now = toDate(getDate(Const.DateFormat.DATEFORMAT_DT), Const.DateFormat.DATEFORMAT_DT);
            Date updDt = toDate(resultList.get(0).get(Const.ColumnKeyApdata.UPD_DT), Const.DateFormat.DATEFORMAT_DT);
            long dayDiff = (updDt.getTime() - now.getTime()) / (1000 * 60 * 60 * 24);

            if (dayDiff >= Integer.parseInt(hyokaDate)) {
                // 登録した日より、指定した日数を超過したらダイアログ表示
                return true;
            }
        }

        return false;
    }


    /**
     * @param stringList ArrayList<HashMap<String, String>>
     * @return StringをCharSequenceに変換したList
     */
    public static ArrayList<HashMap<String, CharSequence>> changeCharSequenceList(
            ArrayList<HashMap<String, String>> stringList) {

        ArrayList<HashMap<String, CharSequence>> aList = new ArrayList<HashMap<String, CharSequence>>();

        for (HashMap<String, String> map : stringList) {
            HashMap<String, CharSequence> mapList = new HashMap<String, CharSequence>();
            mapList.put(Const.ColumnKeyRegInfo.NO, (CharSequence) map.get(Const.ColumnKeyRegInfo.NO));
            mapList.put(Const.ColumnKeyRegInfo.REG_NAME, (CharSequence) map.get(Const.ColumnKeyRegInfo.REG_NAME));
            mapList.put(Const.ColumnKeyRegInfo.ADDRESS, (CharSequence) map.get(Const.ColumnKeyRegInfo.ADDRESS));
            mapList.put(Const.ColumnKeyRegInfo.LATITUDE, (CharSequence) map.get(Const.ColumnKeyRegInfo.LATITUDE));
            mapList.put(Const.ColumnKeyRegInfo.LONGITUDE, (CharSequence) map.get(Const.ColumnKeyRegInfo.LONGITUDE));
            mapList.put(Const.ColumnKeyRegInfo.STATUS, (CharSequence) map.get(Const.ColumnKeyRegInfo.STATUS));
            mapList.put(Const.ColumnKeyRegInfo.CHANGE_FLG, (CharSequence) map.get(Const.ColumnKeyRegInfo.CHANGE_FLG));
            mapList.put(Const.ColumnKeyRegInfo.WIFI, (CharSequence) map.get(Const.ColumnKeyRegInfo.WIFI));
            mapList.put(Const.ColumnKeyRegInfo.TIME1_FROM, (CharSequence) map.get(Const.ColumnKeyRegInfo.TIME1_FROM));
            mapList.put(Const.ColumnKeyRegInfo.TIME1_TO, (CharSequence) map.get(Const.ColumnKeyRegInfo.TIME1_TO));
            mapList.put(Const.ColumnKeyRegInfo.TIME2_FROM, (CharSequence) map.get(Const.ColumnKeyRegInfo.TIME2_FROM));
            mapList.put(Const.ColumnKeyRegInfo.TIME2_TO, (CharSequence) map.get(Const.ColumnKeyRegInfo.TIME2_TO));

            if (AudioManager.RINGER_MODE_VIBRATE == Integer.parseInt(map.get(Const.ColumnKeyRegInfo.STATUS))) {
                // 通常マナー
                mapList.put(Const.ColumnKeyRegInfo.W_STATUS, Const.RingerMode.RINGER_MODE_VIBRATE);
            } else if (AudioManager.RINGER_MODE_SILENT == Integer.parseInt(map.get(Const.ColumnKeyRegInfo.STATUS))) {
                // サイレントマナー
                mapList.put(Const.ColumnKeyRegInfo.W_STATUS, Const.RingerMode.RINGER_MODE_SILENT);
            } else {
                // OFF
                mapList.put(Const.ColumnKeyRegInfo.W_STATUS, Const.RingerMode.RINGER_MODE_NORMAL);
            }

            if (Const.SurveillanceFlg.ON.equals(map.get(Const.ColumnKeyRegInfo.CHANGE_FLG))) {
                // 監視ON
                mapList.put(Const.ColumnKeyRegInfo.W_CHANGE_FLG, Const.WSurveillanceFlg.ON);
            } else {
                // 監視OFF
                mapList.put(Const.ColumnKeyRegInfo.W_CHANGE_FLG, Const.WSurveillanceFlg.OFF);
            }

            if (Const.WiFiFlg.ON.equals(map.get(Const.ColumnKeyRegInfo.WIFI))) {
                // 監視ON
                mapList.put(Const.ColumnKeyRegInfo.W_WIFI, Const.WWiFiFlg.ON);

            } else if (Const.WiFiFlg.OFF.equals(map.get(Const.ColumnKeyRegInfo.WIFI))) {
                // 監視OFF
                mapList.put(Const.ColumnKeyRegInfo.W_WIFI, Const.WWiFiFlg.OFF);

            } else {

                mapList.put(Const.ColumnKeyRegInfo.W_WIFI, Const.WWiFiFlg.RINGER_MODE_NONE);

            }

            aList.add(mapList);
        }
        return aList;

    }

    /**
     * 現在時刻を取得
     *
     * @param dateFormat 日付のフォーマット
     * @return 【登録情報】の登録最大値
     */
    public static String getDate(String dateFormat) {
        // 表示形式を設定
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat);
            return sdf1.format(new Date());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 現在時刻を取得
     *
     * @param dateFormat 日付のフォーマット
     * @return 【登録情報】の登録最大値
     */
    public static Date toDate(String str, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            log.logE("フォーマット失敗", e);
            return null;
        }
        return date;
    }

    /**
     * @param is SQLiteを発行するオブジェクト
     * @return 【登録情報】の登録最大値
     */
    public static String getMaxReginfo(IssueSQL is) {
        ArrayList<HashMap<String, String>> result = is.selectSQL(Const.SqlKey.SELECT002);
        HashMap<String, String> map = result.get(0);
        if (map.get(Const.ColumnKeyRegInfo.MAX_NO) == null) {
            return "0";
        }

        return map.get(Const.ColumnKeyRegInfo.MAX_NO);
    }

    /**
     * @param is SQLiteを発行するオブジェクト
     * @return 【登録情報】の登録最大値+1
     */
    public static String getNextReginfo(IssueSQL is) {
        int nextReginfo = Integer.parseInt(getMaxReginfo(is)) + 1;
        return Integer.toString(nextReginfo);
    }

    /**
     * 登録名を取得する
     *
     * @param is SQLiteを発行するオブジェクト
     * @return 登録名
     */
    public static String getRegName(IssueSQL is) {
        return "登録名" + getNextReginfo(is);
    }

    /**
     * @return 値をセットしたLocationManager
     */
    public static Criteria getCriteriaFine() {

        // ローケーション取得条件の設定
        Criteria criteria = new Criteria();
        // 緯度と経度用の要求精度を示します。
        // ACCURACY_FINE = 1 素晴らしい
        // ACCURACY_COARSE = 2 近似(100-500m)
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // POWER_LOW = 1 低い所要電力を示す定数。
        // POWER_MEDIUM = 2 中間の所要電力を示す定数。
        // POWER_HIGH = 3 高い所要電力を示す定数。
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        // 速度情報 有無
        criteria.setSpeedRequired(false);
        // 高度情報 有無
        criteria.setAltitudeRequired(false);
        // 方位情報 有無
        criteria.setBearingRequired(false);
        // 取得費用 有無
        criteria.setCostAllowed(false);

        return criteria;

    }

    /**
     * @return 値をセットしたLocationManager
     */
    public static Criteria getCriteria() {

        // ローケーション取得条件の設定
        Criteria criteria = new Criteria();
        // 緯度と経度用の要求精度を示します。
        // ACCURACY_FINE = 1 素晴らしい
        // ACCURACY_COARSE = 2 近似(100-500m)
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        // POWER_LOW = 1 低い所要電力を示す定数。
        // POWER_MEDIUM = 2 中間の所要電力を示す定数。
        // POWER_HIGH = 3 高い所要電力を示す定数。
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        // 速度情報 有無
        criteria.setSpeedRequired(false);
        // 高度情報 有無
        criteria.setAltitudeRequired(false);
        // 方位情報 有無
        criteria.setBearingRequired(false);
        // 取得費用 有無
        criteria.setCostAllowed(false);

        return criteria;

    }

    /**
     * @param context    呼び出し元のContext
     * @param title      ダイアログのタイトル
     * @param message    ダイアログメッセージ
     * @param cancelable キャンセル可否
     * @param button1    ダイアログボタン1
     * @param ocl1       ダイアログボタン1の処理
     * @return AlertDialog
     */
    public static AlertDialog alertDialog(Context context, String title, String message, boolean cancelable,
                                          String button1, DialogInterface.OnClickListener ocl1) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // アラートダイアログのタイトルを設定します
        alertDialogBuilder.setTitle(title);
        // アラートダイアログのメッセージを設定します
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(button1, ocl1);
        // アラートダイアログのキャンセルが可能かどうかを設定します
        alertDialogBuilder.setCancelable(cancelable);
        return alertDialogBuilder.create();

    }

    /**
     * @param context    呼び出し元のContext
     * @param title      ダイアログのタイトル
     * @param message    ダイアログメッセージ
     * @param cancelable キャンセル可否
     * @param button1    ダイアログボタン1
     * @param button2    ダイアログボタン2
     * @param ocl1       ダイアログボタン1の処理
     * @param ocl2       ダイアログボタン2の処理
     * @return AlertDialog
     */
    public static AlertDialog alertDialog(Context context, String title, String message, boolean cancelable,
                                          String button1, String button2, DialogInterface.OnClickListener ocl1, DialogInterface.OnClickListener ocl2) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // アラートダイアログのタイトルを設定します
        alertDialogBuilder.setTitle(title);
        // アラートダイアログのメッセージを設定します
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(button1, ocl1);
        alertDialogBuilder.setNeutralButton(button2, ocl2);
        // アラートダイアログのキャンセルが可能かどうかを設定します
        alertDialogBuilder.setCancelable(cancelable);
        return alertDialogBuilder.create();
    }

    /**
     * @param context    呼び出し元のContext
     * @param title      ダイアログのタイトル
     * @param message    ダイアログメッセージ
     * @param cancelable キャンセル可否
     * @param button1    ダイアログボタン1
     * @param button2    ダイアログボタン2
     * @param button3    ダイアログボタン3
     * @param ocl1       ダイアログボタン1の処理
     * @param ocl2       ダイアログボタン2の処理
     * @param ocl3       ダイアログボタン3の処理
     * @return AlertDialog
     */
    public static AlertDialog alertDialog(Context context, String title, String message, boolean cancelable,
                                          String button1, String button2, String button3, DialogInterface.OnClickListener ocl1,
                                          DialogInterface.OnClickListener ocl2, DialogInterface.OnClickListener ocl3) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // アラートダイアログのタイトルを設定します
        alertDialogBuilder.setTitle(title);
        // アラートダイアログのメッセージを設定します
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton(button1, ocl1);
        alertDialogBuilder.setNeutralButton(button2, ocl2);
        alertDialogBuilder.setNegativeButton(button3, ocl3);
        // アラートダイアログのキャンセルが可能かどうかを設定します
        alertDialogBuilder.setCancelable(cancelable);
        return alertDialogBuilder.create();
    }

    /**
     * 画面サイズ取得
     *
     * @param context 呼び出し元のContext
     * @return 画面サイズを持つDisplayクラス
     */
    public static Display getDisplay(Context context) {
        // 画面サイズの取得
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        return wm.getDefaultDisplay();
    }

    /**
     * px→dpに変換
     *
     * @param context 呼び出し元のContext
     * @param px      変更するpx
     * @return pxから変更されたdp
     */
    public static int changeDp(Context context, float px) {
        // density (比率)を取得する
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }

    /**
     * dp→pxに変換
     *
     * @param context 呼び出し元のContext
     * @param dp      変更するdp
     * @return dpから変更されたpx
     */
    public static int changePx(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }
}
