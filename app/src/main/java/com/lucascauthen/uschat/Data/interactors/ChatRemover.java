package com.lucascauthen.uschat.data.interactors;

import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.data.repository.Repo;

/**
 * Created by lhc on 6/25/15.
 */
public class ChatRemover {
    private final Repo<Chat> repo;

    public ChatRemover(Repo<Chat> repo) {
        this.repo = repo;
    }

    public Chat execute(Chat item) {
        repo.remove(item);
        return item;
    }
}
