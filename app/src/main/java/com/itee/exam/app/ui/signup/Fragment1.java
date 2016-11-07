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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.itee.exam.R;
import com.itee.exam.app.AppContext;
import com.itee.exam.app.ui.common.fragment.BaseFragment;
import com.itee.exam.app.ui.untils.IDCard;
import com.itee.exam.core.utils.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by pkwsh on 2016-07-30.
 */
public class Fragment1 extends BaseFragment {
    private boolean flag = false;
    private ViewHolder holder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);
        flag = true;
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        holder = new ViewHolder(view);
        holder.male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppContext.apply.setSex("男");
                }
            }
        });
        holder.female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppContext.apply.setSex("女");
                }
            }
        });
        holder.certificatesType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AppContext.apply.setCertificateType(holder.certificatesType.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.certificatesNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (0 == holder.certificatesType.getSelectedItemPosition()) {
                    if (holder.certificatesNumber.getText().toString().length() > 0) {
                        boolean flag = IDCard.IDCardValidate(s.toString());
                        if (flag) {
                            AppContext.apply.setCertificateNumber(s.toString());
                            String birthdate = s.toString().substring(6, 14);
                            holder.birthdate.setText(birthdate);
                            try {
                                DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                                AppContext.apply.setBirthday(df.parse(birthdate+"000000"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            holder.certificatesNumber.setError("输入的身份证号码错误，请重新输入");
                        }
                    }
                } else {
                    if (holder.certificatesNumber.getText().length() > 0) {
                        AppContext.apply.setCertificateNumber(s.toString());
                    }
                }
            }
        });

        holder.birthAddress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    AddressDialog dialog = new AddressDialog();
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 1);
                    dialog.setArguments(bundle);
                    dialog.setListner(new AddressDialog.onAddressListner() {
                        @Override
                        public void onAddressListner(String address) {
                            holder.birthAddress.setText(address);
                            AppContext.apply.setBirthplace(address);
                        }
                    });
                    dialog.show(getFragmentManager(), "AddressDialog");
                    return true;
                }
                return true;
            }
        });

        AppContext.apply.setCurrentLocation("湖北省");
        holder.curAddress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    AddressDialog dialog = new AddressDialog();
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 2);
                    dialog.setArguments(bundle);
                    dialog.setListner(new AddressDialog.onAddressListner() {
                        @Override
                        public void onAddressListner(String address) {
                            String[] pc=address.split(",");
                            AppContext.apply.setCurrentProvince(pc[0]);
                            holder.curAddress.setText(pc[1]);
                            AppContext.apply.setCurrentCity(pc[1]);
                        }
                    });
                    dialog.show(getFragmentManager(), "AddressDialog");
                    return true;
                }
                return true;
            }
        });

        holder.birthdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    DatepickerDialogFragment dialogFragment = new DatepickerDialogFragment();
                    dialogFragment.setOnDateSelected(new DatepickerDialogFragment.DateSelectedListener() {
                        @Override
                        public void onDateSelected(String date) {
                            holder.birthdate.setText(date);
                            try {
                                DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                                AppContext.apply.setBirthday(df.parse(date + "000000"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    dialogFragment.show(getFragmentManager(), "DatepickerDialog");
                    return true;
                }
                return true;
            }
        });

        holder.realName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                AppContext.apply.setRealName(s.toString());
            }
        });

    }

    @Override
    public void onResume() {
        if (flag) {
            holder.realName.setText(AppContext.apply.getRealName());
            String sex = AppContext.apply.getSex();
            if ("男".equals(sex)) {
                holder.male.setChecked(true);
            } else if ("女".equals(sex)) {
                holder.female.setChecked(true);
            }
            if (StringUtils.isNotBlank(AppContext.apply.getCertificateType())) {
                setSpinnerItemSelectedByValue(holder.certificatesType, AppContext.apply.getCertificateType());
            }
            holder.certificatesNumber.setText(AppContext.apply.getCertificateNumber());
            holder.birthAddress.setText(AppContext.apply.getBirthplace());
            holder.curAddress.setText(AppContext.apply.getCurrentCity());
            if (AppContext.apply.getBirthday() != null) {
                DateFormat df = new SimpleDateFormat("yyyyMMdd");
                holder.birthdate.setText(df.format(AppContext.apply.getBirthday()));
            }
            flag = false;
        }
        super.onResume();
    }

    private void setSpinnerItemSelectedByValue(Spinner spinner, String value) {
        if (StringUtils.isBlank(value)) {
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

    public final class ViewHolder {
        private EditText realName;
        private RadioButton male;
        private RadioButton female;
        private Spinner certificatesType;
        private EditText certificatesNumber;
        private EditText birthAddress;
        private EditText curAddress;
        private EditText birthdate;


        public ViewHolder(View view) {
            realName = (EditText) view.findViewById(R.id.et_apply_real_name);
//            realName = (TextView) view.findViewById(R.id.tv_apply_real_name);
            male = (RadioButton) view.findViewById(R.id.rb_apply_user_sex_male);
            female = (RadioButton) view.findViewById(R.id.rb_apply_user_sex_female);
            certificatesType = (Spinner) view.findViewById(R.id.sp_certificates_type);
            certificatesNumber = (EditText) view.findViewById(R.id.et_certificates_number);
            birthAddress = (EditText) view.findViewById(R.id.et_birth_address);
            curAddress = (EditText) view.findViewById(R.id.et_cur_address);
            birthdate = (EditText) view.findViewById(R.id.tv_birthdate);
        }
    }
}
