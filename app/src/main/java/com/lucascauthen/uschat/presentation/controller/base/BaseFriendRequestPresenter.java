package com.lucascauthen.uschat.presentation.controller.base;

/**
 * Created by lhc on 8/4/15.
 */
public interface BaseFriendRequestPresenter extends BasePresenter<BaseFriendRequestPresenter.BaseRequestListView>, BaseSwipeRefreshViewPresenter, BaseRecyclerViewPresenter<String, BasePersonListViewPresenter.BasePersonListAdapter> {

    interface BaseRequestListView extends BaseListView{
    }
}
