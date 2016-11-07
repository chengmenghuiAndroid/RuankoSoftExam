package com.itee.exam.vo;

/**
 * 银行卡信息
 * Created by xin on 2015-08-11-0011.
 */
public class CardInfo {

    /**
     * 银行卡的类型
     */
    private String cardtype;
    /**
     * 银行卡的长度
     */
    private int cardlength;
    /**
     * 银行卡前缀
     */
    private String cardprefixnum;
    /**
     * 银行卡名称
     */
    private String cardname;
    /**
     * 归属银行
     */
    private String bankname;
    /**
     * 内部结算代码
     */
    private String banknum;

    public CardInfo() {
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public int getCardlength() {
        return cardlength;
    }

    public void setCardlength(int cardlength) {
        this.cardlength = cardlength;
    }

    public String getCardprefixnum() {
        return cardprefixnum;
    }

    public void setCardprefixnum(String cardprefixnum) {
        this.cardprefixnum = cardprefixnum;
    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBanknum() {
        return banknum;
    }

    public void setBanknum(String banknum) {
        this.banknum = banknum;
    }
}
