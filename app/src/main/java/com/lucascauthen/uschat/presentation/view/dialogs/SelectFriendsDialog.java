package com.lucascauthen.uschat.presentation.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.data.entities.User;
import com.lucascauthen.uschat.presentation.controller.base.BasePersonListViewPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BaseSelectFriendsDialogPresenter;
import com.lucascauthen.uschat.presentation.controller.implmentations.SelectFriendsDialogPresenter;
import com.lucascauthen.uschat.presentation.view.adapters.newadapters.PersonViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lhc on 7/2/15.
 */
public class SelectFriendsDialog extends Dialog implements SelectFriendsDialogPresenter.BaseSelectFriendsDialog{


    @InjectView(R.id.send_chat_accept) ImageButton sendButton;
    @InjectView(R.id.select_friends_rv) RecyclerView recyclerView;
    @InjectView(R.id.send_chat_reject) ImageButton rejectChat;
    @InjectView(R.id.select_friends__loading) ProgressBar progress;

    private LinearLayoutManager layoutManager;
    private List<Person> selectedFriends = new ArrayList<>();
    private final BaseSelectFriendsDialogPresenter presenter;

    public SelectFriendsDialog(Context context, BaseSelectFriendsDialogPresenter presenter, PersonViewAdapter adapter) {
        super(context, R.style.SelectFriendsDialog);
        setContentView(R.layout.dialog_select_friends);
        ButterKnife.inject(this);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.presenter = presenter;

        sendButton.setOnClickListener(view -> {
            dismiss();
            presenter.onSend();
        });
        rejectChat.setOnClickListener((view) -> {
            dismiss();
        });

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
        presenter.attachAdapter(adapter);
        presenter.attachView(this);
    }

    @Override
    public void enableSend() {
        sendButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void disableSend() {
        sendButton.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progress.setVisibility(View.GONE);
    }

    public void update(byte[] data) {
        this.presenter.attachPictureData(data);
        this.presenter.updateRequested();
    }
}
