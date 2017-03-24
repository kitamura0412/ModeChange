package com.k.modechange.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.k.modechange.common.CommonUtil;
import com.k.modechange.common.Const;
import com.k.modechange.common.OutputLog;
import com.k.modechange.common.ServiceManagement;

/**
 * 定期処理
 * 
 * @author kitamura
 * 
 */
public class BootCompletedReceiver extends BroadcastReceiver {

    /** ログ出力クラス */
    private OutputLog log = new OutputLog();

    @Override
    public void onReceive(Context context, Intent intent) {
	log.logF("端末起動");

	if (Const.SurveillanceFlg.ON.equals(CommonUtil.getApData(context, Const.ApdataID.ID_100501))) {
	    // 監視をONにした場合
	    // サービス開始
	    ServiceManagement.startService(context);
	} else {
	    // 監視をOFFにした場合
	    // サービスを停止
	    ServiceManagement.endService(context);
	}
    }

}
