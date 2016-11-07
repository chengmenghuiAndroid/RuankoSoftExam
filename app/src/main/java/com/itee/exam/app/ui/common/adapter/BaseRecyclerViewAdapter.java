package com.itee.exam.app.ui.common.adapter;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by xin on 2015/4/9 0009.
 */
public abstract class BaseRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    public interface CompareCallback<T> {
        int compare(T o1, T o2);

        boolean areContentsTheSame(T oldItem, T newItem);

        boolean areItemsTheSame(T item1, T item2);
    }

    private final SortedList<T> mItems;

    public BaseRecyclerViewAdapter(final CompareCallback<T> callback) {
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        mItems = new SortedList<>(entityClass, new SortedList.Callback<T>() {

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public int compare(T o1, T o2) {
                return callback.compare(o1, o2);
            }

            @Override
            public boolean areContentsTheSame(T oldItem, T newItem) {
                return callback.areContentsTheSame(oldItem, newItem);
            }

            @Override
            public boolean areItemsTheSame(T item1, T item2) {
                return callback.areItemsTheSame(item1, item2);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addAll(Collection<T> items) {
        mItems.beginBatchedUpdates();
        try {
            for (T item : items) {
                mItems.add(item);
            }
        } finally {
            mItems.endBatchedUpdates();
        }
    }

    @SafeVarargs
    public final void addAll(T... items) {
        addAll(Arrays.asList(items));
    }

    public void set(Collection<T> items) {
        mItems.beginBatchedUpdates();
        try {
            mItems.clear();
            addAll(items);
        } finally {
            mItems.endBatchedUpdates();
        }
    }

    public void remove(int pos) {
        mItems.removeItemAt(pos);
    }

    public void remove(T t) {
        mItems.remove(t);
    }

    public void removeAll(Collection<T> items) {
        for (T item : items) {
            remove(item);
        }
    }

    public void removeAll() {
        mItems.clear();
    }

    public T get(int position) {
        return mItems.get(position);
    }

    public List<T> getItems() {
        List<T> items = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++) {
            T t = get(i);
            items.add(t);
        }
        return items;
    }

    public void clear() {
        mItems.clear();
    }
}