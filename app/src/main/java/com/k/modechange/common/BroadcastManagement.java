package com.k.modechange.common;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.k.modechange.receiver.ModeSetReceiver;

/**
 * マナーモードの変更を検地するBroadcastReceiverクラス
 * 
 * @author k.kitamura
 * @date 2014/05/14
 * 
 */
public class BroadcastManagement {
    // 定数宣言
    /** リクエストコード(ModeSetReceiver) */
    public static final int REQUESTCODE_MODESETRECEIVER = 0;

    /** ログ出力クラス */
    private static OutputLog log = new OutputLog();

    /**
     * ModeSetReceiverのセット
     * 
     * @param context
     */
    public static void startModeSetReceiver(Context context) {

	// レシーバーセット
	Intent intent = new Intent(context, ModeSetReceiver.class);
	PendingIntent sender = PendingIntent.getBroadcast(context, REQUESTCODE_MODESETRECEIVER, intent, 0);

	// アラームマネージャの用意（初回は5秒後）
	long firstTime = SystemClock.elapsedRealtime();
	firstTime += 5 * 1000;
	// インターバルの取得
	long lInterval = 1000 * 60 * Long.parseLong(CommonUtil.getApData(context, Const.ApdataID.ID_100601));

	AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	log.logF("アラーム設定");
	am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, lInterval, sender);

    }

    /**
     * ModeSetReceiverのキャンセル
     * 
     * @param context
     */
    public static void endModeSetReceiver(Context context) {
	Intent intent = new Intent(context, ModeSetReceiver.class);
	PendingIntent sender = PendingIntent.getBroadcast(context, REQUESTCODE_MODESETRECEIVER, intent, 0);
	AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	am.cancel(sender);
    }
    
}
