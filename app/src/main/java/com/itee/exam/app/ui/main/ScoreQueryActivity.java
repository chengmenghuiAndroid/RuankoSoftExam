package com.itee.exam.app.ui.main;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.itee.exam.R;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.core.utils.Toasts;

public class ScoreQueryActivity extends BaseActivity {
    private LinearLayout result;
    private Spinner spinner;
    private ArrayAdapter adapter;
    private static final String[] Date = {"2016年下半年", "2017年上半年"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_query);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinner = (Spinner) findViewById(R.id.exam_time);
        result=(LinearLayout)findViewById(R.id.score_result_info);
        result.setVisibility(View.GONE);
        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Date);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);

    }

    public void funSubmit(View view){
        Toasts.showToastInfoShort(this,"成绩尚未发布");
    }
}
