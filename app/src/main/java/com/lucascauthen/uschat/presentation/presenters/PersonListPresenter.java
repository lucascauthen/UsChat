package com.lucascauthen.uschat.presentation.presenters;

import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.data.repository.user.PersonRepo;
import com.lucascauthen.uschat.util.executor.BackgroundExecutor;
import com.lucascauthen.uschat.util.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.view.base.ListView;
import com.lucascauthen.uschat.presentation.view.base.cards.PersonListItem;
import com.lucascauthen.uschat.util.NullObject;

public class PersonListPresenter implements ListPresenter<Person, Person.PersonType, PersonListItem> {
    private static final ListView<Person, Person.PersonType, PersonListItem> NULL_VIEW = NullObject.create(ListView.class);

    private ListView<Person, Person.PersonType, PersonListItem> view = NULL_VIEW;

    private final BackgroundExecutor backgroundExecutor;
    private final ForegroundExecutor foregroundExecutor;
    private final PersonRepo repository;
    private boolean repoNeedUpdate = true;

    private Person.PersonType displayType;

    private String query = "";

    ListView.OnClickListener<Person, PersonListItem, ListPresenter<Person, Person.PersonType, PersonListItem>> listener;
    ListView.InitialStateSetter<Person, PersonListItem> initialStateSetter;

    public PersonListPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, PersonRepo repository) {
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
        this.repository = repository;
    }

    @Override
    public Person getItem(int index) {
        if(repoNeedUpdate) {
            repoNeedUpdate = false;
            return repository.get(new PersonRepo.Request(true, query, displayType)).result().get(index);
        } else {
            return repository.get(new PersonRepo.Request(false, query, displayType)).result().get(index);
        }
    }

    @Override
    public void getItemInBackground(int position, GetItemCallBack<Person> callback) {
        backgroundExecutor.execute(() -> {
            Person item = getItem(position);
            foregroundExecutor.execute(() -> {
                callback.onGetItem(item);
            });
        });
    }

    @Override
    public int getSize() {
        if(repoNeedUpdate) {
            repoNeedUpdate = false;
            return repository.get(new PersonRepo.Request(true, query, displayType)).result().size();
        } else {
            return repository.get(new PersonRepo.Request(false, query, displayType)).result().size();
        }
    }

    @Override
    public void getSizeInBackground(GetSizeCallback callback) {
        backgroundExecutor.execute(() -> {
            int size = getSize();
            foregroundExecutor.execute(() -> {
                callback.onGetSize(size);
            });
        });
    }
    @Override
    public void requestUpdate(ListView.OnUpdateCompleteCallback callback, boolean updateRepo) {
        this.repoNeedUpdate = updateRepo;
        view.notifyUpdate(callback);
    }

    @Override
    public void setDisplayType(Person.PersonType displayType) {
        this.displayType = displayType;
    }

    @Override
    public void setFilterQuery(String query) {
        this.query = query;
    }

    @Override
    public void attachView(ListView<Person, Person.PersonType, PersonListItem> view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        this.view = NULL_VIEW;
    }
}
