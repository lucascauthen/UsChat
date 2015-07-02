package com.lucascauthen.uschat.data.repository;

import java.util.Collection;

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

        public Request(boolean skipCache) {
            this.skipCache = skipCache;
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
    }

    class Response<T> {
        private Collection<T> responses;
        private boolean isCachedData;

        public Response(Collection<T> chats, boolean isCachedData) {
            this.responses = chats;
            this.isCachedData = isCachedData;
        }

        public boolean isCachedData() {
            return isCachedData;
        }

        public Collection<T> getValue() {
            return responses;
        }
    }
}
