package com.lucascauthen.uschat.presentation.view.components.recyclerviews;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.presentation.presenters.ListPresenter;
import com.lucascauthen.uschat.presentation.view.base.ListView;
import com.lucascauthen.uschat.presentation.view.base.cards.PersonListItem;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


public class PersonViewAdapter extends RecyclerView.Adapter<PersonViewAdapter.PersonViewHolder> {


    private ListPresenter<Person, Person.PersonType, PersonListItem> presenter;
    private ListView.OnClickListener<Person, PersonListItem, ListPresenter<Person, Person.PersonType, PersonListItem>> listener;
    private ListView.InitialStateSetter<Person, PersonListItem> initialStateSetter;

    private int numItems = 0;

    public PersonViewAdapter() {
        //EMPTY
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_person, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v, presenter, listener, initialStateSetter);
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

    public void notifyDataUpdate(ListView.OnUpdateCompleteCallback callback) {
        presenter.getSizeInBackground((size) -> {
            callback.done();
            this.numItems = size;
            this.notifyDataSetChanged();
        });
    }

    public void attachPresenter(ListPresenter<Person, Person.PersonType, PersonListItem> presenter) {
        this.presenter = presenter;
    }

    public void setOnClickListener(ListView.OnClickListener<Person, PersonListItem, ListPresenter<Person, Person.PersonType, PersonListItem>> listener) {
        this.listener = listener;
    }

    public void setInitialStateSetter(ListView.InitialStateSetter<Person, PersonListItem> initialStateSetter) {
        this.initialStateSetter = initialStateSetter;
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder implements PersonListItem {

        @InjectView(R.id.person_card_name) TextView personName;
        @InjectView(R.id.person_card_state_image) ImageView stateImage;
        @InjectView(R.id.person_card_action_container) LinearLayout actionContainer;
        @InjectView(R.id.person_card_progress_bar) ProgressBar progressBar;
        @InjectView(R.id.person_cv) CardView cv;
        @InjectView(R.id.person_card_parent_layout) RelativeLayout background;

        private final HashMap<String, ImageButton> actionButtons = new LinkedHashMap<>();

        private Person person;

        private final Context context;
        private final ListPresenter<Person, Person.PersonType, PersonListItem> presenter;
        private final ListView.OnClickListener<Person, PersonListItem, ListPresenter<Person, Person.PersonType, PersonListItem>> listener;
        private final ListView.InitialStateSetter<Person, PersonListItem> initialStateSetter;

        public PersonViewHolder(View itemView, ListPresenter<Person, Person.PersonType, PersonListItem> presenter,
                                ListView.OnClickListener<Person, PersonListItem, ListPresenter<Person, Person.PersonType, PersonListItem>> listener,
                                ListView.InitialStateSetter<Person, PersonListItem> initialStateSetter) {
            super(itemView);
            context = itemView.getContext();
            this.presenter = presenter;
            this.listener = listener;
            this.initialStateSetter = initialStateSetter;
            ButterKnife.inject(this, itemView);
            cv.setOnClickListener((view) -> {
                if(listener != null) {
                    listener.onClick(person, this, presenter);
                }
            });
        }

        @Override
        public void addActionButton(String key, int iconId, PersonListItem.OnClickActionListener listener) {
            ImageButton newButton = new ImageButton(context);
            newButton.setBackground(null);
            newButton.setImageResource(iconId);
            PersonListItem thisObject = this;
            newButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(person, thisObject, presenter);
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
        public void resetActionButtons() {
            for (Iterator<Map.Entry<String, ImageButton>> entries = actionButtons.entrySet().iterator(); entries.hasNext(); ) {
                Map.Entry<String, ImageButton> entry = entries.next();
                ImageButton button = entry.getValue();
                ((ViewGroup) button.getParent()).removeView(button);
                entries.remove();
            }
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
            this.actionContainer.setVisibility(View.GONE);
        }

        @Override
        public void hideLoading() {
            this.progressBar.setVisibility(View.GONE);
            this.actionContainer.setVisibility(View.VISIBLE);
        }

        private void bindData(Person person) {
            this.person = person;
            this.personName.setText(this.person.name());
        }

        private void loadState() {
            initialStateSetter.setState(person, this);
        }
    }
}
