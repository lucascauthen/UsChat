package com.lucascauthen.uschat.data.repository.chat;

import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.data.repository.CachingRepo;
import com.lucascauthen.uschat.data.repository.Repo;

/**
 * Created by lhc on 6/25/15.
 */
public class ChatReadThroughCache implements Repo{
    private final CachingRepo<Chat> firstLevelRepo;
    private final Repo<Chat> secondLevelRepo;

    public ChatReadThroughCache(CachingRepo<Chat> firstLevelRepo, Repo<Chat> secondLevelRepo) {
        this.firstLevelRepo = firstLevelRepo;
        this.secondLevelRepo = secondLevelRepo;
    }

    @Override
    public void put(Object item) {
        firstLevelRepo.setIsStale();
        secondLevelRepo.put((Chat)item);
    }

    @Override
    public Response get(Request request) {
        Response<Chat> retVal = null;
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
    public void remove(Object item) {
        Chat theItem = (Chat)item;
    }

    @Override
    public boolean exists(Object item) {
        return false;
    }
}
