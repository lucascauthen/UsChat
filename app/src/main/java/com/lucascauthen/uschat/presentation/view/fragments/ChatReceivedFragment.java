package com.lucascauthen.uschat.presentation.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.presentation.presenters.ChatReceivedPresenter;
import com.lucascauthen.uschat.presentation.view.components.recyclerviews.ChatRecyclerView;
import com.lucascauthen.uschat.presentation.view.dialogs.ChatViewDialog;
import com.lucascauthen.uschat.presentation.view.base.ChatReceivedView;

public class ChatReceivedFragment extends Fragment implements ChatReceivedView {

    @InjectView(R.id.swipeRefresh) SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.recyclerView) ChatRecyclerView recyclerView;

    private ChatReceivedPresenter presenter;

    private LinearLayoutManager layoutManager;


    public static ChatReceivedFragment newInstance(ChatReceivedPresenter presenter) {
        ChatReceivedFragment f = new ChatReceivedFragment();
        f.presenter = presenter;
        f.recyclerView = new ChatRecyclerView(f.getActivity());
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
        View v = inflater.inflate(R.layout.fragment_chat_sent, null);
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

    @Override
    public void showChat(Chat chat) {
        ChatViewDialog dialog = new ChatViewDialog(getActivity(), chat.getImage(), 10000);
        dialog.setOnCloseListener((theDialog) -> {
            presenter.onOpenChatComplete(chat);
        });
        dialog.show();
    }
}
