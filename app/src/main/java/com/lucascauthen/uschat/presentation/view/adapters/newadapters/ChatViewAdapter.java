package com.lucascauthen.uschat.presentation.view.adapters.newadapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.presentation.controller.base.BaseChatListViewPresenter;
import com.lucascauthen.uschat.util.NullObject;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lhc on 6/10/15.
 */
public class ChatViewAdapter extends RecyclerView.Adapter<ChatViewAdapter.ChatViewHolder> implements BaseChatListViewPresenter.ChatListAdapter {

    private BaseChatListViewPresenter presenter;
    private BaseChatListViewPresenter.ChatListCardView.OnClickChatListener onClickChatListener;
    private int numItems = 0;

    @Inject
    public ChatViewAdapter() {
        //Empty
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_chat, parent, false);
        ChatViewHolder cvh = new ChatViewHolder(v);
        cvh.setOnClickChatListener(onClickChatListener);
        return cvh;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        Chat chat = presenter.getItem(position);
        holder.init(chat);
    }

    @Override
    public int getItemCount() {
        return numItems;
    }

    @Override
    public void notifyDataUpdate(BaseChatListViewPresenter.OnDoneLoadingCallback callback) {
        presenter.getSizeInBackground((size) -> {
            callback.done();
            this.numItems = size;
            this.notifyDataSetChanged();
        });
    }

    @Override
    public void attachPresenter(BaseChatListViewPresenter presenter) {
        this.presenter = presenter;
        //TODO set fields
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder implements BaseChatListViewPresenter.ChatListCardView{
        private final int RECEIVED_ID = R.drawable.ic_action_file_download;
        private final int SENT_ID = R.drawable.ic_action_file_upload;

        private final String SENT_MSG = "The chat will disappear when all of the recipients open the chat";
        private final String RECEIVED_MSG = "Tap to load";

        @InjectView(R.id.chatcard_cv) CardView cv;
        @InjectView(R.id.chatcard_message_type) ImageView chatType;
        @InjectView(R.id.chatcard_name) TextView personName;
        @InjectView(R.id.chatcard_msg) TextView msg;
        @InjectView(R.id.chatcard_progress_bar) ProgressBar loading;

        private Chat chat;

        private final Context context;

        private OnClickChatListener onClickChatListener = NullObject.create(OnClickChatListener.class);

        public ChatViewHolder(View itemView) {
            super(itemView);
            this.context = itemView.getContext();
            ButterKnife.inject(this, itemView);

            BaseChatListViewPresenter.ChatListCardView thisObject = this;
            cv.setOnClickListener(view -> {
                onClickChatListener.onClick(chat, thisObject);
            });
        }

        private void init(Chat chat) {
            this.chat = chat;
            if(chat.getChatType() == Chat.ChatType.RECEIVED_CHAT) {
                personName.setText(chat.getFrom());
                chatType.setImageResource(RECEIVED_ID);
                cv.setCardBackgroundColor(context.getResources().getColor(R.color.accent_dark));
            } else {
                chatType.setImageResource(SENT_ID);
                String name = "";
                for(String person : chat.getTo()) {
                    name += person + ", ";
                }
                name = name.substring(0, name.length() - 2); //This should never throw an exception because the max number of characters in a username is greater than 2
                personName.setText(name);
            }
            if(chat.isFromCurrentUser()) {
                msg.setText(SENT_MSG);
            } else {
                msg.setText(RECEIVED_MSG);
            }
        }

        @Override
        public void toggleLoading() {
            if(loading.getVisibility() == View.VISIBLE) {
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
        public void setMessage(String msg) {
            this.msg.setText(msg);
        }

        //Can only be set by the parent class
        private void setOnClickChatListener(OnClickChatListener listener) {
            this.onClickChatListener = listener;
        }
    }

}
