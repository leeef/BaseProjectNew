package com.leeef.tkstore;

import android.graphics.Paint;
import android.os.Bundle;

import com.gyf.immersionbar.ImmersionBar;
import com.leeef.tkstore.base.activity.BaseActivity;
import com.leeef.tkstore.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        ImmersionBar.with(mActivity).statusBarColor(R.color.red).init();
        viewBind.titleView.getTitle().getPaint().setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
    }
}