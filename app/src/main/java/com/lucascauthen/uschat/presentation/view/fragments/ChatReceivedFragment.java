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
import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.presentation.controller.base.BaseChatListViewPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BaseChatReceivedPresenter;
import com.lucascauthen.uschat.presentation.view.adapters.ChatViewAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lhc on 8/5/15.
 */
public class ChatReceivedFragment extends Fragment implements BaseChatReceivedPresenter.BaseReceivedChatView, BaseChatListViewPresenter.ChatListView {

    @InjectView(R.id.chat_received_list_swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.chat_received_list_rv) RecyclerView recyclerView;

    private BaseChatReceivedPresenter presenter;
    private ChatViewAdapter adapter;
    private LinearLayoutManager layoutManager;

    public static ChatReceivedFragment newInstance(BaseChatReceivedPresenter presenter, ChatViewAdapter adapter) {
        ChatReceivedFragment f = new ChatReceivedFragment();
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
        View v = inflater.inflate(R.layout.fragment_chat_received, null);
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

    @Override
    public void showChat(Chat chat) {

    }
}
