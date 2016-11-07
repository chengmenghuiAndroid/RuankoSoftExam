package com.itee.exam.app.ui.personal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.itee.exam.R;

/**
 * Created by pkwsh on 2016-04-06.
 */
public class GroupDialogFragment extends DialogFragment {
    private static final String[] Name = {"学生", "在职"};
    private static final int[] Id = {2, 6};

    private Spinner spinner;
    private ArrayAdapter adapter;

    public interface GroupInputListener {
        void onGroupInputComplete(String groupName, String groupid);
    }

    private GroupInputListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String groupName = getArguments().getString("groupName");
        String groupId = getArguments().getString("groupId");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_group_dialog, null);
        spinner = (Spinner) view.findViewById(R.id.fragment_group);
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, Name);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);
        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
        //设置默认值
        spinner.setVisibility(View.VISIBLE);
        if (Integer.valueOf(groupId) == 2) {
            spinner.setSelection(0);
        } else if (Integer.valueOf(groupId) == 6) {
            spinner.setSelection(1);
        }
        builder.setView(view).setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onGroupInputComplete((String) spinner.getSelectedItem(), spinner.getTag().toString());
                    }
                }).setNegativeButton("取消", null);
        return builder.create();
    }

    public void setOnGroupInputListener(GroupInputListener listener) {
        this.listener = listener;
    }

    //使用数组形式操作
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
                                   long id) {
            spinner.setTag(Id[position]);
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
}
