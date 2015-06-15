package com.lucascauthen.uschat.Data.Parse;

import com.lucascauthen.uschat.Data.DataQuery;
import com.lucascauthen.uschat.Data.UserObject;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lhc on 6/15/15.
 */
public class ParseDataQuery implements DataQuery {

    ParseQuery<ParseObject> mainObjectQuery;

    ParseQuery<ParseUser> mainUserQuery;

    private boolean isUserQuery = false;

    private String parent;

    public boolean isUserQuery() {
        return isUserQuery;
    }

    public ParseQuery<?> getQuery() {
        if(isUserQuery) {
            return mainUserQuery;
        } else if(!parent.equals("")) {
            return mainObjectQuery;
        }
        //TODO: See if this is the best thing to do
        return null;
    }

    @Override
    public DataQuery whereParentEquals(String parent) {
        this.parent = parent;
        this.mainObjectQuery = ParseQuery.getQuery(parent);
        return this;
    }

    @Override
    public DataQuery whereIsUserQuery() {
        this.isUserQuery = true;
        this.mainUserQuery = ParseUser.getQuery();
        return this;
    }
    public DataQuery whereEqualTo(String key, Object value) {
        getQuery().whereEqualTo(key, value);
        return this;
    }

    @Override
    public DataQuery whereNotEqualTo(String key, Object value) {
        getQuery().whereNotEqualTo(key, value);
        return this;
    }

    @Override
    public DataQuery whereGreaterThan(String key, Object value) {
        getQuery().whereGreaterThan(key, value);
        return this;
    }

    @Override
    public DataQuery whereLessThan(String key, Object value) {
        getQuery().whereLessThan(key, value);
        return this;
    }

    @Override
    public DataQuery whereLessThanOrEqualTo(String key, Object value) {
        getQuery().whereLessThanOrEqualTo(key, value);
        return this;
    }

    @Override
    public DataQuery whereGreaterThanOrEqualTo(String key, Object value) {
        getQuery().whereGreaterThanOrEqualTo(key, value);
        return this;
    }

    @Override
    public DataQuery whereStartsWith(String key, String str) {
        getQuery().whereStartsWith(key, str);
        return this;
    }

    @Override
    public DataQuery whereEndsWith(String key, String str) {
        getQuery().whereEndsWith(key, str);
        return this;
    }

    @Override
    public DataQuery or(DataQuery a, DataQuery b) {
        //TODO: Write this
        return null;
    }

    @Override
    public DataQuery setLimit(int num) {
        getQuery().setLimit(num);
        return this;
    }

    @Override
    public DataQuery setSkip(int num) {
        getQuery().setSkip(num);
        return this;
    }
}
