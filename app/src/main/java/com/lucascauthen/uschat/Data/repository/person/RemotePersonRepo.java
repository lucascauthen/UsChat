package com.lucascauthen.uschat.data.repository.person;

import android.util.Log;

import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.data.repository.Repo;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lhc on 6/25/15.
 */
public class RemotePersonRepo implements Repo<Person> {
    private static final String TAG = "UsChatResponse";

    private Set<Person> items = new LinkedHashSet<>();

    @Override
    public void put(Person item) {
        ParseUser parseUser = ParseUser.getCurrentUser();
        Person user = Person.getCurrentUser();
        if(!user.isFriend(item.getName())) {
            parseUser.add("friends", item.getName());
            user.addFriend(item.getName());
        } else {
            List<String> itemToRemove = new ArrayList<String>();
            itemToRemove.add(item.getName());
            parseUser.removeAll("friends", itemToRemove);
            user.removeFriend(item.getName());
        }
        parseUser.saveInBackground();
    }

    @Override
    public Response<Person> get(Request request) {
        if(request.getConditions().size() > 0) {
            ParseQuery<ParseUser> mainQuery;
            List<ParseQuery<ParseUser>> subQueries = new ArrayList<ParseQuery<ParseUser>>();

            for (String query : request.getConditions()) {
                subQueries.add(ParseUser.getQuery());
                try {
                    subQueries.get(subQueries.size() - 1).whereContains("username", query); //Modify the last query
                } catch (IndexOutOfBoundsException e) {
                    Log.d(TAG, "Error constructing compound query");
                }
            }
            mainQuery = ParseQuery.or(subQueries); //Constructs the compound query

            mainQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            Collection<ParseUser> list = null;
            try {
                list = mainQuery.find();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            List<Person> newPersons = new ArrayList<Person>();
            for (ParseUser parseUser : list) {
                Collection<String> friends = (ArrayList<String>) parseUser.get("friends");
                Person newPerson;
                if (friends != null && friends.contains(ParseUser.getCurrentUser().getUsername())) {
                    newPerson = new Person(parseUser.getUsername(), Person.FriendshipStatus.FRIENDS);
                } else {
                    newPerson = new Person(parseUser.getUsername(), Person.FriendshipStatus.NOT_FRIENDS);
                }
                newPersons.add(newPerson);
            }
            return new Response<>(Collections.unmodifiableList(newPersons), false);
        } else {
            return new Response<>(null, false);
        }
    }

    @Override
    public void remove(Person item) {
        throw new RuntimeException("Not applicable");
    }

    @Override
    public boolean exists(Person item) {
        return items.contains(item);
    }
}
