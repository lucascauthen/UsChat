package com.lucascauthen.uschat.Data.Callbacks;

import com.lucascauthen.uschat.Data.DataObject;
import com.lucascauthen.uschat.Data.Exceptions.DataException;

/**
 * Created by lhc on 6/15/15.
 */
public interface SaveDataCallback {
    void done(DataObject object, DataException e);
}
