package com.lucascauthen.uschat.Animations;

import com.lucascauthen.uschat.R;

/**
 * Created by lhc on 6/9/15.
 */
public class FadeAnimation implements SimpleAnimation {
    @Override
    public int getEnter() {
        return R.anim.abc_fade_in;
    }

    @Override
    public int getExit() {
        return R.anim.abc_fade_out;
    }

    @Override
    public int getReverseEnter() {
        return R.anim.abc_fade_in;
    }

    @Override
    public int getReverseExit() {
        return R.anim.abc_fade_out;
    }
}
