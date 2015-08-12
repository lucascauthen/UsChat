package com.lucascauthen.uschat.domain.executor;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

/**
 * Created by lhc on 7/30/15.
 */
public class ForegroundExecutor implements Executor {
    private final Executor executor;

    public ForegroundExecutor() {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        executor = command -> {
            mainHandler.post(command);
        };
    }

    @Override
    public void execute(Runnable command) {
        executor.execute(command);
    }
}
