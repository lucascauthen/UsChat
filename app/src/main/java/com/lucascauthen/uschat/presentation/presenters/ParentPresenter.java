package com.lucascauthen.uschat.presentation.presenters;

public interface ParentPresenter<S> {
    void attachSubView(S view);
}
