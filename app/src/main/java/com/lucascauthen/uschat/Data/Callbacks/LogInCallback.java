package com.lucascauthen.uschat.Data.Callbacks;

import com.lucascauthen.uschat.Data.Exceptions.DataException;
import com.lucascauthen.uschat.Data.UserObject;

/**
 * Created by lhc on 6/15/15.
 */
public interface LogInCallback {
    void done(UserObject user, DataException e);
}
