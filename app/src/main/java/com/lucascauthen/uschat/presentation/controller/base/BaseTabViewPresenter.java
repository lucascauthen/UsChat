package com.lucascauthen.uschat.presentation.controller.base;

import rx.Observable;
import rx.android.widget.OnTextChangeEvent;

/**
 * Created by lhc on 7/31/15.
 */
public interface BaseTabViewPresenter extends BasePresenter<BaseTabViewPresenter.BaseTabView>, BasePagerViewPresenter.PagerSubView {

    interface BaseTabView {

    }

}
