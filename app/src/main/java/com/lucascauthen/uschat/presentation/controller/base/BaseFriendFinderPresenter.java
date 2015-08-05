package com.lucascauthen.uschat.presentation.controller.base;

import rx.Observable;
import rx.android.widget.OnTextChangeEvent;

/**
 * Created by lhc on 8/4/15.
 */
public interface BaseFriendFinderPresenter extends BasePresenter<BaseFriendFinderPresenter.BaseFriendFinderView>, BaseSwipeRefreshViewPresenter{

    interface BaseFriendFinderView {
        Observable<OnTextChangeEvent> bindPersonSearchObservable();
        void reSendSearch();
    }

}
