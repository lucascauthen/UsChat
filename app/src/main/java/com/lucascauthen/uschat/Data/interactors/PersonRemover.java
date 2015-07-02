package com.lucascauthen.uschat.data.interactors;

import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.data.repository.Repo;

/**
 * Created by lhc on 6/25/15.
 */
public class PersonRemover {
    private final Repo<Person> repo;

    public PersonRemover(Repo<Person> repo) {
        this.repo = repo;
    }

    public Person execute(Person favorite) {
        repo.remove(favorite);
        return favorite;
    }
}
