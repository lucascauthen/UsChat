package com.lucascauthen.uschat.presentation.controller.base;

/**
 * Created by lhc on 7/30/15.
 */
public interface BaseRecyclerViewPresenter<T, A> {

    T getItem(int index);

    void getItemInBackground(int index, OnGetItemCallback<T> callback);

    int getSize();

    void getSizeInBackground(OnGetSizeCallback callback);

    void attachAdapter(A adapter);

    void detachAdapter();

    interface OnGetItemCallback<T> {
        void onGetItem(T item);
    }

    interface OnGetSizeCallback {
        void onGetSize(int size);
    }
}
