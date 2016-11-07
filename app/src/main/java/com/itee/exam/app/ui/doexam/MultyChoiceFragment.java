package com.itee.exam.app.ui.doexam;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
public class MultyChoiceFragment extends QuestionFragment {
    TextView tvTitle;
    ImageView imageTitle;
    View viewRightAnswer;
    LinearLayout rgSelection;
    TextView tvRightAnswer;
    TextView tvAnswerDes;
    LinkedHashMap<String,RadioButton> radioButtons = new LinkedHashMap<>();

    private String defaultAnswer;
    private String myAnswer;
    private boolean showResult = false;

    public MultyChoiceFragment(){
        super();
    }

    public MultyChoiceFragment(QuestionQueryResultVO questionQueryResultVO, String defaultAnswer, boolean showResult){
        super(questionQueryResultVO);
        this.defaultAnswer = defaultAnswer;
        this.showResult = showResult;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multy_choice, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        imageTitle = (ImageView) view.findViewById(R.id.imageTitle);
        rgSelection = (LinearLayout)view.findViewById(R.id.rgSelection);

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

        final QuestionContent content = questionQueryResultVO.getQuestionContent();
        tvTitle.setText(content.getTitle());
        ImageLoader imageLoader = ImageLoader.getInstance();
        if (!StringUtils.isBlank(content.getTitleImg())){
            imageTitle.setVisibility(View.VISIBLE);
            final String path=AppContext.getImgPath(content.getTitleImg());
            tvTitle.setOnClickListener(new View.OnClickListener() {
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

        LinkedHashMap<String, String> choiceList = content.getChoiceList();
        LinkedHashMap<String, String> choiceImgList = content.getChoiceImgList();
        if (choiceList == null || choiceList.size() == 0) return;
        radioButtons.clear();
        rgSelection.removeAllViews();
        for (Iterator<String> iterator = choiceList.keySet().iterator(); iterator.hasNext();){
            String key = iterator.next();
            CheckBox tempButton = new CheckBox(getBaseActivity());
            Resources resources = getBaseActivity().getResources();
            //tempButton.setBackgroundResource(R.drawable.xxx);   // 设置RadioButton的背景图片
            //tempButton.setButtonDrawable(R.drawable.xxx);           // 设置按钮的样式
            tempButton.setGravity(Gravity.TOP);
            tempButton.setPadding(0, 8, 0, 0);                 // 设置文字距离按钮四周的距离
            tempButton.setText(key + ". " + choiceList.get(key));
            tempButton.setTextColor(resources.getColor(R.color.normal_font));
            tempButton.setTextSize(resources.getDimension(R.dimen.radio_button_font));
            rgSelection.addView(tempButton, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            tempButton.setTag(key);
            if (defaultAnswer != null && defaultAnswer.indexOf(key) >= 0){
                tempButton.setChecked(true);
            }
            if (choiceImgList != null && choiceImgList.containsKey(key) && choiceImgList.get(key) != null){
                ImageView imageView = new ImageView(getBaseActivity());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(5, 5, 5, 5);
                imageView.setLayoutParams(lp);
                imageView.setAdjustViewBounds(true);
                imageView.setMaxHeight(200);
                imageView.setMinimumHeight(50);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                final String path=AppContext.getImgPath(choiceImgList.get(key));
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),ImageActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("Path",path);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                rgSelection.addView(imageView);
                imageLoader.displayImage(path, imageView, ImageUtils.getDisplayImageOptions());
            }
        }
    }

    @Override
    public String getAnswer() {
        if (rgSelection == null || rgSelection.getChildCount() == 0) return defaultAnswer;
        int count = rgSelection.getChildCount();
        myAnswer = "";
        for (int i = 0; i< count; i++){
            CheckBox checkBox = (CheckBox) rgSelection.getChildAt(i);
            String answer = (String) checkBox.getTag();
            if (checkBox.isChecked()) myAnswer = myAnswer + answer;
        }
        return myAnswer;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
