package com.lucascauthen.uschat.presentation.view.fragments.newfrag;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lucascauthen.uschat.R;
import com.lucascauthen.uschat.presentation.controller.base.BaseTabViewPresenter;

import butterknife.ButterKnife;
import rx.Observable;
import rx.android.widget.OnTextChangeEvent;
import rx.android.widget.WidgetObservable;


/**
 * Created by lhc on 7/1/15.
 */
public class TabFragment extends Fragment  implements BaseTabViewPresenter.BaseTabView{

    RecyclerView recyclerView;
    ImageButton searchButton;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar loading;
    TextView title;
    EditText searchField;

    private BaseTabViewPresenter presenter;

    private InputMethodManager imm;
    private LinearLayoutManager layoutManager;



    public TabFragment() {
        //Required empty
    }

    public static TabFragment newInstance(BaseTabViewPresenter presenter) {
        TabFragment f = new TabFragment();
        f.presenter = presenter;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friends, null);
        ButterKnife.inject(this, v);
        presenter.attachView(this);
        recyclerView.setLayoutManager(layoutManager);


        searchField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    imm.showSoftInput(searchField, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(() -> {

        });
        return v;
    }
    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    /*
    @OnClick(R.id.friend_search_button)
    void onSearchClick() {
        if(isShowingFriends) {
            searchField.setVisibility(View.VISIBLE);
            title.setVisibility(View.GONE);
            imm.showSoftInput(searchField, InputMethodManager.SHOW_IMPLICIT);
            searchField.setEnabled(true);
            searchField.requestFocus();
            isShowingFriends = false;
            this.recyclerView.setAdapter(personSearchAdapter);
        } else {
            searchField.setVisibility(View.GONE);
            title.setVisibility(View.VISIBLE);
            searchField.getRootView().clearFocus();
            imm.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
            isShowingFriends = true;
            this.recyclerView.setAdapter(friendsListAdapter);
        }
    }
    */


    @Override
    public Observable<OnTextChangeEvent> bindPersonSearchObservable() {
        return WidgetObservable.text(searchField);
    }

    @Override
    public void reSendSearch() {
        this.searchField.setText(searchField.getText().toString());
    }
}
