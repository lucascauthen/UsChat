package com.lucascauthen.uschat.data.repository.person;

import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.data.repository.CachingRepo;
import com.lucascauthen.uschat.data.repository.Repo;

/**
 * Created by lhc on 6/25/15.
 */
public class PersonReadThroughCache implements Repo<Person> {
    private final CachingRepo<Person> firstLevelRepo;
    private final Repo<Person> secondLevelRepo;

    public PersonReadThroughCache(CachingRepo<Person> firstLevelRepo, Repo<Person> secondLevelRepo) {
        this.firstLevelRepo = firstLevelRepo;
        this.secondLevelRepo = secondLevelRepo;
    }

    @Override
    public void put(Person item) {
        firstLevelRepo.setIsStale();
        secondLevelRepo.put(item);
    }

    @Override
    public Response<Person> get(Request request) {
        Response<Person> retVal = null;
        if(!request.skipCache() && !firstLevelRepo.isStale()) {
            retVal = firstLevelRepo.get(request);
        }

        if(retVal == null) {
            retVal = secondLevelRepo.get(request);
            firstLevelRepo.cache(retVal.getValue());
        }
        return retVal;
    }

    @Override
    public void remove(Person item) {
        Person theItem = item;
    }

    @Override
    public boolean exists(Person item) {
        return false;
    }
}
