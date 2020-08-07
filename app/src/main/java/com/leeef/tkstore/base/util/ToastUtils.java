package com.leeef.tkstore.base.util;

import android.annotation.SuppressLint;
import android.widget.Toast;

import com.leeef.tkstore.base.BaseApplication;


public class ToastUtils {

    private static Toast toast;

    @SuppressLint("ShowToast")
    public static void show(String content) {
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.getContext(), content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }
}
