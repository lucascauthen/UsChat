package com.lucascauthen.uschat.data.net.Callbacks;

import com.lucascauthen.uschat.data.net.DataObject;
import com.lucascauthen.uschat.data.net.Exceptions.DataException;

/**
 * Created by lhc on 6/17/15.
 */
public interface GetObjectCallback {
    void done(DataObject object, DataException e);
}
