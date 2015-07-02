package com.lucascauthen.uschat.data.repository.person;

import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.data.repository.CachingRepo;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by lhc on 6/25/15.
 */
public class InMemoryPersonRepo implements CachingRepo<Person> {
    private Set<Person> items = new LinkedHashSet<>();
    private boolean isStale;

    @Override
    public boolean isStale() {
        return isStale;
    }

    @Override
    public void setIsStale() {
        isStale = true;
    }

    @Override
    public void cache(Collection<Person> items) {
        this.items = new LinkedHashSet<>(items);
        isStale = false;
    }

    @Override
    public void put(Person item) {
        throw new RuntimeException("Not applicable");
    }

    @Override
    public Response<Person> get(Request request) {
        return new Response<>(Collections.unmodifiableCollection(items), true);
    }

    @Override
    public void remove(Person item) {
        throw new RuntimeException("Not applicable");
    }

    @Override
    public boolean exists(Person item) {
        return items.contains(item);
    }
}
