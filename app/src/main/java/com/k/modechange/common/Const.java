package com.k.modechange.common;

/**
 * 定数定義クラス
 *
 * @author k.kitamura
 * @date 2014/05/05
 */
public class Const {

    /**
     * 外部ファイルのファイル名を定義するクラス
     *
     * @author kitamura
     */
    public class FileName {
        /**
         * SQL定義クラス
         */
        public static final String SQL_PROPERTIES = "sql.properties";
    }

    /**
     * SQLプロパティーファイルのKey情報を定義する。
     *
     * @author kitamura
     */
    public class SqlKey {
        /**
         * 【登録情報】の作成
         */
        public static final String CREATE001 = "CREATE001";
        /**
         * 【アプリケーションデータ】の作成
         */
        public static final String CREATE002 = "CREATE002";
        /**
         * 【登録情報】の削除
         */
        public static final String DROP001 = "DROP001";
        /**
         * 【アプリケーションデータ】の削除
         */
        public static final String DROP002 = "DROP002";

        /**
         * 【登録情報】のデータ投入
         */
        public static final String INSERT001 = "INSERT001";
        /**
         * 【アプリケーションデータ】のデータ投入(初期データ「前回監視時間」)
         */
        public static final String INSERT101 = "INSERT101";
        /**
         * 【アプリケーションデータ】のデータ投入(初期データ「アプリ初回起動フラグ」)
         */
        public static final String INSERT102 = "INSERT102";
        /**
         * 【アプリケーションデータ】のデータ投入(初期データ「GPS利用認識フラグ」)
         */
        public static final String INSERT103 = "INSERT103";
        // /** 【アプリケーションデータ】のデータ投入(初期データ「ユーザマナーモード制御監視フラグ」) */
        // public static final String INSERT104 = "INSERT104";
        /**
         * 【アプリケーションデータ】のデータ投入(初期データ「デフォルトのモード（マナーモード）」)
         */
        public static final String INSERT104 = "INSERT104";
        /**
         * 【アプリケーションデータ】のデータ投入(初期データ「デフォルトのモード（Wi-Fi）」)
         */
        public static final String INSERT107 = "INSERT107";
        /**
         * 【アプリケーションデータ】のデータ投入(初期データ「アプリの機能を有効にする」)
         */
        public static final String INSERT105 = "INSERT105";
        /**
         * 【アプリケーションデータ】のデータ投入(初期データ「監視間隔（分）」)
         */
        public static final String INSERT106 = "INSERT106";

        /**
         * 【登録情報】の更新
         */
        public static final String UPDATE001 = "UPDATE001";
        /**
         * 【アプリケーションデータ】の更新
         */
        public static final String UPDATE002 = "UPDATE002";

        /**
         * 【登録情報】を全量検索
         */
        public static final String SELECT001 = "SELECT001";
        /**
         * 【登録情報】の最大値取得
         */
        public static final String SELECT002 = "SELECT002";
        /**
         * 【アプリケーションデータ】の検索
         */
        public static final String SELECT003 = "SELECT003";
        /**
         * 【登録情報】を条件付検索（位置情報、時間）
         */
        public static final String SELECT004 = "SELECT004";

        /**
         * 【登録情報】の削除
         */
        public static final String DELETE001 = "DELETE001";

    }

    /**
     * 【登録情報】のカラム名
     *
     * @author kitamura
     */
    public class ColumnKeyRegInfo {
        /**
         * カラム名【アプリケーションデータ.No】
         */
        public static final String NO = "NO";
        /**
         * カラム名【アプリケーションデータ.登録名】
         */
        public static final String REG_NAME = "REG_NAME";
        /**
         * カラム名【アプリケーションデータ.住所】
         */
        public static final String ADDRESS = "ADDRESS";
        /**
         * カラム名【アプリケーションデータ.緯度】
         */
        public static final String LATITUDE = "LATITUDE";
        /**
         * カラム名【アプリケーションデータ.経度】
         */
        public static final String LONGITUDE = "LONGITUDE";
        // /** カラム名【アプリケーションデータ.基準】 */
        // public static final String STANDARD = "STANDARD";
        /**
         * カラム名【アプリケーションデータ.ステータス】(マナーモード)
         */
        public static final String STATUS = "STATUS";
        /**
         * カラム名【アプリケーションデータ.WiFi】(WiFi)
         */
        public static final String WIFI = "WIFI";
        /**
         * カラム名【アプリケーションデータ.WiFi】和名(WiFi)
         */
        public static final String W_WIFI = "W_WIFI";
        /**
         * カラム名【アプリケーションデータ.切替フラグ】
         */
        public static final String CHANGE_FLG = "CHANGE_FLG";
        /**
         * カラム名【アプリケーションデータ.登録日時】
         */
        public static final String REG_DT = "REG_DT";
        /**
         * カラム名【アプリケーションデータ.更新日時】
         */
        public static final String UPD_DT = "UPD_DT";
        /**
         * カラム名【アプリケーションデータ.MAX(NO) AS MAX_NO】
         */
        public static final String MAX_NO = "MAX_NO";
        /**
         * カラム名【アプリケーションデータ.ステータス(和名)】(マナーモード)
         */
        public static final String W_STATUS = "W_STATUS";
        /**
         * カラム名【アプリケーションデータ.切替フラグ(和名)】(監視フラグ)
         */
        public static final String W_CHANGE_FLG = "W_CHANGE_FLG";
        /**
         * カラム名【アプリケーションデータ.時間１From】
         */
        public static final String TIME1_FROM = "TIME1_FROM";
        /**
         * カラム名【アプリケーションデータ.時間１To】
         */
        public static final String TIME1_TO = "TIME1_TO";
        /**
         * カラム名【アプリケーションデータ.時間2From】
         */
        public static final String TIME2_FROM = "TIME2_FROM";
        /**
         * カラム名【アプリケーションデータ.時間2To】
         */
        public static final String TIME2_TO = "TIME2_TO";
    }

    /**
     * 【アプリケーションデータ】のカラム名
     *
     * @author kitamura
     */
    public class ColumnKeyApdata {
        /**
         * カラム名【アプリケーションデータ.ID】
         */
        public static final String ID = "ID";
        /**
         * カラム名【アプリケーションデータ.データ】
         */
        public static final String DATA = "DATA";
        /**
         * カラム名【アプリケーションデータ.登録日時】
         */
        public static final String REG_DT = "REG_DT";
        /**
         * カラム名【アプリケーションデータ.更新日時】
         */
        public static final String UPD_DT = "UPD_DT";
    }

    /**
     * 【アプリケーションデータ】のID
     *
     * @author kitamura
     */
    public class ApdataID {
        /**
         * 【アプリケーションデータ.ID】のコード「設定画面への誘導」
         */
        public static final String ID_100101 = "100101";
        /**
         * 【アプリケーションデータ.ID】のコード「アプリ初回起動」
         */
        public static final String ID_100201 = "100201";
        /**
         * 【アプリケーションデータ.ID】のコード「ポップアップチェック」
         */
        public static final String ID_100301 = "100301";
        /**
         * 【アプリケーションデータ.ID】のコード「デフォルトのモード」（マナーモード）
         */
        public static final String ID_100401 = "100401";
        /**
         * 【アプリケーションデータ.ID】のコード「デフォルトのモード」（Wi-Fi）
         */
        public static final String ID_100402 = "100402";
        /**
         * 【アプリケーションデータ.ID】のコード「アプリケーション機能有効/無効」
         */
        public static final String ID_100501 = "100501";
        /**
         * 【アプリケーションデータ.ID】のコード「監視間隔（分）」
         */
        public static final String ID_100601 = "100601";
        /**
         * 【アプリケーションデータ.ID】のコード「評価」
         */
        public static final String ID_100701 = "100701";
    }

    /**
     * デフォルトのマナーモードのコードを定義するクラス
     */
    public class RingerModeCD {
        /**
         * 通常マナー
         */
        public static final String RINGER_MODE_VIBRATE = "1";
        /**
         * サイレントマナー
         */
        public static final String RINGER_MODE_SILENT = "0";
        /**
         * OFF
         */
        public static final String RINGER_MODE_NORMAL = "2";
        /**
         * ユーザが最後に設定したマナーモード
         */
        public static final String RINGER_MODE_LAST = "3";
        /**
         * 何もしない
         */
        public static final String RINGER_MODE_NONE = "4";
    }

    /**
     * 日付フォーマットクラス
     *
     * @author kitamura
     */
    public class DateFormat {
        /**
         * フォーマット yyyy/mm/dd hh:mm:ss'SSS
         */
        public static final String DATEFORMAT_DT = "yyyy/MM/dd hh:mm:ss.SSS";
        /**
         * フォーマット yyyy/mm/dd hh:mm:ss
         */
        public static final String DATEFORMAT_DT2 = "yyyy/MM/dd hh:mm:ss";
    }

    /**
     * 監視フラグ
     */
    public class SurveillanceFlg {
        /**
         * 監視フラグON
         */
        public static final String ON = "1";
        /**
         * 監視フラグOFF
         */
        public static final String OFF = "0";
    }

    /**
     * 監視フラグ(和名)
     */
    public class WSurveillanceFlg {
        /**
         * 監視フラグON
         */
        public static final String ON = "監視ON";
        /**
         * 監視フラグOFF
         */
        public static final String OFF = "監視OFF";
    }

    /**
     * 説明画面への誘導フラグ
     */
    public class InfoFlg {
        /**
         * 監視フラグON
         */
        public static final String ON = "1";
        /**
         * 監視フラグOFF
         */
        public static final String OFF = "0";
    }

    /**
     * マナーモード
     */
    public class RingerMode {
        /**
         * 通常マナー
         */
        public static final String RINGER_MODE_VIBRATE = "通常マナー";
        /**
         * サイレントマナー
         */
        public static final String RINGER_MODE_SILENT = "サイレントマナー";
        /**
         * OFF
         */
        public static final String RINGER_MODE_NORMAL = "OFF";
    }

    /**
     * GooglePlay開発者サービスの外部情報
     */
    public class GooglePlayDeveloperService {
        /**
         * GooglePlay開発者サービスのインストール用のURL
         */
        public static final String URL = "https://play.google.com/store/apps/details?id=com.google.android.gms";
    }

    /**
     * マナーモードチェンジャーの外部情報
     */
    public class ModeChange {
        /**
         * GooglePlay開発者サービスの評価用のURL
         */
        public static final String URL = "https://play.google.com/store/apps/details?id=com.k.modechange";
    }

    /**
     * 画面遷移ID
     */
    public class ForResult {

        /**
         * 位置情報設定画面
         */
        public static final int FORRESULT_LOCATION_SETTINGS = 1;
    }


    /**
     * WiFiステータスフラグ
     */
    public class WiFiFlg {
        /**
         * WiFiステータスフラグON
         */
        public static final String ON = "1";
        /**
         * WiFiステータスフラグOFF
         */
        public static final String OFF = "0";
        /**
         * 何もしない
         */
        public static final String RINGER_MODE_NONE = "4";
    }

    /**
     * WiFiステータスフラグ 和名
     */
    public class WWiFiFlg {
        /**
         * WiFiステータスフラグON
         */
        public static final String ON = "ON";
        /**
         * WiFiステータスフラグOFF
         */
        public static final String OFF = "OFF";
        /**
         * 何もしない
         */
        public static final String RINGER_MODE_NONE = "何もしない";
    }

    public class Hyoka {
        public static final String COMPLETE =  "0";
        public static final String SHORT =  "30";
        public static final String LONG =  "300";
//        public static final String COMPLETE =  "0";
//        public static final String SHORT =  "-1";
//        public static final String LONG =  "-2";
    }
}
