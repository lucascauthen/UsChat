package com.lucascauthen.uschat.presentation.controller;

import android.support.annotation.Nullable;

import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.data.repository.Repo;
import com.lucascauthen.uschat.data.repository.person.InMemoryPersonRepo;
import com.lucascauthen.uschat.data.repository.person.PersonReadThroughCache;
import com.lucascauthen.uschat.data.repository.person.RemotePersonRepo;
import com.lucascauthen.uschat.presentation.view.adapters.PersonViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import rx.Observable;
import rx.Scheduler;
import rx.android.widget.OnTextChangeEvent;

/**
 * Created by lhc on 7/24/15.
 */
public class PersonViewPresenter {
    private static final PersonFinderAdapter NULL_ADAPTER = new NullPersonFinderAdapter();
    private static final PersonViewAdapter.PersonViewHolder.InitialStateSetter NULL_STATE_SETTER = new NullInitialStateSetter();
    private static final OnPersonSettingClickedListener NULL_CLICK_LISTENER = new NullOnPersonSettingCLickedListener();

    private final Scheduler backgroundScheduler;
    private final Scheduler foregroundScheduler;
    private final Executor backgroundExecutor;
    private final Executor foregroundExecutor;

    private Observable<OnTextChangeEvent> searchTextObservable;


    private PersonFinderAdapter adapter = NULL_ADAPTER;
    private PersonViewAdapter.PersonViewHolder.InitialStateSetter stateSetter = NULL_STATE_SETTER;
    private OnPersonSettingClickedListener personClickListener = NULL_CLICK_LISTENER;


    private final PersonReadThroughCache personRepository = new PersonReadThroughCache(new InMemoryPersonRepo(), new RemotePersonRepo());

    public PersonViewPresenter(Scheduler backgroundScheduler, Scheduler foregroundScheduler, Executor backgroundExecutor, Executor foregroundExecutor) {
        this.backgroundScheduler = backgroundScheduler;
        this.foregroundScheduler = foregroundScheduler;
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
    }

    public void updateView(@Nullable Repo.Request request) {
        if(request == null) {
            this.adapter.updateData(null);
        } else {
            this.adapter.updateData(new ArrayList<>(personRepository.get(request).getValue()));
        }
    }

    public void updateView(Person item) {
        if(item != null) {
            personRepository.put(item);
        }
    }

    public void attachAdapter(PersonFinderAdapter adapter) {
        this.adapter = adapter;
    }

    public void attachClickListener(OnPersonSettingClickedListener l) {
        adapter.setClickListener(l);
    }

    public void setStateSetter(PersonViewAdapter.PersonViewHolder.InitialStateSetter setter) {
        this.stateSetter = setter;
    }

    public void setOnPersonSettingCLickedListener(OnPersonSettingClickedListener l) {
        this.personClickListener = l;
    }


    public interface PersonFinderAdapter {

        void updateData(List<Person> persons);

        void setClickListener(OnPersonSettingClickedListener l);

    }

    public static class NullInitialStateSetter implements PersonViewAdapter.PersonViewHolder.InitialStateSetter {

        @Override
        public int getState(Person person) {
            return -1;
        }
    }

    public static class NullPersonFinderAdapter implements PersonFinderAdapter {

        @Override
        public void updateData(List<Person> persons) {
            //EMPTY
        }

        @Override
        public void setClickListener(OnPersonSettingClickedListener l) {

        }
    }

    public static class NullOnPersonSettingCLickedListener implements OnPersonSettingClickedListener {

        @Override
        public int onClick(Person person, int stateBeforeClick) {
            return -1;
        }
    }

    public interface OnPersonSettingClickedListener {
        int onClick(Person person, int stateBeforeClick);
    }

}
