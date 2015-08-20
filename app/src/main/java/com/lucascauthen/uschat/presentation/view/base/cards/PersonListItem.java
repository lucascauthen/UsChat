package com.lucascauthen.uschat.presentation.view.base.cards;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.data.entities.Person;
import com.lucascauthen.uschat.data.repository.user.PersonRepo;
import com.lucascauthen.uschat.presentation.presenters.ListPresenter;
import com.lucascauthen.uschat.presentation.view.base.ListView;

public interface PersonListItem {
    void addActionButton(String key, int iconId, OnClickActionListener listener);

    void removeActionButton(String key);

    void resetActionButtons();

    void changeIconState(String key, int iconId);

    void setStateIcon(int iconId);

    void showLoading();

    void hideLoading();

    interface OnClickActionListener {
        void onClick(Person person, PersonListItem item);
    }


    interface PersonListPresenter {
        void sendAction(Person person, BaseActions action, PersonRepo.OnCompleteAction callback);

        void onActionComplete(Runnable completeFunction);
    }

    interface OnClickListener extends ListView.OnClickListener<Person, PersonListItem, ListPresenter<Person, Person.PersonType, PersonListItem>> {
        //This redefines the type to reduce boilerplate
    }

    interface InitialStateSetter extends ListView.InitialStateSetter<Person, PersonListItem> {
        //This redefines the type to reduce boilerplate
    }
    enum BaseActions {
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
        private static PersonListItem.OnClickActionListener cancelRequest(PersonListPresenter presenter) {
            return (person, cardView) -> {
                cardView.showLoading();
                cardView.setStateIcon(BaseIcons.PERSON_STATE_NOT_FRIEND);
                cardView.resetActionButtons();
                cardView.addActionButton("addFriend", BaseIcons.ACTION_ADD, addFriend(presenter));
                presenter.sendAction(person, BaseActions.CANCEL_REQUEST, (msg) -> {
                    presenter.onActionComplete(() -> {
                        cardView.hideLoading();
                    });
                });
            };
        }

        private static PersonListItem.OnClickActionListener addFriend(PersonListPresenter presenter) {
            return (person, cardView) -> {
                cardView.showLoading();
                cardView.setStateIcon(BaseIcons.PERSON_STATE_SENT_REQUEST);
                cardView.resetActionButtons();
                cardView.addActionButton("cancelRequest", BaseIcons.ACTION_REMOVE, cancelRequest(presenter));
                presenter.sendAction(person, BaseActions.ADD_FRIEND, (msg) -> {
                    presenter.onActionComplete(() -> {
                        cardView.hideLoading();
                    });
                });
            };
        }

        private static PersonListItem.OnClickActionListener removeFriend(PersonListPresenter presenter) {
            return (person, cardView) -> {
                cardView.showLoading();
                cardView.setStateIcon(BaseIcons.PERSON_STATE_NOT_FRIEND);
                cardView.resetActionButtons();
                cardView.addActionButton("addFriend", BaseIcons.ACTION_ADD, addFriend(presenter));
                presenter.sendAction(person, BaseActions.REMOVE_FRIEND, (msg) -> {
                    presenter.onActionComplete(() -> {
                        cardView.hideLoading();
                    });
                });

            };
        }

        private static PersonListItem.OnClickActionListener acceptRequest(PersonListPresenter presenter) {
            return (person, cardView) -> {
                cardView.showLoading();
                cardView.setStateIcon(BaseIcons.PERSON_STATE_FRIEND);
                cardView.resetActionButtons();
                cardView.addActionButton("removeFriend", BaseIcons.ACTION_REMOVE, removeFriend(presenter));
                presenter.sendAction(person, BaseActions.ACCEPT_REQUEST, (msg) -> {
                    presenter.onActionComplete(() -> {
                        cardView.hideLoading();
                    });
                });
            };
        }

        private static PersonListItem.OnClickActionListener rejectRequest(PersonListPresenter presenter) {
            return (person, cardView) -> {
                cardView.showLoading();
                cardView.setStateIcon(BaseIcons.PERSON_STATE_NOT_FRIEND);
                cardView.resetActionButtons();
                cardView.addActionButton("addFriend", BaseIcons.ACTION_ADD, addFriend(presenter));
                presenter.sendAction(person, BaseActions.REJECT_REQUEST, (msg) -> {
                    presenter.onActionComplete(() -> {
                        cardView.hideLoading();
                    });
                });
            };
        }

        public static PersonListItem.InitialStateSetter defaultSetter(PersonListPresenter presenter) {
            return (person, cardView) -> {
                cardView.resetActionButtons(); //Ensures that there is not action buttons assigned
                switch (person.state()) {
                    case SENT_REQUEST:
                        cardView.setStateIcon(BaseIcons.PERSON_STATE_SENT_REQUEST);
                        cardView.addActionButton("cancelRequest", BaseIcons.ACTION_REMOVE, cancelRequest(presenter));
                        break;
                    case RECEIVED_REQUEST:
                        cardView.setStateIcon(BaseIcons.PERSON_STATE_RECEIVED_REQUEST);
                        cardView.addActionButton("acceptRequest", BaseIcons.ACTION_ACCEPT, acceptRequest(presenter));
                        cardView.addActionButton("rejectRequest", BaseIcons.ACTION_REMOVE, rejectRequest(presenter));
                        break;
                    case FRIEND:
                        cardView.setStateIcon(BaseIcons.PERSON_STATE_FRIEND);
                        cardView.addActionButton("removeFriend", BaseIcons.ACTION_REMOVE, removeFriend(presenter));
                        break;
                    case NOT_FRIEND:
                        cardView.setStateIcon(BaseIcons.PERSON_STATE_NOT_FRIEND);
                        cardView.addActionButton("addFriend", BaseIcons.ACTION_ADD, addFriend(presenter));
                        break;
                    default:
                        //EMPTY
                }
            };
        }
    }
}

