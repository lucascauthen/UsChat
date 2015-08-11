package com.lucascauthen.uschat.data.entities;

import com.lucascauthen.uschat.data.repository.user.PersonCache;
import com.lucascauthen.uschat.data.repository.user.PersonRepo;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by lhc on 6/10/15.
 */
public class User implements PersonRepo {
    private static String name = "Anonymous User";

    private final PersonCache cachingRepo;

    private final PersonRepo secondaryRepo;

    public User(PersonCache cache, PersonRepo secondaryRepo) {
        this.cachingRepo = cache;
        this.secondaryRepo = secondaryRepo;
    }

    public static void login(String name) {
        User.name = name;
    }

    public static String getName() {
        return name;
    }

    public void logout() {
        cachingRepo.clear();
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public void sendFriendRequest(Person person, OnCompleteAction callback) {
        cachingRepo.sendFriendRequest(person, null);
        secondaryRepo.sendFriendRequest(person, callback);
    }

    @Override
    public void acceptReceivedRequest(Person person, OnCompleteAction callback) {
        cachingRepo.acceptReceivedRequest(person, null);
        secondaryRepo.acceptReceivedRequest(person, callback);
    }

    @Override
    public void rejectReceivedRequest(Person person, OnCompleteAction callback) {
        cachingRepo.rejectReceivedRequest(person, null);
        secondaryRepo.rejectReceivedRequest(person, callback);
    }

    @Override
    public void deleteSentRequest(Person person, OnCompleteAction callback) {
        cachingRepo.deleteSentRequest(person, null);
        secondaryRepo.deleteSentRequest(person, callback);
    }

    @Override
    public void removeFriend(Person person, OnCompleteAction callback) {
        cachingRepo.removeFriend(person, null);
        secondaryRepo.removeFriend(person, callback);
    }

    @Override
    public Response get(Request request) {
        if (request.query() == null) { //Clear the cache
            cachingRepo.clear();
            return new Response(Collections.unmodifiableList(new ArrayList<>()), request.requestType());
        } else {
            Response response;
            if (request.skipCache()) {
                response = secondaryRepo.get(request);
                cachingRepo.cache(response);
            } else {
                response = cachingRepo.get(request);
            }
            return response;
        }
    }

    @Override
    public void get(Request request, GetCallback callback) {
        callback.onGet(get(request));
    }

    public enum PersonState {
        FRIENDS,
        SENT_REQUEST,
        RECIEVED_REQUEST,
        NOT_FRIENDS
    }
}
