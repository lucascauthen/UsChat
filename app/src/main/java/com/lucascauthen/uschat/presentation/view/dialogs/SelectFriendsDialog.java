package com.lucascauthen.uschat.presentation.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.data.entities.Person;
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


    @InjectView(R.id.send_chat_accept) ImageButton sendButton;
    @InjectView(R.id.select_friends_rv) RecyclerView recyclerView;
    @InjectView(R.id.send_chat_reject) ImageButton rejectChat;

    private LinearLayoutManager layoutManager;
    private List<User> selectedFriends = new ArrayList<>();

    public SelectFriendsDialog(Context context) {
        super(context, R.style.SelectFriendsDialog);
        setContentView(R.layout.dialog_select_friends);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ButterKnife.inject(this);


        layoutManager = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView)findViewById(R.id.select_friends_rv);
        recyclerView.setLayoutManager(layoutManager);

    }
}
