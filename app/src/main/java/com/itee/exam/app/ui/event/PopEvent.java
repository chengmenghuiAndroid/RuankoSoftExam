package com.itee.exam.app.ui.event;

/**
 * Created by rkcoe on 2016/10/10.
 */
public class PopEvent {
    private CharSequence textTel;
    public PopEvent(CharSequence textTel){
        this.textTel = textTel;
    }

    public CharSequence getText(){
        return this.textTel;
    }
}
