package com.lucascauthen.uschat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lucascauthen.uschat.Adapters.FriendsViewAdapter;
import com.lucascauthen.uschat.Adapters.PersonViewAdapter;
import com.lucascauthen.uschat.Chatting.Friend;
import com.lucascauthen.uschat.Chatting.Friendship;
import com.lucascauthen.uschat.Chatting.FriendshipStatus;
import com.lucascauthen.uschat.Chatting.Person;
import com.lucascauthen.uschat.Events.Task;
import com.lucascauthen.uschat.Events.TaskListener;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class FriendsFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private LinearLayoutManager layoutManager;
    private FriendsViewAdapter friendsAdapter;
    private PersonViewAdapter personAdapter;
    private RecyclerView recyclerView;
    private EditText searchEditText;
    private TextView friendsListTitle;
    private InputMethodManager imm;
    //TODO: Change this to be loaded via serialization on startup
    private ArrayList<Person> personList = new ArrayList<Person>();
    private ArrayList<Friend> friendList = new ArrayList<Friend>();
    private ArrayList<Friendship> friendShipList = new ArrayList<Friendship>();
    private boolean showFriends = true;
    private boolean isStarted = false;


    private SwipeRefreshLayout refreshLayout;

    public static FriendsFragment newInstance(String param1, String param2) {
        FriendsFragment fragment = new FriendsFragment();
        return fragment;
    }

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        recyclerView = (RecyclerView)view.findViewById(R.id.friend_rv);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        ((RecyclerView)view.findViewById(R.id.friend_rv)).setLayoutManager(layoutManager);

        this.refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshFriendsList);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });
        if(!isStarted) {
            loadPersonsFromCloud(null); //Null to prevent the view from being updated twice
            loadFriendsFromCloud();
            isStarted = true;
        }


        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        searchEditText = (EditText)view.findViewById(R.id.search_edit_text);
        friendsListTitle = (TextView)view.findViewById(R.id.friends_list_title_text_view);
        searchEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
                }
            }
        });
        ((ImageButton)view.findViewById(R.id.friend_search_button)).setOnClickListener(new View.OnClickListener() {
            //Toggles the search function:
            @Override
            public void onClick(View v) {
                //If you were searching, stop
                if(searchEditText.getVisibility() == View.VISIBLE) {
                    searchEditText.setVisibility(View.GONE);
                    friendsListTitle.setVisibility(View.VISIBLE);
                    searchEditText.getRootView().clearFocus();
                    searchEditText.setText("");
                    imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
                    showFriends = true;
                } else { //If you were not searching, start
                    searchEditText.setVisibility(View.VISIBLE);
                    friendsListTitle.setVisibility(View.GONE);
                    imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
                    searchEditText.setEnabled(true);
                    searchEditText.requestFocus();
                    showFriends = false;
                }
                loadRecyclerView();
            }
        });

        return view;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }
    public void loadRecyclerView() {
        if(showFriends) {
            this.friendsAdapter = new FriendsViewAdapter(friendList);
            this.recyclerView.setAdapter(friendsAdapter);
        } else { //Show persons
            this.personAdapter = new PersonViewAdapter(personList);
            this.recyclerView.setAdapter(personAdapter);
        }
    }
    public void refreshItems() {
        if(showFriends) {
            loadFriendsFromCloud();
        } else {
            loadPersonsFromCloud();
        }
    }
    public void loadFriendsFromCloud(TaskListener listener) {
        friendList = new ArrayList<Friend>();
        new Task(listener, new Runnable() {
            @Override
            public void run() {
                loadFriendshipsFromCloud(new TaskListener() {
                    @Override
                    public void finished(boolean result) {
                        for(Friendship friendship : friendShipList) {
                            friendList.add(new Friend(friendship.getName(), friendship.getStatus()));
                        }
                    }
                });
            }
        }).execute();
    }
    public void loadFriendsFromCloud() {
        loadFriendsFromCloud(new TaskListener() {
            @Override
            public void finished(boolean result) {
                onFriendsLoaded();
            }
        });
    }
    public void onFriendsLoaded() {
        onLoadComplete();
        loadFriendshipsFromCloud();
        loadRecyclerView();
    }
    public void onLoadComplete() {
        refreshLayout.setRefreshing(false);
    }

    public void loadPersonsFromCloud(TaskListener taskListener) {
        personList = new ArrayList<Person>();
        final ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        new Task(taskListener, new Runnable() {
            @Override
            public void run() {
                try {
                    List<ParseUser> list = query.find();
                    for (ParseUser user : list) {
                        personList.add(new Person(user.getString("username")));
                    }
                } catch (ParseException e) {
                    Log.d("friends", "Something went wrong retrieving persons: " + e.getMessage());
                }
            }
        }).execute();
    }
    public void loadPersonsFromCloud() {
        loadPersonsFromCloud(new TaskListener() {
            @Override
            public void finished(boolean result) {
                onPersonsLoaded();
            }
        });
    }

    public void onPersonsLoaded() { //This is the default result after a load -> the view gets updated
        onLoadComplete();
        loadFriendshipsFromCloud();
        loadRecyclerView();
    }
    //TODO: Rename to something more descriptive. It don't just 'get' the friendships from the cloud
    //Gets a list of ParseObjects from the cloud with all of the friendships between people and the current user
    public void loadFriendshipsFromCloud() {
        loadFriendshipsFromCloud(new TaskListener() {
            @Override
            public void finished(boolean result) {
                onFriendshipsLoaded();
            }
        });
    }
    public void loadFriendshipsFromCloud(TaskListener listener) {
        final ParseQuery<ParseObject> toQuery = ParseQuery.getQuery("FriendRequest");
        final ParseQuery<ParseObject> fromQuery = ParseQuery.getQuery("FriendRequest");

        toQuery.whereEqualTo("requestTo", ParseUser.getCurrentUser().getUsername());
        fromQuery.whereEqualTo("requestFrom", ParseUser.getCurrentUser().getUsername());

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(toQuery);
        queries.add(fromQuery);
        final ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
        new Task(listener, new Runnable() {
            @Override
            public void run() {
                try {
                    List<ParseObject> list = mainQuery.find();
                    friendShipList = new ArrayList<Friendship>();
                    for(ParseObject friendship : list) {
                        String otherPerson = ""; //Person in a friendship that is not the current user
                        String from = ""; //Person who sent the request
                        String friendshipStatus = friendship.getString("type");
                        //If the name in the requestTo field is not the current user then it is the other person
                        boolean isFrom;
                        if(ParseUser.getCurrentUser().getUsername().equals(friendship.getString("requestTo"))) {
                            otherPerson = friendship.getString("requestFrom");
                            from = otherPerson;
                            isFrom = true;
                        } else { //This means the currentUser's name IS in the requestTo field, so we want the requestFrom field (because it has the other person)
                            otherPerson = friendship.getString("requestTo");
                            from = ParseUser.getCurrentUser().getUsername();
                            isFrom = false;
                        }

                        friendShipList.add(new Friendship(otherPerson, from, statusFactory(friendshipStatus, isFrom)));
                    }
                } catch (ParseException e) {
                    Log.d("friends", "Something went wrong loading friendship statuses: " + e.getMessage());
                }
            }
        }).execute();
    }
    public void onFriendshipsLoaded() {
        updateFriendsFriendshipStatus();
        updatePersonsFriendshipStatus();
        loadRecyclerView();
    }
    public FriendshipStatus statusFactory(String status, boolean isFrom) {
        if(status.equals(getString(R.string.database_friendship_status_requested))) {
            if(isFrom) {
                return FriendshipStatus.REQUEST_RECIEVED;
            }
            return FriendshipStatus.REQUEST_SENT;
        } else if(status.equals(getString(R.string.database_friendship_status_friends))) {
            return FriendshipStatus.FRIENDS;
        } else if(status.equals(getString(R.string.database_friendship_status_notfriends))) {
            return FriendshipStatus.NOT_FRIENDS;
        }
        return FriendshipStatus.NOT_FRIENDS; //default
    }

    public void updateFriendsFriendshipStatus() {
        //Update friendship statuses for friends
        for(Friendship friendship : friendShipList) {
            for (Friend friend : friendList) {
                if(friend.getName().equals(friendship.getName())) {
                    friend.setFriendShipStatus(friendship.getStatus());
                }
            }
        }

    }
    public void updatePersonsFriendshipStatus() {

        for (Person person : personList) {
            for(Friendship friendship : friendShipList) {
                if(person.getName().equals(friendship.getName())) {
                    person.setFriendShipStatus(friendship.getStatus());
                    person.setRequestFrom(friendship.getFrom());
                }
            }
            //For any person that does not have a status, set it to not friends
            if(!person.getIsStatusLoaded()) {
                person.setFriendShipStatus(FriendshipStatus.NOT_FRIENDS);
            }
        }
    }
    public void onPersonSettingsRequest(int personIndex) {

        Dialog settingsDialog = settingsDialogFactory(personIndex);
        settingsDialog.show();
    }
    public Dialog settingsDialogFactory(final int personIndex) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_settings);
        dialog.setTitle("What would you like to do?");
        Button button1 = (Button)dialog.findViewById(R.id.dialog_button_1);
        Button button2 = (Button)dialog.findViewById(R.id.dialog_button_2);
        Button closeButton = (Button)dialog.findViewById(R.id.dialog_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        switch(personList.get(personIndex).getFriendshipStatus()) {
            case FRIENDS:
                //Remove Friend from list
                button1.setVisibility(View.VISIBLE);
                button1.setText(getString(R.string.dialog_remove_friend));
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        requestRemoveFriend(personIndex);
                    }
                });
                break;
            case NOT_FRIENDS:
                //Add friend
                button1.setVisibility(View.VISIBLE);
                button1.setText(getString(R.string.dialog_add_friend));
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        requestAddFriend(personIndex);
                    }
                });
                break;
            case REQUEST_RECIEVED:
                button1.setVisibility(View.VISIBLE);
                button1.setText(getString(R.string.dialog_accept_request));
                button2.setVisibility(View.VISIBLE);
                button2.setText(getString(R.string.dialog_reject_request));
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        requestAcceptFriendRequest(personIndex);
                    }
                });
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        requestRejectFriendRequest(personIndex);
                    }
                });
                break;
            case REQUEST_SENT:
                button1.setVisibility(View.VISIBLE);
                button1.setText(getString(R.string.dialog_cancel_request));
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        requestCancelFriendRequest(personIndex);
                    }
                });
                break;
            case NOT_LOADED: //These two are the same:
            default:

        }
        return dialog;
    }
    public void requestRemoveFriend(int personIndex) {
        Log.d("friends", "Requesting to remove " + personList.get(personIndex).getName());
        Person person = personList.get(personIndex);
        //TODO: Fix this.. Its kinda inefficient
        if(person.getName().equals(person.getRequestFrom())) { //This means the person was sent a request
            deleteFriendRequest(person.getName(), ParseUser.getCurrentUser().getUsername());
        } else {
            deleteFriendRequest(ParseUser.getCurrentUser().getUsername(), person.getName());
        }
    }
    public void requestCancelFriendRequest(int personIndex) {
        Log.d("friends", "Requesting to cancel request to " + personList.get(personIndex).getName());
        Log.d("friends", "Requesting to reject request from " + personList.get(personIndex).getName());
        deleteFriendRequest(ParseUser.getCurrentUser().getUsername() ,personList.get(personIndex).getName() );
    }
    public void requestAcceptFriendRequest(int personIndex) {
        Log.d("friends", "Requesting to accept request from " + personList.get(personIndex).getName());
        requestRejectFriendRequest(personIndex);
        ParseObject newFriend = new ParseObject("FriendRequest");
        newFriend.put("requestFrom", ParseUser.getCurrentUser().getUsername());
        newFriend.put("requestTo", personList.get(personIndex).getName());
        newFriend.put("type", getString(R.string.database_friendship_status_friends));
        newFriend.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                refreshItems();
            }
        });
    }
    public void requestRejectFriendRequest(int personIndex) {
        Log.d("friends", "Requesting to reject request from " + personList.get(personIndex).getName());
        deleteFriendRequest(personList.get(personIndex).getName(), ParseUser.getCurrentUser().getUsername());
    }
    public void deleteFriendRequest(String from, String to) {
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendRequest");
        query.whereEqualTo("requestTo", to);
        query.whereEqualTo("requestFrom", from);
        new Task(null, new Runnable() {
            @Override
            public void run() {
                try {
                    List<ParseObject> list = query.find();
                    if(!list.isEmpty()) {
                        ParseObject request = list.get(0);
                        request.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                refreshItems();
                            }
                        });
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }).execute();
    }
    public void requestAddFriend(int personIndex) {
        Log.d("friends", "Requesting to add " + personList.get(personIndex).getName());
        ParseObject newFriend = new ParseObject("FriendRequest");
        newFriend.put("requestFrom", ParseUser.getCurrentUser().getUsername());
        newFriend.put("requestTo", personList.get(personIndex).getName());
        newFriend.put("type", getString(R.string.database_friendship_status_requested));
        newFriend.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                refreshItems();
            }
        });
    }
    public List<Friend> getFriends() {
        List<Friend> realFriends = new ArrayList<Friend>();
        for(Friend friend : friendList) {
            if(friend.getFriendshipStatus().equals(FriendshipStatus.FRIENDS)) {
                realFriends.add(friend);
            }
        }
        return realFriends;
    }
}
