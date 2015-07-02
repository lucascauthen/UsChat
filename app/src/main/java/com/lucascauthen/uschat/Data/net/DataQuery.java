package com.lucascauthen.uschat.data.net;

/**
 * Created by lhc on 6/15/15.
 */

public interface DataQuery {


    //One of these two are required to have a successful query
    DataQuery whereParentEquals(String parent);
    DataQuery whereIsUserQuery();

    DataQuery whereEqualTo(String key, Object value);
    DataQuery whereNotEqualTo(String key, Object value);

    DataQuery whereGreaterThan(String key, Object value);
    DataQuery whereLessThan(String key, Object value);
    DataQuery whereLessThanOrEqualTo(String key, Object value);
    DataQuery whereGreaterThanOrEqualTo(String key, Object value);

    DataQuery whereStartsWith(String key, String str);
    DataQuery whereEndsWith(String key, String str);

    DataQuery whereOr(DataQuery other);

    DataQuery setLimit(int num);
    DataQuery setSkip(int num);

    boolean isValid();



}
