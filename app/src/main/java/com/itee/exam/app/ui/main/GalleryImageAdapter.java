package com.itee.exam.app.ui.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.itee.exam.R;

@SuppressWarnings("deprecation")
public class GalleryImageAdapter extends BaseAdapter {
	private Context context;
	private List<Map> list;
	private int len;
	public GalleryImageAdapter(Context context, List<Map> list){
		this.context=context;
		this.list=list;
		this.len=list.size();
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position%len);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.gallery_item, null);
			holder.imageView = (ImageView) convertView.findViewById(R.id.gallery_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.imageView.setImageResource((int)list.get(position%len).get("Image"));
		holder.imageView.setTag(list.get(position%len).get("URL"));
		convertView.setLayoutParams(new Gallery.LayoutParams(
				Gallery.LayoutParams.MATCH_PARENT,
				Gallery.LayoutParams.MATCH_PARENT));
		return convertView;
	}

	public final class ViewHolder {
		public ImageView imageView;
	}
}
