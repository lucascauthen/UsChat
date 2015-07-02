package com.lucascauthen.uschat.data.net.Parse;

import com.lucascauthen.uschat.data.net.Callbacks.GetObjectCallback;
import com.lucascauthen.uschat.data.net.Data;
import com.lucascauthen.uschat.data.net.DataObject;

import com.lucascauthen.uschat.data.net.Exceptions.DataException;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.Set;

/**
 * Created by lhc on 6/15/15.
 */
public class ParseDataObject implements DataObject {
    private String objectId;
    private String parentKey;
    private final Data data;

    public ParseDataObject(String parentKey) {
        this.parentKey = parentKey;
        this.data = new Data(parentKey);
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
        return data.getString(key);
    }

    @Override
    public int getInt(String key) {
        return data.getInt(key);
    }

    @Override
    public boolean getBoolean(String key) {
        return data.getBoolean(key);
    }

    @Override
    public float getFloat(String key) {
        return data.getFloat(key);
    }

    @Override
    public double getDouble(String key) {
        return data.getDouble(key);
    }

    //WARNING: With the current system, if you query for an item in a table and edit it, saving an object that contains a relation to the edited object will not save the edits

    @Override
    public DataObject child(String key) throws DataException {
        try {
            ParseObject parseObject = ((ParseObject)data.get(key)).fetchIfNeeded();
            return ParseNetworkInterface.convertObjectFromParse(parseObject);
        } catch (ParseException e) {
            throw new DataException(e.getCode(), e.getMessage());
        }
    }

    public void child(String key, final GetObjectCallback callback) {
        ((ParseObject)data.get(key)).fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e != null) {
                    //Failure
                    callback.done(null, new DataException(e.getCode(), e.getMessage()));
                } else {
                    //Success
                    callback.done(ParseNetworkInterface.convertObjectFromParse(parseObject), null);
                }
            }
        });
    }

    @Override
    public Set<String> getKeySet() {
        return data.getKeySet();
    }
}
