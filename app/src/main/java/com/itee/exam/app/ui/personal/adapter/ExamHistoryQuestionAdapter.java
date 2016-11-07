package com.itee.exam.app.ui.personal.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itee.exam.R;
import com.itee.exam.app.AppContext;
import com.itee.exam.app.entity.AnswerSheet;
import com.itee.exam.app.entity.AnswerSheetItem;
import com.itee.exam.app.entity.QuestionQueryResultVO;
import com.itee.exam.app.entity.meta.QuestionType;
import com.itee.exam.app.ui.doexam.AnswerSheetPopWindow;
import com.itee.exam.app.ui.image.ImageActivity;
import com.itee.exam.app.ui.personal.examhistory.ExamHistoryDetailActivity;
import com.itee.exam.app.widget.RichText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jack on 2016-03-24.
 */
public class ExamHistoryQuestionAdapter extends PagerAdapter {
    private ExamHistoryDetailActivity context;
    private List<QuestionQueryResultVO> dataItems;
    private List<AnswerSheetItem> answerSheetItems;
    private AnswerSheet answerSheet;
    private RichText.ImageFixListener listener;
    // 每个item的页面view
    private View convertView;

    public ExamHistoryQuestionAdapter(ExamHistoryDetailActivity context, List<QuestionQueryResultVO> dataItems, AnswerSheet answerSheet, RichText.ImageFixListener listener) {
        this.context = context;
        this.dataItems = dataItems;
        this.answerSheet = answerSheet;
        this.answerSheetItems = answerSheet.getAnswerSheetItems();
        this.listener = listener;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView(viewItems.get(position));
    }

    @Override
    public int getCount() {
        if (dataItems == null)
            return 0;
        return dataItems.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ViewHolder holder = null;
        QuestionQueryResultVO question = dataItems.get(position);
        int type = question.getQuestionTypeId();
        QuestionType questionType = QuestionType.instance(type);
        if ((QuestionType.SINGLE_SELECTION == questionType) || (QuestionType.MULTIPLE_SELECTION == questionType) || (QuestionType.TRUE_FALSE_SELECTION == questionType)) {
            convertView = context.getLayoutInflater().inflate(R.layout.exam_history_question_item, null);
            holder = new ViewHolder();
            initSelection(holder, convertView);
            holder.question.setRichText("(" + questionType.toString() + ")" + (position + 1) + "." + question.getQuestionContent().getTitle());
            String imgUrl = question.getQuestionContent().getTitleImg();
            final String path = AppContext.getImgPath(imgUrl);
            if (imgUrl.trim().length() > 0) {
                imgUrl = "<img src=\"" + AppContext.getImgPath(imgUrl) + "\"/>";
                holder.questionImg.setVisibility(View.VISIBLE);
                holder.questionImg.setImageFixListener(listener);
                holder.questionImg.setRichText(imgUrl);
                holder.questionImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.position=position;
                        Intent intent = new Intent(context,ImageActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("Path", path);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });
            }
        }
        if ((QuestionType.SINGLE_SELECTION == questionType) || (QuestionType.MULTIPLE_SELECTION == questionType)) {
            View[] layout = {holder.layoutA, holder.layoutB, holder.layoutC, holder.layoutD, holder.layoutE, holder.layoutF};
            TextView[] tv = {holder.tvA, holder.tvB, holder.tvC, holder.tvD, holder.tvE, holder.tvF};
            ImageView[] iv = {holder.ivA, holder.ivB, holder.ivC, holder.ivD, holder.ivE, holder.ivF};
            RichText[] imageView = {holder.imageA, holder.imageB, holder.imageC, holder.imageD, holder.imageE, holder.imageF};
            String[] no = {"A", "B", "C", "D", "E", "F"};
            HashMap<String, String> choiceList = question.getQuestionContent().getChoiceList();
            int len = choiceList.size();
            for (int i = 0; i < len; i++) {
                layout[i].setVisibility(View.VISIBLE);
                tv[i].setText(no[i] + "." + choiceList.get(no[i]));

            }
            HashMap<String, String> choiceImgList = question.getQuestionContent().getChoiceImgList();
            if (choiceImgList!=null && choiceImgList.size() > 0) {
                for (Map.Entry entry : choiceImgList.entrySet()) {
                    Object key = entry.getKey();
                    for (int i = 0; i < no.length; i++) {
                        if (no[i].equals(key)) {
                            String url = entry.getValue().toString();
                            final String path = AppContext.getImgPath(url);
                            if (url.trim().length() > 0) {
                                url = "<img src=\"" + AppContext.getImgPath(url) + "\"/>";
                                imageView[i].setVisibility(View.VISIBLE);
                                imageView[i].setImageFixListener(listener);
                                imageView[i].setRichText(url);
                                imageView[i].setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        context.position=position;
                                        Intent intent = new Intent(context,ImageActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("Path", path);
                                        intent.putExtras(bundle);
                                        context.startActivity(intent);
                                    }
                                });
                            }
                            break;
                        }
                    }
                }
            }
            String customerAnswer = answerSheetItems.get(position).getAnswer();
            boolean right = answerSheetItems.get(position).isRight();
            for (int i = 0; i < no.length; i++) {
                if ((QuestionType.SINGLE_SELECTION == questionType) && (no[i].equals(customerAnswer))) {
                    if (right) {
                        iv[i].setImageResource(R.drawable.ic_practice_test_right);
                        tv[i].setTextColor(Color.parseColor("#61bc31"));
                    } else {
                        iv[i].setImageResource(R.drawable.ic_practice_test_wrong);
                        tv[i].setTextColor(Color.parseColor("#d53235"));
                    }
                    break;
                } else if (QuestionType.MULTIPLE_SELECTION == questionType) {
                    int length = customerAnswer.trim().length();
                    String[] answerItem = new String[length];
                    for (int j = 0; j < length; j++) {
                        answerItem[j] = String.valueOf(customerAnswer.trim().charAt(j));
                        for (int k = 0; k < no.length; k++) {
                            if (no[k].equals(answerItem[j])) {
                                if (right) {
                                    iv[k].setImageResource(R.drawable.ic_practice_test_right);
                                    tv[k].setTextColor(Color.parseColor("#61bc31"));
                                } else {
                                    iv[k].setImageResource(R.drawable.ic_practice_test_wrong);
                                    tv[k].setTextColor(Color.parseColor("#d53235"));
                                }
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        } else if (QuestionType.TRUE_FALSE_SELECTION == questionType) {
            holder.layoutA.setVisibility(View.VISIBLE);
            holder.layoutB.setVisibility(View.VISIBLE);
            holder.tvA.setText("A.是");
            holder.tvB.setText("B.否");
            String customerAnswer = answerSheetItems.get(position).getAnswer().trim();
            boolean right = answerSheetItems.get(position).isRight();
            if ("T".equals(customerAnswer)) {
                if (right) {
                    holder.ivA.setImageResource(R.drawable.ic_practice_test_right);
                    holder.tvA.setTextColor(Color.parseColor("#61bc31"));
                } else {
                    holder.ivA.setImageResource(R.drawable.ic_practice_test_wrong);
                    holder.tvA.setTextColor(Color.parseColor("#d53235"));
                }
            } else if ("F".equals(customerAnswer)) {
                if (right) {
                    holder.ivB.setImageResource(R.drawable.ic_practice_test_right);
                    holder.tvB.setTextColor(Color.parseColor("#61bc31"));
                } else {
                    holder.ivB.setImageResource(R.drawable.ic_practice_test_wrong);
                    holder.tvB.setTextColor(Color.parseColor("#d53235"));
                }
            }
        }

        String answer = question.getAnswer();
        String analysis = question.getAnalysis();
        String examingPoint = question.getExamingPoint();
//        Pattern pattern = Pattern.compile(Constants.RegexRUL);
//        Matcher matcher = pattern.matcher(analysis);
//        while(matcher.find()){
//            String url=matcher.group();
//            String result = "<img src=\"" + url + "\"/>";
//            analysis=analysis.replace(url,result);
//        }
        String res = context.getResources().getString(R.string.analysis, answer, examingPoint.trim().length() == 0 ? "无" : examingPoint, analysis.trim().length() == 0 ? "无" : analysis);
//        holder.analysis.setImageFixListener(listener);
        holder.analysis.setRichText(res.replace("\n", "<br>"));
        holder.totalText.setText(position + 1 + "/" + dataItems.size());

        holder.showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View viewBottom = convertView.findViewById(R.id.viewBottom);
                AnswerSheetPopWindow answerSheetPopWindow = new AnswerSheetPopWindow(context, answerSheet, position,
                        new AnswerSheetPopWindow.ClickCallBack() {
                            @Override
                            public void clicked(int index) {
                                context.setCurrentView(index);
                            }
                        });
                answerSheetPopWindow.showPopupWindow(viewBottom);
            }
        });
        holder.previousBtn.setOnClickListener(new LinearOnClickListener(position - 1));
        holder.nextBtn.setOnClickListener(new LinearOnClickListener(position + 1));
        if (0 == position) {
            holder.previousBtn.setVisibility(View.INVISIBLE);
        }
        if (getCount() == position + 1) {
            holder.nextBtn.setVisibility(View.INVISIBLE);
        }
        container.addView(convertView);
        return convertView;
    }

    public class ViewHolder {
        RichText question;
        RichText questionImg;
        TextView totalText;
        LinearLayout layoutA;
        LinearLayout layoutB;
        LinearLayout layoutC;
        LinearLayout layoutD;
        LinearLayout layoutE;
        LinearLayout layoutF;
        ImageView ivA;
        ImageView ivB;
        ImageView ivC;
        ImageView ivD;
        ImageView ivE;
        ImageView ivF;
        TextView tvA;
        TextView tvB;
        TextView tvC;
        TextView tvD;
        TextView tvE;
        TextView tvF;
        RichText imageA;
        RichText imageB;
        RichText imageC;
        RichText imageD;
        RichText imageE;
        RichText imageF;
        RichText analysis;
        LinearLayout previousBtn, nextBtn, showBtn;
    }

    private void initSelection(ViewHolder holder, View view) {
        holder.question = (RichText) view
                .findViewById(R.id.exam_question_title);
        holder.questionImg = (RichText) view
                .findViewById(R.id.exam_question_title_image);
        holder.totalText = (TextView) view
                .findViewById(R.id.exam_prepare_test_totalTv);
        holder.layoutA = (LinearLayout) view
                .findViewById(R.id.exam_prepare_test_layout_a);
        holder.layoutB = (LinearLayout) view
                .findViewById(R.id.exam_prepare_test_layout_b);
        holder.layoutC = (LinearLayout) view
                .findViewById(R.id.exam_prepare_test_layout_c);
        holder.layoutD = (LinearLayout) view
                .findViewById(R.id.exam_prepare_test_layout_d);
        holder.layoutE = (LinearLayout) view
                .findViewById(R.id.exam_prepare_test_layout_e);
        holder.layoutF = (LinearLayout) view
                .findViewById(R.id.exam_prepare_test_layout_f);
        holder.ivA = (ImageView) view
                .findViewById(R.id.vote_submit_select_image_a);
        holder.ivB = (ImageView) view
                .findViewById(R.id.vote_submit_select_image_b);
        holder.ivC = (ImageView) view
                .findViewById(R.id.vote_submit_select_image_c);
        holder.ivD = (ImageView) view
                .findViewById(R.id.vote_submit_select_image_d);
        holder.ivE = (ImageView) view
                .findViewById(R.id.vote_submit_select_image_e);
        holder.ivF = (ImageView) view
                .findViewById(R.id.vote_submit_select_image_f);
        holder.tvA = (TextView) view
                .findViewById(R.id.vote_submit_select_text_a);
        holder.tvB = (TextView) view
                .findViewById(R.id.vote_submit_select_text_b);
        holder.tvC = (TextView) view
                .findViewById(R.id.vote_submit_select_text_c);
        holder.tvD = (TextView) view
                .findViewById(R.id.vote_submit_select_text_d);
        holder.tvE = (TextView) view
                .findViewById(R.id.vote_submit_select_text_e);
        holder.tvF = (TextView) view
                .findViewById(R.id.vote_submit_select_text_f);
        holder.imageA = (RichText) view
                .findViewById(R.id.vote_submit_select_text_a_img);
        holder.imageB = (RichText) view
                .findViewById(R.id.vote_submit_select_text_b_img);
        holder.imageC = (RichText) view
                .findViewById(R.id.vote_submit_select_text_c_img);
        holder.imageD = (RichText) view
                .findViewById(R.id.vote_submit_select_text_d_img);
        holder.analysis = (RichText) view
                .findViewById(R.id.activity_prepare_test_explaindetail);
        holder.previousBtn = (LinearLayout) view
                .findViewById(R.id.exam_prepare_test_upLayout);
        holder.nextBtn = (LinearLayout) view
                .findViewById(R.id.exam_prepare_test_nextLayout);
        holder.showBtn = (LinearLayout) view
                .findViewById(R.id.exam_prepare_test_answer);
    }

    class LinearOnClickListener implements View.OnClickListener {
        private int mPosition;

        public LinearOnClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View v) {
            context.setCurrentView(mPosition);
        }
    }

}
