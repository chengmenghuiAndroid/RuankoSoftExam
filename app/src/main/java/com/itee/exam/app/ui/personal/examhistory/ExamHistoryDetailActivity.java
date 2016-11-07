package com.itee.exam.app.ui.personal.examhistory;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.itee.exam.R;
import com.itee.exam.app.entity.AnswerSheet;
import com.itee.exam.app.entity.ExamHistoryVO;
import com.itee.exam.app.entity.QuestionQueryResultVO;
import com.itee.exam.app.entity.User;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.app.ui.personal.adapter.ExamHistoryQuestionAdapter;
import com.itee.exam.app.widget.RichText;
import com.itee.exam.app.widget.VoteSubmitViewPager;
import com.itee.exam.core.utils.JupiterAsyncTask;
import com.itee.exam.utils.PreferenceUtil;
import com.itee.exam.vo.HttpMessage;

import java.lang.reflect.Field;
import java.util.List;

public class ExamHistoryDetailActivity extends BaseActivity {
    private int hisId;
    private String examName;
    private String userName;
    private String appToken;

    public int position;

    private VoteSubmitViewPager viewPager;
    private ExamHistoryQuestionAdapter adapter;

    private boolean load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_history_detail);
        Bundle bundle = getIntent().getExtras();
        hisId = bundle.getInt("hisId", -1);
        examName = bundle.getString("examName", "考试历史详情");
        load=true;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(examName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        User user = PreferenceUtil.getInstance().getUser();
        userName = user.getUserName();
        viewPager = (VoteSubmitViewPager) findViewById(R.id.exam_history_question_page);
        initViewPagerScroll();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i("pkwsh","onPageScrolled:"+position);
            }

            @Override
            public void onPageSelected(int position) {
                ExamHistoryDetailActivity.this.position=position;
                Log.i("pkwsh","onPageSelected:"+position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("pkwsh","onPageScrollStateChanged:"+state);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(load){
            loadData();
            load=false;
        }
    }

    RichText.ImageFixListener listener = new RichText.ImageFixListener() {
        @Override
        public void onFix(RichText.ImageHolder holder) {
            if (holder.getWidth() > 100 || holder.getHeight() > 100) {
                int width = getScreenWidth(getApplicationContext());
                int height = (int) (holder.getHeight() * 1f * width / holder
                        .getWidth()) - 300;
                holder.setWidth(width);
                holder.setHeight(height);
                holder.setScaleType(RichText.ImageHolder.CENTER_INSIDE);
            }
        }
    };

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.widthPixels;
    }

    private void loadData() {
        appToken = PreferenceUtil.getInstance().getAppToken().getAppToken();
        new JupiterAsyncTask<HttpMessage<ExamHistoryVO>>(this) {

            @Override
            protected void onPreExecute() throws Exception {
                setShowExceptionTip(true);
            }

            @Override
            public HttpMessage<ExamHistoryVO> call() throws Exception {
                return getAppContext().getHttpService().getExamHistoryByHisId(hisId, appToken, userName);
            }

            @Override
            protected void onSuccess(HttpMessage<ExamHistoryVO> examHistoryVO) throws Exception {
                ExamHistoryVO history = examHistoryVO.getObject();
                List<QuestionQueryResultVO> question_query_result = history.getQuestion_query_result();
                AnswerSheet answerSheet = history.getAnswerSheet();
                adapter = new ExamHistoryQuestionAdapter(ExamHistoryDetailActivity.this, question_query_result,answerSheet,listener);
                viewPager.setAdapter(adapter);
                viewPager.setScrollable(true);
                viewPager.getParent().requestDisallowInterceptTouchEvent(false);
            }

        }.execute();
    }

    private void initViewPagerScroll() {
        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ViewPagerScroller scroller = new ViewPagerScroller(
                    viewPager.getContext());
            mScroller.set(viewPager, scroller);
        } catch (NoSuchFieldException e) {

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        }
    }

    public void setCurrentView(int index) {
        viewPager.setCurrentItem(index);
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        setCurrentView(position);
    }
}
