package com.lucascauthen.uschat.data.repository.chat;

import com.lucascauthen.uschat.data.entities.Chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CachedChatRepo implements ChatCache {
    private List<Chat> sentChats = new ArrayList<>();

    private List<Chat> receivedChats = new ArrayList<>();

    @Override
    public void cacheSentChat(Chat chat) {
        sentChats.add(chat);
    }

    @Override
    public void cacheReceivedChat(Chat chat) {
        receivedChats.add(chat);
    }

    @Override
    public void cacheSentChats(List<Chat> chats) {
        if (chats != null) {
            sentChats = chats;
        } else {
            sentChats.clear();
        }
    }

    @Override
    public void cacheReceivedChats(List<Chat> chats) {
        if (chats != null) {
            receivedChats = chats;
        } else {
            sentChats.clear();
        }
    }

    @Override
    public void cache(Response response) {
        switch (response.requestType()) {
            case SENT:
                cacheSentChats(response.result());
                break;
            case RECEIVED:
                cacheReceivedChats(response.result());
                break;
            case UNKNOWN:
                clear();
                for (Chat chat : response.result()) {
                    if (chat.isFromCurrentUser()) {
                        sentChats.add(chat);
                    } else {
                        receivedChats.add(chat);
                    }
                }
                break;
            default:
                //empty
        }
    }

    @Override
    public void clear() {
        sentChats.clear();
        receivedChats.clear();
    }

    @Override
    public void sendChat(Chat chat, OnCompleteAction callback) {
        sentChats.add(chat);
    }

    @Override
    public void sendChat(Chat chat, OnCompleteAction callback, boolean waitForRemote) {
        sendChat(chat, callback);
    }

    @Override
    public void openChat(Chat chat, OnCompleteAction callback) {
        receivedChats.remove(chat);
        if(callback != null) {
            callback.onComplete("");
        }
    }

    @Override
    public void openChat(Chat chat, OnCompleteAction callback, boolean waitForRemote) {

    }

    @Override
    public Response get(Request request) {
        Response response;
        List<Chat> listToSend = null;
        switch (request.requestType()) {
            case SENT:
                listToSend = Collections.unmodifiableList(sentChats);
                break;
            case RECEIVED:
                listToSend = Collections.unmodifiableList(receivedChats);
                break;
            case UNKNOWN:
                listToSend = new ArrayList<>(sentChats);
                listToSend.addAll(receivedChats);
                listToSend = Collections.unmodifiableList(listToSend);
                break;
            default:
                //EMPTY
        }
        return new Response(listToSend, request.requestType());
    }

    @Override
    public void get(Request request, GetCallback callback) {
        callback.onGet(get(request));
    }
}
