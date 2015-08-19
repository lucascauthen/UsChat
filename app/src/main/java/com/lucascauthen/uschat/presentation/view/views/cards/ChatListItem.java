package com.lucascauthen.uschat.presentation.view.views.cards;

import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.presentation.presenters.ListPresenter;
import com.lucascauthen.uschat.presentation.view.views.ListView;

public interface ChatListItem {
    void toggleLoading();

    void setLoadingProgress(int progress);

    void setMessage(String msg);

    interface Presenter extends ListPresenter<Chat, Chat.ChatType, ChatListItem> {
        //This redefines the type to reduce boilerplate
    }

    interface OnClickListener extends ListView.OnClickListener<Chat, ChatListItem, ListPresenter<Chat, Chat.ChatType, ChatListItem>> {
        //This redefines the type to reduce boilerplate
    }

    interface InitialStateSetter extends ListView.InitialStateSetter<Chat, ChatListItem> {
        //This redefines the type to reduce boilerplate
    }
}
