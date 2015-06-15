package com.lucascauthen.uschat.Data.Callbacks;

import com.lucascauthen.uschat.Data.Exceptions.DataException;

/**
 * Created by lhc on 6/15/15.
 */
public interface LogOutCallback {
    void done(DataException e);
}
