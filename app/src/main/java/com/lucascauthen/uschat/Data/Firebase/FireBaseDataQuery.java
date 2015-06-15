package com.lucascauthen.uschat.Data.Firebase;

import com.lucascauthen.uschat.Data.DataQuery;
import com.lucascauthen.uschat.Data.UserObject;

/**
 * Created by lhc on 6/15/15.
 */
public class FirebaseDataQuery implements DataQuery {

    @Override
    public DataQuery whereParentEquals(String parent) {
        return null;
    }

    @Override
    public DataQuery whereIsUserQuery() {
        return null;
    }

    @Override
    public DataQuery whereEqualTo(String key, Object value) {
        return null;
    }

    @Override
    public DataQuery whereNotEqualTo(String key, Object value) {
        return null;
    }

    @Override
    public DataQuery whereGreaterThan(String key, Object value) {
        return null;
    }

    @Override
    public DataQuery whereLessThan(String key, Object value) {
        return null;
    }

    @Override
    public DataQuery whereLessThanOrEqualTo(String key, Object value) {
        return null;
    }

    @Override
    public DataQuery whereGreaterThanOrEqualTo(String key, Object value) {
        return null;
    }

    @Override
    public DataQuery whereStartsWith(String key, String str) {
        return null;
    }

    @Override
    public DataQuery whereEndsWith(String key, String str) {
        return null;
    }

    @Override
    public DataQuery or(DataQuery a, DataQuery b) {
        return null;
    }

    @Override
    public DataQuery setLimit(int num) {
        return null;
    }

    @Override
    public DataQuery setSkip(int num) {
        return null;
    }
}
