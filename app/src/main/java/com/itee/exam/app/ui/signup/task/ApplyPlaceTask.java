package com.itee.exam.app.ui.signup.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.itee.exam.R;
import com.itee.exam.app.AppContext;
import com.itee.exam.app.ui.vo.ApplyPlace;
import com.itee.exam.app.ui.vo.Subject;
import com.itee.exam.vo.HttpMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pkwsh on 2016-08-02.
 */
public class ApplyPlaceTask extends AsyncTask<Void,Void,HttpMessage<List<ApplyPlace>>> {
    private Context context;
    private Spinner spinner;
    public ApplyPlaceTask(Context context, Spinner spinner){
        this.context=context;
        this.spinner=spinner;
    }

    @Override
    protected HttpMessage<List<ApplyPlace>> doInBackground(Void... params) {
        return AppContext.getInstance().getHttpService().registrationPlace();
    }

    @Override
    protected void onPostExecute(HttpMessage<List<ApplyPlace>> message) {
        String result=message.getResult();
        if("success".equals(result)){
            List<ApplyPlace> list= message.getObject();
            try{
                List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
                for(int i=0;i<list.size();i++){
                    ApplyPlace subject=list.get(i);
                    Map<String, Object> listem = new HashMap<String, Object>();
                    listem.put("placeId",String.valueOf(subject.getId()));
                    listem.put("placeName",subject.getApplyPlace());
                    listems.add(listem);
                }
                SimpleAdapter adapter=new SimpleAdapter(context,listems, R.layout.apply_place_item,
                        new String[]{"placeId","placeName"},
                        new int[]{R.id.tv_apply_place_id,R.id.tv_apply_place_name});
                spinner.setAdapter(adapter);
                spinner.setSelection(0);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
