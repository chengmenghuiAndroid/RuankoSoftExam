package com.itee.exam.app.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itee.exam.R;
import com.itee.exam.app.entity.AnswerSheet;
import com.itee.exam.app.entity.AnswerSheetItem;
import com.itee.exam.app.entity.Exam;
import com.itee.exam.app.entity.ExamPaperVO;
import com.itee.exam.app.entity.Field;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.app.ui.doexam.AfterExamActivity;
import com.itee.exam.app.ui.doexam.ExamAnswerActivity;
import com.itee.exam.app.ui.login.LoginActivity;
import com.itee.exam.core.utils.ProgressTask;
import com.itee.exam.core.utils.Toasts;
import com.itee.exam.utils.PreferenceUtil;
import com.itee.exam.vo.HttpMessage;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rkcoe on 2016/9/12.
 */
public class RuanKuExamActivity extends BaseActivity {

    @BindView(R.id.select_exam)
    RelativeLayout rlSelectorExam;
    @BindView(R.id.tv_exam3)
    TextView tvExamSelector;
    @BindView(R.id.tv_exam2)
    TextView tvExamType;
    @BindView(R.id.exam_type)
    RelativeLayout  rlType;
    @BindView(R.id.tv_exam)
    TextView tvExam;
    @BindView(R.id.rl_exam_begin)
    RelativeLayout RlexamBegin;
    @BindView(R.id.ll_exam)
    RelativeLayout rlExam;

    private List<Field> subjectList;
    private int selectedExamSubject = -1;
    private int selectedExamSelector = -1;
    private int selectedExam = -1;
    private final static String AM_EXAM = "上午卷";
    private final static String  PM_EXAM = "下午卷";
    String[] examType = new String[]{AM_EXAM, PM_EXAM};

    private final static String  THIS_FILE = "RuanKuExamActivity";
    private int selectedExamType = -1;
    private String type;
    private List<Exam> paperList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_exam);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
    }

    /**
     * 功能1-选择科目
     */
    @OnClick(R.id.ll_exam)
    public void Function1() {
        new ProgressTask<HttpMessage<List<Field>>>(this, getString(R.string.waiting)) {
            @Override
            public HttpMessage<List<Field>> call() throws Exception {
                return getAppContext().getHttpService().getAllExamPaperType();
            }

            @Override
            protected void onSuccess(HttpMessage<List<Field>> httpMessage) throws Exception {
                if ("success".equals(httpMessage.getResult())) {
                    subjectList = httpMessage.getObject();
                    if (subjectList != null && subjectList.size() > 0) {
                        String[] subjects = new String[subjectList.size()];
                        for (int i = 0; i < subjectList.size(); i++) {
                            subjects[i] = subjectList.get(i).getFieldName();
                            Log.e(THIS_FILE, ""+subjectList.get(i).getFieldName());
                        }
                        showSubjectSelect(subjects);
//                        imgSeleted1.setVisibility(View.VISIBLE);
//                        imgSeleted2.setVisibility(View.GONE);
//                        imgSeleted3.setVisibility(View.GONE);
//                        imgSeleted4.setVisibility(View.GONE);
                    } else {
                        Toasts.showToastInfoLong(getContext(), R.string.exam_subject_paper_null);
                    }
                }
            }
        }.execute();
    }

    @OnClick(R.id.exam_type)
    public void examType() {
        showTypeSelect(examType);
    }

    /**
     * 选择科目dialog
     */
    private void showSubjectSelect(final String[] subjecs) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.exam_subjects_select_type)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(subjecs, -1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                selectedExamSubject = which;
//                                updateExamSubject();
                                if(subjectList !=null && subjectList.size() !=0){
                                    tvExam.setText(subjectList.get(selectedExamSubject).getFieldName());
                                    rlExam.setBackground(getResources().getDrawable(R.drawable.android_confirm_btn));
                                }
                                selectedExam = -1;
//                                updateExamPaper();
                                updateExam();
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", null).show();
    }


    /**
     * 选择上午卷还是下午卷
     *
     */

    private void showTypeSelect(final String[] examType) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.exam_subjects_select)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(examType, -1,
                         new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {
                                selectedExamType = which;
//                                updateExamSubject();
                                type = examType[which];
                                tvExamType.setText(type);
                                rlType.setBackground(getResources().getDrawable(R.drawable.android_confirm_btn));
                                selectedExam = -1;
//                                updateExamPaper();
                                 updateExam();
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", null).show();
    }

    private void updateExam() {
        tvExamSelector.setText(R.string.exam_papers_select);
    }

    /**
     * 选择试卷
     */

    @OnClick(R.id.select_exam)
    public void selectExam(){
        if (subjectList == null || selectedExamSubject < 0 || selectedExamSubject >= subjectList.size()) {
            Toasts.showToastInfoLong(this, "尚未选择科目");
            return;
        }

        if (subjectList == null || selectedExamType < 0 || selectedExamType >= subjectList.size()) {
            Toasts.showToastInfoLong(this, "尚未选择试卷类型");
            return;
        }
        final int subjectId = subjectList.get(selectedExamSubject).getFieldId();
        if (type == AM_EXAM){
            Log.e(THIS_FILE, "subjectId:"+subjectId);
            new ProgressTask<HttpMessage<List<Exam>>>(RuanKuExamActivity.this, getString(R.string.waiting)) {
                @Override
                public HttpMessage<List<Exam>> call() throws Exception {
                    return getAppContext().getHttpService().listExamByFieldAndFlag(subjectId, "1");
                }

                @Override
                protected void onSuccess(HttpMessage<List<Exam>> listHttpMessage) throws Exception {
                    if("success".equals(listHttpMessage.getResult())){
                        paperList = listHttpMessage.getObject();
                        if (paperList !=null && paperList.size()>0){
                            String[] papers = new String[paperList.size()];
                            for(int i = 0; i<paperList.size(); i ++){
                                papers[i] = paperList.get(i).getExamName();
                                Log.e(THIS_FILE,"AM_EXAM:"+paperList.get(i).getExamName());
                            }
                            showPaperSelect(papers);
                        }else {
                            mHandler.sendEmptyMessage(1);
                        }
                    }else if ("no-exam".equals(listHttpMessage.getResult())){
                        mHandler.sendEmptyMessage(1);
                    }

                }
            }.execute();
        }else if (type == PM_EXAM){
            new ProgressTask<HttpMessage<List<Exam>>>(RuanKuExamActivity.this, getString(R.string.waiting)) {
                @Override
                public HttpMessage<List<Exam>> call() throws Exception {
                    return getAppContext().getHttpService().listExamByFieldAndFlag(subjectId, "2");
                }

                @Override
                protected void onSuccess(HttpMessage<List<Exam>> listHttpMessage) throws Exception {
                    if ("success".equals(listHttpMessage.getResult())) {
                        paperList = listHttpMessage.getObject();
                        if (paperList != null && paperList.size() > 0) {
                            String[] papers = new String[paperList.size()];
                            for (int i = 0; i < paperList.size(); i++) {
                                papers[i] = paperList.get(i).getExamName();
                                Log.e(THIS_FILE,"PM_EXAM:"+paperList.get(i).getExamName());
                            }
                            showPaperSelect(papers);
//                            Type=1;
//                        PreferenceUtil.getInstance().setExamType(1);
                        } else {
                            mHandler.sendEmptyMessage(2);
                        }
                    } else if ("no-exam".equals(listHttpMessage.getResult())) {
                        mHandler.sendEmptyMessage(2);
                    }
                }
            }.execute();

        }

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 1:
                    Toasts.showToastInfoShort(RuanKuExamActivity.this, "该科目下暂无上试卷");
                    break;
                case 2:
                    Toasts.showToastInfoShort(RuanKuExamActivity.this, "该科目下暂无下午试卷");
                    break;
            }
        }
    };

    private void showPaperSelect(String[] myPapers) {
        new AlertDialog.Builder(this).setTitle(R.string.exam_papers_select).setIcon(android.R.drawable.ic_dialog_info).
                setSingleChoiceItems(myPapers, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedExam = which;
//                        updateExamPaper(type);
                        if(paperList !=null && paperList.size() !=0){
                            tvExamSelector.setText(paperList.get(selectedExam).getExamName());
                            rlSelectorExam.setBackground(getResources().getDrawable(R.drawable.android_confirm_btn));
                        }
                        selectedExamSelector = -1;
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", null).show();

    }

    @OnClick(R.id.rl_exam_begin)
    void examBegin(){
        if (!PreferenceUtil.getInstance().getIsLogin()) {
            Toasts.showToastInfoShort(this, "只有登录之后才能进行考试");
            Intent intent = LoginActivity.generateIntent(this);
            startActivity(intent);
            return;
        }

        if (PreferenceUtil.getInstance().getIsDoExam()) {
            showOldExam();
            return;
        }

        if (paperList == null || selectedExam < 0 || selectedExam >= paperList.size()) {
            Toasts.showToastInfoLong(this, "尚未选择试卷");
            return;
        }
        int examType = PreferenceUtil.getInstance().getExamType();
        if (type == AM_EXAM) {
            new ProgressTask<HttpMessage<ExamPaperVO>>(RuanKuExamActivity.this, getString(R.string.getting_paper)) {
                @Override
                public HttpMessage<ExamPaperVO> call() throws Exception {
                    String userName = PreferenceUtil.getInstance().getUserName();
                    String token = PreferenceUtil.getInstance().getTokenContent();
                    return getAppContext().getHttpService().startMockExam(userName, token, paperList.get(selectedExam).getExamId());
                }

                @Override
                protected void onSuccess(HttpMessage<ExamPaperVO> httpMessage) throws Exception {
                    if ("success".equals(httpMessage.getResult())) {
                        ExamPaperVO paper = httpMessage.getObject();
                        if (paper != null) {
                            Toasts.showToastInfoShort(getContext(), "开始考试：" + paper.getName());
                            initAndStartExam(paper);
                        }
                    } else {
                        Toasts.showToastInfoLong(getContext(), httpMessage.getMessageInfo());
                    }
                }
            }.execute();
        } else if (type == PM_EXAM) {
            new ProgressTask<HttpMessage<ExamPaperVO>>(RuanKuExamActivity.this, getString(R.string.getting_paper)) {
                @Override
                public HttpMessage<ExamPaperVO> call() throws Exception {
                    String userName = PreferenceUtil.getInstance().getUserName();
                    String token = PreferenceUtil.getInstance().getTokenContent();
                   Log.e(THIS_FILE,"examId:"+paperList.get(selectedExam).getExamId());
                    return getAppContext().getHttpService().startAfternoonExam(userName, token, paperList.get(selectedExam).getExamId());
                }

                @Override
                protected void onSuccess(HttpMessage<ExamPaperVO> httpMessage) throws Exception {
                    if ("success".equals(httpMessage.getResult())) {
                        ExamPaperVO paper = httpMessage.getObject();
                        if (paper != null) {
                            Toasts.showToastInfoShort(getContext(), "开始考试：" + paper.getName());
                            initAfternoonExam(paper);
                        }
                    } else {
                        Toasts.showToastInfoLong(getContext(), httpMessage.getMessageInfo());
                    }
                }
            }.execute();
        }
    }

    private void initAfternoonExam(ExamPaperVO paper) {
        if (null == paper) {
            return;
        }
        AnswerSheet answerSheet = paper.getAnswer_sheet_result();
        List<AnswerSheetItem> items = answerSheet.getAnswerSheetItems();
        for (AnswerSheetItem item : items) {
            item.setAnswer("");
        }
        answerSheet.setStartTime(new Date());
        PreferenceUtil.getInstance().saveAnswerSheet(answerSheet);
        PreferenceUtil.getInstance().saveExamPaper(paper);
        PreferenceUtil.getInstance().setDoExam(true);
        PreferenceUtil.getInstance().setExamType(1);
        //启动考试界面
        startActivity(AfterExamActivity.generateIntent(RuanKuExamActivity.this, paper, answerSheet));
    }

    private void initAndStartExam(ExamPaperVO paper) {
        if (paper == null) return;
        AnswerSheet answerSheet = paper.getAnswer_sheet_result();
        List<AnswerSheetItem> items = answerSheet.getAnswerSheetItems();
        for (AnswerSheetItem item : items) {
            item.setAnswer("");
        }
        answerSheet.setStartTime(new Date());
        PreferenceUtil.getInstance().saveAnswerSheet(answerSheet);
        PreferenceUtil.getInstance().saveExamPaper(paper);
        PreferenceUtil.getInstance().setDoExam(true);
        PreferenceUtil.getInstance().setExamType(0);
        //启动考试界面
        startActivity(ExamAnswerActivity.generateIntent(RuanKuExamActivity.this, paper, answerSheet));
    }

    private void showOldExam() {
        final ExamPaperVO paper = PreferenceUtil.getInstance().getExamPaper();
        if (paper == null) return;
        final int examType = PreferenceUtil.getInstance().getExamType();
        String paperTitle = paper.getName();
        AlertDialog.Builder builder = new AlertDialog.Builder(RuanKuExamActivity.this);
        builder.setTitle("考试提醒");
        builder.setMessage("《" + paperTitle + "》 正在进行中...");
        builder.setPositiveButton("进入", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AnswerSheet answerSheet = PreferenceUtil.getInstance().getAnswerSheet();
                if (0 == examType) {
                    //启动考试界面
                    startActivity(ExamAnswerActivity.generateIntent(RuanKuExamActivity.this, paper, answerSheet));
                } else if (1 == examType) {
                    startActivity(AfterExamActivity.generateIntent(RuanKuExamActivity.this, paper, answerSheet));
                }
            }
        });
        builder.setNegativeButton("稍后", null);
        builder.setNeutralButton("终止", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PreferenceUtil.getInstance().setDoExam(false);
            }
        });
        builder.create().show();
    }

}
