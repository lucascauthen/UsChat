package com.lucascauthen.uschat.data.repository.chat;

import com.lucascauthen.uschat.data.entities.Chat;

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
    public void sendChat(Chat chat, OnCompleteAction callback, boolean waitForRemote) {
        //WARNING: Not tested and it has the potential to break
        if(waitForRemote) {
            sendChat(chat, callback);
        } else {
            OnCompleteAction cacheCallback = (msg) -> {
                chat.setId(msg);
            };
            cache.sendChat(chat, callback);
            secondaryRepo.sendChat(chat, cacheCallback);
        }
    }

    @Override
    public void sendChat(Chat chat, OnCompleteAction callback) {
        //The cached version of the chats does not contain a valid id, so it needs to be edited by the secondary repo when it is done
        OnCompleteAction cacheCallback = (msg) -> {
            chat.setId(msg);
            callback.onComplete(msg);
        };
        cache.sendChat(chat, null);
        secondaryRepo.sendChat(chat, cacheCallback);
    }

    @Override
    public void openChat(Chat chat, OnCompleteAction callback) {
        cache.openChat(chat, null); //This does not need a callback
        secondaryRepo.openChat(chat, callback);
    }

    @Override
    public void openChat(Chat chat, OnCompleteAction callback, boolean waitForRemote) {
        if (waitForRemote) {
            openChat(chat, callback);
        } else {
            cache.openChat(chat, callback); //This does not need a callback
            secondaryRepo.openChat(chat, null);
        }
    }

    @Override
    public Response get(Request request) {
        Response response;
        if (request.skipCache()) {
            response = secondaryRepo.get(request);
            cache.cache(response);
        } else {
            response = cache.get(request);
        }
        return response;
    }

    @Override
    public void get(Request request, GetCallback callback) {
        callback.onGet(get(request));
    }
}
