package com.lucascauthen.uschat.presentation.view.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.view.components.NestedViewPager;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lhc on 7/1/15.
 */
public class FriendTabFragment extends Fragment {

    private final List<Fragment> fragments = new ArrayList<>();
    @InjectView(R.id.viewPager)NestedViewPager viewPager;
    @InjectView(R.id.tabs)TabLayout tabLayout;

    public FriendTabFragment() {
        //Required empty
    }

    public static FriendTabFragment newInstance(Fragment friendList, Fragment requestList, Fragment personSearch) {
        FriendTabFragment f = new FriendTabFragment();
        f.fragments.add(friendList);
        f.fragments.add(requestList);
        f.fragments.add(personSearch);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friend_tab_view, null);
        ButterKnife.inject(this, v);
        //presenter.attachView(this); TODO might take out (i.e. it might not need a presenter)
        viewPager.setAdapter(new TabbedPageAdapter(getChildFragmentManager(), getActivity(), fragments));
        tabLayout.setupWithViewPager(viewPager);

        return v;
    }

    private class TabbedPageAdapter extends FragmentPagerAdapter{
        private final int[] icons = {
            R.drawable.ic_friends,
            R.drawable.ic_friend_requests,
            R.drawable.ic_friend_search
        };
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
        @Override
        public CharSequence getPageTitle(int position) {
            Drawable image = null;
            if(position <= icons.length - 1) {
                 image = context.getResources().getDrawable(icons[position]);
            } else {
                image = context.getResources().getDrawable(R.drawable.ic_action_error);
            }
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            SpannableString sb = new SpannableString(" ");
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        }
    }
}
