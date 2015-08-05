package com.lucascauthen.uschat.presentation.controller.implmentations;

import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.controller.base.BaseFriendRequestPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BasePersonListViewPresenter;

/**
 * Created by lhc on 8/5/15.
 */
public class FriendRequestPresenter implements BaseFriendRequestPresenter {

    private final BackgroundExecutor backgroundExecutor;
    private final ForegroundExecutor foregroundExecutor;
    private final BasePersonListViewPresenter subPresenter;

    public FriendRequestPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, BasePersonListViewPresenter subPresenter) {
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
        this.subPresenter = subPresenter;
    }

    @Override
    public void attachView(BaseRequestListView view) {

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
    public void onSwipe() {

    }
}
