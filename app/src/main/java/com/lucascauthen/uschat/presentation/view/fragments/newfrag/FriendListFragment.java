package com.lucascauthen.uschat.presentation.view.fragments.newfrag;

import android.support.v4.app.Fragment;

import com.lucascauthen.uschat.presentation.controller.base.BaseFriendsListPresenter;
import com.lucascauthen.uschat.presentation.view.adapters.newadapters.PersonViewAdapter;

/**
 * Created by lhc on 8/4/15.
 */
public class FriendListFragment extends Fragment {
    private BaseFriendsListPresenter presenter;
    private PersonViewAdapter adapter;

    public static FriendListFragment newInstance(BaseFriendsListPresenter presenter, PersonViewAdapter adapter) {
        FriendListFragment f = new FriendListFragment();
        f.presenter = presenter;
        f.adapter = adapter;
        return f;
    }
}
