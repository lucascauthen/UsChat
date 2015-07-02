package com.lucascauthen.uschat.data.repository.chat;

import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.data.repository.CachingRepo;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by lhc on 6/25/15.
 */
public class InMemoryChatRepo implements CachingRepo<Chat> {
    private Set<Chat> items = new LinkedHashSet<>();
    private boolean isStale;

    @Override
    public boolean isStale() {
        return isStale;
    }

    @Override
    public void setIsStale() {
        isStale = true;
    }

    @Override
    public void cache(Collection<Chat> items) {
        this.items = new LinkedHashSet<Chat>((LinkedHashSet<Chat>)items);
        isStale = false;
    }

    @Override
    public void put(Chat item) {
        throw new RuntimeException("Not applicable");
    }


    @Override
    public Response get(Request request) {
        return new Response<Chat>(Collections.unmodifiableCollection(items), true);
    }

    @Override
    public void remove(Chat item) {

    }

    @Override
    public boolean exists(Chat item) {
        return false;
    }
}
