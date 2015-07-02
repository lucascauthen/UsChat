package com.lucascauthen.uschat.data.interactors;

import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.data.repository.Repo;

/**
 * Created by lhc on 6/25/15.
 */
public class PersonGetter {
    private final Repo<Person> repo;

    public PersonGetter(Repo<Person> repo) {
        this.repo = repo;
    }

    public Repo.Response<Person> execute(Repo.Request request) {
        return repo.get(request);
    }
}
