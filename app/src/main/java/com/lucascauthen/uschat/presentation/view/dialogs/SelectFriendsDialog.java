package com.lucascauthen.uschat.presentation.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.controller.base.BaseSelectFriendsDialogPresenter;
import com.lucascauthen.uschat.presentation.controller.implmentations.SelectFriendsDialogPresenter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import com.lucascauthen.uschat.presentation.presenters.FriendSelectPresenter;
import com.lucascauthen.uschat.presentation.view.components.recyclerviews.PersonRecyclerView;
import com.lucascauthen.uschat.presentation.view.base.FriendSelectView;


public class SelectFriendsDialog extends Dialog implements FriendSelectView{


    @InjectView(R.id.accept) ImageButton sendButton;
    @InjectView(R.id.recyclerView) PersonRecyclerView recyclerView;
    @InjectView(R.id.reject) ImageButton rejectChat;
    @InjectView(R.id.progress) ProgressBar progress;

    private LinearLayoutManager layoutManager;
    private final FriendSelectPresenter presenter;
    private byte[] pictureData;

    public SelectFriendsDialog(Context context, FriendSelectPresenter presenter, byte[] pictureData) {
        super(context, R.style.SelectFriendsDialog);
        setContentView(R.layout.dialog_select_friends);
        this.presenter = presenter;
        this.pictureData = pictureData;
        ButterKnife.inject(this);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        sendButton.setOnClickListener(view -> {
            dismiss();
            presenter.onSend(pictureData);
            dispose();
        });
        rejectChat.setOnClickListener((view) -> {
            dismiss();
            dispose();
        });

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        presenter.attachView(this);
        presenter.attachSubView(recyclerView);
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

    private void dispose() {
        this.pictureData = null;
    }
}
