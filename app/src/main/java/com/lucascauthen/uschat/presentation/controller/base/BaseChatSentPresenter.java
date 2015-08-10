package com.lucascauthen.uschat.presentation.controller.base;

import com.lucascauthen.uschat.data.entities.Chat;

/**
 * Created by lhc on 8/5/15.
 */
public interface BaseChatSentPresenter extends BasePresenter<BaseChatSentPresenter.BaseSentChatView>, BaseRecyclerViewPresenter<Chat, BaseChatListViewPresenter.ChatListAdapter>, BaseSwipeRefreshViewPresenter{

    interface BaseSentChatView extends BaseListView{

    }
}
