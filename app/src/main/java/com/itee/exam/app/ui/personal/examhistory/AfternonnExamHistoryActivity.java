package com.itee.exam.app.ui.personal.examhistory;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.itee.exam.R;
import com.itee.exam.app.AppContext;
import com.itee.exam.app.entity.AnswerSheet;
import com.itee.exam.app.entity.ExamHistoryVO;
import com.itee.exam.app.entity.QuestionQueryResultVO;
import com.itee.exam.app.entity.User;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.app.ui.doexam.AfternoonExamFragment;
import com.itee.exam.app.ui.doexam.AnalysisActivity;
import com.itee.exam.app.ui.doexam.AnswerDialogFragment;
import com.itee.exam.app.ui.doexam.AnswerSheetPopWindow;
import com.itee.exam.core.utils.JupiterAsyncTask;
import com.itee.exam.core.utils.Toasts;
import com.itee.exam.utils.PreferenceUtil;
import com.itee.exam.vo.HttpMessage;

import java.util.List;

public class AfternonnExamHistoryActivity extends BaseActivity implements View.OnTouchListener, GestureDetector.OnGestureListener{
    private int hisId;
    private String examName;
    private String userName;
    private String appToken;

    private int curQuestionIndex = 0;
    private AnswerSheet answerSheet;
    private TextView tvProgress;
    private List<QuestionQueryResultVO> questionList;
    private AfternoonExamFragment questionFragment;

    private GestureDetector mGestureDetector;
    private int verticalMinDistance = 150;
    private int minVelocity = 0;
    private Toast mToast;
    private int AIRPLAY_TOAST_DISPLAY_TIME = 1000;

    private boolean load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afternonn_exam_history);
        Bundle bundle = getIntent().getExtras();
        hisId = bundle.getInt("hisId", -1);
        examName = bundle.getString("examName", "考试历史详情");
        mGestureDetector = new GestureDetector(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(examName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        User user = PreferenceUtil.getInstance().getUser();
        userName = user.getUserName();
        load=true;
    }

    @Override
    protected void onResume() {
        if(load){
            loadData();
            load=false;
        }
        super.onResume();
    }

    private void loadData(){
        appToken = PreferenceUtil.getInstance().getAppToken().getAppToken();
        new JupiterAsyncTask<HttpMessage<ExamHistoryVO>>(this) {

            @Override
            protected void onPreExecute() throws Exception {
                setShowExceptionTip(true);
            }

            @Override
            public HttpMessage<ExamHistoryVO> call() throws Exception {
                return getAppContext().getHttpService().getExamHistoryAfternoon(hisId, appToken, userName);
            }

            @Override
            protected void onSuccess(HttpMessage<ExamHistoryVO> examHistoryVO) throws Exception {
                ExamHistoryVO history = examHistoryVO.getObject();
                questionList = history.getQuestion_query_result();
                answerSheet = history.getAnswerSheet();
                tvProgress = (TextView) findViewById(R.id.tvProgress);
                questionFragment = new AfternoonExamFragment(questionList.get(0));
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragmentContainer, questionFragment)
                        .commit();

                updateAnswerProgress(R.anim.push_left_in, R.anim.push_left_out);
            }

        }.execute();
    }

    private void updateAnswerProgress(int in, int out) {
        if (questionList == null || questionList.size() == 0 || curQuestionIndex < 0
                || curQuestionIndex >= questionList.size()) return;

        int index = curQuestionIndex + 1;
        tvProgress.setText("" + index + "/" + questionList.size());

        QuestionQueryResultVO questtion = questionList.get(curQuestionIndex);
        questionFragment = new AfternoonExamFragment(questtion);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(in, out)
                .replace(R.id.fragmentContainer, questionFragment)
                .commit();
    }

    public void OnPreviousQuestion(View view) {
        curQuestionIndex--;
        if (curQuestionIndex < 0) {
            curQuestionIndex = 0;
            Toasts.showToastInfoShort(this, "已经是第一题了");
            return;
        }
        updateAnswerProgress(R.anim.push_right_in, R.anim.push_right_out);
    }

    public void OnNextQuestion(View view) {
        curQuestionIndex++;
        if (curQuestionIndex >= questionList.size()) {
            curQuestionIndex = questionList.size() - 1;
            Toasts.showToastInfoShort(this, "已经是最后一题了");
            return;
        }
        updateAnswerProgress(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void funAnswer(View view) {
        String answer = null;
        if (answerSheet != null) {
            answer = answerSheet.getAnswerSheetItems().get(curQuestionIndex).getAnswer();
        }
        QuestionQueryResultVO question = questionList.get(curQuestionIndex);
        int size = Integer.valueOf(question.getQuestionContent().getSpaceCount());
        AnswerDialogFragment dialogFragment = new AnswerDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("SIZE", size);
        bundle.putString("Answer",answer);
        bundle.putInt("Modle",1);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getFragmentManager(), "AnswerFragmentDialog");
    }

    public void showAnswerSheet(View view){
        View viewBottom = findViewById(R.id.viewBottom);
        AnswerSheetPopWindow answerSheetPopWindow = new AnswerSheetPopWindow(this, answerSheet, curQuestionIndex,
                new AnswerSheetPopWindow.ClickCallBack() {
                    @Override
                    public void clicked(int index) {
                        curQuestionIndex = index;
                        updateAnswerProgress(R.anim.push_right_in, R.anim.push_right_out);
                    }
                });
        answerSheetPopWindow.showPopupWindow(viewBottom);
    }

    public void funAnalysis(View view) {
        QuestionQueryResultVO question = questionList.get(curQuestionIndex);
        String path = question.getQuestionAnalysis().getFileUrl();
        path = path.replace("\\", "/");
        path = AppContext.SERVER_IMG_URL + "/" + path;
        Intent intent = new Intent(this, AnalysisActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("URL", path);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    private Handler m_Handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    cancelToast();
                    break;
            }
        }
    };

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {
            curQuestionIndex++;
            if (curQuestionIndex >= questionList.size()) {
                curQuestionIndex = questionList.size() - 1;
                showToast("已经是最后一题了");
                Message delayMsg = m_Handler.obtainMessage(1);
                m_Handler.sendMessageDelayed(delayMsg, AIRPLAY_TOAST_DISPLAY_TIME);
            } else {
                updateAnswerProgress(R.anim.push_left_in, R.anim.push_left_out);
            }
        } else if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {
            curQuestionIndex--;
            if (curQuestionIndex < 0) {
                curQuestionIndex = 0;
                showToast("已经是第一题了");
            } else {
                updateAnswerProgress(R.anim.push_right_in, R.anim.push_right_out);
            }
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent arg0) {
    }

    @Override
    public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent arg0) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent arg0) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent arg0) {
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mGestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }
}
