package com.itee.exam.app.ui.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Gallery;
import android.widget.Toast;

import com.itee.exam.R;
import com.itee.exam.app.ui.common.BaseActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by pkwsh on 2016-03-31.
 */
public class ImageActivity extends BaseActivity implements View.OnTouchListener {
    // 屏幕宽度
    public static int screenWidth;
    // 屏幕高度
    public static int screenHeight;
    private MyGallery gallery;

    private Bitmap mBitmap;
    private String path;

    private float beforeLenght = 0.0f; // 两触点距离
    private float afterLenght = 0.0f; // 两触点距离
    private  boolean isScale = false;
    private float currentScale = 1.0f;// 当前图片的缩放比率
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Bundle bundle=getIntent().getExtras();
        path=bundle.getString("Path");
        screenWidth = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        screenHeight = getWindow().getWindowManager().getDefaultDisplay().getHeight();
        initView();
    }

    private void initView(){
        gallery = (MyGallery) findViewById(R.id.gallery);
        gallery.setVerticalFadingEdgeEnabled(false);// 取消竖直渐变边框
        gallery.setHorizontalFadingEdgeEnabled(false);// 取消水平渐变边框
        new Thread(connectNet).start();
    }

    private Runnable connectNet = new Runnable(){
        @Override
        public void run() {
            try {
                mBitmap = BitmapFactory.decodeStream(getImageStream(path));
                connectHanlder.sendEmptyMessage(0);
            } catch (Exception e) {
                Looper.prepare();
                Toast.makeText(ImageActivity.this,"无法链接网络！", Toast.LENGTH_SHORT).show();
                ImageActivity.this.finish();
                Looper.loop();
                e.printStackTrace();
            }

        }

    };

    private Handler connectHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 更新UI，显示图片
            if (mBitmap != null) {
                gallery.setAdapter(new GalleryAdapter(ImageActivity.this,mBitmap));
            }else {
                Toast.makeText(ImageActivity.this,"下载图片失败！", Toast.LENGTH_SHORT).show();
                ImageActivity.this.finish();
            }
        }
    };

    public InputStream getImageStream(String path) throws Exception{
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
            return conn.getInputStream();
        }
        return null;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN:// 多点缩放
                beforeLenght = spacing(event);
                if (beforeLenght > 5f) {
                    isScale = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isScale) {
                    afterLenght = spacing(event);
                    if (afterLenght < 5f)
                        break;
                    float gapLenght = afterLenght - beforeLenght;
                    if (gapLenght == 0) {
                        break;
                    } else if (Math.abs(gapLenght) > 5f) {
                        float scaleRate = gapLenght / 854;// 缩放比例
                        Animation myAnimation_Scale = new ScaleAnimation(currentScale, currentScale + scaleRate, currentScale, currentScale + scaleRate, Animation.RELATIVE_TO_SELF, 0.5f,
                                Animation.RELATIVE_TO_SELF, 0.5f);
                        myAnimation_Scale.setDuration(100);
                        myAnimation_Scale.setFillAfter(true);
                        myAnimation_Scale.setFillEnabled(true);
                        currentScale = currentScale + scaleRate;
                        gallery.getSelectedView().setLayoutParams(new Gallery.LayoutParams((int) (480 * (currentScale)), (int) (854 * (currentScale))));
                        beforeLenght = afterLenght;
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                isScale = false;
                break;
        }

        return false;
    }

    /**
     * 就算两点间的距离
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }
}
