package com.itee.exam.app.ui.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itee.exam.R;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.app.ui.main.DemoActivity;
import com.itee.exam.app.ui.update.UpdateActivity;
import com.itee.exam.app.ui.update.UpdateManager;
import com.itee.exam.core.utils.JupiterAsyncTask;
import com.itee.exam.core.utils.StringUtils;
import com.itee.exam.core.utils.Toasts;
import com.itee.exam.utils.PreferenceUtil;
import com.itee.exam.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import de.greenrobot.event.EventBus;

/**
 * 启动页面，默认停留2秒
 * Created by xin on 2015-07-07.
 */
public class LaunchActivity extends BaseActivity {
    public static final int UPDATE = 1;
    private UpdateManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.enableStrictMode();

        setContentView(R.layout.activity_launch);

        File dir = new File(Environment.getExternalStorageDirectory() + "/exam/download/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        dir = new File(Environment.getExternalStorageDirectory() + "/exam/crash/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        dir = new File(Environment.getExternalStorageDirectory() + "/exam/db/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/exam/db/pcc.db");
        if (!dir.exists()) {
            try{
                InputStream is = this.getAssets().open("pcc.db");
                dir.createNewFile();
                OutputStream myOutput = new FileOutputStream(dir);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
                myOutput.close();
                is.close();
            }catch (Exception e) {
                e.printStackTrace();
            }

        }

        ImageView lay = (ImageView) findViewById(R.id.app_logo_lay_top);
        AlphaAnimation mAlphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        mAlphaAnimation.setDuration(2000);
        lay.setAnimation(mAlphaAnimation);
        mAlphaAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                onHandle();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationStart(Animation animation) {

            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void onHandle() {
        if (!getAppContext().checkNetwork(true)) {
            navigationToMain();
            return;
        }
        manager = new UpdateManager(getAppContext());
        new JupiterAsyncTask<Boolean>(this) {
            @Override
            public Boolean call() throws Exception {
                return manager.isUpdate();
            }

            @Override
            protected void onSuccess(Boolean result) throws Exception {
                if (result) {
                    View view = LayoutInflater.from(LaunchActivity.this).inflate(R.layout.update_dialog, null);
                    Button btnCancel = (Button) view.findViewById(R.id.btn_update_dialog_cancel);
                    Button btnOk = (Button) view.findViewById(R.id.btn_update_dialog_ok);
                    TextView message = (TextView) view.findViewById(R.id.update_dialog_message);
                    AlertDialog.Builder builder = new AlertDialog.Builder(LaunchActivity.this);
                    builder.setCancelable(false);
                    builder.setView(view);
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    message.setText(manager.getContent());
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toasts.showToastFail(LaunchActivity.this, getString(R.string.tip_not_updata) + "\n5秒后系统将自动关闭", Toast.LENGTH_LONG);
                            dialog.dismiss();
                            //autoLogin();
                            closeApp();
                        }
                    });
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                showProgressBar();// 更新当前版本
                                dialog.dismiss();
                            } catch (Exception e) {
                                Toasts.showToastFail(LaunchActivity.this, getString(R.string.tip_update_error), Toast.LENGTH_LONG);
                                //autoLogin();
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    autoLogin();
                }
            }
        }.execute();

    }

    private void autoLogin(){
        PreferenceUtil preferenceUtil = PreferenceUtil.getInstance();
        final String userName = preferenceUtil.getUserName();
        final String password = preferenceUtil.getPassword();

        if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password)&&PreferenceUtil.getInstance().isAutoLogin()) {
            new LoginManager(LaunchActivity.this).login(userName, password);
        }else {
            navigationToMain();
        }
    }

    protected void showProgressBar() {
        if (manager != null) {
            String url = manager.getDownlaodUrl() + File.separator + manager.getApkName();
            Intent intent = UpdateActivity.generateIntent(this,url,manager.getContent());
            this.startActivityForResult(intent,UPDATE);
        }
    }

    private void closeApp() {
        new CountDownTimer(1000 * 5, 1000) {
            public void onTick(long millisUntilFinished) {
//                timer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                finish();
            }
        }.start();
    }

    private void navigationToMain(){
        Intent intent = new Intent(this, DemoActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UPDATE) {
            onHandle();
        }
    }
}
