package com.lucascauthen.uschat.presentation.view.adapters;

import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.presentation.controller.PersonViewPresenter;


import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

/**
 * Created by lhc on 6/11/15.
 */
public class PersonViewAdapter extends RecyclerView.Adapter<PersonViewAdapter.PersonViewHolder> implements PersonViewPresenter.PersonFinderAdapter {


    private List<Person> persons = new CopyOnWriteArrayList<>(); //Needed or an exception is thrown

    private final Executor foregroundExecutor;

    private PersonViewHolder.InitialStateSetter stateSetter; //Tells each holder what state to be in when they are created
    private PersonViewPresenter.OnPersonSettingClickedListener clickListener= new PersonViewPresenter.NullOnPersonSettingCLickedListener(); //Tells the holder what state to be in after a click
    private final int[] iconPack;

    public PersonViewAdapter(Executor foregroundExecutor, PersonViewHolder.InitialStateSetter stateSetter, int[] iconPack) {
        this.foregroundExecutor = foregroundExecutor;
        this.stateSetter = stateSetter;
        this.iconPack = iconPack;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_person, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, final int position) {
        Person currentPerson = persons.get(position);
        holder.attachData(currentPerson);
        holder.setIconPack(iconPack);
        holder.setInitialButtonState(stateSetter.getState(currentPerson));
        holder.personName.setText(currentPerson.getName());
        holder.setSelectPersonButtonOnClickListener(clickListener);
    }

    public void setClickListener(PersonViewPresenter.OnPersonSettingClickedListener l) {
        this.clickListener = l;
    }
    @Override
    public int getItemCount() {
        return persons.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public void updateData(List<Person> newList) {
        if (newList != null) {
            deleteStale(this.persons, newList);
            addNew(this.persons, newList);
        } else {
            persons.clear();
        }
        foregroundExecutor.execute(() -> notifyDataSetChanged());
    }

    private List<Person> deleteStale(List<Person> activeList, List<Person> updatedList) { //Deletes all the items in activeList that are not also in updatedList
        for (Person oldItem : activeList) {
            boolean isStale = true;
            for (Person newItem : updatedList) {
                if (oldItem.equals(newItem)) {
                    isStale = false;
                    break;
                }
            }
            if (isStale) {
                activeList.remove(oldItem);
            }
        }
        return activeList;
    }

    private List<Person> addNew(List<Person> activeList, List<Person> updatedList) { //Adds all items from updatedList that are not already contained in activeList
        if (updatedList.size() != activeList.size()) { //If the lists are the same size, there will be no new items to add
            for (Person newItem : updatedList) {
                boolean contained = false; //Default assumption that the item is already contained in activeList
                for (Person oldItem : activeList) {
                    if (oldItem.equals(newItem)) {
                        contained = true;
                        break; //The item is already in the list
                    } else {
                        contained = false;
                    }
                }
                if (!contained) {
                    activeList.add(newItem);
                }
            }
        }
        return activeList;
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        private final int SELECT_DELAY_MILLIS = 1000;
        CardView cv;
        TextView personName;
        ImageButton personSettingButton;
        ProgressBar progress;
        private View.OnClickListener externalOnClickListener = null;
        private Person dataObject;

        private int[] iconPack;
        private int curState = -1;

        public void setIconPack(int[] iconPack) {
            this.iconPack = iconPack;
        }

        private final View.OnClickListener NULL_ON_CLICK_LISTENER = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                internalOnClickAction(curState);
            }
        };

        public PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.person_cv);
            personName = (TextView) itemView.findViewById(R.id.person_name);
            personSettingButton = (ImageButton) itemView.findViewById(R.id.person_settings_button);
            progress = (ProgressBar) itemView.findViewById(R.id.person_card_progress_bar);
            personSettingButton.setOnClickListener(NULL_ON_CLICK_LISTENER);
        }

        public void setSelectPersonButtonOnClickListener(PersonViewPresenter.OnPersonSettingClickedListener l) {
            personSettingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    internalOnClickAction(l.onClick(dataObject, curState));
                    if (externalOnClickListener != null) {
                        externalOnClickListener.onClick(v);
                    }
                }
            });
        }
        public void setInitialButtonState(int state) {
            reloadIcon(state);
            progress.setVisibility(View.GONE);
            personSettingButton.setVisibility(View.VISIBLE);

        }
        public void reloadIcon(int state) {
            curState = state;
            if(state == -1 || state >= iconPack.length) {
                //TODO: Set the icon to some internal error default
            } else {
                personSettingButton.setImageResource(iconPack[state]);
            }
        }

        private void internalOnClickAction(int state) {
            progress.setVisibility(View.VISIBLE);
            personSettingButton.setVisibility(View.GONE);
            reloadIcon(state);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progress.setVisibility(View.GONE);
                    personSettingButton.setVisibility(View.VISIBLE);
                }
            }, SELECT_DELAY_MILLIS);
        }
        public void attachData(Person person) {
             this.dataObject = person;
        }

        public interface InitialStateSetter {
            int getState(Person person);
        }
    }

}
