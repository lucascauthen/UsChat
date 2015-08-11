package com.lucascauthen.uschat.data.repository.user;

import android.util.Log;
import com.lucascauthen.uschat.data.entities.Person;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lhc on 8/4/15.
 */
public class RemotePersonRepo implements PersonRepo {
    @Override
    public void sendFriendRequest(Person person, OnCompleteAction callback) {
        ParseUser user = ParseUser.getCurrentUser();
        try {
            user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.add("sentRequests", person.name());
        try {
            user.save();
            //////////////
            //Cloud Code//
            //////////////
            /*

            Because the client can only modify its own data, this request is sent to the server to update the other user that this operation affects

             */
            Map<String, String> map = new HashMap<>();
            map.put("toUser", person.name());
            map.put("fromUser", user.getUsername());
            ParseCloud.callFunctionInBackground("sendRequest", map, new FunctionCallback<Object>() {
                public void done(Object object, ParseException e) {
                    if (e == null) {
                        String msg = object.toString();
                        Log.d("UsChat", msg);
                        callback.onComplete(msg);
                    } else {
                        Log.d("UsChat", e.getMessage());
                        callback.onComplete(e.getMessage());
                    }
                }
            });
            //////////////
            //////////////
        } catch (ParseException e) {
            e.printStackTrace();
            callback.onComplete(e.getMessage());
        }

    }

    @Override
    public void acceptReceivedRequest(Person person, OnCompleteAction callback) {
        ParseUser user = ParseUser.getCurrentUser();
        try {
            user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.removeAll("receivedRequests", Collections.singletonList(person.name()));
        user.addUnique("friends", person.name());
        try {
            user.save();
            //////////////
            //Cloud Code//
            //////////////
            /*

            Because the client can only modify its own data, this request is sent to the server to update the other user that this operation affects

             */
            Map<String, String> map = new HashMap<>();
            map.put("fromUser", person.name());
            map.put("toUser", user.getUsername());
            ParseCloud.callFunctionInBackground("acceptRequest", map, new FunctionCallback<Object>() {
                public void done(Object object, ParseException e) {
                    if (e == null) {
                        String msg = object.toString();
                        Log.d("UsChat", msg);
                        callback.onComplete(msg);
                    } else {
                        Log.d("UsChat", e.getMessage());
                        callback.onComplete(e.getMessage());
                    }
                }
            });
            //////////////
            //////////////
        } catch (ParseException e) {
            e.printStackTrace();
            callback.onComplete(e.getMessage());
        }
    }

    @Override
    public void rejectReceivedRequest(Person person, OnCompleteAction callback) {
        ParseUser user = ParseUser.getCurrentUser();
        try {
            user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.removeAll("receivedRequests", Collections.singletonList(person.name()));
        try {
            user.save();
            //////////////
            //Cloud Code//
            //////////////
            /*

            Because the client can only modify its own data, this request is sent to the server to update the other user that this operation affects

             */
            Map<String, String> map = new HashMap<>();
            map.put("fromUser", person.name());
            map.put("toUser", user.getUsername());
            ParseCloud.callFunctionInBackground("rejectRequest", map, new FunctionCallback<Object>() {
                public void done(Object object, ParseException e) {
                    if (e == null) {
                        String msg = object.toString();
                        Log.d("UsChat", msg);
                        callback.onComplete(msg);
                    } else {
                        Log.d("UsChat", e.getMessage());
                        callback.onComplete(e.getMessage());
                    }
                }
            });
            //////////////
            //////////////
        } catch (ParseException e) {
            e.printStackTrace();
            callback.onComplete(e.getMessage());
        }
    }

    @Override
    public void deleteSentRequest(Person person, OnCompleteAction callback) {
        ParseUser user = ParseUser.getCurrentUser();
        try {
            user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.removeAll("sentRequests", Collections.singletonList(person.name()));
        try {
            user.save();
            //////////////
            //Cloud Code//
            //////////////
            /*

            Because the client can only modify its own data, this request is sent to the server to update the other user that this operation affects

             */
            Map<String, String> map = new HashMap<>();
            map.put("toUser", person.name());
            map.put("fromUser", user.getUsername());
            ParseCloud.callFunctionInBackground("cancelRequest", map, new FunctionCallback<Object>() {
                public void done(Object object, ParseException e) {
                    if (e == null) {
                        String msg = object.toString();
                        Log.d("UsChat", msg);
                        callback.onComplete(msg);
                    } else {
                        Log.d("UsChat", e.getMessage());
                        callback.onComplete(e.getMessage());
                    }
                }
            });
            //////////////
            //////////////
        } catch (ParseException e) {
            e.printStackTrace();
            callback.onComplete(e.getMessage());
        }
    }

    @Override
    public void removeFriend(Person person, OnCompleteAction callback) {
        ParseUser user = ParseUser.getCurrentUser();
        try {
            user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.removeAll("friends", Collections.singletonList(person.name()));
        try {
            user.save();
            //////////////
            //Cloud Code//
            //////////////
            /*

            Because the client can only modify its own data, this request is sent to the server to update the other user that this operation affects

             */
            Map<String, String> map = new HashMap<>();
            map.put("toUser", person.name());
            map.put("fromUser", user.getUsername());
            ParseCloud.callFunctionInBackground("removeFriend", map, new FunctionCallback<Object>() {
                public void done(Object object, ParseException e) {
                    if (e == null) {
                        String msg = object.toString();
                        Log.d("UsChat", msg);
                        callback.onComplete(msg);
                    } else {
                        Log.d("UsChat", e.getMessage());
                        callback.onComplete(e.getMessage());
                    }
                }
            });
            //////////////
            //////////////
        } catch (ParseException e) {
            e.printStackTrace();
            callback.onComplete(e.getMessage());
        }
    }

    @Override
    public Response get(Request request) {
        ParseUser user = ParseUser.getCurrentUser();
        try {
            user.fetch();
            List<String> raw;
            List<Person> result = null;
            switch (request.requestType()) {
                case RECEIVED_REQUEST:
                    raw = user.getList("receivedRequests");
                    result = formatList(raw, Person.PersonState.RECEIVED_REQUEST);
                    break;
                case SENT_REQUEST:
                    raw = user.getList("sentRequests");
                    result = formatList(raw, Person.PersonState.SENT_REQUEST);
                    break;
                case FRIEND:
                    raw = user.getList("friends");
                    result = formatList(raw, Person.PersonState.FRIENDS);
                    break;
                case SEARCH:
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereContains("username", request.query());
                    query.whereNotEqualTo("username", user.getUsername());
                    result = formatQueryResults(query.find(), user);
                    break;
                case REQUESTS:
                    result = formatList(user.getList("sentRequests"), Person.PersonState.SENT_REQUEST);
                    result.addAll(formatList(user.getList("receivedRequests"), Person.PersonState.RECEIVED_REQUEST));
                    break;
                default:
                    //EMPTY
            }
            return new Response(result, request.requestType());
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Error " + e.getCode() + ": " + e.getMessage());
        }
    }

    @Override
    public void get(Request request, GetCallback callback) {
        callback.onGet(this.get(request));
    }

    private List<Person> formatLists(List<String> friends, List<String> sentRequests, List<String> receivedRequests) {
        List<Person> persons = new ArrayList<>();
        for (String name : friends) {
            persons.add(new Person(name, Person.PersonState.FRIENDS));
        }
        for (String name : sentRequests) {
            persons.add(new Person(name, Person.PersonState.SENT_REQUEST));
        }
        for (String name : receivedRequests) {
            persons.add(new Person(name, Person.PersonState.RECEIVED_REQUEST));
        }
        return persons;
    }

    private List<Person> formatList(List<String> items, Person.PersonState type) {
        List<Person> results = new ArrayList<>();
        if (items != null) {
            for (String item : items) {
                results.add(new Person(item, type));
            }
        }
        return results;
    }

    private List<Person> formatQueryResults(List<ParseUser> queryResult, ParseUser user) {
        //Yaa kinda a sorting algorithm
        List<String> friends = user.getList("friends");
        List<String> sentRequests = user.getList("sentRequests");
        List<String> receivedRequests = user.getList("receivedRequests");
        List<Person> result = new ArrayList<>();
        for (ParseUser theUser : queryResult) {
            String name = theUser.getUsername();
            boolean found = false; //Used to skip loops if the person was found in one of the previous searches
            if (friends != null) {
                for (String friendName : friends) {
                    if (name.equals(friendName)) {
                        found = true;
                        result.add(new Person(name, Person.PersonState.FRIENDS));
                        friends.remove(friendName); //This prevents the search from comparing someone we know has already been sorted
                        break; //Exit the loop because we found a match
                    }
                }
            }
            if (!found) { //If the user wasn't found to be a friend, search the next list, otherwise skip
                if (sentRequests != null) {
                    for (String sentRequestName : sentRequests) {
                        if (name.equals(sentRequestName)) {
                            found = true;
                            result.add(new Person(name, Person.PersonState.SENT_REQUEST));
                            sentRequests.remove(sentRequestName); //This prevents the search from comparing someone we know has already been sorted
                            break; //Exit the loop because we found a match
                        }
                    }
                }
                if (!found) { //If the user was not a friend/sentRequest, then search the last list, otherwise skip
                    if (receivedRequests != null) {
                        for (String receivedRequestName : receivedRequests) {
                            if (name.equals(receivedRequestName)) {
                                found = true;
                                result.add(new Person(name, Person.PersonState.RECEIVED_REQUEST));
                                receivedRequests.remove(receivedRequestName); //This prevents the search from comparing someone we know has already been sorted
                                break; //Exit the loop because we found a match
                            }
                        }
                    }
                    if (!found) { //If the user was not in any of the lists, he/she must not be a friend
                        result.add(new Person(name, Person.PersonState.NOT_FRIENDS));
                    }
                }
            }
        }
        return result;
    }
}
