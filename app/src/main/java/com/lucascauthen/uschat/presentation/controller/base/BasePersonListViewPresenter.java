package com.lucascauthen.uschat.presentation.controller.base;

import android.support.annotation.Nullable;

import com.lucascauthen.uschat.data.entities.User;
import com.lucascauthen.uschat.data.repository.user.PersonRepo;

/**
 * Created by lhc on 7/30/15.
 */
public interface BasePersonListViewPresenter extends BaseRecyclerViewPresenter<String, BasePersonListViewPresenter.BasePersonListAdapter> {

    PersonListCardView.InitialStateSetter getInitialStateSetter();

    void attachAdapter(BasePersonListAdapter adapter, PersonListCardView.InitialStateSetter setter);

    void setDisplayType(PersonRepo.Type type);

    void requestUpdate(BasePersonListAdapter.OnDoneLoadingCallback callback, boolean repoNeedUpdate);

    void updateOnNextRequest();

    void setQuery(@Nullable String query);

    interface BasePersonListAdapter {
        void notifyDataUpdate(OnDoneLoadingCallback callback);

        void attachPresenter(BasePersonListViewPresenter presenter);
        interface OnDoneLoadingCallback {
            void done();
        }
    }

    interface PersonListCardView {

        void addActionButton(String key, int iconId, OnClickActionListener listener);

        void removeActionButton(String key);

        void changeIconState(String key, int iconId);

        void setStateIcon(int iconId);

        void showLoading();

        void hideLoading();

        interface OnClickActionListener {
            void onClick(String person, PersonListCardView view);
        }

        interface InitialStateSetter {
            void setState(String person, PersonListCardView view);
        }
    }
}
