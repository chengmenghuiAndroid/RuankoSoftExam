package com.itee.exam.app.entity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.itee.exam.R;
import com.itee.exam.app.interf.VideoUrlListerner;

import java.util.List;

/**
 * Created by rkcoe on 2016/10/28.
 */

public class HplayerSectionsAdapter extends BaseAdapter {

    private static String THIS_LEFT = "HplayerSectionsAdapter";
    private static Context context;
    private static List<SectionsList> sectionsList;
    private static List<ChaptersList> chaptersList;

    public VideoUrlListerner mUrlListerner;

    public HplayerSectionsAdapter(Context context, List<SectionsList> sectionsList, int coursesIndex, List<ChaptersList>
            chaptersList, int positionIndext){
        this.context = context ;
        this.sectionsList = sectionsList;
//        this.coursesIndex = coursesIndex;
        this.chaptersList = chaptersList;
        this.mUrlListerner = mUrlListerner;
        Log.e(THIS_LEFT, "sectionsList........."+sectionsList);

    }
    @Override
    public int getCount() {
        return sectionsList.size();
    }

    @Override
    public Object getItem(int position) {
//        return sectionsList.get(position);
        return position;
    }

    @Override
    public long getItemId(int position) {
        return sectionsList.get(position).getSectionsId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder= new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_hplayer_sectionslist_item, null);
            holder.tvsectionsTitle = (TextView) convertView.findViewById(R.id.tv_sectionsTitle);//sectionsTitle : "第2课"
            holder.tvsectionsSort = (TextView) convertView.findViewById(R.id.tv_sectionsSort);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }



//            holder.tvsectionsSort.setText(String.valueOf(sectionsList.get(position).getSectionsTitle()));
//            holder.tvsectionsTitle.setText(String.valueOf(sectionsList.get(position).getSectionsTitle()));

            Log.e(THIS_LEFT, "sectionsList>>>>>>>>>>>>>>>>>"+sectionsList.get(position));
            Log.e(THIS_LEFT, "getSectionsTitle>>>>>>>>>>>>>>>>>"+sectionsList.get(position).getSectionsTitle()+"......."+position);
            Log.e(THIS_LEFT, "chaptersTitle>>>>>>>>>>>>>>>>>"+chaptersList.get(position).getCoursesId()+ "........"+ position);

        return convertView;
    }

    public class ViewHolder{
        private TextView tvsectionsTitle;
        private TextView tvsectionsSort;
    }

}
