package com.k.modechange.activity;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.k.modechange.R;
import com.k.modechange.common.OutputLog;
import com.k.modechange.receiver.ModeSetReceiver;
import com.k.modechange.service.SendBroadcastService;

import java.util.Calendar;

/**
 * 登録情報一覧のMainActivity
 *
 * @author k.kitamura
 * @date 2014/05/05
 */
public class MainActivity extends BaseActivity implements OnClickListener {

    /**
     * ログ出力クラス
     */
    OutputLog log = new OutputLog();

    /**
     * 地図表示ボタン
     */
    Button buttonMapView;

    Button temp1;
    Button temp2;
    Button temp4;
    Button temp5;
    Button temp6;
    Button temp7;
    Button temp8;
    Button temp9;
    Button temp10;
    Button temp11;
    Button temp12;
    Button temp13;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        log.logD("開始 : onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setScreen(R.string.text_textView_screenName_main);

        // this.buttonMapView = (Button) findViewById(R.id.id_button_mapView);
        // this.buttonMapView.setOnClickListener(this);

        // this.temp1 = setButton(R.id.temp1);
        // this.temp2 = setButton(R.id.temp2);
        // this.temp4 = setButton(R.id.temp4);
        // this.temp5 = setButton(R.id.temp5);
        // this.temp6 = setButton(R.id.temp6);
        // this.temp7 = setButton(R.id.temp7);
        // this.temp8 = setButton(R.id.temp8);
        // this.temp9 = setButton(R.id.temp9);
        // this.temp10 = setButton(R.id.temp10);
        this.temp11 = setButton(R.id.temp11);
        this.temp12 = setButton(R.id.temp12);
        this.temp13 = setButton(R.id.temp13);
        log.logD("終了 : onCreate");
    }

    @Override
    public void onClick(View v) {

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if (v == this.buttonMapView) {
            // 地図表示ボタン押下時
            log.logD("地図表示ボタン押下時");
            Intent intent = new Intent(getApplication(), MapSearchActivity.class);
            startActivity(intent);

        } else if (v == this.temp1) {
            // ショートカット作成ボタン押下
            log.logD("ショートカット作成ボタン押下");
            // ブラウザでWebページを開くためのIntentを作成
            Intent targetIntent = new Intent(Intent.ACTION_VIEW);
            targetIntent.setClassName(this, "com.k.modechange.activity.MainActivity");

            // ショートカットのタイトルは "WebShortCut"、
            // アイコンにはあらかじめ作成しておいた tips_icon.png を使用。
            makeShortCut(this, targetIntent, "WebShortCut", R.drawable.ic_launcher);

        } else if (v == this.temp2) {
            // 通常マナーに変更
            log.logD("通常マナーに変更");
            Toast.makeText(this, "通常マナーに変更", Toast.LENGTH_LONG).show();
            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

            // } else if (v == this.temp3) {
            // // ドライブマナーに変更
            // log.logD("ドライブマナーに変更");
            // Toast.makeText(this, "ドライブマナーに変更", Toast.LENGTH_LONG).show();

        } else if (v == this.temp4) {
            // サイレントマナーに変更
            log.logD("サイレントマナーに変更");
            Toast.makeText(this, "サイレントマナーに変更", Toast.LENGTH_LONG).show();
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            // ドライブモード
        } else if (v == this.temp5) {
            // マナーモードOFF
            log.logD("マナーモードOFF");
            Toast.makeText(this, "マナーモードOFF", Toast.LENGTH_LONG).show();
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        } else if (v == this.temp6) {
            // ユーザが変更したモードを取得
            log.logD("ユーザが変更したモードを取得");
            Toast.makeText(this, "ユーザが変更したモードを取得" + audioManager.getRingerMode(), Toast.LENGTH_LONG).show();

        } else if (v == this.temp7) {
            // マナーモード変更を検知（サービス開始）
            log.logD("マナーモード変更を検知（サービス開始）");
            Toast.makeText(this, "マナーモード変更を検知（サービス開始）", Toast.LENGTH_LONG).show();

            // サービス開始
            startService(new Intent(MainActivity.this, SendBroadcastService.class));

        } else if (v == this.temp8) {
            // マナーモード変更を検知（サービス開始）
            log.logD("マナーモード変更を検知（サービス終了）");
            Toast.makeText(this, "マナーモード変更を検知（サービス終了）", Toast.LENGTH_LONG).show();

            stopService(new Intent(MainActivity.this, SendBroadcastService.class));

        } else if (v == this.temp9) {
            startService(new Intent(this, SendBroadcastService.class));
            log.logT(this, "サービスを開始しました");
        } else if (v == this.temp10) {
            stopService(new Intent(this, SendBroadcastService.class));
            log.logT(this, "サービスを終了しました");
        } else if (v == this.temp11) {
            log.logD("時間設定");

            Intent intent = new Intent(MainActivity.this, ModeSetReceiver.class);
            PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.SECOND, 20);
            getCalendarLog(calendar);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            // one shot
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);

        } else if (v == this.temp12) {
            // ボタンクリックでレシーバーセット
            Intent intent = new Intent(this, ModeSetReceiver.class);
            PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

            // アラームマネージャの用意（初回は5秒後,そのあとは20秒毎に実行）
            long firstTime = SystemClock.elapsedRealtime();
            firstTime += 5 * 1000;
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 20 * 1000, sender);
        } else if (v == this.temp13) {
            // ステータスバー
            //			Notification.Builder builder = new Notification.Builder(this);
            //			builder.setTicker("ticker");
            //			builder.setContentTitle("title");
            //			builder.setContentText("text");
            //			Notification notification = builder.getNotification();
            //
            //			NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            //			manager.notify(0, notification);

            //
            //			Notification noti = new Notification.Builder(this)
            //					.setContentTitle("New mail from ")
            //					.setContentText("test")
            //					.setSmallIcon(R.drawable.common_signin_btn_icon_dark)
            //					.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.common_signin_btn_icon_dark))
            //					.build();

            //			//通知内容を決定
            //			Notification notification = Notification.Builder(this);
            //
            //			//PendingIntentはタイミングを指定したインテント
            //			//今回はユーザーがnotificationを選択した時にActivityを起動
            //			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
            //					new Intent(getApplicationContext(), MainActivity.class), 0);
            //
            //			//notificationを設定
            //			notification.setLatestEventInfo(getApplicationContext(), "NotificationService",
            //					"text", contentIntent);
            //			NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //			 mNotificationManager.notify(0, notification);

            NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext());

//			if(Build.VERSION.SDK_INT >= 14){
//				// API レベル19以上の場合
            Intent notificationIntent = new Intent(this, MainActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//
//				Notification.Builder notification = new Notification.Builder(this);
            notification.setContentTitle("マナーモードチェンジャー");
            notification.setContentText(".位置情報の設定がOFFです。位置情報が取得できませんでした。");
            notification.setContentIntent(pendingIntent);
            notification.setSmallIcon(R.drawable.ic_launcher);
            notification.setAutoCancel(true);

            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            nm.notify(1000, notification.build());
//			}


            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());

        }
    }

    private void getCalendarLog(Calendar calendar) {
        log.logD("lFirstTime : " + calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/"
                + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) + "'"
                + calendar.get(Calendar.MILLISECOND));

    }

    /**
     * Buttonを生成してリスナーをONにします。
     *
     * @param id
     * @return Button
     */
    private Button setButton(int id) {
        Button button = (Button) findViewById(id);
        button.setOnClickListener(this);
        return button;
    }

    /**
     * ショートカット作成
     *
     * @param con
     * @param targetIntent
     * @param title
     * @param iconResource
     */
    private void makeShortCut(Context con, Intent targetIntent, String title, int iconResource) {
        // ショートカット作成依頼のためのIntent
        Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

        // ショートカットのタップ時に起動するIntentを指定
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, targetIntent);

        // ショートカットのアイコンと、タイトルを指定
        Parcelable icon = Intent.ShortcutIconResource.fromContext(con, iconResource);
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);

        // BroadCast をつかって、システムにショートカット作成を依頼する
        con.sendBroadcast(intent);

        Toast.makeText(this, "ショートカットを作成しました。", Toast.LENGTH_LONG).show();
    }
}
