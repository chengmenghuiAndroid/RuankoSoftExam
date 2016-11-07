package com.itee.exam.app.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/3/17.
 */

public class ExamPaperVO implements java.io.Serializable {
    protected int id;
    protected String name;
    protected List<QuestionQueryResultVO> question_query_result;
    protected int duration;
    protected int pass_point;
    protected float total_point;
    protected Date create_time;
    /**
     * 0:默认 1：发布
     */
    protected int status;
    protected String summary;
    protected boolean is_visible;
    protected int group_id;
    protected boolean is_subjective;



    protected  AnswerSheet answer_sheet_result ;
    protected String creator;
    protected String paper_type;
    protected int field_id;
    protected int field_name;

    //试卷类型标识：上午卷=1、下午卷=2、其他=3
    protected  String paper_flag;

    public String getPaper_flag() {
        return paper_flag;
    }

    public void setPaper_flag(String paper_flag) {
        this.paper_flag = paper_flag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AnswerSheet getAnswer_sheet_result() {
        return answer_sheet_result;
    }

    public void setAnswer_sheet_result(AnswerSheet answer_sheet_result) {
        this.answer_sheet_result = answer_sheet_result;
    }

    public List<QuestionQueryResultVO> getQuestion_query_result() {
        return question_query_result;
    }

    public void setQuestion_query_result(List<QuestionQueryResultVO> question_query_result) {
        this.question_query_result = question_query_result;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPass_point() {
        return pass_point;
    }

    public void setPass_point(int pass_point) {
        this.pass_point = pass_point;
    }

    public float getTotal_point() {
        return total_point;
    }

    public void setTotal_point(float total_point) {
        this.total_point = total_point;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public boolean is_visible() {
        return is_visible;
    }

    public void setIs_visible(boolean is_visible) {
        this.is_visible = is_visible;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public boolean is_subjective() {
        return is_subjective;
    }

    public void setIs_subjective(boolean is_subjective) {
        this.is_subjective = is_subjective;
    }


    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getPaper_type() {
        return paper_type;
    }

    public void setPaper_type(String paper_type) {
        this.paper_type = paper_type;
    }

    public int getField_id() {
        return field_id;
    }

    public void setField_id(int field_id) {
        this.field_id = field_id;
    }

    public int getField_name() {
        return field_name;
    }

    public void setField_name(int field_name) {
        this.field_name = field_name;
    }

    public ExamPaperVO(ExamPaper examPaper) {
       // this.answer_sheet = examPaper.getAnswer_sheet();
        this.id = examPaper.getId();
        this.name = examPaper.getName();
        this.duration = examPaper.getDuration();
        this.pass_point = examPaper.getPass_point();
        this.total_point = examPaper.getTotal_point();
        this.create_time = examPaper.getCreate_time();
        this.status = examPaper.getStatus();
        this.summary = examPaper.getSummary();
        this.is_visible = examPaper.isIs_visible();
        this.group_id = examPaper.getGroup_id();
        this.is_subjective = examPaper.isIs_subjective();
        this.creator = examPaper.getCreator();
        this.paper_type = examPaper.getPaper_type();
        this.field_id = examPaper.getField_id();
        this.field_name = examPaper.getField_name();
        this.question_query_result = autoSetQuestionQueryResult(examPaper.getContent());

        this.answer_sheet_result =autoSetAnswerSheetResult(examPaper.getAnswer_sheet());
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
