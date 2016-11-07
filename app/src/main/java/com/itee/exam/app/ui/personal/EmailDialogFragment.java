package com.itee.exam.app.ui.personal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.itee.exam.R;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jack on 2016-03-22.
 */
public class EmailDialogFragment extends DialogFragment {
    private EditText et_email;

    public interface EmailInputListener {
        void onEmailInputComplete(String email);
    }

    private EmailInputListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String email = getArguments().getString("Email");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_email_dialog, null);
        et_email = (EditText) view.findViewById(R.id.fragment_email);
        et_email.setText(email);
        et_email.setSelection(email.length());
        builder.setView(view).setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        donotExit(dialog,false);
                        String email=et_email.getText().toString();
                        if(!isEmail(email)){
                            et_email.setError("请输入正确格式的邮箱地址");
                            return;
                        }
                        listener.onEmailInputComplete(email);
                        donotExit(dialog,true);
                    }
                }).setNegativeButton("取消", null);
        return builder.create();
    }

    private boolean isEmail(String email){
        String regex="\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(email);
        return matcher.matches();
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

    public void setOnEmailInputListener(EmailInputListener listener) {
        this.listener = listener;
    }
}
