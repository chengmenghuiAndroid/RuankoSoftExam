package com.itee.exam.app.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.itee.exam.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by rkcoe on 2016/10/25.
 */

public class LearningCentreAdapter extends BaseAdapter{


    private LayoutInflater mInflater = null;
    private List<Courses> courses ;
    private static String THIS_LEFT = "LearningCentreAdapter";
    private  Courses coursesItem;
    private  Context context;

    public LearningCentreAdapter(Context context, List<Courses> courses) {
        this.mInflater = LayoutInflater.from(context);
        this.courses = courses ;
        this.context = context;
        Log.e(THIS_LEFT, "courses:" + courses);
    }

    @Override
    public int getCount() {
        return courses.size();
    }

    @Override
    public Courses getItem(int position) {
        return courses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return courses.get(position).getCoursesId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.activity_learning_centry_item, null);
            holder.videoPic= (ImageView) convertView.findViewById(R.id.video_pic);
            holder.time= (TextView) convertView.findViewById(R.id.video_time);
            holder.title= (TextView) convertView.findViewById(R.id.video_title);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.title.setText(courses.get(position).getCoursesTitle());
        holder.time.setText(courses.get(position).getLastUpdatetime());
        String coursesImgUrl = courses.get(position).getCoursesImg();
        if (coursesImgUrl !=null ){
            Bitmap httpBitmap = getHttpBitmap(coursesImgUrl);
            holder.videoPic.setImageBitmap(httpBitmap);
//            ImageLoaderUtils.displayWhole(context, holder.videoPic, coursesImgUrl);
        }else {
            holder.videoPic.setImageResource(R.drawable.learning_pic);
        }
        Log.e(THIS_LEFT, "url:" +coursesImgUrl);
        Log.e(THIS_LEFT, "title:" +courses.get(position).getCoursesTitle());
        return convertView;
    }

    public class ViewHolder{
        public TextView title;
        public TextView time;
        public ImageView videoPic;
    }

    public static Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return bitmap;

    }

}
