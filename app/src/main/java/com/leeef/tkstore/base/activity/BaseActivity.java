package com.leeef.tkstore.base.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gyf.immersionbar.ImmersionBar;
import com.leeef.tkstore.base.BaseApplication;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by PC on 2016/9/8.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final int INVALID_VAL = -1;
    /**
     * 如果这个CompositeDisposable容器已经是处于dispose的状态，那么所有加进来的disposable都会被自动切断。防止内存泄漏
     */
    protected CompositeDisposable mDisposable;
    //ButterKnife
    private Toolbar mToolbar;

    private Unbinder unbinder;

    private LinearLayout back;

    private ImageView back_image;

    public Activity mActivity;

    /****************************abstract area*************************************/

    @LayoutRes
    protected abstract int getContentId();

    /************************init area************************************/
    protected void addDisposable(Disposable d) {
        if (mDisposable == null) {
            mDisposable = new CompositeDisposable();
        }
        mDisposable.add(d);
    }

    /**
     * 配置Toolbar
     *
     * @param toolbar
     */
    protected void setUpToolbar(Toolbar toolbar) {
    }

    protected void initData(Bundle savedInstanceState) {
    }

    /**
     * 初始化零件
     */
    protected void initWidget() {

    }

    /**
     * 初始化点击事件
     */
    protected void initClick() {
    }

    /**
     * 逻辑使用区
     */
    protected void processLogic() {
    }

    protected void initPresenter() {

    }

    /*************************lifecycle area*****************************************************/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentId());
        ImmersionBar.with(this)
//      .statusBarColor(R.color.white)
                .autoDarkModeEnable(true)//自动根据背景设置状态栏字体和图标颜色
                .statusBarDarkFont(true)
                .init();
        unbinder = ButterKnife.bind(this);

        WeakReference<BaseActivity> weakReference = new WeakReference<>(BaseActivity.this);
        mActivity = weakReference.get();
        initPresenter();
        initData(savedInstanceState);
        initBack();
        initWidget();
        initClick();
        processLogic();

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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
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
