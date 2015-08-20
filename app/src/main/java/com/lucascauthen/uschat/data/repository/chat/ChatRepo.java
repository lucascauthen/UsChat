package com.lucascauthen.uschat.data.repository.chat;

import com.lucascauthen.uschat.data.entities.Chat;

import java.util.List;

/**
 * Created by lhc on 8/4/15.
 */
public interface ChatRepo {
    void sendChat(Chat chat, OnCompleteAction callback);

    void sendChat(Chat chat, OnCompleteAction callback, boolean waitForRemote);

    void openChat(Chat chat, OnCompleteAction callback);

    void openChat(Chat chat, OnCompleteAction callback, boolean waitForRemote);

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
        private final Chat.ChatType requestType;

        public Request(boolean skipCache, Chat.ChatType requestType) {
            this.skipCache = skipCache;
            this.requestType = requestType;
        }

        public boolean skipCache() {
            return skipCache;
        }

        public Chat.ChatType requestType() {
            return requestType;
        }
    }

    class Response {
        private final List<Chat> result;
        private final Chat.ChatType requestType;

        public Response(List<Chat> result, Chat.ChatType requestType) {
            this.result = result;
            this.requestType = requestType;
        }

        public Chat.ChatType requestType() {
            return requestType;
        }

        public List<Chat> result() {
            return result;
        }
    }
}
