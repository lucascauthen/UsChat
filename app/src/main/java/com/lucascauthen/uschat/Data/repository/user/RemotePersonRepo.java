package com.lucascauthen.uschat.data.repository.user;

import com.lucascauthen.uschat.data.entities.Person;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by lhc on 8/4/15.
 */
public class RemotePersonRepo implements PersonRepo {
    @Override
    public void sendFriendRequest(Person person) {
        ParseUser user = ParseUser.getCurrentUser();
        try {
            user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.add("sentRequests", person.name());
        try {
            user.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void acceptReceivedRequest(Person person) {
        ParseUser user = ParseUser.getCurrentUser();
        try {
            user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.removeAll("receivedRequests", Collections.singletonList(person));
        user.addUnique("friends", person.name());
        try {
            user.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rejectReceivedRequest(Person person) {
        ParseUser user = ParseUser.getCurrentUser();
        try {
            user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.removeAll("receivedRequests", Collections.singletonList(person.name()));
        try {
            user.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteSentRequest(Person person) {
        ParseUser user = ParseUser.getCurrentUser();
        try {
            user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.removeAll("sentRequests", Collections.singletonList(person.name()));
        try {
            user.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeFriend(Person person) {
        ParseUser user = ParseUser.getCurrentUser();
        try {
            user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.removeAll("friends", Collections.singletonList(person.name()));
        try {
            user.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Response get(Request request) {
        return null; //TODO
    }

    @Override
    public void get(Request request, GetCallback callback) {
        callback.onGet(this.get(request));
    }

}
