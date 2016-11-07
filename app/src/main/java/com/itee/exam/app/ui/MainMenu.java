package com.itee.exam.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.itee.exam.R;
import com.itee.exam.app.service.ExamService;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.app.ui.event.LoginEvent;
import com.itee.exam.app.ui.login.LoginActivity;
import com.itee.exam.app.ui.personal.PersonalInfoCenter;
import com.itee.exam.app.ui.splash.PrefConstants;
import com.itee.exam.app.ui.splash.SAppUtil;
import com.itee.exam.app.ui.splash.SplashActivity;
import com.itee.exam.app.ui.video.VideoActivity;
import com.itee.exam.utils.PreferenceUtil;

import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

public class MainMenu extends BaseActivity {
    private TextView tvUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkShowStartTutorial();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        if(PreferenceUtil.getInstance().getIsLogin()){
            startService(new Intent(this, ExamService.class));
        }
        EventBus.getDefault().register(this);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        if(!PreferenceUtil.getInstance().getIsLogin()){
            tvUserName.setText("未登陆");
        }else {
            tvUserName.setText(PreferenceUtil.getInstance().getUserName());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, ExamService.class));
    }

    public void OpenSetting(View view) {
        if (PreferenceUtil.getInstance().getIsLogin()) {
            Intent intent = new Intent(this, PersonalInfoCenter.class);
            startActivity(intent);
        } else {
            Intent intent = LoginActivity.generateIntent(MainMenu.this);
            startActivity(intent);
        }
    }

    /**
     * 相应点击事件已经在DemoActivity中处理，目前该方法已经弃用
     * @param view
     */

   /* public void funExam(View view) {
        Intent intent = MainActivity.generateIntent(this);
        startActivity(intent);

//        if (PreferenceUtil.getInstance().getIsLogin()) {
//            Intent intent = MainActivity.generateIntent(this);
//            startActivity(intent);
//        } else {
//            Intent intent = LoginActivity.generateIntent(MainMenu.this);
//            startActivity(intent);
//        }
    }*/

    public void funVideoCourse(View view) {
        Intent intent = new Intent(this, VideoActivity.class);
        startActivity(intent);
//        if (PreferenceUtil.getInstance().getIsLogin()) {
//            Intent intent = new Intent(this, VideoActivity.class);
//            startActivity(intent);
//        } else {
//            Intent intent = LoginActivity.generateIntent(MainMenu.this);
//            startActivity(intent);
//        }
    }

    /*public void funClass(View view) {
        Intent intent = new Intent(this, VideoClassActivity.class);
        startActivity(intent);
//        if (PreferenceUtil.getInstance().getIsLogin()) {
//            Intent intent = new Intent(this, VideoClassActivity.class);
//            startActivity(intent);
//        } else {
//            Intent intent = LoginActivity.generateIntent(MainMenu.this);
//            startActivity(intent);
//        }
    }*/

    public void funAnswerOnline(View view) {
        Intent intent = new Intent(this, QAOnline.class);
        startActivity(intent);
    }

    public void onEventMainThread(LoginEvent event) {
        switch (event.getResult()) {
            case 1:
                tvUserName.setText(PreferenceUtil.getInstance().getUserName());
                startService(new Intent(this, ExamService.class));
                break;
            case 0:
                tvUserName.setText("未登陆");
                stopService(new Intent(this, ExamService.class));
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isExit) {
                isExit = true;
                getAppContext().showToastShort("再按一次退出");
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                }, 2000);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    private static Boolean isExit = false;

    private void checkShowStartTutorial(){
        int oldVersionCode = PrefConstants.getAppPrefInt(this, "version_code");
        int currentVersionCode = SAppUtil.getAppVersionCode(this);
        if(currentVersionCode>oldVersionCode){
            startActivity(new Intent(MainMenu.this,SplashActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            PrefConstants.putAppPrefInt(this, "version_code", currentVersionCode);
        }
    }
}
