package com.lucascauthen.uschat.Data.Firebase;

import com.lucascauthen.uschat.Data.Callbacks.DeleteDataCallback;
import com.lucascauthen.uschat.Data.Callbacks.GetDataCallback;
import com.lucascauthen.uschat.Data.Callbacks.SaveDataCallback;
import com.lucascauthen.uschat.Data.Callbacks.UpdateDataCallback;
import com.lucascauthen.uschat.Data.DataObject;
import com.lucascauthen.uschat.Data.DataQuery;
import com.lucascauthen.uschat.Data.NetworkInterface;
import com.parse.ParseException;

import java.util.List;

/**
 * Created by lhc on 6/15/15.
 */
public class FirebaseNetworkInterface implements NetworkInterface {
    @Override
    public boolean init() {
        return false;
    }

    @Override
    public List<DataObject> getData(DataQuery params) {
        return null;
    }

    @Override
    public void getDataAsync(DataQuery params, GetDataCallback callback) {

    }

    @Override
    public List<DataObject> getUsers(DataQuery params) throws ParseException {
        return null;
    }

    @Override
    public void getUsersAsync(DataQuery params, GetDataCallback callback) {

    }

    @Override
    public void updateData(DataObject object) {

    }

    @Override
    public void updateDataAsync(DataObject object) {

    }

    @Override
    public void updateDataAsync(DataObject object, UpdateDataCallback callback) {

    }

    @Override
    public void saveData(DataObject data) {

    }

    @Override
    public void saveDataAsync(DataObject data) {

    }

    @Override
    public void saveDataAsync(DataObject data, SaveDataCallback callback) {

    }

    @Override
    public void delete(DataObject data) {

    }

    @Override
    public void deleteAsync(DataObject data) {

    }

    @Override
    public void deleteAsync(DataObject data, DeleteDataCallback callback) {

    }
}
