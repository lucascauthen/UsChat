package com.lucascauthen.uschat.Data;

import com.lucascauthen.uschat.Data.Callbacks.GetDataCallback;
import com.lucascauthen.uschat.Data.Callbacks.LogInCallback;
import com.lucascauthen.uschat.Data.Callbacks.LogOutCallback;
import com.lucascauthen.uschat.Data.Callbacks.SignUpCallback;
import com.lucascauthen.uschat.Data.Exceptions.DataException;
import com.parse.ParseException;

import java.util.List;

/**
 * Created by lhc on 6/15/15.
 */

//This is the frame for the UserObject Singleton
public interface UserObject {

    String getUsername();
    String getEmail();

    UserObject login(String username, String password) throws DataException;
    void logInAsync(String username, String password, LogInCallback callback);

    void logOut();
    void logOutAsync();
    void logOutAsync(LogOutCallback callback);

    void signUp(String username, String email, String password) throws DataException;
    void signUpAsync(String username, String email, String password);
    void signUpAsync(String username, String email, String password, SignUpCallback callback);

    List<DataObject> getUserData();
    List<DataObject> getUserDataAsync(GetDataCallback callback);

    boolean isLoggedIn();
}
