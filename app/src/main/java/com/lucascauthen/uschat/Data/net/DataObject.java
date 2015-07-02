package com.lucascauthen.uschat.data.net;

import com.lucascauthen.uschat.data.net.Exceptions.DataException;

import java.util.Set;

/**
 * Created by lhc on 6/15/15.
 */
public interface DataObject {

    void put(String key, Object value);

    String getParentKey();

    Object get(String key);
    String getString(String key);
    int getInt(String key);
    boolean getBoolean(String key);
    float getFloat(String key);
    double getDouble(String key);

    //These are special because the object can be loaded or not loaded so you might not want to run it on the UI thread
    DataObject child(String key) throws DataException;

    Set<String> getKeySet();

}
