package com.lucascauthen.uschat.data.repository.chat;

import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.data.repository.Repo;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by lhc on 6/25/15.
 */
public class RemoteChatRepo implements Repo<Chat> {
    private Set<Chat> items = new LinkedHashSet<>();
    @Override
    public void put(Chat item) {
        //TODO: Add network call to update list
        items.add(item);
    }

    @Override
    public Response<Chat> get(Request request) {
        //TODO: Add network call to update list
        return new Response<>(Collections.unmodifiableCollection(items), false);
    }

    @Override
    public void remove(Chat item) {
        //TODO: Add network call to update list
        items.remove(item);
    }

    @Override
    public boolean exists(Chat item) {
        return items.contains(item);
    }
}
