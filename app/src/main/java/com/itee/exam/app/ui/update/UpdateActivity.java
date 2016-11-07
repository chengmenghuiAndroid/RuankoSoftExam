package com.itee.exam.app.ui.update;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.itee.exam.R;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.core.utils.FileUtils;
import com.itee.exam.core.utils.Toasts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by pkwsh on 2016-04-05.
 */
public class UpdateActivity extends BaseActivity {
    public static final String DOWNLOAD_URL = "download_url";
    public static final String UPDATE_CONTENT = "update_content";
    private TextView tvProgress;
    //    private TextView tvStatus;
    private ProgressBar progressBar;
    private TextView tvUpdateContent;

    private static final int DOWNLOAD_PREPARE = 0;
    private static final int DOWNLOAD_WORK = 1;
    private static final int DOWNLOAD_OK = 2;
    private static final int DOWNLOAD_ERROR = 3;
    private static final int DOWNLOAD_TIMEOUT = 4;

    private static final int TIMEOUT = 30 * 1000;
    private static final String TAG = "IndexActivity";

    private boolean downloadSwitcher = false;
    /**
     * 下载的url
     */
    private String url = null;
    private String filePath;

    /**
     * 文件大小
     */
    int fileSize = 0;

    /**
     * 下载的大小
     */
    int downloadSize = 0;

    Thread downloadThread = null;

    public static Intent generateIntent(Context context, String url, String updateContent) {
        Intent intent = new Intent(context, UpdateActivity.class);
        intent.putExtra(DOWNLOAD_URL,url);
        intent.putExtra(UPDATE_CONTENT,updateContent);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updata);
        initView();

        Intent intent = getIntent();
        if (intent != null){
            url = intent.getStringExtra(DOWNLOAD_URL);
            filePath = FileUtils.getPath(UpdateActivity.this, url);
            tvUpdateContent.setText(intent.getStringExtra(UPDATE_CONTENT));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (url == null || filePath == null){
            Toasts.showToastFail(getBaseContext(),"版本更新地址为空", Toast.LENGTH_LONG);
            finish();
        }

//        tvStatus.setText("准备中...");
        downloadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                downloadSwitcher = true;
                downloadFile();
            }
        });
        downloadThread.start();
        handler.sendEmptyMessageDelayed(DOWNLOAD_TIMEOUT, TIMEOUT);
    }

    /**
     * handler
     */
    private Handler handler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOAD_PREPARE:
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    //tvStatus.setText("准备下载");
                    Log.d(TAG, "文件大小:" + fileSize);
                    progressBar.setMax(fileSize);
                    break;
                case DOWNLOAD_WORK:
                    Log.e(TAG, "已经下载:" + downloadSize);
                    progressBar.setProgress(downloadSize);
                    int res = downloadSize * 100 / fileSize;
                    tvProgress.setText("" + res + "%");
                    //tvStatus.setText("正在下载升级包");
                    break;
                case DOWNLOAD_OK:
                    //tvStatus.setText("下载完成");
                    installNewApk(new File(filePath)); //下载完后立即安装
                    finish();
                    break;
                case DOWNLOAD_ERROR:
                    downloadSwitcher = false;
                    downloadSize = 0;
                    Thread thread = downloadThread;
                    downloadThread = null;
                    thread.interrupt();
                    //tvStatus.setText("下载失败");
                    Toasts.showToastFail(getBaseContext(),"下载失败",Toast.LENGTH_LONG);
                    finish();
                    break;
                case DOWNLOAD_TIMEOUT:
                    downloadSwitcher = false;
                    downloadSize = 0;
                    Thread thread2 = downloadThread;
                    downloadThread = null;
                    thread2.interrupt();
                    Toasts.showToastFail(getBaseContext(),"下载超时",Toast.LENGTH_LONG);
                    finish();
                    break;
                default:
            }
            super.handleMessage(msg);
        }
    };

    private void initView() {
        tvProgress = (TextView) findViewById(R.id.tvProgress);
        //tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvUpdateContent = (TextView) findViewById(R.id.tvUpdateContent);
        progressBar = (ProgressBar) findViewById(R.id.pbDownload);
    }

    /**
     * 下载文件
     */
    private void downloadFile() {
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            InputStream is = conn.getInputStream();
            fileSize = conn.getContentLength();
            if (fileSize < 1 || is == null) {
                handler.removeMessages(DOWNLOAD_TIMEOUT);
                sendMessage(DOWNLOAD_ERROR);
            } else {
                handler.removeMessages(DOWNLOAD_TIMEOUT);
                sendMessage(DOWNLOAD_PREPARE);
                FileOutputStream fos = new FileOutputStream(filePath);
                byte[] bytes = new byte[1024];
                int len = -1;
                while ((len = is.read(bytes)) != -1 && downloadSwitcher) {
                    fos.write(bytes, 0, len);
                    fos.flush();
                    downloadSize += len;
                    sendMessage(DOWNLOAD_WORK);
                }
                sendMessage(DOWNLOAD_OK);
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            handler.removeMessages(DOWNLOAD_TIMEOUT);
            sendMessage(DOWNLOAD_ERROR);
            e.printStackTrace();
        }
    }

    /**
     * @param what
     */
    private void sendMessage(int what) {
        Message m = new Message();
        m.what = what;
        handler.sendMessage(m);
    }

    /***
     * 得到文件的路径
     *
     * @return
     */
    public String getFilePath() {
        return filePath;
    }

    protected void installNewApk(File file) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
