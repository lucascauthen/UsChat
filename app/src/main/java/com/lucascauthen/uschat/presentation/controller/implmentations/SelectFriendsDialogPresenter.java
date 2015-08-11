package com.lucascauthen.uschat.presentation.controller.implmentations;

import android.util.Log;
import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.data.entities.User;
import com.lucascauthen.uschat.data.repository.chat.ChatRepo;
import com.lucascauthen.uschat.data.repository.user.PersonRepo;
import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.controller.base.BasePersonListViewPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BaseRecyclerViewPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BaseSelectFriendsDialogPresenter;
import com.lucascauthen.uschat.presentation.view.dialogs.SelectFriendsDialog;
import com.lucascauthen.uschat.util.NullObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhc on 8/7/15.
 */
public class SelectFriendsDialogPresenter implements BaseSelectFriendsDialogPresenter {

    private static final BaseSelectFriendsDialog NULL_VIEW = NullObject.create(BaseSelectFriendsDialog.class);

    private BaseSelectFriendsDialog view = NULL_VIEW;

    private final BackgroundExecutor backgroundExecutor;
    private final ForegroundExecutor foregroundExecutor;
    private final BasePersonListViewPresenter subPresenter;
    private final ChatRepo repo;
    private byte[] pictureData;

    private List<Person> listToSend = new ArrayList<>();

    public SelectFriendsDialogPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor,  BasePersonListViewPresenter subPresenter, ChatRepo repo) {
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
        this.subPresenter = subPresenter;
        this.repo = repo;
        subPresenter.setDisplayType(PersonRepo.Type.FRIEND);
    }

    @Override
    public void attachView(BaseSelectFriendsDialog view) {
        this.view = view;
        backgroundExecutor.execute(() -> {
            subPresenter.requestUpdate(() -> {
                foregroundExecutor.execute(() -> {
                    view.hideLoading();
                });
            }, true);
        });
    }

    @Override
    public void detachView() {
        this.view = NULL_VIEW;
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public String getItem(int index) {
        return null;
    }

    @Override
    public void getItemInBackground(int index, OnGetItemCallback<String> callback) {
        throw new RuntimeException("Not Applicable.");
    }

    @Override
    public int getSize() {
        throw new RuntimeException("Not Applicable.");
    }

    @Override
    public void getSizeInBackground(OnGetSizeCallback callback) {
        throw new RuntimeException("Not Applicable.");
    }

    @Override
    public void attachAdapter(BasePersonListViewPresenter.BasePersonListAdapter adapter) {
        this.subPresenter.attachAdapter(adapter, setter());
        this.subPresenter.requestUpdate(() -> {
            //EMPTY
        }, true);
    }

    @Override
    public void detachAdapter() {
        this.subPresenter.detachAdapter();
    }

    private BasePersonListViewPresenter.PersonListCardView.InitialStateSetter setter() {
        return (person, cardView) -> {
            cardView.resetActionButtons();
            cardView.setStateIcon(BasePersonListViewPresenter.BaseIcons.PERSON_STATE_FRIEND);
            cardView.addActionButton("add", BasePersonListViewPresenter.BaseIcons.ACTION_ADD, onAddClick());
        };
    }
    private BasePersonListViewPresenter.PersonListCardView.OnClickActionListener onAddClick() {
        return (person, cardView, presenter) -> {
            cardView.resetActionButtons();
            listToSend.add(person);
            view.enableSend();
            cardView.addActionButton("remove", BasePersonListViewPresenter.BaseIcons.ACTION_REMOVE, onRemoveClick());
        };
    }
    private BasePersonListViewPresenter.PersonListCardView.OnClickActionListener onRemoveClick() {
        return (person, cardView, presenter) -> {
            cardView.resetActionButtons();
            listToSend.remove(person);
            if(listToSend.size() == 0) {
                view.disableSend();
            }
            cardView.addActionButton("add", BasePersonListViewPresenter.BaseIcons.ACTION_ADD, onAddClick());
        };
    }

    @Override
    public void onSend() {
        backgroundExecutor.execute(() -> {
            Chat chat = new Chat(listToSend, User.getName(), Chat.ChatType.SENT_CHAT, null);
            chat.setChatData(pictureData);
            repo.sendChat(chat, (msg) -> {
                foregroundExecutor.execute(() -> {
                    //DONE
                    Log.d("UsChat", "Chat sent successfully!");
                    listToSend.clear();
                });
            });
        });

    }

    @Override
    public void updateRequested() {
        subPresenter.requestUpdate(() -> {
            //Empty
        }, true);
    }

    @Override
    public void attachPictureData(byte[] data) {
        this.pictureData = data;
    }
}
