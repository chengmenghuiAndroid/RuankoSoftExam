package com.itee.exam.app.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;

import com.itee.exam.app.AppContext;
import com.itee.exam.app.entity.User;
import com.itee.exam.core.utils.JupiterAsyncTask;
import com.itee.exam.utils.PreferenceUtil;
import com.itee.exam.vo.HttpMessage;
import com.itee.exam.vo.UserAppToken;

/**
 * 收取消息服务
 *
 * @author way
 */
public class ExamService extends Service {
    private static final String TAG = ExamService.class.getName();
    public static final String ACTION_TOKEN = "com.itee.exam.change_token";
    public static final int UPDATE_TOKEN_DELAY = 60;//间隔60分钟获取一次token

    private AppContext application;
    private PendingIntent sender;
    private AlarmManager alarm;

    // 接收系统服务广播
    private BroadcastReceiver msgReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String act = intent.getAction();
            if (ACTION_TOKEN.equals(act)){
                final User user = PreferenceUtil.getInstance().getUser();
                if (user == null) return;

                new JupiterAsyncTask<HttpMessage<UserAppToken>>(application) {

                    @Override
                    public HttpMessage<UserAppToken> call() throws Exception {
                        return application.getHttpService().getUserAppToken(user.getUserName(), user.getAppKey());
                    }

                    @Override
                    protected void onSuccess(HttpMessage<UserAppToken> result) throws Exception {
                        if("success".equals(result.getResult())){
                            UserAppToken token = result.getObject();
                            if (token != null){
                                PreferenceUtil.getInstance().saveAppToken(token);
                            }
                        }
                    }

                }.execute();
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return new PushBinder();
    }

    @Override
    public void onCreate() {// 在onCreate方法里面注册广播接收者
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_TOKEN);
        registerReceiver(msgReceiver, filter);

        application = (AppContext) this.getApplicationContext();
        getValidateToken();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_REDELIVER_INTENT;
    }

    @Override
    // 在服务被摧毁时，做一些事情
    public void onDestroy() {
        super.onDestroy();
        stopFetchToken();
        unregisterReceiver(msgReceiver);
    }

    /**
     * 定期获取后台token，避免token过期
     */
    public void getValidateToken(){
        stopFetchToken();

        Intent intent =new Intent();
        intent.setAction(ACTION_TOKEN);
        sender = PendingIntent.getBroadcast(this, 0, intent, 0);

        //开始时间
        long firstime= SystemClock.elapsedRealtime();

        AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE);
        //1小时一个周期，不停的发送广播
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,firstime, UPDATE_TOKEN_DELAY * 60 * 1000, sender);
    }

    public void stopFetchToken(){
        if (alarm != null && sender != null){
            alarm.cancel(sender);
        }
    }

    public class PushBinder extends Binder {

        public ExamService getService() {
            return ExamService.this;
        }
    }
}
