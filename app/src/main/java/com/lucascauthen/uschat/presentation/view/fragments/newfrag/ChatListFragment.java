package com.lucascauthen.uschat.presentation.view.fragments.newfrag;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.presentation.controller.base.BaseChatListViewPresenter;
import com.lucascauthen.uschat.presentation.view.dialogs.ChatViewDialog;
import com.lucascauthen.uschat.util.ActivityNavigator;
import com.lucascauthen.uschat.presentation.view.adapters.newadapters.ChatViewAdapter;


import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lhc on 7/1/15.
 */
public class ChatListFragment extends Fragment implements BaseChatListViewPresenter.ChatListView {

    @InjectView(R.id.chatselect_logout) ImageButton logoutButton;
    @InjectView(R.id.chatselect_swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.chatselect_rv) RecyclerView rv;
    @InjectView(R.id.chatselect_progress) ProgressBar progress;


    private BaseChatListViewPresenter presenter;
    private ChatViewAdapter adapter;
    private ActivityNavigator navigator;

    private LinearLayoutManager layoutManager;

    public ChatListFragment() {
        //Required empty
    }

    public static ChatListFragment newInstance(BaseChatListViewPresenter presenter, ChatViewAdapter adapter, ActivityNavigator navigator) {
        ChatListFragment f = new ChatListFragment();
        f.presenter = presenter;
        f.adapter = adapter;
        f.navigator = navigator;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, null);
        ButterKnife.inject(this, v);

        presenter.attachView(this);
        presenter.attachAdapter(adapter);
        rv.setAdapter(adapter);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onLogoutClick();
                navigator.navigateToLogin(getActivity());
            }
        });

        rv.setLayoutManager(layoutManager);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onSwipe();
            }
        });

        return v;
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showChat(Chat chat) {
        ChatViewDialog chatView = new ChatViewDialog(getActivity(), chat.getImage(), 10000);
        chatView.show();
        chatView.setOnCloseListener((dialog) -> {
            presenter.onChatClose(chat);
        });
    }

}
