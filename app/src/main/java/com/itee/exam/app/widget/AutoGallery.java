package com.itee.exam.app.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

@SuppressWarnings("deprecation")
public class AutoGallery extends Gallery {
	public AutoGallery(Context context) {
		super(context);
	}

	public AutoGallery(Context context, AttributeSet paramAttributeSet) {
		super(context, paramAttributeSet);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (velocityX > 0) {
			// 往左边滑动
			super.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
		} else {
			// 往右边滑动
			super.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
		}
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent paramMotionEvent1,
			MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {
		float f = 1.4F * paramFloat1;
		return super.onScroll(paramMotionEvent1, paramMotionEvent2, f,
				paramFloat2);

	}
}
