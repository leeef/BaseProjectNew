package com.leeef.tkstore.base.widget;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.viewbinding.ViewBinding;

import com.leeef.tkstore.base.util.ScreenUtils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;


public abstract class BaseDialog<VB extends ViewBinding> extends Dialog {
    public final String TAG = getClass().getSimpleName();

    protected final Activity mActivity;

    public VB viewBind;

    public BaseDialog(@NonNull Activity activity) {
        super(activity, android.R.style.Theme_Holo_Dialog);
        //去掉对话框的标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置无背景边框
        getWindow().setBackgroundDrawable(new ColorDrawable());
        setCancelable(true);
        mActivity = activity;
    }

    public BaseDialog(@NonNull Activity activity, @StyleRes int themeResId) {
        super(activity, themeResId);
        //去掉对话框的标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置无背景边框
        getWindow().setBackgroundDrawable(new ColorDrawable());
        setCancelable(true);
        mActivity = activity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Class clazz = (Class) type.getActualTypeArguments()[0];
        try {
            Method method = clazz.getMethod("inflate", LayoutInflater.class);
            viewBind = (VB) method.invoke(null, getLayoutInflater());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(viewBind.getRoot());
        initCreateData();
    }


    @Override
    public void show() {
        super.show();
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(getWidthHeight(getWindow().getAttributes()));
    }

    //默认dialog的宽设置,想更改可以重写此方法
    protected WindowManager.LayoutParams getWidthHeight(WindowManager.LayoutParams windowParams) {
        windowParams.width = ScreenUtils.getAppSize()[0] * 3 / 4;
        windowParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        return windowParams;
    }

    protected abstract void initCreateData();


}