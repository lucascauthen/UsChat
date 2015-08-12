package com.lucascauthen.uschat.data.repository.chat;

import android.graphics.BitmapFactory;
import android.util.Log;
import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.util.Reversed;
import com.parse.FunctionCallback;
import com.parse.GetDataCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ProgressCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoteChatRepo implements ChatRepo {

    @Override
    public void sendChat(Chat chat, OnCompleteAction callback) {

        ParseObject newChat = new ParseObject("Chats");
        newChat.put("to", chat.getToString());
        newChat.put("from", chat.getFrom());
        ParseFile file = new ParseFile(chat.getChatData());
        try {
            file.save(); //Network call
            newChat.put("file", file);
            newChat.save(); //Network call
            chat.setId(newChat.getObjectId());
            ParseUser user = ParseUser.getCurrentUser().fetch(); //Network call
            //Update the to/from users that there was a new chat sent

            //FROM
            user.add("sentChats", chat.getId());
            user.save();

            //TO

            //////////////
            //Cloud Code//
            //////////////
            /*

            Because the client can only modify its own data, this request is sent to the server to update the other user that this operation affects

             */

            Map<String, Object> map = new HashMap<>();
            map.put("currentUser", ParseUser.getCurrentUser().getUsername());
            map.put("toUser", chat.getToString());
            map.put("chatId", chat.getId());
            ParseCloud.callFunctionInBackground("sendChat", map, new FunctionCallback<Object>() { //Network call
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
        }
        //When finished:
        callback.onComplete(chat.getId());
    }

    @Override
    public void openChat(Chat chat, OnCompleteAction callback) {
        try {
            ParseUser user = ParseUser.getCurrentUser().fetch(); //Network call
            user.removeAll("receivedChats", Collections.singletonList(chat.getId()));
            user.save(); //Network call
            //TO

            //////////////
            //Cloud Code//
            //////////////
            /*


            Because the client can only modify its own data, this request is sent to the server to update the other user that this operation affects

             */
            Map<String, String> map = new HashMap<>();
            map.put("currentUser", ParseUser.getCurrentUser().getUsername());
            map.put("fromUser", chat.getFrom());
            map.put("chatId", chat.getId());
            ParseCloud.callFunctionInBackground("openChat", map, new FunctionCallback<Object>() {
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
        }
    }

    @Override
    public Response get(Request request) {
        try {
            ParseUser user = ParseUser.getCurrentUser().fetch(); //Network call
            List<String> chatIdList = new ArrayList<>();

            boolean receivedChat = false;

            switch(request.requestType()) {
                case RECEIVED:
                    chatIdList = user.getList("receivedChats");
                    receivedChat = true;
                    break;
                case SENT:
                    chatIdList = user.getList("sentChats");
                    break;
                default:
                    break;
            }
            List<Chat> formattedChatList = new ArrayList<>();
            if(chatIdList != null) {
                for (String id : Reversed.reversed(chatIdList)) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Chats");
                    ParseObject chat = query.get(id); //Network call
                    List<Person> toPeople = new ArrayList<>();
                    for(Object name : chat.getList("to")) {
                        toPeople.add(new Person((String)name, Person.PersonState.FRIENDS));
                    }

                    if(receivedChat) {
                        //Generate a function to load the image when the user requests it
                        Chat.LoadImageFunction function = new Chat.LoadImageFunction() {
                            @Override
                            public void loadImage(Chat.ImageReadyCallback readyCallback, Chat.ProgressCallback progressCallback) {
                                final ParseFile image = chat.getParseFile("file");
                                image.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] bytes, ParseException e) {
                                        if (e == null) {
                                            readyCallback.ready(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                                        } else {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new ProgressCallback() {
                                    @Override
                                    public void done(Integer integer) {
                                        progressCallback.getProgress(integer);
                                    }
                                });
                            }
                        };
                        formattedChatList.add(new Chat(toPeople, chat.getString("from"), Chat.ChatType.RECEIVED_CHAT, function));
                    } else {
                        formattedChatList.add(new Chat(toPeople, chat.getString("from"), Chat.ChatType.SENT_CHAT, null));
                    }

                }
                return new Response(formattedChatList, request.requestType()); //Not returning an unmodifiable list because the user never directly has access to it
            } else {
                return new Response(new ArrayList<>(), request.requestType()); //Essentially a "safe" null return
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void get(Request request, GetCallback callback) {
        callback.onGet(get(request));
    }
}
