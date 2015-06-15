package com.lucascauthen.uschat.Data;

import com.lucascauthen.uschat.Data.Callbacks.DeleteDataCallback;
import com.lucascauthen.uschat.Data.Callbacks.GetDataCallback;
import com.lucascauthen.uschat.Data.Callbacks.SaveDataCallback;
import com.lucascauthen.uschat.Data.Callbacks.UpdateDataCallback;
import com.lucascauthen.uschat.Data.Exceptions.DataException;

import java.util.List;

/**
 * Created by lhc on 6/15/15.
 */
public interface NetworkInterface {

    boolean init();

    //Gets specific objects from the cloud server
    List<DataObject> getData(DataQuery params) throws DataException;
    void getDataAsync(DataQuery params, GetDataCallback callback);

    List<DataObject> getUsers(DataQuery params) throws DataException;
    void getUsersAsync(DataQuery params, GetDataCallback callback);

    //Updates a given DataObject from the cloud
    void updateData(DataObject object) throws DataException;
    void updateDataAsync(DataObject object);
    void updateDataAsync(DataObject object, UpdateDataCallback callback);

    //Saves a given DataObject on to the cloud
    void saveData(DataObject data);
    void saveDataAsync(DataObject data);
    void saveDataAsync(DataObject data, SaveDataCallback callback);

    //Deletes a given DataObject from the cloud
    void delete(DataObject data);
    void deleteAsync(DataObject data);
    void deleteAsync(DataObject data, DeleteDataCallback callback);


}
