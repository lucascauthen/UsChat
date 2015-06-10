package com.lucascauthen.uschat;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lucascauthen.uschat.Adapters.FriendsViewAdapter;
import com.lucascauthen.uschat.Chatting.Friend;

import java.util.ArrayList;
import java.util.List;


public class FriendsFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private LinearLayoutManager layoutManager;
    private FriendsViewAdapter adapter;
    private RecyclerView recyclerView;

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
        refreshItems();
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

    public void refreshItems() {
        //TODO: Load items
        this.adapter = new FriendsViewAdapter(loadFriends());
        recyclerView.setAdapter(adapter);
        onItemsLoadComplete();
    }
    public List<Friend> loadFriends() {
        //TODO: Change this function to return objects from parse:
        List<Friend> friends = new ArrayList<Friend>();
        //TODO: Change this function to return objects from parse:
        friends.add(new Friend("Lucas Cauthen", "Friend"));
        friends.add(new Friend("Alex Cauthen", "Request Sent"));
        friends.add(new Friend("Jimmy Cauthen", "Pending Request"));
        friends.add(new Friend("Josh Cauthen", "Friend"));
        friends.add(new Friend("Lucas Cauthen", "Friend"));
        friends.add(new Friend("Alex Cauthen", "Request Sent"));
        friends.add(new Friend("Jimmy Cauthen", "Pending Request"));
        friends.add(new Friend("Josh Cauthen", "Friend"));
        return friends;
    }
    public void onItemsLoadComplete() {
        //TODO: Update the adapter and notify data set changed
        refreshLayout.setRefreshing(false);
    }
}
