package com.itee.exam.core.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itee.exam.R;

import java.util.List;

/**
 * 显示tip工具类
 * 
 * @author moxin
 * 
 */
public final class Toasts {

    private static Toast result;

    private Toasts() {
	}

	public static void showToastException(Context context){
        showToast(context, R.drawable.exception, context.getString(R.string.system_exception_tip),context.getString(R.string.system_exception_tip2),Toast.LENGTH_LONG);
	}

    public static void showToast(Context context,int imgRes,String title,String msg,int duration){
        result = new Toast(context);
        //获取LayoutInflater对象
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //由layout文件创建一个View对象
        View layout = inflater.inflate(R.layout.new_toast, null);

        //实例化ImageView和TextView对象
        ImageView imageView = (ImageView) layout.findViewById(R.id.image0);
        TextView textView1 = (TextView) layout.findViewById(R.id.text1);
        TextView textView2 = (TextView) layout.findViewById(R.id.text2);

        imageView.setImageResource(imgRes);
        if (!StringUtils.isBlank(title)){
            textView1.setText(title);
        }else{
            textView1.setVisibility(View.GONE);
        }

        if (!StringUtils.isBlank(msg)){
            textView2.setText(msg);
        }else{
            textView2.setVisibility(View.GONE);
        }

        result.setView(layout);
        result.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        result.setDuration(duration);
        result.show();
    }

    public static void showToastSuccess(Context context, int resId, int duration) {
        showToast(context, R.drawable.toast_success, null, context.getString(resId), duration);
    }
    public static void showToastSuccess(Context context, String str, int duration) {
        showToast(context, R.drawable.toast_success, null, str, duration);
    }

    public static void showToastFail(Context context, int resId, int duration) {
        showToast(context, R.drawable.toast_fail, null, context.getString(resId), duration);
    }
    public static void showToastFail(Context context, String str, int duration) {
        showToast(context, R.drawable.toast_fail, null, str, duration);
    }

    public static void showToastDream(Context context, int resId, int duration) {
        showToast(context, R.drawable.toast_dream, null, context.getString(resId), duration);
    }
    public static void showToastDream(Context context, String str, int duration) {
        showToast(context, R.drawable.toast_dream, null, str, duration);
    }

	public static void showToastInfoShort(Context context, int resId) {
        if(result == null){
            showToast(context, R.drawable.toast_tip, null, context.getString(resId), Toast.LENGTH_SHORT);
        }else {
            result.cancel();
            showToast(context, R.drawable.toast_tip, null, context.getString(resId), Toast.LENGTH_SHORT);
        }

	}

	public static void showToastInfoShort(Context context, CharSequence msg) {
        if (result == null){
            showToast(context, R.drawable.toast_tip, null, msg.toString(), Toast.LENGTH_SHORT);
        }else{
            result.cancel();
            showToast(context, R.drawable.toast_tip, null, msg.toString(), Toast.LENGTH_SHORT);
        }
	}

    public static void showCopyToastInfoShort(Context context, CharSequence msg) {

        if (result == null){
            showToast(context, R.drawable.toast_success , null, msg.toString(), Toast.LENGTH_SHORT);
        }else{
            result.cancel();
            showToast(context, R.drawable.toast_success , null, msg.toString(), Toast.LENGTH_SHORT);
        }
	}

	public static void showToastInfoLong(Context context, int resId) {
        showToast(context, R.drawable.toast_tip, null, context.getString(resId), Toast.LENGTH_LONG);
	}

	public static void showToastInfoLong(Context context, CharSequence msg) {
        if (result == null){
            showToast(context, R.drawable.toast_tip, null, msg.toString(), Toast.LENGTH_LONG);
        }else{
            result.cancel();
            showToast(context, R.drawable.toast_tip, null, msg.toString(), Toast.LENGTH_LONG);
        }


	}

	public static void showToastErrorInfo(Context context, List<String> errors) {
		StringBuilder builder = new StringBuilder();
		final int size = errors.size();
		for (int i = 0; i < size; i++) {
			builder.append((i + 1) + "." + errors.get(i));
			builder.append("\n");
		}
		builder.deleteCharAt(builder.length() - 1);
		showToastInfoLong(context, builder.toString());
	}
}