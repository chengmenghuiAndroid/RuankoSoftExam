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
public class TelphoneDialogFragment extends DialogFragment {
    private EditText et_tel;

    public interface TelphoneInputListener {
        void onTelphoneInputComplete(String telphone);
    }

    private TelphoneInputListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String telphone = getArguments().getString("Telphone");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_telphone_dialog, null);
        et_tel = (EditText) view.findViewById(R.id.fragment_telphone);
        et_tel.setText(telphone);
        et_tel.setSelection(telphone.length());
        builder.setView(view).setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        donotExit(dialog,false);
                        String phone=et_tel.getText().toString();
                        if(!isPhone(phone)){
                            et_tel.setError("请输入正确的手机号");
                            return;
                        }
                        listener.onTelphoneInputComplete(phone);
                        donotExit(dialog,true);
                    }
                }).setNegativeButton("取消", null);
        return builder.create();
    }

    private boolean isPhone(String phone){
        String regex="0?(13|14|15|18|17)[0-9]{9}";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(phone);
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
    public void setOnTelphoneNameInputListener(TelphoneInputListener listener) {
        this.listener = listener;
    }
}
