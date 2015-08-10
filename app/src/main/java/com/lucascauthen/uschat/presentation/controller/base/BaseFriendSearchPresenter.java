package com.lucascauthen.uschat.presentation.controller.base;

import rx.Observable;
import rx.android.widget.OnTextChangeEvent;

/**
 * Created by lhc on 8/4/15.
 */
public interface BaseFriendSearchPresenter extends BasePresenter<BaseFriendSearchPresenter.BaseFriendSearchView>, BaseSwipeRefreshViewPresenter, BaseRecyclerViewPresenter<String, BasePersonListViewPresenter.BasePersonListAdapter> {

    interface BaseFriendSearchView extends BaseListView {
        Observable<OnTextChangeEvent> bindPersonSearchObservable();

        void reSendSearch();

    }

}
