package com.lucascauthen.uschat.presentation.view.adapters.newadapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.controller.base.BasePersonListViewPresenter;

/**
 * Created by lhc on 6/11/15.
 */
public class PersonViewAdapter extends RecyclerView.Adapter<PersonViewAdapter.PersonViewHolder> implements BasePersonListViewPresenter.PersonListAdapter {


    private BasePersonListViewPresenter presenter = null;

    public PersonViewAdapter() {

    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_person, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        //TODO
    }

    @Override
    public int getItemCount() {
        return 0;//TODO
    }

    @Override
    public void notifyDataUpdate() {
        //TODO
    }

    @Override
    public void attachPresenter(BasePersonListViewPresenter presenter) {
        this.presenter = presenter;
        //TODO
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder implements BasePersonListViewPresenter.PersonListCardView{


        public PersonViewHolder(View itemView) {
            super(itemView);
        }


        @Override
        public void addActionButton(String key, OnClickActionListener listener) {

        }

        @Override
        public void removeActionButton(String key) {

        }

        @Override
        public void changeIconState(String key, int iconPackKey) {

        }
    }
}
