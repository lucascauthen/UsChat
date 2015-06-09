package com.lucascauthen.uschat.Managers;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.lucascauthen.uschat.Animations.SimpleAnimation;
import com.lucascauthen.uschat.R;


/**
 * Created by lhc on 6/9/15.
 */
public class FragmentTransitionManager {
    private final Activity fragmentedActivity;
    private final FragmentManager fragmentManager;
    public FragmentTransitionManager(Activity activity) {
        this.fragmentedActivity = activity;
        fragmentManager = this.fragmentedActivity.getFragmentManager();
    }
    public void replaceFragment(Fragment newFragment, String fragmentToAdd, String fragmentToRemove, SimpleAnimation animation) {
        Fragment oldFragment = getFragmentByTag(fragmentToRemove);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(animation != null) {
            transaction.setCustomAnimations(animation.getEnter(), animation.getExit(), animation.getReverseEnter(), animation.getReverseExit());
        }

        if(oldFragment != null) {
            transaction.detach(oldFragment);
        }
        transaction.add(R.id.group, newFragment, fragmentToAdd);
        transaction.addToBackStack(fragmentToAdd);
        transaction.commit();
    }
    //Without an animation
    public void replaceFragment(Fragment newFragment, String fragmentToAdd, String fragmentToRemove) {
        replaceFragment(newFragment, fragmentToAdd, fragmentToRemove, null);
    }
    //Wrapper
    private Fragment getFragmentByTag(String tag) {
        return fragmentedActivity.getFragmentManager().findFragmentByTag(tag);
    }
}
