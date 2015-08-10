package com.lucascauthen.uschat.presentation.controller.base;

/**
 * Created by lhc on 8/4/15.
 */
public interface BaseFriendsListPresenter extends BasePresenter<BaseFriendsListPresenter.BaseFriendListView>, BaseSwipeRefreshViewPresenter, BaseRecyclerViewPresenter<String, BasePersonListViewPresenter.BasePersonListAdapter>{

    interface BaseFriendListView extends BaseListView {
    }
}
