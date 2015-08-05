package com.lucascauthen.uschat.presentation.view.adapters.newadapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.controller.base.BasePersonListViewPresenter;

import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lhc on 6/11/15.
 */
public class PersonViewAdapter extends RecyclerView.Adapter<PersonViewAdapter.PersonViewHolder> implements BasePersonListViewPresenter.BasePersonListAdapter {


    private BasePersonListViewPresenter presenter = null;
    private int numItems = 0;

    private BasePersonListViewPresenter.PersonListCardView.InitialStateSetter setter;
    public PersonViewAdapter() {

    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_person, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v, setter);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        holder.showLoading();
        presenter.getItemInBackground(position, (result) -> {
            holder.bindData(result);
            holder.loadState();
            holder.hideLoading();
        });
    }

    @Override
    public int getItemCount() {
        return numItems;
    }

    @Override
    public void notifyDataUpdate(OnDoneLoadingCallback callback) {
        presenter.getSizeInBackground((size) -> {
            callback.done();
            this.numItems = size;
            this.notifyDataSetChanged();
        });
    }

    @Override
    public void attachPresenter(BasePersonListViewPresenter presenter) {
        this.presenter = presenter;
        this.setter = this.presenter.getInitialStateSetter();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder implements BasePersonListViewPresenter.PersonListCardView{

        @InjectView(R.id.person_card_name)TextView personName;
        @InjectView(R.id.person_card_state_image)ImageView stateImage;
        @InjectView(R.id.person_card_action_container)LinearLayout actionContainer;
        @InjectView(R.id.person_card_progress_bar)ProgressBar progressBar;

        private final HashMap<String, ImageButton> actionButtons = new LinkedHashMap<>();
        private final InitialStateSetter setter;
        private String name;


        private final Context context;

        public PersonViewHolder(View itemView, InitialStateSetter setter) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            context = itemView.getContext();
            this.setter = setter;
        }

        @Override
        public void addActionButton(String key, int iconId, OnClickActionListener listener) {
            ImageButton newButton = new ImageButton(context);
            newButton.setBackground(null);
            newButton.setImageResource(iconId);
            BasePersonListViewPresenter.PersonListCardView thisObject = this;
            newButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(name, thisObject);
                }
            });
            actionContainer.addView(newButton);
            actionButtons.put(key, newButton);
        }

        @Override
        public void removeActionButton(String key) {
            actionContainer.removeView(actionButtons.get(key));
            actionButtons.remove(key);
        }

        @Override
        public void changeIconState(String key, int iconId) {
            actionButtons.get(key).setImageResource(iconId);
        }

        @Override
        public void setStateIcon(int iconId) {
            this.stateImage.setImageResource(iconId);
        }

        @Override
        public void showLoading() {
            this.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void hideLoading() {
            this.progressBar.setVisibility(View.GONE);
        }

        private void bindData(String name) {
            this.name = name;
        }

        private void loadState() {
            setter.setState(name, this);
        }
    }
}
