package com.itee.exam.tools;

/**
 * Created by pkwsh on 2016-07-28.
 */
public class WeiXinPay {
    //微信支付
    private String WEIXIN_APP_ID="";
    //商户号
    private String WEIXIN_MCH_ID="";
    //  API密钥，在商户平台设置
    private String WEIXIN_API_KEY="";

    private String WEIXIN_APP_SECRET="";

    public String getWEIXIN_APP_ID() {
        return WEIXIN_APP_ID;
    }

    public String getWEIXIN_MCH_ID() {
        return WEIXIN_MCH_ID;
    }

    public String getWEIXIN_API_KEY() {
        return WEIXIN_API_KEY;
    }

    public String getWEIXIN_APP_SECRET() {
        return WEIXIN_APP_SECRET;
    }
}
