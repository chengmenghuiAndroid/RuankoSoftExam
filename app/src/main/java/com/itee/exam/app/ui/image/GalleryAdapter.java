package com.itee.exam.app.ui.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by pkwsh on 2016-03-30.
 */
public class GalleryAdapter extends BaseAdapter {
    private Context context;
    private Bitmap mBitmap;

    public GalleryAdapter(Context context, Bitmap mBitmap) {
        this.context = context;
        this.mBitmap = mBitmap;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SmoothImageView view = new SmoothImageView(context, mBitmap.getWidth(), mBitmap.getHeight());
        view.setLayoutParams(new Gallery.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        view.setImageBitmap(mBitmap);
        return view;
    }
}
