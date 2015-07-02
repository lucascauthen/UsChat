package com.lucascauthen.uschat.data.net.Parse;

import android.content.Context;
import android.support.annotation.NonNull;

import com.lucascauthen.uschat.data.net.Callbacks.DeleteDataCallback;
import com.lucascauthen.uschat.data.net.Callbacks.GetDataCallback;
import com.lucascauthen.uschat.data.net.Callbacks.SaveDataCallback;
import com.lucascauthen.uschat.data.net.Callbacks.UpdateDataCallback;
import com.lucascauthen.uschat.data.net.DataObject;

import com.lucascauthen.uschat.data.net.Exceptions.DataException;
import com.lucascauthen.uschat.data.net.DataQuery;
import com.lucascauthen.uschat.data.net.NetworkInterface;
import com.lucascauthen.uschat.data.net.UserObject;
import com.lucascauthen.uschat.R;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhc on 6/15/15.
 */
public class ParseNetworkInterface implements NetworkInterface {

    private final Context context;

    public ParseNetworkInterface(Context context) {
        this.context = context;
        init();
    }

    @Override
    public boolean init() {
        Parse.initialize(context, context.getString(R.string.parse_app_id), context.getString(R.string.parse_client_key));
        return true;
    }

    @Override
    public List<DataObject> getData(DataQuery params) throws DataException {
        ParseDataQuery query = (ParseDataQuery) params;
        List<ParseObject> list = null;
        try {
            list = (List<ParseObject>)query.getQuery().find();
        } catch (ParseException e) {
            throw new DataException(e.getCode(), e.getMessage());
        }
        return convertObjectsFromParse(list);
    }

    @Override
    public void getDataAsync(DataQuery params, final GetDataCallback callback) {
        ParseDataQuery query = (ParseDataQuery) params;
        ((ParseQuery<ParseObject>)query.getQuery()).findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    //Failed
                    callback.done(convertObjectsFromParse(list), new DataException(e.getCode(), e.getMessage()));
                } else {
                    //Success
                    callback.done(convertObjectsFromParse(list), null);
                }
            }
        });

    }

    @Override
    public void updateData(DataObject object) throws DataException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(object.getParentKey());
        try {
            query.get(((ParseDataObject)object).getObjectId());
        } catch (ParseException e) {
            throw new DataException(e.getCode(), e.getMessage());
        }
    }

    @Override
    public void updateDataAsync(DataObject object) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(object.getParentKey());
        query.getInBackground(((ParseDataObject) object).getObjectId());
    }

    @Override
    public void updateDataAsync(DataObject object, final UpdateDataCallback callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(object.getParentKey());
        query.getInBackground(((ParseDataObject) object).getObjectId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e != null) {
                    //Failed
                    callback.done(new DataException(e.getCode(), e.getMessage()));
                } else {
                    //Success
                    callback.done(null);
                }
            }
        });
    }

    @Override
    public void saveData(DataObject data) throws DataException {
        ParseObject object = convertObjectToParse((ParseDataObject) data);
        try {
            object.save();
        } catch (ParseException e) {
            throw new DataException(e.getCode(), e.getMessage());
        }
    }

    @Override
    public void saveDataAsync(DataObject data) {
        ParseObject object = convertObjectToParse((ParseDataObject) data);
        object.saveInBackground();
    }

    @Override
    public void saveDataAsync(DataObject data, final SaveDataCallback callback) {
        ParseObject object = convertObjectToParse((ParseDataObject) data);
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    //Failed
                    callback.done(new DataException(e.getCode(), e.getMessage()));
                } else {
                    //Success
                    callback.done(null);
                }
            }
        });
    }

    @Override
    public void delete(DataObject data) throws DataException {
        ParseObject object = convertObjectToParse((ParseDataObject) data);
        try {
            object.delete();
        } catch (ParseException e) {
            throw new DataException(e.getCode(), e.getMessage());
        }
    }

    @Override
    public void deleteAsync(DataObject data) {
        ParseObject object = convertObjectToParse((ParseDataObject) data);
        object.deleteInBackground();
    }

    @Override
    public void deleteAsync(DataObject data, final DeleteDataCallback callback) {
        ParseObject object = convertObjectToParse((ParseDataObject) data);
        object.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null) {
                    //Failed
                    callback.done(new DataException(e.getCode(), e.getMessage()));
                } else {
                    //Success
                    callback.done(null);
                }
            }
        });
    }

    @Override
    public DataQuery newDataQuery(Class c) {
        if(c == UserObject.class) {
            return new ParseDataQuery<ParseUser>();
        } else if(c == DataObject.class) {
            return new ParseDataQuery<ParseObject>();
        } else {
            throw new IllegalArgumentException("DataQuery's can only be of class type UserObject or DataObject");
        }
    }

    @Override
    public DataObject newDataObject(String parentKey) {
        return new ParseDataObject(parentKey);
    }



    public static List<DataObject> convertObjectsFromParse(@NonNull List<ParseObject> list){
        List<DataObject> newList = new ArrayList<DataObject>();
        for (ParseObject item : list) {
            ParseDataObject data = (ParseDataObject)convertObjectFromParse(item);
            newList.add(data);
        }
        return newList;
    }
    public static DataObject convertObjectFromParse(@NonNull ParseObject item) {
        ParseDataObject data = new ParseDataObject(item.getClassName());
        for (String key : item.keySet()) {
            data.put(key, item.get(key));
            data.setObjectId(item.getObjectId());
        }
        return data;

    }
    public static ParseObject convertObjectToParse(@NonNull ParseDataObject data) {
        ParseObject object = ParseObject.create(data.getParentKey());
        for(String key : data.getKeySet()) {
            if(data.get(key) instanceof ParseDataObject) {
                object.put(key, convertObjectToParse((ParseDataObject)data.get(key)));
            } else {
                object.put(key, data.get(key));
            }
        }
        object.setObjectId(data.getObjectId());
        return object;
    }
}
