package com.lucascauthen.uschat.presentation.presenters;

import com.lucascauthen.uschat.presentation.view.base.ListView;

//  T - "type": what data type the list view holds
//  D - "display type": what subset of T the list view holds
//  V - "view": the view of each item

public interface ListPresenter<T, D, V> extends BasePresenter<ListView<T, D, V>> {
    T getItem(int index);

    void getItemInBackground(int position, GetItemCallBack<T> callback);

    int getSize();

    void getSizeInBackground(GetSizeCallback callback);

    void requestUpdate(ListView.OnUpdateCompleteCallback callback, boolean updateRepo);

    void setDisplayType(D type);

    void setFilterQuery(String query);

    void setClickListener(ListView.OnClickListener<T, V, ListPresenter<T, D, V>> listener);

    void setInitialStateSetter(ListView.InitialStateSetter<T, V> initialStateSetter);

    interface GetSizeCallback {
        void onGetSize(int size);
    }

    interface GetItemCallBack<T> {
        void onGetItem(T item);
    }

}
