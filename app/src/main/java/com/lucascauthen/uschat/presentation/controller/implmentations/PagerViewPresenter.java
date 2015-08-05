package com.lucascauthen.uschat.presentation.controller.implmentations;

import com.lucascauthen.uschat.presentation.controller.base.BasePagerViewPresenter;
import com.lucascauthen.uschat.util.NullObject;

/**
 * Created by lhc on 7/30/15.
 */
public class PagerViewPresenter implements BasePagerViewPresenter {
    private static final PagerView NULL_VIEW = NullObject.create(PagerView.class);

    private PagerView view = NULL_VIEW;

    @Override
    public void detachView() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void attachView(PagerView view) {

    }
}
