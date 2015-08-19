package com.lucascauthen.uschat.presentation.controller.base;

import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.data.repository.chat.ChatRepo;

/**
 * Created by lhc on 7/30/15.
 */

public interface BaseChatListViewPresenter extends BasePresenter<BaseChatListViewPresenter.ChatDisplayView>, BaseRecyclerViewPresenter<Chat, BaseChatListViewPresenter.ChatListAdapter>,
        BasePagerViewPresenter.PagerSubView {

    ChatListCardView.OnClickChatListener getOnClickChatListener();

    void onChatClose(Chat chat);

    void setDisplayType(Chat.ChatType type);

    void requestUpdate(BaseChatListViewPresenter.OnDoneLoadingCallback callback, boolean repoNeedUpdate);

    interface OnDoneLoadingCallback {
        void done();
    }

    interface ChatDisplayView extends BaseListView {
        void showChat(Chat chat);
    }

    interface ChatListAdapter {
        void notifyDataUpdate(BaseChatListViewPresenter.OnDoneLoadingCallback callback);

        void attachPresenter(BaseChatListViewPresenter presenter, ChatListCardView.OnClickChatListener clickChatListener);
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
