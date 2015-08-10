package com.lucascauthen.uschat.presentation.controller.implmentations;

import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.controller.base.BaseChatListViewPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BaseChatSentPresenter;
import com.lucascauthen.uschat.util.NullObject;

/**
 * Created by lhc on 8/5/15.
 */
public class ChatSentPresenter  implements BaseChatSentPresenter{

    private static final BaseSentChatView NULL_VIEW = NullObject.create(BaseSentChatView.class);
    private BaseSentChatView view = NULL_VIEW;

    private final BackgroundExecutor backgroundExecutor;
    private final ForegroundExecutor foregroundExecutor;
    private final BaseChatListViewPresenter subPresenter;

    public ChatSentPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, BaseChatListViewPresenter subPresenter) {
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
        this.subPresenter = subPresenter;
    }

    @Override
    public void attachView(BaseSentChatView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = NULL_VIEW;
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public Chat getItem(int index) {
        throw new RuntimeException("Not Applicable.");
    }

    @Override
    public void getItemInBackground(int index, OnGetItemCallback<Chat> callback) {
        throw new RuntimeException("Not Applicable.");
    }

    @Override
    public int getSize() {
        throw new RuntimeException("Not Applicable.");
    }

    @Override
    public void getSizeInBackground(OnGetSizeCallback callback) {
        throw new RuntimeException("Not Applicable.");
    }

    @Override
    public void attachAdapter(BaseChatListViewPresenter.ChatListAdapter adapter) {
        this.subPresenter.attachAdapter(adapter);
    }

    @Override
    public void detachAdapter() {
        this.subPresenter.detachAdapter();
    }

    @Override
    public void onSwipe() {
        subPresenter.requestUpdate(() -> {
            view.hideLoading();
        }, true);
    }
}
