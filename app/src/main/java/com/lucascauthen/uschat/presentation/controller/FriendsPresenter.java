package com.lucascauthen.uschat.presentation.controller;

import java.util.concurrent.Executor;

import rx.Scheduler;

/**
 * Created by lhc on 6/25/15.
 */
public class FriendsPresenter implements Presenter {
    private final Scheduler backgroundScheduler;
    private final Scheduler foregroundScheduler;
    private final Executor backgroundExecutor;
    private final Executor foregroundExecutor;

    public FriendsPresenter(Scheduler backgroundScheduler, Scheduler foregroundScheduler, Executor backgroundExecutor, Executor foregroundExecutor) {
        this.backgroundScheduler = backgroundScheduler;
        this.foregroundScheduler = foregroundScheduler;
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }
}
