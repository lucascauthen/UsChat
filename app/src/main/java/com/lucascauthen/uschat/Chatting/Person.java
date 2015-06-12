package com.lucascauthen.uschat.Chatting;

/**
 * Created by lhc on 6/10/15.
 */
public class Person {
    private final String name;
    private boolean isStatusLoaded = false;
    private String requestFrom;

    private FriendshipStatus friendshipStatus;

    public String getRequestFrom() {
        return requestFrom;
    }

    public void setRequestFrom(String requestFrom) {
        this.requestFrom = requestFrom;
    }

    public Person(String name, FriendshipStatus friendshipStatus) {
        this.name = name;
        this.friendshipStatus = friendshipStatus;
        isStatusLoaded = true;
    }
    public Person(String name) {
        this.name = name;
    }
    public void setFriendShipStatus(FriendshipStatus status) {
        this.friendshipStatus = status;
        isStatusLoaded= true;
    }
    public boolean getIsStatusLoaded() {
        return isStatusLoaded;
    }
    public String getName() {
        return name;
    }
    public FriendshipStatus getFriendshipStatus() {
        if(isStatusLoaded) {
            return friendshipStatus;
        }
        return FriendshipStatus.NOT_LOADED;
    }

    public String getFriendshipStatusText() {
        if(isStatusLoaded) {
            return friendshipStatus.toString();
        }
        return "Loading status...";
    }
}
