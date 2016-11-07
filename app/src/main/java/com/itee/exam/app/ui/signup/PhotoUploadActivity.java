package com.itee.exam.app.ui.signup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.itee.exam.R;
import com.itee.exam.app.AppContext;
import com.itee.exam.app.ui.common.BaseActivity;
import com.itee.exam.core.utils.ProgressTask;
import com.itee.exam.core.utils.Toasts;
import com.itee.exam.vo.HttpMessage;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.util.ArrayList;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import retrofit.mime.TypedFile;

public class PhotoUploadActivity extends BaseActivity {
    private boolean EXTRA_SHOW_CAMERA = false;
    private int EXTRA_SELECT_COUNT = 1;
    private int EXTRA_SELECT_MODE = 0;
    private ArrayList<String> mSelectPath;
    private final int REQUEST_IMAGE = 100;

    private ImageView photo;
    private TextView photoPath;
    private Button photoUpload;
    private String path=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_upload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSelectPath = new ArrayList<String>();
        photo = (ImageView) findViewById(R.id.iv_user_photo);
        photoPath = (TextView) findViewById(R.id.tv_photo_path);
        photoUpload = (Button) findViewById(R.id.btn_photo_upload);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void funBrowse(View view){
        Intent intent = new Intent(this, MultiImageSelectorActivity.class);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, EXTRA_SHOW_CAMERA);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, EXTRA_SELECT_COUNT);
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, EXTRA_SELECT_MODE);
        intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    public void funPhotoUpload(View view) {
        new ProgressTask<HttpMessage>(this,"正在上传照片"){
            @Override
            public HttpMessage call() throws Exception {
                path=mSelectPath.get(0);
                TypedFile file = new TypedFile("image/jpg", new File(path));
                return getAppContext().getHttpService().uploadPhoto(AppContext.apply.getApplyId(),file);
            }

            @Override
            protected void onSuccess(HttpMessage message) throws Exception {
                if("success".equals(message.getResult())){
                    Intent intent=new Intent();
                    intent.putExtra("photo",message.getObject().toString());
                    setResult(200,intent);
                    PhotoUploadActivity.this.finish();
                }else {
                    Toasts.showToastInfoShort(PhotoUploadActivity.this,"照片上传失败，请重新上传");
                }

                super.onSuccess(message);
            }
        }.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                for (String path : mSelectPath) {
                    photoPath.setText(path);
                    photo.setVisibility(View.VISIBLE);
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    photo.setImageBitmap(bitmap);
                    int check = CheckPhoto(path);
                    if (1 == check) {
                        Toasts.showToastInfoShort(this, "照片文件容量大小不得大于100KB");
                    } else if (2 == check) {
                        Toasts.showToastInfoShort(this, "宽不得小于140像素、高不得小于210像素");
                    } else if (3 == check) {
                        Toasts.showToastInfoShort(this, "照片宽与高的比例需为2:3");
                    } else if (0 == check) {
                        photoUpload.setVisibility(View.VISIBLE);
                    } else if (-1 == check) {
                        Toasts.showToastInfoShort(this, "照片验证异常，请重新上传");
                    }
                }
            }
        }
    }

    private int CheckPhoto(String path) {
        int res = -1;
        try {
            File picture = new File(path);
            FileInputStream fis = new FileInputStream(picture);
            float size = picture.length() / 1024;
            //获取尺寸
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;
            if (size > 100) {
                res = 1;
            } else if (imageWidth < 140 || imageHeight < 210) {
                res = 2;
            } else if (2 * imageHeight != 3 * imageWidth) {
                res = 3;
            } else {
                res = 0;
            }

        } catch (Exception ex) {
            res = -1;
        }
        return res;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(500);
            PhotoUploadActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
