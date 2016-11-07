package com.itee.exam.app.ui.doexam;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.itee.exam.R;
import com.itee.exam.app.entity.AnswerSheet;
import com.itee.exam.app.entity.AnswerSheetItem;
import com.itee.exam.app.entity.ExamPaperVO;
import com.itee.exam.app.entity.QuestionQueryResultVO;
import com.itee.exam.app.entity.meta.QuestionType;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.core.utils.ProgressTask;
import com.itee.exam.core.utils.StringUtils;
import com.itee.exam.core.utils.Toasts;
import com.itee.exam.utils.PreferenceUtil;
import com.itee.exam.vo.HttpMessage;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 答题界面
 *
 * @author moxin
 */
public class ExamAnswerActivity extends BaseActivity implements View.OnTouchListener, GestureDetector.OnGestureListener {

    public static final String EXTRA_PAPER = "paper";
    public static final String EXTRA_SHEET = "sheet";

    ExamPaperVO paper;
    List<QuestionQueryResultVO> questionList;
    AnswerSheet answerSheet;

    int curQuestionIndex = 0;

    TextView tvProgress;
    TextView tvTimer;
    TextView tvAnswerSheet;
    QuestionFragment questionFragment;
    private Timer timer;
    private int leftTime;
    private float score;
    boolean ended = false;
    private GestureDetector mGestureDetector;
    private int verticalMinDistance = 150;
    private int minVelocity = 0;

    private Toast mToast;
    private int AIRPLAY_TOAST_DISPLAY_TIME = 1000;

    public static Intent generateIntent(Context context, ExamPaperVO paper, AnswerSheet sheet) {
        Intent intent = new Intent(context, ExamAnswerActivity.class);
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_PAPER, paper);
        args.putSerializable(EXTRA_SHEET, sheet);
        intent.putExtras(args);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_answer);
        mGestureDetector = new GestureDetector(this);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            paper = (ExamPaperVO) bundle.getSerializable(EXTRA_PAPER);
            answerSheet = (AnswerSheet) bundle.getSerializable(EXTRA_SHEET);
        }
        initViews();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    private void initViews() {
        if (paper == null) return;

        questionList = paper.getQuestion_query_result();
        if (questionList == null || questionList.size() == 0) return;

        tvProgress = (TextView) findViewById(R.id.tvProgress);
        tvTimer = (TextView) findViewById(R.id.tvTimer);
        tvAnswerSheet = (TextView) findViewById(R.id.tvAnswerSheet);
        questionFragment = new QuestionFragment(questionList.get(0));

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, questionFragment)
                .commit();

        updateAnswerProgress(R.anim.push_left_in, R.anim.push_left_out);
        leftTime = paper.getDuration() * 60;

        startTimer();
    }

    private void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Date now = new Date();
                long lastTimeNum = now.getTime() - answerSheet.getStartTime().getTime();
                int duration = paper.getDuration() * 60;//秒
                leftTime = (int) (duration - lastTimeNum / 1000);
                leftTime = leftTime < 0 ? 0 : leftTime;

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        int leftMinutes = leftTime / 60;
                        int leftSeconds = leftTime % 60;
                        tvTimer.setText(getString(R.string.exam_timer, leftMinutes, leftSeconds));
                    }
                });
                if (leftTime <= 0) {
                    timer.cancel();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ExamAnswerActivity.this);
                            builder.setTitle("时间到");
                            builder.setMessage("考试时间到了，请提交试卷");
                            builder.setCancelable(false);
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    saveAnswer();
                                    uploadSheet();
                                }
                            });
                            builder.create().show();
                        }
                    });
                }
            }
        }, 0, 1000);
    }

    private void updateAnswerProgress(int in, int out) {
        if (questionList == null || questionList.size() == 0 || curQuestionIndex < 0
                || curQuestionIndex >= questionList.size()) return;

        int index = curQuestionIndex + 1;
        tvProgress.setText("" + index + "/" + questionList.size());

        QuestionQueryResultVO questtion = questionList.get(curQuestionIndex);
        String answer = null;
        if (answerSheet != null) {
            answer = answerSheet.getAnswerSheetItems().get(curQuestionIndex).getAnswer();
        }
        QuestionType type = QuestionType.instance(questtion.getQuestionTypeId());
        switch (type) {
            case SINGLE_SELECTION:
                questionFragment = new SingleChoiceFragment(questtion, answer, ended);
                break;
            case MULTIPLE_SELECTION:
                questionFragment = new MultyChoiceFragment(questtion, answer, ended);
                break;
            case TRUE_FALSE_SELECTION:
                questionFragment = new TrueFalseChoiceFragment(questtion, answer, ended);
                break;
            default:
                questionFragment = new InputFragment(questtion, answer, ended);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(in, out)
                .replace(R.id.fragmentContainer, questionFragment)
                .commit();
    }

    public void showAnswerSheet(View view) {
        if (ended) {
            showAnswerResult();
            return;
        }
        View viewBottom = findViewById(R.id.viewBottom);
        AnswerSheetPopWindow answerSheetPopWindow = new AnswerSheetPopWindow(this, answerSheet, curQuestionIndex,
                new AnswerSheetPopWindow.ClickCallBack() {
                    @Override
                    public void clicked(int index) {
                        saveAnswer();
                        curQuestionIndex = index;
                        updateAnswerProgress(R.anim.push_left_in, R.anim.push_left_out);
                    }

                });
        answerSheetPopWindow.showPopupWindow(viewBottom);
    }

    public void showAnswerResult() {
        View viewBottom = findViewById(R.id.viewBottom);
        AnswerResultPopWindow answerResultPopWindow = new AnswerResultPopWindow(this, answerSheet, curQuestionIndex, score, paper,
                new AnswerResultPopWindow.ClickCallBack() {
                    @Override
                    public void clicked(int index) {
                        curQuestionIndex = index;
                        updateAnswerProgress(R.anim.push_left_in, R.anim.push_left_out);
                    }

                });
        answerResultPopWindow.showPopupWindow(viewBottom);
    }

    /**
     * 查询未做试题数目
     * @return
     */
    private int getLeftQuestion(){
        int result = 0;
        if (answerSheet == null) return result;
        List<AnswerSheetItem> items = answerSheet.getAnswerSheetItems();
        for (AnswerSheetItem item : items){
            if (StringUtils.isBlank(item.getAnswer())){
                result ++;
            }
        }
        return result;
    }

    public void SubmitAnswerSheet(View view) {
        if (ended) {
            Toasts.showToastInfoLong(this, "考试已经结束");
        } else {
            saveAnswer();
            final int leftNum = getLeftQuestion();
            if (leftNum > 0){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setMessage("您还有" + leftNum + "道题未做，确认提交试卷吗？");
                builder.setTitle("提醒");
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                uploadSheet();
                            }
                        });
                builder.setNegativeButton("取消",null);
                builder.create().show();
            }else {
                uploadSheet();
            }
        }
    }

    private void uploadSheet() {
        new ProgressTask<HttpMessage<Float>>(this, getString(R.string.waiting)) {
            @Override
            public HttpMessage<Float> call() throws Exception {
                String userName = PreferenceUtil.getInstance().getUserName();
                String token = PreferenceUtil.getInstance().getTokenContent();
                return getAppContext().getHttpService().submitAnswerSheet(userName, token, answerSheet);
            }

            @Override
            protected void onSuccess(HttpMessage<Float> httpMessage) throws Exception {
                if ("success".equals(httpMessage.getResult())) {
                    score = httpMessage.getObject();
                    ended = true;
                    Toasts.showToastInfoShort(ExamAnswerActivity.this, "得分：" + score);
                    if (timer != null) {
                        timer.cancel();
                    }
                    tvAnswerSheet.setText("查看结果");
                    tvTimer.setText("结束");
                    curQuestionIndex = 0;
                    PreferenceUtil.getInstance().setDoExam(false);
                    updateAnswerProgress(R.anim.push_left_in, R.anim.push_left_out);
                    showAnswerResult();
                } else {
                    Toasts.showToastInfoShort(ExamAnswerActivity.this, "" + httpMessage.getMessageInfo());
                    startTimer(); //继续提示交卷
                }
            }
        }.execute();
    }

    private void saveAnswer() {
        if (answerSheet == null || answerSheet.getAnswerSheetItems() == null) return;
        answerSheet.getAnswerSheetItems().get(curQuestionIndex).setAnswer(questionFragment.getAnswer());
        PreferenceUtil.getInstance().saveAnswerSheet(answerSheet);
    }

    public void OnPreviousQuestion(View view) {
        saveAnswer();
        curQuestionIndex--;
        if (curQuestionIndex < 0) curQuestionIndex = 0;
        updateAnswerProgress(R.anim.push_right_in, R.anim.push_right_out);
    }

    public void OnNextQuestion(View view) {
        saveAnswer();
        curQuestionIndex++;
        if (curQuestionIndex >= questionList.size()) {
            curQuestionIndex = questionList.size() - 1;
            Toasts.showToastInfoShort(this, "已经是最后一题了");
        }
        updateAnswerProgress(R.anim.push_left_in, R.anim.push_left_out);

    }

    public void OnBack(View view) {
        finish();
    }

    public void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(this,text, Toast.LENGTH_SHORT);
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
            saveAnswer();
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
            saveAnswer();
            curQuestionIndex--;
            if (curQuestionIndex < 0) {
                curQuestionIndex = 0;
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