package com.itee.exam.app.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 郑晓乐 on 2016/3/22.
 */

public class ExamHistoryVO implements java.io.Serializable {
    private int histId;
    private int userId;
    private String userName;
    private String trueName;
    private Date startTime;
    private int examId;
    private String examName;
    private int examType;
    private boolean enabled;
    private int examPaperId;
    private float point;
    private String seriNo;
    private List<QuestionQueryResultVO> question_query_result;
    private Date createTime;
    private AnswerSheet answerSheet;
    private int duration;
    private float pointGet;
    private Date submitTime;
    //0 未审核 1 通过 2 未通过
    private int approved;
    private Date verifyTime;
    private float passPoint;
    private String nationalId;
    private String depName;

    public int getHistId() {
        return histId;
    }

    public void setHistId(int histId) {
        this.histId = histId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public int getExamType() {
        return examType;
    }

    public void setExamType(int examType) {
        this.examType = examType;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getExamPaperId() {
        return examPaperId;
    }

    public void setExamPaperId(int examPaperId) {
        this.examPaperId = examPaperId;
    }

    public float getPoint() {
        return point;
    }

    public void setPoint(float point) {
        this.point = point;
    }

    public String getSeriNo() {
        return seriNo;
    }

    public void setSeriNo(String seriNo) {
        this.seriNo = seriNo;
    }

    public List<QuestionQueryResultVO> getQuestion_query_result() {
        return question_query_result;
    }

    public void setQuestion_query_result(List<QuestionQueryResultVO> question_query_result) {
        this.question_query_result = question_query_result;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public AnswerSheet getAnswerSheet() {
        return answerSheet;
    }

    public void setAnswerSheet(AnswerSheet answerSheet) {
        this.answerSheet = answerSheet;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public float getPointGet() {
        return pointGet;
    }

    public void setPointGet(float pointGet) {
        this.pointGet = pointGet;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public Date getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }

    public float getPassPoint() {
        return passPoint;
    }

    public void setPassPoint(float passPoint) {
        this.passPoint = passPoint;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }
    public ExamHistoryVO(ExamHistory examHistory){

        this.histId =examHistory.getHistId();
        this.userId = examHistory.getUserId();
        this.userName = examHistory.getTrueName();
        this.startTime = examHistory.getStartTime();
        this.examId = examHistory.getExamId();
        this.examName = examHistory.getExamName();
        this.examType = examHistory.getExamType();
        this.examPaperId = examHistory.getExamPaperId();
        this.point = examHistory.getPoint();
        this.seriNo = examHistory.getSeriNo();
        this.createTime = examHistory.getCreateTime();
        this.duration = examHistory.getDuration();
        this.pointGet = examHistory.getPointGet();
        this.submitTime = examHistory.getSubmitTime();
        this.approved = examHistory.getApproved();
        this.verifyTime = examHistory.getVerifyTime();
        this.passPoint = examHistory.getPassPoint();
        this.nationalId = examHistory.getNationalId();
        this.depName = examHistory.getDepName();
        this.enabled = examHistory.isEnabled();
        this.question_query_result = autoSetQuestionQueryResult(examHistory.getContent());
        this.answerSheet = autoSetAnswerSheetResult(examHistory.getAnswerSheet());
    }

    private AnswerSheet autoSetAnswerSheetResult(String content) {
        Gson gson = new Gson();
        if (content!=null && content.trim()!="")
        {
            AnswerSheet answerSheetList = gson.fromJson(content,new TypeToken<AnswerSheet>(){}.getType());
            return answerSheetList;
        }

        return null;
    }

    private List<QuestionQueryResultVO> autoSetQuestionQueryResult(String content) {
        Gson gson = new Gson();
        List<QuestionQueryResultVO> all = new ArrayList<>();
        if (content != null && content.trim() != "") {
            List<QuestionQueryResult> questionList = gson.fromJson(content, new TypeToken<List<QuestionQueryResult>>() {
            }.getType());

            for (QuestionQueryResult one : questionList) {
                QuestionQueryResultVO questionQueryResultVO = new QuestionQueryResultVO(one);
                all.add(questionQueryResultVO);
            }

            return all;
        }
        return null;
    }

}
