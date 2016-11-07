package com.itee.exam.app.ui.login;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import com.itee.exam.R;
import com.itee.exam.app.entity.User;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.app.ui.event.LoginEvent;
import com.itee.exam.app.ui.main.DemoActivity;
import com.itee.exam.core.utils.JupiterAsyncTask;
import com.itee.exam.core.utils.ProgressLoading;
import com.itee.exam.core.utils.StringUtils;
import com.itee.exam.utils.PreferenceUtil;
import com.itee.exam.vo.HttpMessage;
import com.itee.exam.vo.UserAppToken;

import de.greenrobot.event.EventBus;


/**
 * Created by Administrator on 2015/10/23.
 */
public class LoginManager {
    public static final String DEFAUL_LOGIN_METHOD = "APP";
    private BaseActivity context;
    ProgressLoading loginLoading;

    public LoginManager(BaseActivity context) {
        this.context = context;
        loginLoading = new ProgressLoading(context, context.getString(R.string.progress_msg_login));
    }

    public void login(final String userName, final String password) {
        if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password)) {
            loginLoading.show();
            new JupiterAsyncTask<HttpMessage<UserAppToken>>(context) {
                @Override
                public HttpMessage<UserAppToken> call() throws Exception {
                    HttpMessage<UserAppToken> result = context.getAppContext().getHttpService().login(userName, password);
                    return result;
                }

                @Override
                protected void onSuccess(HttpMessage<UserAppToken> executeResult) throws Exception {
                    String resultCode = executeResult.getResult();
                    if ("success".equals(resultCode)) {
                        UserAppToken token = executeResult.getObject();
                        PreferenceUtil preference = PreferenceUtil.getInstance();
                        preference.saveAppToken(token);
                        preference.setUserName(userName);
                        preference.setPassword(password);
                        saveLoginInfo(null, null);
                        getUserInfo();
                    } else if ("error-password".equals(resultCode)) {
                        showToastShort(R.string.toast_login_failure);
                        loginLoading.close();
                        Intent intent = LoginActivity.generateIntent(context);
                        context.startActivity(intent);
                    } else {
                        showToastShort(R.string.toast_login_no_user);
                        loginLoading.close();
                        Intent intent = LoginActivity.generateIntent(context);
                        context.startActivity(intent);
                    }
                }

                @Override
                protected void onException(Exception e) throws RuntimeException {
                    super.onException(e);
                    loginLoading.close();
                    PreferenceUtil.getInstance().setIsLogin(false);
                    EventBus.getDefault().post(new LoginEvent(0));
                    if (isLoginActivity()) {
                        showToastShort(R.string.toast_network_down);
                    } else {
                        Intent intent = new Intent(context, DemoActivity.class);
                        context.startActivity(intent);
//                        Intent intent = LoginActivity.generateIntent(context);
//                        getContext().startActivity(intent);
                        context.finish();
                    }
                }
            }.execute();
        }
    }

    private void getUserInfo() {
        new JupiterAsyncTask<HttpMessage<User>>(context) {
            @Override
            public HttpMessage<User> call() throws Exception {
                String userName = PreferenceUtil.getInstance().getUserName();
                String tokenContent = PreferenceUtil.getInstance().getTokenContent();
                return context.getAppContext().getHttpService().getUserInfo(userName, tokenContent);
            }

            @Override
            protected void onSuccess(HttpMessage<User> result) throws Exception {
                String resultCode = result.getResult();
                if ("success".equals(resultCode)) {
                    User user = result.getObject();
                    user.setPassword(null);
                    PreferenceUtil.getInstance().saveUser(user);
                    huanXinLogin(user);
                } else {
                    showToastShort(result.getMessageInfo());
                    loginLoading.close();
                    Intent intent = LoginActivity.generateIntent(context);
                    context.startActivity(intent);
                }
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                loginLoading.close();
                if (isLoginActivity()) {
                    showToastShort(R.string.toast_network_down);
                } else {
                    Intent intent = new Intent(context, DemoActivity.class);
                    context.startActivity(intent);
//                        Intent intent = LoginActivity.generateIntent(context);
//                        getContext().startActivity(intent);
                    context.finish();
                }
            }
        }.execute();

    }

    private boolean isLoginActivity() {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(LoginActivity.class.getName());

    }

    private void huanXinLogin(User user) {
        loginLoading.close();
        startDashboard();
    }

    private void saveLoginInfo(String appMethod, String openId) {
        if (appMethod == null || DEFAUL_LOGIN_METHOD.equals(appMethod)) {
            PreferenceUtil.getInstance().setLoginMethod(DEFAUL_LOGIN_METHOD);
        } else {
            PreferenceUtil.getInstance().setLoginMethod(appMethod);
            PreferenceUtil.getInstance().setThirdOpenId(openId);
        }
    }

    private void startDashboard() {
        PreferenceUtil.getInstance().setAutoLogin(true);
        PreferenceUtil.getInstance().setIsLogin(true);
        if (context.getClass() == LaunchActivity.class) {
            Intent intent = new Intent(context, DemoActivity.class);
            context.startActivity(intent);
        }
        EventBus.getDefault().post(new LoginEvent(1));
        context.finish();
    }
}
