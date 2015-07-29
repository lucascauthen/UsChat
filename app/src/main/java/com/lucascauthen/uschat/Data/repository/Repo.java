package com.lucascauthen.uschat.data.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by lhc on 6/25/15.
 */
public interface Repo<T> {

    void put(T item);

    Response<T> get(Request request);

    void remove(T item);

    boolean exists(T item);


    class Request {
        private final boolean skipCache;
        private List<String> conditions = new ArrayList<>();

        public Request(boolean skipCache, String... conditions) {
            this.skipCache = skipCache;
            for(String condition : conditions) {
                this.conditions.add(condition);
            }
        }
        public Request(boolean skipCache, List<String> conditions) {
            this.skipCache = skipCache;
            this.conditions = conditions;
        }

        public boolean skipCache() {
            return skipCache;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Request that = (Request) o;

            if (skipCache != that.skipCache) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return (skipCache ? 1 : 0);
        }

        public List<String> getConditions() {
            return conditions;
        }

    }

    class Response<T> {
        private List<T> responses;
        private boolean isCachedData;

        public Response(List<T> items, boolean isCachedData) {
            this.responses = items;
            this.isCachedData = isCachedData;
        }

        public boolean isCachedData() {
            return isCachedData;
        }

        public List<T> getValue() {
            return responses;
        }
    }
}
