package com.itee.exam.app.entity;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/3/17.
 */
public class ExamVO implements java.io.Serializable {
    private static final long serialVersionUID = -1595051130848974880L;
    private int examId;//考试编号
    private String examName;//考试名称
    private String examSubscribe;//
    private int examType;//考试类型
    private Date createTime;
    private Date effTime;
    private Date expTime;
    private ExamPaperVO examPaperVO;
    private String examPaperName;
    private List<Integer> groupIdList;
    private int creator;
    private String creatorId;
    //准考证号
    private String seriNo;
    //0 未审核, 1 审核通过, 2 审核不通过
    private int approved;


    ExamVO(Exam exam, ExamPaper examPaper){
        this.examId = exam.getExamId();
        this.examName = exam.getExamName();
        this.examSubscribe = exam.getExamSubscribe();
        this.examType = exam.getExamType();
        this.createTime = exam.getCreateTime();
        this.effTime = exam.getEffTime();
        this.expTime = exam.getExpTime();

        this.examPaperName = exam.getExamPaperName();
        this.groupIdList = exam.getGroupIdList();
        this.creator = exam.getCreator();
        this.creatorId = exam.getCreatorId();
        //准考证号
        this.seriNo = exam.getSeriNo();
        //0 未审核, 1 审核通过, 2 审核不通过
        this.approved = exam.getApproved();
        this.examPaperVO = new ExamPaperVO(examPaper);
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

    public String getExamSubscribe() {
        return examSubscribe;
    }

    public void setExamSubscribe(String examSubscribe) {
        this.examSubscribe = examSubscribe;
    }

    public int getExamType() {
        return examType;
    }

    public void setExamType(int examType) {
        this.examType = examType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEffTime() {
        return effTime;
    }

    public void setEffTime(Date effTime) {
        this.effTime = effTime;
    }

    public Date getExpTime() {
        return expTime;
    }

    public void setExpTime(Date expTime) {
        this.expTime = expTime;
    }

    public ExamPaperVO getExamPaperVO() {
        return examPaperVO;
    }

    public void setExamPaperVO(ExamPaperVO examPaperVO) {
        this.examPaperVO = examPaperVO;
    }

    public String getExamPaperName() {
        return examPaperName;
    }

    public void setExamPaperName(String examPaperName) {
        this.examPaperName = examPaperName;
    }

    public List<Integer> getGroupIdList() {
        return groupIdList;
    }

    public void setGroupIdList(List<Integer> groupIdList) {
        this.groupIdList = groupIdList;
    }

    public int getCreator() {
        return creator;
    }

    public void setCreator(int creator) {
        this.creator = creator;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getSeriNo() {
        return seriNo;
    }

    public void setSeriNo(String seriNo) {
        this.seriNo = seriNo;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }
}
