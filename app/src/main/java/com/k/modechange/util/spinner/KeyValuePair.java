package com.k.modechange.util.spinner;

import android.util.Pair;

/**
 * Created by USER on 2015/05/17.
 */
public class KeyValuePair extends Pair<String, String> {

    public KeyValuePair(String key, String value) {
        super(key, value);
    }

    public String getKey(){
        return super.first;
    }

    public String getValue(){
        return super.second;
    }
    @Override
    public String toString() {
        // ここでは単純にvalueを返しているが、ここをカスタマイズすれば表示を調整できる。
        return super.second;
    }
}
