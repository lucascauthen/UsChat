package com.lucascauthen.uschat.presentation.controller.base;

/**
 * Created by lhc on 7/30/15.
 */
public interface BasePresenter<T> {

    void attachView(T view);

    void detachView();

    void onPause();

    void onResume();
}
