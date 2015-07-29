package com.lucascauthen.uschat.domain;



import com.lucascauthen.uschat.R;

import java.util.concurrent.TimeUnit;

import rx.Observable.Operator;
import rx.Subscriber;

/**
 * Created by lhc on 7/14/15.
 */
public class Waiter<T> implements Operator<T, T> {
    private final int timeout;
    private final TimeUnit unit;

    public Waiter(int timeout, TimeUnit unit) {
        this.timeout = timeout;
        this.unit = unit;
    }

    @Override
    public Subscriber<? super T> call(Subscriber<? super T> s) {
        return new Subscriber<T>(s) {
            @Override
            public void onCompleted() {
        /* add your own onCompleted behavior here, or just pass the completed notification through: */
                if(!s.isUnsubscribed()) {
                    s.onCompleted();
                }
            }

            @Override
            public void onError(Throwable t) {
        /* add your own onError behavior here, or just pass the error notification through: */
                if(!s.isUnsubscribed()) {
                    s.onError(t);
                }
            }

            @Override
            public void onNext(T item) {
                if(!s.isUnsubscribed()) {

                }
            }
        };
    }
}