package com.lucascauthen.uschat.data.entities;

import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhc on 6/10/15.
 */
public class Person {
    private final String name;
    private String requestFrom;

    private FriendshipStatus friendshipStatus;

    private List<String> friends = null;

    private static Person currentUser = null;

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
        if (friendshipStatus != null) {
            return friendshipStatus;
        }
        return FriendshipStatus.NOT_FRIENDS;
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
        NOT_FRIENDS {
            public String toString() {
                return "Not Friends";
            }
        };

        public abstract String toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != Person.class) {
            return false;
        } else if (!((Person) o).getName().equals(this.getName())) {
            return false;
        } else {
            return ((Person) o).getFriendshipStatus() == this.getFriendshipStatus();
        }
    }

    @Override
    public String toString() {
        return this.getName();
    }

    public boolean isFriend() {
        return friendshipStatus == FriendshipStatus.FRIENDS;
    }

    public boolean isFriend(String name) {
        if (friends != null) {
            for (String person : friends) {
                if (person.equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> getFriends() {
        return friends;
    }

    public static Person getCurrentUser() {
        ParseUser user = ParseUser.getCurrentUser();
        if (currentUser == null) {
            currentUser = new Person(user.getUsername());
        }
        currentUser.friends = user.getList("friends");
        return currentUser;
    }

    public static void refreshCurrentUser() {
        ParseUser user = null;
        try {
            user = ParseUser.getCurrentUser().fetchIfNeeded();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (user != null) {
            if (currentUser == null) {
                currentUser = new Person(user.getUsername());
            }
            currentUser.friends = user.getList("friends");
        }
    }

    public void removeFriend(String name) {
        if (friends != null) {
            friends.remove(name);
        }
    }

    public void addFriend(String name) {
        if (friends != null) {
            friends.add(name);
        }
    }
}
