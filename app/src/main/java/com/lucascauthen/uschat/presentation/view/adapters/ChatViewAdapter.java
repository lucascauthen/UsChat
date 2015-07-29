package com.lucascauthen.uschat.presentation.view.adapters;

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

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lhc on 6/10/15.
 */
public class ChatViewAdapter extends RecyclerView.Adapter<ChatViewAdapter.ChatViewHolder> {

    private final ChatViewPresenter presenter;

    private final ChatViewHolder.OnClickChatListener listener;

    private static final int RECEIVED_ID = R.drawable.ic_action_file_download;
    private static final int SENT_ID = R.drawable.ic_action_file_upload;

    public ChatViewAdapter(ChatViewPresenter presenter, ChatViewHolder.OnClickChatListener listener) {
        this.presenter = presenter;
        this.listener = listener;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout_chat, parent, false);
        ChatViewHolder cvh = new ChatViewHolder(v);
        cvh.setOnClickListener(listener);
        return cvh;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        Chat chat = presenter.getItem(position);
        holder.attachData(chat);
        if(chat.getChatType() == Chat.ChatType.RECEIVED_CHAT) {
            holder.personName.setText(chat.getFrom());
            holder.chatType.setImageResource(RECEIVED_ID);
            holder.cv.setCardBackgroundColor(presenter.getContext().getResources().getColor(R.color.accent_dark));
        } else {
            holder.chatType.setImageResource(SENT_ID);
            String name = "";
            for(String person : chat.getTo()) {
                name += person + ", ";
            }
            name = name.substring(0, name.length() - 2); //This should never throw an exception because the max number of characters in a username is greater than 2
            holder.personName.setText(name);
        }
    }

    @Override
    public int getItemCount() {
        return presenter.getSize();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        private final String SENT_MSG = "The chat will disappear when all of the recipients open the chat";
        private final String RECEIVED_MSG = "Tap to load";

        @InjectView(R.id.chatcard_cv)
        CardView cv;
        @InjectView(R.id.chatcard_message_type)
        ImageView chatType;
        @InjectView(R.id.chatcard_name)
        TextView personName;
        @InjectView(R.id.chatcard_msg)
        TextView msg;
        @InjectView(R.id.chatcard_progress_bar)
        ProgressBar loading;

        private Chat dataObject;


        private static final OnClickChatListener NULL_CHAT_CLICK_LISTENER = new NullChatClickListener();
        private OnClickChatListener clickListener = NULL_CHAT_CLICK_LISTENER;

        public ChatViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            ChatViewHolder thisObject = this;
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(dataObject, thisObject);
                }
            });
        }

        public void setOnClickListener(OnClickChatListener l) {
            this.clickListener = l;
        }

        public void attachData(Chat chat) {
            this.dataObject = chat;
            if(dataObject.isFromCurrentUser()) {
                msg.setText(SENT_MSG);
            } else {
                msg.setText(RECEIVED_MSG);
            }
        }

        public void setMessage(String message) {
            this.msg.setText(message);
        }

        public void setLoadingProgress(int progress) {
            loading.setProgress(progress);
        }

        public void toggleLoading() {
            if(loading.getVisibility() == View.VISIBLE) {
                loading.setVisibility(View.INVISIBLE);
            } else {
                loading.setVisibility(View.VISIBLE);
            }
        }


        public interface OnClickChatListener {
            void onClick(Chat chat, ChatViewHolder holder);
        }
        public static class NullChatClickListener implements OnClickChatListener {

            @Override
            public void onClick(Chat data, ChatViewHolder holder) {

            }
        }
    }



    public interface ChatViewPresenter {
        int getSize();

        Chat getItem(int index);

        Context getContext();
    }
}
