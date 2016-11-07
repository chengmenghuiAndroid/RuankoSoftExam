package com.itee.exam.app.ui.vo;

import java.io.Serializable;

/**
 * Created by malin on 2016/8/1.
 */
public class ApplyPlace implements Serializable {
    private int id;
    private String applyPlace;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApplyPlace() {
        return applyPlace;
    }

    public void setApplyPlace(String applyPlace) {
        this.applyPlace = applyPlace;
    }
}
