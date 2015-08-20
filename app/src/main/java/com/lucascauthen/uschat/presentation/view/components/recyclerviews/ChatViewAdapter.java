package com.lucascauthen.uschat.presentation.view.components.recyclerviews;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.presentation.presenters.ListPresenter;
import com.lucascauthen.uschat.presentation.view.base.ListView;
import com.lucascauthen.uschat.presentation.view.base.cards.ChatListItem;

import javax.inject.Inject;


public class ChatViewAdapter extends RecyclerView.Adapter<ChatViewAdapter.ChatViewHolder> {

    private ListPresenter<Chat, Chat.ChatType, ChatListItem> presenter;
    private ListView.OnClickListener<Chat, ChatListItem, ListPresenter<Chat, Chat.ChatType, ChatListItem>> listener;
    private ListView.InitialStateSetter<Chat, ChatListItem> initialStateSetter;
    private int numItems = 0;

    @Inject
    public ChatViewAdapter() {
        //Empty
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_chat, parent, false);
        ChatViewHolder cvh = new ChatViewHolder(v, presenter, listener, initialStateSetter);
        return cvh;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {

        presenter.getItemInBackground(position, (chat) -> {
            holder.bindData(chat);
            holder.loadState();
        });
    }

    @Override
    public int getItemCount() {
        return numItems;
    }

    public void notifyDataUpdate(ListView.OnUpdateCompleteCallback callback) {
        presenter.getSizeInBackground((size) -> {
            callback.done();
            this.numItems = size;
            this.notifyDataSetChanged();
        });
    }

    public void attachPresenter(ListPresenter<Chat, Chat.ChatType, ChatListItem> presenter) {
        this.presenter = presenter;
    }

    public void setOnClickListener(ListView.OnClickListener<Chat, ChatListItem, ListPresenter<Chat, Chat.ChatType, ChatListItem>> listener) {
        this.listener = listener;
    }

    public void setInitialStateSetter(ListView.InitialStateSetter<Chat, ChatListItem> initialStateSetter) {
        this.initialStateSetter = initialStateSetter;
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder implements ChatListItem {
        private final int RECEIVED_ID = R.drawable.ic_action_file_download;
        private final int SENT_ID = R.drawable.ic_action_file_upload;

        private final String SENT_MSG = "Chat sent";
        private final String RECEIVED_MSG = "Tap to load";

        @InjectView(R.id.chatcard_cv) CardView cv;
        @InjectView(R.id.chatcard_message_type) ImageView chatType;
        @InjectView(R.id.chatcard_name) TextView personName;
        @InjectView(R.id.chatcard_msg) TextView msg;
        @InjectView(R.id.chatcard_progress_bar) ProgressBar loading;

        private Chat chat;

        private final Context context;

        private final ListPresenter<Chat, Chat.ChatType, ChatListItem> presenter;
        private final ListView.OnClickListener<Chat, ChatListItem, ListPresenter<Chat, Chat.ChatType, ChatListItem>> onClickChatListener;
        private final ListView.InitialStateSetter<Chat, ChatListItem> initialStateSetter;

        public ChatViewHolder(View itemView, ListPresenter<Chat, Chat.ChatType, ChatListItem> presenter,
                              ListView.OnClickListener<Chat, ChatListItem, ListPresenter<Chat, Chat.ChatType, ChatListItem>> listener,
                              ListView.InitialStateSetter<Chat, ChatListItem> initialStateSetter) {
            super(itemView);
            this.context = itemView.getContext();
            this.presenter = presenter;
            this.onClickChatListener = listener;
            this.initialStateSetter = initialStateSetter;
            ButterKnife.inject(this, itemView);

            ChatListItem thisObject = this;
            cv.setOnClickListener(view -> {
                onClickChatListener.onClick(chat, thisObject, presenter);
            });
        }

        private void bindData(Chat chat) {
            this.chat = chat;
        }

        @Override
        public void toggleLoading() {
            if (loading.getVisibility() == View.VISIBLE) {
                loading.setVisibility(View.INVISIBLE);
            } else {
                loading.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void setLoadingProgress(int progress) {
            loading.setProgress(progress);
        }

        @Override
        public void setName(String msg) {
            this.personName.setText(msg);
        }

        @Override
        public void setMessage(String msg) {
            this.msg.setText(msg);
        }

        @Override
        public void setStateIcon(int resourceId) {
            chatType.setImageResource(resourceId);
        }

        private void loadState() {

        }
    }

}
