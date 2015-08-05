package com.lucascauthen.uschat.presentation.view.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.controller.base.BasePagerViewPresenter;
import com.lucascauthen.uschat.presentation.view.fragments.newfrag.CameraFragment;
import com.lucascauthen.uschat.presentation.view.fragments.newfrag.ChatListFragment;
import com.lucascauthen.uschat.presentation.view.fragments.newfrag.TabFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class PagerActivity extends BaseActivity implements BasePagerViewPresenter.PagerView, BasePagerViewPresenter.PagerViewChanger {
    private final int NUM_PAGES = 3;

    @InjectView(R.id.main_pager)ViewPager pager;

    @Inject ChatListFragment chatFragment;
    @Inject CameraFragment cameraFragment;
    @Inject
    TabFragment tabFragment;

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
        pager.setCurrentItem(Pages.CAMERA);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    @Override
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
                case Pages.CHAT:
                    return chatFragment;
                case Pages.CAMERA:
                    return cameraFragment;
                case Pages.FRIENDS:
                    return tabFragment;
                default:
                    return chatFragment;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
