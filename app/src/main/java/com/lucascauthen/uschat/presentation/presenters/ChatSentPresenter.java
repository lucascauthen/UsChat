package com.lucascauthen.uschat.presentation.presenters;

import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.view.base.ChatSentView;
import com.lucascauthen.uschat.presentation.view.base.ListView;
import com.lucascauthen.uschat.presentation.view.base.cards.ChatListItem;
import com.lucascauthen.uschat.util.NullObject;

public class ChatSentPresenter implements BasePresenter<ChatSentView>, ParentPresenter<ListView<Chat, Chat.ChatType, ChatListItem>> {

    private static final ChatSentView NULL_VIEW = NullObject.create(ChatSentView.class);
    private ChatSentView view = NULL_VIEW;


    private final BackgroundExecutor backgroundExecutor;
    private final ForegroundExecutor foregroundExecutor;
    private final ListPresenter<Chat, Chat.ChatType, ChatListItem> subPresenter;

    public ChatSentPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, ListPresenter<Chat, Chat.ChatType, ChatListItem> subPresenter) {
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
        this.subPresenter = subPresenter;
        subPresenter.setDisplayType(Chat.ChatType.SENT);
    }

    @Override
    public void attachView(ChatSentView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = NULL_VIEW;
    }

    public void onSwipe() {
        subPresenter.requestUpdate(() -> {
            view.hideLoading();
        }, true);
    }

    @Override
    public void attachSubView(ListView<Chat, Chat.ChatType, ChatListItem> view) {
        view.setOnClickListener(new ChatListItem.OnClickListener() {
            @Override
            public void onClick(Chat itemData, ChatListItem itemView, ListPresenter<Chat, Chat.ChatType, ChatListItem> presenter) {
                //EMPTY
            }
        });
        view.setInitialStateSetter(new ChatListItem.InitialStateSetter() {
            @Override
            public void setState(Chat itemData, ChatListItem itemView) {
                String name = "";
                for (String person : itemData.getToString()) {
                    name += person + ", ";
                }
                if (name.endsWith(", ")) {
                    name = name.substring(0, name.length() - 2);
                }
                itemView.setName(name);
                itemView.setMessage("Message Sent!");
                itemView.setStateIcon(ChatListItem.SENT_ID);
            }
        });
        subPresenter.attachView(view);
    }
}