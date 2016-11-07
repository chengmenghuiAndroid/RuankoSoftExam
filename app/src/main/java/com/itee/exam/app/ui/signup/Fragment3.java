package com.itee.exam.app.ui.signup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.itee.exam.app.Constants;
import com.itee.exam.core.utils.StringUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import com.itee.exam.R;
import com.itee.exam.app.AppContext;
import com.itee.exam.app.ui.common.fragment.BaseFragment;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pkwsh on 2016-07-30.
 */
public class Fragment3 extends BaseFragment implements View.OnClickListener {
    private boolean flag = false;
    private ImageView photo;
    private ViewHolder holder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3, container, false);
        photo = (ImageView) view.findViewById(R.id.iv_photo_example);
        photo.setOnClickListener(this);
        flag = true;
        initView(view);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_photo_example:
                Intent intent = new Intent(getActivity(), PhotoUploadActivity.class);
                startActivityForResult(intent, 200);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            String path = data.getStringExtra("photo");
            AppContext.apply.setUploadPhoto(path);
            path = path.replace("\\", "/");
            path = AppContext.SERVER_IMG_URL + "/" + path;
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(path, photo, getDisplayImageOptions());
        }
    }

    private void initView(View view) {
        holder = new ViewHolder(view);
        holder.email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (holder.email.getText().length() > 0) {
                    if(isEmail(s.toString())){
                        AppContext.apply.setEmail(s.toString());
                    }else {
                        holder.email.setError("请输入正确邮箱地址");
                    }
                }
            }
        });

        holder.addressType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AppContext.apply.setAddressCategory(holder.addressType.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        holder.address.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    AddressDialog dialog = new AddressDialog();
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 1);
                    dialog.setArguments(bundle);
                    dialog.setListner(new AddressDialog.onAddressListner() {
                        @Override
                        public void onAddressListner(String address) {
                            holder.address.setText(address);
                            AppContext.apply.setAddress(address);
                        }
                    });
                    dialog.show(getFragmentManager(), "AddressDialog");
                    return true;
                }
                return true;
            }
        });

        holder.detailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    AppContext.apply.setDetailAddress(s.toString());
            }
        });


        holder.postcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                AppContext.apply.setPostcode(s.toString());
            }
        });
    }

    public static boolean isEmail(String strEmail) {
        String strPattern = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }

    @Override
    public void onResume() {
        if (flag) {
            holder.email.setText(AppContext.apply.getEmail());
            holder.telphone.setText(AppContext.apply.getPhoneNumber());
            setSpinnerItemSelectedByValue(holder.addressType, AppContext.apply.getAddressCategory());
            holder.address.setText(AppContext.apply.getAddress());
            holder.detailAddress.setText(AppContext.apply.getDetailAddress());
            holder.postcode.setText(AppContext.apply.getPostcode());
            String path = AppContext.apply.getUploadPhoto();
            if(StringUtils.isNotBlank(path)){
                path = path.replace("\\", "/");
                path = AppContext.SERVER_IMG_URL + "/" + path;
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(path, photo, getDisplayImageOptions());
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

    public DisplayImageOptions getDisplayImageOptions() {
        DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.photo_example) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.photo_example)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.photo_example)  //设置图片加载/解码过程中l错误时候显示的图片
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();//构建完成
        return defaultDisplayImageOptions;
    }

    private class ViewHolder {
        private EditText email;
        private TextView telphone;
        private Spinner addressType;
        private EditText address;
        private EditText detailAddress;
        private EditText postcode;

        public ViewHolder(View view) {
            email = (EditText) view.findViewById(R.id.et_apply_email);
            telphone = (TextView) view.findViewById(R.id.tv_apply_tel);
            addressType = (Spinner) view.findViewById(R.id.sp_contact_address_type);
            address = (EditText) view.findViewById(R.id.et_address);
            detailAddress = (EditText) view.findViewById(R.id.et_detail_address);
            postcode = (EditText) view.findViewById(R.id.et_postcode);
        }
    }

}
