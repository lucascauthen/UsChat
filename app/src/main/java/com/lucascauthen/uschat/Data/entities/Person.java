package com.lucascauthen.uschat.data.entities;

/**
 * Created by lhc on 6/10/15.
 */
public class Person {
    private final String name;
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
    }
    public Person(String name) {
        this.name = name;
    }
    public void setFriendShipStatus(FriendshipStatus status) {
        this.friendshipStatus = status;
    }
    public String getName() {
        return name;
    }
    public FriendshipStatus getFriendshipStatus() {
        if(friendshipStatus != null) {
            return friendshipStatus;
        }
        return FriendshipStatus.NOT_LOADED;
    }

    public String getFriendshipStatusText() {
        return getFriendshipStatus().toString();
    }
    public enum FriendshipStatus {
        FRIENDS {
            public String toString() {
                return "Friends";
            }
        },
        NOT_FRIENDS{
            public String toString() {
                return "Not Friends";
            }
        },
        REQUEST_SENT{
            public String toString() {
                return "Request Sent";
            }
        },
        REQUEST_RECIEVED{
            public String toString() {
                return "Pending Request";
            }
        },
        NOT_LOADED{
            public String toString() {
                return "Still loading friendship status...";
            }
        };
        public abstract String toString();
    }

}
