package com.itee.exam.app.ui.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.itee.exam.R;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.core.utils.ProgressTask;
import com.itee.exam.core.utils.StringUtils;
import com.itee.exam.core.utils.ValidatorUtils;
import com.itee.exam.utils.PreferenceUtil;
import com.itee.exam.vo.HttpMessage;

import java.util.Timer;
import java.util.TimerTask;


/**
 * 忘记密码
 *
 * @author moxin
 */
public class ForgotPasswordActivity extends BaseActivity {
    private EditText etTel;
    private EditText etSecurityCode;
    private EditText etPassword;
    private EditText etRePassword;
    private TextView btnGetCode;
    private Button btnSubmit;

    private Timer timer;
    private int seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
    }

    private void initViews() {
        etTel = (EditText) findViewById(R.id.etTel);
        etSecurityCode = (EditText) findViewById(R.id.etSecurityCode);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRePassword = (EditText) findViewById(R.id.etRePassword);
        btnGetCode = (TextView) findViewById(R.id.btnGetCode);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String tel = etTel.getText().toString();
                if (StringUtils.isBlank(tel)) {
                    etTel.setError(getString(R.string.error_mobile_blank));
                    return;
                } else if (!ValidatorUtils.validMobile(tel)) {
                    etTel.setError(getString(R.string.error_mobile_incorrect));
                    return;
                }
                new ProgressTask<HttpMessage>(ForgotPasswordActivity.this, getString(R.string.progress_msg_send_security_code)) {

                    @Override
                    public HttpMessage call() throws Exception {
                        return getAppContext().getHttpService().findPasswordByMobile(tel);
                    }

                    @Override
                    protected void onSuccess(HttpMessage executeResult) throws Exception {
                        if ("success".equals(executeResult.getResult())) {
                            showToastShort(R.string.toast_security_code_send_success);
                            btnGetCode.setEnabled(false);
                            seconds = 60;

                            timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    Handler handler = new Handler(Looper.getMainLooper());
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            btnGetCode.setText(getString(R.string.btn_get_code_after_seconds_is, seconds--));
                                        }
                                    });
                                    if (seconds <= 0) {
                                        timer.cancel();
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                btnGetCode.setEnabled(true);
                                                btnGetCode.setText(R.string.btn_get_code);
                                            }
                                        });
                                    }
                                }
                            }, 0, 1000);
                        } else {
                            showToastShort(getString(R.string.security_code_send_error));
                        }
                    }
                }.execute();
            }
        });

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String newPassword = etPassword.getText().toString();
                    if (StringUtils.isBlank(newPassword)) {
                        etPassword.setError(getString(R.string.error_password_blank));
                    } else if (newPassword.length() < 6 || newPassword.length() > 12) {
                        etPassword.setError(getString(R.string.error_password_length));
                    }
                }
            }
        });
        etRePassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String newPassword = etPassword.getText().toString();
                    String password=etRePassword.getText().toString();
                    if (StringUtils.isBlank(password)) {
                        etRePassword.setError(getString(R.string.error_password_blank));
                    } else if (newPassword.length() < 6 || newPassword.length() > 12) {
                        etRePassword.setError(getString(R.string.error_password_length));
                    }else if(!password.equals(newPassword)){
                        etRePassword.setError(getString(R.string.error_password_diff));
                    }
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String tel = etTel.getText().toString();
                final String securityCode = etSecurityCode.getText().toString();
                final String newPassword = etPassword.getText().toString();

                boolean pass = true;
                if (StringUtils.isBlank(tel)) {
                    etTel.setError(getString(R.string.error_mobile_blank));
                    pass = false;
                }
                if (StringUtils.isBlank(securityCode)) {
                    etSecurityCode.setError(getString(R.string.error_security_code_blank));
                    pass = false;
                }
                if (StringUtils.isBlank(newPassword)) {
                    etPassword.setError(getString(R.string.error_password_blank));
                    pass = false;
                } else if (newPassword.length() < 6 || newPassword.length() > 12) {
                    etPassword.setError(getString(R.string.error_password_length));
                    pass = false;
                }

                if (pass) {
                    // 更新密码
                    new ProgressTask<HttpMessage>(ForgotPasswordActivity.this, getString(R.string.progress_msg_update_password)) {

                        @Override
                        public HttpMessage call() throws Exception {
//                            String userName = PreferenceUtil.getInstance().getUserName();
                            return getAppContext().getHttpService().findPasswordByPhoneNum(tel, securityCode, newPassword);
                        }

                        @Override
                        protected void onSuccess(HttpMessage executeResult) throws Exception {
                            if ("success".equals(executeResult.getResult())) {
                                showToastShort(R.string.toast_password_update_success);
                                finish();
                            } else {
                                showToastShort(executeResult.getMessageInfo());
                            }
                        }
                    }.execute();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (timer != null) {
            timer.cancel();
        }
    }
}
