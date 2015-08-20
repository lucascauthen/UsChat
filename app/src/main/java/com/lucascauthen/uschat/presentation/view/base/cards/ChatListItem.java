package com.lucascauthen.uschat.presentation.view.base.cards;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.presentation.presenters.ListPresenter;
import com.lucascauthen.uschat.presentation.view.base.ListView;

public interface ChatListItem {
    void toggleLoading();

    void setLoadingProgress(int progress);

    void setMessage(String msg);

    void setName(String msg);

    void setStateIcon(int resourceId);

    interface Presenter extends ListPresenter<Chat, Chat.ChatType, ChatListItem> {
        //This redefines the type to reduce boilerplate
    }

    interface OnClickListener extends ListView.OnClickListener<Chat, ChatListItem, ListPresenter<Chat, Chat.ChatType, ChatListItem>> {
        //This redefines the type to reduce boilerplate
    }

    interface InitialStateSetter extends ListView.InitialStateSetter<Chat, ChatListItem> {
        //This redefines the type to reduce boilerplate
    }
    int RECEIVED_ID = R.drawable.ic_action_file_download;
    int SENT_ID = R.drawable.ic_action_file_upload;
}
