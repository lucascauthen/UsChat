package com.lucascauthen.uschat.presentation.controller;

import android.support.v7.widget.RecyclerView;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.data.repository.Repo;
import com.lucascauthen.uschat.presentation.view.adapters.PersonViewAdapter;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Scheduler;
import rx.android.widget.OnTextChangeEvent;
import rx.schedulers.Schedulers;

/**
 * Created by lhc on 6/25/15.
 */
public class FriendsPresenter {
    private static final FriendsCrudView NULL_VIEW = new NullFriendsCrudView();

    private final Scheduler backgroundScheduler;
    private final Scheduler foregroundScheduler;
    private final Executor backgroundExecutor;
    private final Executor foregroundExecutor;

    private FriendsCrudView view = NULL_VIEW;

    private Observable<OnTextChangeEvent> searchTextObservable;


    private PersonViewPresenter personSearchViewPresenter;
    private PersonViewPresenter friendViewPresenter;

    private PersonViewAdapter personSearchAdapter;
    private PersonViewAdapter friendViewAdapter;

    //Used for both the friend/person view because they have the same functionality
    PersonViewAdapter.PersonViewHolder.InitialStateSetter friendStateSetter = new PersonViewAdapter.PersonViewHolder.InitialStateSetter() {
        @Override
        public int getState(Person person) {
            boolean isMyFriend = Person.getCurrentUser().isFriend(person.getName());
            boolean isTheirFriend = person.isFriend();

            if(isMyFriend && isTheirFriend) {
                return 3; //Friends
            } else if(isMyFriend) {
                return 1; //Request sent
            } else if(isTheirFriend) {
                return 2; //Request received
            } else {
                return 0; //Can add as friend
            }
        }
    };

    PersonViewPresenter.OnPersonSettingClickedListener onClickPersonAction = new PersonViewPresenter.OnPersonSettingClickedListener() {
        @Override
        public int onClick(Person person, int stateBeforeClick) {
            int newState = getStateChange(stateBeforeClick);
            //Send update
            backgroundExecutor.execute(() -> personSearchViewPresenter.updateView(person));
            //Return the new state
            return newState;
        }
    };
    PersonViewPresenter.OnPersonSettingClickedListener onClickFriendAction = new PersonViewPresenter.OnPersonSettingClickedListener() {
        @Override
        public int onClick(Person person, int stateBeforeClick) {
            int newState = getStateChange(stateBeforeClick);
            //Send update
            backgroundExecutor.execute(() -> friendViewPresenter.updateView(person));

            //Return the new state
            return newState;

        }
    };
    private int getStateChange(int state) {
        int newState = -1;
        //Find what the state should be
        switch(state) {
            case 0:
                newState = 1;
                break;
            case 1:
                newState = 0;
                break;
            case 2:
                newState = 3;
                break;
            case 3:
                newState = 2;
                break;

            default:
                //NONE
        }
        return newState;
    }


    public FriendsPresenter(Scheduler backgroundScheduler, Scheduler foregroundScheduler, Executor backgroundExecutor, Executor foregroundExecutor) {
        this.backgroundScheduler = backgroundScheduler;
        this.foregroundScheduler = foregroundScheduler;
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
        //Probably should be provided with DI
        personSearchViewPresenter = new PersonViewPresenter(backgroundScheduler, foregroundScheduler, backgroundExecutor, foregroundExecutor);
        friendViewPresenter = new PersonViewPresenter(backgroundScheduler, foregroundScheduler, backgroundExecutor, foregroundExecutor);

        int[] iconPack = {
                R.drawable.ic_action_add_box, //0
                R.drawable.ic_action_request_made, //1
                R.drawable.ic_action_request_received, //2
                R.drawable.ic_action_check //3
        };




        //Same
        friendViewAdapter = new PersonViewAdapter(foregroundExecutor, friendStateSetter, iconPack);
        personSearchAdapter = new PersonViewAdapter(foregroundExecutor, friendStateSetter, iconPack);

        personSearchViewPresenter.attachAdapter(personSearchAdapter);
        friendViewPresenter.attachAdapter(friendViewAdapter);

        personSearchViewPresenter.attachClickListener(onClickPersonAction);
        friendViewPresenter.attachClickListener(onClickFriendAction);
    }

    public void attachView(FriendsCrudView v) {
        if (v != null) {
            this.view = v;
            searchTextObservable = view.bindSearchField();
            searchTextObservable
                    .map(value -> {
                        foregroundExecutor.execute(() -> view.showSearchLoading());
                        return value;
                    })
                    .debounce(1000, TimeUnit.MILLISECONDS, Schedulers.newThread())
                    .map(event -> event.text().toString())
                    .filter(text -> {
                        if (text.equals("")) {
                            personSearchViewPresenter.updateView((Repo.Request)null); //Cast required otherwise it is ambiguous
                            foregroundExecutor.execute(() -> view.hideSearchLoading());
                            return false;

                        }
                        return true;
                    })
                    .observeOn(backgroundScheduler)
                    .subscribe(query -> {
                        personSearchViewPresenter.updateView(new Repo.Request(true, query));
                        foregroundExecutor.execute(() -> view.hideSearchLoading());
                    });
        }
    }

    public void detachView() {
        this.view = NULL_VIEW;
    }

    public RecyclerView.Adapter getFriendAdapter() {
        return friendViewAdapter;
    }

    public RecyclerView.Adapter getPersonAdapter() {
        return personSearchAdapter;
    }

    public void onPersonSwipeRefresh(String query) {
        backgroundExecutor.execute(() -> {
            Person.refreshCurrentUser();
            friendViewPresenter.updateView((Repo.Request) null); //Deletes the entries
            foregroundExecutor.execute(() -> view.refreshSearch());
        });
    }

    public void onFriendsSwipeRefresh() {
        backgroundExecutor.execute(() -> {
            Person.refreshCurrentUser();
            List<String> friends = Person.getCurrentUser().getFriends();
            if(friends.size() > 0) {
                friendViewPresenter.updateView(new Repo.Request(true, Person.getCurrentUser().getFriends()));
            } else {
                friendViewPresenter.updateView((Repo.Request)null);
            }
            foregroundExecutor.execute(() -> view.hideSearchLoading());
        });
    }

    public void onSearchClicked() {

    }

    public interface FriendsCrudView {
        Observable<OnTextChangeEvent> bindSearchField();

        void enableSearch();

        void disableSearch();

        void showSearchLoading();

        void hideSearchLoading();

        void showRefreshLoading();

        void hideRefreshLoading();

        void refreshSearch();

    }


    public static class NullFriendsCrudView implements FriendsCrudView {

        @Override
        public Observable<OnTextChangeEvent> bindSearchField() {
            return null;
        }

        @Override
        public void enableSearch() {

        }

        @Override
        public void disableSearch() {

        }

        @Override
        public void showSearchLoading() {

        }

        @Override
        public void hideSearchLoading() {

        }


        @Override
        public void showRefreshLoading() {

        }

        @Override
        public void hideRefreshLoading() {

        }

        @Override
        public void refreshSearch() {

        }

    }


}
