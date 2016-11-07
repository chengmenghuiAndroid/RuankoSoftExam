package com.itee.exam.app.ui.personal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.itee.exam.R;
import com.itee.exam.app.entity.User;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.app.ui.login.LoginActivity;
import com.itee.exam.app.ui.personal.examhistory.ChangPasswordActivity;
import com.itee.exam.app.ui.personal.examhistory.ExamHistoryActivity;
import com.itee.exam.app.ui.personal.examhistory.OrderRecordActivity;
import com.itee.exam.app.ui.signup.SignUpDetailInfoActivity;
import com.itee.exam.app.ui.vo.Apply;
import com.itee.exam.app.ui.vo.Order;
import com.itee.exam.app.widget.ActionSheetDialog;
import com.itee.exam.core.utils.ProgressLoading;
import com.itee.exam.core.utils.ProgressTask;
import com.itee.exam.core.utils.Toasts;
import com.itee.exam.utils.PreferenceUtil;
import com.itee.exam.vo.HttpMessage;

public class PersonalInfoCenter extends BaseActivity {

    private Order order;
    private Apply apply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_center);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void funPersonalInfo(View view) {
        Intent intent = new Intent(this, PersonalInfo.class);
        startActivityForResult(intent, 0);
    }

    public void funPersonalSignInfo(View view) {
        if (PreferenceUtil.getInstance().getIsLogin()) {
            final User user = PreferenceUtil.getInstance().getUser();
            new ProgressTask<HttpMessage<Order>>(PersonalInfoCenter.this, "正在获取报考信息...") {
                @Override
                public HttpMessage<Order> call() throws Exception {
                    return getAppContext().getHttpService().getOrderinfo(user);
                }

                @Override
                protected void onSuccess(HttpMessage<Order> message) throws Exception {
                    if ("success".equals(message.getResult())) {
                        order = message.getObject();
                        apply = order.getApply();
                        Intent intent = new Intent(PersonalInfoCenter.this, SignUpDetailInfoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("apply", apply);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        Toasts.showToastInfoShort(PersonalInfoCenter.this, "获取报名信息失败!");
                    }
                    super.onSuccess(message);
                }
            }.execute();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    public void funExamHistory(View view) {
        Intent intent = new Intent(this, ExamHistoryActivity.class);
        startActivity(intent);
    }

    public void funDeleteExamHistory(View view) {

    }

    public void funOrderRecord(View view) {
        startActivity(new Intent(this, OrderRecordActivity.class));
    }

    public void funChangePassword(View view){
        startActivity(new Intent(this, ChangPasswordActivity.class));
//        final String name = PreferenceUtil.getInstance().getUserName();
//        PasswordDialogFragment dialogFragment = new PasswordDialogFragment();
//        dialogFragment.setOnPasswordInputListener(new PasswordDialogFragment.PasswordInputListener() {
//            @Override
//            public void onPasswordInputComplete(final String phoneNumber, final String securityCode, final String newPassword) {
//                new ProgressTask<HttpMessage>(PersonalInfoCenter.this, "正在处理，请稍后...") {
//
//                    @Override
//                    public HttpMessage call() throws Exception {
//                        return getAppContext().getHttpService().updateCustomerPasswordByMobile(name, phoneNumber, securityCode, newPassword);
//                    }
//
//                    @Override
//                    protected void onSuccess(HttpMessage executeResult) throws Exception {
//                        if ("success".equals(executeResult.getResult())) {
//                            showToastShort("密码修改成功");
//                            PreferenceUtil.getInstance().setPassword(newPassword);
//                            PreferenceUtil.getInstance().getUser().setPassword(newPassword);
//                            Log.e("TAG", "newPassword:"+newPassword);
//                            finish();
//                        } else {
//                            showToastShort(executeResult.getMessageInfo());
//                        }
//                    }
//                }.execute();
//            }
//        });
//        dialogFragment.show(getFragmentManager(), "PasswordDialog");
    }

    public void funDeleteCache(View view) {
        new ActionSheetDialog(PersonalInfoCenter.this)
                .builder()
                .setTitle("清除缓存之后，在软件运行时，可能会需要下载对应的数据，是否确认清除？")
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .addSheetItem("继续清除", ActionSheetDialog.SheetItemColor.Blue,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                ProgressLoading bar=new ProgressLoading(PersonalInfoCenter.this,"正在清除缓存，请稍后...");
                                bar.show();
                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(PersonalInfoCenter.this);
                                sharedPreferences.edit().putString("answerSheet", null).apply();
                                sharedPreferences.edit().putString("examPaper", null).apply();
                                sharedPreferences.edit().putBoolean("doExam", false).apply();
                                bar.close();
                                Toasts.showToastInfoShort(PersonalInfoCenter.this,"成功清除缓存");
                            }
                        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                finish();
                break;
            default:
                break;
        }
    }
}
