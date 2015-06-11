package com.lucascauthen.uschat;


import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.parse.ParseUser;


public class MainActivity extends AppCompatActivity implements CameraFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener, FriendsFragment.OnFragmentInteractionListener {

    private final int NUM_PAGES = 3;
    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private boolean showStatusBar = true;
    private boolean updateStatusBar = true;
    private HomeFragment homeFragment;
    private CameraFragment cameraFragment;
    private FriendsFragment friendsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //This takes a long time...
        homeFragment = new HomeFragment();
        cameraFragment = new CameraFragment();
        friendsFragment = new FriendsFragment();

        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        //TODO: Fix action bar stuff
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_IDLE:
                        if(updateStatusBar) {
                            switch(pager.getCurrentItem()) {
                                case Pages.CAMERA:
                                    //Hide status bar
                                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                                    break;
                                default:
                                    //Show status bar
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                                    showStatusBar = true;
                                    break;
                            }
                            updateStatusBar = false;
                        }
                        break;
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        if(!updateStatusBar) { //Prevents this from getting set over and over again
                            updateStatusBar = true; //Update the status bar when the pager becomes idle again
                        }
                        if(showStatusBar) {
                            //Hide status bar
                            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                            showStatusBar = false;
                        }
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:

                        break;
                }
            }
        });
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //Sets to middle fragment
        pager.setCurrentItem(1);
    }


    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
           switch(position) {
               case Pages.HOME:
                   return homeFragment;
               case Pages.CAMERA:
                   return cameraFragment;
               case Pages.FRIENDS:
                   return friendsFragment;
               default:
                   return homeFragment;
           }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
    private static class Pages {
        public static final int HOME = 0;
        public static final int CAMERA = 1;
        public static final int FRIENDS = 2;
    }
    public void logout() {
        ParseUser.logOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public int getFriendFragmentId() {
        if(friendsFragment != null) {
            return friendsFragment.getId();
        }
        Log.d("MainActivity", "Trying to access the FriendFragment but it has not been created!");
        return 0;
    }
}
