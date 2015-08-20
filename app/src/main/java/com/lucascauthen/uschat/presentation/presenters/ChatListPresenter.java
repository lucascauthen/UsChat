package com.lucascauthen.uschat.presentation.presenters;

import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.data.repository.chat.ChatRepo;
import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.view.base.ListView;
import com.lucascauthen.uschat.presentation.view.base.cards.ChatListItem;
import com.lucascauthen.uschat.util.NullObject;

public class ChatListPresenter implements ListPresenter<Chat, Chat.ChatType, ChatListItem> {

    private static final ListView<Chat, Chat.ChatType, ChatListItem> NULL_VIEW = NullObject.create(ListView.class);

    private ListView<Chat, Chat.ChatType, ChatListItem> view = NULL_VIEW;

    private final BackgroundExecutor backgroundExecutor;
    private final ForegroundExecutor foregroundExecutor;
    private final ChatRepo repository;
    private boolean repoNeedUpdate = true;

    private Chat.ChatType displayType;

    ListView.OnClickListener<Chat, ChatListItem, ListPresenter<Chat, Chat.ChatType, ChatListItem>> listener;

    ListView.InitialStateSetter<Chat, ChatListItem> initialStateSetter;

    private String query = ""; //Unused right now

    public ChatListPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, ChatRepo repository) {
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
        this.repository = repository;
    }

    @Override
    public Chat getItem(int index) {
        if(repoNeedUpdate) {
            repoNeedUpdate = false;
            return repository.get(new ChatRepo.Request(true, displayType)).result().get(index);
        } else {
            return repository.get(new ChatRepo.Request(false, displayType)).result().get(index);
        }
    }

    @Override
    public void getItemInBackground(int position, GetItemCallBack<Chat> callback) {
        backgroundExecutor.execute(() -> {
            Chat item = getItem(position);
            foregroundExecutor.execute(() -> {
                callback.onGetItem(item);
            });
        });
    }

    @Override
    public int getSize() {
        if(repoNeedUpdate) {
            repoNeedUpdate = false;
            return repository.get(new ChatRepo.Request(true, displayType)).result().size();
        } else {
            return repository.get(new ChatRepo.Request(false, displayType)).result().size();
        }
    }

    @Override
    public void getSizeInBackground(GetSizeCallback callback) {
        backgroundExecutor.execute(() -> {
            int size = getSize();
            foregroundExecutor.execute(() -> {
                callback.onGetSize(size);
            });
        });
    }

    @Override
    public void requestUpdate(ListView.OnUpdateCompleteCallback callback, boolean updateRepo) {
        this.repoNeedUpdate = updateRepo;
        view.notifyUpdate(callback);
    }

    @Override
    public void attachView(ListView<Chat, Chat.ChatType, ChatListItem> view) {
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void detachView() {
        this.view = NULL_VIEW;
    }

    @Override
    public void setDisplayType(Chat.ChatType displayType) {
        this.displayType = displayType;
    }

    @Override
    public void setFilterQuery(String query) {
        this.query = query; //Unused right now
    }

}
