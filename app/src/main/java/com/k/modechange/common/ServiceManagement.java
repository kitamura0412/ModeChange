package com.k.modechange.common;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;

import com.k.modechange.service.SendBroadcastService;

/**
 * マナーモードの変更を検地するBroadcastReceiverクラス
 * 
 * @author k.kitamura
 * @date 2014/05/14
 * 
 */
public class ServiceManagement {

    /** ログ出力クラス */
    private static OutputLog log = new OutputLog();

    /**
     * 定期処理の設定
     * 
     * @param context
     *            呼び出し元のContext
     */
    public static void startService(Context context) {

	// サービス起動チェック
	if (isServiceRunning(context, SendBroadcastService.class)) {
	    // サービス起動中

	    // サービス再起動
	    endService(context);
	    context.startService(new Intent(context, SendBroadcastService.class));

	} else {
	    // サービス未起動

	    // ブロードキャストのキャンセル
	    BroadcastManagement.endModeSetReceiver(context);
	    // サービス開始
	    context.startService(new Intent(context, SendBroadcastService.class));

	}

    }

    /**
     * 定期処理のキャンセル
     * 
     * @param context
     *            呼び出し元のContext
     */
    public static void endService(Context context) {

	// サービス終了
	context.stopService(new Intent(context, SendBroadcastService.class));

	// ブロードキャストのキャンセル
	BroadcastManagement.endModeSetReceiver(context);

    }

    /**
     * サービス起動チェック
     * 
     * @param context
     *            呼び出し元のContext
     * @param cClass
     *            チェックするクラス
     * @return 結果
     */
	private static boolean isServiceRunning(Context context, Class<?> cClass) {
	ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	List<RunningServiceInfo> runningService = activityManager.getRunningServices(100);
	for (RunningServiceInfo i : runningService) {
	    if (cClass.getName().equals(i.service.getClassName())) {
		log.logD("サービス起動チェック " + cClass.getName() + " true");
		return true;
	    }
	}
	log.logD("サービス起動チェック " + cClass.getName() + " false");
	return false;
    }

}
