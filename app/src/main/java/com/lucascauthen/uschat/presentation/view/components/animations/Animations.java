package com.lucascauthen.uschat.presentation.view.components.animations;

/**
 * Created by lhc on 6/9/15.
 */
public enum Animations {
    SLIDE_FROM_LEFT {
        public SimpleAnimation getAnim() {
            return new SlideFromLeftAnimation();
        }
    },
    FADE {
        public SimpleAnimation getAnim() {
            return new FadeAnimation();
        }
    };
    public abstract SimpleAnimation getAnim();
}
