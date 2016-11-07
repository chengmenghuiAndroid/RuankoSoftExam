package com.itee.exam.core.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;

/**
 * Created by xin on 2014/7/17 0017.
 */
public abstract class ProgressTask<T> extends JupiterAsyncTask<T> {

    private String title;
    private String message;
    private boolean indeterminate = true;
    private boolean showProgress = true;

    public interface OnSuccessListener<T> {
        void onSuccess(T t);
    }

    private OnSuccessListener<T> listener;

    private ProgressDialog progressDialog;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private boolean finish;

    public ProgressTask(Context context, String message) {
        super(context);
        setShowExceptionTip(true);
        this.message = message;
    }

    @Override
    protected void onPreExecute() throws Exception {
        finish = false;
        if (showProgress) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(true);
            if (!indeterminate) {
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            }
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (future != null) {
                        cancel(true);
                    }
                }
            });
        }
    }

    @Override
    protected void onSuccess(T t) throws Exception {
        if (listener != null) {
            listener.onSuccess(t);
        }
    }

    @Override
    protected void onFinally() throws RuntimeException {
        finish = true;
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    protected final void updateProgress(final int current, final int max) {
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

    protected final void updateTitle(final String title) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    progressDialog.setTitle(title);
                }
            }
        });
    }

    protected final void updateMessage(final String message) {
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

    public void setOnSuccessListener(OnSuccessListener<T> listener) {
        this.listener = listener;
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

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }

    public boolean isFinish() {
        return finish;
    }

}
