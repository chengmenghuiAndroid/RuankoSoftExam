package com.itee.exam.core.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;

/**
 * Created by xin on 2014/7/17 0017.
 */
public class ProgressLoading{

    private Context context;
    private String title;
    private String message;
    private boolean indeterminate = true;

    private ProgressDialog progressDialog;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private boolean finish;

    public ProgressLoading(Context context, String message) {
        this.context = context;
        this.message = message;
        progressDialog = new ProgressDialog(context);
    }

    public void show() {
        finish = false;
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(true);
        if (!indeterminate) {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        }
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        progressDialog.show();
    }

    public void close(){
        finish = true;
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void updateProgress(final int current, final int max) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    progressDialog.setProgress(current);
                    progressDialog.setMax(max);
                }
            }
        });
    }

    public void updateTitle(final String title) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    progressDialog.setTitle(title);
                }
            }
        });
    }

    public void updateMessage(final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    progressDialog.setMessage(message);
                }
            }
        });
    }

    protected final int getProgress() {
        return progressDialog.getProgress();
    }

    protected final int getMax() {
        return progressDialog.getMax();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate = indeterminate;
    }

    public boolean isFinish() {
        return finish;
    }

}
