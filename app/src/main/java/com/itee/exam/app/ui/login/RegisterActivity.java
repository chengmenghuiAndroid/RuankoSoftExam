package com.itee.exam.app.ui.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.itee.exam.R;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.core.utils.ProgressTask;
import com.itee.exam.core.utils.StringUtils;
import com.itee.exam.core.utils.ValidatorUtils;
import com.itee.exam.utils.PreferenceUtil;
import com.itee.exam.vo.HttpMessage;
import com.itee.exam.vo.meta.CodeType;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 注册
 *
 * @author moxin
 */
public class RegisterActivity extends BaseActivity {

    private EditText etName;
    private EditText etTel;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordAgain;
    private Button btnSubmit;
    private TextView tvProtocol;

    private EditText etSecurityCode;
    TextView btnGetCode;
    private Timer timer;
    private int seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();

        TelephonyManager phoneMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String line1Number = phoneMgr.getLine1Number();
        if (line1Number != null) {
            if (line1Number.length() > 11) {
                line1Number = line1Number.substring(line1Number.length() - 11);
            }
            etTel.setText(line1Number);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (timer != null) {
            timer.cancel();
        }
    }

    private void initViews() {
        etName = (EditText) findViewById(R.id.etName);
        etTel = (EditText) findViewById(R.id.etTel);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPasswordAgain = (EditText) findViewById(R.id.etPasswordAgain);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        tvProtocol = (TextView) findViewById(R.id.tvProtocol);
        CheckBox cbProtocol = (CheckBox) findViewById(R.id.cbProtocol);
        cbProtocol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btnSubmit.setEnabled(isChecked);
            }
        });
//        btnSubmit.setEnabled(cbProtocol.isChecked());
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitClick();
            }
        });


        btnGetCode = (TextView) findViewById(R.id.btnGetCode);

        btnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGetCodeClick();
            }
        });

        etSecurityCode = (EditText) findViewById(R.id.etSecurityCode);

        etTel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String tel = etTel.getText().toString();
                    if (StringUtils.isBlank(tel)) {
                        etTel.setError(getString(R.string.error_mobile_blank));
                        return;
                    }else if(!isPhone(tel)){
                        etTel.setError("请输入正确的手机号");
                        return;
                    }
                }
            }
        });

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String password = etPassword.getText().toString();
                    if (StringUtils.isBlank(password)) {
                        etPassword.setError(getString(R.string.error_password_blank));
                    }else if (password.length() < 6 || password.length() > 12) {
                        etPassword.setError(getString(R.string.error_password_length));
                    }
                }
            }
        });

        etPasswordAgain.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
               if(!hasFocus){
                   String password = etPassword.getText().toString();
                   String passwordAgain = etPasswordAgain.getText().toString();
                   if (!password.equals(passwordAgain)) {
                       etPasswordAgain.setError(getString(R.string.error_password_diff));
                   }
               }
            }
        });
    }

    /**
     * 点击“获取验证码”
     */
    public void onGetCodeClick() {
        final String tel = etTel.getText().toString();
        if (StringUtils.isBlank(tel)) {
            etTel.setError(getString(R.string.error_mobile_blank));
            return;
        }else if(!isPhone(tel)){
            etTel.setError("请输入正确格式的手机号");
            return;
        }

        new ProgressTask<HttpMessage>(RegisterActivity.this, getString(R.string.progress_msg_send_security_code)) {

            @Override
            public HttpMessage call() throws Exception {
                return getAppContext().getHttpService().getSecurityCode(tel, CodeType.REGISTER);
            }

            @Override
            protected void onSuccess(HttpMessage executeResult) throws Exception {
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
            }
        }.execute();
    }

    private boolean isPhone(String phone){
        String regex="0?(13|14|15|18|17)[0-9]{9}";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(phone);
        return matcher.matches();
    }

    /**
     * 点击“提交”
     */
    public void onSubmitClick() {
//        final String name = etName.getText().toString();
        final String tel = etTel.getText().toString();
//        final String email = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        final String passwordAgain = etPasswordAgain.getText().toString();
        final String code = etSecurityCode.getText().toString();

        boolean pass = true;
//        if (StringUtils.isBlank(name)) {
//            etName.setError(getString(R.string.error_name_blank));
//            pass = false;
//        }
        if (StringUtils.isBlank(tel)) {
            etTel.setError(getString(R.string.error_mobile_blank));
            pass = false;
        }
        if (!ValidatorUtils.validMobile(tel)) {
            etTel.setError(getString(R.string.error_mobile_incorrect));
            pass = false;
        }
        if (StringUtils.isBlank(code)) {
            etSecurityCode.setError(getString(R.string.error_security_code_blank));
            pass = false;
        }
//        if (StringUtils.isBlank(email)) {
//            etEmail.setError(getString(R.string.error_email_blank));
//            pass = false;
//        }
        if (StringUtils.isBlank(password)) {
            etPassword.setError(getString(R.string.error_password_blank));
            pass = false;
        } else if (!password.equals(passwordAgain)) {
            etPasswordAgain.setError(getString(R.string.error_password_diff));
            pass = false;
        }else if (password.length() < 6 || password.length() > 12) {
            etPassword.setError(getString(R.string.error_password_length));
            pass = false;
        }

        if (pass) {
            if (!getAppContext().checkNetwork(true)) {
                return;
            }

            new ProgressTask<HttpMessage>(RegisterActivity.this, getString(R.string.progress_msg_register)) {

                @Override
                public HttpMessage call() throws Exception {
                    return getAppContext().getHttpService().registUser(tel, password, tel, code, "unknow");
                }

                @Override
                protected void onSuccess(HttpMessage executeResult) throws Exception {
                    if ("success".equals(executeResult.getResult())) {
                        showToastShort(getString(R.string.tip_register_success));
                        PreferenceUtil.getInstance().setUserName(tel);
                        PreferenceUtil.getInstance().setPassword(password);
                        finish();
                    } else {
//                        showToastShort(executeResult.getMessageInfo());
                        toLogin();
                    }
                }
            }.execute();
        }
    }

    protected void toLogin(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage(R.string.toast_mobile_registered);
        builder.setTitle("注册失败");
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            dialog.dismiss();
                            startActivity(LoginActivity.generateIntent(RegisterActivity.this));
                            finish();
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }
                });
        builder.create().show();
    }
}