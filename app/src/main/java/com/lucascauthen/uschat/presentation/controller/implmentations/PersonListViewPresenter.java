package com.lucascauthen.uschat.presentation.controller.implmentations;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.data.entities.User;
import com.lucascauthen.uschat.data.repository.user.PersonRepo;
import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.controller.base.BasePersonListViewPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BaseRecyclerViewPresenter;
import com.lucascauthen.uschat.util.NullObject;

/**
 * Created by lhc on 7/30/15.
 */
public class PersonListViewPresenter implements BasePersonListViewPresenter {

    private BasePersonListAdapter NULL_ADAPTER = NullObject.create(BasePersonListAdapter.class);
    private BasePersonListAdapter adapter = NULL_ADAPTER;

    private final ForegroundExecutor foregroundExecutor;
    private final BackgroundExecutor backgroundExecutor;
    private final User user;

    private PersonListCardView.InitialStateSetter stateSetter;
    private PersonRepo.Type displayType;
    private boolean repoNeedUpdate = true;
    private String query = "";



    public PersonListViewPresenter(ForegroundExecutor foregroundExecutor, BackgroundExecutor backgroundExecutor, User user) {
        this.foregroundExecutor = foregroundExecutor;
        this.backgroundExecutor = backgroundExecutor;
        this.user = user;
    }

    @Override
    public Person getItem(int index) {
        if(repoNeedUpdate) {
            repoNeedUpdate = false;
            return user.get(new PersonRepo.Request(true, query, displayType)).result().get(index);
        } else {
            return user.get(new PersonRepo.Request(false, query, displayType)).result().get(index);
        }
    }

    @Override
    public void getItemInBackground(int index, OnGetItemCallback<Person> callback) {
        backgroundExecutor.execute(() -> {
            Person item = getItem(index);
            foregroundExecutor.execute(() -> {
                callback.onGetItem(item);
            });
        });
    }

    @Override
    public int getSize() {
        if(repoNeedUpdate) {
            repoNeedUpdate = false;
            return user.get(new PersonRepo.Request(true, query, displayType)).result().size();
        } else {
            return user.get(new PersonRepo.Request(false, query, displayType)).result().size();
        }
    }

    @Override
    public void getSizeInBackground(OnGetSizeCallback callback) {
        backgroundExecutor.execute(() -> {
            int size = getSize();
            foregroundExecutor.execute(() -> {
                callback.onGetSize(size);
            });
        });
    }

    @Override
    public void attachAdapter(BasePersonListAdapter adapter) {
        throw new RuntimeException("Not applicable. You need to pass the presenter the initialStateSetter.");
    }

    @Override
    public void detachAdapter() {
        this.adapter = NULL_ADAPTER;
    }


    @Override
    public PersonListCardView.InitialStateSetter getInitialStateSetter() {
        return stateSetter;
    }


    @Override
    public void attachAdapter(BasePersonListAdapter adapter, PersonListCardView.InitialStateSetter setter) {
        this.adapter = adapter;
        this.stateSetter = setter;
        adapter.attachPresenter(this);
    }

    @Override
    public void setDisplayType(PersonRepo.Type type) {
        this.displayType = type;
    }

    @Override
    public void requestUpdate(BasePersonListAdapter.OnDoneLoadingCallback callback, boolean repoNeedUpdate) {
        this.repoNeedUpdate = repoNeedUpdate;
        adapter.notifyDataUpdate(callback);
    }

    @Override
    public void sendAction(Person person, BaseActions action, PersonRepo.OnCompleteAction callback) {
        backgroundExecutor.execute(() -> {
            action.execute(person, user, callback);
        });
    }

    @Override
    public void setQuery(String query) {
        this.query = query;
    }

}
