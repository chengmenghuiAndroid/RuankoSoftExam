package com.itee.exam.app.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.itee.exam.R;
import com.itee.exam.app.ui.common.BaseActivity;

import cn.jpush.android.api.JPushInterface;

public class MessageActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Intent intent = getIntent();
        TextView title=(TextView)findViewById(R.id.message_title);
        TextView content=(TextView)findViewById(R.id.message_content);
        if (null != intent) {
            Bundle bundle = getIntent().getExtras();
            String mTitle = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            String mContent = bundle.getString(JPushInterface.EXTRA_ALERT);
            title.setText(mTitle);
            content.setText(mContent);
        }
    }
}
