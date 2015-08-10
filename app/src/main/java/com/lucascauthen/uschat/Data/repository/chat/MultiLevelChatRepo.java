package com.lucascauthen.uschat.data.repository.chat;

import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.data.entities.Person;

import java.util.List;

/**
 * Created by lhc on 8/4/15.
 */
public class MultiLevelChatRepo implements ChatRepo {

    private final ChatCache cache;
    private final ChatRepo secondaryRepo;

    public MultiLevelChatRepo(ChatCache cache, ChatRepo secondaryRepo) {
        this.cache = cache;
        this.secondaryRepo = secondaryRepo;
    }

    @Override
    public void sendChat(Chat chat) {
        cache.sendChat(chat);
        secondaryRepo.sendChat(chat);
    }

    @Override
    public void openChat(Chat chat) {
        cache.openChat(chat);
        secondaryRepo.openChat(chat);
    }

    @Override
    public Response get(Request request) {
        if(request.skipCache()) {
            return secondaryRepo.get(request);
        } else {
            return cache.get(request);
        }
    }

    @Override
    public void get(Request request, GetCallback callback) {
        callback.onGet(get(request));
    }
}
