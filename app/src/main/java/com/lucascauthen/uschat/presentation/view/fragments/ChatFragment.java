package com.lucascauthen.uschat.presentation.view.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.data.entities.Chat;
import com.lucascauthen.uschat.presentation.controller.ChatPresenter;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lhc on 7/1/15.
 */
public class ChatFragment extends Fragment{

    private ChatPresenter presenter;

    public ChatFragment() {
        //Required empty
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, null);
        ButterKnife.inject(this, v);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Executor backgroundExecutor = Executors.newFixedThreadPool(10);

        final Handler handler = new Handler();
        Executor foregroundExecutor = new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
        presenter = new ChatPresenter(Schedulers.io(), AndroidSchedulers.mainThread(), backgroundExecutor, foregroundExecutor);
    }

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }
}
