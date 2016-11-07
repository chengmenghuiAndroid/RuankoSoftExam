/**
 *
 */
package com.itee.exam.core.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.net.SocketTimeoutException;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author moxin
 */
public abstract class JupiterAsyncTask<ResultT> extends SafeAsyncTask<ResultT> {

    private boolean showExceptionTip;

    private final Context context;

    private final Resources resources;

    public JupiterAsyncTask(Context context) {
        this.context = context;
        resources = context.getResources();
    }

    public Context getContext() {
        return context;
    }

    public Resources getResources() {
        return resources;
    }

    @Override
    protected void onThrowable(Throwable t) throws RuntimeException {
        super.onThrowable(t);
        if (showExceptionTip) {
            if (t instanceof RetrofitError) {
                RetrofitError cause = (RetrofitError) t;
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
                            Log.e("pkwsh",response.getReason());
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
            } else {
                showToastShort("操作失败");
            }
        }
    }

    public boolean isShowExceptionTip() {
        return showExceptionTip;
    }

    public void setShowExceptionTip(boolean showExceptionTip) {
        this.showExceptionTip = showExceptionTip;
    }

    public void showToastShort(String msg) {
        Toasts.showToastInfoShort(context.getApplicationContext(), msg);
    }

    public void showToastShort(int resId) {
        Toasts.showToastInfoShort(context.getApplicationContext(), resId);
    }

    public void showToastLong(String msg) {
        Toasts.showToastInfoLong(context.getApplicationContext(), msg);
    }

    public void showToastLong(int resId) {
        Toasts.showToastInfoLong(context.getApplicationContext(), resId);
    }

}