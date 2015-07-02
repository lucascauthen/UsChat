package com.lucascauthen.uschat.presentation.view.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.controller.FriendsPresenter;
import com.lucascauthen.uschat.presentation.controller.SignupPresenter;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lhc on 7/1/15.
 */
public class FriendsFragment extends Fragment {

    private FriendsPresenter presenter;
    private LinearLayoutManager layoutManager;
    @InjectView(R.id.friend_rv)RecyclerView recyclerView;


    public FriendsFragment() {
        //Required empty
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Executor backgroundExecutor = Executors.newFixedThreadPool(10);

        final Handler handler = new Handler();
        Executor foregroundExecutor = new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
        presenter = new FriendsPresenter(Schedulers.io(), AndroidSchedulers.mainThread(), backgroundExecutor, foregroundExecutor);

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friends, null);
        ButterKnife.inject(this, v);
        recyclerView.setLayoutManager(layoutManager);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public static FriendsFragment newInstance() {
        return new FriendsFragment();
    }
}
