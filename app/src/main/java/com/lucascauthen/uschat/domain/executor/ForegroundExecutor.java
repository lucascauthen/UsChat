package com.lucascauthen.uschat.domain.executor;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import java.util.concurrent.Executor;

/**
 * Created by lhc on 7/30/15.
 */
public class ForegroundExecutor implements Executor{
    private final Executor executor;

    public ForegroundExecutor(Context application) {
        Handler mainHandler = new Handler(application.getMainLooper());
        executor =  command -> {
            mainHandler.post(command);
        };
    }

    @Override
    public void execute(Runnable command) {
        executor.execute(command);
    }
}
