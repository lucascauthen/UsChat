package com.lucascauthen.uschat.data.repository.user;

import java.util.List;

/**
 * Created by lhc on 8/4/15.
 */
public interface PersonCache extends PersonRepo{
    void cacheFriends(List<String> people);

    void cacheSentRequests(List<String> people);

    void cacheReceivedRequests(List<String> people);

    void cache(Response response);

    void clear();
}
