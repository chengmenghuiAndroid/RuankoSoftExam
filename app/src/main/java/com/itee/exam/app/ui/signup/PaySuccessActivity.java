package com.itee.exam.app.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.itee.exam.R;
import com.itee.exam.app.entity.User;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.app.ui.login.LoginActivity;
import com.itee.exam.app.ui.vo.Apply;
import com.itee.exam.utils.PreferenceUtil;

public class PaySuccessActivity extends BaseActivity {
    private Apply apply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        apply=(Apply)getIntent().getExtras().getSerializable("apply");
        Button go=(Button)findViewById(R.id.btn_goto_info);
        User user = PreferenceUtil.getInstance().getUser();
        if(apply!=null){
            if(!apply.getPhoneNumber().equals(user.getPhoneNum())){
                go.setVisibility(View.GONE);
            }
        }
    }

    public void funSudInfo(View view){
        if (PreferenceUtil.getInstance().getIsLogin()) {
            Intent intent=new Intent(this,SignUpDetailInfoActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("apply",apply);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

    }
}
