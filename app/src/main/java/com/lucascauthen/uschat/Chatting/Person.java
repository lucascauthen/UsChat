package com.lucascauthen.uschat.Chatting;

/**
 * Created by lhc on 6/10/15.
 */
public class Person {
    private final String name;
    private final String friendshipStatus;
    public Person(String name, String friendshipStatus) {
        this.name = name;
        this. friendshipStatus = friendshipStatus;
    }
    public Person(String name) {
        this(name, "notFriends");
    }
    public String getName() {
        return name;
    }
    public String getFriendshipStatus() {
        return friendshipStatus;
    }
}
