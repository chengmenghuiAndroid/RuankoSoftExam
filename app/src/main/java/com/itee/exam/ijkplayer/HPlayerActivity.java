package com.itee.exam.ijkplayer;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dou361.ijkplayer.bean.VideoijkBean;
import com.dou361.ijkplayer.listener.OnShowThumbnailListener;
import com.dou361.ijkplayer.widget.PlayStateParams;
import com.dou361.ijkplayer.widget.PlayerView;
import com.itee.exam.R;
import com.itee.exam.app.Constants;
import com.itee.exam.app.entity.ChaptersList;
import com.itee.exam.app.entity.ChooseCountryBean;
import com.itee.exam.app.entity.Courses;
import com.itee.exam.app.entity.PinnedHeaderListView;
import com.itee.exam.app.entity.VideoUrlBean;
import com.itee.exam.core.utils.Toasts;
import com.itee.exam.utils.MediaUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.itee.exam.ijkplayer.HPlayerActivity.SingRequestQueue.requestQueue;

public class HPlayerActivity extends AppCompatActivity {

    private PlayerView player;
    private Context mContext;
    private List<VideoijkBean> list;
    private PowerManager.WakeLock wakeLock;
    private ChaptersList chaptersListItem;
    private int coursesIndex;
    private TextView video_title;
    private TextView bar_video;
    private PinnedHeaderListView listView;
    private List<ChaptersList> chaptersList;
    View main;
    private static String THIS_LEFT = "HPlayerActivity";
    /**
     * 用来标志请求的what, 类似handler的what一样，这里用来区分请求。
     */
    private static final int NOHTTP_WHAT_TEST = 0x001;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        main = getLayoutInflater().from(this).inflate(R.layout.activity_hplayer, null);
        setContentView(main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        video_title = (TextView) findViewById(R.id.tv_video_title);
//        listView = (PinnedHeaderListView) findViewById(R.id.listView);;
        SingRequestQueue.getInstance();
        /**虚拟按键的隐藏方法*/
        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                //比较Activity根布局与当前布局的大小
                int heightDiff = main.getRootView().getHeight() - main.getHeight();
                if (heightDiff > 100) {
                    //大小超过100时，一般为显示虚拟键盘事件
                    main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                } else {
                    //大小小于100时，为不显示虚拟键盘或虚拟键盘隐藏
                    main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

                }
            }
        });

        /**常亮*/
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "liveTAG");
        wakeLock.acquire();
        list = new ArrayList<VideoijkBean>();

         /**
         *获取视频源
         * @low
         * @normal
         * @original
         */
        String low = getIntent().getExtras().getString("low");
        String normal = getIntent().getExtras().getString("normal");
        String original = getIntent().getExtras().getString("original");
        String sectionsTitle = getIntent().getExtras().getString("sectionsTitle");
        Log.e(THIS_LEFT,">>>>>>>>>"+original);

        list = new ArrayList<VideoijkBean>();
        String url1 = "http://v.ruanko.com/5b3fb88f20234dd2bc4b6ddfd98f4209_normal.mp4?e=1478264571&token=kEUFey_36xJqHaOhr44fNd8eNUo4a9TLtMJSrqhu:QGbGKF8xLmufuMSYwaQ4JMoLwaw=";
        String url2 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4";
        String url3 = "http://v.ruanko.com/fd39f42585ad45a88fbf42167bd6983c_original.mp4?e=1477395081&token=kEUFey_36xJqHaOhr44fNd8eNUo4a9TLtMJSrqhu:53u0MmjSvoyr8jA0_FktvIlCQps=";

        VideoijkBean m1 = new VideoijkBean();
        m1.setStream("标清");
        m1.setUrl(low);
        VideoijkBean m2 = new VideoijkBean();
        m2.setStream("高清");
        m2.setUrl(normal);
        VideoijkBean m3 = new VideoijkBean();
        m3.setStream("超清");
        m3.setUrl(original);
        list.add(m1);
        list.add(m2);
        list.add(m3);
        player = new PlayerView(this,main) {
            @Override
            public PlayerView toggleProcessDurationOrientation() {
                hideSteam(getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                return setProcessDurationOrientation(getScreenOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ? PlayStateParams.PROCESS_PORTRAIT : PlayStateParams.PROCESS_LANDSCAPE);
            }

            @Override
            public PlayerView setPlaySource(List<VideoijkBean> list) {
                return super.setPlaySource(list);
            }
        }
                .setTitle(sectionsTitle)
                .setProcessDurationOrientation(PlayStateParams.PROCESS_PORTRAIT)
                .setScaleType(PlayStateParams.fillparent)
                .forbidTouch(false)
                .hideSteam(true)
                .hideCenterPlayer(true)
                .showThumbnail(new OnShowThumbnailListener() {
                    @Override
                    public void onShowThumbnail(ImageView ivThumbnail) {
                        Glide.with(mContext)
                                .load(String.valueOf(getResources().getDrawable(R.drawable.vedio_bg)))
                                .placeholder(R.color.cl_default)
                                .error(R.color.cl_error)
                                .into(ivThumbnail);
                    }
                })
                .setPlaySource(list)
                .startPlay();
//        getLearnCentreDate();

    }

    private void getLearnCentreDate() {
        Courses coursesItem = (Courses) getIntent().getSerializableExtra("coursesItem");
        video_title.setText(coursesItem.getCoursesTitle());
        Log.e(THIS_LEFT, "title:" + coursesItem.getCoursesTitle());
        coursesIndex = getIntent().getExtras().getInt("coursesIndex");
        Log.e(THIS_LEFT, "coursesIndex" + coursesIndex);
        chaptersList = coursesItem.getChaptersList();

        Log.e(THIS_LEFT, "getChaptersList" + chaptersList);

        if (chaptersList != null && !chaptersList.isEmpty()) {
            Log.e(THIS_LEFT, "size" + chaptersList.size() + "..." + coursesIndex);
            chaptersListItem = chaptersList.get(coursesIndex);
        } else {
            Toasts.showToastInfoShort(HPlayerActivity.this, "没有相关课程介绍");
            return;
        }

//        initAdapter(chaptersList);

    }

    private void initAdapter(List<ChaptersList> chaptersList) {
        listView.setData(getData(chaptersList));
        listView.setOnItemClickListener(onItemClickListener);

    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int coursesIndex = position++;

            TextView ruankoId = (TextView) view.findViewById(R.id.countryCode);

            if ((ruankoId.getTag() == null) || (ruankoId.getTag().equals("") || (ruankoId.getTag().toString().trim().length() == 0))) {
                view.findViewById(R.id.countryCode).setEnabled(false);
            } else {
                Toasts.showToastInfoShort(HPlayerActivity.this, "" + ruankoId.getTag());
//                getVideoUrl(ruankoId.getTag().toString());
            }


        }

    };

    public static class SingRequestQueue {

        public static RequestQueue requestQueue = null;

        public static RequestQueue getInstance() {
            if (requestQueue == null) {

                requestQueue = NoHttp.newRequestQueue();
            }
            return requestQueue;
        }

    }

    private void getVideoUrl(String ruankoId) {
        final Request<String> request = NoHttp.createStringRequest(Constants.VIDEO_URL, RequestMethod.GET);
        request.add("resourceId", ruankoId)
                .setConnectTimeout(10 * 1000)
                .setReadTimeout(10 * 1000)// 设置读取超时时间，也就是服务器的响应超时。
                .setTag(this)
                .setCancelSign(this);

        requestQueue.add(NOHTTP_WHAT_TEST, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                if(what == NOHTTP_WHAT_TEST){
                    Log.e(THIS_LEFT,">>>>>>>>>"+response.get());
                    try {
                        String s = response.get();
                        ObjectMapper objectMapper=new ObjectMapper();
                        VideoUrlBean videoUrlBean = objectMapper.readValue(s, VideoUrlBean.class);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

//                    try {
//                        List<String> list = new ArrayList<String>();
//                        Iterator iterator = jsonToObject(response.get()).entrySet().iterator();
//                        while (iterator.hasNext()) {
//                            Map.Entry<String, String> entry= (Map.Entry<String, String>) iterator.next();
//                            Log.e(THIS_LEFT, "key = " + entry.getKey() + "....." + "key = " + entry.getValue());
//                            list.add(entry.getValue());
//                        }
//                        Log.e(THIS_LEFT,""+map);
//                        Log.e(THIS_LEFT,""+list);
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }

            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    public Map jsonToObject(String jsonStr) throws Exception {
        JSONObject jsonObj = new JSONObject(jsonStr);
        Iterator<String> nameItr = jsonObj.keys();
        String name;
        Map<String, String> outMap = new HashMap<String, String>();
        while (nameItr.hasNext()) {
            name = nameItr.next();
            outMap.put(name, jsonObj.getString(name));
        }
        return outMap;
    }


    private ArrayList<ChooseCountryBean> getData(List<ChaptersList> chaptersList) {
        ArrayList<ChooseCountryBean> beans = new ArrayList<>();

        for (int i = 0; i < chaptersList.size(); i++) {
            if(chaptersList.get(i).getSectionsList().size() == 0){
                ChooseCountryBean bean = new ChooseCountryBean();
                bean.setSort("第" + chaptersList.get(i).getChaptersSort()+ "章"+"      "+chaptersList.get(i).getChaptersTitle());
                bean.setCountry("暂无相关视节数 !");
                bean.setCode("暂无相关视频课程!");
                beans.add(bean);
            }
            for (int j = 0; j < chaptersList.get(i).getSectionsList().size(); j++) {
                ChooseCountryBean bean = new ChooseCountryBean();
                    bean.setCode(chaptersList.get(i).getSectionsList().get(j).getSectionsTitle());
                    bean.setRuankoId(chaptersList.get(i).getSectionsList().get(j).getSectionsRuankoId());
                    bean.setCountry("第" + chaptersList.get(i).getSectionsList().get(j).getSectionsSort() + "节");
                    bean.setSort("第" + chaptersList.get(i).getChaptersSort() + "章"+"      "+chaptersList.get(i).getChaptersTitle());
                    //根据chaptersSort取值
                    //bean.setRuankoId();
//                    Log.e(THIS_LEFT, "setSort" + "第" + chaptersList.get(i).getChaptersSort() + "章");
                    beans.add(bean);
//                    Log.e(THIS_LEFT, "getSectionsList》》》" + chaptersList.get(i).getSectionsList().get(j) + "...." + i);
//                    Log.e(THIS_LEFT, "getSectionsTitle" + chaptersList.get(i).getSectionsList().get(j).getSectionsTitle());
//                    Log.e(THIS_LEFT, "getSectionsList>>>>>>" + chaptersList.get(i).getSectionsList()+".."+i);
            }

        }

        return beans;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
        /**demo的内容，恢复系统其它媒体的状态*/
        MediaUtils.muteAudioFocus(mContext, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
        /**demo的内容，暂停系统其它媒体的状态*/
        MediaUtils.muteAudioFocus(mContext, false);
        /**demo的内容，激活设备常亮状态*/
        if (wakeLock != null) {
            wakeLock.acquire();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
        /**demo的内容，恢复设备亮度状态*/
        if (wakeLock != null) {
            wakeLock.release();
        }
    }

}
