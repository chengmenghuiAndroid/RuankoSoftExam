package com.itee.exam.app.ui.signup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.itee.exam.R;
import com.itee.exam.app.widget.PickerScrollView;
import com.itee.exam.app.widget.Pickers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by pkwsh on 2016-04-08.
 */
public class DatepickerDialogFragment extends DialogFragment {
    private String date;
    private String year;
    private String month;
    private String day;
    private static List<Pickers> Year = new ArrayList<Pickers>();
    private static List<Pickers> Month = new ArrayList<Pickers>();
    private static List<Pickers> Day = new ArrayList<Pickers>();


    public interface DateSelectedListener {
        void onDateSelected(String date);
    }

    private DateSelectedListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_date_dialog, null);
        //20160808------------begin
        int j=1;
        for (int i = 1900; i <= 2035; i++) {
            Year.add(new Pickers(String.valueOf(i), String.valueOf(j)));
            j++;
        }
        j=1;
        for (int i = 1; i <= 12; i++) {
            Month.add(new Pickers(i < 10 ? "0" + String.valueOf(i) : String.valueOf(i), String.valueOf(j)));
            j++;
        }
        j=1;
        for (int i = 1; i <= 31; i++) {
            Day.add(new Pickers(i < 10 ? "0" + String.valueOf(i) : String.valueOf(i), String.valueOf(j)));
            j++;
        }

        final PickerScrollView wvYear = (PickerScrollView) view.findViewById(R.id.wv_year);
        final PickerScrollView wvMonth = (PickerScrollView) view.findViewById(R.id.wv_month);
        final PickerScrollView wvDay = (PickerScrollView) view.findViewById(R.id.wv_day);
        wvYear.setData(Year);
        wvMonth.setData(Month);
        wvDay.setData(Day);
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        Date curDate = new Date(System.currentTimeMillis());
        String tmp=df.format(curDate);
        wvYear.setSelected(tmp.substring(0,4));
        wvMonth.setSelected(tmp.substring(4,6));
        wvDay.setSelected(tmp.substring(6,8));
        //20160808------------end


        Button ok = (Button) view.findViewById(R.id.btn_date_dialog_ok);
        Button cancel = (Button) view.findViewById(R.id.btn_date_dialog_cancel);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.calendarView);
        builder.setCancelable(false);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date=wvYear.getSelectedItem()+wvMonth.getSelectedItem()+wvDay.getSelectedItem();
//                int year = datePicker.getYear();
//                int month = datePicker.getMonth() + 1;
//                int day = datePicker.getDayOfMonth();
//                date = String.valueOf(year);
//                date += month < 10 ? "0" + String.valueOf(month) : String.valueOf(month);
//                date += day < 10 ? "0" + String.valueOf(day) : String.valueOf(day);
                listener.onDateSelected(date);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public void setOnDateSelected(DateSelectedListener listener) {
        this.listener = listener;
    }
}
