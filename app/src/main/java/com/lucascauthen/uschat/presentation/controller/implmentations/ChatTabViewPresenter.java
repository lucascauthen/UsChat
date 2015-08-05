package com.lucascauthen.uschat.presentation.controller.implmentations;

import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.controller.base.BaseChatListViewPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BaseChatTabViewPresenter;

/**
 * Created by lhc on 8/5/15.
 */
public class ChatTabViewPresenter implements BaseChatTabViewPresenter {

    private final BackgroundExecutor backgroundExecutor;
    private final ForegroundExecutor foregroundExecutor;


    public ChatTabViewPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor) {
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
    }

    @Override
    public void attachView(BaseChatTabView view) {

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
