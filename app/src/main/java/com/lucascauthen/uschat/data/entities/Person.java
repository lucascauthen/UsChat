package com.lucascauthen.uschat.data.entities;

/**
 * Created by lhc on 8/5/15.
 */
public class Person {
    private final String name;
    private PersonState state;

    public Person(String name, PersonState state) {
        this.name = name;
        this.state = state;
    }

    public String name() {
        return name;
    }

    public PersonState state() {
        return state;
    }

    public void setState(PersonState state) {
        this.state = state;
    }

    public enum PersonState {
        FRIENDS,
        SENT_REQUEST,
        RECEIVED_REQUEST,
        NOT_FRIENDS
    }

}