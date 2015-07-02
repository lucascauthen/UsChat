package com.lucascauthen.uschat.data.interactors;

import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.data.repository.Repo;

/**
 * Created by lhc on 6/25/15.
 */
public class ChatAdder {
    private final Repo<Chat> repo;

    public ChatAdder(Repo<Chat> repo) {
        this.repo = repo;
    }

    public Response execute(Chat chat) {
        boolean alreadyExisted = repo.exists(chat);
        if(!alreadyExisted) {
            repo.put(chat);
        }
        return new Response(chat, alreadyExisted);
    }

    public static class Response {
        private final Chat chat;
        private  final boolean alreadyExisted;
        public Response(Chat chat, boolean alreadyExisted) {
            this.chat = chat;
            this.alreadyExisted = alreadyExisted;
        }

        boolean alreadyExisted() {
            return alreadyExisted;
        }

        public Chat getChat() {
            return this.chat;
        }
    }
}
