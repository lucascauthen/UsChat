package com.lucascauthen.uschat.presentation.controller.base;

import com.lucascauthen.uschat.data.entities.User;

import java.util.List;

/**
 * Created by lhc on 7/30/15.
 */
public interface BasePersonListViewPresenter extends BaseRecyclerViewPresenter<User, BasePersonListViewPresenter.PersonListAdapter> {

    PersonListCardView.InitialStateSetter getInitialStateSetter();

    List<Integer> getIconPack();

    void attachAdapter(PersonListAdapter adapter, PersonListCardView.InitialStateSetter setter, List<Integer> iconPack);

    interface PersonListAdapter {
        void notifyDataUpdate();

        void attachPresenter(BasePersonListViewPresenter presenter);
    }

    interface PersonListCardView {

        void addActionButton(String key, OnClickActionListener listener);

        void removeActionButton(String key);

        void changeIconState(String key, int iconPackKey);

        interface OnClickActionListener {
            void onClick(String person, PersonListCardView view);
        }

        interface InitialStateSetter {
            void setState(String person, PersonListCardView view);
        }
    }
}
