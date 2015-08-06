package com.lucascauthen.uschat.presentation.controller.base;

import android.support.annotation.Nullable;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.data.repository.user.PersonRepo;

/**
 * Created by lhc on 7/30/15.
 */
public interface BasePersonListViewPresenter extends BaseRecyclerViewPresenter<Person, BasePersonListViewPresenter.BasePersonListAdapter> {

    PersonListCardView.InitialStateSetter getInitialStateSetter();

    void attachAdapter(BasePersonListAdapter adapter, PersonListCardView.InitialStateSetter setter);

    void setDisplayType(PersonRepo.Type type);

    void requestUpdate(BasePersonListAdapter.OnDoneLoadingCallback callback, boolean repoNeedUpdate);

    void sendAction(Person person, BaseActions action, PersonRepo.OnCompleteAction callback);

    void setQuery(@Nullable String query);

    interface BasePersonListAdapter {
        void notifyDataUpdate(OnDoneLoadingCallback callback);

        void attachPresenter(BasePersonListViewPresenter presenter);

        interface OnDoneLoadingCallback {
            void done();
        }
    }

    interface PersonListCardView {

        void addActionButton(String key, int iconId, OnClickActionListener listener);

        void removeActionButton(String key);

        void resetActionButtons();

        void changeIconState(String key, int iconId);

        void setStateIcon(int iconId);

        void showLoading();

        void hideLoading();

        interface OnClickActionListener {
            void onClick(Person person, PersonListCardView view, BasePersonListViewPresenter presenter);
        }

        interface InitialStateSetter {
            void setState(Person person, PersonListCardView view);
        }
    }
    enum BaseActions{
        ADD_FRIEND {
            @Override
            public void execute(Person person, PersonRepo repo, PersonRepo.OnCompleteAction callback) {
                repo.sendFriendRequest(person, callback);
            }
        },

        ACCEPT_REQUEST {
            @Override
            public void execute(Person person, PersonRepo repo, PersonRepo.OnCompleteAction callback) {
                repo.acceptReceivedRequest(person, callback);
            }
        },
        REJECT_REQUEST {
            @Override
            public void execute(Person person, PersonRepo repo, PersonRepo.OnCompleteAction callback) {
                repo.rejectReceivedRequest(person, callback);
            }
        },

        CANCEL_REQUEST {
            @Override
            public void execute(Person person, PersonRepo repo, PersonRepo.OnCompleteAction callback) {
                repo.deleteSentRequest(person, callback);
            }
        },

        REMOVE_FRIEND {
            @Override
            public void execute(Person person, PersonRepo repo, PersonRepo.OnCompleteAction callback) {
                repo.removeFriend(person, callback);
            }
        };

        public abstract void execute(Person person, PersonRepo repo, PersonRepo.OnCompleteAction callback);
    }
    class BaseIcons {
        //State icons
        public static final int PERSON_STATE_FRIEND = R.drawable.ic_friend;
        public static final int PERSON_STATE_RECEIVED_REQUEST = R.drawable.ic_request_received;
        public static final int PERSON_STATE_NOT_FRIEND = R.drawable.ic_not_friends;
        public static final int PERSON_STATE_SENT_REQUEST = R.drawable.ic_request_sent;

        //Action button icons
        public static final int ACTION_ADD = R.drawable.ic_plus;
        public static final int ACTION_REMOVE = R.drawable.ic_remove;
        public static final int ACTION_ACCEPT = R.drawable.ic_check;
    }

    class BasePersonListBehavior {
        private static PersonListCardView.OnClickActionListener cancelRequest() {
            return (person, cardView, presenter) -> {
                cardView.showLoading();
                cardView.setStateIcon(BaseIcons.PERSON_STATE_NOT_FRIEND);
                cardView.resetActionButtons();
                cardView.addActionButton("addFriend", BaseIcons.ACTION_ADD, addFriend());
                presenter.sendAction(person, BaseActions.CANCEL_REQUEST, (msg) -> {
                    cardView.hideLoading();
                });
            };
        }
        private static PersonListCardView.OnClickActionListener addFriend() {
            return (person, cardView, presenter) -> {
                cardView.showLoading();
                cardView.setStateIcon(BaseIcons.PERSON_STATE_SENT_REQUEST);
                cardView.resetActionButtons();
                cardView.addActionButton("cancelRequest", BaseIcons.ACTION_REMOVE, cancelRequest());
                presenter.sendAction(person, BaseActions.ADD_FRIEND, (msg) -> {
                    cardView.hideLoading();
                });
            };
        }
        private static PersonListCardView.OnClickActionListener removeFriend() {
            return (person, cardView, presenter) -> {
                cardView.showLoading();
                cardView.setStateIcon(BaseIcons.PERSON_STATE_NOT_FRIEND);
                cardView.resetActionButtons();
                cardView.addActionButton("addFriend", BaseIcons.ACTION_ADD, addFriend());
                presenter.sendAction(person, BaseActions.REMOVE_FRIEND, (msg) -> {
                    cardView.hideLoading();
                });

            };
        }
        private static PersonListCardView.OnClickActionListener acceptRequest() {
            return (person, cardView, presenter) -> {
                cardView.showLoading();
                cardView.setStateIcon(BaseIcons.PERSON_STATE_FRIEND);
                cardView.resetActionButtons();
                cardView.addActionButton("removeFriend", BaseIcons.ACTION_REMOVE, removeFriend());
                presenter.sendAction(person, BaseActions.ACCEPT_REQUEST, (msg) -> {
                    cardView.hideLoading();
                });
            };
        }
        private static PersonListCardView.OnClickActionListener rejectRequest() {
            return (person, cardView, presenter) -> {
                cardView.showLoading();
                cardView.setStateIcon(BaseIcons.PERSON_STATE_NOT_FRIEND);
                cardView.resetActionButtons();
                cardView.addActionButton("addFriend", BaseIcons.ACTION_ADD, addFriend());
                presenter.sendAction(person, BaseActions.REJECT_REQUEST, (msg) -> {
                    cardView.hideLoading();
                });
            };
        }

        public static PersonListCardView.InitialStateSetter defaultSetter() {
            return (person, cardView) -> {
                cardView.resetActionButtons(); //Ensures that there is not action buttons assigned
                switch (person.state()) {
                    case SENT_REQUEST:
                        cardView.setStateIcon(BaseIcons.PERSON_STATE_SENT_REQUEST);
                        cardView.addActionButton("cancelRequest", BaseIcons.ACTION_REMOVE, cancelRequest());
                        break;
                    case RECIEVED_REQUEST:
                        cardView.setStateIcon(BaseIcons.PERSON_STATE_RECEIVED_REQUEST);
                        cardView.addActionButton("acceptRequest", BaseIcons.ACTION_ACCEPT, acceptRequest());
                        cardView.addActionButton("rejectRequest", BaseIcons.ACTION_REMOVE, rejectRequest());
                        break;
                    case FRIENDS:
                        cardView.setStateIcon(BaseIcons.PERSON_STATE_FRIEND);
                        cardView.addActionButton("removeFriend", BaseIcons.ACTION_REMOVE, removeFriend());
                        break;
                    case NOT_FRIENDS:
                        cardView.setStateIcon(BaseIcons.PERSON_STATE_NOT_FRIEND);
                        cardView.addActionButton("addFriend", BaseIcons.ACTION_ADD, addFriend());
                        break;
                    default:
                        //EMPTY
                }
            };
        }
    }
}
