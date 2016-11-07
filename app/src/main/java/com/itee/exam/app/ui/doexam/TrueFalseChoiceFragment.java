package com.itee.exam.app.ui.doexam;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.itee.exam.R;
import com.itee.exam.app.AppContext;
import com.itee.exam.app.entity.QuestionContent;
import com.itee.exam.app.entity.QuestionQueryResultVO;
import com.itee.exam.app.ui.image.ImageActivity;
import com.itee.exam.core.utils.StringUtils;
import com.itee.exam.utils.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by rkcoe on 2016/3/24.
 */

@SuppressLint("ValidFragment")
public class TrueFalseChoiceFragment extends QuestionFragment {
    TextView tvTitle;
    ImageView imageTitle;
    View viewRightAnswer;
    RadioGroup rgSelection;
    TextView tvRightAnswer;
    TextView tvAnswerDes;
    LinkedHashMap<String,RadioButton> radioButtons = new LinkedHashMap<>();

    private String defaultAnswer;
    private String myAnswer;
    private boolean showResult = false;

    public TrueFalseChoiceFragment(){
        super();
    }

    public TrueFalseChoiceFragment(QuestionQueryResultVO questionQueryResultVO, String defaultAnswer, boolean showResult){
        super(questionQueryResultVO);
        this.defaultAnswer = defaultAnswer;
        this.showResult = showResult;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_true_false_choice, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        imageTitle = (ImageView) view.findViewById(R.id.imageTitle);
        rgSelection = (RadioGroup) view.findViewById(R.id.rgSelection);

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

        QuestionContent content = questionQueryResultVO.getQuestionContent();
        tvTitle.setText(content.getTitle());
        ImageLoader imageLoader = ImageLoader.getInstance();
        if (!StringUtils.isBlank(content.getTitleImg())){
            imageTitle.setVisibility(View.VISIBLE);
            final String path=AppContext.getImgPath(content.getTitleImg());
            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ImageActivity.class);
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
            String answer = "T".equals(questionQueryResultVO.getAnswer()) ? "正确" : "错误" ;
            tvRightAnswer.setText(answer);
            String des = questionQueryResultVO.getAnalysis();
            tvAnswerDes.setText(StringUtils.isBlank(des) ? "" : des);
        }

        LinkedHashMap<String, String> choiceList = new LinkedHashMap<>();
        choiceList.put("T","正确");
        choiceList.put("F","错误");

        radioButtons.clear();
        rgSelection.removeAllViews();
        for (Iterator<String> iterator = choiceList.keySet().iterator(); iterator.hasNext();){
            String key = iterator.next();
            RadioButton tempButton = new RadioButton(getBaseActivity());
            Resources resources = getBaseActivity().getResources();
            //tempButton.setBackgroundResource(R.drawable.xxx);   // 设置RadioButton的背景图片
            //tempButton.setButtonDrawable(R.drawable.xxx);           // 设置按钮的样式
            //tempButton.setPadding(80, 0, 0, 0);                 // 设置文字距离按钮四周的距离
            tempButton.setText(choiceList.get(key));
            tempButton.setTextColor(resources.getColor(R.color.normal_font));
            tempButton.setTextSize(resources.getDimension(R.dimen.radio_button_font));
            rgSelection.addView(tempButton, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            tempButton.setTag(key);
            if (defaultAnswer != null && defaultAnswer.equals(key)){
                tempButton.setChecked(true);
            }
        }

        rgSelection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton tempButton = (RadioButton)group.findViewById(checkedId);
                if (tempButton != null){
                    myAnswer = (String) tempButton.getTag();
                }
            }
        });
    }

    @Override
    public String getAnswer() {
        if (myAnswer == null) myAnswer = defaultAnswer;
        return myAnswer;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
