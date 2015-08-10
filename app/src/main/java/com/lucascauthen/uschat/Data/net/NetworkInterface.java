package com.lucascauthen.uschat.data.net;

import com.lucascauthen.uschat.data.net.Callbacks.DeleteDataCallback;
import com.lucascauthen.uschat.data.net.Callbacks.GetDataCallback;
import com.lucascauthen.uschat.data.net.Callbacks.SaveDataCallback;
import com.lucascauthen.uschat.data.net.Callbacks.UpdateDataCallback;
import com.lucascauthen.uschat.data.net.Exceptions.DataException;

import java.util.List;

/**
 * Created by lhc on 6/15/15.
 */
public interface NetworkInterface {

    boolean init();

    //Gets specific objects from the cloud server
    List<DataObject> getData(DataQuery params) throws DataException;

    void getDataAsync(DataQuery params, GetDataCallback callback);

    //Updates a given DataObject from the cloud
    void updateData(DataObject object) throws DataException;

    void updateDataAsync(DataObject object);

    void updateDataAsync(DataObject object, UpdateDataCallback callback);

    //Saves a given DataObject on to the cloud
    void saveData(DataObject data) throws DataException;

    void saveDataAsync(DataObject data);

    void saveDataAsync(DataObject data, SaveDataCallback callback);

    //Deletes a given DataObject from the cloud
    void delete(DataObject data) throws DataException;

    void deleteAsync(DataObject data);

    void deleteAsync(DataObject data, DeleteDataCallback callback);


    DataQuery newDataQuery(Class c);

    DataObject newDataObject(String parentKey);
}
