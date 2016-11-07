package com.itee.exam.app.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.itee.exam.R;
import com.itee.exam.app.entity.Registration;
import com.itee.exam.app.entity.User;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.app.ui.login.LoginActivity;
import com.itee.exam.app.ui.vo.Apply;
import com.itee.exam.app.ui.vo.Order;
import com.itee.exam.core.utils.ProgressTask;
import com.itee.exam.core.utils.Toasts;
import com.itee.exam.utils.PreferenceUtil;
import com.itee.exam.vo.HttpMessage;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class SignInfoActivity extends BaseActivity {

    @BindView(R.id.btn_sign_up_info)
    ImageView btn_signUpInfo;
    @BindView(R.id.btn_sign_up)
    ImageView btn_signUp;

    private boolean isChick;
//    private boolean flag;
    private Order order;
    private Apply apply;
    private String THIS_LEFT = "SignInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        getAppContext().getHttpService().getApplyState(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String jsonString = new String(((TypedByteArray) response.getBody()).getBytes());
                Registration registration = new Gson().fromJson(jsonString, Registration.class);
                String blooeanStr = registration.getObject();
                isChick = Boolean.parseBoolean(blooeanStr);
                if(!isChick){
                    btn_signUp.setImageDrawable(getResources().getDrawable(R.drawable.register_btn_register_d));
                    Log.e(THIS_LEFT,"isChick:"+isChick);
                }
                Log.e(THIS_LEFT,"success:"+isChick);
            }

            @Override
            public void failure(RetrofitError error) {
                btn_signUp.setEnabled(false);
                btn_signUpInfo.setEnabled(false);
            }
        });

    }

    public void funSignUp(View view){
        if (isChick){
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        }else{
            btn_signUp.setEnabled(false);
            Toasts.showToastInfoShort(this, R.string.btn_sign_up_info);
        }
    }

    public void funSignUpInfo(View view){
//        if(isChick){
            if (PreferenceUtil.getInstance().getIsLogin()) {
                Intent intent = new Intent(this, SignUpInfoActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
//        }else{
//            btn_signUpInfo.setEnabled(false);
//            Toasts.showToastInfoShort(this, R.string.btn_sign_up_info);
//        }

    }

    public void funSignUpdateDetails(View view){

        if (PreferenceUtil.getInstance().getIsLogin()) {
            final User user = PreferenceUtil.getInstance().getUser();
            new ProgressTask<HttpMessage<Order>>(SignInfoActivity.this, "正在获取报考信息...") {
                @Override
                public HttpMessage<Order> call() throws Exception {
                    return getAppContext().getHttpService().getOrderinfo(user);
                }

                @Override
                protected void onSuccess(HttpMessage<Order> message) throws Exception {
                    if ("success".equals(message.getResult())) {
                        order = message.getObject();
                        apply = order.getApply();
                        Intent intent = new Intent(SignInfoActivity.this, SignUpDetailInfoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("apply", apply);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        Toasts.showToastInfoShort(SignInfoActivity.this, "获取报名信息失败!");
                    }
                    super.onSuccess(message);
                }
            }.execute();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

    }

}
