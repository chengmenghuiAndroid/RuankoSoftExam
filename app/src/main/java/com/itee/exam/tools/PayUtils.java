package com.itee.exam.tools;

/**
 * Created by pkwsh on 2016/07/26.
 */
public class PayUtils {
    //商户PID
    public String PARTNER="";
    //商户收款账号
    public String SELLER="";
    //商户私钥，pkcs8格式
    public String RSA_PRIVATE="";
    //支付宝公钥
    public String RSA_PUBLIC="";

    public String getPARTNER() {
        return PARTNER;
    }

    public String getSELLER() {
        return SELLER;
    }

    public String getRSA_PRIVATE() {
        return RSA_PRIVATE;
    }

    public String getRSA_PUBLIC() {
        return RSA_PUBLIC;
    }
}
