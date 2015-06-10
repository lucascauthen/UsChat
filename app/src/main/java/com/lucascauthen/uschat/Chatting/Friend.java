package com.lucascauthen.uschat.Chatting;

import android.graphics.Bitmap;

/**
 * Created by lhc on 6/10/15.
 */
public class Friend {
    private final String name;
    private final String friendshipStatus;


    //TODO: Add field for the friend's image and other friend related information

    public Friend(String name, String friendshipStatus) {
        this.name = name;
        this. friendshipStatus = friendshipStatus;
    }
    public String getName() {
        return name;
    }
    public String getFriendshipStatus() {
        return friendshipStatus;
    }

}
