package com.itee.exam.app.entity;

import com.google.gson.Gson;

/**
 * Created by Administrator on 2016/3/17.
 */

public class  QuestionQueryResultVO implements java.io.Serializable  {
    private int questionId;
    private QuestionContent questionContent;
    private String answer;
    private String analysis;
    private int questionTypeId;
    private String referenceName;
    private String pointName;
    private String fieldName;
    private float questionPoint;
    private String examingPoint;
    private int knowledgePointId;
    private QuestionAnalysis questionAnalysis;

    public QuestionQueryResultVO(QuestionQueryResult questionQueryResult) {
        this.questionId = questionQueryResult.getQuestionId();
        this.answer = questionQueryResult.getAnswer();
        this.analysis = questionQueryResult.getAnalysis();
        this.questionTypeId = questionQueryResult.getQuestionTypeId();
        this.referenceName = questionQueryResult.getReferenceName();
        this.pointName = questionQueryResult.getPointName();
        this.fieldName = questionQueryResult.getFieldName();
        this.questionPoint = questionQueryResult.getQuestionPoint();
        this.examingPoint = questionQueryResult.getExamingPoint();
        this.knowledgePointId = questionQueryResult.getKnowledgePointId();
        this.questionContent = autoSetQuestionContent(questionQueryResult.getContent());
        this.questionAnalysis= autoSetQuestionAnalysis(questionQueryResult.getQuestionAnalysis());
    }

    private QuestionAnalysis autoSetQuestionAnalysis(String content){
        if(content == null ||content.trim()==""){
            return null;
        }
        QuestionAnalysis question1 = new Gson().fromJson(content,QuestionAnalysis.class);
        return question1;
    }

    private QuestionContent autoSetQuestionContent(String content) {
        if(content == null ||content.trim()==""){
            return null;
        }
        QuestionContent question1 = new Gson().fromJson(content,QuestionContent.class);
        return question1;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public QuestionContent getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(QuestionContent questionContent) {
        this.questionContent = questionContent;
    }

    public QuestionAnalysis getQuestionAnalysis() {
        return questionAnalysis;
    }

    public void setQuestionAnalysis(QuestionAnalysis questionAnalysis) {
        this.questionAnalysis = questionAnalysis;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public int getQuestionTypeId() {
        return questionTypeId;
    }

    public void setQuestionTypeId(int questionTypeId) {
        this.questionTypeId = questionTypeId;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public float getQuestionPoint() {
        return questionPoint;
    }

    public void setQuestionPoint(float questionPoint) {
        this.questionPoint = questionPoint;
    }

    public String getExamingPoint() {
        return examingPoint;
    }

    public void setExamingPoint(String examingPoint) {
        this.examingPoint = examingPoint;
    }

    public int getKnowledgePointId() {
        return knowledgePointId;
    }

    public void setKnowledgePointId(int knowledgePointId) {
        this.knowledgePointId = knowledgePointId;
    }
}
