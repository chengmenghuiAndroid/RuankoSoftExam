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

/**
 * Created by jack on 2016-03-22.
 */
public class RealNameDialogFragment extends DialogFragment {
    private EditText et_realName;

    public interface RealNameInputListener {
        void onRealNameInputComplete(String realName);
    }

    private RealNameInputListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String nickName = getArguments().getString("RealName");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_realname_dialog, null);
        et_realName = (EditText) view.findViewById(R.id.fragment_realname);
        et_realName.setText(nickName);
        et_realName.setSelection(nickName.length());
        builder.setView(view).setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onRealNameInputComplete(et_realName.getText().toString());
                    }
                }).setNegativeButton("取消", null);
        return builder.create();
    }

    public void setOnRealNameInputListener(RealNameInputListener listener) {
        this.listener = listener;
    }
}
