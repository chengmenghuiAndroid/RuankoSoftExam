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
public class NickNameDialogFragment extends DialogFragment {
    private EditText et_nickName;

    public interface NickNameInputListener {
        void onNickNameInputComplete(String nickName);
    }

    private NickNameInputListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String nickName = getArguments().getString("NickName");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_nickname_dialog, null);
        et_nickName = (EditText) view.findViewById(R.id.fragment_nickname);
        et_nickName.setText(nickName);
        et_nickName.setSelection(nickName.length());
        builder.setView(view).setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onNickNameInputComplete(et_nickName.getText().toString());
                    }
                }).setNegativeButton("取消", null);
        return builder.create();
    }

    public void setOnNickNameInputListener(NickNameInputListener listener) {
        this.listener = listener;
    }
}
