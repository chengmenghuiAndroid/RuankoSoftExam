package com.itee.exam.app.ui.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 软考科目 2016/7/27.
 */
//@XmlRootElement
public class Subject implements Serializable {

    private static final long serialVersionUID = 6335675770371435246L;

    private int subjectId;
    private String subjectName;
    private int subjectCharge;
    private int grade;  //资格等级（1：初级   2：中级  3：高级）
    private String memo;
    private Date examTime;


    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getSubjectCharge() {
        return subjectCharge;
    }

    public void setSubjectCharge(int subjectCharge) {
        this.subjectCharge = subjectCharge;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

//    @JsonSerialize(using = JsonTimestampSerializer.class)
//    @JsonDeserialize(using = JsonTimestampDeserializer.class)
    public Date getExamTime() {
        return examTime;
    }

    public void setExamTime(Date examTime) {
        this.examTime = examTime;
    }



}
