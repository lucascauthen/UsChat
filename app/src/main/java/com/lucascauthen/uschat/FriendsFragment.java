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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
            refreshItems();
            showFriends = false;
            refreshItems();
            showFriends = true;
            isStarted = false;
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
        //Determines if it should show persons for friends
        if(showFriends) {
            getFriendsFromCloud();
        } else {
            getPersonsFromCloud();
        }
    }
    public void getFriendsFromCloud() {
        //TODO: Change this function to return objects from parse:
        friendList = new ArrayList<Friend>();

        //Stops the loading...
        onItemsLoadComplete();
    }
    public void onItemsLoadComplete() {
        getFriendshipsFromCloud();
        if(showFriends) {
            updateFriendsFriendshipStatus();
        } else {
            updatePersonsFriendshipStatus();
        }
        loadRecyclerView();
        refreshLayout.setRefreshing(false);
    }

    public void getPersonsFromCloud() {
        personList = new ArrayList<Person>();
        final ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    for (ParseUser user : objects) {
                        personList.add(new Person(user.getString("username")));
                    }
                    //TODO: Kill this
                    personList.add(new Person("Test1", FriendshipStatus.FRIENDS));
                    personList.add(new Person("Test2", FriendshipStatus.REQUEST_RECIEVED));
                    personList.add(new Person("Test3", FriendshipStatus.REQUEST_SENT));
                } else {
                    Log.d("friends", "Something went wrong retrieving persons: " + e.getMessage());
                }
                onItemsLoadComplete();
            }
        });
    }
    //TODO: Rename to something more descriptive. It don't just 'get' the friendships from the cloud
    //Gets a list of ParseObjects from the cloud with all of the friendships between people and the current user
    public void  getFriendshipsFromCloud() {
        final ParseQuery<ParseObject> toQuery = ParseQuery.getQuery("FriendRequest");
        final ParseQuery<ParseObject> fromQuery = ParseQuery.getQuery("FriendRequest");

        toQuery.whereEqualTo("requestTo", ParseUser.getCurrentUser().getUsername());
        fromQuery.whereEqualTo("requestFrom", ParseUser.getCurrentUser().getUsername());

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(toQuery);
        queries.add(fromQuery);
        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> results, ParseException e) {
                if (e == null) {
                    updateFriendShipStatuses(results);
                } else {
                    Log.d("friends", "Something went wrong loading friendship statuses: " + e.getMessage());
                }
            }
        });
    }
    //Formats the information from the ParseObjects into the friendships arrayList
    public void updateFriendShipStatuses(List<ParseObject> list) {
        friendShipList = new ArrayList<Friendship>();
        for(ParseObject friendship : list) {
            String otherPerson = ""; //Person in a friendship that is not the current user
            String friendshipStatus = friendship.getString("type");
            //If the name in the requestTo field is not the current user then it is the other person
            boolean isFrom;
            if(friendship.getString("requestTo") != ParseUser.getCurrentUser().getUsername()) {
                otherPerson = friendship.getString("requestTo");
                isFrom = false;
            } else { //This means the currentUser's name IS in the requestTo field, so we want the requestFrom field (because it has the other person)
                otherPerson = friendship.getString("requestFrom");
                isFrom = true;
            }

            friendShipList.add(new Friendship(otherPerson, statusFactory(friendshipStatus, isFrom)));
        }
        onUpdateFriendShipStatusesComplete();
    }
    public FriendshipStatus statusFactory(String status, boolean isFrom) {
        if(status.equals("requested")) {
            if(isFrom) {
                return FriendshipStatus.REQUEST_SENT;
            }
            return FriendshipStatus.REQUEST_RECIEVED;
        } else if(status.equals("friends")) {
            return FriendshipStatus.FRIENDS;
        } else if(status.equals("notFriends")) {
            return FriendshipStatus.NOT_FRIENDS;
        }
        return FriendshipStatus.NOT_FRIENDS; //default
    }

    public void onUpdateFriendShipStatusesComplete() {
        //TODO: Work on this
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
    }
    public void requestCancelFriendRequest(int personIndex) {
        Log.d("friends", "Requesting to cancel request to " + personList.get(personIndex).getName());
    }
    public void requestAcceptFriendRequest(int personIndex) {
        Log.d("friends", "Requesting to accept request from " + personList.get(personIndex).getName());
    }
    public void requestRejectFriendRequest(int personIndex) {
        Log.d("friends", "Requesting to reject request from " + personList.get(personIndex).getName());
    }
    public void requestAddFriend(int personIndex) {
        Log.d("friends", "Requesting to add " + personList.get(personIndex).getName());
    }
}
