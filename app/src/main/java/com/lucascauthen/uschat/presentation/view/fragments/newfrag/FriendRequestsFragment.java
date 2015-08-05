package com.lucascauthen.uschat.presentation.view.fragments.newfrag;

import android.support.v4.app.Fragment;

import com.lucascauthen.uschat.presentation.controller.base.BaseFriendRequestPresenter;
import com.lucascauthen.uschat.presentation.view.adapters.newadapters.PersonViewAdapter;

/**
 * Created by lhc on 8/4/15.
 */
public class FriendRequestsFragment extends Fragment implements BaseFriendRequestPresenter.BaseRequestListView{
    private BaseFriendRequestPresenter presenter;
    private PersonViewAdapter adapter;

    public static FriendRequestsFragment newInstance(BaseFriendRequestPresenter presenter, PersonViewAdapter adapter) {
        FriendRequestsFragment f = new FriendRequestsFragment();
        f.presenter = presenter;
        f.adapter = adapter;
        return f;
    }

    @Override
    public void sendMessage(String msg) {

    }
}
