package com.lucascauthen.uschat.presentation.view.fragments.newfrag;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.controller.base.BaseChatReceivedPresenter;
import com.lucascauthen.uschat.presentation.view.adapters.newadapters.ChatViewAdapter;

import butterknife.InjectView;

/**
 * Created by lhc on 8/5/15.
 */
public class ChatReceivedFragment extends Fragment implements BaseChatReceivedPresenter.BaseReceivedChatView{
    /*
    @InjectView(R.id.chatselect_swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.chatselect_rv) RecyclerView rv;
    */
    private BaseChatReceivedPresenter presenter;
    private ChatViewAdapter adapter;

    public static ChatReceivedFragment newInstance(BaseChatReceivedPresenter presenter, ChatViewAdapter adapter) {
        ChatReceivedFragment f = new ChatReceivedFragment();
        f.presenter = presenter;
        f.adapter = adapter;
        return f;
    }
}
