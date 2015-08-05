package com.lucascauthen.uschat.data.repository.chat;

import com.lucascauthen.uschat.data.entities.Chat;

import java.util.List;

/**
 * Created by lhc on 8/4/15.
 */
public interface ChatCache extends ChatRepo{
    void cacheSentChat(Chat chat);

    void cacheReceivedChat(Chat chat);

    void cacheSentChats(List<Chat> chats);

    void cacheReceivedChats(List<Chat> chats);

    void cache(ChatRepo.Response response);

    void clear();
}
