package com.lucascauthen.uschat.presentation.view.activities;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.view.fragments.CameraFragment;
import com.lucascauthen.uschat.presentation.view.fragments.ChatFragment;
import com.lucascauthen.uschat.presentation.view.fragments.FriendsFragment;


import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends AppCompatActivity implements PagerChanger{

    private final int NUM_PAGES = 3;
    @InjectView(R.id.main_pager)ViewPager pager;
    private PagerAdapter pagerAdapter;

    private ChatFragment chatFragment;
    private CameraFragment cameraFragment;
    private FriendsFragment friendsFragment;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        //Create the fragments
        chatFragment = ChatFragment.newInstance();
        cameraFragment = CameraFragment.newInstance(this);
        friendsFragment = FriendsFragment.newInstance();

        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        //Sets to middle fragment
        pager.setOffscreenPageLimit(3);
        pager.setCurrentItem(1);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0) {//Chat Fragment
                    chatFragment.onPageSelected(); //Pass message to fragment
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onBackPressed() {

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
                   return friendsFragment;
               default:
                   return chatFragment;
           }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
    private static class Pages {
        public static final int CHAT = 0;
        public static final int CAMERA = 1;
        public static final int FRIENDS = 2;
    }

    public void changePage(int pageIndex) {
        if(pageIndex <= pager.getChildCount()) {
            pager.setCurrentItem(pageIndex);
        }
    }
}
