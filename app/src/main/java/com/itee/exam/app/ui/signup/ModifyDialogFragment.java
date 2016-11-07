package com.itee.exam.app.ui.signup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.itee.exam.R;
import com.itee.exam.app.AppContext;
import com.itee.exam.app.ui.signup.task.ExamSubjectTask;
import com.itee.exam.app.ui.vo.Order;
import com.itee.exam.core.utils.ProgressTask;
import com.itee.exam.core.utils.Toasts;
import com.itee.exam.vo.HttpMessage;

import java.util.Map;

/**
 * Created by pkwsh on 2016-08-09.
 */
public class ModifyDialogFragment extends DialogFragment {
    private Order order;
    private int orderId;
    private Spinner spinner;

    public interface onChangedSubjectListener {
        void onChanged(Order order);
    }

    private onChangedSubjectListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        orderId=getArguments().getInt("orderId");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.modify_subject, null);
        spinner = (Spinner) view.findViewById(R.id.sp_exam_subject);
        Button ok = (Button) view.findViewById(R.id.btn_date_dialog_ok);
        Button cancel = (Button) view.findViewById(R.id.btn_date_dialog_cancel);
        builder.setCancelable(false);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Map<String, Object> subject = (Map<String, Object>) spinner.getAdapter().getItem(spinner.getSelectedItemPosition());
                new ProgressTask<HttpMessage<Order>>(getContext(), "正在更新报名科目，请稍后...") {
                    @Override
                    public HttpMessage<Order> call() throws Exception {
                        return AppContext.getInstance().getHttpService().modifySubject(orderId, Integer.valueOf(subject.get("subjectId").toString()));
                    }

                    @Override
                    protected void onSuccess(HttpMessage<Order> message) throws Exception {
                        String result = message.getResult();
                        if ("success".equals(result)) {
                            order = message.getObject();
                            listener.onChanged(order);
                            Toasts.showToastInfoShort(getContext(),"更新成功");
                        }else {
                            Toasts.showToastInfoShort(getContext(),"更新失败");
                        }
                        super.onSuccess(message);
                    }
                }.execute();

                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    @Override
    public void onResume() {
        ExamSubjectTask task = new ExamSubjectTask(getContext(), spinner);
        task.execute();
        super.onResume();
    }

    public void setOnChangedSubjectListener(onChangedSubjectListener listener) {
        this.listener = listener;
    }
}
