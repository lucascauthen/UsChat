package com.lucascauthen.uschat.Managers;

import android.app.Fragment;

import com.lucascauthen.uschat.RegisterFragment;

/**
 * Created by lhc on 6/9/15.
 */
public enum Fragments {
    REGISTER {
        public Fragment get() {
            return new RegisterFragment();
        }
    };
    public abstract Fragment get();
}
