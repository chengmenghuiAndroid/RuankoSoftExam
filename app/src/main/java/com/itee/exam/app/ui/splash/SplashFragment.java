package com.itee.exam.app.ui.splash;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itee.exam.R;

/**
 * Created by jack on 2015-09-07.
 */
public class SplashFragment extends Fragment {
    final static String LAYOUT_ID = "layoutid";

    public static SplashFragment newInstance(int layoutId) {
        SplashFragment pane = new SplashFragment();
        Bundle args = new Bundle();
        args.putInt(LAYOUT_ID, layoutId);
        pane.setArguments(args);
        return pane;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(getArguments().getInt(LAYOUT_ID, -1), container, false);
        if(getArguments().getInt(LAYOUT_ID, -1)== R.layout.welcome_fragment1){
            Bitmap bitmap=decodeSampledBitmapFromResource(getResources(),R.drawable.splash_1,540,960);
            Drawable drawable=new BitmapDrawable(bitmap);
            rootView.setBackground(drawable);
        }else if(getArguments().getInt(LAYOUT_ID, -1)== R.layout.welcome_fragment2){
            Bitmap bitmap=decodeSampledBitmapFromResource(getResources(),R.drawable.splash_2,540,960);
            Drawable drawable=new BitmapDrawable(bitmap);
            rootView.setBackground(drawable);
        }else if(getArguments().getInt(LAYOUT_ID, -1)== R.layout.welcome_fragment3){
            Bitmap bitmap=decodeSampledBitmapFromResource(getResources(),R.drawable.splash_3,540,960);
            Drawable drawable=new BitmapDrawable(bitmap);
            rootView.setBackground(drawable);
        }
        return rootView;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize=2;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
}
