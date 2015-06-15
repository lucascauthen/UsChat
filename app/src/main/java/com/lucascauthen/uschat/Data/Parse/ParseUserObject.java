package com.lucascauthen.uschat.Data.Parse;

import android.content.Context;

import com.lucascauthen.uschat.Data.Callbacks.GetDataCallback;
import com.lucascauthen.uschat.Data.Callbacks.LogInCallback;
import com.lucascauthen.uschat.Data.Callbacks.LogOutCallback;
import com.lucascauthen.uschat.Data.Callbacks.SignUpCallback;
import com.lucascauthen.uschat.Data.DataObject;
import com.lucascauthen.uschat.Data.DataQuery;
import com.lucascauthen.uschat.Data.Exceptions.DataException;
import com.lucascauthen.uschat.Data.NetworkInterface;
import com.lucascauthen.uschat.Data.UserObject;
import com.lucascauthen.uschat.Events.Task;
import com.lucascauthen.uschat.Events.TaskListener;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by lhc on 6/15/15.
 */
public class ParseUserObject implements UserObject{
    private String username;
    private String password;
    private String email;
    private boolean isLoggedIn = false;
    private final NetworkInterface networkInterface;

    public ParseUserObject(NetworkInterface networkInterface) {
        //Assume already initialized
        this.networkInterface = networkInterface;
    }
    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public UserObject login(String username, String password) throws DataException {
        if(ParseUser.getCurrentUser() != null) {
            logOut();
        }
        try {
            ParseUser.logIn(username, password);
            return this;
        } catch (ParseException e) {
            throw new DataException(e.getCode(), e.getMessage());
        }
    }

    @Override
    public void logInAsync(String username, String password, final LogInCallback callback) {
        final ParseUserObject obj = this;
        ParseUser.logInInBackground(username, password, new com.parse.LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if(e != null) {
                    //Login failed
                    callback.done(obj, new DataException(e.getCode(), e.getMessage()));
                } else {
                    //login success
                    callback.done(obj, null);
                }
            }
        });
    }

    @Override
    public void logOut() {
        ParseUser.logOut();
    }

    @Override
    public void logOutAsync() {
        ParseUser.logOutInBackground();
    }

    @Override
    public void logOutAsync(final LogOutCallback callback) {
        ParseUser.logOutInBackground(new com.parse.LogOutCallback() {
            @Override
            public void done(ParseException e) {
                callback.done(new DataException(e.getCode(), e.getMessage()));
            }
        });
    }

    @Override
    public void signUp(String username, String email, String password) throws DataException {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        try {
            user.signUp();
        } catch (ParseException e) {
            throw new DataException(e.getCode(), e.getMessage());
        }
    }

    @Override
    public void signUpAsync(String username, String email, String password) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.signUpInBackground();
    }

    @Override
    public void signUpAsync(String username, String email, String password, final SignUpCallback callback) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.signUpInBackground(new com.parse.SignUpCallback() {
            @Override
            public void done(ParseException e) {
                callback.done(new DataException(e.getCode(), e.getMessage()));
            }
        });
    }

    @Override
    public List<DataObject> getUserData() {
        //TODO: see if this is a nessecary function
        return null;
    }

    @Override
    public List<DataObject> getUserDataAsync(GetDataCallback callback) {
        return null;
    }

    @Override
    public boolean isLoggedIn() {
        if(ParseUser.getCurrentUser() != null) {
            return true;
        }
        return false;
    }
}
