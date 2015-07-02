package com.lucascauthen.uschat.data.repository;

import java.util.Collection;

/**
 * Created by lhc on 6/25/15.
 */
public interface CachingRepo<T> extends Repo<T>{
    boolean isStale();
    void setIsStale();
    void cache(Collection<T> items);
}
