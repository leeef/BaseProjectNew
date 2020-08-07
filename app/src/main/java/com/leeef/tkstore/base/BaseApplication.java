package com.leeef.tkstore.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName: BaseApplication
 * @Description:
 * @Author: leeeeef
 * @CreateDate: 2019/9/21 14:38
 */
public class BaseApplication extends Application {


    private List<Activity> activityList = new LinkedList<>();
    public static BaseApplication instance;
    public static Context sInstance;

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            return new ClassicsFooter(context).setDrawableSize(20);
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        instance = this;
    }


    public static BaseApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return sInstance;
    }


    //添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public List<Activity> getActivityList() {
        return activityList;
    }

    //遍历所有Activity并finish
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        activityList.clear();
    }
}
