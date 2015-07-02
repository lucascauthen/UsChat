package com.lucascauthen.uschat.data.net.Callbacks;

import com.lucascauthen.uschat.data.net.Exceptions.DataException;
import com.lucascauthen.uschat.data.net.UserObject;

/**
 * Created by lhc on 6/15/15.
 */
public interface LogInCallback {
    void done(UserObject user, DataException e);
}
