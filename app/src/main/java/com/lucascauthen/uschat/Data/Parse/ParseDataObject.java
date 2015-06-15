package com.lucascauthen.uschat.Data.Parse;

import com.lucascauthen.uschat.Data.DataObject;

/**
 * Created by lhc on 6/15/15.
 */
public class ParseDataObject implements DataObject {
    private String objectId;
    @Override
    public void put(String key, Object value) {

    }

    @Override
    public String getParentKey() {
        return null;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    @Override
    public Object get(String key) {
        return null;
    }

    @Override
    public String getString(String key) {
        return null;
    }

    @Override
    public int getInt(String key) {
        return 0;
    }

    @Override
    public boolean getBoolean(String key) {
        return false;
    }

    @Override
    public float getFloat(String key) {
        return 0;
    }

    @Override
    public double getDouble(String key) {
        return 0;
    }
}
