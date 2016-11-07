package com.itee.exam.app.ui.video;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.itee.exam.R;
import com.itee.exam.app.entity.User;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.app.ui.login.LoginActivity;
import com.itee.exam.app.ui.personal.SoftExamInfo;
import com.itee.exam.core.utils.JupiterAsyncTask;
import com.itee.exam.core.utils.Toasts;
import com.itee.exam.utils.PreferenceUtil;
import com.itee.exam.vo.HttpMessage;

/**
 * Created by pkwsh on 2016-04-05.
 */
public class VideoClassActivity extends BaseActivity {
    private TextView video1;
    private TextView video2;
    private TextView video3;
    private TextView video4;
    private TextView video5;
    private TextView video6;
    private Context context;
    private String userName;
    private String appToken;
    private String uid = null;
    private String title = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoclass);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        initView();
    }

    private void initView() {
        video1 = (TextView) findViewById(R.id.videoclass1);
        video2 = (TextView) findViewById(R.id.videoclass2);
        video3 = (TextView) findViewById(R.id.videoclass3);
        video4 = (TextView) findViewById(R.id.videoclass4);
        video5 = (TextView) findViewById(R.id.videoclass5);
        video6 = (TextView) findViewById(R.id.videoclass6);
        video1.setOnClickListener(listener);
        video2.setOnClickListener(listener);
        video3.setOnClickListener(listener);
        video4.setOnClickListener(listener);
        video5.setOnClickListener(listener);
        video6.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!PreferenceUtil.getInstance().getIsLogin()){
                Toasts.showToastInfoShort(context,"只有登录之后才能查看软考包过班详细内容");
                Intent intent = LoginActivity.generateIntent(context);
                startActivity(intent);
                return;
            }

            int id = v.getId();
            switch (id) {
                case R.id.videoclass1:
                    uid = video1.getTag().toString();
                    title = video1.getText().toString();
                    break;
                case R.id.videoclass2:
                    uid = video2.getTag().toString();
                    title = video2.getText().toString();
                    break;
                case R.id.videoclass3:
                    uid = video3.getTag().toString();
                    title = video3.getText().toString();
                    break;
                case R.id.videoclass4:
                    uid = video4.getTag().toString();
                    title = video4.getText().toString();
                    break;
                case R.id.videoclass5:
                    uid = video5.getTag().toString();
                    title = video5.getText().toString();
                    break;
                case R.id.videoclass6:
                    uid = video6.getTag().toString();
                    title = video6.getText().toString();
                    break;
                default:
                    break;
            }
            User user = PreferenceUtil.getInstance().getUser();
            userName = user.getUserName();
            appToken = PreferenceUtil.getInstance().getAppToken().getAppToken();
            new JupiterAsyncTask<HttpMessage>(context) {

                @Override
                protected void onPreExecute() throws Exception {
                    setShowExceptionTip(true);
                }

                @Override
                public HttpMessage call() throws Exception {
                    return getAppContext().getHttpService().getRuankoCourseLearnUrl(userName, appToken, uid);
                }

                @Override
                protected void onSuccess(HttpMessage executeResult) throws Exception {
                    if ("success".equals(executeResult.getResult())) {
                        String url = executeResult.getObject().toString();
                        Intent intent = new Intent(context, SoftExamInfo.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("URL", url);
                        bundle.putString("Title", title);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        showToastShort(executeResult.getMessageInfo());
                    }
                }

            }.execute();
        }
    };
}
