package com.lucascauthen.uschat.presentation.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.presenters.FriendSearchPresenter;
import com.lucascauthen.uschat.presentation.view.components.recyclerviews.PersonRecyclerView;
import com.lucascauthen.uschat.presentation.view.base.FriendSearchView;
import rx.Observable;
import rx.android.widget.OnTextChangeEvent;
import rx.android.widget.WidgetObservable;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

public class FriendSearchFragment extends Fragment implements FriendSearchView{
    private FriendSearchPresenter presenter;

    private LinearLayoutManager layoutManager;

    private Observable<OnTextChangeEvent> searchTextObservable;

    @InjectView(R.id.recyclerView)PersonRecyclerView recyclerView;
    @InjectView(R.id.swipeRefresh)SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.searchField)EditText searchField;

    public static FriendSearchFragment newInstance(FriendSearchPresenter presenter) {
        FriendSearchFragment f = new FriendSearchFragment();
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
        View v = inflater.inflate(R.layout.fragment_friend_search, null);
        ButterKnife.inject(this, v);
        recyclerView.setLayoutManager(layoutManager);
        presenter.attachView(this);
        presenter.attachSubView(recyclerView);
        searchTextObservable = WidgetObservable.text(searchField);
        searchTextObservable
                .map(value -> {
                    showLoading();
                    return value;
                })
                .debounce(1000, TimeUnit.MILLISECONDS, Schedulers.newThread())
                .map(event -> event.text().toString())
                .subscribe(query -> {
                    if (query.equals("")) {
                        presenter.sendQuery(null);
                    } else {
                        presenter.sendQuery(query);
                    }
                });
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
