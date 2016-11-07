package com.itee.exam.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.itee.exam.R;
import com.itee.exam.app.entity.QuestionAnswer;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.app.ui.doexam.PmExamFragment;
import com.itee.exam.core.utils.Toasts;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rkcoe on 2016/9/21.
 *
 */
public class PmExamAnswerAndAnalysisActivity extends BaseActivity{

    @BindView(R.id.tvProgress)
    TextView tvProgress;

//    private final QuestionQueryResultVO question;
    private final static String THIS_LEFT = "PmExamAnswerAndAnalysisActivity";
    int curQuestionIndex = 0;
    private String url;
    private String answerAnalysis;
    private int answerNumber;
    private String answerContent;
    private int id;
    private String keyword;
    private List<QuestionAnswer> examAnswer;
    private ArrayList<String> listUrl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pm_exam_answer_analysis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("试卷解析");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
//        listUrl = getIntent().getExtras().getStringArrayList("URL");
//        answerAnalysis = getIntent().getExtras().getString("ANSWER_ANALYSIS");
//        answerNumber = getIntent().getExtras().getInt("ANSWER_NUMBER");
//        answerContent = getIntent().getExtras().getString("ANSWER_CONTENT");
//        id = getIntent().getExtras().getInt("ANSWER_ID");
//        keyword = getIntent().getExtras().getString("ANSWER_KEYWORD");
        examAnswer = (List<QuestionAnswer>) getIntent().getSerializableExtra("PM_EXAM_ANSWER");
        tvProgress.setText("" +  1 + "/" + examAnswer.size());

//        PmExamFragment pmExamFragment = new PmExamFragment(examAnswer.get(0));
        PmExamFragment pmExamFragment = PmExamFragment.newInstance(examAnswer.get(0));
        Log.e(THIS_LEFT, "pmExamFragment:"+pmExamFragment);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, pmExamFragment)
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
        if (curQuestionIndex >= examAnswer.size()) {
            curQuestionIndex = examAnswer.size() - 1;
            Toasts.showToastInfoShort(this, "已经是最后一题了");
            return;
        }
        updateAnswerProgress(R.anim.push_left_in, R.anim.push_left_out);
    }

    private void updateAnswerProgress(int in, int out) {
        if (answerNumber == 0 || curQuestionIndex < 0 || curQuestionIndex >= examAnswer.size()) return;

        int index = curQuestionIndex + 1;
        tvProgress.setText("" + index + "/" + examAnswer.size());
        QuestionAnswer questionAnswer = examAnswer.get(curQuestionIndex);
        Log.e(THIS_LEFT, "questionAnswer:"+questionAnswer.toString()+"..."+curQuestionIndex);

//        PmExamFragment pmExamFragment = new PmExamFragment(questionAnswer);
        PmExamFragment pmExamFragment = PmExamFragment.newInstance(questionAnswer);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(in, out)
                .replace(R.id.fragmentContainer, pmExamFragment )
                .commit();
    }

    public void funAnswer(View view){
        Toasts.showToastInfoLong(PmExamAnswerAndAnalysisActivity.this, "交卷点击答案");
    }
}
