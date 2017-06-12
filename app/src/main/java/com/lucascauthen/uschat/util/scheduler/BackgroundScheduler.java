package com.lucascauthen.uschat.util.scheduler;

import rx.Scheduler;

/**
 * Created by lhc on 7/30/15.
 */
public interface BackgroundScheduler {
    Scheduler getScheduler();
}
