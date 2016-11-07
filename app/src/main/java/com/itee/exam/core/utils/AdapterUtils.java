/**
 *
 */
package com.itee.exam.core.utils;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Adapter工具类
 *
 * @author moxin
 */
public final class AdapterUtils {

    private AdapterUtils() {
    }

    public static <T> ArrayAdapter<T> builderSpinnerAdapter(Context context, T[] items) {
        ArrayAdapter<T> adapter = new ArrayAdapter<T>(context, android.R.layout.simple_spinner_item,
                android.R.id.text1, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    public static <T> ArrayAdapter<T> builderSpinnerAdapter(Context context, List<T> items) {
        ArrayAdapter<T> adapter = new ArrayAdapter<T>(context, android.R.layout.simple_spinner_item,
                android.R.id.text1, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
}
