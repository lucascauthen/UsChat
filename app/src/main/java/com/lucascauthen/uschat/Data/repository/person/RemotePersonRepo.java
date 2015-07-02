package com.lucascauthen.uschat.data.repository.person;

import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.data.repository.Repo;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by lhc on 6/25/15.
 */
public class RemotePersonRepo implements Repo<Person> {
    private Set<Person> items = new LinkedHashSet<>();
    @Override
    public void put(Person item) {
        //TODO: Add network call to update list
        items.add(item);
    }

    @Override
    public Response<Person> get(Request request) {
        //TODO: Add network call to update list
        return new Response<>(Collections.unmodifiableCollection(items), false);
    }

    @Override
    public void remove(Person item) {
        //TODO: Add network call to update list
        items.remove(item);
    }

    @Override
    public boolean exists(Person item) {
        return items.contains(item);
    }
}
