package com.lucascauthen.uschat.data.net.Callbacks;

import com.lucascauthen.uschat.data.net.Exceptions.DataException;

/**
 * Created by lhc on 6/15/15.
 */
public interface DeleteDataCallback {
    void done(DataException e);
}
