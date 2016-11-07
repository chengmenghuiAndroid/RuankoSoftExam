package com.itee.exam.vo;

/**
 * 支付宝支付相关参数
 * Created by xin on 2015-09-02-0002.
 */
public class AlipayParams {

    private String partner;
    private String seller;
    private String rsaPrivate;
    private String rsaPublic;

    public AlipayParams() {
    }

    public AlipayParams(String partner, String seller, String rsaPrivate, String rsaPublic) {
        this.partner = partner;
        this.seller = seller;
        this.rsaPrivate = rsaPrivate;
        this.rsaPublic = rsaPublic;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getRsaPrivate() {
        return rsaPrivate;
    }

    public void setRsaPrivate(String rsaPrivate) {
        this.rsaPrivate = rsaPrivate;
    }

    public String getRsaPublic() {
        return rsaPublic;
    }

    public void setRsaPublic(String rsaPublic) {
        this.rsaPublic = rsaPublic;
    }
}
