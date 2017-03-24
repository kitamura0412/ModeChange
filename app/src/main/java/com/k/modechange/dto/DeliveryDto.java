package com.k.modechange.dto;

import java.io.Serializable;

/**
 * 画面間で引き継ぐデータを保持するクラス
 * 
 * @author k.kitamura
 * @date 2014/05/05
 * 
 */
public class DeliveryDto implements Serializable {

    /** 画面間受け渡し名 */
    public static final String NAME_DELIVERYDTO = "NAME_DELIVERYDTO";

    // 新規/更新の場合
    /** 緯度 */
    public double latitude = 0.0;
    /** 経度 */
    public double longitude = 0.0;
    /** ズームレベル */
    public int zoomlevel = 0;
    /** 住所 */
    public String address = null;

    // 更新の場合
    /** No(登録番号) */
    public String no = null;
    /** 登録名 */
    public String regName = null;
    /** ステータス(マナーモード) */
    public String status = null;
    /** ステータス(wifi) */
    public String wifi = null;
    /** 切替フラグ(監視状態) */
    public String changeFlg = null;
    /** 時間１From */
    public String time1FormKey = null;
    /** 時間１To */
    public String time1ToKey = null;
    /** 時間２From */
    public String time2FormKey = null;
    /** 時間２To */
    public String time2ToKey = null;
}
