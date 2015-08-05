package com.lucascauthen.uschat.data.repository.user;

import android.support.annotation.NonNull;


import com.lucascauthen.uschat.data.entities.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by lhc on 8/4/15.
 */
public interface PersonRepo {
    void sendFriendRequest(Person person);

    void acceptReceivedRequest(Person person);

    void rejectReceivedRequest(Person person);

    void deleteSentRequest(Person person);

    void removeFriend(Person person);

    Response get(Request request);

    void get(Request request, GetCallback callback);

    enum Type {
        FRIEND,
        SENT_REQUEST,
        RECEIVED_REQUEST,
        SEARCH
    }

    class Request {
        private final boolean skipCache;
        private final String query;
        private final Type requestType;
        private final boolean hasQuery;

        public Request(boolean skipCache, Type requestType) {
            this.skipCache = skipCache;
            this.query = null;
            this.requestType = requestType;
            hasQuery = false;
        }
        public Request(boolean skipCache, String query, Type requestType) {
            this.skipCache = skipCache;
            this.query = query;
            this.requestType = requestType;
            hasQuery = (!query.equals(""));
        }

        public boolean skipCache() {
            return skipCache;
        }

        public Type requestType() {
            return requestType;
        }

        public boolean hasQuery() {
            return hasQuery;
        }

        public String query() {
            return query;
        }

    }

    class Response {
        private final List<Person> result;
        private final Type responseType;

        public Response(List<Person> result, @NonNull Type responseType) {
            if(result != null) {
                this.result = result;
            } else {
                this.result = new ArrayList<>();
            }
            this.responseType = responseType;
        }

        public List<Person> result() {
            return result;
        }

        public Type responseType() {
            return responseType;
        }

    }

    interface GetCallback {
        void onGet(Response response);
    }
}
