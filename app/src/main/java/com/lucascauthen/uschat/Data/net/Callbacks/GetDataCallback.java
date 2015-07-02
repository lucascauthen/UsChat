package com.lucascauthen.uschat.data.net.Callbacks;

import com.lucascauthen.uschat.data.net.DataObject;
import com.lucascauthen.uschat.data.net.Exceptions.DataException;

import java.util.List;

/**
 * Created by lhc on 6/15/15.
 */
public interface GetDataCallback {
    void done(List<DataObject> data, DataException e);
}
