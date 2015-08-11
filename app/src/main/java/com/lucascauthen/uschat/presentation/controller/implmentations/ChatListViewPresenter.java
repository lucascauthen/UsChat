package com.lucascauthen.uschat.presentation.controller.implmentations;

import android.graphics.Bitmap;

import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.data.repository.chat.ChatRepo;
import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.controller.base.BaseChatListViewPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BasePagerViewPresenter;
import com.lucascauthen.uschat.util.NullObject;

import java.util.List;

/**
 * Created by lhc on 7/30/15.
 */
public class ChatListViewPresenter implements BaseChatListViewPresenter {
    private static final ChatListAdapter NULL_ADAPTER = NullObject.create(ChatListAdapter.class);
    private static final ChatDisplayView NULL_VIEW = NullObject.create(ChatDisplayView.class);
    private static final BasePagerViewPresenter.PagerViewChanger NULL_PAGER_CHANGER = NullObject.create(BasePagerViewPresenter.PagerViewChanger.class);

    private ChatListAdapter adapter = NULL_ADAPTER;
    private ChatDisplayView view = NULL_VIEW;
    private BasePagerViewPresenter.PagerViewChanger changer = NULL_PAGER_CHANGER;

    private final BackgroundExecutor backgroundExecutor;
    private final ForegroundExecutor foregroundExecutor;
    private final ChatRepo repository;
    private boolean repoNeedUpdate = true;

    private ChatRepo.RequestType displayType;

    public ChatListViewPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, ChatRepo repository) {
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
        this.repository = repository;
    }

    @Override
    public Chat getItem(int index) {
        if(repoNeedUpdate) {
            repoNeedUpdate = false;
            return repository.get(new ChatRepo.Request(true, displayType)).result().get(index);
        } else {
            List<Chat> result = repository.get(new ChatRepo.Request(false, displayType)).result();
            return result.get(index);
        }
    }

    @Override
    public void getItemInBackground(int index, OnGetItemCallback<Chat> callback) {
        backgroundExecutor.execute(() -> {
            Chat item = getItem(index);
            foregroundExecutor.execute(() -> {
                callback.onGetItem(item);
            });
        });
    }

    @Override
    public int getSize() {
        if(repoNeedUpdate) {
            repoNeedUpdate = false;
            return repository.get(new ChatRepo.Request(true, displayType)).result().size();
        } else {
            return repository.get(new ChatRepo.Request(false, displayType)).result().size();
        }
    }

    @Override
    public void getSizeInBackground(OnGetSizeCallback callback) {
        backgroundExecutor.execute(() -> {
            int size = getSize();
            foregroundExecutor.execute(() -> {
                callback.onGetSize(size);
            });
        });
    }

    @Override
    public void attachAdapter(ChatListAdapter adapter) {
        this.adapter = adapter;
        adapter.attachPresenter(this, getOnClickChatListener());
    }

    @Override
    public void detachAdapter() {
        this.adapter = NULL_ADAPTER;
    }

    @Override
    public void attachView(ChatDisplayView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = NULL_VIEW;
    }

    @Override
    public void onPause() {
        //Empty
    }

    @Override
    public void onResume() {
        //Empty
    }

    @Override
    public ChatListCardView.OnClickChatListener getOnClickChatListener() {
        return (chat, cardView) -> {
            if (!chat.isFromCurrentUser()) {
                if (!chat.isImageLoaded() && !chat.isLoadingImage()) {
                    chat.setIsLoadingImage(true);
                    cardView.toggleLoading();
                    cardView.setMessage("Loading...");
                    backgroundExecutor.execute(() -> {
                        chat.loadImage(new Chat.ImageReadyCallback() {
                            @Override
                            public void ready(Bitmap image) {
                                foregroundExecutor.execute(() -> {
                                    chat.setIsLoadingImage(false);
                                    chat.setIsImageLoaded(true);
                                    chat.setImage(image);
                                    cardView.setMessage("Tap to view chat!");
                                    cardView.toggleLoading();
                                });
                            }
                        }, new Chat.ProgressCallback() {
                            @Override
                            public void getProgress(int progress) {
                                foregroundExecutor.execute(() -> {
                                    cardView.setLoadingProgress(progress);
                                });
                            }
                        });
                    });

                } else if (chat.isImageLoaded()) {
                    foregroundExecutor.execute(() -> {
                        view.showChat(chat);
                    });
                }
            }
        };
    }

    @Override
    public void onChatClose(Chat chat) {
        backgroundExecutor.execute(() -> {
            repository.openChat(chat, (msg) -> {
                requestUpdate(() -> {
                    //Empty
                }, false);
            });
        });
    }

    @Override
    public void setDisplayType(ChatRepo.RequestType type) {
        this.displayType = type;
    }

    @Override
    public void requestUpdate(OnDoneLoadingCallback callback, boolean repoNeedUpdate) {
        this.repoNeedUpdate = repoNeedUpdate;
        adapter.notifyDataUpdate(callback);
    }

    /*
    @Override
    public void onLogoutClick() {
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //Empty
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
    */

    @Override
    public void attachPageChanger(BasePagerViewPresenter.PagerViewChanger changer) {
        this.changer = changer;
    }
}
