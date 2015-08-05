package com.lucascauthen.uschat.presentation.view.fragments.newfrag;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.presentation.controller.base.BaseChatTabViewPresenter;
import com.lucascauthen.uschat.presentation.view.components.NestedViewPager;
import com.lucascauthen.uschat.presentation.view.dialogs.ChatViewDialog;
import com.lucascauthen.uschat.util.ActivityNavigator;


import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lhc on 7/1/15.
 */
public class ChatTabFragment extends Fragment implements BaseChatTabViewPresenter.BaseChatTabView {

    @InjectView(R.id.tab_viewpager)NestedViewPager viewPager;
    @InjectView(R.id.tab_view_tabs)TabLayout tabLayout;

    private final int[] icons = {
            R.drawable.ic_sent,
            R.drawable.ic_received
    };
    private final String[] tabTitles = {
            "Sent",
            "Received"
    };


    private BaseChatTabViewPresenter presenter;
    private ActivityNavigator navigator;
    private final List<Fragment> fragments = new ArrayList<>();


    public ChatTabFragment() {
        //Required empty
    }

    public static ChatTabFragment newInstance(BaseChatTabViewPresenter presenter, Fragment sentChats, Fragment receivedChats, ActivityNavigator navigator) {
        ChatTabFragment f = new ChatTabFragment();
        f.presenter = presenter;
        f.navigator = navigator;
        f.fragments.add(sentChats);
        f.fragments.add(receivedChats);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat_tab_view, null);
        ButterKnife.inject(this, v);
        viewPager.setAdapter(new TabbedPageAdapter(getChildFragmentManager(), getActivity(), fragments));
        tabLayout.setupWithViewPager(viewPager);
        for(int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            View tabView = inflater.inflate(R.layout.tab_chat, null);
            ImageView icon = (ImageView)tabView.findViewById(R.id.tab_icon);
            TextView title = (TextView) tabView.findViewById(R.id.tab_title);

            if(i <= icons.length - 1 && i <= tabTitles.length - 1) {
                icon.setImageResource(icons[i]);
                title.setText(tabTitles[i]);
            } else {
                icon.setImageResource(R.drawable.ic_action_error);
                title.setText("Error");
            }
            tab.setCustomView(tabView);

        }
        presenter.attachView(this);
        return v;
    }
    private class TabbedPageAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragments;
        private final Context context;

        public TabbedPageAdapter(FragmentManager fm, Context context, @NonNull List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
