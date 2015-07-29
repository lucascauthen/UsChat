package com.lucascauthen.uschat.data.repository.chat;

import android.graphics.BitmapFactory;

import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.data.repository.Repo;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ProgressCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lhc on 6/25/15.
 */
public class RemoteChatRepo implements Repo<Chat> {
    private List<Chat> items = new ArrayList<>();

    @Override
    public void put(Chat item) {
        //TODO: Add network call to update list
        items.add(item);
    }

    @Override
    public Response<Chat> get(Request request) {
        /*

            One efficiency note on this:

                Because the user can't be a sender and a recipient of a chat,
                the query should exclude looking at the "to" array
                if the value of that row's from column is the user's name.
                (The same goes in reverse, but you don't save processing time
                as much as the first scenario)

         */
        if (request != null) {
            List<ParseQuery<ParseObject>> queries = new ArrayList<>();

            ParseQuery<ParseObject> fromQuery = ParseQuery.getQuery("Chats");
            fromQuery.whereEqualTo("from", Person.getCurrentUser().getName());

            ParseQuery<ParseObject> toQuery = ParseQuery.getQuery("Chats");
            toQuery.whereContainedIn("to", Collections.singletonList(Person.getCurrentUser().getName()));

            queries.add(fromQuery);
            queries.add(toQuery);

            ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
            items.clear();
            try {
                List<ParseObject> chats = mainQuery.find();
                for (ParseObject chat : chats) {
                    String from = chat.getString("from");
                    List<String> to = chat.getList("to");
                    String id = chat.getObjectId();
                    Chat.ChatType chatType;
                    if (from.equals(Person.getCurrentUser().getName())) { //This chat is a sent chat from the current user
                        chatType = Chat.ChatType.SENT_CHAT;
                    } else { //This chat is not from the current user
                        chatType = Chat.ChatType.RECEIVED_CHAT;
                    }
                    Chat.LoadImageFunction function = new Chat.LoadImageFunction() {
                        @Override
                        public void loadImage(Chat.ImageReadyCallback readyCallback, Chat.ProgressCallback progressCallback) {
                            final ParseFile image = chat.getParseFile("file");
                            image.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] bytes, ParseException e) {
                                    if(e == null) {
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
                    items.add(new Chat(from, to, id, chatType, function));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return new Response<>(Collections.unmodifiableList(items), false);
        }
        throw new RuntimeException("Cannot retrieve chat from network because an invalid request was supplied");
    }

    @Override
    public void remove(Chat item) {
        //TODO: Add network call to update list
        items.remove(item);
    }

    @Override
    public boolean exists(Chat item) {
        return items.contains(item);
    }
}
