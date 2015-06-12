package com.lucascauthen.uschat.Chatting;

/**
 * Created by lhc on 6/11/15.
 */
public class Friendship {
    private final String name;
    private final FriendshipStatus status;
    private final String from;

    public String getName() {
        return name;
    }

    public FriendshipStatus getStatus() {
        return status;
    }


    public String getFrom() {
        return from;
    }

    public Friendship(String name, String from, FriendshipStatus status) {
        this.name = name;
        this.status = status;
        this.from = from;
    }
}
