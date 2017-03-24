package com.k.modechange.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.k.modechange.common.OutputLog;

/**
 * マナーモードの変更を検地するBroadcastReceiverクラス
 * 
 * @author k.kitamura
 * @date 2014/05/05
 * 
 */
public class ModeGetReceiver extends BroadcastReceiver {

    /** ログ出力クラス */
    OutputLog log = new OutputLog();

    @Override
    public void onReceive(Context context, Intent intent) {
	log.logD("onReceive : 開始");
	Bundle bundle = intent.getExtras();
	int  i = bundle.getInt("android.media.EXTRA_RINGER_MODE");
	log.logD("マナーモードが変更されました : " + i);
		
	log.logD("onReceive : 終了");
    }
}
