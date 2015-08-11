package com.lucascauthen.uschat.presentation.controller.base;

import com.lucascauthen.uschat.data.entities.Chat;

/**
 * Created by lhc on 8/5/15.
 */
public interface BaseChatReceivedPresenter extends BasePresenter<BaseChatReceivedPresenter.BaseReceivedChatView>, BaseRecyclerViewPresenter<Chat, BaseChatListViewPresenter.ChatListAdapter>, BaseSwipeRefreshViewPresenter {

    interface BaseReceivedChatView extends BaseListView, BaseChatListViewPresenter.ChatDisplayView {

    }
}
