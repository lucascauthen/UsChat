package com.lucascauthen.uschat.presentation.view.components.customlayouts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by lhc on 6/8/15.
 */
public class SlidingRelativeLayout extends RelativeLayout {
    private float yFraction = 0;
    private float xFraction = 0;

    public SlidingRelativeLayout(Context context) {
        super(context);
    }
    public SlidingRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public void setXFraction(final float fraction) {
        this.xFraction = fraction;
        //Ensures that the first frame does not draw incorrectly
        if(getWidth() == 0) {
            setVisibility(View.INVISIBLE);
        } else {
            setVisibility(View.VISIBLE);
        }
        setTranslationX(getWidth() * fraction);
    }
    public void setYFraction(final float fraction) {
        this.yFraction = fraction;
        //Ensures that the first frame does not draw incorrectly
        if(getHeight() == 0) {
            setVisibility(View.INVISIBLE);
        } else {
            setVisibility(View.VISIBLE);
        }
        setTranslationY(getHeight() * fraction);
    }
    public float getXFraction() {
        return this.xFraction;
    }
    public float getYFraction() {
        return this.yFraction;
    }
}
