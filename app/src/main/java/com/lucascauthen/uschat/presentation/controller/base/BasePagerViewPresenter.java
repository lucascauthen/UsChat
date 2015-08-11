package com.lucascauthen.uschat.presentation.controller.base;

/**
 * Created by lhc on 7/30/15.
 */
public interface BasePagerViewPresenter extends BasePresenter<BasePagerViewPresenter.PagerView> {

    interface PagerView { //Interface for the presenter
        class Pages {
            public static final int CHAT = 0;
            public static final int CAMERA = 1;
            public static final int FRIENDS = 2;
        }
    }
    interface PagerViewChanger  {
        void changePage(int index);
    }
    interface PagerSubView {
        void attachPageChanger(PagerViewChanger changer);
    }
}
