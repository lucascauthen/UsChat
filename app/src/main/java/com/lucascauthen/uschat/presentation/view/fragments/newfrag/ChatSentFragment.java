package com.lucascauthen.uschat.presentation.view.fragments.newfrag;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.controller.base.BaseChatSentPresenter;
import com.lucascauthen.uschat.presentation.view.adapters.newadapters.ChatViewAdapter;

import butterknife.InjectView;

/**
 * Created by lhc on 8/5/15.
 */
public class ChatSentFragment extends Fragment implements BaseChatSentPresenter.BaseChatSentView{
    /*
    @InjectView(R.id.chatselect_swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.chatselect_rv) RecyclerView rv;
    */
    private BaseChatSentPresenter presenter;
    private ChatViewAdapter adapter;

    public static ChatSentFragment newInstance(BaseChatSentPresenter presenter, ChatViewAdapter adapter) {
        ChatSentFragment f = new ChatSentFragment();
        f.presenter = presenter;
        f.adapter = adapter;
        return f;
    }
}
