package com.lucascauthen.uschat.presentation.presenters;

import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.view.views.FriendRequestView;
import com.lucascauthen.uschat.presentation.view.views.ListView;
import com.lucascauthen.uschat.presentation.view.views.cards.PersonListItem;
import com.lucascauthen.uschat.util.NullObject;

public class FriendRequestPresenter implements BasePresenter<FriendRequestView>, ParentPresenter<ListView<Person, Person.PersonType, PersonListItem>> {
    private static final FriendRequestView NULL_VIEW = NullObject.create(FriendRequestView.class);
    private FriendRequestView view = NULL_VIEW;

    private final BackgroundExecutor backgroundExecutor;
    private final ForegroundExecutor foregroundExecutor;
    private final ListPresenter<Person, Person.PersonType, PersonListItem> subPresenter;

    public FriendRequestPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, ListPresenter<Person, Person.PersonType, PersonListItem> subPresenter) {
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
        this.subPresenter = subPresenter;
        subPresenter.setDisplayType(Person.PersonType.REQUESTS);
    }

    public void onSwipe() {
        subPresenter.requestUpdate(() -> {
            view.hideLoading();
        }, true);
    }

    @Override
    public void attachView(FriendRequestView view) {
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
