package com.lucascauthen.uschat.presentation.presenters;

import android.util.Log;
import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.data.entities.User;
import com.lucascauthen.uschat.data.repository.chat.ChatRepo;
import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.presentation.view.views.FriendSelectView;
import com.lucascauthen.uschat.presentation.view.views.ListView;
import com.lucascauthen.uschat.presentation.view.views.cards.PersonListItem;
import com.lucascauthen.uschat.util.NullObject;

import java.util.ArrayList;
import java.util.List;

public class FriendSelectPresenter implements BasePresenter<FriendSelectView>, ParentPresenter<ListView<Person, Person.PersonType, PersonListItem>> {
    private static final FriendSelectView NULL_VIEW = NullObject.create(FriendSelectView.class);
    private FriendSelectView view = NULL_VIEW;

    private final BackgroundExecutor backgroundExecutor;
    private final ForegroundExecutor foregroundExecutor;
    private final ListPresenter<Person, Person.PersonType, PersonListItem> subPresenter;
    private final ChatRepo repo;

    private List<Person> listToSend = new ArrayList<>();

    public FriendSelectPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, ListPresenter<Person, Person.PersonType, PersonListItem> subPresenter, ChatRepo repo) {
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
        this.subPresenter = subPresenter;
        this.repo = repo;
        subPresenter.setDisplayType(Person.PersonType.FRIEND);
    }

    public void onSwipe() {
        subPresenter.requestUpdate(() -> {
            view.hideLoading();
        }, true);
    }

    public void onSend(byte[] data) {
        backgroundExecutor.execute(() -> {
            Chat chat = new Chat(listToSend, User.getName(), Chat.ChatType.SENT, null);
            chat.setChatData(data);
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
    public void attachView(FriendSelectView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = NULL_VIEW;
    }

    @Override
    public void attachSubView(ListView<Person, Person.PersonType, PersonListItem> view) {
        view.setOnClickListener(new PersonListItem.OnClickListener() {
            @Override
            public void onClick(Person person, PersonListItem item, ListPresenter presenter) {
                //EMPTY
            }
        });
        view.setInitialStateSetter(new PersonListItem.InitialStateSetter() {
            @Override
            public void setState(Person itemData, PersonListItem itemView) {
                //TODO
            }
        });
        subPresenter.attachView(view);
    }
}
