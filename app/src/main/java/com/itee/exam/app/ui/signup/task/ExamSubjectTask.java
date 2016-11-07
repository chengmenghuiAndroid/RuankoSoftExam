package com.itee.exam.app.ui.signup.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.itee.exam.R;
import com.itee.exam.app.AppContext;
import com.itee.exam.app.ui.vo.Subject;
import com.itee.exam.vo.HttpMessage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pkwsh on 2016-08-01.
 */
public class ExamSubjectTask extends AsyncTask<Void,Void,HttpMessage> {
    private Context context;
    private Spinner spinner;
    public ExamSubjectTask(Context context, Spinner spinner){
        this.context=context;
        this.spinner=spinner;
    }

    @Override
    protected HttpMessage doInBackground(Void... params) {
        return AppContext.getInstance().getHttpService().examSubjects();
    }

    @Override
    protected void onPostExecute(HttpMessage message) {
        String result=message.getResult();
        if("success".equals(result)){
            List<Subject> list=(List<Subject>) message.getObject();
            try{
                List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
                for(int i=0;i<list.size();i++){
                    Subject subject=list.get(i);
                    Map<String, Object> listem = new HashMap<String, Object>();
                    listem.put("subjectId",String.valueOf(subject.getSubjectId()));
                    listem.put("subjectName",subject.getSubjectName());
                    listem.put("subjectCharge",String.valueOf(subject.getSubjectCharge()));
                    listems.add(listem);
                }
                SimpleAdapter adapter=new SimpleAdapter(context,listems, R.layout.subject_item,
                        new String[]{"subjectId","subjectName","subjectCharge"},
                        new int[]{R.id.tv_subject_id,R.id.tv_subject_name,R.id.tv_subject_charge});
                spinner.setAdapter(adapter);
                spinner.setSelection(0);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
