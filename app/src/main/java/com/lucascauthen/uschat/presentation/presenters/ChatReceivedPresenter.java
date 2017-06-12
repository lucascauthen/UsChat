package com.lucascauthen.uschat.presentation.presenters;

import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.data.repository.chat.ChatRepo;
import com.lucascauthen.uschat.util.executor.BackgroundExecutor;
import com.lucascauthen.uschat.util.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.view.base.ChatReceivedView;
import com.lucascauthen.uschat.presentation.view.base.ListView;
import com.lucascauthen.uschat.presentation.view.base.cards.ChatListItem;
import com.lucascauthen.uschat.util.NullObject;

public class ChatReceivedPresenter implements BasePresenter<ChatReceivedView>, ParentPresenter<ListView<Chat, Chat.ChatType, ChatListItem>> {

    private static final ChatReceivedView NULL_VIEW = NullObject.create(ChatReceivedView.class);
    private ChatReceivedView view = NULL_VIEW;


    private final BackgroundExecutor backgroundExecutor;
    private final ForegroundExecutor foregroundExecutor;
    private final ListPresenter<Chat, Chat.ChatType, ChatListItem> subPresenter;
    private final ChatRepo repo;

    public ChatReceivedPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, ListPresenter<Chat, Chat.ChatType, ChatListItem> subPresenter, ChatRepo repo) {
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
        this.subPresenter = subPresenter;
        this.repo = repo;
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
        ChatReceivedPresenter thisObject = this;
        view.setOnClickListener(new ChatListItem.OnClickListener() {
            @Override
            public void onClick(Chat itemData, ChatListItem itemView, ListPresenter<Chat, Chat.ChatType, ChatListItem> presenter) {
                if (!itemData.isImageLoaded() && !itemData.isLoadingImage()) {
                    itemData.setIsLoadingImage(true);
                    itemView.toggleLoading();
                    itemView.setMessage("Loading...");
                    backgroundExecutor.execute(() -> {
                        itemData.loadImage(new Chat.ImageReadyCallback() {
                            @Override
                            public void ready(byte[] image) {
                                foregroundExecutor.execute(() -> {
                                    itemData.setIsLoadingImage(false);
                                    itemData.setIsImageLoaded(true);
                                    itemData.setImage(image);
                                    itemView.setMessage("Tap to view chat!");
                                    itemView.toggleLoading();
                                });
                            }
                        }, new Chat.ProgressCallback() {
                            @Override
                            public void getProgress(int progress) {
                                foregroundExecutor.execute(() -> {
                                    itemView.setLoadingProgress(progress);
                                });
                            }
                        });
                    });

                } else if (itemData.isImageLoaded()) {
                    foregroundExecutor.execute(() -> {
                        thisObject.view.showChat(itemData);
                    });
                }
            }
        });
        view.setInitialStateSetter(new ChatListItem.InitialStateSetter() {
            @Override
            public void setState(Chat itemData, ChatListItem itemView) {
                itemView.setName(itemData.getFrom());
                itemView.setMessage("Tap to load!");
                itemView.setStateIcon(ChatListItem.SENT_ID);
            }
        });
        subPresenter.attachView(view);
        this.view.showLoading();
        subPresenter.requestUpdate(() -> {
            this.view.hideLoading();
        }, true);
    }

    public void onOpenChatComplete(Chat chat) {
        backgroundExecutor.execute(() -> {
            repo.openChat(chat, (msg) -> {
                subPresenter.requestUpdate(() -> {
                    //EMPTY
                }, false);
            }, false);
        });
    }
}