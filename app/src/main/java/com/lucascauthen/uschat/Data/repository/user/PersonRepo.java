package com.lucascauthen.uschat.data.repository.user;

import android.support.annotation.NonNull;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhc on 8/4/15.
 */
public interface PersonRepo {
    void sendFriendRequest(String person);

    void acceptReceivedRequest(String person);

    void rejectReceivedRequest(String person);

    void deleteSentRequest(String person);

    void removeFriend(String person);

    Response get(Request request);

    void get(Request request, GetCallback callback);

    enum Type {
        FRIEND,
        SENT_REQUEST,
        RECEIVED_REQUEST
    }

    class Request {
        private final boolean skipCache;
        private final String query;
        private final Type requestType;
        private final boolean hasQuery;

        public Request(boolean skipCache, Type requestType) {
            this.skipCache = skipCache;
            this.query = "";
            this.requestType = requestType;
            hasQuery = false;
        }
        public Request(boolean skipCache, String query, Type requestType) {
            this.skipCache = skipCache;
            this.query = query;
            this.requestType = requestType;
            hasQuery = true;
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
        private final List<String> result;
        private final Type responseType;

        public Response(List<String> result, @NonNull Type responseType) {
            if(result != null) {
                this.result = result;
            } else {
                this.result = new ArrayList<>();
            }
            this.responseType = responseType;
        }

        public List<String> result() {
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
