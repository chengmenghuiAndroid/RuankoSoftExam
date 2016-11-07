package com.itee.exam.app.ui.common.fragment;

import android.support.v4.app.Fragment;

import com.itee.exam.app.AppContext;
import com.itee.exam.app.ui.common.BaseActivity;

/**
 * Created by xin on 2015-05-31.
 */
public class BaseFragment extends Fragment {

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public AppContext getAppContext() {
        if (getActivity() != null) {
            return (AppContext) getActivity().getApplication();
        }else{
            return null;
        }
    }
}
