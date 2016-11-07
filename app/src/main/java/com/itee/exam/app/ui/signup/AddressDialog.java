package com.itee.exam.app.ui.signup;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.itee.exam.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pkwsh on 2016-08-03.
 */
public class AddressDialog extends DialogFragment {
    private boolean flag = false;
    private int type = 0;
    private ViewHolder holder;

    public interface onAddressListner {
        void onAddressListner(String address);
    }

    private onAddressListner listner;

    public void setListner(onAddressListner listner) {
        this.listner = listner;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        type = getArguments().getInt("type");
        flag = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.address, null);
        holder = new ViewHolder(view);
        if(2==type){
            holder.item.setVisibility(View.GONE);
        }
        holder.province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Map<String,String> map=(Map<String,String>)holder.province.getAdapter().getItem(position);
                initSpinner(map,holder.city);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(1==type){
            holder.city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Map<String,String> map=(Map<String,String>)holder.city.getAdapter().getItem(position);
                    initSpinner(map,holder.county);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        builder.setView(view).setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String address=null;
                        if (1 == type) {
                            address = ((Map)holder.province.getSelectedItem()).get("AddressName").toString() + ",";
                            address += ((Map)holder.city.getSelectedItem()).get("AddressName").toString() + ",";
                            address += ((Map)holder.county.getSelectedItem()).get("AddressName").toString();
                        } else if (2 == type) {
                            address = ((Map)holder.province.getSelectedItem()).get("AddressName").toString() + ",";
                            address += ((Map)holder.city.getSelectedItem()).get("AddressName").toString();
                        }

                        listner.onAddressListner(address);
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    private void initSpinner(Map<String,String> map,Spinner spinner){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/exam/db/pcc.db";
        SQLiteDatabase database=SQLiteDatabase.openOrCreateDatabase(path, null);
        String sql="select ID,NAME from area where PARENT_ID='"+map.get("AddressId")+"'";
        Cursor cursor=database.rawQuery(sql,null);
        List<Map<String,String>> list=new ArrayList<Map<String, String>>();
        while (cursor.moveToNext()){
            Map<String,String> map1=new HashMap<String, String>();
            map1.put("AddressId",cursor.getString(0));
            map1.put("AddressName",cursor.getString(1));
            list.add(map1);
        }
        cursor.close();
        database.close();
        SimpleAdapter adapter=new SimpleAdapter(getContext(),list, R.layout.address_item,
                new String[]{"AddressId","AddressName"},
                new int[]{R.id.address_item_id,R.id.address_item_name});
        spinner.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        if (flag) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/exam/db/pcc.db";
            SQLiteDatabase database=SQLiteDatabase.openOrCreateDatabase(path, null);
            String sql="select ID,NAME from area where PARENT_ID='0'";
            Cursor cursor=database.rawQuery(sql,null);
            List<Map<String,String>> list=new ArrayList<Map<String, String>>();
            while (cursor.moveToNext()){
                Map<String,String> map=new HashMap<String, String>();
                map.put("AddressId",cursor.getString(0));
                map.put("AddressName",cursor.getString(1));
                list.add(map);
            }
            cursor.close();
            database.close();
            SimpleAdapter adapter=new SimpleAdapter(getContext(),list, R.layout.address_item,
                    new String[]{"AddressId","AddressName"},
                    new int[]{R.id.address_item_id,R.id.address_item_name});
            holder.province.setAdapter(adapter);
            flag = false;
        }
        super.onResume();
    }

    public final class ViewHolder {
        public Spinner province;
        public Spinner city;
        public Spinner county;
        public LinearLayout item;

        public ViewHolder(View view) {
            province = (Spinner) view.findViewById(R.id.sp_address_province);
            city = (Spinner) view.findViewById(R.id.sp_address_city);
            county = (Spinner) view.findViewById(R.id.sp_address_county);
            item=(LinearLayout)view.findViewById(R.id.ll_county);
        }
    }
}
