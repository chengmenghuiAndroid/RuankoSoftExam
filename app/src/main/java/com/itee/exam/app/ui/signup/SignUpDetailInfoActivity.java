package com.itee.exam.app.ui.signup;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.itee.exam.R;
import com.itee.exam.app.AppContext;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.app.ui.common.fragment.SlidingTabsFragment;
import com.itee.exam.app.ui.vo.Apply;
import com.itee.exam.core.utils.ProgressTask;
import com.itee.exam.core.utils.Toasts;
import com.itee.exam.vo.HttpMessage;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class SignUpDetailInfoActivity extends BaseActivity {
    private Apply apply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_detail_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        apply = (Apply) getIntent().getExtras().getSerializable("apply");
        AppContext.apply = apply;
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, new TabsFragment())
                .commit();
    }

    public void funSave(View view){
        new ProgressTask<HttpMessage>(this,"正在更新个人详细信息..."){
            @Override
            public HttpMessage call() throws Exception {
                return getAppContext().getHttpService().examSupplement(AppContext.apply);
            }

            @Override
            protected void onSuccess(HttpMessage httpMessage) throws Exception {
                if("success".equals(httpMessage.getResult())){
                    SignUpDetailInfoActivity.this.finish();
                }else if("fail".equals(httpMessage.getResult())) {
                    if("CertificateNumberIsRepeat".equals(httpMessage.getMessageInfo())){
                        Toasts.showToastInfoShort(SignUpDetailInfoActivity.this,"证件号码已经被使用");
                    }else {
                        Toasts.showToastInfoShort(SignUpDetailInfoActivity.this, "更新详细报名信息失败");
                    }
                }
                super.onSuccess(httpMessage);
            }
        }.execute();
    }

    public static class TabsFragment extends SlidingTabsFragment {
        @Override
        protected void populateTabs(FragmentPagerItems pages) {
            pages.add(FragmentPagerItem.of("第一步", Fragment1.class));
            pages.add(FragmentPagerItem.of("第二步", Fragment2.class));
            pages.add(FragmentPagerItem.of("第三步", Fragment3.class));
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            setTabBackgroundColor(getResources().getColor(R.color.black_cc));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
