package com.lucascauthen.uschat.presentation.controller.base;

import com.lucascauthen.uschat.data.entities.Chat;

/**
 * Created by lhc on 7/30/15.
 */

public interface BaseChatListViewPresenter extends BasePresenter<BaseChatListViewPresenter.ChatListView>, BaseRecyclerViewPresenter<Chat, BaseChatListViewPresenter.ChatListAdapter>,
        BaseSwipeRefreshViewPresenter, BasePagerViewPresenter.PagerSubView {

    ChatListCardView.OnClickChatListener getOnClickChatListener();

    void onChatClose(Chat chat);

    void onLogoutClick(); //Might get rid of this/Move it somewhere else

    interface ChatListView extends BaseListView {
        void showChat(Chat chat);
    }

    interface ChatListAdapter {
        void notifyDataUpdate();
    }

    interface ChatListCardView {
        void toggleLoading();

        void setLoadingProgress(int progress);

        void setMessage(String msg);

        interface OnClickChatListener {
            void onClick(Chat chat, ChatListCardView view);
        }
    }
}
