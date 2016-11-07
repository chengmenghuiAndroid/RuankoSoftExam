package com.itee.exam.app.ui.signup;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.itee.exam.R;
import com.itee.exam.app.AppContext;
import com.itee.exam.app.ui.common.fragment.BaseFragment;
import com.itee.exam.core.utils.StringUtils;

import java.util.Date;

/**
 * Created by pkwsh on 2016-07-30.
 */
public class Fragment2 extends BaseFragment {
    private boolean flag = false;
    private ViewHolder holder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);
        flag = true;
        initView(view);
        return view;
    }

    private void initView(View view) {
        holder = new ViewHolder(view);
        holder.highestEdu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AppContext.apply.setHighestEducation(holder.highestEdu.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        holder.highestDegree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AppContext.apply.setHighestDegree(holder.highestDegree.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        holder.collegesType1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppContext.apply.setHighestCollegesType(holder.collegesType1.getText().toString());
                }
            }
        });
        holder.collegesType2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppContext.apply.setHighestCollegesType(holder.collegesType2.getText().toString());
                }
            }
        });
        holder.collegesType3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppContext.apply.setHighestCollegesType(holder.collegesType3.getText().toString());
                }
            }
        });

        holder.collegesName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                AppContext.apply.setHighestColleges(s.toString());
            }
        });

        holder.entranceDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    AppContext.apply.setEntranceTime(holder.entranceDate.getText().toString());
                }
            }
        });

        holder.entranceDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    DatepickerDialogFragment dialogFragment = new DatepickerDialogFragment();
                    dialogFragment.setOnDateSelected(new DatepickerDialogFragment.DateSelectedListener() {
                        @Override
                        public void onDateSelected(String date) {
                            holder.entranceDate.setText(date);
                            AppContext.apply.setEntranceTime(date);
                        }
                    });
                    dialogFragment.show(getFragmentManager(), "DatepickerDialog");
                    return true;
                }
                return true;
            }
        });

        holder.professionalType1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppContext.apply.setProfessionalOrSubjects(holder.professionalType1.getText().toString());
                }
            }
        });
        holder.professionalType2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppContext.apply.setProfessionalOrSubjects(holder.professionalType2.getText().toString());
                }
            }
        });
        holder.professionalType3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppContext.apply.setProfessionalOrSubjects(holder.professionalType3.getText().toString());
                }
            }
        });

        holder.occupationState1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppContext.apply.setOccupationState("在职");
                    holder.company.setVisibility(View.VISIBLE);
                    holder.colleges.setVisibility(View.GONE);
                    AppContext.apply.setFaculties("");
                    AppContext.apply.setProfessional("");
                }
            }
        });
        holder.occupationState2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppContext.apply.setOccupationState("在读");
                    holder.company.setVisibility(View.GONE);
                    holder.colleges.setVisibility(View.VISIBLE);
                    AppContext.apply.setCompanyType("");
                    AppContext.apply.setCompanyName("");
                    AppContext.apply.setJobLevel("");
                }
            }
        });
        holder.occupationState3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppContext.apply.setOccupationState("无业");
                    holder.company.setVisibility(View.GONE);
                    holder.colleges.setVisibility(View.GONE);
                    AppContext.apply.setCompanyType("");
                    AppContext.apply.setCompanyName("");
                    AppContext.apply.setJobLevel("");
                    AppContext.apply.setFaculties("");
                    AppContext.apply.setProfessional("");
                }
            }
        });

        holder.companyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AppContext.apply.setCompanyType(holder.companyType.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        holder.companyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    AppContext.apply.setCompanyName(s.toString());
            }
        });

        holder.jobLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AppContext.apply.setJobLevel(holder.jobLevel.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        holder.faculties.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    AppContext.apply.setFaculties(s.toString());

            }
        });
        holder.professional.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                AppContext.apply.setProfessional(s.toString());
            }
        });
    }

    @Override
    public void onResume() {
        if (flag) {
            if(StringUtils.isNotBlank(AppContext.apply.getHighestEducation())) {
                setSpinnerItemSelectedByValue(holder.highestEdu, AppContext.apply.getHighestEducation());
            }
            if(StringUtils.isNotBlank(AppContext.apply.getHighestDegree())) {
                setSpinnerItemSelectedByValue(holder.highestDegree, AppContext.apply.getHighestDegree());
            }
            String type=AppContext.apply.getHighestCollegesType();
            if(holder.collegesType1.getText().toString().equals(type)){
                holder.collegesType1.setChecked(true);
            }else if(holder.collegesType2.getText().toString().equals(type)){
                holder.collegesType2.setChecked(true);
            }else if(holder.collegesType3.getText().toString().equals(type)){
                holder.collegesType3.setChecked(true);
            }
            holder.collegesName.setText(AppContext.apply.getHighestColleges());
            holder.entranceDate.setText(AppContext.apply.getEntranceTime());
            type=AppContext.apply.getProfessionalOrSubjects();
            if(holder.professionalType1.getText().toString().equals(type)){
                holder.professionalType1.setChecked(true);
            }else if(holder.professionalType2.getText().toString().equals(type)){
                holder.professionalType2.setChecked(true);
            }else if(holder.professionalType3.getText().toString().equals(type)){
                holder.professionalType3.setChecked(true);
            }
            type=AppContext.apply.getOccupationState();
            if("在职".equals(type)){
                holder.occupationState1.setChecked(true);
                holder.company.setVisibility(View.VISIBLE);
                holder.colleges.setVisibility(View.GONE);
            }else if("在读".equals(type)){
                holder.occupationState2.setChecked(true);
                holder.company.setVisibility(View.GONE);
                holder.colleges.setVisibility(View.VISIBLE);
            }else if("无业".equals(type)){
                holder.occupationState3.setChecked(true);
                holder.company.setVisibility(View.GONE);
                holder.colleges.setVisibility(View.GONE);
            }
            setSpinnerItemSelectedByValue(holder.companyType,AppContext.apply.getCompanyType());
            holder.companyName.setText(AppContext.apply.getCompanyName());
            setSpinnerItemSelectedByValue(holder.jobLevel,AppContext.apply.getJobLevel());
            holder.faculties.setText(AppContext.apply.getFaculties());
            holder.professional.setText(AppContext.apply.getProfessional());
            flag = false;
        }
        super.onResume();
    }

    private void setSpinnerItemSelectedByValue(Spinner spinner, String value) {
        if(StringUtils.isBlank(value)){
            spinner.setSelection(0);
            return;
        }
        SpinnerAdapter apsAdapter = spinner.getAdapter(); //得到SpinnerAdapter对象
        int k = apsAdapter.getCount();
        for (int i = 0; i < k; i++) {
            if (value.equals(apsAdapter.getItem(i).toString())) {
                spinner.setSelection(i, true);// 默认选中项
                break;
            }
        }
    }

    private class ViewHolder {
        private Spinner highestEdu;
        private Spinner highestDegree;
        private RadioButton collegesType1;
        private RadioButton collegesType2;
        private RadioButton collegesType3;
        private EditText collegesName;
        private EditText entranceDate;
        private RadioButton professionalType1;
        private RadioButton professionalType2;
        private RadioButton professionalType3;
        private RadioButton occupationState1;
        private RadioButton occupationState2;
        private RadioButton occupationState3;
        private LinearLayout company;
        private Spinner companyType;
        private EditText companyName;
        private Spinner jobLevel;
        private LinearLayout colleges;
        private EditText faculties;
        private EditText professional;

        public ViewHolder(View view) {
            highestEdu = (Spinner) view.findViewById(R.id.sp_highest_edu);
            highestDegree = (Spinner) view.findViewById(R.id.sp_highest_degree);
            collegesType1 = (RadioButton) view.findViewById(R.id.rb_colleges_type_1);
            collegesType2 = (RadioButton) view.findViewById(R.id.rb_colleges_type_2);
            collegesType3 = (RadioButton) view.findViewById(R.id.rb_colleges_type_3);
            collegesName = (EditText) view.findViewById(R.id.et_colleges_name);
            entranceDate = (EditText) view.findViewById(R.id.et_entrance_date);
            professionalType1 = (RadioButton) view.findViewById(R.id.rb_pro_sub_1);
            professionalType2 = (RadioButton) view.findViewById(R.id.rb_pro_sub_2);
            professionalType3 = (RadioButton) view.findViewById(R.id.rb_pro_sub_3);
            occupationState1 = (RadioButton) view.findViewById(R.id.rb_occupation_state_1);
            occupationState2 = (RadioButton) view.findViewById(R.id.rb_occupation_state_2);
            occupationState3 = (RadioButton) view.findViewById(R.id.rb_occupation_state_3);
            company = (LinearLayout) view.findViewById(R.id.ll_company_lay);
            companyType = (Spinner) view.findViewById(R.id.sp_company_type);
            companyName = (EditText) view.findViewById(R.id.et_company_name);
            jobLevel = (Spinner) view.findViewById(R.id.sp_job_level);
            colleges = (LinearLayout) view.findViewById(R.id.ll_colleges);
            faculties = (EditText) view.findViewById(R.id.et_faculties);
            professional = (EditText) view.findViewById(R.id.et_professional);
        }
    }
}
