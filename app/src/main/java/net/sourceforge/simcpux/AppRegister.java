package net.sourceforge.simcpux;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.itee.exam.app.Constants;
import com.itee.exam.tools.PayTools;
import com.itee.exam.tools.WeiXinPay;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class AppRegister extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
		WeiXinPay pay=new WeiXinPay();
		PayTools.getWeixinParam(pay);
		// 将该app注册到微信
		msgApi.registerApp(pay.getWEIXIN_APP_ID());
	}
}
