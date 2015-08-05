package com.lucascauthen.uschat.domain.scheduler;

import rx.Scheduler;

/**
 * Created by lhc on 7/30/15.
 */
public interface ForegroundScheduler {
    Scheduler getScheduler();
}
