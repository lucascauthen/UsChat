package com.lucascauthen.uschat.presentation.presenters;

import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.data.repository.user.PersonRepo;
import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.view.base.FriendSearchView;
import com.lucascauthen.uschat.presentation.view.base.ListView;
import com.lucascauthen.uschat.presentation.view.base.cards.PersonListItem;
import com.lucascauthen.uschat.util.NullObject;

public class FriendSearchPresenter implements BasePresenter<FriendSearchView>, ParentPresenter<ListView<Person, Person.PersonType, PersonListItem>>, PersonListItem.PersonListPresenter{
    private static final FriendSearchView NULL_VIEW = NullObject.create(FriendSearchView.class);
    private FriendSearchView view = NULL_VIEW;

    private final BackgroundExecutor backgroundExecutor;
    private final ForegroundExecutor foregroundExecutor;
    private final ListPresenter<Person, Person.PersonType, PersonListItem> subPresenter;
    private final PersonRepo repo;

    public FriendSearchPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, ListPresenter<Person, Person.PersonType, PersonListItem> subPresenter, PersonRepo repo) {
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
        this.subPresenter = subPresenter;
        this.repo = repo;
        subPresenter.setDisplayType(Person.PersonType.SEARCH);
    }

    public void onSwipe() {
        subPresenter.requestUpdate(() -> {
            view.hideLoading();
        }, true);
    }

    public void sendQuery(String query) {
        subPresenter.setFilterQuery(query);
        subPresenter.requestUpdate(() -> {
            view.hideLoading();
        }, true);
    }

    @Override
    public void attachView(FriendSearchView view) {
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
                //EMPTY
            }
        });
        view.setInitialStateSetter(PersonListItem.BasePersonListBehavior.defaultSetter(this));
        subPresenter.attachView(view);
    }

    @Override
    public void sendAction(Person person, PersonListItem.BaseActions action, PersonRepo.OnCompleteAction callback) {
        backgroundExecutor.execute(() -> {
            action.execute(person, repo, callback);
        });
    }

    @Override
    public void onActionComplete(Runnable completeFunction) {
        foregroundExecutor.execute(completeFunction);
    }
}

