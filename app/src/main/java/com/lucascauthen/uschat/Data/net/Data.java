package com.lucascauthen.uschat.data.net;

import com.lucascauthen.uschat.data.net.Exceptions.DataException;
import com.lucascauthen.uschat.data.net.Parse.ParseNetworkInterface;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by lhc on 6/17/15.
 */
public class Data implements DataObject {
    private String objectId;
    private String parentKey;
    private HashMap<String, Object> data = new HashMap<String, Object>();

    public Data(String parentKey) {
        this.parentKey = parentKey;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    @Override
    public void put(String key, Object value) {
        data.put(key, value);
    }

    @Override
    public String getParentKey() {
        return parentKey;
    }


    @Override
    public Object get(String key) {
        return data.get(key);
    }

    @Override
    public String getString(String key) {
        try {
            return (String) data.get(key);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getInt(String key) {
        try {
            return (int) data.get(key);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public boolean getBoolean(String key) {
        try {
            return (boolean) data.get(key);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public float getFloat(String key) {
        try {
            return (float) data.get(key);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public double getDouble(String key) {
        try {
            return (Double) data.get(key);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return 0;
        }
    }

    //WARNING: With the current system, if you query for an item in a table and edit it, saving an object that contains a relation to the edited object will not save the edits

    @Override
    public DataObject child(String key) throws DataException {
        try {
            ParseObject parseObject = ((ParseObject) data.get(key)).fetchIfNeeded();
            return ParseNetworkInterface.convertObjectFromParse(parseObject);
        } catch (ParseException e) {
            throw new DataException(e.getCode(), e.getMessage());
        }
    }

    @Override
    public Set<String> getKeySet() {
        return data.keySet();
    }
}
