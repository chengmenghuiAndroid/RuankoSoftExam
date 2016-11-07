package com.itee.exam.app.ui.main;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itee.exam.R;
import com.itee.exam.app.service.ExamService;
import com.itee.exam.app.ui.QAOnline;
import com.itee.exam.app.ui.RuanKuExamActivity;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.app.ui.event.LoginEvent;
import com.itee.exam.app.ui.event.UserIdEvent;
import com.itee.exam.app.ui.leaflets.LeafletsActivity;
import com.itee.exam.app.ui.login.LoginActivity;
import com.itee.exam.app.ui.personal.PersonalInfoCenter;
import com.itee.exam.app.ui.signup.SignInfoActivity;
import com.itee.exam.app.ui.splash.PrefConstants;
import com.itee.exam.app.ui.splash.SAppUtil;
import com.itee.exam.app.ui.splash.SplashActivity;
import com.itee.exam.app.ui.video.VideoActivity;
import com.itee.exam.app.ui.video.VideoClassActivity;
import com.itee.exam.core.utils.StringUtils;
import com.itee.exam.receiver.MyReceiver;
import com.itee.exam.utils.PreferenceUtil;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;

public class DemoActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.days_tv)
    TextView daysTv;
    @BindView(R.id.hours_tv)
    TextView hoursTv;
    @BindView(R.id.minutes_tv)
    TextView minutesTv;
    @BindView(R.id.countdown_layout)
    LinearLayout countDown;
    @BindView(R.id.seconds_tv)
    TextView secondsTv;

    // 倒计时
    private  static String webUrl2 = "http://www.baidu.com";//百度
    private  static String webUrl3 = "http://www.taobao.com";//淘宝
    private  static String webUrl4 = "http://www.ntsc.ac.cn";//中国科学院国家授时中心
    private long mDay = 10;
    private long mHour = 10;
    private long mMin = 30;
    private long mSecond = 00;// 天 ,小时,分钟,秒
    private boolean isRun = true;
    private Animation anim_in, anim_out;
    private LinearLayout llContainer;
    private Handler mHandler;
    private boolean runFlag = true;
    private int index = 0;
    private ImageView header;
    private Gallery gallery;

    private String endTime ="20161112" ;  //"yyyyMMdd"格式 如 20131022
    public static boolean isForeground = false;
    private String THIS_LEFT = "DemoActivity";
    private double dayCount;
    private String countDownTime;
    private Typeface typeFace;

    public static Intent generateIntent(Context context) {
        return new Intent(context, DemoActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkShowStartTutorial();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        startService(new Intent(this, ExamService.class));
        EventBus.getDefault().register(this);
        setStyleBasic();
        setStyleCustom();
        ButterKnife.bind(this);
        // 如果是Home键等其他操作导致销毁重建，那么判断是否存在，
        // 如果存在，那么获取上次滚动到状态的index



        // 如果不存在，那么调用初始化方法
        if (null != savedInstanceState) {
            index = savedInstanceState.getInt("currIndex");
        } else {
            init();
        }
        initGallery();
        AutoGallery();
        header = (ImageView) findViewById(R.id.header);
        if (PreferenceUtil.getInstance().getIsLogin()) {
            header.setVisibility(View.GONE);
        }
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LoginActivity.generateIntent(DemoActivity.this);
                startActivity(intent);
            }
        });
        JPushInterface.init(getApplicationContext());
        registerMessageReceiver();
    }

    /**
     * 设置通知提示方式 - 基础属性
     */
    private void setStyleBasic() {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(DemoActivity.this);
        builder.statusBarDrawable = R.drawable.jpush_notification_icon;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
        builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
        JPushInterface.setPushNotificationBuilder(1, builder);
    }


    /**
     * 设置通知栏样式 - 定义通知栏Layout
     */
    private void setStyleCustom() {
        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(DemoActivity.this, R.layout.customer_notitfication_layout, R.id.icon, R.id.title, R.id.text);
        builder.layoutIconDrawable = R.drawable.jpush_notification_icon;
        builder.developerArg0 = "developerArg2";
        JPushInterface.setPushNotificationBuilder(2, builder);
    }

    public void funExam(View view) {
        Intent intent = new Intent(this, RuanKuExamActivity.class);
        startActivity(intent);
        startActivity(intent);
    }

    public void funVideoCourse(View view) {
        Intent intent = new Intent(this, VideoActivity.class);
        startActivity(intent);
//        if (PreferenceUtil.getInstance().getIsLogin()) {
//            Intent intent = new Intent(this, VideoActivity.class);
//            startActivity(intent);
//        } else {
//            Intent intent = LoginActivity.generateIntent(DemoActivity.this);
//            startActivity(intent);
//        }
    }

   /* public void funClass(View view) {
        Intent intent = new Intent(this, VideoClassActivity.class);
        startActivity(intent);
//        if (PreferenceUtil.getInstance().getIsLogin()) {
//            Intent intent = new Intent(this, VideoClassActivity.class);
//            startActivity(intent);
//        } else {
//            Intent intent = LoginActivity.generateIntent(DemoActivity.this);
//            startActivity(intent);
//        }
    }*/

    public void funSignUp(View view) {
        Intent intent = new Intent(this, SignInfoActivity.class);
        startActivity(intent);
    }

    //新的软考包过班模块
//    public void funClass(View view) {
//        Intent intent = new Intent(this, VideoClassPassActivity.class);
//        startActivity(intent);
//    }

    public void funClass(View view) {
        Intent intent = new Intent(this, VideoClassActivity.class);
        startActivity(intent);
    }

    public void funScoreQuery(View view) {
        Intent intent = new Intent(this, ScoreQueryActivity.class);
        startActivity(intent);
    }

    public void funAnswerOnline(View view) {
        Intent intent = new Intent(this, QAOnline.class);
        startActivity(intent);
    }

    public void funUserInfo(View view) {
        if (PreferenceUtil.getInstance().getIsLogin()) {
            Intent intent = new Intent(this, PersonalInfoCenter.class);
            startActivity(intent);
        } else {
            Intent intent = LoginActivity.generateIntent(DemoActivity.this);
            startActivity(intent);
        }
    }

    public void funNews(View view) {
//            if (PreferenceUtil.getInstance().getIsLogin()) {
                openActivity(LearningCentreActivity.class);
                EventBus.getDefault().postSticky(new UserIdEvent(PreferenceUtil.getInstance().getUserId()));
                Log.e(THIS_LEFT, " "+PreferenceUtil.getInstance().getUserId());
//            } else {
//                Intent intent = LoginActivity.generateIntent(DemoActivity.this);
//                startActivity(intent);
//            }
    }

    private void initGallery() {
        gallery = (Gallery) findViewById(R.id.lay_header_gallery);
        GalleryTask task = new GalleryTask(DemoActivity.this, gallery);
        task.execute();
        gallery.setOnItemClickListener(new Gallery.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map map = (Map) gallery.getAdapter().getItem(position);
                Intent intent = new Intent(DemoActivity.this, LeafletsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("URL", map.get("URL").toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }

        });
    }

    private void AutoGallery() {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                galleryHandler.sendEmptyMessage(1);
            }
        }, 3000, 3000);

    }

    private final Handler galleryHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 1:
                    int selectedimage = gallery.getSelectedItemPosition() + 1;
                    gallery.setSelection(selectedimage);
                    break;
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private void init() {
        // 找到装载这个滚动TextView的LinearLayout
        llContainer = (LinearLayout) findViewById(R.id.ll_container);
        // 加载进入动画
        anim_in = AnimationUtils.loadAnimation(this, R.anim.anim_tv_marquee_in);
        // 加载移除动画
        anim_out = AnimationUtils.loadAnimation(this,
                R.anim.anim_tv_marquee_out);
        // 填充装文字的list
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("title", "1、软酷网软考包过班--软考包过行业领先");
        map.put("url", "http://m.ruanko.com/subject/software_exam.html");
        list.add(map);
        map = new HashMap<String, String>();
        map.put("title", "2、软酷网——青少年编程学习领导品牌");
        map.put("url", "http://code.ruanko.com/");
        list.add(map);
        map = new HashMap<String, String>();
        map.put("title", "3、软酷网个性化能力培养");
        map.put("url", "http://www.ruanko.com/subject/personal_bility_tranining.html");
        list.add(map);

        // 根据list的大小，动态创建同样个数的TextView
        for (int i = 0; i < list.size(); i++) {
            TextView tvTemp = new TextView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER_VERTICAL;
            tvTemp.setGravity(Gravity.CENTER_VERTICAL);
            tvTemp.setSingleLine(true);
            tvTemp.setEllipsize(TextUtils.TruncateAt.END);
            tvTemp.setTextColor(this.getResources().getColor(R.color.blue_2));
            tvTemp.setTextSize(12);
            if (i == 0) {
                tvTemp.setVisibility(View.VISIBLE);
            } else {
                tvTemp.setVisibility(View.GONE);
            }
            tvTemp.setText(list.get(i).get("title"));
            tvTemp.setTag(list.get(i).get("url"));
            tvTemp.setId(i + 10000);
            tvTemp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DemoActivity.this, LeafletsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("URL", v.getTag().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            llContainer.addView(tvTemp);
        }

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        // 移除
                        TextView tvTemp = (TextView) msg.obj;
                        tvTemp.startAnimation(anim_out);
                        tvTemp.setVisibility(View.GONE);
                        break;
                    case 1:
                        // 进入
                        TextView tvTemp2 = (TextView) msg.obj;
                        tvTemp2.startAnimation(anim_in);
                        tvTemp2.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };
    }

    /*
     * 停止动画
     */
    private void stopEffect() {
        runFlag = false;
    }

    /*
     * 启动动画
     */
    private void startEffect() {
        runFlag = true;
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (runFlag) {
                    try {
                        // 每隔2秒轮换一次
                        Thread.sleep(2000);
                        if (runFlag) {
                            // 获取第index个TextView开始移除动画
                            TextView tvTemp = (TextView) llContainer.getChildAt(index);
                            mHandler.obtainMessage(0, tvTemp).sendToTarget();
                            if (index < llContainer.getChildCount()) {
                                index++;
                                if (index == llContainer.getChildCount()) {
                                    index = 0;
                                }
                                // index+1个动画开始进入动画
                                tvTemp = (TextView) llContainer.getChildAt(index);
                                mHandler.obtainMessage(1, tvTemp)
                                        .sendToTarget();
                            }
                        }
                    } catch (InterruptedException e) {
                        // 如果有异常，那么停止轮换。当然这种情况很难发生
                        runFlag = false;
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /*
     * 当页面暂停，那么停止轮换
     */
    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
        stopEffect();
    }

    /*
     * 当页面可见，开始轮换
     */
    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
        startEffect();
        initView();
    }

    /*
     * 用于保存当前index的,结合onCreate方法
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currIndex", index);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
        stopService(new Intent(this, ExamService.class));
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

    public void onEventMainThread(LoginEvent event) {
        switch (event.getResult()) {
            case 1:
                header.setVisibility(View.GONE);
                break;
            case 0:
                header.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void checkShowStartTutorial() {
        int oldVersionCode = PrefConstants.getAppPrefInt(this, "version_code");
        int currentVersionCode = SAppUtil.getAppVersionCode(this);
        if (currentVersionCode > oldVersionCode) {
            startActivity(new Intent(DemoActivity.this, SplashActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            PrefConstants.putAppPrefInt(this, "version_code", currentVersionCode);
        }
    }

    private MyReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.itee.exam.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!StringUtils.isNotBlank(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
//                setCostomMsg(showMsg.toString());
            }
        }
    }

    /**
     *
     * 考试倒计时功能
     */

    private void initView(){
        startRun();
        getCountDown();
        typeFace = Typeface.createFromAsset(getAssets(),"fonts/hylilianghei.ttf");
        tvTitle.setTypeface(typeFace);
    }

    /**
     *
     * 开启倒计时功能
     */

    private void startRun() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRun){
                    try {
                        Message messege = Message.obtain();
                        messege.what = 1;
                        timeHandler.sendMessage(messege);
                        Thread.sleep(518400000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    private Handler timeHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1) {
                computeTime();
                String countDownExamTime = countDownTime.substring(0, countDownTime.indexOf("."));
                daysTv.setTypeface(typeFace);
                if(Integer.parseInt(countDownExamTime) >=10){
                    daysTv.setText(countDownExamTime);
                }else {
                    daysTv.setText("0"+countDownExamTime);
                }
//                daysTv.setText(mDay+"");
                hoursTv.setText(mHour+"");
                minutesTv.setText(mMin+"");
                secondsTv.setText(mSecond+"");
                if (Integer.parseInt(countDownExamTime) < 0) {
                    countDown.setVisibility(View.GONE);
                    gallery.setVisibility(View.VISIBLE);

                }
            }
        }
    };


    /**
     * 倒计时计算
     */

    private void computeTime() {
        mSecond--;
        if (mSecond < 0) {
            mMin -- ;
            mSecond = 59;
            if (mMin < 0){
                mMin = 59 ;
                mHour -- ;
                if (mHour < 0){
                    //倒计时结束
                    mHour = 23 ;
                    mDay --;
                }
            }
        }
    }

    /**
     * 获取指定网站的日期时间
     *
     * @return
     * @author SHANHY
     */

    private String getBeginTime() {
//        boolean connected = NetworkUtils.isNetworkConnected(this);
//        if (connected){
//            //获取网络时间
//             return getWebTime();
//        }else{
            //获取系统时间
             return getSystemTime();
        }
//    }

    private  String getWebTime() {
        String userTime = null;
        try {
            URL urlTime = new URL(webUrl2);//取得资源对象
            URLConnection uc = urlTime.openConnection();//生成连接对象
            uc.connect(); //发出连接
            long ld = uc.getDate(); //取得网站日期时间
            Date date = new Date(ld); //转换为标准时间对象
            SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMdd");//24小时制,12小时制则HH为hh
            userTime = sdformat.format(date);
            Log.e(THIS_LEFT,"获取网络开始时间:"+userTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userTime;
    }

    private  String getSystemTime() {
        long sysTime = System.currentTimeMillis();
        Date date = new Date(sysTime); //转换为标准时间对象
        SimpleDateFormat sysTimeFormat = new SimpleDateFormat("yyyyMMdd");//24小时制,12小时制则HH为hh
        String userTimeSys = sysTimeFormat.format(date);
        Log.e(THIS_LEFT,"获取系统开始时间:"+userTimeSys);
        return userTimeSys;
    }

    /**
     * 倒计时天数
     */
    public  void getCountDown(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");//输入日期的格式
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(getBeginTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = simpleDateFormat.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        GregorianCalendar cal1 = new GregorianCalendar();
        GregorianCalendar cal2 = new GregorianCalendar();
        cal1.setTime(date1);
        cal2.setTime(date2);
        //从间隔毫秒变成间隔天数
        dayCount = (cal2.getTimeInMillis()-cal1.getTimeInMillis())/(1000*3600*24);
        countDownTime = String.valueOf(dayCount);
        Log.e(THIS_LEFT,"倒计时天数:"+ countDownTime.substring(0, countDownTime.indexOf(".")));
    }

}
