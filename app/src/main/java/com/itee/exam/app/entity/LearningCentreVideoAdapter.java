package com.itee.exam.app.entity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.itee.exam.R;

import java.util.List;

/**
 * Created by rkcoe on 2016/10/28.
 */

public class LearningCentreVideoAdapter extends BaseAdapter{

    private Context context ;
    private  LayoutInflater mInflater = null;
    private static String THIS_LEFT = "LearningCentreVideoAdapter";
    private static ChaptersList chaptersListItem;
    private static List<ChaptersList> chaptersList;
    private static int coursesIndex;
//    private final List<SectionsList> sectionsList;

    public LearningCentreVideoAdapter(Context context, ChaptersList chaptersListItem, List<ChaptersList> chaptersList, int
            coursesIndex) {
        mInflater = LayoutInflater.from(context);
        this.context = context ;
        this.chaptersListItem = chaptersListItem;
        this.chaptersList = chaptersList;
        this.coursesIndex = coursesIndex;
    }

    @Override
    public int getCount() {
        return chaptersList.size();
    }

    @Override
    public ChaptersList getItem(int position) {
        return chaptersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view = null ;
        if (convertView  == null){
            holder = new ViewHolder();
            view = View.inflate(context,R.layout.activity_hplayer_item, null);
            holder.tvchaptersSort = (TextView) view.findViewById(R.id.tv_chaptersSort);
            holder.tvchaptersTitle = (TextView) view.findViewById(R.id.tv_chaptersTitle);
//            holder.sectionsListItem = (ListView) convertView.findViewById(R.id.activity_hplayer_sectionslist_item);
            holder.itemNull = (TextView) view.findViewById(R.id.item_null);
            view.setTag(holder);
        }else {
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }
        holder.tvchaptersSort.setText(String.valueOf(chaptersList.get(position).getChaptersSort()));
        holder.tvchaptersTitle.setText(chaptersList.get(position).getChaptersTitle());

        List<SectionsList> sectionsList = chaptersList.get(position).getSectionsList();

        if(sectionsList !=null && sectionsList.size()>0){
//            HplayerSectionsAdapter sectionsAdapter = new HplayerSectionsAdapter(context, sectionsList, coursesIndex,
//                    chaptersList, position);
//            holder.sectionsListItem.setAdapter(sectionsAdapter);
//            SetListViewHigh.setListViewHeightBasedOnChildren(holder.sectionsListItem);
        }else{
            holder.itemNull.setVisibility(View.VISIBLE);//item为null
        }
        Log.e(THIS_LEFT, "sectionsList........."+sectionsList+"........"+position);

        return view;
    }

    public class ViewHolder {
        private TextView tvchaptersSort;
        private TextView tvchaptersTitle;
        private ListView sectionsListItem;
        private TextView itemNull;
    }
}
