package com.itee.exam.app.ui.personal.examhistory;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.itee.exam.R;
import com.itee.exam.app.AppContext;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.core.utils.ProgressTask;
import com.itee.exam.core.utils.StringUtils;
import com.itee.exam.core.utils.Toasts;
import com.itee.exam.utils.PreferenceUtil;
import com.itee.exam.vo.HttpMessage;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rkcoe on 2016/9/6.
 */
public class ChangPasswordActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_old_password)
    EditText oldPassword;
    @BindView(R.id.et_new_password)
    EditText newPassword;
    @BindView(R.id.et_set_new_password)
    EditText setPassword;
    @BindView(R.id.et_telephone)
    EditText etTel;
    @BindView(R.id.et_verification_code)
    EditText etVeriCode;
    @BindView(R.id.btn_verification_code)
    TextView btnVeriCode;

    private Timer timer;
    private int seconds;
    private String old_password;
    private String new_password;
    private String set_password;
    private String phoneNumber;
    private String securityCode;
    private boolean pass = true;

    public interface PasswordInputListener {
        void onPasswordInputComplete(String phoneNumber,String securityCode, String newPassword);
    }

    private PasswordInputListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chang_password);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initDate() {

        TelephonyManager phoneMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String line1Number = phoneMgr.getLine1Number();
        if (line1Number != null) {
            if (line1Number.length() > 11) {
                line1Number = line1Number.substring(line1Number.length() - 11);
            }
            etTel.setText(line1Number);
        }
        old_password = oldPassword.getText().toString();
        new_password = newPassword.getText().toString();
        set_password = setPassword.getText().toString();
        phoneNumber = etTel.getText().toString();
        securityCode = etVeriCode.getText().toString();
        if(StringUtils.isBlank(old_password)){
            oldPassword.setError("请输入旧密码");
            pass = false;
            return;
        }else if(StringUtils.isBlank(new_password)|| isPassword(new_password)){
            newPassword.setError("请输入新密码");
            pass = false;
            return;
        }else if(StringUtils.isBlank(set_password)){
            setPassword.setError("请输入新密码");
            pass = false;
            return;
        }else if(StringUtils.isBlank(phoneNumber)){
            etTel.setError("请输入手机号");
            pass = false;
            return;
        }else if(StringUtils.isBlank(securityCode)){
            etVeriCode.setError("请输入验证码");
            pass = false;
            return;
        }

        String old= PreferenceUtil.getInstance().getPassword();
        if(!old.equals(old_password)){
            oldPassword.setError("旧密码不正确");
            pass = false;
            return;
        }
        if (!new_password.equals(set_password)) {
            newPassword.setError("两次输入的新密码不一致");
            setPassword.setError("两次输入的新密码不一致");
            pass = false;
            return;
        }
        timer.cancel();
//        listener.onPasswordInputComplete(phoneNumber, securityCode, set_password);
    }

    private boolean isPassword(String psw) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(psw);
        if (m.matches()) {
            Toasts.showToastInfoLong(this, "输入的是汉字");
        }
        return m.matches();
    }


    public void setOnPasswordInputListener(PasswordInputListener listener) {
        this.listener = listener;
    }

    private boolean isPhone(String phone){
        String regex="0?(13|14|15|18|17)[0-9]{9}";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(phone);
        return matcher.matches();
    }

    public void onGetCodeClick() {
        final String tel = etTel.getText().toString();
        if (StringUtils.isBlank(tel)) {
            etTel.setError(getString(R.string.error_mobile_blank));
            return;
        }else if(!isPhone(tel)){
            etTel.setError("请输入正确格式的手机号");
            return;
        }
        if(!tel.equals(PreferenceUtil.getInstance().getUser().getPhoneNum())){
            etTel.setError("请输入注册或修改后手机号");
            return;
        }
        new ProgressTask<HttpMessage>(this, getString(R.string.progress_msg_send_security_code)) {

            @Override
            public HttpMessage call() throws Exception {
                return ((AppContext) getApplication()).getHttpService().findPasswordByMobile(tel);
            }

            @Override
            protected void onSuccess(HttpMessage executeResult) throws Exception {
                showToastShort(R.string.toast_security_code_send_success);

                btnVeriCode.setEnabled(false);
                seconds = 60;

                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                btnVeriCode.setText(getString(R.string.btn_get_code_after_seconds_is, seconds--));
                            }
                        });
                        if (seconds <= 0) {
                            timer.cancel();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    btnVeriCode.setEnabled(true);
                                    btnVeriCode.setText(R.string.btn_get_code);
                                }
                            });
                        }
                    }
                }, 0, 1000);
            }
        }.execute();
    }

    @OnClick(R.id.btn_cancel)
    void cancel(){
        finish();
    }

    @OnClick(R.id.btn_confirm)

    void confirm(){
        initDate();
        if(pass){
            final String name = PreferenceUtil.getInstance().getUserName();
            new ProgressTask<HttpMessage>(ChangPasswordActivity.this, "正在处理，请稍后...") {
                @Override
                public HttpMessage call() throws Exception {
                    Log.e("TAG", "HttpMessage");
                    return getAppContext().getHttpService().updateCustomerPasswordByMobile(name, phoneNumber, securityCode, new_password);
                }

                @Override
                protected void onSuccess(HttpMessage executeResult) throws Exception {
                    if ("success".equals(executeResult.getResult())) {
                        showToastShort("密码修改成功");
                        PreferenceUtil.getInstance().setPassword(new_password);
                        PreferenceUtil.getInstance().getUser().setPassword(new_password);
                        Log.e("TAG", "newPassword:"+new_password);
                        finish();
                    } else {
                        showToastShort(executeResult.getMessageInfo());
                    }
                }
            }.execute();

            Toast.makeText(this, "确定", Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.btn_verification_code)
    void verificationCode(){
        onGetCodeClick();
        Toast.makeText(this, "获取验证码", Toast.LENGTH_SHORT).show();
    }

}
