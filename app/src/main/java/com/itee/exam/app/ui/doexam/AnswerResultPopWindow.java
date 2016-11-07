package com.itee.exam.app.ui.doexam;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.itee.exam.R;
import com.itee.exam.app.entity.AnswerSheet;
import com.itee.exam.app.entity.AnswerSheetItem;
import com.itee.exam.app.entity.ExamPaperVO;
import com.itee.exam.app.ui.common.view.FlowLayout;

public class AnswerResultPopWindow extends PopupWindow {
    private View conentView;
    private Activity context;
    private AnswerSheet answerSheet;
    private int selectedIndex = 0;
    private ClickCallBack clickCallBack;
    private TextView tvDes;
    private TextView tvScore;
    private ExamPaperVO paper;
    private float score;

    @SuppressLint("InflateParams")
    public AnswerResultPopWindow(final Activity context, AnswerSheet answerSheet,int selectedIndex, float score, ExamPaperVO paper,final ClickCallBack clickCallBack) {
        this.answerSheet = answerSheet;
        this.context = context;
        this.selectedIndex = selectedIndex;
        this.clickCallBack = clickCallBack;
        this.score = score;
        this.paper = paper;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.popupwindow_answer_result, null);

        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);

        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);

        initView();
    }

    private void initView(){
        updateView();
    }

    private void updateView(){
        if (conentView == null) return;

        Resources res = context.getResources();
        tvDes = (TextView) conentView.findViewById(R.id.tvDes);
        tvDes.setText(res.getString(R.string.exam_result_tip,paper.getTotal_point(),paper.getPass_point()));

        tvScore = (TextView) conentView.findViewById(R.id.tvScore);
        tvScore.setText(res.getString(R.string.exam_result_score,score));

        FlowLayout flSheet = (FlowLayout) conentView.findViewById(R.id.flSheet);
        flSheet.removeAllViews();

        if (answerSheet == null || answerSheet.getAnswerSheetItems() == null ||
                paper == null || paper.getQuestion_query_result() == null) return;
        int size  = answerSheet.getAnswerSheetItems().size();

        for (int i = 0; i < size; i++){
            AnswerSheetItem item = answerSheet.getAnswerSheetItems().get(i);
            String answer = item.getAnswer();
            boolean isRight = answer == null ? false : answer.equals(paper.getQuestion_query_result().get(i).getAnswer());

            TextView textView = new TextView(context);
            int width = (int)res.getDimension(R.dimen.answer_sheet_btn_size);
            int height = (int)res.getDimension(R.dimen.answer_sheet_btn_size);
            FlowLayout.LayoutParams lp = new FlowLayout.LayoutParams(width, height);
            lp.setMargins(20, 20, 0, 0);
            textView.setLayoutParams(lp);
            textView.setGravity(Gravity.CENTER);
            int index = i + 1;
            textView.setText("" + index);
            textView.setTextSize(res.getDimension(R.dimen.radio_button_font));
            textView.setTextColor(res.getColor(R.color.white));

            if (isRight){
                textView.setBackgroundColor(res.getColor(R.color.Green));
            }else{
                textView.setBackgroundColor(res.getColor(R.color.Red));
            }
            if (i == selectedIndex) textView.setTextColor(res.getColor(R.color.orange));
            textView.setTag(i);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedIndex = (int) v.getTag();
                    updateView();
                    if (clickCallBack != null) {
                        clickCallBack.clicked(selectedIndex);
                    }
                }
            });

            flSheet.addView(textView);
        }
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            //
            // this.showAsDropDown(parent, 0, 0);
            // this.showAtLocation(parent, gravity, x, y);
            int[] location = new int[2];
            parent.getLocationOnScreen(location);
            this.showAtLocation(parent, Gravity.NO_GRAVITY,
                    location[0] , location[1] - this.getHeight());
        } else {
            this.dismiss();
        }
    }

    public interface ClickCallBack {
        void clicked(int index);
    }
}