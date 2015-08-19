package com.lucascauthen.uschat.presentation.view.views.cards;

import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.presentation.presenters.ListPresenter;
import com.lucascauthen.uschat.presentation.view.views.ListView;

public interface PersonListItem {
    void addActionButton(String key, int iconId, OnClickActionListener listener);

    void removeActionButton(String key);

    void resetActionButtons();

    void changeIconState(String key, int iconId);

    void setStateIcon(int iconId);

    void showLoading();

    void hideLoading();

    interface OnClickActionListener {
        void onClick(Person person, PersonListItem item, ListPresenter presenter);
    }


    interface Presenter extends ListPresenter<Person, Person.PersonType, PersonListItem> {
        //This redefines the type to reduce boilerplate
    }

    interface OnClickListener extends ListView.OnClickListener<Person, PersonListItem, ListPresenter<Person, Person.PersonType, PersonListItem>> {
        //This redefines the type to reduce boilerplate
    }

    interface InitialStateSetter extends ListView.InitialStateSetter<Person, PersonListItem> {
        //This redefines the type to reduce boilerplate
    }
}

