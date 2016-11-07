package com.itee.exam.app.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.itee.exam.R;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.app.ui.customview.SelectPicPopupWindow;
import com.itee.exam.app.ui.customview.SelectPicPopupWindowCopy;
import com.itee.exam.app.ui.event.PopEvent;
import com.itee.exam.core.utils.Toasts;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class QAOnline extends BaseActivity {

    @BindView(R.id.tv_sz_tel)
    TextView szTel;
    @BindView(R.id.tv_qg_tel)
    TextView qgTel;
    @BindView(R.id.tv_qq_qun)
    TextView tvQQQun;
    @BindView(R.id.tv_mail)
    TextView tvMail;
    @BindView(R.id.tv_wh_tel)
    TextView wuHanTel;

//    自定义的弹出框类
    SelectPicPopupWindow menuWindow;
    SelectPicPopupWindowCopy menuCopyWindow;

    private String THIS_LEFT = "QAOnline";
    private ClipData newPlainText;
    private ClipboardManager mManager;
    private String textNumer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_qaonline);
        setContentView(R.layout.activity_online_answer);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        EventBus.getDefault().register(this);
    }

    public void funQQContent(View view) {
        if (!isInstalled(this, "com.tencent.mobileqq")) {
            Toast.makeText(this, "本机没有安装QQ", Toast.LENGTH_SHORT).show();
            return;
        }
        String qq = "2200935530";
        String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq;
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public void funWhTel(View view){
        showPopWindow();
        CharSequence text = wuHanTel.getText();
        EventBus.getDefault().postSticky(new PopEvent(text));
//        EventBus.getDefault().post(new PopEvent(text));
        Log.e(THIS_LEFT, " "+text);
    }

    public void funSzTel(View view){
        showPopWindow();
        EventBus.getDefault().postSticky(new PopEvent(szTel.getText()));
    }
    public void funQgTel(View view){
        showPopWindow();
        EventBus.getDefault().postSticky(new PopEvent(qgTel.getText()));
    }
    public void funCopyMail(View view){
        Toasts.showCopyToastInfoShort(this, "复制成功,快去黏贴吧 !");
        copyText("simple_text", tvMail.getText());
    }

    public void funCopyQQQun(View view){
        Toasts.showCopyToastInfoShort(this, "复制成功,快去黏贴吧 !");
        copyText("simple_text", tvQQQun.getText());
    }


    /**
     * 复制文本信息
     * @param label
     * @param text
     */
    private void copyText(CharSequence label, CharSequence text) {
        newPlainText = ClipData.newPlainText(label, text);
        mManager.setPrimaryClip(newPlainText);
    }

    /**
     * 判断应用是否已安装
     *
     * @param context
     * @param packageName
     * @return
     */
    private boolean isInstalled(Context context, String packageName) {
        if (packageName == null || "".equals(packageName)) {
            return false;
        }
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    /**
     * 弹出电话popWindow
     */
    private void showPopWindow() {
        //实例化SelectPicPopupWindow
        menuWindow = new SelectPicPopupWindow(QAOnline.this, itemsOnClick);
        //显示窗口
        menuWindow.showAtLocation(QAOnline.this.findViewById(R.id.main),
        Gravity.CENTER_HORIZONTAL|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
    }

    /**
     * 弹出复制popWindow
     */
    private void showPopCopyWindow() {
        menuCopyWindow = new SelectPicPopupWindowCopy(QAOnline.this, itemsCopyOnClick);
        //显示窗口
        menuCopyWindow.showAtLocation(QAOnline.this.findViewById(R.id.main),
        Gravity.CENTER_HORIZONTAL|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()){
                case R.id.iv_tel:
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    Uri data = Uri.parse("tel:" + textNumer);
                    intent.setData(data);
                    startActivity(intent);
                    break;
                case R.id.iv_save_number:
                    break;
            }

        }
    };

    public void onEventMainThread(PopEvent textTel){
        textNumer = (String) textTel.getText();
    }

    private View.OnClickListener itemsCopyOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
