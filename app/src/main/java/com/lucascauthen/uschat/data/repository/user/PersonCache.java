package com.lucascauthen.uschat.data.repository.user;

import com.lucascauthen.uschat.data.entities.Person;

import java.util.List;

/**
 * Created by lhc on 8/4/15.
 */
public interface PersonCache extends PersonRepo {
    void cacheFriends(List<Person> people);

    void cacheSentRequests(List<Person> people);

    void cacheReceivedRequests(List<Person> people);

    void cacheSearchResults(List<Person> people);

    void cache(Response response);

    void clear();
}
