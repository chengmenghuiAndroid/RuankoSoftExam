package com.itee.exam.app.entity;

import java.io.Serializable;

/**
 * Created by rkcoe on 2016/9/23.
 */
public class QuestionAnswer implements Serializable {
    private static final long serialVersionUID = 7956850583554324205L;
    /**
     * id : 1
     * questionId : 95
     * answerNumber : 1
     * answerTitle : 1.1
     * answerContent : abcd
     * answerAnalysis : {"analysisContent":"\u003cp\u003e来自百度\u003c/p\u003e\n","fileUrl":"files\\analysisHtml\\95\\1\\c3aa7bf8-3ef0-43ad-8cd6-bdaf9a6eac15.html"}
     * questionAnalysis : null
     * keyword : 程序员
     */

    private int id;
    private int questionId;
    private int answerNumber;
    private String answerTitle;
    private String answerContent;
    private String answerAnalysis;
    private QuestionAnalysis questionAnalysis;
    private String keyword;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getAnswerNumber() {
        return answerNumber;
    }

    public void setAnswerNumber(int answerNumber) {
        this.answerNumber = answerNumber;
    }

    public String getAnswerTitle() {
        return answerTitle;
    }

    public void setAnswerTitle(String answerTitle) {
        this.answerTitle = answerTitle;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public String getAnswerAnalysis() {
        return answerAnalysis;
    }

    public void setAnswerAnalysis(String answerAnalysis) {
        this.answerAnalysis = answerAnalysis;
    }

    public QuestionAnalysis getQuestionAnalysis() {
        return questionAnalysis;
    }

    public void setQuestionAnalysis(QuestionAnalysis questionAnalysis) {
        this.questionAnalysis = (QuestionAnalysis) questionAnalysis;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
