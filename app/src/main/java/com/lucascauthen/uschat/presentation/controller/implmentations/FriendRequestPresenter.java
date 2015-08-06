package com.lucascauthen.uschat.presentation.controller.implmentations;

import com.lucascauthen.uschat.data.repository.user.PersonRepo;
import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.controller.base.BaseFriendRequestPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BasePersonListViewPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BaseRecyclerViewPresenter;
import com.lucascauthen.uschat.util.NullObject;

/**
 * Created by lhc on 8/5/15.
 */
public class FriendRequestPresenter implements BaseFriendRequestPresenter {

    BaseFriendRequestPresenter.BaseRequestListView NULL_VIEW = NullObject.create(BaseFriendRequestPresenter.BaseRequestListView.class);
    BaseFriendRequestPresenter.BaseRequestListView view = NULL_VIEW;

    private final BackgroundExecutor backgroundExecutor;
    private final ForegroundExecutor foregroundExecutor;
    private final BasePersonListViewPresenter subPresenter;

    public FriendRequestPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, BasePersonListViewPresenter subPresenter) {
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
        this.subPresenter = subPresenter;
        subPresenter.setDisplayType(PersonRepo.Type.REQUESTS);
    }

    @Override
    public void attachView(BaseRequestListView view) {
        this.view = view;
        view.showLoading();
        backgroundExecutor.execute(() -> {
            subPresenter.requestUpdate(() -> {
                foregroundExecutor.execute(() -> {
                    view.hideLoading();
                });
            }, true);
        });
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
        subPresenter.requestUpdate(() -> {
            view.hideLoading();
        }, true);
    }

    @Override
    public String getItem(int index) {
        throw new RuntimeException("Not Applicable.");
    }

    @Override
    public void getItemInBackground(int index, OnGetItemCallback<String> callback) {
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
    public void attachAdapter(BasePersonListViewPresenter.BasePersonListAdapter adapter) {
        this.subPresenter.attachAdapter(adapter, BasePersonListViewPresenter.BasePersonListBehavior.defaultSetter());
    }

    @Override
    public void detachAdapter() {
        this.subPresenter.detachAdapter();
    }

}
