package com.lucascauthen.uschat.presentation.presenters;

import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.data.repository.user.PersonRepo;
import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.controller.base.BasePagerViewPresenter;
import com.lucascauthen.uschat.presentation.view.views.ListView;
import com.lucascauthen.uschat.presentation.view.views.cards.PersonListItem;
import com.lucascauthen.uschat.util.NullObject;

public class PersonListPresenter implements ListPresenter<Person, Person.PersonType, PersonListItem> {
    private static final ListView<Person, Person.PersonType, PersonListItem> NULL_VIEW = NullObject.create(ListView.class);
    private static final BasePagerViewPresenter.PagerViewChanger NULL_PAGER_CHANGER = NullObject.create(BasePagerViewPresenter.PagerViewChanger.class);

    private ListView<Person, Person.PersonType, PersonListItem> view = NULL_VIEW;
    private BasePagerViewPresenter.PagerViewChanger changer = NULL_PAGER_CHANGER;

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
    public void setClickListener(ListView.OnClickListener<Person, PersonListItem, ListPresenter<Person, Person.PersonType, PersonListItem>> listener) {
        this.listener = listener;
    }

    @Override
    public void setInitialStateSetter(ListView.InitialStateSetter<Person, PersonListItem> initialStateSetter) {
        this.initialStateSetter = initialStateSetter;
    }


    @Override
    public void attachView(ListView<Person, Person.PersonType, PersonListItem> view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = NULL_VIEW;
    }
}
