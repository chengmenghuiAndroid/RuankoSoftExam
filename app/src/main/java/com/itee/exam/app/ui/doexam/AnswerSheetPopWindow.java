package com.itee.exam.app.ui.doexam;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.itee.exam.R;
import com.itee.exam.app.entity.AnswerSheet;
import com.itee.exam.app.entity.AnswerSheetItem;
import com.itee.exam.app.ui.common.view.FlowLayout;
import com.itee.exam.core.utils.StringUtils;

public class AnswerSheetPopWindow extends PopupWindow {
    private View conentView;
    private Activity context;
    private AnswerSheet answerSheet;
    private int selectedIndex = 0;
    private ClickCallBack clickCallBack;

    public AnswerSheetPopWindow(){
    }

    @SuppressLint("InflateParams")
    public AnswerSheetPopWindow(final Activity context,AnswerSheet answerSheet,int selectedIndex,final ClickCallBack clickCallBack) {
        this.answerSheet = answerSheet;
        this.context = context;
        this.selectedIndex = selectedIndex;
        this.clickCallBack = clickCallBack;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.popupwindow_answer_sheet, null);

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

//        LinearLayout ll_zan = (LinearLayout) conentView.findViewById(R.id.ll_zan);
//        LinearLayout ll_pl = (LinearLayout) conentView.findViewById(R.id.ll_pl);
//        TextView tv_good = (TextView) conentView.findViewById(R.id.tv_good);
//        tv_good.setText((String) iv_temp.getTag());
//        ll_zan.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                clickCallBack.clicked(1);
//                AddPopWindow.this.dismiss();
//            }
//
//        });
//        ll_pl.setOnClickListener(new OnClickListener() {
//            // 扫一扫 ，调出扫二维码 gongfan
//            @Override
//            public void onClick(View v) {
//                clickCallBack.clicked(2);
//                AddPopWindow.this.dismiss();
//            }
//
//        });
    }

    private void initView(){
        updateView();
    }

    private void updateView(){
        if (conentView == null) return;

        FlowLayout flSheet = (FlowLayout) conentView.findViewById(R.id.flSheet);
        flSheet.removeAllViews();

        if (answerSheet == null || answerSheet.getAnswerSheetItems() == null) return;
        int size  = answerSheet.getAnswerSheetItems().size();

        for (int i = 0; i < size; i++){
            AnswerSheetItem item = answerSheet.getAnswerSheetItems().get(i);
            boolean hasDone = !StringUtils.isBlank(item.getAnswer());

            TextView textView = new TextView(context);
            Resources res = context.getResources();
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

            if (hasDone){
                textView.setBackgroundColor(res.getColor(R.color.primary));
            }else{
                textView.setBackgroundColor(res.getColor(R.color.DimGray));
            }
            if (i == selectedIndex) textView.setBackgroundColor(res.getColor(R.color.orange));
            textView.setTag(i);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedIndex = (int) v.getTag();
                    updateView();
                    if (clickCallBack != null){
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
//            this.showAtLocation(parent, Gravity.BOTTOM,
//                    location[0] , location[1] - this.getHeight());
            this.showAtLocation(parent, Gravity.BOTTOM,0,
                    160);
            Log.e("TAG", "location:"+location[0]+".."+location[1]+".."+this.getHeight());
        } else {
            this.dismiss();
        }
    }

    public interface ClickCallBack {
        void clicked(int index);
    }
}