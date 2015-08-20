package com.lucascauthen.uschat.presentation.view.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.presenters.FriendListPresenter;
import com.lucascauthen.uschat.presentation.presenters.FriendRequestPresenter;
import com.lucascauthen.uschat.presentation.view.base.FriendRequestView;
import com.lucascauthen.uschat.presentation.view.components.recyclerviews.PersonRecyclerView;

public class FriendRequestFragment extends Fragment implements FriendRequestView{
    private FriendRequestPresenter presenter;

    private LinearLayoutManager layoutManager;

    @InjectView(R.id.recyclerView)PersonRecyclerView recyclerView;
    @InjectView(R.id.swipeRefresh)SwipeRefreshLayout swipeRefreshLayout;

    public static FriendRequestFragment newInstance(FriendRequestPresenter presenter) {
        FriendRequestFragment f = new FriendRequestFragment();
        f.presenter = presenter;
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
        View v = inflater.inflate(R.layout.fragment_friend_list, null);
        ButterKnife.inject(this, v);
        recyclerView.setLayoutManager(layoutManager);
        presenter.attachView(this);
        presenter.attachSubView(recyclerView);
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
