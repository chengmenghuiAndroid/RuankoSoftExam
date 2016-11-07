package com.itee.exam.app.ui.doexam;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itee.exam.R;
import com.itee.exam.core.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by pkwsh on 2016-08-22.
 */
public class AnswerDialogFragment extends DialogFragment {


    public interface onCompleteListener {
        void onCompleted(String answer);
    }

    private onCompleteListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.activity_afternoon_exam_answer, null);
        final int size = getArguments().getInt("SIZE");
        String answer = getArguments().getString("Answer");
        final int modle=getArguments().getInt("Modle");
        String result = getArguments().getString("Result");
        try {
            JSONArray array = null;
            if (StringUtils.isNotBlank(answer)) {
                array = new JSONArray(answer);
            }
            final LinearLayout layout = (LinearLayout) view.findViewById(R.id.answer_sheet_layout);
            for (int i = 0; i < size; i++) {
               View item = inflater.inflate(R.layout.answer_sheet_item, null);
                if("success".equals(result)){
                    item.findViewById(R.id.answer_item_content).setFocusable(false);
                }else{
                    item.findViewById(R.id.answer_item_content).setFocusable(true);
                }
                ((TextView) item.findViewById(R.id.answer_item_id)).setText("(" + String.valueOf(i + 1) + ")");
                if(1==modle){
                    ((TextView) item.findViewById(R.id.answer_item_content)).setEnabled(false);
                }
                if(null!=array) {
                    for (int j = 0; j < array.length(); j++) {
                        JSONObject object=array.optJSONObject(j);
                        if(String.valueOf(i+1).equals(object.getString("answerId"))){
                            ((TextView) item.findViewById(R.id.answer_item_content)).setText(object.getString("answerContent"));
                            break;
                        }
                    }
                }
                layout.addView(item, i);
            }
            TextView hide = (TextView) view.findViewById(R.id.btn_hide_dialog);
            hide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        JSONArray array = new JSONArray();
                        for (int i = 0; i < size; i++) {
                            JSONObject object = new JSONObject();
                            View child = layout.getChildAt(i);
                            String content = ((TextView) child.findViewById(R.id.answer_item_content)).getText().toString();
                            object.put("answerId", String.valueOf(i + 1));
                            object.put("answerContent", content);
                            array.put(object);
                        }
                        if(0==modle){
                            listener.onCompleted(array.toString());
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        if(0==modle){
                            listener.onCompleted("ERROR");
                        }
                    }
                    dismiss();
                    ;
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        builder.setView(view);
        return builder.create();
    }

    public void setOnCompleteListener(onCompleteListener listener) {
        this.listener = listener;
    }
}
