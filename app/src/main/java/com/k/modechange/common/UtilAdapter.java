package com.k.modechange.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.k.modechange.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author kentarho
 * @date 2013/05/12
 * <p/>
 * Adapterを管理するクラス
 */
public class UtilAdapter extends BaseAdapter {

    // クラス変数宣言
    /**
     * 呼び出し元クラスのContext
     */
    private Context cContext;
    /**
     * Adapterを管理するList
     */
    private ArrayList<HashMap<String, CharSequence>> aList;
    /**
     * 長押しした時のイベント
     */
    private OnClickListener ocl;

    /**
     * コンストラクタ
     *
     * @param cContext 呼び出し元クラスのContext
     * @param aList    Adapterを管理するList
     * @param ocl      タップした時のイベント
     */
    public UtilAdapter(Context cContext, ArrayList<HashMap<String, CharSequence>> aList, OnClickListener ocl) {
        super();

        // 各引数をクラス変数にsetする
        this.cContext = cContext;
        this.aList = aList;
        this.ocl = ocl;

    }

    @Override
    public int getCount() {
        return this.aList.size();
    }

    @Override
    public HashMap<String, CharSequence> getItem(int position) {
        return this.aList.get(position);
    }

    /**
     * @param position 置換要素のindex
     * @param map      置換要素
     */
    public void setItem(int position, HashMap<String, CharSequence> map) {
        this.aList.set(position, map);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * @param position
     * @param sKey
     * @return mapのvalue
     */
    public String getValue(int position, String sKey) {
        return this.aList.get(position).get(sKey).toString();
    }

    /**
     * データ保存用のViewHolder
     *
     * @author kitamura
     */
    static class ViewHolder {

        /**
         * 一行分のレイアウト
         */
        LinearLayout linearLayoutViewReg;
        /**
         * No
         */
        TextView textviewViewRegNo;
        /**
         * 登録名
         */
        TextView textviewViewRegName;
        /**
         * 住所
         */
        TextView textviewViewAddress;
        /**
         * 緯度
         */
        TextView textviewViewLatitude;
        /**
         * 経度
         */
        TextView textviewViewLongitude;
        /**
         * マナーモード
         */
        TextView textviewViewMannersMode;
        /**
         * 監視状態
         */
        TextView textviewViewChangeFlg;
        /**
         * Wi-Fi
         */
        TextView textviewViewWiFi;
        /**
         * 時間１From
         */
        TextView time1FormKey;
        /**
         * 時間１To
         */
        TextView time1ToKey;
        /**
         * 時間２From
         */
        TextView time2FormKey;
        /**
         * 時間２To
         */
        TextView time2ToKey;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewgroup) {

        // positionのアイテムを取得
        HashMap<String, CharSequence> mapItem = (HashMap<String, CharSequence>) getItem(position);

        ViewHolder holder;

        if (convertView == null) {

            // contextとレイアウトを結びつける
            LayoutInflater inflater = LayoutInflater.from(this.cContext);
            // レイアウトのフォーマットを取得
            convertView = inflater.inflate(R.layout.util_adapter, null);

            // データ保存用のViewHolderの生成
            holder = new ViewHolder();

            // レイアウトを生成する
            holder.linearLayoutViewReg = (LinearLayout) convertView.findViewById(R.id.id_linearLayout_viewReg);
            holder.textviewViewRegNo = (TextView) convertView.findViewById(R.id.id_textview_viewRegNo);
            holder.textviewViewRegName = (TextView) convertView.findViewById(R.id.id_textview_viewRegName);
            holder.textviewViewAddress = (TextView) convertView.findViewById(R.id.id_textview_viewAddress);
            holder.textviewViewLatitude = (TextView) convertView.findViewById(R.id.id_textview_viewLatitude);
            holder.textviewViewLongitude = (TextView) convertView.findViewById(R.id.id_textview_viewLongitude);
            holder.textviewViewMannersMode = (TextView) convertView.findViewById(R.id.id_textview_viewMannersMode);
            holder.textviewViewChangeFlg = (TextView) convertView.findViewById(R.id.id_textview_viewChangeFlg);
            holder.textviewViewWiFi = (TextView) convertView.findViewById(R.id.id_textview_viewWiFi);
            holder.time1FormKey = (TextView) convertView.findViewById(R.id.id_spinnerTime1_form);
            holder.time1ToKey = (TextView) convertView.findViewById(R.id.id_spinnerTime1_to);
            holder.time2FormKey = (TextView) convertView.findViewById(R.id.id_spinnerTime2_form);
            holder.time2ToKey = (TextView) convertView.findViewById(R.id.id_spinnerTime2_to);
            // tagにViewHolderを紐付ける
            convertView.setTag(holder);
        } else {
            // 既に生成されている場合
            holder = (ViewHolder) convertView.getTag();
        }

        holder.linearLayoutViewReg.setOnClickListener(this.ocl);
        holder.textviewViewRegNo.setText(mapItem.get(Const.ColumnKeyRegInfo.NO));
        holder.textviewViewRegName.setText(mapItem.get(Const.ColumnKeyRegInfo.REG_NAME));
        holder.textviewViewAddress.setText(mapItem.get(Const.ColumnKeyRegInfo.ADDRESS));
        holder.textviewViewLatitude.setText(mapItem.get(Const.ColumnKeyRegInfo.LATITUDE));
        holder.textviewViewLongitude.setText(mapItem.get(Const.ColumnKeyRegInfo.LONGITUDE));
        holder.textviewViewMannersMode.setText(mapItem.get(Const.ColumnKeyRegInfo.W_STATUS));
        holder.textviewViewMannersMode.setTag(mapItem.get(Const.ColumnKeyRegInfo.STATUS));
        holder.textviewViewChangeFlg.setText(mapItem.get(Const.ColumnKeyRegInfo.W_CHANGE_FLG));
        holder.textviewViewChangeFlg.setTag(mapItem.get(Const.ColumnKeyRegInfo.CHANGE_FLG));
        holder.textviewViewWiFi.setText(mapItem.get(Const.ColumnKeyRegInfo.W_WIFI));
        holder.textviewViewWiFi.setTag(mapItem.get(Const.ColumnKeyRegInfo.WIFI));
        holder.time1FormKey.setTag(mapItem.get(Const.ColumnKeyRegInfo.TIME1_FROM));
        holder.time1ToKey.setTag(mapItem.get(Const.ColumnKeyRegInfo.TIME1_TO));
        holder.time2FormKey.setTag(mapItem.get(Const.ColumnKeyRegInfo.TIME2_FROM));
        holder.time2ToKey.setTag(mapItem.get(Const.ColumnKeyRegInfo.TIME2_TO));
        // 画面表示
        return convertView;

    }
}
