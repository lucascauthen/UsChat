package com.lucascauthen.uschat.presentation.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.data.entities.User;
import com.lucascauthen.uschat.presentation.controller.base.BasePersonListViewPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lhc on 7/2/15.
 */
public class SelectFriendsDialog extends Dialog {

    private final BasePersonListViewPresenter.PersonListAdapter adapter;
    private final BasePersonListViewPresenter presenter;
    private final BasePersonListViewPresenter.PersonListCardView.InitialStateSetter setter;
    private final List<Integer> iconPack;
    private final OnSendChatListener onSendChatListener;

    @InjectView(R.id.send_chat_accept) ImageButton sendButton;
    @InjectView(R.id.select_friends_rv) RecyclerView recyclerView;
    @InjectView(R.id.send_chat_reject) ImageButton rejectChat;

    private LinearLayoutManager layoutManager;
    private List<User> selectedFriends = new ArrayList<>();

    public SelectFriendsDialog(Context context, BasePersonListViewPresenter presenter, BasePersonListViewPresenter.PersonListAdapter adapter, OnSendChatListener sendChatListener) {
        super(context, R.style.SelectFriendsDialog);
        setContentView(R.layout.dialog_select_friends);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        ButterKnife.inject(this);

        this.adapter = adapter;
        this.presenter = presenter;
        /*
        this.listener = (person, view) -> {
            int newState = -1;
            if(view.getState() == 0) {
                newState = 1;
                selectedFriends.add(person);
            } else {
                newState = 0;
                selectedFriends.remove(person);
            }
            if(selectedFriends.size() > 0) {
                sendButton.setVisibility(View.VISIBLE);
            } else {
                sendButton.setVisibility(View.GONE);
            }
            return newState;
        };
        */
        setter = null; //TODO

        //Init iconPack
        this.iconPack = new ArrayList<>();
        iconPack.add(R.drawable.ic_action_add_box);
        iconPack.add(R.drawable.ic_action_clear);

        onSendChatListener = sendChatListener;

        sendButton.setOnClickListener(view -> {
            ((ViewGroup) getWindow().getDecorView()).getChildAt(0).startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_right));
            cancel();
            onSendChatListener.sendChats(selectedFriends);
        });
        rejectChat.setOnClickListener(v -> {
            cancel();
        });

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView)findViewById(R.id.select_friends_rv);
        recyclerView.setLayoutManager(layoutManager);

    }

    public interface OnSendChatListener {
        void sendChats(List<User> people);
    }
}
