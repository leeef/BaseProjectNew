package com.leeef.tkstore.base.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.leeef.tkstore.R;


/**
 * Created by LT on 2018/5/12.
 */
public class LoadingDialog extends Dialog {

  private static final String TAG = "LoadingDialog";

  private String mMessage;
  private int mImageId;
  private boolean mCancelable;
  private RotateAnimation mRotateAnimation;
  private ImageView iv_loading;

  public LoadingDialog(Context context, String mMessage) {
    this(context, R.style.LoadingDialog, mMessage, R.drawable.loading_dialog, false);
  }

  public LoadingDialog(Context context, String mMessage, boolean mCancelable) {
    this(context, R.style.LoadingDialog, mMessage, R.drawable.loading_dialog, mCancelable);
  }

  public LoadingDialog(@NonNull Context context, String message, int imageId) {
    this(context, R.style.LoadingDialog, message, imageId, false);
  }

  private LoadingDialog(@NonNull Context context, int themeResId, String message, int imageId, boolean cancelable) {
    super(context, themeResId);
    mMessage = message;
    mImageId = imageId;
    mCancelable = cancelable;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate: ");
    initView();
  }

  private void initView() {
    setContentView(R.layout.base_loading_dialog);
    // 设置窗口大小
    setCancelable(mCancelable);

    TextView tv_loading = findViewById(R.id.tv_loading);
    iv_loading = findViewById(R.id.iv_loading);
    tv_loading.setText(mMessage);
    iv_loading.measure(0, 0);
    mRotateAnimation = new RotateAnimation(0, 360, iv_loading.getMeasuredWidth() / 2, iv_loading.getMeasuredHeight() / 2);
    mRotateAnimation.setInterpolator(new LinearInterpolator());
    mRotateAnimation.setDuration(1000);
    mRotateAnimation.setRepeatCount(-1);
    iv_loading.startAnimation(mRotateAnimation);

  }

  @Override
  public void dismiss() {
    mRotateAnimation.cancel();
    super.dismiss();
  }

  @Override
  public void show() {
    if (iv_loading != null) {
      iv_loading.startAnimation(mRotateAnimation);
    }
    super.show();
  }

  @Override
  public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      // 屏蔽返回键
      return !mCancelable;
    }
    return super.onKeyDown(keyCode, event);
  }
}
