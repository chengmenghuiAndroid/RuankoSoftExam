package com.itee.exam.app.ui.doexam;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.itee.exam.R;
import com.itee.exam.app.AppContext;
import com.itee.exam.app.entity.QuestionContent;
import com.itee.exam.app.entity.QuestionQueryResultVO;
import com.itee.exam.app.entity.meta.QuestionType;
import com.itee.exam.app.ui.image.ImageActivity;
import com.itee.exam.core.utils.StringUtils;
import com.itee.exam.utils.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by rkcoe on 2016/3/24.
 */

@SuppressLint("ValidFragment")
public class InputFragment extends QuestionFragment {
    TextView tvTitle;
    ImageView imageTitle;
    TextView tvType;
    EditText etAnswer;
    View viewRightAnswer;
    TextView tvRightAnswer;
    TextView tvAnswerDes;

    private String defaultAnswer;
    private boolean showResult = false;

    public InputFragment(){
        super();
    }

    public InputFragment(QuestionQueryResultVO questionQueryResultVO, String defaultAnswer, boolean showResult){
        super(questionQueryResultVO);
        this.defaultAnswer = defaultAnswer;
        this.showResult = showResult;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        imageTitle = (ImageView) view.findViewById(R.id.imageTitle);
        tvType = (TextView) view.findViewById(R.id.tvType);
        etAnswer = (EditText) view.findViewById(R.id.etAnswer);

        tvRightAnswer =(TextView) view.findViewById(R.id.tvRightAnswer);
        tvAnswerDes =(TextView) view.findViewById(R.id.tvAnswerDes);
        viewRightAnswer = view.findViewById(R.id.viewRightAnswer);
        if (showResult){
            viewRightAnswer.setVisibility(View.VISIBLE);
        }else{
            viewRightAnswer.setVisibility(View.GONE);
        }
        updateQuestion(getQuestion());
    }

    @Override
    public void updateQuestion(QuestionQueryResultVO questionQueryResultVO) {
        super.updateQuestion(questionQueryResultVO);

        QuestionType type = QuestionType.instance(questionQueryResultVO.getQuestionTypeId());
        tvType.setText(type.toString());
        QuestionContent content = questionQueryResultVO.getQuestionContent();
        tvTitle.setText(content.getTitle());
        ImageLoader imageLoader = ImageLoader.getInstance();
        if (!StringUtils.isBlank(content.getTitleImg())){
            imageTitle.setVisibility(View.VISIBLE);
            final String path=AppContext.getImgPath(content.getTitleImg());
            imageTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),ImageActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Path",path);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            imageLoader.displayImage(path, imageTitle, ImageUtils.getDisplayImageOptions());
        }else{
            imageTitle.setVisibility(View.GONE);
        }
        if(showResult){
            tvRightAnswer.setText(questionQueryResultVO.getAnswer());
            String des = questionQueryResultVO.getAnalysis();
            tvAnswerDes.setText(StringUtils.isBlank(des) ? "" : des);
        }
        etAnswer.setText(defaultAnswer);
    }

    @Override
    public String getAnswer() {
        return etAnswer.getText().toString();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
