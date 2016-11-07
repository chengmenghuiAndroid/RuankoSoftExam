package com.itee.exam.app.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.itee.exam.R;
import com.itee.exam.app.entity.Courses;
import com.itee.exam.app.entity.LearningCentreAdapter;
import com.itee.exam.app.interf.Subscriber;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.app.ui.event.UserIdEvent;
import com.itee.exam.core.utils.ProgressTask;
import com.itee.exam.core.utils.Toasts;
import com.itee.exam.vo.HttpMessage;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;


/**
 * Created by rkcoe on 2016/10/24.
 */

public class LearningCentreActivity extends BaseActivity implements WaveSwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.main_list)
    ListView mListView;
    private static String THIS_LEFT = "LearningCentreActivity";
    private int userId;
    private List<Courses> courses;
    private LearningCentreAdapter learningCentreAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_learning_centry);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        EventBus.getDefault().registerSticky(this);
        initView();
        initDate();
    }

    private void initDate() {
        getCourses();
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int coursesIndex = position++;
            Log.e(THIS_LEFT, "coursesIndex"+coursesIndex);
            Courses coursesItem = courses.get(coursesIndex);
            Intent intent = new Intent(LearningCentreActivity.this, VideoCourseActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("coursesItem", coursesItem);
            bundle.putInt("coursesIndex", coursesIndex);
            intent.putExtras(bundle);
            startActivity(intent);
//            openActivity(HPlayerActivity.class);
        }
    };

    private void initView() {

    }

    @Override
    public void onRefresh() {
        onRefresh();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getCourses();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscriber
    public void onEvent(UserIdEvent eventUserId) {
        userId = eventUserId.getResult();
    }

    private void getCourses() {
        new ProgressTask<HttpMessage<List<Courses>>>(this, "正在获取课程视频列表") {

            @Override
            public HttpMessage<List<Courses>> call() throws Exception {
                Log.e(THIS_LEFT, "userId:" + userId);
                return getAppContext().getHttpService().getUserCourseListByUserId(userId);
            }

            @Override
            protected void onSuccess(HttpMessage<List<Courses>> coursesHttpMessage) throws Exception {
                super.onSuccess(coursesHttpMessage);
                if ("success".equals(coursesHttpMessage.getResult())) {
                    courses = coursesHttpMessage.getObject();
//                    Log.e(THIS_LEFT, "courses:" + courses);
                    if (courses != null && !courses.isEmpty()) {
                        learningCentreAdapter = new LearningCentreAdapter(LearningCentreActivity.this, courses);
                        if (learningCentreAdapter == null) {
                            Toasts.showToastInfoLong(LearningCentreActivity.this, "操作失败");
                            Log.e(THIS_LEFT, "learningCentreAdapter:" + learningCentreAdapter);
                            return;
                        } else {
                            mListView.setAdapter(learningCentreAdapter);
                            mListView.setOnItemClickListener(onItemClickListener);
                        }
                    }

                } else if ("no-course".equals(coursesHttpMessage.getResult())) {
                    Toasts.showToastInfoShort(LearningCentreActivity.this, coursesHttpMessage.getMessageInfo());
                    return;
                }
            }

        }.execute();
    }

}

