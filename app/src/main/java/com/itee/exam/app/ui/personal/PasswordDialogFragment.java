package com.itee.exam.app.ui.personal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.itee.exam.R;
import com.itee.exam.app.AppContext;
import com.itee.exam.core.utils.ProgressTask;
import com.itee.exam.core.utils.StringUtils;
import com.itee.exam.utils.PreferenceUtil;
import com.itee.exam.vo.HttpMessage;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jack on 2016-03-22.
 */
public class PasswordDialogFragment extends DialogFragment {
    private EditText et_old_password;
    private EditText et_new_password;
    private EditText et_renew_password;
    private EditText et_phone;
    private EditText et_SecurityCode;
    private TextView btnGetCode;

    private Timer timer;
    private int seconds;

    public interface PasswordInputListener {
        void onPasswordInputComplete(String phoneNumber,String securityCode, String newPassword);
    }

    private PasswordInputListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_password_dialog, null);
        et_old_password = (EditText) view.findViewById(R.id.fragment_password_old);
        et_new_password = (EditText) view.findViewById(R.id.fragment_password_new);
        et_renew_password = (EditText) view.findViewById(R.id.fragment_password_renew);
        et_phone = (EditText) view.findViewById(R.id.fragment_phone_number);
        et_SecurityCode = (EditText) view.findViewById(R.id.etSecurityCode);
        btnGetCode = (TextView) view.findViewById(R.id.btnGetCode);
        TelephonyManager phoneMgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        String line1Number = phoneMgr.getLine1Number();
        if (line1Number != null) {
            if (line1Number.length() > 11) {
                line1Number = line1Number.substring(line1Number.length() - 11);
            }
            et_phone.setText(line1Number);
        }
        btnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGetCodeClick();
            }
        });
        builder.setView(view).setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        donotExit(dialog,false);
                        String oldPassword = et_old_password.getText().toString();
                        String newPassword = et_new_password.getText().toString();
                        String renewPassword = et_renew_password.getText().toString();
                        String phoneNumber=et_phone.getText().toString();
                        String securityCode=et_SecurityCode.getText().toString();
                        if(StringUtils.isBlank(oldPassword)){
                            et_old_password.setError("请输入旧密码");
                            return;
                        }else if(StringUtils.isBlank(newPassword) || isPassword(newPassword)){
                            et_new_password.setError("请输入新密码");
                            return;
                        }else if(StringUtils.isBlank(renewPassword)){
                            et_renew_password.setError("请输入新密码");
                            return;
                        }else if(StringUtils.isBlank(phoneNumber)){
                            et_phone.setError("请输入手机号");
                            return;
                        }else if(StringUtils.isBlank(securityCode)){
                            et_SecurityCode.setError("请输入验证码");
                            return;
                        }

                        String old= PreferenceUtil.getInstance().getPassword();
                        if(!old.equals(oldPassword)){
                            et_old_password.setError("旧密码不正确");
                            return;
                        }
                        if (!newPassword.equals(renewPassword)) {
                            et_new_password.setError("两次输入的新密码不一致");
                            et_renew_password.setError("两次输入的新密码不一致");
                            return;
                        }
                        timer.cancel();
                        listener.onPasswordInputComplete(phoneNumber, securityCode,newPassword);
                        donotExit(dialog,true);
                    }
                }).setNegativeButton("取消", null);
        return builder.create();
    }

    public void setOnPasswordInputListener(PasswordInputListener listener) {
        this.listener = listener;
    }

    private void donotExit(DialogInterface dialog,boolean flag) {
        try {
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            // 设置mShowing值，欺骗android系统
            field.set(dialog, flag);//如果为true则会推出
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private boolean isPhone(String phone){
        String regex="0?(13|14|15|18|17)[0-9]{9}";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(phone);
        return matcher.matches();
    }

    private boolean isPassword(String psw) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(psw);
        if (m.matches()) {
            Toast.makeText(getActivity(), "输入的是汉字", Toast.LENGTH_SHORT).show();

        }
        return m.matches();
    }

    public void onGetCodeClick() {
        final String tel = et_phone.getText().toString();
        if (StringUtils.isBlank(tel)) {
            et_phone.setError(getString(R.string.error_mobile_blank));
            return;
        }else if(!isPhone(tel)){
            et_phone.setError("请输入正确格式的手机号");
            return;
        }
        if(!tel.equals(PreferenceUtil.getInstance().getUser().getPhoneNum())){
            et_phone.setError("请输入注册或修改后手机号");
            return;
        }
        new ProgressTask<HttpMessage>(getActivity(), getString(R.string.progress_msg_send_security_code)) {

            @Override
            public HttpMessage call() throws Exception {
                return ((AppContext) getActivity().getApplication()).getHttpService().findPasswordByMobile(tel);
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
}
