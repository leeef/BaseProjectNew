package com.leeef.tkstore.base.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.gyf.immersionbar.components.SimpleImmersionFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public abstract class BaseFragment<VB extends ViewBinding> extends SimpleImmersionFragment {

    public static final String TAG = BaseFragment.class.getSimpleName();
    public Context mContext;
    private boolean isViewCreated;
    private boolean isUIVisible;
    public boolean isVisibleToUser;
    private View rootView;
    public VB vb = null;

    /**
     * 如果这个CompositeDisposable容器已经是处于dispose的状态，那么所有加进来的disposable都会被自动切断。防止内存泄漏
     */
    protected CompositeDisposable mDisposable;


    protected void addDisposable(Disposable d) {
        if (mDisposable == null) {
            mDisposable = new CompositeDisposable();
        }
        mDisposable.add(d);
    }

    //setUserVisibleHint()在Fragment创建时会先被调用一次，传入isVisibleToUser = false
    //如果当前Fragment可见，那么setUserVisibleHint()会再次被调用一次，传入isVisibleToUser = true
    //如果Fragment从可见->不可见，那么setUserVisibleHint()也会被调用，传入isVisibleToUser = false
    //总结：setUserVisibleHint()除了Fragment的可见状态发生变化时会被回调外，在new Fragment()时也会被回调
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }

    private void lazyLoad() {
        if (isViewCreated && isUIVisible) {
            lazyLoadData();
            isViewCreated = false;
            isUIVisible = false;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
            Class clazz = (Class) type.getActualTypeArguments()[0];
            try {
                Method method = clazz.getMethod("inflate", LayoutInflater.class);
                vb = (VB) method.invoke(null, getLayoutInflater());
                rootView = vb.getRoot();
            } catch (Exception e) {
                e.printStackTrace();
            }
            initData();
            initListener();
        }
        return rootView;
    }

    public abstract void initData();

    public abstract void initListener();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //如果setUserVisibleHint()在rootView创建前调用时，那么
        //就等到rootView创建完后才回调onFragmentVisibleChange(true)
        //保证onFragmentVisibleChange()的回调发生在rootView创建完成之后，以便支持ui操作
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        lazyLoad();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        rootView = null;
        EventBus.getDefault().unregister(this);
        if (mDisposable != null) {
            mDisposable.dispose();
        }
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


    public abstract void lazyLoadData(); //需要懒加载的数据，重写此方法


}