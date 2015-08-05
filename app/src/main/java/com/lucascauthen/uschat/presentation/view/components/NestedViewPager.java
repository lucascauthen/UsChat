package com.lucascauthen.uschat.presentation.view.components;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by lhc on 8/5/15.
 */
public class NestedViewPager extends ViewPager {

    public NestedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOffscreenPageLimit(3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
