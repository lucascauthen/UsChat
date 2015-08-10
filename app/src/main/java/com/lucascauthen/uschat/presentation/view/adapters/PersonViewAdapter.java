package com.lucascauthen.uschat.presentation.view.adapters.newadapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.controller.base.BasePersonListViewPresenter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lhc on 6/11/15.
 */
public class PersonViewAdapter extends RecyclerView.Adapter<PersonViewAdapter.PersonViewHolder> implements BasePersonListViewPresenter.BasePersonListAdapter {


    private BasePersonListViewPresenter presenter = null;
    private int numItems = 0;

    private final ForegroundExecutor foregroundExecutor;
    private BasePersonListViewPresenter.PersonListCardView.InitialStateSetter setter;
    public PersonViewAdapter(ForegroundExecutor foregroundExecutor) {
        this.foregroundExecutor = foregroundExecutor;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_person, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v, setter, presenter, foregroundExecutor);
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
        @InjectView(R.id.person_cv) CardView cv;
        @InjectView(R.id.person_card_parent_layout) RelativeLayout background;

        private final HashMap<String, ImageButton> actionButtons = new LinkedHashMap<>();
        private final InitialStateSetter setter;
        private final BasePersonListViewPresenter presenter;
        private final ForegroundExecutor foregroundExecutor;
        private Person person;




        private final Context context;

        public PersonViewHolder(View itemView, InitialStateSetter setter, BasePersonListViewPresenter presenter, ForegroundExecutor foregroundExecutor) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            context = itemView.getContext();
            this.setter = setter;
            this.presenter = presenter;
            this.foregroundExecutor = foregroundExecutor;

        }

        @Override
        public void addActionButton(String key, int iconId, OnClickActionListener listener) {
            foregroundExecutor.execute(() -> {
                ImageButton newButton = new ImageButton(context);
                newButton.setBackground(null);
                newButton.setImageResource(iconId);
                BasePersonListViewPresenter.PersonListCardView thisObject = this;
                newButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onClick(person, thisObject, presenter);
                    }
                });
                actionContainer.addView(newButton);
                actionButtons.put(key, newButton);
            });
        }

        @Override
        public void removeActionButton(String key) {
            foregroundExecutor.execute(() -> {
                actionContainer.removeView(actionButtons.get(key));
                actionButtons.remove(key);
            });
        }

        @Override
        public void resetActionButtons() {
            foregroundExecutor.execute(() -> {
                for(Iterator<Map.Entry<String, ImageButton>> entries = actionButtons.entrySet().iterator(); entries.hasNext();) {
                    Map.Entry<String, ImageButton> entry = entries.next();
                    ImageButton button = entry.getValue();
                    ((ViewGroup)button.getParent()).removeView(button);
                    entries.remove();
                }
            });
        }

        @Override
        public void changeIconState(String key, int iconId) {
            foregroundExecutor.execute(() -> {
                actionButtons.get(key).setImageResource(iconId);
            });
        }

        @Override
        public void setStateIcon(int iconId) {
            foregroundExecutor.execute(() -> {
                this.stateImage.setImageResource(iconId);
            });
        }


        @Override
        public void showLoading() {
            foregroundExecutor.execute(() -> {
                this.progressBar.setVisibility(View.VISIBLE);
                this.actionContainer.setVisibility(View.GONE);
            });
        }

        @Override
        public void hideLoading() {
            foregroundExecutor.execute(() -> {
                this.progressBar.setVisibility(View.GONE);
                this.actionContainer.setVisibility(View.VISIBLE);
            });
        }

        private void bindData(Person person) {
            this.person = person;
            this.personName.setText(this.person.name());
        }

        private void loadState() {
            setter.setState(person, this);
        }
    }
}
