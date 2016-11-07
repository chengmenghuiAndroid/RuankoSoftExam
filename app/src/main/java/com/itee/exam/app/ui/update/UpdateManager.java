package com.itee.exam.app.ui.update;

import android.content.Context;

/**
 * Created by pkwsh on 2016-04-05.
 */
public class UpdateManager {
    private int verCode = -1;
    private int curCode = -1;
    private String downlaodUrl=null;
    private String apkName=null;
    private String content;

    private static Context context;

    public UpdateManager(Context context){
        UpdateManager.context =context;
    }

    public void setDownlaodUrl(String downlaodUrl){
        this.downlaodUrl=downlaodUrl;
    }

    public String getDownlaodUrl(){
        return this.downlaodUrl;
    }

    public void setApkName(String apkName){
        this.apkName=apkName;
    }

    public String getApkName(){
        return this.apkName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isUpdate(){
        try{
            //获取服务器端版本控制文件
            String verson=GetUpdateInfo.getUpdataVerJSON(Config.downPath+Config.appVersion);
            verCode=GetUpdateInfo.getVerCode(verson);
            curCode=CurrentVersion.getVerCode(context);
            setApkName(GetUpdateInfo.getApkName(verson));
            setDownlaodUrl(GetUpdateInfo.getURL(verson));
            setContent(GetUpdateInfo.getContent(verson));
            if(verCode>curCode){
                return true;
            }
        }catch (Exception e){
            return false;
        }

        return false;
    }
}
