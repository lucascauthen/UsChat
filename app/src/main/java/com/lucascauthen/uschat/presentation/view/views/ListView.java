package com.lucascauthen.uschat.presentation.view.views;

import com.lucascauthen.uschat.presentation.presenters.ListPresenter;

//  T - "type": what data type the list view holds
//  D - "display type": what subset of T the list view holds
//  V - "view": the view of each item

public interface ListView<T, D, V> {
    void notifyUpdate(OnUpdateCompleteCallback callback);

    void attachPresenter(ListPresenter<T, D, V> presenter);

    void setOnClickListener(OnClickListener<T, V, ListPresenter<T, D, V>> listener);

    void setInitialStateSetter(InitialStateSetter<T, V> initialStateSetter);

    interface OnUpdateCompleteCallback<T, I> {
        void done();
    }

    interface OnClickListener<T, V, P> {
        //  T - "type": what data type the list view holds
        //  V - "view": the view of each item
        //  P - "presenter": the list presenter
        void onClick(T itemData, V itemView, P presenter);
    }

    interface InitialStateSetter<T, V> {
        //  T - "type": what data type the list view holds
        //  V - "view": the view of each item
        void setState(T itemData, V itemView);
    }
}
