package com.lucascauthen.uschat.presentation.view.components.recyclerviews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.presentation.presenters.ListPresenter;
import com.lucascauthen.uschat.presentation.view.base.ListView;
import com.lucascauthen.uschat.presentation.view.base.cards.PersonListItem;

public class PersonRecyclerView extends RecyclerView implements ListView<Person, Person.PersonType, PersonListItem> {
    private final PersonViewAdapter adapter;

    public PersonRecyclerView(Context context) {
        super(context);
        this.adapter = new PersonViewAdapter();
    }

    @Override
    public void notifyUpdate(ListView.OnUpdateCompleteCallback callback) {
        this.adapter.notifyDataUpdate(callback);
    }

    @Override
    public void attachPresenter(ListPresenter<Person, Person.PersonType, PersonListItem> presenter) {
        this.adapter.attachPresenter(presenter);
    }

    @Override
    public void setOnClickListener(ListView.OnClickListener<Person, PersonListItem, ListPresenter<Person, Person.PersonType, PersonListItem>> listener) {
        this.adapter.setOnClickListener(listener);
    }

    @Override
    public void setInitialStateSetter(InitialStateSetter<Person, PersonListItem> initialStateSetter) {
        this.adapter.setInitialStateSetter(initialStateSetter);
    }

}
