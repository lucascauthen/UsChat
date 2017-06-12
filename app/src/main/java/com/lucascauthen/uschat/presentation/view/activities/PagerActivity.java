package com.lucascauthen.uschat.presentation.view.activities;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.view.base.PagerView;
import com.lucascauthen.uschat.presentation.view.fragments.CameraFragment;
import com.lucascauthen.uschat.presentation.view.fragments.ChatTabFragment;
import com.lucascauthen.uschat.presentation.view.fragments.FriendTabFragment;
import com.lucascauthen.uschat.util.Permissions;

import javax.inject.Inject;



public class PagerActivity extends BaseActivity {
    private final int NUM_PAGES = 3;

    @InjectView(R.id.mainPager)ViewPager pager;

    @Inject ChatTabFragment chatTabFragment;
    @Inject CameraFragment cameraFragment;
    @Inject FriendTabFragment friendTabFragment;

    private ScreenSlidePagerAdapter pagerAdapter;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, PagerActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        getApplicationComponent().inject(this);

        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        pager.setOffscreenPageLimit(NUM_PAGES);
        pager.setCurrentItem(PagerView.Pages.CAMERA);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0) {//Chat Fragment
                    //pass event off
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void changePage(int index) {
        if(index < pager.getChildCount()) {
            pager.setCurrentItem(index);
        } else {
            throw new RuntimeException("Trying to go to a page that does not exist");
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case PagerView.Pages.CHAT:
                    return chatTabFragment;
                case PagerView.Pages.CAMERA:
                    return cameraFragment;
                case PagerView.Pages.FRIENDS:
                    return friendTabFragment;
                default:
                    return chatTabFragment;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


}
