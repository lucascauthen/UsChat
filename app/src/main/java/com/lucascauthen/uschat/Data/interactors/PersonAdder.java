package com.lucascauthen.uschat.data.interactors;

import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.data.repository.Repo;

/**
 * Created by lhc on 6/25/15.
 */
public class PersonAdder {
    private final Repo<Person> repo;

    public PersonAdder(Repo<Person> repo) {
        this.repo = repo;
    }

    public Response execute(Person person) {
        boolean alreadyExisted = repo.exists(person);
        if(!alreadyExisted) {
            repo.put(person);
        }
        return new Response(person, alreadyExisted);
    }

    public static class Response {
        private final Person person;
        private  final boolean alreadyExisted;
        public Response(Person person, boolean alreadyExisted) {
            this.person = person;
            this.alreadyExisted = alreadyExisted;
        }

        boolean alreadyExisted() {
            return alreadyExisted;
        }

        public Person getPerson() {
            return this.person;
        }
    }
}
