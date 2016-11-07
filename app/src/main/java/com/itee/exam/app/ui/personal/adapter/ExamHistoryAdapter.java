package com.itee.exam.app.ui.personal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.itee.exam.R;
import com.itee.exam.app.entity.ExamHistory;

import java.util.List;

/**
 * Created by jack on 2016-03-23.
 */
public class ExamHistoryAdapter extends BaseAdapter {
    private Context mContext;
    private List<ExamHistory> history;

    public ExamHistoryAdapter(Context mContext, List<ExamHistory> history) {
        this.mContext = mContext;
        this.history = history;
    }

    @Override
    public int getCount() {
        return history.size();
    }

    @Override
    public Object getItem(int position) {
        return history.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.recycler_item_exam_history, null);
            viewHolder = new ViewHolder();
            viewHolder.examId = (TextView) convertView.findViewById(R.id.exam_history_examid);
            viewHolder.examName = (TextView) convertView.findViewById(R.id.exam_history_examname);
            viewHolder.hisId = (TextView) convertView.findViewById(R.id.exam_history_exam_hisid);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.examId.setText(String.valueOf(history.get(position).getExamId()));
        viewHolder.examName.setText(history.get(position).getExamName());
        viewHolder.hisId.setText(String.valueOf(history.get(position).getHistId()));
        return convertView;
    }


    public class ViewHolder {
        TextView examId;
        TextView examName;
        TextView hisId;
    }

}
