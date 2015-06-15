package com.lucascauthen.uschat.Data.Firebase;

import com.lucascauthen.uschat.Data.Callbacks.GetDataCallback;
import com.lucascauthen.uschat.Data.Callbacks.LogInCallback;
import com.lucascauthen.uschat.Data.Callbacks.LogOutCallback;
import com.lucascauthen.uschat.Data.Callbacks.SignUpCallback;
import com.lucascauthen.uschat.Data.DataObject;
import com.lucascauthen.uschat.Data.Exceptions.DataException;
import com.lucascauthen.uschat.Data.UserObject;

import java.util.List;

/**
 * Created by lhc on 6/15/15.
 */
public class FirebaseUserObject implements UserObject{
    private String username;
    private String password;
    private String email;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public UserObject login(String username, String password) throws DataException{
        return null;
    }

    @Override
    public void logInAsync(String username, String password, LogInCallback callback) {

    }

    @Override
    public void logOut() {

    }

    @Override
    public void logOutAsync() {

    }

    @Override
    public void logOutAsync(LogOutCallback callback) {

    }

    @Override
    public void signUp(String username, String email, String password) {

    }

    @Override
    public void signUpAsync(String username, String email, String password) {

    }

    @Override
    public void signUpAsync(String username, String email, String password, SignUpCallback callback) {

    }

    @Override
    public List<DataObject> getUserData() {
        return null;
    }

    @Override
    public List<DataObject> getUserDataAsync(GetDataCallback callback) {
        return null;
    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }
}
