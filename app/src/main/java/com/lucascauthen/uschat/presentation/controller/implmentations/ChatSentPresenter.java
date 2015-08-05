package com.lucascauthen.uschat.presentation.controller.implmentations;

import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.controller.base.BaseChatListViewPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BaseChatSentPresenter;
import com.lucascauthen.uschat.util.NullObject;

/**
 * Created by lhc on 8/5/15.
 */
public class ChatSentPresenter  implements BaseChatSentPresenter{

    private static final BaseChatSentView NULL_VIEW = NullObject.create(BaseChatSentView.class);
    private BaseChatSentView view = NULL_VIEW;

    private final BackgroundExecutor backgroundExecutor;
    private final ForegroundExecutor foregroundExecutor;
    private final BaseChatListViewPresenter subPresenter;

    public ChatSentPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, BaseChatListViewPresenter subPresenter) {
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
        this.subPresenter = subPresenter;
    }

    @Override
    public void attachView(BaseChatSentView view) {
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
}
