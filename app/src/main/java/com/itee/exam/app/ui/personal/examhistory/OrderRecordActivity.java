package com.itee.exam.app.ui.personal.examhistory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.itee.exam.R;
import com.itee.exam.app.ui.common.BaseActivity;

import butterknife.ButterKnife;

/**
 * Created by rkcoe on 2016/9/7.
 */
public class OrderRecordActivity extends BaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
    }
}
