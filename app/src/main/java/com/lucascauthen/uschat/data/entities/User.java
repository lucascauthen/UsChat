package com.lucascauthen.uschat.data.entities;

import com.lucascauthen.uschat.data.repository.user.PersonCache;
import com.lucascauthen.uschat.data.repository.user.PersonRepo;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by lhc on 6/10/15.
 */
public class User {
    private static String name = "Anonymous User";

    private final PersonCache cachingRepo;

    private final PersonRepo secondaryRepo;

    public User(PersonCache cache, PersonRepo secondaryRepo) {
        this.cachingRepo = cache;
        this.secondaryRepo = secondaryRepo;
    }

    public static void login(String name) {
        User.name = name;
    }

    public static String getName() {
        return name;
    }

    public void logout() {
        cachingRepo.clear();
    }

    @Override
    public String toString() {
        return this.getName();
    }

}
