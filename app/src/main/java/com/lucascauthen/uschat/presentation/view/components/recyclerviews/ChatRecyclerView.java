package com.lucascauthen.uschat.presentation.view.components.recyclerviews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.presentation.presenters.ListPresenter;
import com.lucascauthen.uschat.presentation.view.base.ListView;
import com.lucascauthen.uschat.presentation.view.base.cards.ChatListItem;

public class ChatRecyclerView extends RecyclerView implements ListView<Chat, Chat.ChatType, ChatListItem>{
    private final ChatViewAdapter adapter;
    public ChatRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.adapter = new ChatViewAdapter();
        this.setAdapter(adapter);
    }
    public ChatRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.adapter = new ChatViewAdapter();
        this.setAdapter(adapter);
    }
    public ChatRecyclerView(Context context) {
        super(context);
        this.adapter = new ChatViewAdapter();
        this.setAdapter(adapter);
    }

    @Override
    public void notifyUpdate(OnUpdateCompleteCallback callback) {
        this.adapter.notifyDataUpdate(callback);
    }

    @Override
    public void attachPresenter(ListPresenter<Chat, Chat.ChatType, ChatListItem> presenter) {
        this.adapter.attachPresenter(presenter);
    }

    @Override
    public void setOnClickListener(ListView.OnClickListener<Chat, ChatListItem, ListPresenter<Chat, Chat.ChatType, ChatListItem>> listener) {
        this.adapter.setOnClickListener(listener);
    }

    @Override
    public void setInitialStateSetter(InitialStateSetter<Chat, ChatListItem> initialStateSetter) {
        this.adapter.setInitialStateSetter(initialStateSetter);
    }
}
