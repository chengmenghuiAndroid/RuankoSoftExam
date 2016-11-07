/**
 * 
 */
package com.itee.exam.core.utils;

import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author moxin
 * 
 */
public abstract class JupiterBaseAdapter<T> extends BaseAdapter {

	private List<T> mItems = new ArrayList<T>();

	/**
	 * 先清空数据项集合，然后添加指定的数据项集合
	 * 
	 * @param items
	 */
	public void setItems(Collection<? extends T> items) {
		mItems.clear();
		addAll(items);
	}

	public List<T> getItems() {
		return mItems;
	}

	/**
	 * 添加数据项
	 * 
	 * @param t
	 */
	public void add(T t) {
		mItems.add(t);
		notifyDataSetChanged();
	}

	/**
	 * 追加指定数据项集合
	 * 
	 * @param items
	 */
	public void addAll(Collection<? extends T> items) {
		mItems.addAll(items);
		notifyDataSetChanged();
	}

	/**
	 * 删除知指定数据项
	 * 
	 * @param t
	 */
	public void remove(T t) {
		mItems.remove(t);
		notifyDataSetChanged();
	}

	/**
	 * 删除指定位置项
	 * 
	 * @param pos
	 */
	public void remove(int pos) {
		mItems.remove(pos);
		notifyDataSetChanged();
	}

	/**
	 * 清空数据项
	 */
	public void clear() {
		mItems.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public T getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}