package com.k.modechange.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.k.modechange.R;
import com.k.modechange.common.CheckLogic;
import com.k.modechange.common.CommonUtil;
import com.k.modechange.common.Const;
import com.k.modechange.common.OutputLog;
import com.k.modechange.dto.DeliveryDto;

import java.util.List;
import java.util.Locale;

/**
 * 住所検索のActivityクラス
 *
 * @author k.kitamura
 * @date 2014/05/05
 */
public class MapSearchActivity extends FragmentActivity implements OnClickListener, OnMapLongClickListener,
        OnMyLocationButtonClickListener, OnMapClickListener, GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    // クラス定数宣言
    /**
     * 初期表示用 緯度
     */
    private final double FARST_LATITUDE = 35.685175;
    /**
     * 初期表示用 経度
     */
    private final double FARST_LONGITUDE = 139.752799;
    /**
     * 初期表示用 ズームレベル
     */
    private final int FARST_ZOOMLEVEL = 7;
    /**
     * 位置情報更新時 ズームレベル
     */
    private final int SECOND_ZOOMLEVEL = 15;

    // クラス変数宣言
    /**
     * ログ出力クラス
     */
    private OutputLog log = new OutputLog();
    /**
     * 呼び出し元のActivity
     */
    Activity thisActivity;
    /**
     * mapObject
     */
    private GoogleMap map;
    /**
     * 『住所』
     */
    private EditText editTextAddress;
    /**
     * 『検索』
     */
    private ImageView mageviewSearch;
    /**
     * 『現在住所』
     */
    private TextView textViewPresentAdr;
    /**
     * 『登録』
     */
    private LinearLayout linearLayoutRegAdr;
    /**
     * 画面項目を保持するクラス
     */
    private DeliveryDto deliveryDto;
    // 6/15 Google Play Servicesへの移行
    // /** ローケーション情報 */
    // private LocationManager locationManager;
    /**
     * 初期表示フラグ
     */
    private boolean farstView = false;
    /**
     * 登録フラグ(登録=true/更新=false)
     */
    private boolean regFlg = false;

    // 6/15 Google Play Servicesへの移行
    /**
     * 位置情報を取得するコネクション
     */
    private LocationClient locationClient;

    /**
     * 位置情報初期値取得フラグ
     */
    private boolean locationFarstFlg = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        try {
            log.logD("開始 : onCreate");

            thisActivity = this;

            // タイトルバー非表示
            requestWindowFeature(Window.FEATURE_NO_TITLE);

            setContentView(R.layout.mapsearch);

            // 画面項目の引継ぎ、又は初期化
            this.deliveryDto = getDeliveryDto();

            if (this.deliveryDto.no == null) {
                // 登録の場合true
                this.regFlg = true;
            }

            // 位置情報の設定
            if (this.regFlg) {
                // 登録の場合
                setLocation();
            }

            // 画面項目の設定
            setInit();

            // 画面項目の設定
            if (!this.regFlg) {
                // 更新の場合
                setUpdInit();
            }

            // 地図の設定
            if (this.regFlg) {
                // 登録の場合
                setUpMapIfNeeded(FARST_LATITUDE, FARST_LONGITUDE, FARST_ZOOMLEVEL);

            } else {
                // 更新の場合
                setUpMapIfNeeded(this.deliveryDto.latitude, this.deliveryDto.longitude, SECOND_ZOOMLEVEL);

                // アイコンの表示
                displayIcon(this.deliveryDto.latitude, this.deliveryDto.longitude);
            }

            // 位置情報取得チェック
            inputCheck();

            log.logD("終了 : onCreate");

        } catch (Exception e) {
            log.logE("予期せぬ例外が発生しました。", e);
            Toast.makeText(this, "予期せぬ例外が発生しました。", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(thisActivity, RegInfoListActivity.class);
            // 次の画面の戻るボタンでActivityを全て破棄して終了する。
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == this.mageviewSearch) {
            // 『検索』ボタンイベント
            log.logD("『検索』押下");
            // キーボードを非表示
            hideSoftInputFromWindow();

            if (this.editTextAddress == null || this.editTextAddress.getText() == null
                    || "".equals(this.editTextAddress.getText().toString())) {
                // 住所が入力されていない場合
                log.logD("住所が入力されていない");
                Toast.makeText(this, "住所を入力してください", Toast.LENGTH_LONG).show();
            } else {
                // 住所が入力されている場合
                // 『検索』 押下処理
                log.logD("住所取得 : " + this.editTextAddress.getText().toString());

                // 現在地の取得
                Address addr = getLocation();

                if (addr != null) {
                    deliveryDto.latitude = addr.getLatitude();
                    deliveryDto.longitude = addr.getLongitude();
                    String address = getAddress(addr.getLatitude(), addr.getLongitude());
                    deliveryDto.address = address;
                    this.textViewPresentAdr.setText(address);
                    // アイコンの表示
                    displayIcon(addr.getLatitude(), addr.getLongitude());
                    // 地図表示
                    setMapView(addr.getLatitude(), addr.getLongitude(), 0);
                }
            }
        } else if (v == this.linearLayoutRegAdr) {
            // 『登録』押下
            log.logD("『登録』押下");

            // 入力チェック
            if (!CheckLogic.checkLocation(this.deliveryDto.latitude, this.deliveryDto.longitude)) {
                // 位置情報が取得できていない場合
                Toast.makeText(this, "位置情報が取得できませんでした、\n再度取得しなおしてください。", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(thisActivity, RegActivity.class);
                intent.putExtra(DeliveryDto.NAME_DELIVERYDTO, this.deliveryDto);
                startActivity(intent);
            }
        }
    }

    // 6/15 Google Play Servicesへの移行
    // /**
    // * Locationが変更された時
    // * */
    // @Override
    // public void onLocationChanged(Location location) {
    //
    // log.logD(location.getLatitude() + " , " + location.getLongitude());
    //
    // // 位置情報を取得した時
    // locationChanged(location);
    //
    // }

    /**
     * Locationが変更された時
     *
     * @param location 変更時の位置情報
     */
    private void locationChanged(Location location) {

        log.logD(location.getLatitude() + " , " + location.getLongitude());

        this.deliveryDto.latitude = location.getLatitude();
        this.deliveryDto.longitude = location.getLongitude();
        this.deliveryDto.zoomlevel = SECOND_ZOOMLEVEL;

        // 『現在住所』の設定
        String address = getAddress(location.getLatitude(), location.getLongitude());
        deliveryDto.address = address;
        this.textViewPresentAdr.setText(address);

        if (!farstView) {
            // 初期表示
            // 地図再描画
            setMapView(location.getLatitude(), location.getLongitude(), SECOND_ZOOMLEVEL);

            farstView = true;
        }

    }

    // 6/15 Google Play Servicesへの移行
    // /**
    // * Statusが変更された時
    // * */
    // @Override
    // public void onStatusChanged(String provider, int status, Bundle extras) {
    // // 未使用
    // }
    //
    // /**
    // * LocationProviderが有効になった場合に呼び出される
    // * */
    // @Override
    // public void onProviderDisabled(String provider) {
    // // 未使用
    // }
    //
    // /**
    // * LocationProviderが無効になった場合に呼び出される
    // * */
    // @Override
    // public void onProviderEnabled(String provider) {
    // // 未使用
    // }

    /**
     * 地図を長押しした時に呼び出されるリスナー
     */
    @Override
    public void onMapLongClick(LatLng point) {
        this.deliveryDto.latitude = point.latitude;
        this.deliveryDto.longitude = point.longitude;
        // 住所の表示
        String address = getAddress(point.latitude, point.longitude);
        deliveryDto.address = address;
        this.textViewPresentAdr.setText(address);
        // this.textViewPresentAdr.setText(getAddress(point.latitude,
        // point.longitude));

        // アイコンの表示
        displayIcon(point.latitude, point.longitude);

        // カメラの移動（ズームレベル保持）
        setMapView(point.latitude, point.longitude, 0);

    }

    /**
     * 地図がクリックされた時呼び出されるリスナー
     */
    @Override
    public void onMapClick(LatLng arg0) {
        // キーボードを非表示
        hideSoftInputFromWindow();
    }


    // TODO 現在地表示　バグ改修

    /**
     * 現在地表示ボタンが押された時呼び出されるリスナー
     */
    @Override
    public boolean onMyLocationButtonClick() {
        // キーボードを非表示
//		hideSoftInputFromWindow();

        if (this.map != null) {

            Location location = this.map.getMyLocation();
            if (location != null) {
                this.map.stopAnimation();
                this.map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location
                        .getLongitude())));

                this.deliveryDto.latitude = location.getLatitude();
                this.deliveryDto.longitude = location.getLongitude();
                // 『現在住所』の設定
                String address = getAddress(location.getLatitude(), location.getLongitude());
                deliveryDto.address = address;
                this.textViewPresentAdr.setText(address);
            }else{
                Toast.makeText(this, "位置情報が取得できませんでした。", Toast.LENGTH_LONG).show();
            }
        }
        return false;
    }

    /**
     * 位置情報初期化
     */
    private void setLocation() {
        // 6/15 Google Play Servicesへの移行
        // // システムサービスのLOCATION_SERVICEからLocationManager objectを取得
        // locationManager = (LocationManager)
        // getSystemService(LOCATION_SERVICE);
        // // 位置情報取得
        // // [ver]Android2.3以降
        // locationManager.requestSingleUpdate(CommonUtil.getCriteria(), this,
        // this.getMainLooper());

        // 今回追加
        locationClient = new LocationClient(this, this, this);

        // ロケーション初期値取得フラグ
        locationFarstFlg = true;
    }

    /**
     * 画面項目の設定
     */
    private void setInit() {
        this.editTextAddress = (EditText) findViewById(R.id.id_editText_address);
        this.mageviewSearch = (ImageView) findViewById(R.id.id_imageview_search);
        this.mageviewSearch.setOnClickListener(this);
        this.textViewPresentAdr = (TextView) findViewById(R.id.id_textView_presentAdr);
        this.linearLayoutRegAdr = (LinearLayout) findViewById(R.id.id_linearLayout_regAdr);
        this.linearLayoutRegAdr.setOnClickListener(this);

    }

    /**
     * 画面項目の設定(更新の場合)
     */
    private void setUpdInit() {
        this.textViewPresentAdr.setText(this.deliveryDto.address);
    }

    /**
     * 地図の設定
     *
     * @param latitude  緯度
     * @param longitude 経度
     * @param zoomLevel ズームレベル
     */
    private void setUpMapIfNeeded(double latitude, double longitude, int zoomLevel) {
        // TODO
        // 地図の初期化
        this.map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.id_fragment_mapview))
                .getMap();
        // 長押し
        this.map.setOnMapLongClickListener(this);
        // クリック
        this.map.setOnMapClickListener(this);
        // 現在地表示ボタン
        this.map.setOnMyLocationButtonClickListener(this);

        // 現在地表示ボタン
        this.map.setMyLocationEnabled(true);
        // コンパスの有効化
        this.map.getUiSettings().setCompassEnabled(false);
        // 3D階層
        this.map.setBuildingsEnabled(false);
        // 交通
        this.map.setTrafficEnabled(false);

        // グーグル・マップのアンドロイドのAPIを初期化
        MapsInitializer.initialize(this);

        // 地図描画
        setMapView(latitude, longitude, zoomLevel);

    }

    /**
     * アイコンを表示
     *
     * @param latitude  緯度
     * @param longitude 経度
     */
    private void displayIcon(double latitude, double longitude) {
        this.map.clear();
        this.map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    }

    /**
     * キーボードを非表示にする
     */
    private void hideSoftInputFromWindow() {
        // キーボードを非表示にする
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // ボタンを押したときにソフトキーボードを閉じる
        inputMethodManager.hideSoftInputFromWindow(this.mageviewSearch.getWindowToken(), 0);
    }

    /**
     * googleMapから現在地の取得
     *
     * @return 取得した位置情報
     */
    private Address getLocation() {
        Geocoder gcoder = new Geocoder(this, Locale.getDefault());
        List<Address> lstAddr;
        // EditText name = (EditText) findViewById(R.id.editAddr);
        try {
            // 位置情報の取得
            lstAddr = gcoder.getFromLocationName(this.editTextAddress.getText().toString(), 1);
            if (lstAddr != null && lstAddr.size() > 0) {
                // 緯度/経度取得
                return lstAddr.get(0);
            }
        } catch (Exception e) {
            Toast.makeText(this, "検索機能は利用できません", Toast.LENGTH_LONG).show();
        }

        return null;
    }

    /**
     * 現在地から住所の取得
     *
     * @param latitude  緯度
     * @param longitude 経度
     * @return 住所
     */
    private String getAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.JAPAN);
        List<Address> list_address = null;
        String sAddress;
        try {
            list_address = geocoder.getFromLocation(latitude, longitude, 1);
            Address address = list_address.get(0);

            StringBuffer sbAddress = new StringBuffer();
            int addressLineMax = address.getMaxAddressLineIndex();
            for (int i = 0; i <= addressLineMax; i++) {
                sbAddress.append(address.getAddressLine(i));
            }

            // if (address.getCountryName() != null) {
            // sbAddress.append(address.getCountryName());
            // if (address.getAdminArea() != null) {
            // sbAddress.append(address.getAdminArea());
            // if (address.getLocality() != null) {
            // sbAddress.append(address.getLocality());
            // if (address.getSubLocality() != null) {
            // sbAddress.append(address.getSubLocality());
            // if (address.getThoroughfare() != null) {
            // sbAddress.append(address.getThoroughfare());
            // if (address.getSubThoroughfare() != null) {
            // sbAddress.append(address.getSubThoroughfare());
            // if (address.getFeatureName() != null) {
            // sbAddress.append("－");
            // sbAddress.append(address.getFeatureName());
            // }
            // }
            // }
            // }
            // }
            // }
            // }

            sAddress = sbAddress.toString();

            log.logD("住所取得 : " + sAddress);

        } catch (Exception e) {
            // 住所の取得に失敗
            return "";
        }
        return sAddress;
    }

    /**
     * DeliveryDtoを前の画面から引き継ぐ
     *
     * @return 前の画面から引き継いだDeliveryDto
     */
    public DeliveryDto getDeliveryDto() {
        // 画面項目を保持するクラスの初期化
        DeliveryDto deliveryDto = (DeliveryDto) getIntent().getSerializableExtra(DeliveryDto.NAME_DELIVERYDTO);
        if (deliveryDto == null) {
            deliveryDto = new DeliveryDto();
        }
        return deliveryDto;
    }

    /**
     * 地図再描画
     *
     * @param latitude  緯度
     * @param longitude 経度
     * @param zoomLevel ズームレベル
     */
    private void setMapView(double latitude, double longitude, int zoomLevel) {
        CameraUpdate cu;
        // パラメータの設定
        if (zoomLevel != 0) {
            // ズームレベル設定
            cu = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoomLevel);

        } else {
            // ズームレベル保持
            cu = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));

        }

        this.map.animateCamera(cu);
    }


    /**
     * 入力チェック
     */
    private void inputCheck() {

        if (!CheckLogic.checkLocationSettion(this)) {
            // 本体の設定「位置情報」が無効の場合

            String title = getResources().getString(R.string.alertdialog_title_002);
            String message = getResources().getString(R.string.alertdialog_message_003);
            String button001 = getResources().getString(R.string.alertdialog_button_005);
            String button002 = getResources().getString(R.string.alertdialog_button_004);

            AlertDialog alertDialog = CommonUtil.alertDialog(this, title, message, true, button001, button002,
                    startOcl2, null);

            // ダイアログを表示
            alertDialog.show();
        }
    }

    // 6/15 Google Play Servicesへの移行
    @Override
    protected void onStart() {
        super.onStart();
        // APIに接続
        if (this.regFlg) {
            // 登録の場合
            log.logD("onStart : APIに接続");
            locationClient.connect();
        }

    }

    // 6/15 Google Play Servicesへの移行
    @Override
    protected void onStop() {
        super.onStop();
        // APIから切断
        log.logD("onStop : APIから切断");
        if (this.regFlg) {
            // 登録の場合
            log.logD("onStart : APIに接続");
            locationClient.disconnect();
        }
    }

    // 6/15 Google Play Servicesへの移行
    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        log.logD("onConnectionFailed : 接続失敗");
    }

    // 6/15 Google Play Servicesへの移行
    @Override
    public void onConnected(Bundle arg0) {
        // 接続開始
        log.logD("onConnected : 接続");

        if (locationFarstFlg) {
            Location location = locationClient.getLastLocation();

            if (location == null) {
                log.logD("location == null ");
            } else {
                log.logD(location.getLatitude() + " , " + location.getLongitude());
                // 位置情報を取得した時
                locationChanged(location);

                locationFarstFlg = false;
            }
        }
    }

    // 6/15 Google Play Servicesへの移行
    @Override
    public void onDisconnected() {
        log.logD("onDisconnected : 切断");
    }

    /**
     * 位置情報設定画面に遷移
     */
    DialogInterface.OnClickListener startOcl2 = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // GPS設定画面へ遷移
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                    Const.ForResult.FORRESULT_LOCATION_SETTINGS);
        }
    };

    /**
     * 別画面から戻るボタン押下時
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        log.logD("onActivityResult");
        if (requestCode == Const.ForResult.FORRESULT_LOCATION_SETTINGS) {
            // 位置情報設定画面からの戻り
            inputCheck();
        }
    }

}
