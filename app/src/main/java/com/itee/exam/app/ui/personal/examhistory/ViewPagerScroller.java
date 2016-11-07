package com.itee.exam.app.ui.personal.examhistory;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;
/**
 * Created by jack on 2016-03-24.
 */
public class ViewPagerScroller extends Scroller {

    private int mScrollDuration = 1500;// 滑动速度
    public ViewPagerScroller(Context context) {
        super(context);
    }
    public ViewPagerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }
    public ViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }
    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }
    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }

}
