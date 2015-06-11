package com.lucascauthen.uschat.Chatting;

/**
 * Created by lhc on 6/11/15.
 */
public class Friendship {
    private final String name;
    private final FriendshipStatus status;

    public String getName() {
        return name;
    }

    public FriendshipStatus getStatus() {
        return status;
    }


    public Friendship(String name, FriendshipStatus status) {
        this.name = name;
        this.status = status;
    }
}
