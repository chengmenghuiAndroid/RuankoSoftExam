package com.itee.exam.app.ui.video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.itee.exam.R;
import com.itee.exam.app.AppContext;
import com.itee.exam.app.ui.common.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rkcoe on 2016/9/8.
 */
public class VideoClassPassActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_video_class);
        setContentView(R.layout.activity_videoclass);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public AppContext getAppContext() {
        return super.getAppContext();
    }


}
