package com.itee.exam.app.ui.doexam;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itee.exam.R;
import com.itee.exam.app.entity.QuestionQueryResultVO;
import com.itee.exam.app.entity.meta.QuestionType;
import com.itee.exam.app.ui.common.fragment.BaseFragment;

/**
 * Created by rkcoe on 2016/3/24.
 */
@SuppressLint("ValidFragment")
public class QuestionFragment extends BaseFragment {
    private QuestionType questionType;
    private QuestionQueryResultVO question;

    public QuestionFragment(){}

    public QuestionFragment(QuestionQueryResultVO questionQueryResultVO){
        question = questionQueryResultVO;
        if (question != null) {
            questionType = QuestionType.instance(question.getQuestionTypeId());
        }else {
            questionType = QuestionType.SINGLE_SELECTION;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        return view;
    }

    public void updateQuestion(QuestionQueryResultVO questionQueryResultVO){
        question = questionQueryResultVO;
    }

    public QuestionQueryResultVO getQuestion(){
        return question;
    }

    public String getAnswer(){
        return "A";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
