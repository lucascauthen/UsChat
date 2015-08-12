package com.lucascauthen.uschat.presentation.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.controller.base.BaseFriendRequestPresenter;
import com.lucascauthen.uschat.presentation.view.components.PersonViewAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lhc on 8/4/15.
 */
public class FriendRequestsFragment extends Fragment implements BaseFriendRequestPresenter.BaseRequestListView{
    private BaseFriendRequestPresenter presenter;
    private PersonViewAdapter adapter;

    private LinearLayoutManager layoutManager;

    @InjectView(R.id.friend_requests_rv)RecyclerView recyclerView;
    @InjectView(R.id.friend_requests_swipe_refresh)SwipeRefreshLayout swipeRefreshLayout;

    public static FriendRequestsFragment newInstance(BaseFriendRequestPresenter presenter, PersonViewAdapter adapter) {
        FriendRequestsFragment f = new FriendRequestsFragment();
        f.presenter = presenter;
        f.adapter = adapter;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friend_requests, null);
        ButterKnife.inject(this, v);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        presenter.attachAdapter(adapter);
        presenter.attachView(this);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.onSwipe();
        });
        return v;
    }

    @Override
    public void showLoading() {
        this.swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        this.swipeRefreshLayout.setRefreshing(false);
    }
}
