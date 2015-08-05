package com.lucascauthen.uschat.data.repository.chat;

import com.lucascauthen.uschat.data.entities.Chat;

/**
 * Created by lhc on 8/4/15.
 */
public class RemoteChatRepo implements ChatRepo {
    @Override
    public void sendChat(Chat chat) {
        //TODO
    }

    @Override
    public void openChat(Chat chat) {
        //TODO
    }

    @Override
    public Response get(Request request) {
        return null; //TODO
    }

    @Override
    public void get(Request request, GetCallback callback) {
        callback.onGet(get(request));
    }
}
