package com.itee.exam.app.ui.login;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;

import com.itee.exam.R;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.app.ui.event.LoginEvent;
import com.itee.exam.core.utils.StringUtils;
import com.itee.exam.utils.PreferenceUtil;

import de.greenrobot.event.EventBus;

/**
 * 登录
 *
 * @author moxin
 */
public class LoginActivity extends BaseActivity {

    private EditText etUserName;
    private EditText etPassword;

    public static Intent generateIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        EventBus.getDefault().register(this);
    }

    private void initViews() {
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        PreferenceUtil preferenceUtil = PreferenceUtil.getInstance();
        if (preferenceUtil.getUser() != null) {
            String customerName = preferenceUtil.getUser().getUserName();
            if (customerName != null && customerName != "") {
                etUserName.setText(customerName);
            }

            String password = preferenceUtil.getUser().getPassword();
            if (password != null && password != "") {
                etPassword.setText(password);
            }

            if(customerName == null || customerName == ""){
                etUserName.setFocusable(true);
                etUserName.setFocusableInTouchMode(true);
                etUserName.requestFocus();
                etUserName.findFocus();
            }else if(password == null || password == ""){
                etPassword.setFocusable(true);
                etPassword.setFocusableInTouchMode(true);
                etPassword.requestFocus();
                etPassword.findFocus();
            }
        }
        if (!getAppContext().checkNetwork(true)) {
            dialog(this);
        }
    }

    protected void dialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage("您暂时未连接网络，请开启网络连接");
        builder.setTitle("提示");
        builder.setPositiveButton("马上开启",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                        Intent intent=null;
                        if(Build.VERSION.SDK_INT>10){
                            intent=new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        }else{
                            intent=new Intent();
                            ComponentName component=new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
                            intent.setComponent(component);
                            intent.setAction("android.intent.action.VIEW");
                        }
                        context.startActivity(intent);
                    }
                });
        builder.setNegativeButton("退出",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    public void onEventMainThread(LoginEvent event) {
        finish();
    }

    /**
     * 点击了“忘记密码”
     *
     * @param view
     */
    public void onForgetPasswordClick(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    /**
     * 点击了“注册按钮”
     *
     * @param view
     */
    public void onRegisterClick(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * 点击了“登录”
     *
     * @param view
     */
    public void onLoginClick(View view) {
        final String userName = etUserName.getText().toString();
        final String password = etPassword.getText().toString();

        boolean pass = true;
        if (StringUtils.isBlank(userName)) {
            etUserName.setError(getString(R.string.error_account));
            pass = false;
        }
        if (StringUtils.isBlank(password)) {
            etPassword.setError(getString(R.string.error_password));
            pass = false;
        }

        if (pass) {
            if (!getAppContext().checkNetwork(true)) {
                return;
            }
            new LoginManager(LoginActivity.this).login(userName,password);
        }
    }

    /**
     * 点击了“微信登录”
     *
     * @param view
     */
    public void onWechatClick(View view) {
//        thirdLoginManager.login(ThirdLoginManager.LOGIN_TYPE_WEIXIN);
    }

    /**
     * 点击了“微信登录”
     *
     * @param view
     */
    public void onQQClick(View view) {
//        thirdLoginManager.login(ThirdLoginManager.LOGIN_TYPE_QQ);
    }

    /**
     * 点击了“微博登录”
     *
     * @param view
     */
    public void onWeiboClick(View view) {
//        thirdLoginManager.login(ThirdLoginManager.LOGIN_TYPE_WEIBO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(thirdLoginManager != null){
//            thirdLoginManager.onActivityResult(requestCode, resultCode, data);
//        }
    }
}
