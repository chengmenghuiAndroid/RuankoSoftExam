package com.itee.exam.app.ui.doexam;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itee.exam.R;
import com.itee.exam.app.AppContext;
import com.itee.exam.app.entity.QuestionAnswer;
import com.itee.exam.app.ui.common.fragment.BaseFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by rkcoe on 2016/9/21.
 */
public class PmExamFragment extends BaseFragment {

    @BindView(R.id.question_web)
    WebView questionWeb;
    @BindView(R.id.answerContent)
    TextView tvAnswerContent;
    @BindView(R.id.answerTitle)
    TextView tvAnswerTitle;
    private ProgressBar load;
    private WebView webView;
    private QuestionAnswer questionAnswers;
    private Unbinder bind;
    private static final String THIS_LEFT = "PmExamFragment";
    List<String> list = new ArrayList<>();



    public PmExamFragment() {
        super();
    }

//    public PmExamFragment(QuestionAnswer questionAnswer) {
//        questionAnswers = questionAnswer;
//    }


    public static PmExamFragment newInstance(QuestionAnswer questionAnswer) {
        PmExamFragment newFragment = new PmExamFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("QuestionAnswer", questionAnswer);
        newFragment.setArguments(bundle);
        return newFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pm_exam, container, false);
        bind = ButterKnife.bind(this, view);
        try {
            initViews(view);
            getUrl();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private String  getUrl() throws Exception {
        String answerAnalysis = questionAnswers.getAnswerAnalysis();
        Iterator iterator = jsonToObject(answerAnalysis).entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry= (Map.Entry<String, String>) iterator.next();
            Log.e(THIS_LEFT, "key = " + entry.getKey() + "....." + "value = " + entry.getValue());
            if (entry.getKey().equals("fileUrl")) {
                list.add(entry.getValue());
            }
        }
        Log.e(THIS_LEFT, "list >>>>:"+list.get(0));
        Log.e(THIS_LEFT, "answerAnalysis >>>>"+answerAnalysis);
        return list.get(0);
    }

    private void initViews(View view) throws Exception {
        tvAnswerTitle.setText(questionAnswers.getAnswerTitle());
        tvAnswerContent.setText(questionAnswers.getAnswerContent());
        load = (ProgressBar) view.findViewById(R.id.webview_load_progress);
        load.setMax(100);
        webView = (WebView) view.findViewById(R.id.question_web);
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
//        String path = question.getQuestionContent().getFileUrl();
        String path = getUrl();
        path = path.replace("\\", "/");
        path = AppContext.SERVER_IMG_URL + "/" + path;
        Log.e(THIS_LEFT, "list >>>>:"+ AppContext.SERVER_IMG_URL + "/" + path);
        webView.loadUrl(path);
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



    /**
     * jsonToMap
     *
     * @param jsonStr
     * @return
     * @throws Exception
     */

    public Map jsonToObject(String jsonStr) throws Exception {
        JSONObject jsonObj = new JSONObject(jsonStr);
        Iterator<String> nameItr = jsonObj.keys();
        String name;
        Map<String, String> outMap = new HashMap<>();
        while (nameItr.hasNext()) {
            name = nameItr.next();
            outMap.put(name, jsonObj.getString(name));
        }
        return outMap;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }
}
