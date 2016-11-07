package com.itee.exam.app.ui.doexam;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bm.library.PhotoView;
import com.itee.exam.R;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.utils.ImageLoaderUtils;

/**
 * Created by rkcoe on 2016/10/8.
 */
public class ShowWebImageActivity extends BaseActivity{

    //获取图片的地址
    private String imageUrl = null;

    //用户放大,缩小,旋转,
    private PhotoView imageView = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_webimage);
        initView();
    }

    private void initView() {
        imageUrl = getIntent().getStringExtra("image");
        imageView = (PhotoView) findViewById(R.id.show_webimage_imageview);
        // 启用图片缩放功能
        imageView.enable();
        //显示图片
        ImageLoaderUtils.displayWhole(this, imageView, imageUrl);
    }
}
