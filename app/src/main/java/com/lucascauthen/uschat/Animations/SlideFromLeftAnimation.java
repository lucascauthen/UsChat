package com.lucascauthen.uschat.Animations;

import com.lucascauthen.uschat.R;

/**
 * Created by lhc on 6/9/15.
 */
public class SlideFromLeftAnimation implements SimpleAnimation {
    @Override
    public int getEnter() {
        return R.anim.enter_from_left;
    }

    @Override
    public int getExit() {
        return R.anim.exit_from_right;
    }

    @Override
    public int getReverseEnter() {
        return R.anim.enter_from_right;
    }

    @Override
    public int getReverseExit() {
        return R.anim.exit_from_left;
    }
}
