package com.lucascauthen.uschat.Data;

import java.util.List;

/**
 * Created by lhc on 6/15/15.
 */
public interface DataObject {

    public void put(String key, Object value);

    public String getParentKey();

    public Object get(String key);
    public String getString(String key);
    public int getInt(String key);
    public boolean getBoolean(String key);
    public float getFloat(String key);
    public double getDouble(String key);

}
