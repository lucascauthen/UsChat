package com.lucascauthen.uschat.presentation.view.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.presentation.controller.PersonViewPresenter;
import com.lucascauthen.uschat.presentation.view.adapters.PersonViewAdapter;
import com.lucascauthen.uschat.presentation.view.dialogs.listeners.OnAcceptListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lhc on 7/2/15.
 */
public class SelectFriendsDialog extends Dialog {
    private Bitmap image;
    private NullOnAcceptListener NULL_LISTENER = new NullOnAcceptListener();
    private OnSendChatListener onSendChatListener = NULL_LISTENER;

    private ImageButton sendButton;
    private PersonViewAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private List<String> selectedFriends = new ArrayList<>();

    private PersonViewPresenter.OnPersonSettingClickedListener clickedListener = new PersonViewPresenter.OnPersonSettingClickedListener() {
        @Override
        public int onClick(Person person, int stateBeforeClick) {
            int newState = -1;
            if(stateBeforeClick == 0) {
                newState = 1;
                selectedFriends.add(person.getName());
            } else {
                newState = 0;
                selectedFriends.remove(person.getName());
            }
            if(selectedFriends.size() > 0) {
                sendButton.setVisibility(View.VISIBLE);
            } else {
                sendButton.setVisibility(View.GONE);
            }
            return newState;
        }
    };

    public SelectFriendsDialog(Context context) {
        super(context, R.style.SelectFriendsDialog);
        setContentView(R.layout.dialog_select_friends);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        sendButton = (ImageButton)findViewById(R.id.send_chat_accept);
        sendButton.setOnClickListener(view -> {
            ((ViewGroup) getWindow().getDecorView())
                    .getChildAt(0).startAnimation(AnimationUtils.loadAnimation(
                    getContext(), R.anim.slide_out_right));
            cancel();
            onSendChatListener.sendChats(selectedFriends);
        });
        findViewById(R.id.send_chat_reject).setOnClickListener(view -> {
            cancel();
        });
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView)findViewById(R.id.select_friends_rv);
        recyclerView.setLayoutManager(layoutManager);

    }

    public void setOnSendChatListener(OnSendChatListener l) {
        this.onSendChatListener = l;
    }

    public void attachAdapter(PersonViewAdapter adpter) {
        this.adapter = adpter;
        this.recyclerView.setAdapter(adpter);
        this.adapter.setClickListener(clickedListener);
        Person.refreshCurrentUser();
        List<String> friends = Person.getCurrentUser().getFriends();
        List<Person> persons = new ArrayList<>();
        for(String friend : friends) {
            persons.add(new Person(friend));
        }
        this.adapter.updateData(persons);
    }

    private static class NullOnAcceptListener implements OnSendChatListener {

        @Override
        public void sendChats(List<String> names) {

        }
    }

    public interface OnSendChatListener {
        void sendChats(List<String> names);
    }
}
