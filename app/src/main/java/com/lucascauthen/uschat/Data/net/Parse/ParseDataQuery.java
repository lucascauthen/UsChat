package com.lucascauthen.uschat.data.net.Parse;

import com.lucascauthen.uschat.data.net.DataQuery;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhc on 6/15/15.
 */
public class ParseDataQuery<T extends ParseObject> implements DataQuery {

    ParseQuery<T> mainQuery;


    private boolean isUserQuery = false;

    private String parentKey = "";

    public boolean isUserQuery() {
        return isUserQuery;
    }

    public ParseQuery<T> getQuery() {
        return mainQuery;
    }

    @Override
    public DataQuery whereParentEquals(String parent) {
        this.parentKey = parent;
        this.mainQuery = ParseQuery.getQuery(parent);
        return this;
    }

    @Override
    public DataQuery whereIsUserQuery() {
        this.isUserQuery = true;
        this.mainQuery = (ParseQuery<T>) ParseQuery.getQuery(ParseUser.class);
        return this;
    }
    @Override
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
    public DataQuery whereOr(DataQuery other) {
        ParseQuery<T> beforeQuery = mainQuery;
        ParseQuery<T> otherQuery = ((ParseDataQuery<T>)other).getQuery();
        List<ParseQuery<T>> queries = new ArrayList<ParseQuery<T>>();
        queries.add(beforeQuery);
        queries.add(otherQuery);
        mainQuery = ParseQuery.or(queries);
        return this;
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

    @Override
    public boolean isValid() {
        if(!parentKey.equals("")) {
            return true;
        }
        return false;
    }
}
