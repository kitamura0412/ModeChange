package com.k.modechange.common;

import android.content.Context;
import android.provider.Settings;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
/**
 * 入力チェッククラス
 * 
 * @author k.kitamura
 * @date 2014/05/05
 * 
 */
public class CheckLogic {

	/**
	 * GooglePlay開発者サービスが有効であるかチェックする
	 * 
	 * @param context 呼び出し元のContext
	 * @return 利用可能=true/利用不可=false
	 */
	public static boolean checkGooglePlayDeveloperService(Context context) {

		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);

		if (ConnectionResult.SUCCESS == resultCode) {
			// 利用可能
			return true;
		}
		// 利用不可
		return false;

	}

	/**
	 * 「位置情報」がONになっているかチェックする
	 * 
	 * @param context 呼び出し元のContext
	 * @return ON=true/OFF=false
	 */
	public static boolean checkLocationSettion(Context context) {

		String setting = Settings.System.getString(context.getContentResolver(), Settings.System.LOCATION_PROVIDERS_ALLOWED);

		// 利用不可
		return !"".equals(setting);

	}
	
	/**
	 * 緯度経度取得チェック
	 * @param latitude 緯度
	 * @param longitude 経度
	 * @return true=取得/false=未取得
	 * */
	public static boolean checkLocation(double latitude, double longitude) {

		if (latitude == 0.0 && longitude == 0.0) {

			return false;
		}
		// 利用不可
		return true;

	}

}
