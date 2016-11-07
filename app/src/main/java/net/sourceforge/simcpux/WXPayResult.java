package net.sourceforge.simcpux;

/**
 * Created by BG170 on 2015/12/2.
 */
public interface WXPayResult {
    int SUCCESS = 0;
    int FAILURE = -1;
    int CANCELED = -2;
    int NoWXAppInstalled = -3;
    int NotWXAppSupportAPI = -4;
}
