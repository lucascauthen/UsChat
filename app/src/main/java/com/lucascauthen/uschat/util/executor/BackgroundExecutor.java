package com.lucascauthen.uschat.util.executor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by lhc on 7/30/15.
 */
public class BackgroundExecutor implements Executor {
    private final Executor executor;

    public BackgroundExecutor() {
        executor = Executors.newFixedThreadPool(10);
    }

    @Override
    public void execute(Runnable command) {
        executor.execute(command);
    }
}
