package com.lucascauthen.uschat.presentation.controller.base;

/**
 * Created by lhc on 8/6/15.
 */
public interface BaseSelectFriendsDialogPresenter extends BasePresenter<BaseSelectFriendsDialogPresenter.BaseSelectFriendsDialog>, BaseRecyclerViewPresenter<String, BasePersonListViewPresenter.BasePersonListAdapter> {

    void onSend();

    void updateRequested();

    interface BaseSelectFriendsDialog {
        void enableSend();

        void disableSend();

        void showLoading();

        void hideLoading();

    }
}
