package com.lucascauthen.uschat.data.repository.user;

import com.lucascauthen.uschat.data.entities.Person;

import java.util.ArrayList;
import java.util.Collections;

public class MultiLevelPersonRepo implements PersonRepo {

    private final PersonCache cachingRepo;

    private final PersonRepo secondaryRepo;

    public MultiLevelPersonRepo(PersonCache cache, PersonRepo secondaryRepo) {
        this.cachingRepo = cache;
        this.secondaryRepo = secondaryRepo;
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

}
