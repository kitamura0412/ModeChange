package com.k.modechange.util.spinner;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by USER on 2015/05/17.
 */
public class SpinnerUtil {

    /**
     * @return
     * @brief スピナーデータを取得します。
     */
    public static ArrayAdapter<KeyValuePair> getSpinnerData(Context context, int arrayId) {
        ArrayAdapter<KeyValuePair> list = new ArrayAdapter<KeyValuePair>(context, android.R.layout.simple_spinner_item);
        Resources res = context.getResources();
        TypedArray spinnerdata = res.obtainTypedArray(arrayId);
        for (int i = 0; i < spinnerdata.length(); ++i) {
            int id = spinnerdata.getResourceId(i, -1);
            if (id > -1) {
                String[] item = res.getStringArray(id);
                list.add(new KeyValuePair(item[0], item[1]));
            }
        }
        spinnerdata.recycle();
        return list;
    }

    /**
     * Keyを取得する
     *
     * @param spinner
     * @return Key
     */
    public static String getKey(Spinner spinner) {
        KeyValuePair idSpinnerTime1Form = (KeyValuePair) spinner.getSelectedItem();
        return idSpinnerTime1Form.getKey();
    }


    public static int keyToPosition(ArrayAdapter<KeyValuePair> arrayAdapter, String key) {
        for (int i = 0; i < arrayAdapter.getCount(); i++) {
            if (key.equals(arrayAdapter.getItem(i).getKey())) {
                return i;
            }
        }
        return -1;
    }

}
