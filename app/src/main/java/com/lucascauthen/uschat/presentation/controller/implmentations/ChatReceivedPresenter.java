package com.lucascauthen.uschat.presentation.controller.implmentations;

import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.controller.base.BaseChatListViewPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BaseChatReceivedPresenter;

/**
 * Created by lhc on 8/5/15.
 */
public class ChatReceivedPresenter implements BaseChatReceivedPresenter {

    private final BackgroundExecutor backgroundExecutor;
    private final ForegroundExecutor foregroundExecutor;
    private final BaseChatListViewPresenter subPresenter;

    public ChatReceivedPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, BaseChatListViewPresenter subPresenter) {
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
        this.subPresenter = subPresenter;
    }

    @Override
    public void attachView(BaseReceivedChatView view) {

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
}
