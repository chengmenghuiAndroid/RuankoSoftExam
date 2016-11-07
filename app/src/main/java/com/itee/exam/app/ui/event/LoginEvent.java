package com.itee.exam.app.ui.event;

/**
 * Created by xin on 2015-06-11.
 */
public class LoginEvent {
    int result;
    public LoginEvent(int result){
        this.result = result;
    }

    public int getResult() {
        return result;
    }
}
