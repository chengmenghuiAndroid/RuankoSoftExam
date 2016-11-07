package com.itee.exam.app.widget;

import java.io.Serializable;

/**
 * Created by pkwsh on 2016-08-08.
 */
public class Pickers implements Serializable {
    private static final long serialVersionUID = 1L;

    private String showConetnt;
    private String showId;

    public String getShowConetnt() {
        return showConetnt;
    }

    public String getShowId() {
        return showId;
    }

    public Pickers(String showConetnt, String showId) {
        super();
        this.showConetnt = showConetnt;
        this.showId = showId;
    }

    public Pickers() {
        super();
    }
}
