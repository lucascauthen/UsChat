package com.lucascauthen.uschat.data.net.Parse;

import com.lucascauthen.uschat.data.net.Callbacks.GetObjectCallback;
import com.lucascauthen.uschat.data.net.Callbacks.LogInCallback;
import com.lucascauthen.uschat.data.net.Callbacks.LogOutCallback;
import com.lucascauthen.uschat.data.net.Callbacks.SignUpCallback;
import com.lucascauthen.uschat.data.net.DataObject;
import com.lucascauthen.uschat.data.net.Exceptions.DataException;
import com.lucascauthen.uschat.data.net.UserObject;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Set;

/**
 * Created by lhc on 6/15/15.
 */
public class ParseUserObject implements UserObject {
    private String username;
    private String password;
    private String email;
    private boolean isLoggedIn = false;

    public ParseUserObject() {
        //Assume already initialized
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
        if (ParseUser.getCurrentUser() != null) {
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
                if (e != null) {
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
    public boolean isLoggedIn() {
        if (ParseUser.getCurrentUser() != null) {
            return true;
        }
        return false;
    }

    @Override
    public void put(String key, Object value) {
        ParseUser.getCurrentUser().put(key, value);
    }

    @Override
    public String getParentKey() {
        return ParseUser.getCurrentUser().getClassName();
    }

    @Override
    public Object get(String key) {
        return ParseUser.getCurrentUser().get(key);
    }

    @Override
    public String getString(String key) {
        return ParseUser.getCurrentUser().getString(key);
    }

    @Override
    public int getInt(String key) {
        return ParseUser.getCurrentUser().getInt(key);
    }

    @Override
    public boolean getBoolean(String key) {
        return ParseUser.getCurrentUser().getBoolean(key);
    }

    @Override
    public float getFloat(String key) {
        return (float) getDouble(key);
    }

    @Override
    public double getDouble(String key) {
        return ParseUser.getCurrentUser().getDouble(key);
    }

    @Override
    public DataObject child(String key) throws DataException {
        try {
            ParseObject parseObject = ParseUser.getCurrentUser().getParseObject(key).fetchIfNeeded();
            return ParseNetworkInterface.convertObjectFromParse(parseObject);
        } catch (ParseException e) {
            throw new DataException(e.getCode(), e.getMessage());
        }
    }

    public void child(String key, final GetObjectCallback callback) {
        ParseUser.getCurrentUser().getParseObject(key).fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e != null) {
                    //Failure
                    callback.done(null, new DataException(e.getCode(), e.getMessage()));
                } else {
                    //Success
                    callback.done(ParseNetworkInterface.convertObjectFromParse(parseObject), null);
                }
            }
        });
    }

    @Override
    public Set<String> getKeySet() {
        return ParseUser.getCurrentUser().keySet();
    }
}
