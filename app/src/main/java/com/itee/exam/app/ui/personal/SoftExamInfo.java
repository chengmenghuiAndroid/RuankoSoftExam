package com.itee.exam.app.ui.personal;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.itee.exam.R;
import com.itee.exam.app.ui.common.BaseActivity;

public class SoftExamInfo extends BaseActivity {
    private WebView webview;
    private String address;
    private String title;
    private ProgressBar load;
    private LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_exam_info);
        Bundle bundle = getIntent().getExtras();
        address = bundle.getString("URL");
        title = bundle.getString("Title");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        root = (LinearLayout) findViewById(R.id.root_layout);
        load = (ProgressBar) findViewById(R.id.pb);
        load.setMax(100);
        webview = (WebView) findViewById(R.id.soft_exam_info_webview);
        webview.setInitialScale(5);
        webview.setWebChromeClient(new WebViewClient());
        //支持javascript
        webview.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webview.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webview.getSettings().setBuiltInZoomControls(false);
        //扩大比例的缩放
        webview.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.getSettings().setLoadWithOverviewMode(true);
        int screenDensity = getResources().getDisplayMetrics().densityDpi;
        WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.FAR;
        switch (screenDensity) {
            case DisplayMetrics.DENSITY_LOW:
                zoomDensity = WebSettings.ZoomDensity.CLOSE;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                zoomDensity = WebSettings.ZoomDensity.MEDIUM;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                zoomDensity = WebSettings.ZoomDensity.FAR;
                break;
        }
        webview.getSettings().setDefaultZoom(zoomDensity);
        webview.loadUrl(address);
        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webview.setWebViewClient(new android.webkit.WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
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

    @Override
    protected void onPause() {
        super.onPause();
        toggleWebViewState(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        toggleWebViewState(false);
    }

    private void toggleWebViewState(boolean pause) {
        try {
            Class.forName("android.webkit.WebView")
                    .getMethod(pause
                            ? "onPause"
                            : "onResume", (Class[]) null)
                    .invoke(webview, (Object[]) null);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        root.removeView(webview);
        webview.destroy();
    }
}
