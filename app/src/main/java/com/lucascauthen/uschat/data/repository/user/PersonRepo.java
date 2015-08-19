package com.lucascauthen.uschat.data.repository.user;

import android.support.annotation.NonNull;
import com.lucascauthen.uschat.data.entities.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhc on 8/4/15.
 */
public interface PersonRepo {
    void sendFriendRequest(Person person, OnCompleteAction callback);

    void acceptReceivedRequest(Person person, OnCompleteAction callback);

    void rejectReceivedRequest(Person person, OnCompleteAction callback);

    void deleteSentRequest(Person person, OnCompleteAction callback);

    void removeFriend(Person person, OnCompleteAction callback);

    Response get(Request request);

    void get(Request request, GetCallback callback);

    interface GetCallback {
        void onGet(Response response);
    }

    interface OnCompleteAction {
        void onComplete(String optionalMessage);
    }

    class Request {
        private final boolean skipCache;
        private final String query;
        private final Person.PersonType requestType;
        private final boolean hasQuery;

        public Request(boolean skipCache, Person.PersonType requestType) {
            this.skipCache = skipCache;
            this.query = null;
            this.requestType = requestType;
            hasQuery = false;
        }

        public Request(boolean skipCache, String query, Person.PersonType requestType) {
            this.skipCache = skipCache;
            this.query = query;
            this.requestType = requestType;
            if (query != null) {
                hasQuery = (!query.equals(""));
            } else {
                hasQuery = false;
            }
        }

        public boolean skipCache() {
            return skipCache;
        }

        public Person.PersonType requestType() {
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
        private final Person.PersonType responseType;

        public Response(List<Person> result, @NonNull Person.PersonType responseType) {
            if (result != null) {
                this.result = result;
            } else {
                this.result = new ArrayList<>();
            }
            this.responseType = responseType;
        }

        public List<Person> result() {
            return result;
        }

        public Person.PersonType responseType() {
            return responseType;
        }

    }
}
