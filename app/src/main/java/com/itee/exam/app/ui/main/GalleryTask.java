package com.itee.exam.app.ui.main;

        import android.content.Context;
        import android.os.AsyncTask;
        import android.widget.Gallery;

        import com.itee.exam.R;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

@SuppressWarnings("deprecation")
public class GalleryTask extends AsyncTask<Void, Void, List<Map>> {
    private Gallery gallery;
    private Context context;

    public GalleryTask(Context context, Gallery gallery) {
        this.context = context;
        this.gallery = gallery;
    }

    @Override
    protected List<Map> doInBackground(Void... params) {
        List<Map> list = new ArrayList<Map>();
        Map map = new HashMap();
        map.put("Image", R.drawable.home_banner01);
        map.put("URL", "http://m.ruanko.com/subject/software_exam.html");
        list.add(map);
        map = new HashMap();
        map.put("Image", R.drawable.home_banner02);
        map.put("URL", "http://www.ruanko.com/subject/personal_bility_tranining.html");
        list.add(map);
        map = new HashMap();
        map.put("Image", R.drawable.home_banner03);
        map.put("URL", "http://code.ruanko.com/");
        list.add(map);
        return list;
    }

    @Override
    protected void onPostExecute(List<Map> result) {
        GalleryImageAdapter adapter = new GalleryImageAdapter(context, result);
        gallery.setAdapter(adapter);
    }
}
