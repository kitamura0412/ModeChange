package com.k.modechange.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.k.modechange.common.BroadcastManagement;
import com.k.modechange.common.OutputLog;

/**
 * マナーモードの変更を検地するBroadcastReceiverクラス
 * 
 * @author k.kitamura
 * @date 2014/05/05
 * 
 */
public class SendBroadcastService extends Service {
    /** ログ出力クラス */
    private OutputLog log = new OutputLog();

    @Override
    public void onCreate() {
	super.onCreate();
	log.logF("サービス開始");

	// レシーバーセット
	BroadcastManagement.startModeSetReceiver(this);

    }

    @Override
    public void onDestroy() {
	super.onDestroy();

	// ブロードキャストのキャンセル
	BroadcastManagement.endModeSetReceiver(this);
	log.logF("サービス終了");
    }

    @Override
    public IBinder onBind(Intent intent) {
	// 未使用
	return null;
    }

}
