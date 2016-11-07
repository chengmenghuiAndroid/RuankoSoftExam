package com.itee.exam.core.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.itee.exam.app.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xin on 2015-08-13-0013.
 */
public class FileUtils {

    /**
     * 删除指定文件
     *
     * @param path
     * @return
     */
    public static boolean delFile(String path) {
        if (path == null) {
            return false;
        }
        File file = new File(path);
        return file.exists() && file.delete();
    }

    /**
     * 创建目录
     *
     * @param dirPath
     */
    public static void mkdirs(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 获取目录名称
     * @param url
     * @return FileName
     */
    public static String getFileName(String url)
    {
        int lastIndexStart = url.lastIndexOf("/");
        if(lastIndexStart!=-1)
        {
            return url.substring(lastIndexStart+1, url.length());
        }else{
            return new Timestamp(System.currentTimeMillis()).toString();
        }
    }
    /**
     * 判断SD卡是否存在
     * @return boolean
     */
    public static boolean checkSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 保存目录目录到目录
     * @param context
     * @return  目录保存的目录
     */
    public static String setMkdir(Context context)
    {
        String filePath = null;
        if(checkSDCard())
        {
            filePath = Environment.getExternalStorageDirectory()+File.separator + Constants.APP_NAME + File.separator+"downloads";
        }else{
            filePath = context.getCacheDir().getAbsolutePath()+File.separator + Constants.APP_NAME + File.separator+"downloads";
        }
        File file = new File(filePath);
        if(!file.exists())
        {
            file.mkdirs();
            Log.e("file", "目录不存在   创建目录    ");
        }else{
            Log.e("file", "目录存在");
        }
        return filePath;
    }

    /**
     * 获取路径
     * @return
     */
    public static  String getPath(Context context,String url)
    {
        String path = null;
        try {
            path = FileUtils.setMkdir(context)+File.separator+url.substring(url.lastIndexOf("/")+1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * 获取文件的大小
     *
     * @param fileSize
     *            文件的大小
     * @return
     */
    public static String FormetFileSize(int fileSize) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileSize < 1024) {
            fileSizeString = df.format((double) fileSize) + "B";
        } else if (fileSize < 1048576) {
            fileSizeString = df.format((double) fileSize / 1024) + "K";
        } else if (fileSize < 1073741824) {
            fileSizeString = df.format((double) fileSize / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileSize / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 下载文件
     */
    public static void downloadFile(Context context,List<String> filelist,DownLoadCallback callback) {
        if (filelist == null){
            callback.onFail();
            return;
        }
        final int max = 100;
        List<String> localFiles = new ArrayList<String>();
        int fileCount = filelist.size();
        for (int i = 0; i < fileCount; i++) {
            String url = filelist.get(i);
            try {
                URL u = new URL(url);
                String filePath = getPath(context, url);
                URLConnection conn = u.openConnection();
                InputStream is = conn.getInputStream();
                int fileSize = conn.getContentLength();
                if (fileSize < 1 || is == null) {
                    callback.onError("zero_size");
                } else {
                    FileOutputStream fos = new FileOutputStream(filePath);
                    byte[] bytes = new byte[1024];
                    int len = -1;
                    while ((len = is.read(bytes)) != -1) {
                        fos.write(bytes, 0, len);
                        fos.flush();
                        int progress = i * max /fileCount + len * max/ fileSize;
                        callback.onProgress(progress,max);
                    }
                    is.close();
                    fos.close();
                    localFiles.add(filePath);
                }
            } catch (Exception e) {
                callback.onError("exception:" + e.toString());
                e.printStackTrace();
            }
        }
        if (localFiles.size() == 0){
            callback.onFail();
        }else {
            callback.onSuccess(localFiles);
        }
    }

    public interface DownLoadCallback{
        void onSuccess(List<String> localFiles);
        void onFail();
        void onProgress(int progress, int max);
        void onError(String reason);
    }
}
