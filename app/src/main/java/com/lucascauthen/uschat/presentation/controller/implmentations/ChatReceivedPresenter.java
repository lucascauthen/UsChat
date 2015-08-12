package com.lucascauthen.uschat.presentation.controller.implmentations;

import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.data.repository.chat.ChatRepo;
import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.controller.base.BaseChatListViewPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BaseChatReceivedPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BaseRecyclerViewPresenter;
import com.lucascauthen.uschat.util.NullObject;

/**
 * Created by lhc on 8/5/15.
 */
public class ChatReceivedPresenter implements BaseChatReceivedPresenter {

    private final BackgroundExecutor backgroundExecutor;
    private final ForegroundExecutor foregroundExecutor;
    private final BaseChatListViewPresenter subPresenter;

    private static final BaseReceivedChatView NULL_VIEW = NullObject.create(BaseReceivedChatView.class);
    private BaseReceivedChatView view = NULL_VIEW;

    public ChatReceivedPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, BaseChatListViewPresenter subPresenter) {
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
        this.subPresenter = subPresenter;
    }

    @Override
    public void attachView(BaseReceivedChatView view) {
        this.view = view;
        this.subPresenter.attachView(view);
    }

    @Override
    public void detachView() {

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
        subPresenter.setDisplayType(ChatRepo.RequestType.RECEIVED);
        view.showLoading();
        subPresenter.requestUpdate(() -> {
            view.hideLoading();
        }, true);
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

    @Override
    public void onOpenChatComplete(Chat chat) {
        subPresenter.onChatClose(chat);
    }
}
