package com.itee.exam.app.ui.event;

import com.itee.exam.app.entity.Courses;

import java.util.List;

/**
 * Created by rkcoe on 2016/10/27.
 */

public class CoursesEvent {
    List<Courses> courses;
    public CoursesEvent(List<Courses> courses){
        this.courses = courses;
    }

    public List<Courses> getResult() {
        return courses;
    }
}
