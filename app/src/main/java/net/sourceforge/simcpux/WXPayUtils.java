package net.sourceforge.simcpux;

import android.content.Context;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.itee.exam.core.utils.Toasts;
import com.itee.exam.events.WXPayEvent;
import com.itee.exam.tools.PayTools;
import com.itee.exam.tools.WeiXinPay;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.greenrobot.event.EventBus;

/**
 * Created by jack on 2015-11-28.
 */
public class WXPayUtils {
    public interface OnPayListener {
        /**
         * 支付成功
         */
        void onSuccess();

        /**
         * 支付失败
         */
        void onFailure();

        /**
         * 支付取消
         */
        void onCanceled();
    }

    public static WXPayUtils newInstance(Context context, WXPayParams params,WeiXinPay pay) {
        return new WXPayUtils(context,params,pay);
    }
    private static final String TAG = "WXPayUtils";
    private WXPayParams params;
    private IWXAPI api;
    private Context context;
    private PayReq req;
    private WeiXinPay pay;

    private OnPayListener listener;

    public WXPayUtils(Context context,WXPayParams params,WeiXinPay pay){
        this.context = context;
        this.params = params;
        this.pay=pay;
    }

    public WXPayUtils setOnPayListener(OnPayListener listener) {
        this.listener = listener;
        return this;
    }

    public void wxpay(){
        pay=new WeiXinPay();
        PayTools.getWeixinParam(pay);
        req = new PayReq();
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(context, pay.getWEIXIN_APP_ID(), false);
        api.registerApp(pay.getWEIXIN_APP_ID());
        if(!api.isWXAppInstalled()){
            EventBus.getDefault().post(new WXPayEvent(WXPayResult.NoWXAppInstalled));
            Toasts.showToastInfoShort(context, "您使用的设备上还未安装微信！");
            return;
        }
        if(!api.isWXAppSupportAPI()){
            EventBus.getDefault().post(new WXPayEvent(WXPayResult.NotWXAppSupportAPI));
            Toasts.showToastInfoShort(context, "您使用的设备上安装微信版本不支持酷客APP所使用的微信支付接口！");
            return;
        }
        String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
        String entity = genProductArgs();
        try{
            byte[] buf = Util.httpPost(url, entity);
            if (buf != null && buf.length > 0){
                String content = new String(buf);
                Map<String,String> xml=decodeXml(content);
                if("FAIL".equals(xml.get("return_code"))){
                    Toast.makeText(context, xml.get("return_msg"), Toast.LENGTH_SHORT).show();
                }else if ("SUCCESS".equals(xml.get("return_code"))){
                    req.appId = pay.getWEIXIN_APP_ID();
                    req.partnerId = pay.getWEIXIN_MCH_ID();
                    req.prepayId = xml.get("prepay_id");
                    req.packageValue = "Sign=WXPay";
                    req.nonceStr = genNonceStr();
                    req.timeStamp = String.valueOf(genTimeStamp());
                    List<NameValuePair> signParams = new LinkedList<NameValuePair>();
                    signParams.add(new BasicNameValuePair("appid", req.appId));
                    signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
                    signParams.add(new BasicNameValuePair("package", req.packageValue));
                    signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
                    signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
                    signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
                    req.sign = genAppSign(signParams);
                    api.sendReq(req);
                }

            }else{
                Log.d("PAY_GET", "服务器请求错误");
                Toast.makeText(context, "服务器请求错误", Toast.LENGTH_SHORT).show();
            }

        }catch(Exception e){
            Log.e("PAY_GET", "异常："+e.getMessage());
            Toast.makeText(context, "异常："+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String genProductArgs() {
        StringBuffer xml = new StringBuffer();
        try {
            String	nonceStr = genNonceStr();
            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", pay.getWEIXIN_APP_ID()));
            packageParams.add(new BasicNameValuePair("body", params.getBody()));
            packageParams.add(new BasicNameValuePair("mch_id", pay.getWEIXIN_MCH_ID()));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", "http://wxpay.weixin.qq.com/pub_v2/pay/notify.v2.php"));
            packageParams.add(new BasicNameValuePair("out_trade_no",genOutTradNo()));
            packageParams.add(new BasicNameValuePair("spbill_create_ip","127.0.0.1"));
            packageParams.add(new BasicNameValuePair("total_fee", String.valueOf((int)(Float.valueOf(params.getMoney())*100))));//Money,单位为分
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));
            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));
            String xmlstring =toXml(packageParams);
            return new String(xmlstring.getBytes(),"ISO8859-1");
        } catch (Exception e) {
            Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
            return null;
        }
    }

    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private String genOutTradNo() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(pay.getWEIXIN_API_KEY());
        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion",packageSign);
        return packageSign;
    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<"+params.get(i).getName()+">");
            sb.append(params.get(i).getValue());
            sb.append("</"+params.get(i).getName()+">");
        }
        sb.append("</xml>");
        Log.e("orion",sb.toString());
        return sb.toString();
    }

    public Map<String,String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName=parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if("xml".equals(nodeName)==false){
                            xml.put(nodeName,parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("orion",e.toString());
        }
        return null;

    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(pay.getWEIXIN_API_KEY());

        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion",appSign);
        return appSign;
    }
}
