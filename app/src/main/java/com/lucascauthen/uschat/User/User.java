package com.lucascauthen.uschat.User;

/**
 * Created by lhc on 6/12/15.
 */
public class User {
    private String username;
    private String password;
    private String email;
    private static User user = null;


    public User getCurrentUser() {
        if(!user.equals(null)) {
            return user;
        } else {
            throw new IllegalAccessError("The current user is not set.");
        }
    }
    public void loginInBackGround() {

    }
}
