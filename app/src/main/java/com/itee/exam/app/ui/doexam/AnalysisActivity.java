package com.itee.exam.app.ui.doexam;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.itee.exam.R;
import com.itee.exam.app.ui.common.BaseActivity;

public class AnalysisActivity extends BaseActivity {
    private String url;
    private ProgressBar load;
    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        url=getIntent().getExtras().getString("URL");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    private void initView(){
        load = (ProgressBar) findViewById(R.id.webview_load_progress);
        load.setMax(100);
        webView = (WebView) findViewById(R.id.question_analysis);
        webView.setInitialScale(5);
        webView.setWebChromeClient(new WebViewClient());
        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("GB2312");
        //设置支持Javascript
        settings.setJavaScriptEnabled(false);
        //优先使用缓存
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 设置可以支持缩放
        settings.setSupportZoom(false);
        // 设置出现缩放工具
        settings.setBuiltInZoomControls(false);
        //扩大比例的缩放
        settings.setUseWideViewPort(true);
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        settings.setLoadWithOverviewMode(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new android.webkit.WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }

    private class WebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            load.setProgress(newProgress);
            if (newProgress == 100) {
                load.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }
    }
}
