package com.itee.exam.app.entity.meta;

/**
 * Created by gonglei on 2015/6/11.
 */
public enum QuestionType {
    SINGLE_SELECTION(1),//单选
    MULTIPLE_SELECTION(2),//多选
    TRUE_FALSE_SELECTION(3),//判断
    FILL_BLANK(4),//填空
    SHORT_ANSWER(5),//简答
    ESSAY_QUESTION(6),//论述题
    ANALYSIS_QUESTION(7);//分析题

    private int value;
    QuestionType(int _value){
        value = _value;
    }

    public static QuestionType instance(int _value){
        switch (_value) {
            case 1:
                return SINGLE_SELECTION;
            case 2:
                return MULTIPLE_SELECTION;
            case 3:
                return TRUE_FALSE_SELECTION;
            case 4:
                return FILL_BLANK;
            case 5:
                return SHORT_ANSWER;
            case 6:
                return ESSAY_QUESTION;
            case 7:
                return ANALYSIS_QUESTION;
            default:
                return SINGLE_SELECTION;
        }
    }

    @Override
    public String toString() {
        switch (this.value) {
            case 1:
                return "单选题";
            case 2:
                return "多选题";
            case 3:
                return "判断题";
            case 4:
                return "填空题";
            case 5:
                return "简答题";
            case 6:
                return "论述题";
            case 7:
                return "分析题";
            default:
                return "";
        }
    }
}
