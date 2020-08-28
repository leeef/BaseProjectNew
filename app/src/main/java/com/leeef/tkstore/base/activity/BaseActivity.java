package com.leeef.tkstore.base.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.gyf.immersionbar.ImmersionBar;
import com.leeef.tkstore.base.BaseApplication;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by PC on 2016/9/8.
 */
public abstract class BaseActivity<VB extends ViewBinding> extends AppCompatActivity {
    private static final int INVALID_VAL = -1;
    /**
     * 如果这个CompositeDisposable容器已经是处于dispose的状态，那么所有加进来的disposable都会被自动切断。防止内存泄漏
     */
    protected CompositeDisposable mDisposable;


    private LinearLayout back;

    private ImageView back_image;

    public Activity mActivity;
    public VB viewBind = null;

    /****************************abstract area*************************************/


    /************************init area************************************/
    protected void addDisposable(Disposable d) {
        if (mDisposable == null) {
            mDisposable = new CompositeDisposable();
        }
        mDisposable.add(d);
    }


    protected void initData(Bundle savedInstanceState) {
    }

    /**
     * 初始化点击事件
     */
    protected void initListener() {
    }


    /*************************lifecycle area*****************************************************/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
//      .statusBarColor(R.color.white)
                .autoDarkModeEnable(true)//自动根据背景设置状态栏字体和图标颜色
                .statusBarDarkFont(true)
                .init();
        EventBus.getDefault().register(this);
        try {
            //获取ViewBinding
            ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
            Class clazz = (Class) type.getActualTypeArguments()[0];
            Method method = clazz.getMethod("inflate", LayoutInflater.class);
            viewBind = (VB) method.invoke(null, getLayoutInflater());
            setContentView(viewBind.getRoot());
        } catch (Throwable e) {
            finish();
            return;
        }


        WeakReference<BaseActivity> weakReference = new WeakReference<>(BaseActivity.this);
        mActivity = weakReference.get();
        initData(savedInstanceState);
        initBack();
        initListener();

        // 添加Activity到堆栈
        BaseApplication.getInstance().addActivity(this);
    }


    private void initBack() {
//    back = findViewById(R.id.back);
//    if (back != null) {
//      back.setOnClickListener(v -> finish());
//    }
//    back_image = findViewById(R.id.back_image);
//    if (back_image != null) {
//
//      Drawable drawable = StringUtils.getDrawable(R.drawable.left).mutate();
//      drawable.setColorFilter(StringUtils.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//      back_image.setImageDrawable(drawable);
//    }
    }


    /**
     * 统一消息处理
     *
     * @param eventMsg
     */
    @Subscribe
    public void onEventMainThread(EventMsg eventMsg) {
        handleEventMsg(eventMsg);
    }

    public void handleEventMsg(EventMsg eventMsg) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    /**************************used method area*******************************************/

    protected void startActivity(Class<? extends AppCompatActivity> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }


}
