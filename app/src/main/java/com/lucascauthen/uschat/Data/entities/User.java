package com.lucascauthen.uschat.data.entities;

import com.lucascauthen.uschat.data.repository.user.PersonCache;
import com.lucascauthen.uschat.data.repository.user.PersonRepo;

/**
 * Created by lhc on 6/10/15.
 */
public class User implements PersonRepo {
    private String name = "Anonymous User";

    private final PersonCache cachingRepo;

    private final PersonRepo secondaryRepo;

    public User(PersonCache cache, PersonRepo secondaryRepo) {
        this.cachingRepo = cache;
        this.secondaryRepo = secondaryRepo;
    }

    public String getName() {
        return name;
    }

    public void login(String name) {
        this.name = name;
    }

    public void logout() {
        cachingRepo.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != User.class) {
            return false;
        } else if (!((User) o).getName().equals(this.getName())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public void sendFriendRequest(String person) {
        cachingRepo.sendFriendRequest(person);
        secondaryRepo.sendFriendRequest(person);
    }

    @Override
    public void acceptReceivedRequest(String person) {
        cachingRepo.acceptReceivedRequest(person);
        secondaryRepo.acceptReceivedRequest(person);
    }

    @Override
    public void rejectReceivedRequest(String person) {
        cachingRepo.rejectReceivedRequest(person);
        secondaryRepo.rejectReceivedRequest(person);
    }

    @Override
    public void deleteSentRequest(String person) {
        cachingRepo.deleteSentRequest(person);
        secondaryRepo.deleteSentRequest(person);
    }

    @Override
    public void removeFriend(String person) {
        cachingRepo.removeFriend(person);
        secondaryRepo.removeFriend(person);
    }

    @Override
    public Response get(Request request) {
        Response response;
        if (request.skipCache()) {
            response = secondaryRepo.get(request);
            cachingRepo.cache(response);
        } else {
            response = cachingRepo.get(request);
        }
        return response;
    }

    @Override
    public void get(Request request, GetCallback callback) {
        callback.onGet(get(request));
    }

}
