package com.itee.exam.app.ui.doexam;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.itee.exam.R;
import com.itee.exam.app.entity.AnswerSheet;
import com.itee.exam.app.entity.ExamPaperVO;
import com.itee.exam.app.entity.QuestionAnalysis;
import com.itee.exam.app.entity.QuestionAnswer;
import com.itee.exam.app.entity.QuestionQueryResultVO;
import com.itee.exam.app.ui.PmExamAnswerAndAnalysisActivity;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.core.utils.ProgressTask;
import com.itee.exam.core.utils.Toasts;
import com.itee.exam.utils.PreferenceUtil;
import com.itee.exam.vo.HttpMessage;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AfterExamActivity extends BaseActivity implements View.OnTouchListener, GestureDetector.OnGestureListener {
    public static final String EXTRA_PAPER = "paper";
    public static final String EXTRA_SHEET = "sheet";

    private ExamPaperVO paper;
    private AnswerSheet answerSheet;
    private List<QuestionQueryResultVO> questionList;
    private int curQuestionIndex = 0;
    private int leftTime;
    private Timer timer;

    private TextView tvProgress;
    private TextView tvTimer;

    private AfternoonExamFragment questionFragment;

    private GestureDetector mGestureDetector;
    private int verticalMinDistance = 150;
    private int minVelocity = 0;
    private Toast mToast;
    private int AIRPLAY_TOAST_DISPLAY_TIME = 1000;
    private String THIS_LEFT = "AfterExamActivity";
    private String result;
    ArrayList<String> list = new ArrayList<>();


    public static Intent generateIntent(Context context, ExamPaperVO paper, AnswerSheet sheet) {
        Intent intent = new Intent(context, AfterExamActivity.class);
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_PAPER, paper);
        args.putSerializable(EXTRA_SHEET, sheet);
        intent.putExtras(args);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_exam);
        mGestureDetector = new GestureDetector(this);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            paper = (ExamPaperVO) bundle.getSerializable(EXTRA_PAPER);
            answerSheet = (AnswerSheet) bundle.getSerializable(EXTRA_SHEET);
        }
        initViews();
    }

    private void initViews() {
        if (paper == null) return;

        questionList = paper.getQuestion_query_result();
        if (questionList == null || questionList.size() == 0) return;

        tvProgress = (TextView) findViewById(R.id.tvProgress);
        tvTimer = (TextView) findViewById(R.id.tvTimer);
        questionFragment = new AfternoonExamFragment(questionList.get(0));
        Log.e(THIS_LEFT, "questionList.get(0):" + questionList.get(0).toString());

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, questionFragment)
                .commit();

        updateAnswerProgress(R.anim.push_left_in, R.anim.push_left_out);
        leftTime = paper.getDuration() * 60;
        startTimer();
    }

    private void updateAnswerProgress(int in, int out) {
        if (questionList == null || questionList.size() == 0 || curQuestionIndex < 0
                || curQuestionIndex >= questionList.size()) return;

        int index = curQuestionIndex + 1;
        tvProgress.setText("" + index + "/" + questionList.size());

        QuestionQueryResultVO questtion = questionList.get(curQuestionIndex);
        Log.e(THIS_LEFT, "questtion:" + questtion.toString());

        questionFragment = new AfternoonExamFragment(questtion);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(in, out)
                .replace(R.id.fragmentContainer, questionFragment)
                .commit();
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(AfterExamActivity.this);
                            builder.setTitle("时间到");
                            builder.setMessage("考试时间到了，请提交试卷");
                            builder.setCancelable(false);
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    uploadAnswerSheet();
                                }
                            });
                            builder.create().show();
                        }
                    });
                }
            }
        }, 0, 1000);
    }

    public void SubmitAnswerSheet(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("确认提交试卷吗？");
        builder.setTitle("提醒");
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uploadAnswerSheet();
                    }
                });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    private void uploadAnswerSheet() {
        new ProgressTask<HttpMessage>(this, getString(R.string.waiting)) {
            @Override
            public HttpMessage call() throws Exception {
                String userName = PreferenceUtil.getInstance().getUserName();
                String token = PreferenceUtil.getInstance().getTokenContent();
                return getAppContext().getHttpService().submitAfternoonPaper(userName, token, answerSheet);
            }

            @Override
            protected void onSuccess(HttpMessage httpMessage) throws Exception {
                result = httpMessage.getResult();
                if ("success".equals(result)) {
                    if (timer != null) {
                        timer.cancel();
                    }
//                    tvTimer.setText("结束");
                    findViewById(R.id.btn_submit_lay).setVisibility(View.INVISIBLE);
//                    findViewById(R.id.iv_analysis).setVisibility(View.VISIBLE);
                    findViewById(R.id.btn_timer_clock).setVisibility(View.INVISIBLE);
                    findViewById(R.id.btn_answer_sheet).setVisibility(View.VISIBLE);
                    curQuestionIndex = 0;
                    PreferenceUtil.getInstance().setDoExam(false);
                    updateAnswerProgress(R.anim.push_left_in, R.anim.push_left_out);
                } else {
                    Toasts.showToastInfoShort(AfterExamActivity.this, "交卷失败");
                    startTimer(); //继续提示交卷
                }
            }
        }.execute();
    }

    public void showAnswerSheet(View view) {
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
        dialogFragment.setOnCompleteListener(new AnswerDialogFragment.onCompleteListener() {
            @Override
            public void onCompleted(String answer) {
                if (!"ERROR".equals(answer)) {
                    saveAnswer(answer);
                }
            }
        });
        Bundle bundle = new Bundle();
        bundle.putInt("SIZE", size);
        bundle.putString("Answer", answer);
        bundle.putInt("Modle", 0);
        bundle.putString("Result", result);
        Log.e(THIS_LEFT," :"+result);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getFragmentManager(), "AnswerFragmentDialog");
    }

    private void saveAnswer(String answer) {
        if (answerSheet == null || answerSheet.getAnswerSheetItems() == null) return;
        answerSheet.getAnswerSheetItems().get(curQuestionIndex).setAnswer(answer);
        PreferenceUtil.getInstance().saveAnswerSheet(answerSheet);
    }

    public void funAnalysis(View view) {
        answerKey();
//        QuestionQueryResultVO question = questionList.get(curQuestionIndex);
//        String path = question.getQuestionAnalysis().getFileUrl();
//        path = path.replace("\\", "/");
//        path = AppContext.SERVER_IMG_URL + "/" + path;
//        Intent intent = new Intent(this, AnalysisActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("URL", path);
//        intent.putExtras(bundle);
//        startActivity(intent);
    }

    private void answerKey() {
        new ProgressTask<HttpMessage<List<QuestionAnswer>>>(AfterExamActivity.this, getString(R.string.getting_analysis)) {
            @Override
            public HttpMessage<List<QuestionAnswer>> call() throws Exception {
                QuestionQueryResultVO question = questionList.get(curQuestionIndex);
//                Log.e(THIS_LEFT, "questionId :"+question.getQuestionId());
                return getAppContext().getHttpService().getAnswerListByQuestionId(question.getQuestionId());
//                return getAppContext().getHttpService().getAnswerListByQuestionId(95);
            }

            @Override
            protected void onSuccess(HttpMessage<List<QuestionAnswer>> listHttpMessage) throws Exception {
                if ("success".equals(listHttpMessage.getResult())) {
                    List<QuestionAnswer> pmExamAnswer = listHttpMessage.getObject();
                    Log.e(THIS_LEFT, "pmExamAnswer:" + pmExamAnswer);
                    if (pmExamAnswer != null && pmExamAnswer.size() > 0) {
//                        String[] answerKey = new String[pmExamAnswer.size()];
                        String answerAnalysis = pmExamAnswer.get(curQuestionIndex).getAnswerAnalysis();
                        int answerNumber = pmExamAnswer.get(curQuestionIndex).getAnswerNumber();
                        String answerTitle = pmExamAnswer.get(curQuestionIndex).getAnswerTitle();
                        String answerContent = pmExamAnswer.get(curQuestionIndex).getAnswerContent();
                        QuestionAnalysis questionAnalysis = (QuestionAnalysis) pmExamAnswer.get(curQuestionIndex).getQuestionAnalysis();
                        int id = pmExamAnswer.get(curQuestionIndex).getId();
                        String keyword = pmExamAnswer.get(curQuestionIndex).getKeyword();
                        Log.e(THIS_LEFT, " :" + answerAnalysis + ".." + answerNumber + ".." + answerTitle + ".." + answerContent);
                        Iterator iterator = jsonToObject(answerAnalysis).entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<String, String> entry= (Map.Entry<String, String>) iterator.next();
                            Log.e(THIS_LEFT, "key = " + entry.getKey() + "....." + "value = " + entry.getValue());
                            if (entry.getKey().equals("fileUrl")) {
                                list.add(entry.getValue());
                            }
                        }
                        initPmExamAnswerAndAnalysis(answerAnalysis, answerNumber, answerTitle, answerContent, id, keyword,
                                pmExamAnswer, list);
                    }
                    Toasts.showToastInfoShort(AfterExamActivity.this, "请求成功");
                } else if ("no-answer".equals(listHttpMessage.getResult())) {
                    mHandler.sendEmptyMessage(1);
                } else {
                    Toasts.showToastInfoShort(AfterExamActivity.this, "请求失败");
                }
            }

        }.execute();
    }

    /**
     *  Answer Keys
     * @param answerAnalysis
     * @param answerNumber
     * @param answerTitle
     * @param answerContent
     * @param id
     * @param keyword
     * @param pmExamAnswer
     */

    private void initPmExamAnswerAndAnalysis(String answerAnalysis, int answerNumber, String answerTitle, String answerContent,
                                             int id, String keyword, List<QuestionAnswer> pmExamAnswer, ArrayList<String> list) {
        Intent intent = new Intent(AfterExamActivity.this, PmExamAnswerAndAnalysisActivity.class);
        Bundle bundle = new Bundle();
//        bundle.putStringArrayList("URL",list);
//        bundle.putString("ANSWER_ANALYSIS", answerAnalysis);
//        bundle.putInt("ANSWER_NUMBER", answerNumber);
//        bundle.putString("ANSWER_CONTENT", answerContent);
//        bundle.putString("ANSWER_TITLE", answerTitle);
//        bundle.putString("ANSWER_CONTENT", answerContent);
//        bundle.putInt("ANSWER_ID", id);
//        bundle.putString("ANSWER_KEYWORD", keyword);
        bundle.putSerializable("PM_EXAM_ANSWER", (Serializable) pmExamAnswer);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * jsonToMap
     *
     * @param jsonStr
     * @return
     * @throws Exception
     */

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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 1:
                    Toasts.showToastInfoShort(AfterExamActivity.this, "无试卷解析");
                    break;
            }
        }
    };

    public void OnBack(View view) {
        finish();
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
