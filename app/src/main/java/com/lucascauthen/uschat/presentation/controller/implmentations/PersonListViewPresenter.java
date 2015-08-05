package com.lucascauthen.uschat.presentation.controller.implmentations;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.data.entities.User;
import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.controller.base.BasePersonListViewPresenter;
import com.lucascauthen.uschat.util.NullObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhc on 7/30/15.
 */
public class PersonListViewPresenter implements BasePersonListViewPresenter {

    private PersonListAdapter NULL_ADAPTER = NullObject.create(PersonListAdapter.class);
    private PersonListAdapter adapter = NULL_ADAPTER;

    private final ForegroundExecutor foregroundExecutor;
    private final BackgroundExecutor backgroundExecutor;

    private List<Integer> iconPack = new ArrayList<>(); //Default

    private PersonListCardView.InitialStateSetter stateSetter;


    public PersonListViewPresenter(ForegroundExecutor foregroundExecutor, BackgroundExecutor backgroundExecutor) {
        this.foregroundExecutor = foregroundExecutor;
        this.backgroundExecutor = backgroundExecutor;
        iconPack.add(R.drawable.ic_action_error);
    }

    @Override
    public User getItem(int index) {
        return null; //TODO
    }

    @Override
    public void getItemInBackground(int index, OnGetItemCallback<User> callback) {
        //TODO
    }

    @Override
    public int getSize() {
        return 0; //TODO
    }

    @Override
    public void getSizeInBackground(OnGetSizeCallback callback) {
        //TODO
    }

    @Override
    public void attachAdapter(PersonListAdapter adapter) {
        throw new RuntimeException("Not applicable. You need to pass the presenter the onClickListener, initialStateSetter, and iconPack.");
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
    public List<Integer> getIconPack() {
        return this.iconPack;
    }

    @Override
    public void attachAdapter(PersonListAdapter adapter, PersonListCardView.InitialStateSetter setter, List<Integer> iconPack) {
        this.adapter = adapter;
        this.stateSetter = setter;
        this.iconPack = iconPack;
        adapter.attachPresenter(this);
    }
}
