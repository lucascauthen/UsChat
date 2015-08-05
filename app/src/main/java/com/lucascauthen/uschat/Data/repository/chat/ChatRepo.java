package com.lucascauthen.uschat.data.repository.chat;

import com.lucascauthen.uschat.data.entities.Chat;


import java.util.List;

/**
 * Created by lhc on 8/4/15.
 */
public interface ChatRepo {
    void sendChat(Chat chat);

    void openChat(Chat chat);

    Response get(Request request);

    void get(Request request, GetCallback callback);

    enum RequestType {
        SENT,
        RECEIVED,
        COMBINED
    }

    class Request {
        private final boolean skipCache;
        private final RequestType requestType;
        public Request(boolean skipCache, RequestType requestType) {
            this.skipCache = skipCache;
            this.requestType = requestType;
        }

        public boolean skipCache() {
            return skipCache;
        }

        public RequestType requestType() {
            return requestType;
        }
    }

    class Response {
        private final List<Chat> result;
        private final RequestType requestType;
        public Response(List<Chat> result, RequestType requestType) {
            this.result = result;
            this.requestType = requestType;
        }

        public RequestType requestType() {
            return requestType;
        }

        public List<Chat> result() {
            return result;
        }
    }

    interface GetCallback {
        void onGet(Response response);
    }
}
