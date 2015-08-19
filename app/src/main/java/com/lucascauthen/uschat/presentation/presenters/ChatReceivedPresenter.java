package com.lucascauthen.uschat.presentation.presenters;

import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.view.views.ChatReceivedView;
import com.lucascauthen.uschat.presentation.view.views.ListView;
import com.lucascauthen.uschat.presentation.view.views.cards.ChatListItem;
import com.lucascauthen.uschat.util.NullObject;

public class ChatReceivedPresenter implements BasePresenter<ChatReceivedView>, ParentPresenter<ListView<Chat, Chat.ChatType, ChatListItem>> {

    private static final ChatReceivedView NULL_VIEW = NullObject.create(ChatReceivedView.class);
    private ChatReceivedView view = NULL_VIEW;


    private final BackgroundExecutor backgroundExecutor;
    private final ForegroundExecutor foregroundExecutor;
    private final ListPresenter<Chat, Chat.ChatType, ChatListItem> subPresenter;

    public ChatReceivedPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, ListPresenter<Chat, Chat.ChatType, ChatListItem> subPresenter) {
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
        this.subPresenter = subPresenter;
        subPresenter.setDisplayType(Chat.ChatType.RECEIVED);
    }

    @Override
    public void attachView(ChatReceivedView view) {
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

            }
        });
        view.setInitialStateSetter(new ChatListItem.InitialStateSetter() {
            @Override
            public void setState(Chat itemData, ChatListItem itemView) {

            }
        });
        subPresenter.attachView(view);
    }
}