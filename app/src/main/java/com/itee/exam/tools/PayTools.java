package com.itee.exam.tools;

/**
 * Created by pkwsh on 2016/07/25.
 */
public class PayTools {
    static {
        System.loadLibrary("payJni");
    }
    public static native String getAlipayParam(PayUtils pay);
    public static native String getWeixinParam(WeiXinPay pay);
}
