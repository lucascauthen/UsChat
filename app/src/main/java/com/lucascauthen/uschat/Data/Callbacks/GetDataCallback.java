package com.lucascauthen.uschat.Data.Callbacks;

import com.lucascauthen.uschat.Data.DataObject;
import com.lucascauthen.uschat.Data.Exceptions.DataException;

import java.util.List;

/**
 * Created by lhc on 6/15/15.
 */
public interface GetDataCallback {
    void done(List<DataObject> data, DataException e);
}
