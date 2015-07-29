package com.lucascauthen.uschat.presentation.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.controller.FriendsPresenter;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.widget.WidgetObservable;
import rx.schedulers.Schedulers;
import rx.android.widget.OnTextChangeEvent;

/**
 * Created by lhc on 7/1/15.
 */
public class FriendsFragment extends Fragment implements FriendsPresenter.FriendsCrudView{

    private FriendsPresenter presenter;
    private LinearLayoutManager layoutManager;

    @InjectView(R.id.friend_rv)RecyclerView recyclerView;
    @InjectView(R.id.friend_search_button)ImageButton searchButton;
    @InjectView(R.id.friend_swipe_refresh)SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.friend_loading)ProgressBar loading;
    @InjectView(R.id.friends_list_title_text_view)TextView title;
    @InjectView(R.id.friend_search_edit_text)EditText searchField;

    InputMethodManager imm;

    private boolean showingFriends = true;


    public FriendsFragment() {
        //Required empty
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Executor backgroundExecutor = Executors.newFixedThreadPool(10);

        final Handler handler = new Handler();
        Executor foregroundExecutor = new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
        presenter = new FriendsPresenter(Schedulers.io(), AndroidSchedulers.mainThread(), backgroundExecutor, foregroundExecutor);

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friends, null);
        ButterKnife.inject(this, v);
        presenter.attachView(this);
        recyclerView.setLayoutManager(layoutManager);
        searchField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    imm.showSoftInput(searchField, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (!showingFriends) {
                presenter.onPersonSwipeRefresh(searchField.getText().toString());

            } else {
                presenter.onFriendsSwipeRefresh();
            }
        });
        this.recyclerView.setAdapter(presenter.getFriendAdapter());
        presenter.onFriendsSwipeRefresh();
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static FriendsFragment newInstance() {
        return new FriendsFragment();
    }

    @Override
    public Observable<OnTextChangeEvent> bindSearchField() {
        return WidgetObservable.text(searchField);
    }

    @Override
    public void enableSearch() {
        searchButton.setEnabled(true);
    }

    @Override
    public void disableSearch() {
        searchButton.setEnabled(false);
    }

    @Override
    public void showSearchLoading() {
        //loading.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideSearchLoading() {
        //loading.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showRefreshLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideRefreshLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void refreshSearch() {
        this.searchField.setText(this.searchField.getText().toString());
    }

    @OnClick(R.id.friend_search_button)
    void onSearchClick() {
        toggleSearch();
    }

    public void toggleSearch() {
        if(searchField.getVisibility() == View.VISIBLE) { //Stop searching for friends
            searchField.setVisibility(View.GONE);
            title.setVisibility(View.VISIBLE);
            searchField.getRootView().clearFocus();
            searchField.setText("");
            imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
            showingFriends = true;
            this.recyclerView.setAdapter(presenter.getFriendAdapter());
            presenter.onFriendsSwipeRefresh();
        } else { //Start searching for friends
            searchField.setVisibility(View.VISIBLE);
            title.setVisibility(View.GONE);
            imm.showSoftInput(searchField, InputMethodManager.SHOW_IMPLICIT);
            searchField.setEnabled(true);
            searchField.requestFocus();
            showingFriends = false;
            this.recyclerView.setAdapter(presenter.getPersonAdapter());
        }
    }

}
