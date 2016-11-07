package com.itee.exam.app.entity;

import java.io.Serializable;

/**
 * Created by rkcoe on 2016/3/22.
 */
public class ExamSimplePaper implements Serializable {
    private static final long serialVersionUID = 2866441053387084227L;
    protected int id;
    protected String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
