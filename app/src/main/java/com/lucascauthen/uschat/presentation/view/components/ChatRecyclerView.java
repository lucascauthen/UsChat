package com.lucascauthen.uschat.presentation.view.components;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import com.lucascauthen.uschat.data.repository.chat.ChatRepo;
import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.controller.base.BaseChatListViewPresenter;
import com.lucascauthen.uschat.presentation.controller.implmentations.ChatListViewPresenter;
import com.lucascauthen.uschat.util.NullObject;

import javax.inject.Inject;

public class ChatRecyclerView extends RecyclerView {
    private final ForegroundExecutor foregroundExecutor;
    private final BackgroundExecutor backgroundExecutor;
    private final ChatListViewPresenter presenter;

    public ChatRecyclerView(Context context, ForegroundExecutor foregroundExecutor, BackgroundExecutor backgroundExecutor, ChatListViewPresenter presenter, ChatRepo repo) {
        super(context);
        this.foregroundExecutor = foregroundExecutor;
        this.backgroundExecutor = backgroundExecutor;
        this.presenter = presenter;
        //this.setAdapter();
    }

}
