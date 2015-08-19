package com.lucascauthen.uschat.data.entities;

public class Person {
    private final String name;
    private PersonType state;

    public Person(String name, PersonType state) {
        this.name = name;
        this.state = state;
    }

    public String name() {
        return name;
    }

    public PersonType state() {
        return state;
    }

    public void setState(PersonType state) {
        this.state = state;
    }

    public enum PersonType {
        NOT_FRIEND,
        FRIEND,
        SENT_REQUEST, //This person was sent a request
        RECEIVED_REQUEST, //The user received a request from this user
        SEARCH,
        REQUESTS
    }

}