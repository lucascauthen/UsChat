package com.lucascauthen.uschat.data.repository.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lhc on 8/4/15.
 */
public class CachedPersonRepo implements PersonCache {

    private List<String> friends = new ArrayList<>();

    private List<String> sentRequests = new ArrayList<>();

    private List<String> receivedRequests = new ArrayList<>();

    @Override
    public void sendFriendRequest(String person) {
        sentRequests.add(person);
    }

    @Override
    public void acceptReceivedRequest(String person) {
        friends.add(person);
        receivedRequests.remove(person);
    }

    @Override
    public void rejectReceivedRequest(String person) {
        receivedRequests.remove(person);
    }

    @Override
    public void deleteSentRequest(String person) {
        sentRequests.remove(person);
    }

    @Override
    public void removeFriend(String person) {
        friends.remove(person);
    }

    @Override
    public Response get(Request request) {
        List<String> listToQuery = null;
        switch (request.requestType()) {
            case RECEIVED_REQUEST:
                listToQuery = receivedRequests;
                break;
            case SENT_REQUEST:
                listToQuery = sentRequests;
                break;
            case FRIEND:
                listToQuery = friends;
                break;
        }
        if (request.hasQuery()) {
            List<String> result = new ArrayList<>();
            for (String item : listToQuery) {
                if (item.contains(request.query())) {
                    result.add(item);
                }
            }
            return new Response(Collections.unmodifiableList(result), request.requestType());
        } else {
            return new Response(Collections.unmodifiableList(listToQuery), request.requestType());
        }
    }

    @Override
    public void get(Request request, GetCallback callback) {
        callback.onGet(this.get(request));
    }


    @Override
    public void cacheFriends(List<String> people) {
        if (people != null) {
            this.friends = people;
        } else {
            friends.clear();
        }
    }

    @Override
    public void cacheSentRequests(List<String> people) {
        if (people != null) {
            this.sentRequests = people;
        } else {
            this.sentRequests.clear();
        }
    }


    @Override
    public void cacheReceivedRequests(List<String> people) {
        if (people != null) {
            this.receivedRequests = people;
        } else {
            receivedRequests.clear();
        }
    }

    @Override
    public void cache(Response response) {
        switch (response.responseType()) {
            case RECEIVED_REQUEST:
                cacheReceivedRequests(response.result());
                break;
            case SENT_REQUEST:
                cacheSentRequests(response.result());
                break;
            case FRIEND:
                cacheFriends(response.result());
                break;
            default:
                //EMPTY
        }
    }

    @Override
    public void clear() {
        friends.clear();
        sentRequests.clear();
        receivedRequests.clear();
    }
}
