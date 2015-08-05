package com.lucascauthen.uschat.presentation.controller.implmentations;

import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.controller.base.BaseFriendsListPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BasePersonListViewPresenter;
import com.lucascauthen.uschat.util.NullObject;

/**
 * Created by lhc on 8/5/15.
 */
public class FriendListPresenter implements BaseFriendsListPresenter {

    private static final BaseFriendListView NULL_VIEW = NullObject.create(BaseFriendListView.class);
    private BaseFriendListView view = NULL_VIEW;

    private final BackgroundExecutor backgroundExecutor;
    private final ForegroundExecutor foregroundExecutor;
    private final BasePersonListViewPresenter subPresenter;

    public FriendListPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, BasePersonListViewPresenter subPresenter) {
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
        this.subPresenter = subPresenter;
    }

    @Override
    public void attachView(BaseFriendListView view) {
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
    public void onSwipe() {

    }
}
