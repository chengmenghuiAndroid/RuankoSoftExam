package com.itee.exam.vo;

/**
 * Created by xin on 2015-08-11-0011.
 */
public class CardQueryResult {

    public static final int OK = 1;

    //<editor-fold desc="银行卡信息调用方错误码：">
    public static final int ERR0_1 = -1;
    //</editor-fold>

    //<editor-fold desc="限制类错误：">
    public static final int ERR1_1 = 300101;
    public static final int ERR1_2 = 300102;
    public static final int ERR1_3 = 300103;
    public static final int ERR1_4 = 300104;
    //</editor-fold>

    //<editor-fold desc="调用方错误：">
    public static final int ERR2_1 = 300201;
    public static final int ERR2_2 = 300202;
    public static final int ERR2_3 = 300203;
    public static final int ERR2_4 = 300204;
    public static final int ERR2_5 = 300205;
    public static final int ERR2_6 = 300206;
    //</editor-fold>

    //<editor-fold desc="代理平台错误：">
    public static final int ERR3_1 = 300301;
    public static final int ERR3_2 = 300302;
    //</editor-fold>

    private int status;
    private CardInfo data;

    public CardQueryResult() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public CardInfo getData() {
        return data;
    }

    public void setData(CardInfo data) {
        this.data = data;
    }

    public static String getErrorDesc(int error) {
        switch (error) {
            case ERR0_1:
                return "无效的卡号";

            case ERR1_1:
                return "用户请求过期";
            case ERR1_2:
                return "用户日调用量超限";
            case ERR1_3:
                return "服务每秒调用量超限";
            case ERR1_4:
                return "服务日调用量超限";

            case ERR2_1:
                return "url无法解析";
            case ERR2_2:
                return "请求缺少apikey，登录即可获取";
            case ERR2_3:
                return "服务没有取到apikey或secretkey";
            case ERR2_4:
                return "apikey不存在";
            case ERR2_5:
                return "api不存在";
            case ERR2_6:
                return "api已关闭服务";

            case ERR3_1:
                return "内部错误";
            case ERR3_2:
                return "系统繁忙稍候再试";

            default:
                return "未知错误";
        }
    }
}
