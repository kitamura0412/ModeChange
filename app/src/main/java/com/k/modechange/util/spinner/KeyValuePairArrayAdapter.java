package com.k.modechange.util.spinner;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by USER on 2015/05/17.
 */
public class KeyValuePairArrayAdapter extends ArrayAdapter<KeyValuePair> {


    /**
     * @param context
     * @param textViewResourceId
     * @brief コンストラクタ
     */
    public KeyValuePairArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    /**
     * @param context
     * @param textViewResourceId
     * @param list
     * @brief コンストラクタ
     */
    public KeyValuePairArrayAdapter(Context context, int textViewResourceId, List<KeyValuePair> list) {
        super(context, textViewResourceId, list);
    }

    /**
     * @brief Spinerに表示するViewを取得します。
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setText(getItem(position).getValue());
        return view;

    }

    /**
     * @brief Spinerのドロップダウンアイテムに表示するViewを取得します。
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setText(getItem(position).getValue());
        return view;
    }
}
