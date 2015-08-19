package com.lucascauthen.uschat.presentation.presenters;

import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.view.views.FriendListView;
import com.lucascauthen.uschat.presentation.view.views.ListView;
import com.lucascauthen.uschat.presentation.view.views.cards.PersonListItem;
import com.lucascauthen.uschat.util.NullObject;

public class FriendListPresenter implements BasePresenter<FriendListView>, ParentPresenter<ListView<Person, Person.PersonType, PersonListItem>> {
    private static final FriendListView NULL_VIEW = NullObject.create(FriendListView.class);
    private FriendListView view = NULL_VIEW;

    private final BackgroundExecutor backgroundExecutor;
    private final ForegroundExecutor foregroundExecutor;
    private final ListPresenter<Person, Person.PersonType, PersonListItem> subPresenter;

    public FriendListPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, ListPresenter<Person, Person.PersonType, PersonListItem> subPresenter) {
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
        this.subPresenter = subPresenter;
        subPresenter.setDisplayType(Person.PersonType.FRIEND);
    }

    public void onSwipe() {
        subPresenter.requestUpdate(() -> {
            view.hideLoading();
        }, true);
    }

    @Override
    public void attachView(FriendListView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = NULL_VIEW;
    }

    @Override
    public void attachSubView(ListView<Person, Person.PersonType, PersonListItem> view) {
        view.setOnClickListener(new PersonListItem.OnClickListener() {
            @Override
            public void onClick(Person person, PersonListItem item, ListPresenter presenter) {
                //TODO
            }
        });
        view.setInitialStateSetter(new PersonListItem.InitialStateSetter() {
            @Override
            public void setState(Person itemData, PersonListItem itemView) {
                //TODO
            }
        });
        subPresenter.attachView(view);
    }
}
