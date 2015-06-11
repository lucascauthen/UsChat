package com.lucascauthen.uschat.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lucascauthen.uschat.Chatting.Person;
import com.lucascauthen.uschat.FriendsFragment;
import com.lucascauthen.uschat.MainActivity;
import com.lucascauthen.uschat.R;

import java.util.List;

/**
 * Created by lhc on 6/11/15.
 */
public class PersonViewAdapter extends RecyclerView.Adapter<PersonViewAdapter.PersonViewHolder>{
    private List<Person> persons;

    public PersonViewAdapter(List<Person> persons) {
        this.persons = persons;
    }
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.personcard_layout, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, final int position) {
        holder.personName.setText(persons.get(position).getName());
        holder.personFriendshipStatus.setText(persons.get(position).getFriendshipStatusText());
        ((ImageButton)holder.personSettingButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) v.getContext();
                ((FriendsFragment)mainActivity.getSupportFragmentManager().findFragmentById(mainActivity.getFriendFragmentId()))
                        .onPersonSettingsRequest(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView personName;
        TextView personFriendshipStatus;
        ImageButton personSettingButton;

        public PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.person_cv);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personFriendshipStatus = (TextView)itemView.findViewById(R.id.person_friendship_status);
            personSettingButton = (ImageButton)itemView.findViewById(R.id.person_settings_button);
        }
    }
}
