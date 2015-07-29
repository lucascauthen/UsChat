package com.lucascauthen.uschat.presentation.controller;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.data.repository.Repo;
import com.lucascauthen.uschat.data.repository.chat.ChatReadThroughCache;
import com.lucascauthen.uschat.data.repository.chat.InMemoryChatRepo;
import com.lucascauthen.uschat.data.repository.chat.RemoteChatRepo;
import com.lucascauthen.uschat.presentation.view.adapters.ChatViewAdapter;
import com.lucascauthen.uschat.presentation.view.dialogs.ChatViewDialog;
import com.lucascauthen.uschat.presentation.view.dialogs.listeners.OnCloseListener;
import com.parse.DeleteCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.concurrent.Executor;

import rx.Scheduler;

/**
 * Created by lhc on 6/25/15.
 */
public class ChatPresenter implements ChatViewAdapter.ChatViewPresenter{
    private static final ChatCrudView NULL_VIEW = new NullChatCrudView();

    private final Scheduler backgroundScheduler;
    private final Scheduler foregroundScheduler;
    private final Executor backgroundExecutor;
    private final Executor foregroundExecutor;

    private ChatCrudView view = NULL_VIEW;

    private ChatReadThroughCache chatRepo = new ChatReadThroughCache(new InMemoryChatRepo(), new RemoteChatRepo());
    private ChatViewAdapter adapter;
    private ChatViewAdapter.ChatViewHolder.OnClickChatListener listener = new ChatViewAdapter.ChatViewHolder.OnClickChatListener() {
        @Override
        public void onClick(Chat chat, ChatViewAdapter.ChatViewHolder holder) {
            if(!chat.isFromCurrentUser()) {
                if (!chat.isImageLoaded() && !chat.isLoadingImage()) {
                    chat.setIsLoadingImage(true);
                    holder.toggleLoading();
                    holder.setMessage("Loading...");
                    chat.loadImage(new Chat.ImageReadyCallback() {
                        @Override
                        public void ready(Bitmap image) {
                            chat.setIsLoadingImage(false);
                            chat.setIsImageLoaded(true);
                            chat.setImage(image);
                            holder.setMessage("Tap to view chat!");
                            holder.toggleLoading();
                        }
                    }, new Chat.ProgressCallback() {
                        @Override
                        public void getProgress(int progress) {
                            holder.setLoadingProgress(progress);
                        }
                    });
                } else if(chat.isImageLoaded()){
                    showChat(chat);
                }
            }
        }
    };

    public ChatPresenter(Scheduler backgroundScheduler, Scheduler foregroundScheduler, Executor backgroundExecutor, Executor foregroundExecutor) {
        this.backgroundScheduler = backgroundScheduler;
        this.foregroundScheduler = foregroundScheduler;
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
        this.adapter = new ChatViewAdapter(this, listener);
    }

    public void attachView(ChatCrudView view) {
        this.view = view;
    }

    public void detachView() {
        this.view = NULL_VIEW;
    }

    public void reloadView() {
        backgroundExecutor.execute(() -> {
            chatRepo.get(new Repo.Request(true));
            foregroundExecutor.execute(() -> {
                adapter.notifyDataSetChanged();
                view.hideLoading();
            });
        });
    }

    public void onLogoutClick() {
        view.showLoading();
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    view.exit();
                }
            }
        });
    }
    public RecyclerView.Adapter getAdapter() {
        return this.adapter;
    }

    public void showChat(Chat chat) {
        ChatViewDialog chatView = new ChatViewDialog(view.getContext(), chat.getImage(), 10000);
        chatView.show();
        chatView.setOnCloseListener(new OnCloseListener() {
            @Override
            public void close(Dialog dialog) {
                backgroundExecutor.execute(() -> {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Chats");
                    try {
                        ParseObject result = query.get(chat.getId());
                        List<String> people = result.getList("to");
                        if(people.size() > 1) { //If you not the last person to open the chat
                            people.remove(Person.getCurrentUser().getName());
                            result.save();
                            reloadView();
                        } else { //If you are the last person to open the chat
                            result.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e == null) {
                                        reloadView();
                                    } else {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    @Override
    public int getSize() {
        return chatRepo.get(new Repo.Request(false)).getValue().size();
    }

    @Override
    public Chat getItem(int index) {
        return chatRepo.get(new Repo.Request(false)).getValue().get(index);
    }

    @Override
    public Context getContext() {
        return view.getContext();
    }

    public interface ChatCrudView {
        void showLoading();

        void hideLoading();

        void exit();

        Context getContext();

    }
    public static class NullChatCrudView implements ChatCrudView{

        @Override
        public void showLoading() {

        }

        @Override
        public void hideLoading() {

        }

        @Override
        public void exit() {

        }

        @Override
        public Context getContext() {
            return null;
        }

    }
}
