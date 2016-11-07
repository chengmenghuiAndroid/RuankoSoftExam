package com.itee.exam.app.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.itee.exam.R;
import com.itee.exam.app.Constants;
import com.itee.exam.app.entity.ChaptersList;
import com.itee.exam.app.entity.ChooseCountryBean;
import com.itee.exam.app.entity.Courses;
import com.itee.exam.app.entity.PinnedHeaderListView;
import com.itee.exam.app.entity.VideoUrlBean;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.core.utils.Toasts;
import com.itee.exam.ijkplayer.HPlayerActivity;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.itee.exam.ijkplayer.HPlayerActivity.SingRequestQueue.requestQueue;

/**
 * Created by rkcoe on 2016/11/3.
 */

public class VideoCourseActivity extends BaseActivity {

    private ChaptersList chaptersListItem;
    private int coursesIndex;
    private TextView video_title;
    private PinnedHeaderListView listView;
    private List<ChaptersList> chaptersList;

    private static String THIS_LEFT = "VideoCourseActivity";
    /**
     * 用来标志请求的what, 类似handler的what一样，这里用来区分请求。
     */
    private static final int NOHTTP_WHAT_TEST = 0x001;
    private TextView ruankoId;
    private TextView sectionsTitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_course_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        video_title = (TextView) findViewById(R.id.tv_video_title);
        listView = (PinnedHeaderListView) findViewById(R.id.listView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        HPlayerActivity.SingRequestQueue.getInstance();
        getLearnCentreDate();
        SingRequestQueue.getInstance();
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
            Toasts.showToastInfoShort(VideoCourseActivity.this, "没有相关课程介绍");
            return;
        }

        initAdapter(chaptersList);

    }

    private void initAdapter(List<ChaptersList> chaptersList) {
        listView.setData(getData(chaptersList));
        listView.setOnItemClickListener(onItemClickListener);

    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int coursesIndex = position++;

            sectionsTitle = (TextView) view.findViewById(R.id.alphaitem_tv_content);
            ruankoId = (TextView) view.findViewById(R.id.countryCode);
            if ((ruankoId.getTag() == null) || (ruankoId.getTag().equals("") || (ruankoId.getTag().toString().trim().length() == 0))) {
                view.findViewById(R.id.countryCode).setEnabled(false);
            } else {
                Log.e(THIS_LEFT, "sectionsTitle>>>" + sectionsTitle.getTag().toString());
                 getVideoUrl(ruankoId.getTag().toString(), sectionsTitle.getTag().toString());

            }

        }

    };

    private void getVideoUrl(String ruankoIdStr, final String sectionsTitleStr) {
        final Request<String> request = NoHttp.createStringRequest(Constants.VIDEO_URL, RequestMethod.GET);
        request.add("resourceId", ruankoIdStr)
                .setConnectTimeout(10 * 1000)
                .setReadTimeout(10 * 1000)// 设置读取超时时间，也就是服务器的响应超时。
                .setTag(this)
                .setCancelSign(this);
        requestQueue.add(NOHTTP_WHAT_TEST, request, new OnResponseListener<String>() {

            private VideoUrlBean videoUrlBean;

            @Override
            public void onStart(int what) {
                Toasts.showToastInfoShort(VideoCourseActivity.this, "正在获取视频源");
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                if (what == NOHTTP_WHAT_TEST) {
                    Log.e(THIS_LEFT, ">>>>>>>>>" + response.get());
                    try {
                        String videoUrlBeanStr = response.get();
                        ObjectMapper objectMapper = new ObjectMapper();
                        videoUrlBean = objectMapper.readValue(videoUrlBeanStr, VideoUrlBean.class);
                        if (videoUrlBean.getResult() == 1) {
                            Intent intent = new Intent(VideoCourseActivity.this, HPlayerActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("normal", videoUrlBean.getNormal());
                            bundle.putString("original", videoUrlBean.getOriginal());
                            bundle.putString("low", videoUrlBean.getLow());
                            bundle.putString("sectionsTitle", sectionsTitleStr);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            Toasts.showToastInfoShort(VideoCourseActivity.this, "获取视频源失败");

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailed(int what, Response<String> response) {
                Toasts.showToastInfoShort(VideoCourseActivity.this, "获取数据失败");
            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    /**
     * 单例队列
     *
     * @requestQueue
     */
    public static class SingRequestQueue {

        public static RequestQueue requestQueue = null;

        public static RequestQueue getInstance() {
            if (requestQueue == null) {

                requestQueue = NoHttp.newRequestQueue();
            }
            return requestQueue;
        }

    }

    private ArrayList<ChooseCountryBean> getData(List<ChaptersList> chaptersList) {
        ArrayList<ChooseCountryBean> beans = new ArrayList<>();

        for (int i = 0; i < chaptersList.size(); i++) {
            if (chaptersList.get(i).getSectionsList().size() == 0) {
                ChooseCountryBean bean = new ChooseCountryBean();
                bean.setSort("第" + chaptersList.get(i).getChaptersSort() + "章" + "      " + chaptersList.get(i).getChaptersTitle());
                bean.setCountry("暂无相关视节数 !");
                bean.setCode("暂无相关视频课程!");
                beans.add(bean);
            }
            for (int j = 0; j < chaptersList.get(i).getSectionsList().size(); j++) {
                ChooseCountryBean bean = new ChooseCountryBean();
                bean.setCode(chaptersList.get(i).getSectionsList().get(j).getSectionsTitle());
                bean.setRuankoId(chaptersList.get(i).getSectionsList().get(j).getSectionsRuankoId());
                bean.setSectionsTitle(chaptersList.get(i).getSectionsList().get(j).getSectionsTitle());
                bean.setCountry("第" + chaptersList.get(i).getSectionsList().get(j).getSectionsSort() + "节");
                bean.setSort("第" + chaptersList.get(i).getChaptersSort() + "章" + "      " + chaptersList.get(i).getChaptersTitle());
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
    protected void onResume() {
        super.onResume();
    }
}
