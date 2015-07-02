package com.lucascauthen.uschat.data.interactors;

import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.data.repository.Repo;

/**
 * Created by lhc on 6/25/15.
 */
public class ChatGetter {
    private final Repo<Chat> repo;

    public ChatGetter(Repo<Chat> repo) {
        this.repo = repo;
    }

    public Repo.Response<Chat> execute(Repo.Request request) {
        return repo.get(request);
    }
}
