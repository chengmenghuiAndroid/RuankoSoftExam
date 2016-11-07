package com.itee.exam.app.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

public class MainActivity extends BaseActivity {
    private TextView tvExamPaper;
    private TextView tvExamSuject;
    private ImageView imgSeleted1;
    private ImageView imgSeleted2;
    private ImageView imgSeleted3;
    private ImageView imgSeleted4;

    private List<Field> subjectList;
    int selectedExamSubject = -1;
    private List<Exam> paperList;
    int selectedExam = -1;

    private int Type = -1;

   /* public static Intent generateIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();

        if (PreferenceUtil.getInstance().getIsDoExam()) {
            showOldExam();
        }
    }

    private void showOldExam() {
        final ExamPaperVO paper = PreferenceUtil.getInstance().getExamPaper();
        if (paper == null) return;
        final int examType = PreferenceUtil.getInstance().getExamType();
        String paperTitle = paper.getName();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("考试提醒");
        builder.setMessage("《" + paperTitle + "》 正在进行中...");
        builder.setPositiveButton("进入", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AnswerSheet answerSheet = PreferenceUtil.getInstance().getAnswerSheet();
                if (0 == examType) {
                    //启动考试界面
                    startActivity(ExamAnswerActivity.generateIntent(MainActivity.this, paper, answerSheet));
                } else if (1 == examType) {
                    startActivity(AfterExamActivity.generateIntent(MainActivity.this, paper, answerSheet));
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

    private void initView() {
        imgSeleted1 = (ImageView) findViewById(R.id.imgSeleted1);
        imgSeleted2 = (ImageView) findViewById(R.id.imgSeleted2);
        imgSeleted3 = (ImageView) findViewById(R.id.imgSeleted3);
        imgSeleted4 = (ImageView) findViewById(R.id.imgSeleted4);
        tvExamSuject = (TextView) findViewById(R.id.tvExamSuject);
        tvExamPaper = (TextView) findViewById(R.id.tvExamPaper);
        imgSeleted1.setVisibility(View.GONE);
        imgSeleted2.setVisibility(View.GONE);
        imgSeleted3.setVisibility(View.GONE);
        imgSeleted4.setVisibility(View.GONE);
    }

    private void updateExamSubject() {
        if (subjectList == null || subjectList.size() == 0 ||
                selectedExamSubject < 0 && selectedExamSubject >= subjectList.size()) {
            tvExamSuject.setText(R.string.exam_subject_null);
            imgSeleted1.setVisibility(View.GONE);
        } else {
            tvExamSuject.setText(subjectList.get(selectedExamSubject).getFieldName());
        }

    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 1:
                    Toasts.showToastInfoShort(MainActivity.this, R.string.exam_subject_paper_null);
                    break;
                case 2:
                    Toasts.showToastInfoShort(MainActivity.this, "该科目下暂无下午(一)试卷");
                    break;
            }
        }
    };


    /**
     * 功能0-模拟考试
     *
     * @param view
     */
    public void Function0(View view) {
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
        if (0 == Type) {
            new ProgressTask<HttpMessage<ExamPaperVO>>(MainActivity.this, getString(R.string.getting_paper)) {
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
        } else if (1 == Type) {
            new ProgressTask<HttpMessage<ExamPaperVO>>(MainActivity.this, getString(R.string.getting_paper)) {
                @Override
                public HttpMessage<ExamPaperVO> call() throws Exception {
                    String userName = PreferenceUtil.getInstance().getUserName();
                    String token = PreferenceUtil.getInstance().getTokenContent();
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
        startActivity(AfterExamActivity.generateIntent(MainActivity.this, paper, answerSheet));
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
        startActivity(ExamAnswerActivity.generateIntent(MainActivity.this, paper, answerSheet));
    }

    /**
     * 功能1-选择科目
     *
     * @param view
     */
    public void Function1(View view) {
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
                        }
                        showSubjectSelect(subjects);
                        imgSeleted1.setVisibility(View.VISIBLE);
                        imgSeleted2.setVisibility(View.GONE);
                        imgSeleted3.setVisibility(View.GONE);
                        imgSeleted4.setVisibility(View.GONE);
                    } else {
                        Toasts.showToastInfoLong(getContext(), R.string.exam_subject_paper_null);
                    }
                }
            }
        }.execute();
    }

    /**
     * 选择科目dialog
     */
    private void showSubjectSelect(String[] subjecs) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.exam_subjects_select)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(subjecs, -1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                selectedExamSubject = which;
                                updateExamSubject();
                                selectedExam = -1;
//                                updateExamPaper();
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", null).show();
    }

    /**
     * 功能2-选择试卷
     *
     * @param view
     */
    public void Function2(View view) {

        if (subjectList == null || selectedExamSubject < 0 || selectedExamSubject >= subjectList.size()) {
            Toasts.showToastInfoLong(this, "尚未选择科目");
            return;
        }
        final int subjectId = subjectList.get(selectedExamSubject).getFieldId();

        new ProgressTask<HttpMessage<List<Exam>>>(MainActivity.this, getString(R.string.waiting)) {
            @Override
            public HttpMessage<List<Exam>> call() throws Exception {
                return getAppContext().getHttpService().listExamByFieldAndFlag(subjectId, "1");
            }

            @Override
            protected void onSuccess(HttpMessage<List<Exam>> listHttpMessage) throws Exception {
                if ("success".equals(listHttpMessage.getResult())) {
                    paperList = listHttpMessage.getObject();
                    Log.e("TAG",":"+listHttpMessage.getResult());
                    Log.e("TAG",":"+listHttpMessage.getObject());
                    if (paperList != null && paperList.size() > 0) {
                        String[] papers = new String[paperList.size()];
                        for (int i = 0; i < paperList.size(); i++) {
                            papers[i] = paperList.get(i).getExamName();
                        }
                        showPaperSelect(papers, 0);
                        Type = 0;
//                        PreferenceUtil.getInstance().setExamType(0);
                        imgSeleted2.setVisibility(View.GONE);
                        imgSeleted3.setVisibility(View.GONE);
                        imgSeleted4.setVisibility(View.GONE);
                    } else {
                        mHandler.sendEmptyMessage(1);
                    }
                } else if ("no-exam".equals(listHttpMessage.getResult())) {
                    mHandler.sendEmptyMessage(1);
                }
            }
        }.execute();
    }

    private void showPaperSelect(String[] myPapers, final int type) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.exam_papers_select)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(myPapers, -1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                selectedExam = which;
                                updateExamPaper(type);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", null).show();
    }

    private void updateExamPaper(int type) {
        if (paperList == null || paperList.size() == 0 || selectedExam < 0 || selectedExam >= paperList.size()) {
            tvExamPaper.setText(R.string.exam_paper_null);
            if (0 == type) {
                imgSeleted2.setVisibility(View.GONE);
            } else if (1 == type) {
                imgSeleted3.setVisibility(View.GONE);
            } else if (2 == type) {
                imgSeleted4.setVisibility(View.GONE);
            }
        } else {
            tvExamPaper.setText(paperList.get(selectedExam).getExamName());
            if (0 == type) {
                imgSeleted2.setVisibility(View.VISIBLE);
            } else if (1 == type) {
                imgSeleted3.setVisibility(View.VISIBLE);
            } else if (2 == type) {
                imgSeleted4.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 功能3
     *
     * @param view
     */
    public void Function3(View view) {
        if (subjectList == null || selectedExamSubject < 0 || selectedExamSubject >= subjectList.size()) {
            Toasts.showToastInfoLong(this, "尚未选择科目");
            return;
        }
        final int subjectId = subjectList.get(selectedExamSubject).getFieldId();
        new ProgressTask<HttpMessage<List<Exam>>>(MainActivity.this, getString(R.string.waiting)) {
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
                        }
                        showPaperSelect(papers, 1);
                        Type=1;
//                        PreferenceUtil.getInstance().setExamType(1);
                        imgSeleted2.setVisibility(View.GONE);
                        imgSeleted3.setVisibility(View.GONE);
                        imgSeleted4.setVisibility(View.GONE);
                    } else {
                        mHandler.sendEmptyMessage(2);
                    }
                } else if ("no-exam".equals(listHttpMessage.getResult())) {
                    mHandler.sendEmptyMessage(2);
                }
            }
        }.execute();
    }

    /**
     * 功能4
     *
     * @param view
     */
    public void Function4(View view) {
        if (subjectList == null || selectedExamSubject < 0 || selectedExamSubject >= subjectList.size()) {
            Toasts.showToastInfoLong(this, "尚未选择科目");
            return;
        }
        Toasts.showToastInfoShort(MainActivity.this, "该科目下暂无下午(二)试卷");
    }

}
