package com.lucascauthen.uschat.Chatting;

/**
 * Created by lhc on 6/11/15.
 */
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
