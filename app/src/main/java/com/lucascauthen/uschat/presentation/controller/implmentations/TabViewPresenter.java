package com.lucascauthen.uschat.presentation.controller.implmentations;

import com.lucascauthen.uschat.domain.executor.BackgroundExecutor;
import com.lucascauthen.uschat.domain.executor.ForegroundExecutor;
import com.lucascauthen.uschat.domain.scheduler.BackgroundScheduler;
import com.lucascauthen.uschat.presentation.controller.base.BaseTabViewPresenter;
import com.lucascauthen.uschat.presentation.controller.base.BasePagerViewPresenter;
import com.lucascauthen.uschat.util.NullObject;

/**
 * Created by lhc on 7/31/15.
 */
public class TabViewPresenter implements BaseTabViewPresenter {

    private final BackgroundExecutor backgroundExecutor;
    private final ForegroundExecutor foregroundExecutor;
    private final BackgroundScheduler backgroundScheduler;

    BasePagerViewPresenter.PagerViewChanger changer;

    private static final BaseTabView NULL_VIEW = NullObject.create(BaseTabView.class);
    BaseTabView view = NULL_VIEW;

    public TabViewPresenter(BackgroundExecutor backgroundExecutor, ForegroundExecutor foregroundExecutor, BackgroundScheduler backgroundScheduler) {
        this.backgroundExecutor = backgroundExecutor;
        this.foregroundExecutor = foregroundExecutor;
        this.backgroundScheduler = backgroundScheduler;
    }

    @Override
    public void attachView(BaseTabView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = NULL_VIEW;
    }

    @Override
    public void onPause() {
        //EMPTY
    }

    @Override
    public void onResume() {
        //EMPTY
    }

    @Override
    public void attachPageChanger(BasePagerViewPresenter.PagerViewChanger changer) {
        this.changer = changer;
    }
}
