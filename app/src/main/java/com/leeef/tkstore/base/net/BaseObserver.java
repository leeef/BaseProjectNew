package com.leeef.tkstore.base.net;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.leeef.tkstore.base.BaseApplication;
import com.leeef.tkstore.base.util.LoadingUtil;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

/**
 * @Description: 处理网络请求返回的结果
 * @Author: leeeef
 * @CreateDate: 2019/5/20 13:57
 */
public abstract class BaseObserver<T> extends DisposableObserver<T> {
    /**
     * 解析数据失败
     */
    private static final int PARSE_ERROR = 1001;
    /**
     * 网络问题
     */
    private static final int BAD_NETWORK = 1002;
    /**
     * 连接错误
     */
    private static final int CONNECT_ERROR = 1003;
    /**
     * 连接超时
     */
    private static final int CONNECT_TIMEOUT = 1004;

    private boolean showLoad = true;


    protected BaseObserver() {
    }

    protected BaseObserver(boolean showLoad) {
        this.showLoad = showLoad;
    }


    @Override
    protected void onStart() {
        //请求前的加载框
        if (showLoad) {
            LoadingUtil.show(BaseApplication.getContext());
        }
    }


    @Override
    public void onNext(T o) {
        //处理请求成功的数据
        try {
            if (o instanceof BaseResponse) {
                BaseResponse baseResponse = (BaseResponse) o;
                if (baseResponse.getCode() == 200) {
                    onSuccess(o);
                } else {
                    onError(baseResponse.getMsg());
                }
            } else {
                onError("服务器异常");
            }

        } catch (Exception e) {
            e.printStackTrace();
            onError("服务器异常");
        } finally {
            if (showLoad) {
                LoadingUtil.hide();
            }
        }


    }

    @Override
    public void onError(Throwable e) {
        if (showLoad) {
            LoadingUtil.hide();
        }
        if (e instanceof HttpException) {
            //   HTTP错误
            onException(BAD_NETWORK);
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {
            //   连接错误
            onException(CONNECT_ERROR);
        } else if (e instanceof InterruptedIOException) {
            //  连接超时
            onException(CONNECT_TIMEOUT);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            //  解析错误
            onException(PARSE_ERROR);
        } else {
            if (e != null) {
                onError(e.toString());
            } else {
                onError("服务器异常");
            }
        }

    }

    private void onException(int unknownError) {
        onError("服务器异常");
//        switch (unknownError) {
//            case CONNECT_ERROR:
//                onError("服务器异常");
//                break;
//
//            case CONNECT_TIMEOUT:
//                onError("服务器异常");
//                break;
//
//            case BAD_NETWORK:
//                onError("服务器异常");
//                break;
//
//            case PARSE_ERROR:
//                onError("服务器异常");
//                break;
//
//            default:
//                break;
//        }
    }


    @Override
    public void onComplete() {
        if (showLoad) {
            LoadingUtil.hide();
        }

    }


    public abstract void onSuccess(T o);

    public abstract void onError(String msg);
}