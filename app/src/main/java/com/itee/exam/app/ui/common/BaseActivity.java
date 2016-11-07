package com.itee.exam.app.ui.common;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.itee.exam.app.AppContext;

/**
 * Created by xin on 2015-05-29.
 */
public class BaseActivity extends AppCompatActivity {

    public AppContext getAppContext() {
        return (AppContext) getApplication();
    }

    /**
     * 根据传入的类(class)打开指定的activity
     * @param pClass
     */
    protected void openActivity(Class<?> pClass) {
        Intent _Intent = new Intent();
        _Intent.setClass(this, pClass);
        startActivity(_Intent);
    }

    protected void openActivityByIntent(Intent pIntent){
        startActivity(pIntent);
    }


}
