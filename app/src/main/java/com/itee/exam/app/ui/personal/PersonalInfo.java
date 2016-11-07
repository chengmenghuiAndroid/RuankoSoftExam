package com.itee.exam.app.ui.personal;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.itee.exam.R;
import com.itee.exam.app.entity.User;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.app.ui.event.LoginEvent;
import com.itee.exam.core.utils.JupiterAsyncTask;
import com.itee.exam.core.utils.Toasts;
import com.itee.exam.utils.PreferenceUtil;
import com.itee.exam.vo.HttpMessage;

import de.greenrobot.event.EventBus;


public class PersonalInfo extends BaseActivity {
    private static int EDIT_PERSONAL_INFO = 0;
    private View arrow_nickname;
    private View arrow_realname;
    private View arrow_tel;
    private View arrow_email;
    private View arrow_group;
    private View arrow_password;
    private TextView nickName;
    private TextView realName;
    private TextView telphone;
    private TextView email;
    private TextView group;
    private String appToken;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        loadData();
    }

    private void initView() {
        arrow_nickname = findViewById(R.id.personal_list_item_arrow_nickname);
        arrow_realname = findViewById(R.id.personal_list_item_arrow_realname);
        arrow_tel = findViewById(R.id.personal_list_item_arrow_tel);
        arrow_email = findViewById(R.id.personal_list_item_arrow_email);
        arrow_group = findViewById(R.id.personal_list_item_arrow_group);
        arrow_password = findViewById(R.id.personal_list_item_arrow_password);
        nickName = (TextView) findViewById(R.id.value_personal_info_nickname);
        realName = (TextView) findViewById(R.id.value_personal_info_realname);
        telphone = (TextView) findViewById(R.id.value_personal_info_telphone);
        email = (TextView) findViewById(R.id.value_personal_info_email);
        group = (TextView) findViewById(R.id.value_personal_info_group);
    }

    private void loadData() {
        user = PreferenceUtil.getInstance().getUser();
        nickName.setText(user.getUserName());
        realName.setText(user.getTrueName());
        telphone.setText(user.getPhoneNum());
        email.setText(user.getEmail());
        group.setText(user.getGroupName());
        group.setTag(user.getGroupId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_personal_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit_personal) {
            if (0 == EDIT_PERSONAL_INFO) {
                EDIT_PERSONAL_INFO = 1;
                setEditStatus();
                item.setTitle("完成");
            } else if (1 == EDIT_PERSONAL_INFO) {
                EDIT_PERSONAL_INFO = 0;
                savePersonalInfo();
                setReadOnlyStatus();
                item.setTitle("编辑");
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setEditStatus() {
        arrow_realname.setVisibility(View.VISIBLE);
        arrow_tel.setVisibility(View.VISIBLE);
        arrow_email.setVisibility(View.VISIBLE);
        arrow_group.setVisibility(View.VISIBLE);
    }

    private void setReadOnlyStatus() {
        arrow_realname.setVisibility(View.GONE);
        arrow_tel.setVisibility(View.GONE);
        arrow_email.setVisibility(View.GONE);
        arrow_group.setVisibility(View.GONE);
    }

    private void savePersonalInfo() {
        user.setTrueName(realName.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPhoneNum(telphone.getText().toString());
        final int groupId = Integer.valueOf(group.getTag().toString());
        user.setGroupId(groupId);
        user.setGroupName(group.getText().toString());
        appToken = PreferenceUtil.getInstance().getAppToken().getAppToken();
        new JupiterAsyncTask<HttpMessage>(PersonalInfo.this) {

            @Override
            protected void onPreExecute() throws Exception {
                setShowExceptionTip(true);
            }

            @Override
            public HttpMessage call() throws Exception {
                return getAppContext().getHttpService().updateUser(user, groupId, appToken);
            }

            @Override
            protected void onSuccess(HttpMessage executeResult) throws Exception {
                if ("success".equals(executeResult.getResult())) {
                    PreferenceUtil.getInstance().saveUser(user);
                    Toasts.showToastInfoShort(PersonalInfo.this, "用户信息更新完成");
                } else {
                    showToastShort(executeResult.getMessageInfo());
                }
            }

        }.execute();
    }

    public void funNickName(View view) {
//        if (0 == EDIT_PERSONAL_INFO) {
//            return;
//        }
//        String nickname = nickName.getText().toString();
//        NickNameDialogFragment dialogFragment = new NickNameDialogFragment();
//        dialogFragment.setOnNickNameInputListener(new NickNameDialogFragment.NickNameInputListener() {
//            @Override
//            public void onNickNameInputComplete(String nickname){
//                nickName.setText(nickname);
//            }
//        });
//        Bundle bundle = new Bundle();
//        bundle.putString("NickName", nickname);
//        dialogFragment.setArguments(bundle);
//        dialogFragment.show(getFragmentManager(), "NickNameDialog");
    }

    public void funRealName(View view) {
        if (0 == EDIT_PERSONAL_INFO) {
            return;
        }
        String realname = realName.getText().toString();
        RealNameDialogFragment dialogFragment = new RealNameDialogFragment();
        dialogFragment.setOnRealNameInputListener(new RealNameDialogFragment.RealNameInputListener() {
            @Override
            public void onRealNameInputComplete(String realname) {
                realName.setText(realname);
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString("RealName", realname);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getFragmentManager(), "RealNameDialog");
    }

    public void funTelphone(View view) {
        if (0 == EDIT_PERSONAL_INFO) {
            return;
        }
        String tel = telphone.getText().toString();
        TelphoneDialogFragment dialogFragment = new TelphoneDialogFragment();
        dialogFragment.setOnTelphoneNameInputListener(new TelphoneDialogFragment.TelphoneInputListener() {
            @Override
            public void onTelphoneInputComplete(String tel) {
                telphone.setText(tel);
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString("Telphone", tel);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getFragmentManager(), "TelphoneDialog");
    }

    public void funEmail(View view) {
        if (0 == EDIT_PERSONAL_INFO) {
            return;
        }
        String Email = email.getText().toString();
        EmailDialogFragment dialogFragment = new EmailDialogFragment();
        dialogFragment.setOnEmailInputListener(new EmailDialogFragment.EmailInputListener() {
            @Override
            public void onEmailInputComplete(String Email) {
                email.setText(Email);
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString("Email", Email);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getFragmentManager(), "EmailDialog");
    }

    public void funGroup(View view) {
        if (0 == EDIT_PERSONAL_INFO) {
            return;
        }
        String groupName = group.getText().toString();
        String groupId = group.getTag().toString();
        GroupDialogFragment dialogFragment = new GroupDialogFragment();
        dialogFragment.setOnGroupInputListener(new GroupDialogFragment.GroupInputListener() {
            @Override
            public void onGroupInputComplete(String groupName, String groupId) {
                group.setText(groupName);
                group.setTag(groupId);
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString("groupName", groupName);
        bundle.putString("groupId", groupId);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getFragmentManager(), "GroupDialog");
    }


    public void funPassword(View view) {
//        if (0 == EDIT_PERSONAL_INFO) {
//            return;
//        }
        final String name = PreferenceUtil.getInstance().getUserName();
        PasswordDialogFragment dialogFragment = new PasswordDialogFragment();
        /*dialogFragment.setOnPasswordInputListener(new PasswordDialogFragment.PasswordInputListener() {
            @Override
            public void onPasswordInputComplete(final String phoneNumber, final String securityCode, final String newPassword) {
                new ProgressTask<HttpMessage>(PersonalInfo.this, "正在处理，请稍后...") {

                    @Override
                    public HttpMessage call() throws Exception {
                        return getAppContext().getHttpService().updateCustomerPasswordByMobile(name, phoneNumber, securityCode, newPassword);
                    }

                    @Override
                    protected void onSuccess(HttpMessage executeResult) throws Exception {
                        if ("success".equals(executeResult.getResult())) {
                            showToastShort("密码修改成功");
                            PreferenceUtil.getInstance().setPassword(newPassword);
                            PreferenceUtil.getInstance().getUser().setPassword(newPassword);
                            finish();
                        } else {
                            showToastShort(executeResult.getMessageInfo());
                        }
                    }
                }.execute();
            }
        });
        dialogFragment.show(getFragmentManager(), "PasswordDialog");*/
    }

    public void funExit(View view) {
        View exitDialog = LayoutInflater.from(this).inflate(R.layout.exit_dialog, null);
        Button btnCancel = (Button) exitDialog.findViewById(R.id.btn_exit_dialog_cancel);
        Button btnOk = (Button) exitDialog.findViewById(R.id.btn_exit_dialog_ok);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(exitDialog);
        final AlertDialog dialog = builder.create();
        Window window=dialog.getWindow();
        window.setWindowAnimations(R.style.CustomdialogAnimation); //设置窗口弹出动画
        window.setBackgroundDrawableResource(R.color.transparent); //设置对话框背景为透明
        dialog.show();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                PreferenceUtil.getInstance().setAutoLogin(false);
                PreferenceUtil.getInstance().setIsLogin(false);
                PreferenceUtil.getInstance().saveUser(new User());
                EventBus.getDefault().post(new LoginEvent(0));
                setResult(RESULT_OK);
                finish();
            }
        });


//        new ActionSheetDialog(this)
//                .builder()
//                .setTitle("退出登录后，将会导致系统很多功能无权使用，是否确认退出？")
//                .setCancelable(false)
//                .setCanceledOnTouchOutside(false)
//                .addSheetItem("确认退出", ActionSheetDialog.SheetItemColor.Blue,
//                        new ActionSheetDialog.OnSheetItemClickListener() {
//                            @Override
//                            public void onClick(int which) {
//                                PreferenceUtil.getInstance().setAutoLogin(false);
//                                PreferenceUtil.getInstance().setIsLogin(false);
//                                EventBus.getDefault().post(new LoginEvent(0));
//                                setResult(RESULT_OK);
//                                finish();
//                            }
//                        }).show();
    }
}
