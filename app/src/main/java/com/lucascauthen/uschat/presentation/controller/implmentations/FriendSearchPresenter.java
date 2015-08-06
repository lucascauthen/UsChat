package com.lucascauthen.uschat.presentation.controller.implmentations;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.data.repository.user.PersonRepo;
import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.domain.scheduler.BackgroundScheduler;
import com.lucascauthen.uschat.presentation.controller.base.BaseFriendSearchPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BasePersonListViewPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BaseRecyclerViewPresenter;
import com.lucascauthen.uschat.util.NullObject;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.widget.OnTextChangeEvent;
import rx.schedulers.Schedulers;

/**
 * Created by lhc on 8/4/15.
 */
public class FriendSearchPresenter implements BaseFriendSearchPresenter {

    private static final BaseFriendSearchView NULL_VIEW = NullObject.create(BaseFriendSearchView.class);
    private BaseFriendSearchView view = NULL_VIEW;

    private final BackgroundExecutor backgroundExecutor;
    private final ForegroundExecutor foregroundExecutor;
    private final BackgroundScheduler backgroundScheduler;
    private final BasePersonListViewPresenter subPresenter;

    private Observable<OnTextChangeEvent> searchTextObservable;

    public FriendSearchPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, BackgroundScheduler backgroundScheduler, BasePersonListViewPresenter subPresenter) {
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
        this.backgroundScheduler = backgroundScheduler;
        this.subPresenter = subPresenter;
        this.subPresenter.setDisplayType(PersonRepo.Type.SEARCH);
    }

    @Override
    public void attachView(BaseFriendSearchView view) {
        this.view = view;
        this.searchTextObservable = view.bindPersonSearchObservable();

        searchTextObservable
                .map(value -> {
                    foregroundExecutor.execute(() -> view.showLoading());
                    return value;
                })
                .debounce(1000, TimeUnit.MILLISECONDS, Schedulers.newThread())
                .map(event -> event.text().toString())
                .observeOn(backgroundScheduler.getScheduler())
                .subscribe(query -> {
                    if (query.equals("")) {
                        subPresenter.setQuery(null);
                    } else {
                        subPresenter.setQuery(query);
                    }
                    subPresenter.requestUpdate(() -> {
                        view.hideLoading();
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
        view.reSendSearch();
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
