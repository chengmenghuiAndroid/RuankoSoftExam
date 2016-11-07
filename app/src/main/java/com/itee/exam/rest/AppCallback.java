package com.itee.exam.rest;

import android.content.Context;
import android.util.Log;

import com.itee.exam.app.AppContext;

import java.net.SocketTimeoutException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by xin on 2015-06-11.
 */
public abstract class AppCallback<T> implements Callback<T> {

    private static final String TAG = AppCallback.class.getName();

    private final Context context;

    public AppCallback() {
        this(null);
    }

    public AppCallback(Context showFailTipCtx) {
        this.context = showFailTipCtx;
    }

    public void failure(RetrofitError cause) {
        Log.e(TAG, "请求出错", cause);
        if (context != null) {
            switch (cause.getKind()) {
                case NETWORK:
                    if (cause.getCause() instanceof SocketTimeoutException) {
                        //Socket Timeout
                        showToastShort("连接超时，请检查您的网络连接");
                    } else {
                        //No Connection
                        showToastShort("当前网络不可用，请检查您的网络连接");
                    }
                    break;

                case HTTP:
                    String toast = "HTTP异常";
                    Response response = cause.getResponse();
                    if (response != null) {
                        toast += "，状态码：" + response.getStatus();
                    }
                    showToastShort(toast);
                    break;

                case CONVERSION:
                    showToastShort("JSON转换异常");
                    break;

                default:
                    showToastShort("请求失败");
                    break;
            }
        }
    }

    public void showToastShort(String msg) {
        ((AppContext) context.getApplicationContext()).showToastShort(msg);
    }
}
