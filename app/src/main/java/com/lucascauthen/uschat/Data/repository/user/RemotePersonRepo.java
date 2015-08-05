package com.lucascauthen.uschat.data.repository.user;

/**
 * Created by lhc on 8/4/15.
 */
public class RemotePersonRepo implements PersonRepo {
    @Override
    public void sendFriendRequest(String person) {
        //TODO
    }

    @Override
    public void acceptReceivedRequest(String person) {
        //TODO
    }

    @Override
    public void rejectReceivedRequest(String person) {
        //TODO
    }

    @Override
    public void deleteSentRequest(String person) {
        //TODO
    }

    @Override
    public void removeFriend(String person) {
        //TODO
    }

    @Override
    public Response get(Request request) {
        return null; //TODO
    }

    @Override
    public void get(Request request, GetCallback callback) {
        callback.onGet(this.get(request));
    }

}
