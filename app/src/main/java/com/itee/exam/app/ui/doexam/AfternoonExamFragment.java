package com.itee.exam.app.ui.doexam;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.itee.exam.R;
import com.itee.exam.app.AppContext;
import com.itee.exam.app.entity.QuestionQueryResultVO;
import com.itee.exam.app.ui.common.fragment.BaseFragment;
import com.itee.exam.core.utils.Toasts;

/**
 * Created by pkwsh on 2016-08-19.
 */
@SuppressLint("ValidFragment")
public class AfternoonExamFragment extends BaseFragment {
    private QuestionQueryResultVO question;
    private ProgressBar load;
    private WebView webView;

    public AfternoonExamFragment() {
        super();
    }

    public AfternoonExamFragment(QuestionQueryResultVO questionQueryResultVO) {
        question = questionQueryResultVO;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_afternoon_exam, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        load = (ProgressBar) view.findViewById(R.id.webview_load_progress);
        load.setMax(100);
        webView = (WebView) view.findViewById(R.id.question_web);
        webView.setInitialScale(5);
        webView.setWebChromeClient(new WebViewClient());
        final WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("GB2312");
        //设置支持Javascript
        settings.setJavaScriptEnabled(true);
        //载入js
        webView.addJavascriptInterface(new JavascriptInterface(getActivity()), "imagelistner");
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
        String path = question.getQuestionContent().getFileUrl();
        path = path.replace("\\", "/");
        path = AppContext.SERVER_IMG_URL + "/" + path;
        webView.loadUrl(path);
        webView.setWebViewClient(new android.webkit.WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                view.getSettings().setJavaScriptEnabled(true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.getSettings().setJavaScriptEnabled(true);
                // html加载完成之后，添加监听图片的点击js函数
                addImageClickListner();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                //出现页面错误的时候，不让webView显示了。同时跳出一个错误Toast
                webView.setVisibility(View.INVISIBLE);
                Toasts.showToastInfoShort(getAppContext(), "请检查您的网络设置");
                super.onReceivedError(view, request, error);
            }
        });
    }

    // 注入js函数监听
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }

    // js通信接口
    public class JavascriptInterface {

        private Context context;


        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void openImage(String img) {
            Log.e("AfternoonExamFragment", "JavascriptInterface");
            Intent intent = new Intent();
            intent.putExtra("image", img);
            intent.setClass(context, ShowWebImageActivity.class);
            context.startActivity(intent);
            Log.e("AfternoonExamFragment", "img:"+img);
        }
    }

    public QuestionQueryResultVO getQuestion() {
        return question;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
