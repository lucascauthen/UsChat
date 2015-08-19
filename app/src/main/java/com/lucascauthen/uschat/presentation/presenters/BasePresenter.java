package com.lucascauthen.uschat.presentation.presenters;

public interface BasePresenter<V> {
    void attachView(V view);

    void detachView();
}
