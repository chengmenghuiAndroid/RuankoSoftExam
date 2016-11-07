package com.itee.exam.events;

/**
 * Created by BG170 on 2015/12/2.
 */
public class WXPayEvent {
    int result;
    public WXPayEvent(int result){
        this.result = result;
    }

    public int getResult() {
        return result;
    }
}
