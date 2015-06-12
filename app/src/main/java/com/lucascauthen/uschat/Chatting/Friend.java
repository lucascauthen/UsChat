package com.lucascauthen.uschat.Chatting;

import android.graphics.Bitmap;

/**
 * Created by lhc on 6/10/15.
 */
public class Friend {
    private final String name;
    private FriendshipStatus friendshipStatus;
    private boolean isStatusLoaded;

    //TODO: Add field for the friend's image and other friend related information

    public Friend(String name, FriendshipStatus friendshipStatus) {
        this.name = name;
        this. friendshipStatus = friendshipStatus;
        isStatusLoaded = true;
    }
    public Friend(String name) {
        this.name = name;
    }
    public void setFriendShipStatus(FriendshipStatus status) {
        this.friendshipStatus = status;
        isStatusLoaded= true;
    }
    public String getName() {
        return name;
    }
    public boolean getIsStatusLoaded() {
        return isStatusLoaded;
    }
    public FriendshipStatus getFriendshipStatus() {
        return this.friendshipStatus;
    }
    public String getFriendshipStatusString() {
        if(isStatusLoaded) {
            return friendshipStatus.toString();
        } else {
            return "Loading status...";
        }
    }

}
