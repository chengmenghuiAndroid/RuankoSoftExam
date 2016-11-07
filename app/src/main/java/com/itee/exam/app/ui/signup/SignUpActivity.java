package com.itee.exam.app.ui.signup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.itee.exam.R;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.app.ui.signup.task.ApplyPlaceTask;
import com.itee.exam.app.ui.signup.task.ExamSubjectTask;
import com.itee.exam.app.ui.vo.Apply;
import com.itee.exam.app.ui.vo.ApplyPlace;
import com.itee.exam.app.ui.vo.Order;
import com.itee.exam.app.ui.vo.Subject;
import com.itee.exam.core.utils.ProgressTask;
import com.itee.exam.core.utils.StringUtils;
import com.itee.exam.core.utils.Toasts;
import com.itee.exam.vo.HttpMessage;
import com.itee.exam.vo.meta.CodeType;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends BaseActivity {
    private int seconds;
    private Timer timer;
    private EditText phone;
    private EditText userName;
    private Button btnSecurityCode;
    private EditText securityCode;

    private int flag = 0;

    private Spinner examSubject;
    private Spinner applyPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        flag = 0;
        phone = (EditText) findViewById(R.id.et_sign_up_phone);
        btnSecurityCode = (Button) findViewById(R.id.btn_get_security_code);
        securityCode = (EditText) findViewById(R.id.et_security_code);
        userName = (EditText) findViewById(R.id.et_sign_up_realname);
        examSubject = (Spinner) findViewById(R.id.sp_exam_subject);
        applyPlace = (Spinner) findViewById(R.id.sp_apply_place);
    }

    public void onGetCodeClick(View view) {
        final String tel = phone.getText().toString();
        if (StringUtils.isBlank(tel)) {
            phone.setError(getString(R.string.error_mobile_blank));
            return;
        } else if (!isPhone(tel)) {
            phone.setError("请输入正确格式的手机号");
            return;
        }
        new ProgressTask<HttpMessage>(this, getString(R.string.progress_msg_send_security_code)) {

            @Override
            public HttpMessage call() throws Exception {
                return getAppContext().getHttpService().getSecurityCode(tel, CodeType.REGISTER);
            }

            @Override
            protected void onSuccess(HttpMessage executeResult) throws Exception {
                showToastShort(R.string.toast_security_code_send_success);

                btnSecurityCode.setEnabled(false);
                seconds = 60;

                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                btnSecurityCode.setText(getString(R.string.btn_get_code_after_seconds_is, seconds--));
                            }
                        });
                        if (seconds <= 0) {
                            timer.cancel();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    btnSecurityCode.setEnabled(true);
                                    btnSecurityCode.setText(R.string.btn_get_code);
                                }
                            });
                        }
                    }
                }, 0, 1000);
            }
        }.execute();
    }

    public void funNext(View view) {
        final String tel = phone.getText().toString();
        if (StringUtils.isBlank(tel)) {
            phone.setError(getString(R.string.error_mobile_blank));
            return;
        } else if (!isPhone(tel)) {
            phone.setError("请输入正确格式的手机号");
            return;
        }
        final String code = securityCode.getText().toString();
        if (StringUtils.isBlank(code)) {
            securityCode.setError("请输入验证码");
            return;
        }

        String name = userName.getText().toString();
        if (StringUtils.isBlank(name)) {
            userName.setError("请输入真实姓名");
            return;
        }
        Map<String, Object> subject = (Map<String, Object>) examSubject.getAdapter().getItem(examSubject.getSelectedItemPosition());
        Map<String, Object> place = (Map<String, Object>) applyPlace.getAdapter().getItem(applyPlace.getSelectedItemPosition());
        final Apply apply = new Apply();
        apply.setSubjectId(Integer.valueOf(subject.get("subjectId").toString()));
        apply.setSubjectName(subject.get("subjectName").toString());
        apply.setPhoneNumber(tel);
        apply.setRealName(name);
        apply.setApplyPlace(place.get("placeName").toString());
        new ProgressTask<HttpMessage<Order>>(this, "正在报名，请稍后...") {
            @Override
            public HttpMessage<Order> call() throws Exception {
                return getAppContext().getHttpService().examRegistration(apply, code);
            }

            @Override
            protected void onSuccess(final HttpMessage<Order> message) throws Exception {
                String result = message.getResult();
                String info = message.getMessageInfo();
                if ("success".equals(result)) {
                    if("newUser".equals(info)){
                        AlertDialog.Builder builder=new AlertDialog.Builder(SignUpActivity.this);
                        builder.setTitle("提示");
                        builder.setMessage("尊敬的用户，您在支付完成之后，可以用以下的用户名和密码登陆APP完善报名信息。" +
                                "\n用户名："+tel+"\n密码：111111\n登陆之后请修改您的密码。"); //设置内容
//                        builder.setIcon(R.drawable.logo);//设置图标，图片id即可
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss(); //关闭dialog
                                Order order = message.getObject();
                                Intent intent = new Intent(SignUpActivity.this, PayActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("order", order);
                                intent.putExtras(bundle);
                                startActivityForResult(intent, 200);
                            }
                        });
                        Dialog dialog=builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }else if("oldUser".equals(info)){
                        Order order = message.getObject();
                        Intent intent = new Intent(SignUpActivity.this, PayActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("order", order);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 200);
                    }
                }else if("fail".equals(result)){
                    if("validateCode".equals(info)){
                        Toasts.showToastInfoShort(SignUpActivity.this,"验证码错误");
                    }else if("isRepeat".equals(info)){
                        Toasts.showToastInfoShort(SignUpActivity.this,"该手机号已经报名,去报名情况里查看");
                        SignUpActivity.this.finish();
                    }
                }
                super.onSuccess(message);
            }
        }.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 200:
                finish();
                break;
            default:
                break;
        }
    }

    private boolean isPhone(String phone) {
        String regex = "0?(13|14|15|18|17)[0-9]{9}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    @Override
    protected void onResume() {
        if (0 == flag) {
            ExamSubjectTask task = new ExamSubjectTask(this, examSubject);
            task.execute();
            ApplyPlaceTask placeTask = new ApplyPlaceTask(this, applyPlace);
            placeTask.execute();
            flag = 1;
        }
        super.onResume();
    }
}
