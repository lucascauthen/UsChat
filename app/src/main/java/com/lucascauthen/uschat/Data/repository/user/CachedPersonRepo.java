package com.lucascauthen.uschat.data.repository.user;

import com.lucascauthen.uschat.data.entities.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lhc on 8/4/15.
 */
public class CachedPersonRepo implements PersonCache {

    private List<Person> friends = new ArrayList<>();

    private List<Person> sentRequests = new ArrayList<>();

    private List<Person> receivedRequests = new ArrayList<>();

    private List<Person> searchResults = new ArrayList<>();

    @Override
    public void sendFriendRequest(Person person) {
        person.setState(Person.PersonState.SENT_REQUEST);
        sentRequests.add(person);
    }

    @Override
    public void acceptReceivedRequest(Person person) {
        person.setState(Person.PersonState.FRIENDS);
        friends.add(person);
        receivedRequests.remove(person);
    }

    @Override
    public void rejectReceivedRequest(Person person) {
        person.setState(Person.PersonState.NOT_FRIENDS);
        receivedRequests.remove(person);
    }

    @Override
    public void deleteSentRequest(Person person) {
        person.setState(Person.PersonState.NOT_FRIENDS);
        sentRequests.remove(person);
    }

    @Override
    public void removeFriend(Person person) {
        person.setState(Person.PersonState.NOT_FRIENDS);
        friends.remove(person);
    }

    @Override
    public Response get(Request request) {
        List<Person> listToQuery = null;
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
            case SEARCH:
                listToQuery = searchResults;
                break;
        }
        if (request.hasQuery()) {
            List<Person> result = new ArrayList<>();
            for (Person item : listToQuery) {
                if (item.name().contains(request.query())) {
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
    public void cacheFriends(List<Person> people) {
        if (people != null) {
            this.friends = people;
        } else {
            friends.clear();
        }
    }

    @Override
    public void cacheSentRequests(List<Person> people) {
        if (people != null) {
            this.sentRequests = people;
        } else {
            this.sentRequests.clear();
        }
    }


    @Override
    public void cacheReceivedRequests(List<Person> people) {
        if (people != null) {
            this.receivedRequests = people;
        } else {
            receivedRequests.clear();
        }
    }

    @Override
    public void cacheSearchResults(List<Person> people) {
        this.searchResults = people;
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
            case SEARCH:
                cacheSearchResults(response.result());
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
