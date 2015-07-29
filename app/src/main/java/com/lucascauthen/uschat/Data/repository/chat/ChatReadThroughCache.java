package com.lucascauthen.uschat.data.repository.chat;

import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.data.repository.CachingRepo;
import com.lucascauthen.uschat.data.repository.Repo;

/**
 * Created by lhc on 6/25/15.
 */
public class ChatReadThroughCache implements Repo<Chat>{
    private final CachingRepo<Chat> firstLevelRepo;
    private final Repo<Chat> secondLevelRepo;

    public ChatReadThroughCache(CachingRepo<Chat> firstLevelRepo, Repo<Chat> secondLevelRepo) {
        this.firstLevelRepo = firstLevelRepo;
        this.secondLevelRepo = secondLevelRepo;
    }

    @Override
    public void put(Chat item) {
        firstLevelRepo.setIsStale();
        secondLevelRepo.put(item);
    }

    @Override
    public Response<Chat> get(Request request) {
        Response<Chat> retVal = null;if(request != null) {
            if (!request.skipCache() && !firstLevelRepo.isStale()) {
                retVal = firstLevelRepo.get(request);
            }
        }
        if(retVal == null) {
            retVal = secondLevelRepo.get(request);
            firstLevelRepo.cache(retVal.getValue());
        }
        return retVal;
    }

    @Override
    public void remove(Chat item) {
        Chat theItem = item;
    }

    @Override
    public boolean exists(Chat item) {
        return false;
    }
}
