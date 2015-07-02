package com.lucascauthen.uschat.data.net;

import com.lucascauthen.uschat.data.net.Callbacks.LogInCallback;
import com.lucascauthen.uschat.data.net.Callbacks.LogOutCallback;
import com.lucascauthen.uschat.data.net.Callbacks.SignUpCallback;
import com.lucascauthen.uschat.data.net.Exceptions.DataException;

/**
 * Created by lhc on 6/15/15.
 */

//This is the frame for the UserObject Singleton
public interface UserObject extends DataObject {

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


    boolean isLoggedIn();
}
