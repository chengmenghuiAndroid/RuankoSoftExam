package com.itee.exam.app.ui.update;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by pkwsh on 2016-04-05.
 */
public class GetUpdateInfo {
    public static String getUpdataVerJSON(String serverPath) throws Exception{
        StringBuilder newVerJSON = new StringBuilder();
        HttpClient client = new DefaultHttpClient();//新建http客户端
        HttpParams httpParams = client.getParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 3000);//设置连接超时范围
        HttpConnectionParams.setSoTimeout(httpParams, 5000);
        //serverPath是version.json的路径
        HttpResponse response = client.execute(new HttpGet(serverPath));
        HttpEntity entity = response.getEntity();
        if(entity != null){
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(entity.getContent(),"UTF-8"),8192);
            String line = null;
            while((line = reader.readLine()) != null){
                newVerJSON.append(line+"\n");//按行读取放入StringBuilder中
            }
            reader.close();
        }
        return newVerJSON.toString();
    }

    public static int getVerCode(String json) throws Exception{
        JSONObject obj=new JSONObject(json);
        return Integer.parseInt(obj.getString("versionCode"));
    }

    public static String getURL(String json) throws Exception{
        JSONObject obj=new JSONObject(json);
        return obj.getString("downloadUrl");
    }

    public static String getApkName(String json) throws Exception{
        JSONObject obj=new JSONObject(json);
        return obj.getString("apkName");
    }

    public static String getContent(String json) throws Exception{
        JSONObject obj=new JSONObject(json);
        return obj.getString("content");
    }
}
