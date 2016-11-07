/**
 *
 */
package com.itee.exam.core.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

/**
 * @author xin
 */
public final class ExternalStorageUtils {

    private ExternalStorageUtils() {
    }

    /**
     * 检测sdcard是否可写
     *
     * @return
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     * 检测sdcard是否可读
     *
     * @return
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    @SuppressLint("NewApi")
    public static long getTotalSizeByPath(String path) {
        long size;
        StatFs sf = new StatFs(path);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            size = sf.getTotalBytes();
        } else {
            long blockSize = sf.getBlockSize();
            long totalBlocks = sf.getBlockCount();
            size = blockSize * totalBlocks;
        }
        return size;
    }

    @SuppressLint("NewApi")
    public static long getAvailableSizeByPath(String path) {
        long size;
        StatFs sf = new StatFs(path);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            size = sf.getAvailableBytes();
        } else {
            long blockSize = sf.getBlockSize();
            long availableBlocks = sf.getAvailableBlocks();
            size = blockSize * availableBlocks;
        }
        return size;
    }
}
